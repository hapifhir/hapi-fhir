package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ObjectUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class ValidationContext<T> extends BaseValidationContext<T> implements IValidationContext<T> {

	private final IEncoder myEncoder;
	private final T myResource;
	private final EncodingEnum myResourceAsStringEncoding;
	private final ValidationOptions myOptions;
	private String myResourceAsString;

	private ValidationContext(FhirContext theContext, T theResource, IEncoder theEncoder, ValidationOptions theOptions) {
		this(theContext, theResource, theEncoder, new ArrayList<>(), theOptions);
	}

	private ValidationContext(FhirContext theContext, T theResource, IEncoder theEncoder, List<SingleValidationMessage> theMessages, ValidationOptions theOptions) {
		super(theContext, theMessages);
		myResource = theResource;
		myEncoder = theEncoder;
		myOptions = theOptions;
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

	@Nonnull
	@Override
	public ValidationOptions getOptions() {
		return myOptions;
	}

	private interface IEncoder {
		String encode();

		EncodingEnum getEncoding();
	}

	public static <T extends IBaseResource> IValidationContext<T> forResource(final FhirContext theContext, final T theResource, ValidationOptions theOptions) {
		ObjectUtil.requireNonNull(theContext, "theContext can not be null");
		ObjectUtil.requireNonNull(theResource, "theResource can not be null");
		ValidationOptions options = defaultIfNull(theOptions, ValidationOptions.empty());

		IEncoder encoder = new IEncoder() {
			@Override
			public String encode() {
				return theContext.newJsonParser().encodeResourceToString(theResource);
			}

			@Override
			public EncodingEnum getEncoding() {
				return EncodingEnum.JSON;
			}
		};
		return new ValidationContext<>(theContext, theResource, encoder, options);
	}

	public static IValidationContext<IBaseResource> forText(final FhirContext theContext, final String theResourceBody, final ValidationOptions theOptions) {
		ObjectUtil.requireNonNull(theContext, "theContext can not be null");
		ObjectUtil.requireNotEmpty(theResourceBody, "theResourceBody can not be null or empty");
		ValidationOptions options = defaultIfNull(theOptions, ValidationOptions.empty());

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
						throw new InvalidRequestException(Msg.code(1971) + theContext.getLocalizer().getMessage(ValidationContext.class, "unableToDetermineEncoding"));
					}
				}
				return myEncoding;
			}

			@Nonnull
			@Override
			public ValidationOptions getOptions() {
				return options;
			}

		};
	}

	public static IValidationContext<IBaseResource> subContext(final IValidationContext<IBaseResource> theCtx, final IBaseResource theResource, ValidationOptions theOptions) {
		return new ValidationContext<>(theCtx.getFhirContext(), theResource, new IEncoder() {
			@Override
			public String encode() {
				return theCtx.getFhirContext().newXmlParser().encodeResourceToString(theResource);
			}

			@Override
			public EncodingEnum getEncoding() {
				return EncodingEnum.XML;
			}
		}, theCtx.getMessages(), theOptions);
	}
}
