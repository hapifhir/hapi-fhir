package ca.uhn.fhir.rest.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing the values for the <code>_summary</code> search parameter
 */
public enum SummaryEnum {

	/**
	 * Return only those elements marked as 'summary' in the base definition of the resource(s)
	 */
	TRUE("true"),

	/**
	 * Return only the 'text' element, and any mandatory elements
	 */
	TEXT("text"),

	/**
	 * Remove the text element
	 */
	DATA("data"),

	/**
	 * Search only: just return a count of the matching resources, without returning the actual matches
	 */
	COUNT("count"),

	/**
	 * Return all parts of the resource(s)
	 */
	FALSE("false");
	
	private String myCode;
	private static Map<String, SummaryEnum> ourCodeToSummary = null;

	SummaryEnum(String theCode) {
		myCode = theCode;
	}

	public String getCode() {
		return myCode;
	}

	public static SummaryEnum fromCode(String theCode) {
		Map<String, SummaryEnum> c2s = ourCodeToSummary;
		if (c2s == null) {
			c2s = new HashMap<String, SummaryEnum>();
			for (SummaryEnum next : values()) {
				c2s.put(next.getCode(), next);
			}
			ourCodeToSummary = c2s;
		}
		return c2s.get(theCode);
	}
	
}
