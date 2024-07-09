package ca.uhn.fhir.jpa.mdm.svc;

import static org.assertj.core.api.Assertions.assertThat;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_DUPLICATE;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_MATCH;
import static org.slf4j.LoggerFactory.getLogger;

@TestPropertySource(properties = {
	"mdm.prevent_multiple_eids=false"
})
public class MdmMatchLinkSvcMultipleEidModeTest extends BaseMdmR4Test {
	private static final Logger ourLog = getLogger(MdmMatchLinkSvcMultipleEidModeTest.class);
	@Autowired
	private EIDHelper myEidHelper;

	@Test
	public void testIncomingPatientWithEIDThatMatchesGoldenResourceWithHapiEidAddsExternalEidsToGoldenResource() {
		// Existing GoldenResource with system-assigned EID found linked from matched Patient.  incoming Patient has EID.
		// Replace GoldenResource system-assigned EID with Patient EID.
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);
		assertLinksMatchScore(1.0);
		assertLinksMatchVector((Long) null);

		IAnyResource janeGoldenResource = getGoldenResourceFromTargetResource(patient);
		List<CanonicalEID> hapiEid = myEidHelper.getHapiEid(janeGoldenResource);
		String foundHapiEid = hapiEid.get(0).getValue();

		Patient janePatient = buildJanePatient();
		addExternalEID(janePatient, "12345");
		addExternalEID(janePatient, "67890");
		createPatientAndUpdateLinks(janePatient);
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, false);
		assertLinksMatchedByEid(false, false);
		assertLinksMatchScore(1.0, 2.0/3.0);
		assertLinksMatchVector(null, 6L);

		//We want to make sure the patients were linked to the same GoldenResource.
		mdmAssertThat(patient).is_MATCH_to(janePatient);

		Patient sourcePatient = (Patient) getGoldenResourceFromTargetResource(patient);

		List<Identifier> identifier = sourcePatient.getIdentifier();

		//The collision should have kept the old identifier
		Identifier firstIdentifier = identifier.get(0);
		assertThat(firstIdentifier.getSystem()).isEqualTo(MdmConstants.HAPI_ENTERPRISE_IDENTIFIER_SYSTEM);
		assertThat(firstIdentifier.getValue()).isEqualTo(foundHapiEid);

		//The collision should have added a new identifier with the external system.
		Identifier secondIdentifier = identifier.get(1);
		assertThat(secondIdentifier.getSystem()).isEqualTo(myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType("Patient"));
		assertThat(secondIdentifier.getValue()).isEqualTo("12345");

		Identifier thirdIdentifier = identifier.get(2);
		assertThat(thirdIdentifier.getSystem()).isEqualTo(myMdmSettings.getMdmRules().getEnterpriseEIDSystemForResourceType("Patient"));
		assertThat(thirdIdentifier.getValue()).isEqualTo("67890");
	}

	@Test
	// Test Case #4
	public void testHavingMultipleEIDsOnIncomingPatientMatchesCorrectly() {

		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "id_1");
		addExternalEID(patient1, "id_2");
		addExternalEID(patient1, "id_3");
		addExternalEID(patient1, "id_4");
		createPatientAndUpdateLinks(patient1);
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);
		assertLinksMatchScore(1.0);
		assertLinksMatchVector((Long) null);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "id_5");
		addExternalEID(patient2, "id_1");
		patient2 = createPatientAndUpdateLinks(patient2);
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, false);
		assertLinksMatchedByEid(false, true);
		assertLinksMatchScore(1.0, 1.0);
		assertLinksMatchVector(null, null);

		mdmAssertThat(patient1).is_MATCH_to(patient2);

		clearExternalEIDs(patient2);
		addExternalEID(patient2, "id_6");

		//At this point, there should be 5 EIDs on the GoldenResource
		Patient patientFromTarget = (Patient) getGoldenResourceFromTargetResource(patient2);
		assertThat(patientFromTarget.getIdentifier()).hasSize(5);

		ourLog.info("About to update patient...");
		updatePatientAndUpdateLinks(patient2);
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, false);
		assertLinksMatchedByEid(false, true);
		assertLinksMatchScore(1.0, 1.0);
		assertLinksMatchVector(null, null);

		mdmAssertThat(patient1).is_MATCH_to(patient2);

		patientFromTarget = (Patient) getGoldenResourceFromTargetResource(patient2);
		assertThat(patientFromTarget.getIdentifier()).hasSize(6);
	}

	@Test
	public void testDuplicateGoldenResourceLinkIsCreatedWhenAnIncomingPatientArrivesWithEIDThatMatchesAnotherEIDPatient() {
		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "eid-1");
		addExternalEID(patient1, "eid-11");
		patient1 = createPatientAndUpdateLinks(patient1);
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);
		assertLinksMatchScore(1.0);
		assertLinksMatchVector((Long) null);

		Patient patient2 = buildJanePatient();
		addExternalEID(patient2, "eid-2");
		addExternalEID(patient2, "eid-22");
		patient2 = createPatientAndUpdateLinks(patient2);
		assertLinksMatchResult(MATCH, MATCH, POSSIBLE_DUPLICATE);
		assertLinksCreatedNewResource(true, true, false);
		assertLinksMatchedByEid(false, false, true);
		assertLinksMatchScore(1.0, 1.0, null);
		assertLinksMatchVector(null, null, null);

		List<MdmLink> possibleDuplicates = (List<MdmLink>) myMdmLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates).hasSize(1);

		Patient finalPatient1 = patient1;
		Patient finalPatient2 = patient2;
		List<IResourcePersistentId> duplicatePids = runInTransaction(()->Stream.of(finalPatient1, finalPatient2)
			.map(t -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), getGoldenResourceFromTargetResource(t)))
			.collect(Collectors.toList()));

		//The two GoldenResources related to the patients should both show up in the only existing POSSIBLE_DUPLICATE MdmLink.
		MdmLink mdmLink = possibleDuplicates.get(0);
		assertThat(mdmLink.getGoldenResourcePersistenceId()).isIn(duplicatePids);
		assertThat(mdmLink.getSourcePersistenceId()).isIn(duplicatePids);
	}

	@Test
	// Test Case #5
	public void testWhenPatientEidUpdateWouldCauseALinkChangeThatDuplicateGoldenResourceIsCreatedInstead() {
		Patient patient1 = buildJanePatient();
		addExternalEID(patient1, "eid-1");
		addExternalEID(patient1, "eid-11");
		patient1 = createPatientAndUpdateLinks(patient1);
		assertLinksMatchResult(MATCH);
		assertLinksCreatedNewResource(true);
		assertLinksMatchedByEid(false);
		assertLinksMatchScore(1.0);
		assertLinksMatchVector((Long) null);

		Patient patient2 = buildPaulPatient();
		addExternalEID(patient2, "eid-2");
		addExternalEID(patient2, "eid-22");
		patient2 = createPatientAndUpdateLinks(patient2);
		assertLinksMatchResult(MATCH, MATCH);
		assertLinksCreatedNewResource(true, true);
		assertLinksMatchedByEid(false, false);
		assertLinksMatchScore(1.0, 1.0);
		assertLinksMatchVector(null, null);

		Patient patient3 = buildPaulPatient();
		addExternalEID(patient3, "eid-22");
		patient3 = createPatientAndUpdateLinks(patient3);
		assertLinksMatchResult(MATCH, MATCH, MATCH);
		assertLinksCreatedNewResource(true, true, false);
		assertLinksMatchedByEid(false, false, true);
		assertLinksMatchScore(1.0, 1.0, 1.0);
		assertLinksMatchVector(null, null, null);

		//Now, Patient 2 and 3 are linked, and the GoldenResource has 2 eids.
		mdmAssertThat(patient2).is_MATCH_to(patient3);

		//Now lets change one of the EIDs on the second patient to one that matches our original patient.
		//This should create a situation in which the incoming EIDs are matched to _two_ different GoldenResources. In this case, we want to
		//set them all to possible_match, and set the two GoldenResources as possible duplicates.
		patient2.getIdentifier().clear();
		addExternalEID(patient2, "eid-11");
		addExternalEID(patient2, "eid-22");
		patient2 = updatePatientAndUpdateLinks(patient2);
		logAllLinks();
		assertLinksMatchResult(MATCH, POSSIBLE_MATCH, MATCH, POSSIBLE_MATCH, POSSIBLE_DUPLICATE);
		assertLinksCreatedNewResource(true, true, false, false, false);
		assertLinksMatchedByEid(false, true, true, true, true);
		assertLinksMatchScore(1.0, 1.0, 1.0, 1.0, null);
		assertLinksMatchVector(null, null, null, null, null);

		mdmAssertThat(patient2).doesNotHaveGoldenResourceMatch();
		mdmAssertThat(patient2).is_POSSIBLE_MATCH_to(patient1);
		mdmAssertThat(patient2).is_POSSIBLE_MATCH_to(patient3);

		List<MdmLink> possibleDuplicates = (List<MdmLink>) myMdmLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates).hasSize(1);
		mdmAssertThat(patient3).is_POSSIBLE_DUPLICATE_to(patient1);
	}
}
