package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import io.swagger.v3.core.util.Yaml;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class OpenApiInterceptorJpaTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OpenApiInterceptorJpaTest.class);

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		ourRestServer.getInterceptorService().unregisterInterceptorsIf(t->t instanceof OpenApiInterceptor);
	}

	@Test
	public void testFetchOpenApi() throws IOException {
		ourRestServer.registerInterceptor(new OpenApiInterceptor());

		HttpGet get = new HttpGet(ourServerBase + "/api-docs");
		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String string = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(string);

			assertEquals(200, response.getStatusLine().getStatusCode());
		}
	}


}
