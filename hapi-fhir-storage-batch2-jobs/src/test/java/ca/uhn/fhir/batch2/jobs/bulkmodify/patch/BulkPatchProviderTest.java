package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.eclipse.jetty.http.HttpStatus;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulkPatchProviderTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	private static final BulkPatchProvider ourProvider = new BulkPatchProvider();
	private static final String MY_INSTANCE_ID = "MY-INSTANCE-ID";

	@RegisterExtension
	static RestfulServerExtension ourFhirServer = new RestfulServerExtension(ourCtx)
		.keepAliveBetweenTests()
		.registerProvider(ourProvider);

	@RegisterExtension
	static HttpClientExtension ourHttpClient = new HttpClientExtension();

	@Mock
	private IJobCoordinator myJobCoordinator;

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@BeforeEach
	void beforeEach() {
		ourProvider.setContextForUnitTest(ourCtx);
		ourProvider.setJobCoordinatorForUnitTest(myJobCoordinator);
	}

	@Test
	void testInitiateJob() throws IOException {
		// Setup
		Batch2JobStartResponse startResponse = new Batch2JobStartResponse();
		startResponse.setInstanceId(MY_INSTANCE_ID);
		when(myJobCoordinator.startInstance(any(), any())).thenReturn(startResponse);

		Parameters patch = new Parameters();
		patch.setId("PATCH");

		Parameters request = new Parameters();
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_PATCH)
			.setResource(patch);
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_URL)
			.setValue(new StringType("Patient?"));
		request.addParameter()
			.setName(JpaConstants.OPERATION_BULK_PATCH_PARAM_URL)
			.setValue(new StringType("Location?"));

		// Test
		String url = ourFhirServer.getBaseUrl() + "/" + JpaConstants.OPERATION_BULK_PATCH;
		HttpPost post = new HttpPost(url);
		post.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RESPOND_ASYNC);
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {

			// Verify
			String expectedUrl = ourFhirServer.getBaseUrl() + "/$bulk-patch-status?_jobId=MY-INSTANCE-ID";
			assertEquals(HttpStatus.Code.NO_CONTENT.getCode(), response.getStatusLine().getStatusCode());
			assertEquals(expectedUrl, response.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());

		}

		verify(myJobCoordinator, times(1)).startInstance(any(), myStartRequestCaptor.capture());
		JobInstanceStartRequest startRequest = myStartRequestCaptor.getValue();
		assertEquals(BulkPatchJobAppCtx.JOB_ID,startRequest.getJobDefinitionId());
		BulkPatchJobParameters jobParameters = startRequest.getParameters(BulkPatchJobParameters.class);
		assertNotNull(jobParameters.getFhirPatch(ourCtx));
		assertEquals(Parameters.class, jobParameters.getFhirPatch(ourCtx).getClass());
		assertEquals("AA", jobParameters.getFhirPatch(ourCtx).getIdElement().getValue());
	}

}
