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
	// wipmb add count?

	TokenAutocompleteHit(@Nonnull String theSystemCode, String theDisplayText) {
		Validate.notEmpty(theSystemCode);
		mySystemCode = theSystemCode;
		myDisplayText = theDisplayText;
	}

	IBaseCoding makeCoding(FhirContext theFhirContext) {
		TokenParam tokenParam = new TokenParam();
		tokenParam.setValueAsQueryToken(theFhirContext, null, null, mySystemCode);

		IBaseCoding coding = TerserUtil.newElement(theFhirContext, "Coding");
		coding.setCode(tokenParam.getValue());
		coding.setSystem(tokenParam.getSystem());
		coding.setDisplay(myDisplayText);

		return coding;
	}
}
