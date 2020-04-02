package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.rules.config.EmpiConfigImpl;
import ca.uhn.fhir.empi.rules.config.IEmpiConfig;
import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.api.IEmpiLinkSvc;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
	IEmpiConfig myEmpiConfig;
	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private EmpiResourceDaoSvc myEmpiResourceDaoSvc;

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
		Patient originalJane = createPatient(buildJanePatient());

		myEmpiLinkSvc.updateLink(person, originalJane, EmpiMatchResultEnum.NO_MATCH, EmpiLinkSourceEnum.MANUAL);
		//TODO change this so that it will only partially match.
		Patient similarJane = createPatientAndUpdateLinks(buildJanePatient());

		assertThat(similarJane, is(not(samePersonAs(person))));
		assertThat(similarJane, is(not(linkedTo(originalJane))));
	}



	@Test
	public void testWhenPatientIsCreatedWithEIDThatItPropagatesToNewPersons() {
		String sampleEID = "sample-eid";
		Patient janePatient = addEID(buildJanePatient(), sampleEID);
		janePatient = createPatientAndUpdateLinks(janePatient);

		EmpiLink empiLink = myEmpiLinkDaoSvc.getLinkByTargetResourceId(janePatient.getIdElement().getIdPartAsLong());
		Person person = myPersonDao.read(new IdDt(empiLink.getPersonPid()));
		Identifier identifier = person.getIdentifierFirstRep();
		assertThat(identifier.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(identifier.getValue(), is(equalTo(sampleEID)));
	}

	@Test
	public void testWhenPatientIsCreatedWithoutAnEIDThePersonGetsAutomaticallyAssignedOne() {
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		EmpiLink empiLink = myEmpiLinkDaoSvc.getLinkByTargetResourceId(patient.getIdElement().getIdPartAsLong());

		Person person = myPersonDao.read(new IdDt(empiLink.getPersonPid()));
		Identifier identifierFirstRep = person.getIdentifierFirstRep();
		assertThat(identifierFirstRep.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(identifierFirstRep.getValue(), is(notNullValue()));
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

		Patient janePatient= addEID(buildJanePatient(), "12345");
		createPatientAndUpdateLinks(janePatient);


		//We want to make sure the patients were linked to the same person.
		assertThat(patient, is(samePersonAs(janePatient)));

		EmpiLink empiLink = myEmpiLinkDaoSvc.getLinkByTargetResourceId(patient.getIdElement().getIdPartAsLong());
		Person person = myPersonDao.read(new IdDt(empiLink.getPersonPid()));
		Identifier identifier = person.getIdentifierFirstRep();

		assertThat(identifier.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(identifier.getValue(), is(equalTo("12345")));
	}

	public Patient addEID(Patient thePatient, String theEID) {
		thePatient.addIdentifier().setSystem(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem()).setValue(theEID);
		return thePatient;
	}

	@Test
	public void testIncomingPatientWithEIDMatchesAnotherPatientWithSameEIDAreLinked() {
		Patient patient1 = addEID(buildJanePatient(), "12345");
		patient1 = createPatientAndUpdateLinks(patient1);

		Patient patient2 = addEID(buildPaulPatient(), "12345");
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

	// FIXME EMPI Test: Patient with "no-empi" tag is not matched

	private Patient createPatientAndUpdateLinks(Patient thePatient) {
		//Note that since our empi-rules block on active=true, all patients must be active.
		thePatient.setActive(true);
		DaoMethodOutcome daoMethodOutcome = myPatientDao.create(thePatient);
		thePatient.setId(daoMethodOutcome.getId());
		myEmpiMatchLinkSvc.updateEmpiLinksForPatient(thePatient);
		return thePatient;
	}
}
