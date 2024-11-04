package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport.createCodeValidationIssues;

public interface IRemoteTerminologyValidateCodeTest extends IValidateCodeTest {
	default List<IValidationSupport.CodeValidationIssue> getCodeValidationIssues(IBaseOperationOutcome theOperationOutcome) {
		// this method should be removed once support for issues is fully implemented across all validator types
		Optional<Collection<IValidationSupport.CodeValidationIssue>> issues = RemoteTerminologyServiceValidationSupport.createCodeValidationIssues(theOperationOutcome, getService().getFhirContext().getVersion().getVersion());
		return issues.map(theCodeValidationIssues -> theCodeValidationIssues.stream().toList()).orElseGet(List::of);
	}

	@Test
	default void createCodeValidationIssues_withCodeSystemOutcomeForInvalidCode_returnsAsExpected() {
		IBaseOperationOutcome outcome = getCodeSystemInvalidCodeOutcome();
		FhirVersionEnum versionEnum = getService().getFhirContext().getVersion().getVersion();
		Optional<Collection<IValidationSupport.CodeValidationIssue>> issuesOptional = createCodeValidationIssues(outcome, versionEnum);
		assertThat(issuesOptional).isPresent();
		assertThat(issuesOptional.get()).hasSize(1);
		IValidationSupport.CodeValidationIssue issue = issuesOptional.get().iterator().next();
		assertThat(issue.getType().getCode()).isEqualTo("code-invalid");
		assertThat(issue.getSeverity().getCode()).isEqualTo("error");
		assertThat(issue.getDetails().getCodings()).hasSize(1);
		IValidationSupport.CodeValidationIssueCoding issueCoding = issue.getDetails().getCodings().get(0);
		assertThat(issueCoding.getSystem()).isEqualTo("http://hl7.org/fhir/tools/CodeSystem/tx-issue-type");
		assertThat(issueCoding.getCode()).isEqualTo("invalid-code");
		assertThat(issue.getDetails().getText()).isEqualTo("Unknown code 'CODE' in the CodeSystem 'http://code.system/url' version '1.0.0'");
		assertThat(issue.getDiagnostics()).isNull();
	}

	@Test
	default void createCodeValidationIssues_withValueSetOutcomeForInvalidCode_returnsAsExpected() {
		IBaseOperationOutcome outcome = getValueSetInvalidCodeOutcome();
		FhirVersionEnum versionEnum = getService().getFhirContext().getVersion().getVersion();
		Optional<Collection<IValidationSupport.CodeValidationIssue>> issuesOptional = createCodeValidationIssues(outcome, versionEnum);
		assertThat(issuesOptional).isPresent();
		assertThat(issuesOptional.get()).hasSize(2);
		IValidationSupport.CodeValidationIssue issue = issuesOptional.get().iterator().next();
		assertThat(issue.getType().getCode()).isEqualTo("code-invalid");
		assertThat(issue.getSeverity().getCode()).isEqualTo("error");
		assertThat(issue.getDetails().getCodings()).hasSize(1);
		IValidationSupport.CodeValidationIssueCoding issueCoding = issue.getDetails().getCodings().get(0);
		assertThat(issueCoding.getSystem()).isEqualTo("http://hl7.org/fhir/tools/CodeSystem/tx-issue-type");
		assertThat(issueCoding.getCode()).isEqualTo("not-in-vs");
		assertThat(issue.getDetails().getText()).isEqualTo("The provided code 'http://code.system/url#CODE' was not found in the value set 'http://value.set/url%7C1.0.0'");
		assertThat(issue.getDiagnostics()).isNull();
	}

	@Test
	default void createCodeValidationIssues_withValueSetOutcomeWithCustomDetailCode_returnsAsExpected() {
		IBaseOperationOutcome outcome = getValueSetCustomDetailCodeOutcome();
		FhirVersionEnum versionEnum = getService().getFhirContext().getVersion().getVersion();
		Optional<Collection<IValidationSupport.CodeValidationIssue>> issuesOptional = createCodeValidationIssues(outcome, versionEnum);
		assertThat(issuesOptional).isPresent();
		assertThat(issuesOptional.get()).hasSize(1);
		IValidationSupport.CodeValidationIssue issue = issuesOptional.get().iterator().next();
		assertThat(issue.getType().getCode()).isEqualTo("processing");
		assertThat(issue.getSeverity().getCode()).isEqualTo("information");
		assertThat(issue.getDetails().getCodings()).hasSize(1);
		IValidationSupport.CodeValidationIssueCoding issueCoding = issue.getDetails().getCodings().get(0);
		assertThat(issueCoding.getSystem()).isEqualTo("http://example.com/custom-issue-type");
		assertThat(issueCoding.getCode()).isEqualTo("valueset-is-draft");
		assertThat(issue.getDetails().getText()).isNull();
		assertThat(issue.getDiagnostics()).isEqualTo("The ValueSet status is marked as draft.");
	}
}
