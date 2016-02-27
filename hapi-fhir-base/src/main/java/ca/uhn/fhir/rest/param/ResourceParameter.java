package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.IParameter;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ResourceParameter implements IParameter {

	private Mode myMode;
	private Class<? extends IBaseResource> myResourceType;

	public ResourceParameter(Class<? extends IResource> theParameterType, Object theProvider, Mode theMode) {
		Validate.notNull(theParameterType, "theParameterType can not be null");
		Validate.notNull(theMode, "theMode can not be null");

		myResourceType = theParameterType;
		myMode = theMode;

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
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		// TODO Auto-generated method stub

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
			return RestfulServerUtils.determineRequestEncoding(theRequest);
		case RESOURCE:
		default:
			return parseResourceFromRequest(theRequest, theMethodBinding, myResourceType);
		}
		// }
	}

	public static Reader createRequestReader(RequestDetails theRequest, Charset charset) {
		Reader requestReader = new InputStreamReader(new ByteArrayInputStream(theRequest.loadRequestContents()), charset);
		return requestReader;
	}

	public static Reader createRequestReader(RequestDetails theRequest) {
		return createRequestReader(theRequest, determineRequestCharset(theRequest));
	}

	public static Charset determineRequestCharset(RequestDetails theRequest) {
		Charset charset =  theRequest.getCharset();
		if (charset == null) {
			charset = Charset.forName("UTF-8");
		}
		return charset;
	}

	@SuppressWarnings("unchecked")
	public static <T extends IBaseResource> T loadResourceFromRequest(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding, Class<T> theResourceType) {
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
				/*
				 * If the client didn't send a content type, try to guess
				 */
				String body;
				try {
					body = IOUtils.toString(requestReader);
				} catch (IOException e) {
					// This shouldn't happen since we're reading from a byte array..
					throw new InternalErrorException(e);
				}
				encoding = MethodUtil.detectEncodingNoDefault(body);
				if (encoding == null) {
					String msg = ctx.getLocalizer().getMessage(ResourceParameter.class, "noContentTypeInRequest", restOperationType);
					throw new InvalidRequestException(msg);
				} else {
					requestReader = new InputStreamReader(new ByteArrayInputStream(theRequest.loadRequestContents()), charset);
				}
			} else {
				String msg = ctx.getLocalizer().getMessage(ResourceParameter.class, "invalidContentTypeInRequest", ctValue, restOperationType);
				throw new InvalidRequestException(msg);
			}
		}

		IParser parser = encoding.newParser(ctx);

		T retVal;
		if (theResourceType != null) {
			retVal = parser.parseResource(theResourceType, requestReader);
		} else {
			retVal = (T) parser.parseResource(requestReader);
		}

		if (theRequest.getId() != null && theRequest.getId().hasIdPart()) {
			retVal.setId(theRequest.getId());
		}

		if (theRequest.getServer().getFhirContext().getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
			TagList tagList = new TagList();
			for (String nextTagComplete : theRequest.getHeaders(Constants.HEADER_CATEGORY)) {
				MethodUtil.parseTagValue(tagList, nextTagComplete);
			}
			if (tagList.isEmpty() == false) {
				((IResource) retVal).getResourceMetadata().put(ResourceMetadataKeyEnum.TAG_LIST, tagList);
			}
		}
		return retVal;
	}

	public static IBaseResource parseResourceFromRequest(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding, Class<? extends IBaseResource> theResourceType) {
		IBaseResource retVal = null;
		
		if (IBaseBinary.class.isAssignableFrom(theResourceType)) {
			String ct = theRequest.getHeader(Constants.HEADER_CONTENT_TYPE);
			if (EncodingEnum.forContentTypeStrict(ct) == null) {
			FhirContext ctx = theRequest.getServer().getFhirContext();
			IBaseBinary binary = (IBaseBinary) ctx.getResourceDefinition("Binary").newInstance();
			binary.setContentType(ct);
			binary.setContent(theRequest.loadRequestContents());
			retVal = binary;
			}
		}
		
		if (retVal == null) {
			retVal = loadResourceFromRequest(theRequest, theMethodBinding, theResourceType);
		}
		return retVal;
	}

	public enum Mode {
		BODY, BODY_BYTE_ARRAY, ENCODING, RESOURCE
	}

}
