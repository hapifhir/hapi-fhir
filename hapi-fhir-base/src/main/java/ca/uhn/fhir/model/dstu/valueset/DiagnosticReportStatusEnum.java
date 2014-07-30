
package ca.uhn.fhir.model.dstu.valueset;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public enum DiagnosticReportStatusEnum {

	/**
	 * Code Value: <b>registered</b>
	 *
	 * The existence of the report is registered, but there is nothing yet available.
	 */
	REGISTERED("registered", "http://hl7.org/fhir/diagnostic-report-status"),
	
	/**
	 * Code Value: <b>partial</b>
	 *
	 * This is a partial (e.g. initial, interim or preliminary) report: data in the report may be incomplete or unverified.
	 */
	PARTIAL("partial", "http://hl7.org/fhir/diagnostic-report-status"),
	
	/**
	 * Code Value: <b>final</b>
	 *
	 * The report is complete and verified by an authorized person.
	 */
	FINAL("final", "http://hl7.org/fhir/diagnostic-report-status"),
	
	/**
	 * Code Value: <b>corrected</b>
	 *
	 * The report has been modified subsequent to being Final, and is complete and verified by an authorized person.
	 */
	CORRECTED("corrected", "http://hl7.org/fhir/diagnostic-report-status"),
	
	/**
	 * Code Value: <b>amended</b>
	 *
	 * The report has been modified subsequent to being Final, and is complete and verified by an authorized person, and data has been changed.
	 */
	AMENDED("amended", "http://hl7.org/fhir/diagnostic-report-status"),
	
	/**
	 * Code Value: <b>appended</b>
	 *
	 * The report has been modified subsequent to being Final, and is complete and verified by an authorized person. New content has been added, but existing content hasn't changed.
	 */
	APPENDED("appended", "http://hl7.org/fhir/diagnostic-report-status"),
	
	/**
	 * Code Value: <b>cancelled</b>
	 *
	 * The report is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
	 */
	CANCELLED("cancelled", "http://hl7.org/fhir/diagnostic-report-status"),
	
	/**
	 * Code Value: <b>entered in error</b>
	 *
	 * The report has been withdrawn following previous Final release.
	 */
	ENTERED_IN_ERROR("entered in error", "http://hl7.org/fhir/diagnostic-report-status"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/diagnostic-report-status
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/diagnostic-report-status";

	/**
	 * Name for this Value Set:
	 * DiagnosticReportStatus
	 */
	public static final String VALUESET_NAME = "DiagnosticReportStatus";

	private static Map<String, DiagnosticReportStatusEnum> CODE_TO_ENUM = new HashMap<String, DiagnosticReportStatusEnum>();
	private static Map<String, Map<String, DiagnosticReportStatusEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DiagnosticReportStatusEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DiagnosticReportStatusEnum next : DiagnosticReportStatusEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DiagnosticReportStatusEnum>());
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
	public DiagnosticReportStatusEnum forCode(String theCode) {
		DiagnosticReportStatusEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DiagnosticReportStatusEnum> VALUESET_BINDER = new IValueSetEnumBinder<DiagnosticReportStatusEnum>() {
		@Override
		public String toCodeString(DiagnosticReportStatusEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DiagnosticReportStatusEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DiagnosticReportStatusEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DiagnosticReportStatusEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DiagnosticReportStatusEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DiagnosticReportStatusEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
