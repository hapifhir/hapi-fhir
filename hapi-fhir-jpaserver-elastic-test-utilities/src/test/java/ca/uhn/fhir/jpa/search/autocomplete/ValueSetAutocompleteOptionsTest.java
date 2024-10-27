package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ValueSetAutocompleteOptions validation and parsing")
class ValueSetAutocompleteOptionsTest {
	static final int ERROR_AUTOCOMPLETE_ONLY_TYPE_LEVEL = 2020;
	static final int ERROR_AUTOCOMPLETE_REQUIRES_CONTEXT = 2021;
	static final int ERROR_REQUIRES_EXTENDED_INDEXING = 2022;

	private IPrimitiveType<String> myContext;
	private IPrimitiveType<String> myFilter;
	private IPrimitiveType<Integer> myCount;
	private IIdType myId;
	private IPrimitiveType<String> myUrl;
	private ValueSet myValueSet;
	private ValueSetAutocompleteOptions myOptionsResult;
	final private JpaStorageSettings myStorageSettings = new JpaStorageSettings();

	{
		myStorageSettings.setAdvancedHSearchIndexing(true);
	}

	@Test
	public void validWithBroadSPReference() {
		myContext = new StringDt("code");

		parseOptions();

		assertNotNull(myOptionsResult);
		assertNull(myOptionsResult.getResourceType());
		assertEquals("code", myOptionsResult.getSearchParamCode());
	}

	@Test
	public void validWithPlainSPReference() {
		myContext = new StringDt("Observation.code");

		parseOptions();

		assertNotNull(myOptionsResult);
		assertEquals("Observation", myOptionsResult.getResourceType());
		assertEquals("code", myOptionsResult.getSearchParamCode());
		assertNull(myOptionsResult.getSearchParamModifier());
	}

	@Test
	public void validWithTextModifier() {
		myContext = new StringDt("Observation.code:text");

		parseOptions();

		assertNotNull(myOptionsResult);
		assertEquals("Observation", myOptionsResult.getResourceType());
		assertEquals("code", myOptionsResult.getSearchParamCode());
		assertEquals("text", myOptionsResult.getSearchParamModifier());
	}

	@Test
	public void validContextWithFilter() {
		myContext = new StringDt("Observation.code:text");
		myFilter = new StringDt("blood");

		parseOptions();

		assertNotNull(myOptionsResult);
		assertEquals("blood", myOptionsResult.getFilter());
	}

	@Test
	public void emptyFilterOK() {
		myContext = new StringDt("Observation.code:text");
		myFilter = new StringDt("");

		parseOptions();

		assertNotNull(myOptionsResult);
		assertEquals("", myOptionsResult.getFilter());
	}

	@Test
	public void defaultCountAndOffsetAreEmpty() {
		myContext = new StringDt("Observation.code:text");

		parseOptions();

		assertEquals(Optional.empty(), myOptionsResult.getCount());
	}

	@Test
	public void parsesCount() {
		myContext = new StringDt("Observation.code:text");
		myCount = new IntegerDt(50);

		parseOptions();

		assertNotNull(myOptionsResult);
		assertEquals(Optional.of(50), myOptionsResult.getCount());
	}

	@Nested
	@DisplayName("is invalid")
	public class InvalidCases {
		@Test
		public void withId() {
			myId = new IdDt("123");

			assertParseThrowsInvalidRequestWithErrorCode(ERROR_AUTOCOMPLETE_ONLY_TYPE_LEVEL);
		}

		@Test
		public void withValueSetIdentifier() {
			myUrl = new StringDt("http://example.com");

			assertParseThrowsInvalidRequestWithErrorCode(ERROR_AUTOCOMPLETE_ONLY_TYPE_LEVEL);
		}

		@Test
		public void withValueSet() {
			myValueSet = new ValueSet();
			myValueSet.addIdentifier().setValue("anId");

			assertParseThrowsInvalidRequestWithErrorCode(ERROR_AUTOCOMPLETE_ONLY_TYPE_LEVEL);
		}

		@Test
		public void withoutContext() {
			myFilter = new StringDt("blood");

			assertParseThrowsInvalidRequestWithErrorCode(ERROR_AUTOCOMPLETE_REQUIRES_CONTEXT);
		}

		@Test
		public void withEmptyContext() {
			myFilter = new StringDt("blood");
			myContext = new StringDt("");

			assertParseThrowsInvalidRequestWithErrorCode(ERROR_AUTOCOMPLETE_REQUIRES_CONTEXT);
		}

		@Test
		public void withUnsupportedModifier() {
			myFilter = new StringDt("blood");
			myContext = new StringDt("Observation.code:exact");

			assertParseThrowsInvalidRequestWithErrorCode(2069);
		}

		@Test
		public void whenAdvancedIndexingOff() {
		    // given
			myStorageSettings.setAdvancedHSearchIndexing(false);

			assertParseThrowsInvalidRequestWithErrorCode(ERROR_REQUIRES_EXTENDED_INDEXING);
		}


		private void assertParseThrowsInvalidRequestWithErrorCode(int theErrorCode) {
			InvalidRequestException e = assertThrows(InvalidRequestException.class, ValueSetAutocompleteOptionsTest.this::parseOptions);
			assertThat(e.getMessage()).startsWith(Msg.code(theErrorCode));
		}

	}

	void parseOptions() {
		myOptionsResult = ValueSetAutocompleteOptions.validateAndParseOptions(myStorageSettings, myContext, myFilter, myCount, myId, myUrl, myValueSet);
	}

}
