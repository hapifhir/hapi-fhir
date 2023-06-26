package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchBundleProviderWithNoSizeR4Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static TokenAndListParam ourIdentifiers;
	private static IBundleProvider ourLastBundleProvider;
	private static String ourLastMethod;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchBundleProviderWithNoSizeR4Test.class);
	private static int ourPort;

	private static Server ourServer;

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourIdentifiers = null;
	}

	@Test
	public void testBundleProviderReturnsNoSize() throws Exception {
		Bundle respBundle;
		
		ourLastBundleProvider = mock(IBundleProvider.class);
		when(ourLastBundleProvider.getCurrentPageOffset()).thenReturn(null);
		when(ourLastBundleProvider.size()).thenReturn(null);
		when(ourLastBundleProvider.getResources(any(int.class), any(int.class))).then(new Answer<List<IBaseResource>>() {
			@Override
			public List<IBaseResource> answer(InvocationOnMock theInvocation) {
				int from =(Integer)theInvocation.getArguments()[0]; 
				int to =(Integer)theInvocation.getArguments()[1];
				ArrayList<IBaseResource> retVal = Lists.newArrayList();
				for (int i = from; i < to; i++) {
					Patient p = new Patient();
					p.setId(Integer.toString(i));
					retVal.add(p);
				}
				return retVal;
			}});
		
		HttpGet httpGet;
		CloseableHttpResponse status = null;
		BundleLinkComponent linkNext;
		
		try {
			httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=json");
			status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("searchAll", ourLastMethod);
			respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			
			assertEquals(10, respBundle.getEntry().size());
			assertEquals("Patient/0", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
			linkNext = respBundle.getLink("next");
			assertNotNull(linkNext);
			
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		
		when(ourLastBundleProvider.size()).thenReturn(25);

		try {
			httpGet = new HttpGet(linkNext.getUrl());
			status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("searchAll", ourLastMethod);
			respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			
			assertEquals(10, respBundle.getEntry().size());
			assertEquals("Patient/10", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
			linkNext = respBundle.getLink("next");
			assertNotNull(linkNext);

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		try {
			httpGet = new HttpGet(linkNext.getUrl());
			status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("searchAll", ourLastMethod);
			respBundle = ourCtx.newJsonParser().parseResource(Bundle.class, responseContent);
			
			assertEquals(5, respBundle.getEntry().size());
			assertEquals("Patient/20", respBundle.getEntry().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
			linkNext = respBundle.getLink("next");
			assertNull(linkNext);

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
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

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		servlet.setResourceProviders(patientProvider);
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

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Search()
		public IBundleProvider searchAll() {
			ourLastMethod = "searchAll";
			return ourLastBundleProvider;
		}

	}

}
