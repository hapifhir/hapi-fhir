
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum PropertyRepresentationEnum {

	/**
	 * Code Value: <b>xmlAttr</b>
	 *
	 * In XML, this property is represented as an attribute not an element.
	 */
	XMLATTR("xmlAttr", "http://hl7.org/fhir/property-representation"),
	
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
	private static Map<String, Map<String, PropertyRepresentationEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, PropertyRepresentationEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (PropertyRepresentationEnum next : PropertyRepresentationEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, PropertyRepresentationEnum>());
			}
			SYSTEM_TO_CODE_TO_ENUM.get(next.getSystem()).put(next.getCode(), next);			
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
	}
	
	/**
	 * Returns the code system associated with this enumerated value
	 */
	public String getSystem() {
		return mySystem;
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
		public String toSystemString(PropertyRepresentationEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public PropertyRepresentationEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public PropertyRepresentationEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, PropertyRepresentationEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	PropertyRepresentationEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
