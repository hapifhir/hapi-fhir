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

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class TitleStandardizer extends LastNameStandardizer {

	private Set<String> myExceptions = new HashSet<>(Arrays.asList("EAS", "EPS", "LLC", "LLP", "of", "at", "in", "and"));
	private Set<String[]> myBiGramExceptions = new HashSet<String[]>();

	public TitleStandardizer() {
		super();
		addDelimiters("/", ".", "|", ">", "<", "(", ")", ":", "!");
		addAllowed('(', ')', '@', ':', '!', '|', '>', '<');
		myBiGramExceptions.add(new String[] {"'", "s"});
	}

	private void addAllowed(char... theCharacter) {
		for (char ch : theCharacter) {
			addAllowedExtendedAsciiRange((int) ch, (int) ch);
			addAllowedNonLetterAndDigitCharacters(ch);
		}
	}

	@Override
	public String standardize(String theString) {
		theString = replaceTranslates(theString);

		return Arrays.stream(theString.split("\\s+"))
			.map(String::trim)
			.map(this::standardizeText)
			.filter(s -> !StringUtils.isEmpty(s))
			.map(this::checkTitleExceptions)
			.collect(Collectors.joining(" "));
	}

	private List<String> split(String theString) {
		int cursor = 0;
		int start = 0;

		List<String> retVal = new ArrayList<>();
		StringBuilder buf = new StringBuilder();

		while (cursor < theString.length()) {
			int codePoint = theString.codePointAt(cursor);
			cursor += Character.charCount(codePoint);
			if (isNoiseCharacter(codePoint)) {
				continue;
			}

			String str = new String(Character.toChars(codePoint));
			if (isDelimiter(str)) {
				if (buf.length() != 0) {
					retVal.add(buf.toString());
					buf.setLength(0);
				}
				retVal.add(str);
				continue;
			}

			buf.append(str);
		}

		if (buf.length() != 0) {
			retVal.add(buf.toString());
		}

		return retVal;
	}

	protected String standardizeText(String theToken) {
		StringBuilder buf = new StringBuilder();
		List<String> parts = split(theToken);

		String prevPart = null;
		for(String part : parts) {
			if (isAllText(part)) {
				part = standardizeNameToken(part);
			}

			part = checkBiGram(prevPart, part);
			buf.append(part);
			prevPart = part;
		}
		return buf.toString();
	}

	private String checkBiGram(String thePart0, String thePart1) {
		for (String[] biGram : myBiGramExceptions) {
			if (biGram[0].equalsIgnoreCase(thePart0)
				&& biGram[1].equalsIgnoreCase(thePart1)) {
				return biGram[1];
			}
		}
		return thePart1;
	}

	private boolean isAllText(String thePart) {
		for (int offset = 0; offset < thePart.length(); ) {
			int codePoint = thePart.codePointAt(offset);
			if (!Character.isLetter(codePoint)) {
				return false;
			}
			offset += Character.charCount(codePoint);
		}
		return true;
	}

	@Override
	protected String standardizeNameToken(String theToken) {
		String exception = myExceptions.stream()
			.filter(s -> s.equalsIgnoreCase(theToken))
			.findFirst()
			.orElse(null);
		if (exception != null) {
			return exception;
		}

		return super.standardizeNameToken(theToken);
	}

	private String checkTitleExceptions(String theString) {
		return myExceptions.stream()
			.filter(s -> s.equalsIgnoreCase(theString))
			.findFirst()
			.orElse(theString);
	}
}
