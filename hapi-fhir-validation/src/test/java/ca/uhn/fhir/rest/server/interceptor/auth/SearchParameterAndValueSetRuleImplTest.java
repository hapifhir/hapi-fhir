package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersR4;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

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
	private static final String INCLUDED_VS_URL = "http://example.org/vs/colours-included";

	@RegisterExtension
	static RestfulServerExtension ourRemoteTerminologyServer = new RestfulServerExtension(ourCtx);

	/**
	 * The troubleshooting log of the {@code AuthorizationInterceptor}, where a rule says it denied because a code's
	 * membership could not be decided
	 */
	@RegisterExtension
	final LogbackTestExtension myAuthorizationLog = new LogbackTestExtension(AuthorizationInterceptor.class.getName());

	private IValidationProvidersR4.MyValueSetProviderR4 myRemoteValueSets;

	@BeforeEach
	void before() {
		myRemoteValueSets = new IValidationProvidersR4.MyValueSetProviderR4();
		ourRemoteTerminologyServer.getRestfulServer().registerProvider(myRemoteValueSets);
	}

	@AfterEach
	void after() {
		ourRemoteTerminologyServer.getRestfulServer().unregisterProvider(myRemoteValueSets);
	}

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
	 * A ValueSet listing its codes from a CodeSystem that is not loaded cannot establish membership, so every rule
	 * shape denies, as when the ValueSet cannot be validated at all (#8415)
	 */
	@ParameterizedTest
	@EnumSource(CodeRuleEnum.class)
	void applyRulesAndReturnDecision_valueSetOverUnknownCodeSystem_denies(CodeRuleEnum theRule) {
		// Setup
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addValueSet(newColoursValueSet(null));

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCodedRed(theRule, prePopulated);

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
	}

	/**
	 * A code the ValueSet does not list is not a member whatever the CodeSystem contains, but a ValueSet code rule
	 * still denies when the ValueSet uses a CodeSystem that is not loaded, for every rule shape, as for a listed code
	 * (#8415)
	 */
	// Created by Claude Opus 5.5
	@ParameterizedTest
	@EnumSource(CodeRuleEnum.class)
	void applyRulesAndReturnDecision_unlistedCodeAndValueSetOverUnknownCodeSystem_denies(CodeRuleEnum theRule) {
		// Setup
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addValueSet(newColoursValueSet(null));

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCoded(theRule, newChain(prePopulated), "blue");

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
	}

	/**
	 * A ValueSet listing its codes from a CodeSystem version that is not installed cannot establish membership either,
	 * so every rule shape denies (#8415)
	 */
	@ParameterizedTest
	@EnumSource(CodeRuleEnum.class)
	void applyRulesAndReturnDecision_valueSetOverUninstalledCodeSystemVersion_denies(CodeRuleEnum theRule) {
		// Setup
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addCodeSystem(newColoursCodeSystem("1.0"));
		prePopulated.addValueSet(newColoursValueSet("2.0"));

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCodedRed(theRule, prePopulated);

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
	}

	/**
	 * As {@link #applyRulesAndReturnDecision_valueSetOverUninstalledCodeSystemVersion_denies}, with the version written
	 * into {@code include.system} as {@code url|version} and a code the ValueSet does not list
	 */
	@ParameterizedTest
	@EnumSource(CodeRuleEnum.class)
	void applyRulesAndReturnDecision_unlistedCodeAndIncludeSystemNamesUninstalledVersion_denies(CodeRuleEnum theRule) {
		// Setup
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addCodeSystem(newColoursCodeSystem("1.0"));
		ValueSet valueSet = newColoursValueSet(null);
		valueSet.getCompose().getIncludeFirstRep().setSystem(CS_URL + "|2.0");
		prePopulated.addValueSet(valueSet);

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCoded(theRule, newChain(prePopulated), "blue");

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
	}

	/**
	 * A ValueSet hosted on a remote terminology server whose {@code $validate-code} answer names the code system it
	 * does not support cannot establish membership either, so every rule shape denies (#8415)
	 */
	@ParameterizedTest
	@EnumSource(CodeRuleEnum.class)
	void applyRulesAndReturnDecision_remoteValueSetOverCodeSystemTheRemoteDoesNotSupport_denies(
			CodeRuleEnum theRule) {
		// Setup
		myRemoteValueSets.addTerminologyResource(VS_URL);
		Parameters response = new Parameters();
		response.addParameter("result", false);
		response.addParameter("message", "A definition for CodeSystem '" + CS_URL + "' could not be found");
		response.addParameter(
				IValidationSupport.CodeValidationResult.CAUSED_BY_UNKNOWN_SYSTEM, new CanonicalType(CS_URL));
		myRemoteValueSets.addTerminologyResponse(JpaConstants.OPERATION_VALIDATE_CODE, VS_URL, "red", response);
		ValidationSupportChain validationSupport = newRemoteChain();

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCodedRed(theRule, validationSupport);

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
	}

	/**
	 * A remote terminology server that rejects {@code $validate-code} with HTTP 400 or 404 has not checked the code,
	 * so membership cannot be established and every rule shape denies
	 */
	// Created by Claude Opus 5.5
	@ParameterizedTest
	@CsvSource({
		"ALLOW_IN,     404",
		"ALLOW_IN,     400",
		"ALLOW_NOT_IN, 404",
		"ALLOW_NOT_IN, 400",
		"DENY_IN,      404",
		"DENY_IN,      400",
		"DENY_NOT_IN,  404",
		"DENY_NOT_IN,  400"
	})
	void applyRulesAndReturnDecision_remoteRejectsValidateCode_denies(CodeRuleEnum theRule, int theStatusCode) {
		// Setup
		myRemoteValueSets.addTerminologyResource(VS_URL);
		myRemoteValueSets.addException(
				JpaConstants.OPERATION_VALIDATE_CODE,
				VS_URL,
				"red",
				BaseServerResponseException.newInstance(theStatusCode, "Simulated rejection"));

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCodedRed(theRule, newRemoteChain());

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
		// a deny not-in rule would deny a code outside the ValueSet too, so only the log shows which path denied
		assertThat(isUndecidedDenyLogged()).isTrue();
	}

	/**
	 * A code whose membership could not be decided only denies when the other code does not settle the rule, so the
	 * verdict does not depend on the order of the codings. A code in the ValueSet settles every rule shape but a deny
	 * not-in rule, whose undecided code might be outside the ValueSet. Allow not-in, which a code in the ValueSet keeps
	 * from applying, leaves the read to the {@code allowAll()} rule that follows it. Only the deny not-in rule denies
	 * because of the undecided code, which its log shows.
	 */
	// Created by Claude Opus 5.5
	@ParameterizedTest
	@CsvSource({
		"ALLOW_IN,     undecided, red,       ALLOW, false",
		"ALLOW_IN,     red,       undecided, ALLOW, false",
		"DENY_IN,      undecided, red,       DENY,  false",
		"DENY_IN,      red,       undecided, DENY,  false",
		"ALLOW_NOT_IN, undecided, red,       ALLOW, false",
		"ALLOW_NOT_IN, red,       undecided, ALLOW, false",
		"DENY_NOT_IN,  undecided, red,       DENY,  true",
		"DENY_NOT_IN,  red,       undecided, DENY,  true"
	})
	void applyRulesAndReturnDecision_undecidedCodeAndCodeInValueSet_decidesWhateverTheOrder(
			CodeRuleEnum theRule,
			String theFirstCode,
			String theSecondCode,
			PolicyEnum theExpectedDecision,
			boolean theExpectedUndecidedDeny) {
		// Setup
		givenRemoteDecidesRedInBlueOutAndCannotDecideUndecided();

		// Test
		AuthorizationInterceptor.Verdict verdict =
				readObservationCoded(theRule, newRemoteChain(), theFirstCode, theSecondCode);

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(theExpectedDecision);
		assertThat(isUndecidedDenyLogged()).isEqualTo(theExpectedUndecidedDeny);
	}

	/**
	 * With the other code outside the ValueSet, only a deny not-in rule is settled, by that code, and denies; every
	 * other rule shape could be changed by the undecided code, so it denies, whatever the order of the codings. The log
	 * shows which of them denied because of the undecided code.
	 */
	// Created by Claude Opus 5.5
	@ParameterizedTest
	@CsvSource({
		"ALLOW_IN,     undecided, blue,      true",
		"ALLOW_IN,     blue,      undecided, true",
		"DENY_IN,      undecided, blue,      true",
		"DENY_IN,      blue,      undecided, true",
		"ALLOW_NOT_IN, undecided, blue,      true",
		"ALLOW_NOT_IN, blue,      undecided, true",
		"DENY_NOT_IN,  undecided, blue,      false",
		"DENY_NOT_IN,  blue,      undecided, false"
	})
	void applyRulesAndReturnDecision_undecidedCodeAndCodeNotInValueSet_deniesWhateverTheOrder(
			CodeRuleEnum theRule, String theFirstCode, String theSecondCode, boolean theExpectedUndecidedDeny) {
		// Setup
		givenRemoteDecidesRedInBlueOutAndCannotDecideUndecided();

		// Test
		AuthorizationInterceptor.Verdict verdict =
				readObservationCoded(theRule, newRemoteChain(), theFirstCode, theSecondCode);

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
		assertThat(isUndecidedDenyLogged()).isEqualTo(theExpectedUndecidedDeny);
	}

	/**
	 * Whether a rule logged that it denied because a code's membership could not be decided
	 */
	private boolean isUndecidedDenyLogged() {
		return !myAuthorizationLog
				.getLogEvents(t -> t.getLevel() == Level.WARN
						&& t.getFormattedMessage().contains("could not be validated by terminology service"))
				.isEmpty();
	}

	/**
	 * The remote server has the ValueSet, answers that {@code red} is in it and {@code blue} is not, and rejects
	 * {@code $validate-code} for {@code undecided} with HTTP 400
	 */
	private void givenRemoteDecidesRedInBlueOutAndCannotDecideUndecided() {
		myRemoteValueSets.addTerminologyResource(VS_URL);
		myRemoteValueSets.addTerminologyResponse(
				JpaConstants.OPERATION_VALIDATE_CODE, VS_URL, "red", new Parameters().addParameter("result", true));
		myRemoteValueSets.addTerminologyResponse(
				JpaConstants.OPERATION_VALIDATE_CODE,
				VS_URL,
				"blue",
				new Parameters().addParameter("result", false).addParameter("message", "Unknown code blue"));
		myRemoteValueSets.addException(
				JpaConstants.OPERATION_VALIDATE_CODE,
				VS_URL,
				"undecided",
				BaseServerResponseException.newInstance(400, "Simulated rejection"));
	}

	private static ValidationSupportChain newRemoteChain() {
		return new ValidationSupportChain(
				new RemoteTerminologyServiceValidationSupport(ourCtx, ourRemoteTerminologyServer.getBaseUrl()),
				new DefaultProfileValidationSupport(ourCtx),
				new CommonCodeSystemsTerminologyService(ourCtx),
				new InMemoryTerminologyServerValidationSupport(ourCtx));
	}

	/**
	 * With {@code useCurrentVersionForUnknownCodeSystemVersion} on, a code the current version has is checked against
	 * it, so every rule shape decides as for a loaded CodeSystem
	 */
	@ParameterizedTest
	@CsvSource({
		"ALLOW_IN,     ALLOW",
		"ALLOW_NOT_IN, ALLOW",
		"DENY_IN,      DENY",
		"DENY_NOT_IN,  ALLOW"
	})
	void applyRulesAndReturnDecision_useCurrentVersionAndCodeInCurrentVersion_followsTheRule(
			CodeRuleEnum theRule, PolicyEnum theExpectedDecision) {
		// Setup
		ValidationSupportChain validationSupport = newUseCurrentVersionChain();

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCoded(theRule, validationSupport, "red");

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(theExpectedDecision);
	}

	/**
	 * With {@code useCurrentVersionForUnknownCodeSystemVersion} on, a code the ValueSet lists but the current version
	 * lacks cannot be shown to be outside the ValueSet, so every rule shape denies
	 */
	@ParameterizedTest
	@EnumSource(CodeRuleEnum.class)
	void applyRulesAndReturnDecision_useCurrentVersionAndCodeOnlyListed_denies(CodeRuleEnum theRule) {
		// Setup
		ValidationSupportChain validationSupport = newUseCurrentVersionChain();

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCoded(theRule, validationSupport, "green");

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
	}

	/**
	 * As {@link #applyRulesAndReturnDecision_useCurrentVersionAndCodeOnlyListed_denies}, with the rule naming a
	 * ValueSet whose only include is {@code include.valueSet} naming the one that lists the codes
	 */
	@ParameterizedTest
	@EnumSource(CodeRuleEnum.class)
	void applyRulesAndReturnDecision_useCurrentVersionAndCodeOnlyListedInIncludedValueSet_denies(
			CodeRuleEnum theRule) {
		// Setup
		ValueSet outer = new ValueSet();
		outer.setUrl(VS_URL);
		outer.setStatus(Enumerations.PublicationStatus.ACTIVE);
		outer.getCompose().addInclude().addValueSet(INCLUDED_VS_URL);
		ValueSet included = newColoursValueSet("2.0");
		included.setUrl(INCLUDED_VS_URL);
		included.getCompose().getIncludeFirstRep().addConcept().setCode("green");
		ValidationSupportChain validationSupport = newUseCurrentVersionChain(outer, included);

		// Test
		AuthorizationInterceptor.Verdict verdict = readObservationCoded(theRule, validationSupport, "green");

		// Verify
		assertThat(verdict.getDecision()).isEqualTo(PolicyEnum.DENY);
	}

	/**
	 * Version 1.0 of the colours CodeSystem, holding {@code red}, and a ValueSet listing {@code red} and {@code green}
	 * from version 2.0, with {@code useCurrentVersionForUnknownCodeSystemVersion} on
	 */
	private static ValidationSupportChain newUseCurrentVersionChain() {
		ValueSet valueSet = newColoursValueSet("2.0");
		valueSet.getCompose().getIncludeFirstRep().addConcept().setCode("green");
		return newUseCurrentVersionChain(valueSet);
	}

	/**
	 * Version 1.0 of the colours CodeSystem, holding {@code red}, and the given ValueSets, with
	 * {@code useCurrentVersionForUnknownCodeSystemVersion} on
	 */
	private static ValidationSupportChain newUseCurrentVersionChain(ValueSet... theValueSets) {
		PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ourCtx);
		prePopulated.addCodeSystem(newColoursCodeSystem("1.0"));
		for (ValueSet next : theValueSets) {
			prePopulated.addValueSet(next);
		}
		InMemoryTerminologyServerValidationSupport inMemory = new InMemoryTerminologyServerValidationSupport(ourCtx);
		inMemory.setUseCurrentVersionForUnknownCodeSystemVersion(true);
		return new ValidationSupportChain(
				new DefaultProfileValidationSupport(ourCtx),
				prePopulated,
				new CommonCodeSystemsTerminologyService(ourCtx),
				inMemory);
	}

	private static AuthorizationInterceptor.Verdict readObservationCodedRed(
			CodeRuleEnum theRule, PrePopulatedValidationSupport thePrePopulated) {
		return readObservationCodedRed(theRule, newChain(thePrePopulated));
	}

	private static ValidationSupportChain newChain(PrePopulatedValidationSupport thePrePopulated) {
		return new ValidationSupportChain(
				new DefaultProfileValidationSupport(ourCtx),
				thePrePopulated,
				new CommonCodeSystemsTerminologyService(ourCtx),
				new InMemoryTerminologyServerValidationSupport(ourCtx));
	}

	private static AuthorizationInterceptor.Verdict readObservationCodedRed(
			CodeRuleEnum theRule, ValidationSupportChain theValidationSupport) {
		return readObservationCoded(theRule, theValidationSupport, "red");
	}

	private static AuthorizationInterceptor.Verdict readObservationCoded(
			CodeRuleEnum theRule, ValidationSupportChain theValidationSupport, String... theCodes) {
		AuthorizationInterceptor interceptor = new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return theRule.addTo(new RuleBuilder()).allowAll().build();
			}
		}.setValidationSupport(theValidationSupport);

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setFhirContext(ourCtx);
		requestDetails.setResourceName("Observation");
		Observation observation = new Observation();
		observation.setId(new IdType("Observation/10"));
		for (String next : theCodes) {
			observation.getCode().addCoding().setSystem(CS_URL).setCode(next);
		}

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
