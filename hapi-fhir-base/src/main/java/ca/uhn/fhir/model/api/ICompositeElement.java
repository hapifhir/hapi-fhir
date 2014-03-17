package ca.uhn.fhir.model.api;

import java.util.List;

public interface ICompositeElement extends IElement {

	/**
	 * Returns a list containing all child elements 
	 */
	public java.util.List<IElement> getAllPopulatedChildElements();

	/**
	 * Returns a list containing all child elements matching a given type
	 * 
	 * @param theType The type to match. If set to null, all child elements will be returned
	 */
	<T extends IElement>
	List<T> getAllPopulatedChildElementsOfType(Class<T> theType);
	
}
