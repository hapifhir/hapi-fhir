
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum PropertyRepresentationEnum {

	/**
	 * Code Value: <b>xmlAttr</b>
	 *
	 * In XML, this property is represented as an attribute not an element.
	 */
	XMLATTR("xmlAttr"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/property-representation
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/property-representation";

	/**
	 * Name for this Value Set:
	 * PropertyRepresentation
	 */
	public static final String VALUESET_NAME = "PropertyRepresentation";

	private static Map<String, PropertyRepresentationEnum> CODE_TO_ENUM = new HashMap<String, PropertyRepresentationEnum>();
	private String myCode;
	
	static {
		for (PropertyRepresentationEnum next : PropertyRepresentationEnum.values()) {
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
	public PropertyRepresentationEnum forCode(String theCode) {
		PropertyRepresentationEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<PropertyRepresentationEnum> VALUESET_BINDER = new IValueSetEnumBinder<PropertyRepresentationEnum>() {
		@Override
		public String toCodeString(PropertyRepresentationEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public PropertyRepresentationEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	PropertyRepresentationEnum(String theCode) {
		myCode = theCode;
	}

	
}
