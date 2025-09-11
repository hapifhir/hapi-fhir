package ca.uhn.fhir.jpa.dao;

// Created by Sonnet 4

import ca.uhn.fhir.interceptor.model.IDefaultPartitionSettings;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaBulkDataExportHistoryHelperTest {

	@Mock
	private PersistedJpaBundleProviderFactory myBundleProviderFactory;

	@InjectMocks
	private JpaBulkDataExportHistoryHelper myHistoryHelper;

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

		when(myBundleProviderFactory.historyFromResourceIds(eq(resourceType), eq(resourceIds), eq(partitionId)))
			.thenReturn(myMockBundleProvider);

		// When
		IBundleProvider result = myHistoryHelper.fetchHistoryForResourceIds(resourceType, resourceIds, partitionId);

		// Then
		assertThat(result).isSameAs(myMockBundleProvider);

		verify(myBundleProviderFactory).historyFromResourceIds(eq(resourceType), eq(resourceIds), eq(partitionId));
	}

	@Test
	void fetchHistoryForResourceIds_withEmptyResourceIds_shouldCallBundleProviderFactory() {
		// Given
		String resourceType = "Observation";
		List<String> resourceIds = Collections.emptyList();
		RequestPartitionId partitionId = RequestPartitionId.defaultPartition(new IDefaultPartitionSettings() {
		});

		when(myBundleProviderFactory.historyFromResourceIds(eq(resourceType), eq(resourceIds), eq(partitionId)))
			.thenReturn(myMockBundleProvider);

		// When
		IBundleProvider result = myHistoryHelper.fetchHistoryForResourceIds(resourceType, resourceIds, partitionId);

		// Then
		assertThat(result).isSameAs(myMockBundleProvider);

		verify(myBundleProviderFactory).historyFromResourceIds(eq(resourceType), eq(resourceIds), eq(partitionId));
	}

	@Test
	void fetchHistoryForResourceIds_withNullResourceType_shouldThrowException() {
		// Given
		List<String> resourceIds = List.of("123");
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();

		// When/Then
		assertThatThrownBy(() ->
			myHistoryHelper.fetchHistoryForResourceIds(null, resourceIds, partitionId))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void fetchHistoryForResourceIds_withNullResourceIds_shouldCallHistoryUnlimited() {
		// Given
		String resourceType = "Patient";
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();

		when(myBundleProviderFactory.historyFromResourceIds(eq(resourceType), eq(null), eq(partitionId)))
			.thenReturn(myMockBundleProvider);

		// When
		IBundleProvider result = myHistoryHelper.fetchHistoryForResourceIds(resourceType, null, partitionId);

		// Then
		assertThat(result).isSameAs(myMockBundleProvider);
		
		verify(myBundleProviderFactory).historyFromResourceIds(eq(resourceType), eq(null), eq(partitionId));
	}

	@Test
	void fetchHistoryForResourceIds_withSingleResourceId_shouldCallBundleProviderFactory() {
		// Given
		String resourceType = "Encounter";
		List<String> resourceIds = List.of("single-id");
		RequestPartitionId partitionId = RequestPartitionId.fromPartitionIds(1, 2);

		when(myBundleProviderFactory.historyFromResourceIds(eq(resourceType), eq(resourceIds), eq(partitionId)))
			.thenReturn(myMockBundleProvider);

		// When
		IBundleProvider result = myHistoryHelper.fetchHistoryForResourceIds(resourceType, resourceIds, partitionId);

		// Then
		assertThat(result).isSameAs(myMockBundleProvider);

		verify(myBundleProviderFactory).historyFromResourceIds(eq(resourceType), eq(resourceIds), eq(partitionId));
	}
}
