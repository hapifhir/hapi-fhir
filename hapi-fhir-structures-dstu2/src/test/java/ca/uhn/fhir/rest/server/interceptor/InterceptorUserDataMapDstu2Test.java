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
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InterceptorUserDataMapDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InterceptorUserDataMapDstu2Test.class);
	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private final Object myKey = "KEY";
	private final Object myValue = "VALUE";
	private Map<Object, Object> myMap;
	private Set<String> myMapCheckMethods;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.JSON)
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new PlainProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourServer.getInterceptorService().unregisterAllInterceptors();
		ourServer.getInterceptorService().registerInterceptor(new MyInterceptor());
	}


	@BeforeEach
	public void beforePurgeMap() {
		myMap = null;
		myMapCheckMethods = Collections.synchronizedSet(new LinkedHashSet<>());
	}


	@Test
	public void testException() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=foo");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(400, status.getStatusLine().getStatusCode());
		}

		await().untilAsserted(() ->
			assertThat(myMapCheckMethods).containsExactly(
				"incomingRequestPostProcessed",
				"incomingRequestPreHandled",
				"preProcessOutgoingException",
				"handleException",
				"processingCompleted"
			)
		);
	}

	@Test
	public void testRead() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");

		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {

			String response = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(response).contains("\"id\":\"1\"");
		await().untilAsserted(() -> assertThat(myMapCheckMethods).contains("incomingRequestPostProcessed", "incomingRequestPreHandled", "outgoingResponse", "processingCompletedNormally", "processingCompleted"));
	}

	}

	private void updateMapUsing(Map<Object, Object> theUserData, String theMethod) {
		assertNotNull(theUserData);
		if (myMap == null) {
			myMap = theUserData;
			myMap.put(myKey, myValue);
		} else {
			assertThat(theUserData).isSameAs(myMap);
			assertThat(myMap).containsEntry(myKey, myValue);
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
		public void incomingRequestPreHandled(RequestDetails theRequestDetails) {
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
			Map<String, Patient> idToPatient = new HashMap<>();
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
		TestUtil.randomizeLocaleAndTimezone();
	}

}
