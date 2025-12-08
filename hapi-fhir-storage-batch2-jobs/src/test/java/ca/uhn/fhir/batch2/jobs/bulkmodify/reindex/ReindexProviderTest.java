package ca.uhn.fhir.batch2.jobs.bulkmodify.reindex;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReindexProviderTest {
	public static final String TEST_JOB_ID = "test-job-id";
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexProviderTest.class);

	@Spy
	private final FhirContext myCtx = FhirContext.forR4Cached();

	@Spy
	private final PartitionSettings myPartitionSettings = new PartitionSettings();

	@RegisterExtension
	public final RestfulServerExtension myServerExtension = new RestfulServerExtension(myCtx);

	@Mock(strictness = Mock.Strictness.LENIENT)
	private IJobCoordinator myJobCoordinator;

	@Mock(strictness = Mock.Strictness.LENIENT)
	private IJobPartitionProvider myJobPartitionProvider;

	@Mock(strictness = Mock.Strictness.LENIENT)
	private IDaoRegistry myDaoRegistry;

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@InjectMocks
	private ReindexProvider mySvc;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void beforeEach() {
		myServerExtension.registerProvider(mySvc);

		when(myJobCoordinator.startInstance(isNotNull(), any()))
			.thenReturn(createJobStartResponse());

		when(myDaoRegistry.isResourceTypeSupported(eq("Patient"))).thenReturn(true);
		when(myDaoRegistry.isResourceTypeSupported(eq("Observation"))).thenReturn(true);
		when(myJobPartitionProvider.getPartitionedUrls(any(), any())).thenAnswer(t-> {
			List<String> urls = t.getArgument(1, List.class);
			return urls.stream().map(u -> new PartitionedUrl()
				.setUrl(u)
				.setRequestPartitionId(RequestPartitionId.fromPartitionId(1))).toList();
		});

		mySvc.setDaoRegistryForUnitTest(myDaoRegistry);
		mySvc.setJobPartitionProviderForUnitTest(myJobPartitionProvider);
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

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"Observation?status=active", "", "Observation?_includeDeleted=exclusive"})
	public void testReindex_withUrlAndNonDefaultParams(String theUrl) {
		// setup
		Parameters input = new Parameters();
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_URL, theUrl);
		input.addParameter(ProviderConstants.OPERATION_REINDEX_PARAM_BATCH_SIZE, new IntegerType(123));
		input.addParameter(ReindexJobParameters.REINDEX_SEARCH_PARAMETERS, new CodeType("none"));
		input.addParameter(ReindexJobParameters.OPTIMISTIC_LOCK, new BooleanType(false));
		input.addParameter(ReindexJobParameters.OPTIMIZE_STORAGE, new CodeType("current_version"));
		input.addParameter(ReindexJobParameters.CORRECT_CURRENT_VERSION, new CodeType("ALL"));

		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// Execute
		OperationOutcome response = myServerExtension
			.getFhirClient()
			.operation()
			.onServer()
			.named(ProviderConstants.OPERATION_REINDEX)
			.withParameters(input)
			.returnResourceType(OperationOutcome.class)
			.execute();

		// Verify
		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response));
		assertThat(response.getIssueFirstRep().getDiagnostics()).containsSubsequence(TEST_JOB_ID);

		verify(myJobCoordinator, times(1)).startInstance(isNotNull(), myStartRequestCaptor.capture());

		ReindexJobParameters params = myStartRequestCaptor.getValue().getParameters(ReindexJobParameters.class);
		if (isBlank(theUrl)) {
			assertThat(params.getPartitionedUrls()).containsExactlyInAnyOrder(
				new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1)),
				new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1))
			);
		} else {
			RequestPartitionId partitionId = RequestPartitionId.fromPartitionId(1);
			final PartitionedUrl partitionedUrl = new PartitionedUrl().setUrl(theUrl).setRequestPartitionId(partitionId);
			assertThat(params.getPartitionedUrls().iterator().next()).isEqualTo(partitionedUrl);
		}

		// Non-default values
		assertEquals(ReindexParameters.ReindexSearchParametersEnum.NONE, params.getReindexSearchParameters());
		assertFalse(params.getOptimisticLock());
		assertEquals(ReindexParameters.OptimizeStorageModeEnum.CURRENT_VERSION, params.getOptimizeStorage());
		assertEquals(ReindexParameters.CorrectCurrentVersionModeEnum.ALL, params.getCorrectCurrentVersion());
		assertEquals(123, params.getBatchSize());
	}

	@Test
	public void testReindex_withDefaults() {
		// setup
		Parameters input = new Parameters();
		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// Execute
		MethodOutcome response = myServerExtension
				.getFhirClient()
				.operation()
				.onServer()
				.named(ProviderConstants.OPERATION_REINDEX)
				.withParameters(input)
				.returnMethodOutcome()
				.execute();

		// Verify
		String expectedPollUrl = "http://localhost:" + myServerExtension.getPort() + "/$hapi.fhir.bulk-patch-status?_jobId=test-job-id";

		String serializedOutput = myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response.getOperationOutcome());
		ourLog.info(serializedOutput);
		assertThat(serializedOutput).contains("$reindex job has been accepted. Poll for status at the following URL: " + expectedPollUrl);
		assertEquals(expectedPollUrl, response.getResponseHeaders().get(Constants.HEADER_CONTENT_LOCATION).get(0));

		verify(myJobCoordinator, times(1)).startInstance(isNotNull(), myStartRequestCaptor.capture());
		ReindexJobParameters params = myStartRequestCaptor.getValue().getParameters(ReindexJobParameters.class);

		// Default values
		assertEquals(ReindexParameters.ReindexSearchParametersEnum.ALL, params.getReindexSearchParameters());
		assertTrue(params.getOptimisticLock());
		assertEquals(ReindexParameters.OptimizeStorageModeEnum.NONE, params.getOptimizeStorage());
		assertEquals(ReindexParameters.CorrectCurrentVersionModeEnum.NONE, params.getCorrectCurrentVersion());
		assertThat(params.getPartitionedUrls()).containsExactlyInAnyOrder(
			new PartitionedUrl().setUrl("Observation?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1)),
			new PartitionedUrl().setUrl("Patient?").setRequestPartitionId(RequestPartitionId.fromPartitionId(1))
		);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testReindex_noPreferHeader(boolean theOmitHeader) {
		// setup
		Parameters input = new Parameters();
		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// Execute
		MethodOutcome response = myServerExtension
				.getFhirClient()
				.operation()
				.onServer()
				.named(ProviderConstants.OPERATION_REINDEX)
				.withParameters(input)
			.withAdditionalHeader(Constants.HEADER_PREFER, theOmitHeader ? "123" : Constants.HEADER_PREFER_RESPOND_ASYNC)
				.returnMethodOutcome()
				.execute();

		// Verify
		String serializedResponseBody = myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(response.getOperationOutcome());
		String warning = "This method should be invoked with the Prefer: respond-async header";
		if (theOmitHeader) {
			assertThat(serializedResponseBody).contains(warning);
		} else {
			assertThat(serializedResponseBody).doesNotContain(warning);
		}
	}

	@Test
	public void testReindex_invalidRequest_BadCorrectCurrentVersionValue() {
		// setup
		Parameters input = new Parameters();
		input.addParameter(ReindexJobParameters.CORRECT_CURRENT_VERSION, new CodeType("BAD_VALUE"));
		ourLog.debug(myCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		// Execute
		assertThatThrownBy(()->myServerExtension
				.getFhirClient()
				.operation()
				.onServer()
				.named(ProviderConstants.OPERATION_REINDEX)
				.withParameters(input)
				.execute())
				// Verify
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("Invalid correctCurrentVersion value: BAD_VALUE");

	}
}
