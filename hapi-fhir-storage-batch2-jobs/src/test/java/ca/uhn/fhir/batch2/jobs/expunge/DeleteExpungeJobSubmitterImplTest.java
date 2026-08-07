package ca.uhn.fhir.batch2.jobs.expunge;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// Created by claude-opus-4-6
@ExtendWith(MockitoExtension.class)
class DeleteExpungeJobSubmitterImplTest {

	@Mock
	private IJobCoordinator myJobCoordinator;
	@Mock
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Mock
	private JpaStorageSettings myStorageSettings;
	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	private IJobPartitionProvider myJobPartitionProvider;
	@Mock
	private RequestDetails myRequestDetails;

	@InjectMocks
	private DeleteExpungeJobSubmitterImpl mySvc;

	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myStartRequestCaptor;

	@BeforeEach
	void beforeEach() {
		when(myStorageSettings.canDeleteExpunge()).thenReturn(true);
	}

	private void setupJobCoordinator() {
		Batch2JobStartResponse jobResponse = new Batch2JobStartResponse();
		jobResponse.setInstanceId("test-job-id");
		when(myJobCoordinator.startInstance(any(RequestDetails.class), any())).thenReturn(jobResponse);
	}

	private DeleteExpungeJobParameters captureJobParameters() {
		verify(myJobCoordinator).startInstance(eq(myRequestDetails), myStartRequestCaptor.capture());
		return myStartRequestCaptor.getValue().getParameters(DeleteExpungeJobParameters.class);
	}

	@Test
	void submitJob_withUrls_delegatesToJobPartitionProvider() {
		List<String> urls = List.of("Patient?active=false", "Observation?status=active");
		PartitionedUrl pu1 = new PartitionedUrl().setUrl("Patient?active=false").setRequestPartitionId(RequestPartitionId.fromPartitionId(0));
		PartitionedUrl pu2 = new PartitionedUrl().setUrl("Observation?status=active").setRequestPartitionId(RequestPartitionId.fromPartitionId(0));
		when(myJobPartitionProvider.getPartitionedUrls(any(), eq(urls))).thenReturn(List.of(pu1, pu2));
		setupJobCoordinator();

		String jobId = mySvc.submitJob(100, urls, true, 5, myRequestDetails);

		assertThat(jobId).isEqualTo("test-job-id");
		verify(myJobPartitionProvider).getPartitionedUrls(myRequestDetails, urls);
		DeleteExpungeJobParameters params = captureJobParameters();
		assertThat(params.getPartitionedUrls()).containsExactly(pu1, pu2);
		assertThat(params.getBatchSize()).isEqualTo(100);
		assertThat(params.isCascade()).isTrue();
		assertThat(params.getCascadeMaxRounds()).isEqualTo(5);
	}

	@Test
	void submitJob_withMixedBlankAndValidUrls_onlyPassesValidUrlsToProvider() {
		PartitionedUrl pu = new PartitionedUrl().setUrl("Patient?active=true").setRequestPartitionId(RequestPartitionId.fromPartitionId(0));
		when(myJobPartitionProvider.getPartitionedUrls(any(), eq(List.of("Patient?active=true")))).thenReturn(List.of(pu));
		setupJobCoordinator();

		mySvc.submitJob(100, List.of("", "Patient?active=true", "  "), false, null, myRequestDetails);

		verify(myJobPartitionProvider).getPartitionedUrls(myRequestDetails, List.of("Patient?active=true"));
		assertThat(captureJobParameters().getPartitionedUrls()).containsExactly(pu);
	}

	@Test
	void submitJob_providerExpandsAllPartitions_expansionPassedThrough() {
		List<String> urls = List.of("Patient?active=false");
		PartitionedUrl pu1 = new PartitionedUrl().setUrl("Patient?active=false").setRequestPartitionId(RequestPartitionId.fromPartitionId(1));
		PartitionedUrl pu2 = new PartitionedUrl().setUrl("Patient?active=false").setRequestPartitionId(RequestPartitionId.fromPartitionId(2));
		when(myJobPartitionProvider.getPartitionedUrls(any(), eq(urls))).thenReturn(List.of(pu1, pu2));
		setupJobCoordinator();

		mySvc.submitJob(50, urls, false, null, myRequestDetails);

		assertThat(captureJobParameters().getPartitionedUrls()).containsExactly(pu1, pu2);
	}

	static Stream<List<String>> emptyUrlLists() {
		return Stream.of(
				List.of(),
				List.of(""),
				List.of("", "  ")
		);
	}

	@ParameterizedTest
	@MethodSource("emptyUrlLists")
	void submitJob_noEffectiveUrls_fallsBackToServerOperationPartition(List<String> theUrls) {
		RequestPartitionId partition = RequestPartitionId.fromPartitionId(1);
		when(myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
				any(), eq(ProviderConstants.OPERATION_DELETE_EXPUNGE))).thenReturn(partition);
		setupJobCoordinator();

		mySvc.submitJob(50, theUrls, false, null, myRequestDetails);

		verifyNoInteractions(myJobPartitionProvider);
		DeleteExpungeJobParameters params = captureJobParameters();
		assertThat(params.getPartitionedUrls()).hasSize(1);
		assertThat(params.getPartitionedUrls().get(0).getRequestPartitionId()).isEqualTo(partition);
		assertThat(params.getPartitionedUrls().get(0).getUrl()).isNull();
	}

	@Test
	void submitJob_nullBatchSize_usesStorageSettingsDefault() {
		when(myStorageSettings.getExpungeBatchSize()).thenReturn(999);
		List<String> urls = List.of("Patient?active=false");
		PartitionedUrl pu = new PartitionedUrl().setUrl("Patient?active=false").setRequestPartitionId(RequestPartitionId.allPartitions());
		when(myJobPartitionProvider.getPartitionedUrls(any(), eq(urls))).thenReturn(List.of(pu));
		setupJobCoordinator();

		mySvc.submitJob(null, urls, false, null, myRequestDetails);

		assertThat(captureJobParameters().getBatchSize()).isEqualTo(999);
	}

	@Test
	void submitJob_deleteExpungeDisabled_throwsForbidden() {
		when(myStorageSettings.canDeleteExpunge()).thenReturn(false);
		when(myStorageSettings.cannotDeleteExpungeReason()).thenReturn("not allowed");

		assertThatThrownBy(() -> mySvc.submitJob(100, List.of("Patient?"), false, null, myRequestDetails))
				.hasMessageContaining("Delete Expunge not allowed");
	}
}
