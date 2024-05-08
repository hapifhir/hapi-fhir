package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
			assertEquals("http://loinc.org", myCoding.getSystem());
			assertEquals("4544-3", myCoding.getCode());
			assertEquals("Hematocrit [Volume Fraction] of Blood by Automated count", myCoding.getDisplay());
		}

		@Test
		public void testCreateCodingNoSystem() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", "Some text");

			makeCoding(entry);

			assertNotNull(myCoding);
			assertNull(myCoding.getSystem());
			assertEquals("some_code", myCoding.getCode());
			assertEquals("Some text", myCoding.getDisplay());
		}

		@Test
		public void testCreateCodingNoDisplay() {
			TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", null);

			makeCoding(entry);

			assertNotNull(myCoding);
			assertNull(myCoding.getSystem());
			assertEquals("some_code", myCoding.getCode());
			assertNull(myCoding.getDisplay());

		}

		private void makeCoding(TokenAutocompleteHit theEntry) {
			myCoding = myValueSetAutocompleteSearch.makeCoding(theEntry);
		}
	}
}
