
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum FilterOperatorEnum {

	/**
	 * =
	 * 
	 *
	 * The property value has the concept specified by the value.
	 */
	EQUALS("="),
	
	/**
	 * is-a
	 * 
	 *
	 * The property value has a concept that has an is-a relationship with the value.
	 */
	IS_A("is-a"),
	
	/**
	 * is-not-a
	 * 
	 *
	 * The property value has a concept that does not have an is-a relationship with the value.
	 */
	IS_NOT_A("is-not-a"),
	
	/**
	 * regex
	 * 
	 *
	 * The property value representation matches the regex specified in the value.
	 */
	REGEX("regex"),
	
	/**
	 * in
	 * 
	 *
	 * The property value is in the set of codes or concepts identified by the value.
	 */
	IN("in"),
	
	/**
	 * not in
	 * 
	 *
	 * The property value is not in the set of codes or concepts identified by the value.
	 */
	NOT_IN("not in"),
	
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
	private String myCode;
	
	static {
		for (FilterOperatorEnum next : FilterOperatorEnum.values()) {
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
		public FilterOperatorEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	FilterOperatorEnum(String theCode) {
		myCode = theCode;
	}

	
}
