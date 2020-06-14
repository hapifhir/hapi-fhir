package ca.uhn.fhir.empi.rules.metric.matcher;

public class SubstringStringMatcher implements IEmpiStringMatcher {
	@Override
	public boolean matches(String theLeftString, String theRightString) {
		return theLeftString.startsWith(theRightString) || theRightString.startsWith(theLeftString);
	}
}
