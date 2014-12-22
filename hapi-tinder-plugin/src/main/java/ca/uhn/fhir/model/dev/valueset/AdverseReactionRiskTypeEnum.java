
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdverseReactionRiskTypeEnum {

	/**
	 * Display: <b>Immune Mediated</b><br/>
	 * Code Value: <b>immune</b>
	 *
	 * Immune mediated reaction, including allergic reactions and hypersensitivities.
	 */
	IMMUNE_MEDIATED("immune", "http://hl7.org/fhir/reaction-risk-type"),
	
	/**
	 * Display: <b>Non-immune mediated</b><br/>
	 * Code Value: <b>non-immune</b>
	 *
	 * A non-immune mediated reaction, which can include pseudoallergic reactions, side effects, intolerances, drug toxicities (eg to Gentamicin), drug-drug interactions, food-drug interactions, and drug-disease interactions.
	 */
	NON_IMMUNE_MEDIATED("non-immune", "http://hl7.org/fhir/reaction-risk-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/reaction-risk-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/reaction-risk-type";

	/**
	 * Name for this Value Set:
	 * AdverseReactionRiskType
	 */
	public static final String VALUESET_NAME = "AdverseReactionRiskType";

	private static Map<String, AdverseReactionRiskTypeEnum> CODE_TO_ENUM = new HashMap<String, AdverseReactionRiskTypeEnum>();
	private static Map<String, Map<String, AdverseReactionRiskTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdverseReactionRiskTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdverseReactionRiskTypeEnum next : AdverseReactionRiskTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdverseReactionRiskTypeEnum>());
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
	public AdverseReactionRiskTypeEnum forCode(String theCode) {
		AdverseReactionRiskTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdverseReactionRiskTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdverseReactionRiskTypeEnum>() {
		@Override
		public String toCodeString(AdverseReactionRiskTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdverseReactionRiskTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdverseReactionRiskTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdverseReactionRiskTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdverseReactionRiskTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdverseReactionRiskTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
