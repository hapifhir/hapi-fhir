package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.List;

import javax.servlet.ServletException;

import org.hamcrest.core.StringContains;
import org.junit.Test;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;

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

	@Test
	public void testInvalidSpecialNameResourceProvider() {
		RestfulServer srv = new RestfulServer();
		srv.setResourceProviders(new InvalidSpecialParameterNameResourceProvider());
		
		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("_pretty"));
		}
	}
	
	@Test
	public void testReadMethodWithSearchParameters() {
		RestfulServer srv = new RestfulServer();
		srv.setResourceProviders(new ReadMethodWithSearchParamProvider());
		
		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
		}
	}

	@Test
	public void testSearchWithId() {
		RestfulServer srv = new RestfulServer();
		srv.setResourceProviders(new SearchWithIdParamProvider());
		
		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("compartment"));
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
	
	public static class ReadMethodWithSearchParamProvider implements IResourceProvider
	{

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}
		
		@SuppressWarnings("unused")
		@Read
		public Patient read(@IdParam IdDt theId, @RequiredParam(name="aaa") StringParam theParam) {
			return null;
		}
		
	}
	
	public static class SearchWithIdParamProvider implements IResourceProvider
	{

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}
		
		@SuppressWarnings("unused")
		@Search
		public List<Patient> read(@IdParam IdDt theId, @RequiredParam(name="aaa") StringParam theParam) {
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
	
	
	public static class InvalidSpecialParameterNameResourceProvider implements IResourceProvider
	{

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@SuppressWarnings("unused")
		@Search
		public List<Patient> search(@RequiredParam(name="_pretty") StringParam theParam) {
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
