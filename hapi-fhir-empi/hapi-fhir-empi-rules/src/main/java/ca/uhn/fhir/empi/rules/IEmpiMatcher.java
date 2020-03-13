package ca.uhn.fhir.empi.rules;

public interface IEmpiMatcher<T> {
	boolean match(T theLeftString, T theRightString);
}
