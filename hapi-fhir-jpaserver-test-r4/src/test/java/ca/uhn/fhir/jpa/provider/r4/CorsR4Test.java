package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import org.junit.jupiter.api.Test;

public class CorsR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CorsR4Test.class);

	@Test
	public void saveLocalOrigin() {
		HttpTestResponse resp = myServer.fhirRequest("/Patient?name=test")
			.withHeader(Constants.HEADER_CORS_ORIGIN, "file://")
			.get();

		ourLog.info(resp.toString());

		resp.assertStatus(200);
	}



}
