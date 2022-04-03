package ca.uhn.fhir.rest.api;

/*-
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

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Represents values for "return" value as provided in the the <a href="https://tools.ietf.org/html/rfc7240#section-4.2">HTTP Prefer header</a>.
 */
public enum PreferReturnEnum {

	REPRESENTATION(Constants.HEADER_PREFER_RETURN_REPRESENTATION), MINIMAL(Constants.HEADER_PREFER_RETURN_MINIMAL), OPERATION_OUTCOME(Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);

	private static HashMap<String, PreferReturnEnum> ourValues;
	private String myHeaderValue;

	PreferReturnEnum(String theHeaderValue) {
		myHeaderValue = theHeaderValue;
	}

	public String getHeaderValue() {
		return myHeaderValue;
	}

	@Nullable
	public static PreferReturnEnum fromHeaderValue(String theHeaderValue) {
		if (ourValues == null) {
			HashMap<String, PreferReturnEnum> values = new HashMap<>();
			for (PreferReturnEnum next : PreferReturnEnum.values()) {
				values.put(next.getHeaderValue(), next);
			}
			ourValues = values;
		}
		return ourValues.get(theHeaderValue);
	}

}
