package ca.uhn.fhir.rest.client.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;

import java.io.IOException;

/**
 * This interface represents an interceptor which can be used to access (and optionally change or take actions upon)
 * requests that are being sent by the HTTP client, and responses received by it.
 * <p>
 * See the <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_client_interceptor.html">HAPI Documentation Client Interceptor</a>
 * page for more information on how to use this feature.
 * </p> 
 */
public interface IClientInterceptor {

	/**
	 * Fired by the client just before invoking the HTTP client request
	 */
	@Hook(Pointcut.CLIENT_REQUEST)
	void interceptRequest(IHttpRequest theRequest);
	
	/**
	 * Fired by the client upon receiving an HTTP response, prior to processing that response
	 */
	@Hook(Pointcut.CLIENT_RESPONSE)
	void interceptResponse(IHttpResponse theResponse) throws IOException;

}
