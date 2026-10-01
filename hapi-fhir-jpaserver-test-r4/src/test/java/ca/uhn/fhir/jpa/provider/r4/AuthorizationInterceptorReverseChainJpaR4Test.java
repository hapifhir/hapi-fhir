package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.matcher.AuthorizationSearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * A search with a <code>_has:T:...</code> parameter requires the caller to be allowed to read every
 * resource of type <code>T</code> the search can reach. A type-level read always qualifies. A read
 * limited to a compartment qualifies only when the search is limited to that compartment's owners and
 * <code>T</code> is linked through one of its compartment search parameters. Reads limited to specific
 * instances, a filter or a value set don't qualify.
 */
// Created by Claude Opus 5.5
public class AuthorizationInterceptorReverseChainJpaR4Test extends BaseResourceProviderR4Test {

	private static final String LOINC = "http://loinc.org";
	private static final String HIV_VIRAL_LOAD = "20447-9";
	private static final String GLUCOSE = "15074-8";
	private static final String HIV_VALUE_SET = "http://example.org/ValueSet/hiv-codes";

	private static final String HAS_HIV_OBSERVATION = "Patient?_has:Observation:subject:code=" + HIV_VIRAL_LOAD;
	private static final String HAS_ENCOUNTER_FOR_HIV_OBSERVATION =
		"Patient?_has:Observation:subject:_has:Encounter:reason-reference:_id=encA";

	@Autowired
	private SearchParamMatcher mySearchParamMatcher;

	private IIdType myPatientA;
	private IIdType myPatientB;
	private IIdType myObservationB;

	@BeforeEach
	void createData() {
		myPatientA = createPatient(withId("pA"), withFamily("Alpha"));
		myPatientB = createPatient(withId("pB"), withFamily("Bravo"));
		IIdType observationA = createObservation(
			withId("obsA"), withSubject(myPatientA), withObservationCode(LOINC, HIV_VIRAL_LOAD));
		myObservationB = createObservation(
			withId("obsB"), withSubject(myPatientB), withObservationCode(LOINC, GLUCOSE));
		createEncounter(withId("encA"), withReference("reasonReference", observationA));
	}

	@ParameterizedTest
	@EnumSource(value = SearchStyleEnum.class, names = {"GET", "POST"})
	void testHas_readPatientOnly_forbidden(SearchStyleEnum theSearchStyle) {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.build());

		assertSearchForbidden(HAS_HIV_OBSERVATION, theSearchStyle);
	}

	@ParameterizedTest
	@EnumSource(value = SearchStyleEnum.class, names = {"GET", "POST"})
	void testHas_readPatientAndObservationType_allowed(SearchStyleEnum theSearchStyle) {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().andThen()
			.build());

		assertSearchReturns(HAS_HIV_OBSERVATION, theSearchStyle, myPatientA);
	}

	@Test
	void testHas_readAllResources_allowed() {
		registerRules(new RuleBuilder()
			.allow().read().allResources().withAnyId().andThen()
			.build());

		assertSearchReturns(HAS_HIV_OBSERVATION, SearchStyleEnum.GET, myPatientA);
	}

	@Test
	void testHas_readObservationInOtherPatientCompartment_forbidden() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").inCompartment("Patient", myPatientB).andThen()
			.build());

		assertSearchForbidden(HAS_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testHas_readAllInOwnPatientCompartment_allowed() {
		registerRules(new RuleBuilder()
			.allow().read().allResources().inCompartment("Patient", myPatientA).andThen()
			.build());

		assertSearchReturns("Patient?_id=pA&_has:Observation:subject:code=" + HIV_VIRAL_LOAD, SearchStyleEnum.GET, myPatientA);
	}

	@Test
	void testHas_readObservationInOwnPatientCompartment_allowed() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").inCompartment("Patient", myPatientA).andThen()
			.build());

		assertSearchReturns("Patient?_id=pA&_has:Observation:subject:code=" + HIV_VIRAL_LOAD, SearchStyleEnum.GET, myPatientA);
	}

	@Test
	void testHas_readObservationInOwnPatientCompartmentWithoutIdFilter_forbidden() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").inCompartment("Patient", myPatientA).andThen()
			.build());

		assertSearchForbidden(HAS_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testHas_readObservationInOwnPatientCompartmentButIdFilterIncludesOtherPatient_forbidden() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").inCompartment("Patient", myPatientA).andThen()
			.build());

		assertSearchForbidden("Patient?_id=pA,pB&_has:Observation:subject:code=" + HIV_VIRAL_LOAD, SearchStyleEnum.GET);
	}

	/**
	 * <code>focus</code> doesn't place an Observation in the Patient compartment, so an Observation focused on
	 * <code>pA</code> can belong to another patient.
	 */
	@Test
	void testHas_readObservationInOwnPatientCompartmentButLinkOutsideCompartment_forbidden() {
		createObservation(
			withId("obsFocusA"),
			withSubject(myPatientB),
			withReference("focus", myPatientA),
			withObservationCode(LOINC, HIV_VIRAL_LOAD));
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").inCompartment("Patient", myPatientA).andThen()
			.build());

		assertSearchForbidden("Patient?_id=pA&_has:Observation:focus:code=" + HIV_VIRAL_LOAD, SearchStyleEnum.GET);
	}

	/**
	 * An Encounter is in a Patient's compartment through its own <code>subject</code>/<code>patient</code> field.
	 * <code>reason-reference</code> doesn't make it a member. So an Encounter whose reason is one of
	 * <code>myPatientA</code>'s Observations can belong to someone else, and nothing in the query limits whose Encounter
	 * it is. The inner level therefore needs a type-level read on Encounter, as granted in
	 * {@link #testNestedHas_readAllTraversedTypes_allowed()}.
	 */
	@Test
	void testNestedHas_readAllInOwnPatientCompartment_forbidden() {
		registerRules(new RuleBuilder()
			.allow().read().allResources().inCompartment("Patient", myPatientA).andThen()
			.build());

		assertSearchForbidden(
			"Patient?_id=pA&_has:Observation:subject:_has:Encounter:reason-reference:_id=encA", SearchStyleEnum.GET);
	}

	@Test
	void testHas_readSingleObservationInstance_forbidden() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().instance(myObservationB).andThen()
			.build());

		assertSearchForbidden(HAS_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testHas_readObservationTypeWithFilter_forbidden() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withAnyId()
			.withFilterTester("category=vital-signs").andThen()
			.build());

		assertSearchForbidden(HAS_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testHas_readAllResourcesButObservationDenied_forbidden() {
		registerRules(new RuleBuilder()
			.deny().read().resourcesOfType("Observation").withAnyId().andThen()
			.allow().read().allResources().withAnyId().andThen()
			.build());

		assertSearchForbidden(HAS_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testHas_allowAll_allowed() {
		registerRules(new RuleBuilder()
			.allowAll()
			.build());

		assertSearchReturns(HAS_HIV_OBSERVATION, SearchStyleEnum.GET, myPatientA);
	}

	@Test
	void testHas_defaultPolicyAllowWithNoObservationRule_allowed() {
		registerRules(PolicyEnum.ALLOW, new RuleBuilder()
			.deny().read().resourcesOfType("Practitioner").withAnyId().andThen()
			.build());

		assertSearchReturns(HAS_HIV_OBSERVATION, SearchStyleEnum.GET, myPatientA);
	}

	@Test
	void testHas_observationDeniedInOneCompartmentThenReadAll_forbidden() {
		registerRules(new RuleBuilder()
			.deny().read().resourcesOfType("Observation").inCompartment("Patient", myPatientB).andThen()
			.allow().read().allResources().withAnyId().andThen()
			.build());

		assertSearchForbidden(HAS_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testHas_readObservationWithCodeInValueSet_forbidden() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withCodeInValueSet("code", HIV_VALUE_SET).andThen()
			.build());

		assertSearchForbidden(HAS_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testHas_observationBlockedUnlessCodeInValueSetThenReadAll_forbidden() {
		registerRules(new RuleBuilder()
			.deny().read().resourcesOfType("Observation").withCodeNotInValueSet("code", HIV_VALUE_SET).andThen()
			.allow().read().allResources().withAnyId().andThen()
			.build());

		assertSearchForbidden(HAS_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testNestedHas_noReadOnInnerType_forbidden() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().andThen()
			.build());

		assertSearchForbidden(HAS_ENCOUNTER_FOR_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testNestedHas_readPatientOnly_forbidden() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.build());

		assertSearchForbidden(HAS_ENCOUNTER_FOR_HIV_OBSERVATION, SearchStyleEnum.GET);
	}

	@Test
	void testNestedHas_readAllTraversedTypes_allowed() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.allow().read().resourcesOfType("Observation").withAnyId().andThen()
			.allow().read().resourcesOfType("Encounter").withAnyId().andThen()
			.build());

		assertSearchReturns(HAS_ENCOUNTER_FOR_HIV_OBSERVATION, SearchStyleEnum.GET, myPatientA);
	}

	@Test
	void testNoHas_readPatientOnly_allowed() {
		registerRules(new RuleBuilder()
			.allow().read().resourcesOfType("Patient").withAnyId().andThen()
			.build());

		assertSearchReturns("Patient?family=Alpha", SearchStyleEnum.GET, myPatientA);
	}

	private void registerRules(List<IAuthRule> theRules) {
		registerRules(PolicyEnum.DENY, theRules);
	}

	private void registerRules(PolicyEnum theDefaultPolicy, List<IAuthRule> theRules) {
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor(theDefaultPolicy) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRules;
			}
		};
		interceptor.setAuthorizationSearchParamMatcher(new AuthorizationSearchParamMatcher(mySearchParamMatcher));
		myServer.getRestfulServer().registerInterceptor(interceptor);
	}

	private Bundle search(String theUrl, SearchStyleEnum theSearchStyle) {
		return myClient
			.search()
			.byUrl(theUrl)
			.usingStyle(theSearchStyle)
			.returnBundle(Bundle.class)
			.execute();
	}

	private void assertSearchReturns(String theUrl, SearchStyleEnum theSearchStyle, IIdType... theExpectedIds) {
		List<String> actualIds = search(theUrl, theSearchStyle).getEntry().stream()
			.map(entry -> entry.getResource().getIdElement().toUnqualifiedVersionless().getValue())
			.toList();
		List<String> expectedIds = Arrays.stream(theExpectedIds)
			.map(id -> id.toUnqualifiedVersionless().getValue())
			.toList();

		assertThat(actualIds).containsExactlyInAnyOrderElementsOf(expectedIds);
	}

	private void assertSearchForbidden(String theUrl, SearchStyleEnum theSearchStyle) {
		assertThatThrownBy(() -> search(theUrl, theSearchStyle))
			.isInstanceOf(ForbiddenOperationException.class);
	}
}
