package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import javax.servlet.ServletException;

import org.hamcrest.core.StringContains;
import org.junit.Test;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;

public class ServerInvalidDefinitionTest {

	@Test
	public void testNonInstantiableTypeForResourceProvider() {
		RestfulServer srv = new RestfulServer();
		srv.setResourceProviders(new NonInstantiableTypeForResourceProvider());
		
		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
		}
	}
	
	@Test
	public void testPrivateResourceProvider() {
		RestfulServer srv = new RestfulServer();
		srv.setResourceProviders(new PrivateResourceProvider());
		
		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("public"));
		}
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
	
	private static class PrivateResourceProvider implements IResourceProvider
	{

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}
		
		@SuppressWarnings("unused")
		@Read
		public Patient read(@IdParam IdDt theId) {
			return null;
		}
		
	}
	
	public static class NonInstantiableTypeForResourceProvider implements IResourceProvider
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
	
	public static class InstantiableTypeForResourceProvider implements IResourceProvider
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
