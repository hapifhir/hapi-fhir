package ca.uhn.fhir.jpa.conformance;

/*-
 * #%L
 * HAPI FHIR Test Utilities
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

import ca.uhn.fhir.util.CollectionUtil;
import org.junit.jupiter.params.provider.Arguments;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Collection of test cases for date type search.
 *
 * Each test case includes a resource value, a query value, the operator to test, and the expected result.
 *
 * @see <a href="https://www.hl7.org/fhir/search.html#date">the spec</a>
 */
public class DateSearchTestCase {
	final String myResourceValue;
	final String myQueryValue;
	final boolean expectedResult;
	final String myFileName;
	final int myLineNumber;

	public DateSearchTestCase(String myResourceValue, String myQueryValue, boolean expectedResult, String theFileName, int theLineNumber) {
		this.myResourceValue = myResourceValue;
		this.myQueryValue = myQueryValue;
		this.expectedResult = expectedResult;
		this.myFileName = theFileName;
		this.myLineNumber = theLineNumber;
	}

	public Arguments toArguments() {
		return Arguments.of(myResourceValue, myQueryValue, expectedResult, myFileName, myLineNumber);
	}

	/**
	 * We have two sources of test cases:
	 * - DateSearchTestCase.csv which holds one test case per line
	 * - DateSearchTestCase-compact.csv which specifies all operators for each value pair
 	 */
	public final static List<DateSearchTestCase> ourCases;
	static {
		ourCases = new ArrayList<>();
		ourCases.addAll(expandedCases());
		ourCases.addAll(compactCases());
	}

	private static List<DateSearchTestCase> expandedCases() {
		String csv = "DateSearchTestCase.csv";
		InputStream resource = DateSearchTestCase.class.getResourceAsStream(csv);
		assert resource != null;
		InputStreamReader inputStreamReader = new InputStreamReader(resource, StandardCharsets.UTF_8);
		List<DateSearchTestCase> cases = parseCsvCases(inputStreamReader, csv);
		try {
			resource.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cases;
	}

	static List<DateSearchTestCase> parseCsvCases(Reader theSource, String theFileName) {
		LineNumberReader lineNumberReader = new LineNumberReader(theSource);
		return lineNumberReader.lines()
			.filter(l->!l.startsWith("#")) // strip comments
			.map(l -> l.split(","))
			.map(fields -> new DateSearchTestCase(fields[0].trim(), fields[1].trim(), Boolean.parseBoolean(fields[2].trim()), theFileName, lineNumberReader.getLineNumber()))
			.collect(Collectors.toList());
	}

	public static List<DateSearchTestCase> compactCases() {
		String compactCsv = "DateSearchTestCase-compact.csv";
		InputStream compactStream = DateSearchTestCase.class.getResourceAsStream(compactCsv);
		assert compactStream != null;
		return expandPrefixCases(new InputStreamReader(compactStream, StandardCharsets.UTF_8), compactCsv);
	}

	/**
	 * helper for compressed format of date test cases.
	 * <p>
	 * The csv has rows with: Matching prefixes, Query Date, Resource Date
	 * E.g. "eq ge le,2020, 2020"
	 * This helper expands that one line into test for all of: eq, ge, gt, le, lt, and ne,
	 * expecting the listed prefixes to match, and the unlisted ones to not match.
	 *
	 * @return List of test cases
	 */
	@Nonnull
	static List<DateSearchTestCase> expandPrefixCases(Reader theSource, String theFileName) {
		Set<String> supportedPrefixes = CollectionUtil.newSet("eq", "ge", "gt", "le", "lt", "ne");

		// expand these into individual tests for each prefix.
		LineNumberReader lineNumberReader = new LineNumberReader(theSource);
		return lineNumberReader.lines()
			.filter(l->!l.startsWith("#")) // strip comments
			.map(l -> l.split(","))
			.flatMap(fields -> {
				// line looks like: "eq ge le,2020, 2020"
				// Matching prefixes, Query Date, Resource Date
				String resourceValue = fields[0].trim();
				String truePrefixes = fields[1].trim();
				String queryValue = fields[2].trim();
				Set<String> expectedTruePrefixes = Arrays.stream(truePrefixes.split("\\s+")).map(String::trim).collect(Collectors.toSet());

				// expand to one test case per supportedPrefixes
				return supportedPrefixes.stream()
					.map(prefix -> {
						boolean expectMatch = expectedTruePrefixes.contains(prefix);
						return new DateSearchTestCase(resourceValue, prefix + queryValue, expectMatch, theFileName, lineNumberReader.getLineNumber());
					});
			})
			.collect(Collectors.toList());
	}
}
