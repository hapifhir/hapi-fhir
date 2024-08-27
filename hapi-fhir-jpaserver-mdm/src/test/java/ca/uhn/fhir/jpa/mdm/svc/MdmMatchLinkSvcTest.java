package ca.uhn.fhir.jpa.mdm.svc;

import static org.assertj.core.api.Assertions.assertThat;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.config.BaseTestMdmConfig;
import ca.uhn.fhir.jpa.mdm.config.BlockListConfig;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.jpa.mdm.matcher.GoldenResourceMatchingAssert;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.blocklist.json.BlockListJson;
import ca.uhn.fhir.mdm.blocklist.json.BlockListRuleJson;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.model.MdmCreateOrUpdateParams;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.NO_MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_DUPLICATE;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_MATCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * These tests use the rules defined in mdm-rules.json
 * See {@link BaseTestMdmConfig}
 */
public class MdmMatchLinkSvcTest {

	private static final Logger ourLog = getLogger(MdmMatchLinkSvcTest.class);

	@Nested
	public class NoBlockLinkTest extends BaseMdmR4Test {
		@Autowired
		IMdmLinkSvc myMdmLinkSvc;
		@Autowired
		private EIDHelper myEidHelper;
		@Autowired
		private GoldenResourceHelper myGoldenResourceHelper;
		@Autowired
		private IMdmSurvivorshipService myMdmSurvivorshipService;
		@Autowired
		private IMdmLinkUpdaterSvc myMdmLinkUpdaterSvc;

		@Test
		public void testAddPatientLinksToNewGoldenResourceIfNoneFound() {
			createPatientAndUpdateLinks(buildJanePatient());
			assertLinkCount(1);
			assertLinksMatchResult(MATCH);
			assertLinksCreatedNewResource(true);
			assertLinksMatchedByEid(false);
			assertLinksMatchScore(1.0);
			assertLinksMatchVector((Long) null);
		}

		@Test
		public void testAddMedicationLinksToNewGoldenRecordMedicationIfNoneFound() {
			createDummyOrganization();

			createMedicationAndUpdateLinks(buildMedication("Organization/mfr"));
			assertLinkCount(1);
			assertLinksMatchResult(MATCH);
			assertLinksCreatedNewResource(true);
			assertLinksMatchedByEid(false);
			assertLinksMatchScore(1.0);
			assertLinksMatchVector((Long) null);
		}

		@RepeatedTest(20)
		public void testUpdatingAResourceToMatchACurrentlyUnmatchedResource_resultsInUpdatedLinksForBoth() {
			// setup
			MDMState<Patient, JpaPid> state = new MDMState<>();
			String startingState = """
   			GP1, AUTO, MATCH, P1
   			GP2, AUTO, MATCH, P2
			""";

			Map<String, Patient> idToResource = new HashMap<>();

			// we're creating our patients manually,
			// because we're testing mdm rules and how candidates are found
			// so the patient info matters
			Patient jane = buildJanePatient();
			Long id;
			{
				Patient p = createPatient(jane);
				idToResource.put("P1", p);
				Long patientId = p.getIdElement().getIdPartAsLong();
				state.addPID(patientId.toString(), JpaPid.fromIdAndResourceType(patientId, "Patient"));
			}
			{
				Patient yui = buildJanePatient();
				yui.setName(new ArrayList<>());
				yui.addName()
					.addGiven("Yui")
					.setFamily("Hirasawa");
				Patient retVal = createPatient(yui);
				id = retVal.getIdElement().getIdPartAsLong();
				idToResource.put("P2", retVal);
				state.addPID(String.valueOf(id), JpaPid.fromIdAndResourceType(id, "Patient"));
			}

			// initialize our links
			state.setInputState(startingState);
			state.setParameterToValue(idToResource);
			myLinkHelper.setup(state);

			// test
			Patient toUpdate = buildJanePatient();
			toUpdate.setId("Patient/" + id.longValue());
			updatePatientAndUpdateLinks(toUpdate);

			// verify
			String endState = """
   			GP1, AUTO, MATCH, P1
   			GP2, AUTO, POSSIBLE_MATCH, P2
   			GP1, AUTO, POSSIBLE_MATCH, P2
   			GP2, AUTO, POSSIBLE_DUPLICATE, GP1
			""";
			state.setParameterToValue(idToResource);
			state.setOutputState(endState);
			myLinkHelper.validateResults(state);
			myLinkHelper.logMdmLinks();
		}

		@Test
		public void testAddPatientLinksToNewlyCreatedResourceIfNoMatch() {
			Patient patient1 = createPatientAndUpdateLinks(buildJanePatient());
			Patient patient2 = createPatientAndUpdateLinks(buildPaulPatient());

			assertLinkCount(2);

			 mdmAssertThat(patient1).is_not_MATCH_to(patient2);

			assertLinksMatchResult(MATCH, MATCH);
			assertLinksCreatedNewResource(true, true);
			assertLinksMatchedByEid(false, false);
			assertLinksMatchScore(1.0, 1.0);
			assertLinksMatchVector(null, null);
		}

		@Test
		public void testAddPatientLinksToExistingGoldenResourceIfMatch() {
			Patient patient1 = createPatientAndUpdateLinks(buildJanePatient());
			assertLinkCount(1);

			Patient patient2 = createPatientAndUpdateLinks(buildJanePatient());
			assertLinkCount(2);

			mdmAssertThat(patient1).is_MATCH_to(patient2);
			assertLinksMatchResult(MATCH, MATCH);
			assertLinksCreatedNewResource(true, false);
			assertLinksMatchedByEid(false, false);
			assertLinksMatchScore(1.0, 2.0 / 3.0);
			assertLinksMatchVector(null, 6L);
		}

		@Test
		public void testWhenMatchOccursOnGoldenResourceThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
			Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());
			Patient janeGoldenResource = getGoldenResourceFromTargetResource(originalJane);

			//Create a manual NO_MATCH between janeGoldenResource and unmatchedJane.
			Patient unmatchedJane = createPatient(buildJanePatient());
			myMdmLinkSvc.updateLink(janeGoldenResource, unmatchedJane, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));

			//rerun MDM rules against unmatchedJane.
			myMdmMatchLinkSvc.updateMdmLinksForMdmSource(unmatchedJane, createContextForCreate("Patient"));

			mdmAssertThat(unmatchedJane).is_not_MATCH_to(janeGoldenResource);
			mdmAssertThat(unmatchedJane).is_not_MATCH_to(originalJane);

			assertLinksMatchResult(MATCH, NO_MATCH, MATCH);
			assertLinksCreatedNewResource(true, false, true);
			assertLinksMatchedByEid(false, false, false);
			assertLinksMatchScore(1.0, null, 1.0);
			assertLinksMatchVector(null, null, null);
		}

	@Test
	public void updateMdmLinksForMdmSource_singleCandidateDuringUpdate_DoesNotNullPointer() {

		//Given: A patient exists with a matched golden resource.
		Patient jane = createPatientAndUpdateLinks(buildJanePatient());
		Patient goldenJane = getGoldenResourceFromTargetResource(jane);

		//When: A patient who has no existing MDM links comes in as an update
		Patient secondaryJane = createPatient(buildJanePatient(), false, false);
		secondaryJane.setActive(true);
		IAnyResource resource = (IAnyResource) myPatientDao.update(secondaryJane).getResource();

		//Then: The secondary jane should link to the first jane.
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(resource, buildUpdateResourceMdmTransactionContext());
		mdmAssertThat(secondaryJane).is_MATCH_to(goldenJane);
	}

	@Test
	public void testWhenPOSSIBLE_MATCHOccursOnGoldenResourceThatHasBeenManuallyNOMATCHedThatItIsBlocked() {
		Patient originalJane = createPatientAndUpdateLinks(buildJanePatient());

			IBundleProvider search = myPatientDao.search(buildGoldenRecordSearchParameterMap());
			Patient janeGoldenResource = (Patient) search.getResources(0, 1).get(0);

			Patient unmatchedPatient = createPatient(buildJanePatient());

			// This simulates an admin specifically saying that unmatchedPatient does NOT match janeGoldenResource.
			myMdmLinkSvc.updateLink(janeGoldenResource, unmatchedPatient, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
			// TODO change this so that it will only partially match.

			//Now normally, when we run update links, it should link to janeGoldenResource. However, this manual NO_MATCH link
			//should cause a whole new GoldenResource to be created.
			myMdmMatchLinkSvc.updateMdmLinksForMdmSource(unmatchedPatient, createContextForCreate("Patient"));

		GoldenResourceMatchingAssert.assertThat(unmatchedPatient, myIdHelperService, myMdmLinkDaoSvc).is_not_MATCH_to(janeGoldenResource);
		GoldenResourceMatchingAssert.assertThat(unmatchedPatient, myIdHelperService, myMdmLinkDaoSvc).is_not_MATCH_to(originalJane);

			assertLinksMatchResult(MATCH, NO_MATCH, MATCH);
			assertLinksCreatedNewResource(true, false, true);
			assertLinksMatchedByEid(false, false, false);
			assertLinksMatchScore(1.0, null, 1.0);
			assertLinksMatchVector(null, null, null);
		}

		@Test
		public void testWhenPatientIsCreatedWithEIDThatItPropagatesToNewGoldenResource() {
			String sampleEID = "sample-eid";
			Patient janePatient = addExternalEID(buildJanePatient(), sampleEID);
			janePatient = createPatientAndUpdateLinks(janePatient);

			Optional<? extends IMdmLink> mdmLink = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(JpaPid.fromId(janePatient.getIdElement().getIdPartAsLong()));
			assertTrue(mdmLink.isPresent());

			Patient patient = getTargetResourceFromMdmLink(mdmLink.get(), "Patient");
			List<CanonicalEID> externalEid = myEidHelper.getExternalEid(patient);

			assertThat(externalEid.get(0).getSystem()).isEqualTo(myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType("Patient"));
			assertThat(externalEid.get(0).getValue()).isEqualTo(sampleEID);
		}

		@Test
		public void testWhenPatientIsCreatedWithoutAnEIDTheGoldenResourceGetsAutomaticallyAssignedOne() {
			Patient patient = createPatientAndUpdateLinks(buildJanePatient());
			IMdmLink mdmLink = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(JpaPid.fromId(patient.getIdElement().getIdPartAsLong())).get();

			Patient targetPatient = getTargetResourceFromMdmLink(mdmLink, "Patient");
			Identifier identifierFirstRep = targetPatient.getIdentifierFirstRep();
			assertThat(identifierFirstRep.getSystem()).isEqualTo(MdmConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM);
			assertThat(identifierFirstRep.getValue()).isNotBlank();
		}

		@Test
		public void testPatientAttributesAreCopiedOverWhenGoldenResourceIsCreatedFromPatient() {
			Patient patient = createPatientAndUpdateLinks(buildPatientWithNameIdAndBirthday("Gary", "GARY_ID", new Date()));

			Optional<? extends IMdmLink> mdmLink = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(JpaPid.fromId(patient.getIdElement().getIdPartAsLong()));
			Patient read = getTargetResourceFromMdmLink(mdmLink.get(), "Patient");

			assertThat(read.getNameFirstRep().getFamily()).isEqualTo(patient.getNameFirstRep().getFamily());
			assertThat(read.getNameFirstRep().getGivenAsSingleString()).isEqualTo(patient.getNameFirstRep().getGivenAsSingleString());
			assertThat(read.getBirthDateElement().toHumanDisplay()).isEqualTo(patient.getBirthDateElement().toHumanDisplay());
			assertThat(read.getTelecomFirstRep().getValue()).isEqualTo(patient.getTelecomFirstRep().getValue());
			assertThat(read.getPhoto().size()).isEqualTo(patient.getPhoto().size());
			assertThat(read.getPhotoFirstRep().getData()).isEqualTo(patient.getPhotoFirstRep().getData());
			assertThat(read.getGender()).isEqualTo(patient.getGender());
		}

		@Test
		public void testPatientMatchingAnotherPatientLinksToSameGoldenResource() {
			Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
			Patient sameJanePatient = createPatientAndUpdateLinks(buildJanePatient());
			mdmAssertThat(janePatient).is_MATCH_to(sameJanePatient);
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
			mdmAssertThat(patient).is_MATCH_to(janePatient);

			Patient sourcePatient = getGoldenResourceFromTargetResource(patient);

			List<Identifier> identifier = sourcePatient.getIdentifier();

			//The collision should have kept the old identifier
			Identifier firstIdentifier = identifier.get(0);
			assertThat(firstIdentifier.getSystem()).isEqualTo(MdmConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM);
			assertThat(firstIdentifier.getValue()).isEqualTo(foundHapiEid);

			//The collision should have added a new identifier with the external system.
			Identifier secondIdentifier = identifier.get(1);
			assertThat(secondIdentifier.getSystem()).isEqualTo(myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType("Patient"));
			assertThat(secondIdentifier.getValue()).isEqualTo("12345");
		}

		@Test
		public void testIncomingPatientWithEidMatchesAnotherPatientWithSameEIDAreLinked() {
			// Create Use Case #3
			Patient patient1 = addExternalEID(buildJanePatient(), "uniqueid");
			createPatientAndUpdateLinks(patient1);

			Patient patient2 = buildPaulPatient();
			patient2 = addExternalEID(patient2, "uniqueid");
			createPatientAndUpdateLinks(patient2);

			mdmAssertThat(patient1).is_MATCH_to(patient2);
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

			mdmAssertThat(patient1).is_MATCH_to(patient2);
		}

		@Test
		public void testDuplicateGoldenResourceLinkIsCreatedWhenAnIncomingPatientArrivesWithEIDThatMatchesAnotherEIDPatient() {

			Patient patient1 = addExternalEID(buildJanePatient(), "eid-1");
			patient1 = createPatientAndUpdateLinks(patient1);

			Patient patient2 = addExternalEID(buildJanePatient(), "eid-2");
			patient2 = createPatientAndUpdateLinks(patient2);

			List<MdmLink> possibleDuplicates = (List<MdmLink>) myMdmLinkDaoSvc.getPossibleDuplicates();
			assertThat(possibleDuplicates).hasSize(1);

			Patient finalPatient1 = patient1;
			Patient finalPatient2 = patient2;
			List<IResourcePersistentId> duplicatePids = runInTransaction(() -> Stream.of(finalPatient1, finalPatient2)
				.map(t -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), getGoldenResourceFromTargetResource(t)))
				.collect(Collectors.toList()));

			//The two GoldenResources related to the patients should both show up in the only existing POSSIBLE_DUPLICATE MdmLink.
			MdmLink mdmLink = possibleDuplicates.get(0);
			assertThat(mdmLink.getGoldenResourcePersistenceId()).isIn(duplicatePids);
			assertThat(mdmLink.getSourcePersistenceId()).isIn(duplicatePids);
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
			mdmAssertThat(janePatient).is_not_MATCH_to(janePractitioner);
		}

		@Test
		public void testPractitionersThatMatchShouldLink() {
			Practitioner janePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());
			Practitioner anotherJanePractitioner = createPractitionerAndUpdateLinks(buildJanePractitioner());

			assertLinkCount(2);
			mdmAssertThat(anotherJanePractitioner).is_MATCH_to(janePractitioner);
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
			mdmAssertThat(janePatient).hasGoldenResourceMatch();
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
			mdmAssertThat(janePatient).is_MATCH_to(janePatient2);


			Patient incomingJanePatient = createPatientAndUpdateLinks(buildJanePatient());
			mdmAssertThat(incomingJanePatient)
				.is_MATCH_to(janePatient)
				.is_MATCH_to(janePatient2);

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
			IAnyResource goldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
				janePatient2,
				new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE),
				myMdmSurvivorshipService
			);
			myMdmLinkSvc.updateLink(goldenResource, janePatient2, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
			mdmAssertThat(janePatient).is_not_MATCH_to(janePatient2);

			//In theory, this will match both GoldenResources!
			Patient incomingJanePatient = createPatientAndUpdateLinks(buildJanePatient());

			//There should now be a single POSSIBLE_DUPLICATE link with
			mdmAssertThat(janePatient).is_POSSIBLE_DUPLICATE_to(janePatient2);

			//There should now be 2 POSSIBLE_MATCH links with this goldenResource.
			mdmAssertThat(incomingJanePatient)
				.is_POSSIBLE_MATCH_to(janePatient)
				.is_POSSIBLE_MATCH_to(janePatient2);

			//Ensure there is no successful MATCH links for incomingJanePatient
			Optional<? extends IMdmLink> matchedLinkForTargetPid = runInTransaction(() -> myMdmLinkDaoSvc.getMatchedLinkForSourcePid(myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), incomingJanePatient)));
			assertThat(matchedLinkForTargetPid.isPresent()).isEqualTo(false);

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
			mdmAssertThat(patient).is_MATCH_to(patient);

			Patient patient2 = buildJanePatient();
			patient2.getNameFirstRep().setFamily("pleasedonotmatchatall");
			patient2 = createPatientAndUpdateLinks(patient2);
			mdmAssertThat(patient2).is_POSSIBLE_MATCH_to(patient);

			Patient patient3 = buildJanePatient();
			patient3.getNameFirstRep().setFamily("pleasedonotmatchatall");
			patient3 = createPatientAndUpdateLinks(patient3);

			mdmAssertThat(patient3).is_POSSIBLE_MATCH_to(patient2);
			mdmAssertThat(patient3).is_POSSIBLE_MATCH_to(patient);

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
			mdmAssertThat(patient).is_MATCH_to(patient);

			Patient patient2 = buildJanePatient();
			patient2.getNameFirstRep().setFamily("pleasedonotmatchatall");
			patient2 = createPatientAndUpdateLinks(patient2);

			Patient patient3 = buildJanePatient();
			patient3.getNameFirstRep().setFamily("familyone");
			patient3 = createPatientAndUpdateLinks(patient3);

			mdmAssertThat(patient2).is_not_MATCH_to(patient);
			mdmAssertThat(patient2).is_POSSIBLE_MATCH_to(patient);
			mdmAssertThat(patient3).is_MATCH_to(patient);
		}


		@Test
		public void testPossibleMatchUpdatedToMatch() {
			// setup
			Patient patient = buildJanePatient();
			patient.getNameFirstRep().setFamily("familyone");
			patient = createPatientAndUpdateLinks(patient);
			mdmAssertThat(patient).is_MATCH_to(patient);

			Patient patient2 = buildJanePatient();
			patient2.getNameFirstRep().setFamily("pleasedonotmatchatall");
			patient2 = createPatientAndUpdateLinks(patient2);

			mdmAssertThat(patient2).is_not_MATCH_to(patient);
			mdmAssertThat(patient2).is_POSSIBLE_MATCH_to(patient);

			patient2.getNameFirstRep().setFamily(patient.getNameFirstRep().getFamily());

			// execute
			updatePatientAndUpdateLinks(patient2);

			// validate
			mdmAssertThat(patient2).is_MATCH_to(patient);
		}

		@Test
		public void testCreateGoldenResourceFromMdmTarget() {
			// Create Use Case #2 - adding patient with no EID
			Patient janePatient = buildJanePatient();
			Patient janeGoldenResourcePatient = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
				janePatient,
				new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE),
				myMdmSurvivorshipService
			);

			// golden record now contains HAPI-generated EID and HAPI tag
			assertTrue(MdmResourceUtil.isMdmManaged(janeGoldenResourcePatient));
			assertFalse(myEidHelper.getHapiEid(janeGoldenResourcePatient).isEmpty());

			// original checks - verifies that EIDs are assigned
			assertThat(janePatient != janeGoldenResourcePatient).as("Resource must not be identical").isTrue();
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

			mdmAssertThat(janeSourcePatient).is_MATCH_to(janePaulPatient);

			//Ensure the related GoldenResource was updated with new info.
			Patient sourcePatientFromTarget = getGoldenResourceFromTargetResource(janePaulPatient);
			HumanName nameFirstRep = sourcePatientFromTarget.getNameFirstRep();

			assertThat(nameFirstRep.getGivenAsSingleString()).isEqualToIgnoringCase("paul");
		}

		@Test
		//Test Case #1
		public void testPatientUpdatesOverwriteGoldenResourceData() {
			Patient paul = buildPaulPatient();
			String incorrectBirthdate = "1980-06-27";
			paul.getBirthDateElement().setValueAsString(incorrectBirthdate);
			paul = createPatientAndUpdateLinks(paul);

			Patient sourcePatientFromTarget = getGoldenResourceFromTargetResource(paul);
			assertThat(sourcePatientFromTarget.getBirthDateElement().getValueAsString()).isEqualTo(incorrectBirthdate);

			String correctBirthdate = "1990-06-28";
			paul.getBirthDateElement().setValueAsString(correctBirthdate);

			paul = updatePatientAndUpdateLinks(paul);

			sourcePatientFromTarget = getGoldenResourceFromTargetResource(paul);
			assertThat(sourcePatientFromTarget.getBirthDateElement().getValueAsString()).isEqualTo(correctBirthdate);
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

			mdmAssertThat(originalJaneGolden).is_POSSIBLE_DUPLICATE_to(originalPaulGolden);
			mdmAssertThat(jane).is_MATCH_to(paul);
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
			MdmCreateOrUpdateParams params = new MdmCreateOrUpdateParams();
			params.setGoldenResource(originalPaulGolden);
			params.setSourceResource(paul);
			params.setMatchResult(NO_MATCH);
			params.setRequestDetails(new SystemRequestDetails());
			params.setMdmContext(mdmCtx);
			myMdmLinkUpdaterSvc.updateLink(params);

			clearExternalEIDs(paul);
			addExternalEID(paul, EID_2);

			// execute
			updatePatientAndUpdateLinks(paul);

			// verify
			mdmAssertThat(originalJaneGolden).is_not_POSSIBLE_DUPLICATE_to(originalPaulGolden);
			mdmAssertThat(jane).is_MATCH_to(paul);
		}

		@Test
		public void testSinglyLinkedGoldenResourceThatGetsAnUpdatedEidSimplyUpdatesEID() {
			//Use Case # 2
			Patient paul = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), EID_1));
			Patient originalPaulGolden = getGoldenResourceFromTargetResource(paul);

			String oldEid = myEidHelper.getExternalEid(originalPaulGolden).get(0).getValue();
			assertThat(oldEid).isEqualTo(EID_1);

			clearExternalEIDs(paul);
			addExternalEID(paul, EID_2);

			paul = updatePatientAndUpdateLinks(paul);
			assertNoDuplicates();

			Patient newlyFoundPaulPatient = getGoldenResourceFromTargetResource(paul);
			mdmAssertThat(originalPaulGolden).is_MATCH_to(newlyFoundPaulPatient);
			String newEid = myEidHelper.getExternalEid(newlyFoundPaulPatient).get(0).getValue();
			assertThat(newEid).isEqualTo(EID_2);
		}

		private void assertNoDuplicates() {
			List<MdmLink> possibleDuplicates = (List<MdmLink>) myMdmLinkDaoSvc.getPossibleDuplicates();
			assertThat(possibleDuplicates).hasSize(0);
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
			mdmAssertThat(patient2).is_MATCH_to(patient3);
			assertNoDuplicates();
			//	GoldenResource A -> {P1}
			//	GoldenResource B -> {P2, P3}

			patient2.getIdentifier().clear();
			addExternalEID(patient2, "eid-1");
			patient2 = updatePatientAndUpdateLinks(patient2);

			// GoldenResource A -> {P1, P2}
			// GoldenResource B -> {P3}
			// Possible duplicates A<->B

			mdmAssertThat(patient2).is_MATCH_to(patient1);

			List<MdmLink> possibleDuplicates = (List<MdmLink>) myMdmLinkDaoSvc.getPossibleDuplicates();
			assertThat(possibleDuplicates).hasSize(1);
			mdmAssertThat(patient3).is_POSSIBLE_DUPLICATE_to(patient1);
		}

		@Test
		public void testWhen_POSSIBLE_MATCH_And_POSSIBLE_DUPLICATE_LinksCreated_ScorePopulatedOnPossibleMatchLinks() {
			Patient janePatient = createPatientAndUpdateLinks(buildJanePatient());
			Patient janePatient2 = createPatient(buildJanePatient());

			//In a normal situation, janePatient2 would just match to jane patient, but here we need to hack it so they are their
			//own individual GoldenResource for the purpose of this test.
			IAnyResource goldenResource = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
				janePatient2,
				new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE),
				myMdmSurvivorshipService
			);
			myMdmLinkSvc.updateLink(goldenResource, janePatient2, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH,
				MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
			mdmAssertThat(janePatient).is_not_MATCH_to(janePatient2);

			//In theory, this will match both GoldenResources!
			Patient incomingJanePatient = createPatientAndUpdateLinks(buildJanePatient());

			//There should now be a single POSSIBLE_DUPLICATE link with
			mdmAssertThat(janePatient).is_POSSIBLE_DUPLICATE_to(janePatient2);

			//There should now be 2 POSSIBLE_MATCH links with this goldenResource.
			mdmAssertThat(incomingJanePatient).is_POSSIBLE_MATCH_to(janePatient).is_POSSIBLE_MATCH_to(janePatient2);

			// Ensure both links are POSSIBLE_MATCH and both have a score value
			List<? extends IMdmLink> janetPatientLinks = runInTransaction(() -> myMdmLinkDaoSvc.findMdmLinksBySourceResource(incomingJanePatient));
			assertEquals(2, janetPatientLinks.size());
			janetPatientLinks.forEach(l -> {
				assertEquals(MdmMatchResultEnum.POSSIBLE_MATCH, l.getMatchResult());
				assertNotNull(l.getScore());
			});
		}
	}

	@Nested
	@ContextConfiguration(classes = {
		BlockListConfig.class
	})
	public class BlockLinkTest extends BaseMdmR4Test {

		@Autowired
		private IBlockListRuleProvider myBlockListRuleProvider;

		@Test
		public void updateMdmLinksForMdmSource_createBlockedResource_alwaysCreatesNewGoldenResource() {
			// setup
			String blockedFirstName = "Jane";
			String blockedLastName = "Doe";

			BlockListJson blockListJson = new BlockListJson();
			BlockListRuleJson rule = new BlockListRuleJson();
			rule.setResourceType("Patient");
			rule.addBlockListField()
				.setFhirPath("name.single().family")
				.setBlockedValue(blockedLastName);
			rule.addBlockListField()
				.setFhirPath("name.single().given.first()")
				.setBlockedValue(blockedFirstName);
			blockListJson.addBlockListRule(rule);

			MdmTransactionContext mdmContext = createContextForCreate("Patient");

			// when
			when(myBlockListRuleProvider.getBlocklistRules())
				.thenReturn(blockListJson);

			// create patients
			Patient unblockedPatient;
			{
				unblockedPatient = buildJanePatient();
				unblockedPatient = createPatient(unblockedPatient);
				myMdmMatchLinkSvc.updateMdmLinksForMdmSource(unblockedPatient, mdmContext);
			}

				// our blocked name is Jane Doe... let's make sure that's the case
				Patient blockedPatient = buildJanePatient();
				assertEquals(blockedLastName, blockedPatient.getName().get(0).getFamily());
				assertEquals(blockedFirstName, blockedPatient.getName().get(0).getGivenAsSingleString());
				blockedPatient = createPatient(blockedPatient);

			// test
			myMdmMatchLinkSvc.updateMdmLinksForMdmSource(blockedPatient, mdmContext);

			// verify
			List<IBaseResource> grs = getAllGoldenPatients();
			assertEquals(2, grs.size());
			assertEquals(0, myMdmLinkDaoSvc.getPossibleDuplicates().size());

			List<MdmLink> links = new ArrayList<>();
			for (IBaseResource gr : grs) {
				links.addAll(getAllMdmLinks((Patient)gr));
			}
			assertEquals(2, links.size());
			Set<Long> ids = new HashSet<>();
			for (MdmLink link : links) {
				JpaPid pid = link.getSourcePersistenceId();
				assertTrue(ids.add(pid.getId()));
				JpaPid gpid = link.getGoldenResourcePersistenceId();
				assertTrue(ids.add(gpid.getId()));
			}
		}

		public List<MdmLink> getAllMdmLinks(Patient theGoldenPatient) {
			return myMdmLinkDaoSvc.findMdmLinksByGoldenResource(theGoldenPatient).stream()
				.map( link -> (MdmLink) link)
				.collect(Collectors.toList());
		}
	}
}
