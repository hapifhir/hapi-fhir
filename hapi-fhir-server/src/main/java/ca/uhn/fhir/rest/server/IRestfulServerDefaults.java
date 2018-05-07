package ca.uhn.fhir.rest.server;

import java.util.List;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

public interface IRestfulServerDefaults {
	/**
	 * @return Returns the setting for automatically adding profile tags
	 * @deprecated As of HAPI FHIR 1.5, this property has been moved to
	 *             {@link FhirContext#setAddProfileTagWhenEncoding(AddProfileTagEnum)}
	 */
	@Deprecated
	AddProfileTagEnum getAddProfileTag();

	/**
	 * @return Returns the default encoding to return (XML/JSON) if an incoming request does not specify a preference
	 *         (either with the <code>_format</code> URL parameter, or with an <code>Accept</code> header
	 *         in the request. The default is {@link EncodingEnum#XML}. Will not return null.
	 */
	EncodingEnum getDefaultResponseEncoding();

	/**
	 * @return Returns the server support for ETags (will not be <code>null</code>). Default is
	 *         {@link RestfulServer#DEFAULT_ETAG_SUPPORT}
	 */
	ETagSupportEnum getETagSupport();

	/**
	 * Gets the {@link FhirContext} associated with this server. For efficient processing, resource providers and plain
	 * providers should generally use this context if one is needed, as opposed to
	 * creating their own.
	 */
	FhirContext getFhirContext();

	/**
	 * Returns the list of interceptors registered against this server
	 */
	List<IServerInterceptor> getInterceptors();

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
	 * @return If <code>true</code> the server will use browser friendly content-types (instead of standard FHIR ones)
	 *         when it detects that the request is coming from a browser
	 *         instead of a FHIR
	 */
	boolean isUseBrowserFriendlyContentTypes();


}
