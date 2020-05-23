package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@SuppressWarnings({"ConstantConditions"})
public class FhirResourceDaoCreatePlaceholdersR4Test extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoCreatePlaceholdersR4Test.class);

	@After
	public final void afterResetDao() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(new DaoConfig().isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setResourceClientIdStrategy(new DaoConfig().getResourceClientIdStrategy());
		myDaoConfig.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(new DaoConfig().isPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets());
		myDaoConfig.setBundleTypesAllowedForStorage(new DaoConfig().getBundleTypesAllowedForStorage());

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
			assertThat(e.getMessage(), startsWith("Resource Patient/FOO not found, specified in path: Observation.subject"));
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
			assertThat(e.getMessage(), startsWith("Resource Patient/FOO not found, specified in path: Observation.subject"));
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

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierNotCopiedByDefault() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);

		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		Observation createdObs = myObservationDao.read(id);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		Patient patient = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(0, patient.getIdentifier().size());
	}


	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierCopied_NotPreExisting() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);
		myDaoConfig.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		Observation createdObs = myObservationDao.read(id);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));

		Patient patient = myPatientDao.read(new IdType(createdObs.getSubject().getReference()));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(1, patient.getIdentifier().size());
		assertEquals("http://foo", patient.getIdentifier().get(0).getSystem());
		assertEquals("123", patient.getIdentifier().get(0).getValue());
	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_IdentifierNotCopiedBecauseNoFieldMatches() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);
		myDaoConfig.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);
		myDaoConfig.setBundleTypesAllowedForStorage(Sets.newHashSet(""));

		AuditEvent eventToCreate = new AuditEvent();
		Reference what = eventToCreate.addEntity().getWhat();
		what.setReference("Bundle/ABC");
		what.getIdentifier().setSystem("http://foo");
		what.getIdentifier().setValue("123");
		IIdType id = myAuditEventDao.create(eventToCreate, mySrd).getId();

		AuditEvent createdEvent = myAuditEventDao.read(id);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEvent));

	}

	@Test
	public void testCreatePlaceholderWithMatchUrl_PreExisting() {
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(true);
		myDaoConfig.setAllowInlineMatchUrlReferences(true);
		myDaoConfig.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);

		Patient patient = new Patient();
		patient.setId("ABC");
		patient.addIdentifier().setSystem("http://foo").setValue("123");
		myPatientDao.update(patient);

		Observation obsToCreate = new Observation();
		obsToCreate.setStatus(ObservationStatus.FINAL);
		obsToCreate.getSubject().setReference("Patient?identifier=http://foo|123");
		obsToCreate.getSubject().getIdentifier().setSystem("http://foo").setValue("123");
		IIdType id = myObservationDao.create(obsToCreate, mySrd).getId();

		Observation createdObs = myObservationDao.read(id);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		assertEquals("Patient/ABC", obsToCreate.getSubject().getReference());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
