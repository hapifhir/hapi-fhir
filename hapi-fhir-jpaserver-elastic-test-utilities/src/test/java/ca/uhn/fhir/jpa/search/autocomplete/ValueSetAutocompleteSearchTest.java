package ca.uhn.fhir.jpa.search.autocomplete;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

			assertNotNull(myCoding);
			assertThat(myCoding.getSystem()).isEqualTo("http://loinc.org");
			assertThat(myCoding.getCode()).isEqualTo("4544-3");
			assertThat(myCoding.getDisplay()).isEqualTo("Hematocrit [Volume Fraction] of Blood by Automated count");
		}

		@Test
		public void testCreateCodingNoSystem() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", "Some text");

			makeCoding(entry);

			assertNotNull(myCoding);
			assertNull(myCoding.getSystem());
			assertThat(myCoding.getCode()).isEqualTo("some_code");
			assertThat(myCoding.getDisplay()).isEqualTo("Some text");
		}

		@Test
		public void testCreateCodingNoDisplay() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", null);

			makeCoding(entry);

			assertNotNull(myCoding);
			assertNull(myCoding.getSystem());
			assertThat(myCoding.getCode()).isEqualTo("some_code");
			assertNull(myCoding.getDisplay());

		}

		private void makeCoding(TokenAutocompleteHit theEntry) {
			myCoding = myValueSetAutocompleteSearch.makeCoding(theEntry);
		}
	}
}
