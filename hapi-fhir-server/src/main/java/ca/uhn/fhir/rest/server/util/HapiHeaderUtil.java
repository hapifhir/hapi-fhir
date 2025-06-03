package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.rest.api.HapiHeaderConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Function;

public final class HapiHeaderUtil {
	private HapiHeaderUtil() {}

	public static String getRequestIdKey(Set<String> theHeaderKeys) {
		// First try the official key
		for (String key : theHeaderKeys) {
			if (HapiHeaderConstants.REQUEST_ID.equalsIgnoreCase(key)) {
				return key;
			}
		}

		// Then try the deprecated key
		for (String key : theHeaderKeys) {
			if (HapiHeaderConstants.DEPRECATED_REQUEST_ID.equalsIgnoreCase(key)) {
				return key;
			}
		}

		return HapiHeaderConstants.REQUEST_ID; // Default fallback
	}

	@Nullable
	public static String getRequestId(Function<String, String> theGetHeaderFunction) {
		String requestId = theGetHeaderFunction.apply(HapiHeaderConstants.REQUEST_ID);
		if (requestId == null) {
			requestId = theGetHeaderFunction.apply(HapiHeaderConstants.DEPRECATED_REQUEST_ID);
		}
		return requestId;
	}

	public static boolean hasRequestId(Function<String, Boolean> theHasHeaderFunction) {
		return theHasHeaderFunction.apply(HapiHeaderConstants.REQUEST_ID) || theHasHeaderFunction.apply(HapiHeaderConstants.DEPRECATED_REQUEST_ID);
	}
}
