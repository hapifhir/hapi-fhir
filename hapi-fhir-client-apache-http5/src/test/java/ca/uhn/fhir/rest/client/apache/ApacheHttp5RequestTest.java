package ca.uhn.fhir.rest.client.apache;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApacheHttp5RequestTest {

	private final String ENTITY_CONTENT = "Some entity with special characters: é";
	private StringEntity entity;
	private final HttpPost request = new HttpPost("");

	@Test
	public void testGetRequestBodyFromStream() throws IOException {
		entity = new StringEntity(ENTITY_CONTENT, StandardCharsets.ISO_8859_1);
		request.setHeader("Content-type", "text/plain; charset=ISO-8859-1");
		request.setEntity(entity);

		String result = new ApacheHttp5Request(null, request).getRequestBodyFromStream();

		assertEquals(ENTITY_CONTENT, result);
	}

	@Test
	public void testGetRequestBodyFromStreamWithDefaultCharset() throws IOException {
		entity = new StringEntity(ENTITY_CONTENT, Charset.defaultCharset());
		request.setHeader("Content-type", "text/plain");
		request.setEntity(entity);

		String result = new ApacheHttp5Request(null, request).getRequestBodyFromStream();

		assertEquals(ENTITY_CONTENT, result);
	}
}
