package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReindexProviderTest {
	public static final String TEST_JOB_ID = "test-job-id";
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexProviderTest.class);

	@Spy
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public final RestfulServerExtension myServerExtension = new RestfulServerExtension(myCtx);

	@Mock
	private IJobCoordinator myJobCoordinator;

	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Mock
	private IJobPartitionProvider myJobPartitionProvider;

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@InjectMocks
	private ReindexProvider mySvc;

	@BeforeEach
	public void beforeEach() {
		myServerExtension.registerProvider(mySvc);

		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());
	}

	private Batch2JobStartResponse createJobStartResponse() {
		Batch2JobStartResponse response = new Batch2JobStartResponse();
		response.setInstanceId(TEST_JOB_ID);
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

		RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);
		when(myJobPartitionProvider.getPartitionedUrls(any(), any())).thenReturn(List.of(new PartitionedUrl().setUrl(url).setRequestPartitionId(partitionId)));

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// Execute

		Parameters response = myServerExtension
			.getFhirClient()
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_REINDEX)
			.withParameters(input)
			.execute();

		// Verify

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		StringType jobId = (StringType) response.getParameterValue(ProviderConstants.OPERATION_REINDEX_RESPONSE_JOB_ID);
		assertEquals(TEST_JOB_ID, jobId.getValue());

		verify(myJobCoordinator, times(1)).startInstance(isNotNull(), myStartRequestCaptor.capture());
		verify(myJobPartitionProvider, times(1)).getPartitionedUrls(any(), eq(List.of(url)));
		verifyNoInteractions(myRequestPartitionHelperSvc);

		ReindexJobParameters params = myStartRequestCaptor.getValue().getParameters(ReindexJobParameters.class);
		assertThat(params.getUrls()).hasSize(1).containsExactly(url);
		assertThat(params.getPartitionedUrls()).hasSize(1);
		PartitionedUrl partitionedUrl = params.getPartitionedUrls().iterator().next();
		assertThat(partitionedUrl.getUrl()).isEqualTo(url);
		assertThat(partitionedUrl.getRequestPartitionId()).isEqualTo(partitionId);

		// Default values
		assertEquals(ReindexParameters.ReindexSearchParametersEnum.ALL, params.getReindexSearchParameters());
		assertTrue(params.getOptimisticLock());
		assertEquals(ReindexParameters.OptimizeStorageModeEnum.NONE, params.getOptimizeStorage());

		myServerExtension
				.getFhirClient()
				.operation()
				.onServer()
				.named(ProviderConstants.OPERATION_REINDEX)
				.withParameters(input)
				.execute();
	}
}
