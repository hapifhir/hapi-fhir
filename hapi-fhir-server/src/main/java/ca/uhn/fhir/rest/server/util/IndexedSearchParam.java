/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.util;

/**
 * Simplified model of indexed search parameter
 */
public class IndexedSearchParam {

	private final String myParameterName;
	private final String myResourceType;

	public IndexedSearchParam(String theParameterName, String theResourceType) {
		this.myParameterName = theParameterName;
		this.myResourceType = theResourceType;
	}

	public String getParameterName() {
		return myParameterName;
	}

	public String getResourceType() {
		return myResourceType;
	}
}
