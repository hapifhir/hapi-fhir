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
import ca.uhn.fhir.jpa.search.reindex.BlockPolicy;
import ca.uhn.fhir.jpa.util.ResourceParserUtil;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.IMetaTagSorter;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.ThreadPoolUtil;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static ca.uhn.fhir.jpa.util.ResourceParserUtil.EsrResourceDetails;
import static ca.uhn.fhir.jpa.util.ResourceParserUtil.getEsrResourceDetails;

/**
 * Batch loader for efficiently loading FHIR resources during search operations.
 * Optimizes performance by loading externally stored resources in parallel and
 * batch-extracting metadata (tags, provenance) for all resources.
 */
public class BatchResourceLoader {

	private static final Logger ourLog = LoggerFactory.getLogger(BatchResourceLoader.class);
	private static final LenientErrorHandler LENIENT_ERROR_HANDLER = new LenientErrorHandler(false).disableAllErrors();
	private static final String EXTERNALLY_STORED_RESOURCE_LOADING_THREAD_PREFIX =
			"externally-stored-resource-loading-";
	private static final int THREAD_POOL_DEFAULT_CORE_POOL_SIZE = 5;
	private static final int THREAD_POOL_DEFAULT_MAX_POOL_SIZE = 50;

	private final FhirContext myFhirContext;
	private final IResourceMetadataExtractorSvc myResourceMetadataExtractorSvc;
	private final IJpaStorageResourceParser myJpaStorageResourceParser;
	private final ExternallyStoredResourceServiceRegistry myExternallyStoredResourceServiceRegistry;
	private final IMetaTagSorter myMetaTagSorter;
	private final PartitionSettings myPartitionSettings;
	private final IPartitionLookupSvc myPartitionLookupSvc;
	private final ThreadPoolTaskExecutor myThreadPoolTaskExecutor;

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
		myThreadPoolTaskExecutor = createExecutor();
	}

	/**
	 * Creates a thread pool executor for fetching externally stored resources in parallel.
	 * Uses 5 core threads, max 50 threads, no queue.
	 * When all threads are busy, the calling thread blocks until a worker becomes available.
	 * Max 50 threads was chosen based on performance testing with external storage.
	 * Higher thread counts lead to degradation due to network overhead and storage throttling.
	 */
	private ThreadPoolTaskExecutor createExecutor() {
		return ThreadPoolUtil.newThreadPool(
				THREAD_POOL_DEFAULT_CORE_POOL_SIZE,
				THREAD_POOL_DEFAULT_MAX_POOL_SIZE,
				EXTERNALLY_STORED_RESOURCE_LOADING_THREAD_PREFIX,
				0,
				new BlockPolicy());
	}

	@PreDestroy
	public void destroy() {
		myThreadPoolTaskExecutor.shutdown();
	}

	public record ResourceLoadResult(JpaPid id, IBaseResource resource, boolean isDeleted) {}

	private record EntityResourceHolder(ResourceHistoryTable entity, IBaseResource resource) {}

	private record EsrEntityResourceHolder(ResourceHistoryTable entity, Future<IBaseResource> future) {}

	/**
	 * Loads and parses FHIR resources from resource history entities with performance optimizations.
	 * Externally stored resources are loaded in parallel, database-stored resources are parsed sequentially,
	 * and metadata (tags, provenance) is extracted in batch operations. All resources are populated
	 * with metadata, partition information, and tags.
	 *
	 * @param theResourceHistoryEntities List of resource history entities to load
	 * @param theForHistoryOperation Whether this is for a history operation (affects metadata population)
	 * @return List of loaded resources with their database IDs
	 */
	public List<ResourceLoadResult> loadResources(
			List<ResourceHistoryTable> theResourceHistoryEntities, boolean theForHistoryOperation) {
		// 1. Iterate over history entities and split them into ESR and non-ESR lists
		List<ResourceLoadResult> result = new ArrayList<>(theResourceHistoryEntities.size());
		List<EntityResourceHolder> esrEntities = new ArrayList<>();
		List<EntityResourceHolder> preloadedEntities = new ArrayList<>();
		theResourceHistoryEntities.forEach(
				historyEntity -> preProcessEntities(historyEntity, esrEntities, preloadedEntities, result));

		// 2. Submit ESR entities for parallel processing
		List<EsrEntityResourceHolder> esrFutures = processEsrEntities(esrEntities);

		// 3. Batch extract tags for all entities
		Map<JpaPid, Collection<BaseTag>> tagsMap =
				myResourceMetadataExtractorSvc.getTagsBatch(theResourceHistoryEntities);

		// 4. Parse non-ESR entities sequentially
		List<EntityResourceHolder> preloadedResources = processPreloadedEntities(preloadedEntities, tagsMap);

		// 5. Non-ESR resources post-processing
		preloadedResources.forEach(holder -> {
			Collection<BaseTag> tags = tagsMap.get(holder.entity().getPersistentId());
			postProcessResource(holder.resource(), holder.entity(), tags, result, theForHistoryOperation);
		});

		// 6. ESR resources post-processing
		esrFutures.forEach(holder -> {
			ResourceHistoryTable historyEntity = holder.entity();
			Future<IBaseResource> futureResource = holder.future();
			Collection<BaseTag> tags = tagsMap.get(historyEntity.getPersistentId());
			postProcessEsrFutures(historyEntity, futureResource, tags, result, theForHistoryOperation);
		});
		return result;
	}

	private void postProcessEsrFutures(
			ResourceHistoryTable theHistoryEntity,
			Future<IBaseResource> theFutureResource,
			Collection<BaseTag> theTags,
			List<ResourceLoadResult> theResult,
			boolean theForHistoryOperation) {
		try {
			IBaseResource resource = theFutureResource.get();
			postProcessResource(resource, theHistoryEntity, theTags, theResult, theForHistoryOperation);
		} catch (InterruptedException theException) {
			Thread.currentThread().interrupt();
			String message = "Thread interrupted while loading externally stored resource:";
			String formattedMessage = getDetailedErrorMessage(theHistoryEntity, message, theException);
			throw new InternalErrorException(Msg.code(2835) + formattedMessage, theException);
		} catch (ExecutionException theException) {
			String message = "Failed to load externally stored resource:";
			String formattedMessage = getDetailedErrorMessage(theHistoryEntity, message, theException);
			throw new InternalErrorException(Msg.code(2836) + formattedMessage, theException);
		}
	}

	private void preProcessEntities(
			ResourceHistoryTable theHistoryEntity,
			List<EntityResourceHolder> theEsrEntities,
			List<EntityResourceHolder> thePreloadedEntities,
			List<ResourceLoadResult> theResult) {
		// Skip parsing for deleted resources
		if (theHistoryEntity.getDeleted() != null || theHistoryEntity.getEncoding() == ResourceEncodingEnum.DEL) {
			theResult.add(new ResourceLoadResult(theHistoryEntity.getPersistentId(), null, true));
			return;
		}
		// Add to externally stored resources list for parallel processing
		if (theHistoryEntity.getEncoding() == ResourceEncodingEnum.ESR) {
			theEsrEntities.add(new EntityResourceHolder(theHistoryEntity, null));
		} else {
			// Add to non-ESR list for sequential processing
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
		if (theTags == null) {
			theTags = myResourceMetadataExtractorSvc.getTags(theHistoryEntity);
		}
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
				String formattedMessage = getDetailedErrorMessage(historyEntity, message, theException);
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

	private List<EsrEntityResourceHolder> processEsrEntities(List<EntityResourceHolder> theEsrEntities) {
		List<EsrEntityResourceHolder> retVal = new ArrayList<>(theEsrEntities.size());
		theEsrEntities.forEach(holder -> {
			Future<IBaseResource> future = myThreadPoolTaskExecutor.submit(() -> {
				// 1. Get ESR resource details (providerId, address)
				ResourceHistoryTable historyEntity = holder.entity();
				byte[] resourceBytes = historyEntity.getResource();
				String resourceText = historyEntity.getResourceTextVc();
				ResourceEncodingEnum resourceEncoding = historyEntity.getEncoding();
				String decodedText = ResourceParserUtil.getResourceText(resourceBytes, resourceText, resourceEncoding);
				EsrResourceDetails resourceDetails = getEsrResourceDetails(decodedText);

				// 2. Get parsed ESR resource from provider
				IExternallyStoredResourceService provider =
						myExternallyStoredResourceServiceRegistry.getProvider(resourceDetails.providerId());
				return provider.fetchResource(resourceDetails.address());
			});
			retVal.add(new EsrEntityResourceHolder(holder.entity(), future));
		});

		return retVal;
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

	private String getDetailedErrorMessage(ResourceHistoryTable theEntity, String theMessage, Exception theException) {
		return String.format(
				"%s %s/%s/_history/%s, pid: %s, encoding: %s, reason: %s",
				theMessage,
				theEntity.getResourceType(),
				theEntity.getIdDt().getIdPart(),
				theEntity.getVersion(),
				theEntity.getId(),
				theEntity.getEncoding(),
				theException.getMessage());
	}
}
