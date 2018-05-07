package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class SearchBundleProviderWithNoSizeDstu1Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static TokenAndListParam ourIdentifiers;
	private static IBundleProvider ourLastBundleProvider;
	private static String ourLastMethod;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchBundleProviderWithNoSizeDstu1Test.class);
	private static int ourPort;

	private static Server ourServer;

	@Before
	public void before() {
		ourLastMethod = null;
		ourIdentifiers = null;
	}

	@Test
	public void testBundleProviderReturnsNoSize() throws Exception {
		Bundle respBundle;
		
		ourLastBundleProvider = mock(IBundleProvider.class); 
		when(ourLastBundleProvider.size()).thenReturn(null);
		when(ourLastBundleProvider.getResources(any(int.class), any(int.class))).then(new Answer<List<IBaseResource>>() {
			@Override
			public List<IBaseResource> answer(InvocationOnMock theInvocation) throws Throwable {
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
		StringDt linkNext;
		
		try {
			httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=json");
			status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("searchAll", ourLastMethod);
			respBundle = ourCtx.newJsonParser().parseBundle(responseContent);
			
			assertEquals(10, respBundle.getEntries().size());
			assertEquals("Patient/0", respBundle.getEntries().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
			linkNext = respBundle.getLinkNext();
			assertNotNull(linkNext.getValue());
			
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		
		when(ourLastBundleProvider.size()).thenReturn(25);

		try {
			httpGet = new HttpGet(linkNext.getValue());
			status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("searchAll", ourLastMethod);
			respBundle = ourCtx.newJsonParser().parseBundle(responseContent);
			
			assertEquals(10, respBundle.getEntries().size());
			assertEquals("Patient/10", respBundle.getEntries().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
			linkNext = respBundle.getLinkNext();
			assertNotNull(linkNext.getValue());

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		try {
			httpGet = new HttpGet(linkNext.getValue());
			status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("searchAll", ourLastMethod);
			respBundle = ourCtx.newJsonParser().parseBundle(responseContent);
			
			assertEquals(5, respBundle.getEntries().size());
			assertEquals("Patient/20", respBundle.getEntries().get(0).getResource().getIdElement().toUnqualifiedVersionless().getValue());
			linkNext = respBundle.getLinkNext();
			assertNull(linkNext.getValue());

		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

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

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

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
