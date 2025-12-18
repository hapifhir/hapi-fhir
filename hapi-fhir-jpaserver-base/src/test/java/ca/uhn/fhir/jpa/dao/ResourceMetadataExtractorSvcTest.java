package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryProvenanceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.StorageSettings.TagStorageModeEnum;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static ca.uhn.fhir.jpa.dao.IResourceMetadataExtractorSvc.ProvenanceDetails;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceMetadataExtractorSvcTest {

	@Mock
	private IResourceHistoryTagDao myResourceHistoryTagDao;
	@Mock
	private IResourceTagDao myResourceTagDao;
	@Mock
	private IResourceHistoryProvenanceDao myResourceHistoryProvenanceDao;

	private ResourceHistoryTable myHistoryEntity;
	private ResourceTable myResourceTable;
	private IResourceMetadataExtractorSvc myResourceTagReadSvc;
	private JpaStorageSettings myStorageSettings;

	@BeforeEach
	void setUp() {
		myStorageSettings = new JpaStorageSettings();
		myStorageSettings.setAccessMetaSourceInformationFromProvenanceTable(false);
		myResourceTagReadSvc = new ResourceMetadataExtractorSvcImpl(myStorageSettings, myResourceHistoryTagDao,
			myResourceTagDao, myResourceHistoryProvenanceDao);
		myHistoryEntity = new ResourceHistoryTable();
		myResourceTable = new ResourceTable();
	}

	@Test
	void getTagsFromHistoryTable_withVersionedStorageMode_returnsTags() {
		// setup
		myStorageSettings.setTagStorageMode(TagStorageModeEnum.VERSIONED);
		List<ResourceHistoryTag> expectedTags = List.of(new ResourceHistoryTag(), new ResourceHistoryTag());
		myHistoryEntity.getTags().addAll(expectedTags);
		myHistoryEntity.setHasTags(true);

		// execute
		Collection<? extends BaseTag> result = myResourceTagReadSvc.getTags(myHistoryEntity);

		// verify
		assertThat(result).isNotNull().hasSize(2)
			.map(ResourceHistoryTag.class::cast)
			.containsExactlyInAnyOrderElementsOf(myHistoryEntity.getTags());
	}

	@Test
	void getTagsFromHistoryTable_withNonVersionedStorageMode_returnsTags() {
		// setup
		myStorageSettings.setTagStorageMode(TagStorageModeEnum.NON_VERSIONED);
		createTagsForResourceTable();
		myHistoryEntity.setResourceTable(myResourceTable);

		// execute
		Collection<? extends BaseTag> result = myResourceTagReadSvc.getTags(myHistoryEntity);

		// verify
		assertThat(result).isNotNull().hasSize(2)
			.map(ResourceTag.class::cast)
			.containsExactlyInAnyOrderElementsOf(myResourceTable.getTags());
	}

	@ParameterizedTest
	@EnumSource(value = TagStorageModeEnum.class, names = {"NON_VERSIONED", "VERSIONED"})
	void getTagsFromResourceTable_withNonVersionedAndVersionedStorageMode_returnsTags(TagStorageModeEnum theStorageMode) {
		// setup
		myStorageSettings.setTagStorageMode(theStorageMode);
		createTagsForResourceTable();

		// execute
		Collection<? extends BaseTag> result = myResourceTagReadSvc.getTags(myResourceTable);

		// verify
		assertThat(result).isNotNull().hasSize(2)
			.map(ResourceTag.class::cast)
			.containsExactlyInAnyOrderElementsOf(myResourceTable.getTags());
	}

	private void createTagsForResourceTable() {
		myResourceTable.setHasTags(true);
		ResourceTag tag1 = new ResourceTag();
		tag1.setTag(new TagDefinition(TagTypeEnum.TAG, "http://example.com", "code", "test"));
		ResourceTag tag2 = new ResourceTag();
		myResourceTable.getTags().addAll(Set.of(tag1, tag2));
	}

	@ParameterizedTest
	@EnumSource(TagStorageModeEnum.class)
	void getTagsFromHistoryTable_hasNoTags_returnsNull(TagStorageModeEnum theStorageMode) {
		// setup
		myStorageSettings.setTagStorageMode(theStorageMode);
		myHistoryEntity.setResourceTable(myResourceTable);

		// execute
		Collection<? extends BaseTag> result = myResourceTagReadSvc.getTags(myHistoryEntity);

		// verify
		assertNull(result);
	}

	@ParameterizedTest
	@EnumSource(value = TagStorageModeEnum.class, names = {"NON_VERSIONED", "VERSIONED"})
	void getTagsFromResourceTable_hasNoTags_returnsEmptyList(TagStorageModeEnum theStorageMode) {
		// setup
		myStorageSettings.setTagStorageMode(theStorageMode);

		// execute
		Collection<? extends BaseTag> result = myResourceTagReadSvc.getTags(myResourceTable);

		// verify
		assertThat(result).isNotNull().isEmpty();
	}

	@Test
	void getTagsFromResourceTable_withInlineStorageMode_returnsNull() {
		// setup
		myStorageSettings.setTagStorageMode(TagStorageModeEnum.INLINE);

		// execute
		Collection<? extends BaseTag> result = myResourceTagReadSvc.getTags(myResourceTable);

		// verify
		assertNull(result);
	}

	@Test
	void getTagsBatch_withVersionedMode_returnsTagMap() {
		// setup
		myStorageSettings.setTagStorageMode(TagStorageModeEnum.VERSIONED);

		List<ResourceHistoryTable> historyEntities = createHistoryEntityList();

		ResourceHistoryTag tag1 = new ResourceHistoryTag();
		tag1.setResourceId(1L);
		ResourceHistoryTag tag2 = new ResourceHistoryTag();
		tag2.setResourceId(1L);
		ResourceHistoryTag tag3 = new ResourceHistoryTag();
		tag3.setResourceId(2L);

		when(myResourceHistoryTagDao.findByVersionIds(anyList())).thenReturn(List.of(tag1, tag2, tag3));

		// execute
		Map<JpaPid, Collection<BaseTag>> result = myResourceTagReadSvc.getTagsBatch(historyEntities);

		// verify
		assertThat(result).hasSize(2);
		assertThat(result.get(tag1.getResourcePid())).hasSize(2).containsExactlyInAnyOrder(tag1, tag2);
		assertThat(result.get(tag3.getResourcePid())).hasSize(1).containsExactlyInAnyOrder(tag3);
	}

	@Test
	void getTagsBatch_withNonVersionedMode_returnsTagMap() {
		// setup
		myStorageSettings.setTagStorageMode(TagStorageModeEnum.NON_VERSIONED);

		List<ResourceHistoryTable> historyEntities = createHistoryEntityList();

		ResourceTag tag1 = new ResourceTag();
		tag1.setResourceId(1L);
		ResourceTag tag2 = new ResourceTag();
		tag2.setResourceId(1L);
		ResourceTag tag3 = new ResourceTag();
		tag3.setResourceId(2L);

		when(myResourceTagDao.findByResourceIds(anyList())).thenReturn(List.of(tag1, tag2, tag3));

		// execute
		Map<JpaPid, Collection<BaseTag>> result = myResourceTagReadSvc.getTagsBatch(historyEntities);

		// verify
		assertThat(result).hasSize(2);
		assertThat(result.get(tag1.getResourceId())).hasSize(2).containsExactlyInAnyOrder(tag1, tag2);
		assertThat(result.get(tag3.getResourceId())).hasSize(1).containsExactlyInAnyOrder(tag3);
	}

	private static List<ResourceHistoryTable> createHistoryEntityList() {
		ResourceHistoryTable entity1 = new ResourceHistoryTable();
		entity1.setHasTags(true);
		ResourceHistoryTable entity2 = new ResourceHistoryTable();
		entity2.setHasTags(true);
		return List.of(entity1, entity2);
	}

	@ParameterizedTest
	@EnumSource(TagStorageModeEnum.class)
	void getTagsBatch_noTags_returnsEmptyMap(TagStorageModeEnum theStorageMode) {
		// setup
		myStorageSettings.setTagStorageMode(theStorageMode);

		ResourceHistoryTable entity1 = new ResourceHistoryTable();
		entity1.setHasTags(false);

		// execute
		Map<JpaPid, Collection<BaseTag>> result = myResourceTagReadSvc.getTagsBatch(List.of(entity1));

		// verify
		assertThat(result).isEmpty();
	}

	@ParameterizedTest
	@CsvSource({
		"http://example.com, request-123, return sourceUri and requestId from entity",
		"http://example.com, 			, return sourceUri from entity",
		"				   , request-123, return requestId from entity",
		"				   , 			, sourceUri and requestId null return null"
	})
	void getProvenanceDetails_withSourceUriAndRequestIdOnEntity_returnsProvenanceFromEntity(String theSourceUri,
																							String theRequestId,
																							String theMessage) {
		// setup
		myHistoryEntity.setSourceUri(theSourceUri);
		myHistoryEntity.setRequestId(theRequestId);

		// execute
		ProvenanceDetails result = myResourceTagReadSvc.getProvenanceDetails(myHistoryEntity);

		// verify
		assertNotNull(result);
		assertEquals(theSourceUri, result.provenanceSourceUri(), theMessage);
		assertEquals(theRequestId, result.provenanceRequestId(), theMessage);
		verify(myResourceHistoryProvenanceDao, never()).findById(any());
	}

	@ParameterizedTest
	@ValueSource(strings = {""})
	@NullSource
	void getProvenanceDetails_withBlankValuesAndProvenanceTableEnabled_fetchesFromProvenanceTable(String theValue) {
		// setup
		myStorageSettings.setAccessMetaSourceInformationFromProvenanceTable(true);
		myHistoryEntity.setSourceUri(theValue);
		myHistoryEntity.setRequestId(theValue);
		ResourceHistoryProvenanceEntity provenanceEntity = new ResourceHistoryProvenanceEntity();
		provenanceEntity.setSourceUri("http://example.com");
		provenanceEntity.setRequestId("requestId");
		when(myResourceHistoryProvenanceDao.findById(any())).thenReturn(Optional.of(provenanceEntity));

		// execute
		ProvenanceDetails result = myResourceTagReadSvc.getProvenanceDetails(myHistoryEntity);

		// verify
		assertNotNull(result);
		assertEquals("http://example.com", result.provenanceSourceUri());
		assertEquals("requestId", result.provenanceRequestId());
	}

	@Test
	void getProvenanceDetails_withNullValuesAndProvenanceTableEnabledButNoData_returnsBlankValues() {
		// setup
		myStorageSettings.setAccessMetaSourceInformationFromProvenanceTable(true);
		myHistoryEntity.setSourceUri(null);
		myHistoryEntity.setRequestId(null);
		when(myResourceHistoryProvenanceDao.findById(any())).thenReturn(Optional.empty());

		// execute
		ProvenanceDetails result = myResourceTagReadSvc.getProvenanceDetails(myHistoryEntity);

		// verify
		assertNotNull(result);
		assertNull(result.provenanceSourceUri());
		assertNull(result.provenanceRequestId());
	}
}
