package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.dstu2.hapi.rest.server.ServerConformanceProvider;
import org.hl7.fhir.dstu2.model.*;
import org.hl7.fhir.dstu2.model.Conformance.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchParameter;

public class ServerConformanceProviderHl7OrgDstu2Test {

  static {
    System.setProperty("test", "true");
  }

  private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerConformanceProviderHl7OrgDstu2Test.class);

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
		when(sc.getServletContext()).thenReturn(null);
		return sc;
	}

	@Test
	public void testConditionalOperations() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ConditionalProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		ConformanceRestResourceComponent res = conformance.getRest().get(0).getResource().get(1);
		assertEquals("Patient", res.getType());
		
		assertTrue(res.getConditionalCreate());
		assertEquals(ConditionalDeleteStatus.SINGLE, res.getConditionalDelete());
		assertTrue(res.getConditionalUpdate());
	}
	
	@Test
	public void testExtendedOperationReturningBundle() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertEquals(1, conformance.getRest().get(0).getOperation().size());
		assertEquals("$everything", conformance.getRest().get(0).getOperation().get(0).getName());
		assertEquals("OperationDefinition/Patient-i-everything", conformance.getRest().get(0).getOperation().get(0).getDefinition().getReference());
	}

	
	@Test
	public void testExtendedOperationReturningBundleOperation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-everything"), createRequestDetails(rs));
				
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(opDef);
		ourLog.info(conf);

		assertEquals("$everything", opDef.getCode());
		assertEquals(true, opDef.getIdempotent());
	}

	@Test
	public void testInstanceHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new InstanceHistoryProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf, containsString("<interaction><code value=\"" + TypeRestfulInteraction.HISTORYINSTANCE.toCode() + "\"/></interaction>"));
	}

	@Test
	public void testMultiOptionalDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiOptionalProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().iterator().next();
				assertEquals("The patient's identifier", param.getDescription());
				found = true;
			}
		}

		assertTrue(found);
		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier\"/>"));
		assertThat(conf, containsString("<documentation value=\"The patient's name\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));
	}

	@Test
	public void testNonConditionalOperations() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new NonConditionalProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		ConformanceRestResourceComponent res = conformance.getRest().get(0).getResource().get(1);
		assertEquals("Patient", res.getType());
		
		assertNull(res.getConditionalCreateElement().getValue());
		assertNull(res.getConditionalDeleteElement().getValue());
		assertNull(res.getConditionalUpdateElement().getValue());
	}

	@Test
	public void testOperationDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SearchProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().iterator().next();
				assertEquals("The patient's identifier (MRN or other card number)", param.getDescription());
				found = true;
			}
		}
		assertTrue(found);
		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));

	}

	@Test
	public void testOperationOnNoTypes() throws Exception {
		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new PlainProviderWithExtendedOperationOnNoType());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs) {
			@Override
			public Conformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
				return super.getServerConformance(theRequest, theRequestDetails);
			}
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance sconf = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		assertEquals("OperationDefinition/-is-plain", sconf.getRest().get(0).getOperation().get(0).getDefinition().getReference());

		OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/-is-plain"), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(opDef);
		ourLog.info(conf);

		assertEquals("$plain", opDef.getCode());
		assertEquals(true, opDef.getIdempotent());
		assertEquals(3, opDef.getParameter().size());
		assertEquals("start", opDef.getParameter().get(0).getName());
		assertEquals("in", opDef.getParameter().get(0).getUse().toCode());
		assertEquals("0", opDef.getParameter().get(0).getMinElement().getValueAsString());
		assertEquals("date", opDef.getParameter().get(0).getTypeElement().getValueAsString());

		assertEquals("out1", opDef.getParameter().get(2).getName());
		assertEquals("out", opDef.getParameter().get(2).getUse().toCode());
		assertEquals("1", opDef.getParameter().get(2).getMinElement().getValueAsString());
		assertEquals("2", opDef.getParameter().get(2).getMaxElement().getValueAsString());
		assertEquals("string", opDef.getParameter().get(2).getTypeElement().getValueAsString());
}

	@Test
	public void testProviderWithRequiredAndOptional() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithRequiredAndOptional());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		ConformanceRestComponent rest = conformance.getRest().get(0);
		ConformanceRestResourceComponent res = rest.getResource().get(0);
		assertEquals("DiagnosticReport", res.getType());

		assertEquals(DiagnosticReport.SP_SUBJECT, res.getSearchParam().get(0).getName());
		assertEquals("identifier", res.getSearchParam().get(0).getChain().get(0).getValue());

		assertEquals(DiagnosticReport.SP_CODE, res.getSearchParam().get(1).getName());

		assertEquals(DiagnosticReport.SP_DATE, res.getSearchParam().get(2).getName());

		assertEquals(1, res.getSearchInclude().size());
		assertEquals("DiagnosticReport.result", res.getSearchInclude().get(0).getValue());
	}

	@Test
	public void testReadAndVReadSupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new VreadProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf, containsString("<interaction><code value=\"vread\"/></interaction>"));
		assertThat(conf, containsString("<interaction><code value=\"read\"/></interaction>"));
	}

	@Test
	public void testReadSupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ReadProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf, not(containsString("<interaction><code value=\"vread\"/></interaction>")));
		assertThat(conf, containsString("<interaction><code value=\"read\"/></interaction>"));
	}

	@Test
	public void testSearchParameterDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SearchProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().iterator().next();
				assertEquals("The patient's identifier (MRN or other card number)", param.getDescription());
				found = true;
			}
		}
		assertTrue(found);
		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);
		
		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));

	}

	@Test
	public void testSystemHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SystemHistoryProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf, containsString("<interaction><code value=\"" + SystemRestfulInteraction.HISTORYSYSTEM.toCode() + "\"/></interaction>"));
	}

	@Test
	public void testTypeHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new TypeHistoryProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf, containsString("<interaction><code value=\"" + TypeRestfulInteraction.HISTORYTYPE.toCode() + "\"/></interaction>"));
	}

	@Test
	public void testValidateGeneratedStatement() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiOptionalProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		assertTrue(ourCtx.newValidator().validateWithResult(conformance).isSuccessful());
	}

	public static class ConditionalProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient, @ConditionalUrlParam String theConditionalUrl) {
			return null;
		}

		@Delete
		public MethodOutcome delete(@IdParam IdType theId, @ConditionalUrlParam String theConditionalUrl) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient thePatient, @ConditionalUrlParam String theConditionalUrl) {
			return null;
		}

	}

	public static class InstanceHistoryProvider implements IResourceProvider {
		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@History
		public List<IBaseResource> history(@IdParam IdType theId) {
			return null;
		}

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class MultiOptionalProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier") @OptionalParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier, @Description(shortDefinition = "The patient's name") @OptionalParam(name = Patient.SP_NAME) StringParam theName) {
			return null;
		}

	}

	public static class NonConditionalProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			return null;
		}

		@Delete
		public MethodOutcome delete(@IdParam IdType theId) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient thePatient) {
			return null;
		}

	}

	public static class PlainProviderWithExtendedOperationOnNoType {

		@Operation(name = "plain", idempotent = true, returnParameters= {
			@OperationParam(min=1, max=2, name="out1", type=StringType.class)
		})
		public IBundleProvider everything(javax.servlet.http.HttpServletRequest theServletRequest, @IdParam IdType theId, @OperationParam(name = "start") DateType theStart, @OperationParam(name = "end") DateType theEnd) {
			return null;
		}

	}

	public static class ProviderWithExtendedOperationReturningBundle implements IResourceProvider {

		@Operation(name = "everything", idempotent = true)
		public IBundleProvider everything(javax.servlet.http.HttpServletRequest theServletRequest, @IdParam IdType theId, @OperationParam(name = "start") DateType theStart, @OperationParam(name = "end") DateType theEnd) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

	public static class ProviderWithRequiredAndOptional {

		@Description(shortDefinition = "This is a search for stuff!")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatient(@RequiredParam(name = DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) TokenParam thePatientId, @OptionalParam(name = DiagnosticReport.SP_CODE) TokenOrListParam theNames,
				@OptionalParam(name = DiagnosticReport.SP_DATE) DateRangeParam theDateRange, @IncludeParam(allow = { "DiagnosticReport.result" }) Set<Include> theIncludes) throws Exception {
			return null;
		}

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class ReadProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier (MRN or other card number)") @RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			return null;
		}

		@Read(version = false)
		public Patient readPatient(@IdParam IdType theId) {
			return null;
		}

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class SearchProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier (MRN or other card number)") @RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			return null;
		}

	}

	public static class SystemHistoryProvider {

		@History
		public List<IBaseResource> history() {
			return null;
		}

	}

	public static class TypeHistoryProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@History
		public List<IBaseResource> history() {
			return null;
		}

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class VreadProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier (MRN or other card number)") @RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			return null;
		}

		@Read(version = true)
		public Patient readPatient(@IdParam IdType theId) {
			return null;
		}

	}

  private RequestDetails createRequestDetails(RestfulServer theServer) {
    ServletRequestDetails retVal = new ServletRequestDetails(null);
    retVal.setServer(theServer);
    return retVal;
  }

}
