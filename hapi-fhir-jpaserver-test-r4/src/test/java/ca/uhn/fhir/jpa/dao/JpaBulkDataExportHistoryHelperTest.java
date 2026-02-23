package ca.uhn.fhir.jpa.dao;

// Created by Sonnet 4

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JpaBulkDataExportHistoryHelperTest {

	@Mock
	private PersistedJpaBundleProviderFactory myBundleProviderFactory;

	@InjectMocks
	private JpaBulkDataExportHistoryHelper myHistoryHelper;

	private final Date myStartPeriod = new Date();
	private final Date myEndPeriod = new Date();

	@Test
	void fetchHistoryForResourceIds_callsBundleProviderFactory() {
		// Given
		String resourceType = "Patient";
		List<IResourcePersistentId<?>> resourceIds =List.of(
			JpaPid.fromId(123L),
			JpaPid.fromId(456L),
			JpaPid.fromId(789L)
		);
		RequestPartitionId partitionId = RequestPartitionId.allPartitions();

		// When
		myHistoryHelper.fetchHistoryForResourceIds(resourceType, resourceIds, partitionId, myStartPeriod, myEndPeriod);

		// Then
		verify(myBundleProviderFactory).historyFromResourceIds(resourceType, resourceIds, partitionId, myStartPeriod, myEndPeriod);
	}

}
