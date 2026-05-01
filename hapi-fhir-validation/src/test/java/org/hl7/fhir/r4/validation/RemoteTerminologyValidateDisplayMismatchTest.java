package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationIssue;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersR4;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies that {@code display_mismatch_policy} is applied per-issue to Remote Terminology
 * {@code $validate-code} responses.
 * <p>
 * Contract — the response-level gate (<i>gate</i> = request {@code display} ≠ response {@code display})
 * must fire for any per-issue severity override to apply. When the gate fires, an issue qualifies if the
 * issue itself, or its {@code diagnostics} primitive element, carries an extension whose URL is either
 * <ul>
 *   <li>(a) {@value RemoteTerminologyServiceValidationSupport#DISPLAY_MISMATCH_EXTENSION_URL}; or</li>
 *   <li>(b) {@value RemoteTerminologyServiceValidationSupport#MESSAGE_ID_EXTENSION_URL} with a primitive value
 *       containing the substring "display" (case-insensitive) — e.g. an i18n key like
 *       {@code Display_Name_for__should_be_one_of__instead_of}.</li>
 * </ul>
 * Additional behaviour:
 * <ul>
 *   <li><b>No-OO fallback</b>: when the server returns only a free-text {@code message} (no structured
 *       {@code issues} OperationOutcome), the synthesized issue gets the configured severity if (and only if)
 *       displays differ AND the message text contains "display"; otherwise it stays at ERROR.</li>
 *   <li><b>Result severity</b>: the highest severity across the (possibly adjusted) issues, with precedence
 *       {@code FATAL > ERROR > WARNING > INFORMATION}.</li>
 * </ul>
 */
public class RemoteTerminologyValidateDisplayMismatchTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	private static final String CS_URL = "http://test.smilecdr.com/CodeSystem/test-drugs";
	private static final String CODE = "01916971";
	private static final String CANONICAL_DISPLAY = "TUSSIONEX";
	private static final String WRONG_CASE_DISPLAY = "Tussionex";
	private static final String DISPLAY_MISMATCH_DIAGNOSTICS =
			"Wrong Display Name '" + WRONG_CASE_DISPLAY + "' for " + CS_URL + "#" + CODE;
	private static final String NEUTRAL_DIAGNOSTICS =
			"Code '" + CODE + "' resolved with annotations differing from request";
	private static final String INVALID_CODE_DIAGNOSTICS =
			"Code '" + CODE + "' is not a valid code in CodeSystem '" + CS_URL + "'";

	@RegisterExtension
	public static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx);

	private IValidationProvidersR4.MyCodeSystemProviderR4 myCodeSystemProvider;
	private RemoteTerminologyServiceValidationSupport mySvc;

	@BeforeEach
	public void before() {
		myCodeSystemProvider = new IValidationProvidersR4.MyCodeSystemProviderR4();
		ourServer.getRestfulServer().registerProvider(myCodeSystemProvider);
		myCodeSystemProvider.addTerminologyResource(CS_URL);
		mySvc = new RemoteTerminologyServiceValidationSupport(
				ourCtx, "http://localhost:" + ourServer.getPort());
	}

	// =============== Signal (a): displayMismatched extension on the issue ===============

	@ParameterizedTest
	@CsvSource({"INFORMATION", "WARNING", "ERROR"})
	void displaysDiffer_issueHasDisplayMismatchExtension_severityIsConfigured(
			IValidationSupport.IssueSeverity theConfiguredSeverity) {
		mySvc.setIssueSeverityForCodeDisplayMismatch(theConfiguredSeverity);
		myCodeSystemProvider.addTerminologyResponse(
				"$validate-code",
				CS_URL,
				CODE,
				buildOOResponse(issue(NEUTRAL_DIAGNOSTICS, /*addExtension*/ true)));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("signal (a): displayMismatched extension on issue → severity overridden")
				.isEqualTo(theConfiguredSeverity);
	}

	@Test
	void displaysDiffer_diagnosticsElementHasDisplayMismatchExtension_severityIsConfigured() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		OperationOutcome.OperationOutcomeIssueComponent issue = issue(NEUTRAL_DIAGNOSTICS, /*addExtension*/ false);
		issue.getDiagnosticsElement()
				.addExtension()
				.setUrl(RemoteTerminologyServiceValidationSupport.DISPLAY_MISMATCH_EXTENSION_URL)
				.setValue(new BooleanType(true));
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, buildOOResponse(issue));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("signal (a) variant: extension on diagnostics primitive element → severity overridden")
				.isEqualTo(IValidationSupport.IssueSeverity.WARNING);
	}

	// =============== Signal (b): message-id extension with a "display"-bearing i18n key ===============

	@Test
	void displaysDiffer_messageIdExtensionWithDisplayKey_severityIsConfigured() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		OperationOutcome.OperationOutcomeIssueComponent issueComponent = issue(NEUTRAL_DIAGNOSTICS, /*addExtension*/ false);
		issueComponent
				.addExtension()
				.setUrl(RemoteTerminologyServiceValidationSupport.MESSAGE_ID_EXTENSION_URL)
				.setValue(new StringType("Display_Name_for__should_be_one_of__instead_of"));
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, buildOOResponse(issueComponent));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("signal (b): message-id extension whose value contains 'display' → severity overridden")
				.isEqualTo(IValidationSupport.IssueSeverity.WARNING);
	}

	@Test
	void displaysDiffer_messageIdExtensionWithoutDisplayKey_noAdjustment() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		OperationOutcome.OperationOutcomeIssueComponent issueComponent =
				issue(INVALID_CODE_DIAGNOSTICS, /*addExtension*/ false);
		issueComponent
				.addExtension()
				.setUrl(RemoteTerminologyServiceValidationSupport.MESSAGE_ID_EXTENSION_URL)
				.setValue(new StringType("Unknown_Code_in_System"));
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, buildOOResponse(issueComponent));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("message-id extension whose value lacks 'display' must not qualify")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	// =============== Negative cases for the per-issue rule ===============

	@Test
	void displaysDiffer_neitherSignal_noAdjustment() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		myCodeSystemProvider.addTerminologyResponse(
				"$validate-code",
				CS_URL,
				CODE,
				buildOOResponse(issue(INVALID_CODE_DIAGNOSTICS, /*addExtension*/ false)));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("no displayMismatched extension and no 'display' word → severity unchanged")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
		assertThat(outcome.getSeverity()).isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	@Test
	void mixedIssues_onlyDisplaySignalIssueIsAdjusted_resultSeverityIsMax() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		myCodeSystemProvider.addTerminologyResponse(
				"$validate-code",
				CS_URL,
				CODE,
				buildOOResponse(
						issue(INVALID_CODE_DIAGNOSTICS, /*addExtension*/ false),
						issue(DISPLAY_MISMATCH_DIAGNOSTICS, /*addExtension*/ true)));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(2);
		CodeValidationIssue invalidCode = findByDiagnostics(outcome, INVALID_CODE_DIAGNOSTICS);
		CodeValidationIssue displayIssue = findByDiagnostics(outcome, DISPLAY_MISMATCH_DIAGNOSTICS);
		assertThat(invalidCode.getSeverity())
				.as("non-display issue keeps server severity")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
		assertThat(displayIssue.getSeverity())
				.as("display-signal issue is downgraded")
				.isEqualTo(IValidationSupport.IssueSeverity.WARNING);
		assertThat(outcome.getSeverity())
				.as("result severity is max across (adjusted) issues")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	// =============== Gate: displays match ===============

	@Test
	void displaysMatch_evenWithDisplaySignals_noAdjustment() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		// Server echoes the *same* display as requested → gate doesn't fire even though the issue
		// carries both display signals.
		Parameters params = new Parameters();
		params.addParameter().setName("result").setValue(new BooleanType(false));
		params.addParameter().setName("code").setValue(new StringType(CODE));
		params.addParameter().setName("system").setValue(new StringType(CS_URL));
		params.addParameter().setName("display").setValue(new StringType(WRONG_CASE_DISPLAY));
		params.addParameter().setName("message").setValue(new StringType(DISPLAY_MISMATCH_DIAGNOSTICS));
		OperationOutcome issuesOO = new OperationOutcome();
		issuesOO.addIssue(issue(DISPLAY_MISMATCH_DIAGNOSTICS, /*addExtension*/ true));
		params.addParameter().setName("issues").setResource(issuesOO);
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, params);

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("gate did not fire (displays match) → no adjustment, server severity preserved")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	// =============== No-OO fallback (synthesized issue from message) ===============

	@Test
	void noOO_displaysDiffer_messageContainsDisplay_synthesizedIssueGetsConfiguredSeverity() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		Parameters params = new Parameters();
		params.addParameter().setName("result").setValue(new BooleanType(false));
		params.addParameter().setName("code").setValue(new StringType(CODE));
		params.addParameter().setName("system").setValue(new StringType(CS_URL));
		params.addParameter().setName("display").setValue(new StringType(CANONICAL_DISPLAY));
		params.addParameter().setName("message").setValue(new StringType(DISPLAY_MISMATCH_DIAGNOSTICS));
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, params);

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("no-OO fallback: gate fires + message mentions 'display' → synthesized severity overridden")
				.isEqualTo(IValidationSupport.IssueSeverity.WARNING);
	}

	@Test
	void noOO_displaysDiffer_messageHasNoDisplayWord_synthesizedIssueStaysAtError() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		Parameters params = new Parameters();
		params.addParameter().setName("result").setValue(new BooleanType(false));
		params.addParameter().setName("code").setValue(new StringType(CODE));
		params.addParameter().setName("system").setValue(new StringType(CS_URL));
		params.addParameter().setName("display").setValue(new StringType(CANONICAL_DISPLAY));
		params.addParameter().setName("message").setValue(new StringType(INVALID_CODE_DIAGNOSTICS));
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, params);

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("no-OO fallback: message doesn't mention 'display' → synthesized severity stays ERROR")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	@Test
	void noOO_displaysMatch_messageContainsDisplay_synthesizedIssueStaysAtError() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		Parameters params = new Parameters();
		params.addParameter().setName("result").setValue(new BooleanType(false));
		params.addParameter().setName("code").setValue(new StringType(CODE));
		params.addParameter().setName("system").setValue(new StringType(CS_URL));
		params.addParameter().setName("display").setValue(new StringType(WRONG_CASE_DISPLAY));
		params.addParameter().setName("message").setValue(new StringType(DISPLAY_MISMATCH_DIAGNOSTICS));
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, params);

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("no-OO fallback: gate doesn't fire (displays match) → severity stays ERROR")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	// =============== Edge cases ===============

	@Test
	void displaysDiffer_issueHasBothSignals_severityAdjustedOnce() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		// Issue carries BOTH signal (a) (displayMismatched extension) AND signal (b) (message-id with display key).
		OperationOutcome.OperationOutcomeIssueComponent issueComponent = issue(NEUTRAL_DIAGNOSTICS, /*addExtension*/ true);
		issueComponent
				.addExtension()
				.setUrl(RemoteTerminologyServiceValidationSupport.MESSAGE_ID_EXTENSION_URL)
				.setValue(new StringType("Display_Name_for__should_be_one_of__instead_of"));
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, buildOOResponse(issueComponent));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("OR'd signals → single adjustment to the configured severity (no double-flip)")
				.isEqualTo(IValidationSupport.IssueSeverity.WARNING);
	}

	@Test
	void displaysDiffer_almostMatchingExtensionUrl_noAdjustment() {
		// Extension URL is *close* to DISPLAY_MISMATCH_EXTENSION_URL but not exactly equal — must NOT match.
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		OperationOutcome.OperationOutcomeIssueComponent issueComponent =
				new OperationOutcome.OperationOutcomeIssueComponent();
		issueComponent.setSeverity(OperationOutcome.IssueSeverity.ERROR);
		issueComponent.setCode(OperationOutcome.IssueType.INVALID);
		issueComponent.setDiagnostics(NEUTRAL_DIAGNOSTICS);
		issueComponent.getDetails().setText(NEUTRAL_DIAGNOSTICS);
		issueComponent
				.addExtension()
				.setUrl("http://hl7.org/fhir/StructureDefinition/operationoutcome-issue-display") // missing 'Mismatched'
				.setValue(new BooleanType(true));
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, buildOOResponse(issueComponent));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("extension URL must match exactly — substring/near matches do not qualify")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	@Test
	void requestDisplayBlank_gateDoesNotFire() {
		// Even though the issue carries a display signal, a blank request display means we have nothing
		// to compare against → gate doesn't fire.
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		myCodeSystemProvider.addTerminologyResponse(
				"$validate-code",
				CS_URL,
				CODE,
				buildOOResponse(issue(DISPLAY_MISMATCH_DIAGNOSTICS, /*addExtension*/ true)));

		IValidationSupport.CodeValidationResult outcome = mySvc.validateCode(
				null, new ConceptValidationOptions().setValidateDisplay(true), CS_URL, CODE, /* blank */ "", null);

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("blank request display → gate doesn't fire")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	@Test
	void responseDisplayMissing_gateDoesNotFire() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		Parameters params = new Parameters();
		params.addParameter().setName("result").setValue(new BooleanType(false));
		params.addParameter().setName("code").setValue(new StringType(CODE));
		params.addParameter().setName("system").setValue(new StringType(CS_URL));
		// no `display` parameter
		params.addParameter().setName("message").setValue(new StringType(DISPLAY_MISMATCH_DIAGNOSTICS));
		OperationOutcome issuesOO = new OperationOutcome();
		issuesOO.addIssue(issue(DISPLAY_MISMATCH_DIAGNOSTICS, /*addExtension*/ true));
		params.addParameter().setName("issues").setResource(issuesOO);
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, params);

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("response display absent → gate doesn't fire")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	@Test
	void responseDisplayBlank_gateDoesNotFire() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		Parameters params = new Parameters();
		params.addParameter().setName("result").setValue(new BooleanType(false));
		params.addParameter().setName("code").setValue(new StringType(CODE));
		params.addParameter().setName("system").setValue(new StringType(CS_URL));
		params.addParameter().setName("display").setValue(new StringType("")); // blank
		params.addParameter().setName("message").setValue(new StringType(DISPLAY_MISMATCH_DIAGNOSTICS));
		OperationOutcome issuesOO = new OperationOutcome();
		issuesOO.addIssue(issue(DISPLAY_MISMATCH_DIAGNOSTICS, /*addExtension*/ true));
		params.addParameter().setName("issues").setResource(issuesOO);
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, params);

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("response display blank → gate doesn't fire")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	@Test
	void noOO_noMessage_synthesizedIssueStaysAtError() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		Parameters params = new Parameters();
		params.addParameter().setName("result").setValue(new BooleanType(false));
		params.addParameter().setName("code").setValue(new StringType(CODE));
		params.addParameter().setName("system").setValue(new StringType(CS_URL));
		params.addParameter().setName("display").setValue(new StringType(CANONICAL_DISPLAY));
		// no `message` parameter and no `issues` OO
		myCodeSystemProvider.addTerminologyResponse("$validate-code", CS_URL, CODE, params);

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("no message + no OO → synthesized issue keeps default ERROR")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	// =============== Severity aggregation precedence ===============

	@Test
	void aggregateMaxSeverity_fatalDominatesAdjustedDisplayIssue() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		// Two issues: a non-display FATAL + a display-signal ERROR (downgraded to WARNING).
		// Result severity should be FATAL (max across adjusted issues).
		OperationOutcome.OperationOutcomeIssueComponent fatalIssue =
				new OperationOutcome.OperationOutcomeIssueComponent();
		fatalIssue.setSeverity(OperationOutcome.IssueSeverity.FATAL);
		fatalIssue.setCode(OperationOutcome.IssueType.EXCEPTION);
		fatalIssue.setDiagnostics(INVALID_CODE_DIAGNOSTICS);
		fatalIssue.getDetails().setText(INVALID_CODE_DIAGNOSTICS);

		myCodeSystemProvider.addTerminologyResponse(
				"$validate-code",
				CS_URL,
				CODE,
				buildOOResponse(fatalIssue, issue(NEUTRAL_DIAGNOSTICS, /*addExtension*/ true)));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(2);
		assertThat(outcome.getSeverity())
				.as("aggregate severity uses FATAL > ERROR > WARNING > INFORMATION precedence")
				.isEqualTo(IValidationSupport.IssueSeverity.FATAL);
	}

	// =============== Malformed issue (missing severity / code) ===============

	@Test
	void malformedIssue_missingSeverityAndCode_doesNotNpeAndDefaultsSeverityToError() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		// FHIR requires severity and code on every issue, but a non-conformant remote could omit them.
		// We must not NPE; instead default severity to ERROR and leave issue type code null.
		OperationOutcome.OperationOutcomeIssueComponent malformed =
				new OperationOutcome.OperationOutcomeIssueComponent();
		malformed.setDiagnostics(INVALID_CODE_DIAGNOSTICS);
		malformed.getDetails().setText(INVALID_CODE_DIAGNOSTICS);
		myCodeSystemProvider.addTerminologyResponse(
				"$validate-code", CS_URL, CODE, buildOOResponse(malformed));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("missing severity → defaults to ERROR")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
		assertThat(outcome.getSeverity()).isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	// =============== helpers ===============

	private IValidationSupport.CodeValidationResult invokeValidate() {
		return mySvc.validateCode(
				null,
				new ConceptValidationOptions().setValidateDisplay(true),
				CS_URL,
				CODE,
				WRONG_CASE_DISPLAY,
				null);
	}

	private static CodeValidationIssue findByDiagnostics(
			IValidationSupport.CodeValidationResult theOutcome, String theDiagnostics) {
		return theOutcome.getIssues().stream()
				.filter(i -> theDiagnostics.equals(i.getDiagnostics()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("No issue with diagnostics: " + theDiagnostics));
	}

	private static OperationOutcome.OperationOutcomeIssueComponent issue(String theDiagnostics, boolean theAddExtension) {
		OperationOutcome.OperationOutcomeIssueComponent issue = new OperationOutcome.OperationOutcomeIssueComponent();
		issue.setSeverity(OperationOutcome.IssueSeverity.ERROR);
		issue.setCode(OperationOutcome.IssueType.INVALID);
		issue.setDiagnostics(theDiagnostics);
		issue.getDetails().setText(theDiagnostics);
		if (theAddExtension) {
			issue.addExtension()
					.setUrl(RemoteTerminologyServiceValidationSupport.DISPLAY_MISMATCH_EXTENSION_URL)
					.setValue(new BooleanType(true));
		}
		return issue;
	}

	private static Parameters buildOOResponse(OperationOutcome.OperationOutcomeIssueComponent... theIssues) {
		Parameters params = new Parameters();
		params.addParameter().setName("result").setValue(new BooleanType(false));
		params.addParameter().setName("code").setValue(new StringType(CODE));
		params.addParameter().setName("system").setValue(new StringType(CS_URL));
		params.addParameter().setName("display").setValue(new StringType(CANONICAL_DISPLAY));
		params.addParameter().setName("message").setValue(new StringType(DISPLAY_MISMATCH_DIAGNOSTICS));

		OperationOutcome issuesOO = new OperationOutcome();
		for (OperationOutcome.OperationOutcomeIssueComponent issue : theIssues) {
			issuesOO.addIssue(issue);
		}
		params.addParameter().setName("issues").setResource(issuesOO);
		return params;
	}
}
