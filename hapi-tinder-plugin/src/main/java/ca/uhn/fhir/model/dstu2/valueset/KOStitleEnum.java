
package ca.uhn.fhir.model.dstu2.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum KOStitleEnum {

	/**
	 * Code Value: <b>113000</b>
	 */
	_113000("113000", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113001</b>
	 */
	_113001("113001", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113002</b>
	 */
	_113002("113002", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113003</b>
	 */
	_113003("113003", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113004</b>
	 */
	_113004("113004", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113005</b>
	 */
	_113005("113005", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113006</b>
	 */
	_113006("113006", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113007</b>
	 */
	_113007("113007", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113008</b>
	 */
	_113008("113008", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113009</b>
	 */
	_113009("113009", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113010</b>
	 */
	_113010("113010", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113013</b>
	 */
	_113013("113013", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113018</b>
	 */
	_113018("113018", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113020</b>
	 */
	_113020("113020", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113021</b>
	 */
	_113021("113021", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113030</b>
	 */
	_113030("113030", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113031</b>
	 */
	_113031("113031", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113032</b>
	 */
	_113032("113032", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113033</b>
	 */
	_113033("113033", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113034</b>
	 */
	_113034("113034", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113035</b>
	 */
	_113035("113035", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113036</b>
	 */
	_113036("113036", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113037</b>
	 */
	_113037("113037", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113038</b>
	 */
	_113038("113038", "http://nema.org/dicom/dcid"),
	
	/**
	 * Code Value: <b>113039</b>
	 */
	_113039("113039", "http://nema.org/dicom/dcid"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/kos-title
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/kos-title";

	/**
	 * Name for this Value Set:
	 * KOStitle
	 */
	public static final String VALUESET_NAME = "KOStitle";

	private static Map<String, KOStitleEnum> CODE_TO_ENUM = new HashMap<String, KOStitleEnum>();
	private static Map<String, Map<String, KOStitleEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, KOStitleEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (KOStitleEnum next : KOStitleEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, KOStitleEnum>());
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
	public KOStitleEnum forCode(String theCode) {
		KOStitleEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<KOStitleEnum> VALUESET_BINDER = new IValueSetEnumBinder<KOStitleEnum>() {
		@Override
		public String toCodeString(KOStitleEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(KOStitleEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public KOStitleEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public KOStitleEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, KOStitleEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	KOStitleEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
