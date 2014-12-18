
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AnimalSpeciesEnum {

	/**
	 * Display: <b>Dog</b><br/>
	 * Code Value: <b>canislf</b>
	 *
	 * Canis lupus familiaris
	 */
	DOG("canislf", "http://hl7.org/fhir/animal-species"),
	
	/**
	 * Display: <b>Sheep</b><br/>
	 * Code Value: <b>ovisa</b>
	 *
	 * Ovis aries
	 */
	SHEEP("ovisa", "http://hl7.org/fhir/animal-species"),
	
	/**
	 * Display: <b>Domestic Canary</b><br/>
	 * Code Value: <b>serinuscd</b>
	 *
	 * Serinus canaria domestica
	 */
	DOMESTIC_CANARY("serinuscd", "http://hl7.org/fhir/animal-species"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/animal-species
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/animal-species";

	/**
	 * Name for this Value Set:
	 * AnimalSpecies
	 */
	public static final String VALUESET_NAME = "AnimalSpecies";

	private static Map<String, AnimalSpeciesEnum> CODE_TO_ENUM = new HashMap<String, AnimalSpeciesEnum>();
	private static Map<String, Map<String, AnimalSpeciesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AnimalSpeciesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AnimalSpeciesEnum next : AnimalSpeciesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AnimalSpeciesEnum>());
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
	public AnimalSpeciesEnum forCode(String theCode) {
		AnimalSpeciesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AnimalSpeciesEnum> VALUESET_BINDER = new IValueSetEnumBinder<AnimalSpeciesEnum>() {
		@Override
		public String toCodeString(AnimalSpeciesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AnimalSpeciesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AnimalSpeciesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AnimalSpeciesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AnimalSpeciesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AnimalSpeciesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
