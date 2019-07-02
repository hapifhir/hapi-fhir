package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Task;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

@SuppressWarnings({"unchecked", "deprecation"})
public class FhirResourceDaoCreatePlaceholdersR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCreatePlaceholdersR4Test.class);

	@After
	public final void afterResetDao() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(new DaoConfig().isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setResourceClientIdStrategy(new DaoConfig().getResourceClientIdStrategy());
	}

	@Test
	public void testCreateWithBadReferenceFails() {

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		try {
			myObservationDao.create(o, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Resource Patient/FOO not found, specified in path: Observation.subject", e.getMessage());
		}
	}

	@Test
	public void testCreateWithBadReferenceIsPermitted() {
		assertFalse(myDaoConfig.isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		myObservationDao.create(o, mySrd);
	}

	@Test
	public void testCreateWithMultiplePlaceholders() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Task task = new Task();
		task.addNote().setText("A note");
		task.addPartOf().setReference("Task/AAA");
		task.addPartOf().setReference("Task/AAA");
		task.addPartOf().setReference("Task/AAA");
		IIdType id = myTaskDao.create(task).getId().toUnqualifiedVersionless();

		task = myTaskDao.read(id);
		assertEquals(3, task.getPartOf().size());
		assertEquals("Task/AAA", task.getPartOf().get(0).getReference());
		assertEquals("Task/AAA", task.getPartOf().get(1).getReference());
		assertEquals("Task/AAA", task.getPartOf().get(2).getReference());

		SearchParameterMap params = new SearchParameterMap();
		params.add(Task.SP_PART_OF, new ReferenceParam("Task/AAA"));
		List<String> found = toUnqualifiedVersionlessIdValues(myTaskDao.search(params));
		assertThat(found, contains(id.getValue()));


	}

	@Test
	public void testUpdateWithBadReferenceFails() {

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o, mySrd).getId();

		o = new Observation();
		o.setId(id);
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		try {
			myObservationDao.update(o, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Resource Patient/FOO not found, specified in path: Observation.subject", e.getMessage());
		}
	}

	@Test
	public void testUpdateWithBadReferenceIsPermittedAlphanumeric() {
		assertFalse(myDaoConfig.isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o, mySrd).getId();

		try {
			myPatientDao.read(new IdType("Patient/FOO"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		o = new Observation();
		o.setId(id);
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/FOO");
		myObservationDao.update(o, mySrd);

		myPatientDao.read(new IdType("Patient/FOO"));

	}

	@Test
	public void testUpdateWithBadReferenceIsPermittedNumeric() {
		assertFalse(myDaoConfig.isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setResourceClientIdStrategy(DaoConfig.ClientIdStrategyEnum.ANY);

		Observation o = new Observation();
		o.setStatus(ObservationStatus.FINAL);
		IIdType id = myObservationDao.create(o, mySrd).getId();

		try {
			myPatientDao.read(new IdType("Patient/999999999999999"));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}

		o = new Observation();
		o.setId(id);
		o.setStatus(ObservationStatus.FINAL);
		o.getSubject().setReference("Patient/999999999999999");
		myObservationDao.update(o, mySrd);


		myPatientDao.read(new IdType("Patient/999999999999999"));

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("_id", new TokenParam("999999999999999"));
		IBundleProvider outcome = myPatientDao.search(map);
		assertEquals(1, outcome.size().intValue());
		assertEquals("Patient/999999999999999", outcome.getResources(0,1).get(0).getIdElement().toUnqualifiedVersionless().getValue());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
