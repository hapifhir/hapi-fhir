package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MetadataConformanceDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MetadataConformanceDstu3Test.class);
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testSummary() throws Exception {
		String output;

		// With
		HttpRequestBase httpPost = new HttpGet(ourServer.getBaseUrl() + "/metadata?_summary=true&_pretty=true");
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			ourLog.info(output);
			assertThat(output).contains("<CapabilityStatement");
			assertThat(output).contains("<meta>", "SUBSETTED", "</meta>");
			assertThat(output).doesNotContain("searchParam");
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

		// Without
		httpPost = new HttpGet(ourServer.getBaseUrl() + "/metadata?_pretty=true");
		status = ourClient.execute(httpPost);
		try {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			ourLog.info(output);
			assertThat(output).contains("<CapabilityStatement");
			assertThat(output).doesNotContain("<meta>", "SUBSETTED", "</meta>");
			assertThat(output).contains("searchParam");
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testElements() throws Exception {
		String output;

		HttpRequestBase httpPost = new HttpGet(ourServer.getBaseUrl() + "/metadata?_elements=fhirVersion&_pretty=true");
		CloseableHttpResponse status = ourClient.execute(httpPost);
		try {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			ourLog.info(output);
			assertThat(output).contains("<CapabilityStatement");
			assertThat(output).contains("<meta>", "SUBSETTED", "</meta>");
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testHttpMethods() throws Exception {
		String output;

		HttpRequestBase httpOperation = new HttpGet(ourServer.getBaseUrl() + "/metadata");
		try (CloseableHttpResponse status = ourClient.execute(httpOperation)) {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(output).contains("<CapabilityStatement");
			assertThat(status.getFirstHeader("X-Powered-By").getValue()).contains("HAPI FHIR " + VersionUtil.getVersion());
			assertThat(status.getFirstHeader("X-Powered-By").getValue()).contains("REST Server (FHIR Server; FHIR " + ourCtx.getVersion().getVersion().getFhirVersionString() + "/" + ourCtx.getVersion().getVersion().name() + ")");
		}

		httpOperation = new HttpOptions(ourServer.getBaseUrl());
		try (CloseableHttpResponse status = ourClient.execute(httpOperation)) {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(output).contains("<CapabilityStatement");
			assertThat(status.getFirstHeader("X-Powered-By").getValue()).contains("HAPI FHIR " + VersionUtil.getVersion());
			assertThat(status.getFirstHeader("X-Powered-By").getValue()).contains("REST Server (FHIR Server; FHIR " + ourCtx.getVersion().getVersion().getFhirVersionString() + "/" + ourCtx.getVersion().getVersion().name() + ")");
		}

		httpOperation = new HttpPost(ourServer.getBaseUrl() + "/metadata");
		try (CloseableHttpResponse status = ourClient.execute(httpOperation)) {
			output = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			assertEquals(405, status.getStatusLine().getStatusCode());
			assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"" + Msg.code(388) + "/metadata request must use HTTP GET\"/></issue></OperationOutcome>", output);
		}

		/*
		 * There is no @read on the RP below, so this should fail. Otherwise it
		 * would be interpreted as a read on ID "metadata"
		 */
		httpOperation = new HttpGet(ourServer.getBaseUrl() + "/Patient/metadata");
		try (CloseableHttpResponse status = ourClient.execute(httpOperation)) {
			assertEquals(400, status.getStatusLine().getStatusCode());
		}
	}

	@SuppressWarnings("unused")
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> search(@OptionalParam(name = "foo") StringParam theFoo) {
			throw new UnsupportedOperationException();
		}

		@Validate()
		public MethodOutcome validate(@ResourceParam Patient theResource) {
			return new MethodOutcome();
		}
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
