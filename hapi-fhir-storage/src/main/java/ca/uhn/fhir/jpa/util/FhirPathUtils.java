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

		// remove trailing .
		while (path.endsWith(".")) {
			path = path.substring(0, path.length() - 1);
		}

		// remove preceding .
		while (path.startsWith(".")) {
			path = path.substring(1);
		}

		// balance brackets
		int openBrace = path.indexOf("(");
		String remainder = path;
		int endingIndex = openBrace == -1 ? path.length() : 0;
		while (openBrace != -1) {
			int closing = RandomTextUtils.findMatchingClosingBrace(openBrace, remainder);
			endingIndex += closing + 1; // +1 because substring ending is exclusive
			remainder = remainder.substring(closing + 1);
			openBrace = remainder.indexOf("(");
		}
		path = path.substring(0, endingIndex);

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
