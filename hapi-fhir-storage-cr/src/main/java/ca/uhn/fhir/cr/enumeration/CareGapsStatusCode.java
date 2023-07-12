/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.enumeration;

import ca.uhn.fhir.i18n.Msg;

public enum CareGapsStatusCode {
	OPEN_GAP("open-gap"),
	CLOSED_GAP("closed-gap"),
	NOT_APPLICABLE("not-applicable");

	private final String myValue;

	CareGapsStatusCode(final String theValue) {
		myValue = theValue;
	}

	@Override
	public String toString() {
		return myValue;
	}

	public String toDisplayString() {
		if (myValue.equals("open-gap")) {
			return "Open Gap";
		}

		if (myValue.equals("closed-gap")) {
			return "Closed Gap";
		}

		if (myValue.equals("not-applicable")) {
			return "Not Applicable";
		}

		throw new RuntimeException(Msg.code(2301) + "Error getting display strings for care gaps status codes");
	}
}
