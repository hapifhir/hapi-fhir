package ca.uhn.fhir.validation;

import java.util.ArrayList;
import java.util.List;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ObjectUtil;

public class ValidationContext<T> extends BaseValidationContext<T> implements IValidationContext<T> {

	private final IEncoder myEncoder;
	private final T myResource;
	private String myResourceAsString;
	private final EncodingEnum myResourceAsStringEncoding;

	private ValidationContext(FhirContext theContext, T theResource, IEncoder theEncoder) {
		this(theContext, theResource, theEncoder, new ArrayList<SingleValidationMessage>());
	}
	
	private ValidationContext(FhirContext theContext, T theResource, IEncoder theEncoder, List<SingleValidationMessage> theMessages) {
		super(theContext, theMessages);
		myResource = theResource;
		myEncoder = theEncoder;
		if (theEncoder != null) {
			myResourceAsStringEncoding = theEncoder.getEncoding();
		} else {
			myResourceAsStringEncoding = null;
		}
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

	public static <T extends IBaseResource> IValidationContext<T> forResource(final FhirContext theContext, final T theResource) {
		return new ValidationContext<T>(theContext, theResource, new IEncoder() {
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

	private interface IEncoder {
		String encode();

		EncodingEnum getEncoding();
	}

	public static IValidationContext<IBaseResource> forText(final FhirContext theContext, final String theResourceBody) {
		ObjectUtil.requireNonNull(theContext, "theContext can not be null");
		ObjectUtil.requireNotEmpty(theResourceBody, "theResourceBody can not be null or empty");
		return new BaseValidationContext<IBaseResource>(theContext) {

			private EncodingEnum myEncoding;
			private IBaseResource myParsed;

			@Override
			public IBaseResource getResource() {
				if (myParsed == null) {
					IParser parser = getResourceAsStringEncoding().newParser(getFhirContext());
					LenientErrorHandler errorHandler = new LenientErrorHandler();
					errorHandler.setErrorOnInvalidValue(false);
					parser.setParserErrorHandler(errorHandler);
					myParsed = parser.parseResource(getResourceAsString());
				}
				return myParsed;
			}

			@Override
			public String getResourceAsString() {
				return theResourceBody;
			}

			@Override
			public EncodingEnum getResourceAsStringEncoding() {
				if (myEncoding == null) {
					myEncoding = EncodingEnum.detectEncodingNoDefault(theResourceBody);
					if (myEncoding == null) {
						throw new InvalidRequestException(theContext.getLocalizer().getMessage(ValidationContext.class, "unableToDetermineEncoding"));
					}
				}
				return myEncoding;
			}

		};
	}

	public static IValidationContext<IBaseResource> subContext(final IValidationContext<IBaseResource> theCtx, final IBaseResource theResource) {
		return new ValidationContext<IBaseResource>(theCtx.getFhirContext(), theResource, new IEncoder() {
			@Override
			public String encode() {
				return theCtx.getFhirContext().newXmlParser().encodeResourceToString(theResource);
			}

			@Override
			public EncodingEnum getEncoding() {
				return EncodingEnum.XML;
			}
		}, theCtx.getMessages());
	}
}
