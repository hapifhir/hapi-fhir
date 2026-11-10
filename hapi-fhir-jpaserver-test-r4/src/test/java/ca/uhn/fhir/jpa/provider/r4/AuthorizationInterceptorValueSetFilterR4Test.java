package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.matcher.AuthorizationSearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@code AuthorizationInterceptor} rules with a {@code withFilter(...)} search expression using {@code :in} /
 * {@code :not-in}, which are matched in memory. When the server cannot tell whether a code is in the ValueSet, because
 * it does not have the CodeSystem the ValueSet lists the code from or does not have the ValueSet, the filter does not
 * let the read through.
 */
// Created by Claude Opus 5.5
class AuthorizationInterceptorValueSetFilterR4Test extends BaseResourceProviderR4Test {
	private static final String KNOWN_CS_URL = "http://example.org/CodeSystem/loaded";
	private static final String KNOWN_VS_URL = "http://example.org/ValueSet/loaded";
	private static final String UNKNOWN_CS_URL = "http://example.org/CodeSystem/not-loaded";
	private static final String UNKNOWN_VS_URL = "http://example.org/ValueSet/not-loaded";
	private static final String MISSING_VS_URL = "http://example.org/ValueSet/not-stored";

	@Autowired
	private SearchParamMatcher mySearchParamMatcher;

	@BeforeEach
	void beforeCreateTerminologyAndObservations() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(KNOWN_CS_URL);
		codeSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem.addConcept().setCode("A");
		codeSystem.addConcept().setCode("B");
		myCodeSystemDao.create(codeSystem, mySrd);

		createValueSet("loaded", KNOWN_VS_URL, KNOWN_CS_URL);
		createValueSet("not-loaded", UNKNOWN_VS_URL, UNKNOWN_CS_URL);

		createObservation(withId("coded-a"), withObservationCode(KNOWN_CS_URL, "A"));
		createObservation(withId("coded-b"), withObservationCode(KNOWN_CS_URL, "B"));
		createObservation(withId("unknown-coded-a"), withObservationCode(UNKNOWN_CS_URL, "A"));
		createObservation(withId("unknown-coded-z"), withObservationCode(UNKNOWN_CS_URL, "Z"));
		myBatch2JobHelper.awaitNoJobsRunning();
	}

	@AfterEach
	void afterUnregisterInterceptor() {
		myInterceptorRegistry.unregisterInterceptorsIf(AuthorizationInterceptor.class::isInstance);
	}

	@Test
	void read_allowFilteredByNotInAndCodeSystemUnknown_isDenied() {
		registerRules(new RuleBuilder()
				.allow()
				.read()
				.resourcesOfType("Observation")
				.withFilter("code:not-in=" + UNKNOWN_VS_URL)
				.andThen()
				.build());

		assertReadIsDenied("Observation/unknown-coded-z");
	}

	@Test
	void read_allowFilteredByNotInAndValueSetMissing_isDenied() {
		registerRules(new RuleBuilder()
				.allow()
				.read()
				.resourcesOfType("Observation")
				.withFilter("code:not-in=" + MISSING_VS_URL)
				.andThen()
				.build());

		assertReadIsDenied("Observation/coded-b");
	}

	/**
	 * A deny rule's filter is matched against the resource a create sends. A read is checked before the resource is
	 * loaded, where a deny rule with a filter denies every read, so the {@code :in} cases use creates.
	 */
	@Test
	void create_denyFilteredByInAndCodeSystemUnknown_isDenied() {
		registerRules(denyCreateFilteredByIn(UNKNOWN_VS_URL));

		assertCreateIsDenied(UNKNOWN_CS_URL, "A");
	}

	/**
	 * Control for {@link #create_denyFilteredByInAndCodeSystemUnknown_isDenied}: over a CodeSystem the server has, the
	 * deny filter matches only codes in the ValueSet.
	 */
	@Test
	void create_denyFilteredByInAndCodeSystemKnown_deniesOnlyCodesInTheValueSet() {
		registerRules(denyCreateFilteredByIn(KNOWN_VS_URL));

		assertCreateIsAllowed(KNOWN_CS_URL, "B");
		assertCreateIsDenied(KNOWN_CS_URL, "A");
	}

	/**
	 * Control for the {@code :not-in} tests above: over a CodeSystem the server has, the filter allows a code outside the
	 * ValueSet and denies a code in it.
	 */
	@Test
	void read_allowFilteredByNotInAndCodeSystemKnown_allowsOnlyCodesOutsideTheValueSet() {
		registerRules(new RuleBuilder()
				.allow()
				.read()
				.resourcesOfType("Observation")
				.withFilter("code:not-in=" + KNOWN_VS_URL)
				.andThen()
				.build());

		assertReadIsAllowed("Observation/coded-b");
		assertReadIsDenied("Observation/coded-a");
	}

	/**
	 * Stores a ValueSet listing code {@code A} from the given CodeSystem
	 */
	private void createValueSet(String theId, String theUrl, String theCodeSystemUrl) {
		ValueSet valueSet = new ValueSet();
		valueSet.setId(theId);
		valueSet.setUrl(theUrl);
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.getCompose().addInclude().setSystem(theCodeSystemUrl).addConcept().setCode("A");
		myValueSetDao.update(valueSet, mySrd);
	}

	private void registerRules(List<IAuthRule> theRules) {
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRules;
			}
		}.setValidationSupport(myValidationSupport);
		interceptor.setAuthorizationSearchParamMatcher(new AuthorizationSearchParamMatcher(mySearchParamMatcher));
		myServer.getRestfulServer().registerInterceptor(interceptor);
	}

	private void assertReadIsAllowed(String theId) {
		Observation result = myClient.read().resource(Observation.class).withId(theId).execute();
		assertThat(result.getIdElement().toUnqualifiedVersionless().getValue()).isEqualTo(theId);
	}

	private void assertReadIsDenied(String theId) {
		var request = myClient.read().resource(Observation.class).withId(theId);
		assertThatThrownBy(request::execute).isInstanceOf(ForbiddenOperationException.class);
	}

	private static List<IAuthRule> denyCreateFilteredByIn(String theValueSetUrl) {
		return new RuleBuilder()
				.deny()
				.write()
				.resourcesOfType("Observation")
				.withFilter("code:in=" + theValueSetUrl)
				.andThen()
				.allow()
				.write()
				.resourcesOfType("Observation")
				.withAnyId()
				.andThen()
				.build();
	}

	private void assertCreateIsAllowed(String theSystem, String theCode) {
		MethodOutcome outcome = myClient.create().resource(newObservation(theSystem, theCode)).execute();
		assertThat(outcome.getCreated()).isTrue();
	}

	private void assertCreateIsDenied(String theSystem, String theCode) {
		var request = myClient.create().resource(newObservation(theSystem, theCode));
		assertThatThrownBy(request::execute).isInstanceOf(ForbiddenOperationException.class);
	}

	private static Observation newObservation(String theSystem, String theCode) {
		Observation observation = new Observation();
		observation.setStatus(Observation.ObservationStatus.FINAL);
		observation.getCode().addCoding().setSystem(theSystem).setCode(theCode);
		return observation;
	}
}
