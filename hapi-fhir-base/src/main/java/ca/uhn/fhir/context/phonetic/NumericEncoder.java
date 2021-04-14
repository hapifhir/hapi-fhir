package ca.uhn.fhir.context.phonetic;

import com.google.common.base.CharMatcher;

// Useful for numerical identifiers like phone numbers, address parts etc.
// This should not be used where decimals are important.  A new "quantity encoder" should be added to handle cases like that.
public class NumericEncoder implements IPhoneticEncoder {
	@Override
	public String name() {
		return "NUMERIC";
	}

	@Override
	public String encode(String theString) {
		// Remove everything but the numbers
		return CharMatcher.inRange('0', '9').retainFrom(theString);
	}
}
