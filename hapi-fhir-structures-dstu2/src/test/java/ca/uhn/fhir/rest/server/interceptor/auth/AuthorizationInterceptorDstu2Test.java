package ca.uhn.fhir.rest.server.interceptor.auth;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.method.IRequestOperationCallback;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.util.PortUtil;

public class AuthorizationInterceptorDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static boolean ourHitMethod;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AuthorizationInterceptorDstu2Test.class);
	private static int ourPort;
	private static List<IResource> ourReturn;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@Before
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		for (IServerInterceptor next : new ArrayList<IServerInterceptor>(ourServlet.getInterceptors())) {
			ourServlet.unregisterInterceptor(next);
		}
		ourReturn = null;
		ourHitMethod = false;
	}

	private HttpEntity createFhirResourceEntity(IBaseResource theResource) {
		String out = ourCtx.newJsonParser().encodeResourceToString(theResource);
		return new StringEntity(out, ContentType.create(Constants.CT_FHIR_JSON, "UTF-8"));
	}

	private IResource createObservation(Integer theId, String theSubjectId) {
		Observation retVal = new Observation();
		if (theId != null) {
			retVal.setId(new IdDt("Observation", (long) theId));
		}
		retVal.getCode().setText("OBS");
		retVal.setSubject(new ResourceReferenceDt(theSubjectId));
		return retVal;
	}

	private IResource createPatient(Integer theId) {
		Patient retVal = new Patient();
		if (theId != null) {
			retVal.setId(new IdDt("Patient", (long) theId));
		}
		retVal.addName().addFamily("FAM");
		return retVal;
	}

	private String extractResponseAndClose(HttpResponse status) throws IOException {
		if (status.getEntity() == null) {
			return null;
		}
		String responseContent;
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		return responseContent;
	}

	@Test
	public void testMetadataAllow() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").metadata()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testTransactionWriteGood() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").transaction().withAnyOperation().andApplyNormalRules().andThen()
					.allow("Rule 2").write().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").read().allResources().inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.build();
				//@formatter:on
			}
		});

		Bundle input = new Bundle();
		input.setType(BundleTypeEnum.TRANSACTION);
		input.addEntry().setResource(createPatient(1)).getRequest().setUrl("/Patient");
		
		Bundle output = new Bundle();
		output.setType(BundleTypeEnum.TRANSACTION_RESPONSE);
		output.addEntry().getResponse().setLocation("/Patient/1");
		
		HttpPost httpPost;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList((IResource)output);
		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/");
		httpPost.setEntity(createFhirResourceEntity(input));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testMetadataDeny() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.ALLOW) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.deny("Rule 1").metadata()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(401, status.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testReadByAnyId() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).withAnyId()
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testAllowAll() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.deny("Rule 1").read().resourcesOfType(Patient.class).withAnyId().andThen()
					.allowAll("Default Rule")
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Rule 1"));
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testDenyAll() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
					.denyAll("Default Rule")
					.build();
				//@formatter:on
		}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by rule: Default Rule"));
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	
	@Test
	public void testReadByCompartmentRight() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;

		ourReturn = Arrays.asList(createPatient(1));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testReadByCompartmentWrong() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").read().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").read().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Arrays.asList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation/10");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(1), createObservation(10, "Patient/2"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourReturn = Arrays.asList(createPatient(2), createObservation(10, "Patient/1"));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		ourLog.info(response);
		assertThat(response, containsString("Access denied by default policy (no applicable rules)"));
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

	}

	@Test
	public void testWriteByCompartmentCreate() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(createFhirResourceEntity(createPatient(null)));
		status = ourClient.execute(httpPost);
		String response = extractResponseAndClose(status);
		assertEquals("Access denied by default policy (no applicable rules)", response);
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/2")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals("Access denied by default policy (no applicable rules)", response);
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(createFhirResourceEntity(createObservation(null, "Patient/1")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentDelete() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpDelete httpDelete;
		HttpResponse status;

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(2));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/2");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		ourReturn = Arrays.asList(createPatient(1));
		httpDelete = new HttpDelete("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpDelete);
		extractResponseAndClose(status);
		assertEquals(204, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);
	}

	@Test
	public void testWriteByCompartmentUpdate() throws Exception {
		ourServlet.registerInterceptor(new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				//@formatter:off
				return new RuleBuilder()
					.allow("Rule 1").write().resourcesOfType(Patient.class).inCompartment("Patient", new IdDt("Patient/1")).andThen()
					.allow("Rule 2").write().resourcesOfType(Observation.class).inCompartment("Patient", new IdDt("Patient/1"))
					.build();
				//@formatter:on
			}
		});

		HttpEntityEnclosingRequestBase httpPost;
		HttpResponse status;

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/2");
		httpPost.setEntity(createFhirResourceEntity(createPatient(2)));
		status = ourClient.execute(httpPost);
		String response = extractResponseAndClose(status);
		assertEquals("Access denied by default policy (no applicable rules)", response);
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/1");
		httpPost.setEntity(createFhirResourceEntity(createPatient(1)));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/10");
		httpPost.setEntity(createFhirResourceEntity(createObservation(10, "Patient/1")));
		status = ourClient.execute(httpPost);
		extractResponseAndClose(status);
		assertEquals(201, status.getStatusLine().getStatusCode());
		assertTrue(ourHitMethod);

		ourHitMethod = false;
		httpPost = new HttpPut("http://localhost:" + ourPort + "/Observation/10");
		httpPost.setEntity(createFhirResourceEntity(createObservation(10, "Patient/2")));
		status = ourClient.execute(httpPost);
		response = extractResponseAndClose(status);
		assertEquals("Access denied by default policy (no applicable rules)", response);
		assertEquals(401, status.getStatusLine().getStatusCode());
		assertFalse(ourHitMethod);
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {

		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patProvider = new DummyPatientResourceProvider();
		DummyObservationResourceProvider obsProv = new DummyObservationResourceProvider();
		PlainProvider plainProvider = new PlainProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setFhirContext(ourCtx);
		ourServlet.setResourceProviders(patProvider, obsProv);
		ourServlet.setPlainProviders(plainProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Observation theResource) {
			ourHitMethod = true;
			theResource.setId("Observation/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(@IdParam IdDt theId) {
			ourHitMethod = true;
			MethodOutcome retVal = new MethodOutcome();
			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Observation.class;
		}

		@Read(version = true)
		public Observation read(@IdParam IdDt theId) {
			ourHitMethod = true;
			return (Observation) ourReturn.get(0);
		}

		@Search()
		public List<IResource> search() {
			ourHitMethod = true;
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdDt theId, @ResourceParam Observation theResource) {
			ourHitMethod = true;
			theResource.setId(theId.withVersion("2"));
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

	}

	public static class PlainProvider
	{
		@Transaction()
		public Bundle search(@TransactionParam Bundle theInput) {
			ourHitMethod = true;
			return (Bundle) ourReturn.get(0);
		}

	}
	
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theResource) {
			ourHitMethod = true;
			theResource.setId("Patient/1/_history/1");
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

		@Delete()
		public MethodOutcome delete(IRequestOperationCallback theRequestOperationCallback, @IdParam IdDt theId) {
			ourHitMethod = true;
			for (IBaseResource next : ourReturn) {
				theRequestOperationCallback.resourceDeleted(next);
			}
			MethodOutcome retVal = new MethodOutcome();
			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read(version = true)
		public Patient read(@IdParam IdDt theId) {
			ourHitMethod = true;
			return (Patient) ourReturn.get(0);
		}

		@Search()
		public List<IResource> search() {
			ourHitMethod = true;
			return ourReturn;
		}

		@Update()
		public MethodOutcome update(@IdParam IdDt theId, @ResourceParam Patient theResource) {
			ourHitMethod = true;
			theResource.setId(theId.withVersion("2"));
			MethodOutcome retVal = new MethodOutcome();
			retVal.setCreated(true);
			retVal.setResource(theResource);
			return retVal;
		}

	}

}
