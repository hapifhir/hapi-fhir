package ca.uhn.fhir.empi;

public interface IEmpiComparator<T> {
	double compare(T theLeftT, T theRightT);
}
