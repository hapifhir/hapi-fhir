
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AggregationModeEnum {

	/**
	 * contained
	 * 
	 *
	 * The reference is a local reference to a contained resource.
	 */
	CONTAINED("contained"),
	
	/**
	 * referenced
	 * 
	 *
	 * The reference to to a resource that has to be resolved externally to the resource that includes the reference.
	 */
	REFERENCED("referenced"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/resource-aggregation-mode
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/resource-aggregation-mode";

	/**
	 * Name for this Value Set:
	 * AggregationMode
	 */
	public static final String VALUESET_NAME = "AggregationMode";

	private static Map<String, AggregationModeEnum> CODE_TO_ENUM = new HashMap<String, AggregationModeEnum>();
	private String myCode;
	
	static {
		for (AggregationModeEnum next : AggregationModeEnum.values()) {
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
	public AggregationModeEnum forCode(String theCode) {
		AggregationModeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AggregationModeEnum> VALUESET_BINDER = new IValueSetEnumBinder<AggregationModeEnum>() {
		@Override
		public String toCodeString(AggregationModeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public AggregationModeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	AggregationModeEnum(String theCode) {
		myCode = theCode;
	}

	
}
