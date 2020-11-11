package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.MATCH;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.NO_MATCH;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.POSSIBLE_DUPLICATE;
import static ca.uhn.fhir.empi.api.EmpiMatchResultEnum.POSSIBLE_MATCH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

public class EmpiMatchLinkSvcTest extends BaseEmpiR4Test {

	private static final Logger ourLog = getLogger(EmpiMatchLinkSvcTest.class);

	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;
	@Autowired
	private EIDHelper myEidHelper;
	@Autowired
	private PersonHelper myPersonHelper;
	@Autowired
	private IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@BeforeEach
	public void before() {
		super.loadEmpiSearchParameters();
	}

	@Test
	public void testAddPatientLinksToNewPersonIfNoneFound() {
		createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);
	}

	@Test
	public void testAddMedicationLinksToNewGoldenRecordMedicationIfNoneFound() {
		Organization org = new Organization();
		org.setId("Organization/mfr");
		myOrganizationDao.update(org);

		createMedicationAndUpdateLinks(buildMedication("Organization/mfr"));
		assertLinkCount(1);
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);
	}

	@Test
	public void testAddPatientLinksToNewlyCreatedResourceIfNoMatch() {
		Patient patient1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patient2 = createPatientAndUpdateLinks(buildPaulPatient());

		assertLinkCount(2);

		assertThat(patient1, is(not(sameSourceResourceAs(patient2))));

		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, true);
		assertLinksMatchedByEid(false, false);
	}

	@Test
	public void testAddPatientLinksToExistingPersonIfMatch() {
		Patient patient1 = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);

		Patient patient2 = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(2);

		assertThat(patient1, is(sameSourceResourceAs(patient2)));
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, false);
		assertLinksMatchedByEid(false, false);
	}

	@Test
	public void testWhenMatchOccursOnPersonThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());
		IAnyResource janePerson = getSourceResourceFromTargetResource(originalJane);

		//Create a manual NO_MATCH between janePerson and unmatchedJane.
		Patient unmatchedJane = createPatient(buildJanePatient());
		myEmpiLinkSvc.updateLink(janePerson, unmatchedJane, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate("Patient"));

		//rerun EMPI rules against unmatchedJane.
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(unmatchedJane, createContextForCreate("Patient"));

		assertThat(unmatchedJane, is(not(sameSourceResourceAs(janePerson))));
		assertThat(unmatchedJane, is(not(linkedTo(originalJane))));

		assertLinksMatchResult(MATCH, NO_MATCH, MATCH);
		assertLinksCreatedNewResource(true, false, true);
		assertLinksMatchedByEid(false, false, false);
	}

	@Test
	public void testWhenPOSSIBLE_MATCHOccursOnPersonThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());

		IBundleProvider search = myPatientDao.search(buildGoldenRecordSearchParameterMap());
		IAnyResource janePerson = (IAnyResource) search.getResources(0, 1).get(0);

		Patient unmatchedPatient = createPatient(buildJanePatient());

		// This simulates an admin specifically saying that unmatchedPatient does NOT match janePerson.
		myEmpiLinkSvc.updateLink(janePerson, unmatchedPatient, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		// TODO change this so that it will only partially match.

		//Now normally, when we run update links, it should link to janePerson. However, this manual NO_MATCH link
		//should cause a whole new Person to be created.
		myEmpiMatchLinkSvc.updateEmpiLinksForEmpiTarget(unmatchedPatient, createContextForCreate("Patient"));

		assertThat(unmatchedPatient, is(not(sameSourceResourceAs(janePerson))));
		assertThat(unmatchedPatient, is(not(linkedTo(originalJane))));

		assertLinksMatchResult(MATCH, NO_MATCH, MATCH);
		assertLinksCreatedNewResource(true, false, true);
		assertLinksMatchedByEid(false, false, false);
	}

	@Test
	public void testWhenPatientIsCreatedWithEIDThatItPropagatesToNewPerson() {
		String sampleEID = "sample-eid";
		Patient janePatient = addExternalEID(buildJanePatient(), sampleEID);
		janePatient = createPatientAndUpdateLinks(janePatient);

		Optional<EmpiLink> empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(janePatient.getIdElement().getIdPartAsLong());
		assertThat(empiLink.isPresent(), is(true));

		Patient patient = getTargetResourceFromEmpiLink(empiLink.get(), "Patient");
		List<CanonicalEID> externalEid = myEidHelper.getExternalEid(patient);

		assertThat(externalEid.get(0).getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(externalEid.get(0).getValue(), is(equalTo(sampleEID)));
	}

	@Test
	public void testWhenPatientIsCreatedWithoutAnEIDThePersonGetsAutomaticallyAssignedOne() {
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		EmpiLink empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(patient.getIdElement().getIdPartAsLong()).get();

		Patient targetPatient = getTargetResourceFromEmpiLink(empiLink, "Patient");
		Identifier identifierFirstRep = targetPatient.getIdentifierFirstRep();
		assertThat(identifierFirstRep.getSystem(), is(equalTo(EmpiConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(identifierFirstRep.getValue(), not(blankOrNullString()));
	}

	@Test
	public void testPatientAttributesAreCopiedOverWhenPersonIsCreatedFromPatient() {
		Patient patient = createPatientAndUpdateLinks(buildPatientWithNameIdAndBirthday("Gary", "GARY_ID", new Date()));

		Optional<EmpiLink> empiLink = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(patient.getIdElement().getIdPartAsLong());
		Patient read = getTargetResourceFromEmpiLink(empiLink.get(), "Patient");

		// TODO NG - rules haven't been determined yet revisit once implemented...
//		assertThat(read.getNameFirstRep().getFamily(), is(equalTo(patient.getNameFirstRep().getFamily())));
//		assertThat(read.getNameFirstRep().getGivenAsSingleString(), is(equalTo(patient.getNameFirstRep().getGivenAsSingleString())));
//		assertThat(read.getBirthDateElement().toHumanDisplay(), is(equalTo(patient.getBirthDateElement().toHumanDisplay())));
//		assertThat(read.getTelecomFirstRep().getValue(), is(equalTo(patient.getTelecomFirstRep().getValue())));
//		assertThat(read.getPhoto().getData(), is(equalTo(patient.getPhotoFirstRep().getData())));
//		assertThat(read.getGender(), is(equalTo(patient.getGender())));
	}

	@Test
	public void testPatientMatchingAnotherPatientLinksToSamePerson() {
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient sameJanePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertThat(janePatient, is(sameSourceResourceAs(sameJanePatient)));
	}

	@Test
	public void testIncomingPatientWithEIDThatMatchesPersonWithHapiEidAddsExternalEidToPerson() {
		// Existing Person with system-assigned EID found linked from matched Patient.  incoming Patient has EID.  Replace Person system-assigned EID with Patient EID.
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());

		IAnyResource janePerson = getSourceResourceFromTargetResource(patient);
		List<CanonicalEID> hapiEid = myEidHelper.getHapiEid(janePerson);
		String foundHapiEid = hapiEid.get(0).getValue();

		Patient janePatient = addExternalEID(buildJanePatient(), "12345");
		createPatientAndUpdateLinks(janePatient);

		//We want to make sure the patients were linked to the same person.
		assertThat(patient, is(sameSourceResourceAs(janePatient)));

		Patient sourcePatient = (Patient) getSourceResourceFromTargetResource(patient);

		List<Identifier> identifier = sourcePatient.getIdentifier();

		//The collision should have kept the old identifier
		Identifier firstIdentifier = identifier.get(0);
		assertThat(firstIdentifier.getSystem(), is(equalTo(EmpiConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(firstIdentifier.getValue(), is(equalTo(foundHapiEid)));

		//The collision should have added a new identifier with the external system.
		Identifier secondIdentifier = identifier.get(1);
		assertThat(secondIdentifier.getSystem(), is(equalTo(myEmpiConfig.getEmpiRules().getEnterpriseEIDSystem())));
		assertThat(secondIdentifier.getValue(), is(equalTo("12345")));
	}

	@Test
	public void testIncomingPatientWithEidMatchesAnotherPatientWithSameEIDAreLinked() {
		// Create Use Case #3
		Patient patient1 = addExternalEID(buildJanePatient(), "uniqueid");
		createPatientAndUpdateLinks(patient1);

		Patient patient2 = buildPaulPatient();
		patient2 = addExternalEID(patient2, "uniqueid");
		createPatientAndUpdateLinks(patient2);

		assertThat(patient1, is(sameSourceResourceAs(patient2)));
	}

	@Test
	public void testHavingMultipleEIDsOnIncomingPatientMatchesCorrectly() {

		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "id_1");
		addExternalEID(patient1, "id_2");
		addExternalEID(patient1, "id_3");
		addExternalEID(patient1, "id_4");
		createPatientAndUpdateLinks(patient1);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "id_5");
		addExternalEID(patient2, "id_1");
		createPatientAndUpdateLinks(patient2);

		assertThat(patient1, is(sameSourceResourceAs(patient2)));
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
			.map(this::getSourceResourceFromTargetResource)
			.map(myIdHelperService::getPidOrNull)
			.collect(Collectors.toList());

		//The two Persons related to the patients should both show up in the only existing POSSIBLE_DUPLICATE EmpiLink.
		EmpiLink empiLink = possibleDuplicates.get(0);
		assertThat(empiLink.getSourceResourcePid(), is(in(duplicatePids)));
		assertThat(empiLink.getTargetPid(), is(in(duplicatePids)));
	}

	@Test
	public void testPatientWithNoEmpiTagIsNotMatched() {
		// Patient with "no-empi" tag is not matched
		Patient janePatient = buildJanePatient();
		janePatient.getMeta().addTag(EmpiConstants.SYSTEM_EMPI_MANAGED, EmpiConstants.CODE_NO_EMPI_MANAGED, "Don't EMPI on me!");
		createPatientAndUpdateLinks(janePatient);
		assertLinkCount(0);
	}

	@Test
	public void testPractitionersDoNotMatchToPatients() {
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Practitioner janePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());

		assertLinkCount(2);
		assertThat(janePatient, is(not(sameSourceResourceAs(janePractitioner))));
	}

	@Test
	public void testPractitionersThatMatchShouldLink() {
		Practitioner janePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());
		Practitioner anotherJanePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());

		assertLinkCount(2);
		assertThat(anotherJanePractitioner, is(sameSourceResourceAs(janePractitioner)));
	}

	@Test
	public void testWhenThereAreNoMATCHOrPOSSIBLE_MATCHOutcomesThatANewPersonIsCreated() {
		/**
		 * CASE 1: No MATCHED and no PROBABLE_MATCHED outcomes -> a new Person resource
		 * is created and linked to that Pat/Prac.
		 */
		assertLinkCount(0);
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);
		assertThat(janePatient, is(matchedToAPerson()));
	}

	@Test
	public void testWhenAllMATCHResultsAreToSamePersonThatTheyAreLinked() {
		/**
		 * CASE 2: All of the MATCHED Pat/Prac resources are already linked to the same Person ->
		 * a new Link is created between the new Pat/Prac and that Person and is set to MATCHED.
		 */
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient janePatient2 = createPatientAndUpdateLinks(buildJanePatient());

		assertLinkCount(2);
		assertThat(janePatient, is(sameSourceResourceAs(janePatient2)));

		Patient incomingJanePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertThat(incomingJanePatient, is(sameSourceResourceAs(janePatient, janePatient2)));
		assertThat(incomingJanePatient, is(linkedTo(janePatient, janePatient2)));
	}

	@Test
	public void testMATCHResultWithMultipleCandidatesCreatesPOSSIBLE_DUPLICATELinksAndNoPersonIsCreated() {
		/**
		 * CASE 3: The MATCHED Pat/Prac resources link to more than one Person -> Mark all links as POSSIBLE_MATCH.
		 * All other Person resources are marked as POSSIBLE_DUPLICATE of this first Person.
		 */
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());

		Patient janePatient2 = createPatient(buildJanePatient());

		//In a normal situation, janePatient2 would just match to jane patient, but here we need to hack it so they are their
		//own individual Persons for the purpose of this test.
		IAnyResource person = myPersonHelper.createSourceResourceFromEmpiTarget(janePatient2);
		myEmpiLinkSvc.updateLink(person, janePatient2, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertThat(janePatient, is(not(sameSourceResourceAs(janePatient2))));

		//In theory, this will match both Persons!
		Patient incomingJanePatient = createPatientAndUpdateLinks(buildJanePatient());

		//There should now be a single POSSIBLE_DUPLICATE link with
		assertThat(janePatient, is(possibleDuplicateOf(janePatient2)));

		//There should now be 2 POSSIBLE_MATCH links with this person.
		assertThat(incomingJanePatient, is(possibleMatchWith(janePatient, janePatient2)));

		//Ensure there is no successful MATCH links for incomingJanePatient
		Optional<EmpiLink> matchedLinkForTargetPid = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myIdHelperService.getPidOrNull(incomingJanePatient));
		assertThat(matchedLinkForTargetPid.isPresent(), is(false));

		logAllLinks();
		assertLinksMatchResult(MATCH, MATCH, POSSIBLE_MATCH, POSSIBLE_MATCH, POSSIBLE_DUPLICATE);
		assertLinksCreatedNewResource(true, true, false, false, false);
		assertLinksMatchedByEid(false, false, false, false, false);
	}

	@Test
	public void testWhenAllMatchResultsArePOSSIBLE_MATCHThattheyAreLinkedAndNoSourceResourceIsCreated() {
		/**
		 * CASE 4: Only POSSIBLE_MATCH outcomes -> In this case, empi-link records are created with POSSIBLE_MATCH
		 * outcome and await manual assignment to either NO_MATCH or MATCHED. Person link is added.
		 */
		Patient patient = buildJanePatient();
		patient.getNameFirstRep().setFamily("familyone");
		patient = createPatientAndUpdateLinks(patient);
		assertThat(patient, is(sameSourceResourceAs(patient)));

		Patient patient2 = buildJanePatient();
		patient2.getNameFirstRep().setFamily("pleasedonotmatchatall");
		patient2 = createPatientAndUpdateLinks(patient2);
		assertThat(patient2, is(possibleMatchWith(patient)));

		Patient patient3 = buildJanePatient();
		patient3.getNameFirstRep().setFamily("pleasedonotmatchatall");
		patient3 = createPatientAndUpdateLinks(patient3);

		assertThat(patient3, is(possibleMatchWith(patient2)));
		assertThat(patient3, is(possibleMatchWith(patient)));

		IBundleProvider bundle = myPatientDao.search(buildGoldenRecordSearchParameterMap());
		assertEquals(1, bundle.size());
		Patient sourcePatient = (Patient) bundle.getResources(0, 1).get(0);
		//assertEquals(Person.IdentityAssuranceLevel.LEVEL2, sourcePatient.getLink().get(0).getAssurance());
		//assertEquals(Person.IdentityAssuranceLevel.LEVEL1, sourcePatient.getLink().get(1).getAssurance());
		//assertEquals(Person.IdentityAssuranceLevel.LEVEL1, sourcePatient.getLink().get(2).getAssurance());
		//TODO GGG MDM: Convert these asserts to checking the MPI_LINK table

		assertLinksMatchResult(MATCH, POSSIBLE_MATCH, POSSIBLE_MATCH);
		assertLinksCreatedNewResource(true, false, false);
		assertLinksMatchedByEid(false, false, false);
	}

	private SearchParameterMap buildGoldenRecordSearchParameterMap() {
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLoadSynchronous(true);
		searchParameterMap.add("_tag", new TokenParam(EmpiConstants.SYSTEM_EMPI_MANAGED, EmpiConstants.CODE_HAPI_EMPI_MANAGED));
		return searchParameterMap;
	}

	@Test
	public void testWhenAnIncomingResourceHasMatchesAndPossibleMatchesThatItLinksToMatch() {
		Patient patient = buildJanePatient();
		patient.getNameFirstRep().setFamily("familyone");
		patient = createPatientAndUpdateLinks(patient);
		assertThat(patient, is(sameSourceResourceAs(patient)));

		Patient patient2 = buildJanePatient();
		patient2.getNameFirstRep().setFamily("pleasedonotmatchatall");
		patient2 = createPatientAndUpdateLinks(patient2);

		Patient patient3 = buildJanePatient();
		patient3.getNameFirstRep().setFamily("familyone");
		patient3 = createPatientAndUpdateLinks(patient3);

		assertThat(patient2, is(not(sameSourceResourceAs(patient))));
		assertThat(patient2, is(possibleMatchWith(patient)));
		assertThat(patient3, is(sameSourceResourceAs(patient)));
	}

	@Test
	public void testCreateSourceResourceFromEmpiTarget() {
		// Create Use Case #2 - adding patient with no EID
		Patient janePatient = buildJanePatient();
		Patient janeSourceResourcePatient = myPersonHelper.createSourceResourceFromEmpiTarget(janePatient);

		// golden record now contains HAPI-generated EID and HAPI tag
		assertTrue(EmpiUtil.isEmpiManaged(janeSourceResourcePatient));
		assertFalse(myEidHelper.getHapiEid(janeSourceResourcePatient).isEmpty());

		// original checks - verifies that EIDs are assigned
		assertThat("Resource must not be identical", janePatient != janeSourceResourcePatient);
		assertFalse(janePatient.getIdentifier().isEmpty());
		assertFalse(janeSourceResourcePatient.getIdentifier().isEmpty());

//		Identifier janeId = janePatient.getIdentifier().get(0);
//		Identifier janeSourceResourceId = janeSourceResourcePatient.getIdentifier().get(0);
		CanonicalEID janeId = myEidHelper.getHapiEid(janePatient).get(0);
		CanonicalEID janeSourceResourceId = myEidHelper.getHapiEid(janeSourceResourcePatient).get(0);
		print(janePatient);
		print(janeSourceResourcePatient);

		// source and target EIDs must match, as target EID should be reset to the newly created EID
		assertEquals(janeId.getValue(), janeSourceResourceId.getValue());
		assertEquals(janeId.getSystem(), janeSourceResourceId.getSystem());
	}

	//Case #1
	@Test
	public void testPatientUpdateOverwritesPersonDataOnChanges() {
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient janeSourcePatient = (Patient) getSourceResourceFromTargetResource(janePatient);

		//Change Jane's name to paul.
		Patient patient1 = buildPaulPatient();
		patient1.setId(janePatient.getId());
		Patient janePaulPatient = updatePatientAndUpdateLinks(patient1);

		assertThat(janeSourcePatient, is(sameSourceResourceAs(janePaulPatient)));

		//Ensure the related person was updated with new info.
		Patient sourcePatientFromTarget = (Patient) getSourceResourceFromTargetResource(janePaulPatient);
		HumanName nameFirstRep = sourcePatientFromTarget.getNameFirstRep();

		// TODO NG  attribute propagation has been removed - revisit once source survivorship rules are defined
		// assertThat(nameFirstRep.getGivenAsSingleString(), is(equalToIgnoringCase("paul")));
	}

	@Test
	public void testPatientCreateDoesNotOverwritePersonAttributesThatAreInvolvedInLinking() {
		Patient paul = buildPaulPatient();
		paul.setGender(Enumerations.AdministrativeGender.MALE);
		paul = createPatientAndUpdateLinks(paul);

		Patient sourcePatientFromTarget = (Patient) getSourceResourceFromTargetResource(paul);

		// TODO NG - rules haven't been determined yet revisit once implemented...
//		assertThat(sourcePatientFromTarget.getGender(), is(equalTo(Enumerations.AdministrativeGender.MALE)));

		Patient paul2 = buildPaulPatient();
		paul2.setGender(Enumerations.AdministrativeGender.FEMALE);
		paul2 = createPatientAndUpdateLinks(paul2);

		assertThat(paul2, is(sameSourceResourceAs(paul)));

		//Newly matched patients aren't allowed to overwrite Person Attributes unless they are empty, so gender should still be set to male.
		Patient paul2Person = (Patient) getSourceResourceFromTargetResource(paul2);
//		assertThat(paul2Person.getGender(), is(equalTo(Enumerations.AdministrativeGender.MALE)));
	}

	@Test
	//Test Case #1
	public void testPatientUpdatesOverwritePersonData() {
		Patient paul = buildPaulPatient();
		String incorrectBirthdate = "1980-06-27";
		paul.getBirthDateElement().setValueAsString(incorrectBirthdate);
		paul = createPatientAndUpdateLinks(paul);

		Patient sourcePatientFromTarget = (Patient) getSourceResourceFromTargetResource(paul);
		// TODO NG - rules haven't been determined yet revisit once implemented...
//		assertThat(sourcePatientFromTarget.getBirthDateElement().getValueAsString(), is(incorrectBirthdate));

		String correctBirthdate = "1990-06-28";
		paul.getBirthDateElement().setValueAsString(correctBirthdate);

		paul = updatePatientAndUpdateLinks(paul);

		sourcePatientFromTarget = (Patient) getSourceResourceFromTargetResource(paul);
		// TODO NG - rules haven't been determined yet revisit once implemented...
//		assertThat(sourcePatientFromTarget.getBirthDateElement().getValueAsString(), is(equalTo(correctBirthdate)));
		assertLinkCount(1);
	}

	@Test
	// Test Case #3
	public void testUpdatedEidThatWouldRelinkAlsoCausesPossibleDuplicate() {
		String EID_1 = "123";
		String EID_2 = "456";

		Patient paul = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), EID_1));
		Patient originalPaulPatient = (Patient) getSourceResourceFromTargetResource(paul);

		Patient jane = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), EID_2));
		Patient originalJanePatient = (Patient) getSourceResourceFromTargetResource(jane);

		clearExternalEIDs(paul);
		addExternalEID(paul, EID_2);
		updatePatientAndUpdateLinks(paul);

		assertThat(originalJanePatient, is(possibleDuplicateOf(originalPaulPatient)));
		assertThat(jane, is(sameSourceResourceAs(paul)));
	}

	@Test
	public void testSinglyLinkedPersonThatGetsAnUpdatedEidSimplyUpdatesEID() {
		//Use Case # 2
		String EID_1 = "123";
		String EID_2 = "456";

		Patient paul = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), EID_1));
		print(paul);
		Patient originalPaulPatient = (Patient) getSourceResourceFromTargetResource(paul);
		print(originalPaulPatient);

		String oldEid = myEidHelper.getExternalEid(originalPaulPatient).get(0).getValue();
		assertThat(oldEid, is(equalTo(EID_1)));

		clearExternalEIDs(paul);
		addExternalEID(paul, EID_2);

		System.out.println("Paul Before");
		print(paul);
		paul = updatePatientAndUpdateLinks(paul);
		System.out.println("Paul After");
		print(paul); // TODO NG - Paul after still has the EID - is it ok?

		assertNoDuplicates();

		Patient newlyFoundPaulPatient = (Patient) getSourceResourceFromTargetResource(paul);
		assertThat(originalPaulPatient, is(sameSourceResourceAs(newlyFoundPaulPatient)));
		String newEid = myEidHelper.getExternalEid(newlyFoundPaulPatient).get(0).getValue();
		assertThat(newEid, is(equalTo(EID_2)));
	}

	private void assertNoDuplicates() {
		List<EmpiLink> possibleDuplicates = myEmpiLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(0));
	}

	@Test
	//Test Case #3
	public void testWhenAnEidChangeWouldCauseARelinkingThatAPossibleDuplicateIsCreated() {
		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "eid-1");
		patient1 = createPatientAndUpdateLinks(patient1);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "eid-2");
		patient2 = createPatientAndUpdateLinks(patient2);

		Patient patient3 = buildPaulPatient();
		addExternalEID(patient3, "eid-2");
		patient3 = createPatientAndUpdateLinks(patient3);

		//Now, Patient 2 and 3 are linked, and the person has 2 eids.
		assertThat(patient2, is(sameSourceResourceAs(patient3)));
		assertNoDuplicates();
		//	Person A -> {P1}
		//	Person B -> {P2, P3}

		patient2.getIdentifier().clear();
		addExternalEID(patient2, "eid-1");
		patient2 = updatePatientAndUpdateLinks(patient2);

		// Person A -> {P1, P2}
		// Person B -> {P3}
		// Possible duplicates A<->B

		assertThat(patient2, is(sameSourceResourceAs(patient1)));

		List<EmpiLink> possibleDuplicates = myEmpiLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(1));
		assertThat(patient3, is(possibleDuplicateOf(patient1)));

	}
}
