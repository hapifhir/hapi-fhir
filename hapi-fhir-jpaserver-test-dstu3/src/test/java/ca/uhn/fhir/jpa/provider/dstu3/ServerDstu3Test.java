package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ServerDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerDstu3Test.class);

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		ourRestServer.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof OpenApiInterceptor);
	}


	/**
	 * See #519
	 */
	@Test
	public void saveIdParamOnlyAppearsOnce() throws IOException {
		HttpGet get = new HttpGet(ourServerBase + "/metadata?_pretty=true&_format=xml");
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			ourLog.info(resp.toString());
			assertEquals(200, resp.getStatusLine().getStatusCode());

			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(respString);

			CapabilityStatement cs = myFhirContext.newXmlParser().parseResource(CapabilityStatement.class, respString);

			for (CapabilityStatementRestResourceComponent nextResource : cs.getRest().get(0).getResource()) {
				ourLog.info("Testing resource: " + nextResource.getType());
				Set<String> sps = new HashSet<String>();
				for (CapabilityStatementRestResourceSearchParamComponent nextSp : nextResource.getSearchParam()) {
					if (sps.add(nextSp.getName()) == false) {
						fail("Duplicate search parameter " + nextSp.getName() + " for resource " + nextResource.getType());
					}
				}

				if (!sps.contains("_id")) {
					fail("No search parameter _id for resource " + nextResource.getType());
				}
			}
		} finally {
			IOUtils.closeQuietly(resp.getEntity().getContent());
		}
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
