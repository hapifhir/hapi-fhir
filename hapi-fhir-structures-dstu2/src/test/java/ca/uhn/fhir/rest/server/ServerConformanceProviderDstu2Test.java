package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestOperation;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestSecurity;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.OperationDefinition;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.ConditionalDeleteStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.RestfulSecurityServiceEnum;
import ca.uhn.fhir.model.dstu2.valueset.SystemRestfulInteractionEnum;
import ca.uhn.fhir.model.dstu2.valueset.TypeRestfulInteractionEnum;
import ca.uhn.fhir.model.dstu2.valueset.UnknownContentCodeEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.IParameter;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchParameter;
import ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerConformanceProviderDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerConformanceProviderDstu2Test.class);
	private static FhirContext ourCtx;
	private static FhirValidator ourValidator;

	static {
		ourCtx = FhirContext.forDstu2();
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

		assertTrue(result.isSuccessful(), outcome);
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

	private RestResource findRestResource(Conformance conformance, String wantResource) throws Exception {
		RestResource resource = null;
		for (RestResource next : conformance.getRest().get(0).getResource()) {
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

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		RestResource res = conformance.getRest().get(0).getResource().get(1);
		assertEquals("Patient", res.getType());

		assertTrue(res.getConditionalCreate());
		assertEquals(ConditionalDeleteStatusEnum.MULTIPLE_DELETES_SUPPORTED, res.getConditionalDeleteElement().getValueAsEnum());
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
		assertEquals("everything", conformance.getRest().get(0).getOperation().get(0).getName());
		assertEquals("OperationDefinition/Patient-i-everything", conformance.getRest().get(0).getOperation().get(0).getDefinition().getReference().getValue());
	}

	@Test
	public void testExtendedOperationReturningBundleOperation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs) {
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		OperationDefinition opDef = sc.readOperationDefinition(new IdDt("OperationDefinition/Patient-i-everything"), createRequestDetails(rs));
		validate(opDef);

		assertEquals("everything", opDef.getCode());
		assertEquals(true, opDef.getIdempotent().booleanValue());
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
		assertThat(conf, containsString("<interaction><code value=\"" + TypeRestfulInteractionEnum.HISTORY_INSTANCE.getCode() + "\"/></interaction>"));
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

		RestResource res = conformance.getRest().get(0).getResource().get(1);
		assertEquals("Patient", res.getType());

		assertNull(res.getConditionalCreate());
		assertNull(res.getConditionalDelete());
		assertNull(res.getConditionalUpdate());
	}

	/**
	 * See #379
	 */
	@Test
	public void testOperationAcrossMultipleTypes() throws Exception {
		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiTypePatientProvider(), new MultiTypeEncounterProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertEquals(2, conformance.getRest().get(0).getOperation().size());
		List<String> operationNames = toOperationNames(conformance.getRest().get(0).getOperation());
		assertThat(operationNames, containsInAnyOrder("someOp", "validate"));

		List<String> operationIdParts = toOperationIdParts(conformance.getRest().get(0).getOperation());
		assertThat(operationIdParts, containsInAnyOrder("EncounterPatient-i-someOp", "EncounterPatient-i-validate"));

		{
			OperationDefinition opDef = sc.readOperationDefinition(new IdDt("OperationDefinition/EncounterPatient-i-someOp"), createRequestDetails(rs));
			validate(opDef);

			Set<String> types = toStrings(opDef.getType());
			assertEquals("someOp", opDef.getCode());
			assertEquals(true, opDef.getInstance());
			assertEquals(false, opDef.getSystem());
			assertThat(types, containsInAnyOrder("Patient", "Encounter"));
			assertEquals(2, opDef.getParameter().size());
			assertEquals("someOpParam1", opDef.getParameter().get(0).getName());
			assertEquals("date", opDef.getParameter().get(0).getType());
			assertEquals("someOpParam2", opDef.getParameter().get(1).getName());
		}
	}

	@Test
	public void testOperationDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SearchProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info("AAAAAA" + conf);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));

	}

	@Test
	public void testProviderForSmart() throws ServletException {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.createConfiguration();
		rs.setProviders(new ProviderWithRequiredAndOptional());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs) {
			@Override
			public Conformance getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
				Conformance conformance = super.getServerConformance(theRequest, theRequestDetails);
				ExtensionDt extensionDt = new ExtensionDt();
				ExtensionDt extensionDtToken = new ExtensionDt();
				ExtensionDt extensionDtAuthorize = new ExtensionDt();
				Rest rest = conformance.getRestFirstRep();
				RestSecurity restSecurity = rest.getSecurity();

				conformance.setAcceptUnknown(UnknownContentCodeEnum.UNKNOWN_ELEMENTS_AND_EXTENSIONS);
				restSecurity.addService(RestfulSecurityServiceEnum.SMART_ON_FHIR);
				restSecurity.getServiceFirstRep().setText("OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org)");
				extensionDt.setUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");
				extensionDtToken.setUrl("token");
				extensionDtToken.setValue(new UriDt("https://SERVERNAME/token"));
				extensionDtAuthorize.setUrl("authorize");
				extensionDtAuthorize.setValue(new UriDt("https://SERVERNAME/authorize"));
				extensionDt.addUndeclaredExtension(extensionDtToken);
				extensionDt.addUndeclaredExtension(extensionDtAuthorize);
				restSecurity.addUndeclaredExtension(extensionDt);

				return conformance;
			}
		};

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		Conformance parsed = ourCtx.newJsonParser().parseResource(Conformance.class, conf);
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

		Rest rest = conformance.getRestFirstRep();
		RestResource res = rest.getResourceFirstRep();
		assertEquals("DiagnosticReport", res.getType());

		assertEquals(DiagnosticReport.SP_SUBJECT, res.getSearchParam().get(0).getName());
		assertEquals("identifier", res.getSearchParam().get(0).getChain().get(0).getValue());

		assertEquals(DiagnosticReport.SP_CODE, res.getSearchParam().get(1).getName());

		assertEquals(DiagnosticReport.SP_DATE, res.getSearchParam().get(2).getName());

		assertEquals(1, res.getSearchInclude().size());
		assertEquals("DiagnosticReport.result", res.getSearchIncludeFirstRep().getValue());
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
		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
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

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding<?>> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().get(24);
				assertEquals("The organization at which this person is a patient", param.getDescription());
				found = true;
			}
		}
		assertTrue(found);
		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

	}


	/**
	 * See #286
	 */
	@Test
	public void testSearchReferenceParameterWithWhitelistDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SearchProviderWithWhitelist());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
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
		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		RestResource resource = findRestResource(conformance, "Patient");

		RestResourceSearchParam param = resource.getSearchParam().get(0);
		assertEquals("bar", param.getChain().get(0).getValue());
		assertEquals("foo", param.getChain().get(1).getValue());
		assertEquals(2, param.getChain().size());
	}

	@Test
	public void testSearchReferenceParameterWithExplicitChainsDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SearchProviderWithExplicitChains());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
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
		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		RestResource resource = findRestResource(conformance, "Patient");

		assertEquals(1, resource.getSearchParam().size());
		RestResourceSearchParam param = resource.getSearchParam().get(0);
		assertEquals("organization", param.getName());
		assertEquals("bar", param.getChain().get(0).getValue());
		assertEquals("baz.bob", param.getChain().get(1).getValue());
		assertEquals("foo", param.getChain().get(2).getValue());
		assertEquals(3, param.getChain().size());
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
		assertThat(conf, containsString("<interaction><code value=\"" + SystemRestfulInteractionEnum.HISTORY_SYSTEM.getCode() + "\"/></interaction>"));
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
		assertThat(conf, containsString("<interaction><code value=\"" + TypeRestfulInteractionEnum.HISTORY_TYPE.getCode() + "\"/></interaction>"));
	}

	@Test
	public void testValidateGeneratedStatement() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiOptionalProvider());

		ServerConformanceProvider sc = new ServerConformanceProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		Conformance conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance));

		ValidationResult result = ourCtx.newValidator().validateWithResult(conformance);
		assertTrue(result.isSuccessful(), result.getMessages().toString());
	}

	private List<String> toOperationIdParts(List<RestOperation> theOperation) {
		ArrayList<String> retVal = Lists.newArrayList();
		for (RestOperation next : theOperation) {
			retVal.add(next.getDefinition().getReference().getIdPart());
		}
		return retVal;
	}

	private List<String> toOperationNames(List<RestOperation> theOperation) {
		ArrayList<String> retVal = Lists.newArrayList();
		for (RestOperation next : theOperation) {
			retVal.add(next.getName());
		}
		return retVal;
	}

	private Set<String> toStrings(List<? extends CodeDt> theType) {
		HashSet<String> retVal = new HashSet<String>();
		for (CodeDt next : theType) {
			retVal.add(next.getValueAsString());
		}
		return retVal;
	}

	private RequestDetails createRequestDetails(RestfulServer theServer) {
		ServletRequestDetails retVal = new ServletRequestDetails();
		retVal.setServer(theServer);
		return retVal;
	}

	public static class ConditionalProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient, @ConditionalUrlParam String theConditionalUrl) {
			return null;
		}

		@Delete
		public MethodOutcome delete(@IdParam IdDt theId, @ConditionalUrlParam(supportsMultiple = true) String theConditionalUrl) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome update(@IdParam IdDt theId, @ResourceParam Patient thePatient, @ConditionalUrlParam String theConditionalUrl) {
			return null;
		}

	}

	public static class InstanceHistoryProvider implements IResourceProvider {
		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@History
		public List<IBaseResource> history(@IdParam IdDt theId) {
			return null;
		}

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class MultiOptionalProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier") @OptionalParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier, @Description(shortDefinition = "The patient's name") @OptionalParam(name = Patient.SP_NAME) StringDt theName) {
			return null;
		}

	}

	public static class MultiTypeEncounterProvider implements IResourceProvider {

		@Operation(name = "someOp")
		public IBundleProvider everything(javax.servlet.http.HttpServletRequest theServletRequest, @IdParam IdDt theId,
													 @OperationParam(name = "someOpParam1") DateDt theStart, @OperationParam(name = "someOpParam2") Encounter theEnd) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Encounter.class;
		}

		@Validate
		public IBundleProvider validate(javax.servlet.http.HttpServletRequest theServletRequest, @IdParam IdDt theId, @ResourceParam Encounter thePatient) {
			return null;
		}

	}

	public static class MultiTypePatientProvider implements IResourceProvider {

		@Operation(name = "someOp")
		public IBundleProvider everything(javax.servlet.http.HttpServletRequest theServletRequest, @IdParam IdDt theId,
													 @OperationParam(name = "someOpParam1") DateDt theStart, @OperationParam(name = "someOpParam2") Patient theEnd) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Validate
		public IBundleProvider validate(javax.servlet.http.HttpServletRequest theServletRequest, @IdParam IdDt theId, @ResourceParam Patient thePatient) {
			return null;
		}

	}

	public static class NonConditionalProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			return null;
		}

		@Delete
		public MethodOutcome delete(@IdParam IdDt theId) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome update(@IdParam IdDt theId, @ResourceParam Patient thePatient) {
			return null;
		}

	}

	public static class PlainProviderWithExtendedOperationOnNoType {

		@Operation(name = "plain", idempotent = true, returnParameters = {@OperationParam(min = 1, max = 2, name = "out1", type = StringDt.class)})
		public IBundleProvider everything(javax.servlet.http.HttpServletRequest theServletRequest, @IdParam ca.uhn.fhir.model.primitive.IdDt theId, @OperationParam(name = "start") DateDt theStart, @OperationParam(name = "end") DateDt theEnd) {
			return null;
		}

	}

	public static class ProviderWithExtendedOperationReturningBundle implements IResourceProvider {

		@Operation(name = "everything", idempotent = true)
		public IBundleProvider everything(javax.servlet.http.HttpServletRequest theServletRequest, @IdParam ca.uhn.fhir.model.primitive.IdDt theId, @OperationParam(name = "start") DateDt theStart, @OperationParam(name = "end") DateDt theEnd) {
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
		public List<DiagnosticReport> findDiagnosticReportsByPatient(@RequiredParam(name = DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) IdentifierDt thePatientId, @OptionalParam(name = DiagnosticReport.SP_CODE) TokenOrListParam theNames,
																						 @OptionalParam(name = DiagnosticReport.SP_DATE) DateRangeParam theDateRange, @IncludeParam(allow = {"DiagnosticReport.result"}) Set<Include> theIncludes) throws Exception {
			return null;
		}

	}

	public static class ReadProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier (MRN or other card number)") @RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
			return null;
		}

		@Read(version = false)
		public Patient readPatient(@IdParam IdDt theId) {
			return null;
		}

	}

	public static class SearchProvider {

		@Search(type = Patient.class)
		public Patient findPatient1(@Description(shortDefinition = "The patient's identifier (MRN or other card number)") @RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
			return null;
		}

		@Search(type = Patient.class)
		public Patient findPatient2(@Description(shortDefinition = "All patients linked to the given patient") @OptionalParam(name = "link", targetTypes = {Patient.class}) ReferenceAndListParam theLink) {
			return null;
		}

	}

	public static class SearchProviderWithWhitelist {

		@Search(type = Patient.class)
		public Patient findPatient1(
			@Description(shortDefinition = "The organization at which this person is a patient")
			@RequiredParam(name = Patient.SP_ORGANIZATION, chainWhitelist = {"foo", "bar"})
				ReferenceAndListParam theIdentifier) {
			return null;
		}

	}

	public static class SearchProviderWithExplicitChains {

		@Search(type = Patient.class)
		public Patient findPatient1(
			@Description(shortDefinition = "The organization at which this person is a patient")
			@RequiredParam(name = "organization.foo") ReferenceAndListParam theFoo,
			@RequiredParam(name = "organization.bar") ReferenceAndListParam theBar,
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
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier (MRN or other card number)") @RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
			return null;
		}

		@Read(version = true)
		public Patient readPatient(@IdParam IdDt theId) {
			return null;
		}

	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


}
