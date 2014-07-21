
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

public enum ImmunizationRouteCodesEnum {

	/**
	 * Code Value: <b>IM</b>
	 */
	IM("IM", "http://hl7.org/fhir/v3/RouteOfAdministration"),
	
	/**
	 * Code Value: <b>PO</b>
	 */
	PO("PO", "http://hl7.org/fhir/v3/RouteOfAdministration"),
	
	/**
	 * Code Value: <b>NASINHL</b>
	 */
	NASINHL("NASINHL", "http://hl7.org/fhir/v3/RouteOfAdministration"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/immunization-route
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/immunization-route";

	/**
	 * Name for this Value Set:
	 * Immunization Route Codes
	 */
	public static final String VALUESET_NAME = "Immunization Route Codes";

	private static Map<String, ImmunizationRouteCodesEnum> CODE_TO_ENUM = new HashMap<String, ImmunizationRouteCodesEnum>();
	private static Map<String, Map<String, ImmunizationRouteCodesEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, ImmunizationRouteCodesEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (ImmunizationRouteCodesEnum next : ImmunizationRouteCodesEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, ImmunizationRouteCodesEnum>());
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
	public ImmunizationRouteCodesEnum forCode(String theCode) {
		ImmunizationRouteCodesEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ImmunizationRouteCodesEnum> VALUESET_BINDER = new IValueSetEnumBinder<ImmunizationRouteCodesEnum>() {
		@Override
		public String toCodeString(ImmunizationRouteCodesEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(ImmunizationRouteCodesEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public ImmunizationRouteCodesEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public ImmunizationRouteCodesEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, ImmunizationRouteCodesEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	ImmunizationRouteCodesEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
