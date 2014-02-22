package ca.uhn.fhir.model.api;

import java.util.List;

public interface ISupportsUndeclaredExtensions {
	
	/**
	 * Returns a list containing all undeclared extensions
	 */
	List<UndeclaredExtension> getUndeclaredExtensions();
	
}
