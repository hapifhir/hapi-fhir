
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

public enum DataTypeEnum {

	/**
	 * Code Value: <b>Address</b>
	 *
	 * There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world.
	 */
	ADDRESS("Address", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Age</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.  If value is present, it SHALL be positive.
	 */
	AGE("Age", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Attachment</b>
	 *
	 * For referring to data content defined in other formats.
	 */
	ATTACHMENT("Attachment", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>CodeableConcept</b>
	 *
	 * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
	 */
	CODEABLECONCEPT("CodeableConcept", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Coding</b>
	 *
	 * A reference to a code defined by a terminology system.
	 */
	CODING("Coding", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Contact</b>
	 *
	 * All kinds of technology mediated contact details for a person or organization, including telephone, email, etc.
	 */
	CONTACT("Contact", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Count</b>
	 *
	 * There SHALL be a code with a value of "1" if there is a value and it SHALL be an expression of length.  If system is present, it SHALL be UCUM.  If present, the value SHALL a whole number.
	 */
	COUNT("Count", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Distance</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of length.  If system is present, it SHALL be UCUM.
	 */
	DISTANCE("Distance", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Duration</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.
	 */
	DURATION("Duration", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Extension</b>
	 *
	 * Optional Extensions Element - found in all resources.
	 */
	EXTENSION("Extension", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>HumanName</b>
	 *
	 * A human's name with the ability to identify parts and usage.
	 */
	HUMANNAME("HumanName", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Identifier</b>
	 *
	 * A technical identifier - identifies some entity uniquely and unambiguously.
	 */
	IDENTIFIER("Identifier", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Money</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of currency.  If system is present, it SHALL be ISO 4217 (system = "urn:std:iso:4217" - currency).
	 */
	MONEY("Money", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Narrative</b>
	 *
	 * A human-readable formatted text, including images.
	 */
	NARRATIVE("Narrative", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Period</b>
	 *
	 * A time period defined by a start and end date and optionally time.
	 */
	PERIOD("Period", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Quantity</b>
	 *
	 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
	 */
	QUANTITY("Quantity", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Range</b>
	 *
	 * A set of ordered Quantities defined by a low and high limit.
	 */
	RANGE("Range", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Ratio</b>
	 *
	 * A relationship of two Quantity values - expressed as a numerator and a denominator.
	 */
	RATIO("Ratio", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>ResourceReference</b>
	 *
	 * A reference from one resource to another.
	 */
	RESOURCEREFERENCE("ResourceReference", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>SampledData</b>
	 *
	 * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
	 */
	SAMPLEDDATA("SampledData", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>Schedule</b>
	 *
	 * Specifies an event that may occur multiple times. Schedules are used for to reord when things are expected or requested to occur.
	 */
	SCHEDULE("Schedule", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>base64Binary</b>
	 *
	 * A stream of bytes
	 */
	BASE64BINARY("base64Binary", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>boolean</b>
	 *
	 * Value of "true" or "false"
	 */
	BOOLEAN("boolean", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>code</b>
	 *
	 * A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents
	 */
	CODE("code", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>date</b>
	 *
	 * A date, or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.
	 */
	DATE("date", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>dateTime</b>
	 *
	 * A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds may be provided but may also be ignored.  Dates SHALL be valid dates.
	 */
	DATETIME("dateTime", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>decimal</b>
	 *
	 * A rational number with implicit precision
	 */
	DECIMAL("decimal", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>id</b>
	 *
	 * A whole number in the range 0 to 2^64-1, optionally represented in hex, a uuid, an oid or any other combination of lower-case letters a-z, numerals, "-" and ".", with a length limit of 36 characters
	 */
	ID("id", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>instant</b>
	 *
	 * An instant in time - known at least to the second
	 */
	INSTANT("instant", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>integer</b>
	 *
	 * A whole number
	 */
	INTEGER("integer", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>oid</b>
	 *
	 * An oid represented as a URI
	 */
	OID("oid", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>string</b>
	 *
	 * A sequence of Unicode characters
	 */
	STRING("string", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>uri</b>
	 *
	 * String of characters used to identify a name or a resource
	 */
	URI("uri", "http://hl7.org/fhir/data-types"),
	
	/**
	 * Code Value: <b>uuid</b>
	 *
	 * A UUID, represented as a URI
	 */
	UUID("uuid", "http://hl7.org/fhir/data-types"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/data-types
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/data-types";

	/**
	 * Name for this Value Set:
	 * DataType
	 */
	public static final String VALUESET_NAME = "DataType";

	private static Map<String, DataTypeEnum> CODE_TO_ENUM = new HashMap<String, DataTypeEnum>();
	private static Map<String, Map<String, DataTypeEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, DataTypeEnum>>();
	
	private final String myCode;
	private final String mySystem;
	
	static {
		for (DataTypeEnum next : DataTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
			
			if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
				SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, DataTypeEnum>());
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
	public DataTypeEnum forCode(String theCode) {
		DataTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<DataTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<DataTypeEnum>() {
		@Override
		public String toCodeString(DataTypeEnum theEnum) {
			return theEnum.getCode();
		}

		@Override
		public String toSystemString(DataTypeEnum theEnum) {
			return theEnum.getSystem();
		}
		
		@Override
		public DataTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
		
		@Override
		public DataTypeEnum fromCodeString(String theCodeString, String theSystemString) {
			Map<String, DataTypeEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
			if (map == null) {
				return null;
			}
			return map.get(theCodeString);
		}
		
	};
	
	/** 
	 * Constructor
	 */
	DataTypeEnum(String theCode, String theSystem) {
		myCode = theCode;
		mySystem = theSystem;
	}

	
}
