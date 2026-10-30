package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
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
		setUseCurrentCodeSystemVersionWhenNamedVersionMissing(new JpaStorageSettings().isUseCurrentCodeSystemVersionWhenNamedVersionMissing());
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
		void read_defaultPolicyAndCodeInCurrentVersion_isDenied() {
			assertReadIsDenied("Observation/coded-a");
		}

		@Test
		void read_useCurrentVersionAndCodeInCurrentVersion_isAllowed() {
			setUseCurrentCodeSystemVersionWhenNamedVersionMissing(true);

			Observation result = myClient.read()
					.resource(Observation.class)
					.withId("Observation/coded-a")
					.execute();

			assertThat(result).isNotNull();
		}

		@Test
		void read_useCurrentVersionAndCodeOnlyListed_isDenied() {
			setUseCurrentCodeSystemVersionWhenNamedVersionMissing(true);

			assertReadIsDenied("Observation/coded-b");
		}
	}

	/**
	 * A block-list rule, {@code deny().read().withCodeInValueSet(...)} followed by an allow. With the current version used for a missing
	 * named version or not, a resource coded with a code the ValueSet lists is denied.
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
		void read_useCurrentVersionAndCodeInCurrentVersion_isDenied() {
			setUseCurrentCodeSystemVersionWhenNamedVersionMissing(true);

			assertReadIsDenied("Observation/coded-a");
		}

		@Test
		void read_useCurrentVersionAndCodeOnlyListed_isDenied() {
			setUseCurrentCodeSystemVersionWhenNamedVersionMissing(true);

			assertReadIsDenied("Observation/coded-b");
		}

		@Test
		void read_useCurrentVersionAndPreExpandedAndCodeOnlyListed_isDenied() {
			setUseCurrentCodeSystemVersionWhenNamedVersionMissing(true);
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
		void read_useCurrentVersionAndCodeInCurrentVersion_isDenied() {
			setUseCurrentCodeSystemVersionWhenNamedVersionMissing(true);

			assertReadIsDenied("Observation/coded-a");
		}

		@Test
		void read_useCurrentVersionAndCodeOnlyListed_isDenied() {
			setUseCurrentCodeSystemVersionWhenNamedVersionMissing(true);

			assertReadIsDenied("Observation/coded-b");
		}
	}

	private void registerRules(List<IAuthRule> theRules) {
		myServer.getRestfulServer().registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRules;
			}
		}.setValidationSupport(myValidationSupport));
	}

	private void assertReadIsDenied(String theId) {
		var request = myClient.read().resource(Observation.class).withId(theId);
		assertThatThrownBy(request::execute).isInstanceOf(ForbiddenOperationException.class);
	}

	private void setUseCurrentCodeSystemVersionWhenNamedVersionMissing(boolean theUseCurrentVersion) {
		myStorageSettings.setUseCurrentCodeSystemVersionWhenNamedVersionMissing(theUseCurrentVersion);
		myInMemoryTerminologyServerValidationSupport.setUseCurrentCodeSystemVersionWhenNamedVersionMissing(theUseCurrentVersion);
	}
}
