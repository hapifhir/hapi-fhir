package ca.uhn.fhir.cr.common.utility;

/*-
 * #%L
 * HAPI FHIR JPA Server - Clinical Reasoning
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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class provides utility functions for creating IGenericClients and
 * setting up authentication
 */
public class Clients {

	private Clients() {
	}

	/**
	 * Creates an IGenericClient for the given url. Defaults to NEVER
	 * ServerValidationMode
	 * 
	 * @param theFhirVersionEnum the FHIR version to create a client for
	 * @param theUrl             the server base url to connect to
	 * @return IGenericClient for the given url
	 */
	public static IGenericClient forUrl(FhirVersionEnum theFhirVersionEnum, String theUrl) {
		checkNotNull(theFhirVersionEnum);
		checkNotNull(theUrl);

		return forUrl(FhirContext.forCached(theFhirVersionEnum), theUrl);
	}

	/**
	 * Creates an IGenericClient for the given url. Defaults to NEVER
	 * ServerValidationMode
	 * 
	 * @param theFhirContext the FhirContext to use to create the client
	 * @param theUrl         the server base url to connect to
	 * @return IGenericClient for the given url
	 */
	public static IGenericClient forUrl(FhirContext theFhirContext, String theUrl) {
		checkNotNull(theFhirContext);
		checkNotNull(theUrl);

		return forUrl(theFhirContext, theUrl, ServerValidationModeEnum.NEVER);
	}

	/**
	 * Creates an IGenericClient for the given url.
	 * 
	 * @param theFhirVersionEnum          the FHIR version to create a client for
	 * @param theUrl                      the server base url to connect to
	 * @param theServerValidationModeEnum the ServerValidationMode to use
	 * @return IGenericClient for the given url, with the server validation mode set
	 */
	public static IGenericClient forUrl(FhirVersionEnum theFhirVersionEnum, String theUrl,
			ServerValidationModeEnum theServerValidationModeEnum) {
		checkNotNull(theFhirVersionEnum, "theFhirVersionEnum is required");
		checkNotNull(theUrl, "theUrl is required");
		checkNotNull(theServerValidationModeEnum, "theServerValidationModeEnum is required");

		return forUrl(FhirContext.forCached(theFhirVersionEnum), theUrl, theServerValidationModeEnum);
	}

	/**
	 * Creates an IGenericClient for the given url.
	 * 
	 * @param theFhirContext              the FhirContext to use to create the
	 *                                    client
	 * @param theUrl                      the server base url to connect to
	 * @param theServerValidationModeEnum the ServerValidationMode to use
	 * @return IGenericClient for the given url, with the server validation mode set
	 */
	public static IGenericClient forUrl(FhirContext theFhirContext, String theUrl,
			ServerValidationModeEnum theServerValidationModeEnum) {
		checkNotNull(theFhirContext);
		checkNotNull(theUrl);
		checkNotNull(theServerValidationModeEnum, "theServerValidationModeEnum is required");

		theFhirContext.getRestfulClientFactory().setServerValidationMode(theServerValidationModeEnum);
		return theFhirContext.newRestfulGenericClient(theUrl);
	}

	/**
	 * Creates an IGenericClient for the given Endpoint.
	 * 
	 * @param theEndpoint the Endpoint to connect to
	 * @return IGenericClient for the given Endpoint, with appropriate header
	 *         interceptors set up
	 */
	public static IGenericClient forEndpoint(org.hl7.fhir.dstu3.model.Endpoint theEndpoint) {
		checkNotNull(theEndpoint);

		return forEndpoint(FhirContext.forDstu3Cached(), theEndpoint);
	}

	/**
	 * Creates an IGenericClient for the given Endpoint.
	 * 
	 * @param theFhirContext the FhirContext to use to create the client
	 * @param theEndpoint    the Endpoint to connect to
	 * @return IGenericClient for the given Endpoint, with appropriate header
	 *         interceptors set up
	 */
	public static IGenericClient forEndpoint(FhirContext theFhirContext,
			org.hl7.fhir.dstu3.model.Endpoint theEndpoint) {
		checkNotNull(theFhirContext);
		checkNotNull(theEndpoint);

		IGenericClient client = forUrl(theFhirContext, theEndpoint.getAddress());
		if (theEndpoint.hasHeader()) {
			List<String> headerList = theEndpoint.getHeader().stream().map(headerString -> headerString.asStringValue())
					.collect(Collectors.toList());
			registerHeaders(client, headerList);
		}
		return client;
	}

	/**
	 * Creates an IGenericClient for the given Endpoint.
	 * 
	 * @param theEndpoint the Endpoint to connect to
	 * @return IGenericClient for the given Endpoint, with appropriate header
	 *         interceptors set up
	 */
	public static IGenericClient forEndpoint(org.hl7.fhir.r4.model.Endpoint theEndpoint) {
		checkNotNull(theEndpoint);

		return forEndpoint(FhirContext.forR4Cached(), theEndpoint);
	}

	/**
	 * Creates an IGenericClient for the given Endpoint.
	 * 
	 * @param theFhirContext the FhirContext to use to create the client
	 * @param theEndpoint    the Endpoint to connect to
	 * @return IGenericClient for the given Endpoint, with appropriate header
	 *         interceptors set up
	 */
	public static IGenericClient forEndpoint(FhirContext theFhirContext, org.hl7.fhir.r4.model.Endpoint theEndpoint) {
		checkNotNull(theFhirContext);
		checkNotNull(theEndpoint);

		IGenericClient client = forUrl(theFhirContext, theEndpoint.getAddress());
		if (theEndpoint.hasHeader()) {
			List<String> headerList = theEndpoint.getHeader().stream().map(headerString -> headerString.asStringValue())
					.collect(Collectors.toList());
			registerHeaders(client, headerList);
		}
		return client;
	}

	/**
	 * Creates an IGenericClient for the given Endpoint.
	 * 
	 * @param theEndpoint the Endpoint to connect to
	 * @return IGenericClient for the given Endpoint, with appropriate header
	 *         interceptors set up
	 */
	public static IGenericClient forEndpoint(org.hl7.fhir.r5.model.Endpoint theEndpoint) {
		checkNotNull(theEndpoint, "theEndpoint is required");

		return forEndpoint(FhirContext.forR4Cached(), theEndpoint);
	}

	/**
	 * Creates an IGenericClient for the given Endpoint.
	 * 
	 * @param theFhirContext the FhirContext to use to create the client
	 * @param theEndpoint    the Endpoint to connect to
	 * @return IGenericClient for the given Endpoint, with appropriate header
	 *         interceptors set up
	 */
	public static IGenericClient forEndpoint(FhirContext theFhirContext, org.hl7.fhir.r5.model.Endpoint theEndpoint) {
		checkNotNull(theFhirContext);
		checkNotNull(theEndpoint);

		IGenericClient client = forUrl(theFhirContext, theEndpoint.getAddress());
		if (theEndpoint.hasHeader()) {
			List<String> headerList = theEndpoint.getHeader().stream().map(headerString -> headerString.asStringValue())
					.collect(Collectors.toList());
			registerHeaders(client, headerList);
		}
		return client;
	}

	/**
	 * Registers HeaderInjectionInterceptors on a client.
	 * 
	 * @param theClient  the client to add headers to
	 * @param theHeaders an Array of Strings representing headers to add
	 */
	public static void registerHeaders(IGenericClient theClient, String... theHeaders) {
		checkNotNull(theClient);

		registerHeaders(theClient, Arrays.asList(theHeaders));
	}

	/**
	 * Registers HeaderInjectionInterceptors on a client
	 * 
	 * @param theClient     the client to add headers to
	 * @param theHeaderList a List of Strings representing headers to add
	 */
	public static void registerHeaders(IGenericClient theClient, List<String> theHeaderList) {
		checkNotNull(theClient);
		checkNotNull(theHeaderList);

		Map<String, String> headerMap = setupHeaderMap(theHeaderList);
		for (Map.Entry<String, String> entry : headerMap.entrySet()) {
			IClientInterceptor headInterceptor = new HeaderInjectionInterceptor(entry.getKey(), entry.getValue());
			theClient.registerInterceptor(headInterceptor);
		}
	}

	/**
	 * Registers BasicAuthInterceptors on a client. This is useful when you have a
	 * username and password.
	 * 
	 * @param theClient   the client to register basic auth on
	 * @param theUsername the username
	 * @param thePassword the password
	 */
	public static void registerBasicAuth(IGenericClient theClient, String theUsername, String thePassword) {
		checkNotNull(theClient, "theClient is required");

		if (theUsername != null) {
			BasicAuthInterceptor authInterceptor = new BasicAuthInterceptor(theUsername, thePassword);
			theClient.registerInterceptor(authInterceptor);
		}
	}

	/**
	 * Registers BearerAuthInterceptors on a client. This is useful when you have a
	 * bearer token.
	 * 
	 * @param theClient the client to register BearerToken authentication on
	 * @param theToken  the bearer token to register
	 */
	public static void registerBearerTokenAuth(IGenericClient theClient, String theToken) {
		checkNotNull(theClient, "theClient is required");

		if (theToken != null) {
			BearerTokenAuthInterceptor authInterceptor = new BearerTokenAuthInterceptor(theToken);
			theClient.registerInterceptor(authInterceptor);
		}
	}

	/**
	 * Parses a list of headers into their constituent parts. Used to prep the
	 * headers for registration with HeaderInjectionInterceptors
	 * 
	 * @param theHeaderList a List of Strings representing headers to create
	 * @return key-value pairs of headers
	 */
	static Map<String, String> setupHeaderMap(List<String> theHeaderList) {
		checkNotNull(theHeaderList, "theHeaderList is required");

		Map<String, String> headerMap = new HashMap<String, String>();
		String leftAuth = null;
		String rightAuth = null;
		if (theHeaderList.size() < 1 || theHeaderList.isEmpty()) {
			leftAuth = null;
			rightAuth = null;
			headerMap.put(leftAuth, rightAuth);
		} else {
			for (String header : theHeaderList) {
				if (!header.contains(":")) {
					throw new IllegalArgumentException("Endpoint header must contain \":\" .");
				}
				String[] authSplit = header.split(":");
				leftAuth = authSplit[0];
				rightAuth = authSplit[1];
				headerMap.put(leftAuth, rightAuth);
			}

		}
		return headerMap;
	}
}
