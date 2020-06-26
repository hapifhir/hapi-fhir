package ca.uhn.fhir.context.phonetic;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApacheEncoder implements IPhoneticEncoder {
	private static final Logger ourLog = LoggerFactory.getLogger(ApacheEncoder.class);

	private final StringEncoder myStringEncoder;

	public ApacheEncoder(StringEncoder theStringEncoder) {
		myStringEncoder = theStringEncoder;
	}

	@Override
	public String encode(String theString) {
		try {
			return myStringEncoder.encode(theString);
		} catch (EncoderException e) {
			ourLog.error("Failed to encode string " + theString, e);
			return theString;
		}
	}
}
