package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValueSetAutocompleteSearchTest {
	FhirContext myFhirContext = FhirContext.forR4();
	private StorageSettings myStorageSettings;
	ValueSetAutocompleteSearch myValueSetAutocompleteSearch = new ValueSetAutocompleteSearch(myFhirContext, myStorageSettings, null);

	@Nested
	public class HitToValueSetConversion {

		private ValueSet.ValueSetExpansionContainsComponent myCoding;

		@Test
		public void testCreateCoding() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("http://loinc.org|4544-3", "Hematocrit [Volume Fraction] of Blood by Automated count");

			makeCoding(entry);

			assertThat(myCoding).isNotNull();
			assertThat(myCoding.getSystem()).isEqualTo("http://loinc.org");
			assertThat(myCoding.getCode()).isEqualTo("4544-3");
			assertThat(myCoding.getDisplay()).isEqualTo("Hematocrit [Volume Fraction] of Blood by Automated count");
		}

		@Test
		public void testCreateCodingNoSystem() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", "Some text");

			makeCoding(entry);

			assertThat(myCoding).isNotNull();
			assertThat(myCoding.getSystem()).isNull();
			assertThat(myCoding.getCode()).isEqualTo("some_code");
			assertThat(myCoding.getDisplay()).isEqualTo("Some text");
		}

		@Test
		public void testCreateCodingNoDisplay() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", null);

			makeCoding(entry);

			assertThat(myCoding).isNotNull();
			assertThat(myCoding.getSystem()).isNull();
			assertThat(myCoding.getCode()).isEqualTo("some_code");
			assertThat(myCoding.getDisplay()).isNull();

		}

		private void makeCoding(TokenAutocompleteHit theEntry) {
			myCoding = myValueSetAutocompleteSearch.makeCoding(theEntry);
		}
	}
}
