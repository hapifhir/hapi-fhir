package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.OperationOutcomeUtil;

/**
 * Encapsulates the results of validation
 *
 * @see ca.uhn.fhir.validation.FhirValidator
 * @since 0.7
 */
public class ValidationResult {
	private IBaseOperationOutcome myOperationOutcome;
	private boolean myIsSuccessful;
	private FhirContext myCtx;

	private ValidationResult(FhirContext theCtx, IBaseOperationOutcome theOperationOutcome, boolean isSuccessful) {
		this.myCtx = theCtx;
		this.myOperationOutcome = theOperationOutcome;
		this.myIsSuccessful = isSuccessful;
	}

	public static ValidationResult valueOf(FhirContext theCtx, IBaseOperationOutcome myOperationOutcome) {
		boolean noIssues = !OperationOutcomeUtil.hasIssues(theCtx, myOperationOutcome);
		return new ValidationResult(theCtx, myOperationOutcome, noIssues);
	}

	public IBaseOperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	@Override
	public String toString() {
		return "ValidationResult{" + "myOperationOutcome=" + myOperationOutcome + ", isSuccessful=" + myIsSuccessful + ", description='" + toDescription() + '\'' + '}';
	}

	private String toDescription() {
		StringBuilder b = new StringBuilder(100);
		if (myOperationOutcome != null && OperationOutcomeUtil.hasIssues(myCtx, myOperationOutcome)) {
			b.append(OperationOutcomeUtil.getFirstIssueDetails(myCtx, myOperationOutcome));
			b.append(" - ");
			b.append(OperationOutcomeUtil.getFirstIssueLocation(myCtx, myOperationOutcome));
		} else {
			b.append("No issues");
		}
		return b.toString();
	}

	/**
	 * Was the validation successful
	 * 
	 * @return true if the validation was successful
	 */
	public boolean isSuccessful() {
		return myIsSuccessful;
	}
}
