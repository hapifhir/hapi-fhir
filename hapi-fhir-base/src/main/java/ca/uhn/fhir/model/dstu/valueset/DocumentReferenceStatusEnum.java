
package ca.uhn.fhir.model.dstu.valueset;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum DocumentReferenceStatusEnum {

	/**
	 * Code Value: <b>current</b>
	 *
	 * This is the current reference for this document.
	 */
	CURRENT("current", "http://hl7.org/fhir/document-reference-status"),
	
	/**
	 * Code Value: <b>superceded</b>
	 *
	 * This reference has been superseded by another reference.
	 */
	SUPERCEDED("superceded", "http://hl7.org/fhir/document-reference-status"),
	
	/**
	 * Code Value: <b>entered in error</b>
	 *
	 * This reference was created in error.
	 */
	ENTERED_IN_ERROR("entered in error", "http://hl7.org/fhir/document-reference-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/document-reference-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/document-reference-status";

	/**
	 * Name for this Value Set:
	 * DocumentReferenceStatus
	 */
	public static final String VALUESET_NAME = "DocumentReferenceStatus";

	private static Map<String, DocumentReferenceStatusEnum> CODE_TO_ENUM = new HashMap<String, DocumentReferenceStatusEnum>();
	private static Map<String, Map<String, DocumentReferenceStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DocumentReferenceStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DocumentReferenceStatusEnum next : DocumentReferenceStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DocumentReferenceStatusEnum>());
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
	public DocumentReferenceStatusEnum forCode(String theCode) {
		DocumentReferenceStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DocumentReferenceStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<DocumentReferenceStatusEnum>() {
		@Override
		public String toCodeString(DocumentReferenceStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DocumentReferenceStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DocumentReferenceStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DocumentReferenceStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DocumentReferenceStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DocumentReferenceStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
