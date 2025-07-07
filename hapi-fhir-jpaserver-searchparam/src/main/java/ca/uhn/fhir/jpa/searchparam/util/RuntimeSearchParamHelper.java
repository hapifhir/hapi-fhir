/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import jakarta.annotation.Nonnull;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_HAS;
import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static ca.uhn.fhir.rest.api.Constants.PARAM_IN;
import static ca.uhn.fhir.rest.api.Constants.PARAM_LANGUAGE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_LASTUPDATED;
import static ca.uhn.fhir.rest.api.Constants.PARAM_PROFILE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SECURITY;
import static ca.uhn.fhir.rest.api.Constants.PARAM_SOURCE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_TAG;
import static ca.uhn.fhir.rest.api.Constants.PARAM_TEXT;
import static org.apache.commons.lang3.StringUtils.defaultString;
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

	/**
	 * Returns {@literal true} if the given Search Parameter is one that is handled by special
	 * handling as opposed to being simply indexed by FHIRPath expression. For example, parameters
	 * such as _tag, _id, _content, etc. are all handled in one-off routines as opposed to just
	 * looking up the value using the FHIRPath expression and indexing it.
	 */
	@SuppressWarnings("DuplicateBranchesInSwitch")
	public static boolean isSpeciallyHandledSearchParameter(
			@Nonnull RuntimeSearchParam theSearchParameter, StorageSettings theStorageSettings) {
		return switch (defaultString(theSearchParameter.getName())) {
			case PARAM_CONTENT -> true;
			case PARAM_HAS -> true;
			case PARAM_ID -> true;
			case PARAM_IN -> true;
			case PARAM_LASTUPDATED -> true;
			case PARAM_LANGUAGE -> false;
			case PARAM_SOURCE -> true;
			case PARAM_TEXT -> true;
			case PARAM_PROFILE, PARAM_TAG, PARAM_SECURITY -> theStorageSettings.getTagStorageMode()
					!= StorageSettings.TagStorageModeEnum.INLINE;
			default -> false;
		};
	}
}
