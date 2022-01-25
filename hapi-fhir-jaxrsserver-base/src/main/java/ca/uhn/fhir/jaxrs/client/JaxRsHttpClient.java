package ca.uhn.fhir.jaxrs.client;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.HttpClientUtil;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.method.MethodUtil;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import java.util.List;
import java.util.Map;

/**
 * A Http Request based on JaxRs. This is an adapter around the class
 * {@link javax.ws.rs.client.Client Client}
 *
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsHttpClient implements IHttpClient {

	private Client myClient;
	private List<Header> myHeaders;
	private StringBuilder myUrl;
	private Map<String, List<String>> myIfNoneExistParams;
	private String myIfNoneExistString;
	private RequestTypeEnum myRequestType;

	public JaxRsHttpClient(Client theClient, StringBuilder theUrl, Map<String, List<String>> theIfNoneExistParams, String theIfNoneExistString,
								  RequestTypeEnum theRequestType, List<Header> theHeaders) {
		this.myClient = theClient;
		this.myUrl = theUrl;
		this.myIfNoneExistParams = theIfNoneExistParams;
		this.myIfNoneExistString = theIfNoneExistString;
		this.myRequestType = theRequestType;
		this.myHeaders = theHeaders;
	}

	@Override
	public IHttpRequest createByteRequest(FhirContext theContext, String theContents, String theContentType, EncodingEnum theEncoding) {
		Entity<String> entity = Entity.entity(theContents, theContentType + Constants.HEADER_SUFFIX_CT_UTF_8);
		JaxRsHttpRequest retVal = createHttpRequest(entity);
		addHeadersToRequest(retVal, theEncoding, theContext);
		retVal.addHeader(Constants.HEADER_CONTENT_TYPE, theContentType + Constants.HEADER_SUFFIX_CT_UTF_8);
		return retVal;
	}

	@Override
	public IHttpRequest createParamRequest(FhirContext theContext, Map<String, List<String>> theParams, EncodingEnum theEncoding) {
		MultivaluedMap<String, String> map = new MultivaluedHashMap<String, String>();
		for (Map.Entry<String, List<String>> nextParam : theParams.entrySet()) {
			List<String> value = nextParam.getValue();
			for (String s : value) {
				map.add(nextParam.getKey(), s);
			}
		}
		Entity<Form> entity = Entity.form(map);
		JaxRsHttpRequest retVal = createHttpRequest(entity);
		addHeadersToRequest(retVal, theEncoding, theContext);
		return retVal;
	}

	@Override
	public IHttpRequest createBinaryRequest(FhirContext theContext, IBaseBinary theBinary) {
		Entity<String> entity = Entity.entity(theBinary.getContentAsBase64(), theBinary.getContentType());
		JaxRsHttpRequest retVal = createHttpRequest(entity);
		addHeadersToRequest(retVal, null, theContext);
		return retVal;
	}

	@Override
	public IHttpRequest createGetRequest(FhirContext theContext, EncodingEnum theEncoding) {
		JaxRsHttpRequest result = createHttpRequest(null);
		addHeadersToRequest(result, theEncoding, theContext);
		return result;
	}

	public void addHeadersToRequest(JaxRsHttpRequest theHttpRequest, EncodingEnum theEncoding, FhirContext theContext) {
		if (myHeaders != null) {
			for (Header next : myHeaders) {
				theHttpRequest.addHeader(next.getName(), next.getValue());
			}
		}

		theHttpRequest.addHeader("User-Agent", HttpClientUtil.createUserAgentString(theContext, "jax-rs"));
		theHttpRequest.addHeader("Accept-Charset", "utf-8");

		Builder request = theHttpRequest.getRequest();
		request.acceptEncoding("gzip");

		MethodUtil.addAcceptHeaderToRequest(theEncoding, theHttpRequest, theContext);
	}

	private JaxRsHttpRequest createHttpRequest(Entity<?> entity) {
		Builder request = myClient.target(myUrl.toString()).request();
		JaxRsHttpRequest result = new JaxRsHttpRequest(request, myRequestType, entity);
		addHeaderIfNoneExist(result);
		return result;
	}

	private void addHeaderIfNoneExist(IHttpRequest result) {
		if (myIfNoneExistParams != null) {
			StringBuilder b = newHeaderBuilder(myUrl);
			BaseHttpClientInvocation.appendExtraParamsWithQuestionMark(myIfNoneExistParams, b, b.indexOf("?") == -1);
			result.addHeader(Constants.HEADER_IF_NONE_EXIST, b.toString());
		}

		if (myIfNoneExistString != null) {
			StringBuilder b = newHeaderBuilder(myUrl);
			b.append(b.indexOf("?") == -1 ? '?' : '&');
			b.append(myIfNoneExistString.substring(myIfNoneExistString.indexOf('?') + 1));
			result.addHeader(Constants.HEADER_IF_NONE_EXIST, b.toString());
		}
	}

	private StringBuilder newHeaderBuilder(StringBuilder theUrlBase) {
		StringBuilder b = new StringBuilder();
		b.append(theUrlBase);
		if (theUrlBase.length() > 0 && theUrlBase.charAt(theUrlBase.length() - 1) == '/') {
			b.deleteCharAt(b.length() - 1);
		}
		return b;
	}

}
