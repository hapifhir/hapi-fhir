package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
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
	final private DaoConfig myDaoConfig = new DaoConfig();

	{
		myDaoConfig.setAdvancedLuceneIndexing(true);
	}

	@Test
	public void validWithBroadSPReference() {
		myContext = new StringDt("code");

		parseOptions();

		assertThat(myOptionsResult, is(not(nullValue())));
		assertThat(myOptionsResult.getResourceType(), is(nullValue()));
		assertThat(myOptionsResult.getSearchParamCode(), equalTo("code"));
	}

	@Test
	public void validWithPlainSPReference() {
		myContext = new StringDt("Observation.code");

		parseOptions();

		assertThat(myOptionsResult, is(not(nullValue())));
		assertThat(myOptionsResult.getResourceType(), equalTo("Observation"));
		assertThat(myOptionsResult.getSearchParamCode(), equalTo("code"));
		assertThat(myOptionsResult.getSearchParamModifier(), is(nullValue()));
	}

	@Test
	public void validWithTextModifier() {
		myContext = new StringDt("Observation.code:text");

		parseOptions();

		assertThat(myOptionsResult, is(not(nullValue())));
		assertThat(myOptionsResult.getResourceType(), equalTo("Observation"));
		assertThat(myOptionsResult.getSearchParamCode(), equalTo("code"));
		assertThat(myOptionsResult.getSearchParamModifier(), equalTo("text"));
	}

	@Test
	public void validContextWithFilter() {
		myContext = new StringDt("Observation.code:text");
		myFilter = new StringDt("blood");

		parseOptions();

		assertThat(myOptionsResult, is(not(nullValue())));
		assertThat(myOptionsResult.getFilter(), equalTo("blood"));
	}

	@Test
	public void emptyFilterOK() {
		myContext = new StringDt("Observation.code:text");
		myFilter = new StringDt("");

		parseOptions();

		assertThat(myOptionsResult, is(not(nullValue())));
		assertThat(myOptionsResult.getFilter(), equalTo(""));
	}

	@Test
	public void defaultCountAndOffsetAreEmpty() {
		myContext = new StringDt("Observation.code:text");

		parseOptions();

		assertThat(myOptionsResult.getCount(), is(equalTo(Optional.empty())));
	}

	@Test
	public void parsesCount() {
		myContext = new StringDt("Observation.code:text");
		myCount = new IntegerDt(50);

		parseOptions();

		assertThat(myOptionsResult, is(not(nullValue())));
		assertThat(myOptionsResult.getCount(), equalTo(Optional.of(50)));
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
			myDaoConfig.setAdvancedLuceneIndexing(false);

			assertParseThrowsInvalidRequestWithErrorCode(ERROR_REQUIRES_EXTENDED_INDEXING);
		}


		private void assertParseThrowsInvalidRequestWithErrorCode(int theErrorCode) {
			InvalidRequestException e = assertThrows(InvalidRequestException.class, ValueSetAutocompleteOptionsTest.this::parseOptions);
			assertThat(e.getMessage(), startsWith(Msg.code(theErrorCode)));
		}

	}

	void parseOptions() {
		myOptionsResult = ValueSetAutocompleteOptions.validateAndParseOptions(myDaoConfig, myContext, myFilter, myCount, myId, myUrl, myValueSet);
	}

}
