
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum StructureDefinitionKindEnum {

	/**
	 * Display: <b>Data Type</b><br>
	 * Code Value: <b>datatype</b>
	 *
	 * A data type - either a primitive or complex structure that defines a set of data elements. These can be used throughout Resource and extension definitions
	 */
	DATA_TYPE("datatype", "http://hl7.org/fhir/structure-definition-kind"),
	
	/**
	 * Display: <b>Resource</b><br>
	 * Code Value: <b>resource</b>
	 *
	 * A resource defined by the FHIR specification
	 */
	RESOURCE("resource", "http://hl7.org/fhir/structure-definition-kind"),
	
	/**
	 * Display: <b>Logical Model</b><br>
	 * Code Value: <b>logical</b>
	 *
	 * A logical model - a conceptual package of data that will be mapped to resources for implementation
	 */
	LOGICAL_MODEL("logical", "http://hl7.org/fhir/structure-definition-kind"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * 
	 */
	public static final String VALUESET_IDENTIFIER = "";

	/**
	 * Name for this Value Set:
	 * StructureDefinitionKind
	 */
	public static final String VALUESET_NAME = "StructureDefinitionKind";

	private static Map<String, StructureDefinitionKindEnum> CODE_TO_ENUM = new HashMap<String, StructureDefinitionKindEnum>();
	private static Map<String, Map<String, StructureDefinitionKindEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, StructureDefinitionKindEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (StructureDefinitionKindEnum next : StructureDefinitionKindEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, StructureDefinitionKindEnum>());
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
	public StructureDefinitionKindEnum forCode(String theCode) {
		StructureDefinitionKindEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<StructureDefinitionKindEnum> VALUESET_BINDER = new IValueSetEnumBinder<StructureDefinitionKindEnum>() {
		@Override
		public String toCodeString(StructureDefinitionKindEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(StructureDefinitionKindEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public StructureDefinitionKindEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public StructureDefinitionKindEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, StructureDefinitionKindEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	StructureDefinitionKindEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
