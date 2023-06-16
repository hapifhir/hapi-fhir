/*-
 * #%L
 * HAPI FHIR Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.RuntimeSearchParam;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class RuntimeSearchParamHelper {

	/**
	 * Helper function to determine if a RuntimeSearchParam is a resource level search param.
	 *
	 * @param theSearchParam the parameter to check
	 * @return return boolean
	 */
	public static boolean isResourceLevel(RuntimeSearchParam theSearchParam) {
		return startsWith(theSearchParam.getPath(), "Resource.");
	}
}
