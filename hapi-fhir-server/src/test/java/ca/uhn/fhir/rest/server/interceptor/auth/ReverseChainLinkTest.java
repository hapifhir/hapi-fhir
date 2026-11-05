package ca.uhn.fhir.rest.server.interceptor.auth;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class ReverseChainLinkTest {

	@Test
	void fromParameterNames_singleHas_returnsOneLink() {
		Set<ReverseChainLink> links = ReverseChainLink.fromParameterNames(List.of("_has:Observation:subject:code"));
		assertThat(links).containsExactly(new ReverseChainLink("Observation", "subject", true));
	}

	@Test
	void fromParameterNames_nestedHas_returnsOneLinkPerLevel() {
		Set<ReverseChainLink> links = ReverseChainLink.fromParameterNames(
				List.of("_has:Observation:subject:_has:Encounter:reason-reference:_id"));
		assertThat(links)
				.containsExactly(
						new ReverseChainLink("Observation", "subject", true),
						new ReverseChainLink("Encounter", "reason-reference", false));
	}

	@Test
	void fromParameterNames_modifierOnInnerParameter_ignoresModifier() {
		Set<ReverseChainLink> links =
				ReverseChainLink.fromParameterNames(List.of("_has:Observation:subject:code:text"));
		assertThat(links).containsExactly(new ReverseChainLink("Observation", "subject", true));
	}

	@Test
	void fromParameterNames_multipleHasParameters_returnsDistinctLinks() {
		Set<ReverseChainLink> links = ReverseChainLink.fromParameterNames(List.of(
				"_has:Observation:subject:code", "_has:Condition:subject:code", "_has:Observation:subject:status"));
		assertThat(links)
				.containsExactly(
						new ReverseChainLink("Observation", "subject", true),
						new ReverseChainLink("Condition", "subject", true));
	}

	@Test
	void fromParameterNames_noHasParameter_returnsNoLinks() {
		Set<ReverseChainLink> links = ReverseChainLink.fromParameterNames(
				List.of("name", "_id", "subject:Patient.name", "_has", "_has:Observation"));
		assertThat(links).isEmpty();
	}
}
