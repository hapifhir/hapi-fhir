package ca.uhn.fhir.rest.client.api;

import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.rest.annotation.Metadata;

/**
 * Base interface for a client supporting the mandatory operations as defined by
 * the FHIR specification.
 */
public interface IBasicClient extends IRestfulClient {

	/**
	 * Returns the server conformance statement
	 * 
	 * @see See the <a href="http://hl7.org/implement/standards/fhir/http.html#conformance">FHIR HTTP Conformance</a> definition
	 */
	@Metadata
	Conformance getServerConformanceStatement();

}
