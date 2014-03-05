
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum SearchParamTypeEnum {

	/**
	 * Code Value: <b>number</b>
	 *
	 * Search parameter SHALL be a number (a whole number, or a decimal).
	 */
	NUMBER("number"),
	
	/**
	 * Code Value: <b>date</b>
	 *
	 * Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported.
	 */
	DATE("date"),
	
	/**
	 * Code Value: <b>string</b>
	 *
	 * Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces.
	 */
	STRING("string"),
	
	/**
	 * Code Value: <b>token</b>
	 *
	 * Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a "|", depending on the modifier used.
	 */
	TOKEN("token"),
	
	/**
	 * Code Value: <b>reference</b>
	 *
	 * A reference to another resource.
	 */
	REFERENCE("reference"),
	
	/**
	 * Code Value: <b>composite</b>
	 *
	 * A composite search parameter that combines a search on two values together.
	 */
	COMPOSITE("composite"),
	
	/**
	 * Code Value: <b>quantity</b>
	 *
	 * A search parameter that searches on a quantity.
	 */
	QUANTITY("quantity"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/search-param-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/search-param-type";

	/**
	 * Name for this Value Set:
	 * SearchParamType
	 */
	public static final String VALUESET_NAME = "SearchParamType";

	private static Map<String, SearchParamTypeEnum> CODE_TO_ENUM = new HashMap<String, SearchParamTypeEnum>();
	private String myCode;
	
	static {
		for (SearchParamTypeEnum next : SearchParamTypeEnum.values()) {
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
	public SearchParamTypeEnum forCode(String theCode) {
		SearchParamTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SearchParamTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<SearchParamTypeEnum>() {
		@Override
		public String toCodeString(SearchParamTypeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public SearchParamTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	SearchParamTypeEnum(String theCode) {
		myCode = theCode;
	}

	
}
