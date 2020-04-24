package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ResourceProviderExpungeR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderExpungeR4Test.class);
	private IIdType myOneVersionPatientId;
	private IIdType myTwoVersionPatientId;
	private IIdType myDeletedPatientId;
	private IIdType myOneVersionObservationId;
	private IIdType myTwoVersionObservationId;
	private IIdType myDeletedObservationId;

	@After
	public void afterDisableExpunge() {
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());
	}

	private void assertExpunged(IIdType theId) {
		try {
			getDao(theId).read(theId);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	private void assertGone(IIdType theId) {
		try {
			getDao(theId).read(theId);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	private void assertStillThere(IIdType theId) {
		getDao(theId).read(theId);
	}

	@Override
	@Before
	public void before() throws Exception {
		super.before();

		Patient p = new Patient();
		p.setId("PT-ONEVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addIdentifier().setSystem("foo").setValue("bar");
		p.addName().setFamily("FAM");
		myOneVersionPatientId = myPatientDao.update(p).getId();

		p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		myTwoVersionPatientId = myPatientDao.update(p).getId();
		p.setActive(false);
		myTwoVersionPatientId = myPatientDao.update(p).getId();

		p = new Patient();
		p.setId("PT-DELETED");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		myDeletedPatientId = myPatientDao.update(p).getId();
		myDeletedPatientId = myPatientDao.delete(myDeletedPatientId).getId();

		assertStillThere(myDeletedPatientId.withVersion("1"));
		assertGone(myDeletedPatientId.withVersion("2"));

		// Observation

		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myOneVersionObservationId = myObservationDao.create(o).getId();

		o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myTwoVersionObservationId = myObservationDao.create(o).getId();
		o.setStatus(Observation.ObservationStatus.AMENDED);
		myTwoVersionObservationId = myObservationDao.update(o).getId();

		o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myDeletedObservationId = myObservationDao.create(o).getId();
		myDeletedObservationId = myObservationDao.delete(myDeletedObservationId).getId();

	}

	@Before
	public void beforeEnableExpunge() {
		myDaoConfig.setExpungeEnabled(true);
	}

	private IFhirResourceDao<?> getDao(IIdType theId) {
		IFhirResourceDao<?> dao;
		switch (theId.getResourceType()) {
			case "Patient":
				dao = myPatientDao;
				break;
			case "Observation":
				dao = myObservationDao;
				break;
			default:
				fail("Restype: " + theId.getResourceType());
				dao = myPatientDao;
		}
		return dao;
	}

	@Test
	public void testExpungeInstanceOldVersionsAndDeleted() {
		Parameters input = new Parameters();
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT)
			.setValue(new IntegerType(1000));
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES)
			.setValue(new BooleanType(true));
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS)
			.setValue(new BooleanType(true));

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Parameters output = ourClient
			.operation()
			.onInstance(myTwoVersionPatientId)
			.named("expunge")
			.withParameters(input)
			.execute();

		assertEquals("count", output.getParameter().get(0).getName());
		assertEquals(1, ((IntegerType) output.getParameter().get(0).getValue()).getValue().intValue());

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);

	}

	@Test
	public void testExpungeDisabled() {
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());

		Parameters input = new Parameters();
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT)
			.setValue(new IntegerType(1000));
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES)
			.setValue(new BooleanType(true));
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS)
			.setValue(new BooleanType(true));

		try {
			ourClient
				.operation()
				.onInstance(myTwoVersionPatientId)
				.named("expunge")
				.withParameters(input)
				.execute();
			fail();
		} catch (MethodNotAllowedException e){
			assertEquals("HTTP 405 Method Not Allowed: $expunge is not enabled on this server", e.getMessage());
		}
		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertStillThere(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);

	}

	@Test
	public void testExpungeSystemEverything() {
		Parameters input = new Parameters();
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_EVERYTHING)
			.setValue(new BooleanType(true));

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Parameters output = ourClient
			.operation()
			.onServer()
			.named("expunge")
			.withParameters(input)
			.execute();

//		assertEquals("count", output.getParameter().get(0).getName());
//		assertEquals(3, ((IntegerType) output.getParameter().get(0).getValue()).getValue().intValue());

		// All patients deleted
		assertExpunged(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// All observations deleted
		assertExpunged(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertExpunged(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);

	}

	@Test
	public void testExpungeTypeOldVersionsAndDeleted() {
		Parameters input = new Parameters();
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT)
			.setValue(new IntegerType(1000));
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES)
			.setValue(new BooleanType(true));
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS)
			.setValue(new BooleanType(true));

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Parameters output = ourClient
			.operation()
			.onType(Patient.class)
			.named("expunge")
			.withParameters(input)
			.execute();

		assertEquals("count", output.getParameter().get(0).getName());
		assertEquals(3, ((IntegerType) output.getParameter().get(0).getValue()).getValue().intValue());

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);

	}

	@Test
	public void testExpungeVersion() {
		Patient p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p).getId();

		Parameters input = new Parameters();
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_LIMIT)
			.setValue(new IntegerType(1000));
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_DELETED_RESOURCES)
			.setValue(new BooleanType(true));
		input.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_PARAM_EXPUNGE_PREVIOUS_VERSIONS)
			.setValue(new BooleanType(true));

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Parameters output = ourClient
			.operation()
			.onInstanceVersion(myTwoVersionPatientId.withVersion("1"))
			.named("expunge")
			.withParameters(input)
			.execute();

		assertEquals("count", output.getParameter().get(0).getName());
		assertEquals(1, ((IntegerType) output.getParameter().get(0).getValue()).getValue().intValue());

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
