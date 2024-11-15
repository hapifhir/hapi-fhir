package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.RemoteTerminologyServiceValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IRemoteTerminologyValidateCodeTest extends IValidateCodeTest {
	default List<IValidationSupport.CodeValidationIssue> getCodeValidationIssues(IBaseOperationOutcome theOperationOutcome) {
		// this method should be removed once support for issues is fully implemented across all validator types
		Optional<Collection<IValidationSupport.CodeValidationIssue>> issues = RemoteTerminologyServiceValidationSupport.createCodeValidationIssues(theOperationOutcome, getService().getFhirContext().getVersion().getVersion());
		return issues.map(theCodeValidationIssues -> theCodeValidationIssues.stream().toList()).orElseGet(List::of);
	}
}
