
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

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum OrganizationTypeEnum {

	/**
	 * Display: <b>Healthcare Provider</b><br/>
	 * Code Value: <b>prov</b>
	 */
	HEALTHCARE_PROVIDER("prov", "http://hl7.org/fhir/organization-type"),
	
	/**
	 * Display: <b>Hospital Department</b><br/>
	 * Code Value: <b>dept</b>
	 */
	HOSPITAL_DEPARTMENT("dept", "http://hl7.org/fhir/organization-type"),
	
	/**
	 * Display: <b>Intensive Care Unit</b><br/>
	 * Code Value: <b>icu</b>
	 */
	INTENSIVE_CARE_UNIT("icu", "http://hl7.org/fhir/organization-type"),
	
	/**
	 * Display: <b>Organizational team</b><br/>
	 * Code Value: <b>team</b>
	 */
	ORGANIZATIONAL_TEAM("team", "http://hl7.org/fhir/organization-type"),
	
	/**
	 * Display: <b>Federal Government</b><br/>
	 * Code Value: <b>fed</b>
	 */
	FEDERAL_GOVERNMENT("fed", "http://hl7.org/fhir/organization-type"),
	
	/**
	 * Display: <b>Insurance Company</b><br/>
	 * Code Value: <b>ins</b>
	 */
	INSURANCE_COMPANY("ins", "http://hl7.org/fhir/organization-type"),
	
	/**
	 * Display: <b>Educational Institute</b><br/>
	 * Code Value: <b>edu</b>
	 */
	EDUCATIONAL_INSTITUTE("edu", "http://hl7.org/fhir/organization-type"),
	
	/**
	 * Display: <b>Religious Institution</b><br/>
	 * Code Value: <b>reli</b>
	 */
	RELIGIOUS_INSTITUTION("reli", "http://hl7.org/fhir/organization-type"),
	
	/**
	 * Display: <b>Pharmacy</b><br/>
	 * Code Value: <b>pharm</b>
	 */
	PHARMACY("pharm", "http://hl7.org/fhir/organization-type"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/organization-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/organization-type";

	/**
	 * Name for this Value Set:
	 * OrganizationType
	 */
	public static final String VALUESET_NAME = "OrganizationType";

	private static Map<String, OrganizationTypeEnum> CODE_TO_ENUM = new HashMap<String, OrganizationTypeEnum>();
	private static Map<String, Map<String, OrganizationTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, OrganizationTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (OrganizationTypeEnum next : OrganizationTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, OrganizationTypeEnum>());
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
	public OrganizationTypeEnum forCode(String theCode) {
		OrganizationTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<OrganizationTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<OrganizationTypeEnum>() {
		@Override
		public String toCodeString(OrganizationTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(OrganizationTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public OrganizationTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public OrganizationTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, OrganizationTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	OrganizationTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
