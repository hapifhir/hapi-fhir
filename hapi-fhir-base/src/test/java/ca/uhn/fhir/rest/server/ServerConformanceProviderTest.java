package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestQuery;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.SearchParameter;
import ca.uhn.fhir.rest.param.CodingListParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.provider.ServerConformanceProvider;

public class ServerConformanceProviderTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerConformanceProviderTest.class);

	@Test
	public void testSearchParameterDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer();
		rs.setProviders(new SearchProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(null);
		
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
		Conformance conformance = sc.getServerConformance();
		String conf = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));
	}

	
	@Test
	public void testMultiOptionalDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer();
		rs.setProviders(new MultiOptionalProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(null);
		
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
		Conformance conformance = sc.getServerConformance();
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

		rs.init(null);
		
		Conformance conformance = sc.getServerConformance();
		String conf = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		Rest rest = conformance.getRestFirstRep();
		RestResource res = rest.getResourceFirstRep();
		assertEquals("DiagnosticReport", res.getType().getValueAsString());
		
		RestQuery p0 = rest.getQueryFirstRep();
		assertEquals("subject.identifier", p0.getParameterFirstRep().getName().getValue());
		
		assertEquals(1,res.getSearchInclude().size());
		assertEquals("DiagnosticReport.result", res.getSearchIncludeFirstRep().getValue());
	}
	
	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	@SuppressWarnings("unused")
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
	@SuppressWarnings("unused")
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
				@OptionalParam(name=DiagnosticReport.SP_NAME) CodingListParam theNames,
				@OptionalParam(name=DiagnosticReport.SP_DATE) DateRangeParam theDateRange,
				@IncludeParam(allow= {"DiagnosticReport.result"}) Set<Include> theIncludes
				) throws Exception {
			return null;
		}

		
	}
	

}
