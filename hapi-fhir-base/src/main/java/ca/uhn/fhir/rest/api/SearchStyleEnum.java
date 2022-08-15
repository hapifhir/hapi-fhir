package ca.uhn.fhir.rest.api;

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

/**
 * Enumerated type to represent the various allowable syntax for a search/query
 * as described in the  
 * <a href="http://www.hl7.org/implement/standards/fhir/http.html#search">FHIR Specification Section 2.1.11</a>
 */
public enum SearchStyleEnum {

	/**
	 * This is the most common (and generally the default) behaviour. Performs the search using the style:
	 * <br>
	 * <code>GET [base]/[resource type]?[params]</code>
	 */
	GET,
	
	/**
	 * Performs the search using the style below. Note that this style is less commonly supported
	 * in servers so it should not be used unless there is a specific reason for needing to.
	 * <br>
	 * <code>GET [base]/[resource type]/_search?[params]</code>
	 */
	GET_WITH_SEARCH,

	/**
	 * Performs the search using the style below. This style is useful when you have long search strings.
	 * <br>
	 * <code>POST [base]/[resource type]/_search</code>
	 * <br>
	 * and the params in a form encoded POST body.
	 */
	POST
	
}
