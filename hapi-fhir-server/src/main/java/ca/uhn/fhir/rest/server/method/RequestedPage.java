/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.method;

/**
 * This is an intermediate record object that holds the offset and limit (count) the user requested for the page of results.
 */
public class RequestedPage {
	/**
	 * The search results offset requested by the user
	 */
	public final Integer offset;
	/**
	 * The number of results starting from the offset requested by the user
	 */
	public final Integer limit;

	public RequestedPage(Integer theOffset, Integer theLimit) {
		offset = theOffset;
		limit = theLimit;
	}
}
