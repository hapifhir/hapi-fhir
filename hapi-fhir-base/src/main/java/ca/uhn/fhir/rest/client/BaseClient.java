package ca.uhn.fhir.rest.client;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.method.IClientResponseHandler;
import ca.uhn.fhir.rest.method.IClientResponseHandlerHandlesBinary;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public abstract class BaseClient implements IRestfulClient {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseClient.class);

	private final HttpClient myClient;
	private boolean myDontValidateConformance;
	private EncodingEnum myEncoding = null; // default unspecified (will be XML)
	private final RestfulClientFactory myFactory;
	private List<IClientInterceptor> myInterceptors = new ArrayList<IClientInterceptor>();
	private boolean myKeepResponses = false;
	private HttpResponse myLastResponse;
	private String myLastResponseBody;
	private Boolean myPrettyPrint = false;
	private SummaryEnum mySummary;
	private final String myUrlBase;
	
	BaseClient(HttpClient theClient, String theUrlBase, RestfulClientFactory theFactory) {
		super();
		myClient = theClient;
		myUrlBase = theUrlBase;
		myFactory = theFactory;
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

	void forceConformanceCheck() {
		myFactory.validateServerBase(myUrlBase, myClient, this);
	}

	/**
	 * Returns the encoding that will be used on requests. Default is <code>null</code>, which means the client will not explicitly request an encoding. (This is standard behaviour according to the
	 * FHIR specification)
	 */
	public EncodingEnum getEncoding() {
		return myEncoding;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpClient getHttpClient() {
		return myClient;
	}

	public List<IClientInterceptor> getInterceptors() {
		return Collections.unmodifiableList(myInterceptors);
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public HttpResponse getLastResponse() {
		return myLastResponse;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public String getLastResponseBody() {
		return myLastResponseBody;
	}

	/**
	 * Returns the pretty print flag, which is a request to the server for it to return "pretty printed" responses. Note that this is currently a non-standard flag (_pretty) which is supported only by
	 * HAPI based servers (and any other servers which might implement it).
	 */
	public Boolean getPrettyPrint() {
		return myPrettyPrint;
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

	<T> T invokeClient(FhirContext theContext, IClientResponseHandler<T> binding, BaseHttpClientInvocation clientInvocation, EncodingEnum theEncoding, Boolean thePrettyPrint,
			boolean theLogRequestAndResponse, SummaryEnum theSummaryMode, Set<String> theSubsetElements) {

		if (!myDontValidateConformance) {
			myFactory.validateServerBaseIfConfiguredToDoSo(myUrlBase, myClient, this);
		}

		// TODO: handle non 2xx status codes by throwing the correct exception,
		// and ensure it's passed upwards
		HttpRequestBase httpRequest;
		HttpResponse response;
		try {
			Map<String, List<String>> params = createExtraParams();

			if (theEncoding == EncodingEnum.XML) {
				params.put(Constants.PARAM_FORMAT, Collections.singletonList("xml"));
			} else if (theEncoding == EncodingEnum.JSON) {
				params.put(Constants.PARAM_FORMAT, Collections.singletonList("json"));
			}
			
			if (theSummaryMode != null) {
				params.put(Constants.PARAM_SUMMARY, Collections.singletonList(theSummaryMode.getCode()));
			} else if (mySummary != null) {
				params.put(Constants.PARAM_SUMMARY, Collections.singletonList(mySummary.getCode()));
			}

			if (thePrettyPrint == Boolean.TRUE) {
				params.put(Constants.PARAM_PRETTY, Collections.singletonList(Constants.PARAM_PRETTY_VALUE_TRUE));
			}
			
			if (theSubsetElements != null && theSubsetElements.isEmpty()== false) {
				params.put(Constants.PARAM_ELEMENTS, Collections.singletonList(StringUtils.join(theSubsetElements, ',')));
			}

			EncodingEnum encoding = getEncoding();
			if (theEncoding != null) {
				encoding = theEncoding;
			}

			httpRequest = clientInvocation.asHttpRequest(myUrlBase, params, encoding, thePrettyPrint);

			if (theLogRequestAndResponse) {
				ourLog.info("Client invoking: {}", httpRequest);
				if (httpRequest instanceof HttpEntityEnclosingRequest) {
					HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
					if (entity.isRepeatable()) {
						String content = IOUtils.toString(entity.getContent());
						ourLog.info("Client request body: {}", content);
					}
				}
			}

			for (IClientInterceptor nextInterceptor : myInterceptors) {
				nextInterceptor.interceptRequest(httpRequest);
			}

			response = myClient.execute(httpRequest);

			for (IClientInterceptor nextInterceptor : myInterceptors) {
				nextInterceptor.interceptResponse(response);
			}

		} catch (DataFormatException e) {
			throw new FhirClientConnectionException(e);
		} catch (IOException e) {
			throw new FhirClientConnectionException(e);
		}

		try {
			String mimeType;
			if (Constants.STATUS_HTTP_204_NO_CONTENT == response.getStatusLine().getStatusCode()) {
				mimeType = null;
			} else {
				ContentType ct = ContentType.get(response.getEntity());
				mimeType = ct != null ? ct.getMimeType() : null;
			}

			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			if (response.getAllHeaders() != null) {
				for (Header next : response.getAllHeaders()) {
					String name = next.getName().toLowerCase();
					List<String> list = headers.get(name);
					if (list == null) {
						list = new ArrayList<String>();
						headers.put(name, list);
					}
					list.add(next.getValue());
				}
			}

			if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() > 299) {
				String body = null;
				Reader reader = null;
				try {
					reader = createReaderFromResponse(response);
					body = IOUtils.toString(reader);
				} catch (Exception e) {
					ourLog.debug("Failed to read input stream", e);
				} finally {
					IOUtils.closeQuietly(reader);
				}

				String message = "HTTP " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();
				BaseOperationOutcome oo = null;
				if (Constants.CT_TEXT.equals(mimeType)) {
					message = message + ": " + body;
				} else {
					EncodingEnum enc = EncodingEnum.forContentType(mimeType);
					if (enc != null) {
						IParser p = enc.newParser(theContext);
						try {
							// TODO: handle if something other than OO comes back
							oo = (BaseOperationOutcome) p.parseResource(body);
							if (oo.getIssueFirstRep().getDetailsElement().isEmpty() == false) {
								message = message + ": " + oo.getIssueFirstRep().getDetailsElement().getValue();
							}
						} catch (Exception e) {
							ourLog.debug("Failed to process OperationOutcome response");
						}
					}
				}

				keepResponseAndLogIt(theLogRequestAndResponse, response, body);

				BaseServerResponseException exception = BaseServerResponseException.newInstance(response.getStatusLine().getStatusCode(), message);
				exception.setOperationOutcome(oo);

				if (body != null) {
					exception.setResponseBody(body);
				}

				throw exception;
			}
			if (binding instanceof IClientResponseHandlerHandlesBinary) {
				IClientResponseHandlerHandlesBinary<T> handlesBinary = (IClientResponseHandlerHandlesBinary<T>) binding;
				if (handlesBinary.isBinary()) {
					InputStream reader = response.getEntity().getContent();
					try {

						if (ourLog.isTraceEnabled() || myKeepResponses || theLogRequestAndResponse) {
							byte[] responseBytes = IOUtils.toByteArray(reader);
							if (myKeepResponses) {
								myLastResponse = response;
								myLastResponseBody = null;
							}
							String message = "HTTP " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();
							if (theLogRequestAndResponse) {
								ourLog.info("Client response: {} - {} bytes", message, responseBytes.length);
							} else {
								ourLog.trace("Client response: {} - {} bytes", message, responseBytes.length);
							}
							reader = new ByteArrayInputStream(responseBytes);
						}

						return handlesBinary.invokeClient(mimeType, reader, response.getStatusLine().getStatusCode(), headers);
					} finally {
						IOUtils.closeQuietly(reader);
					}
				}
			}

			Reader reader = createReaderFromResponse(response);

			if (ourLog.isTraceEnabled() || myKeepResponses || theLogRequestAndResponse) {
				String responseString = IOUtils.toString(reader);
				keepResponseAndLogIt(theLogRequestAndResponse, response, responseString);
				reader = new StringReader(responseString);
			}

			try {
				return binding.invokeClient(mimeType, reader, response.getStatusLine().getStatusCode(), headers);
			} finally {
				IOUtils.closeQuietly(reader);
			}

		} catch (IllegalStateException e) {
			throw new FhirClientConnectionException(e);
		} catch (IOException e) {
			throw new FhirClientConnectionException(e);
		} finally {
			if (response instanceof CloseableHttpResponse) {
				try {
					((CloseableHttpResponse) response).close();
				} catch (IOException e) {
					ourLog.debug("Failed to close response", e);
				}
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
	 * Returns the pretty print flag, which is a request to the server for it to return "pretty printed" responses. Note that this is currently a non-standard flag (_pretty) which is supported only by
	 * HAPI based servers (and any other servers which might implement it).
	 */
	public boolean isPrettyPrint() {
		return Boolean.TRUE.equals(myPrettyPrint);
	}

	private void keepResponseAndLogIt(boolean theLogRequestAndResponse, HttpResponse response, String responseString) {
		if (myKeepResponses) {
			myLastResponse = response;
			myLastResponseBody = responseString;
		}
		if (theLogRequestAndResponse) {
			String message = "HTTP " + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase();
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
	 * This method is an internal part of the HAPI API and may change, use with caution. If you want to disable the loading of conformance statements, use
	 * {@link IRestfulClientFactory#setServerValidationModeEnum(ServerValidationModeEnum)}
	 */
	public void setDontValidateConformance(boolean theDontValidateConformance) {
		myDontValidateConformance = theDontValidateConformance;
	}

	/**
	 * Sets the encoding that will be used on requests. Default is <code>null</code>, which means the client will not explicitly request an encoding. (This is perfectly acceptable behaviour according
	 * to the FHIR specification. In this case, the server will choose which encoding to return, and the client can handle either XML or JSON)
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
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setLastResponse(HttpResponse theLastResponse) {
		myLastResponse = theLastResponse;
	}

	/**
	 * For now, this is a part of the internal API of HAPI - Use with caution as this method may change!
	 */
	public void setLastResponseBody(String theLastResponseBody) {
		myLastResponseBody = theLastResponseBody;
	}

	/**
	 * Sets the pretty print flag, which is a request to the server for it to return "pretty printed" responses. Note that this is currently a non-standard flag (_pretty) which is supported only by
	 * HAPI based servers (and any other servers which might implement it).
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

	public static Reader createReaderFromResponse(HttpResponse theResponse) throws IllegalStateException, IOException {
		HttpEntity entity = theResponse.getEntity();
		if (entity == null) {
			return new StringReader("");
		}
		Charset charset = null;
		if (entity.getContentType() != null && entity.getContentType().getElements() != null && entity.getContentType().getElements().length > 0) {
			ContentType ct = ContentType.get(entity);
			charset = ct.getCharset();
		}
		if (charset == null) {
			if (Constants.STATUS_HTTP_204_NO_CONTENT != theResponse.getStatusLine().getStatusCode()) {
				ourLog.warn("Response did not specify a charset.");
			}
			charset = Charset.forName("UTF-8");
		}

		Reader reader = new InputStreamReader(theResponse.getEntity().getContent(), charset);
		return reader;
	}

}
