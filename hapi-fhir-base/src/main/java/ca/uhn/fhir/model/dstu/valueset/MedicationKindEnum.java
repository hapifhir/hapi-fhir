
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum MedicationKindEnum {

	/**
	 * Code Value: <b>product</b>
	 *
	 * The medication is a product.
	 */
	PRODUCT("product"),
	
	/**
	 * Code Value: <b>package</b>
	 *
	 * The medication is a package - a contained group of one of more products.
	 */
	PACKAGE("package"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/medication-kind
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/medication-kind";

	/**
	 * Name for this Value Set:
	 * MedicationKind
	 */
	public static final String VALUESET_NAME = "MedicationKind";

	private static Map<String, MedicationKindEnum> CODE_TO_ENUM = new HashMap<String, MedicationKindEnum>();
	private String myCode;
	
	static {
		for (MedicationKindEnum next : MedicationKindEnum.values()) {
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
	public MedicationKindEnum forCode(String theCode) {
		MedicationKindEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<MedicationKindEnum> VALUESET_BINDER = new IValueSetEnumBinder<MedicationKindEnum>() {
		@Override
		public String toCodeString(MedicationKindEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public MedicationKindEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	MedicationKindEnum(String theCode) {
		myCode = theCode;
	}

	
}
