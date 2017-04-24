package ca.uhn.fhir.rest.client;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.method.HttpGetClientInvocation;
import ca.uhn.fhir.rest.method.IClientResponseHandler;
import ca.uhn.fhir.rest.method.IClientResponseHandlerHandlesBinary;
import ca.uhn.fhir.rest.method.MethodUtil;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.XmlUtil;

public abstract class BaseClient implements IRestfulClient {

	/**
	 * This property is used by unit tests - do not rely on it in production code
	 * as it may change at any time. If you want to capture responses in a reliable
	 * way in your own code, just use client interceptors
	 */
	static final String HAPI_CLIENT_KEEPRESPONSES = "hapi.client.keepresponses";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseClient.class);

	private final IHttpClient myClient;
	private boolean myDontValidateConformance;
	private EncodingEnum myEncoding = null; // default unspecified (will be XML)
	private final RestfulClientFactory myFactory;
	private List<IClientInterceptor> myInterceptors = new ArrayList<IClientInterceptor>();
	private boolean myKeepResponses = false;
	private IHttpResponse myLastResponse;
	private String myLastResponseBody;
	private Boolean myPrettyPrint = false;
	private SummaryEnum mySummary;
	private final String myUrlBase;

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
		
		if (XmlUtil.isStaxPresent() == false) {
			myEncoding = EncodingEnum.JSON;
		}
		
	}

	protected Map<String, List<String>> createExtraParams() {
		HashMap<String, List<String>> retVal = new LinkedHashMap<String, List<String>>();

		if (getEncoding() == EncodingEnum.XML) {
			retVal.put(Constants.PARAM_FORMAT, Collections.singletonList("xml"));
		} else if (getEncoding() == EncodingEnum.JSON) {
			retVal.put(Constants.PARAM_FORMAT, Collections.singletonList("json"));
		}

		if (isPrettyPrint()) {
			retVal.put(Constants.PARAM_PRETTY, Collections.singletonList(Constants.PARAM_PRETTY_VALUE_TRUE));
		}

		return retVal;
	}

	@Override
	public <T extends IBaseResource> T fetchResourceFromUrl(Class<T> theResourceType, String theUrl) {
		BaseHttpClientInvocation clientInvocation = new HttpGetClientInvocation(getFhirContext(), theUrl);
		ResourceResponseHandler<T> binding = new ResourceResponseHandler<T>(theResourceType);
		return invokeClient(getFhirContext(), binding, clientInvocation, null, false, false, null, null);
	}

	void forceConformanceCheck() {
		myFactory.validateServerBase(myUrlBase, myClient, this);
	}

	/**
	 * Returns the encoding that will be used on requests. Default is <code>null</code>, which means the client will not
	 * explicitly request an encoding. (This is standard behaviour according to the FHIR specification)
	 */
	public EncodingEnum getEncoding() {
		return myEncoding;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IHttpClient getHttpClient() {
		return myClient;
	}

	public List<IClientInterceptor> getInterceptors() {
		return Collections.unmodifiableList(myInterceptors);
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

	public String getUrlBase() {
		return myUrlBase;
	}

	<T> T invokeClient(FhirContext theContext, IClientResponseHandler<T> binding, BaseHttpClientInvocation clientInvocation) {
		return invokeClient(theContext, binding, clientInvocation, false);
	}

	<T> T invokeClient(FhirContext theContext, IClientResponseHandler<T> binding, BaseHttpClientInvocation clientInvocation, boolean theLogRequestAndResponse) {
		return invokeClient(theContext, binding, clientInvocation, null, null, theLogRequestAndResponse, null, null);
	}

	<T> T invokeClient(FhirContext theContext, IClientResponseHandler<T> binding, BaseHttpClientInvocation clientInvocation, EncodingEnum theEncoding, Boolean thePrettyPrint, boolean theLogRequestAndResponse, SummaryEnum theSummaryMode, Set<String> theSubsetElements) {

		if (!myDontValidateConformance) {
			myFactory.validateServerBaseIfConfiguredToDoSo(myUrlBase, myClient, this);
		}

		// TODO: handle non 2xx status codes by throwing the correct exception,
		// and ensure it's passed upwards
		IHttpRequest httpRequest = null;
		IHttpResponse response = null;
		try {
			Map<String, List<String>> params = createExtraParams();

			if (clientInvocation instanceof HttpGetClientInvocation) {
				if (theEncoding == EncodingEnum.XML) {
					params.put(Constants.PARAM_FORMAT, Collections.singletonList("xml"));
				} else if (theEncoding == EncodingEnum.JSON) {
					params.put(Constants.PARAM_FORMAT, Collections.singletonList("json"));
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

			if (theLogRequestAndResponse) {
				ourLog.info("Client invoking: {}", httpRequest);
				String body = httpRequest.getRequestBodyFromStream();
				if (body != null) {
					ourLog.info("Client request body: {}", body);
				}
			}

			for (IClientInterceptor nextInterceptor : myInterceptors) {
				nextInterceptor.interceptRequest(httpRequest);
			}

			response = httpRequest.execute();

			for (IClientInterceptor nextInterceptor : myInterceptors) {
				nextInterceptor.interceptResponse(response);
			}

			String mimeType;
			if (Constants.STATUS_HTTP_204_NO_CONTENT == response.getStatus()) {
				mimeType = null;
			} else {
				mimeType = response.getMimeType();
			}

			Map<String, List<String>> headers = response.getAllHeaders();

			if (response.getStatus() < 200 || response.getStatus() > 299) {
				String body = null;
				Reader reader = null;
				try {
					reader = response.createReader();
					body = IOUtils.toString(reader);
				} catch (Exception e) {
					ourLog.debug("Failed to read input stream", e);
				} finally {
					IOUtils.closeQuietly(reader);
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
					InputStream reader = response.readEntity();
					try {
						return handlesBinary.invokeClient(mimeType, reader, response.getStatus(), headers);
					} finally {
						IOUtils.closeQuietly(reader);
					}
				}
			}

			Reader reader = response.createReader();

			if (ourLog.isTraceEnabled() || myKeepResponses || theLogRequestAndResponse) {
				String responseString = IOUtils.toString(reader);
				keepResponseAndLogIt(theLogRequestAndResponse, response, responseString);
				reader = new StringReader(responseString);
			}

			try {
				return binding.invokeClient(mimeType, reader, response.getStatus(), headers);
			} finally {
				IOUtils.closeQuietly(reader);
			}

		} catch (DataFormatException e) {
			//FIXME potential null access on httpResquest
			String msg = getFhirContext().getLocalizer().getMessage(BaseClient.class, "failedToParseResponse", httpRequest.getHttpVerbName(), httpRequest.getUri(), e.toString());
			throw new FhirClientConnectionException(msg, e);
		} catch (IllegalStateException e) {
			throw new FhirClientConnectionException(e);
		} catch (IOException e) {
			String msg = getFhirContext().getLocalizer().getMessage(BaseClient.class, "ioExceptionDuringOperation", httpRequest.getHttpVerbName(), httpRequest.getUri(), e.toString());
			throw new FhirClientConnectionException(msg, e);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new FhirClientConnectionException(e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public boolean isKeepResponses() {
		return myKeepResponses;
	}

	/**
	 * Returns the pretty print flag, which is a request to the server for it to return "pretty printed" responses. Note
	 * that this is currently a non-standard flag (_pretty) which is supported only by HAPI based servers (and any other
	 * servers which might implement it).
	 */
	public boolean isPrettyPrint() {
		return Boolean.TRUE.equals(myPrettyPrint);
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
				ourLog.info("Client response: {}", message, responseString);
			}
		} else {
			ourLog.trace("FHIR response:\n{}\n{}", response, responseString);
		}
	}

	@Override
	public void registerInterceptor(IClientInterceptor theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		myInterceptors.add(theInterceptor);
	}

	/**
	 * This method is an internal part of the HAPI API and may change, use with caution. If you want to disable the
	 * loading of conformance statements, use
	 * {@link IRestfulClientFactory#setServerValidationModeEnum(ServerValidationModeEnum)}
	 */
	public void setDontValidateConformance(boolean theDontValidateConformance) {
		myDontValidateConformance = theDontValidateConformance;
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
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setKeepResponses(boolean theKeepResponses) {
		myKeepResponses = theKeepResponses;
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

	@Override
	public void setSummary(SummaryEnum theSummary) {
		mySummary = theSummary;
	}

	@Override
	public void unregisterInterceptor(IClientInterceptor theInterceptor) {
		Validate.notNull(theInterceptor, "Interceptor can not be null");
		myInterceptors.remove(theInterceptor);
	}
	static ArrayList<Class<? extends IBaseResource>> toTypeList(Class<? extends IBaseResource> thePreferResponseType) {
		ArrayList<Class<? extends IBaseResource>> preferResponseTypes = null;
		if (thePreferResponseType != null) {
			preferResponseTypes = new ArrayList<Class<? extends IBaseResource>>(1);
			preferResponseTypes.add(thePreferResponseType);
		}
		return preferResponseTypes;
	}

	protected final class ResourceResponseHandler<T extends IBaseResource> implements IClientResponseHandler<T> {

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
		public T invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
			EncodingEnum respType = EncodingEnum.forContentType(theResponseMimeType);
			if (respType == null) {
				if (myAllowHtmlResponse && theResponseMimeType.toLowerCase().contains(Constants.CT_HTML) && myReturnType != null) {
					return readHtmlResponse(theResponseReader);
				}
				throw NonFhirResponseException.newInstance(theResponseStatusCode, theResponseMimeType, theResponseReader);
			}
			IParser parser = respType.newParser(getFhirContext());
      parser.setServerBaseUrl(getUrlBase());
			if (myPreferResponseTypes != null) {
				parser.setPreferTypes(myPreferResponseTypes);
			}
			T retVal = parser.parseResource(myReturnType, theResponseReader);

			MethodUtil.parseClientRequestResourceHeaders(myId, theHeaders, retVal);

			return retVal;
		}

		@SuppressWarnings("unchecked")
		private T readHtmlResponse(Reader theResponseReader) {
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
				divInstance.setValueAsString(IOUtils.toString(theResponseReader));
			} catch (Exception e) {
				throw new InvalidResponseException(400, "Failed to process HTML response from server: " + e.getMessage(), e);
			}
			divChild.getMutator().addValue(textInstance, divInstance);
			return (T) instance;
		}

		public void setPreferResponseTypes(List<Class<? extends IBaseResource>> thePreferResponseTypes) {
			myPreferResponseTypes = thePreferResponseTypes;
		}
	}

}
