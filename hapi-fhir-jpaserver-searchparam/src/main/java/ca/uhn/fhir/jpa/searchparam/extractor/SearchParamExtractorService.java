/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BasePartitionable;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.IResourceIndexComboSearchParameter;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import ca.uhn.fhir.util.FhirTerser;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchParamExtractorService {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamExtractorService.class);

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private StorageSettings myStorageSettings;

	@Autowired
	private FhirContext myContext;

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired(required = false)
	private IResourceLinkResolver myResourceLinkResolver;

	private SearchParamExtractionUtil mySearchParamExtractionUtil;

	@VisibleForTesting
	public void setSearchParamExtractor(ISearchParamExtractor theSearchParamExtractor) {
		mySearchParamExtractor = theSearchParamExtractor;
	}

	public void extractFromResource(
			RequestPartitionId theRequestPartitionId,
			RequestDetails theRequestDetails,
			ResourceIndexedSearchParams theParams,
			ResourceTable theEntity,
			IBaseResource theResource,
			TransactionDetails theTransactionDetails,
			boolean theFailOnInvalidReference) {
		extractFromResource(
				theRequestPartitionId,
				theRequestDetails,
				theParams,
				ResourceIndexedSearchParams.withSets(),
				theEntity,
				theResource,
				theTransactionDetails,
				theFailOnInvalidReference,
				ISearchParamExtractor.ALL_PARAMS);
	}

	/**
	 * This method is responsible for scanning a resource for all of the search parameter instances.
	 * I.e. for all search parameters defined for
	 * a given resource type, it extracts the associated indexes and populates
	 * {@literal theParams}.
	 */
	public void extractFromResource(
			RequestPartitionId theRequestPartitionId,
			RequestDetails theRequestDetails,
			ResourceIndexedSearchParams theNewParams,
			ResourceIndexedSearchParams theExistingParams,
			ResourceTable theEntity,
			IBaseResource theResource,
			TransactionDetails theTransactionDetails,
			boolean theFailOnInvalidReference,
			@Nonnull ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		// All search parameter types except Reference
		ResourceIndexedSearchParams normalParams = ResourceIndexedSearchParams.withSets();
		getExtractionUtil()
				.extractSearchIndexParameters(theRequestDetails, normalParams, theResource, theSearchParamFilter);
		mergeParams(normalParams, theNewParams);

		boolean indexOnContainedResources = myStorageSettings.isIndexOnContainedResources();
		ISearchParamExtractor.SearchParamSet<PathAndRef> indexedReferences =
				mySearchParamExtractor.extractResourceLinks(theResource, indexOnContainedResources);
		SearchParamExtractorService.handleWarnings(theRequestDetails, myInterceptorBroadcaster, indexedReferences);

		if (indexOnContainedResources) {
			ResourceIndexedSearchParams containedParams = ResourceIndexedSearchParams.withSets();
			extractSearchIndexParametersForContainedResources(
					theRequestDetails, containedParams, theResource, theEntity, indexedReferences);
			mergeParams(containedParams, theNewParams);
		}

		if (myStorageSettings.isIndexOnUpliftedRefchains()) {
			ResourceIndexedSearchParams containedParams = ResourceIndexedSearchParams.withSets();
			extractSearchIndexParametersForUpliftedRefchains(
					theRequestDetails,
					containedParams,
					theEntity,
					theRequestPartitionId,
					theTransactionDetails,
					indexedReferences);
			mergeParams(containedParams, theNewParams);
		}

		// Do this after, because we add to strings during both string and token processing, and contained resource if
		// any
		populateResourceTables(theNewParams, theEntity);

		// Reference search parameters
		extractResourceLinks(
				theRequestPartitionId,
				theExistingParams,
				theNewParams,
				theEntity,
				theResource,
				theTransactionDetails,
				theFailOnInvalidReference,
				theRequestDetails,
				indexedReferences);

		if (indexOnContainedResources) {
			extractResourceLinksForContainedResources(
					theRequestPartitionId,
					theNewParams,
					theEntity,
					theResource,
					theTransactionDetails,
					theFailOnInvalidReference,
					theRequestDetails);
		}

		// Missing (:missing) Indexes - These are indexes to satisfy the :missing
		// modifier
		if (myStorageSettings.getIndexMissingFields() == StorageSettings.IndexEnabledEnum.ENABLED) {

			// References
			Map<String, Boolean> presenceMap = getReferenceSearchParamPresenceMap(theEntity, theNewParams);
			presenceMap.forEach((key, value) -> {
				SearchParamPresentEntity present = new SearchParamPresentEntity();
				present.setPartitionSettings(myPartitionSettings);
				present.setResource(theEntity);
				present.setParamName(key);
				present.setPresent(value);
				present.setPartitionId(theEntity.getPartitionId());
				present.calculateHashes();
				theNewParams.mySearchParamPresentEntities.add(present);
			});

			// Everything else
			ResourceSearchParams activeSearchParams =
					mySearchParamRegistry.getActiveSearchParams(theEntity.getResourceType());
			theNewParams.findMissingSearchParams(myPartitionSettings, myStorageSettings, theEntity, activeSearchParams);
		}

		extractSearchParamComboUnique(theEntity, theNewParams);

		extractSearchParamComboNonUnique(theEntity, theNewParams);

		theNewParams.setUpdatedTime(theTransactionDetails.getTransactionDate());
	}

	private SearchParamExtractionUtil getExtractionUtil() {
		if (mySearchParamExtractionUtil == null) {
			mySearchParamExtractionUtil = new SearchParamExtractionUtil(
					myContext, myStorageSettings, mySearchParamExtractor, myInterceptorBroadcaster);
		}
		return mySearchParamExtractionUtil;
	}

	@Nonnull
	private Map<String, Boolean> getReferenceSearchParamPresenceMap(
			ResourceTable entity, ResourceIndexedSearchParams newParams) {
		Map<String, Boolean> retval = new HashMap<>();

		for (String nextKey : newParams.getPopulatedResourceLinkParameters()) {
			retval.put(nextKey, Boolean.TRUE);
		}

		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams(entity.getResourceType());
		activeSearchParams.getReferenceSearchParamNames().forEach(key -> retval.putIfAbsent(key, Boolean.FALSE));
		return retval;
	}

	@VisibleForTesting
	public void setStorageSettings(StorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	/**
	 * Extract search parameter indexes for contained resources. E.g. if we
	 * are storing a Patient with a contained Organization, we might extract
	 * a String index on the Patient with paramName="organization.name" and
	 * value="Org Name"
	 */
	private void extractSearchIndexParametersForContainedResources(
			RequestDetails theRequestDetails,
			ResourceIndexedSearchParams theParams,
			IBaseResource theResource,
			ResourceTable theEntity,
			ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences) {

		FhirTerser terser = myContext.newTerser();

		// 1. get all contained resources
		Collection<IBaseResource> containedResources = terser.getAllEmbeddedResources(theResource, false);

		// Extract search parameters
		IChainedSearchParameterExtractionStrategy strategy = new IChainedSearchParameterExtractionStrategy() {
			@Nonnull
			@Override
			public ISearchParamExtractor.ISearchParamFilter getSearchParamFilter(@Nonnull PathAndRef thePathAndRef) {
				// Currently for contained resources we always index all search parameters
				// on all contained resources. A potential nice future optimization would
				// be to make this configurable, perhaps with an optional extension you could
				// add to a SearchParameter?
				return ISearchParamExtractor.ALL_PARAMS;
			}

			@Override
			public IBaseResource fetchResourceAtPath(@Nonnull PathAndRef thePathAndRef) {
				if (thePathAndRef.getRef() == null) {
					return null;
				}
				return findContainedResource(containedResources, thePathAndRef.getRef());
			}
		};
		boolean recurse = myStorageSettings.isIndexOnContainedResourcesRecursively();
		extractSearchIndexParametersForTargetResources(
				theRequestDetails,
				theParams,
				theEntity,
				new HashSet<>(),
				strategy,
				theIndexedReferences,
				recurse,
				true);
	}

	/**
	 * Extract search parameter indexes for uplifted refchains. E.g. if we
	 * are storing a Patient with reference to an Organization and the
	 * "Patient:organization" SearchParameter declares an uplifted refchain
	 * on the "name" SearchParameter, we might extract a String index
	 * on the Patient with paramName="organization.name" and value="Org Name"
	 */
	private void extractSearchIndexParametersForUpliftedRefchains(
			RequestDetails theRequestDetails,
			ResourceIndexedSearchParams theParams,
			ResourceTable theEntity,
			RequestPartitionId theRequestPartitionId,
			TransactionDetails theTransactionDetails,
			ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences) {
		IChainedSearchParameterExtractionStrategy strategy = new IChainedSearchParameterExtractionStrategy() {

			@Nonnull
			@Override
			public ISearchParamExtractor.ISearchParamFilter getSearchParamFilter(@Nonnull PathAndRef thePathAndRef) {
				String searchParamName = thePathAndRef.getSearchParamName();
				RuntimeSearchParam searchParam =
						mySearchParamRegistry.getActiveSearchParam(theEntity.getResourceType(), searchParamName);
				Set<String> upliftRefchainCodes = searchParam.getUpliftRefchainCodes();
				if (upliftRefchainCodes.isEmpty()) {
					return ISearchParamExtractor.NO_PARAMS;
				}
				return sp -> sp.stream()
						.filter(t -> upliftRefchainCodes.contains(t.getName()))
						.collect(Collectors.toList());
			}

			@Override
			public IBaseResource fetchResourceAtPath(@Nonnull PathAndRef thePathAndRef) {
				// The PathAndRef will contain a resource if the SP path was inside a Bundle
				// and pointed to a resource (e.g. Bundle.entry.resource) as opposed to
				// pointing to a reference (e.g. Observation.subject)
				if (thePathAndRef.getResource() != null) {
					return thePathAndRef.getResource();
				}

				// Ok, it's a normal reference
				IIdType reference = thePathAndRef.getRef().getReferenceElement();

				// If we're processing a FHIR transaction, we store the resources
				// mapped by their resolved resource IDs in theTransactionDetails
				IBaseResource resolvedResource = theTransactionDetails.getResolvedResource(reference);

				// And the usual case is that the reference points to a resource
				// elsewhere in the repository, so we load it
				if (resolvedResource == null
						&& myResourceLinkResolver != null
						&& !reference.getValue().startsWith("urn:uuid:")) {
					RequestPartitionId targetRequestPartitionId = determineResolverPartitionId(theRequestPartitionId);
					resolvedResource = myResourceLinkResolver.loadTargetResource(
							targetRequestPartitionId,
							theEntity.getResourceType(),
							thePathAndRef,
							theRequestDetails,
							theTransactionDetails);
					if (resolvedResource != null) {
						ourLog.trace("Found target: {}", resolvedResource.getIdElement());
						theTransactionDetails.addResolvedResource(
								thePathAndRef.getRef().getReferenceElement(), resolvedResource);
					}
				}

				return resolvedResource;
			}
		};
		extractSearchIndexParametersForTargetResources(
				theRequestDetails, theParams, theEntity, new HashSet<>(), strategy, theIndexedReferences, false, false);
	}

	/**
	 * Extract indexes for contained references as well as for uplifted refchains.
	 * These two types of indexes are both similar special cases. Normally we handle
	 * chained searches ("Patient?organization.name=Foo") using a join from the
	 * {@link ResourceLink} table (for the "organization" part) to the
	 * {@link ResourceIndexedSearchParamString} table (for the "name" part). But
	 * for both contained resource indexes and uplifted refchains we use only the
	 * {@link ResourceIndexedSearchParamString} table to handle the entire
	 * "organization.name" part, or the other similar tables for token, number, etc.
	 *
	 * @see #extractSearchIndexParametersForContainedResources(RequestDetails, ResourceIndexedSearchParams, IBaseResource, ResourceTable, ISearchParamExtractor.SearchParamSet)
	 * @see #extractSearchIndexParametersForUpliftedRefchains(RequestDetails, ResourceIndexedSearchParams, ResourceTable, RequestPartitionId, TransactionDetails, ISearchParamExtractor.SearchParamSet)
	 */
	private void extractSearchIndexParametersForTargetResources(
			RequestDetails theRequestDetails,
			ResourceIndexedSearchParams theParams,
			ResourceTable theEntity,
			Collection<IBaseResource> theAlreadySeenResources,
			IChainedSearchParameterExtractionStrategy theTargetIndexingStrategy,
			ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences,
			boolean theRecurse,
			boolean theIndexOnContainedResources) {
		// 2. Find referenced search parameters

		String spnamePrefix;
		// 3. for each referenced search parameter, create an index
		for (PathAndRef nextPathAndRef : theIndexedReferences) {

			// 3.1 get the search parameter name as spname prefix
			spnamePrefix = nextPathAndRef.getSearchParamName();

			if (spnamePrefix == null || (nextPathAndRef.getRef() == null && nextPathAndRef.getResource() == null))
				continue;

			// 3.1.2 check if this ref actually applies here
			ISearchParamExtractor.ISearchParamFilter searchParamsToIndex =
					theTargetIndexingStrategy.getSearchParamFilter(nextPathAndRef);
			if (searchParamsToIndex == ISearchParamExtractor.NO_PARAMS) {
				continue;
			}

			// 3.2 find the target resource
			IBaseResource targetResource = theTargetIndexingStrategy.fetchResourceAtPath(nextPathAndRef);
			if (targetResource == null) continue;

			// 3.2.1 if we've already processed this resource upstream, do not process it again, to prevent infinite
			// loops
			if (theAlreadySeenResources.contains(targetResource)) {
				continue;
			}

			ResourceIndexedSearchParams currParams = ResourceIndexedSearchParams.withSets();

			// 3.3 create indexes for the current contained resource
			getExtractionUtil()
					.extractSearchIndexParameters(theRequestDetails, currParams, targetResource, searchParamsToIndex);

			// 3.4 recurse to process any other contained resources referenced by this one
			// Recursing is currently only allowed for contained resources and not
			// uplifted refchains because the latter could potentially kill performance
			// with the number of resource resolutions needed in order to handle
			// a single write. Maybe in the future we could add caching to improve
			// this
			if (theRecurse) {
				HashSet<IBaseResource> nextAlreadySeenResources = new HashSet<>(theAlreadySeenResources);
				nextAlreadySeenResources.add(targetResource);

				ISearchParamExtractor.SearchParamSet<PathAndRef> indexedReferences =
						mySearchParamExtractor.extractResourceLinks(targetResource, theIndexOnContainedResources);
				SearchParamExtractorService.handleWarnings(
						theRequestDetails, myInterceptorBroadcaster, indexedReferences);

				extractSearchIndexParametersForTargetResources(
						theRequestDetails,
						currParams,
						theEntity,
						nextAlreadySeenResources,
						theTargetIndexingStrategy,
						indexedReferences,
						true,
						theIndexOnContainedResources);
			}

			// 3.5 added reference name as a prefix for the contained resource if any
			// e.g. for Observation.subject contained reference
			// the SP_NAME = subject.family
			currParams.updateSpnamePrefixForIndexOnUpliftedChain(
					theEntity.getResourceType(), nextPathAndRef.getSearchParamName());

			// 3.6 merge to the mainParams
			// NOTE: the spname prefix is different
			mergeParams(currParams, theParams);
		}
	}

	private IBaseResource findContainedResource(Collection<IBaseResource> resources, IBaseReference reference) {
		for (IBaseResource resource : resources) {
			if (resource.getIdElement().equals(reference.getReferenceElement())) return resource;
		}
		return null;
	}

	private void mergeParams(ResourceIndexedSearchParams theSrcParams, ResourceIndexedSearchParams theTargetParams) {

		theTargetParams.myNumberParams.addAll(theSrcParams.myNumberParams);
		theTargetParams.myQuantityParams.addAll(theSrcParams.myQuantityParams);
		theTargetParams.myQuantityNormalizedParams.addAll(theSrcParams.myQuantityNormalizedParams);
		theTargetParams.myDateParams.addAll(theSrcParams.myDateParams);
		theTargetParams.myUriParams.addAll(theSrcParams.myUriParams);
		theTargetParams.myTokenParams.addAll(theSrcParams.myTokenParams);
		theTargetParams.myStringParams.addAll(theSrcParams.myStringParams);
		theTargetParams.myCoordsParams.addAll(theSrcParams.myCoordsParams);
		theTargetParams.myCompositeParams.addAll(theSrcParams.myCompositeParams);
	}

	private void populateResourceTables(ResourceIndexedSearchParams theParams, ResourceTable theEntity) {

		populateResourceTable(theParams.myNumberParams, theEntity);
		populateResourceTable(theParams.myQuantityParams, theEntity);
		populateResourceTable(theParams.myQuantityNormalizedParams, theEntity);
		populateResourceTable(theParams.myDateParams, theEntity);
		populateResourceTable(theParams.myUriParams, theEntity);
		populateResourceTable(theParams.myTokenParams, theEntity);
		populateResourceTable(theParams.myStringParams, theEntity);
		populateResourceTable(theParams.myCoordsParams, theEntity);
	}

	@VisibleForTesting
	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	private void extractResourceLinks(
			RequestPartitionId theRequestPartitionId,
			ResourceIndexedSearchParams theParams,
			ResourceTable theEntity,
			IBaseResource theResource,
			TransactionDetails theTransactionDetails,
			boolean theFailOnInvalidReference,
			RequestDetails theRequest,
			ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences) {
		extractResourceLinks(
				theRequestPartitionId,
				ResourceIndexedSearchParams.withSets(),
				theParams,
				theEntity,
				theResource,
				theTransactionDetails,
				theFailOnInvalidReference,
				theRequest,
				theIndexedReferences);
	}

	private void extractResourceLinks(
			RequestPartitionId theRequestPartitionId,
			ResourceIndexedSearchParams theExistingParams,
			ResourceIndexedSearchParams theNewParams,
			ResourceTable theEntity,
			IBaseResource theResource,
			TransactionDetails theTransactionDetails,
			boolean theFailOnInvalidReference,
			RequestDetails theRequest,
			ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences) {
		String sourceResourceName = myContext.getResourceType(theResource);

		for (PathAndRef nextPathAndRef : theIndexedReferences) {
			if (nextPathAndRef.getRef() != null) {
				if (nextPathAndRef.getRef().getReferenceElement().isLocal()) {
					continue;
				}

				RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(
						sourceResourceName, nextPathAndRef.getSearchParamName());
				extractResourceLinks(
						theRequestPartitionId,
						theExistingParams,
						theNewParams,
						theEntity,
						theTransactionDetails,
						sourceResourceName,
						searchParam,
						nextPathAndRef,
						theFailOnInvalidReference,
						theRequest);
			}
		}

		theEntity.setHasLinks(!theNewParams.myLinks.isEmpty());
	}

	private void extractResourceLinks(
			@Nonnull RequestPartitionId theRequestPartitionId,
			ResourceIndexedSearchParams theExistingParams,
			ResourceIndexedSearchParams theNewParams,
			ResourceTable theEntity,
			TransactionDetails theTransactionDetails,
			String theSourceResourceName,
			RuntimeSearchParam theRuntimeSearchParam,
			PathAndRef thePathAndRef,
			boolean theFailOnInvalidReference,
			RequestDetails theRequest) {
		IBaseReference nextReference = thePathAndRef.getRef();
		IIdType nextId = nextReference.getReferenceElement();
		String path = thePathAndRef.getPath();
		Date transactionDate = theTransactionDetails.getTransactionDate();

		/*
		 * This can only really happen if the DAO is being called
		 * programmatically with a Bundle (not through the FHIR REST API)
		 * but Smile does this
		 */
		if (nextId.isEmpty() && nextReference.getResource() != null) {
			nextId = nextReference.getResource().getIdElement();
		}

		if (myContext.getParserOptions().isStripVersionsFromReferences()
				&& !myContext
						.getParserOptions()
						.getDontStripVersionsFromReferencesAtPaths()
						.contains(thePathAndRef.getPath())
				&& nextId.hasVersionIdPart()) {
			nextId = nextId.toVersionless();
		}

		theNewParams.myPopulatedResourceLinkParameters.add(thePathAndRef.getSearchParamName());

		boolean canonical = thePathAndRef.isCanonical();
		if (LogicalReferenceHelper.isLogicalReference(myStorageSettings, nextId) || canonical) {
			String value = nextId.getValue();
			ResourceLink resourceLink =
					ResourceLink.forLogicalReference(thePathAndRef.getPath(), theEntity, value, transactionDate);
			if (theNewParams.myLinks.add(resourceLink)) {
				ourLog.debug("Indexing remote resource reference URL: {}", nextId);
			}
			return;
		}

		String baseUrl = nextId.getBaseUrl();

		// If this is a conditional URL, the part after the question mark
		// can include URLs (e.g. token system URLs) and these really confuse
		// the IdType parser because a conditional URL isn't actually a valid
		// FHIR ID. So in order to truly determine whether we're dealing with
		// an absolute reference, we strip the query part and reparse
		// the reference.
		int questionMarkIndex = nextId.getValue().indexOf('?');
		if (questionMarkIndex != -1) {
			IdType preQueryId = new IdType(nextId.getValue().substring(0, questionMarkIndex - 1));
			baseUrl = preQueryId.getBaseUrl();
		}

		String typeString = nextId.getResourceType();
		if (isBlank(typeString)) {
			String msg = "Invalid resource reference found at path[" + path + "] - Does not contain resource type - "
					+ nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(Msg.code(505) + msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}
		RuntimeResourceDefinition resourceDefinition;
		try {
			resourceDefinition = myContext.getResourceDefinition(typeString);
		} catch (DataFormatException e) {
			String msg = "Invalid resource reference found at path[" + path
					+ "] - Resource type is unknown or not supported on this server - " + nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(Msg.code(506) + msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}

		if (theRuntimeSearchParam.hasTargets()) {
			if (!theRuntimeSearchParam.getTargets().contains(typeString)) {
				return;
			}
		}

		if (isNotBlank(baseUrl)) {
			if (!myStorageSettings.getTreatBaseUrlsAsLocal().contains(baseUrl)
					&& !myStorageSettings.isAllowExternalReferences()) {
				String msg = myContext
						.getLocalizer()
						.getMessage(BaseSearchParamExtractor.class, "externalReferenceNotAllowed", nextId.getValue());
				throw new InvalidRequestException(Msg.code(507) + msg);
			} else {
				ResourceLink resourceLink =
						ResourceLink.forAbsoluteReference(thePathAndRef.getPath(), theEntity, nextId, transactionDate);
				if (theNewParams.myLinks.add(resourceLink)) {
					ourLog.debug("Indexing remote resource reference URL: {}", nextId);
				}
				return;
			}
		}

		Class<? extends IBaseResource> type = resourceDefinition.getImplementingClass();
		String targetId = nextId.getIdPart();
		if (StringUtils.isBlank(targetId)) {
			String msg = "Invalid resource reference found at path[" + path + "] - Does not contain resource ID - "
					+ nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(Msg.code(508) + msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}

		IIdType referenceElement = thePathAndRef.getRef().getReferenceElement();
		JpaPid resolvedTargetId = (JpaPid) theTransactionDetails.getResolvedResourceId(referenceElement);
		ResourceLink resourceLink;

		Long targetVersionId = nextId.getVersionIdPartAsLong();
		if (resolvedTargetId != null) {

			/*
			 * If we have already resolved the given reference within this transaction, we don't
			 * need to resolve it again
			 */
			myResourceLinkResolver.validateTypeOrThrowException(type);
			resourceLink = ResourceLink.forLocalReference(
					thePathAndRef.getPath(),
					theEntity,
					typeString,
					resolvedTargetId.getId(),
					targetId,
					transactionDate,
					targetVersionId);

		} else if (theFailOnInvalidReference) {

			/*
			 * The reference points to another resource, so let's look it up. We need to do this
			 * since the target may be a forced ID, but also so that we can throw an exception
			 * if the reference is invalid
			 */
			myResourceLinkResolver.validateTypeOrThrowException(type);

			/*
			 * We need to obtain a resourceLink out of the provided {@literal thePathAndRef}.  In the case
			 * where we are updating a resource that already has resourceLinks (stored in {@literal theExistingParams.getResourceLinks()}),
			 * let's try to match thePathAndRef to an already existing resourceLink to avoid the
			 * very expensive operation of creating a resourceLink that would end up being exactly the same
			 * one we already have.
			 */
			Optional<ResourceLink> optionalResourceLink =
					findMatchingResourceLink(thePathAndRef, theExistingParams.getResourceLinks());
			if (optionalResourceLink.isPresent()) {
				resourceLink = optionalResourceLink.get();
			} else {
				resourceLink = resolveTargetAndCreateResourceLinkOrReturnNull(
						theRequestPartitionId,
						theSourceResourceName,
						thePathAndRef,
						theEntity,
						transactionDate,
						nextId,
						theRequest,
						theTransactionDetails);
			}

			if (resourceLink == null) {
				return;
			} else {
				// Cache the outcome in the current transaction in case there are more references
				JpaPid persistentId = JpaPid.fromId(resourceLink.getTargetResourcePid());
				theTransactionDetails.addResolvedResourceId(referenceElement, persistentId);
			}

		} else {

			/*
			 * Just assume the reference is valid. This is used for in-memory matching since there
			 * is no expectation of a database in this situation
			 */
			ResourceTable target;
			target = new ResourceTable();
			target.setResourceType(typeString);
			resourceLink = ResourceLink.forLocalReference(
					thePathAndRef.getPath(), theEntity, typeString, null, targetId, transactionDate, targetVersionId);
		}

		theNewParams.myLinks.add(resourceLink);
	}

	private Optional<ResourceLink> findMatchingResourceLink(
			PathAndRef thePathAndRef, Collection<ResourceLink> theResourceLinks) {
		IIdType referenceElement = thePathAndRef.getRef().getReferenceElement();
		List<ResourceLink> resourceLinks = new ArrayList<>(theResourceLinks);
		for (ResourceLink resourceLink : resourceLinks) {

			// comparing the searchParam path ex: Group.member.entity
			boolean hasMatchingSearchParamPath =
					StringUtils.equals(resourceLink.getSourcePath(), thePathAndRef.getPath());

			boolean hasMatchingResourceType =
					StringUtils.equals(resourceLink.getTargetResourceType(), referenceElement.getResourceType());

			boolean hasMatchingResourceId =
					StringUtils.equals(resourceLink.getTargetResourceId(), referenceElement.getIdPart());

			boolean hasMatchingResourceVersion = myContext.getParserOptions().isStripVersionsFromReferences()
					|| referenceElement.getVersionIdPartAsLong() == null
					|| referenceElement.getVersionIdPartAsLong().equals(resourceLink.getTargetResourceVersion());

			if (hasMatchingSearchParamPath
					&& hasMatchingResourceType
					&& hasMatchingResourceId
					&& hasMatchingResourceVersion) {
				return Optional.of(resourceLink);
			}
		}

		return Optional.empty();
	}

	private void extractResourceLinksForContainedResources(
			RequestPartitionId theRequestPartitionId,
			ResourceIndexedSearchParams theParams,
			ResourceTable theEntity,
			IBaseResource theResource,
			TransactionDetails theTransactionDetails,
			boolean theFailOnInvalidReference,
			RequestDetails theRequest) {

		FhirTerser terser = myContext.newTerser();

		// 1. get all contained resources
		Collection<IBaseResource> containedResources = terser.getAllEmbeddedResources(theResource, false);

		extractResourceLinksForContainedResources(
				theRequestPartitionId,
				theParams,
				theEntity,
				theResource,
				theTransactionDetails,
				theFailOnInvalidReference,
				theRequest,
				containedResources,
				new HashSet<>());
	}

	private void extractResourceLinksForContainedResources(
			RequestPartitionId theRequestPartitionId,
			ResourceIndexedSearchParams theParams,
			ResourceTable theEntity,
			IBaseResource theResource,
			TransactionDetails theTransactionDetails,
			boolean theFailOnInvalidReference,
			RequestDetails theRequest,
			Collection<IBaseResource> theContainedResources,
			Collection<IBaseResource> theAlreadySeenResources) {

		// 2. Find referenced search parameters
		ISearchParamExtractor.SearchParamSet<PathAndRef> referencedSearchParamSet =
				mySearchParamExtractor.extractResourceLinks(theResource, true);

		String spNamePrefix;
		ResourceIndexedSearchParams currParams;
		// 3. for each referenced search parameter, create an index
		for (PathAndRef nextPathAndRef : referencedSearchParamSet) {

			// 3.1 get the search parameter name as spname prefix
			spNamePrefix = nextPathAndRef.getSearchParamName();

			if (spNamePrefix == null || nextPathAndRef.getRef() == null) continue;

			// 3.2 find the contained resource
			IBaseResource containedResource = findContainedResource(theContainedResources, nextPathAndRef.getRef());
			if (containedResource == null) continue;

			// 3.2.1 if we've already processed this resource upstream, do not process it again, to prevent infinite
			// loops
			if (theAlreadySeenResources.contains(containedResource)) {
				continue;
			}

			currParams = ResourceIndexedSearchParams.withSets();

			// 3.3 create indexes for the current contained resource
			ISearchParamExtractor.SearchParamSet<PathAndRef> indexedReferences =
					mySearchParamExtractor.extractResourceLinks(containedResource, true);
			extractResourceLinks(
					theRequestPartitionId,
					currParams,
					theEntity,
					containedResource,
					theTransactionDetails,
					theFailOnInvalidReference,
					theRequest,
					indexedReferences);

			// 3.4 recurse to process any other contained resources referenced by this one
			if (myStorageSettings.isIndexOnContainedResourcesRecursively()) {
				HashSet<IBaseResource> nextAlreadySeenResources = new HashSet<>(theAlreadySeenResources);
				nextAlreadySeenResources.add(containedResource);
				extractResourceLinksForContainedResources(
						theRequestPartitionId,
						currParams,
						theEntity,
						containedResource,
						theTransactionDetails,
						theFailOnInvalidReference,
						theRequest,
						theContainedResources,
						nextAlreadySeenResources);
			}

			// 3.4 added reference name as a prefix for the contained resource if any
			// e.g. for Observation.subject contained reference
			// the SP_NAME = subject.family
			currParams.updateSpnamePrefixForLinksOnContainedResource(nextPathAndRef.getPath());

			// 3.5 merge to the mainParams
			// NOTE: the spname prefix is different
			theParams.getResourceLinks().addAll(currParams.getResourceLinks());
		}
	}

	@SuppressWarnings("unchecked")
	private ResourceLink resolveTargetAndCreateResourceLinkOrReturnNull(
			@Nonnull RequestPartitionId theRequestPartitionId,
			String theSourceResourceName,
			PathAndRef thePathAndRef,
			ResourceTable theEntity,
			Date theUpdateTime,
			IIdType theNextId,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails) {
		JpaPid resolvedResourceId = (JpaPid) theTransactionDetails.getResolvedResourceId(theNextId);
		if (resolvedResourceId != null) {
			String targetResourceType = theNextId.getResourceType();
			Long targetResourcePid = resolvedResourceId.getId();
			String targetResourceIdPart = theNextId.getIdPart();
			Long targetVersion = theNextId.getVersionIdPartAsLong();
			return ResourceLink.forLocalReference(
					thePathAndRef.getPath(),
					theEntity,
					targetResourceType,
					targetResourcePid,
					targetResourceIdPart,
					theUpdateTime,
					targetVersion);
		}

		/*
		 * We keep a cache of resolved target resources. This is good since for some resource types, there
		 * are multiple search parameters that map to the same element path within a resource (e.g.
		 * Observation:patient and Observation.subject and we don't want to force a resolution of the
		 * target any more times than we have to.
		 */

		IResourceLookup<JpaPid> targetResource;
		if (myPartitionSettings.isPartitioningEnabled()) {
			if (myPartitionSettings.getAllowReferencesAcrossPartitions()
					== PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED) {

				// Interceptor: Pointcut.JPA_CROSS_PARTITION_REFERENCE_DETECTED
				if (CompositeInterceptorBroadcaster.hasHooks(
						Pointcut.JPA_RESOLVE_CROSS_PARTITION_REFERENCE, myInterceptorBroadcaster, theRequest)) {
					CrossPartitionReferenceDetails referenceDetails = new CrossPartitionReferenceDetails(
							theRequestPartitionId,
							theSourceResourceName,
							thePathAndRef,
							theRequest,
							theTransactionDetails);
					HookParams params = new HookParams(referenceDetails);
					targetResource =
							(IResourceLookup<JpaPid>) CompositeInterceptorBroadcaster.doCallHooksAndReturnObject(
									myInterceptorBroadcaster,
									theRequest,
									Pointcut.JPA_RESOLVE_CROSS_PARTITION_REFERENCE,
									params);
				} else {
					targetResource = myResourceLinkResolver.findTargetResource(
							RequestPartitionId.allPartitions(),
							theSourceResourceName,
							thePathAndRef,
							theRequest,
							theTransactionDetails);
				}

			} else {
				targetResource = myResourceLinkResolver.findTargetResource(
						theRequestPartitionId, theSourceResourceName, thePathAndRef, theRequest, theTransactionDetails);
			}
		} else {
			targetResource = myResourceLinkResolver.findTargetResource(
					theRequestPartitionId, theSourceResourceName, thePathAndRef, theRequest, theTransactionDetails);
		}

		if (targetResource == null) {
			return null;
		}

		String targetResourceType = targetResource.getResourceType();
		Long targetResourcePid = targetResource.getPersistentId().getId();
		String targetResourceIdPart = theNextId.getIdPart();
		Long targetVersion = theNextId.getVersionIdPartAsLong();
		return ResourceLink.forLocalReference(
				thePathAndRef.getPath(),
				theEntity,
				targetResourceType,
				targetResourcePid,
				targetResourceIdPart,
				theUpdateTime,
				targetVersion);
	}

	private RequestPartitionId determineResolverPartitionId(@Nonnull RequestPartitionId theRequestPartitionId) {
		RequestPartitionId targetRequestPartitionId = theRequestPartitionId;
		if (myPartitionSettings.isPartitioningEnabled()
				&& myPartitionSettings.getAllowReferencesAcrossPartitions()
						== PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED) {
			targetRequestPartitionId = RequestPartitionId.allPartitions();
		}
		return targetRequestPartitionId;
	}

	private void populateResourceTable(
			Collection<? extends BaseResourceIndexedSearchParam> theParams, ResourceTable theResourceTable) {
		for (BaseResourceIndexedSearchParam next : theParams) {
			if (next.getResourcePid() == null) {
				next.setResource(theResourceTable);
			}
		}
	}

	private void populateResourceTableForComboParams(
			Collection<? extends IResourceIndexComboSearchParameter> theParams, ResourceTable theResourceTable) {
		for (IResourceIndexComboSearchParameter next : theParams) {
			if (next.getResource() == null) {
				next.setResource(theResourceTable);
				if (next instanceof BasePartitionable) {
					((BasePartitionable) next).setPartitionId(theResourceTable.getPartitionId());
				}
			}
		}
	}

	@VisibleForTesting
	void setInterceptorBroadcasterForUnitTest(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@Nonnull
	public List<String> extractParamValuesAsStrings(
			RuntimeSearchParam theActiveSearchParam, IBaseResource theResource) {
		return mySearchParamExtractor.extractParamValuesAsStrings(theActiveSearchParam, theResource);
	}

	public void extractSearchParamComboUnique(ResourceTable theEntity, ResourceIndexedSearchParams theParams) {
		String resourceType = theEntity.getResourceType();
		Set<ResourceIndexedComboStringUnique> comboUniques =
				mySearchParamExtractor.extractSearchParamComboUnique(resourceType, theParams);
		theParams.myComboStringUniques.addAll(comboUniques);
		populateResourceTableForComboParams(theParams.myComboStringUniques, theEntity);
	}

	public void extractSearchParamComboNonUnique(ResourceTable theEntity, ResourceIndexedSearchParams theParams) {
		String resourceType = theEntity.getResourceType();
		Set<ResourceIndexedComboTokenNonUnique> comboNonUniques =
				mySearchParamExtractor.extractSearchParamComboNonUnique(resourceType, theParams);
		theParams.myComboTokenNonUnique.addAll(comboNonUniques);
		populateResourceTableForComboParams(theParams.myComboTokenNonUnique, theEntity);
	}

	/**
	 * This interface is used by {@link #extractSearchIndexParametersForTargetResources(RequestDetails, ResourceIndexedSearchParams, ResourceTable, Collection, IChainedSearchParameterExtractionStrategy, ISearchParamExtractor.SearchParamSet, boolean, boolean)}
	 * in order to use that method for extracting chained search parameter indexes both
	 * from contained resources and from uplifted refchains.
	 */
	private interface IChainedSearchParameterExtractionStrategy {

		/**
		 * Which search parameters should be indexed for the resource target
		 * at the given path. In other words if thePathAndRef contains
		 * "Patient/123", then we could return a filter that only lets the
		 * "name" and "gender" search params through  if we only want those
		 * two parameters to be indexed for the resolved Patient resource
		 * with that ID.
		 */
		@Nonnull
		ISearchParamExtractor.ISearchParamFilter getSearchParamFilter(@Nonnull PathAndRef thePathAndRef);

		/**
		 * Actually fetch the resource at the given path, or return
		 * {@literal null} if none can be found.
		 */
		@Nullable
		IBaseResource fetchResourceAtPath(@Nonnull PathAndRef thePathAndRef);
	}

	static void handleWarnings(
			RequestDetails theRequestDetails,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			ISearchParamExtractor.SearchParamSet<?> theSearchParamSet) {
		if (theSearchParamSet.getWarnings().isEmpty()) {
			return;
		}

		// If extraction generated any warnings, broadcast an error
		if (CompositeInterceptorBroadcaster.hasHooks(
				Pointcut.JPA_PERFTRACE_WARNING, theInterceptorBroadcaster, theRequestDetails)) {
			for (String next : theSearchParamSet.getWarnings()) {
				StorageProcessingMessage messageHolder = new StorageProcessingMessage();
				messageHolder.setMessage(next);
				HookParams params = new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
						.add(StorageProcessingMessage.class, messageHolder);
				CompositeInterceptorBroadcaster.doCallHooks(
						theInterceptorBroadcaster, theRequestDetails, Pointcut.JPA_PERFTRACE_WARNING, params);
			}
		}
	}
}
