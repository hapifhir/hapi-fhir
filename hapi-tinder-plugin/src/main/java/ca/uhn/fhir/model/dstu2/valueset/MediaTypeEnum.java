
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum MediaTypeEnum {

	/**
	 * Code Value: <b>photo</b>
	 *
	 * The media consists of one or more unmoving images, including photographs, computer-generated graphs and charts, and scanned documents.
	 */
	PHOTO("photo", "http://hl7.org/fhir/media-type"),
	
	/**
	 * Code Value: <b>video</b>
	 *
	 * The media consists of a series of frames that capture a moving image.
	 */
	VIDEO("video", "http://hl7.org/fhir/media-type"),
	
	/**
	 * Code Value: <b>audio</b>
	 *
	 * The media consists of a sound recording.
	 */
	AUDIO("audio", "http://hl7.org/fhir/media-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/media-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/media-type";

	/**
	 * Name for this Value Set:
	 * MediaType
	 */
	public static final String VALUESET_NAME = "MediaType";

	private static Map<String, MediaTypeEnum> CODE_TO_ENUM = new HashMap<String, MediaTypeEnum>();
	private static Map<String, Map<String, MediaTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, MediaTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (MediaTypeEnum next : MediaTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, MediaTypeEnum>());
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
	public MediaTypeEnum forCode(String theCode) {
		MediaTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<MediaTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<MediaTypeEnum>() {
		@Override
		public String toCodeString(MediaTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(MediaTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public MediaTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public MediaTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, MediaTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	MediaTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
