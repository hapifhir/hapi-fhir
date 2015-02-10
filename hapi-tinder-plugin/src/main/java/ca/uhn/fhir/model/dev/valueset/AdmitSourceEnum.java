
package ca.uhn.fhir.model.dev.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum AdmitSourceEnum {

	/**
	 * Display: <b>Transferred from other hospital</b><br/>
	 * Code Value: <b>hosp-trans</b>
	 */
	TRANSFERRED_FROM_OTHER_HOSPITAL("hosp-trans", "http://hl7.org/fhir/admit-source"),
	
	/**
	 * Display: <b>From accident/emergency department</b><br/>
	 * Code Value: <b>emd</b>
	 */
	FROM_ACCIDENT_EMERGENCY_DEPARTMENT("emd", "http://hl7.org/fhir/admit-source"),
	
	/**
	 * Display: <b>From outpatient department</b><br/>
	 * Code Value: <b>outp</b>
	 */
	FROM_OUTPATIENT_DEPARTMENT("outp", "http://hl7.org/fhir/admit-source"),
	
	/**
	 * Display: <b>Born in hospital</b><br/>
	 * Code Value: <b>born</b>
	 */
	BORN_IN_HOSPITAL("born", "http://hl7.org/fhir/admit-source"),
	
	/**
	 * Display: <b>General Practitioner referral</b><br/>
	 * Code Value: <b>gp</b>
	 */
	GENERAL_PRACTITIONER_REFERRAL("gp", "http://hl7.org/fhir/admit-source"),
	
	/**
	 * Display: <b>Medical Practitioner/physician referral</b><br/>
	 * Code Value: <b>mp</b>
	 */
	MEDICAL_PRACTITIONER_PHYSICIAN_REFERRAL("mp", "http://hl7.org/fhir/admit-source"),
	
	/**
	 * Display: <b>From nursing home</b><br/>
	 * Code Value: <b>nursing</b>
	 */
	FROM_NURSING_HOME("nursing", "http://hl7.org/fhir/admit-source"),
	
	/**
	 * Display: <b>From psychiatric hospital</b><br/>
	 * Code Value: <b>psych</b>
	 */
	FROM_PSYCHIATRIC_HOSPITAL("psych", "http://hl7.org/fhir/admit-source"),
	
	/**
	 * Display: <b>From rehabilitation facility</b><br/>
	 * Code Value: <b>rehab</b>
	 */
	FROM_REHABILITATION_FACILITY("rehab", "http://hl7.org/fhir/admit-source"),
	
	/**
	 * Display: <b>Other</b><br/>
	 * Code Value: <b>other</b>
	 */
	OTHER("other", "http://hl7.org/fhir/admit-source"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/encounter-admit-source
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/encounter-admit-source";

	/**
	 * Name for this Value Set:
	 * AdmitSource
	 */
	public static final String VALUESET_NAME = "AdmitSource";

	private static Map<String, AdmitSourceEnum> CODE_TO_ENUM = new HashMap<String, AdmitSourceEnum>();
	private static Map<String, Map<String, AdmitSourceEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, AdmitSourceEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (AdmitSourceEnum next : AdmitSourceEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, AdmitSourceEnum>());
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
	public AdmitSourceEnum forCode(String theCode) {
		AdmitSourceEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<AdmitSourceEnum> VALUESET_BINDER = new IValueSetEnumBinder<AdmitSourceEnum>() {
		@Override
		public String toCodeString(AdmitSourceEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(AdmitSourceEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public AdmitSourceEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public AdmitSourceEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, AdmitSourceEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	AdmitSourceEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
