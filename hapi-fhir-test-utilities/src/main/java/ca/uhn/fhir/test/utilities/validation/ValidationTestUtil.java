package ca.uhn.fhir.test.utilities.validation;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;

import java.util.List;

/**
 * Utility class for resource validation.
 */
public class ValidationTestUtil {
	private ValidationTestUtil() {
		// utility
	}

	/**
	 * Invokes validate operation against the provided FHIR client with the provided FHIR resource.
	 * @param theClient the client
	 * @param theResource the resource
	 * @return the errors returned by the validate call
	 */
	public static List<String> getValidationErrors(IGenericClient theClient, IBaseResource theResource) {
		MethodOutcome result = theClient.validate().resource(theResource).execute();
		OperationOutcome operationOutcome = (OperationOutcome) result.getOperationOutcome();
		return operationOutcome.getIssue().stream()
				.filter(issue -> issue.getSeverity() == OperationOutcome.IssueSeverity.ERROR)
				.map(OperationOutcome.OperationOutcomeIssueComponent::getDiagnostics)
				.toList();
	}
}
