package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ServerDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerDstu3Test.class);

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myRestServer.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof OpenApiInterceptor);
	}


	/**
	 * See #519
	 */
	@Test
	public void saveIdParamOnlyAppearsOnce() {
		HttpTestResponse resp = myServer.fhirRequest("/metadata?_pretty=true&_format=xml").get();
		ourLog.info(resp.toString());
		resp.assertStatus(200);

		String respString = resp.getBody();
		ourLog.debug(respString);

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
	}


	@Test
	public void testFetchOpenApi() {
		myRestServer.registerInterceptor(new OpenApiInterceptor());

		HttpTestResponse response = myServer.fhirRequest("/api-docs").get();
		ourLog.info(response.getBody());

		response.assertStatus(200);
	}


}
