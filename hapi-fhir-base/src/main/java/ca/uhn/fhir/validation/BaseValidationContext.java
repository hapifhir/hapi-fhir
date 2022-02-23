package ca.uhn.fhir.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.ObjectUtil;

abstract class BaseValidationContext<T> implements IValidationContext<T> {

	protected final FhirContext myFhirContext;

	private List<SingleValidationMessage> myMessages;

	/**
	 * Constructor
	 */
	BaseValidationContext(FhirContext theFhirContext) {
		this(theFhirContext, new ArrayList<>());
	}

	/**
	 * Constructor
	 */
	BaseValidationContext(FhirContext theFhirContext, List<SingleValidationMessage> theMessages) {
		myFhirContext = theFhirContext;
		myMessages = theMessages;
	}

	@Override
	public void addValidationMessage(SingleValidationMessage theMessage) {
		ObjectUtil.requireNonNull(theMessage, "theMessage must not be null");
		myMessages.add(theMessage);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public List<SingleValidationMessage> getMessages() {
		return myMessages;
	}

	@Override
	public ValidationResult toResult() {
		return new ValidationResult(myFhirContext, myMessages);
	}

}
