package ca.uhn.fhir.jpa.model.config;

public interface IPhoneticEncoder {
	/**
	 * Encode the provided string using a phonetic encoder like Soundex
	 * @param theString
	 * @return
	 */
	String encode(String theString);
}
