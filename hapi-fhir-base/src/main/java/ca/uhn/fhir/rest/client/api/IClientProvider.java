package ca.uhn.fhir.rest.client.api;

public interface IClientProvider {

	IGenericClient newRestfulGenericClient(String theBaseUrl);
}
