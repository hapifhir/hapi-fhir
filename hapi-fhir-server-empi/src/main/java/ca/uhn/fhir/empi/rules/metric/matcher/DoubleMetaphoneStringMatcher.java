package ca.uhn.fhir.empi.rules.metric.matcher;

import org.apache.commons.codec.language.DoubleMetaphone;

public class DoubleMetaphoneStringMatcher implements IEmpiStringMatcher {
	@Override
	public boolean matches(String theLeftString, String theRightString) {
		return new DoubleMetaphone().isDoubleMetaphoneEqual(theLeftString, theRightString);
	}
}
