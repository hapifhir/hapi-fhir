package ca.uhn.fhir.model.api;

import java.util.List;

public interface ISupportsUndeclaredExtensions extends IElement {
	
	/**
	 * Returns a list containing all undeclared non-modifier extensions
	 */
	List<ExtensionDt> getUndeclaredExtensions();

	/**
	 * Returns an <b>immutable</b> list containing all extensions (modifier and non-modifier)
	 */
	List<ExtensionDt> getAllUndeclaredExtensions();

	/**
	 * Returns a list containing all undeclared modifier extensions
	 */
	List<ExtensionDt> getUndeclaredModifierExtensions();
	
}
