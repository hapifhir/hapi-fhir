
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum DeviceMetricCalibrationTypeEnum {

	/**
	 * Code Value: <b>unspecified</b>
	 *
	 * TODO.
	 */
	UNSPECIFIED("unspecified", "http://hl7.org/fhir/metric-calibration-type"),
	
	/**
	 * Code Value: <b>offset</b>
	 *
	 * TODO.
	 */
	OFFSET("offset", "http://hl7.org/fhir/metric-calibration-type"),
	
	/**
	 * Code Value: <b>gain</b>
	 *
	 * TODO.
	 */
	GAIN("gain", "http://hl7.org/fhir/metric-calibration-type"),
	
	/**
	 * Code Value: <b>two-point</b>
	 *
	 * TODO.
	 */
	TWO_POINT("two-point", "http://hl7.org/fhir/metric-calibration-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/metric-calibration-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/metric-calibration-type";

	/**
	 * Name for this Value Set:
	 * DeviceMetricCalibrationType
	 */
	public static final String VALUESET_NAME = "DeviceMetricCalibrationType";

	private static Map<String, DeviceMetricCalibrationTypeEnum> CODE_TO_ENUM = new HashMap<String, DeviceMetricCalibrationTypeEnum>();
	private static Map<String, Map<String, DeviceMetricCalibrationTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DeviceMetricCalibrationTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DeviceMetricCalibrationTypeEnum next : DeviceMetricCalibrationTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DeviceMetricCalibrationTypeEnum>());
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
	public DeviceMetricCalibrationTypeEnum forCode(String theCode) {
		DeviceMetricCalibrationTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DeviceMetricCalibrationTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<DeviceMetricCalibrationTypeEnum>() {
		@Override
		public String toCodeString(DeviceMetricCalibrationTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DeviceMetricCalibrationTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DeviceMetricCalibrationTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DeviceMetricCalibrationTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DeviceMetricCalibrationTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DeviceMetricCalibrationTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
