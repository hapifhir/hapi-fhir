package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class TokenAutocompleteHitTest {

	FhirContext myFhirContext = FhirContext.forR4();

	@Test
	public void testCreateCoding() {
		TokenAutocompleteHit entry = new TokenAutocompleteHit("http://loinc.org|4544-3", "Hematocrit [Volume Fraction] of Blood by Automated count");

		IBaseCoding coding = entry.makeCoding(myFhirContext);

		assertThat(coding, is(not(nullValue())));
		assertThat(coding.getSystem(), equalTo("http://loinc.org"));
		assertThat(coding.getCode(), equalTo("4544-3"));
		assertThat(coding.getDisplay(), equalTo("Hematocrit [Volume Fraction] of Blood by Automated count"));
	}

	@Test
	public void testCreateCodingNoSystem() {
		TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", "Some text");

		IBaseCoding coding = entry.makeCoding(myFhirContext);

		assertThat(coding, is(not(nullValue())));
		assertThat(coding.getSystem(), is(nullValue()));
		assertThat(coding.getCode(), equalTo("some_code"));
		assertThat(coding.getDisplay(), equalTo("Some text"));
	}

	@Test
	public void testCreateCodingNoDisplay() {
		TokenAutocompleteHit entry = new TokenAutocompleteHit("|some_code", null);

		IBaseCoding coding = entry.makeCoding(myFhirContext);

		assertThat(coding, is(not(nullValue())));
		assertThat(coding.getSystem(), is(nullValue()));
		assertThat(coding.getCode(), equalTo("some_code"));
		assertThat(coding.getDisplay(), is(nullValue()));

	}
}
