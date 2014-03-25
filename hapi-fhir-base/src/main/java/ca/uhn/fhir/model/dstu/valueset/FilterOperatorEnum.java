
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum FilterOperatorEnum {

	/**
	 * Code Value: <b>=</b>
	 *
	 * The property value has the concept specified by the value.
	 */
	EQUALS("=", "http://hl7.org/fhir/filter-operator"),
	
	/**
	 * Code Value: <b>is-a</b>
	 *
	 * The property value has a concept that has an is-a relationship with the value.
	 */
	IS_A("is-a", "http://hl7.org/fhir/filter-operator"),
	
	/**
	 * Code Value: <b>is-not-a</b>
	 *
	 * The property value has a concept that does not have an is-a relationship with the value.
	 */
	IS_NOT_A("is-not-a", "http://hl7.org/fhir/filter-operator"),
	
	/**
	 * Code Value: <b>regex</b>
	 *
	 * The property value representation matches the regex specified in the value.
	 */
	REGEX("regex", "http://hl7.org/fhir/filter-operator"),
	
	/**
	 * Code Value: <b>in</b>
	 *
	 * The property value is in the set of codes or concepts identified by the value.
	 */
	IN("in", "http://hl7.org/fhir/filter-operator"),
	
	/**
	 * Code Value: <b>not in</b>
	 *
	 * The property value is not in the set of codes or concepts identified by the value.
	 */
	NOT_IN("not in", "http://hl7.org/fhir/filter-operator"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/filter-operator
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/filter-operator";

	/**
	 * Name for this Value Set:
	 * FilterOperator
	 */
	public static final String VALUESET_NAME = "FilterOperator";

	private static Map<String, FilterOperatorEnum> CODE_TO_ENUM = new HashMap<String, FilterOperatorEnum>();
	private static Map<String, Map<String, FilterOperatorEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, FilterOperatorEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (FilterOperatorEnum next : FilterOperatorEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, FilterOperatorEnum>());
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
	public FilterOperatorEnum forCode(String theCode) {
		FilterOperatorEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<FilterOperatorEnum> VALUESET_BINDER = new IValueSetEnumBinder<FilterOperatorEnum>() {
		@Override
		public String toCodeString(FilterOperatorEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(FilterOperatorEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public FilterOperatorEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public FilterOperatorEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, FilterOperatorEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	FilterOperatorEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
