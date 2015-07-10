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

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.thymeleaf.util.Validate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.server.EncodingEnum;

class ValidationContext<T> implements IValidationContext<T> {

	private final IEncoder myEncoder;
	private final FhirContext myFhirContext;
	private List<SingleValidationMessage> myMessages = new ArrayList<SingleValidationMessage>();
	private final T myResource;
	private String myResourceAsString;
	private final EncodingEnum myResourceAsStringEncoding;
	private final String myResourceName;

	private ValidationContext(FhirContext theContext, T theResource, String theResourceName, IEncoder theEncoder) {
		myFhirContext = theContext;
		myResource = theResource;
		myEncoder = theEncoder;
		myResourceName = theResourceName;
		if (theEncoder != null) {
			myResourceAsStringEncoding = theEncoder.getEncoding();
		} else {
			myResourceAsStringEncoding = null;
		}
	}

	@Override
	public void addValidationMessage(SingleValidationMessage theMessage) {
		Validate.notNull(theMessage, "theMessage must not be null");
		myMessages.add(theMessage);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	public T getResource() {
		return myResource;
	}

	@Override
	public String getResourceAsString() {
		if (myResourceAsString == null) {
			myResourceAsString = myEncoder.encode();
		}
		return myResourceAsString;
	}

	@Override
	public EncodingEnum getResourceAsStringEncoding() {
		return myResourceAsStringEncoding;
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public ValidationResult toResult() {
		return new ValidationResult(myFhirContext, myMessages);
	}

	public static IValidationContext<Bundle> forBundle(final FhirContext theContext, final Bundle theBundle) {
		String resourceName = "Bundle";
		return new ValidationContext<Bundle>(theContext, theBundle, resourceName, new IEncoder() {
			@Override
			public String encode() {
				return theContext.newXmlParser().encodeBundleToString(theBundle);
			}

			@Override
			public EncodingEnum getEncoding() {
				return EncodingEnum.XML;
			}
		});
	}

	public static <T extends IBaseResource> IValidationContext<T> forResource(final FhirContext theContext, final T theResource) {
		String resourceName = theContext.getResourceDefinition(theResource).getName();
		return new ValidationContext<T>(theContext, theResource, resourceName, new IEncoder() {
			@Override
			public String encode() {
				return theContext.newXmlParser().encodeResourceToString(theResource);
			}

			@Override
			public EncodingEnum getEncoding() {
				return EncodingEnum.XML;
			}
		});
	}

	public static IValidationContext<IBaseResource> newChild(final IValidationContext<Bundle> theContext, final IResource theResource) {
		return new IValidationContext<IBaseResource>() {

			@Override
			public void addValidationMessage(SingleValidationMessage theMessage) {
				theContext.addValidationMessage(theMessage);
			}

			@Override
			public FhirContext getFhirContext() {
				return theContext.getFhirContext();
			}

			@Override
			public IBaseResource getResource() {
				return theResource;
			}

			@Override
			public String getResourceAsString() {
				return theContext.getFhirContext().newXmlParser().encodeResourceToString(theResource);
			}

			@Override
			public EncodingEnum getResourceAsStringEncoding() {
				return EncodingEnum.XML;
			}

			@Override
			public String getResourceName() {
				return theContext.getFhirContext().getResourceDefinition(theResource).getName();
			}

			@Override
			public ValidationResult toResult() {
				return theContext.toResult();
			}
		};
	}

	private interface IEncoder {
		String encode();

		EncodingEnum getEncoding();
	}

}
