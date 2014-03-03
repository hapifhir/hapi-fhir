
package ca.uhn.fhir.model.dstu.valueset;

import ca.uhn.fhir.model.api.*;
import java.util.HashMap;
import java.util.Map;

public enum BindingConformanceEnum {

	/**
	 * required
	 * 
	 *
	 * Only codes in the specified set are allowed.  If the binding is extensible, other codes may be used for concepts not covered by the bound set of codes.
	 */
	REQUIRED("required"),
	
	/**
	 * preferred
	 * 
	 *
	 * For greater interoperability, implementers are strongly encouraged to use the bound set of codes, however alternate codes may be used in derived profiles and implementations if necessary without being considered non-conformant.
	 */
	PREFERRED("preferred"),
	
	/**
	 * example
	 * 
	 *
	 * The codes in the set are an example to illustrate the meaning of the field. There is no particular preference for its use nor any assertion that the provided values are sufficient to meet implementation needs.
	 */
	EXAMPLE("example"),
	
	;
	
	/**
	 * Identifier for this Value Set:
	 * http://hl7.org/fhir/vs/binding-conformance
	 */
	public static final String VALUESET_IDENTIFIER = "http://hl7.org/fhir/vs/binding-conformance";

	/**
	 * Name for this Value Set:
	 * BindingConformance
	 */
	public static final String VALUESET_NAME = "BindingConformance";

	private static Map<String, BindingConformanceEnum> CODE_TO_ENUM = new HashMap<String, BindingConformanceEnum>();
	private String myCode;
	
	static {
		for (BindingConformanceEnum next : BindingConformanceEnum.values()) {
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
	public BindingConformanceEnum forCode(String theCode) {
		BindingConformanceEnum retVal = CODE_TO_ENUM.get(theCode);
		return retVal;
	}

	/**
	 * Converts codes to their respective enumerated values
	 */
	public static final IValueSetEnumBinder<BindingConformanceEnum> VALUESET_BINDER = new IValueSetEnumBinder<BindingConformanceEnum>() {
		@Override
		public String toCodeString(BindingConformanceEnum theEnum) {
			return theEnum.getCode();
		}
		
		@Override
		public BindingConformanceEnum fromCodeString(String theCodeString) {
			return CODE_TO_ENUM.get(theCodeString);
		}
	};
	
	/** 
	 * Constructor
	 */
	BindingConformanceEnum(String theCode) {
		myCode = theCode;
	}

	
}
