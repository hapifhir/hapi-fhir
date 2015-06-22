package ca.uhn.fhir.rest.server.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.resource.BaseSecurityEvent;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent;
import ca.uhn.fhir.model.dstu.resource.SecurityEvent.ObjectElement;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectLifecycleEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventObjectTypeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventOutcomeEnum;
import ca.uhn.fhir.model.dstu.valueset.SecurityEventSourceTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.interceptor.UserInfoInterceptor;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.audit.IResourceAuditor;
import ca.uhn.fhir.rest.server.audit.PatientAuditor;
import ca.uhn.fhir.store.IAuditDataStore;
import ca.uhn.fhir.util.PortUtil;

public class AuditingInterceptorTest {

	private static CloseableHttpClient ourClient;
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer servlet;
	private IServerInterceptor myInterceptor;
	private static final FhirContext ourCtx = FhirContext.forDstu1();
	
	private class MockDataStore implements IAuditDataStore {

		@Override
		public void store(BaseSecurityEvent auditEvent) throws Exception {
			//do nothing
		}
		
	}

	@Test
	public void testSinglePatient() throws Exception {
		
		AuditingInterceptor interceptor = new AuditingInterceptor("HAPITEST", false);
		Map<String, Class<? extends IResourceAuditor<? extends IResource>>> auditors = new HashMap<String, Class<? extends IResourceAuditor<? extends IResource>>>();
		auditors.put("Patient", PatientAuditor.class);
		interceptor.setAuditableResources(auditors );
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor)interceptor));
		
		MockDataStore mockDataStore = mock(MockDataStore.class);
		interceptor.setDataStore(mockDataStore);
		
		String requestURL = "http://localhost:" + ourPort + "/Patient/1";
		HttpGet httpGet = new HttpGet(requestURL);
		httpGet.addHeader(UserInfoInterceptor.HEADER_USER_ID, "hapi-fhir-junit-user");
		httpGet.addHeader(UserInfoInterceptor.HEADER_USER_NAME, "HAPI FHIR Junit Test Cases");
		httpGet.addHeader(UserInfoInterceptor.HEADER_APPLICATION_NAME, "hapi-fhir-junit");
		
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<SecurityEvent> captor = ArgumentCaptor.forClass(SecurityEvent.class);
		
		verify(mockDataStore, times(1)).store(captor.capture());
		
		SecurityEvent auditEvent = captor.getValue();
		assertEquals(SecurityEventOutcomeEnum.SUCCESS.getCode(), auditEvent.getEvent().getOutcome().getValue());
		assertEquals("HAPI FHIR Junit Test Cases", auditEvent.getParticipantFirstRep().getName().getValue());
		assertEquals("hapi-fhir-junit-user", auditEvent.getParticipantFirstRep().getUserId().getValue());				
		assertEquals("hapi-fhir-junit", auditEvent.getSource().getIdentifier().getValue());
		assertEquals("HAPITEST", auditEvent.getSource().getSite().getValue());
		assertEquals(SecurityEventSourceTypeEnum.USER_DEVICE.getCode(), auditEvent.getSource().getTypeFirstRep().getCode().getValue());
		
		List<ObjectElement> objects = auditEvent.getObject();
		assertEquals(1, objects.size());		
		ObjectElement object = objects.get(0);		
		assertEquals("00001", object.getIdentifier().getValue().getValue());
		assertEquals("Patient: PatientOne Test", object.getName().getValue());
		assertEquals(SecurityEventObjectLifecycleEnum.ACCESS_OR_USE, object.getLifecycle().getValueAsEnum());
		assertEquals(SecurityEventObjectTypeEnum.PERSON, object.getType().getValueAsEnum());
		assertEquals(requestURL, new String(Base64.decodeBase64(object.getQuery().getValueAsString())));
		
	}

	@Test
	public void testBundle() throws Exception {
		
		AuditingInterceptor interceptor = new AuditingInterceptor("HAPITEST", false);
		Map<String, Class<? extends IResourceAuditor<? extends IResource>>> auditors = new HashMap<String, Class<? extends IResourceAuditor<? extends IResource>>>();
		auditors.put("Patient", PatientAuditor.class);
		interceptor.setAuditableResources(auditors );
		servlet.setInterceptors(Collections.singletonList((IServerInterceptor)interceptor));
		
		MockDataStore mockDataStore = mock(MockDataStore.class);
		interceptor.setDataStore(mockDataStore);
		
		String requestURL = "http://localhost:" + ourPort + "/Patient?_id=1,2";
		HttpGet httpGet = new HttpGet(requestURL);
		httpGet.addHeader(UserInfoInterceptor.HEADER_USER_ID, "hapi-fhir-junit-user");
		httpGet.addHeader(UserInfoInterceptor.HEADER_USER_NAME, "HAPI FHIR Junit Test Cases");
		httpGet.addHeader(UserInfoInterceptor.HEADER_APPLICATION_NAME, "hapi-fhir-junit");
		
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ArgumentCaptor<SecurityEvent> captor = ArgumentCaptor.forClass(SecurityEvent.class);
		
		verify(mockDataStore, times(1)).store(captor.capture());
		
		SecurityEvent auditEvent = captor.getValue();
		assertEquals(SecurityEventOutcomeEnum.SUCCESS.getCode(), auditEvent.getEvent().getOutcome().getValue());
		assertEquals("HAPI FHIR Junit Test Cases", auditEvent.getParticipantFirstRep().getName().getValue());
		assertEquals("hapi-fhir-junit-user", auditEvent.getParticipantFirstRep().getUserId().getValue());				
		assertEquals("hapi-fhir-junit", auditEvent.getSource().getIdentifier().getValue());
		assertEquals("HAPITEST", auditEvent.getSource().getSite().getValue());
		assertEquals(SecurityEventSourceTypeEnum.USER_DEVICE.getCode(), auditEvent.getSource().getTypeFirstRep().getCode().getValue());
		
		List<ObjectElement> objects = auditEvent.getObject();
		assertEquals(2, objects.size());
		
		for(ObjectElement object: objects){	
			if("00001".equals(object.getIdentifier().getValue().getValue())){
				assertEquals("Patient: PatientOne Test", object.getName().getValue());	
			}else if("00002".equals(object.getIdentifier().getValue().getValue())){
				assertEquals("Patient: Ms Laura Elizabeth MacDougall Sookraj B.Sc.", object.getName().getValue());
			}else{
				fail("Unexpected patient identifier being audited: " + object.getIdentifier().getValue().getValue());
			}
			assertEquals(requestURL, new String(Base64.decodeBase64(object.getQuery().getValueAsString())));
			assertEquals(SecurityEventObjectTypeEnum.PERSON, object.getType().getValueAsEnum());
		}
		
	}
	

	
	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@Before
	public void before() {
		myInterceptor = mock(IServerInterceptor.class);
		servlet.setInterceptors(Collections.singletonList(myInterceptor));
	}

	
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}


	public static class DummyPatientResourceProvider implements IResourceProvider {

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
				HumanNameDt name = new HumanNameDt();
				name.addPrefix("Ms");
				name.addGiven("Laura");
				name.addGiven("Elizabeth");
				name.addFamily("MacDougall");
				name.addFamily("Sookraj");
				name.addSuffix("B.Sc.");
				patient.getName().add(name);				
				patient.getGender().setText("F");
				patient.getId().setValue("2");
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *            The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient getResourceById(@IdParam IdDt theId) {
			String key = theId.getIdPart();
			Patient retVal = getIdToPatient().get(key);
			return retVal;
		}

		
		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *            The resource identity
		 * @return The resource
		 */
		@Search()
		public List<Patient> getResourceById(@RequiredParam(name = "_id") TokenOrListParam theIds) {
			List<Patient> patients = new ArrayList<Patient>(); 
			for(BaseCodingDt id: theIds.getListAsCodings()){
				Patient patient = getIdToPatient().get(id.getCodeElement().getValue());
				if (patient != null) {
					patients.add(patient);			
				}
			}
			return patients;
		}


		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		private Patient createPatient1() {
			Patient patient = new Patient();
			patient.addIdentifier();
			patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
			patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
			patient.getIdentifier().get(0).setValue("00001");
			patient.addName();
			patient.getName().get(0).addFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			patient.getGender().setText("M");
			patient.getId().setValue("1");
			return patient;
		}

	}

}
