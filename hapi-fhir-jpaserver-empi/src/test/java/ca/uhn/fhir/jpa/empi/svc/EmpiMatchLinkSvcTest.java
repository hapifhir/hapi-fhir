package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class EmpiMatchLinkSvcTest extends BaseEmpiR4Test {
	private static final Logger ourLog = getLogger(EmpiMatchLinkSvcTest.class);

	@Autowired
	private EmpiMatchLinkSvc myEmpiMatchLinkSvc;
	@Autowired
	private ResourceTableHelper myResourceTableHelper;
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	@Autowired
	EmpiLinkSvcImpl myEmpiLinkSvc;

	@Test
	public void testAddPatientLinksToNewPersonIfNoneFound() {
		createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);
	}

	@Test
	public void testAddPatientLinksToNewPersonIfNoMatch() {
		Patient patient1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patient2 = createPatientAndUpdateLinks(buildPaulPatient());

		assertLinkCount(2);
		assertThat(patient1, is(not(samePersonAs(patient2))));
	}

	@Test
	public void testAddPatientLinksToExistingPersonIfMatch() {
		Patient patient1 = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);

		Patient patient2 = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(2);

		assertThat(patient1, is(samePersonAs(patient2)));
	}

	@Test
	public void testPatientLinksToPersonIfMatch() {
		//FIXME EMPI QUESTION: We agreed we were not going to match to Person Attributes right? lmk.
		Person janePerson = buildJanePerson();
		DaoMethodOutcome outcome = myPersonDao.create(janePerson);
		Long origPersonPid = myResourceTableHelper.getPidOrNull(outcome.getResource());

		createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);
		List<EmpiLink> links = myEmpiLinkDao.findAll();
		EmpiLink link = links.get(0);
		Long linkedPersonPid = link.getPersonPid();
		assertEquals(EmpiMatchResultEnum.MATCH, link.getMatchResult());

		assertEquals(origPersonPid, linkedPersonPid);
	}

	@Test
	public void testWhenMatchOccursOnPersonThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Person person= createPerson(buildJanePerson());
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());

		myEmpiLinkSvc.updateLink(person, originalJane, EmpiMatchResultEnum.NO_MATCH, EmpiLinkSourceEnum.MANUAL);

		Patient similarJane = createPatientAndUpdateLinks(buildJanePatient());

		assertThat(similarJane, is(not(samePersonAs(person))));
		assertThat(similarJane, is(not(linkedTo(originalJane))));
	}

	@Test
	public void testWhenPOSSIBLE_MATCHOccursOnPersonThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Person person= createPerson(buildJanePerson());
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());

		myEmpiLinkSvc.updateLink(person, originalJane, EmpiMatchResultEnum.NO_MATCH, EmpiLinkSourceEnum.MANUAL);
		//TODO change this so that it will only partially match.
		Patient similarJane = createPatientAndUpdateLinks(buildJanePatient());

		assertThat(similarJane, is(not(samePersonAs(person))));
		assertThat(similarJane, is(not(linkedTo(originalJane))));
	}

	@Test
	public void testAutomaticallyAddedNO_MATCHEmpiLinksAreNotAllowed() {
		Person person = createPerson(buildJanePerson());
		Patient patient = createPatient(buildJanePatient());

		// Test: it should be impossible to have a AUTO NO_MATCH record.  The only NO_MATCH records in the system must be MANUAL.
		try {
			myEmpiLinkSvc.updateLink(person, patient, EmpiMatchResultEnum.NO_MATCH, EmpiLinkSourceEnum.AUTO);
			fail();
		} catch (IllegalArgumentException e) {}
	}

	@Test
	public void testWhenPatientIsCreatedWithEIDThatItPropagatesToNewPersons() {
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		//FIXME EMPI fix the above to have an EID.
		EmpiLink empiLink = myEmpiLinkDaoSvc.getLinkByTargetResourceId(patient.getIdElement().getIdPartAsLong());
		Person read = myPersonDao.read(new IdDt(empiLink.getPersonPid()));
		assertThat(myEmpiMatchLinkSvc.getEID(patient), is(equalTo(myEmpiMatchLinkSvc.getEID(read)));
	}

	@Test
	public void testWhenPatientIsCreatedWithoutAnEIDThePersonGetsAutomaticallyAssignedOne() {
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		EmpiLink empiLink = myEmpiLinkDaoSvc.getLinkByTargetResourceId(patient.getIdElement().getIdPartAsLong());
		Person read = myPersonDao.read(new IdDt(empiLink.getPersonPid()));

		assertThat(myEmpiMatchLinkSvc.getEID(read), is(notNullValue()));
	}

	@Test
	public void testPatientAttributesAreCarriedOverWhenPersonIsCreatedFromPatient() {
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		EmpiLink empiLink = myEmpiLinkDaoSvc.getLinkByTargetResourceId(patient.getIdElement().getIdPartAsLong());
		Person read = myPersonDao.read(new IdDt(empiLink.getPersonPid()));

		assertThat(read.getName(), is(equalTo(patient.getName())));
		assertThat(read.getAddress(), is(equalTo(patient.getAddress())));
		assertThat(read.getIdentifier(), is(equalTo(patient.getIdentifier())));
	}


	//FIXME EMPI QUESTION I have no clue what this test means. Create link all done??
	// Test: Existing Person found linked from matched Patient.  incoming Patient has no EID.  Create link all done.


	@Test
	public void testIncomingPatientWithEIDThatMatchesPersonWithDifferentEIDCausesOverwriteOnPerson(){
		//FIXME EMPI
		// Test: Existing Person with system-assigned EID found linked from matched Patient.  incoming Patient has EID.  Replace Person system-assigned EID with Patient EID.
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());

		Patient janePatient= buildJanePatient();
		janePatient.addIdentifier().setSystem("FIXME_EID_SYSTEM").setValue("12345");
		createPatientAndUpdateLinks(janePatient); //FIXME during this call, add the EID to jane patient before creation.


		EmpiLink empiLink = myEmpiLinkDaoSvc.getLinkByTargetResourceId(patient.getIdElement().getIdPartAsLong());
		Person person = myPersonDao.read(new IdDt(empiLink.getPersonPid()));
		assertThat(myEmpiMatchLinkSvc.getEID(person), is(equalTo("12345")));
	}

	@Test
	public void testIncomingPatientWithEIDMatchesAnotherPatientWithSameEIDAreLinked() {
		Patient patient1 = buildJanePatient();
		patient1.addIdentifier().setSystem("FIXME_EID_SYSTEM").setValue("12345");
		patient1 = createPatientAndUpdateLinks(patient1);
		Patient patient2 = buildPaulPatient();
		patient2.addIdentifier().setSystem("FIXME_EID_SYSTEM").setValue("12345");
		patient2 = createPatientAndUpdateLinks(patient2);

		assertThat(patient1, is(samePersonAs(patient2)));
	}


	@Test
	public void testDuplicatePersonLinkIsCreatedWhenAnIncomingPatientArrivesWithEIDThatMatchesAnotherEIDPatient() {
		//FIXME EMPI
		// Test: Existing Person with legit EID (from a Patient) found linked from matched Patient.  incoming Patient has different EID.   Create new Person with incoming EID and link.
		// Record somehow (design TBD) that these two Persons may be duplicates.  -- Maybe we have a special kind of EmpiLink table entry where the target also points to a Person and it's
		// flagged with a special PROBABLE_DUPLICATE match status?
	}


	@Test
	public void testEmpiManagedPersonCannotBeModifiedByPersonUpdateRequest() {
		//FIXME EMPI
		// Test: Existing Person with Meta TAg indicating they are Empi-Managed. requestors cannot remove this tag.
	}

	@Test
	public void testNonEmpiManagedPersonCannotHaveEmpiManagedTagAddedToThem() {
		//FIXME EMPI
		// Test: Existing Person without Meta Tag indicating they are EMPI-Managed. Requestors cannot add the tag.
	}

	private Patient createPatientAndUpdateLinks(Patient thePatient) {
		//Note that since our empi-rules block on active=true, all patients must be active.
		thePatient.setActive(true);
		DaoMethodOutcome daoMethodOutcome = myPatientDao.create(thePatient);
		thePatient.setId(daoMethodOutcome.getId());
		myEmpiMatchLinkSvc.updateEmpiLinksForPatient(thePatient);
		return thePatient;
	}
}
