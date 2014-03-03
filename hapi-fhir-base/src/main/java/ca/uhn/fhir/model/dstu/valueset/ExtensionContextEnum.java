
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ExtensionContextEnum {

	/**
	 * resource
	 * 
	 *
	 * The context is all elements matching a particular resource element path.
	 */
	RESOURCE("resource"),
	
	/**
	 * datatype
	 * 
	 *
	 * The context is all nodes matching a particular data type element path (root or repeating element) or all elements referencing a particular primitive data type (expressed as the datatype name).
	 */
	DATATYPE("datatype"),
	
	/**
	 * mapping
	 * 
	 *
	 * The context is all nodes whose mapping to a specified reference model corresponds to a particular mapping structure.  The context identifies the mapping target. The mapping should clearly identify where such an extension could be used.
	 */
	MAPPING("mapping"),
	
	/**
	 * extension
	 * 
	 *
	 * The context is a particular extension from a particular profile.  Expressed as uri#name, where uri identifies the profile and #name identifies the extension code.
	 */
	EXTENSION("extension"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/extension-context
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/extension-context";

	/**
	 * Name for this Value Set:
	 * ExtensionContext
	 */
	public static final String VALUESET_NAME = "ExtensionContext";

	private static Map<String, ExtensionContextEnum> CODE_TO_ENUM = new HashMap<String, ExtensionContextEnum>();
	private String myCode;
	
	static {
		for (ExtensionContextEnum next : ExtensionContextEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
	}
	
	/**
	 * Returns the enumerated value associated with this code
	 */
	public ExtensionContextEnum forCode(String theCode) {
		ExtensionContextEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ExtensionContextEnum> VALUESET_BINDER = new IValueSetEnumBinder<ExtensionContextEnum>() {
		@Override
		public String toCodeString(ExtensionContextEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public ExtensionContextEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	ExtensionContextEnum(String theCode) {
		myCode = theCode;
	}

	
}
