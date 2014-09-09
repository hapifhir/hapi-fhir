
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

public enum SecurityEventObjectLifecycleEnum {

	/**
	 * Code Value: <b>1</b>
	 *
	 * Origination / Creation.
	 */
	ORIGINATION_OR_CREATION("1", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>2</b>
	 *
	 * Import / Copy from original.
	 */
	IMPORT_OR_COPY_FROM_ORIGINAL("2", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>3</b>
	 *
	 * Amendment.
	 */
	AMENDMENT("3", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>4</b>
	 *
	 * Verification.
	 */
	VERIFICATION("4", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>5</b>
	 *
	 * Translation.
	 */
	TRANSLATION("5", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>6</b>
	 *
	 * Access / Use.
	 */
	ACCESS_OR_USE("6", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>7</b>
	 *
	 * De-identification.
	 */
	DE_IDENTIFICATION("7", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>8</b>
	 *
	 * Aggregation, summarization, derivation.
	 */
	AGGREGATION("8", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>9</b>
	 *
	 * Report.
	 */
	REPORT("9", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>10</b>
	 *
	 * Export / Copy to target.
	 */
	EXPORT_OR_COPY_TO_TARGET("10", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>11</b>
	 *
	 * Disclosure.
	 */
	DISCLOSURE("11", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>12</b>
	 *
	 * Receipt of disclosure.
	 */
	RECEIPT_OF_DISCLOSURE("12", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>13</b>
	 *
	 * Archiving.
	 */
	ARCHIVING("13", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>14</b>
	 *
	 * Logical deletion.
	 */
	LOGICAL_DELETION("14", "http://hl7.org/fhir/object-lifecycle"),
	
	/**
	 * Code Value: <b>15</b>
	 *
	 * Permanent erasure / Physical destruction.
	 */
	PERMANENT_ERASURE_OR_PHYSICAL_DESTRUCTION("15", "http://hl7.org/fhir/object-lifecycle"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/object-lifecycle
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/object-lifecycle";

	/**
	 * Name for this Value Set:
	 * SecurityEventObjectLifecycle
	 */
	public static final String VALUESET_NAME = "SecurityEventObjectLifecycle";

	private static Map<String, SecurityEventObjectLifecycleEnum> CODE_TO_ENUM = new HashMap<String, SecurityEventObjectLifecycleEnum>();
	private static Map<String, Map<String, SecurityEventObjectLifecycleEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, SecurityEventObjectLifecycleEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (SecurityEventObjectLifecycleEnum next : SecurityEventObjectLifecycleEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, SecurityEventObjectLifecycleEnum>());
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
	public SecurityEventObjectLifecycleEnum forCode(String theCode) {
		SecurityEventObjectLifecycleEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<SecurityEventObjectLifecycleEnum> VALUESET_BINDER = new IValueSetEnumBinder<SecurityEventObjectLifecycleEnum>() {
		@Override
		public String toCodeString(SecurityEventObjectLifecycleEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(SecurityEventObjectLifecycleEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public SecurityEventObjectLifecycleEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public SecurityEventObjectLifecycleEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, SecurityEventObjectLifecycleEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	SecurityEventObjectLifecycleEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
