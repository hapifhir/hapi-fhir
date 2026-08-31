package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_DUPLICATE;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_MATCH;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Linking behaviour when a resource type is identified by more than one EID system. Runs at the default
 * {@code prevent_multiple_eids=true}, since that safeguard is now scoped per EID system and so no longer
 * stands in the way of a Patient carrying one MRN and one NPI.
 */
@TestPropertySource(properties = {"module.mdm.config.script.file=classpath:mdm/mdm-rules-multi-eid-systems.json"})
// Created by claude-opus-5
public class MdmMatchLinkSvcMultiEidSystemTest extends BaseMdmR4Test {

	private String mrnSystem() {
		return patientEidSystems().get(0);
	}
	private String npiSystem() { return patientEidSystems().get(1); }

	@Test
	public void patientsSharingAnMrn_areLinkedToTheSameGoldenResource() {
		Patient first = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1"));
		Patient second = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), mrnSystem(), "mrn-1"));

		assertThat(getGoldenResourceFromTargetResource(second).getIdElement().toUnqualifiedVersionless())
			.isEqualTo(getGoldenResourceFromTargetResource(first).getIdElement().toUnqualifiedVersionless());
	}

	/**
	 * The second EID system must be just as good a basis for linking as the first.
	 */
	@Test
	public void patientsSharingAnNpi_areLinkedToTheSameGoldenResource() {
		Patient first = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), npiSystem(), "npi-9"));
		Patient second = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), npiSystem(), "npi-9"));

		assertThat(getGoldenResourceFromTargetResource(second).getIdElement().toUnqualifiedVersionless())
			.isEqualTo(getGoldenResourceFromTargetResource(first).getIdElement().toUnqualifiedVersionless());
	}

	@Test
	public void patientCarryingBothEidsOfOneGoldenResource_producesASingleMatchAndNoDuplicate() {
		Patient first = addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1");
		addExternalEID(first, npiSystem(), "npi-9");
		first = createPatientAndUpdateLinks(first);

		Patient second = addExternalEID(buildPaulPatient(), mrnSystem(), "mrn-1");
		addExternalEID(second, npiSystem(), "npi-9");
		second = createPatientAndUpdateLinks(second);

		assertLinksMatchResult(MATCH, MATCH);
		assertThat(myMdmLinkDaoSvc.getPossibleDuplicates()).isEmpty();
		assertThat(getGoldenResourceFromTargetResource(second).getIdElement().toUnqualifiedVersionless())
			.isEqualTo(getGoldenResourceFromTargetResource(first).getIdElement().toUnqualifiedVersionless());
	}

	/**
	 * The diamond. An MRN and an NPI were each assigned to their own golden resource before anything
	 * revealed that both identify one person. The resource that carries both is linked to each golden
	 * resource as a POSSIBLE_MATCH, and the golden resources are flagged as possible duplicates so that a
	 * data steward can resolve them.
	 */
	@Test
	public void patientCarryingEidsOfTwoDifferentGoldenResources_createsPossibleMatchesAndAPossibleDuplicate() {
		Patient mrnOnly = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1"));
		Patient npiOnly = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), npiSystem(), "npi-9"));

		Patient carriesBoth = addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1");
		addExternalEID(carriesBoth, npiSystem(), "npi-9");
		createPatientAndUpdateLinks(carriesBoth);

		// Two MATCH links from the earlier creates, a POSSIBLE_MATCH to each golden resource, and the
		// POSSIBLE_DUPLICATE link between the two golden resources themselves.
		assertLinksMatchResult(MATCH, MATCH, POSSIBLE_MATCH, POSSIBLE_MATCH, POSSIBLE_DUPLICATE);

		List<MdmLink> possibleDuplicates = myMdmLinkDaoSvc.getPossibleDuplicates();
		assertThat(possibleDuplicates).hasSize(1);

		List<IResourcePersistentId<?>> goldenPids = runInTransaction(() -> Stream.of(mrnOnly, npiOnly)
			.map(t -> myIdHelperService.getPidOrNull(
				RequestPartitionId.allPartitions(), getGoldenResourceFromTargetResource(t)))
			.collect(Collectors.toList()));

		MdmLink duplicateLink = possibleDuplicates.get(0);
		assertThat(duplicateLink.getGoldenResourcePersistenceId()).isIn(goldenPids);
		assertThat(duplicateLink.getSourcePersistenceId()).isIn(goldenPids);
	}

	/**
	 * An MRN and an NPI that happen to read the same are not the same identifier.
	 */
	@Test
	public void patientsWhoseEidValuesCollideAcrossSystems_areNotLinked() {
		Patient mrnPatient = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), mrnSystem(), "123"));
		Patient npiPatient = createPatientAndUpdateLinks(addExternalEID(buildPaulPatient(), npiSystem(), "123"));

		assertThat(getGoldenResourceFromTargetResource(npiPatient).getIdElement().toUnqualifiedVersionless())
			.isNotEqualTo(getGoldenResourceFromTargetResource(mrnPatient).getIdElement().toUnqualifiedVersionless());
		assertThat(myMdmLinkDaoSvc.getPossibleDuplicates()).isEmpty();
	}

	@Test
	public void goldenResource_carriesTheEidsOfEveryConfiguredSystem() {
		Patient patient = addExternalEID(buildJanePatient(), mrnSystem(), "mrn-1");
		addExternalEID(patient, npiSystem(), "npi-9");
		patient = createPatientAndUpdateLinks(patient);

		List<CanonicalEID> goldenEids = myEIDHelper.getExternalEid(getGoldenResourceFromTargetResource(patient));

		assertThat(goldenEids).extracting(CanonicalEID::getSystemAndValueKey)
			.containsExactlyInAnyOrder(mrnSystem() + "|mrn-1", npiSystem() + "|npi-9");
	}
}
