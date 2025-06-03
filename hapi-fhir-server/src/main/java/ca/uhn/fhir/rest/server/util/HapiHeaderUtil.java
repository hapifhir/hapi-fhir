package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.rest.api.HapiHeaderConstants;

import javax.annotation.Nullable;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class HapiHeaderUtil {
	private HapiHeaderUtil() {
	}

	public static final Map<String, String> ourHeaderToDeprecatedHeaderMap = Map.of(
		HapiHeaderConstants.REQUEST_ID, HapiHeaderConstants.DEPRECATED_REQUEST_ID,
		HapiHeaderConstants.RETRY_ON_VERSION_CONFLICT, HapiHeaderConstants.DEPRECATED_RETRY_ON_VERSION_CONFLICT
	);

	public static String getHeaderOrDeprecatedHeaderKey(Set<String> theHeaderKeys, String theHeaderKey) {
		// First try the official key
		for (String key : theHeaderKeys) {
			if (theHeaderKey.equalsIgnoreCase(key)) {
				return key;
			}
		}

		// Then try the deprecated key
		for (String key : theHeaderKeys) {
			if (ourHeaderToDeprecatedHeaderMap.get(theHeaderKey).equalsIgnoreCase(key)) {
				return key;
			}
		}

		return theHeaderKey; // Default fallback
	}

	@Nullable
	public static String getHeaderOrDeprecatedHeader(Function<String, String> theGetHeaderFunction, String theHeaderKey) {
		String requestId = theGetHeaderFunction.apply(theHeaderKey);
		if (requestId == null) {
			requestId = theGetHeaderFunction.apply(ourHeaderToDeprecatedHeaderMap.get(theHeaderKey));
		}
		return requestId;
	}

	public static boolean hasHeaderOrDeprecatedHeader(Function<String, Boolean> theHasHeaderFunction, String theHeaderKey) {
		return theHasHeaderFunction.apply(theHeaderKey) || theHasHeaderFunction.apply(ourHeaderToDeprecatedHeaderMap.get(theHeaderKey));
	}

	public static Enumeration<String> getHeadersOrDeprecatedHeaders(Function<String, Enumeration<String>> theGetHeadersFunction, String theHeaderKey) {
		Enumeration<String> headers = theGetHeadersFunction.apply(theHeaderKey);
		if (headers == null || !headers.hasMoreElements()) {
			headers = theGetHeadersFunction.apply(ourHeaderToDeprecatedHeaderMap.get(theHeaderKey));
		}
		return headers;
	}
}
