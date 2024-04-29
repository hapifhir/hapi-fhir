/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;

import static ca.uhn.fhir.jpa.searchparam.SearchParameterMap.INTEGER_0;

public class SearchParameterMapCalculator {

	public static boolean isWantCount(SearchParameterMap myParams) {
		return isWantCount(myParams.getSearchTotalMode());
	}

	public static boolean isWantCount(SearchTotalModeEnum theSearchTotalModeEnum) {
		return SearchTotalModeEnum.ACCURATE.equals(theSearchTotalModeEnum);
	}

	public static boolean isWantOnlyCount(SearchParameterMap myParams) {
		return SummaryEnum.COUNT.equals(myParams.getSummaryMode()) | INTEGER_0.equals(myParams.getCount());
	}
}
