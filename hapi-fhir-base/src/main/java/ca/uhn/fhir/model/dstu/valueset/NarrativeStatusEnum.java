
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NarrativeStatusEnum {

	/**
	 * generated
	 * 
	 *
	 * The contents of the narrative are entirely generated from the structured data in the resource.
	 */
	GENERATED("generated"),
	
	/**
	 * extensions
	 * 
	 *
	 * The contents of the narrative are entirely generated from the structured data in the resource and some of the content is generated from extensions.
	 */
	EXTENSIONS("extensions"),
	
	/**
	 * additional
	 * 
	 *
	 * The contents of the narrative contain additional information not found in the structured data.
	 */
	ADDITIONAL("additional"),
	
	/**
	 * empty
	 * 
	 *
	 * the contents of the narrative are some equivalent of "No human-readable text provided for this resource".
	 */
	EMPTY("empty"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/narrative-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/narrative-status";

	/**
	 * Name for this Value Set:
	 * NarrativeStatus
	 */
	public static final String VALUESET_NAME = "NarrativeStatus";

	private static Map<String, NarrativeStatusEnum> CODE_TO_ENUM = new HashMap<String, NarrativeStatusEnum>();
	private String myCode;
	
	static {
		for (NarrativeStatusEnum next : NarrativeStatusEnum.values()) {
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
	public NarrativeStatusEnum forCode(String theCode) {
		NarrativeStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NarrativeStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<NarrativeStatusEnum>() {
		@Override
		public String toCodeString(NarrativeStatusEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public NarrativeStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	NarrativeStatusEnum(String theCode) {
		myCode = theCode;
	}

	
}
