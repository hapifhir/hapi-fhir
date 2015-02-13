
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum EpisodeOfCareStatusEnum {

	/**
	 * Display: <b>Planned</b><br>
	 * Code Value: <b>planned</b>
	 *
	 * This episode of care is planned to start at the date specified in the period.start. During this status an organization may perform assessments to determine if they are eligible to receive services, or be organizing to make resources available to provide care services.
	 */
	PLANNED("planned", "http://hl7.org/fhir/episode-of-care-status"),
	
	/**
	 * Display: <b>Active</b><br>
	 * Code Value: <b>active</b>
	 *
	 * This episode of care is current.
	 */
	ACTIVE("active", "http://hl7.org/fhir/episode-of-care-status"),
	
	/**
	 * Display: <b>On Hold</b><br>
	 * Code Value: <b>onhold</b>
	 *
	 * This episode of care is on hold, the organization has limitted responsibility for the patient (such as while on respite).
	 */
	ON_HOLD("onhold", "http://hl7.org/fhir/episode-of-care-status"),
	
	/**
	 * Display: <b>Finished</b><br>
	 * Code Value: <b>finished</b>
	 *
	 * This episode of care is finished at the organization is not expecting to be providing care to the patient.
	 */
	FINISHED("finished", "http://hl7.org/fhir/episode-of-care-status"),
	
	/**
	 * Display: <b>Withdrawn</b><br>
	 * Code Value: <b>withdrawn</b>
	 *
	 * The episode of care was withdrawn from service, often selected during the planned stage as the patient may have gone elsewhere, or the circumstances have changed and the organization is unable to provide the care.
	 */
	WITHDRAWN("withdrawn", "http://hl7.org/fhir/episode-of-care-status"),
	
	/**
	 * Display: <b>Other</b><br>
	 * Code Value: <b>other</b>
	 *
	 * The status is outside one of these values, an extension should be used to define what the status reason is.
	 */
	OTHER("other", "http://hl7.org/fhir/episode-of-care-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/episode-of-care-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/episode-of-care-status";

	/**
	 * Name for this Value Set:
	 * EpisodeOfCareStatus
	 */
	public static final String VALUESET_NAME = "EpisodeOfCareStatus";

	private static Map<String, EpisodeOfCareStatusEnum> CODE_TO_ENUM = new HashMap<String, EpisodeOfCareStatusEnum>();
	private static Map<String, Map<String, EpisodeOfCareStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, EpisodeOfCareStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (EpisodeOfCareStatusEnum next : EpisodeOfCareStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, EpisodeOfCareStatusEnum>());
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
	public EpisodeOfCareStatusEnum forCode(String theCode) {
		EpisodeOfCareStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<EpisodeOfCareStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<EpisodeOfCareStatusEnum>() {
		@Override
		public String toCodeString(EpisodeOfCareStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(EpisodeOfCareStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public EpisodeOfCareStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public EpisodeOfCareStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, EpisodeOfCareStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	EpisodeOfCareStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
