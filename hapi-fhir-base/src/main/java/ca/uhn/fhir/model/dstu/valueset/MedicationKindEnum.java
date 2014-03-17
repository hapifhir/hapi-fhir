
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
	PRODUCT("product", "http://hl7.org/fhir/medication-kind"),
	
	/**
	 * Code Value: <b>package</b>
	 *
	 * The medication is a package - a contained group of one of more products.
	 */
	PACKAGE("package", "http://hl7.org/fhir/medication-kind"),
	
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
	private static Map<String, Map<String, MedicationKindEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, MedicationKindEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (MedicationKindEnum next : MedicationKindEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, MedicationKindEnum>());
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
		public String toSystemString(MedicationKindEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public MedicationKindEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public MedicationKindEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, MedicationKindEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	MedicationKindEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
