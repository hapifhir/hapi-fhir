package ca.uhn.fhir.context.phonetic;

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
		return theString.replaceAll("[^0-9]", "");
	}
}
