package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericClientNonFhirTest {
	FhirContext myFhirContext = FhirContext.forR4Cached();
	@Test
	void genericClientHttpClient_canBuildGetRequest() {
	    // given
		IGenericClient genericClient = myFhirContext.newRestfulGenericClient("http://example.com");

		IHttpRequest getRequest = genericClient.getHttpClient().createGetRequest(myFhirContext, EncodingEnum.JSON);
		assertEquals("GET", getRequest.getHttpVerbName());
	}

}
