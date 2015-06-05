package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchParameter;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider;

public class ServerConformanceProviderDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerConformanceProviderDstu2Test.class);
	private FhirContext myCtx = new FhirContext();
	
	@Test
	public void testSearchParameterDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer();
		rs.setProviders(new SearchProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());
		
		boolean found=false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().iterator().next();
				assertEquals("The patient's identifier (MRN or other card number)", param.getDescription());
				found=true;
			}
		}
		assertTrue(found);
		Conformance conformance = sc.getServerConformance(createHttpServletRequest());

		String conf = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));
		
	}

	@Test
	public void testOperationDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer();
		rs.setProviders(new SearchProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());
		
		boolean found=false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().iterator().next();
				assertEquals("The patient's identifier (MRN or other card number)", param.getDescription());
				found=true;
			}
		}
		assertTrue(found);
		Conformance conformance = sc.getServerConformance(createHttpServletRequest());

		String conf = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));
		
	}

	@Test
	public void testExtendedOperationReturningBundle() throws Exception {

		RestfulServer rs = new RestfulServer();
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());
		
		Conformance conformance = sc.getServerConformance(createHttpServletRequest());

		String conf = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);
		
	}
	
	@Test
	public void testValidateGeneratedStatement() throws Exception {

		RestfulServer rs = new RestfulServer();
		rs.setProviders(new MultiOptionalProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());
		
		Conformance conformance = sc.getServerConformance(createHttpServletRequest());

		assertTrue(myCtx.newValidator().validateWithResult(conformance).isSuccessful());
	}

	
	
	@Test
	public void testMultiOptionalDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer();
		rs.setProviders(new MultiOptionalProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());
		
		boolean found=false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().iterator().next();
				assertEquals("The patient's identifier", param.getDescription());
				found=true;
			}
		}

		assertTrue(found);
		Conformance conformance = sc.getServerConformance(createHttpServletRequest());
		String conf = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier\"/>"));
		assertThat(conf, containsString("<documentation value=\"The patient's name\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));
	}

	@Test
	public void testProviderWithRequiredAndOptional() throws Exception {

		RestfulServer rs = new RestfulServer();
		rs.setProviders(new ProviderWithRequiredAndOptional());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());
		
		Conformance conformance = sc.getServerConformance(createHttpServletRequest());
		String conf = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		Rest rest = conformance.getRestFirstRep();
		RestResource res = rest.getResourceFirstRep();
		assertEquals("DiagnosticReport", res.getType());
		
		assertEquals(DiagnosticReport.SP_SUBJECT , res.getSearchParam().get(0).getName());
		assertEquals("identifier", res.getSearchParam().get(0).getChain().get(0).getValue());
		
		assertEquals(DiagnosticReport.SP_NAME, res.getSearchParam().get(2).getName());
		
		assertEquals(DiagnosticReport.SP_DATE, res.getSearchParam().get(1).getName());
				
		assertEquals(1,res.getSearchInclude().size());
		assertEquals("DiagnosticReport.result", res.getSearchIncludeFirstRep().getValue());
	}
	
	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class SearchProvider {

		@Search(type = Patient.class)
		public Patient findPatient(
				@Description(shortDefinition = "The patient's identifier (MRN or other card number)") 
				@RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
			return null;
		}

	}
	
	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class MultiOptionalProvider {

		@Search(type = Patient.class)
		public Patient findPatient(
				@Description(shortDefinition = "The patient's identifier") 
				@OptionalParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier,
				@Description(shortDefinition = "The patient's name") 
				@OptionalParam(name=Patient.SP_NAME) StringDt theName) {
			return null;
		}

	}
	
	
	public static class ProviderWithRequiredAndOptional {
		
		@Description(shortDefinition="This is a search for stuff!")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatient (
				@RequiredParam(name=DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) IdentifierDt thePatientId, 
				@OptionalParam(name=DiagnosticReport.SP_NAME) TokenOrListParam theNames,
				@OptionalParam(name=DiagnosticReport.SP_DATE) DateRangeParam theDateRange,
				@IncludeParam(allow= {"DiagnosticReport.result"}) Set<Include> theIncludes
				) throws Exception {
			return null;
		}

	}
	
	public static class ProviderWithExtendedOperationReturningBundle implements IResourceProvider {
		
		@Operation(name="everything", idempotent=true)
		public ca.uhn.fhir.rest.server.IBundleProvider everything(
				javax.servlet.http.HttpServletRequest theServletRequest, 
				@IdParam ca.uhn.fhir.model.primitive.IdDt theId,
				@OperationParam(name="start") DateDt theStart,
				@OperationParam(name="end") DateDt theEnd) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

	private HttpServletRequest createHttpServletRequest() {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getRequestURI()).thenReturn("/FhirStorm/fhir/Patient/_search");
		when(req.getServletPath()).thenReturn("/fhir");
		when(req.getRequestURL()).thenReturn(new StringBuffer().append("http://fhirstorm.dyndns.org:8080/FhirStorm/fhir/Patient/_search"));
		when(req.getContextPath()).thenReturn("/FhirStorm");
		return req;
	}

	private ServletConfig createServletConfig() {
		ServletConfig sc = mock(ServletConfig.class);
		when (sc.getServletContext()).thenReturn(null);
		return sc;
	}

}
