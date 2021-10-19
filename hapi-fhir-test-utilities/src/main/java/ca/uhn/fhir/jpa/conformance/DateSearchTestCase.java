package ca.uhn.fhir.jpa.conformance;

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

public class DateSearchTestCase {
	final String myResourceValue;
	final String myQueryValue;
	final boolean expectedResult;
	final String myFileName;
	private final int myLineNumber;

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

	public final static List<DateSearchTestCase> ourCases;
	static {
		String csv = "DateSearchTestCase.csv";
		InputStream resource = DateSearchTestCase.class.getResourceAsStream(csv);
		assert resource != null;
		ourCases = parseCsvCases(new InputStreamReader(resource, StandardCharsets.UTF_8), csv);
		// wipmb merge these once we have the nih-fixes.
		//ourCases.addAll(yearPrecisionDateSearchCases());
	}

	static List<DateSearchTestCase> parseCsvCases(Reader theSource, String theFileName) {
		LineNumberReader lineNumberReader = new LineNumberReader(theSource);
		return lineNumberReader.lines()
			.map(l -> l.split(","))
			// todo MB drop precisions wider than a day until we merge with nih-testing
			.filter(
				fields ->
					fields[0].length() >= 10 &&
					fields[1].length() >= 10 &&
					!fields[0].startsWith("#"))
			.map(fields -> new DateSearchTestCase(fields[0].trim(), fields[1].trim(), Boolean.parseBoolean(fields[2].trim()), theFileName, lineNumberReader.getLineNumber()))
			.collect(Collectors.toList());
	}

	// wipmb fix for these is on nih-testing branch
	static final String[] ourYearPrecisionDatePrefixCases = {
		"Resource Date, Matching prefixes, Query Date",
		"2020, eq ge le,2020",
		"2021, gt ge ne,2020",
		"2020, lt le ne,2021",
		"2021-01-01, ne gt ge,2020"
	};

	public static List<Arguments> yearPrecisionDateSearchCases() throws IOException {
		return expandPrefixCases(Arrays.asList(ourYearPrecisionDatePrefixCases));
	}

	/**
	 * helper for compressed format of date test cases.
	 * <p>
	 * The csv has rows with: Matching prefixes, Query Date, Resource Date
	 * E.g. "eq ge le,2020, 2020"
	 * This helper expands that one line into test for all of eq, ge, gt, le, lt, and ne,
	 * expecting the listed prefixes to match, and the unlisted ones to not match.
	 *
	 * @return the individual test case arguments for testDateSearchMatching()
	 */
	@Nonnull
	static List<Arguments> expandPrefixCases(List<String> theTestCaseLines) {
		Set<String> supportedPrefixes = CollectionUtil.newSet("eq", "ge", "gt", "le", "lt", "ne");

		// expand these into individual tests for each prefix.
		List<Arguments> testCases = new ArrayList<>();
		for (String line : theTestCaseLines) {
			// line looks like: "eq ge le,2020, 2020"
			// Matching prefixes, Query Date, Resource Date
			String[] fields = line.split(",");
			String truePrefixes = fields[0].trim();
			String queryValue = fields[1].trim();
			String resourceValue = fields[2].trim();

			Set<String> expectedTruePrefixes = Arrays.stream(truePrefixes.split(" +")).map(String::trim).collect(Collectors.toSet());

			for (String prefix : supportedPrefixes) {
				boolean expectMatch = expectedTruePrefixes.contains(prefix);
				testCases.add(Arguments.of(resourceValue, prefix + queryValue, expectMatch));
			}
		}

		return testCases;
	}
}
