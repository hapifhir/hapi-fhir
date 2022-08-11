package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.jobs.parameters.UrlPartitioner;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReindexProviderTest {
	public static final String TEST_JOB_ID = "test-job-id";
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexProviderTest.class);

	@Spy
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@RegisterExtension
	private final RestfulServerExtension myServerExtension = new RestfulServerExtension(myCtx);

	@Mock
	private IJobCoordinator myJobCoordinator;

	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Mock
	private UrlPartitioner myUrlPartitioner;

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@InjectMocks
	private ReindexProvider mySvc;

	@BeforeEach
	public void beforeEach() {
		myServerExtension.registerProvider(mySvc);

		when(myJobCoordinator.startInstance(any()))
			.thenReturn(createJobStartResponse());
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequest(any(), any(), any())).thenReturn(RequestPartitionId.allPartitions());
	}

	private Batch2JobStartResponse createJobStartResponse() {
		Batch2JobStartResponse response = new Batch2JobStartResponse();
		response.setJobId(TEST_JOB_ID);
		return response;
	}

	@AfterEach
	public void afterEach() {
		myServerExtension.unregisterProvider(mySvc);
	}

	@Test
	public void testReindex_ByUrl() {
		// setup
		Parameters input = new Parameters();
		String url = "Observation?status=active";
		int batchSize = 2401;
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_URL, url);
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_BATCH_SIZE, new DecimalType(batchSize));

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		PartitionedUrl partitionedUrl = new PartitionedUrl();
		partitionedUrl.setUrl(url);
		partitionedUrl.setRequestPartitionId(RequestPartitionId.defaultPartition());
		when(myUrlPartitioner.partitionUrl(anyString(), any())).thenReturn(partitionedUrl);

		// Execute

		Parameters response = myServerExtension
			.getFhirClient()
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_REINDEX)
			.withParameters(input)
			.execute();

		// Verify

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		StringType jobId = (StringType) response.getParameter(ProviderConstants.OPERATION_REINDEX_RESPONSE_JOB_ID);
		assertEquals(TEST_JOB_ID, jobId.getValue());

		verify(myJobCoordinator, times(1)).startInstance(myStartRequestCaptor.capture());
		ReindexJobParameters params = myStartRequestCaptor.getValue().getParameters(ReindexJobParameters.class);
		assertThat(params.getPartitionedUrls(), hasSize(1));
		assertEquals(url, params.getPartitionedUrls().get(0).getUrl());
	}

	@Test
	public void testReindex_NoUrl() {
		// setup
		Parameters input = new Parameters();
		int batchSize = 2401;
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_BATCH_SIZE, new DecimalType(batchSize));
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_EVERYTHING, new BooleanType(true));

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// Execute

		Parameters response = myServerExtension
			.getFhirClient()
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_REINDEX)
			.withNoParameters(Parameters.class)
			.execute();

		// Verify

		ourLog.info(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		StringType jobId = (StringType) response.getParameter(ProviderConstants.OPERATION_REINDEX_RESPONSE_JOB_ID);
		assertEquals(TEST_JOB_ID, jobId.getValue());

		verify(myJobCoordinator, times(1)).startInstance(myStartRequestCaptor.capture());
		ReindexJobParameters params = myStartRequestCaptor.getValue().getParameters(ReindexJobParameters.class);
		assertThat(params.getPartitionedUrls(), empty());

	}
}
