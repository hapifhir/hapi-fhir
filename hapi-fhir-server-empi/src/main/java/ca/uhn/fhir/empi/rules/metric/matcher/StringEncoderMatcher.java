package ca.uhn.fhir.empi.rules.metric.matcher;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringEncoderMatcher implements IEmpiStringMatcher {
	private static final Logger ourLog = LoggerFactory.getLogger(StringEncoderMatcher.class);

	private final StringEncoder myStringEncoder;

	public StringEncoderMatcher(StringEncoder theStringEncoder) {
		myStringEncoder = theStringEncoder;
	}

	@Override
	public boolean matches(String theLeftString, String theRightString) {
		try {
			return myStringEncoder.encode(theLeftString).equals(myStringEncoder.encode(theRightString));
		} catch (EncoderException e) {
			ourLog.error("Failed to match strings '{}' and '{}' using encoder {}", theLeftString, theRightString, myStringEncoder.getClass().getName(), e);
		}
		return false;
	}
}
