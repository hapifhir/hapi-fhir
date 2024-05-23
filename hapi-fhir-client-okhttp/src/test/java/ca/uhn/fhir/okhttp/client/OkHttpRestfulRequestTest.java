package ca.uhn.fhir.okhttp.client;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OkHttpRestfulRequestTest {

	private final String ENTITY_CONTENT = "Some entity with special characters: é";

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

	@Test
	public void testGetRequestBodyFromStream() throws IOException {
		RequestBody requestBody = RequestBody.create(ENTITY_CONTENT.getBytes(StandardCharsets.ISO_8859_1), MediaType.parse("text/plain; charset=ISO-8859-1"));

		String result = new OkHttpRestfulRequest(null, "https://test.com", null, requestBody).getRequestBodyFromStream();

		assertEquals(ENTITY_CONTENT, result);
	}

	@Test
	public void testGetRequestBodyFromStreamWithUnknownCharset() throws IOException {
		RequestBody requestBody = RequestBody.create(ENTITY_CONTENT.getBytes(StandardCharsets.UTF_8), MediaType.parse("text/plain; charset=UTF-8"));

		String result = new OkHttpRestfulRequest(null, "https://test.com", null, requestBody).getRequestBodyFromStream();

		assertEquals(ENTITY_CONTENT, result);
	}
}
