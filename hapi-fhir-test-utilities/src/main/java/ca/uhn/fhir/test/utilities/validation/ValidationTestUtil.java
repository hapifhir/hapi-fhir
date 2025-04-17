/*-
 * #%L
 * HAPI FHIR Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
