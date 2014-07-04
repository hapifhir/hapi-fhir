
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum SecurityEventActionEnum {

	/**
	 * Display: <b>Create</b><br/>
	 * Code Value: <b>C</b>
	 *
	 * Create a new database object, such as Placing an Order.
	 */
	CREATE("C", "http://hl7.org/fhir/security-event-action"),
	
	/**
	 * Display: <b>Read/View/Print</b><br/>
	 * Code Value: <b>R</b>
	 *
	 * Display or print data, such as a Doctor Census.
	 */
	READ_VIEW_PRINT("R", "http://hl7.org/fhir/security-event-action"),
	
	/**
	 * Display: <b>Update</b><br/>
	 * Code Value: <b>U</b>
	 *
	 * Update data, such as Revise Patient Information.
	 */
	UPDATE("U", "http://hl7.org/fhir/security-event-action"),
	
	/**
	 * Display: <b>Delete</b><br/>
	 * Code Value: <b>D</b>
	 *
	 * Delete items, such as a doctor master file record.
	 */
	DELETE("D", "http://hl7.org/fhir/security-event-action"),
	
	/**
	 * Display: <b>Execute</b><br/>
	 * Code Value: <b>E</b>
	 *
	 * Perform a system or application function such as log-on, program execution or use of an object's method, or perform a query/search operation.
	 */
	EXECUTE("E", "http://hl7.org/fhir/security-event-action"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/security-event-action
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/security-event-action";

	/**
	 * Name for this Value Set:
	 * SecurityEventAction
	 */
	public static final String VALUESET_NAME = "SecurityEventAction";

	private static Map<String, SecurityEventActionEnum> CODE_TO_ENUM = new HashMap<String, SecurityEventActionEnum>();
	private static Map<String, Map<String, SecurityEventActionEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SecurityEventActionEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SecurityEventActionEnum next : SecurityEventActionEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SecurityEventActionEnum>());
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
	public SecurityEventActionEnum forCode(String theCode) {
		SecurityEventActionEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SecurityEventActionEnum> VALUESET_BINDER = new IValueSetEnumBinder<SecurityEventActionEnum>() {
		@Override
		public String toCodeString(SecurityEventActionEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SecurityEventActionEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SecurityEventActionEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SecurityEventActionEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SecurityEventActionEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SecurityEventActionEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
