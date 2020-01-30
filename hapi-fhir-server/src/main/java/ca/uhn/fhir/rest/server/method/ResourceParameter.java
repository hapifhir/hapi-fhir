package ca.uhn.fhir.rest.server.method;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.BinaryUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ResourceParameter implements IParameter {

	private final boolean myMethodIsOperation;
	private Mode myMode;
	private Class<? extends IBaseResource> myResourceType;

	public ResourceParameter(Class<? extends IBaseResource> theParameterType, Object theProvider, Mode theMode, boolean theMethodIsOperation) {
		Validate.notNull(theParameterType, "theParameterType can not be null");
		Validate.notNull(theMode, "theMode can not be null");

		myResourceType = theParameterType;
		myMode = theMode;
		myMethodIsOperation = theMethodIsOperation;

		Class<? extends IBaseResource> providerResourceType = null;
		if (theProvider instanceof IResourceProvider) {
			providerResourceType = ((IResourceProvider) theProvider).getResourceType();
		}

		if (Modifier.isAbstract(myResourceType.getModifiers()) && providerResourceType != null) {
			myResourceType = providerResourceType;
		}

	}

	public Mode getMode() {
		return myMode;
	}

	public Class<? extends IBaseResource> getResourceType() {
		return myResourceType;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// ignore for now
	}


	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		switch (myMode) {
			case BODY:
				try {
					return IOUtils.toString(createRequestReader(theRequest));
				} catch (IOException e) {
					// Shouldn't happen since we're reading from a byte array
					throw new InternalErrorException("Failed to load request", e);
				}
			case BODY_BYTE_ARRAY:
				return theRequest.loadRequestContents();
			case ENCODING:
				return RestfulServerUtils.determineRequestEncodingNoDefault(theRequest);
			case RESOURCE:
			default:
				Class<? extends IBaseResource> resourceTypeToParse = myResourceType;
				if (myMethodIsOperation) {
					// Operations typically have a Parameters resource as the body
					resourceTypeToParse = null;
				}
				return parseResourceFromRequest(theRequest, theMethodBinding, resourceTypeToParse);
		}
		// }
	}

	public enum Mode {
		BODY, BODY_BYTE_ARRAY, ENCODING, RESOURCE
	}

	private static Reader createRequestReader(RequestDetails theRequest, Charset charset) {
		return new InputStreamReader(new ByteArrayInputStream(theRequest.loadRequestContents()), charset);
	}

	// Do not make private
	@SuppressWarnings("WeakerAccess")
	public static Reader createRequestReader(RequestDetails theRequest) {
		return createRequestReader(theRequest, determineRequestCharset(theRequest));
	}

	public static Charset determineRequestCharset(RequestDetails theRequest) {
		Charset charset = theRequest.getCharset();
		if (charset == null) {
			charset = Charset.forName("UTF-8");
		}
		return charset;
	}

	@SuppressWarnings("unchecked")
	static <T extends IBaseResource> T loadResourceFromRequest(RequestDetails theRequest, @Nonnull BaseMethodBinding<?> theMethodBinding, Class<T> theResourceType) {
		FhirContext ctx = theRequest.getServer().getFhirContext();

		final Charset charset = determineRequestCharset(theRequest);
		Reader requestReader = createRequestReader(theRequest, charset);

		RestOperationTypeEnum restOperationType = theMethodBinding != null ? theMethodBinding.getRestOperationType() : null;

		EncodingEnum encoding = RestfulServerUtils.determineRequestEncodingNoDefault(theRequest);
		if (encoding == null) {
			String ctValue = theRequest.getHeader(Constants.HEADER_CONTENT_TYPE);
			if (ctValue != null) {
				if (ctValue.startsWith("application/x-www-form-urlencoded")) {
					String msg = theRequest.getServer().getFhirContext().getLocalizer().getMessage(ResourceParameter.class, "invalidContentTypeInRequest", ctValue, theMethodBinding.getRestOperationType());
					throw new InvalidRequestException(msg);
				}
			}
			if (isBlank(ctValue)) {
				String body;
				try {
					body = IOUtils.toString(requestReader);
				} catch (IOException e) {
					// This shouldn't happen since we're reading from a byte array..
					throw new InternalErrorException(e);
				}
				if (isBlank(body)) {
					return null;
				}

				String msg = ctx.getLocalizer().getMessage(ResourceParameter.class, "noContentTypeInRequest", restOperationType);
				throw new InvalidRequestException(msg);
			} else {
				String msg = ctx.getLocalizer().getMessage(ResourceParameter.class, "invalidContentTypeInRequest", ctValue, restOperationType);
				throw new InvalidRequestException(msg);
			}
		}

		IParser parser = encoding.newParser(ctx);
		parser.setServerBaseUrl(theRequest.getFhirServerBase());
		T retVal;
		try {
			if (theResourceType != null) {
				retVal = parser.parseResource(theResourceType, requestReader);
			} else {
				retVal = (T) parser.parseResource(requestReader);
			}
		} catch (DataFormatException e) {
			String msg = ctx.getLocalizer().getMessage(ResourceParameter.class, "failedToParseRequest", encoding.name(), e.getMessage());
			throw new InvalidRequestException(msg);
		}

		return retVal;
	}

	static IBaseResource parseResourceFromRequest(RequestDetails theRequest, @Nonnull BaseMethodBinding<?> theMethodBinding, Class<? extends IBaseResource> theResourceType) {
		if (theRequest.getResource() != null) {
			return theRequest.getResource();
		}

		IBaseResource retVal = null;

		if (theResourceType != null && IBaseBinary.class.isAssignableFrom(theResourceType)) {
			String ct = theRequest.getHeader(Constants.HEADER_CONTENT_TYPE);
			if (EncodingEnum.forContentTypeStrict(ct) == null) {
				FhirContext ctx = theRequest.getServer().getFhirContext();
				IBaseBinary binary = BinaryUtil.newBinary(ctx);
				binary.setId(theRequest.getId());
				binary.setContentType(ct);
				binary.setContent(theRequest.loadRequestContents());
				retVal = binary;

				/*
				 * Security context header, which is only in
				 * DSTU3+
				 */
				if (ctx.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
					String securityContext = theRequest.getHeader(Constants.HEADER_X_SECURITY_CONTEXT);
					if (isNotBlank(securityContext)) {
						BinaryUtil.setSecurityContext(ctx, binary, securityContext);
					}
				}
			}
		}

		if (retVal == null) {
			retVal = loadResourceFromRequest(theRequest, theMethodBinding, theResourceType);
		}

		theRequest.setResource(retVal);

		return retVal;
	}

}
