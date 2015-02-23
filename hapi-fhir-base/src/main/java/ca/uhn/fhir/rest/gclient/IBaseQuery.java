package ca.uhn.fhir.rest.gclient;

public interface IBaseQuery<T> {

	T where(ICriterion<?> theCriterion);

	T and(ICriterion<?> theCriterion);

}
