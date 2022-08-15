package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestFormatParamStyleEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


public interface IClientExecutable<T extends IClientExecutable<?, Y>, Y> {

	/**
	 * If set to true, the client will log the request and response to the SLF4J logger. This can be useful for
	 * debugging, but is generally not desirable in a production situation.
	 *
	 * @deprecated Use the client logging interceptor to log requests and responses instead. See <a href="https://hapifhir.io/hapi-fhir/docs/interceptors/built_in_client_interceptors.html">here</a> for more information.
	 */
	@Deprecated
	T andLogRequestAndResponse(boolean theLogRequestAndResponse);

	/**
	 * Sets the <code>Cache-Control</code> header value, which advises the server (or any cache in front of it)
	 * how to behave in terms of cached requests
	 */
	T cacheControl(CacheControlDirective theCacheControlDirective);

	/**
	 * Request that the server return subsetted resources, containing only the elements specified in the given parameters.
	 * For example: <code>subsetElements("name", "identifier")</code> requests that the server only return
	 * the "name" and "identifier" fields in the returned resource, and omit any others.
	 */
	T elementsSubset(String... theElements);

	/**
	 * Request that the server respond with JSON via the Accept header and possibly also the
	 * <code>_format</code> parameter if {@link ca.uhn.fhir.rest.client.api.IRestfulClient#setFormatParamStyle(RequestFormatParamStyleEnum) configured to do so}.
	 * <p>
	 * This method will have no effect if {@link #accept(String) a custom Accept header} is specified.
	 * </p>
	 *
	 * @see #accept(String)
	 */
	T encoded(EncodingEnum theEncoding);

	/**
	 * Request that the server respond with JSON via the Accept header and possibly also the
	 * <code>_format</code> parameter if {@link ca.uhn.fhir.rest.client.api.IRestfulClient#setFormatParamStyle(RequestFormatParamStyleEnum) configured to do so}.
	 * <p>
	 * This method will have no effect if {@link #accept(String) a custom Accept header} is specified.
	 * </p>
	 *
	 * @see #accept(String)
	 * @see #encoded(EncodingEnum)
	 */
	T encodedJson();

	/**
	 * Request that the server respond with JSON via the Accept header and possibly also the
	 * <code>_format</code> parameter if {@link ca.uhn.fhir.rest.client.api.IRestfulClient#setFormatParamStyle(RequestFormatParamStyleEnum) configured to do so}.
	 * <p>
	 * This method will have no effect if {@link #accept(String) a custom Accept header} is specified.
	 * </p>
	 *
	 * @see #accept(String)
	 * @see #encoded(EncodingEnum)
	 */
	T encodedXml();

	/**
	 * Set a HTTP header not explicitly defined in FHIR but commonly used in real-world scenarios. One
	 * important example is to set the Authorization header (e.g. Basic Auth or OAuth2-based Bearer auth),
	 * which tends to be cumbersome using {@link ca.uhn.fhir.rest.client.api.IClientInterceptor IClientInterceptors},
	 * particularly when REST clients shall be reused and are thus supposed to remain stateless.
	 * <p>It is the responsibility of the caller to care for proper encoding of the header value, e.g.
	 * using Base64.</p>
	 * <p>This is a short-cut alternative to using a corresponding client interceptor</p>
	 *
	 * @param theHeaderName header name
	 * @param theHeaderValue header value
	 * @return
	 */
	T withAdditionalHeader(String theHeaderName, String theHeaderValue);

	/**
	 * Actually execute the client operation
	 */
	Y execute();

	/**
	 * Explicitly specify a custom structure type to attempt to use when parsing the response. This
	 * is useful for invocations where the response is a Bundle/Parameters containing nested resources,
	 * and you want to use specific custom structures for those nested resources.
	 * <p>
	 * See <a href="https://jamesagnew.github.io/hapi-fhir/doc_extensions.html">Profiles and Extensions</a> for more information on using custom structures
	 * </p>
	 */
	T preferResponseType(Class<? extends IBaseResource> theType);

	/**
	 * Explicitly specify a list of custom structure types to attempt to use (in order from most to
	 * least preferred) when parsing the response. This
	 * is useful for invocations where the response is a Bundle/Parameters containing nested resources,
	 * and you want to use specific custom structures for those nested resources.
	 * <p>
	 * See <a href="https://jamesagnew.github.io/hapi-fhir/doc_extensions.html">Profiles and Extensions</a> for more information on using custom structures
	 * </p>
	 */
	T preferResponseTypes(List<Class<? extends IBaseResource>> theTypes);

	/**
	 * Request pretty-printed response via the <code>_pretty</code> parameter
	 */
	T prettyPrint();

	/**
	 * Request that the server modify the response using the <code>_summary</code> param
	 */
	T summaryMode(SummaryEnum theSummary);

	/**
	 * Specifies a custom <code>Accept</code> header that should be supplied with the
	 * request.
	 * <p>
	 * Note that this method overrides any encoding preferences specified with
	 * {@link #encodedJson()} or {@link #encodedXml()}. It is generally easier to
	 * just use those methods if you simply want to request a specific FHIR encoding.
	 * </p>
	 *
	 * @param theHeaderValue The header value, e.g. "application/fhir+json". Constants such
	 *                       as {@link ca.uhn.fhir.rest.api.Constants#CT_FHIR_XML_NEW} and
	 *                       {@link ca.uhn.fhir.rest.api.Constants#CT_FHIR_JSON_NEW} may
	 *                       be useful. If set to <code>null</code> or an empty string, the
	 *                       default Accept header will be used.
	 * @see #encoded(EncodingEnum)
	 * @see #encodedJson()
	 * @see #encodedXml()
	 */
	T accept(String theHeaderValue);
}
