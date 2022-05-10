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
 * Standardizes phone numbers to fit 123-456-7890 pattern.
 */
public class PhoneStandardizer implements IStandardizer {

	public static final String PHONE_NUMBER_PATTERN = "(\\d{3})(\\d{3})(\\d+)";
	public static final String PHONE_NUMBER_REPLACE_PATTERN = "$1-$2-$3";

	@Override
	public String standardize(String thePhone) {
		StringBuilder buf = new StringBuilder(thePhone.length());
		for (char ch : thePhone.toCharArray()) {
			if (Character.isDigit(ch)) {
				buf.append(ch);
			}
		}
		return buf.toString().replaceFirst(PHONE_NUMBER_PATTERN, PHONE_NUMBER_REPLACE_PATTERN);
	}

}
