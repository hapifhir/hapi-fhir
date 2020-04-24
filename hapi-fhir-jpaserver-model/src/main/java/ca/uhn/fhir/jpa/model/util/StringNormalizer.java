package ca.uhn.fhir.jpa.model.util;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import java.io.CharArrayWriter;
import java.text.Normalizer;

public class StringNormalizer {
	public static String normalizeString(String theString) {
		CharArrayWriter outBuffer = new CharArrayWriter(theString.length());

		/*
		 * The following block of code is used to strip out diacritical marks from latin script
		 * and also convert to upper case. E.g. "j?mes" becomes "JAMES".
		 *
		 * See http://www.unicode.org/charts/PDF/U0300.pdf for the logic
		 * behind stripping 0300-036F
		 *
		 * See #454 for an issue where we were completely stripping non latin characters
		 * See #832 for an issue where we normalize korean characters, which are decomposed
		 */
		String string = Normalizer.normalize(theString, Normalizer.Form.NFD);
		for (int i = 0, n = string.length(); i < n; ++i) {
			char c = string.charAt(i);
			if (c >= '\u0300' && c <= '\u036F') {
				continue;
			} else {
				outBuffer.append(c);
			}
		}

		return new String(outBuffer.toCharArray()).toUpperCase();
	}


}
