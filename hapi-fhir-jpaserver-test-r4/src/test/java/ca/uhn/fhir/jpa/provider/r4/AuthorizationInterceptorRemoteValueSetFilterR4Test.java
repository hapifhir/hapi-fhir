package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.matcher.AuthorizationSearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRuleBuilderRuleOpClassifier;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRuleFinished;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProviders;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersR4;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.OPERATION_VALIDATE_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@code AuthorizationInterceptor} rules allowing reads of codes not in a ValueSet the server checks codes against on a
 * remote terminology server, through {@code withFilter("code:not-in=...")} or {@code withCodeNotInValueSet}. When the
 * remote rejects {@code $validate-code} with HTTP 400 or 404, the server cannot tell whether the code is in the
 * ValueSet, so the rule does not let the read through.
 */
// Created by Claude Opus 5.5
class AuthorizationInterceptorRemoteValueSetFilterR4Test extends BaseResourceProviderR4Test {
	private static final String REMOTE_CS_URL = "http://example.org/cs/restricted";
	private static final String REMOTE_VS_URL = "http://example.org/vs/restricted";
	private static final String UNLOADED_CS_URL = "http://example.org/cs/unloaded";
	private static final String UNLOADED_CODE = "x1";
	private static final String OBSERVATION_ID = "Observation/unloaded-coded";
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	static RestfulServerExtension ourRemoteServer = new RestfulServerExtension(ourCtx);

	@Autowired
	private SearchParamMatcher mySearchParamMatcher;

	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT_CHAIN)
	private ValidationSupportChain myValidationSupportChain;

	private RemoteTerminologyServiceValidationSupport myRemoteSvc;
	private IValidationProviders.MyValidationProvider<CodeSystem> myRemoteCodeSystemProvider;
	private IValidationProviders.MyValidationProvider<ValueSet> myRemoteValueSetProvider;

	@BeforeEach
	void beforeRegisterRemoteAndCreateObservation() {
		myRemoteCodeSystemProvider = new IValidationProvidersR4.MyCodeSystemProviderR4();
		myRemoteValueSetProvider = new IValidationProvidersR4.MyValueSetProviderR4();
		myRemoteCodeSystemProvider.setShouldThrowExceptionForResourceNotFound(false);
		myRemoteValueSetProvider.setShouldThrowExceptionForResourceNotFound(false);
		ValueSet valueSet = myRemoteValueSetProvider.addTerminologyResource(REMOTE_VS_URL);
		valueSet.getCompose().addInclude().setSystem(REMOTE_CS_URL).addConcept().setCode("r1");
		ourRemoteServer.registerProvider(myRemoteCodeSystemProvider);
		ourRemoteServer.registerProvider(myRemoteValueSetProvider);

		myRemoteSvc = new RemoteTerminologyServiceValidationSupport(
				ourCtx, "http://localhost:" + ourRemoteServer.getPort());
		myValidationSupportChain.addValidationSupport(0, myRemoteSvc);

		createObservation(withId("unloaded-coded"), withObservationCode(UNLOADED_CS_URL, UNLOADED_CODE));
	}

	@AfterEach
	void afterUnregisterRemoteAndInterceptor() {
		myInterceptorRegistry.unregisterInterceptorsIf(AuthorizationInterceptor.class::isInstance);
		myValidationSupportChain.removeValidationSupport(myRemoteSvc);
		ourRemoteServer.unregisterProvider(myRemoteCodeSystemProvider);
		ourRemoteServer.unregisterProvider(myRemoteValueSetProvider);
	}

	@ParameterizedTest
	@CsvSource({
		"FILTER,                 404",
		"FILTER,                 400",
		"CODE_NOT_IN_VALUE_SET,  404",
		"CODE_NOT_IN_VALUE_SET,  400"
	})
	void read_allowedByNotInAndRemoteRejectsValidateCode_isDenied(NotInRule theRule, int theStatusCode) {
		registerRules(theRule.build());
		myRemoteValueSetProvider.addException(
				OPERATION_VALIDATE_CODE,
				REMOTE_VS_URL,
				UNLOADED_CODE,
				BaseServerResponseException.newInstance(theStatusCode, "Simulated rejection"));

		assertReadIsDenied(OBSERVATION_ID);
	}

	/**
	 * Control for {@link #read_allowedByNotInAndRemoteRejectsValidateCode_isDenied}: a remote answer that the code is
	 * not in the ValueSet lets the read through.
	 */
	@ParameterizedTest
	@EnumSource(NotInRule.class)
	void read_allowedByNotInAndRemoteAnswersNotAMember_isAllowed(NotInRule theRule) {
		registerRules(theRule.build());
		givenRemoteValidateCodeAnswers(false);

		assertReadIsAllowed(OBSERVATION_ID);
	}

	/**
	 * Control for {@link #read_allowedByNotInAndRemoteRejectsValidateCode_isDenied}: a remote answer that the code is in
	 * the ValueSet denies the read.
	 */
	@ParameterizedTest
	@EnumSource(NotInRule.class)
	void read_allowedByNotInAndRemoteAnswersMember_isDenied(NotInRule theRule) {
		registerRules(theRule.build());
		givenRemoteValidateCodeAnswers(true);

		assertReadIsDenied(OBSERVATION_ID);
	}

	private void givenRemoteValidateCodeAnswers(boolean theResult) {
		Parameters response = new Parameters().addParameter("result", theResult);
		if (!theResult) {
			response.addParameter("message", "Code " + UNLOADED_CODE + " is not in the ValueSet");
		}
		myRemoteValueSetProvider.addTerminologyResponse(
				OPERATION_VALIDATE_CODE, REMOTE_VS_URL, UNLOADED_CODE, response);
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

	/**
	 * The two ways to allow reading Observations whose code is not in the remote ValueSet: a {@code withFilter}
	 * expression, matched by the {@code InMemoryResourceMatcher}, and {@code withCodeNotInValueSet}, matched by the
	 * rule itself
	 */
	private enum NotInRule {
		FILTER,
		CODE_NOT_IN_VALUE_SET;

		List<IAuthRule> build() {
			IAuthRuleBuilderRuleOpClassifier observations =
					new RuleBuilder().allow().read().resourcesOfType("Observation");
			IAuthRuleFinished finished =
					switch (this) {
						case FILTER -> observations.withFilter("code:not-in=" + REMOTE_VS_URL);
						case CODE_NOT_IN_VALUE_SET -> observations.withCodeNotInValueSet("code", REMOTE_VS_URL);
					};
			return finished.andThen().build();
		}
	}
}
