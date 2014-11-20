package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;

/**
 * Encapsulates the results of validation
 *
 * @see ca.uhn.fhir.validation.FhirValidator
 * @since 0.7
 */
public class ValidationResult {
    private BaseOperationOutcome myOperationOutcome;
    private boolean isSuccessful;

    private ValidationResult(BaseOperationOutcome myOperationOutcome, boolean isSuccessful) {
        this.myOperationOutcome = myOperationOutcome;
        this.isSuccessful = isSuccessful;
    }

    public static ValidationResult valueOf(BaseOperationOutcome myOperationOutcome) {
        boolean noIssues = myOperationOutcome == null || myOperationOutcome.getIssue().isEmpty();
        return new ValidationResult(myOperationOutcome, noIssues);
    }

    public BaseOperationOutcome getOperationOutcome() {
        return myOperationOutcome;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "myOperationOutcome=" + myOperationOutcome +
                ", isSuccessful=" + isSuccessful +
                ", description='" + toDescription() + '\'' +
                '}';
    }

    private String toDescription() {
        StringBuilder b = new StringBuilder(100);
        if (myOperationOutcome != null) {
            BaseOperationOutcome.BaseIssue issueFirstRep = myOperationOutcome.getIssueFirstRep();
            b.append(issueFirstRep.getDetailsElement().getValue());
            b.append(" - ");
            b.append(issueFirstRep.getLocationFirstRep().getValue());
        }
        return b.toString();
    }

    /**
     * Was the validation successful
     * @return true if the validation was successful
     */
    public boolean isSuccessful() {
        return isSuccessful;
    }
}
