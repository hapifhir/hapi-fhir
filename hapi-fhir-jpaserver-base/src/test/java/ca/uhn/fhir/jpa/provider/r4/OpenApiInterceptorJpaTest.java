package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenApiInterceptorJpaTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OpenApiInterceptorJpaTest.class);

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		ourRestServer.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof OpenApiInterceptor);
	}

	@Test
	public void testFetchOpenApi() throws IOException {
		ourRestServer.registerInterceptor(new OpenApiInterceptor());

		HttpGet get = new HttpGet(ourServerBase + "/metadata?_format=json&_pretty=true");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String string = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(string);

			assertEquals(200, response.getStatusLine().getStatusCode());
		}

		get = new HttpGet(ourServerBase + "/api-docs");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String string = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(string);

			assertEquals(200, response.getStatusLine().getStatusCode());
		}
	}


}
