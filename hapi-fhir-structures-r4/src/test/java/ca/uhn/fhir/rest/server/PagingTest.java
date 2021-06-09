package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.security.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jmarchionatto based on old test from: Created by dsotnikov on 2/25/2014.
 */
public class PagingTest {

	private static RestfulServer myRestfulServer;
	private static SimpleBundleProvider ourBundleProvider;
	private static CloseableHttpClient ourClient;
	private static FhirContext ourContext;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static Server ourServer;
	private IPagingProvider myPagingProvider;
	private RequestDetails myRequestDetails;

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourContext = FhirContext.forR4();

		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		myRestfulServer = new RestfulServer(ourCtx);
		myRestfulServer.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(myRestfulServer);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

	@BeforeEach
	public void before() {
		myPagingProvider = mock(IPagingProvider.class);
		myRestfulServer.setPagingProvider(myPagingProvider);
		when(myPagingProvider.canStoreSearchResults()).thenReturn(true);
	}

	private void initBundleProvider(int theResourceQty) {
		List<IBaseResource> retVal = new ArrayList<>();
		for (int i = 0; i < theResourceQty; i++) {
			Patient patient = new Patient();
			patient.setId("" + i);
			patient.addName().setFamily("" + i);
			retVal.add(patient);
		}
		ourBundleProvider = new SimpleBundleProvider(retVal);
	}

	/**
	 * Reproduced: https://github.com/hapifhir/hapi-fhir/issues/2509
	 *
	 * A bundle size of 21 is used to make last page the third one to validate that the previous link of the
	 * last page has the correct offset
	 */
	@Test()
	public void test_previous_link_last_page_when_bunlde_size_equals_page_size_plus_one() throws Exception {
		initBundleProvider(21);
		when(myPagingProvider.getDefaultPageSize()).thenReturn(10);
		when(myPagingProvider.getMaximumPageSize()).thenReturn(50);
		when(myPagingProvider.storeResultList(any(RequestDetails.class), any(IBundleProvider.class))).thenReturn("ABCD");
		when(myPagingProvider.retrieveResultList(any(RequestDetails.class), anyString())).thenReturn(ourBundleProvider);

		String nextLink;
		String base = "http://localhost:" + ourPort;
		{
			HttpGet httpGet = new HttpGet(base + "/Patient?");
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), CHARSET_UTF8);
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);
			assertEquals(10, bundle.getEntry().size());

			assertNull(bundle.getLink(IBaseBundle.LINK_PREV));

			String linkSelf = bundle.getLink(IBaseBundle.LINK_SELF).getUrl();
			assertNotNull(linkSelf, "'self' link is not present");

			nextLink = bundle.getLink(IBaseBundle.LINK_NEXT).getUrl();
			assertNotNull(nextLink, "'next' link is not present");
			checkParam(nextLink, Constants.PARAM_PAGINGOFFSET, "10");
			checkParam(nextLink, Constants.PARAM_COUNT, "10");
		}
		{
			HttpGet httpGet = new HttpGet(nextLink);
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), CHARSET_UTF8);
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);

			assertEquals(10, bundle.getEntry().size());

			String linkPrev = bundle.getLink(IBaseBundle.LINK_PREV).getUrl();
			assertNotNull(linkPrev, "'previous' link is not present");
			checkParam(linkPrev, Constants.PARAM_PAGINGOFFSET, "0");
			checkParam(linkPrev, Constants.PARAM_COUNT, "10");

			String linkSelf = bundle.getLink(IBaseBundle.LINK_SELF).getUrl();
			assertNotNull(linkSelf, "'self' link is not present");
			checkParam(linkSelf, Constants.PARAM_PAGINGOFFSET, "10");
			checkParam(linkSelf, Constants.PARAM_COUNT, "10");

			nextLink = bundle.getLink(IBaseBundle.LINK_NEXT).getUrl();
			assertNotNull(nextLink, "'next' link is not present");
			checkParam(nextLink, Constants.PARAM_PAGINGOFFSET, "20");
			checkParam(nextLink, Constants.PARAM_COUNT, "10");
		}
		{
			HttpGet httpGet = new HttpGet(nextLink);
			HttpResponse status = ourClient.execute(httpGet);
			String responseContent = IOUtils.toString(status.getEntity().getContent(), CHARSET_UTF8);
			IOUtils.closeQuietly(status.getEntity().getContent());

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);

			assertEquals(1, bundle.getEntry().size());

			String linkPrev = bundle.getLink(IBaseBundle.LINK_PREV).getUrl();
			assertNotNull(linkPrev, "'previous' link is not present");
			checkParam(linkPrev, Constants.PARAM_PAGINGOFFSET, "10");
			checkParam(linkPrev, Constants.PARAM_COUNT, "10");

			String linkSelf = bundle.getLink(IBaseBundle.LINK_SELF).getUrl();
			assertNotNull(linkSelf, "'self' link is not present");
			checkParam(linkSelf, Constants.PARAM_PAGINGOFFSET, "20");
//			assertTrue(linkSelf.contains(Constants.PARAM_COUNT + "=1"));

			assertNull(bundle.getLink(IBaseBundle.LINK_NEXT));
		}
	}

	private void checkParam(String theUri, String theCheckedParam, String theExpectedValue) {
		Optional<String> paramValue = URLEncodedUtils.parse(theUri, CHARSET_UTF8).stream()
			.filter(nameValuePair -> nameValuePair.getName().equals(theCheckedParam))
			.map(NameValuePair::getValue)
			.findAny();
		assertTrue(paramValue.isPresent(), "No parameter '" + theCheckedParam + "' present in response");
		assertEquals(theExpectedValue, paramValue.get());
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Search
		public IBundleProvider findPatient(@IncludeParam Set<Include> theIncludes) {
			return ourBundleProvider;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}


}
