package ca.uhn.fhir.jaxrs.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.client.JaxRsRestfulClientFactory;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsResponseException;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsConformanceRestProviderDstu3;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsMockPageProviderDstu3;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsMockPatientRestProviderDstu3;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.SearchStyleEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class AbstractJaxRsResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AbstractJaxRsResourceProviderDstu3Test.class);
	private static final String PATIENT_NAME = "Van Houte";
	private static IGenericClient client;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static int ourPort;
	private static String serverBase;
	private static Server jettyServer;
	private TestJaxRsMockPatientRestProviderDstu3 mock;
	private ArgumentCaptor<IdType> idCaptor;
	private ArgumentCaptor<String> conditionalCaptor;
	private ArgumentCaptor<Patient> patientCaptor;

	private void compareResultId(int id, IBaseResource resource) {
		assertEquals(id, Integer.parseInt(resource.getIdElement().getIdPart()));
	}

	private void compareResultUrl(String url, IBaseResource resource) {
		assertEquals(url, resource.getIdElement().getValueAsString().substring(serverBase.length() - 1));
	}

	private Patient createPatient(long id) {
		Patient theResource = new Patient();
		theResource.setId(new IdType(id));
		return theResource;
	}

	private List<Patient> createPatients(int firstId, int lastId) {
		List<Patient> result = new ArrayList<Patient>(lastId - firstId);
		for (long i = firstId; i <= lastId; i++) {
			result.add(createPatient(i));
		}
		return result;
	}

	/**
	 * Find By Id
	 */
	@Test
	public void findUsingGenericClientById() {
		when(mock.find(any(IdType.class))).thenReturn(createPatient(1));
		Patient result = client.read(Patient.class, "1");
		compareResultId(1, result);
		compareResultUrl("/Patient/1", result);
		reset(mock);
		when(mock.find(eq(result.getIdElement()))).thenReturn(createPatient(1));
		result = (Patient) client.read(new UriDt(result.getId()));
		compareResultId(1, result);
		compareResultUrl("/Patient/1", result);
	}

	private Bundle getPatientBundle(int size) {
		Bundle result = new Bundle();
		for (long i = 0; i < size; i++) {
			Patient patient = createPatient(i);
			BundleEntryComponent entry = new BundleEntryComponent();
			entry.setResource(patient);
			result.addEntry(entry);
		}
		return result;
	}

	@BeforeEach
	public void setUp() {
		this.mock = TestJaxRsMockPatientRestProviderDstu3.mock;
		idCaptor = ArgumentCaptor.forClass(IdType.class);
		patientCaptor = ArgumentCaptor.forClass(Patient.class);
		conditionalCaptor = ArgumentCaptor.forClass(String.class);
		reset(mock);
	}

	/**
	 * Conditional Creates
	 */
	@Test
	public void testConditionalCreate() throws Exception {
		Patient toCreate = createPatient(1);
		MethodOutcome outcome = new MethodOutcome();
		toCreate.getIdentifier().add(new Identifier().setValue("myIdentifier"));
		outcome.setResource(toCreate);

		when(mock.create(patientCaptor.capture(), eq("/Patient?_format=json&identifier=2"))).thenReturn(outcome);
		client.setEncoding(EncodingEnum.JSON);

		MethodOutcome response = client.create().resource(toCreate).conditional()
			.where(Patient.IDENTIFIER.exactly().identifier("2")).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals("myIdentifier", patientCaptor.getValue().getIdentifier().get(0).getValue());
		IBaseResource resource = response.getResource();
		compareResultId(1, resource);
	}

	/**
	 * Conformance - Server
	 */
	@Test
	public void testConformance() {
		final CapabilityStatement conf = client.fetchConformance().ofType(CapabilityStatement.class).execute();
		assertEquals(conf.getRest().get(0).getResource().get(0).getType(), "Patient");
	}

	@Test
	public void testCreatePatient() throws Exception {
		Patient toCreate = createPatient(1);
		MethodOutcome outcome = new MethodOutcome();
		toCreate.getIdentifier().add(new Identifier().setValue("myIdentifier"));
		outcome.setResource(toCreate);

		when(mock.create(patientCaptor.capture(), isNull())).thenReturn(outcome);
		client.setEncoding(EncodingEnum.JSON);
		final MethodOutcome response = client.create().resource(toCreate).prefer(PreferReturnEnum.REPRESENTATION)
			.execute();
		IBaseResource resource = response.getResource();
		compareResultId(1, resource);
		assertEquals("myIdentifier", patientCaptor.getValue().getIdentifier().get(0).getValue());
	}

	@Test
	public void testDeletePatient() {
		when(mock.delete(idCaptor.capture(), conditionalCaptor.capture())).thenReturn(new MethodOutcome());
		final IBaseOperationOutcome results = client.delete().resourceById("Patient", "1").execute().getOperationOutcome();
		assertEquals("1", idCaptor.getValue().getIdPart());
	}

	@Test
	public void testConditionalDelete() {
		when(mock.delete(idCaptor.capture(), conditionalCaptor.capture())).thenReturn(new MethodOutcome());
		client.delete().resourceConditionalByType("Patient").where(Patient.IDENTIFIER.exactly().identifier("2")).execute();
		assertEquals("Patient?identifier=2&_format=json", conditionalCaptor.getValue());
	}

	/**
	 * Extended Operations
	 */
	@Test
	public void testExtendedOperations() {
		// prepare mock
		Parameters resultParameters = new Parameters();
		resultParameters.addParameter().setName("return").setResource(createPatient(1)).setValue(new StringType("outputValue"));
		when(mock.someCustomOperation(any(IdType.class), argThat(new StringTypeMatcher(new StringType("myAwesomeDummyValue"))))).thenReturn(resultParameters);
		// Create the input parameters to pass to the server
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("start").setValue(new DateType("2001-01-01"));
		inParams.addParameter().setName("end").setValue(new DateType("2015-03-01"));
		inParams.addParameter().setName("dummy").setValue(new StringType("myAwesomeDummyValue"));
		//invoke
		Parameters outParams = client.operation().onInstance(new IdType("Patient", "1")).named("$someCustomOperation")
			.withParameters(inParams).execute();
		//verify
		assertEquals("outputValue", ((StringType) outParams.getParameter().get(0).getValue()).getValueAsString());
	}

	@Test
	public void testExtendedOperationsUsingGet() {
		// prepare mock
		Parameters resultParameters = new Parameters();
		resultParameters.addParameter().setName("return").setResource(createPatient(1)).setValue(new StringType("outputValue"));
		when(mock.someCustomOperation(any(IdType.class), argThat(new StringTypeMatcher(new StringType("myAwesomeDummyValue"))))).thenReturn(resultParameters);
		// Create the input parameters to pass to the server
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("start").setValue(new DateType("2001-01-01"));
		inParams.addParameter().setName("end").setValue(new DateType("2015-03-01"));
		inParams.addParameter().setName("dummy").setValue(new StringType("myAwesomeDummyValue"));

		// invoke
		Parameters outParams = client
			.operation()
			.onInstance(new IdType("Patient", "1"))
			.named("$someCustomOperation")
			.withParameters(inParams)
			.useHttpGet()
			.execute();

		// verify
		assertEquals("outputValue", ((StringType) outParams.getParameter().get(0).getValue()).getValueAsString());
	}

	@Test
	public void testRead() {
		when(mock.find(idCaptor.capture())).thenReturn(createPatient(1));
		final Patient patient = client.read(Patient.class, "1");
		compareResultId(1, patient);
		compareResultUrl("/Patient/1", patient);
		assertEquals("1", idCaptor.getValue().getIdPart());
	}

	/**
	 * Search - Compartments
	 */
	@Test
	public void testSearchCompartments() {
		when(mock.searchCompartment(any(IdType.class))).thenReturn(Arrays.asList((IBaseResource) createPatient(1)));
		org.hl7.fhir.dstu3.model.Bundle response = client.search().forResource(Patient.class).withIdAndCompartment("1", "Condition")
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class).execute();
		Resource resource = response.getEntry().get(0).getResource();
		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
	}

	/**
	 *
	 */
	@Test
	public void testSearchPost() {
		when(mock.search(ArgumentMatchers.isNull(), ArgumentMatchers.isNull()))
			.thenReturn(createPatients(1, 13));
		org.hl7.fhir.dstu3.model.Bundle result = client.search().forResource("Patient").usingStyle(SearchStyleEnum.POST)
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class).execute();
		Resource resource = result.getEntry().get(0).getResource();
		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
	}

	/**
	 * Search/Query - Type
	 */
	@Test
	public void testSearchUsingGenericClientBySearch() {
		// Perform a search
		when(mock.search(any(StringParam.class), isNull()))
			.thenReturn(Arrays.asList(createPatient(1)));
		final Bundle results = client.search().forResource(Patient.class)
			.where(Patient.NAME.matchesExactly().value(PATIENT_NAME)).returnBundle(Bundle.class).execute();
		verify(mock).search(any(StringParam.class), isNull());
		IBaseResource resource = results.getEntry().get(0).getResource();

		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
	}

	/**
	 * Search - Multi-valued Parameters (ANY/OR)
	 */
	@Test
	public void testSearchUsingGenericClientBySearchWithMultiValues() {
		when(mock.search(any(StringParam.class), ArgumentMatchers.notNull()))
			.thenReturn(Arrays.asList(createPatient(1)));
		final Bundle results = client.search().forResource(Patient.class)
			.where(Patient.ADDRESS.matches().values("Toronto")).and(Patient.ADDRESS.matches().values("Ontario"))
			.and(Patient.ADDRESS.matches().values("Canada"))
			.where(Patient.NAME.matches().value("SHORTNAME")).returnBundle(Bundle.class).execute();
		IBaseResource resource = results.getEntry().get(0).getResource();

		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
	}

	/**
	 * Search - Paging
	 */
	@Test
	public void testSearchWithPaging() {
		// Perform a search
		when(mock.search(ArgumentMatchers.isNull(), ArgumentMatchers.isNull()))
			.thenReturn(createPatients(1, 13));
		final org.hl7.fhir.dstu3.model.Bundle results = client.search().forResource(Patient.class).count(8).returnBundle(org.hl7.fhir.dstu3.model.Bundle.class)
			.execute();

		assertEquals(results.getEntry().size(), 8);
		IBaseResource resource = results.getEntry().get(0).getResource();
		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
		compareResultId(8, results.getEntry().get(7).getResource());

//		ourLog.info("Next: " + results.getLink("next").getUrl());
//		String url = results.getLink("next").getUrl().replace("?", "Patient?");
//		results.getLink("next").setUrl(url);
//		ourLog.info("New Next: " + results.getLink("next").getUrl());

		// load next page
		final org.hl7.fhir.dstu3.model.Bundle nextPage = client.loadPage().next(results).execute();
		resource = nextPage.getEntry().get(0).getResource();
		compareResultId(9, resource);
		compareResultUrl("/Patient/9", resource);
		assertNull(nextPage.getLink(org.hl7.fhir.dstu3.model.Bundle.LINK_NEXT));
	}

	/**
	 * Search - Subsetting (_summary and _elements)
	 */
	@Test
	@Disabled
	public void testSummary() {
		Object response = client.search().forResource(Patient.class)
			.returnBundle(org.hl7.fhir.dstu3.model.Bundle.class).execute();
	}

	/**
	 * Transaction - Server
	 */
//	@Disabled
//	@Test
//	public void testTransaction() {
//		ca.uhn.fhir.model.api.Bundle bundle = new ca.uhn.fhir.model.api.Bundle();
//		BundleEntry entry = bundle.addEntry();
//		final Patient existing = new Patient();
//		existing.getName().get(0).addFamily("Created with bundle");
//		entry.setResource(existing);
//
//		BoundCodeDt<BundleEntryTransactionMethodEnum> theTransactionOperation = new BoundCodeDt(
//				BundleEntryTransactionMethodEnum.VALUESET_BINDER, BundleEntryTransactionMethodEnum.POST);
//		entry.setTransactionMethod(theTransactionOperation);
//		ca.uhn.fhir.model.api.Bundle response = client.transaction().withBundle(bundle).execute();
//	}
	@Test
	public void testUpdateById() throws Exception {
		when(mock.update(idCaptor.capture(), patientCaptor.capture(), conditionalCaptor.capture())).thenReturn(new MethodOutcome());
		client.update("1", createPatient(1));
		assertEquals("1", idCaptor.getValue().getIdPart());
		compareResultId(1, patientCaptor.getValue());
	}

	@Test
	public void testConditionalUpdate() throws Exception {
		when(mock.update(idCaptor.capture(), patientCaptor.capture(), conditionalCaptor.capture())).thenReturn(new MethodOutcome());
		client.update().resource(createPatient(1)).conditional().where(Patient.IDENTIFIER.exactly().identifier("2")).execute();

		assertEquals(null, patientCaptor.getValue().getIdElement().getIdPart());
		assertEquals(null, patientCaptor.getValue().getIdElement().getVersionIdPart());
		assertEquals("Patient?identifier=2&_format=json", conditionalCaptor.getValue());
	}

	@SuppressWarnings("unchecked")
	@Disabled
	@Test
	public void testResourceNotFound() throws Exception {
		when(mock.update(idCaptor.capture(), patientCaptor.capture(), conditionalCaptor.capture())).thenThrow(ResourceNotFoundException.class);
		try {
			client.update("1", createPatient(2));
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	@Test
	public void testVRead() {
		when(mock.findHistory(idCaptor.capture())).thenReturn(createPatient(1));
		final Patient patient = client.vread(Patient.class, "1", "2");
		compareResultId(1, patient);
		compareResultUrl("/Patient/1", patient);
		assertEquals("1", idCaptor.getValue().getIdPart());
		assertEquals("2", idCaptor.getValue().getVersionIdPart());
	}

	@Test
	public void testXFindUnknownPatient() {
		try {
			JaxRsResponseException notFoundException = new JaxRsResponseException(new ResourceNotFoundException(new IdType("999955541264")));
			when(mock.find(idCaptor.capture())).thenThrow(notFoundException);
			client.read(Patient.class, "999955541264");
			fail();
		} catch (final ResourceNotFoundException e) {
			assertEquals(ResourceNotFoundException.STATUS_CODE, e.getStatusCode());
			assertTrue(e.getMessage().contains("999955541264"));
		}
	}

	@Test
	public void testValidate() {
		// prepare mock
		final OperationOutcome oo = new OperationOutcome();
		final Patient patient = new Patient();
		patient.addIdentifier((new Identifier().setValue("1")));
		//invoke
		final Parameters inParams = new Parameters();
		inParams.addParameter().setResource(patient);

		final MethodOutcome mO = client.validate().resource(patient).execute();
		//verify
		assertNotNull(mO.getOperationOutcome());
	}

	class StringTypeMatcher implements ArgumentMatcher<StringType> {
		private StringType myStringType;

		public StringTypeMatcher(StringType stringType) {
			myStringType = stringType;
		}

		@Override
		public boolean matches(StringType argument) {
			return myStringType.getValue().equals(argument.getValue());
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(jettyServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void setUpClass() throws Exception {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		jettyServer = new Server(0);
		jettyServer.setHandler(context);
		ServletHolder jerseyServlet = context.addServlet(HttpServletDispatcher.class, "/*");
		jerseyServlet.setInitOrder(0);

		//@formatter:off
		jerseyServlet.setInitParameter("resteasy.resources",
			StringUtils.join(Arrays.asList(
				TestJaxRsMockPatientRestProviderDstu3.class.getCanonicalName(),
//					JaxRsExceptionInterceptor.class.getCanonicalName(),
				TestJaxRsConformanceRestProviderDstu3.class.getCanonicalName(),
				TestJaxRsMockPageProviderDstu3.class.getCanonicalName()
			), ","));
		//@formatter:on

		JettyUtil.startServer(jettyServer);
		ourPort = JettyUtil.getPortForStartedServer(jettyServer);

		ourCtx.setRestfulClientFactory(new JaxRsRestfulClientFactory(ourCtx));
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourCtx.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
		serverBase = "http://localhost:" + ourPort + "/";
		client = ourCtx.newRestfulGenericClient(serverBase);
		client.setEncoding(EncodingEnum.JSON);
		client.registerInterceptor(new LoggingInterceptor(true));
	}

}
