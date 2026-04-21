package ca.uhn.fhir.context.support;

import org.hl7.fhir.r4.model.Coding;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for {@link ca.uhn.fhir.context.support.IValidationSupport.TranslateCodeRequest}
 * which is used as a cacheKey for $translate operations.
 */
class TranslateCodeRequestTest {

	@Test
	void testEqualsAndHashCodeWithNoVersion() {
		// Two requests with identical codings (but different instances)
		Coding coding1 = new Coding("http://loinc.org", "1234-5", "Test Display");
		Coding coding2 = new Coding("http://loinc.org", "1234-5", "Test Display");

		IValidationSupport.TranslateCodeRequest request1 =
			new IValidationSupport.TranslateCodeRequest(
				Collections.singletonList(coding1), "http://target.system");
		IValidationSupport.TranslateCodeRequest request2 =
			new IValidationSupport.TranslateCodeRequest(
				Collections.singletonList(coding2), "http://target.system");

		assertEquals(request1, request2);
		assertEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	void testEqualsAndHashCodeWithVersion() {
		Coding coding1 = codingWithVersion("http://loinc.org", "1234-5", null, "v1");
		Coding coding2 = codingWithVersion("http://loinc.org", "1234-5", null, "v1");

		IValidationSupport.TranslateCodeRequest request1 =
			new IValidationSupport.TranslateCodeRequest(
				Collections.singletonList(coding1), "http://target.system");
		IValidationSupport.TranslateCodeRequest request2 =
			new IValidationSupport.TranslateCodeRequest(
				Collections.singletonList(coding2), "http://target.system");

		assertEquals(request1, request2);
		assertEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	void testEqualsAndHashCodeWithMultipleCodings() {
		Coding coding1a = new Coding("http://loinc.org", "1234-5", null);
		Coding coding1b = new Coding("http://snomed.info/sct", "9999-0", null);
		Coding coding2a = new Coding("http://loinc.org", "1234-5", null);
		Coding coding2b = new Coding("http://snomed.info/sct", "9999-0", null);

		IValidationSupport.TranslateCodeRequest request1 =
			new IValidationSupport.TranslateCodeRequest(
				Arrays.asList(coding1a, coding1b), "http://target.system");
		IValidationSupport.TranslateCodeRequest request2 =
			new IValidationSupport.TranslateCodeRequest(
				Arrays.asList(coding2a, coding2b), "http://target.system");

		assertEquals(request1, request2);
		assertEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	void testEqualsAndHashCodeWithoutCodings() {
		IValidationSupport.TranslateCodeRequest request1 =
			new IValidationSupport.TranslateCodeRequest(
				Collections.emptyList(), "http://target.system");
		IValidationSupport.TranslateCodeRequest request2 =
			new IValidationSupport.TranslateCodeRequest(
				Collections.emptyList(), "http://target.system");

		assertEquals(request1, request2);
		assertEquals(request1.hashCode(), request2.hashCode());
	}

	@Test
	void testEqualsAndHashCodeIgnoresDisplay() {
		Coding coding1 = new Coding("http://loinc.org", "1234-5", "Display Text 1");
		Coding coding2 = new Coding("http://loinc.org", "1234-5", "Display Text 2");

		IValidationSupport.TranslateCodeRequest request1 =
			new IValidationSupport.TranslateCodeRequest(
				Collections.singletonList(coding1), "http://target.system");
		IValidationSupport.TranslateCodeRequest request2 =
			new IValidationSupport.TranslateCodeRequest(
				Collections.singletonList(coding2), "http://target.system");

		assertEquals(request1, request2);
		assertEquals(request1.hashCode(), request2.hashCode());
	}

	static Stream<Object[]> differenceTestCases() {
		return Stream.of(
			new Object[]{
				"different code",
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(new Coding("http://loinc.org", "1234-5", null)),
					"http://target.system"),
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(new Coding("http://loinc.org", "9999-9", null)),
					"http://target.system")
			},
			new Object[]{
				"different system",
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(new Coding("http://loinc.org", "1234-5", null)),
					"http://target.system"),
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(new Coding("http://snomed.info/sct", "1234-5", null)),
					"http://target.system")
			},
			new Object[]{
				"different version",
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(codingWithVersion("http://loinc.org", "1234-5", null, "v1")),
					"http://target.system"),
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(codingWithVersion("http://loinc.org", "1234-5", null, "v2")),
					"http://target.system")
			},
			new Object[]{
				"different target system",
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(new Coding("http://loinc.org", "1234-5", null)),
					"http://target.system"),
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(new Coding("http://loinc.org", "1234-5", null)),
					"http://different.target.system")
			},
			new Object[]{
				"different number of codings",
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(new Coding("http://loinc.org", "1234-5", null)),
					"http://target.system"),
				new IValidationSupport.TranslateCodeRequest(
					Arrays.asList(
						new Coding("http://loinc.org", "1234-5", null),
						new Coding("http://loinc.org", "6789-0", null)),
					"http://target.system")
			},
			new Object[]{
				"different coding order",
				new IValidationSupport.TranslateCodeRequest(
					Arrays.asList(
						new Coding("http://loinc.org", "1234-5", null),
						new Coding("http://snomed.info/sct", "9999-0", null)),
					"http://target.system"),
				new IValidationSupport.TranslateCodeRequest(
					Arrays.asList(
						new Coding("http://snomed.info/sct", "9999-0", null),
						new Coding("http://loinc.org", "1234-5", null)),
					"http://target.system")
			},
			new Object[]{
				"one with coding one without",
				new IValidationSupport.TranslateCodeRequest(
					Collections.singletonList(new Coding("http://loinc.org", "1234-5", null)),
					"http://target.system"),
				new IValidationSupport.TranslateCodeRequest(
					Collections.emptyList(),
					"http://target.system")
			}
		);
	}

	@ParameterizedTest
	@MethodSource("differenceTestCases")
	void testNotEqualRequests(String theMessage,
											IValidationSupport.TranslateCodeRequest theValue,
											IValidationSupport.TranslateCodeRequest theComparisonValue) {
		assertNotEquals(theValue, theComparisonValue);
		assertNotEquals(theValue.hashCode(), theComparisonValue.hashCode(), theMessage);
	}

	private static Coding codingWithVersion(String theSystem, String theCode, String theDisplay, String theVersion) {
		Coding coding = new Coding(theSystem, theCode, theDisplay);
		coding.setVersion(theVersion);
		return coding;
	}
}
