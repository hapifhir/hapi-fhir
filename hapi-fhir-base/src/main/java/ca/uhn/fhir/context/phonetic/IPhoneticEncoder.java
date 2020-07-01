package ca.uhn.fhir.context.phonetic;

public interface IPhoneticEncoder {
	String name();

	/**
	 * Encode the provided string using a phonetic encoder like Soundex
	 * @param theString
	 * @return
	 */
	String encode(String theString);
}
