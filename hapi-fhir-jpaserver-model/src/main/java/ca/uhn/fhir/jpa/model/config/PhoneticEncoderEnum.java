package ca.uhn.fhir.jpa.model.config;

import org.apache.commons.codec.language.Soundex;

public enum PhoneticEncoderEnum {
	SOUNDEX("SOUNDEX", new ApacheEncoder(new Soundex()));

	private final String myCode;
	private final IPhoneticEncoder myPhoneticEncoder;

	PhoneticEncoderEnum(String theCode, IPhoneticEncoder thePhoneticEncoder) {
		myCode = theCode;
		myPhoneticEncoder = thePhoneticEncoder;
	}

	public IPhoneticEncoder getPhoneticEncoder() {
		return myPhoneticEncoder;
	}
}
