package ca.uhn.fhir.rest.api;

import java.util.List;
import java.util.Map;

/**
 * Contract for a response plausible for header addition
 */
public interface IHasHeaders {

	Map<String, List<String>> getResponseHeaders();

}
