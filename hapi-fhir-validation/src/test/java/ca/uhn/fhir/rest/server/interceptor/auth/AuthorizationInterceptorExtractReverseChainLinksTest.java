package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.ReverseChainLink;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class AuthorizationInterceptorExtractReverseChainLinksTest {

	@Test
	void extractReverseChainLinks_singleHas_returnsOneLink() {
		assertThat(AuthorizationInterceptor.extractReverseChainLinks(
				List.of("_has:Observation:subject:code")))
			.containsExactly(new ReverseChainLink("Observation", "subject", true));
	}

	@Test
	void extractReverseChainLinks_nestedHas_returnsOneLinkPerLevel() {
		assertThat(AuthorizationInterceptor.extractReverseChainLinks(
				List.of("_has:Observation:subject:_has:Encounter:reason-reference:_id")))
			.containsExactly(
				new ReverseChainLink("Observation", "subject", true),
				new ReverseChainLink("Encounter", "reason-reference", false));
	}

	@Test
	void extractReverseChainLinks_modifierOnInnerParameter_ignoresModifier() {
		assertThat(AuthorizationInterceptor.extractReverseChainLinks(
				List.of("_has:Observation:subject:code:text")))
			.containsExactly(new ReverseChainLink("Observation", "subject", true));
	}

	@Test
	void extractReverseChainLinks_multipleHasParameters_returnsDistinctLinks() {
		assertThat(AuthorizationInterceptor.extractReverseChainLinks(
				List.of("_has:Observation:subject:code", "_has:Condition:subject:code", "_has:Observation:subject:status")))
			.containsExactly(
				new ReverseChainLink("Observation", "subject", true),
				new ReverseChainLink("Condition", "subject", true));
	}

	@Test
	void extractReverseChainLinks_noHasParameter_returnsNoLinks() {
		assertThat(AuthorizationInterceptor.extractReverseChainLinks(
				List.of("name", "_id", "subject:Patient.name", "_has", "_has:Observation")))
			.isEmpty();
	}
}
