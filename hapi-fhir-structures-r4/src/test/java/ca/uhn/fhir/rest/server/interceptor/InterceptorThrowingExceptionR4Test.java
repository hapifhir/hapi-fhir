package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterceptorThrowingExceptionR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InterceptorThrowingExceptionR4Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static boolean ourHitMethod;
	private static int ourPort;
	private static List<Resource> ourReturn;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@BeforeEach
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		ourServlet.getInterceptorService().unregisterAllInterceptors();
		ourServlet.setTenantIdentificationStrategy(null);
		ourReturn = null;
		ourHitMethod = false;
	}

	@AfterEach
	public void after() {
		ourServlet.getInterceptorService().unregisterAllInterceptors();
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
		status.getEntity().getContent().close();
		return responseContent;
	}

	@Test
	public void testFailureInProcessingCompletedNormally() throws Exception {
		final List<Integer> hit = Collections.synchronizedList(new ArrayList<>());
		ourServlet.getInterceptorService().registerInterceptor(new InterceptorAdapter() {
			@Override
			public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
				hit.add(1);
				throw new NullPointerException("Hit 1");
			}
		});
		ourServlet.getInterceptorService().registerInterceptor(new InterceptorAdapter() {
			@Override
			public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
				hit.add(2);
				throw new NullPointerException("Hit 2");
			}
		});
		ourServlet.getInterceptorService().registerInterceptor(new InterceptorAdapter() {
			@Override
			public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
				hit.add(3);
				throw new NullPointerException("Hit 3");
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

		await().until(() -> hit.size() == 3);

		ourLog.info("Hit: {}", hit);
		assertThat("Hits: " + hit.toString(), hit, contains(1, 2, 3));

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

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		try {
			ourServer = new Server(0);
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
		JettyUtil.startServer(ourServer);
		ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}


}
