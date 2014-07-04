
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum SecurityEventObjectLifecycleEnum {

	/**
	 * Code Value: <b>1</b>
	 *
	 * Origination / Creation.
	 */
	_1("1", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>2</b>
	 *
	 * Import / Copy from original.
	 */
	_2("2", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>3</b>
	 *
	 * Amendment.
	 */
	_3("3", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>4</b>
	 *
	 * Verification.
	 */
	_4("4", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>5</b>
	 *
	 * Translation.
	 */
	_5("5", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>6</b>
	 *
	 * Access / Use.
	 */
	_6("6", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>7</b>
	 *
	 * De-identification.
	 */
	_7("7", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>8</b>
	 *
	 * Aggregation, summarization, derivation.
	 */
	_8("8", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>9</b>
	 *
	 * Report.
	 */
	_9("9", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>10</b>
	 *
	 * Export / Copy to target.
	 */
	_10("10", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>11</b>
	 *
	 * Disclosure.
	 */
	_11("11", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>12</b>
	 *
	 * Receipt of disclosure.
	 */
	_12("12", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>13</b>
	 *
	 * Archiving.
	 */
	_13("13", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>14</b>
	 *
	 * Logical deletion.
	 */
	_14("14", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>15</b>
	 *
	 * Permanent erasure / Physical destruction.
	 */
	_15("15", "http://hl7.org/fhir/object-lifecycle"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/object-lifecycle
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/object-lifecycle";

	/**
	 * Name for this Value Set:
	 * SecurityEventObjectLifecycle
	 */
	public static final String VALUESET_NAME = "SecurityEventObjectLifecycle";

	private static Map<String, SecurityEventObjectLifecycleEnum> CODE_TO_ENUM = new HashMap<String, SecurityEventObjectLifecycleEnum>();
	private static Map<String, Map<String, SecurityEventObjectLifecycleEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SecurityEventObjectLifecycleEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SecurityEventObjectLifecycleEnum next : SecurityEventObjectLifecycleEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SecurityEventObjectLifecycleEnum>());
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
	public SecurityEventObjectLifecycleEnum forCode(String theCode) {
		SecurityEventObjectLifecycleEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SecurityEventObjectLifecycleEnum> VALUESET_BINDER = new IValueSetEnumBinder<SecurityEventObjectLifecycleEnum>() {
		@Override
		public String toCodeString(SecurityEventObjectLifecycleEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SecurityEventObjectLifecycleEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SecurityEventObjectLifecycleEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SecurityEventObjectLifecycleEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SecurityEventObjectLifecycleEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SecurityEventObjectLifecycleEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
