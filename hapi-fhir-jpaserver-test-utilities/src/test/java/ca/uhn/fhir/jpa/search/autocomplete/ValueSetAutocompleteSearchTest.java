package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

class ValueSetAutocompleteSearchTest {
	FhirContext myFhirContext = FhirContext.forR4();
	private ModelConfig myModelConfig;
	ValueSetAutocompleteSearch myValueSetAutocompleteSearch = new ValueSetAutocompleteSearch(myFhirContext, myModelConfig, null);

	@Nested
	public class HitToValueSetConversion {

		private ValueSet.ValueSetExpansionContainsComponent myCoding;

		@Test
		public void testCreateCoding() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("http://loinc.org|4544-3", "Hematocrit [Volume Fraction] of Blood by Automated count");

			makeCoding(entry);

			assertThat(myCoding, is(not(nullValue())));
			assertThat(myCoding.getSystem(), equalTo("http://loinc.org"));
			assertThat(myCoding.getCode(), equalTo("4544-3"));
			assertThat(myCoding.getDisplay(), equalTo("Hematocrit [Volume Fraction] of Blood by Automated count"));
		}

		@Test
		public void testCreateCodingNoSystem() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", "Some text");

			makeCoding(entry);

			assertThat(myCoding, is(not(nullValue())));
			assertThat(myCoding.getSystem(), is(nullValue()));
			assertThat(myCoding.getCode(), equalTo("some_code"));
			assertThat(myCoding.getDisplay(), equalTo("Some text"));
		}

		@Test
		public void testCreateCodingNoDisplay() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", null);

			makeCoding(entry);

			assertThat(myCoding, is(not(nullValue())));
			assertThat(myCoding.getSystem(), is(nullValue()));
			assertThat(myCoding.getCode(), equalTo("some_code"));
			assertThat(myCoding.getDisplay(), is(nullValue()));

		}

		private void makeCoding(TokenAutocompleteHit theEntry) {
			myCoding = myValueSetAutocompleteSearch.makeCoding(theEntry);
		}
	}
}
