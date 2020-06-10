package ca.uhn.fhir.empi.rules.metric.matcher;

import org.apache.commons.codec.language.Soundex;

public class SoundexStringMatcher implements IEmpiStringMatcher {
	private final Soundex mySoundex = new Soundex();

	@Override
	public boolean matches(String theLeftString, String theRightString) {
		return mySoundex.encode(theLeftString).equals(mySoundex.encode(theRightString));
	}
}
