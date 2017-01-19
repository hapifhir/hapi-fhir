package ca.uhn.fhir.jaxrs.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.client.JaxRsRestfulClientFactory;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsExceptionInterceptor;
import ca.uhn.fhir.jaxrs.server.interceptor.JaxRsResponseException;
import ca.uhn.fhir.jaxrs.server.test.RandomServerPortProvider;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsConformanceRestProvider;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsMockPageProvider;
import ca.uhn.fhir.jaxrs.server.test.TestJaxRsMockPatientRestProvider;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.method.SearchStyleEnum;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AbstractJaxRsResourceProviderTest {

	private static IGenericClient client;
	

	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final String PATIENT_NAME = "Van Houte";

	private static int ourPort;
	private static String serverBase;
	private static Server jettyServer;
	private TestJaxRsMockPatientRestProvider mock;
	private ArgumentCaptor<IdDt> idCaptor;
    private ArgumentCaptor<String> conditionalCaptor;	
	private ArgumentCaptor<Patient> patientCaptor;

	private void compareResultId(int id, IResource resource) {
		assertEquals(id, resource.getId().getIdPartAsLong().intValue());
	}

	private void compareResultUrl(String url, IResource resource) {
		assertEquals(url, resource.getId().getValueAsString().substring(serverBase.length() - 1));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	private Patient createPatient(long id) {
		Patient theResource = new Patient();
		theResource.setId(new IdDt(id));
		return theResource;
	}

	private List<Patient> createPatients(int firstId, int lastId) {
		List<Patient> result = new ArrayList<Patient>(lastId - firstId);
		for (long i = firstId; i <= lastId; i++) {
			result.add(createPatient(i));
		}
		return result;
	}

	/** Find By Id */
	@Test
	public void findUsingGenericClientById() {
		when(mock.find(any(IdDt.class))).thenReturn(createPatient(1));
		Patient result = client.read(Patient.class, "1");
		compareResultId(1, result);
		compareResultUrl("/Patient/1", result);
		reset(mock);
		when(mock.find(withId(result.getId()))).thenReturn(createPatient(1));
		result = (Patient) client.read(new UriDt(result.getId().getValue()));
		compareResultId(1, result);
		compareResultUrl("/Patient/1", result);
	}

	private Bundle getPatientBundle(int size) {
		Bundle result = new Bundle();
		for (long i = 0; i < size; i++) {
			Patient patient = createPatient(i);
			Entry entry = new Entry().setResource(patient);
			result.addEntry(entry);
		}
		return result;
	}

	@Before
	public void setUp() {
		this.mock = TestJaxRsMockPatientRestProvider.mock;
		idCaptor = ArgumentCaptor.forClass(IdDt.class);
		patientCaptor = ArgumentCaptor.forClass(Patient.class);
		conditionalCaptor = ArgumentCaptor.forClass(String.class);
		reset(mock);
	}

	/** Conditional Creates */
	@Test
	public void testConditionalCreate() throws Exception {
		Patient toCreate = createPatient(1);
		MethodOutcome outcome = new MethodOutcome();
		toCreate.getIdentifierFirstRep().setValue("myIdentifier");
		outcome.setResource(toCreate);

		when(mock.create(patientCaptor.capture(), eq("/Patient?_format=json&identifier=2"))).thenReturn(outcome);
		client.setEncoding(EncodingEnum.JSON);

		MethodOutcome response = client.create().resource(toCreate).conditional()
				.where(Patient.IDENTIFIER.exactly().identifier("2")).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals("myIdentifier", patientCaptor.getValue().getIdentifierFirstRep().getValue());
		IResource resource = (IResource) response.getResource();
		compareResultId(1, resource);
	}

	/** Conformance - Server */
	@Test
	public void testConformance() {
		final Conformance conf = client.fetchConformance().ofType(Conformance.class).execute();
		assertEquals(conf.getRest().get(0).getResource().get(0).getType().toString(), "Patient");
	}

	@Test
	public void testCreatePatient() throws Exception {
		Patient toCreate = createPatient(1);
		MethodOutcome outcome = new MethodOutcome();
		toCreate.getIdentifierFirstRep().setValue("myIdentifier");
		outcome.setResource(toCreate);

		when(mock.create(patientCaptor.capture(), isNull(String.class))).thenReturn(outcome);
		client.setEncoding(EncodingEnum.JSON);
		final MethodOutcome response = client.create().resource(toCreate).prefer(PreferReturnEnum.REPRESENTATION)
				.execute();
		IResource resource = (IResource) response.getResource();
		compareResultId(1, resource);
		assertEquals("myIdentifier", patientCaptor.getValue().getIdentifierFirstRep().getValue());
	}

	@Test
	public void testDeletePatient() {
		when(mock.delete(idCaptor.capture(), conditionalCaptor.capture())).thenReturn(new MethodOutcome());
		final IBaseOperationOutcome results = client.delete().resourceById("Patient", "1").execute();
		assertEquals("1", idCaptor.getValue().getIdPart());
	}

    @Test
    public void testConditionalDelete() throws Exception {
        when(mock.delete(idCaptor.capture(), conditionalCaptor.capture())).thenReturn(new MethodOutcome());
        client.delete().resourceConditionalByType("Patient").where(Patient.IDENTIFIER.exactly().identifier("2")).execute();
        assertEquals("Patient?identifier=2&_format=json", conditionalCaptor.getValue());
    }
    
	/** Extended Operations */
	@Test
	public void testExtendedOperations() {
		// prepare mock
		Parameters resultParameters = new Parameters();
		resultParameters.addParameter().setName("return").setResource(createPatient(1)).setValue(new StringDt("outputValue"));
		when(mock.someCustomOperation(any(IdDt.class), eq(new StringDt("myAwesomeDummyValue")))).thenReturn(resultParameters);
		// Create the input parameters to pass to the server
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("start").setValue(new DateDt("2001-01-01"));
		inParams.addParameter().setName("end").setValue(new DateDt("2015-03-01"));
		inParams.addParameter().setName("dummy").setValue(new StringDt("myAwesomeDummyValue"));
		//invoke
		Parameters outParams = client.operation().onInstance(new IdDt("Patient", "1")).named("$someCustomOperation")
				.withParameters(inParams).execute();
		//verify
		assertEquals("outputValue", ((StringDt)outParams.getParameter().get(0).getValue()).getValueAsString());
	}

	@Test
	public void testExtendedOperationsUsingGet() {
		// prepare mock
		Parameters resultParameters = new Parameters();
		resultParameters.addParameter().setName("return").setResource(createPatient(1)).setValue(new StringDt("outputValue"));
		when(mock.someCustomOperation(any(IdDt.class), eq(new StringDt("myAwesomeDummyValue")))).thenReturn(resultParameters);		
		// Create the input parameters to pass to the server
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("start").setValue(new DateDt("2001-01-01"));
		inParams.addParameter().setName("end").setValue(new DateDt("2015-03-01"));
		inParams.addParameter().setName("dummy").setValue(new StringDt("myAwesomeDummyValue"));

		// invoke
		Parameters outParams = client.operation().onInstance(new IdDt("Patient", "1")).named("$someCustomOperation")
				.withParameters(inParams).useHttpGet().execute();
		// verify
		assertEquals("outputValue", ((StringDt)outParams.getParameter().get(0).getValue()).getValueAsString());
	}

	/** Search using other query options */
	public void testOther() {
		// missing
	}

	@Test
	public void testRead() {
		when(mock.find(idCaptor.capture())).thenReturn(createPatient(1));
		final Patient patient = client.read(Patient.class, "1");
		compareResultId(1, patient);
		compareResultUrl("/Patient/1", patient);
		assertEquals("1", idCaptor.getValue().getIdPart());
	}

	/** Search - Compartments */
	@Test
	public void testSearchCompartements() {
		when(mock.searchCompartment(any(IdDt.class))).thenReturn(Arrays.asList((IResource) createPatient(1)));
		Bundle response = client.search().forResource(Patient.class).withIdAndCompartment("1", "Condition")
				.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class).execute();
		IResource resource = response.getEntry().get(0).getResource();
		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
	}

	/** */
	@Test
	public void testSearchPost() {
		when(mock.search(any(StringParam.class), Matchers.isNull(StringAndListParam.class)))
				.thenReturn(createPatients(1, 13));
		Bundle result = client.search().forResource("Patient").usingStyle(SearchStyleEnum.POST)
				.returnBundle(Bundle.class).execute();
		IResource resource = result.getEntry().get(0).getResource();
		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
	}

	/** Search/Query - Type */
	@Test
	public void testSearchUsingGenericClientBySearch() {
		// Perform a search
		when(mock.search(any(StringParam.class), Matchers.isNull(StringAndListParam.class)))
				.thenReturn(Arrays.asList(createPatient(1)));
		final ca.uhn.fhir.model.api.Bundle results = client.search().forResource(Patient.class)
				.where(Patient.NAME.matchesExactly().value(PATIENT_NAME)).execute();
		verify(mock).search(any(StringParam.class), Matchers.isNull(StringAndListParam.class));
		IResource resource = results.getEntries().get(0).getResource();

		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
	}

	/** Search - Multi-valued Parameters (ANY/OR) */
	@Test
	public void testSearchUsingGenericClientBySearchWithMultiValues() {
		when(mock.search(any(StringParam.class), Matchers.isNotNull(StringAndListParam.class)))
				.thenReturn(Arrays.asList(createPatient(1)));
		final ca.uhn.fhir.model.api.Bundle results = client.search().forResource(Patient.class)
				.where(Patient.ADDRESS.matches().values("Toronto")).and(Patient.ADDRESS.matches().values("Ontario"))
				.and(Patient.ADDRESS.matches().values("Canada"))
				.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("SHORTNAME", "TOYS")).execute();
		IResource resource = results.getEntries().get(0).getResource();

		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
	}

	/** Search - Paging */
	@Test
	public void testSearchWithPaging() {
		// Perform a search
		when(mock.search(any(StringParam.class), Matchers.isNull(StringAndListParam.class)))
				.thenReturn(createPatients(1, 13));
		final Bundle results = client.search().forResource(Patient.class).limitTo(8).returnBundle(Bundle.class)
				.execute();

		assertEquals(results.getEntry().size(), 8);
		IResource resource = results.getEntry().get(0).getResource();
		compareResultId(1, resource);
		compareResultUrl("/Patient/1", resource);
		compareResultId(8, results.getEntry().get(7).getResource());

//		ourLog.info("Next: " + results.getLink("next").getUrl());
//		String url = results.getLink("next").getUrl().replace("?", "Patient?");
//		results.getLink("next").setUrl(url);
//		ourLog.info("New Next: " + results.getLink("next").getUrl());

		// load next page
		final Bundle nextPage = client.loadPage().next(results).execute();
		resource = nextPage.getEntry().get(0).getResource();
		compareResultId(9, resource);
		compareResultUrl("/Patient/9", resource);
		assertNull(nextPage.getLink(Bundle.LINK_NEXT));
	}

	/** Search - Subsetting (_summary and _elements) */
	@Test
	@Ignore
	public void testSummary() {
		Object response = client.search().forResource(Patient.class)
				.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class).execute();
	}

	/** Transaction - Server */
	@Ignore
	@Test
	public void testTransaction() {
		ca.uhn.fhir.model.api.Bundle bundle = new ca.uhn.fhir.model.api.Bundle();
		BundleEntry entry = bundle.addEntry();
		final Patient existing = new Patient();
		existing.getNameFirstRep().addFamily("Created with bundle");
		entry.setResource(existing);

		BoundCodeDt<BundleEntryTransactionMethodEnum> theTransactionOperation = new BoundCodeDt(
				BundleEntryTransactionMethodEnum.VALUESET_BINDER, BundleEntryTransactionMethodEnum.POST);
		entry.setTransactionMethod(theTransactionOperation);
		ca.uhn.fhir.model.api.Bundle response = client.transaction().withBundle(bundle).execute();
	}
	
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

        assertEquals(null, patientCaptor.getValue().getId().getIdPart());
        assertEquals("Patient?identifier=2&_format=json", conditionalCaptor.getValue());
    }
    
	@SuppressWarnings("unchecked")
	@Ignore
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
			JaxRsResponseException notFoundException = new JaxRsResponseException(new ResourceNotFoundException(new IdDt("999955541264")));
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
		patient.addIdentifier((new IdentifierDt().setValue("1")));
		//invoke
		final Parameters inParams = new Parameters();
		inParams.addParameter().setResource(patient);

		final MethodOutcome mO = client.validate().resource(patient).execute();
		//verify
		assertNotNull(mO.getOperationOutcome());
	}
  
	private <T> T withId(final T id) {
		return argThat(new BaseMatcher<T>() {
			@Override
			public void describeTo(Description arg0) {
			}

			@Override
			public boolean matches(Object other) {
				IdDt thisId;
				IdDt otherId;
				if (id instanceof IdDt) {
					thisId = (IdDt) id;
					otherId = (IdDt) other;
				} else {
					thisId = ((IResource) id).getId();
					otherId = ((IResource) other).getId();
				}
				return thisId.getIdPartAsLong().equals(otherId.getIdPartAsLong());
			}
		});
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		System.out.println(ourPort);
		jettyServer = new Server(ourPort);
		jettyServer.setHandler(context);
		ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);

		//@formatter:off
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				StringUtils.join(Arrays.asList(
					TestJaxRsMockPatientRestProvider.class.getCanonicalName(),
					JaxRsExceptionInterceptor.class.getCanonicalName(),
					TestJaxRsConformanceRestProvider.class.getCanonicalName(),
					TestJaxRsMockPageProvider.class.getCanonicalName()
						), ";"));
		//@formatter:on
		
		jettyServer.start();

		ourCtx.setRestfulClientFactory(new JaxRsRestfulClientFactory(ourCtx));
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourCtx.getRestfulClientFactory().setSocketTimeout(1200 * 1000);
        serverBase = "http://localhost:" + ourPort + "/";
        client = ourCtx.newRestfulGenericClient(serverBase);
		client.setEncoding(EncodingEnum.JSON);
		client.registerInterceptor(new LoggingInterceptor(true));
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		try {
			jettyServer.destroy();
		} catch (Exception e) {

		}
	}

}
