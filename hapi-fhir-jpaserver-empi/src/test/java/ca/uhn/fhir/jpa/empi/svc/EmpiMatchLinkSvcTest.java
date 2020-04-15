package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.Constants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.util.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class EmpiMatchLinkSvcTest extends BaseEmpiR4Test {
	private static final Logger ourLog = getLogger(EmpiMatchLinkSvcTest.class);

	@Autowired
	IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private EIDHelper myEidHelper;
	@Autowired
	private PersonHelper myPersonHelper;

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
	public void testWhenMatchOccursOnPersonThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());
		IBundleProvider search = myPersonDao.search(new SearchParameterMap());
		IBaseResource janePerson = search.getResources(0,1).get(0);

		//Create a manual NO_MATCH between janePerson and unmatchedJane.
		Patient unmatchedJane= createPatient(buildJanePatient());
		myEmpiLinkSvc.updateLink(janePerson, unmatchedJane, EmpiMatchResultEnum.NO_MATCH, EmpiLinkSourceEnum.MANUAL);

		//rerun EMPI rules against unmatchedJane.
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(unmatchedJane);

		assertThat(unmatchedJane, is(not(samePersonAs(janePerson))));
		assertThat(unmatchedJane, is(not(linkedTo(originalJane))));
	}

	@Test
	public void testWhenPOSSIBLE_MATCHOccursOnPersonThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());
		IBundleProvider search = myPersonDao.search(new SearchParameterMap());
		IBaseResource janePerson = search.getResources(0, 1).get(0);

		Patient unmatchedPatient = createPatient(buildJanePatient());

		//This simulates an admin specifically saying that unmatchedPatient does NOT match janePerson.
		myEmpiLinkSvc.updateLink(janePerson, unmatchedPatient, EmpiMatchResultEnum.NO_MATCH, EmpiLinkSourceEnum.MANUAL);
		//TODO change this so that it will only partially match.

		//Now normally, when we run update links, it should link to janePerson. However, this manual NO_MATCH link
		//should cause a whole new Person to be created.
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(unmatchedPatient);

		assertThat(unmatchedPatient, is(not(samePersonAs(janePerson))));
		assertThat(unmatchedPatient, is(not(linkedTo(originalJane))));
	}

	@Test
	public void testWhenPatientIsCreatedWithEIDThatItPropagatesToNewPerson() {
		String sampleEID = "sample-eid";
		Patient janePatient = addExternalEID(buildJanePatient(), sampleEID);
		janePatient = createPatientAndUpdateLinks(janePatient);

		Optional<EmpiLink> empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(janePatient.getIdElement().getIdPartAsLong());
		assertThat(empiLink.isPresent(), is(true));

		Person person = getPersonFromEmpiLink(empiLink.get());
		Optional<CanonicalEID> externalEid = myEidHelper.getExternalEid(person);

		assertThat(externalEid.get().getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(externalEid.get().getValue(), is(equalTo(sampleEID)));
	}

	@Test
	public void testWhenPatientIsCreatedWithoutAnEIDThePersonGetsAutomaticallyAssignedOne() {
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		EmpiLink empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(patient.getIdElement().getIdPartAsLong()).get();

		Person person = getPersonFromEmpiLink(empiLink);
		Identifier identifierFirstRep = person.getIdentifierFirstRep();
		assertThat(identifierFirstRep.getSystem(), is(equalTo(Constants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(identifierFirstRep.getValue(), not(blankOrNullString()));
	}

	@Test
	public void testPatientAttributesAreCopiedOverWhenPersonIsCreatedFromPatient() {
		Patient patient = createPatientAndUpdateLinks(buildPatientWithNameIdAndBirthday("Gary", "GARY_ID", new Date()));

		Optional<EmpiLink> empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(patient.getIdElement().getIdPartAsLong());
		Person read = getPersonFromEmpiLink(empiLink.get());

		assertThat(read.getNameFirstRep().getFamily(), is(equalTo(patient.getNameFirstRep().getFamily())));
		assertThat(read.getNameFirstRep().getGivenAsSingleString(), is(equalTo(patient.getNameFirstRep().getGivenAsSingleString())));
		assertThat(read.getBirthDateElement().toHumanDisplay(), is(equalTo(patient.getBirthDateElement().toHumanDisplay())));
		assertThat(read.getTelecomFirstRep().getValue(), is(equalTo(patient.getTelecomFirstRep().getValue())));
		assertThat(read.getPhoto().getData(), is(equalTo(patient.getPhotoFirstRep().getData())));
		assertThat(read.getGender(), is(equalTo(patient.getGender())));
	}


	@Test
	public void testPatientMatchingAnotherPatientLinksToSamePerson() {
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient sameJanePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertThat(janePatient, is(samePersonAs(sameJanePatient)));
	}


	@Test
	public void testIncomingPatientWithEIDThatMatchesPersonWithHapiEidAddsExternalEidToPerson(){
		// Existing Person with system-assigned EID found linked from matched Patient.  incoming Patient has EID.  Replace Person system-assigned EID with Patient EID.
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());

		Person janePerson = getPersonFromResource(patient);
		Optional<CanonicalEID> hapiEid = myEidHelper.getHapiEid(janePerson);
		String foundHapiEid = hapiEid.get().getValue();

		Patient janePatient= addExternalEID(buildJanePatient(), "12345");
		createPatientAndUpdateLinks(janePatient);

		//We want to make sure the patients were linked to the same person.
		assertThat(patient, is(samePersonAs(janePatient)));

		Person person = getPersonFromResource(patient);

		List<Identifier> identifier = person.getIdentifier();

		//The collision should have kept the old identifier
		Identifier firstIdentifier = identifier.get(0);
		assertThat(firstIdentifier.getSystem(), is(equalTo(Constants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(firstIdentifier.getValue(), is(equalTo(foundHapiEid)));

		//The collision should have added a new identifier with the external system.
		Identifier secondIdentifier = identifier.get(1);
		assertThat(secondIdentifier.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(secondIdentifier.getValue(), is(equalTo("12345")));
	}

	@Test
	public void testIncomingPatientWithEidMatchesAnotherPatientWithSameEIDAreLinked() {
		Patient patient1 = addExternalEID(buildJanePatient(), "uniqueid");
		createPatientAndUpdateLinks(patient1);

		Patient patient2 = addExternalEID(buildPaulPatient(), "uniqueid");
		createPatientAndUpdateLinks(patient2);

		assertThat(patient1, is(samePersonAs(patient2)));
	}

	@Test
	public void testDuplicatePersonLinkIsCreatedWhenAnIncomingPatientArrivesWithEIDThatMatchesAnotherEIDPatient() {

		Patient patient1 = addExternalEID(buildJanePatient(), "eid-1");
		patient1 = createPatientAndUpdateLinks(patient1);

		Patient patient2 = addExternalEID(buildJanePatient(), "eid-2");
		patient2 = createPatientAndUpdateLinks(patient2);

		List<EmpiLink> possibleDuplicates = myEmpiLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(1));


		List<Long> duplicatePids = Stream.of(patient1, patient2)
			.map(this::getPersonFromResource)
			.map(myResourceTableHelper::getPidOrNull)
			.collect(Collectors.toList());

		//The two Persons related to the patients should both show up in the only existing POSSIBLE_DUPLICATE EmpiLink.
		EmpiLink empiLink = possibleDuplicates.get(0);
		assertThat(empiLink.getPersonPid(), is(in(duplicatePids)));
		assertThat(empiLink.getTargetPid(), is(in(duplicatePids)));
	}

	@Test
	public void testPatientWithNoEmpiTagIsNotMatched() {
		// Patient with "no-empi" tag is not matched
		Patient janePatient = buildJanePatient();
		janePatient.getMeta().addTag(Constants.SYSTEM_EMPI_MANAGED, Constants.CODE_NO_EMPI_MANAGED, "Don't EMPI on me!");
		createPatientAndUpdateLinks(janePatient);
		assertLinkCount(0);
	}

	@Test
	public void testPractitionersDoNotMatchToPatients() {
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Practitioner janePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());

		assertLinkCount(2);
		assertThat(janePatient, is(not(samePersonAs(janePractitioner))));
	}

	@Test
	public void testPractitionersThatMatchShouldLink() {
		Practitioner janePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());
		Practitioner anotherJanePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());

		assertLinkCount(2);
		assertThat(anotherJanePractitioner, is(samePersonAs(janePractitioner)));
	}

	@Test
	public void testCase1(){
		/**
		 * CASE 1: No MATCHED and no PROBABLE_MATCHED outcomes -> a new Person resource
		 * is created and linked to that Pat/Prac. All fields are copied from the Pat/Prac to the Person.
		 * If the incoming resource has an EID, it is copied to the Person. Otherwise a new unique uuid is created and used as the EID.
		 */
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);
		Person person = getPersonFromResource(janePatient);
		assertThat(janePatient, is(linkedTo(person)));
	}

	@Test
	public void testCase2(){
		/**
		 * CASE 2: All of the MATCHED Pat/Prac resources are already linked to the same Person ->
		 * a new Link is created between the new Pat/Prac and that Person and is set to MATCHED.
		 */
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient janePatient2 = createPatientAndUpdateLinks(buildJanePatient());

		assertLinkCount(2);
		assertThat(janePatient, is(samePersonAs(janePatient2)));

		Patient incomingJanePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertThat(incomingJanePatient, is(samePersonAs(janePatient)));
		assertThat(incomingJanePatient, is(samePersonAs(janePatient2)));
	}

	@Test
	public void testCase3(){
		/**
		 * CASE 3: The MATCHED Pat/Prac resources link to more than one Person -> Mark all links as PROBABLE_MATCHED.
		 * All other Person resources are marked as POSSIBLE_DUPLICATE of this first Person.
		 * These duplicates are manually reviewed later and either merged or marked as NO_MATCH
		 * and the system will no longer consider them as a POSSIBLE_DUPLICATE going forward.
		 */
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());

		Patient janePatient2 = createPatient(buildJanePatient());

		//In a normal situation, janePatient2 would just match to jane patient, but here we need to hack it so they are their
		//own individual Persons for the purpose of this test.
		IBaseResource person = myPersonHelper.createPersonFromEmpiTarget(janePatient2);
		person.getMeta().addTag().setSystem("ASDASDASD").setCode("ASDASDASD");
		myEmpiLinkSvc.updateLink(person, janePatient2, EmpiMatchResultEnum.MATCH, EmpiLinkSourceEnum.AUTO);
		assertThat(janePatient, is(not(samePersonAs(janePatient2))));

		//In theory, this will match both Persons!
		Patient incomingJanePatient = createPatientAndUpdateLinks(buildJanePatient());

	}
	@Test
	public void testCase4(){
		/**
		 * CASE 4: Only PROBABLE_MATCH outcomes -> In this case, empi-link records are created with PROBABLE_MATCH
		 * outcome and await manual assignment to either NO_MATCH or MATCHED. Person resources are not changed.
		 */
	}


}
