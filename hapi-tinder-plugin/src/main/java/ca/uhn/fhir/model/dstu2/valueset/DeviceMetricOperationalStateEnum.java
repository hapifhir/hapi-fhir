
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum DeviceMetricOperationalStateEnum {

	/**
	 * Code Value: <b>on</b>
	 *
	 * The DeviceMetric is operating and will generate DeviceObservations.
	 */
	ON("on", "http://hl7.org/fhir/metric-operational-status"),
	
	/**
	 * Code Value: <b>off</b>
	 *
	 * The DeviceMetric is not operating.
	 */
	OFF("off", "http://hl7.org/fhir/metric-operational-status"),
	
	/**
	 * Code Value: <b>standby</b>
	 *
	 * The DeviceMetric is operating, but will not generate any DeviceObservations.
	 */
	STANDBY("standby", "http://hl7.org/fhir/metric-operational-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/metric-operational-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/metric-operational-status";

	/**
	 * Name for this Value Set:
	 * DeviceMetricOperationalState
	 */
	public static final String VALUESET_NAME = "DeviceMetricOperationalState";

	private static Map<String, DeviceMetricOperationalStateEnum> CODE_TO_ENUM = new HashMap<String, DeviceMetricOperationalStateEnum>();
	private static Map<String, Map<String, DeviceMetricOperationalStateEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DeviceMetricOperationalStateEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DeviceMetricOperationalStateEnum next : DeviceMetricOperationalStateEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DeviceMetricOperationalStateEnum>());
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
	public DeviceMetricOperationalStateEnum forCode(String theCode) {
		DeviceMetricOperationalStateEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DeviceMetricOperationalStateEnum> VALUESET_BINDER = new IValueSetEnumBinder<DeviceMetricOperationalStateEnum>() {
		@Override
		public String toCodeString(DeviceMetricOperationalStateEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DeviceMetricOperationalStateEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DeviceMetricOperationalStateEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DeviceMetricOperationalStateEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DeviceMetricOperationalStateEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DeviceMetricOperationalStateEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
