package ca.uhn.fhir.test.utilities;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ca.uhn.test.util.AssertJson.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssertJsonTest {

	private final String myJsonString = """
			{
			    "firstName": "John",
			    "lastName": "Smith",
			    "age": 25,
			    "address": {
			        "streetAddress": "21 2nd Street",
			        "city": "New York",
			        "state": "NY",
			        "postalCode": 10021
			    },
			    "phoneNumbers": [
			        {
			            "type": "home",
			            "number": "212 555-1234"
			        },
			        {
			            "type": "fax",
			            "number": "646 555-4567"
			        }
			    ]
			}
			""";

	String goodAddress = """
			{
				"streetAddress": "21 2nd Street",
			    "city": "New York",
			    "state": "NY",
			    "postalCode": 10021
			}
			""";

	String wrongAddress = """
			{
				"streetAddress": "432 Hillcrest Rd",
			    "city": "Mississauga",
			    "state": "ON",
			    "postalCode": 10021
			}
			""";

	String goodPhoneNumbers = """
		[
			{
				"type": "home",
				"number": "212 555-1234"
			},
			{
				"type": "fax",
				"number": "646 555-4567"
			}
		]
		""";

	String wrongPhoneNumbers = """
		[
			{
				"type": "cell",
				"number": "212 555-9876"
			},
			{
				"type": "fax",
				"number": "646 666-4567"
			}
		]
		""";

	@Test
	void testFluency() {
		assertThat(myJsonString)
			.hasPath("address.city")
			.hasPaths("firstName", "address.city")
			.hasKeys("firstName", "address")
			.hasExactlyKeys("firstName", "lastName", "age", "address", "phoneNumbers")
			.hasKeyWithValue("lastName", "Smith")
			.hasKeysWithValues(
				List.of("firstName", "lastName"),
				List.of("John", "Smith"))
			.hasExactlyKeysWithValues(
				List.of("firstName", "lastName", "age", "address", "phoneNumbers"),
				List.of("John", "Smith", 25, goodAddress, goodPhoneNumbers))
			.hasPath("lastName");
	}

	@Nested
	class TestHasPath {

		@Test
		void test_success() {
			assertThat(myJsonString).hasPath("address.city");
		}

		@Test
		void test_fails_pathNotFound() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPath("address.city.neighbourhood"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("address.city doesn't contain a json object but a plain string");
		}
	}

	@Nested
	class TestHasPaths {

		@Test
		void test_success() {
			assertThat(myJsonString).hasPaths("lastName", "address.city");
		}

		@Test
		void test_fails_pathNotFound() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPaths("lastName", "address.city.neighbourhood"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("address.city doesn't contain a json object but a plain string");
		}
	}

	@Nested
	class TestHasPathWithValue {

		@Test
		void testStringValue_success() {
			assertThat(myJsonString).hasPathWithValue("address.city", "New York");
		}

		@Test
		void testExpectedStringValue_failure_isList() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathWithValue(
				"phoneNumbers.number",
				"212 555-1234"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Path: phoneNumbers' is not a json object but a Json  list");
		}

		@Test
		void testExpectedStringValue_failure_isObject() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathWithValue(
				"address",
				"432 Hillcrest Rd"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Types mismatch. Te expected 'address' value is a plain string (not json) whereas the actual value is a json object");
		}

		@Test
		void testExpectedStringValue_failure_differentString() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathWithValue(
				"address.streetAddress",
				"181 Hillcrest Rd."))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("expected: \"181 Hillcrest Rd.\"")
				.hasMessageContaining("but was: \"21 2nd Street\"");
		}

		@Test
		void testExpectedObjectValue_success() {
			assertThat(myJsonString).hasPathWithValue("address", goodAddress);
		}

		@Test
		void testExpectedObjectValue_failure_isString() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathWithValue("age", goodAddress))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Types mismatch. Te expected 'age' value is a json object whereas the actual value is a plain string (not json)");
		}

		@Test
		void testExpectedObjectValue_failure_isList() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathWithValue("phoneNumbers", "223-217-5555"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Types mismatch. Te expected 'phoneNumbers' value is a plain string (not json) whereas the actual value is a json list");
		}

		@Test
		void testExpectedObjectValue_failure_different() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathWithValue("address", wrongAddress))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("""
					Multiple Failures (3 failures)
					-- failure 1 --
					[Unexpected value for key: streetAddress:]\s
					expected: "432 Hillcrest Rd"
					 but was: "21 2nd Street"
					""")
				.hasMessageContaining("""
					-- failure 2 --
					[Unexpected value for key: city:]\s
					expected: "Mississauga"
					 but was: "New York"
					""")
				.hasMessageContaining("""
					-- failure 3 --
					[Unexpected value for key: state:]\s
					expected: "ON"
					 but was: "NY"
					""");
		}

		@Test
		void testExpectedListValue_success() {
			assertThat(myJsonString).hasPathWithValue("phoneNumbers", goodPhoneNumbers);
		}

		@Test
		void testExpectedListValue_failure_isString() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathWithValue("phoneNumbers", "222-555-7654"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Types mismatch. Te expected 'phoneNumbers' value is a plain string (not json) whereas the actual value is a json list");
		}

		@Test
		void testExpectedListValue_failure_isObject() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathWithValue("phoneNumbers", goodAddress))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Types mismatch. Te expected 'phoneNumbers' value is a json object whereas the actual value is a json list");
		}

		@Test
		void testExpectedListValue_failure_different() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathWithValue("phoneNumbers", wrongPhoneNumbers))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("Expecting ArrayList:")
				.hasMessageContaining("""
					element(s) not found:
					  [{"number"="212 555-9876", "type"="cell"},
					    {"number"="646 666-4567", "type"="fax"}]
					and element(s) not expected:
					  [{"number"="212 555-1234", "type"="home"},
					    {"number"="646 555-4567", "type"="fax"}]
					""");
		}

	}

	@Nested
	class TestHasPathsWithValues {

		@Test
		void testMixedValues_success() {
			assertThat(myJsonString).hasPathsWithValues(List.of("phoneNumbers", "address", "lastName"),
				List.of(goodPhoneNumbers, goodAddress, "Smith"));
		}

		@Test
		void testDifferentSizes_failure() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathsWithValues(List.of("phoneNumbers"), List.of(goodPhoneNumbers, wrongPhoneNumbers)))
				.isInstanceOf(AssertionError.class)
				.hasMessageStartingWith("Paths size (1) is different than values size (2)");
		}

		@Test
		void testMixedValues_failure_wrongTypeString() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathsWithValues(List.of("phoneNumbers", "address", "lastName"),
				List.of(goodPhoneNumbers, goodAddress, wrongAddress)))
				.isInstanceOf(AssertionError.class)
				.hasMessage("Types mismatch. Te expected 'lastName' value is a json object whereas the actual value is a plain string (not json)");
		}

		@Test
		void testMixedValues_failure_wrongTypeList() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathsWithValues(List.of("phoneNumbers", "address", "lastName"),
				List.of("Wesson", goodAddress, "Smith")))
				.isInstanceOf(AssertionError.class)
				.hasMessage("Types mismatch. Te expected 'phoneNumbers' value is a plain string (not json) whereas the actual value is a json list");
		}

		@Test
		void testMixedValues_failure_wrongTypeObject() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathsWithValues(List.of("phoneNumbers", "address", "lastName"),
				List.of(goodPhoneNumbers, "Wesson", "Smith")))
				.isInstanceOf(AssertionError.class)
				.hasMessage("Types mismatch. Te expected 'address' value is a plain string (not json) whereas the actual value is a json object");
		}

		@Test
		void testMixedValues_failure_differentString() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathsWithValues(List.of("phoneNumbers", "address", "lastName"),
				List.of(goodPhoneNumbers, goodAddress, "Wesson")))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("[Extracted: lastName]")
				.hasMessageContaining("expected: \"Wesson\"")
				.hasMessageContaining("but was: \"Smith\"");
		}

		@Test
		void testMixedValues_failure_differentList() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathsWithValues(List.of("phoneNumbers", "address", "lastName"),
				List.of(wrongPhoneNumbers, goodAddress, "Smith")))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("""
					element(s) not found:
					  [{"number"="212 555-9876", "type"="cell"},
					    {"number"="646 666-4567", "type"="fax"}]
					and element(s) not expected:
					  [{"number"="212 555-1234", "type"="home"},
					    {"number"="646 555-4567", "type"="fax"}]
					""");
		}

		@Test
		void testMixedValues_failure_differentObject() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasPathsWithValues(List.of("phoneNumbers", "address", "lastName"),
				List.of(goodPhoneNumbers, wrongAddress, "Smith")))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("""
					Multiple Failures (3 failures)
					-- failure 1 --
					[Unexpected value for key: streetAddress:]\s
					expected: "432 Hillcrest Rd"
					 but was: "21 2nd Street"
					""")
				.hasMessageContaining("""
					-- failure 2 --
					[Unexpected value for key: city:]\s
					expected: "Mississauga"
					 but was: "New York"
					""")
				.hasMessageContaining("""
					-- failure 3 --
					[Unexpected value for key: state:]\s
					expected: "ON"
					 but was: "NY"
					""");
		}

	}



	@Test
	void hasExactlyKeys() {
		assertThat(myJsonString).hasExactlyKeys("firstName", "lastName", "age", "address", "phoneNumbers");
	}

	@Nested
	class TestHasExactlyKeys {

		@Test
		void hasExactlyKeys_succeeds() {
			assertThat(myJsonString).hasExactlyKeys("firstName", "lastName", "age", "address", "phoneNumbers");
		}

		@Test
		void hasExactlyKeys_fails() {
			assertThatThrownBy(() -> assertThat(myJsonString)
					.hasExactlyKeys("firstName", "age", "address", "phoneNumbers", "extraKey"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("""
						element(s) not found:
						  ["extraKey"]""")
				.hasMessageContaining("""
						and element(s) not expected:
						  ["lastName"]""");
		}
	}

	@Nested
	class TestHasKeysWithValues {

		@Test
		void hasExactlyKeysWithValues_succeeds() {
			assertThat(myJsonString).hasKeysWithValues(
				List.of("firstName", "lastName", "age"),
				List.of("John", "Smith", 25));
		}

		@Test
		void testKeysAndValues_haveDifferentSizes_fails() {
			assertThatThrownBy(() ->
				assertThat(myJsonString).hasKeysWithValues(
					List.of("firstName", "lastName", "age"),
					List.of("John", "Wesson", 31, 28)))
				.isInstanceOf(AssertionError.class)
				.hasMessage("Keys and values should have same size. Received 3 keys and 4 values.");
		}

		@Test
		void testKeysAndValues_haveDifferentValues_failsShowingAllProblems() {
			assertThatThrownBy(() ->
				assertThat(myJsonString).hasKeysWithValues(
					List.of("firstName", "lastName", "age"),
					List.of("John", "Wesson", 31)))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("""
						Multiple Failures (2 failures)
						-- failure 1 --
						[Unexpected value for key: lastName:]\s
						expected: "Wesson"
						 but was: "Smith"
						""")
				.hasMessageContaining("""
						-- failure 2 --
						[Unexpected value for key: age:]\s
						expected: 31
						 but was: 25
						""");
		}
	}

	@Nested
	class TestHasKeys {
		@Test
		void testSuccess() {
			assertThat(myJsonString).hasKeys("address", "phoneNumbers");
		}

		@Test
		void testFailure() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasKeys("address", "weight"))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("""
					but could not find the following element(s):
					  ["weight"]
					""");
		}
	}

	@Nested
	class TestHasKeyWithValue {

		@Test
		void testSuccess() {
			assertThat(myJsonString).hasKeyWithValue("address", goodAddress);
		}

		@Test
		void testFailure() {
			assertThatThrownBy(() -> assertThat(myJsonString).hasKeyWithValue("address", wrongAddress))
				.isInstanceOf(AssertionError.class)
				.hasMessageContaining("""
					Multiple Failures (3 failures)
					-- failure 1 --
					[Unexpected value for key: streetAddress:]\s
					expected: "432 Hillcrest Rd"
					 but was: "21 2nd Street"
					""")
				.hasMessageContaining("""
					-- failure 2 --
					[Unexpected value for key: city:]\s
					expected: "Mississauga"
					 but was: "New York"
					""")
				.hasMessageContaining("""
					-- failure 3 --
					[Unexpected value for key: state:]\s
					expected: "ON"
					 but was: "NY"
					""");
		}

	}


}
