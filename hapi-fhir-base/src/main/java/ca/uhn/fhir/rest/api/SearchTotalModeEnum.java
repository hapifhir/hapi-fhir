package ca.uhn.fhir.rest.api;

import java.util.HashMap;
import java.util.Map;

public enum SearchTotalModeEnum {

	NONE("none"),
	ESTIMATED("estimated"),
	ACCURATE("accurate");

	private static volatile Map<String, SearchTotalModeEnum> ourCodeToEnum;
	private final String myCode;

	SearchTotalModeEnum(String theCode) {
		myCode = theCode;
	}

	public String getCode() {
		return myCode;
	}

	public static SearchTotalModeEnum fromCode(String theCode) {
		Map<String, SearchTotalModeEnum> map = ourCodeToEnum;
		if (map == null) {
			map = new HashMap<>();
			for (SearchTotalModeEnum next : values()) {
				map.put(next.getCode(), next);
			}
			ourCodeToEnum = map;
		}
		return map.get(theCode);
	}
}
