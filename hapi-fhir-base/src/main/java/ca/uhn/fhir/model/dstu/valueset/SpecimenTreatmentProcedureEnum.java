
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum SpecimenTreatmentProcedureEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/specimen-treatment-procedure
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/specimen-treatment-procedure";

	/**
	 * Name for this Value Set:
	 * SpecimenTreatmentProcedure
	 */
	public static final String VALUESET_NAME = "SpecimenTreatmentProcedure";

	private static Map<String, SpecimenTreatmentProcedureEnum> CODE_TO_ENUM = new HashMap<String, SpecimenTreatmentProcedureEnum>();
	private static Map<String, Map<String, SpecimenTreatmentProcedureEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SpecimenTreatmentProcedureEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SpecimenTreatmentProcedureEnum next : SpecimenTreatmentProcedureEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SpecimenTreatmentProcedureEnum>());
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
	public SpecimenTreatmentProcedureEnum forCode(String theCode) {
		SpecimenTreatmentProcedureEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SpecimenTreatmentProcedureEnum> VALUESET_BINDER = new IValueSetEnumBinder<SpecimenTreatmentProcedureEnum>() {
		@Override
		public String toCodeString(SpecimenTreatmentProcedureEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SpecimenTreatmentProcedureEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SpecimenTreatmentProcedureEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SpecimenTreatmentProcedureEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SpecimenTreatmentProcedureEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SpecimenTreatmentProcedureEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
