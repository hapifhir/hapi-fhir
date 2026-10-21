package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
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
		CapabilityStatement cs = myServer.fhirRequest("/metadata?_pretty=true&_format=xml").get()
				.assertStatus(200)
				.assertContentType(Constants.CT_FHIR_XML_NEW)
				.getBodyAsResource(CapabilityStatement.class);

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

		myServer.fhirRequest("/api-docs").get().assertStatus(200);
	}


}
