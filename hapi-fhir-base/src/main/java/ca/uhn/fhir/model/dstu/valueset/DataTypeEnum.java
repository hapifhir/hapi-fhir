
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum DataTypeEnum {

	/**
	 * Code Value: <b>Address</b>
	 *
	 * There is a variety of postal address formats defined around the world. This format defines a superset that is the basis for all addresses around the world.
	 */
	ADDRESS("Address"),
	
	/**
	 * Code Value: <b>Age</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.  If value is present, it SHALL be positive.
	 */
	AGE("Age"),
	
	/**
	 * Code Value: <b>Attachment</b>
	 *
	 * For referring to data content defined in other formats.
	 */
	ATTACHMENT("Attachment"),
	
	/**
	 * Code Value: <b>CodeableConcept</b>
	 *
	 * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
	 */
	CODEABLECONCEPT("CodeableConcept"),
	
	/**
	 * Code Value: <b>Coding</b>
	 *
	 * A reference to a code defined by a terminology system.
	 */
	CODING("Coding"),
	
	/**
	 * Code Value: <b>Contact</b>
	 *
	 * All kinds of technology mediated contact details for a person or organization, including telephone, email, etc.
	 */
	CONTACT("Contact"),
	
	/**
	 * Code Value: <b>Count</b>
	 *
	 * There SHALL be a code with a value of "1" if there is a value and it SHALL be an expression of length.  If system is present, it SHALL be UCUM.  If present, the value SHALL a whole number.
	 */
	COUNT("Count"),
	
	/**
	 * Code Value: <b>Distance</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of length.  If system is present, it SHALL be UCUM.
	 */
	DISTANCE("Distance"),
	
	/**
	 * Code Value: <b>Duration</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of time.  If system is present, it SHALL be UCUM.
	 */
	DURATION("Duration"),
	
	/**
	 * Code Value: <b>Extension</b>
	 *
	 * Optional Extensions Element - found in all resources.
	 */
	EXTENSION("Extension"),
	
	/**
	 * Code Value: <b>HumanName</b>
	 *
	 * A human's name with the ability to identify parts and usage.
	 */
	HUMANNAME("HumanName"),
	
	/**
	 * Code Value: <b>Identifier</b>
	 *
	 * A technical identifier - identifies some entity uniquely and unambiguously.
	 */
	IDENTIFIER("Identifier"),
	
	/**
	 * Code Value: <b>Money</b>
	 *
	 * There SHALL be a code if there is a value and it SHALL be an expression of currency.  If system is present, it SHALL be ISO 4217 (system = "urn:std:iso:4217" - currency).
	 */
	MONEY("Money"),
	
	/**
	 * Code Value: <b>Narrative</b>
	 *
	 * A human-readable formatted text, including images.
	 */
	NARRATIVE("Narrative"),
	
	/**
	 * Code Value: <b>Period</b>
	 *
	 * A time period defined by a start and end date and optionally time.
	 */
	PERIOD("Period"),
	
	/**
	 * Code Value: <b>Quantity</b>
	 *
	 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
	 */
	QUANTITY("Quantity"),
	
	/**
	 * Code Value: <b>Range</b>
	 *
	 * A set of ordered Quantities defined by a low and high limit.
	 */
	RANGE("Range"),
	
	/**
	 * Code Value: <b>Ratio</b>
	 *
	 * A relationship of two Quantity values - expressed as a numerator and a denominator.
	 */
	RATIO("Ratio"),
	
	/**
	 * Code Value: <b>ResourceReference</b>
	 *
	 * A reference from one resource to another.
	 */
	RESOURCEREFERENCE("ResourceReference"),
	
	/**
	 * Code Value: <b>SampledData</b>
	 *
	 * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
	 */
	SAMPLEDDATA("SampledData"),
	
	/**
	 * Code Value: <b>Schedule</b>
	 *
	 * Specifies an event that may occur multiple times. Schedules are used for to reord when things are expected or requested to occur.
	 */
	SCHEDULE("Schedule"),
	
	/**
	 * Code Value: <b>base64Binary</b>
	 *
	 * A stream of bytes
	 */
	BASE64BINARY("base64Binary"),
	
	/**
	 * Code Value: <b>boolean</b>
	 *
	 * Value of "true" or "false"
	 */
	BOOLEAN("boolean"),
	
	/**
	 * Code Value: <b>code</b>
	 *
	 * A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents
	 */
	CODE("code"),
	
	/**
	 * Code Value: <b>date</b>
	 *
	 * A date, or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.
	 */
	DATE("date"),
	
	/**
	 * Code Value: <b>dateTime</b>
	 *
	 * A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds may be provided but may also be ignored.  Dates SHALL be valid dates.
	 */
	DATETIME("dateTime"),
	
	/**
	 * Code Value: <b>decimal</b>
	 *
	 * A rational number with implicit precision
	 */
	DECIMAL("decimal"),
	
	/**
	 * Code Value: <b>id</b>
	 *
	 * A whole number in the range 0 to 2^64-1, optionally represented in hex, a uuid, an oid or any other combination of lower-case letters a-z, numerals, "-" and ".", with a length limit of 36 characters
	 */
	ID("id"),
	
	/**
	 * Code Value: <b>instant</b>
	 *
	 * An instant in time - known at least to the second
	 */
	INSTANT("instant"),
	
	/**
	 * Code Value: <b>integer</b>
	 *
	 * A whole number
	 */
	INTEGER("integer"),
	
	/**
	 * Code Value: <b>oid</b>
	 *
	 * An oid represented as a URI
	 */
	OID("oid"),
	
	/**
	 * Code Value: <b>string</b>
	 *
	 * A sequence of Unicode characters
	 */
	STRING("string"),
	
	/**
	 * Code Value: <b>uri</b>
	 *
	 * String of characters used to identify a name or a resource
	 */
	URI("uri"),
	
	/**
	 * Code Value: <b>uuid</b>
	 *
	 * A UUID, represented as a URI
	 */
	UUID("uuid"),
	
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
	private String myCode;
	
	static {
		for (DataTypeEnum next : DataTypeEnum.values()) {
			CODE_TO_ENUM.put(next.getCode(), next);
		}
	}
	
	/**
	 * Returns the code associated with this enumerated value
	 */
	public String getCode() {
		return myCode;
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
		public DataTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	DataTypeEnum(String theCode) {
		myCode = theCode;
	}

	
}
