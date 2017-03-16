package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Collections;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;
import ca.uhn.fhir.util.OperationOutcomeUtil;

/**
 * Encapsulates the results of validation
 *
 * @see ca.uhn.fhir.validation.FhirValidator
 * @since 0.7
 */
public class ValidationResult {
	private final FhirContext myCtx;
	private final boolean myIsSuccessful;
	private final List<SingleValidationMessage> myMessages;

	public ValidationResult(FhirContext theCtx, List<SingleValidationMessage> theMessages) {
		boolean successful = true;
		myCtx = theCtx;
		myMessages = theMessages;
		for (SingleValidationMessage next : myMessages) {
			next.getSeverity();
			if (next.getSeverity() == null || next.getSeverity().ordinal() > ResultSeverityEnum.WARNING.ordinal()) {
				successful = false;
			}
		}
		myIsSuccessful = successful;
	}

	public List<SingleValidationMessage> getMessages() {
		return Collections.unmodifiableList(myMessages);
	}

	/**
	 * Was the validation successful (in other words, do we have no issues that are at
	 * severity {@link ResultSeverityEnum#ERROR} or {@link ResultSeverityEnum#FATAL}. A validation
	 * is still considered successful if it only has issues at level {@link ResultSeverityEnum#WARNING} or
	 * lower.
	 * 
	 * @return true if the validation was successful
	 */
	public boolean isSuccessful() {
		return myIsSuccessful;
	}

	private String toDescription() {
		StringBuilder b = new StringBuilder(100);
		if (myMessages.size() > 0) {
			if (myMessages.get(0).getSeverity() != null) {
				b.append(myMessages.get(0).getSeverity().name());
				b.append(" - ");
			}
			b.append(myMessages.get(0).getMessage());
			b.append(" - ");
			b.append(myMessages.get(0).getLocationString());
		} else {
			b.append("No issues");
		}
		return b.toString();
	}

	/**
	 * @deprecated Use {@link #toOperationOutcome()} instead since this method returns a view.
	 *             {@link #toOperationOutcome()} is identical to this method, but has a more suitable name so this method
	 *             will be removed at some point.
	 */
	@Deprecated
	public IBaseOperationOutcome getOperationOutcome() {
		return toOperationOutcome();
	}

	/**
	 * Create an OperationOutcome resource which contains all of the messages found as a result of this validation
	 */
	public IBaseOperationOutcome toOperationOutcome() {
		IBaseOperationOutcome oo = (IBaseOperationOutcome) myCtx.getResourceDefinition("OperationOutcome").newInstance();
		populateOperationOutcome(oo);
		return oo;
	}

	/**
	 * Populate an operation outcome with the results of the validation 
	 */
	public void populateOperationOutcome(IBaseOperationOutcome theOperationOutcome) {
		for (SingleValidationMessage next : myMessages) {
			String location;
			if (isNotBlank(next.getLocationString())) {
				location = next.getLocationString();
			} else if (next.getLocationLine() != null || next.getLocationCol() != null) {
				location = "Line[" + next.getLocationLine() + "] Col[" + next.getLocationCol() + "]";
			} else {
				location = null;
			}
			String severity = next.getSeverity() != null ? next.getSeverity().getCode() : null;
			OperationOutcomeUtil.addIssue(myCtx, theOperationOutcome, severity, next.getMessage(), location, ExceptionHandlingInterceptor.PROCESSING);
		}

		if (myMessages.isEmpty()) {
			String message = myCtx.getLocalizer().getMessage(ValidationResult.class, "noIssuesDetected");
			OperationOutcomeUtil.addIssue(myCtx, theOperationOutcome, "information", message, null, "informational");
		}
	}

	@Override
	public String toString() {
		return "ValidationResult{" + "messageCount=" + myMessages.size() + ", isSuccessful=" + myIsSuccessful + ", description='" + toDescription() + '\'' + '}';
	}
}
