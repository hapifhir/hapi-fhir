package ca.uhn.fhir.rest.server;

/**
 * @see <a href="http://hapifhir.io/doc_rest_server.html#extended_elements_support">Extended Elements Support</a>
 */
public enum ElementsSupportEnum {

	/**
	 * The server will support only the FHIR standard features for the <code>_elements</code>
	 * parameter.
	 *
	 * @see <a href="http://hl7.org/fhir/search.html#elements">http://hl7.org/fhir/search.html#elements</a>
	 */
	STANDARD,

	/**
	 * The server will support both the standard features as well as support for elements
	 * exclusion.
	 */
	EXTENDED

}
