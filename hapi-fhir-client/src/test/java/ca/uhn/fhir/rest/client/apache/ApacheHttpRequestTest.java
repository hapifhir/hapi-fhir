package ca.uhn.fhir.rest.client.apache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;

public class ApacheHttpRequestTest {

	private final String ENTITY_CONTENT = "Some entity with special characters: é";
	private StringEntity httpEntity;
	private HttpPost apacheRequest = new HttpPost("");

	@Test
	public void testGetRequestBodyFromStream() throws IOException {
		httpEntity = new StringEntity(ENTITY_CONTENT, Charset.forName("ISO-8859-1"));
		apacheRequest.setHeader("Content-type", "text/plain; charset=ISO-8859-1");
		apacheRequest.setEntity(httpEntity);

		String result = new ApacheHttpRequest(null, apacheRequest).getRequestBodyFromStream();

		assertEquals(ENTITY_CONTENT, result);
	}

	@Test
	public void testGetRequestBodyFromStreamWithDefaultCharset() throws IOException {
		httpEntity = new StringEntity(ENTITY_CONTENT, Charset.defaultCharset());
		apacheRequest.setHeader("Content-type", "text/plain");
		apacheRequest.setEntity(httpEntity);

		String result = new ApacheHttpRequest(null, apacheRequest).getRequestBodyFromStream();

		assertEquals(ENTITY_CONTENT, result);
	}
}
