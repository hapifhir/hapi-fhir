package ca.uhn.fhir.cr.r4;

import ca.uhn.fhir.cr.BaseCrR4Test;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import io.specto.hoverfly.junit.core.HoverflyMode;
import io.specto.hoverfly.junit5.HoverflyExtension;
import io.specto.hoverfly.junit5.api.HoverflyConfig;
import io.specto.hoverfly.junit5.api.HoverflyCore;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static javolution.testing.TestContext.assertEquals;

@ExtendWith(SpringExtension.class)
@ExtendWith(HoverflyExtension.class)
@HoverflyCore(mode = HoverflyMode.SIMULATE, config = @HoverflyConfig(adminPort = 9000, proxyPort = 9001))
class CareGapsOperationProviderIT extends BaseCrR4Test {

	protected IGenericClient myClient;

	@BeforeEach
	void beforeEach() {
		myClient = getFhirContext().newRestfulGenericClient(TEST_ADDRESS);
	}
	@Test
	public void operationProviderWasRegistered(){
		/*myClient.transaction().withBundle(IBaseBundle).execute();
		myClient.operation().onInstance("measureID").named("SubmitData").withParameters(IBaseParameters).execute();*/

		myClient.registerInterceptor(new BasicAuthInterceptor("admin", "password"));
		CapabilityStatement capabilityStatement = myClient.capabilities().ofType(CapabilityStatement.class).execute();
		assertEquals("Smile CDR", capabilityStatement.getSoftware().getName());
		/*
		assertEquals("Smile CDR", cs.getSoftware().getName());
		assertEquals("FHIR Endpoint powered by Smile CDR", cs.getImplementation().getDescription());
		assertEquals("http://foo/bar", cs.getImplementation().getUrl());
		 */
	}

}
