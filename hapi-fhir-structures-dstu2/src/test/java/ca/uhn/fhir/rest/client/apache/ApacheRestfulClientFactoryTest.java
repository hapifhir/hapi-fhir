package ca.uhn.fhir.rest.client.apache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.BaseClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

public class ApacheRestfulClientFactoryTest {

	@Test
	public void testSetContext() {
		ApacheRestfulClientFactory factory = new ApacheRestfulClientFactory();
		factory.getServerValidationModeEnum();
		factory.setFhirContext(FhirContext.forDstu2());
		try {
			factory.setFhirContext(FhirContext.forDstu2());
			fail();
		} catch (IllegalStateException e) {
			assertEquals("java.lang.IllegalStateException: RestfulClientFactory instance is already associated with one FhirContext. RestfulClientFactory instances can not be shared.", e.toString());
		}
	}

	@Test
	public void testValidatateBase() {
		FhirContext ctx = FhirContext.forDstu2();
		ApacheRestfulClientFactory factory = new ApacheRestfulClientFactory();
		factory.setFhirContext(ctx);
		factory.setConnectTimeout(1);
		try {
			factory.validateServerBase("http://127.0.0.1:22225", factory.getHttpClient("http://foo"), (BaseClient) ctx.newRestfulGenericClient("http://foo"));
			fail();
		} catch (FhirClientConnectionException e) {
			assertEquals("Failed to retrieve the server metadata statement during client initialization. URL used was http://127.0.0.1:22225metadata", e.getMessage());
		}
	}
}
