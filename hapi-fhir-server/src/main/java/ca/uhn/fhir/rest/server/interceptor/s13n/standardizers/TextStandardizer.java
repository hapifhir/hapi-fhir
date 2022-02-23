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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Standardizes text literals by removing noise characters.
 */
public class TextStandardizer implements IStandardizer {

	public static final Pattern DIACRITICAL_MARKS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

	public static final int EXT_ASCII_RANGE_START = 155;
	public static final int EXT_ASCII_RANGE_END = 255;

	private List<Range> myAllowedExtendedAscii;
	private Set<Integer> myAllowedNonLetterAndDigitCharacters = new HashSet<>();
	private NoiseCharacters myNoiseCharacters = new NoiseCharacters();
	private Map<Integer, Character> myTranslates = new HashMap<>();

	public TextStandardizer() {
		myNoiseCharacters.initializeFromClasspath();

		initializeAllowedNonLetterAndDigitCharacters();
		initializeTranslates();
		initializeAllowedExtendedAscii();
	}

	protected void initializeAllowedNonLetterAndDigitCharacters() {
		addAllowedNonLetterAndDigitCharacters('.', '\'', ',', '-', '#', '/', '\\', ' ');
	}

	protected TextStandardizer addAllowedNonLetterAndDigitCharacters(Character... theCharacters) {
		myAllowedNonLetterAndDigitCharacters.addAll(asSet(theCharacters));
		return this;
	}

	protected Set<Integer> asSet(Character... theCharacters) {
		return Arrays.stream(theCharacters)
			.map(c -> (int) c)
			.collect(Collectors.toSet());
	}

	protected TextStandardizer addTranslate(int theTranslate, char theMapping) {
		myTranslates.put(theTranslate, theMapping);
		return this;
	}

	protected void initializeTranslates() {
		addTranslate(0x0080, '\''); // PAD
		addTranslate(0x00A0, ' '); // &nbsp
		addTranslate((int) ' ', ' '); // &nbsp
		addTranslate(0x201C, '"');
		addTranslate(0x201D, '"');
		addTranslate(0x2019, ' ');
		addTranslate(0x2018, ' ');
		addTranslate(0x02BD, ' ');
		addTranslate(0x00B4, ' ');
		addTranslate(0x02DD, '"');
		addTranslate((int) '–', '-');
		addTranslate((int) '-', '-');
		addTranslate((int) '~', '-');
	}

	protected void initializeAllowedExtendedAscii() {
		myAllowedExtendedAscii = new ArrayList<>();

		// refer to https://www.ascii-code.com for the codes
		for (int[] i : new int[][]{{192, 214}, {216, 246}, {248, 255}}) {
			addAllowedExtendedAsciiRange(i[0], i[1]);
		}
	}

	protected TextStandardizer addAllowedExtendedAsciiRange(int theRangeStart, int theRangeEnd) {
		myAllowedExtendedAscii.add(new Range(theRangeStart, theRangeEnd));
		return this;
	}

	public String standardize(String theString) {
		theString = replaceTranslates(theString);
		return removeNoise(theString);
	}

	protected String replaceTranslates(String theString) {
		StringBuilder buf = new StringBuilder(theString.length());
		for (char ch : theString.toCharArray()) {
			if (myTranslates.containsKey((int) ch)) {
				buf.append(myTranslates.get((int) ch));
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}

	protected String replaceAccents(String theString) {
		String string = java.text.Normalizer.normalize(theString, java.text.Normalizer.Form.NFD);
		return DIACRITICAL_MARKS.matcher(string).replaceAll("");
	}

	protected String removeNoise(String theToken) {
		StringBuilder token = new StringBuilder(theToken.length());
		for (int offset = 0; offset < theToken.length(); ) {
			int codePoint = theToken.codePointAt(offset);
			offset += Character.charCount(codePoint);

			switch (Character.getType(codePoint)) {
				case Character.CONTROL:     // \p{Cc}
				case Character.FORMAT:      // \p{Cf}
				case Character.PRIVATE_USE: // \p{Co}
				case Character.SURROGATE:   // \p{Cs}
				case Character.UNASSIGNED:  // \p{Cn}
					break;
				default:
					if (!isNoiseCharacter(codePoint)) {
						token.append(Character.toChars(codePoint));
					}
					break;
			}
		}
		return token.toString();
	}

	protected boolean isTranslate(int theChar) {
		return myTranslates.containsKey(theChar);
	}

	protected boolean isNoiseCharacter(int theChar) {
		if (myAllowedExtendedAscii.stream().anyMatch(r -> r.isInRange(theChar))) {
			return false;
		}
		boolean isExtendedAscii = (theChar >= EXT_ASCII_RANGE_START && theChar <= EXT_ASCII_RANGE_END);
		if (isExtendedAscii) {
			return true;
		}
		return myNoiseCharacters.isNoise(theChar);
	}

}
