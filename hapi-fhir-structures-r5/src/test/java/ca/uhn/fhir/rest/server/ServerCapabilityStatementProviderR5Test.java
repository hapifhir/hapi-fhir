package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.IParameter;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchParameter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.hapi.rest.server.ServerCapabilityStatementProvider;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceOperationComponent;
import org.hl7.fhir.r5.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.r5.model.CapabilityStatement.ConditionalDeleteStatus;
import org.hl7.fhir.r5.model.CapabilityStatement.SystemRestfulInteraction;
import org.hl7.fhir.r5.model.CapabilityStatement.TypeRestfulInteraction;
import org.hl7.fhir.r5.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r5.model.OperationDefinition.OperationDefinitionParameterComponent;
import org.hl7.fhir.r5.model.OperationDefinition.OperationKind;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerCapabilityStatementProviderR5Test {

	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerCapabilityStatementProviderR5Test.class);

	static {
		ourCtx = FhirContext.forR5();
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

	private CapabilityStatement.CapabilityStatementRestResourceComponent findRestResource(CapabilityStatement conformance, String wantResource) throws Exception {
		CapabilityStatement.CapabilityStatementRestResourceComponent resource = null;
		for (CapabilityStatement.CapabilityStatementRestResourceComponent next : conformance.getRest().get(0).getResource()) {
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

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		CapabilityStatementRestResourceComponent res = conformance.getRest().get(0).getResource().get(1);
		assertEquals("Patient", res.getType());

		assertTrue(res.getConditionalCreate());
		assertEquals(ConditionalDeleteStatus.MULTIPLE, res.getConditionalDelete());
		assertTrue(res.getConditionalUpdate());
	}

	private RequestDetails createRequestDetails(RestfulServer theServer) {
		ServletRequestDetails retVal = new ServletRequestDetails(null);
		retVal.setServer(theServer);
		return retVal;
	}

	@Test
	public void testExtendedOperationReturningBundle() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertEquals(1, conformance.getRest().get(0).getOperation().size());
		assertEquals("everything", conformance.getRest().get(0).getOperation().get(0).getName());

		OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-everything"), createRequestDetails(rs));
		validate(opDef);
		assertEquals("everything", opDef.getCode());
		assertThat(opDef.getSystem(), is(false));
		assertThat(opDef.getType(), is(false));
		assertThat(opDef.getInstance(), is(true));
	}

	@Test
	public void testExtendedOperationReturningBundleOperation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider() {
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-everything"), createRequestDetails(rs));
		validate(opDef);

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(opDef);
		ourLog.info(conf);

		assertEquals("everything", opDef.getCode());
		assertEquals(false, opDef.getAffectsState());
	}

	@Test
	public void testInstanceHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new InstanceHistoryProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf, containsString("<interaction><code value=\"" + TypeRestfulInteraction.HISTORYINSTANCE.toCode() + "\"/></interaction>"));
	}

	@Test
	public void testMultiOptionalDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiOptionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
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
		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
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

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		CapabilityStatementRestResourceComponent res = conformance.getRest().get(0).getResource().get(1);
		assertEquals("Patient", res.getType());

		assertNull(res.getConditionalCreateElement().getValue());
		assertNull(res.getConditionalDeleteElement().getValue());
		assertNull(res.getConditionalUpdateElement().getValue());
	}

	/** See #379 */
	@Test
	public void testOperationAcrossMultipleTypes() throws Exception {
		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiTypePatientProvider(), new MultiTypeEncounterProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertEquals(4, conformance.getRest().get(0).getOperation().size());
		List<String> operationNames = toOperationNames(conformance.getRest().get(0).getOperation());
		assertThat(operationNames, containsInAnyOrder("someOp", "validate", "someOp", "validate"));

		List<String> operationIdParts = toOperationIdParts(conformance.getRest().get(0).getOperation());
		assertThat(operationIdParts, containsInAnyOrder("Patient-i-someOp", "Encounter-i-someOp", "Patient-i-validate", "Encounter-i-validate"));

		{
			OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-someOp"), createRequestDetails(rs));
			validate(opDef);
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(opDef));
			Set<String> types = toStrings(opDef.getResource());
			assertEquals("someOp", opDef.getCode());
			assertEquals(true, opDef.getInstance());
			assertEquals(false, opDef.getSystem());
			assertThat(types, containsInAnyOrder("Patient"));
			assertEquals(2, opDef.getParameter().size());
			assertEquals("someOpParam1", opDef.getParameter().get(0).getName());
			assertEquals("date", opDef.getParameter().get(0).getType().toCode());
			assertEquals("someOpParam2", opDef.getParameter().get(1).getName());
			assertEquals("Patient", opDef.getParameter().get(1).getType().toCode());
		}
		{
			OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Encounter-i-someOp"), createRequestDetails(rs));
			validate(opDef);
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(opDef));
			Set<String> types = toStrings(opDef.getResource());
			assertEquals("someOp", opDef.getCode());
			assertEquals(true, opDef.getInstance());
			assertEquals(false, opDef.getSystem());
			assertThat(types, containsInAnyOrder("Encounter"));
			assertEquals(2, opDef.getParameter().size());
			assertEquals("someOpParam1", opDef.getParameter().get(0).getName());
			assertEquals("date", opDef.getParameter().get(0).getType().toCode());
			assertEquals("someOpParam2", opDef.getParameter().get(1).getName());
			assertEquals("Encounter", opDef.getParameter().get(1).getType().toCode());
		}
		{
			OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-validate"), createRequestDetails(rs));
			validate(opDef);
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(opDef));
			Set<String> types = toStrings(opDef.getResource());
			assertEquals("validate", opDef.getCode());
			assertEquals(true, opDef.getInstance());
			assertEquals(false, opDef.getSystem());
			assertThat(types, containsInAnyOrder("Patient"));
			assertEquals(1, opDef.getParameter().size());
			assertEquals("resource", opDef.getParameter().get(0).getName());
			assertEquals("Patient", opDef.getParameter().get(0).getType().toCode());
		}
	}

	@Test
	public void testOperationDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SearchProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);

		assertThat(conf, containsString("<documentation value=\"The patient's identifier (MRN or other card number)\"/>"));
		assertThat(conf, containsString("<type value=\"token\"/>"));

	}

	@Test
	public void testOperationOnNoTypes() throws Exception {
		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new PlainProviderWithExtendedOperationOnNoType());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider() {
			@Override
			public CapabilityStatement getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails) {
				return super.getServerConformance(theRequest, createRequestDetails(rs));
			}
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		OperationDefinition opDef = sc.readOperationDefinition(new IdType("OperationDefinition/-is-plain"), createRequestDetails(rs));
		validate(opDef);

		assertEquals("plain", opDef.getCode());
		assertEquals(false, opDef.getAffectsState());
		assertEquals(3, opDef.getParameter().size());

		assertTrue(opDef.getParameter().get(0).hasName());
		assertEquals("start", opDef.getParameter().get(0).getName());
		assertEquals("in", opDef.getParameter().get(0).getUse().toCode());
		assertEquals("0", opDef.getParameter().get(0).getMinElement().getValueAsString());
		assertEquals("date", opDef.getParameter().get(0).getTypeElement().getValueAsString());

		assertEquals("out1", opDef.getParameter().get(2).getName());
		assertEquals("out", opDef.getParameter().get(2).getUse().toCode());
		assertEquals("1", opDef.getParameter().get(2).getMinElement().getValueAsString());
		assertEquals("2", opDef.getParameter().get(2).getMaxElement().getValueAsString());
		assertEquals("string", opDef.getParameter().get(2).getTypeElement().getValueAsString());

		assertThat(opDef.getSystem(), is(true));
		assertThat(opDef.getType(), is(false));
		assertThat(opDef.getInstance(), is(true));
	}

	@Test
	public void testProviderWithRequiredAndOptional() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new ProviderWithRequiredAndOptional());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		CapabilityStatementRestComponent rest = conformance.getRest().get(0);
		CapabilityStatementRestResourceComponent res = rest.getResource().get(0);
		assertEquals("DiagnosticReport", res.getType());

		assertEquals(DiagnosticReport.SP_SUBJECT, res.getSearchParam().get(0).getName());
//		assertEquals("identifier", res.getSearchParam().get(0).getChain().get(0).getValue());

		assertEquals(DiagnosticReport.SP_CODE, res.getSearchParam().get(1).getName());

		assertEquals(DiagnosticReport.SP_DATE, res.getSearchParam().get(2).getName());

		assertEquals(1, res.getSearchInclude().size());
		assertEquals("DiagnosticReport.result", res.getSearchInclude().get(0).getValue());
	}

	@Test
	public void testReadAndVReadSupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new VreadProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
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

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
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

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
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
		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

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

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
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
		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

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

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
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
		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		CapabilityStatementRestResourceComponent resource = findRestResource(conformance, "Patient");

		CapabilityStatementRestResourceSearchParamComponent param = resource.getSearchParam().get(0);
//		assertEquals("bar", param.getChain().get(0).getValue());
//		assertEquals("foo", param.getChain().get(1).getValue());
//		assertEquals(2, param.getChain().size());
	}

	@Test
	public void testSearchReferenceParameterWithList() throws Exception {

		RestfulServer rsNoType = new RestfulServer(ourCtx){
			@Override
			public RestfulServerConfiguration createConfiguration() {
				RestfulServerConfiguration retVal = super.createConfiguration();
				retVal.setConformanceDate(new InstantDt("2011-02-22T11:22:33Z"));
				return retVal;
			}
		};
		rsNoType.registerProvider(new SearchProviderWithListNoType());
		ServerCapabilityStatementProvider scNoType = new ServerCapabilityStatementProvider();
		rsNoType.setServerConformanceProvider(scNoType);
		rsNoType.init(createServletConfig());

		CapabilityStatement conformance = scNoType.getServerConformance(createHttpServletRequest(), createRequestDetails(rsNoType));
		String confNoType = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(confNoType);

		RestfulServer rsWithType = new RestfulServer(ourCtx){
			@Override
			public RestfulServerConfiguration createConfiguration() {
				RestfulServerConfiguration retVal = super.createConfiguration();
				retVal.setConformanceDate(new InstantDt("2011-02-22T11:22:33Z"));
				return retVal;
			}
		};
		rsWithType.registerProvider(new SearchProviderWithListWithType());
		ServerCapabilityStatementProvider scWithType = new ServerCapabilityStatementProvider();
		rsWithType.setServerConformanceProvider(scWithType);
		rsWithType.init(createServletConfig());

		CapabilityStatement conformanceWithType = scWithType.getServerConformance(createHttpServletRequest(), createRequestDetails(rsWithType));
		String confWithType = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformanceWithType);
		ourLog.info(confWithType);

		assertEquals(confNoType, confWithType);
		assertThat(confNoType, containsString("<date value=\"2011-02-22T11:22:33Z\"/>"));
	}

	@Test
	public void testSystemHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new SystemHistoryProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf, containsString("<interaction><code value=\"" + SystemRestfulInteraction.HISTORYSYSTEM.toCode() + "\"/></interaction>"));
	}

	@Test
	public void testTypeHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new TypeHistoryProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		conf = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf, containsString("<interaction><code value=\"" + TypeRestfulInteraction.HISTORYTYPE.toCode() + "\"/></interaction>"));
	}

	@Test
	@Ignore
	public void testValidateGeneratedStatement() throws Exception {

		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new MultiOptionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance));

		ValidationResult result = ourCtx.newValidator().validateWithResult(conformance);
		assertTrue(result.getMessages().toString(), result.isSuccessful());
	}

	@Test
	public void testSystemLevelNamedQueryWithParameters() throws Exception {
		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new NamedQueryPlainProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance));

		CapabilityStatementRestComponent restComponent = conformance.getRest().get(0);
		CapabilityStatementRestResourceOperationComponent operationComponent = restComponent.getOperation().get(0);
		assertThat(operationComponent.getName(), is(NamedQueryPlainProvider.QUERY_NAME));

		String operationReference = operationComponent.getDefinition();
		assertThat(operationReference, not(nullValue()));

		OperationDefinition operationDefinition = sc.readOperationDefinition(new IdType(operationReference), createRequestDetails(rs));
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationDefinition));
		validate(operationDefinition);
		assertThat(operationDefinition.getCode(), is(NamedQueryPlainProvider.QUERY_NAME));
		assertThat("The operation name should be the description, if a description is set", operationDefinition.getName(), is(NamedQueryPlainProvider.DESCRIPTION));
		assertThat(operationDefinition.getStatus(), is(PublicationStatus.ACTIVE));
		assertThat(operationDefinition.getKind(), is(OperationKind.QUERY));
		assertThat(operationDefinition.getDescription(), is(NamedQueryPlainProvider.DESCRIPTION));
		assertThat(operationDefinition.getAffectsState(), is(false));
		assertThat("A system level search has no target resources", operationDefinition.getResource(), is(empty()));
		assertThat(operationDefinition.getSystem(), is(true));
		assertThat(operationDefinition.getType(), is(false));
		assertThat(operationDefinition.getInstance(), is(false));
		List<OperationDefinitionParameterComponent> parameters = operationDefinition.getParameter();
		assertThat(parameters.size(), is(1));
		OperationDefinitionParameterComponent param = parameters.get(0);
		assertThat(param.getName(), is(NamedQueryPlainProvider.SP_QUANTITY));
		assertThat(param.getType().toCode(), is("string"));
		assertThat(param.getSearchTypeElement().asStringValue(), is(RestSearchParameterTypeEnum.QUANTITY.getCode()));
		assertThat(param.getMin(), is(1));
		assertThat(param.getMax(), is("1"));
		assertThat(param.getUse(), is(Enumerations.OperationParameterUse.IN));
	}

	@Test
	public void testResourceLevelNamedQueryWithParameters() throws Exception {
		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new NamedQueryResourceProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance));

		CapabilityStatementRestComponent restComponent = conformance.getRest().get(0);
		CapabilityStatementRestResourceOperationComponent operationComponent = restComponent.getOperation().get(0);
		String operationReference = operationComponent.getDefinition();
		assertThat(operationReference, not(nullValue()));

		OperationDefinition operationDefinition = sc.readOperationDefinition(new IdType(operationReference), createRequestDetails(rs));
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationDefinition));
		validate(operationDefinition);
		assertThat("The operation name should be the code if no description is set", operationDefinition.getName(), is(NamedQueryResourceProvider.QUERY_NAME));
		String patientResourceName = "Patient";
		assertThat("A resource level search targets the resource of the provider it's defined in", operationDefinition.getResource().get(0).getValue(), is(patientResourceName));
		assertThat(operationDefinition.getSystem(), is(false));
		assertThat(operationDefinition.getType(), is(true));
		assertThat(operationDefinition.getInstance(), is(false));
		List<OperationDefinitionParameterComponent> parameters = operationDefinition.getParameter();
		assertThat(parameters.size(), is(1));
		OperationDefinitionParameterComponent param = parameters.get(0);
		assertThat(param.getName(), is(NamedQueryResourceProvider.SP_PARAM));
		assertThat(param.getType().toCode(), is("string"));
		assertThat(param.getSearchTypeElement().asStringValue(), is(RestSearchParameterTypeEnum.STRING.getCode()));
		assertThat(param.getMin(), is(0));
		assertThat(param.getMax(), is("1"));
		assertThat(param.getUse(), is(Enumerations.OperationParameterUse.IN));

		CapabilityStatementRestResourceComponent patientResource = restComponent.getResource().stream()
				.filter(r -> patientResourceName.equals(r.getType()))
				.findAny().get();
		assertThat("Named query parameters should not appear in the resource search params", patientResource.getSearchParam(), is(empty()));
	}

	@Test
	public void testExtendedOperationAtTypeLevel() throws Exception {
		RestfulServer rs = new RestfulServer(ourCtx);
		rs.setProviders(new TypeLevelOperationProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		List<CapabilityStatementRestResourceOperationComponent> operations = conformance.getRest().get(0).getOperation();
		assertThat(operations.size(), is(1));
		assertThat(operations.get(0).getName(), is(TypeLevelOperationProvider.OPERATION_NAME));

		OperationDefinition opDef = sc.readOperationDefinition(new IdType(operations.get(0).getDefinition()), createRequestDetails(rs));
		validate(opDef);
		assertEquals(TypeLevelOperationProvider.OPERATION_NAME, opDef.getCode());
		assertThat(opDef.getSystem(), is(false));
		assertThat(opDef.getType(), is(true));
		assertThat(opDef.getInstance(), is(false));
	}

	@Test
    public void testProfiledResourceStructureDefinitionLinks() throws Exception {
        RestfulServer rs = new RestfulServer(ourCtx);
        rs.setResourceProviders(new ProfiledPatientProvider(), new MultipleProfilesPatientProvider());

        ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider();
        rs.setServerConformanceProvider(sc);

        rs.init(createServletConfig());

        CapabilityStatement conformance = sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
        ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance));

        List<CapabilityStatementRestResourceComponent> resources = conformance.getRestFirstRep().getResource();
        CapabilityStatementRestResourceComponent patientResource = resources.stream()
            .filter(resource -> "Patient".equals(resource.getType()))
            .findFirst().get();
        assertThat(patientResource.getProfile(), containsString(PATIENT_SUB));
    }

	private List<String> toOperationIdParts(List<CapabilityStatementRestResourceOperationComponent> theOperation) {
		ArrayList<String> retVal = Lists.newArrayList();
		for (CapabilityStatementRestResourceOperationComponent next : theOperation) {
			retVal.add(new IdType(next.getDefinition()).getIdPart());
		}
		return retVal;
	}

	private List<String> toOperationNames(List<CapabilityStatementRestResourceOperationComponent> theOperation) {
		ArrayList<String> retVal = Lists.newArrayList();
		for (CapabilityStatementRestResourceOperationComponent next : theOperation) {
			retVal.add(next.getName());
		}
		return retVal;
	}

	private Set<String> toStrings(List<CodeType> theType) {
		HashSet<String> retVal = new HashSet<String>();
		for (CodeType next : theType) {
			retVal.add(next.getValueAsString());
		}
		return retVal;
	}

	private void validate(OperationDefinition theOpDef) {
		String conf = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(theOpDef);
		ourLog.info("Def: {}", conf);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	public static class MultiOptionalProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@Description(shortDefinition = "The patient's identifier") @OptionalParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier,
				@Description(shortDefinition = "The patient's name") @OptionalParam(name = Patient.SP_NAME) StringParam theName) {
			return null;
		}

	}

	@SuppressWarnings("unused")
	public static class MultiTypeEncounterProvider implements IResourceProvider {

		@Operation(name = "someOp")
		public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId,
													 @OperationParam(name = "someOpParam1") DateType theStart, @OperationParam(name = "someOpParam2") Encounter theEnd) {
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

	@SuppressWarnings("unused")
	public static class MultiTypePatientProvider implements IResourceProvider {

		@Operation(name = "someOp")
		public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId,
													 @OperationParam(name = "someOpParam1") DateType theStart, @OperationParam(name = "someOpParam2") Patient theEnd) {
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

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	public static class PlainProviderWithExtendedOperationOnNoType {

		@Operation(name = "plain", idempotent = true, returnParameters = { @OperationParam(min = 1, max = 2, name = "out1", type = StringType.class) })
		public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId, @OperationParam(name = "start") DateType theStart,
													 @OperationParam(name = "end") DateType theEnd) {
			return null;
		}

	}

	@SuppressWarnings("unused")
	public static class ProviderWithExtendedOperationReturningBundle implements IResourceProvider {

		@Operation(name = "everything", idempotent = true)
		public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId, @OperationParam(name = "start") DateType theStart,
													 @OperationParam(name = "end") DateType theEnd) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

	@SuppressWarnings("unused")
	public static class ProviderWithRequiredAndOptional {

		@Description(shortDefinition = "This is a search for stuff!")
		@Search
		public List<DiagnosticReport> findDiagnosticReportsByPatient(@RequiredParam(name = DiagnosticReport.SP_SUBJECT + '.' + Patient.SP_IDENTIFIER) TokenParam thePatientId,
				@OptionalParam(name = DiagnosticReport.SP_CODE) TokenOrListParam theNames, @OptionalParam(name = DiagnosticReport.SP_DATE) DateRangeParam theDateRange,
				@IncludeParam(allow = { "DiagnosticReport.result" }) Set<Include> theIncludes) throws Exception {
			return null;
		}

	}

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
	public static class SearchProvider {

		@Search(type = Patient.class)
		public Patient findPatient1(@Description(shortDefinition = "The patient's identifier (MRN or other card number)") @RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			return null;
		}

		@Search(type = Patient.class)
		public Patient findPatient2(
				@Description(shortDefinition = "All patients linked to the given patient") @OptionalParam(name = "link", targetTypes = { Patient.class }) ReferenceAndListParam theLink) {
			return null;
		}

	}

	@SuppressWarnings("unused")
	public static class SearchProviderWithWhitelist {

		@Search(type = Patient.class)
		public Patient findPatient1(@Description(shortDefinition = "The organization at which this person is a patient") @RequiredParam(name = Patient.SP_ORGANIZATION, chainWhitelist = { "foo",
				"bar" }) ReferenceAndListParam theIdentifier) {
			return null;
		}

	}

	@SuppressWarnings("unused")
	public static class SearchProviderWithListNoType  implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}



		@Search()
		public List<Patient> findPatient1(@Description(shortDefinition = "The organization at which this person is a patient") @RequiredParam(name = Patient.SP_ORGANIZATION) ReferenceAndListParam theIdentifier) {
			return null;
		}

	}

	@SuppressWarnings("unused")
	public static class SearchProviderWithListWithType  implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}



		@Search(type=Patient.class)
		public List<Patient> findPatient1(@Description(shortDefinition = "The organization at which this person is a patient") @RequiredParam(name = Patient.SP_ORGANIZATION) ReferenceAndListParam theIdentifier) {
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

	@SuppressWarnings("unused")
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
  
	public static class TypeLevelOperationProvider implements IResourceProvider {

		public static final String OPERATION_NAME = "op";

		@Operation(name = OPERATION_NAME, idempotent = true)
		public IBundleProvider op() {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

	public static class NamedQueryPlainProvider {

		public static final String QUERY_NAME = "testQuery";
		public static final String DESCRIPTION = "A query description";
		public static final String SP_QUANTITY = "quantity";

		@Search(queryName = QUERY_NAME)
		@Description(formalDefinition = DESCRIPTION)
		public Bundle findAllGivenParameter(@RequiredParam(name = SP_QUANTITY) QuantityParam quantity) {
			return null;
		}
	}

	public static class NamedQueryResourceProvider implements IResourceProvider {

		public static final String QUERY_NAME = "testQuery";
		public static final String SP_PARAM = "param";

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search(queryName = QUERY_NAME)
		public Bundle findAllGivenParameter(@OptionalParam(name = SP_PARAM) StringParam param) {
			return null;
		}

	}

    public static class ProfiledPatientProvider implements IResourceProvider {

    @Override
    public Class<? extends IBaseResource> getResourceType() {
      return PatientSubSub2.class;
    }

    @Search
    public List<PatientSubSub2> find() {
      return null;
    }
  }

  public static class MultipleProfilesPatientProvider implements IResourceProvider {

    @Override
    public Class<? extends IBaseResource> getResourceType() {
      return PatientSubSub.class;
    }

    @Read(type = PatientTripleSub.class)
    public PatientTripleSub read(@IdParam IdType theId) {
      return null;
    }

  }

  public static final String PATIENT_SUB = "PatientSub";
  public static final String PATIENT_SUB_SUB = "PatientSubSub";
  public static final String PATIENT_SUB_SUB_2 = "PatientSubSub2";
  public static final String PATIENT_TRIPLE_SUB = "PatientTripleSub";

  @ResourceDef(id = PATIENT_SUB)
  public static class PatientSub extends Patient {}

  @ResourceDef(id = PATIENT_SUB_SUB)
  public static class PatientSubSub extends PatientSub {}

  @ResourceDef(id = PATIENT_SUB_SUB_2)
  public static class PatientSubSub2 extends PatientSub {}

  @ResourceDef(id = PATIENT_TRIPLE_SUB)
  public static class PatientTripleSub extends PatientSubSub {}

}
