package ca.uhn.fhir.rest.client.impl;

/*
 * #%L
 * HAPI FHIR - Client Framework
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestFormatParamStyleEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import ca.uhn.fhir.rest.client.method.HttpGetClientInvocation;
import ca.uhn.fhir.rest.client.method.IClientResponseHandler;
import ca.uhn.fhir.rest.client.method.IClientResponseHandlerHandlesBinary;
import ca.uhn.fhir.rest.client.method.MethodUtil;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.BinaryUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.XmlDetectionUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseClient implements IRestfulClient {

	/**
	 * This property is used by unit tests - do not rely on it in production code
	 * as it may change at any time. If you want to capture responses in a reliable
	 * way in your own code, just use client interceptors
	 */
	public static final String HAPI_CLIENT_KEEPRESPONSES = "hapi.client.keepresponses";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseClient.class);

	private final IHttpClient myClient;
	private final RestfulClientFactory myFactory;
	private final String myUrlBase;
	private boolean myDontValidateConformance;
	private EncodingEnum myEncoding = null; // default unspecified (will be JSON)
	private boolean myKeepResponses = false;
	private IHttpResponse myLastResponse;
	private String myLastResponseBody;
	private Boolean myPrettyPrint = false;
	private SummaryEnum mySummary;
	private RequestFormatParamStyleEnum myRequestFormatParamStyle = RequestFormatParamStyleEnum.SHORT;
	private IInterceptorService myInterceptorService;

	BaseClient(IHttpClient theClient, String theUrlBase, RestfulClientFactory theFactory) {
		super();
		myClient = theClient;
		myUrlBase = theUrlBase;
		myFactory = theFactory;

		/*
		 * This property is used by unit tests - do not rely on it in production code
		 * as it may change at any time. If you want to capture responses in a reliable
		 * way in your own code, just use client interceptors
		 */
		if ("true".equals(System.getProperty(HAPI_CLIENT_KEEPRESPONSES))) {
			setKeepResponses(true);
		}

		if (XmlDetectionUtil.isStaxPresent() == false) {
			myEncoding = EncodingEnum.JSON;
		}

		setInterceptorService(new InterceptorService());
	}

	@Override
	public IInterceptorService getInterceptorService() {
		return myInterceptorService;
	}

	@Override
	public void setInterceptorService(@Nonnull IInterceptorService theInterceptorService) {
		Validate.notNull(theInterceptorService, "theInterceptorService must not be null");
		myInterceptorService = theInterceptorService;
	}

	protected Map<String, List<String>> createExtraParams(String theCustomAcceptHeader) {
		HashMap<String, List<String>> retVal = new LinkedHashMap<>();

		if (isBlank(theCustomAcceptHeader)) {
			if (myRequestFormatParamStyle == RequestFormatParamStyleEnum.SHORT) {
				if (getEncoding() == EncodingEnum.XML) {
					retVal.put(Constants.PARAM_FORMAT, Collections.singletonList("xml"));
				} else if (getEncoding() == EncodingEnum.JSON) {
					retVal.put(Constants.PARAM_FORMAT, Collections.singletonList("json"));
				}
			}
		}

		if (isPrettyPrint()) {
			retVal.put(Constants.PARAM_PRETTY, Collections.singletonList(Constants.PARAM_PRETTY_VALUE_TRUE));
		}

		return retVal;
	}

	@Override
	public <T extends IBaseResource> T fetchResourceFromUrl(Class<T> theResourceType, String theUrl) {
		BaseHttpClientInvocation clientInvocation = new HttpGetClientInvocation(getFhirContext(), theUrl);
		ResourceResponseHandler<T> binding = new ResourceResponseHandler<>(theResourceType);
		return invokeClient(getFhirContext(), binding, clientInvocation, null, false, false, null, null, null, null, null);
	}

	void forceConformanceCheck() {
		myFactory.validateServerBase(myUrlBase, myClient, this);
	}

	@Override
	public EncodingEnum getEncoding() {
		return myEncoding;
	}

	/**
	 * Sets the encoding that will be used on requests. Default is <code>null</code>, which means the client will not
	 * explicitly request an encoding. (This is perfectly acceptable behaviour according to the FHIR specification. In
	 * this case, the server will choose which encoding to return, and the client can handle either XML or JSON)
	 */
	@Override
	public void setEncoding(EncodingEnum theEncoding) {
		myEncoding = theEncoding;
		// return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IHttpClient getHttpClient() {
		return myClient;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public IHttpResponse getLastResponse() {
		return myLastResponse;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public String getLastResponseBody() {
		return myLastResponseBody;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServerBase() {
		return myUrlBase;
	}

	public SummaryEnum getSummary() {
		return mySummary;
	}

	@Override
	public void setSummary(SummaryEnum theSummary) {
		mySummary = theSummary;
	}

	public String getUrlBase() {
		return myUrlBase;
	}

	@Override
	public void setFormatParamStyle(RequestFormatParamStyleEnum theRequestFormatParamStyle) {
		Validate.notNull(theRequestFormatParamStyle, "theRequestFormatParamStyle must not be null");
		myRequestFormatParamStyle = theRequestFormatParamStyle;
	}

	protected <T> T invokeClient(FhirContext theContext, IClientResponseHandler<T> binding, BaseHttpClientInvocation clientInvocation) {
		return invokeClient(theContext, binding, clientInvocation, false);
	}

	protected <T> T invokeClient(FhirContext theContext, IClientResponseHandler<T> binding, BaseHttpClientInvocation clientInvocation, boolean theLogRequestAndResponse) {
		return invokeClient(theContext, binding, clientInvocation, null, null, theLogRequestAndResponse, null, null, null, null, null);
	}

	protected <T> T invokeClient(FhirContext theContext, IClientResponseHandler<T> binding, BaseHttpClientInvocation clientInvocation, EncodingEnum theEncoding, Boolean thePrettyPrint,
							 boolean theLogRequestAndResponse, SummaryEnum theSummaryMode, Set<String> theSubsetElements, CacheControlDirective theCacheControlDirective, String theCustomAcceptHeader,
							 Map<String, List<String>> theCustomHeaders) {

		if (!myDontValidateConformance) {
			myFactory.validateServerBaseIfConfiguredToDoSo(myUrlBase, myClient, this);
		}

		// TODO: handle non 2xx status codes by throwing the correct exception,
		// and ensure it's passed upwards
		IHttpRequest httpRequest = null;
		IHttpResponse response = null;
		try {
			Map<String, List<String>> params = createExtraParams(theCustomAcceptHeader);

			if (clientInvocation instanceof HttpGetClientInvocation) {
				if (myRequestFormatParamStyle == RequestFormatParamStyleEnum.SHORT && isBlank(theCustomAcceptHeader)) {
					if (theEncoding == EncodingEnum.XML) {
						params.put(Constants.PARAM_FORMAT, Collections.singletonList("xml"));
					} else if (theEncoding == EncodingEnum.JSON) {
						params.put(Constants.PARAM_FORMAT, Collections.singletonList("json"));
					}
				}
			}

			if (theSummaryMode != null) {
				params.put(Constants.PARAM_SUMMARY, Collections.singletonList(theSummaryMode.getCode()));
			} else if (mySummary != null) {
				params.put(Constants.PARAM_SUMMARY, Collections.singletonList(mySummary.getCode()));
			}

			if (thePrettyPrint == Boolean.TRUE) {
				params.put(Constants.PARAM_PRETTY, Collections.singletonList(Constants.PARAM_PRETTY_VALUE_TRUE));
			}

			if (theSubsetElements != null && theSubsetElements.isEmpty() == false) {
				params.put(Constants.PARAM_ELEMENTS, Collections.singletonList(StringUtils.join(theSubsetElements, ',')));
			}

			EncodingEnum encoding = getEncoding();
			if (theEncoding != null) {
				encoding = theEncoding;
			}

			httpRequest = clientInvocation.asHttpRequest(myUrlBase, params, encoding, thePrettyPrint);

			if (isNotBlank(theCustomAcceptHeader)) {
				httpRequest.removeHeaders(Constants.HEADER_ACCEPT);
				httpRequest.addHeader(Constants.HEADER_ACCEPT, theCustomAcceptHeader);
			}

			if (theCacheControlDirective != null) {
				StringBuilder b = new StringBuilder();
				addToCacheControlHeader(b, Constants.CACHE_CONTROL_NO_CACHE, theCacheControlDirective.isNoCache());
				addToCacheControlHeader(b, Constants.CACHE_CONTROL_NO_STORE, theCacheControlDirective.isNoStore());
				if (theCacheControlDirective.getMaxResults() != null) {
					addToCacheControlHeader(b, Constants.CACHE_CONTROL_MAX_RESULTS + "=" + theCacheControlDirective.getMaxResults().intValue(), true);
				}
				if (b.length() > 0) {
					httpRequest.addHeader(Constants.HEADER_CACHE_CONTROL, b.toString());
				}
			}

			if (theLogRequestAndResponse) {
				ourLog.info("Client invoking: {}", httpRequest);
				String body = httpRequest.getRequestBodyFromStream();
				if (body != null) {
					ourLog.info("Client request body: {}", body);
				}
			}

			if (theCustomHeaders != null) {
				AdditionalRequestHeadersInterceptor interceptor = new AdditionalRequestHeadersInterceptor(theCustomHeaders);
				interceptor.interceptRequest(httpRequest);
			}

			HookParams requestParams = new HookParams();
			requestParams.add(IHttpRequest.class, httpRequest);
			requestParams.add(IRestfulClient.class, this);
			getInterceptorService().callHooks(Pointcut.CLIENT_REQUEST, requestParams);

			response = httpRequest.execute();

			HookParams responseParams = new HookParams();
			responseParams.add(IHttpRequest.class, httpRequest);
			responseParams.add(IHttpResponse.class, response);
			responseParams.add(IRestfulClient.class, this);
			getInterceptorService().callHooks(Pointcut.CLIENT_RESPONSE, responseParams);

			String mimeType;
			if (Constants.STATUS_HTTP_204_NO_CONTENT == response.getStatus()) {
				mimeType = null;
			} else {
				mimeType = response.getMimeType();
			}

			Map<String, List<String>> headers = response.getAllHeaders();

			if (response.getStatus() < 200 || response.getStatus() > 299) {
				String body = null;
				try (Reader reader = response.createReader()) {
					body = IOUtils.toString(reader);
				} catch (Exception e) {
					ourLog.debug("Failed to read input stream", e);
				}

				String message = "HTTP " + response.getStatus() + " " + response.getStatusInfo();
				IBaseOperationOutcome oo = null;
				if (Constants.CT_TEXT.equals(mimeType)) {
					message = message + ": " + body;
				} else {
					EncodingEnum enc = EncodingEnum.forContentType(mimeType);
					if (enc != null) {
						IParser p = enc.newParser(theContext);
						try {
							// TODO: handle if something other than OO comes back
							oo = (IBaseOperationOutcome) p.parseResource(body);
							String details = OperationOutcomeUtil.getFirstIssueDetails(getFhirContext(), oo);
							if (isNotBlank(details)) {
								message = message + ": " + details;
							}
						} catch (Exception e) {
							ourLog.debug("Failed to process OperationOutcome response");
						}
					}
				}

				keepResponseAndLogIt(theLogRequestAndResponse, response, body);

				BaseServerResponseException exception = BaseServerResponseException.newInstance(response.getStatus(), message);
				exception.setOperationOutcome(oo);

				if (body != null) {
					exception.setResponseBody(body);
				}

				throw exception;
			}
			if (binding instanceof IClientResponseHandlerHandlesBinary) {
				IClientResponseHandlerHandlesBinary<T> handlesBinary = (IClientResponseHandlerHandlesBinary<T>) binding;
				if (handlesBinary.isBinary()) {
					try (InputStream reader = response.readEntity()) {
						return handlesBinary.invokeClientForBinary(mimeType, reader, response.getStatus(), headers);
					}
				}
			}

			try (InputStream inputStream = response.readEntity()) {
				InputStream inputStreamToReturn = inputStream;

				if (ourLog.isTraceEnabled() || myKeepResponses || theLogRequestAndResponse) {
					if (inputStream != null) {
						String responseString = IOUtils.toString(inputStream, Charsets.UTF_8);
						keepResponseAndLogIt(theLogRequestAndResponse, response, responseString);
						inputStreamToReturn = new ByteArrayInputStream(responseString.getBytes(Charsets.UTF_8));
					}
				}

				if (inputStreamToReturn == null) {
					inputStreamToReturn = new ByteArrayInputStream(new byte[]{});
				}

				return binding.invokeClient(mimeType, inputStreamToReturn, response.getStatus(), headers);
			}

		} catch (DataFormatException e) {
			String msg;
			if (httpRequest != null) {
				msg = getFhirContext().getLocalizer().getMessage(BaseClient.class, "failedToParseResponse", httpRequest.getHttpVerbName(), httpRequest.getUri(), e.toString());
			} else {
				msg = getFhirContext().getLocalizer().getMessage(BaseClient.class, "failedToParseResponse", "UNKNOWN", "UNKNOWN", e.toString());
			}
			throw new FhirClientConnectionException(Msg.code(1359) + msg, e);
		} catch (IllegalStateException e) {
			throw new FhirClientConnectionException(Msg.code(1360) + e);
		} catch (IOException e) {
			String msg;
			msg = getFhirContext().getLocalizer().getMessage(BaseClient.class, "failedToParseResponse", httpRequest.getHttpVerbName(), httpRequest.getUri(), e.toString());
			throw new FhirClientConnectionException(Msg.code(1361) + msg, e);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new FhirClientConnectionException(Msg.code(1362) + e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	private void addToCacheControlHeader(StringBuilder theBuilder, String theDirective, boolean theActive) {
		if (theActive) {
			if (theBuilder.length() > 0) {
				theBuilder.append(", ");
			}
			theBuilder.append(theDirective);
		}
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public boolean isKeepResponses() {
		return myKeepResponses;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setKeepResponses(boolean theKeepResponses) {
		myKeepResponses = theKeepResponses;
	}

	/**
	 * Returns the pretty print flag, which is a request to the server for it to return "pretty printed" responses. Note
	 * that this is currently a non-standard flag (_pretty) which is supported only by HAPI based servers (and any other
	 * servers which might implement it).
	 */
	public boolean isPrettyPrint() {
		return Boolean.TRUE.equals(myPrettyPrint);
	}

	/**
	 * Sets the pretty print flag, which is a request to the server for it to return "pretty printed" responses. Note
	 * that this is currently a non-standard flag (_pretty) which is supported only by HAPI based servers (and any other
	 * servers which might implement it).
	 */
	@Override
	public void setPrettyPrint(Boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		// return this;
	}

	private void keepResponseAndLogIt(boolean theLogRequestAndResponse, IHttpResponse response, String responseString) {
		if (myKeepResponses) {
			myLastResponse = response;
			myLastResponseBody = responseString;
		}
		if (theLogRequestAndResponse) {
			String message = "HTTP " + response.getStatus() + " " + response.getStatusInfo();
			if (StringUtils.isNotBlank(responseString)) {
				ourLog.info("Client response: {}\n{}", message, responseString);
			} else {
				ourLog.info("Client response: {}", message);
			}
		} else {
			ourLog.trace("FHIR response:\n{}\n{}", response, responseString);
		}
	}

	@Override
	public void registerInterceptor(Object theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		getInterceptorService().registerInterceptor(theInterceptor);
	}

	/**
	 * This method is an internal part of the HAPI API and may change, use with caution. If you want to disable the
	 * loading of conformance statements, use
	 * {@link IRestfulClientFactory#setServerValidationMode(ServerValidationModeEnum)}
	 */
	public void setDontValidateConformance(boolean theDontValidateConformance) {
		myDontValidateConformance = theDontValidateConformance;
	}

	@Override
	public void unregisterInterceptor(Object theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		getInterceptorService().unregisterInterceptor(theInterceptor);
	}

	protected final class ResourceOrBinaryResponseHandler extends ResourceResponseHandler<IBaseResource> {


		@Override
		public IBaseResource invokeClient(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {

			/*
			 * For operation responses, if the response content type is a FHIR content-type
			 * (which is will probably almost always be) we just handle it normally. However,
			 * if we get back a successful (2xx) response from an operation, and the content
			 * type is something other than FHIR, we'll return it as a Binary wrapped in
			 * a Parameters resource.
			 */
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType != null || theResponseStatusCode < 200 || theResponseStatusCode >= 300) {
				return super.invokeClient(theResponseMimeType, theResponseInputStream, theResponseStatusCode, theHeaders);
			}

			// Create a Binary resource to return
			IBaseBinary responseBinary = BinaryUtil.newBinary(getFhirContext());

			// Fetch the content type
			String contentType = null;
			List<String> contentTypeHeaders = theHeaders.get(Constants.HEADER_CONTENT_TYPE_LC);
			if (contentTypeHeaders != null && contentTypeHeaders.size() > 0) {
				contentType = contentTypeHeaders.get(0);
			}
			responseBinary.setContentType(contentType);

			// Fetch the content itself
			try {
				responseBinary.setContent(IOUtils.toByteArray(theResponseInputStream));
			} catch (IOException e) {
				throw new InternalErrorException(Msg.code(1363) + "IO failure parsing response", e);
			}

			return responseBinary;
		}

	}

	protected class ResourceResponseHandler<T extends IBaseResource> implements IClientResponseHandler<T> {

		private boolean myAllowHtmlResponse;
		private IIdType myId;
		private List<Class<? extends IBaseResource>> myPreferResponseTypes;
		private Class<T> myReturnType;

		public ResourceResponseHandler() {
			this(null);
		}

		public ResourceResponseHandler(Class<T> theReturnType) {
			this(theReturnType, null, null);
		}

		public ResourceResponseHandler(Class<T> theReturnType, Class<? extends IBaseResource> thePreferResponseType, IIdType theId) {
			this(theReturnType, thePreferResponseType, theId, false);
		}

		public ResourceResponseHandler(Class<T> theReturnType, Class<? extends IBaseResource> thePreferResponseType, IIdType theId, boolean theAllowHtmlResponse) {
			this(theReturnType, toTypeList(thePreferResponseType), theId, theAllowHtmlResponse);
		}

		public ResourceResponseHandler(Class<T> theClass, List<Class<? extends IBaseResource>> thePreferResponseTypes) {
			this(theClass, thePreferResponseTypes, null, false);
		}

		public ResourceResponseHandler(Class<T> theReturnType, List<Class<? extends IBaseResource>> thePreferResponseTypes, IIdType theId, boolean theAllowHtmlResponse) {
			myReturnType = theReturnType;
			myId = theId;
			myPreferResponseTypes = thePreferResponseTypes;
			myAllowHtmlResponse = theAllowHtmlResponse;
		}

		@Override
		public T invokeClient(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
			if (theResponseStatusCode == Constants.STATUS_HTTP_204_NO_CONTENT) {
				return null;
			}

			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				if (myAllowHtmlResponse && theResponseMimeType.toLowerCase().contains(Constants.CT_HTML) && myReturnType != null) {
					return readHtmlResponse(theResponseInputStream);
				}
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseInputStream);
			}
			IParser parser = respType.newParser(getFhirContext());
			parser.setServerBaseUrl(getUrlBase());
			if (myPreferResponseTypes != null) {
				parser.setPreferTypes(myPreferResponseTypes);
			}
			T retVal = parser.parseResource(myReturnType, theResponseInputStream);

			MethodUtil.parseClientRequestResourceHeaders(myId, theHeaders, retVal);

			return retVal;
		}

		@SuppressWarnings("unchecked")
		private T readHtmlResponse(InputStream theResponseInputStream) {
			RuntimeResourceDefinition resDef = getFhirContext().getResourceDefinition(myReturnType);
			IBaseResource instance = resDef.newInstance();
			BaseRuntimeChildDefinition textChild = resDef.getChildByName("text");
			BaseRuntimeElementCompositeDefinition<?> textElement = (BaseRuntimeElementCompositeDefinition<?>) textChild.getChildByName("text");
			IBase textInstance = textElement.newInstance();
			textChild.getMutator().addValue(instance, textInstance);

			BaseRuntimeChildDefinition divChild = textElement.getChildByName("div");
			BaseRuntimeElementDefinition<?> divElement = divChild.getChildByName("div");
			IPrimitiveType<?> divInstance = (IPrimitiveType<?>) divElement.newInstance();
			try {
				divInstance.setValueAsString(IOUtils.toString(theResponseInputStream, Charsets.UTF_8));
			} catch (Exception e) {
				throw new InvalidResponseException(Msg.code(1364) + "Failed to process HTML response from server: " + e.getMessage(), 400, e);
			}
			divChild.getMutator().addValue(textInstance, divInstance);
			return (T) instance;
		}

		public ResourceResponseHandler<T> setPreferResponseTypes(List<Class<? extends IBaseResource>> thePreferResponseTypes) {
			myPreferResponseTypes = thePreferResponseTypes;
			return this;
		}
	}

	static ArrayList<Class<? extends IBaseResource>> toTypeList(Class<? extends IBaseResource> thePreferResponseType) {
		ArrayList<Class<? extends IBaseResource>> preferResponseTypes = null;
		if (thePreferResponseType != null) {
			preferResponseTypes = new ArrayList<>(1);
			preferResponseTypes.add(thePreferResponseType);
		}
		return preferResponseTypes;
	}

}
