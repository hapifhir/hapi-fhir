
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum IdentifierUseEnum {

	/**
	 * Code Value: <b>usual</b>
	 *
	 * the identifier recommended for display and use in real-world interactions.
	 */
	USUAL("usual"),
	
	/**
	 * Code Value: <b>official</b>
	 *
	 * the identifier considered to be most trusted for the identification of this item.
	 */
	OFFICIAL("official"),
	
	/**
	 * Code Value: <b>temp</b>
	 *
	 * A temporary identifier.
	 */
	TEMP("temp"),
	
	/**
	 * Code Value: <b>secondary</b>
	 *
	 * An identifier that was assigned in secondary use - it serves to identify the object in a relative context, but cannot be consistently assigned to the same object again in a different context.
	 */
	SECONDARY("secondary"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/identifier-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/identifier-use";

	/**
	 * Name for this Value Set:
	 * IdentifierUse
	 */
	public static final String VALUESET_NAME = "IdentifierUse";

	private static Map<String, IdentifierUseEnum> CODE_TO_ENUM = new HashMap<String, IdentifierUseEnum>();
	private String myCode;
	
	static {
		for (IdentifierUseEnum next : IdentifierUseEnum.values()) {
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
	public IdentifierUseEnum forCode(String theCode) {
		IdentifierUseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<IdentifierUseEnum> VALUESET_BINDER = new IValueSetEnumBinder<IdentifierUseEnum>() {
		@Override
		public String toCodeString(IdentifierUseEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public IdentifierUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	IdentifierUseEnum(String theCode) {
		myCode = theCode;
	}

	
}
