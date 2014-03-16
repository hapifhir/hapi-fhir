package ca.uhn.fhir.model.api;

public interface ICompositeElement extends IElement {

	/**
	 * Returns a list containing all child elements 
	 */
	public java.util.List<IElement> getAllPopulatedChildElements();
	
}
