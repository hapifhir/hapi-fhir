package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.patch.ParsedFhirPath;

public class FhirPathUtils {
	/**
	 * Turns an invalid FhirPath into a valid one
	 * -
	 * Do not use this for user input; but it can be used for internally parsed paths
	 */
	public static String cleansePath(String thePath) {
		String path = thePath;

		if (path.startsWith("()")) {
			path = path.substring(2);
		}

		// remove trailing .
		while (path.endsWith(".")) {
			path = path.substring(0, path.length() - 1);
		}

		// remove preceding .
		while (path.startsWith(".")) {
			path = path.substring(1);
		}

		return path;
	}

	/**
	 * Determines if the node is a subsetting node
	 * as described by http://hl7.org/fhirpath/N1/#subsetting
	 */
	public static boolean isSubsettingNode(ParsedFhirPath.FhirPathNode theNode) {
		if (theNode.getListIndex() >= 0) {
			return true;
		}
		if (theNode.isFunction()) {
			String funName = theNode.getValue();
			switch (funName) {
				case "first", "last", "single", "tail", "skip", "take", "exclude", "intersect" -> {
					return true;
				}
			}
		}
		return false;
	}
}
