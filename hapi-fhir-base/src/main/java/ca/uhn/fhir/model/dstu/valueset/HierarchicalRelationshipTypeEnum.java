
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum HierarchicalRelationshipTypeEnum {

	/**
	 * parent
	 * Parent
	 *
	 * The target resource is the parent of the focal specimen resource.
	 */
	PARENT("parent"),
	
	/**
	 * child
	 * Child
	 *
	 * The target resource is the child of the focal specimen resource.
	 */
	CHILD("child"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/hierarchical-relationship-type
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/hierarchical-relationship-type";

	/**
	 * Name for this Value Set:
	 * HierarchicalRelationshipType
	 */
	public static final String VALUESET_NAME = "HierarchicalRelationshipType";

	private static Map<String, HierarchicalRelationshipTypeEnum> CODE_TO_ENUM = new HashMap<String, HierarchicalRelationshipTypeEnum>();
	private String myCode;
	
	static {
		for (HierarchicalRelationshipTypeEnum next : HierarchicalRelationshipTypeEnum.values()) {
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
	public HierarchicalRelationshipTypeEnum forCode(String theCode) {
		HierarchicalRelationshipTypeEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<HierarchicalRelationshipTypeEnum> VALUESET_BINDER = new IValueSetEnumBinder<HierarchicalRelationshipTypeEnum>() {
		@Override
		public String toCodeString(HierarchicalRelationshipTypeEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public HierarchicalRelationshipTypeEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	HierarchicalRelationshipTypeEnum(String theCode) {
		myCode = theCode;
	}

	
}
