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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;

import static ca.uhn.fhir.jpa.searchparam.SearchParameterMap.INTEGER_0;
import static java.util.Objects.nonNull;

public class SearchParameterMapCalculator {

	public static boolean isWantCount(SearchParameterMap myParams) {
		return isWantCount(myParams.getSearchTotalMode());
	}

	public static boolean isWantCount(SearchTotalModeEnum theSearchTotalModeEnum) {
		return SearchTotalModeEnum.ACCURATE.equals(theSearchTotalModeEnum);
	}

	/**
	 * Returns true if either:
	 * <ul>
	 *     <li>{@link SearchParameterMap#getSummaryMode()} is {@link SummaryEnum#COUNT}</ul>
	 *     <li>{@link SearchParameterMap#getCount()} is {@literal 0}
	 * </ul>
	 */
	public static boolean isWantOnlyCount(SearchParameterMap myParams) {
		return SummaryEnum.COUNT.equals(myParams.getSummaryMode()) | INTEGER_0.equals(myParams.getCount());
	}

	/**
	 * Returns true if either:
	 * <ul>
	 *     <li>{@link SearchParameterMap#getSearchTotalMode()} is {@link SearchTotalModeEnum#ACCURATE}</ul>
	 *     <li>{@link SearchParameterMap#getSearchTotalMode()} is {@literal null} and {@link JpaStorageSettings#getDefaultTotalMode()} is {@link SearchTotalModeEnum#ACCURATE}</ul>
	 * </ul>
	 *
	 * @since 8.14.0
	 */
	public static boolean isWantCount(SearchParameterMap theParams, JpaStorageSettings theStorageSettings) {
		return nonNull(theParams.getSearchTotalMode())
				? isWantCount(theParams)
				: isWantCount(theStorageSettings.getDefaultTotalMode());
	}
}
