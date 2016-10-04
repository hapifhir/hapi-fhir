package ca.uhn.fhir.rest.server.interceptor;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class LoggingInterceptorDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer servlet;
	private IServerInterceptor myInterceptor;
	private static int ourDelayMs;
	private static Exception ourThrowException;

	@Before
	public void before() {
		myInterceptor = mock(IServerInterceptor.class);
		servlet.setInterceptors(Collections.singletonList(myInterceptor));
		ourThrowException = null;
		ourDelayMs=0;
	}

	@Test
	public void testException() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setLogExceptions(true);
		assertTrue(interceptor.isLogExceptions());
		interceptor.setErrorMessageFormat("ERROR - ${requestVerb} ${requestUrl}");
		assertEquals("ERROR - ${requestVerb} ${requestUrl}", interceptor.getErrorMessageFormat());

		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/EX");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getAllValues().get(0), StringContains.containsString("ERROR - GET http://localhost:" + ourPort + "/Patient/EX"));
	}

	@Test
	public void testMetadata() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());
		
		

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue(), StringContains.containsString("metadata - "));
	}

	@Test
	public void testOperationOnInstance() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${operationName} - ${idOrResourceName}");
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/$everything");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertEquals("extended-operation-instance - $everything - Patient/123", captor.getValue());
	}


	@Test
	public void testRequestBodyRead() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${operationName} - ${idOrResourceName} - ${requestBodyFhir}");
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");

		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertEquals("read -  - Patient/1 - ", captor.getValue());
	}

	@Test
	public void testProcessingTime() throws Exception {
		ourDelayMs = 110;

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${processingTimeMillis}");
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");

		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue(), matchesPattern("[0-9]{3}"));
	}

	@Test
	public void testRequestBodyReadWithContentTypeHeader() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${operationName} - ${idOrResourceName} - ${requestBodyFhir}");
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		httpGet.addHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_FHIR_XML);

		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertEquals("read -  - Patient/1 - ", captor.getValue());
	}

	@Test
	public void testRequestBodyCreate() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${operationName} - ${idOrResourceName} - ${requestBodyFhir}");
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		Patient p = new Patient();
		p.addIdentifier().setValue("VAL");
		String input = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(input, ContentType.parse(Constants.CT_FHIR_XML + ";charset=utf-8")));

		HttpResponse status = ourClient.execute(httpPost);
		IOUtils.closeQuietly(status.getEntity().getContent());


		
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertEquals("create -  - Patient - <Patient xmlns=\"http://hl7.org/fhir\"><identifier><value value=\"VAL\"/></identifier></Patient>", captor.getValue());
	}

	@Test
	public void testRequestBodyCreateException() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${operationName} - ${idOrResourceName} - ${requestBodyFhir}");
		interceptor.setErrorMessageFormat("ERROR - ${operationType} - ${operationName} - ${idOrResourceName} - ${requestBodyFhir}");
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		Patient p = new Patient();
		p.addIdentifier().setValue("VAL");
		String input = ourCtx.newXmlParser().encodeResourceToString(p);

		ourThrowException = new NullPointerException("FOO");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(input, ContentType.parse(Constants.CT_FHIR_XML + ";charset=utf-8")));

		HttpResponse status = ourClient.execute(httpPost);
		IOUtils.closeQuietly(status.getEntity().getContent());

		

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertEquals("ERROR - create -  - Patient - <Patient xmlns=\"http://hl7.org/fhir\"><identifier><value value=\"VAL\"/></identifier></Patient>", captor.getValue());
	}

	@Test
	public void testOperationOnServer() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${operationName} - ${idOrResourceName}");
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/$everything");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertEquals("extended-operation-server - $everything - ", captor.getValue());
	}

	@Test
	public void testOperationOnType() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${operationName} - ${idOrResourceName}");
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/$everything");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());
		
		
		
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertEquals("extended-operation-type - $everything - Patient", captor.getValue());
	}

	@Test
	public void testRead() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue(), StringContains.containsString("read - Patient/1"));
	}

	@Test
	public void testSearch() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${idOrResourceName} - ${requestParameters}");
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor) interceptor));

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=1");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		
		
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue(), StringContains.containsString("search-type - Patient - ?_id=1"));
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();
		servlet = new RestfulServer(ourCtx);

		servlet.setResourceProviders(new DummyPatientResourceProvider());
		servlet.setPlainProviders(new PlainProvider());

		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		private Patient createPatient1() {
			Patient patient = new Patient();
			patient.addIdentifier();
			patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
			patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
			patient.getIdentifier().get(0).setValue("00001");
			patient.addName();
			patient.getName().get(0).addFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			patient.setGender(AdministrativeGenderEnum.MALE);
			patient.getId().setValue("1");
			return patient;
		}

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<String, Patient>();
			{
				Patient patient = createPatient1();
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
				patient.getIdentifier().add(new IdentifierDt());
				patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00002");
				patient.getName().add(new HumanNameDt());
				patient.getName().get(0).addFamily("Test");
				patient.getName().get(0).addGiven("PatientTwo");
				patient.setGender(AdministrativeGenderEnum.FEMALE);
				patient.getId().setValue("2");
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *           The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient getResourceById(@IdParam IdDt theId) throws InterruptedException {
			if (ourDelayMs>0) {
				Thread.sleep(ourDelayMs);
			}
			
			if (theId.getIdPart().equals("EX")) {
				throw new InvalidRequestException("FOO");
			}
			String key = theId.getIdPart();
			Patient retVal = getIdToPatient().get(key);
			return retVal;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *           The resource identity
		 * @return The resource
		 */
		@Search()
		public List<Patient> getResourceById(@RequiredParam(name = "_id") String theId) {
			Patient patient = getIdToPatient().get(theId);
			if (patient != null) {
				return Collections.singletonList(patient);
			} else {
				return Collections.emptyList();
			}
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) throws Exception {
			if (ourThrowException != null) {
				throw ourThrowException;
			}
			return new MethodOutcome(new IdDt("Patient/1"));
		}

		@Operation(name = "$everything", idempotent = true)
		public Bundle patientTypeOperation(@OperationParam(name = "start") DateDt theStart, @OperationParam(name = "end") DateDt theEnd) {

			Bundle retVal = new Bundle();
			// Populate bundle with matching resources
			return retVal;
		}

		@Operation(name = "$everything", idempotent = true)
		public Bundle patientTypeOperation(@IdParam IdDt theId, @OperationParam(name = "start") DateDt theStart, @OperationParam(name = "end") DateDt theEnd) {

			Bundle retVal = new Bundle();
			// Populate bundle with matching resources
			return retVal;
		}

	}

	public static class PlainProvider {

		@Operation(name = "$everything", idempotent = true)
		public Bundle patientTypeOperation(@OperationParam(name = "start") DateDt theStart, @OperationParam(name = "end") DateDt theEnd) {

			Bundle retVal = new Bundle();
			// Populate bundle with matching resources
			return retVal;
		}

	}

}
