package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class OpenApiInterceptorJpaTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OpenApiInterceptorJpaTest.class);

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myServer.getRestfulServer().getInterceptorService().unregisterInterceptorsIf(t -> t instanceof OpenApiInterceptor);
	}

	@Test
	public void testFetchOpenApi() {
		myServer.getRestfulServer().registerInterceptor(new OpenApiInterceptor());

		ourLog.info(myServer.fhirRequest("/metadata?_format=json&_pretty=true").get().assertStatus(200).getBody());

		ourLog.info(myServer.fhirRequest("/api-docs").get().assertStatus(200).getBody());
	}


}
