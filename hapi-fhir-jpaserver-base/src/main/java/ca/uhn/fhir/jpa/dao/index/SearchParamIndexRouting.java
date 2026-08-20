/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

/**
 * Identifies which search parameter index type is being routed, passed to index write-policy providers
 * so they can decide whether to handle or suppress the built-in index for that type.
 */
public final class SearchParamIndexRouting {

	private final RestSearchParameterTypeEnum myParamType;

	private SearchParamIndexRouting(RestSearchParameterTypeEnum theParamType) {
		myParamType = theParamType;
	}

	/**
	 * @param theParamType the search parameter index type being routed
	 */
	public static SearchParamIndexRouting forParamType(RestSearchParameterTypeEnum theParamType) {
		return new SearchParamIndexRouting(theParamType);
	}

	public RestSearchParameterTypeEnum getParamType() {
		return myParamType;
	}
}
