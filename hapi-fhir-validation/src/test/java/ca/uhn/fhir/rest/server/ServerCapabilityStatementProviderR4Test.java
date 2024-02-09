package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.GraphQL;
import ca.uhn.fhir.rest.annotation.GraphQLQueryBody;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
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
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.IParameter;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchParameter;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.collect.Lists;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceOperationComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.CapabilityStatementRestResourceSearchParamComponent;
import org.hl7.fhir.r4.model.CapabilityStatement.ConditionalDeleteStatus;
import org.hl7.fhir.r4.model.CapabilityStatement.SystemRestfulInteraction;
import org.hl7.fhir.r4.model.CapabilityStatement.TypeRestfulInteraction;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationDefinition;
import org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterComponent;
import org.hl7.fhir.r4.model.OperationDefinition.OperationKind;
import org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUse;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.test.utilities.server.MockServletUtil.createServletConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class ServerCapabilityStatementProviderR4Test extends BaseValidationTestWithInlineMocks {

	public static final String PATIENT_SUB = "PatientSub";
	public static final String PATIENT_SUB_SUB = "PatientSubSub";
	public static final String PATIENT_SUB_SUB_2 = "PatientSubSub2";
	public static final String PATIENT_TRIPLE_SUB = "PatientTripleSub";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerCapabilityStatementProviderR4Test.class);
	private final FhirContext myCtx = FhirContext.forR4Cached();
	private FhirValidator myValidator;

	private static Set<String> toStrings(Collection<? extends IPrimitiveType> theType) {
		HashSet<String> retVal = new HashSet<String>();
		for (IPrimitiveType next : theType) {
			retVal.add(next.getValueAsString());
		}
		return retVal;
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeEach
	public void before() {
		myValidator = myCtx.newValidator();
		myValidator.registerValidatorModule(new FhirInstanceValidator(myCtx));
	}

	private HttpServletRequest createHttpServletRequest() {
		HttpServletRequest req = mock(HttpServletRequest.class, withSettings().strictness(Strictness.LENIENT));
		when(req.getRequestURI()).thenReturn("/FhirStorm/fhir/Patient/_search");
		when(req.getServletPath()).thenReturn("/fhir");
		when(req.getRequestURL()).thenReturn(new StringBuffer().append("http://fhirstorm.dyndns.org:8080/FhirStorm/fhir/Patient/_search"));
		when(req.getContextPath()).thenReturn("/FhirStorm");
		return req;
	}

	private CapabilityStatementRestResourceComponent findRestResource(CapabilityStatement conformance, String wantResource) throws Exception {
		CapabilityStatementRestResourceComponent resource = null;
		for (CapabilityStatementRestResourceComponent next : conformance.getRest().get(0).getResource()) {
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
	public void testFormats() throws ServletException {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new ConditionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement cs = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		List<String> formats = cs
			.getFormat()
			.stream()
			.map(t -> t.getCode())
			.collect(Collectors.toList());
		assertThat(formats).as(formats.toString()).containsExactlyInAnyOrder("application/fhir+xml", "xml", "application/fhir+json", "json", "application/x-turtle", "ttl");
	}

	@Test
	public void testConditionalOperations() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new ConditionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		assertThat(conformance.getRest().get(0).getResource()).hasSize(2);
		CapabilityStatementRestResourceComponent res = conformance.getRest().get(0).getResource().get(1);
		assertThat(res.getType()).isEqualTo("Patient");

		assertThat(res.getConditionalCreate()).isTrue();
		assertThat(res.getConditionalDelete()).isEqualTo(ConditionalDeleteStatus.MULTIPLE);
		assertThat(res.getConditionalUpdate()).isTrue();
	}

	@Test
	public void testMethodGetServerConformance_whenServerSupportsExportOperation_willIncludeInstantiatesElement() throws Exception {
		// given
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new BulkDataExportProvider());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));
		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		// when
		rs.init(createServletConfig());
		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		// then
		String instantiatesFirstRepValue = conformance.getInstantiates().get(0).getValue();
		assertThat(instantiatesFirstRepValue).isEqualTo(Constants.BULK_DATA_ACCESS_IG_URL);
	}

	private RequestDetails createRequestDetails(RestfulServer theServer) {
		ServletRequestDetails retVal = new ServletRequestDetails();
		retVal.setServer(theServer);
		retVal.setFhirServerBase("http://localhost/baseR4");
		return retVal;
	}

	@Test
	public void testExtendedOperationReturningBundle() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		validate(conformance);

		CapabilityStatementRestResourceComponent patient = conformance.getRestFirstRep().getResource().stream().filter(t -> t.getType().equals("Patient")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		assertThat(patient.getOperation()).hasSize(1);
		assertThat(patient.getOperation().get(0).getName()).isEqualTo("everything");
		assertThat(patient.getOperation().get(0).getDefinition()).isEqualTo("http://localhost/baseR4/OperationDefinition/Patient-i-everything");

		OperationDefinition opDef = (OperationDefinition) sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-everything"), createRequestDetails(rs));
		validate(opDef);
		assertThat(opDef.getCode()).isEqualTo("everything");
		assertThat(opDef.getSystem()).isEqualTo(false);
		assertThat(opDef.getType()).isEqualTo(false);
		assertThat(opDef.getInstance()).isEqualTo(true);
	}

	@Test
	public void testExtendedOperationReturningBundleOperation() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new ProviderWithExtendedOperationReturningBundle());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs) {
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		OperationDefinition opDef = (OperationDefinition) sc.readOperationDefinition(new IdType("OperationDefinition/Patient-i-everything"), createRequestDetails(rs));
		validate(opDef);

		String conf = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(opDef);
		ourLog.info(conf);

		assertThat(opDef.getCode()).isEqualTo("everything");
		assertThat(opDef.getAffectsState()).isEqualTo(false);
	}

	@Test
	public void testInstanceHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new InstanceHistoryProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = validate(conformance);

		conf = myCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf).contains("<interaction><code value=\"" + TypeRestfulInteraction.HISTORYINSTANCE.toCode() + "\"/></interaction>");
	}

	@Test
	public void testMultiOptionalDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new MultiOptionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().iterator().next();
				assertThat(param.getDescription()).isEqualTo("The patient's identifier");
				found = true;
			}
		}

		assertThat(found).isTrue();
		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = validate(conformance);

		assertThat(conf).contains("<documentation value=\"The patient's identifier\"/>");
		assertThat(conf).contains("<documentation value=\"The patient's name\"/>");
		assertThat(conf).contains("<type value=\"token\"/>");
	}

	@Test
	public void testNonConditionalOperations() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new NonConditionalProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		validate(conformance);

		CapabilityStatementRestResourceComponent res = conformance.getRest().get(0).getResource().get(1);
		assertThat(res.getType()).isEqualTo("Patient");

		assertThat(res.getConditionalCreateElement().getValue()).isNull();
		assertThat(res.getConditionalDeleteElement().getValue()).isNull();
		assertThat(res.getConditionalUpdateElement().getValue()).isNull();
	}

	@Test
	public void testOperationParameterDocumentation() throws Exception {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new MultiTypePatientProvider(), new MultiTypeEncounterProvider());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		validate(conformance);

		IdType operationId = new IdType("OperationDefinition/EncounterPatient-i-someOp");
		RequestDetails requestDetails = createRequestDetails(rs);
		OperationDefinition opDef = (OperationDefinition) sc.readOperationDefinition(operationId, requestDetails);
		validate(opDef);
		Set<String> types = toStrings(opDef.getResource());
		assertThat(opDef.getCode()).isEqualTo("someOp");
		assertThat(opDef.getInstance()).isEqualTo(true);
		assertThat(opDef.getSystem()).isEqualTo(false);
		assertThat(types).containsExactlyInAnyOrder("Patient", "Encounter");
		assertThat(opDef.getParameter()).hasSize(2);
		assertThat(opDef.getParameter().get(0).getName()).isEqualTo("someOpParam1");
		assertThat(opDef.getParameter().get(0).getType()).isEqualTo("date");
		assertThat(opDef.getParameter().get(0).getDocumentation()).isEqualTo("Start description");

		List<Extension> exampleExtensions = opDef.getParameter().get(0).getExtensionsByUrl(HapiExtensions.EXT_OP_PARAMETER_EXAMPLE_VALUE);
		assertThat(exampleExtensions).hasSize(2);
		assertThat(exampleExtensions.get(0).getValueAsPrimitive().getValueAsString()).isEqualTo("2001");
		assertThat(exampleExtensions.get(1).getValueAsPrimitive().getValueAsString()).isEqualTo("2002");
	}

	/**
	 * See #379
	 */
	@Test
	public void testOperationAcrossMultipleTypes() throws Exception {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new MultiTypePatientProvider(), new MultiTypeEncounterProvider());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		validate(conformance);

		CapabilityStatementRestResourceComponent patient = conformance.getRestFirstRep().getResource().stream().filter(t -> t.getType().equals("Patient")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		assertThat(patient.getOperation()).hasSize(2);
		assertThat(toOperationNames(patient.getOperation())).containsExactlyInAnyOrder("someOp", "validate");
		assertThat(toOperationDefinitions(patient.getOperation())).containsExactlyInAnyOrder("http://localhost/baseR4/OperationDefinition/EncounterPatient-i-someOp", "http://localhost/baseR4/OperationDefinition/EncounterPatient-i-validate");

		CapabilityStatementRestResourceComponent encounter = conformance.getRestFirstRep().getResource().stream().filter(t -> t.getType().equals("Encounter")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		assertThat(encounter.getOperation()).hasSize(2);
		assertThat(toOperationNames(encounter.getOperation())).containsExactlyInAnyOrder("someOp", "validate");
		assertThat(toOperationDefinitions(encounter.getOperation())).as(toOperationDefinitions(encounter.getOperation()).toString()).containsExactlyInAnyOrder("http://localhost/baseR4/OperationDefinition/EncounterPatient-i-someOp", "http://localhost/baseR4/OperationDefinition/EncounterPatient-i-validate");


		{
			OperationDefinition opDef = (OperationDefinition) sc.readOperationDefinition(new IdType("OperationDefinition/EncounterPatient-i-someOp"), createRequestDetails(rs));
			validate(opDef);
			ourLog.debug(myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(opDef));
			Set<String> types = toStrings(opDef.getResource());
			assertThat(opDef.getCode()).isEqualTo("someOp");
			assertThat(opDef.getInstance()).isEqualTo(true);
			assertThat(opDef.getSystem()).isEqualTo(false);
			assertThat(types).containsExactlyInAnyOrder("Patient", "Encounter");
			assertThat(opDef.getParameter()).hasSize(2);
			assertThat(opDef.getParameter().get(0).getName()).isEqualTo("someOpParam1");
			assertThat(opDef.getParameter().get(0).getType()).isEqualTo("date");
			assertThat(opDef.getParameter().get(1).getName()).isEqualTo("someOpParam2");
			assertThat(opDef.getParameter().get(1).getType()).isEqualTo("Resource");
		}
		{
			OperationDefinition opDef = (OperationDefinition) sc.readOperationDefinition(new IdType("OperationDefinition/EncounterPatient-i-validate"), createRequestDetails(rs));
			validate(opDef);
			ourLog.debug(myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(opDef));
			Set<String> types = toStrings(opDef.getResource());
			assertThat(opDef.getCode()).isEqualTo("validate");
			assertThat(opDef.getInstance()).isEqualTo(true);
			assertThat(opDef.getSystem()).isEqualTo(false);
			assertThat(types).containsExactlyInAnyOrder("Patient", "Encounter");
			assertThat(opDef.getParameter()).hasSize(1);
			assertThat(opDef.getParameter().get(0).getName()).isEqualTo("resource");
			assertThat(opDef.getParameter().get(0).getType()).isEqualTo("Resource");
		}
	}

	@Test
	public void testOperationDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new SearchProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = validate(conformance);

		assertThat(conf).contains("<documentation value=\"The patient's identifier (MRN or other card number)\"/>");
		assertThat(conf).contains("<type value=\"token\"/>");

	}

	@Test
	public void testProviderWithRequiredAndOptional() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new ProviderWithRequiredAndOptional());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		validate(conformance);

		CapabilityStatementRestComponent rest = conformance.getRest().get(0);
		CapabilityStatementRestResourceComponent res = rest.getResource().get(0);
		assertThat(res.getType()).isEqualTo("DiagnosticReport");

		assertThat(res.getSearchParam().get(0).getName()).isEqualTo("subject.identifier");
//		assertEquals("identifier", res.getSearchParam().get(0).getChain().get(0).getValue());

		assertThat(res.getSearchParam().get(1).getName()).isEqualTo(DiagnosticReport.SP_CODE);

		assertThat(res.getSearchParam().get(2).getName()).isEqualTo(DiagnosticReport.SP_DATE);

		assertThat(res.getSearchInclude()).hasSize(1);
		assertThat(res.getSearchInclude().get(0).getValue()).isEqualTo("DiagnosticReport.result");
	}

	@Test
	public void testReadAndVReadSupported() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new VreadProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = validate(conformance);

		assertThat(conf).contains("<interaction><code value=\"vread\"/></interaction>");
		assertThat(conf).contains("<interaction><code value=\"read\"/></interaction>");
	}

	@Test
	public void testReadSupported() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new ReadProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance);
		ourLog.info(conf);

		conf = myCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(conformance);
		assertThat(conf).doesNotContain("<interaction><code value=\"vread\"/></interaction>");
		assertThat(conf).contains("<interaction><code value=\"read\"/></interaction>");
	}

	@Test
	public void testSearchParameterDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new SearchProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding> methodBindings = resourceBinding.getMethodBindings();
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
		assertThat(found).isTrue();
		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = validate(conformance);

		assertThat(conf).contains("<documentation value=\"The patient's identifier (MRN or other card number)\"/>");
		assertThat(conf).contains("<type value=\"token\"/>");

	}

	@Test
	public void testFormatIncludesSpecialNonMediaTypeFormats() throws ServletException {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new SearchProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());
		CapabilityStatement serverConformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		List<String> formatCodes = serverConformance.getFormat().stream().map(c -> c.getCode()).collect(Collectors.toList());

		assertThat(formatCodes).contains(Constants.FORMAT_XML);
		assertThat(formatCodes).contains(Constants.FORMAT_JSON);
		assertThat(formatCodes).contains(Constants.CT_FHIR_JSON_NEW);
		assertThat(formatCodes).contains(Constants.CT_FHIR_XML_NEW);
	}

	/**
	 * See #286
	 */
	@Test
	public void testSearchReferenceParameterDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new PatientResourceProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().get(25);
				assertThat(param.getDescription()).isEqualTo("The organization at which this person is a patient");
				found = true;
			}
		}
		assertThat(found).isTrue();
		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = validate(conformance);

	}

	/**
	 * See #286
	 */
	@Test
	public void testSearchReferenceParameterWithWhitelistDocumentation() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new SearchProviderWithWhitelist());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		boolean found = false;
		Collection<ResourceBinding> resourceBindings = rs.getResourceBindings();
		for (ResourceBinding resourceBinding : resourceBindings) {
			if (resourceBinding.getResourceName().equals("Patient")) {
				List<BaseMethodBinding> methodBindings = resourceBinding.getMethodBindings();
				SearchMethodBinding binding = (SearchMethodBinding) methodBindings.get(0);
				SearchParameter param = (SearchParameter) binding.getParameters().get(0);
				assertThat(param.getDescription()).isEqualTo("The organization at which this person is a patient");
				found = true;
			}
		}
		assertThat(found).isTrue();
		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		String conf = validate(conformance);

		CapabilityStatementRestResourceComponent resource = findRestResource(conformance, "Patient");

		CapabilityStatementRestResourceSearchParamComponent param = resource.getSearchParam().get(0);
//		assertEquals("bar", param.getChain().get(0).getValue());
//		assertEquals("foo", param.getChain().get(1).getValue());
//		assertEquals(2, param.getChain().size());
	}

	@Test
	public void testSearchReferenceParameterWithList() throws Exception {

		RestfulServer rsNoType = new RestfulServer(myCtx) {
			@Override
			public RestfulServerConfiguration createConfiguration() {
				RestfulServerConfiguration retVal = super.createConfiguration();
				retVal.setConformanceDate(new InstantDt("2011-02-22T11:22:33Z"));
				return retVal;
			}
		};
		rsNoType.registerProvider(new SearchProviderWithListNoType());
		ServerCapabilityStatementProvider scNoType = new ServerCapabilityStatementProvider(rsNoType);
		rsNoType.setServerConformanceProvider(scNoType);
		rsNoType.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) scNoType.getServerConformance(createHttpServletRequest(), createRequestDetails(rsNoType));
		conformance.setId("");
		String confNoType = validate(conformance);

		RestfulServer rsWithType = new RestfulServer(myCtx) {
			@Override
			public RestfulServerConfiguration createConfiguration() {
				RestfulServerConfiguration retVal = super.createConfiguration();
				retVal.setConformanceDate(new InstantDt("2011-02-22T11:22:33Z"));
				return retVal;
			}
		};
		rsWithType.registerProvider(new SearchProviderWithListWithType());
		ServerCapabilityStatementProvider scWithType = new ServerCapabilityStatementProvider(rsWithType);
		rsWithType.setServerConformanceProvider(scWithType);
		rsWithType.init(createServletConfig());

		CapabilityStatement conformanceWithType = (CapabilityStatement) scWithType.getServerConformance(createHttpServletRequest(), createRequestDetails(rsWithType));
		conformanceWithType.setId("");
		String confWithType = validate(conformanceWithType);

		assertThat(confWithType).isEqualTo(confNoType);
		assertThat(confNoType).contains("<date value=\"2011-02-22T11:22:33Z\"/>");
	}

	@Test
	public void testSystemHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new SystemHistoryProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = validate(conformance);

		assertThat(conf).contains("<interaction><code value=\"" + SystemRestfulInteraction.HISTORYSYSTEM.toCode() + "\"/></interaction>");
	}

	@Test
	public void testTypeHistorySupported() throws Exception {

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new TypeHistoryProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		String conf = validate(conformance);

		assertThat(conf).contains("<interaction><code value=\"" + TypeRestfulInteraction.HISTORYTYPE.toCode() + "\"/></interaction>");
	}

	@Test
	public void testStaticIncludeChains() throws Exception {

		class MyProvider implements IResourceProvider {

			@Override
			public Class<DiagnosticReport> getResourceType() {
				return DiagnosticReport.class;
			}

			@Search
			public List<IBaseResource> search(@RequiredParam(name = DiagnosticReport.SP_PATIENT + "." + Patient.SP_FAMILY) StringParam lastName,
														 @RequiredParam(name = DiagnosticReport.SP_PATIENT + "." + Patient.SP_GIVEN) StringParam firstName,
														 @RequiredParam(name = DiagnosticReport.SP_PATIENT + "." + Patient.SP_BIRTHDATE) DateParam dob,
														 @OptionalParam(name = DiagnosticReport.SP_DATE) DateRangeParam range) {
				return null;
			}

		}

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new MyProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs) {
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement opDef = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		validate(opDef);

		CapabilityStatementRestResourceComponent resource = opDef.getRest().get(0).getResource().get(0);
		assertThat(resource.getType()).isEqualTo("DiagnosticReport");
		List<String> searchParamNames = resource.getSearchParam().stream().map(t -> t.getName()).collect(Collectors.toList());
		assertThat(searchParamNames).containsExactlyInAnyOrder("patient.birthdate", "patient.family", "patient.given", "date");
	}

	@Test
	public void testGraphQLOperation() throws Exception {

		class MyProvider {

			@Description(value = "This operation invokes a GraphQL expression for fetching an joining a graph of resources, returning them in a custom format.")
			@GraphQL(type = RequestTypeEnum.GET)
			public String processGraphQlGetRequest(ServletRequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQueryUrl String queryUrl) {
				throw new IllegalStateException();
			}

			@Description(value = "This operation invokes a GraphQL expression for fetching an joining a graph of resources, returning them in a custom format.")
			@GraphQL(type = RequestTypeEnum.POST)
			public String processGraphQlPostRequest(ServletRequestDetails theRequestDetails, @IdParam IIdType theId, @GraphQLQueryBody String queryBody) {
				throw new IllegalStateException();
			}

		}

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));
		rs.registerProvider(new MyProvider());
		rs.registerProvider(new HashMapResourceProvider<>(myCtx, Patient.class));
		rs.registerProvider(new HashMapResourceProvider<>(myCtx, Observation.class));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs) {
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement opDef = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		validate(opDef);

		// On Patient Resource
		CapabilityStatementRestResourceComponent resource = opDef.getRest().get(0).getResource().stream().filter(t -> t.getType().equals("Patient")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		CapabilityStatementRestResourceOperationComponent graphQlOperation = resource.getOperation().stream().filter(t -> t.getName().equals("graphql")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		assertThat(graphQlOperation.getName()).isEqualTo("graphql");
		assertThat(graphQlOperation.getDefinition()).isEqualTo("http://localhost/baseR4/OperationDefinition/Global-is-graphql");
		assertThat(graphQlOperation.getDocumentation()).isEqualTo("This operation invokes a GraphQL expression for fetching an joining a graph of resources, returning them in a custom format.");

		// On Patient Resource
		resource = opDef.getRest().get(0).getResource().stream().filter(t -> t.getType().equals("Observation")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		graphQlOperation = resource.getOperation().stream().filter(t -> t.getName().equals("graphql")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		assertThat(graphQlOperation.getName()).isEqualTo("graphql");
		assertThat(graphQlOperation.getDefinition()).isEqualTo("http://localhost/baseR4/OperationDefinition/Global-is-graphql");
		assertThat(graphQlOperation.getDocumentation()).isEqualTo("This operation invokes a GraphQL expression for fetching an joining a graph of resources, returning them in a custom format.");

		// At Server Level
		CapabilityStatementRestComponent rest = opDef.getRest().get(0);
		graphQlOperation = rest.getOperation().stream().filter(t -> t.getName().equals("graphql")).findAny().orElseThrow(() -> new IllegalArgumentException());
		assertThat(graphQlOperation.getName()).isEqualTo("graphql");
		assertThat(graphQlOperation.getDefinition()).isEqualTo("http://localhost/baseR4/OperationDefinition/Global-is-graphql");
		assertThat(graphQlOperation.getDocumentation()).isEqualTo("This operation invokes a GraphQL expression for fetching an joining a graph of resources, returning them in a custom format.");

		// Fetch OperationDefinition
		IdType id = new IdType("http://localhost/baseR4/OperationDefinition/Global-is-graphql");
		RequestDetails requestDetails = createRequestDetails(rs);
		OperationDefinition operationDefinition = (OperationDefinition) sc.readOperationDefinition(id, requestDetails);
		assertThat(operationDefinition.getName()).isEqualTo("Graphql");
		assertThat(operationDefinition.getCode()).isEqualTo("graphql");
		assertThat(operationDefinition.getUrl()).isEqualTo("http://localhost/baseR4/OperationDefinition/Global-is-graphql");
		assertThat(operationDefinition.getDescription()).isEqualTo("This operation invokes a GraphQL expression for fetching an joining a graph of resources, returning them in a custom format.");
		assertThat(operationDefinition.getSystem()).isTrue();
		assertThat(operationDefinition.getType()).isFalse();
		assertThat(operationDefinition.getInstance()).isTrue();
	}

	@Test
	public void testPlainProviderGlobalSystemAndInstanceOperations() throws Exception {

		class MyProvider {

			@Description(
				value = "This operation examines two resource versions (can be two versions of the same resource, or two different resources) and generates a FHIR Patch document showing the differences.",
				shortDefinition = "Comparte two resources or two versions of a single resource")
			@Operation(name = ProviderConstants.DIFF_OPERATION_NAME, global = true, idempotent = true)
			public IBaseParameters diff(
				@IdParam IIdType theResourceId,

				@Description(value = "The resource ID and version to diff from", example = "Patient/example/version/1")
				@OperationParam(name = ProviderConstants.DIFF_FROM_VERSION_PARAMETER, typeName = "string", min = 0, max = 1)
					IPrimitiveType<?> theFromVersion,

				@Description(value = "Should differences in the Resource.meta element be included in the diff", example = "false")
				@OperationParam(name = ProviderConstants.DIFF_INCLUDE_META_PARAMETER, typeName = "boolean", min = 0, max = 1)
					IPrimitiveType<Boolean> theIncludeMeta,
				RequestDetails theRequestDetails) {
				throw new IllegalStateException();
			}

			@Description("This operation examines two resource versions (can be two versions of the same resource, or two different resources) and generates a FHIR Patch document showing the differences.")
			@Operation(name = ProviderConstants.DIFF_OPERATION_NAME, idempotent = true)
			public IBaseParameters diff(
				@Description(value = "The resource ID and version to diff from", example = "Patient/example/version/1")
				@OperationParam(name = ProviderConstants.DIFF_FROM_PARAMETER, typeName = "id", min = 1, max = 1)
					IIdType theFromVersion,

				@Description(value = "The resource ID and version to diff to", example = "Patient/example/version/2")
				@OperationParam(name = ProviderConstants.DIFF_TO_PARAMETER, typeName = "id", min = 1, max = 1)
					IIdType theToVersion,

				@Description(value = "Should differences in the Resource.meta element be included in the diff", example = "false")
				@OperationParam(name = ProviderConstants.DIFF_INCLUDE_META_PARAMETER, typeName = "boolean", min = 0, max = 1)
					IPrimitiveType<Boolean> theIncludeMeta,
				RequestDetails theRequestDetails) {
				throw new IllegalStateException();
			}

		}

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));
		rs.registerProvider(new MyProvider());
		rs.registerProvider(new HashMapResourceProvider<>(myCtx, Patient.class));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs) {
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement opDef = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		validate(opDef);

		// On Patient Resource
		CapabilityStatementRestResourceComponent resource = opDef.getRest().get(0).getResource().stream().filter(t -> t.getType().equals("Patient")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		CapabilityStatementRestResourceOperationComponent operation = resource.getOperation().stream().filter(t -> t.getName().equals("diff")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		assertThat(operation.getName()).isEqualTo("diff");
		assertThat(operation.getDefinition()).isEqualTo("http://localhost/baseR4/OperationDefinition/Global-is-diff");
		assertThat(operation.getDocumentation()).isEqualTo("This operation examines two resource versions (can be two versions of the same resource, or two different resources) and generates a FHIR Patch document showing the differences.");

		// At Server Level
		CapabilityStatementRestComponent rest = opDef.getRest().get(0);
		operation = rest.getOperation().stream().filter(t -> t.getName().equals("diff")).findAny().orElse(null);
		assertThat(operation.getName()).isEqualTo("diff");
		assertThat(operation.getDefinition()).isEqualTo("http://localhost/baseR4/OperationDefinition/Global-is-diff");
		assertThat(operation.getDocumentation()).isEqualTo("This operation examines two resource versions (can be two versions of the same resource, or two different resources) and generates a FHIR Patch document showing the differences.");

		// Fetch OperationDefinition
		IdType id = new IdType("http://localhost/baseR4/OperationDefinition/Global-is-diff");
		RequestDetails requestDetails = createRequestDetails(rs);
		OperationDefinition operationDefinition = (OperationDefinition) sc.readOperationDefinition(id, requestDetails);
		assertThat(operationDefinition.getName()).isEqualTo("Diff");
		assertThat(operationDefinition.getCode()).isEqualTo("diff");
		assertThat(operationDefinition.getUrl()).isEqualTo("http://localhost/baseR4/OperationDefinition/Global-is-diff");
		assertThat(operationDefinition.getDescription()).isEqualTo("This operation examines two resource versions (can be two versions of the same resource, or two different resources) and generates a FHIR Patch document showing the differences.");
		assertThat(operationDefinition.getSystem()).isTrue();
		assertThat(operationDefinition.getType()).isFalse();
		assertThat(operationDefinition.getInstance()).isTrue();
	}

	@Test
	public void testResourceProviderTypeAndInstanceLevelOperations() throws Exception {

		class MyProvider implements IResourceProvider {

			@Description("This is the expunge operation")
			@Operation(name = "expunge", idempotent = false, returnParameters = {
				@OperationParam(name = "count", typeName = "integer")
			})
			public IBaseParameters expunge(
				@IdParam IIdType theIdParam,
				@OperationParam(name = "limit", typeName = "integer") IPrimitiveType<Integer> theLimit,
				@OperationParam(name = "deleted", typeName = "boolean") IPrimitiveType<Boolean> theExpungeDeletedResources,
				@OperationParam(name = "previous", typeName = "boolean") IPrimitiveType<Boolean> theExpungeOldVersions,
				RequestDetails theRequest) {
				throw new UnsupportedOperationException();
			}

			@Description("This is the expunge operation")
			@Operation(name = "expunge", idempotent = false, returnParameters = {
				@OperationParam(name = "count", typeName = "integer")
			})
			public IBaseParameters expunge(
				@OperationParam(name = "limit", typeName = "integer") IPrimitiveType<Integer> theLimit,
				@OperationParam(name = "deleted", typeName = "boolean") IPrimitiveType<Boolean> theExpungeDeletedResources,
				@OperationParam(name = "previous", typeName = "boolean") IPrimitiveType<Boolean> theExpungeOldVersions,
				RequestDetails theRequest) {
				throw new UnsupportedOperationException();
			}

			@Override
			public Class<? extends IBaseResource> getResourceType() {
				return Patient.class;
			}
		}

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));
		rs.registerProvider(new MyProvider());
		rs.registerProvider(new HashMapResourceProvider<>(myCtx, Patient.class));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs) {
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement opDef = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		validate(opDef);

		// On Patient Resource
		CapabilityStatementRestResourceComponent resource = opDef.getRest().get(0).getResource().stream().filter(t -> t.getType().equals("Patient")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		List<CapabilityStatementRestResourceOperationComponent> expungeResourceOperations = resource.getOperation().stream().filter(t -> t.getName().equals("expunge")).collect(Collectors.toList());
		assertThat(expungeResourceOperations).hasSize(1);
		CapabilityStatementRestResourceOperationComponent operation = expungeResourceOperations.get(0);
		assertThat(operation.getName()).isEqualTo("expunge");
		assertThat(operation.getDefinition()).isEqualTo("http://localhost/baseR4/OperationDefinition/Patient-it-expunge");
		assertThat(operation.getDocumentation()).isEqualTo("This is the expunge operation");

		// At Server Level
		CapabilityStatementRestComponent rest = opDef.getRest().get(0);
		operation = rest.getOperation().stream().filter(t -> t.getName().equals("expunge")).findAny().orElse(null);
		assertThat(operation).isNull();

		// Fetch OperationDefinition
		IdType id = new IdType("http://localhost/baseR4/OperationDefinition/Patient-it-expunge");
		RequestDetails requestDetails = createRequestDetails(rs);
		OperationDefinition operationDefinition = (OperationDefinition) sc.readOperationDefinition(id, requestDetails);
		assertThat(operationDefinition.getName()).isEqualTo("Expunge");
		assertThat(operationDefinition.getCode()).isEqualTo("expunge");
		assertThat(operationDefinition.getUrl()).isEqualTo("http://localhost/baseR4/OperationDefinition/Patient-it-expunge");
		assertThat(operationDefinition.getDescription()).isEqualTo("This is the expunge operation");
		assertThat(operationDefinition.getSystem()).isFalse();
		assertThat(operationDefinition.getType()).isTrue();
		assertThat(operationDefinition.getInstance()).isTrue();

	}

	@Test
	public void testIncludeLastUpdatedSearchParam() throws Exception {

		class MyProvider implements IResourceProvider {

			@Override
			public Class<DiagnosticReport> getResourceType() {
				return DiagnosticReport.class;
			}

			@Search
			public List<IBaseResource> search(@OptionalParam(name = DiagnosticReport.SP_DATE)
															 DateRangeParam range,

														 @Description(shortDefinition = "Only return resources which were last updated as specified by the given range")
														 @OptionalParam(name = "_lastUpdated")
															 DateRangeParam theLastUpdated
			) {
				return null;
			}

		}

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new MyProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs) {
		};
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement opDef = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		validate(opDef);

		CapabilityStatementRestResourceComponent resource = opDef.getRest().get(0).getResource().get(0);
		assertThat(resource.getType()).isEqualTo("DiagnosticReport");
		List<String> searchParamNames = resource.getSearchParam().stream().map(t -> t.getName()).collect(Collectors.toList());
		assertThat(searchParamNames).containsExactlyInAnyOrder("date", "_lastUpdated");
	}

	@Test
	public void testSystemLevelNamedQueryWithParameters() throws Exception {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new NamedQueryPlainProvider());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		validate(conformance);

		CapabilityStatementRestResourceComponent patient = conformance.getRestFirstRep().getResource().stream().filter(t -> t.getType().equals("Patient")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		CapabilityStatementRestResourceOperationComponent operationComponent = patient.getOperation().get(0);
		assertThat(operationComponent.getName()).isEqualTo(NamedQueryPlainProvider.QUERY_NAME);

		String operationReference = operationComponent.getDefinition();
		assertThat(operationReference).isNotNull();

		OperationDefinition operationDefinition = (OperationDefinition) sc.readOperationDefinition(new IdType(operationReference), createRequestDetails(rs));
		ourLog.debug(myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationDefinition));
		validate(operationDefinition);
		assertThat(operationDefinition.getCode()).isEqualTo(NamedQueryPlainProvider.QUERY_NAME);
		assertThat(operationDefinition.getName()).isEqualTo("TestQuery");
		assertThat(operationDefinition.getStatus()).isEqualTo(PublicationStatus.ACTIVE);
		assertThat(operationDefinition.getKind()).isEqualTo(OperationKind.QUERY);
		assertThat(operationDefinition.getDescription()).isEqualTo(NamedQueryPlainProvider.DESCRIPTION);
		assertThat(operationDefinition.getAffectsState()).isEqualTo(false);
		assertThat(operationDefinition.getResource()).as("A system level search has no target resources").isEmpty();
		assertThat(operationDefinition.getSystem()).isEqualTo(true);
		assertThat(operationDefinition.getType()).isEqualTo(false);
		assertThat(operationDefinition.getInstance()).isEqualTo(false);
		List<OperationDefinitionParameterComponent> parameters = operationDefinition.getParameter();
		assertThat(parameters).hasSize(1);
		OperationDefinitionParameterComponent param = parameters.get(0);
		assertThat(param.getName()).isEqualTo(NamedQueryPlainProvider.SP_QUANTITY);
		assertThat(param.getType()).isEqualTo("string");
		assertThat(param.getSearchTypeElement().asStringValue()).isEqualTo(RestSearchParameterTypeEnum.QUANTITY.getCode());
		assertThat(param.getMin()).isEqualTo(1);
		assertThat(param.getMax()).isEqualTo("1");
		assertThat(param.getUse()).isEqualTo(OperationParameterUse.IN);
	}

	@Test
	public void testResourceLevelNamedQueryWithParameters() throws Exception {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new NamedQueryResourceProvider());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		validate(conformance);

		CapabilityStatementRestResourceComponent restComponent = conformance.getRestFirstRep().getResource().stream().filter(t -> t.getType().equals("Patient")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		CapabilityStatementRestResourceOperationComponent operationComponent = restComponent.getOperation().get(0);
		String operationReference = operationComponent.getDefinition();
		assertThat(operationReference).isNotNull();

		OperationDefinition operationDefinition = (OperationDefinition) sc.readOperationDefinition(new IdType(operationReference), createRequestDetails(rs));
		ourLog.debug(myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationDefinition));
		validate(operationDefinition);
		assertThat(operationDefinition.getName()).as("The operation name should be the code if no description is set").isEqualTo("TestQuery");
		String patientResourceName = "Patient";
		assertThat(operationDefinition.getResource().get(0).getValue()).as("A resource level search targets the resource of the provider it's defined in").isEqualTo(patientResourceName);
		assertThat(operationDefinition.getSystem()).isEqualTo(false);
		assertThat(operationDefinition.getType()).isEqualTo(true);
		assertThat(operationDefinition.getInstance()).isEqualTo(false);
		List<OperationDefinitionParameterComponent> parameters = operationDefinition.getParameter();
		assertThat(parameters).hasSize(1);
		OperationDefinitionParameterComponent param = parameters.get(0);
		assertThat(param.getName()).isEqualTo(NamedQueryResourceProvider.SP_PARAM);
		assertThat(param.getType()).isEqualTo("string");
		assertThat(param.getSearchTypeElement().asStringValue()).isEqualTo(RestSearchParameterTypeEnum.STRING.getCode());
		assertThat(param.getMin()).isEqualTo(0);
		assertThat(param.getMax()).isEqualTo("1");
		assertThat(param.getUse()).isEqualTo(OperationParameterUse.IN);

		List<CapabilityStatementRestResourceSearchParamComponent> patientResource = restComponent.getSearchParam();
		assertThat(patientResource).as("Named query parameters should not appear in the resource search params").isEmpty();
	}

	@Test
	public void testExtendedOperationAtTypeLevel() throws Exception {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new TypeLevelOperationProvider());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));

		validate(conformance);

		CapabilityStatementRestResourceComponent patient = conformance.getRestFirstRep().getResource().stream().filter(t -> t.getType().equals("Patient")).findFirst().orElseThrow(() -> new IllegalArgumentException());
		List<CapabilityStatementRestResourceOperationComponent> operations = patient.getOperation();
		assertThat(operations).hasSize(1);
		assertThat(operations.get(0).getName()).isEqualTo(TypeLevelOperationProvider.OPERATION_NAME);

		OperationDefinition opDef = (OperationDefinition) sc.readOperationDefinition(new IdType(operations.get(0).getDefinition()), createRequestDetails(rs));
		validate(opDef);
		assertThat(opDef.getCode()).isEqualTo(TypeLevelOperationProvider.OPERATION_NAME);
		assertThat(opDef.getSystem()).isEqualTo(false);
		assertThat(opDef.getType()).isEqualTo(true);
		assertThat(opDef.getInstance()).isEqualTo(false);
	}

	@Test
	public void testProfiledResourceStructureDefinitionLinks() throws Exception {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setResourceProviders(new ProfiledPatientProvider(), new MultipleProfilesPatientProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		ourLog.debug(myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance));

		List<CapabilityStatementRestResourceComponent> resources = conformance.getRestFirstRep().getResource();
		CapabilityStatementRestResourceComponent patientResource = resources.stream()
			.filter(resource -> "Patient".equals(resource.getType()))
			.findFirst().get();
		assertThat(patientResource.getProfile()).contains(PATIENT_SUB);
	}

	@Test
	public void testRevIncludes_Explicit() throws Exception {

		class PatientResourceProvider implements IResourceProvider {

			@Override
			public Class<Patient> getResourceType() {
				return Patient.class;
			}

			@Search
			public List<Patient> search(@IncludeParam(reverse = true, allow = {"Observation:foo", "Provenance:bar"}) Set<Include> theRevIncludes) {
				return Collections.emptyList();
			}

		}

		class ObservationResourceProvider implements IResourceProvider {

			@Override
			public Class<Observation> getResourceType() {
				return Observation.class;
			}

			@Search
			public List<Observation> search(@OptionalParam(name = "subject") ReferenceParam theSubject) {
				return Collections.emptyList();
			}

		}

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setResourceProviders(new PatientResourceProvider(), new ObservationResourceProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		sc.setRestResourceRevIncludesEnabled(true);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		ourLog.debug(myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance));

		List<CapabilityStatementRestResourceComponent> resources = conformance.getRestFirstRep().getResource();
		CapabilityStatementRestResourceComponent patientResource = resources.stream()
			.filter(resource -> "Patient".equals(resource.getType()))
			.findFirst().get();
		assertThat(toStrings(patientResource.getSearchRevInclude())).containsExactlyInAnyOrder("Observation:foo", "Provenance:bar");
	}

	@Test
	public void testRevIncludes_Inferred() throws Exception {

		class PatientResourceProvider implements IResourceProvider {

			@Override
			public Class<Patient> getResourceType() {
				return Patient.class;
			}

			@Search
			public List<Patient> search(@IncludeParam(reverse = true) Set<Include> theRevIncludes) {
				return Collections.emptyList();
			}

		}

		class ObservationResourceProvider implements IResourceProvider {

			@Override
			public Class<Observation> getResourceType() {
				return Observation.class;
			}

			@Search
			public List<Observation> search(@OptionalParam(name = "subject") ReferenceParam  theSubject) {
				return Collections.emptyList();
			}

		}

		RestfulServer rs = new RestfulServer(myCtx);
		rs.setResourceProviders(new PatientResourceProvider(), new ObservationResourceProvider());

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		sc.setRestResourceRevIncludesEnabled(true);
		rs.setServerConformanceProvider(sc);

		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		ourLog.debug(myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(conformance));

		List<CapabilityStatementRestResourceComponent> resources = conformance.getRestFirstRep().getResource();
		CapabilityStatementRestResourceComponent patientResource = resources.stream()
			.filter(resource -> "Patient".equals(resource.getType()))
			.findFirst().get();
		assertThat(toStrings(patientResource.getSearchRevInclude())).containsExactlyInAnyOrder("Observation:subject");
	}

	@Test
	public void testBulkDataExport() throws ServletException {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new BulkDataExportProvider());
		rs.setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost/baseR4"));

		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs);
		rs.setServerConformanceProvider(sc);
		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(conformance));

		List<CapabilityStatementRestResourceComponent> resources = conformance.getRestFirstRep().getResource();
		CapabilityStatementRestResourceComponent groupResource = resources.stream()
			.filter(resource -> "Group".equals(resource.getType()))
			.findFirst().get();

		boolean hasExportPollStatusOperationAtSystemLevel = conformance.getRestFirstRep().getOperation().stream()
			.filter(CapabilityStatementRestResourceOperationComponent::hasName)
			.map(CapabilityStatementRestResourceOperationComponent::getName)
			.filter("export-poll-status"::equals).findFirst().isPresent();

		assertThat(toOperationNames(groupResource.getOperation())).containsExactly("export");
		assertThat(hasExportPollStatusOperationAtSystemLevel).isEqualTo(true);
	}

	@Test
	public void testOperationReturningCanonicalUrl() throws Exception {
		RestfulServer rs = new RestfulServer(myCtx);
		rs.setProviders(new ProviderWithOperationReturningCanonicalUrl());
		ServerCapabilityStatementProvider sc = new ServerCapabilityStatementProvider(rs) {};
		rs.setServerConformanceProvider(sc);
		rs.init(createServletConfig());

		CapabilityStatement conformance = (CapabilityStatement) sc.getServerConformance(createHttpServletRequest(), createRequestDetails(rs));
		assertThat(conformance.getRest().get(0).getResource()).hasSize(2);
		List<CapabilityStatementRestResourceOperationComponent> res = conformance.getRest().get(0).getResource().get(1).getOperation();
		assertThat(res).hasSize(1);
		CapabilityStatementRestResourceOperationComponent operationComponent = res.get(0);
		assertThat(operationComponent.getName()).isEqualTo("everything");
		assertThat(operationComponent.getDefinition()).isEqualTo("http://hl7.org/fhir/OperationDefinition/Patient-everything");
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

	private List<String> toOperationDefinitions(List<CapabilityStatementRestResourceOperationComponent> theOperation) {
		ArrayList<String> retVal = Lists.newArrayList();
		for (CapabilityStatementRestResourceOperationComponent next : theOperation) {
			retVal.add(next.getDefinition());
		}
		return retVal;
	}

	private String validate(IBaseResource theResource) {
		String conf = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(theResource);
		ourLog.info("Def:\n{}", conf);

		ValidationResult result = myValidator.validateWithResult(conf);
		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		String outcome = myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info("Outcome: {}", outcome);

		assertThat(result.isSuccessful()).as(outcome).isTrue();
		List<OperationOutcome.OperationOutcomeIssueComponent> warningsAndErrors = operationOutcome
			.getIssue()
			.stream()
			.filter(t -> t.getSeverity().ordinal() <= OperationOutcome.IssueSeverity.WARNING.ordinal()) // <= because this enum has a strange order
			.collect(Collectors.toList());
		assertThat(warningsAndErrors).as(outcome).isEmpty();

		return myCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(theResource);
	}

	public static class BulkDataExportProvider {
		@Operation(name = ProviderConstants.OPERATION_EXPORT, global = false /* set to true once we can handle this */, manualResponse = true, idempotent = true)
		public void export(
			@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theType,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant") IPrimitiveType<Date> theSince,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE_FILTER, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theTypeFilter,
			ServletRequestDetails theRequestDetails
		) {
		}

		@Operation(name = ProviderConstants.OPERATION_EXPORT, manualResponse = true, idempotent = true, typeName = "Group")
		public void groupExport(
			@IdParam IIdType theIdParam,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theType,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant") IPrimitiveType<Date> theSince,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE_FILTER, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theTypeFilter,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_MDM, min = 0, max = 1, typeName = "boolean") IPrimitiveType<Boolean> theMdm,
			ServletRequestDetails theRequestDetails
		) {
		}

		@Operation(name = ProviderConstants.OPERATION_EXPORT, manualResponse = true, idempotent = true, typeName = "Patient")
		public void patientExport(
			@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theType,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant") IPrimitiveType<Date> theSince,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE_FILTER, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theTypeFilter,
			ServletRequestDetails theRequestDetails
		) {
		}

		@Operation(name = ProviderConstants.OPERATION_EXPORT_POLL_STATUS, manualResponse = true, idempotent = true)
		public void exportPollStatus(
			@OperationParam(name = JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID, typeName = "string", min = 0, max = 1) IPrimitiveType<String> theJobId,
			ServletRequestDetails theRequestDetails
		) throws IOException {
		}

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
		public IBundleProvider everything(
			HttpServletRequest theServletRequest,

			@IdParam IdType theId,

			@Description(value = "Start description", example = {"2001", "2002"})
			@OperationParam(name = "someOpParam1") DateType theStart,

			@OperationParam(name = "someOpParam2") Patient theEnd) {
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
																						 @IncludeParam(allow = {"DiagnosticReport.result"}) Set<Include> theIncludes) throws Exception {
			return null;
		}

	}

	public static class ProviderWithOperationReturningCanonicalUrl implements IResourceProvider {
		@Operation(name = "everything", canonicalUrl = "http://hl7.org/fhir/OperationDefinition/Patient-everything", idempotent = true)
		public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdType theId,
													 @OperationParam(name = "someOpParam1") DateType theStart, @OperationParam(name = "someOpParam2") Encounter theEnd) {
			return null;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
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
			@Description(shortDefinition = "All patients linked to the given patient") @OptionalParam(name = "link", targetTypes = {Patient.class}) ReferenceAndListParam theLink) {
			return null;
		}

	}

	@SuppressWarnings("unused")
	public static class SearchProviderWithWhitelist {

		@Search(type = Patient.class)
		public Patient findPatient1(@Description(shortDefinition = "The organization at which this person is a patient") @RequiredParam(name = Patient.SP_ORGANIZATION, chainWhitelist = {"foo",
			"bar"}) ReferenceAndListParam theIdentifier) {
			return null;
		}

	}

	@SuppressWarnings("unused")
	public static class SearchProviderWithListNoType implements IResourceProvider {

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
	public static class SearchProviderWithListWithType implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}


		@Search(type = Patient.class)
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

		@Search(queryName = QUERY_NAME, type = Patient.class)
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

	@ResourceDef(id = PATIENT_SUB)
	public static class PatientSub extends Patient {
	}

	@ResourceDef(id = PATIENT_SUB_SUB)
	public static class PatientSubSub extends PatientSub {
	}

	@ResourceDef(id = PATIENT_SUB_SUB_2)
	public static class PatientSubSub2 extends PatientSub {
	}

	@ResourceDef(id = PATIENT_TRIPLE_SUB)
	public static class PatientTripleSub extends PatientSubSub {
	}

}
