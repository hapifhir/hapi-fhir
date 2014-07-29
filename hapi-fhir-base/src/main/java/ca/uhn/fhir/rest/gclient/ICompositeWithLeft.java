package ca.uhn.fhir.rest.gclient;

public interface ICompositeWithLeft<B extends IParam> {

	ICriterion<B> withRight(ICriterion<B> theRight);
	
}
