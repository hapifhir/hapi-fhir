package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"unchecked", "deprecation", "Duplicates"})
public class FhirResourceDaoR4ConcurrentWriteTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ConcurrentWriteTest.class);
	private ExecutorService myExecutor;

	@Before
	public void before() {
		myExecutor = Executors.newFixedThreadPool(10);
	}

	@After
	public void after() {
		myExecutor.shutdown();
	}

	@Test
	public void testCreateWithClientAssignedId() {

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> myPatientDao.update(p);
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (Exception e) {
				ourLog.info("Future produced exception: {}", e.toString());
				throw new AssertionError("Failed with message: " + e.toString(), e);
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}

	@Test
	public void testCreateWithUniqueConstraint() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender-unique");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-gender");
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId("ABC");
			p.setGender(Enumerations.AdministrativeGender.MALE);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> myPatientDao.update(p);
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (Exception e) {
				ourLog.info("Future produced exception: {}", e.toString());
				throw new AssertionError("Failed with message: " + e.toString(), e);
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}

}
