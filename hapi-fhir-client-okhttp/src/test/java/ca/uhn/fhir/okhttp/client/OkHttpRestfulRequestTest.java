package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OkHttpRestfulRequestTest {

	@Test
	void toString_afterCreation_GetUsefulDataForLogging() {
		String theUrl = "https://example.com/fhir/meta";
		OkHttpRestfulClientFactory clientFactory = new OkHttpRestfulClientFactory();

		OkHttpRestfulRequest okHttpRestfulRequest = new OkHttpRestfulRequest(clientFactory.getNativeClient(), theUrl, RequestTypeEnum.GET, null);
		assertEquals("GET https://example.com/fhir/meta", okHttpRestfulRequest.toString());
	}

	@Test
	void toString_afterCreationPostUsefulDataForLogging() {
		String theUrl = "https://another.example.com/fhir/Task";
		OkHttpRestfulClientFactory clientFactory = new OkHttpRestfulClientFactory();

		OkHttpRestfulRequest okHttpRestfulRequest = new OkHttpRestfulRequest(clientFactory.getNativeClient(), theUrl, RequestTypeEnum.POST, null);
		assertEquals("POST https://another.example.com/fhir/Task", okHttpRestfulRequest.toString());
	}
}
