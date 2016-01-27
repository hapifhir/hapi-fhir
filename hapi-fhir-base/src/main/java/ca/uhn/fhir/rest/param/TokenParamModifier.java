package ca.uhn.fhir.rest.param;

import java.util.HashMap;
import java.util.Map;

/**
 * Modifiers for {@link TokenParam}
 */
public enum TokenParamModifier {
	/** 
	 * :above
	 */
	ABOVE(":above"),
	
	/** 
	 * :above
	 */
	BELOW(":below"),
	
	/** 
	 * :in
	 */
	IN(":in"),
	
	/** 
	 * :not
	 */
	NOT(":not"),
	
	/** 
	 * :not-in
	 */
	NOT_IN(":not-in"),
	
	/** 
	 * :text
	 */
	TEXT(":text");
	
	private static final Map<String, TokenParamModifier> VALUE_TO_ENUM;

	static {
		Map<String, TokenParamModifier> valueToEnum = new HashMap<String, TokenParamModifier>();
		for (TokenParamModifier next : values()) {
			valueToEnum.put(next.getValue(), next);
		}
		VALUE_TO_ENUM = valueToEnum;
	}
	private final String myValue;

	private TokenParamModifier(String theValue) {
		myValue = theValue;
	}
	
	public String getValue() {
		return myValue;
	}

	public static TokenParamModifier forValue(String theValue) {
		return VALUE_TO_ENUM.get(theValue);
	}
	
}
