package ca.uhn.fhir.util;

import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Meta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.test.utilities.TagTestUtil.assertCodingsEqualAndInOrder;
import static ca.uhn.fhir.test.utilities.TagTestUtil.createCoding;
import static ca.uhn.fhir.test.utilities.TagTestUtil.createMeta;
import static ca.uhn.fhir.test.utilities.TagTestUtil.generateAllCodingPairs;
import static ca.uhn.fhir.test.utilities.TagTestUtil.toCanonicalTypeList;
import static ca.uhn.fhir.test.utilities.TagTestUtil.toStringList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Named.named;

class MetaTagSorterAlphabeticalTest {

	private MetaTagSorterAlphabetical myTagSorter;

	@BeforeEach
	public void beforeEach() {
		this.myTagSorter = new MetaTagSorterAlphabetical();
	}

	private static Stream<Arguments> provideTestCodings() {
		return Stream.of(
			Arguments.of(
				//the description of the test case
				named("the system is sorted before the code",
					// the Input
					List.of(createCoding("sys2", "code1"), createCoding("sys1", "code2"))),
				// the expected result
				List.of(createCoding("sys1", "code2"), createCoding("sys2", "code1"))
			),
			Arguments.of(
				//the description of the test case
				named("code determines the order if system are the same",
					// the Input
					List.of(createCoding("sys", "code2"), createCoding("sys", "code1"))),
				// the expected result
				List.of(createCoding("sys", "code1"), createCoding("sys", "code2"))
			),
			Arguments.of(
				//the description of the test case
				named("null system is less than non-null system",
					// the Input
					List.of(createCoding("sys", "code1"), createCoding(null, "code2"))),
				// the expected result
				List.of(createCoding(null, "code2"), createCoding("sys", "code1"))
			),
			Arguments.of(
				//the description of the test case
				named("null code is less than a non-null code",
					// the Input
					List.of(createCoding("sys", "code"), createCoding("sys", null))),
				// the expected result
				List.of(createCoding("sys", null), createCoding("sys", "code"))
			),
			Arguments.of(
				//the description of the test case
				named("works if both system and code are null",
					// the Input
					List.of(createCoding(null, null).setDisplay("display1"),
					createCoding(null, null).setDisplay("display2"))),
				// the expected result
				List.of(createCoding(null, null).setDisplay("display1"),
					createCoding(null, null).setDisplay("display2"))
			),
			Arguments.of(
				//the description of the test case
				named("works on a singleton list",
					// the Input
					List.of(createCoding("sys", "code"))),
				// the expected result
				List.of(createCoding("sys", "code"))
			),
			Arguments.of(
				//the description of the test case
				named("works on an empty list",
					// the Input
					Collections.EMPTY_LIST),
				// the expected result
				Collections.EMPTY_LIST
			),
			Arguments.of(
				//the description of the test case
				named("more than 2 tags",
					// the Input
					generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "b", "a"))),
				// the expected result
				generateAllCodingPairs(List.of("sys1", "sys2"), List.of("a", "b", "c"))
			)
		);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("provideTestCodings")
	public void testSortCodings(List<Coding> theInput, List<Coding> theExpected) {
		// Copy over the input tags into a new list since List.of creates immutable lists
		List<Coding> toBeSorted = new ArrayList<>(theInput);
		myTagSorter.sortCodings(toBeSorted);
		assertCodingsEqualAndInOrder(theExpected, toBeSorted);
	}

	private static Stream<Arguments> provideTestPrimitiveStrings() {
		return Stream.of(
			Arguments.of(
				//the description of the test case
				named("two sorted alphabetically",
					// the Input
					List.of("b", "a")),
				// the expected result
				List.of("a","b")

			),
			Arguments.of(
				//the description of the test case
				named("null is less than non-null value",
					// the Input
					Arrays.asList("a", null)),
				// the expected result
				Arrays.asList(null, "a")
			),
			Arguments.of(
				//the description of the test case
				named("works on a singleton list",
					// the Input
					List.of("x")),
				// the expected result
				List.of("x")
			),
			Arguments.of(
				//the description of the test case
				named("works on an empty list",
					// the Input
					Collections.EMPTY_LIST),
				// the expected result
				Collections.EMPTY_LIST
			),
			Arguments.of(
				//the description of the test case
				named("more than 2 in the list",
					// the Input
					List.of("c", "b", "a")),
				// the expected result
				List.of("a", "b", "c")
			)
		);
	}

	@ParameterizedTest(name = "{index}: {0}")
	@MethodSource("provideTestPrimitiveStrings")
	public void testSortPrimitiveStringTypes(List<String> theInput, List<String> theExpected) {
		List<CanonicalType> toBeSorted = toCanonicalTypeList(theInput);
		myTagSorter.sortPrimitiveStrings(toBeSorted);
		assertEquals(theExpected, toStringList(toBeSorted));
	}

	@Test
	public void testSort() {
		List<Coding> testCoding = List.of(createCoding("s", "2"), createCoding("s", "1"));
		List<String> profiles = List.of("2", "1");
		Meta meta = createMeta(testCoding, testCoding,  profiles);

		myTagSorter.sort(meta);

		List<Coding> expectedCoding = List.of(createCoding("s", "1"), createCoding("s", "2"));
		List<String> expectedProfile = List.of("1", "2");
		assertCodingsEqualAndInOrder(expectedCoding, meta.getTag());
		assertCodingsEqualAndInOrder(expectedCoding, meta.getSecurity());
		assertEquals(expectedProfile, toStringList(meta.getProfile()));
	}






}
