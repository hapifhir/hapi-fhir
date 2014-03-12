package ca.uhn.fhir.rest.client.api;


import org.apache.http.client.HttpClient;

import ca.uhn.fhir.context.FhirContext;

public interface IRestfulClient {

	FhirContext getFhirContext();
	
	HttpClient getHttpClient();
	
	String getServerBase();
	
}
