/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.IResourceMetadataExtractorSvc;
import ca.uhn.fhir.jpa.dao.IResourceMetadataExtractorSvc.ProvenanceDetails;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceServiceRegistry;
import ca.uhn.fhir.jpa.esr.IExternallyStoredResourceService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.search.BatchResourceLoader.ResourceLoadResult;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.IMetaTagSorter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchResourceLoaderTest {

	@Mock
	private IResourceMetadataExtractorSvc myResourceMetadataExtractorSvc;
	@Mock
	private IJpaStorageResourceParser myJpaStorageResourceParser;
	@Mock
	private ExternallyStoredResourceServiceRegistry myExternallyStoredResourceServiceRegistry;
	@Mock
	private IMetaTagSorter myMetaTagSorter;
	@Mock
	private PartitionSettings myPartitionSettings;
	@Mock
	private IPartitionLookupSvc myPartitionLookupSvc;
	@Mock
	private IExternallyStoredResourceService myExternallyStoredResourceService;
	@Captor
	private ArgumentCaptor<IBaseResource> myResourceCaptor;

	private BatchResourceLoader myBatchResourceLoader;
	private FhirContext fhirContext = FhirContext.forR4Cached();

	private boolean myForHistoryOperation;

	@BeforeEach
	void setUp() {
		myBatchResourceLoader = new BatchResourceLoader(
			FhirContext.forR4Cached(),
			myResourceMetadataExtractorSvc,
			myJpaStorageResourceParser,
			myExternallyStoredResourceServiceRegistry,
			myMetaTagSorter,
			myPartitionSettings,
			myPartitionLookupSvc
		);

		myForHistoryOperation = false;

		// Common mock setups
		lenient().when(myPartitionSettings.isPartitioningEnabled()).thenReturn(false);
		lenient().when(myResourceMetadataExtractorSvc.getTagsBatch(any()))
			.thenReturn(new HashMap<>());
		lenient().when(myResourceMetadataExtractorSvc.getProvenanceDetails(any()))
			.thenReturn(new ProvenanceDetails(null, null));
	}

	@Test
	void testLoadResources_withEmptyList_returnsEmptyList() {
		// execute
		List<ResourceLoadResult> results = myBatchResourceLoader.loadResources(Collections.emptyList(), myForHistoryOperation);

		// verify
		assertThat(results).isEmpty();
	}

	@ParameterizedTest
	@CsvSource({
		"DEL, false, Resource encoding is DEL - mark as deleted",
		"	, true , Resource has deleted date - mark as deleted"
	})
	void testLoadResources_withDeletedResource_returnsDeletedResult(ResourceEncodingEnum theEncoding, boolean isDeleted, String theMessage) {
		// setup
		ResourceHistoryTable entity = new ResourceHistoryTable();
		entity.setResourceId(1L);
		entity.setEncoding(theEncoding);
		if (isDeleted) {
			entity.setDeleted(Date.from(Instant.now()));
		}

		// execute
		List<ResourceLoadResult> results = myBatchResourceLoader.loadResources(List.of(entity), myForHistoryOperation);

		// verify
		assertThat(results).hasSize(1);
		validateDeletedResource(results, entity.getPersistentId());
	}

	@Test
	void testLoadResources_withMultipleMixedResources_handlesAllTypesCorrectly() {
		// setup
		String providerId = "test-provider";
		ResourceHistoryTable deleted1 = createDeletedResourceEntity(1L);
		ResourceHistoryTable deleted2 = createDeletedResourceEntity(2L);
		ResourceHistoryTable json1 = createJsonResourceEntity(3L, createPatientJson("Patient3"));
		ResourceHistoryTable json2 = createJsonResourceEntity(4L, createPatientJson("Patient4"));
		ResourceHistoryTable esr1 = createEsrResourceEntity(5L, providerId, "address5");
		ResourceHistoryTable esr2 = createEsrResourceEntity(6L, providerId, "address6");
		List<ResourceHistoryTable> entities = List.of(deleted1, deleted2, json1, json2, esr1, esr2);

		Map<JpaPid, Collection<BaseTag>> tagsMap = new HashMap<>();
		when(myResourceMetadataExtractorSvc.getTagsBatch(entities)).thenReturn(tagsMap);
		when(myExternallyStoredResourceServiceRegistry.getProvider(providerId))
			.thenReturn(myExternallyStoredResourceService);
		Patient esrPatient1 = createPatient("Patient5");
		Patient esrPatient2 = createPatient("Patient6");
		when(myExternallyStoredResourceService.fetchResource("address5")).thenReturn(esrPatient1);
		when(myExternallyStoredResourceService.fetchResource("address6")).thenReturn(esrPatient2);

		// execute
		List<ResourceLoadResult> results = myBatchResourceLoader.loadResources(entities, false);

		// verify
		verify(myExternallyStoredResourceServiceRegistry, times(2)).getProvider(providerId);
		verify(myExternallyStoredResourceService, times(2)).fetchResource(anyString());
		verify(myJpaStorageResourceParser, times(4)).populateResourceMetadata(
			any(ResourceHistoryTable.class), eq(myForHistoryOperation), any(), anyLong(), any(IBaseResource.class)
		);
		assertThat(results).hasSize(6);
		validateDeletedResource(results, deleted1.getResourceId());
		validateDeletedResource(results, deleted2.getResourceId());
		validateJsonResource(results, json1.getResourceId(), json1.getResourceTextVc());
		validateJsonResource(results, json2.getResourceId(), json2.getResourceTextVc());
		validateJsonResource(results, esr1.getPersistentId(), resourceToJson(esrPatient1));
		validateJsonResource(results, esr2.getPersistentId(), resourceToJson(esrPatient2));
	}

	private void validateDeletedResource(List<ResourceLoadResult> theResults, JpaPid  thePid) {
		Optional<ResourceLoadResult> resultOpt =  theResults.stream()
			.filter(r -> r.id().equals(thePid)).findFirst();
		assertThat(resultOpt).isPresent();
		ResourceLoadResult result = resultOpt.get();
		assertThat(result.isDeleted()).isTrue();
		assertThat(result.resource()).isNull();
	}

	private void validateJsonResource(List<ResourceLoadResult> theResults, JpaPid  thePid, String theJsonResource) {
		Optional<ResourceLoadResult> resultOpt =  theResults.stream()
			.filter(r -> r.id().equals(thePid)).findFirst();
		assertThat(resultOpt).isPresent();
		ResourceLoadResult result = resultOpt.get();
		assertThat(result.isDeleted()).isFalse();
		assertThat(result.resource()).isNotNull();
		assertThat(resourceToJson(result.resource())).isEqualTo(theJsonResource);
		assertNull(result.resource().getUserData(Constants.RESOURCE_PARTITION_ID));
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testLoadResources_withForHistoryOperationFlagAndTags_passesToPopulateMetadata(boolean theForHistoryOperation) {
		// setup
		ResourceHistoryTable jsonEntity = createJsonResourceEntity(1L, createPatientJson("Patient1"));
		ResourceHistoryTable esrEntity = createEsrResourceEntity(2L, "testProvider", "address1");
		when(myExternallyStoredResourceServiceRegistry.getProvider("testProvider"))
			.thenReturn(myExternallyStoredResourceService);
		when(myExternallyStoredResourceService.fetchResource(any())).thenReturn(createPatient("Patient2"));
		List<ResourceHistoryTable> entities = List.of(jsonEntity, esrEntity);

		Map<JpaPid, Collection<BaseTag>> tagsMap = new HashMap<>();
		Collection<BaseTag> tagsList = new ArrayList<>();
		tagsMap.put(jsonEntity.getPersistentId(), tagsList);
		tagsMap.put(esrEntity.getPersistentId(), tagsList);
		when(myResourceMetadataExtractorSvc.getTagsBatch(entities)).thenReturn(tagsMap);

		// execute
		myBatchResourceLoader.loadResources(entities, theForHistoryOperation);

		// verify
		verify(myJpaStorageResourceParser, times(2)).populateResourceMetadata(
			any(ResourceHistoryTable.class), eq(theForHistoryOperation), eq(tagsList), anyLong(), any(IBaseResource.class)
		);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testLoadResources_withPartitioningEnabled_populatesPartitionInformation(boolean theDefaultPartition) {
		// setup
		Integer partitionId = 1;
		ResourceHistoryTable jsonEntity = createJsonResourceEntity(1L, createPatientJson("Patient1"));
		ResourceHistoryTable esrEntity = createEsrResourceEntity(2L, "testProvider", "address1");
		when(myExternallyStoredResourceServiceRegistry.getProvider("testProvider"))
			.thenReturn(myExternallyStoredResourceService);
		when(myExternallyStoredResourceService.fetchResource(any())).thenReturn(createPatient("Patient2"));

		if (!theDefaultPartition) {
			jsonEntity.setPartitionId(new PartitionablePartitionId(partitionId, LocalDate.now()));
			esrEntity.setPartitionId(new PartitionablePartitionId(partitionId, LocalDate.now()));

			PartitionEntity partitionEntity = new PartitionEntity();
			partitionEntity.setId(partitionId);
			when(myPartitionLookupSvc.getPartitionById(partitionId)).thenReturn(partitionEntity);
		}

		List<ResourceHistoryTable> entities = List.of(jsonEntity, esrEntity);
		when(myResourceMetadataExtractorSvc.getTagsBatch(entities)).thenReturn(Collections.emptyMap());
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(true);

		// execute
		List<ResourceLoadResult> results = myBatchResourceLoader.loadResources(entities, false);

		// verify
		assertThat(results).hasSize(2).allSatisfy(result -> {
			assertNotNull(result);
			if (theDefaultPartition) {
				assertNull(result.resource().getUserData(Constants.RESOURCE_PARTITION_ID));
			} else {
				assertThat(result.resource().getUserData(Constants.RESOURCE_PARTITION_ID))
					.isEqualTo(RequestPartitionId.fromPartitionId(partitionId));
			}
		});
	}

	@Test
	void testLoadResources_withJsonResourceUnparsable_throwsDataFormatException() {
		// setup
		ResourceHistoryTable jsonResource = createJsonResourceEntity(3L, "ParseFailure");
		List<ResourceHistoryTable> entities = List.of(jsonResource);
		when(myResourceMetadataExtractorSvc.getTagsBatch(anyList())).thenReturn(Collections.emptyMap());

		// execute & verify
		assertThatThrownBy(() -> myBatchResourceLoader.loadResources(entities, false))
			.isInstanceOf(DataFormatException.class)
			.hasMessageContaining("Failed to parse database resource: Patient/3/_history/1")
			.hasMessageContaining("reason: HAPI-1861: Failed to parse JSON encoded FHIR content");
	}

	@Test
	void testLoadResources_withEsrResourceFetchFailure_throwsInternalErrorException() {
		// setup
		String providerId = "test-provider";
		String address = "test-address";
		ResourceHistoryTable entity = createEsrResourceEntity(1L, providerId, address);
		List<ResourceHistoryTable> entities = List.of(entity);

		when(myResourceMetadataExtractorSvc.getTagsBatch(entities)).thenReturn(Collections.emptyMap());
		when(myExternallyStoredResourceServiceRegistry.getProvider(providerId))
			.thenReturn(myExternallyStoredResourceService);
		when(myExternallyStoredResourceService.fetchResource(address))
			.thenThrow(new RuntimeException("Fetch failed"));

		// execute & verify
		assertThatThrownBy(() -> myBatchResourceLoader.loadResources(entities, false))
			.isInstanceOf(InternalErrorException.class)
			.hasMessageContaining("Failed to load externally stored resource: Patient/1/_history/1")
			.hasMessageContaining("reason: java.lang.RuntimeException: Fetch failed");
	}

	@Test
	void testLoadResources_withProvenanceDetails_populatesResourceSource() {
		// setup
		ResourceHistoryTable jsonEntity = createJsonResourceEntity(1L, createPatientJson("Patient1"));
		ResourceHistoryTable esrEntity = createEsrResourceEntity(2L, "testProvider", "address1");
		when(myExternallyStoredResourceServiceRegistry.getProvider("testProvider"))
			.thenReturn(myExternallyStoredResourceService);
		when(myExternallyStoredResourceService.fetchResource(any())).thenReturn(createPatient("Patient2"));
		List<ResourceHistoryTable> entities = List.of(jsonEntity, esrEntity);

		when(myResourceMetadataExtractorSvc.getTagsBatch(entities)).thenReturn(Collections.emptyMap());
		when(myResourceMetadataExtractorSvc.getProvenanceDetails(any(ResourceHistoryTable.class)))
			.thenReturn(new ProvenanceDetails("http://example.com/source", "request-123"));

		// execute
		List<ResourceLoadResult> results = myBatchResourceLoader.loadResources(entities, false);

		// verify
		assertThat(results).hasSize(2);
		// verify
		assertThat(results).hasSize(2).allSatisfy(result -> {
			assertNotNull(result);
			assertInstanceOf(Patient.class, result.resource());
			Patient patient = (Patient) result.resource();
			assertNotNull(patient.getMeta());
			assertNotNull(patient.getMeta().getSource());
			assertEquals("http://example.com/source#request-123", patient.getMeta().getSource());
		});

	}

	// Helper methods

	private ResourceHistoryTable createDeletedResourceEntity(Long thePid) {
		ResourceHistoryTable entity = new ResourceHistoryTable();
		entity.setResourceId(thePid);
		entity.setResourceType("Patient");
		entity.setVersion(1L);
		entity.setEncoding(ResourceEncodingEnum.DEL);
		entity.setTransientForcedId(thePid.toString());
		entity.setResourceTable(createResourceTable(thePid));
		return entity;
	}

	private ResourceHistoryTable createJsonResourceEntity(Long thePid, String theResourceJson) {
		ResourceHistoryTable entity = new ResourceHistoryTable();
		entity.setResourceId(thePid);
		entity.setResourceType("Patient");
		entity.setVersion(1L);
		entity.setEncoding(ResourceEncodingEnum.JSON);
		entity.setResourceTextVc(theResourceJson);
		entity.setResource(theResourceJson.getBytes());
		entity.setTransientForcedId(thePid.toString());
		entity.setResourceTable(createResourceTable(thePid));
		entity.setFhirVersion(FhirVersionEnum.R4);
		return entity;
	}

	private ResourceHistoryTable createEsrResourceEntity(Long thePid, String theProviderId, String theAddress) {
		ResourceHistoryTable entity = new ResourceHistoryTable();
		entity.setResourceId(thePid);
		entity.setResourceType("Patient");
		entity.setVersion(1L);
		entity.setEncoding(ResourceEncodingEnum.ESR);
		String esrContent = String.format("%s:%s", theProviderId, theAddress);
		entity.setResourceTextVc(esrContent);
		entity.setTransientForcedId(thePid.toString());
		entity.setResourceTable(createResourceTable(thePid));
		entity.setFhirVersion(FhirVersionEnum.R4);
		return entity;
	}

	private ResourceTable createResourceTable(Long thePid) {
		ResourceTable table = new ResourceTable();
		table.setIdForUnitTest(thePid);
		table.setFhirId(thePid.toString());
		table.setResourceType("Patient");
		return table;
	}

	private String resourceToJson(IBaseResource resource) {
		return  fhirContext.newJsonParser().encodeResourceToString(resource);
	}

	private String createPatientJson(String theId) {
		return String.format("{\"resourceType\":\"Patient\",\"id\":\"%s\"}", theId);
	}

	private Patient createPatient(String theId) {
		Patient patient = new Patient();
		patient.setId(theId);
		return patient;
	}
}
