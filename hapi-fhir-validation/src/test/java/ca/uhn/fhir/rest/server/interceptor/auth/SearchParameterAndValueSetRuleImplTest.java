package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Authorization rules built with {@code withCodeInValueSet} / {@code withCodeNotInValueSet}, evaluated against a real
 * in-memory terminology service. Lives in this module rather than beside the rule because that service is here.
 */
// Created by Claude Opus 5.5
class SearchParameterAndValueSetRuleImplTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final String CS_URL = "http://example.org/cs/colours";
	private static final String VS_URL = "http://example.org/vs/colours";

	@ParameterizedTest
	@CsvSource({
		"ALLOW_IN,     ALLOW",
		"ALLOW_NOT_IN, ALLOW",
		"DENY_IN,      DENY",
		"DENY_NOT_IN,  ALLOW"
	})
	void applyRulesAndReturnDecision_codeInValueSetOverLoadedCodeSystem_followsTheRule(
			CodeRuleEnum theRule, PolicyEnum theExpectedDecision) {
		// Setup
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addCodeSystem(newColoursCodeSystem("1.0"));
		prePopulated.addValueSet(newColoursValueSet(null));

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCodedRed(theRule, prePopulated);

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(theExpectedDecision);
	}

	/**
	 * A ValueSet listing its codes from a CodeSystem that is not loaded, or not at the version named, cannot establish
	 * membership, so every rule shape denies, as when the ValueSet cannot be validated at all (#8415)
	 */
	@ParameterizedTest
	@MethodSource("codeRulesAndUnresolvableIncludes")
	void applyRulesAndReturnDecision_valueSetOverUnresolvableCodeSystem_denies(
			CodeRuleEnum theRule, UnresolvableIncludeEnum theInclude) {
		// Setup
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		if (theInclude == UnresolvableIncludeEnum.UNINSTALLED_VERSION) {
			prePopulated.addCodeSystem(newColoursCodeSystem("1.0"));
			prePopulated.addValueSet(newColoursValueSet("2.0"));
		} else {
			prePopulated.addValueSet(newColoursValueSet(null));
		}

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCodedRed(theRule, prePopulated);

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
	}

	private static Stream<Arguments> codeRulesAndUnresolvableIncludes() {
		return Arrays.stream(CodeRuleEnum.values())
				.flatMap(rule -> Arrays.stream(UnresolvableIncludeEnum.values())
						.map(include -> Arguments.of(rule, include)));
	}

	private static AuthorizationInterceptor.Verdict readObservationCodedRed(
			CodeRuleEnum theRule, PrePopulatedValidationSupport thePrePopulated) {
		ValidationSupportChain validationSupport = new ValidationSupportChain(
				new DefaultProfileValidationSupport(ourCtx),
				thePrePopulated,
				new CommonCodeSystemsTerminologyService(ourCtx),
				new InMemoryTerminologyServerValidationSupport(ourCtx));
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRule.addTo(new RuleBuilder()).allowAll().build();
			}
		}.setValidationSupport(validationSupport);

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(ourCtx);
		requestDetails.setResourceName("Observation");
		Observation observation = new Observation();
		observation.setId(new IdType("Observation/10"));
		observation.getCode().addCoding().setSystem(CS_URL).setCode("red");

		return interceptor.applyRulesAndReturnDecision(
				RestOperationTypeEnum.READ,
				requestDetails,
				null,
				null,
				observation,
				Pointcut.SERVER_OUTGOING_RESPONSE);
	}

	private static CodeSystem newColoursCodeSystem(String theVersion) {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(CS_URL);
		cs.setVersion(theVersion);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.addConcept().setCode("red");
		return cs;
	}

	private static ValueSet newColoursValueSet(String theIncludeVersion) {
		ValueSet vs = new ValueSet();
		vs.setUrl(VS_URL);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude().setSystem(CS_URL);
		include.setVersion(theIncludeVersion);
		include.addConcept().setCode("red");
		return vs;
	}

	private enum UnresolvableIncludeEnum {
		UNKNOWN_SYSTEM,
		UNINSTALLED_VERSION
	}

	private enum CodeRuleEnum {
		ALLOW_IN,
		ALLOW_NOT_IN,
		DENY_IN,
		DENY_NOT_IN;

		IAuthRuleBuilder addTo(IAuthRuleBuilder theBuilder) {
			IAuthRuleBuilderRule rule =
					switch (this) {
						case ALLOW_IN, ALLOW_NOT_IN -> theBuilder.allow("Rule 1");
						case DENY_IN, DENY_NOT_IN -> theBuilder.deny("Rule 1");
					};
			IAuthRuleBuilderRuleOpClassifier observations = rule.read().resourcesOfType("Observation");
			IAuthRuleFinished finished =
					switch (this) {
						case ALLOW_IN, DENY_IN -> observations.withCodeInValueSet("code", VS_URL);
						case ALLOW_NOT_IN, DENY_NOT_IN -> observations.withCodeNotInValueSet("code", VS_URL);
					};
			return finished.andThen();
		}
	}
}
