package ca.uhn.fhir.rest.client.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestFormatParamStyleEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;

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

public interface IRestfulClient {

	/**
	 * Sets the interfceptor service used by this client
	 *
	 * @since 3.8.0
	 */
	IInterceptorService getInterceptorService();

	/**
	 * Sets the interfceptor service used by this client
	 *
	 * @since 3.8.0
	 */
	void setInterceptorService(@Nonnull IInterceptorService theInterceptorService);

	/**
	 * Retrieve the contents at the given URL and parse them as a resource. This
	 * method could be used as a low level implementation of a read/vread/search
	 * operation.
	 *
	 * @param theResourceType The resource type to parse
	 * @param theUrl          The URL to load
	 * @return The parsed resource
	 */
	<T extends IBaseResource> T fetchResourceFromUrl(Class<T> theResourceType, String theUrl);

	/**
	 * Returns the encoding that will be used on requests. Default is <code>null</code>, which means the client will not
	 * explicitly request an encoding. (This is standard behaviour according to the FHIR specification)
	 */
	EncodingEnum getEncoding();

	/**
	 * Specifies that the client should use the given encoding to do its
	 * queries. This means that the client will append the "_format" param
	 * to GET methods (read/search/etc), and will add an appropriate header for
	 * write methods.
	 *
	 * @param theEncoding The encoding to use in the request, or <code>null</code> not specify
	 *                    an encoding (which generally implies the use of XML). The default is <code>null</code>.
	 */
	void setEncoding(EncodingEnum theEncoding);

	/**
	 * Returns the FHIR context associated with this client
	 */
	FhirContext getFhirContext();

	/**
	 * Do not call this method in client code. It is a part of the internal HAPI API and
	 * is subject to change!
	 */
	IHttpClient getHttpClient();

	/**
	 * Base URL for the server, with no trailing "/"
	 */
	String getServerBase();

	/**
	 * Register a new interceptor for this client. An interceptor can be used to add additional
	 * logging, or add security headers, or pre-process responses, etc.
	 * <p>
	 * This is a convenience method for performing the following call:
	 * <code>getInterceptorService().registerInterceptor(theInterceptor)</code>
	 * </p>
	 */
	void registerInterceptor(Object theInterceptor);

	/**
	 * Specifies that the client should request that the server respond with "pretty printing"
	 * enabled. Note that this is a non-standard parameter, not all servers will
	 * support it.
	 *
	 * @param thePrettyPrint The pretty print flag to use in the request (default is <code>false</code>)
	 */
	void setPrettyPrint(Boolean thePrettyPrint);

	/**
	 * If not set to <code>null</code>, specifies a value for the <code>_summary</code> parameter
	 * to be applied globally on this client.
	 */
	void setSummary(SummaryEnum theSummary);

	/**
	 * Remove an interceptor that was previously registered using {@link IRestfulClient#registerInterceptor(Object)}.
	 * <p>
	 * This is a convenience method for performing the following call:
	 * <code>getInterceptorService().unregisterInterceptor(theInterceptor)</code>
	 * </p>
	 */
	void unregisterInterceptor(Object theInterceptor);

	/**
	 * Configures what style of _format parameter should be used in requests
	 */
	void setFormatParamStyle(RequestFormatParamStyleEnum theRequestFormatParamStyle);
}
