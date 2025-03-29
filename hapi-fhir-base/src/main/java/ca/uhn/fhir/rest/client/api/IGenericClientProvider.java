package ca.uhn.fhir.rest.client.api;

public interface IGenericClientProvider {

	IGenericClient newRestfulGenericClient(String theBaseUrl);
}
