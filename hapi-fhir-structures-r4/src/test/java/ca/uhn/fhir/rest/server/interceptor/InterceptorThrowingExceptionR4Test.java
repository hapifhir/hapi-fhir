package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

public class InterceptorThrowingExceptionR4Test {

	private static final String ERR403 = "{\"resourceType\":\"OperationOutcome\",\"issue\":[{\"severity\":\"error\",\"code\":\"processing\",\"diagnostics\":\"Access denied by default policy (no applicable rules)\"}]}";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InterceptorThrowingExceptionR4Test.class);
	private static CloseableHttpClient ourClient;
	private static String ourConditionalCreateId;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static boolean ourHitMethod;
	private static int ourPort;
	private static List<Resource> ourReturn;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@Before
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		for (IServerInterceptor next : new ArrayList<>(ourServlet.getInterceptors())) {
			ourServlet.unregisterInterceptor(next);
		}
		ourServlet.setTenantIdentificationStrategy(null);
		ourReturn = null;
		ourHitMethod = false;
		ourConditionalCreateId = "1123";
	}


	private Resource createPatient(Integer theId) {
		Patient retVal = new Patient();
		if (theId != null) {
			retVal.setId(new IdType("Patient", (long) theId));
		}
		retVal.addName().setFamily("FAM");
		return retVal;
	}


	private String extractResponseAndClose(HttpResponse status) throws IOException {
		if (status.getEntity() == null) {
			return null;
		}
		String responseContent;
		responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		return responseContent;
	}

	@Test
	public void testFailureInProcessingCompletedNormally() throws Exception {
		final List<Integer> hit = new ArrayList<>();
		ourServlet.registerInterceptor(new InterceptorAdapter(){
			@Override
			public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
				hit.add(1);
				throw new NullPointerException();
			}
		});
		ourServlet.registerInterceptor(new InterceptorAdapter(){
			@Override
			public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
				hit.add(2);
				throw new NullPointerException();
			}
		});
		ourServlet.registerInterceptor(new InterceptorAdapter(){
			@Override
			public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
				hit.add(3);
				throw new NullPointerException();
			}
		});

		HttpGet httpGet;
		HttpResponse status;
		String response;

		ourReturn = Collections.singletonList(createPatient(2));
		ourHitMethod = false;
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1");
		status = ourClient.execute(httpGet);
		response = extractResponseAndClose(status);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(response, containsString("FAM"));
		assertTrue(ourHitMethod);
		ourLog.info("Hit: {}", hit);
		assertThat(hit, contains(3,2,1));

	}


	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {

		ourPort = PortUtil.findFreePort();
		try {
			ourServer = new Server(ourPort);
		} catch (Exception e) {
			ourLog.error("FAILED", e);
			throw e;
		}
		DummyPatientResourceProvider patProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setFhirContext(ourCtx);
		ourServlet.setResourceProviders(patProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}


	public static class DummyPatientResourceProvider implements IResourceProvider {


		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}


		@Read(version = true)
		public Patient read(@IdParam IdType theId) {
			ourHitMethod = true;
			return (Patient) ourReturn.get(0);
		}


	}


}
