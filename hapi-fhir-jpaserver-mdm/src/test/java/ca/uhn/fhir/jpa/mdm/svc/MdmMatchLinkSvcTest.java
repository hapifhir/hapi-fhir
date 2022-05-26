package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.NO_MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_DUPLICATE;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_MATCH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmMatchLinkSvcTest extends BaseMdmR4Test {
	@Autowired
	IMdmLinkSvc myMdmLinkSvc;
	@Autowired
	private EIDHelper myEidHelper;
	@Autowired
	private GoldenResourceHelper myGoldenResourceHelper;
	@Autowired
	private IMdmLinkUpdaterSvc myMdmLinkUpdaterSvc;

	@Test
	public void testAddPatientLinksToNewGoldenResourceIfNoneFound() {
		createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);
	}

	@Test
	public void testAddMedicationLinksToNewGoldenRecordMedicationIfNoneFound() {
		createDummyOrganization();

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

		assertThat(patient1, is(not(sameGoldenResourceAs(patient2))));

		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, true);
		assertLinksMatchedByEid(false, false);
	}

	@Test
	public void testAddPatientLinksToExistingGoldenResourceIfMatch() {
		Patient patient1 = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);

		Patient patient2 = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(2);

		assertThat(patient1, is(sameGoldenResourceAs(patient2)));
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, false);
		assertLinksMatchedByEid(false, false);
	}

	@Test
	public void testWhenMatchOccursOnGoldenResourceThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());
		IAnyResource janeGoldenResource = getGoldenResourceFromTargetResource(originalJane);

		//Create a manual NO_MATCH between janeGoldenResource and unmatchedJane.
		Patient unmatchedJane = createPatient(buildJanePatient());
		myMdmLinkSvc.updateLink(janeGoldenResource, unmatchedJane, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));

		//rerun MDM rules against unmatchedJane.
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(unmatchedJane, createContextForCreate("Patient"));

		assertThat(unmatchedJane, is(not(sameGoldenResourceAs(janeGoldenResource))));
		assertThat(unmatchedJane, is(not(linkedTo(originalJane))));

		assertLinksMatchResult(MATCH, NO_MATCH, MATCH);
		assertLinksCreatedNewResource(true, false, true);
		assertLinksMatchedByEid(false, false, false);
	}

	@Test
	public void testWhenPOSSIBLE_MATCHOccursOnGoldenResourceThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());

		IBundleProvider search = myPatientDao.search(buildGoldenRecordSearchParameterMap());
		IAnyResource janeGoldenResource = (IAnyResource) search.getResources(0, 1).get(0);

		Patient unmatchedPatient = createPatient(buildJanePatient());

		// This simulates an admin specifically saying that unmatchedPatient does NOT match janeGoldenResource.
		myMdmLinkSvc.updateLink(janeGoldenResource, unmatchedPatient, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		// TODO change this so that it will only partially match.

		//Now normally, when we run update links, it should link to janeGoldenResource. However, this manual NO_MATCH link
		//should cause a whole new GoldenResource to be created.
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(unmatchedPatient, createContextForCreate("Patient"));

		assertThat(unmatchedPatient, is(not(sameGoldenResourceAs(janeGoldenResource))));
		assertThat(unmatchedPatient, is(not(linkedTo(originalJane))));

		assertLinksMatchResult(MATCH, NO_MATCH, MATCH);
		assertLinksCreatedNewResource(true, false, true);
		assertLinksMatchedByEid(false, false, false);
	}

	@Test
	public void testWhenPatientIsCreatedWithEIDThatItPropagatesToNewGoldenResource() {
		String sampleEID = "sample-eid";
		Patient janePatient = addExternalEID(buildJanePatient(), sampleEID);
		janePatient = createPatientAndUpdateLinks(janePatient);

		Optional<MdmLink> mdmLink = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(janePatient.getIdElement().getIdPartAsLong());
		assertThat(mdmLink.isPresent(), is(true));

		Patient patient = getTargetResourceFromMdmLink(mdmLink.get(), "Patient");
		List<CanonicalEID> externalEid = myEidHelper.getExternalEid(patient);

		assertThat(externalEid.get(0).getSystem(), is(equalTo(myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType("Patient"))));
		assertThat(externalEid.get(0).getValue(), is(equalTo(sampleEID)));
	}

	@Test
	public void testWhenPatientIsCreatedWithoutAnEIDTheGoldenResourceGetsAutomaticallyAssignedOne() {
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		MdmLink mdmLink = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(patient.getIdElement().getIdPartAsLong()).get();

		Patient targetPatient = getTargetResourceFromMdmLink(mdmLink, "Patient");
		Identifier identifierFirstRep = targetPatient.getIdentifierFirstRep();
		assertThat(identifierFirstRep.getSystem(), is(equalTo(MdmConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(identifierFirstRep.getValue(), not(blankOrNullString()));
	}

	@Test
	public void testPatientAttributesAreCopiedOverWhenGoldenResourceIsCreatedFromPatient() {
		Patient patient = createPatientAndUpdateLinks(buildPatientWithNameIdAndBirthday("Gary", "GARY_ID", new Date()));

		Optional<MdmLink> mdmLink = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(patient.getIdElement().getIdPartAsLong());
		Patient read = getTargetResourceFromMdmLink(mdmLink.get(), "Patient");

		assertThat(read.getNameFirstRep().getFamily(), is(equalTo(patient.getNameFirstRep().getFamily())));
		assertThat(read.getNameFirstRep().getGivenAsSingleString(), is(equalTo(patient.getNameFirstRep().getGivenAsSingleString())));
		assertThat(read.getBirthDateElement().toHumanDisplay(), is(equalTo(patient.getBirthDateElement().toHumanDisplay())));
		assertThat(read.getTelecomFirstRep().getValue(), is(equalTo(patient.getTelecomFirstRep().getValue())));
		assertThat(read.getPhoto().size(), is(equalTo(patient.getPhoto().size())));
		assertThat(read.getPhotoFirstRep().getData(), is(equalTo(patient.getPhotoFirstRep().getData())));
		assertThat(read.getGender(), is(equalTo(patient.getGender())));
	}

	@Test
	public void testPatientMatchingAnotherPatientLinksToSameGoldenResource() {
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient sameJanePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertThat(janePatient, is(sameGoldenResourceAs(sameJanePatient)));
	}

	@Test
	public void testIncomingPatientWithEIDThatMatchesGoldenResourceWithHapiEidAddsExternalEidToGoldenResource() {
		// Existing GoldenResource with system-assigned EID found linked from matched Patient.  incoming Patient has EID.
		// Replace GoldenResource system-assigned EID with Patient EID.
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());

		IAnyResource janeGoldenResource = getGoldenResourceFromTargetResource(patient);
		List<CanonicalEID> hapiEid = myEidHelper.getHapiEid(janeGoldenResource);
		String foundHapiEid = hapiEid.get(0).getValue();

		Patient janePatient = addExternalEID(buildJanePatient(), "12345");
		createPatientAndUpdateLinks(janePatient);

		//We want to make sure the patients were linked to the same Golden Resource.
		assertThat(patient, is(sameGoldenResourceAs(janePatient)));

		Patient sourcePatient = getGoldenResourceFromTargetResource(patient);

		List<Identifier> identifier = sourcePatient.getIdentifier();

		//The collision should have kept the old identifier
		Identifier firstIdentifier = identifier.get(0);
		assertThat(firstIdentifier.getSystem(), is(equalTo(MdmConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM)));
		assertThat(firstIdentifier.getValue(), is(equalTo(foundHapiEid)));

		//The collision should have added a new identifier with the external system.
		Identifier secondIdentifier = identifier.get(1);
		assertThat(secondIdentifier.getSystem(), is(equalTo(myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType("Patient"))));
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

		assertThat(patient1, is(sameGoldenResourceAs(patient2)));
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

		assertThat(patient1, is(sameGoldenResourceAs(patient2)));
	}

	@Test
	public void testDuplicateGoldenResourceLinkIsCreatedWhenAnIncomingPatientArrivesWithEIDThatMatchesAnotherEIDPatient() {

		Patient patient1 = addExternalEID(buildJanePatient(), "eid-1");
		patient1 = createPatientAndUpdateLinks(patient1);

		Patient patient2 = addExternalEID(buildJanePatient(), "eid-2");
		patient2 = createPatientAndUpdateLinks(patient2);

		List<MdmLink> possibleDuplicates = myMdmLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(1));

		Patient finalPatient1 = patient1;
		Patient finalPatient2 = patient2;
		List<Long> duplicatePids = runInTransaction(()->Stream.of(finalPatient1, finalPatient2)
			.map(this::getGoldenResourceFromTargetResource)
			.map(myIdHelperService::getPidOrNull)
			.collect(Collectors.toList()));

		//The two GoldenResources related to the patients should both show up in the only existing POSSIBLE_DUPLICATE MdmLink.
		MdmLink mdmLink = possibleDuplicates.get(0);
		assertThat(mdmLink.getGoldenResourcePid(), is(in(duplicatePids)));
		assertThat(mdmLink.getSourcePid(), is(in(duplicatePids)));
	}

	@Test
	public void testPatientWithNoMdmTagIsNotMatched() {
		// Patient with "no-mdm" tag is not matched
		Patient janePatient = buildJanePatient();
		janePatient.getMeta().addTag(MdmConstants.SYSTEM_MDM_MANAGED, MdmConstants.CODE_NO_MDM_MANAGED, "Don't MDM on me!");
		String s = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(janePatient);
		createPatientAndUpdateLinks(janePatient);
		assertLinkCount(0);
	}

	@Test
	public void testPractitionersDoNotMatchToPatients() {
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Practitioner janePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());

		assertLinkCount(2);
		assertThat(janePatient, is(not(sameGoldenResourceAs(janePractitioner))));
	}

	@Test
	public void testPractitionersThatMatchShouldLink() {
		Practitioner janePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());
		Practitioner anotherJanePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());

		assertLinkCount(2);
		assertThat(anotherJanePractitioner, is(sameGoldenResourceAs(janePractitioner)));
	}

	@Test
	public void testWhenThereAreNoMATCHOrPOSSIBLE_MATCHOutcomesThatANewGoldenResourceIsCreated() {
		/**
		 * CASE 1: No MATCHED and no PROBABLE_MATCHED outcomes -> a new GoldenResource resource
		 * is created and linked to that Pat/Prac.
		 */
		assertLinkCount(0);
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(1);
		assertThat(janePatient, is(matchedToAGoldenResource()));
	}

	@Test
	public void testWhenAllMATCHResultsAreToSameGoldenResourceThatTheyAreLinked() {
		/**
		 * CASE 2: All of the MATCHED Pat/Prac resources are already linked to the same GoldenResource ->
		 * a new Link is created between the new Pat/Prac and that GoldenResource and is set to MATCHED.
		 */
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient janePatient2 = createPatientAndUpdateLinks(buildJanePatient());

		assertLinkCount(2);
		assertThat(janePatient, is(sameGoldenResourceAs(janePatient2)));

		Patient incomingJanePatient = createPatientAndUpdateLinks(buildJanePatient());
		assertThat(incomingJanePatient, is(sameGoldenResourceAs(janePatient, janePatient2)));
		assertThat(incomingJanePatient, is(linkedTo(janePatient, janePatient2)));
	}

	@Test
	public void testMATCHResultWithMultipleCandidatesCreatesPOSSIBLE_DUPLICATELinksAndNoGoldenResourceIsCreated() {
		/**
		 * CASE 3: The MATCHED Pat/Prac resources link to more than one GoldenResource -> Mark all links as POSSIBLE_MATCH.
		 * All other GoldenResource resources are marked as POSSIBLE_DUPLICATE of this first GoldenResource.
		 */
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient janePatient2 = createPatient(buildJanePatient());

		//In a normal situation, janePatient2 would just match to jane patient, but here we need to hack it so they are their
		//own individual GoldenResource for the purpose of this test.
		IAnyResource goldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(janePatient2, new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE));
		myMdmLinkSvc.updateLink(goldenResource, janePatient2, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertThat(janePatient, is(not(sameGoldenResourceAs(janePatient2))));

		//In theory, this will match both GoldenResources!
		Patient incomingJanePatient = createPatientAndUpdateLinks(buildJanePatient());

		//There should now be a single POSSIBLE_DUPLICATE link with
		assertThat(janePatient, is(possibleDuplicateOf(janePatient2)));

		//There should now be 2 POSSIBLE_MATCH links with this goldenResource.
		assertThat(incomingJanePatient, is(possibleMatchWith(janePatient, janePatient2)));

		//Ensure there is no successful MATCH links for incomingJanePatient
		Optional<MdmLink> matchedLinkForTargetPid = runInTransaction(()->myMdmLinkDaoSvc.getMatchedLinkForSourcePid(myIdHelperService.getPidOrNull(incomingJanePatient)));
		assertThat(matchedLinkForTargetPid.isPresent(), is(false));

		logAllLinks();
		assertLinksMatchResult(MATCH, MATCH, POSSIBLE_MATCH, POSSIBLE_MATCH, POSSIBLE_DUPLICATE);
		assertLinksCreatedNewResource(true, true, false, false, false);
		assertLinksMatchedByEid(false, false, false, false, false);
	}

	@Test
	public void testWhenAllMatchResultsArePOSSIBLE_MATCHThattheyAreLinkedAndNoGoldenResourceIsCreated() {
		/**
		 * CASE 4: Only POSSIBLE_MATCH outcomes -> In this case, mdm-link records are created with POSSIBLE_MATCH
		 * outcome and await manual assignment to either NO_MATCH or MATCHED. GoldenResource link is added.
		 */
		Patient patient = buildJanePatient();
		patient.getNameFirstRep().setFamily("familyone");
		patient = createPatientAndUpdateLinks(patient);
		assertThat(patient, is(sameGoldenResourceAs(patient)));

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

		//TODO GGG MDM: Convert these asserts to checking the MPI_LINK table

		assertLinksMatchResult(MATCH, POSSIBLE_MATCH, POSSIBLE_MATCH);
		assertLinksCreatedNewResource(true, false, false);
		assertLinksMatchedByEid(false, false, false);
	}

	private SearchParameterMap buildGoldenRecordSearchParameterMap() {
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLoadSynchronous(true);
		searchParameterMap.add("_tag", new TokenParam(MdmConstants.SYSTEM_MDM_MANAGED, MdmConstants.CODE_HAPI_MDM_MANAGED));
		return searchParameterMap;
	}

	@Test
	public void testWhenAnIncomingResourceHasMatchesAndPossibleMatchesThatItLinksToMatch() {
		Patient patient = buildJanePatient();
		patient.getNameFirstRep().setFamily("familyone");
		patient = createPatientAndUpdateLinks(patient);
		assertThat(patient, is(sameGoldenResourceAs(patient)));

		Patient patient2 = buildJanePatient();
		patient2.getNameFirstRep().setFamily("pleasedonotmatchatall");
		patient2 = createPatientAndUpdateLinks(patient2);

		Patient patient3 = buildJanePatient();
		patient3.getNameFirstRep().setFamily("familyone");
		patient3 = createPatientAndUpdateLinks(patient3);

		assertThat(patient2, is(not(sameGoldenResourceAs(patient))));
		assertThat(patient2, is(possibleMatchWith(patient)));
		assertThat(patient3, is(sameGoldenResourceAs(patient)));
	}


	@Test
	public void testPossibleMatchUpdatedToMatch() {
		// setup
		Patient patient = buildJanePatient();
		patient.getNameFirstRep().setFamily("familyone");
		patient = createPatientAndUpdateLinks(patient);
		assertThat(patient, is(sameGoldenResourceAs(patient)));

		Patient patient2 = buildJanePatient();
		patient2.getNameFirstRep().setFamily("pleasedonotmatchatall");
		patient2 = createPatientAndUpdateLinks(patient2);

		assertThat(patient2, is(not(sameGoldenResourceAs(patient))));
		assertThat(patient2, is(not(linkedTo(patient))));
		assertThat(patient2, is(possibleMatchWith(patient)));

		patient2.getNameFirstRep().setFamily(patient.getNameFirstRep().getFamily());

		// execute
		updatePatientAndUpdateLinks(patient2);

		// validate
		assertThat(patient2, is(linkedTo(patient)));
		assertThat(patient2, is(sameGoldenResourceAs(patient)));
	}

	@Test
	public void testCreateGoldenResourceFromMdmTarget() {
		// Create Use Case #2 - adding patient with no EID
		Patient janePatient = buildJanePatient();
		Patient janeGoldenResourcePatient = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(janePatient, new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE));

		// golden record now contains HAPI-generated EID and HAPI tag
		assertTrue(MdmResourceUtil.isMdmManaged(janeGoldenResourcePatient));
		assertFalse(myEidHelper.getHapiEid(janeGoldenResourcePatient).isEmpty());

		// original checks - verifies that EIDs are assigned
		assertThat("Resource must not be identical", janePatient != janeGoldenResourcePatient);
		assertFalse(janePatient.getIdentifier().isEmpty());
		assertFalse(janeGoldenResourcePatient.getIdentifier().isEmpty());

		CanonicalEID janeId = myEidHelper.getHapiEid(janePatient).get(0);
		CanonicalEID janeGoldenResourceId = myEidHelper.getHapiEid(janeGoldenResourcePatient).get(0);

		// source and target EIDs must match, as target EID should be reset to the newly created EID
		assertEquals(janeId.getValue(), janeGoldenResourceId.getValue());
		assertEquals(janeId.getSystem(), janeGoldenResourceId.getSystem());
	}

	//Case #1
	@Test
	public void testPatientUpdateOverwritesGoldenResourceDataOnChanges() {
		Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
		Patient janeSourcePatient = getGoldenResourceFromTargetResource(janePatient);

		//Change Jane's name to paul.
		Patient patient1 = buildPaulPatient();
		patient1.setId(janePatient.getId());
		Patient janePaulPatient = updatePatientAndUpdateLinks(patient1);

		assertThat(janeSourcePatient, is(sameGoldenResourceAs(janePaulPatient)));

		//Ensure the related GoldenResource was updated with new info.
		Patient sourcePatientFromTarget = getGoldenResourceFromTargetResource(janePaulPatient);
		HumanName nameFirstRep = sourcePatientFromTarget.getNameFirstRep();

		assertThat(nameFirstRep.getGivenAsSingleString(), is(equalToIgnoringCase("paul")));
	}

	@Test
	//Test Case #1
	public void testPatientUpdatesOverwriteGoldenResourceData() {
		Patient paul = buildPaulPatient();
		String incorrectBirthdate = "1980-06-27";
		paul.getBirthDateElement().setValueAsString(incorrectBirthdate);
		paul = createPatientAndUpdateLinks(paul);

		Patient sourcePatientFromTarget = getGoldenResourceFromTargetResource(paul);
		assertThat(sourcePatientFromTarget.getBirthDateElement().getValueAsString(), is(incorrectBirthdate));

		String correctBirthdate = "1990-06-28";
		paul.getBirthDateElement().setValueAsString(correctBirthdate);

		paul = updatePatientAndUpdateLinks(paul);

		sourcePatientFromTarget = getGoldenResourceFromTargetResource(paul);
		assertThat(sourcePatientFromTarget.getBirthDateElement().getValueAsString(), is(equalTo(correctBirthdate)));
		assertLinkCount(1);
	}

	@Test
	// Test Case #3
	public void testUpdatedEidThatWouldRelinkAlsoCausesPossibleDuplicate() {
		Patient paul = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), EID_1));
		Patient originalPaulGolden = getGoldenResourceFromTargetResource(paul);

		Patient jane = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), EID_2));
		Patient originalJaneGolden = getGoldenResourceFromTargetResource(jane);

		clearExternalEIDs(paul);
		addExternalEID(paul, EID_2);
		updatePatientAndUpdateLinks(paul);

		assertThat(originalJaneGolden, is(possibleDuplicateOf(originalPaulGolden)));
		assertThat(jane, is(sameGoldenResourceAs(paul)));
	}

	@Test
	// Test Case #3a
	public void originalLinkIsNoMatch() {
		// setup
		Patient paul = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), EID_1));
		Patient originalPaulGolden = getGoldenResourceFromTargetResource(paul);

		Patient jane = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), EID_2));
		Patient originalJaneGolden = getGoldenResourceFromTargetResource(jane);

		MdmTransactionContext mdmCtx = buildUpdateLinkMdmTransactionContext();
		myMdmLinkUpdaterSvc.updateLink(originalPaulGolden, paul, NO_MATCH, mdmCtx);

		clearExternalEIDs(paul);
		addExternalEID(paul, EID_2);

		// execute
		updatePatientAndUpdateLinks(paul);

		// verify
		assertThat(originalJaneGolden, is(not(possibleDuplicateOf(originalPaulGolden))));
		assertThat(jane, is(sameGoldenResourceAs(paul)));
	}

	@Test
	public void testSinglyLinkedGoldenResourceThatGetsAnUpdatedEidSimplyUpdatesEID() {
		//Use Case # 2
		Patient paul = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), EID_1));
		Patient originalPaulGolden = getGoldenResourceFromTargetResource(paul);

		String oldEid = myEidHelper.getExternalEid(originalPaulGolden).get(0).getValue();
		assertThat(oldEid, is(equalTo(EID_1)));

		clearExternalEIDs(paul);
		addExternalEID(paul, EID_2);

		paul = updatePatientAndUpdateLinks(paul);
		assertNoDuplicates();

		Patient newlyFoundPaulPatient = getGoldenResourceFromTargetResource(paul);
		assertThat(originalPaulGolden, is(sameGoldenResourceAs(newlyFoundPaulPatient)));
		String newEid = myEidHelper.getExternalEid(newlyFoundPaulPatient).get(0).getValue();
		assertThat(newEid, is(equalTo(EID_2)));
	}

	private void assertNoDuplicates() {
		List<MdmLink> possibleDuplicates = myMdmLinkDaoSvc.getPossibleDuplicates();
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

		//Now, Patient 2 and 3 are linked, and the GoldenResource has 2 eids.
		assertThat(patient2, is(sameGoldenResourceAs(patient3)));
		assertNoDuplicates();
		//	GoldenResource A -> {P1}
		//	GoldenResource B -> {P2, P3}

		patient2.getIdentifier().clear();
		addExternalEID(patient2, "eid-1");
		patient2 = updatePatientAndUpdateLinks(patient2);

		// GoldenResource A -> {P1, P2}
		// GoldenResource B -> {P3}
		// Possible duplicates A<->B

		assertThat(patient2, is(sameGoldenResourceAs(patient1)));

		List<MdmLink> possibleDuplicates = myMdmLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates, hasSize(1));
		assertThat(patient3, is(possibleDuplicateOf(patient1)));
	}
}
