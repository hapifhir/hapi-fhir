
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum ObservationRelationshipTypeEnum {

	/**
	 * Code Value: <b>has-component</b>
	 *
	 * The target observation is a component of this observation (e.g. Systolic and Diastolic Blood Pressure).
	 */
	HAS_COMPONENT("has-component"),
	
	/**
	 * Code Value: <b>has-member</b>
	 *
	 * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.
	 */
	HAS_MEMBER("has-member"),
	
	/**
	 * Code Value: <b>derived-from</b>
	 *
	 * The target observation is part of the information from which this observation value is derived (e.g. calculated anion gap, Apgar score).
	 */
	DERIVED_FROM("derived-from"),
	
	/**
	 * Code Value: <b>sequel-to</b>
	 *
	 * This observation follows the target observation (e.g. timed tests such as Glucose Tolerance Test).
	 */
	SEQUEL_TO("sequel-to"),
	
	/**
	 * Code Value: <b>replaces</b>
	 *
	 * This observation replaces a previous observation (i.e. a revised value). The target observation is now obsolete.
	 */
	REPLACES("replaces"),
	
	/**
	 * Code Value: <b>qualified-by</b>
	 *
	 * The value of the target observation qualifies (refines) the semantics of the source observation (e.g. a lipaemia measure target from a plasma measure).
	 */
	QUALIFIED_BY("qualified-by"),
	
	/**
	 * Code Value: <b>interfered-by</b>
	 *
	 * The value of the target observation interferes (degardes quality, or prevents valid observation) with the semantics of the source observation (e.g. a hemolysis measure target from a plasma potassium measure which has no value).
	 */
	INTERFERED_BY("interfered-by"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/observation-relationshiptypes
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/observation-relationshiptypes";

	/**
	 * Name for this Value Set:
	 * ObservationRelationshipType
	 */
	public static final String VALUESET_NAME = "ObservationRelationshipType";

	private static Map<String, ObservationRelationshipTypeEnum> CODE_TO_ENUM = new HashMap<String, ObservationRelationshipTypeEnum>();
	private String myCode;
	
	static {
		for (ObservationRelationshipTypeEnum next : ObservationRelationshipTypeEnum.values()) {
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
	public ObservationRelationshipTypeEnum forCode(String theCode) {
		ObservationRelationshipTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<ObservationRelationshipTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<ObservationRelationshipTypeEnum>() {
		@Override
		public String toCodeString(ObservationRelationshipTypeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public ObservationRelationshipTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	ObservationRelationshipTypeEnum(String theCode) {
		myCode = theCode;
	}

	
}
