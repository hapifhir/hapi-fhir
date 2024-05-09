package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaticCapabilityStatementInterceptorTest {

	@RegisterExtension
	@Order(0)
	protected RestfulServerExtension myRestfulServer = new RestfulServerExtension(FhirVersionEnum.R4);
	@RegisterExtension
	@Order(1)
	protected HashMapResourceProviderExtension<Patient> myResourceProvider = new HashMapResourceProviderExtension<>(myRestfulServer, Patient.class);

	@Test
	public void testCapabilityStatementResource() throws IOException {
		StaticCapabilityStatementInterceptor interceptor = new StaticCapabilityStatementInterceptor();
		interceptor.setCapabilityStatementResource("static-capabilitystatement.json");

		myRestfulServer.getRestfulServer().registerInterceptor(interceptor);
		try {

			CapabilityStatement cs = myRestfulServer.getFhirClient().capabilities().ofType(CapabilityStatement.class).execute();
			assertEquals("Help I'm a Bug", cs.getSoftware().getName());

		} finally {
			myRestfulServer.getRestfulServer().unregisterInterceptor(interceptor);
		}
	}


}
