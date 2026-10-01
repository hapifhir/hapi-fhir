package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
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

	@Override
	@AfterEach
	public void after() throws IOException {
		myMdmSettings.setCertainMatchOnSameEid(true);
		super.after();
	}

	@Test
	public void getMatchedTargets_incomingEidCarriesAValue_matchesTheResourceSharingIt() {
		String eidSystem = patientEidSystems().get(0);
		Patient jane = createPatient(addExternalEID(buildJanePatient(), eidSystem, "eid-1"));
		createPatient(addExternalEID(buildPaulPatient(), eidSystem, "eid-2"));

		Patient incoming = createPatient(addExternalEID(buildFrankPatient(), eidSystem, "eid-1"));

		assertThat(eidMatchedIds(incoming)).containsExactly(versionlessId(jane));
	}

	/**
	 * An identifier carrying an EID system but no value is legal FHIR and identifies nobody, so it must
	 * match nothing. This test rules out the behaviour that results from searching on it anyway.
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

	/**
	 * When the incoming resource has the same EID as an existing resource, the existing resource is returned
	 * as a MATCH, even though the matching rules score the two resources as NO_MATCH.
	 * {@link MdmMatchFinderSvcImpl#getMatchedTargets} looks up resources with the same EID before it evaluates
	 * any rule, and returns early when it finds one, so the rules never run for the incoming resource.
	 * <p>
	 * This is the behaviour while {@link ca.uhn.fhir.mdm.api.IMdmSettings#isCertainMatchOnSameEid()} is
	 * {@code true}, the default. {@link #getMatchedTargets_certainMatchOnSameEidDisabled_doesNotMatchByTheRules()}
	 * uses the same two patients with the setting disabled.
	 * </p>
	 */
	@Test
	public void getMatchedTargets_certainMatchOnSameEidEnabled_rulesDoNotMatchEidIsSame_matchesOnTheEid() {
		String eidSystem = patientEidSystems().get(0);
		Patient existing = createPatient(addExternalEID(buildJanePatient(), eidSystem, "eid-1"));
		Patient incoming = createPatient(addExternalEID(buildFrankPatient(), eidSystem, "eid-1"));

		// The rules do not match patients as patients share only a family name
		assertThat(myMdmResourceMatcherSvc.getMatchResult(incoming, existing).getMatchResultEnum())
				.isEqualTo(MdmMatchResultEnum.NO_MATCH);

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", incoming, RequestPartitionId.allPartitions());

		assertThat(matches).hasSize(1);
		MatchedTarget match = matches.get(0);
		assertThat(match.getTarget().getIdElement().toUnqualifiedVersionless().getValue())
				.isEqualTo(versionlessId(existing));
		// certain match at full confidence, overriding the rules' NO_MATCH above
		assertThat(match.getMatchResult().getMatchResultEnum()).isEqualTo(MdmMatchResultEnum.MATCH);
		assertThat(match.getMatchResult().isMatch()).isTrue();
		assertThat(match.getMatchResult().isEidMatch()).isTrue();
		assertThat(match.getMatchResult().getScore()).isEqualTo(1.0);
		assertThat(match.getMatchResult().getVector()).isNull();
	}

	@Test
	public void getMatchedTargets_certainMatchOnSameEidDisabled_doesNotMatchByTheRules() {
		myMdmSettings.setCertainMatchOnSameEid(false);
		String eidSystem = patientEidSystems().get(0);
		Patient existing = createPatient(addExternalEID(buildJanePatient(), eidSystem, "eid-1"));
		Patient incoming = createPatient(addExternalEID(buildFrankPatient(), eidSystem, "eid-1"));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", incoming, RequestPartitionId.allPartitions());

		// The rules decide: the existing patient comes back as a candidate scored NO_MATCH, not as an EID match.
		assertThat(matches).hasSize(1);
		MatchedTarget match = matches.get(0);
		assertThat(match.getTarget().getIdElement().toUnqualifiedVersionless().getValue())
				.isEqualTo(versionlessId(existing));
		assertThat(match.getMatchResult().getMatchResultEnum()).isEqualTo(MdmMatchResultEnum.NO_MATCH);
		assertThat(match.getMatchResult().isEidMatch()).isFalse();
		assertThat(match.getMatchResult().getVector()).isNotNull();
	}

	@Test
	public void getMatchedTargets_certainMatchOnSameEidDisabled_matchedByTheRules() {
		myMdmSettings.setCertainMatchOnSameEid(false);
		String eidSystem = patientEidSystems().get(0);
		Patient existing = createPatient(addExternalEID(buildJanePatient(), eidSystem, "eid-1"));
		Patient incoming = createPatient(addExternalEID(buildJanePatient(), eidSystem, "eid-1"));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", incoming, RequestPartitionId.allPartitions());

		assertThat(matches).hasSize(1);
		MatchedTarget match = matches.get(0);
		assertThat(match.getTarget().getIdElement().toUnqualifiedVersionless().getValue())
				.isEqualTo(versionlessId(existing));
		assertThat(match.getMatchResult().getMatchResultEnum()).isEqualTo(MdmMatchResultEnum.MATCH);
		assertThat(match.getMatchResult().isEidMatch()).isFalse();
		assertThat(match.getMatchResult().getVector()).isNotNull();
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
