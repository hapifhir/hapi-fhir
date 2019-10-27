package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.IParameter;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchParameter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.rest.server.ServerCapabilityStatementProvider;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerConformanceProviderR4Test {

	private static FhirContext ourCtx;
	private static FhirValidator ourValidator;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerConformanceProviderR4Test.class);

	static {
		ourCtx = FhirContext.forR4();
		ourValidator = ourCtx.newValidator();
		ourValidator.setValidateAgainstStandardSchema(true);
		ourValidator.setValidateAgainstStandardSchematron(true);
	}

	private void validate(OperationDefinition theOpDef) {
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(theOpDef);
		ourLog.info("Def: {}", conf);

		ValidationResult result = ourValidator.validateWithResult(theOpDef);
		String outcome = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info("Outcome: {}", outcome);

		assertTrue(outcome, result.isSuccessful());
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
		when(sc.getServletContext()).thenReturn(null);
		return sc;
	}

	private CapabilityStatement.CapabilityStatementRestResourceComponent findRestResource(CapabilityStatement capabilityStatement, String wantResource) throws Exception {
		CapabilityStatement.CapabilityStatementRestResourceComponent resource = null;
		for (CapabilityStatement.CapabilityStatementRestResourceComponent next : capabilityStatement.getRest().get(0).getResource()) {
			if (next.getType().equals(wantResource)) {
				resource = next;
			}
		}
		if (resource == null) {
			throw new Exception("Could not find resource: " + wantResource);
		}
		return resource;
	}

	@Test
	public void testConditionalOperations() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ConditionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		CapabilityStatement.CapabilityStatementRestResourceComponent res = capabilityStatement.getRest().get(0).getResource().get(1);
		assertEquals("Patient", res.getType());

		assertTrue(res.getConditionalCreate());
		assertEquals(CapabilityStatement.ConditionalDeleteStatus.MULTIPLE, res.getConditionalDeleteElement().getValue());
		assertTrue(res.getConditionalUpdate());
	}

	@Test
	public void testExtendedOperationReturningBundle() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		assertEquals(1, capabilityStatement.getRest().get(0).getOperation().size());
		assertEquals("everything", capabilityStatement.getRest().get(0).getOperation().get(0).getName());
		assertEquals("OperationDefinition/Patient-i-everything", capabilityStatement.getRest().get(0).getOperation().get(0).getDefinition());
	}

	@Test
	public void testExtendedOperationReturningBundleOperation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs) {
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-everything"), createRequestDetails(rs));
		validate(opDef);

		assertEquals("everything", opDef.getCode());
		assertEquals(false, opDef.getAffectsState());
	}

	@Test
	public void testInstanceHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new InstanceHistoryProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(capabilityStatement);
		assertThat(conf, containsString("<interaction><code value=\"" + CapabilityStatement.TypeRestfulInteraction.HISTORYINSTANCE.toCode() + "\"/></interaction>"));
	}


	@Test
	public void testMultiOptionalDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiOptionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
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
		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier\"/>"));
		assertThat(conf, containsString("<documentation value=\"The patient's name\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));
	}

	@Test
	public void testNonConditionalOperations() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new NonConditionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		CapabilityStatement.CapabilityStatementRestResourceComponent res = capabilityStatement.getRest().get(0).getResource().get(1);
		assertEquals("Patient", res.getType());

		assertFalse(res.getConditionalCreate());
		assertEquals(null, res.getConditionalDelete());
		assertFalse(res.getConditionalUpdate());
	}

	/** See #379 */
	@Test
	public void testOperationAcrossMultipleTypes() throws Exception {
		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiTypePatientProvider(), new MultiTypeEncounterProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		assertEquals(4, capabilityStatement.getRest().get(0).getOperation().size());
		List<String> operationNames = toOperationNames(capabilityStatement.getRest().get(0).getOperation());
		assertThat(operationNames, containsInAnyOrder("someOp", "validate", "someOp", "validate"));

		List<String> operationIdParts = toOperationIdParts(capabilityStatement.getRest().get(0).getOperation());

		{
			OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-someOp"), createRequestDetails(rs));
			validate(opDef);

			assertEquals("someOp", opDef.getCode());
			assertEquals(true, opDef.getInstance());
			assertEquals(false, opDef.getSystem());
			assertEquals(2, opDef.getParameter().size());
			assertEquals("someOpParam1", opDef.getParameter().get(0).getName());
			assertEquals("string", opDef.getParameter().get(0).getType());
			assertEquals("someOpParam2", opDef.getParameter().get(1).getName());
			assertEquals("Patient", opDef.getParameter().get(1).getType());
		}
		{
			OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Encounter-i-someOp"), createRequestDetails(rs));
			validate(opDef);

			assertEquals("someOp", opDef.getCode());
			assertEquals(true, opDef.getInstance());
			assertEquals(false, opDef.getSystem());
			assertEquals(2, opDef.getParameter().size());
			assertEquals("someOpParam1", opDef.getParameter().get(0).getName());
			assertEquals("string", opDef.getParameter().get(0).getType());
			assertEquals("someOpParam2", opDef.getParameter().get(1).getName());
			assertEquals("Encounter", opDef.getParameter().get(1).getType());
		}
		{
			OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-validate"), createRequestDetails(rs));
			validate(opDef);

			assertEquals("validate", opDef.getCode());
			assertEquals(true, opDef.getInstance());
			assertEquals(false, opDef.getSystem());
			assertEquals(1, opDef.getParameter().size());
			assertEquals("resource", opDef.getParameter().get(0).getName());
			assertEquals("Patient", opDef.getParameter().get(0).getType());
		}
	}

	@Test
	public void testOperationDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setServerName("MY NAME");
		rs.setServerVersion("MY VERSION");
		rs.setProviders(new SearchProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info("AAAAAA" + conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));
		assertEquals("MY NAME", capabilityStatement.getSoftware().getName());
		assertEquals("MY VERSION", capabilityStatement.getSoftware().getVersion());

	}

	@Test
	public void testOperationOnNoTypes() throws Exception {
		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new PlainProviderWithExtendedOperationOnNoType());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs) {
			@Override
			public CapabilityStatement getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
				return super.getServerConformance(theRequest, theRequestDetails);
			}
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement sconf = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/-is-plain"), createRequestDetails(rs));
		validate(opDef);

		assertEquals("plain", opDef.getCode());
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
	public void testProviderForSmart() throws ServletException {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.createConfiguration();
		rs.setProviders(new ProviderWithRequiredAndOptional());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider() {
			@Override
			public CapabilityStatement getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
				CapabilityStatement capabilityStatement = super.getServerConformance(theRequest, theRequestDetails);
				 Extension extension = new Extension();
				 Extension extensionDtToken = new Extension();
				 Extension extensionDtAuthorize = new Extension();
				CapabilityStatement.CapabilityStatementRestComponent rest = capabilityStatement.getRestFirstRep();
				CapabilityStatement.CapabilityStatementRestSecurityComponent restSecurity = rest.getSecurity();

				 extension.setUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");
				 extensionDtToken.setUrl("token");
				 extensionDtToken.setValue(new UriType("https://SERVERNAME/token"));
				 extensionDtAuthorize.setUrl("authorize");
				 extensionDtAuthorize.setValue(new UriType("https://SERVERNAME/authorize"));
				 extension.addExtension(extensionDtToken);
				extension.addExtension(extensionDtAuthorize);
				restSecurity.addExtension(extension);

				return capabilityStatement;
			}
		};

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		CapabilityStatement parsed = ourCtx.newJsonParser().parseResource(CapabilityStatement.class, conf);
	}

	@Test
	public void testProviderWithRequiredAndOptional() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithRequiredAndOptional());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		CapabilityStatement.CapabilityStatementRestComponent rest = capabilityStatement.getRestFirstRep();
		CapabilityStatement.CapabilityStatementRestResourceComponent res = rest.getResourceFirstRep();
		assertEquals("DiagnosticReport", res.getType());

		assertEquals(DiagnosticReport.SP_SUBJECT, res.getSearchParam().get(0).getName());

		assertEquals(DiagnosticReport.SP_CODE, res.getSearchParam().get(1).getName());

		assertEquals(DiagnosticReport.SP_DATE, res.getSearchParam().get(2).getName());

		assertEquals(1, res.getSearchInclude().size());
	}

	@Test
	public void testReadAndVReadSupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new VreadProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(capabilityStatement);
		assertThat(conf, containsString("<interaction><code value=\"vread\"/></interaction>"));
		assertThat(conf, containsString("<interaction><code value=\"read\"/></interaction>"));
	}

	@Test
	public void testReadSupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ReadProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(capabilityStatement);
		assertThat(conf, not(containsString("<interaction><code value=\"vread\"/></interaction>")));
		assertThat(conf, containsString("<interaction><code value=\"read\"/></interaction>"));
	}

	@Test
	public void testSearchParameterDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SearchProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				for (IParameter next : binding.getParameters()) {
					SearchParameter param = (SearchParameter) next;
					if (param.getDescription().contains("The patient's identifier (MRN or other card number")) {
						found = true;
					}
				}
				found = true;
			}
		}
		assertTrue(found);
		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));

	}

	/**
	 * See #286
	 */
	@Test
	public void testSearchReferenceParameterDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new PatientResourceProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().get(25);
				assertEquals("The organization at which this person is a patient", param.getDescription());
				found = true;
			}
		}
		assertTrue(found);
		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

	}


	/**
	 * See #286
	 */
	@Test
	public void testSearchReferenceParameterWithWhitelistDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SearchProviderWithWhitelist());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().get(0);
				assertEquals("The organization at which this person is a patient", param.getDescription());
				found = true;
			}
		}
		assertTrue(found);
		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		CapabilityStatement.CapabilityStatementRestResourceComponent resource = findRestResource(capabilityStatement, "Patient");

	}

	@Test
	public void testSearchReferenceParameterWithExplicitChainsDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SearchProviderWithExplicitChains());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().get(0);
				assertEquals("The organization at which this person is a patient", param.getDescription());
				found = true;
			}
		}
		assertTrue(found);
		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		CapabilityStatement.CapabilityStatementRestResourceComponent resource = findRestResource(capabilityStatement, "Patient");

		assertEquals(3, resource.getSearchParam().size());
		CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent param = resource.getSearchParam().get(0);
		assertEquals("organization", param.getName());
	}

	@Test
	public void testSystemHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SystemHistoryProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(capabilityStatement);
		assertThat(conf, containsString("<interaction><code value=\"" + CapabilityStatement.SystemRestfulInteraction.HISTORYSYSTEM.toCode() + "\"/></interaction>"));
	}

	@Test
	public void testTypeHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new TypeHistoryProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(capabilityStatement);
		assertThat(conf, containsString("<interaction><code value=\"" + CapabilityStatement.TypeRestfulInteraction.HISTORYTYPE.toCode() + "\"/></interaction>"));
	}

	@Test
	public void testValidateGeneratedStatement() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiOptionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement capabilityStatement = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(capabilityStatement));

		ValidationResult result = ourCtx.newValidator().validateWithResult(capabilityStatement);
		assertTrue(result.getMessages().toString(), result.isSuccessful());
	}

	private List<String> toOperationIdParts(List<CapabilityStatement.CapabilityStatementRestResourceOperationComponent> theOperation) {
		ArrayList<String> retVal = Lists.newArrayList();
		for (CapabilityStatement.CapabilityStatementRestResourceOperationComponent next : theOperation) {
			retVal.add(next.getDefinitionElement().getValue());
		}
		return retVal;
	}

	private List<String> toOperationNames(List<CapabilityStatement.CapabilityStatementRestResourceOperationComponent> theOperation) {
		ArrayList<String> retVal = Lists.newArrayList();
		for (CapabilityStatement.CapabilityStatementRestResourceOperationComponent next : theOperation) {
			retVal.add(next.getName());
		}
		return retVal;
	}

	private Set<String> toStrings(List<? extends CodeType> theType) {
		HashSet<String> retVal = new HashSet<String>();
		for (CodeType next : theType) {
			retVal.add(next.getValueAsString());
		}
		return retVal;
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	public static class ConditionalProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient, @ConditionalUrlParam String theConditionalUrl) {
			return null;
		}

		@Delete
		public MethodOutcome delete(@IdParam IdType theId, @ConditionalUrlParam(supportsMultiple = true) String theConditionalUrl) {
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

	public static class MultiOptionalProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier") @OptionalParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier, @Description(shortDefinition = "The patient's name") @OptionalParam(name = Patient.SP_NAME) StringParam theName) {
			return null;
		}

	}

	public static class MultiTypeEncounterProvider implements IResourceProvider {

		@Operation(name = "someOp")
		public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId,
													 @OperationParam(name = "someOpParam1") DateParam theStart, @OperationParam(name = "someOpParam2") Encounter theEnd) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Encounter.class;
		}

		@Validate
		public IBundleProvider validate(HttpServletRequest theServletRequest, @IdParam IdType theId, @ResourceParam Encounter thePatient) {
			return null;
		}

	}

	public static class MultiTypePatientProvider implements IResourceProvider {

		@Operation(name = "someOp")
		public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId,
													 @OperationParam(name = "someOpParam1") DateParam theStart, @OperationParam(name = "someOpParam2") Patient theEnd) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Validate
		public IBundleProvider validate(HttpServletRequest theServletRequest, @IdParam IdType theId, @ResourceParam Patient thePatient) {
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

		@Operation(name = "plain", idempotent = true, returnParameters = { @OperationParam(min = 1, max = 2, name = "out1", type = StringType.class) })
		public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId, @OperationParam(name = "start") DateType theStart, @OperationParam(name = "end") DateType theEnd) {
			return null;
		}

	}

	public static class ProviderWithExtendedOperationReturningBundle implements IResourceProvider {

		@Operation(name = "everything", idempotent = true)
		public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId, @OperationParam(name = "start") DateType theStart, @OperationParam(name = "end") DateType theEnd) {
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

	public static class SearchProvider {

		@Search(type = Patient.class)
		public Patient findPatient1(@Description(shortDefinition = "The patient's identifier (MRN or other card number)") @RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			return null;
		}

		@Search(type = Patient.class)
		public Patient findPatient2(@Description(shortDefinition = "All patients linked to the given patient") @OptionalParam(name = "link", targetTypes = { Patient.class }) ReferenceAndListParam theLink) {
			return null;
		}

	}

	public static class SearchProviderWithWhitelist {

		@Search(type = Patient.class)
		public Patient findPatient1(
				@Description(shortDefinition = "The organization at which this person is a patient") 
				@RequiredParam(name = Patient.SP_ORGANIZATION, chainWhitelist= {"foo", "bar"}) 
				ReferenceAndListParam theIdentifier) {
			return null;
		}

	}

	public static class SearchProviderWithExplicitChains {

		@Search(type = Patient.class)
		public Patient findPatient1(
			@Description(shortDefinition = "The organization at which this person is a patient")
			@RequiredParam(name = "organization.foo")	ReferenceAndListParam theFoo,
			@RequiredParam(name = "organization.bar")	ReferenceAndListParam theBar,
			@RequiredParam(name = "organization.baz.bob") ReferenceAndListParam theBazbob) {
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
