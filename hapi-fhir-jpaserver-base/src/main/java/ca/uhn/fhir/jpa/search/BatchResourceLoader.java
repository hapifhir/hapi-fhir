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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.IResourceMetadataExtractorSvc;
import ca.uhn.fhir.jpa.dao.IResourceMetadataExtractorSvc.ProvenanceDetails;
import ca.uhn.fhir.jpa.dao.TolerantJsonParser;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.esr.ExternallyStoredResourceServiceRegistry;
import ca.uhn.fhir.jpa.esr.IExternallyStoredResourceService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.util.ResourceParserUtil;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.IMetaTagSorter;
import ca.uhn.fhir.util.MetaUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.util.ResourceParserUtil.EsrResourceDetails;
import static ca.uhn.fhir.jpa.util.ResourceParserUtil.getEsrResourceDetails;

/**
 * Batch loader for efficiently loading FHIR resources during search operations.
 * Optimizes performance by batch-loading externally stored resources and their metadata
 * (tags, provenance) instead of loading resources individually.
 */
public class BatchResourceLoader {

	private static final LenientErrorHandler LENIENT_ERROR_HANDLER = new LenientErrorHandler(false).disableAllErrors();

	private final FhirContext myFhirContext;
	private final IResourceMetadataExtractorSvc myResourceMetadataExtractorSvc;
	private final IJpaStorageResourceParser myJpaStorageResourceParser;
	private final ExternallyStoredResourceServiceRegistry myExternallyStoredResourceServiceRegistry;
	private final IMetaTagSorter myMetaTagSorter;
	private final PartitionSettings myPartitionSettings;
	private final IPartitionLookupSvc myPartitionLookupSvc;

	public BatchResourceLoader(
			FhirContext theFhirContext,
			IResourceMetadataExtractorSvc theResourceMetadataExtractorSvc,
			IJpaStorageResourceParser theJpaStorageResourceParser,
			ExternallyStoredResourceServiceRegistry theExternallyStoredResourceServiceRegistry,
			IMetaTagSorter theMetaTagSorter,
			PartitionSettings thePartitionSettings,
			IPartitionLookupSvc thePartitionLookupSvc) {
		myFhirContext = theFhirContext;
		myResourceMetadataExtractorSvc = theResourceMetadataExtractorSvc;
		myJpaStorageResourceParser = theJpaStorageResourceParser;
		myExternallyStoredResourceServiceRegistry = theExternallyStoredResourceServiceRegistry;
		myMetaTagSorter = theMetaTagSorter;
		myPartitionSettings = thePartitionSettings;
		myPartitionLookupSvc = thePartitionLookupSvc;
	}

	public record ResourceLoadResult(JpaPid id, IBaseResource resource, boolean isDeleted) {}

	private record EntityResourceHolder(ResourceHistoryTable entity, IBaseResource resource) {}

	private record EsrEntityResourceHolder(ResourceHistoryTable entity, String address) {}

	/**
	 * Loads and parses FHIR resources from resource history entities with performance optimizations.
	 * Externally stored resources are batch-loaded per provider, metadata (tags, provenance) is
	 * batch-extracted for all resources, and database-stored resources are parsed sequentially.
	 * All resources are populated with metadata, partition information, and tags.
	 *
	 * @param theResourceHistoryEntities List of resource history entities to load
	 * @param theForHistoryOperation Whether this is for a history operation (affects metadata population)
	 * @return List of loaded resources with their database IDs
	 */
	public List<ResourceLoadResult> loadResources(
			List<ResourceHistoryTable> theResourceHistoryEntities, boolean theForHistoryOperation) {
		// 1. Iterate over history entities and split them into ESR and non-ESR lists
		List<ResourceLoadResult> result = new ArrayList<>(theResourceHistoryEntities.size());
		Map<String, List<EsrEntityResourceHolder>> esrEntities = new HashMap<>();
		List<EntityResourceHolder> preloadedEntities = new ArrayList<>();
		theResourceHistoryEntities.forEach(
				historyEntity -> preProcessEntities(historyEntity, esrEntities, preloadedEntities, result));

		// 2. Process ESR entities in batch per provider
		List<EntityResourceHolder> esrFutures = processEsrEntities(esrEntities);

		// 3. Batch extract tags for all entities
		Map<JpaPid, Collection<BaseTag>> tagsMap =
				myResourceMetadataExtractorSvc.getTagsBatch(theResourceHistoryEntities);

		// 4. Parse non-ESR entities sequentially
		List<EntityResourceHolder> preloadedResources = processPreloadedEntities(preloadedEntities, tagsMap);

		// 5. ESR and Non-ESR resources post-processing
		Stream.concat(preloadedResources.stream(), esrFutures.stream()).forEach(holder -> {
			Collection<BaseTag> tags = tagsMap.get(holder.entity().getPersistentId());
			postProcessResource(holder.resource(), holder.entity(), tags, result, theForHistoryOperation);
		});
		return result;
	}

	private void preProcessEntities(
			ResourceHistoryTable theHistoryEntity,
			Map<String, List<EsrEntityResourceHolder>> theEsrEntities,
			List<EntityResourceHolder> thePreloadedEntities,
			List<ResourceLoadResult> theResult) {
		// Skip parsing for deleted resources
		if (theHistoryEntity.getDeleted() != null || theHistoryEntity.getEncoding() == ResourceEncodingEnum.DEL) {
			theResult.add(new ResourceLoadResult(theHistoryEntity.getPersistentId(), null, true));
			return;
		}
		// Add to externally stored resources list for batch processing
		if (theHistoryEntity.getEncoding() == ResourceEncodingEnum.ESR) {
			EsrResourceDetails resourceDetails = getResourceDetails(theHistoryEntity);
			List<EsrEntityResourceHolder> list =
					theEsrEntities.computeIfAbsent(resourceDetails.providerId(), k -> new ArrayList<>());
			list.add(new EsrEntityResourceHolder(theHistoryEntity, resourceDetails.address()));
		} else {
			// Add to a non-ESR list for sequential processing
			thePreloadedEntities.add(new EntityResourceHolder(theHistoryEntity, null));
		}
	}

	private void postProcessResource(
			IBaseResource theResource,
			ResourceHistoryTable theHistoryEntity,
			Collection<? extends BaseTag> theTags,
			List<ResourceLoadResult> theResult,
			boolean theForHistoryOperation) {
		ProvenanceDetails provenanceDetails = myResourceMetadataExtractorSvc.getProvenanceDetails(theHistoryEntity);
		String provenanceSourceUri = provenanceDetails.provenanceSourceUri();
		String provenanceRequestId = provenanceDetails.provenanceRequestId();

		// 1. Fill MetaData
		myJpaStorageResourceParser.populateResourceMetadata(
				theHistoryEntity, theForHistoryOperation, theTags, theHistoryEntity.getVersion(), theResource);

		// 2. Handle source (provenance)
		MetaUtil.populateResourceSource(myFhirContext, provenanceSourceUri, provenanceRequestId, theResource);

		// 3. Add partition information
		populateResourcePartitionInformation(theHistoryEntity, theResource);

		// 4. Sort tags, security labels and profiles
		myMetaTagSorter.sort(theResource.getMeta());

		// 5. Add resource to load result
		theResult.add(new ResourceLoadResult(theHistoryEntity.getPersistentId(), theResource, false));
	}

	private List<EntityResourceHolder> processPreloadedEntities(
			List<EntityResourceHolder> thePreloadedEntities, Map<JpaPid, Collection<BaseTag>> theTagsMap) {
		List<EntityResourceHolder> result = new ArrayList<>(thePreloadedEntities.size());
		thePreloadedEntities.forEach(holder -> {
			ResourceHistoryTable historyEntity = holder.entity;
			byte[] resourceBytes = historyEntity.getResource();
			String resourceText = historyEntity.getResourceTextVc();
			ResourceEncodingEnum resourceEncoding = historyEntity.getEncoding();
			String decodedText = ResourceParserUtil.getResourceText(resourceBytes, resourceText, resourceEncoding);

			// 1. Use the appropriate custom type if one is specified in the context
			Class<? extends IBaseResource> defaultResourceType = myFhirContext
					.getResourceDefinition(historyEntity.getResourceType())
					.getImplementingClass();
			Class<? extends IBaseResource> resourceType = ResourceParserUtil.determineTypeToParse(
					myFhirContext, defaultResourceType, theTagsMap.get(historyEntity.getPersistentId()));

			IParser parser = new TolerantJsonParser(
					getContext(historyEntity.getFhirVersion()), LENIENT_ERROR_HANDLER, historyEntity.getPersistentId());

			try {
				// 2. Parse the resource from JSON text
				IBaseResource resource = parser.parseResource(resourceType, decodedText);
				result.add(new EntityResourceHolder(historyEntity, resource));
			} catch (Exception theException) {
				String message = "Failed to parse database resource:";
				String formattedMessage = getDetailedErrorMessage(historyEntity, message);
				throw new DataFormatException(Msg.code(2631) + formattedMessage, theException);
			}
		});
		return result;
	}

	private FhirContext getContext(FhirVersionEnum theVersion) {
		Validate.notNull(theVersion, "theVersion must not be null");
		if (theVersion == myFhirContext.getVersion().getVersion()) {
			return myFhirContext;
		}
		return FhirContext.forCached(theVersion);
	}

	private List<EntityResourceHolder> processEsrEntities(Map<String, List<EsrEntityResourceHolder>> theEsrEntities) {
		List<EntityResourceHolder> result = new ArrayList<>();
		for (Map.Entry<String, List<EsrEntityResourceHolder>> entry : theEsrEntities.entrySet()) {
			String providerId = entry.getKey();
			List<EsrEntityResourceHolder> holders = entry.getValue();

			IExternallyStoredResourceService provider =
					myExternallyStoredResourceServiceRegistry.getProvider(providerId);

			Collection<String> addresses =
					holders.stream().map(EsrEntityResourceHolder::address).collect(Collectors.toList());

			Map<String, IBaseResource> resourceMap;
			try {
				resourceMap = provider.fetchResources(addresses);
			} catch (Exception theException) {
				String message = String.format(
						"Failed to load %d externally stored resources from %s provider.",
						addresses.size(), providerId);
				throw new InternalErrorException(Msg.code(2835) + message, theException);
			}

			for (EsrEntityResourceHolder holder : holders) {
				String address = holder.address();
				IBaseResource resource = resourceMap.get(address);
				if (resource != null) {
					result.add(new EntityResourceHolder(holder.entity(), resource));
				} else {
					String message =
							String.format("Failed to load externally stored resource from %s provider:", providerId);
					String formattedMessage = getDetailedErrorMessage(holder.entity(), message);
					throw new InternalErrorException(Msg.code(2836) + formattedMessage);
				}
			}
		}

		return result;
	}

	private EsrResourceDetails getResourceDetails(ResourceHistoryTable theResourceHistoryEntity) {
		byte[] resourceBytes = theResourceHistoryEntity.getResource();
		String resourceText = theResourceHistoryEntity.getResourceTextVc();
		ResourceEncodingEnum resourceEncoding = theResourceHistoryEntity.getEncoding();
		String decodedText = ResourceParserUtil.getResourceText(resourceBytes, resourceText, resourceEncoding);
		return getEsrResourceDetails(decodedText);
	}

	private void populateResourcePartitionInformation(
			ResourceHistoryTable theHistoryEntity, IBaseResource theResource) {
		if (myPartitionSettings.isPartitioningEnabled()) {
			RequestPartitionId requestPartitionId = getRequestPartitionId(theHistoryEntity);
			theResource.setUserData(Constants.RESOURCE_PARTITION_ID, requestPartitionId);
		}
	}

	private RequestPartitionId getRequestPartitionId(ResourceHistoryTable theHistoryEntity) {
		PartitionablePartitionId partitionId = theHistoryEntity.getPartitionId();
		if (partitionId != null && partitionId.getPartitionId() != null) {
			PartitionEntity persistedPartition = myPartitionLookupSvc.getPartitionById(partitionId.getPartitionId());
			return persistedPartition.toRequestPartitionId();
		} else {
			return myPartitionSettings.getDefaultRequestPartitionId();
		}
	}

	private String getDetailedErrorMessage(ResourceHistoryTable theEntity, String theMessage) {
		return String.format(
				"%s %s/%s/_history/%s, pid: %s, encoding: %s",
				theMessage,
				theEntity.getResourceType(),
				theEntity.getIdDt().getIdPart(),
				theEntity.getVersion(),
				theEntity.getId(),
				theEntity.getEncoding());
	}
}
