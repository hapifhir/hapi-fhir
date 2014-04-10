package ca.uhn.fhir.model.api;

import java.util.List;

public interface ISupportsUndeclaredExtensions extends IElement {
	
	/**
	 * Returns a list containing all undeclared non-modifier extensions
	 */
	List<ExtensionDt> getUndeclaredExtensions();

	/**
	 * Returns a list containing all undeclared extensions (modifier and non-modifier) by extension URL
	 */
	List<ExtensionDt> getUndeclaredExtensionsByUrl(String theUrl);

	/**
	 * Returns an <b>immutable</b> list containing all extensions (modifier and non-modifier)
	 */
	List<ExtensionDt> getAllUndeclaredExtensions();

	/**
	 * Returns a list containing all undeclared modifier extensions
	 */
	List<ExtensionDt> getUndeclaredModifierExtensions();
	
	/**
	 * Adds an extension to this object. This extension should have the
	 * following properties set:
	 * <ul>
	 * <li>{@link ExtensionDt#setModifier(boolean) Is Modifier}</li>
	 * <li>{@link ExtensionDt#setUrl(String) URL}</li>
	 * <li>And one of:
	 * <ul>
	 * <li>{@link ExtensionDt#setValue(IElement) A datatype value}</li>
	 * <li>{@link #addUndeclaredExtension(ExtensionDt) Further sub-extensions}</li>
	 * </ul>
	 * </ul> 
	 * 
	 * @param theExtension The extension to add. Can not be null.
	 */
	void addUndeclaredExtension(ExtensionDt theExtension);
	
	/**
	 * Adds an extension to this object
	 */
	void addUndeclaredExtension(boolean theIsModifier, String theUrl, IDatatype theValue);
	
}
