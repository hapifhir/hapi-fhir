package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.servlet.ServletException;

import org.hamcrest.core.StringContains;
import org.hl7.fhir.dstu21.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;

public class ServerUsingOldTypesDstu21Test {

	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	
	@Test
	public void testReadProvider() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new ReadProvider());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
		}
	}
	@Test
	public void testOperationProvider() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setFhirContext(ourCtx);
		srv.setResourceProviders(new OperationProvider());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("Incorrect use of type"));
		}
	}
	
	public static class ReadProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient opTypeRetOldBundle(@IdParam String theId) {
			return null;
		}

	}

	public static class OperationProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Operation(name="foo")
		public Patient opTypeRetOldBundle(@OperationParam(name="foo") IntegerDt theId) {
			return null;
		}

	}

}
