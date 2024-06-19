package ca.uhn.fhir.rest.server.interceptor;

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
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class LoggingInterceptorDstu2Test {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static int ourDelayMs;
	private static Exception ourThrowException;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider())
		.registerProvider(new PlainProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach
	public void before() {
		ourServer.getInterceptorService().unregisterAllInterceptors();
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

		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/EX");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getAllValues().get(0)).contains("ERROR - GET " + ourServer.getBaseUrl() + "/Patient/EX");
	}

	@Test
	public void testMetadata() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/metadata");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());
		
		

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue()).contains("metadata - ");
	}

	@Test
	public void testOperationOnInstance() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${operationName} - ${idOrResourceName}");
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$everything");
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
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");

		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertEquals("read -  - Patient/1 - ", captor.getValue());
	}

	@Test
	public void testRequestId() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${requestId}");
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");

		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue()).hasSize(Constants.REQUEST_ID_LENGTH);
	}

	@Test
	public void testRequestProcessingTime() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${processingTimeMillis}");
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");

		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue()).startsWith("read - ");
		Integer.parseInt(captor.getValue().substring("read - ".length()));
	}

	@Test
	public void testProcessingTime() throws Exception {
		ourDelayMs = 110;

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${processingTimeMillis}");
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");

		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue()).matches("[1-9][0-9]{1,3}");
	}

	@Test
	public void testRequestBodyReadWithContentTypeHeader() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${operationName} - ${idOrResourceName} - ${requestBodyFhir}");
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
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
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		Patient p = new Patient();
		p.addIdentifier().setValue("VAL");
		String input = ourCtx.newXmlParser().encodeResourceToString(p);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
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
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		Patient p = new Patient();
		p.addIdentifier().setValue("VAL");
		String input = ourCtx.newXmlParser().encodeResourceToString(p);

		ourThrowException = new NullPointerException("FOO");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
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
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/$everything");
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
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/$everything");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());
		
		
		
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertEquals("extended-operation-type - $everything - Patient", captor.getValue());
	}

	@Test
	public void testRead() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue()).contains("read - Patient/1");
	}

	@Test
	public void testSearch() throws Exception {

		LoggingInterceptor interceptor = new LoggingInterceptor();
		interceptor.setMessageFormat("${operationType} - ${idOrResourceName} - ${requestParameters}");
		ourServer.getInterceptorService().registerInterceptor(interceptor);

		Logger logger = mock(Logger.class);
		interceptor.setLogger(logger);

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient?_id=1");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		
		
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(logger, timeout(1000).times(1)).info(captor.capture());
		assertThat(captor.getValue()).contains("search-type - Patient - ?_id=1");
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
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
