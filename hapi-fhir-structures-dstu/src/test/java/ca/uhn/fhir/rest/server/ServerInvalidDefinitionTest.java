package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.List;

import javax.servlet.ServletException;

import org.hamcrest.core.StringContains;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.ResourceMetadataMap;
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
import ca.uhn.fhir.util.TestUtil;

public class ServerInvalidDefinitionTest {
	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	/**
	 * Normal, should initialize properly
	 */
	@Test()
	public void testBaseline() throws ServletException {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setResourceProviders(new InstantiableTypeForResourceProvider());
		srv.init();
	}

	@Test
	public void testInvalidSpecialNameResourceProvider() {
		RestfulServer srv = new RestfulServer(ourCtx);
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
	public void testMultipleResourceProviderForSameType() {
		RestfulServer srv = new RestfulServer(ourCtx);
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
	public void testPrivateResourceProvider() {
		RestfulServer srv = new RestfulServer(ourCtx);
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
	public void testProviderWithNonResourceType() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setResourceProviders(new ProviderWithNonResourceType());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("does not contain any valid HAPI-FHIR annotations"));
		}
	}

	@Test
	public void testReadMethodWithoutIdParamProvider() {
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setResourceProviders(new ReadMethodWithoutIdParamProvider());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("does not have a parameter"));
		}
	}

	@Test
	public void testReadMethodWithSearchParameters() {
		RestfulServer srv = new RestfulServer(ourCtx);
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
		RestfulServer srv = new RestfulServer(ourCtx);
		srv.setResourceProviders(new SearchWithIdParamProvider());

		try {
			srv.init();
			fail();
		} catch (ServletException e) {
			assertThat(e.getCause().toString(), StringContains.containsString("ConfigurationException"));
			assertThat(e.getCause().toString(), StringContains.containsString("compartment"));
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

	public static class ProviderWithNonResourceType implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return new IResource() {

				@Override
				public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
					return null;
				}

				@Override
				public IIdType getIdElement() {
					return getId();
				}

				@Override
				public ContainedDt getContained() {
					return null;
				}

				@Override
				public IdDt getId() {
					return null;
				}

				@Override
				public CodeDt getLanguage() {
					return null;
				}

				@Override
				public ResourceMetadataMap getResourceMetadata() {
					return null;
				}

				@Override
				public String getResourceName() {
					return null;
				}

				@Override
				public FhirVersionEnum getStructureFhirVersionEnum() {
					return FhirVersionEnum.DSTU1;
				}

				@Override
				public NarrativeDt getText() {
					return null;
				}

				@Override
				public boolean isEmpty() {
					return false;
				}

				@Override
				public void setId(IdDt theId) {
				}

				@Override
				public void setLanguage(CodeDt theLanguage) {
				}

				@Override
				public void setResourceMetadata(ResourceMetadataMap theMap) {
				}

				@Override
				public IBaseResource setId(String theId) {
					return null;
				}

				@Override
				public IBaseResource setId(IIdType theId) {
					return null;
				}

				@Override
				public boolean hasFormatComment() {
					return false;
				}

				@Override
				public List<String> getFormatCommentsPre() {
					return null;
				}

				@Override
				public List<String> getFormatCommentsPost() {
					return null;
				}

				@Override
				public IBaseMetaType getMeta() {
					// TODO Auto-generated method stub
					return null;
				}
			}.getClass();
		}

		@Search
		public List<Patient> read(@IdParam IdDt theId, @RequiredParam(name = "aaa") StringParam theParam) {
			return null;
		}

	}

	public static class ReadMethodWithoutIdParamProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read() {
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


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
