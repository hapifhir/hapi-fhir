package ca.uhn.fhir.model.api;

import ca.uhn.fhir.model.primitive.IdDt;


public interface IElement {

	boolean isEmpty();
	
	public void setId(IdDt theId);
	
	public IdDt getId();
	
}
