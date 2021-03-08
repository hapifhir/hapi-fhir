package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

/**
 * Contract for standardizing textual primitives in the FHIR resources.
 */
public interface IStandardizer {

	/**
	 * Standardizes the specified string.
	 *
	 * @param theString String to be standardized
	 * @return Returns a standardized string.
	 */
	public String standardize(String theString);

}
