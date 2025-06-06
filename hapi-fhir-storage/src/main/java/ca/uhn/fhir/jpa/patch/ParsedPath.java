package ca.uhn.fhir.jpa.patch;

/**
 * This class is deprecated; consider using ParsedFhirPath, which allows much greater flexibility
 * in handling a fhirpath for various actions
 *
 * This class helps parse a FHIR path into its component parts for easier patch operation processing.
 * It has 3 components:
 *  - The last element name, which is the last element in the path (not including any list index or filter)
 *  - The containing path, which is the prefix of the path up to the last element
 *  - A flag indicating whether the path has a filter or index on the last element of the path, which indicates
 *  that the path we are dealing is probably for a list element.
 * Examples:
 * 1. For path "Patient.identifier[2].system",
 *   - the lastElementName is "system",
 *   - the containingPath is "Patient.identifier[2]",
 *   - and endsWithAFilterOrIndex flag is false
 *
 *  2. For path "Patient.identifier[2]" or for path "Patient.identifier.where('system'='sys1')"
 *  - the lastElementName is "identifier",
 *  - the containingPath is "Patient",
 *  - and the endsWithAFilterOrIndex is true
 */
@Deprecated()
public class ParsedPath {
	private final String myLastElementName;
	private final String myContainingPath;
	private final boolean myEndsWithAFilterOrIndex;

	public ParsedPath(String theLastElementName, String theContainingPath, boolean theEndsWithAFilterOrIndex) {
		myLastElementName = theLastElementName;
		myContainingPath = theContainingPath;
		myEndsWithAFilterOrIndex = theEndsWithAFilterOrIndex;
	}

	/**
	 * returns the last element of the path
	 */
	public String getLastElementName() {
		return myLastElementName;
	}

	/**
	 * Returns the prefix of the path up to the last FHIR resource element
	 */
	public String getContainingPath() {
		return myContainingPath;
	}

	/**
	 * Returns whether the path has a filter or index on the last element of the path, which indicates
	 * that the path we are dealing is probably a list element.
	 */
	public boolean getEndsWithAFilterOrIndex() {
		return myEndsWithAFilterOrIndex;
	}

	public static ParsedPath parse(String path) {
		String containingPath;
		String elementName;
		boolean endsWithAFilterOrIndex = false;

		if (path.endsWith(")")) {
			// This is probably a filter, so we're probably dealing with a list
			endsWithAFilterOrIndex = true;
			int filterArgsIndex = path.lastIndexOf('('); // Let's hope there aren't nested parentheses
			int lastDotIndex = path.lastIndexOf(
				'.',
				filterArgsIndex); // There might be a dot inside the parentheses, so look to the left of that
			int secondLastDotIndex = path.lastIndexOf('.', lastDotIndex - 1);
			containingPath = path.substring(0, secondLastDotIndex);
			elementName = path.substring(secondLastDotIndex + 1, lastDotIndex);
		} else if (path.endsWith("]")) {
			// This is almost definitely a list
			endsWithAFilterOrIndex = true;
			int openBracketIndex = path.lastIndexOf('[');
			int lastDotIndex = path.lastIndexOf('.', openBracketIndex);
			containingPath = path.substring(0, lastDotIndex);
			elementName = path.substring(lastDotIndex + 1, openBracketIndex);
		} else {
			int lastDot = path.lastIndexOf(".");
			containingPath = path.substring(0, lastDot);
			elementName = path.substring(lastDot + 1);
		}

		return new ParsedPath(elementName, containingPath, endsWithAFilterOrIndex);
	}
}
