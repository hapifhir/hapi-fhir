package ca.uhn.fhir.jpa.dao;

// Created by Sonnet 4

import ca.uhn.fhir.interceptor.model.IDefaultPartitionSettings;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaBulkDataExportHistoryHelperTest {

	@Mock
	private PersistedJpaBundleProviderFactory myBundleProviderFactory;

	@InjectMocks
	private JpaBulkDataExportHistoryHelper myHistoryHelper;

	@Captor
	private ArgumentCaptor<RequestDetails> myRequestDetailsCaptor;

	@Captor
	private ArgumentCaptor<RequestPartitionId> myPartitionIdCaptor;

	private IBundleProvider myMockBundleProvider;

	@BeforeEach
	void setUp() {
		myMockBundleProvider = new SimpleBundleProvider(Collections.emptyList());
	}

	@Test
	void fetchHistoryForResourceIds_withValidInputs_shouldCallBundleProviderFactory() {
		// Given
		String resourceType = "Patient";
		List<String> resourceIds = Arrays.asList("123", "456", "789");
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();

		when(myBundleProviderFactory.history(any(), eq(resourceType), isNull(), isNull(), isNull(), isNull(), eq(partitionId)))
			.thenReturn(myMockBundleProvider);

		// When
		IBundleProvider result = myHistoryHelper.fetchHistoryForResourceIds(resourceType, partitionId);

		// Then
		assertThat(result).isSameAs(myMockBundleProvider);
		
		verify(myBundleProviderFactory).history(
			myRequestDetailsCaptor.capture(), 
			eq(resourceType), 
			isNull(), 
			isNull(), 
			isNull(), 
			isNull(), 
			myPartitionIdCaptor.capture()
		);

		RequestDetails capturedRequestDetails = myRequestDetailsCaptor.getValue();
		assertThat(capturedRequestDetails.getResourceName()).isEqualTo(resourceType);
		assertThat(capturedRequestDetails.getParameters().get(PARAM_ID))
			.containsExactlyInAnyOrder("123", "456", "789");
		
		assertThat(myPartitionIdCaptor.getValue()).isSameAs(partitionId);
	}

	@Test
	void fetchHistoryForResourceIds_withEmptyResourceIds_shouldCallBundleProviderFactory() {
		// Given
		String resourceType = "Observation";
		List<String> resourceIds = Collections.emptyList();
		RequestPartitionId partitionId = RequestPartitionId.defaultPartition(new IDefaultPartitionSettings() {
		});

		when(myBundleProviderFactory.history(any(), eq(resourceType), isNull(), isNull(), isNull(), isNull(), eq(partitionId)))
			.thenReturn(myMockBundleProvider);

		// When
		IBundleProvider result = myHistoryHelper.fetchHistoryForResourceIds(resourceType, partitionId);

		// Then
		assertThat(result).isSameAs(myMockBundleProvider);
		
		verify(myBundleProviderFactory).history(
			myRequestDetailsCaptor.capture(), 
			eq(resourceType), 
			isNull(), 
			isNull(), 
			isNull(), 
			isNull(), 
			eq(partitionId)
		);

		RequestDetails capturedRequestDetails = myRequestDetailsCaptor.getValue();
		assertThat(capturedRequestDetails.getResourceName()).isEqualTo(resourceType);
		assertThat(capturedRequestDetails.getParameters().get(PARAM_ID))
			.hasSize(0);
	}

	@Test
	void fetchHistoryForResourceIds_withNullResourceType_shouldThrowException() {
		// Given
		List<String> resourceIds = List.of("123");
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();

		// When/Then
		assertThatThrownBy(() -> 
			myHistoryHelper.fetchHistoryForResourceIds(null, partitionId))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void fetchHistoryForResourceIds_withNullResourceIds_shouldThrowException() {
		// Given
		String resourceType = "Patient";
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();

		// When/Then
		assertThatThrownBy(() -> 
			myHistoryHelper.fetchHistoryForResourceIds(resourceType, partitionId))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void fetchHistoryForResourceIds_withSingleResourceId_shouldCallBundleProviderFactory() {
		// Given
		String resourceType = "Encounter";
		List<String> resourceIds = List.of("single-id");
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIds(1, 2);

		when(myBundleProviderFactory.history(any(), eq(resourceType), isNull(), isNull(), isNull(), isNull(), eq(partitionId)))
			.thenReturn(myMockBundleProvider);

		// When
		IBundleProvider result = myHistoryHelper.fetchHistoryForResourceIds(resourceType, partitionId);

		// Then
		assertThat(result).isSameAs(myMockBundleProvider);
		
		verify(myBundleProviderFactory).history(
			myRequestDetailsCaptor.capture(), 
			eq(resourceType), 
			isNull(), 
			isNull(), 
			isNull(), 
			isNull(), 
			eq(partitionId)
		);

		RequestDetails capturedRequestDetails = myRequestDetailsCaptor.getValue();
		assertThat(capturedRequestDetails.getParameters().get(PARAM_ID))
			.containsExactly("single-id");
	}
}
