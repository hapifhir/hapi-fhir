package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public interface IClientResponseHandler {

	Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException;

}
