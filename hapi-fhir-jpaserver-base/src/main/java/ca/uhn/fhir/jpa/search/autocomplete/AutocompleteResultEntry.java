package ca.uhn.fhir.jpa.search.autocomplete;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

public class AutocompleteResultEntry {
	@Nonnull
	final String mySystemCode;
	final String myDisplayText;

	public AutocompleteResultEntry(@Nonnull String theSystemCode, String theDisplayText) {
		Validate.notEmpty(theSystemCode);
		mySystemCode = theSystemCode;
		myDisplayText = theDisplayText;
	}
}
