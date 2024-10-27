/*
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class UrlPathTokenizer {

	private String[] tokens;
	private int curPos;

	public UrlPathTokenizer(String theRequestPath) {
		if (theRequestPath == null) {
			theRequestPath = "";
		}
		tokens = removeBlanksAndSanitize(theRequestPath.split("/"));
		curPos = 0;
	}

	public boolean hasMoreTokens() {
		return curPos < tokens.length;
	}

	public int countTokens() {
		return tokens.length;
	}

	/**
	 * Returns the next token without updating the current position.
	 * Will throw NoSuchElementException if there are no more tokens.
	 */
	public String peek() {
		if (!hasMoreTokens()) {
			throw new NoSuchElementException(Msg.code(2420) + "Attempt to retrieve URL token out of bounds");
		}
		return tokens[curPos];
	}

	/**
	 * Returns the next portion. Any URL-encoding is undone, but we will
	 * HTML encode the &lt; and &quot; marks since they are both
	 * not useful un URL paths in FHIR and potentially represent injection
	 * attacks.
	 *
	 * @see UrlUtil#sanitizeUrlPart(String)
	 * @see UrlUtil#unescape(String)
	 */
	public String nextTokenUnescapedAndSanitized() {
		String token = peek();
		curPos++;
		return token;
	}

	/**
	 * Given an array of Strings, this method will return all the non-blank entries in that
	 * array, after running sanitizeUrlPart() and unescape() on them.
	 */
	private static String[] removeBlanksAndSanitize(String[] theInput) {
		List<String> output = new ArrayList<>();
		for (String s : theInput) {
			if (!isBlank(s)) {
				output.add(UrlUtil.sanitizeUrlPart(UrlUtil.unescape(s)));
			}
		}
		return output.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
	}
}
