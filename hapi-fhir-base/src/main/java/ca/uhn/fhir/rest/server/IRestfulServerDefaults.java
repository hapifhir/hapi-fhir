package ca.uhn.fhir.rest.server;

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

import ca.uhn.fhir.context.FhirContext;

public interface IRestfulServerDefaults {

	/**
	 * Gets the {@link FhirContext} associated with this server. For efficient processing, resource providers and plain providers should generally use this context if one is needed, as opposed to
	 * creating their own.
	 */
	FhirContext getFhirContext();

	/**
	 * Should the server "pretty print" responses by default (requesting clients can always override this default by supplying an <code>Accept</code> header in the request, or a <code>_pretty</code>
	 * parameter in the request URL.
	 * <p>
	 * The default is <code>false</code>
	 * </p>
	 * 
	 * @return Returns the default pretty print setting
	 */
	boolean isDefaultPrettyPrint();

	/**
	 * @return Returns the server support for ETags (will not be <code>null</code>). Default is {@link RestfulServer#DEFAULT_ETAG_SUPPORT}
	 */	
	ETagSupportEnum getETagSupport();

	/**
	 * @return Returns the setting for automatically adding profile tags
	 */
	AddProfileTagEnum getAddProfileTag();

	/**
	 * @return Returns the default encoding to return (XML/JSON) if an incoming request does not specify a preference (either with the <code>_format</code> URL parameter, or with an <code>Accept</code> header
	 * in the request. The default is {@link EncodingEnum#XML}. Will not return null.
	 */	
	EncodingEnum getDefaultResponseEncoding();

	/**
	 * @return If  <code>true</code> the server will use browser friendly content-types (instead of standard FHIR ones) when it detects that the request is coming from a browser
	 * instead of a FHIR
	 */	
	boolean isUseBrowserFriendlyContentTypes();

}
