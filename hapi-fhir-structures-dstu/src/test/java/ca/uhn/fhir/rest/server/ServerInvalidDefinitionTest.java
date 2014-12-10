package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.hamcrest.core.StringContains;
import org.junit.Test;

import ca.uhn.fhir.model.api.BaseResource;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;

public class ServerInvalidDefinitionTest {

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
	public void testMultipleResourceProviderForSameType() {
		RestfulServer srv = new RestfulServer();
		srv.setResourceProviders(new PatientResourceProvider1(), new PatientResourceProvider2());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("[Patient]"));
			assertThat(e.getCause().toString(), StringContains.containsString("PatientResourceProvider1]"));
			assertThat(e.getCause().toString(), StringContains.containsString("PatientResourceProvider2]"));
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

	@Test
	public void testProviderWithNonResourceType() {
		RestfulServer srv = new RestfulServer();
		srv.setResourceProviders(new ProviderWithNonResourceType());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("does not contain any valid HAPI-FHIR annotations"));
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

	private static class PrivateResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			return null;
		}

	}

	public static class ReadMethodWithSearchParamProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId, @RequiredParam(name = "aaa") StringParam theParam) {
			return null;
		}

	}

	public static class SearchWithIdParamProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> read(@IdParam IdDt theId, @RequiredParam(name = "aaa") StringParam theParam) {
			return null;
		}

	}
	
	
	public static class ProviderWithNonResourceType implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return new IResource() {
				
				@Override
				public boolean isEmpty() {
					return false;
				}
				
				@Override
				public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
					return null;
				}
				
				@Override
				public void setResourceMetadata(Map<ResourceMetadataKeyEnum<?>, Object> theMap) {
				}
				
				@Override
				public void setLanguage(CodeDt theLanguage) {
				}
				
				@Override
				public void setId(IdDt theId) {
				}
				
				@Override
				public NarrativeDt getText() {
					return null;
				}
				
				@Override
				public String getResourceName() {
					return null;
				}
				
				@Override
				public Map<ResourceMetadataKeyEnum<?>, Object> getResourceMetadata() {
					return null;
				}
				
				@Override
				public CodeDt getLanguage() {
					return null;
				}
				
				@Override
				public IdDt getId() {
					return null;
				}
				
				@Override
				public ContainedDt getContained() {
					return null;
				}
			}.getClass();
		}

		@Search
		public List<Patient> read(@IdParam IdDt theId, @RequiredParam(name = "aaa") StringParam theParam) {
			return null;
		}

	}

	public static class InvalidSpecialParameterNameResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> search(@RequiredParam(name = "_pretty") StringParam theParam) {
			return null;
		}

	}

	public static class InstantiableTypeForResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			return null;
		}

	}

	public static class PatientResourceProvider1 implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			return null;
		}

	}

	public static class PatientResourceProvider2 implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			return null;
		}

	}

}
