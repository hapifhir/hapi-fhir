
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum NameUseEnum {

	/**
	 * usual
	 * 
	 *
	 * Known as/conventional/the one you normally use.
	 */
	USUAL("usual"),
	
	/**
	 * official
	 * 
	 *
	 * The formal name as registered in an official (government) registry, but which name might not be commonly used. May be called "legal name".
	 */
	OFFICIAL("official"),
	
	/**
	 * temp
	 * 
	 *
	 * A temporary name. Name.period can provide more detailed information. This may also be used for temporary names assigned at birth or in emergency situations.
	 */
	TEMP("temp"),
	
	/**
	 * nickname
	 * 
	 *
	 * A name that is used to address the person in an informal manner, but is not part of their formal or usual name.
	 */
	NICKNAME("nickname"),
	
	/**
	 * anonymous
	 * 
	 *
	 * Anonymous assigned name, alias, or pseudonym (used to protect a person's identity for privacy reasons).
	 */
	ANONYMOUS("anonymous"),
	
	/**
	 * old
	 * 
	 *
	 * This name is no longer in use (or was never correct, but retained for records).
	 */
	OLD("old"),
	
	/**
	 * maiden
	 * 
	 *
	 * A name used prior to marriage. Marriage naming customs vary greatly around the world. This name use is for use by applications that collect and store "maiden" names. Though the concept of maiden name is often gender specific, the use of this term is not gender specific. The use of this term does not imply any particular history for a person's name, nor should the maiden name be determined algorithmically.
	 */
	MAIDEN("maiden"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/name-use
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/name-use";

	/**
	 * Name for this Value Set:
	 * NameUse
	 */
	public static final String VALUESET_NAME = "NameUse";

	private static Map<String, NameUseEnum> CODE_TO_ENUM = new HashMap<String, NameUseEnum>();
	private String myCode;
	
	static {
		for (NameUseEnum next : NameUseEnum.values()) {
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
	public NameUseEnum forCode(String theCode) {
		NameUseEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<NameUseEnum> VALUESET_BINDER = new IValueSetEnumBinder<NameUseEnum>() {
		@Override
		public String toCodeString(NameUseEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public NameUseEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	NameUseEnum(String theCode) {
		myCode = theCode;
	}

	
}
