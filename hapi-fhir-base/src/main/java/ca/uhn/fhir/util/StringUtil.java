package ca.uhn.fhir.util;

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

import javax.annotation.Nonnull;
import java.io.CharArrayWriter;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Arrays;

public class StringUtil {

	/**
	 * If a string ends with a given character, remove that character from the end of the string (as many times as it occurs at the end)
	 */
	public static String chompCharacter(String theInput, char theCharacter) {
		String retVal = theInput;
		while (retVal != null && retVal.length() > 0 && retVal.charAt(retVal.length() - 1) == theCharacter) {
			retVal = retVal.substring(0, retVal.length() - 1);
		}
		return retVal;
	}

	public static String normalizeStringForSearchIndexing(String theString) {
		if (theString == null) {
			return null;
		}

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

	public static String toUtf8String(byte[] theBytes) {
		byte[] bytes = theBytes;
		if (theBytes.length >= 3) {
			if (theBytes[0] == -17 && theBytes[1] == -69 && theBytes[2] == -65) {
				bytes = Arrays.copyOfRange(theBytes, 3, theBytes.length);
			}
		}
		return new String(bytes, StandardCharsets.UTF_8);
	}

	/**
	 * Gets the string prefix of the specified length.
	 *
	 * @param theString
	 * 	String to get the prefix from
	 * @param theCodePointCount
	 * 	Length of the prefix in code points
	 * @return
	 * 	Returns the string prefix of the specified number of codepoints.
	 */
	public static String left(String theString, int theCodePointCount) {
		if (theString == null) {
			return null;
		}

		if (theCodePointCount < 0) {
			return "";
		}

		// char count can only be bigger than the code point count
		if (theString.length() <= theCodePointCount) {
			return theString;
		}

		return theString.substring(0, theString.offsetByCodePoints(0, theCodePointCount));
	}

	@Nonnull
	public static String prependLineNumbers(@Nonnull String theInput) {
		StringBuilder schemaOutput = new StringBuilder();
		int index = 0;
		for (String next : theInput.split("\\n")) {
			schemaOutput.append(index++).append(": ").append(next.replace("\r", "")).append("\n");
		}
		return schemaOutput.toString();
	}
}
