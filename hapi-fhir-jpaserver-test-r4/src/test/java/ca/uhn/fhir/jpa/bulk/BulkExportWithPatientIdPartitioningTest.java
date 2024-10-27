package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.provider.BulkDataExportProvider;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BulkExportWithPatientIdPartitioningTest extends BaseResourceProviderR4Test {
	private final Logger ourLog = LoggerFactory.getLogger(BulkExportWithPatientIdPartitioningTest.class);

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	private PatientIdPartitionInterceptor myPatientIdPartitionInterceptor;

	@BeforeEach
	public void before() {
		myPatientIdPartitionInterceptor = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings);
		myInterceptorRegistry.registerInterceptor(myPatientIdPartitionInterceptor);
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(myPatientIdPartitionInterceptor);
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setUnnamedPartitionMode(new PartitionSettings().isUnnamedPartitionMode());
	}

	@Test
	public void testSystemBulkExport_withResourceType_success() throws IOException {
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.addHeader(BulkDataExportProvider.PARAM_EXPORT_TYPE, "Patient");
		post.addHeader(BulkDataExportProvider.PARAM_EXPORT_TYPE_FILTER, "Patient?");

		try (CloseableHttpResponse postResponse = myServer.getHttpClient().execute(post)) {
			ourLog.info("Response: {}", postResponse);
			assertEquals(202, postResponse.getStatusLine().getStatusCode());
			assertEquals("Accepted", postResponse.getStatusLine().getReasonPhrase());
		}
	}

	@Test
	public void testSystemBulkExport_withResourceType_pollSuccessful() throws IOException {
		HttpPost post = new HttpPost(myServer.getBaseUrl() + "/" + ProviderConstants.OPERATION_EXPORT);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		post.addHeader(BulkDataExportProvider.PARAM_EXPORT_TYPE, "Patient"); // ignored when computing partition
		post.addHeader(BulkDataExportProvider.PARAM_EXPORT_TYPE_FILTER, "Patient?");

		String locationUrl;

		try (CloseableHttpResponse postResponse = myServer.getHttpClient().execute(post)) {
			ourLog.info("Response: {}", postResponse);
			assertEquals(202, postResponse.getStatusLine().getStatusCode());
			assertEquals("Accepted", postResponse.getStatusLine().getReasonPhrase());

			Header locationHeader = postResponse.getFirstHeader(Constants.HEADER_CONTENT_LOCATION);
			assertNotNull(locationHeader);
			locationUrl = locationHeader.getValue();
		}

		HttpGet get = new HttpGet(locationUrl);
		try (CloseableHttpResponse postResponse = myServer.getHttpClient().execute(get)) {
			ourLog.info("Response: {}", postResponse);
			assertEquals(202, postResponse.getStatusLine().getStatusCode());
		}
	}
}
