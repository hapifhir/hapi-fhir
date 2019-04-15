package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

import java.util.List;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public interface IRestfulServerDefaults {
	/**
	 * @return Returns the setting for automatically adding profile tags
	 * @deprecated As of HAPI FHIR 1.5, this property has been moved to
	 * {@link FhirContext#setAddProfileTagWhenEncoding(AddProfileTagEnum)}
	 */
	@Deprecated
	AddProfileTagEnum getAddProfileTag();

	/**
	 * @return Returns the default encoding to return (XML/JSON) if an incoming request does not specify a preference
	 * (either with the <code>_format</code> URL parameter, or with an <code>Accept</code> header
	 * in the request. The default is {@link EncodingEnum#XML}. Will not return null.
	 */
	EncodingEnum getDefaultResponseEncoding();

	/**
	 * @return Returns the server support for ETags (will not be <code>null</code>). Default is
	 * {@link RestfulServer#DEFAULT_ETAG_SUPPORT}
	 */
	ETagSupportEnum getETagSupport();

	/**
	 * @return Returns the support option for the <code>_elements</code> parameter on search
	 * and read operations.
	 * @see <a href="http://hapifhir.io/doc_rest_server.html#extended_elements_support">Extended Elements Support</a>
	 */
	ElementsSupportEnum getElementsSupport();

	/**
	 * Gets the {@link FhirContext} associated with this server. For efficient processing, resource providers and plain
	 * providers should generally use this context if one is needed, as opposed to
	 * creating their own.
	 */
	FhirContext getFhirContext();

	/**
	 * Returns the list of interceptors registered against this server
	 */
	List<IServerInterceptor> getInterceptors_();

	/**
	 * Returns the paging provider for this server
	 */
	IPagingProvider getPagingProvider();

	/**
	 * Should the server "pretty print" responses by default (requesting clients can always override this default by
	 * supplying an <code>Accept</code> header in the request, or a <code>_pretty</code>
	 * parameter in the request URL.
	 * <p>
	 * The default is <code>false</code>
	 * </p>
	 *
	 * @return Returns the default pretty print setting
	 */
	boolean isDefaultPrettyPrint();

	/**
	 * Returns the interceptor service for this server
	 */
	IInterceptorService getInterceptorService();
}
