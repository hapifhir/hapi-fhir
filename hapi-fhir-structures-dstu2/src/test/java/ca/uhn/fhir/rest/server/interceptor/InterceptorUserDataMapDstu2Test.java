package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class InterceptorUserDataMapDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InterceptorUserDataMapDstu2Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer servlet;
	private final Object myKey = "KEY";
	private final Object myValue = "VALUE";
	private Map<Object, Object> myMap;
	private Set<String> myMapCheckMethods;

	@BeforeEach
	public void before() {
		servlet.getInterceptorService().unregisterAllInterceptors();
		servlet.getInterceptorService().registerInterceptor(new MyInterceptor());
	}


	@BeforeEach
	public void beforePurgeMap() {
		myMap = null;
		myMapCheckMethods = Collections.synchronizedSet(new LinkedHashSet<>());
	}


	@Test
	public void testException() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_id=foo");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(400, status.getStatusLine().getStatusCode());
		}

		await().until(() -> myMapCheckMethods, contains("incomingRequestPostProcessed", "incomingRequestPreHandled", "preProcessOutgoingException", "handleException", "processingCompleted"));
	}

	@Test
	public void testRead() throws Exception {

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {

			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(response, containsString("\"id\":\"1\""));
		await().until(() -> myMapCheckMethods, contains("incomingRequestPostProcessed", "incomingRequestPreHandled", "outgoingResponse", "processingCompletedNormally", "processingCompleted"));
	}

	}

	private void updateMapUsing(Map<Object, Object> theUserData, String theMethod) {
		assertNotNull(theUserData);
		if (myMap == null) {
			myMap = theUserData;
			myMap.put(myKey, myValue);
		} else {
			assertSame(myMap, theUserData);
			assertEquals(myValue, myMap.get(myKey));
		}
		myMapCheckMethods.add(theMethod);
	}

	@Interceptor
	public class MyInterceptor {

		@Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
		public void incomingRequestPostProcessed(RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
			updateMapUsing(theRequestDetails.getUserData(), "incomingRequestPostProcessed");
			updateMapUsing(theServletRequestDetails.getUserData(), "incomingRequestPostProcessed");
		}

		@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
		public void incomingRequestPreHandled(ActionRequestDetails theRequestDetails) {
			updateMapUsing(theRequestDetails.getUserData(), "incomingRequestPreHandled");
		}

		@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
		public void outgoingResponse(RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
			updateMapUsing(theRequestDetails.getUserData(), "outgoingResponse");
			updateMapUsing(theServletRequestDetails.getUserData(), "outgoingResponse");
		}

		@Hook(Pointcut.SERVER_PROCESSING_COMPLETED_NORMALLY)
		public void processingCompletedNormally(RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
			updateMapUsing(theRequestDetails.getUserData(), "processingCompletedNormally");
			updateMapUsing(theServletRequestDetails.getUserData(), "processingCompletedNormally");
		}

		@Hook(Pointcut.SERVER_PROCESSING_COMPLETED)
		public void processingCompleted(RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
			updateMapUsing(theRequestDetails.getUserData(), "processingCompleted");
			updateMapUsing(theServletRequestDetails.getUserData(), "processingCompleted");
		}

		@Hook(Pointcut.SERVER_PRE_PROCESS_OUTGOING_EXCEPTION)
		public void preProcessOutgoingException(RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
			updateMapUsing(theRequestDetails.getUserData(), "preProcessOutgoingException");
			updateMapUsing(theServletRequestDetails.getUserData(), "preProcessOutgoingException");
		}

		@Hook(Pointcut.SERVER_HANDLE_EXCEPTION)
		public void handleException(RequestDetails theRequestDetails, ServletRequestDetails theServletRequestDetails) {
			updateMapUsing(theRequestDetails.getUserData(), "handleException");
			updateMapUsing(theServletRequestDetails.getUserData(), "handleException");
		}

	}

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
		 * @param theId The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient getResourceById(@IdParam IdDt theId) {
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
		 * @param theId The resource identity
		 * @return The resource
		 */
		@Search()
		public List<Patient> getResourceById(@RequiredParam(name = "_id") String theId) {
			throw new InvalidRequestException("FOO");
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
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

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		servlet = new RestfulServer(ourCtx);

		servlet.setResourceProviders(new DummyPatientResourceProvider());
		servlet.setPlainProviders(new PlainProvider());

		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
		ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

}
