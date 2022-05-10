package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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
 * Standardizes email addresses by removing whitespace, ISO control characters and applying lower-case to the values.
 */
public class EmailStandardizer implements IStandardizer {

	@Override
	public String standardize(String theString) {
		StringBuilder buf = new StringBuilder();
		for (int offset = 0; offset < theString.length(); ) {
			int codePoint = theString.codePointAt(offset);
			offset += Character.charCount(codePoint);

			if (Character.isISOControl(codePoint)) {
				continue;
			}

			if (!Character.isWhitespace(codePoint)) {
				buf.append(new String(Character.toChars(codePoint)).toLowerCase());
			}
		}
		return buf.toString();
	}
}
