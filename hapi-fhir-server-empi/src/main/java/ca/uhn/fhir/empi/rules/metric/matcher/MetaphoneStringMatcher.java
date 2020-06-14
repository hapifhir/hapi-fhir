package ca.uhn.fhir.empi.rules.metric.matcher;

import org.apache.commons.codec.language.Metaphone;

public class MetaphoneStringMatcher implements IEmpiStringMatcher {
	@Override
	public boolean matches(String theLeftString, String theRightString) {
		return new Metaphone().isMetaphoneEqual(theLeftString, theRightString);
	}
}
