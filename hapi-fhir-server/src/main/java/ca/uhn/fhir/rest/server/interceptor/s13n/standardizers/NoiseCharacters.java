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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.interceptor.ConfigLoader;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NoiseCharacters {

	private static final int RANGE_THRESHOLD = 150;

	private Set<Integer> myNoiseCharacters = new HashSet<>();
	private Set<Range> myNoiseCharacterRanges = new HashSet<>();

	private int size;

	public int getSize() {
		return myNoiseCharacters.size();
	}

	public void initializeFromClasspath() {
		String noiseChars = ConfigLoader.loadResourceContent("classpath:noise-chars.txt");
		try (Scanner scanner = new Scanner(noiseChars)) {
			while (scanner.hasNext()) {
				parse(scanner.nextLine());
			}
		}
	}

	public boolean isNoise(int theChar) {
		if (myNoiseCharacters.contains(theChar)) {
			return true;
		}

		for (Range r : myNoiseCharacterRanges) {
			if (r.isInRange(theChar)) {
				return true;
			}
		}

		return false;
	}

	private void parse(String theString) {
		if (theString.contains("-")) {
			addRange(theString);
		} else {
			add(theString);
		}
	}

	public NoiseCharacters add(String theLiteral) {
		myNoiseCharacters.add(toInt(theLiteral));
		return this;
	}

	public NoiseCharacters addRange(String theRange) {
		if (!theRange.contains("-")) {
			throw new IllegalArgumentException(Msg.code(350) + String.format("Invalid range %s", theRange));
		}

		String[] range = theRange.split("-");
		if (range.length < 2) {
			throw new IllegalArgumentException(Msg.code(351) + String.format("Invalid range %s", theRange));
		}

		addRange(range[0].trim(), range[1].trim());
		return this;
	}

	public NoiseCharacters addRange(String theLowerBound, String theUpperBound) {
		int lower = toInt(theLowerBound);
		int upper = toInt(theUpperBound);

		if (lower > upper) {
			throw new IllegalArgumentException(Msg.code(352) + String.format("Invalid character range %s-%s", theLowerBound, theUpperBound));
		}

		if (upper - lower >= RANGE_THRESHOLD) {
			myNoiseCharacterRanges.add(new Range(lower, upper));
			return this;
		}

		for (int i = lower; i <= upper; i++) {
			myNoiseCharacters.add(i);
		}
		return this;
	}

	private int toInt(String theLiteral) {
		if (!theLiteral.startsWith("#x")) {
			throw new IllegalArgumentException(Msg.code(353) + "Unable to parse " + theLiteral);
		}

		return Integer.parseInt(theLiteral.substring(2), 16);
	}

}
