package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ServerFeaturesDstu2Test {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerFeaturesDstu2Test.class);

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new DummyPatientResourceProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testOptions() throws Exception {
		HttpOptions httpGet = new HttpOptions(ourServer.getBaseUrl() + "");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<Conformance");

		/*
		 * Now with a leading /
		 */

		httpGet = new HttpOptions(ourServer.getBaseUrl() + "/");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("<Conformance");

	}


	/**
	 * See #313
	 */
	@Test
	public void testOptionsForNonBasePath1() throws Exception {
		HttpOptions httpGet = new HttpOptions(ourServer.getBaseUrl() + "/Foo");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);
		assertEquals(404, status.getStatusLine().getStatusCode());
	}

	/**
	 * See #313
	 */
	@Test
	public void testOptionsForNonBasePath2() throws Exception {
		HttpOptions httpGet = new HttpOptions(ourServer.getBaseUrl() + "/Patient/1");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);
		assertEquals(400, status.getStatusLine().getStatusCode());
	}

	/**
	 * See #313
	 */
	@Test
	public void testOptionsForNonBasePath3() throws Exception {
		HttpOptions httpGet = new HttpOptions(ourServer.getBaseUrl() + "/metadata");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);
		assertEquals(405, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testOptionsJson() throws Exception {
		HttpOptions httpGet = new HttpOptions(ourServer.getBaseUrl() + "?_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("resourceType\":\"Conformance");
	}

	@Test
	public void testHeadJson() throws Exception {
		HttpHead httpGet = new HttpHead(ourServer.getBaseUrl() + "/Patient/123");
		HttpResponse status = ourClient.execute(httpGet);
		assertNull(status.getEntity());

		ourLog.info(status.toString());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.getFirstHeader("x-powered-by").getValue()).contains("HAPI");
	}

	@Test
	public void testRegisterAndUnregisterResourceProviders() throws Exception {
		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent).contains("PRP1");

		Collection<IResourceProvider> originalProviders = new ArrayList<>(ourServer.getRestfulServer().getResourceProviders());
		DummyPatientResourceProvider2 newProvider = new DummyPatientResourceProvider2();
		try {

			// Replace provider
			for (IResourceProvider provider : originalProviders) {
				ourServer.getRestfulServer().unregisterProvider(provider);
			}
			ourServer.getRestfulServer().registerProvider(newProvider);

			httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
			status = ourClient.execute(httpGet);
			responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			IOUtils.closeQuietly(status.getEntity().getContent());
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(responseContent).contains("PRP2");

		} finally {

			// Restore providers
			ourServer.getRestfulServer().unregisterProvider(newProvider);
			originalProviders.forEach(p->ourServer.getRestfulServer().registerProvider(p));

		}
	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			Patient p1 = new Patient();
			p1.setId("p1ReadId");
			p1.addIdentifier().setValue("PRP1");
			return p1;
		}

	}

	public static class DummyPatientResourceProvider2 implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			Patient p1 = new Patient();
			p1.setId("p1ReadId");
			p1.addIdentifier().setValue("PRP2");
			return p1;
		}

	}

}
