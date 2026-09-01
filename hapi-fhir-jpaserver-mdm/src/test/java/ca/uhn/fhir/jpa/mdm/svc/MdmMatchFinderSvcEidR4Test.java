package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EID matching in {@link MdmMatchFinderSvcImpl}, which resolves EIDs against source resources - the
 * second of the two EID lookups in the linking pipeline, reached from
 * {@link ca.uhn.fhir.jpa.mdm.svc.candidate.FindCandidateByExampleSvc} once the golden-resource lookup
 * has come back empty, and directly from the {@code $mdm-match} operation.
 */
// Created by claude-opus-5
public class MdmMatchFinderSvcEidR4Test extends BaseMdmR4Test {

	@Autowired
	private IMdmMatchFinderSvc myMdmMatchFinderSvc;

	@Test
	public void getMatchedTargets_incomingEidCarriesAValue_matchesTheResourceSharingIt() {
		String eidSystem = patientEidSystems().get(0);
		Patient jane = createPatient(addExternalEID(buildJanePatient(), eidSystem, "eid-1"));
		createPatient(addExternalEID(buildPaulPatient(), eidSystem, "eid-2"));

		Patient incoming = createPatient(addExternalEID(buildFrankPatient(), eidSystem, "eid-1"));

		assertThat(eidMatchedIds(incoming)).containsExactly(versionlessId(jane));
	}

	/**
	 * An identifier carrying an EID system but no value is legal FHIR and identifies nobody. Searching on
	 * it would fall back to matching the EID system alone and return every resource in that system, each
	 * as a full MATCH at score 1.0.
	 */
	@Test
	public void getMatchedTargets_incomingEidCarriesNoValue_matchesNothingByEid() {
		String eidSystem = patientEidSystems().get(0);
		createPatient(addExternalEID(buildJanePatient(), eidSystem, "eid-1"));
		createPatient(addExternalEID(buildPaulPatient(), eidSystem, "eid-2"));

		Patient incoming = buildFrankPatient();
		incoming.addIdentifier().setSystem(eidSystem);
		incoming = createPatient(incoming);

		assertThat(eidMatchedIds(incoming)).isEmpty();
	}

	/**
	 * A valueless EID alongside a usable one contributes nothing rather than widening the search.
	 */
	@Test
	public void getMatchedTargets_valuelessEidAlongsideARealOne_matchesOnlyTheRealOne() {
		String eidSystem = patientEidSystems().get(0);
		Patient jane = createPatient(addExternalEID(buildJanePatient(), eidSystem, "eid-1"));
		createPatient(addExternalEID(buildPaulPatient(), eidSystem, "eid-2"));

		Patient incoming = addExternalEID(buildFrankPatient(), eidSystem, "eid-1");
		incoming.addIdentifier().setSystem(eidSystem);
		incoming = createPatient(incoming);

		assertThat(eidMatchedIds(incoming)).containsExactly(versionlessId(jane));
	}

	private List<String> eidMatchedIds(Patient theIncomingResource) {
		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", theIncomingResource, RequestPartitionId.allPartitions());
		return matches.stream()
				.filter(match -> match.getMatchResult().isEidMatch())
				.map(match -> match.getTarget().getIdElement().toUnqualifiedVersionless().getValue())
				.toList();
	}

	private String versionlessId(Patient thePatient) {
		return thePatient.getIdElement().toUnqualifiedVersionless().getValue();
	}
}
