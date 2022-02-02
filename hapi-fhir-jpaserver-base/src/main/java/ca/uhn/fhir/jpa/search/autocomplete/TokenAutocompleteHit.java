package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.TerserUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseCoding;

import javax.annotation.Nonnull;

/**
 * A single autocomplete search hit.
 */
class TokenAutocompleteHit {
	@Nonnull
	final String mySystemCode;
	final String myDisplayText;

	TokenAutocompleteHit(@Nonnull String theSystemCode, String theDisplayText) {
		Validate.notEmpty(theSystemCode);
		mySystemCode = theSystemCode;
		myDisplayText = theDisplayText;
	}

	@Nonnull
	public String getSystemCode() {
		return mySystemCode;
	}
}
