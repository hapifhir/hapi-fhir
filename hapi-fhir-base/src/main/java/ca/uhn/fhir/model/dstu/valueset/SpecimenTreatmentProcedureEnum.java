
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

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
	private String myCode;
	
	static {
		for (SpecimenTreatmentProcedureEnum next : SpecimenTreatmentProcedureEnum.values()) {
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
		public SpecimenTreatmentProcedureEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	SpecimenTreatmentProcedureEnum(String theCode) {
		myCode = theCode;
	}

	
}
