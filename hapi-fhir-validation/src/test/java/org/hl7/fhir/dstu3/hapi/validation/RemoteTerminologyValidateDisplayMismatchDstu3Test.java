package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.test.utilities.validation.IValidationProvidersDstu3;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DSTU3 sibling of {@link org.hl7.fhir.r4.validation.RemoteTerminologyValidateDisplayMismatchTest}.
 * <p>
 * The DSTU3 conversion helper has its own inlined per-issue check (parallel to the R4 helper). These
 * tests pin the same contract for DSTU3 so any future divergence between the two version-specific
 * helpers is caught.
 */
public class RemoteTerminologyValidateDisplayMismatchDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

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

	private IValidationProvidersDstu3.MyCodeSystemProviderDstu3 myCodeSystemProvider;
	private RemoteTerminologyServiceValidationSupport mySvc;

	@BeforeEach
	public void before() {
		myCodeSystemProvider = new IValidationProvidersDstu3.MyCodeSystemProviderDstu3();
		ourServer.getRestfulServer().registerProvider(myCodeSystemProvider);
		myCodeSystemProvider.addTerminologyResource(CS_URL);
		mySvc = new RemoteTerminologyServiceValidationSupport(
				ourCtx, "http://localhost:" + ourServer.getPort());
	}

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
				.as("DSTU3 signal (b): message-id extension whose value contains 'display' → severity overridden")
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
				.as("DSTU3: message-id value lacking 'display' must not qualify")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	@Test
	void displaysDiffer_issueHasDisplayMismatchExtension_severityIsConfigured() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
		myCodeSystemProvider.addTerminologyResponse(
				"$validate-code",
				CS_URL,
				CODE,
				buildOOResponse(issue(NEUTRAL_DIAGNOSTICS, /*addExtension*/ true)));

		IValidationSupport.CodeValidationResult outcome = invokeValidate();

		assertThat(outcome.getIssues()).hasSize(1);
		assertThat(outcome.getIssues().get(0).getSeverity())
				.as("DSTU3 signal (a): displayMismatched extension on issue → severity overridden")
				.isEqualTo(IValidationSupport.IssueSeverity.WARNING);
	}

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
				.as("DSTU3: no display signal → severity unchanged")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

	@Test
	void displaysMatch_evenWithDisplaySignals_noAdjustment() {
		mySvc.setIssueSeverityForCodeDisplayMismatch(IValidationSupport.IssueSeverity.WARNING);
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
				.as("DSTU3: gate doesn't fire (displays match) → severity unchanged")
				.isEqualTo(IValidationSupport.IssueSeverity.ERROR);
	}

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
				.as("DSTU3 no-OO fallback: gate fires + 'display' in message → synthesized severity overridden")
				.isEqualTo(IValidationSupport.IssueSeverity.WARNING);
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
		for (OperationOutcome.OperationOutcomeIssueComponent issueComponent : theIssues) {
			issuesOO.addIssue(issueComponent);
		}
		params.addParameter().setName("issues").setResource(issuesOO);
		return params;
	}
}
