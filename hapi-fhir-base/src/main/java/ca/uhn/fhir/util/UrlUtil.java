package ca.uhn.fhir.util;

public class UrlUtil {

	public static boolean isAbsolute(String theValue) {
		String value = theValue.toLowerCase();
		return value.startsWith("http://") || value.startsWith("https://");
	}

}
