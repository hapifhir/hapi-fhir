package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.patch.ParsedFhirPath;

import java.util.HashMap;
import java.util.Map;

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
		if (!hasBalancedBraces(thePath)) {
			throw new IllegalArgumentException(
				Msg.code(2726)
				+ String.format("Cannot cleanse path - %s is not a valid fhir path", thePath)
			);
		}

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

	private static Map<Character, Integer> parenthesesBalance(String theStr, char theOpening, char theClosing) {
		Map<Character, Integer> map = new HashMap<>();
		map.put(theOpening, 0);
		map.put(theClosing, 0);

		int openingCount = 0;
		int closingCount = 0;
		int len = theStr.length();
		for (int i = 0; i < len; i++) {
			if (theStr.charAt(i) == theOpening) {
				openingCount++;
			} else if (theStr.charAt(i) == theClosing) {
				closingCount++;
			}
		}
		map.put(theOpening, openingCount);
		map.put(theClosing, closingCount);
		return map;
	}

	public static boolean hasBalancedBraces(String theStr) {
		char opening = '(';
		char closing = ')';

		Map<Character, Integer> matching = parenthesesBalance(theStr, opening, closing);

		return matching.get(opening).intValue() == matching.get(closing).intValue();
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
