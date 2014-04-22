package ca.uhn.fhir.rest.server;

import javax.servlet.ServletException;

import org.junit.Test;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.exceptions.ConfigurationException;

public class ServerInvalidDefinitionTest {

	@Test(expected=ConfigurationException.class)
	public void testNonInstantiableTypeForResourceProvider() throws ServletException {
		RestfulServer srv = new RestfulServer();
		srv.setResourceProviders(new NonInstantiableTypeForResourceProvider());
		srv.init();
	}
	
	/**
	 * Normal, should initialize properly
	 */
	@Test()
	public void testBaseline() throws ServletException {
		RestfulServer srv = new RestfulServer();
		srv.setResourceProviders(new InstantiableTypeForResourceProvider());
		srv.init();
	}
	
	
	private static class NonInstantiableTypeForResourceProvider implements IResourceProvider
	{

		@Override
		public Class<? extends IResource> getResourceType() {
			return BaseResource.class;
		}
		
		@SuppressWarnings("unused")
		@Read
		public BaseResource read(@IdParam IdDt theId) {
			return null;
		}
		
	}
	
	private static class InstantiableTypeForResourceProvider implements IResourceProvider
	{

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}
		
		@SuppressWarnings("unused")
		@Read
		public Patient read(@IdParam IdDt theId) {
			return null;
		}
		
	}
	
}
