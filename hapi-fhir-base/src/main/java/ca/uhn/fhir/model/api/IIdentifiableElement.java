package ca.uhn.fhir.model.api;

import ca.uhn.fhir.model.primitive.IdDt;

public interface IIdentifiableElement extends IElement {

	public void setId(IdDt theId);

	public IdDt getId();
	
	
}
