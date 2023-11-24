package ca.uhn.fhir.context.support;

import java.util.Collection;
import java.util.Collections;

public class ValidationSupportParameterObject {
	private final String mySystem;
	private final String myCode;
	private String myDisplayLanguage;
	private Collection<String> myPropertyNames;

	/**
	 * @param theSystem                    The CodeSystem URL
	 * @param theCode                      The code
	 */
	public ValidationSupportParameterObject(String theSystem, String theCode) {
		mySystem = theSystem;
		myCode = theCode;
	}

	/**
	 * @param theSystem                    The CodeSystem URL
	 * @param theCode                      The code
	 * @param theDisplayLanguage           Used to filter out the designation by the display language. To return all designation, set this value to <code>null</code>.
	 * @param thePropertyNames             The collection of properties to be returned in the output. If no properties are specified, the implementor chooses what to return.
	 */
	public ValidationSupportParameterObject(
			String theSystem, String theCode, String theDisplayLanguage, Collection<String> thePropertyNames) {
		this(theSystem, theCode);
		myDisplayLanguage = theDisplayLanguage;
		myPropertyNames = thePropertyNames;
	}

	public String getSystem() {
		return mySystem;
	}

	public String getCode() {
		return myCode;
	}

	public String getDisplayLanguage() {
		return myDisplayLanguage;
	}

	public Collection<String> getPropertyNames() {
		if (myPropertyNames == null) {
			return Collections.emptyList();
		}
		return myPropertyNames;
	}
}
