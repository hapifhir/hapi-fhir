package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jmarchionatto based on old test from: Created by dsotnikov on 2/25/2014.
 */
public class PagingTest {

	private FhirContext ourContext = FhirContext.forR4Cached();
	@RegisterExtension
	public RestfulServerExtension myServerExtension = new RestfulServerExtension(ourContext);

	private static SimpleBundleProvider ourBundleProvider;
	private static CloseableHttpClient ourClient;

	private final IPagingProvider pagingProvider = mock(IPagingProvider.class);

	@BeforeAll
	public static void beforeClass() throws Exception {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}


	/**
	 * Reproduced: https://github.com/hapifhir/hapi-fhir/issues/2509
	 *
	 * A bundle size of 21 is used to make last page the third one to validate that the previous link of the
	 * last page has the correct offset
	 */
	@Test()
	public void testPreviousLinkLastPageWhenBundleSizeEqualsPageSizePlusOne() throws Exception {
		initBundleProvider(21);
		myServerExtension.getRestfulServer().registerProvider(new DummyPatientResourceProvider());
		myServerExtension.getRestfulServer().setPagingProvider(pagingProvider);

		when(pagingProvider.canStoreSearchResults()).thenReturn(true);
		when(pagingProvider.getDefaultPageSize()).thenReturn(10);
		when(pagingProvider.getMaximumPageSize()).thenReturn(50);
		when(pagingProvider.storeResultList(any(RequestDetails.class), any(IBundleProvider.class))).thenReturn("ABCD");
		when(pagingProvider.retrieveResultList(any(RequestDetails.class), anyString())).thenReturn(ourBundleProvider);

		String nextLink;
		String base = "http://localhost:" + myServerExtension.getPort();
		HttpGet get = new HttpGet(base + "/Patient?");
		String responseContent;
		try (CloseableHttpResponse resp = ourClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			responseContent = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);

			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);
			assertThat(bundle.getEntry()).hasSize(10);

			assertNull(bundle.getLink(IBaseBundle.LINK_PREV));

			String linkSelf = bundle.getLink(IBaseBundle.LINK_SELF).getUrl();
			assertThat(linkSelf).as("'self' link is not present").isNotNull();

			nextLink = bundle.getLink(IBaseBundle.LINK_NEXT).getUrl();
			assertThat(nextLink).as("'next' link is not present").isNotNull();
			checkParam(nextLink, Constants.PARAM_PAGINGOFFSET, "10");
			checkParam(nextLink, Constants.PARAM_COUNT, "10");
		}
		try (CloseableHttpResponse resp = ourClient.execute(new HttpGet(nextLink))) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			responseContent = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);

			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);
			assertThat(bundle.getEntry()).hasSize(10);

			String linkPrev = bundle.getLink(IBaseBundle.LINK_PREV).getUrl();
			assertThat(linkPrev).as("'previous' link is not present").isNotNull();
			checkParam(linkPrev, Constants.PARAM_PAGINGOFFSET, "0");
			checkParam(linkPrev, Constants.PARAM_COUNT, "10");

			String linkSelf = bundle.getLink(IBaseBundle.LINK_SELF).getUrl();
			assertThat(linkSelf).as("'self' link is not present").isNotNull();
			checkParam(linkSelf, Constants.PARAM_PAGINGOFFSET, "10");
			checkParam(linkSelf, Constants.PARAM_COUNT, "10");

			nextLink = bundle.getLink(IBaseBundle.LINK_NEXT).getUrl();
			assertThat(nextLink).as("'next' link is not present").isNotNull();
			checkParam(nextLink, Constants.PARAM_PAGINGOFFSET, "20");
			checkParam(nextLink, Constants.PARAM_COUNT, "10");
		}
		try (CloseableHttpResponse resp = ourClient.execute(new HttpGet(nextLink))) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			responseContent = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);

			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);
			assertThat(bundle.getEntry()).hasSize(1);

			String linkPrev = bundle.getLink(IBaseBundle.LINK_PREV).getUrl();
			assertThat(linkPrev).as("'previous' link is not present").isNotNull();
			checkParam(linkPrev, Constants.PARAM_PAGINGOFFSET, "10");
			checkParam(linkPrev, Constants.PARAM_COUNT, "10");

			String linkSelf = bundle.getLink(IBaseBundle.LINK_SELF).getUrl();
			assertThat(linkSelf).as("'self' link is not present").isNotNull();
			checkParam(linkSelf, Constants.PARAM_PAGINGOFFSET, "20");
			//			assertTrue(linkSelf.contains(Constants.PARAM_COUNT + "=1"));

			assertNull(bundle.getLink(IBaseBundle.LINK_NEXT));
		}
	}

	@Test()
	public void testLinksWhenUsingOffsetPaginationWithNoCaching() throws Exception {
		initBundleProvider(10);
		ourBundleProvider.setSize(30);
		myServerExtension.getRestfulServer().registerProvider(new DummyPatientResourceProvider());

		String nextLink;
		String base = "http://localhost:" + myServerExtension.getPort();
		HttpGet get = new HttpGet(base + "/Patient?_count=10");
		String responseContent;
		try (CloseableHttpResponse resp = ourClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			responseContent = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);

			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);
			assertThat(bundle.getEntry()).hasSize(10);

			assertNull(bundle.getLink(IBaseBundle.LINK_PREV));

			String linkSelf = bundle.getLink(IBaseBundle.LINK_SELF).getUrl();
			assertThat(linkSelf).as("'self' link is not present").isNotNull();

			nextLink = bundle.getLink(IBaseBundle.LINK_NEXT).getUrl();
			assertThat(nextLink).as("'next' link is not present").isNotNull();
			checkParam(nextLink, Constants.PARAM_OFFSET, "10");
			checkParam(nextLink, Constants.PARAM_COUNT, "10");
		}
		try (CloseableHttpResponse resp = ourClient.execute(new HttpGet(nextLink))) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			responseContent = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);

			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);
			assertThat(bundle.getEntry()).hasSize(10);

			String linkPrev = bundle.getLink(IBaseBundle.LINK_PREV).getUrl();
			assertThat(linkPrev).as("'previous' link is not present").isNotNull();
			checkParam(linkPrev, Constants.PARAM_OFFSET, "0");
			checkParam(linkPrev, Constants.PARAM_COUNT, "10");

			String linkSelf = bundle.getLink(IBaseBundle.LINK_SELF).getUrl();
			assertThat(linkSelf).as("'self' link is not present").isNotNull();
			checkParam(linkSelf, Constants.PARAM_OFFSET, "10");
			checkParam(linkSelf, Constants.PARAM_COUNT, "10");

			nextLink = bundle.getLink(IBaseBundle.LINK_NEXT).getUrl();
			assertThat(nextLink).as("'next' link is not present").isNotNull();
			checkParam(nextLink, Constants.PARAM_OFFSET, "20");
			checkParam(nextLink, Constants.PARAM_COUNT, "10");
		}
		try (CloseableHttpResponse resp = ourClient.execute(new HttpGet(nextLink))) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			responseContent = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);

			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);
			assertThat(bundle.getEntry()).hasSize(10);

			String linkPrev = bundle.getLink(IBaseBundle.LINK_PREV).getUrl();
			assertThat(linkPrev).as("'previous' link is not present").isNotNull();
			checkParam(linkPrev, Constants.PARAM_OFFSET, "10");
			checkParam(linkPrev, Constants.PARAM_COUNT, "10");

			String linkSelf = bundle.getLink(IBaseBundle.LINK_SELF).getUrl();
			assertThat(linkSelf).as("'self' link is not present").isNotNull();
			checkParam(linkSelf, Constants.PARAM_OFFSET, "20");
			checkParam(linkSelf, Constants.PARAM_COUNT, "10");

			assertNull(bundle.getLink(IBaseBundle.LINK_NEXT));
		}
	}

	@Test()
	public void testSendingSameRequestConsecutivelyResultsInSameResponse() throws Exception {
		initBundleProvider(10);
		myServerExtension.getRestfulServer().registerProvider(new DummyPatientResourceProvider());
		myServerExtension.getRestfulServer().setPagingProvider(pagingProvider);

		when(pagingProvider.canStoreSearchResults()).thenReturn(true);
		when(pagingProvider.getDefaultPageSize()).thenReturn(10);
		when(pagingProvider.getMaximumPageSize()).thenReturn(50);
		when(pagingProvider.storeResultList(any(RequestDetails.class), any(IBundleProvider.class))).thenReturn("ABCD");
		when(pagingProvider.retrieveResultList(any(RequestDetails.class), anyString())).thenReturn(ourBundleProvider);

		String nextLink;
		String base = "http://localhost:" + myServerExtension.getPort();
		HttpGet get = new HttpGet(base + "/Patient?_getpagesoffset=10");
		String responseContent;
		try (CloseableHttpResponse resp = ourClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			responseContent = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);

			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);
			assertThat(bundle.getEntry()).isEmpty();
		}
		try (CloseableHttpResponse resp = ourClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			responseContent = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);

			Bundle bundle = ourContext.newJsonParser().parseResource(Bundle.class, responseContent);
			assertThat(bundle.getEntry()).isEmpty();
		}
	}
	private void checkParam(String theUriString, String theCheckedParam, String theExpectedValue) {
		Optional<String> paramValue = URLEncodedUtils.parse(URI.create(theUriString), CHARSET_UTF8).stream()
			.filter(nameValuePair -> nameValuePair.getName().equals(theCheckedParam))
			.map(NameValuePair::getValue)
			.findAny();
		assertThat(paramValue.isPresent()).as("No parameter '" + theCheckedParam + "' present in response").isTrue();
		assertThat(paramValue).contains(theExpectedValue);
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
