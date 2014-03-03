
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SpecimenCollectionMethodEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/specimen-collection-method
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/specimen-collection-method";

	/**
	 * Name for this Value Set:
	 * SpecimenCollectionMethod
	 */
	public static final String VALUESET_NAME = "SpecimenCollectionMethod";

	private static Map<String, SpecimenCollectionMethodEnum> CODE_TO_ENUM = new HashMap<String, SpecimenCollectionMethodEnum>();
	private String myCode;
	
	static {
		for (SpecimenCollectionMethodEnum next : SpecimenCollectionMethodEnum.values()) {
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
	public SpecimenCollectionMethodEnum forCode(String theCode) {
		SpecimenCollectionMethodEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SpecimenCollectionMethodEnum> VALUESET_BINDER = new IValueSetEnumBinder<SpecimenCollectionMethodEnum>() {
		@Override
		public String toCodeString(SpecimenCollectionMethodEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public SpecimenCollectionMethodEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	SpecimenCollectionMethodEnum(String theCode) {
		myCode = theCode;
	}

	
}
