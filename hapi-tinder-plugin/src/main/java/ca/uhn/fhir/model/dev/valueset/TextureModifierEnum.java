
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum TextureModifierEnum {

	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/texture-code
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/texture-code";

	/**
	 * Name for this Value Set:
	 * TextureModifier
	 */
	public static final String VALUESET_NAME = "TextureModifier";

	private static Map<String, TextureModifierEnum> CODE_TO_ENUM = new HashMap<String, TextureModifierEnum>();
	private static Map<String, Map<String, TextureModifierEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, TextureModifierEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (TextureModifierEnum next : TextureModifierEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, TextureModifierEnum>());
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
	public TextureModifierEnum forCode(String theCode) {
		TextureModifierEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<TextureModifierEnum> VALUESET_BINDER = new IValueSetEnumBinder<TextureModifierEnum>() {
		@Override
		public String toCodeString(TextureModifierEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(TextureModifierEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public TextureModifierEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public TextureModifierEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, TextureModifierEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	TextureModifierEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
