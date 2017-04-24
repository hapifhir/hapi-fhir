package ca.uhn.fhir.rest.gclient;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.api.SummaryEnum;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

	/**
	 * If set to true, the client will log the request and response to the SLF4J logger. This can be useful for
	 * debugging, but is generally not desirable in a production situation.
	 * 
	 * @deprecated Use the client logging interceptor to log requests and responses instead. See <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_client.html#req_resp_logging">here</a> for more information.
	 */
	@Deprecated
	T andLogRequestAndResponse(boolean theLogRequestAndResponse);

	/**
	 * Request that the server return subsetted resources, containing only the elements specified in the given parameters. 
	 * For example: <code>subsetElements("name", "identifier")</code> requests that the server only return
	 * the "name" and "identifier" fields in the returned resource, and omit any others.  
	 */
	T elementsSubset(String... theElements);

	T encodedJson();

	T encodedXml();

	Y execute();

	T prettyPrint();

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
	 * Request that the server modify the response using the <code>_summary</code> param 
	 */
	T summaryMode(SummaryEnum theSummary);

}
