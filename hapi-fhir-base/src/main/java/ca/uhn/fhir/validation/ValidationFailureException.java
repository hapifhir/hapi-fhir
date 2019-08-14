package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public class ValidationFailureException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private IBaseOperationOutcome myOperationOutcome;

//	public ValidationFailureException(String theProblem) {
//		this(theProblem, IssueSeverityEnum.FATAL, null);
//	}

	private static String toDescription(FhirContext theCtx, IBaseOperationOutcome theOo) {
		StringBuilder b = new StringBuilder();
		b.append(OperationOutcomeUtil.getFirstIssueDetails(theCtx, theOo));
//		b.append(" - ");
//		b.append(theOo.getIssueFirstRep().getLocationFirstRep().getValue());
		return b.toString();
	}

//	public ValidationFailureException(String theProblem, Exception theCause) {
//		this(theProblem, IssueSeverityEnum.FATAL, theCause);
//	}

//	public ValidationFailureException(String theProblem, IssueSeverityEnum theSeverity, Exception theCause) {
//		super(theProblem, theCause);
//		myOperationOutcome = new OperationOutcome();
//		myOperationOutcome.addIssue().setSeverity(theSeverity).setDetails(theProblem);
//	}

	public ValidationFailureException(FhirContext theCtx, IBaseOperationOutcome theOperationOutcome) {
		super(toDescription(theCtx, theOperationOutcome));
		myOperationOutcome = theOperationOutcome;
	}

	public IBaseOperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

}
