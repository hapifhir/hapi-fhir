package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.interceptor.UserRequestRetryVersionConflictsInterceptor;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "deprecation", "Duplicates"})
public class FhirResourceDaoR4ConcurrentWriteTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ConcurrentWriteTest.class);
	private ExecutorService myExecutor;
	private UserRequestRetryVersionConflictsInterceptor myRetryInterceptor;


	@Before
	public void before() {
		myExecutor = Executors.newFixedThreadPool(10);
		myRetryInterceptor = new UserRequestRetryVersionConflictsInterceptor();
	}

	@After
	public void after() {
		myExecutor.shutdown();
		myInterceptorRegistry.unregisterInterceptor(myRetryInterceptor);
	}

	@Test
	public void testCreateWithClientAssignedId() {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);
		String value = UserRequestRetryVersionConflictsInterceptor.RETRY + "; " + UserRequestRetryVersionConflictsInterceptor.MAX_RETRIES + "=10";
		when(mySrd.getHeaders(eq(UserRequestRetryVersionConflictsInterceptor.HEADER_NAME))).thenReturn(Collections.singletonList(value));

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> myPatientDao.update(p, mySrd);
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
	public void testCreateWithClientAssignedId_InTransaction() {

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {

			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);

			Bundle bundle = new Bundle();
			bundle.setType(Bundle.BundleType.TRANSACTION);
			bundle
				.addEntry()
				.setResource(p)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("Patient/ABC");
			Runnable task = () -> mySystemDao.transaction(mySrd, bundle);

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
			p.setGender(Enumerations.AdministrativeGender.MALE);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> {
				try {
					myPatientDao.create(p);
				} catch (PreconditionFailedException e) {
					// expected - This is as a result of the unique SP
					assertThat(e.getMessage(), containsString("it would create a duplicate index matching query"));
				}
			};
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

		runInTransaction(() -> {
			ourLog.info("Uniques:\n * " + myResourceIndexedCompositeStringUniqueDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		// Make sure we saved the object
		myCaptureQueriesListener.clear();
		IBundleProvider search = myPatientDao.search(SearchParameterMap.newSynchronous("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male")));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(1, search.sizeOrThrowNpe());

	}

}
