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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;

import java.util.HashMap;
import java.util.Map;

public enum SearchContainedModeEnum {

	/**
	 * default, search on the non-contained (normal) resources
	 */
	FALSE("false"),

	/**
	 * search on the contained resources only
	 */
	TRUE("true"),

	/**
	 * Search on the normal resources and contained resources.
	 * This option is not supported yet.
	 */
	BOTH("both");

	private static volatile Map<String, SearchContainedModeEnum> ourCodeToEnum;
	private final String myCode;

	SearchContainedModeEnum(String theCode) {
		myCode = theCode;
	}

	public String getCode() {
		return myCode;
	}

	public static SearchContainedModeEnum fromCode(String theCode) {
		Map<String, SearchContainedModeEnum> codeToEnum = ourCodeToEnum;
		if (codeToEnum == null) {
			codeToEnum = new HashMap<>();
			for (SearchContainedModeEnum next : values()) {
				codeToEnum.put(next.getCode(), next);
			}
			ourCodeToEnum = codeToEnum;
		}

		SearchContainedModeEnum retVal = codeToEnum.get(theCode);
		if (retVal == null) {
			throw new InvalidRequestException(Msg.code(1963) + "Invalid contained mode: " + UrlUtil.sanitizeUrlPart(theCode));
		}

		return retVal;
	}

}
