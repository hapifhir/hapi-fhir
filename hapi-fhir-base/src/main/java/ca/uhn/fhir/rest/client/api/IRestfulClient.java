package ca.uhn.fhir.rest.client.api;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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


import org.apache.http.client.HttpClient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.EncodingEnum;

public interface IRestfulClient {

	FhirContext getFhirContext();
	
	HttpClient getHttpClient();
	
	/**
	 * Specifies that the client should use the given encoding to do its 
	 * queries. This means that the client will append the "_format" param
	 * to GET methods (read/search/etc), and will add an appropriate header for
	 * write methods. 
	 */
	void setEncoding(EncodingEnum theEncoding);
	
	/**
	 * Specifies that the client should request that the server respond with "pretty printing"
	 * enabled. Note that this is a non-standard parameter, so it may only 
	 * work against HAPI based servers.
	 */
	void setPrettyPrint(boolean thePrettyPrint);
	
	/**
	 * Base URL for the server, with no trailing "/"
	 */
	String getServerBase();
	
}
