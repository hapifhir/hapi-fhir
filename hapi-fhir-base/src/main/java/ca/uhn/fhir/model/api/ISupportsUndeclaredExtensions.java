package ca.uhn.fhir.model.api;

import java.util.List;

public interface ISupportsUndeclaredExtensions {
	
	/**
	 * Returns a list containing all undeclared non-modifier extensions
	 */
	List<UndeclaredExtension> getUndeclaredExtensions();

	/**
	 * Returns an <b>immutable</b> list containing all extensions (modifier and non-modifier)
	 */
	List<UndeclaredExtension> getAllUndeclaredExtensions();

	/**
	 * Returns a list containing all undeclared modifier extensions
	 */
	List<UndeclaredExtension> getUndeclaredModifierExtensions();
	
}
