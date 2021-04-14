package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.phonetic.NumericEncoder;

// Useful for numerical identifiers like phone numbers, address parts etc.
// This should not be used where decimals are important.  A new "quantity matcher" should be added to handle cases like that.
public class NumericMatcher implements IMdmStringMatcher {
	private final NumericEncoder encoder = new NumericEncoder();

	@Override
	public boolean matches(String theLeftString, String theRightString) {
		String left = encoder.encode(theLeftString);
		String right = encoder.encode(theRightString);
		return left.equals(right);
	}
}
