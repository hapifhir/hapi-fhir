package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderExpungeDstu2Test extends BaseResourceProviderDstu2Test {
	private IIdType myOneVersionPatientId;
	private IIdType myTwoVersionPatientId;
	private IIdType myDeletedPatientId;
	private IIdType myOneVersionObservationId;
	private IIdType myTwoVersionObservationId;
	private IIdType myDeletedObservationId;

	@AfterEach
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
	@BeforeEach
	public void before() throws Exception {
		super.before();

		Patient p = new Patient();
		p.setId("PT-ONEVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addIdentifier().setSystem("foo").setValue("bar");
		p.addName().addFamily("FAM");
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
		o.setStatus(ObservationStatusEnum.FINAL);
		myOneVersionObservationId = myObservationDao.create(o).getId();

		o.setStatus(ObservationStatusEnum.FINAL);
		o = new Observation();
		myTwoVersionObservationId = myObservationDao.create(o).getId();
		o.setStatus(ObservationStatusEnum.AMENDED);
		myTwoVersionObservationId = myObservationDao.update(o).getId();

		o = new Observation();
		o.setStatus(ObservationStatusEnum.FINAL);
		myDeletedObservationId = myObservationDao.create(o).getId();
		myDeletedObservationId = myObservationDao.delete(myDeletedObservationId).getId();

	}

	@BeforeEach
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
		Patient p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addName().addFamily("FOO");
		myPatientDao.update(p).getId();

		myPatientDao.expunge(myTwoVersionPatientId.toUnqualifiedVersionless(), new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertStillThere(myTwoVersionPatientId.withVersion("3"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeInstanceVersionCurrentVersion() {

		try {
			myPatientDao.expunge(myTwoVersionPatientId.withVersion("2"), new ExpungeOptions()
				.setExpungeDeletedResources(true)
				.setExpungeOldVersions(true), null);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(969) + "Can not perform version-specific expunge of resource Patient/PT-TWOVERSION/_history/2 as this is the current version", e.getMessage());
		}
	}

	@Test
	public void testExpungeInstanceVersionOldVersionsAndDeleted() {
		Patient p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addName().addFamily("FOO");
		myPatientDao.update(p).getId();

		myPatientDao.expunge(myTwoVersionPatientId.withVersion("2"), new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Patients
		assertStillThere(myOneVersionPatientId);
		assertStillThere(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertStillThere(myTwoVersionPatientId.withVersion("3"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeSystemEverything() {
		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeEverything(true), null);

		// Everything deleted
		assertExpunged(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertExpunged(myDeletedPatientId);

		// Everything deleted
		assertExpunged(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertExpunged(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeSystemOldVersionsAndDeleted() {
		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// Also observations deleted
		assertStillThere(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeTypeDeletedResources() {
		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(false), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertStillThere(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeTypeOldVersions() {
		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(false)
			.setExpungeOldVersions(true), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeTypeOldVersionsAndDeleted() {
		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

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


}
