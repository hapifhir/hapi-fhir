package ca.uhn.fhir.rest.client.api;

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

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Http Base Request. Allows addition of headers and execution of the request.
 */
public interface IHttpRequestBase {
	
	/**
	 * Add a header to the request
	 * @param theName the header name
	 * @param theValue the header value
	 */
	public void addHeader(String theName, String theValue);    

	/**
	 * Execute the request
	 * @return the response
	 * @throws IOException
	 */
	public IHttpResponse execute() throws IOException;

	/**
	 * @return all request headers in lower case
	 */
	public Map<String, List<String>> getAllHeaders();

}
