
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum DeviceMetricCalibrationStateEnum {

	/**
	 * Code Value: <b>not-calibrated</b>
	 *
	 * The metric has not been calibrated.
	 */
	NOT_CALIBRATED("not-calibrated", "http://hl7.org/fhir/metric-calibration-state"),
	
	/**
	 * Code Value: <b>calibration-required</b>
	 *
	 * The metric needs to be calibrated.
	 */
	CALIBRATION_REQUIRED("calibration-required", "http://hl7.org/fhir/metric-calibration-state"),
	
	/**
	 * Code Value: <b>calibrated</b>
	 *
	 * The metric has been calibrated.
	 */
	CALIBRATED("calibrated", "http://hl7.org/fhir/metric-calibration-state"),
	
	/**
	 * Code Value: <b>unspecified</b>
	 *
	 * The state of calibration of this metric is unspecified.
	 */
	UNSPECIFIED("unspecified", "http://hl7.org/fhir/metric-calibration-state"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/metric-calibration-state
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/metric-calibration-state";

	/**
	 * Name for this Value Set:
	 * DeviceMetricCalibrationState
	 */
	public static final String VALUESET_NAME = "DeviceMetricCalibrationState";

	private static Map<String, DeviceMetricCalibrationStateEnum> CODE_TO_ENUM = new HashMap<String, DeviceMetricCalibrationStateEnum>();
	private static Map<String, Map<String, DeviceMetricCalibrationStateEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DeviceMetricCalibrationStateEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DeviceMetricCalibrationStateEnum next : DeviceMetricCalibrationStateEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DeviceMetricCalibrationStateEnum>());
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
	public DeviceMetricCalibrationStateEnum forCode(String theCode) {
		DeviceMetricCalibrationStateEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DeviceMetricCalibrationStateEnum> VALUESET_BINDER = new IValueSetEnumBinder<DeviceMetricCalibrationStateEnum>() {
		@Override
		public String toCodeString(DeviceMetricCalibrationStateEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DeviceMetricCalibrationStateEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DeviceMetricCalibrationStateEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DeviceMetricCalibrationStateEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DeviceMetricCalibrationStateEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DeviceMetricCalibrationStateEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
