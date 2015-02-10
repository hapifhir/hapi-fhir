
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AnswerFormatEnum {

	/**
	 * Code Value: <b>boolean</b>
	 *
	 * Answer is a yes/no answer.
	 */
	BOOLEAN("boolean", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>decimal</b>
	 *
	 * Answer is a floating point number.
	 */
	DECIMAL("decimal", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>integer</b>
	 *
	 * Answer is an integer.
	 */
	INTEGER("integer", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>date</b>
	 *
	 * Answer is a date.
	 */
	DATE("date", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>dateTime</b>
	 *
	 * Answer is a date and time.
	 */
	DATETIME("dateTime", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>instant</b>
	 *
	 * Answer is a system timestamp.
	 */
	INSTANT("instant", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>time</b>
	 *
	 * Answer is a time independent of date.
	 */
	TIME("time", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>string</b>
	 *
	 * Answer is a short (few words to short sentence) free-text entry.
	 */
	STRING("string", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>text</b>
	 *
	 * Answer is a long (potentially multi-paragram) free-text entry.
	 */
	TEXT("text", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>choice</b>
	 *
	 * Answer is a choice from a list of options.
	 */
	CHOICE("choice", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>open-choice</b>
	 *
	 * Answer is a choice from a list of options or a free-text entry.
	 */
	OPEN_CHOICE("open-choice", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>attachment</b>
	 *
	 * Answer is binary content such as a image, PDF, etc.
	 */
	ATTACHMENT("attachment", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>reference</b>
	 *
	 * Answer is a reference to another resource (practitioner, organization, etc.).
	 */
	REFERENCE("reference", "http://hl7.org/fhir/answer-format"),
	
	/**
	 * Code Value: <b>quantity</b>
	 *
	 * Answer is a combination of a numeric value and unit.
	 */
	QUANTITY("quantity", "http://hl7.org/fhir/answer-format"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/answer-format
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/answer-format";

	/**
	 * Name for this Value Set:
	 * AnswerFormat
	 */
	public static final String VALUESET_NAME = "AnswerFormat";

	private static Map<String, AnswerFormatEnum> CODE_TO_ENUM = new HashMap<String, AnswerFormatEnum>();
	private static Map<String, Map<String, AnswerFormatEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AnswerFormatEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AnswerFormatEnum next : AnswerFormatEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AnswerFormatEnum>());
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
	public AnswerFormatEnum forCode(String theCode) {
		AnswerFormatEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AnswerFormatEnum> VALUESET_BINDER = new IValueSetEnumBinder<AnswerFormatEnum>() {
		@Override
		public String toCodeString(AnswerFormatEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AnswerFormatEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AnswerFormatEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AnswerFormatEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AnswerFormatEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AnswerFormatEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
