package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.support.MissingCodeSystemPolicy;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.AuthorizationSearchParamMatcher;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@code AuthorizationInterceptor} rules using {@code withCodeInValueSet} over a ValueSet that lists codes {@code A}
 * and {@code B} from CodeSystem version 0.0.1, which the server does not have, while version 1.0.0 is installed
 * holding code {@code A}.
 */
// Created by Claude Opus 5.5
class AuthorizationInterceptorCodeSystemVersionMismatchR4Test extends BaseResourceProviderR4Test {
	private static final String CS_URL = "http://example.org/CodeSystem/missing-version";
	private static final String VS_URL = "http://example.org/ValueSet/missing-version";
	private static final String UNKNOWN_CS_URL = "http://example.org/CodeSystem/not-loaded";
	private static final String UNKNOWN_VS_URL = "http://example.org/ValueSet/not-loaded";

	@Autowired
	private InMemoryTerminologyServerValidationSupport myInMemoryTerminologyServerValidationSupport;

	private ValueSet myValueSet;

	@BeforeEach
	void beforeCreateTerminologyAndObservations() {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(CS_URL);
		cs.setVersion("1.0.0");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("A");
		myCodeSystemDao.create(cs, mySrd);

		myValueSet = new ValueSet();
		myValueSet.setId("missing-version");
		myValueSet.setUrl(VS_URL);
		myValueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent include =
				myValueSet.getCompose().addInclude().setSystem(CS_URL).setVersion("0.0.1");
		include.addConcept().setCode("A");
		include.addConcept().setCode("B");
		myValueSetDao.update(myValueSet, mySrd);

		createObservation(withId("coded-a"), withObservationCode(CS_URL, "A"));
		createObservation(withId("coded-b"), withObservationCode(CS_URL, "B"));
		myBatch2JobHelper.awaitNoJobsRunning();
	}

	@AfterEach
	void afterResetPolicy() {
		myInterceptorRegistry.unregisterInterceptorsIf(AuthorizationInterceptor.class::isInstance);
		setMissingCodeSystemPolicy(new JpaStorageSettings().getMissingCodeSystemPolicy());
	}

	/**
	 * An allow-list rule, {@code allow().read().withCodeInValueSet(...)}.
	 */
	@Nested
	class AllowList {

		@BeforeEach
		void beforeRegisterRules() {
			registerRules(new RuleBuilder()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withCodeInValueSet("code", VS_URL)
					.andThen()
					.build());
		}

		@Test
		void read_defaultPolicyAndCodeInInstalledVersion_isDenied() {
			assertReadIsDenied("Observation/coded-a");
		}

		@Test
		void read_useInstalledVersionAndCodeInInstalledVersion_isAllowed() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);

			Observation result = myClient.read()
					.resource(Observation.class)
					.withId("Observation/coded-a")
					.execute();

			assertThat(result).isNotNull();
		}

		@Test
		void read_useInstalledVersionAndCodeOnlyListed_isDenied() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);

			assertReadIsDenied("Observation/coded-b");
		}
	}

	/**
	 * A block-list rule, {@code deny().read().withCodeInValueSet(...)} followed by an allow. Under every
	 * {@link MissingCodeSystemPolicy}, a resource coded with a code the ValueSet lists is denied.
	 */
	@Nested
	class BlockList {

		@BeforeEach
		void beforeRegisterRules() {
			registerRules(new RuleBuilder()
					.deny()
					.read()
					.resourcesOfType("Observation")
					.withCodeInValueSet("code", VS_URL)
					.andThen()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withAnyId()
					.andThen()
					.build());
		}

		@Test
		void read_defaultPolicyAndCodeOnlyListed_isDenied() {
			assertReadIsDenied("Observation/coded-b");
		}

		@Test
		void read_useInstalledVersionAndCodeInInstalledVersion_isDenied() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);

			assertReadIsDenied("Observation/coded-a");
		}

		@Test
		void read_useInstalledVersionAndCodeOnlyListed_isDenied() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);

			assertReadIsDenied("Observation/coded-b");
		}

		@Test
		void read_useInstalledVersionAndPreExpandedAndCodeOnlyListed_isDenied() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);
			// Saving the ValueSet again queues a new pre-expansion, which now runs under the policy
			myValueSet.setName("MissingVersion");
			myValueSetDao.update(myValueSet, mySrd);
			myTerminologyDeferredStorageSvc.saveAllDeferred();
			myBatch2JobHelper.awaitNoJobsRunning();
			runInTransaction(() -> assertThat(myTermValueSetDao
							.findTermValueSetByUrlAndNullVersion(VS_URL)
							.orElseThrow()
							.getExpansionStatus())
					.isEqualTo(TermValueSetPreExpansionStatusEnum.EXPANDED));

			assertReadIsDenied("Observation/coded-b");
		}
	}

	/**
	 * The block-list rule over a ValueSet whose only include is {@code include.valueSet} naming the ValueSet above.
	 */
	@Nested
	class BlockListThroughIncludedValueSet {
		private static final String OUTER_VS_URL = "http://example.org/ValueSet/includes-missing-version";

		@BeforeEach
		void beforeCreateOuterValueSetAndRegisterRules() {
			ValueSet outer = new ValueSet();
			outer.setId("includes-missing-version");
			outer.setUrl(OUTER_VS_URL);
			outer.setStatus(Enumerations.PublicationStatus.ACTIVE);
			outer.getCompose().addInclude().addValueSet(VS_URL);
			myValueSetDao.update(outer, mySrd);
			myBatch2JobHelper.awaitNoJobsRunning();

			registerRules(new RuleBuilder()
					.deny()
					.read()
					.resourcesOfType("Observation")
					.withCodeInValueSet("code", OUTER_VS_URL)
					.andThen()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withAnyId()
					.andThen()
					.build());
		}

		@Test
		void read_useInstalledVersionAndCodeInInstalledVersion_isDenied() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);

			assertReadIsDenied("Observation/coded-a");
		}

		@Test
		void read_useInstalledVersionAndCodeOnlyListed_isDenied() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.USE_INSTALLED_VERSION);

			assertReadIsDenied("Observation/coded-b");
		}
	}

	/**
	 * The allow-list rule over a ValueSet listing code {@code A} from a CodeSystem the server does not have in any
	 * version. Under {@link MissingCodeSystemPolicy#ACCEPT_LISTED_CODES} the listed code is taken as a member.
	 */
	@Nested
	class AllowListOverUnknownCodeSystem {

		@BeforeEach
		void beforeCreateValueSetAndRegisterRules() {
			createValueSetAndObservationsOverUnknownCodeSystem();
			registerRules(new RuleBuilder()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withCodeInValueSet("code", UNKNOWN_VS_URL)
					.andThen()
					.build());
		}

		@Test
		void read_defaultPolicyAndListedCode_isDenied() {
			assertReadIsDenied("Observation/unknown-coded-a");
		}

		@Test
		void read_acceptListedCodesAndListedCode_isAllowed() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);

			assertReadIsAllowed("Observation/unknown-coded-a");
		}

		@Test
		void read_acceptListedCodesAndCodeNotListed_isDenied() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);

			assertReadIsDenied("Observation/unknown-coded-z");
		}
	}

	/**
	 * A block-list rule denying codes outside the ValueSet, {@code deny().read().withCodeNotInValueSet(...)} followed
	 * by an allow, over the ValueSet of {@link AllowListOverUnknownCodeSystem}.
	 */
	@Nested
	class BlockListOfCodesNotInValueSetOverUnknownCodeSystem {

		@BeforeEach
		void beforeCreateValueSetAndRegisterRules() {
			createValueSetAndObservationsOverUnknownCodeSystem();
			registerRules(new RuleBuilder()
					.deny()
					.read()
					.resourcesOfType("Observation")
					.withCodeNotInValueSet("code", UNKNOWN_VS_URL)
					.andThen()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withAnyId()
					.andThen()
					.build());
		}

		@Test
		void read_defaultPolicyAndListedCode_isDenied() {
			assertReadIsDenied("Observation/unknown-coded-a");
		}

		@Test
		void read_acceptListedCodesAndListedCode_isAllowed() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);

			assertReadIsAllowed("Observation/unknown-coded-a");
		}

		@Test
		void read_acceptListedCodesAndCodeNotListed_isDenied() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);

			assertReadIsDenied("Observation/unknown-coded-z");
		}
	}

	/**
	 * A block-list rule denying codes in the ValueSet, {@code deny().read().withCodeInValueSet(...)} followed by an
	 * allow, over the ValueSet of {@link AllowListOverUnknownCodeSystem}.
	 */
	@Nested
	class BlockListOfCodesInValueSetOverUnknownCodeSystem {

		@BeforeEach
		void beforeCreateValueSetAndRegisterRules() {
			createValueSetAndObservationsOverUnknownCodeSystem();
			registerRules(new RuleBuilder()
					.deny()
					.read()
					.resourcesOfType("Observation")
					.withCodeInValueSet("code", UNKNOWN_VS_URL)
					.andThen()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withAnyId()
					.andThen()
					.build());
		}

		@Test
		void read_defaultPolicyAndCodeNotListed_isDenied() {
			assertReadIsDenied("Observation/unknown-coded-z");
		}

		@Test
		void read_acceptListedCodesAndListedCode_isDenied() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);

			assertReadIsDenied("Observation/unknown-coded-a");
		}

		@Test
		void read_acceptListedCodesAndCodeNotListed_isAllowed() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);

			assertReadIsAllowed("Observation/unknown-coded-z");
		}
	}

	/**
	 * Rules with a {@code withFilter(...)} search expression using {@code :in} / {@code :not-in}, which are matched in
	 * memory. A code whose CodeSystem the server does not have is neither inside nor outside the ValueSet, so the
	 * filter does not let the read through.
	 */
	@Nested
	class FilterOverUnknownCodeSystem {
		private static final String KNOWN_VS_URL = "http://example.org/ValueSet/installed-version";

		@Autowired
		private SearchParamMatcher mySearchParamMatcher;

		@BeforeEach
		void beforeCreateValueSets() {
			createValueSetAndObservationsOverUnknownCodeSystem();
			ValueSet knownValueSet = new ValueSet();
			knownValueSet.setId("installed-version");
			knownValueSet.setUrl(KNOWN_VS_URL);
			knownValueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
			knownValueSet.getCompose().addInclude().setSystem(CS_URL).addConcept().setCode("A");
			myValueSetDao.update(knownValueSet, mySrd);
			myBatch2JobHelper.awaitNoJobsRunning();
		}

		@Test
		void read_allowFilteredByNotInAndCodeSystemUnknown_isDenied() {
			registerRulesWithSearchParamMatcher(new RuleBuilder()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withFilter("code:not-in=" + UNKNOWN_VS_URL)
					.andThen()
					.build());

			assertReadIsDenied("Observation/unknown-coded-z");
		}

		@Test
		void read_denyFilteredByInAndCodeSystemUnknown_isDenied() {
			registerRulesWithSearchParamMatcher(new RuleBuilder()
					.deny()
					.read()
					.resourcesOfType("Observation")
					.withFilter("code:in=" + UNKNOWN_VS_URL)
					.andThen()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withAnyId()
					.andThen()
					.build());

			assertReadIsDenied("Observation/unknown-coded-a");
		}

		/**
		 * Under {@link MissingCodeSystemPolicy#ACCEPT_LISTED_CODES} the listed code is a member, so the filter
		 * decides for it; a code the ValueSet does not list is still not let through.
		 */
		// Created by Claude Opus 5.5
		@Test
		void read_allowFilteredByInAndAcceptListedCodes_allowsOnlyTheListedCode() {
			setMissingCodeSystemPolicy(MissingCodeSystemPolicy.ACCEPT_LISTED_CODES);
			registerRulesWithSearchParamMatcher(new RuleBuilder()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withFilter("code:in=" + UNKNOWN_VS_URL)
					.andThen()
					.build());

			assertReadIsAllowed("Observation/unknown-coded-a");
			assertReadIsDenied("Observation/unknown-coded-z");
		}

		/**
		 * Control for the tests above: over a CodeSystem the server has, the filter decides as before.
		 */
		@Test
		void read_allowFilteredByNotInAndCodeSystemKnown_allowsOnlyCodesOutsideTheValueSet() {
			registerRulesWithSearchParamMatcher(new RuleBuilder()
					.allow()
					.read()
					.resourcesOfType("Observation")
					.withFilter("code:not-in=" + KNOWN_VS_URL)
					.andThen()
					.build());

			assertReadIsAllowed("Observation/coded-b");
			assertReadIsDenied("Observation/coded-a");
		}

		private void registerRulesWithSearchParamMatcher(List<IAuthRule> theRules) {
			AuthorizationInterceptor interceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
				@Override
				public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
					return theRules;
				}
			}.setValidationSupport(myValidationSupport);
			interceptor.setAuthorizationSearchParamMatcher(new AuthorizationSearchParamMatcher(mySearchParamMatcher));
			myServer.getRestfulServer().registerInterceptor(interceptor);
		}
	}

	/**
	 * Stores a ValueSet listing code {@code A} from a CodeSystem the server does not have, and Observations coded
	 * {@code A} and {@code Z} from it
	 */
	private void createValueSetAndObservationsOverUnknownCodeSystem() {
		ValueSet vs = new ValueSet();
		vs.setId("not-loaded");
		vs.setUrl(UNKNOWN_VS_URL);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem(UNKNOWN_CS_URL).addConcept().setCode("A");
		myValueSetDao.update(vs, mySrd);
		createObservation(withId("unknown-coded-a"), withObservationCode(UNKNOWN_CS_URL, "A"));
		createObservation(withId("unknown-coded-z"), withObservationCode(UNKNOWN_CS_URL, "Z"));
		myBatch2JobHelper.awaitNoJobsRunning();
	}

	private void registerRules(List<IAuthRule> theRules) {
		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRules;
			}
		}.setValidationSupport(myValidationSupport));
	}

	private void assertReadIsAllowed(String theId) {
		Observation result =
				myClient.read().resource(Observation.class).withId(theId).execute();
		assertThat(result).isNotNull();
	}

	private void assertReadIsDenied(String theId) {
		var request = myClient.read().resource(Observation.class).withId(theId);
		assertThatThrownBy(request::execute).isInstanceOf(ForbiddenOperationException.class);
	}

	private void setMissingCodeSystemPolicy(MissingCodeSystemPolicy thePolicy) {
		myStorageSettings.setMissingCodeSystemPolicy(thePolicy);
		myInMemoryTerminologyServerValidationSupport.setMissingCodeSystemPolicy(thePolicy);
	}
}
