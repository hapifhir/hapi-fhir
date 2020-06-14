package ca.uhn.fhir.empi.rules.metric.matcher;

public interface IEmpiStringMatcher {
	boolean matches(String theLeftString, String theRightString);
}
