package ca.uhn.fhir.util;

public final class FhirTypeUtil {

	private FhirTypeUtil() {}

	/**
	 * Returns true if the type is a primitive fhir type
	 * (ie, a type that is IPrimitiveType), false otherwise.
	 */
	public static boolean isPrimitiveType(String theFhirType) {
		switch (theFhirType) {
			default:
				// non-primitive type (or unknown type)
				return false;
			case "string":
			case "code":
			case "markdown":
			case "id":
			case "uri":
			case "url":
			case "canonical":
			case "oid":
			case "uuid":
			case "boolean":
			case "unsignedInt":
			case "positiveInt":
			case "decimal":
			case "integer64":
			case "date":
			case "dateTime":
			case "time":
			case "instant":
			case "base6Binary":
				return true;
		}
	}
}
