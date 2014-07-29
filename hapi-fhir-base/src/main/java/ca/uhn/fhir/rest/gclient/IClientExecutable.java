package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
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


public interface IClientExecutable<T extends IClientExecutable<?,?>, Y> {

	Y execute();

	T encodedJson();

	T encodedXml();

	/**
	 * If set to true, the client will log the request and response to the SLF4J logger. This can be useful for
	 * debugging, but is generally not desirable in a production situation.
	 * 
	 * @deprecated Use the client logging interceptor to log requests and responses instead. See <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_client.html#req_resp_logging">here</a> for more information.
	 */
	T andLogRequestAndResponse(boolean theLogRequestAndResponse);

	T prettyPrint();

}
