package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.FhirTerser;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchParamExtractorService {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamExtractorService.class);
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired(required = false)
	private IResourceLinkResolver myResourceLinkResolver;

	@VisibleForTesting
	public void setSearchParamExtractor(ISearchParamExtractor theSearchParamExtractor) {
		mySearchParamExtractor = theSearchParamExtractor;
	}

	/**
	 * This method is responsible for scanning a resource for all of the search parameter instances. I.e. for all search parameters defined for
	 * a given resource type, it extracts the associated indexes and populates {@literal theParams}.
	 */
	public void extractFromResource(RequestPartitionId theRequestPartitionId, RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, TransactionDetails theTransactionDetails, boolean theFailOnInvalidReference) {

		// All search parameter types except Reference
		ResourceIndexedSearchParams normalParams = new ResourceIndexedSearchParams();
		extractSearchIndexParameters(theRequestDetails, normalParams, theResource, theEntity);
		mergeParams(normalParams, theParams);

		if (myModelConfig.isIndexOnContainedResources()) {
			ResourceIndexedSearchParams containedParams = new ResourceIndexedSearchParams();
			extractSearchIndexParametersForContainedResources(theRequestDetails, containedParams, theResource, theEntity);
			mergeParams(containedParams, theParams);
		}

		// Do this after, because we add to strings during both string and token processing, and contained resource if any
		populateResourceTables(theParams, theEntity);

		// Reference search parameters
		extractResourceLinks(theRequestPartitionId, theParams, theEntity, theResource, theTransactionDetails, theFailOnInvalidReference, theRequestDetails);

		if (myModelConfig.isIndexOnContainedResources()) {
			extractResourceLinksForContainedResources(theRequestPartitionId, theParams, theEntity, theResource, theTransactionDetails, theFailOnInvalidReference, theRequestDetails);
		}

		theParams.setUpdatedTime(theTransactionDetails.getTransactionDate());
	}

	@VisibleForTesting
	public void setModelConfig(ModelConfig theModelConfig) {
		myModelConfig = theModelConfig;
	}

	private void extractSearchIndexParametersForContainedResources(RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, IBaseResource theResource, ResourceTable theEntity) {

		FhirTerser terser = myContext.newTerser();

		// 1. get all contained resources
		Collection<IBaseResource> containedResources = terser.getAllEmbeddedResources(theResource, false);

		extractSearchIndexParametersForContainedResources(theRequestDetails, theParams, theResource, theEntity, containedResources, new HashSet<>());
	}

	private void extractSearchIndexParametersForContainedResources(RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, IBaseResource theResource, ResourceTable theEntity, Collection<IBaseResource> theContainedResources, Collection<IBaseResource> theAlreadySeenResources) {
		// 2. Find referenced search parameters
		ISearchParamExtractor.SearchParamSet<PathAndRef> referencedSearchParamSet = mySearchParamExtractor.extractResourceLinks(theResource, true);

		String spnamePrefix = null;
		ResourceIndexedSearchParams currParams;
		// 3. for each referenced search parameter, create an index
		for (PathAndRef nextPathAndRef : referencedSearchParamSet) {

			// 3.1 get the search parameter name as spname prefix
			spnamePrefix = nextPathAndRef.getSearchParamName();

			if (spnamePrefix == null || nextPathAndRef.getRef() == null)
				continue;

			// 3.2 find the contained resource
			IBaseResource containedResource = findContainedResource(theContainedResources, nextPathAndRef.getRef());
			if (containedResource == null)
				continue;

			// 3.2.1 if we've already processed this resource upstream, do not process it again, to prevent infinite loops
			if (theAlreadySeenResources.contains(containedResource)) {
				continue;
			}

			currParams = new ResourceIndexedSearchParams();

			// 3.3 create indexes for the current contained resource
			extractSearchIndexParameters(theRequestDetails, currParams, containedResource, theEntity);

			// 3.4 recurse to process any other contained resources referenced by this one
			if (myModelConfig.isIndexOnContainedResourcesRecursively()) {
				HashSet<IBaseResource> nextAlreadySeenResources = new HashSet<>(theAlreadySeenResources);
				nextAlreadySeenResources.add(containedResource);
				extractSearchIndexParametersForContainedResources(theRequestDetails, currParams, containedResource, theEntity, theContainedResources, nextAlreadySeenResources);
			}

			// 3.5 added reference name as a prefix for the contained resource if any
			// e.g. for Observation.subject contained reference
			// the SP_NAME = subject.family
			currParams.updateSpnamePrefixForIndexedOnContainedResource(theEntity.getResourceType(), spnamePrefix);

			// 3.6 merge to the mainParams
			// NOTE: the spname prefix is different
			mergeParams(currParams, theParams);
		}
	}

	private IBaseResource findContainedResource(Collection<IBaseResource> resources, IBaseReference reference) {
		for (IBaseResource resource : resources) {
			if (resource.getIdElement().equals(reference.getReferenceElement()))
				return resource;
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
	}

	private void extractSearchIndexParameters(RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, IBaseResource theResource, ResourceTable theEntity) {

		// Strings
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> strings = extractSearchParamStrings(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, strings);
		theParams.myStringParams.addAll(strings);

		// Numbers
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamNumber> numbers = extractSearchParamNumber(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, numbers);
		theParams.myNumberParams.addAll(numbers);

		// Quantities
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantity> quantities = extractSearchParamQuantity(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, quantities);
		theParams.myQuantityParams.addAll(quantities);

		if (myModelConfig.getNormalizedQuantitySearchLevel().equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED) || myModelConfig.getNormalizedQuantitySearchLevel().equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED)) {
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> quantitiesNormalized = extractSearchParamQuantityNormalized(theResource);
			handleWarnings(theRequestDetails, myInterceptorBroadcaster, quantitiesNormalized);
			theParams.myQuantityNormalizedParams.addAll(quantitiesNormalized);
		}

		// Dates
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> dates = extractSearchParamDates(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, dates);
		theParams.myDateParams.addAll(dates);

		// URIs
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamUri> uris = extractSearchParamUri(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, uris);
		theParams.myUriParams.addAll(uris);

		// Tokens (can result in both Token and String, as we index the display name for
		// the types: Coding, CodeableConcept)
		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> tokens = extractSearchParamTokens(theResource);
		for (BaseResourceIndexedSearchParam next : tokens) {
			if (next instanceof ResourceIndexedSearchParamToken) {
				theParams.myTokenParams.add((ResourceIndexedSearchParamToken) next);
			} else if (next instanceof ResourceIndexedSearchParamCoords) {
				theParams.myCoordsParams.add((ResourceIndexedSearchParamCoords) next);
			} else {
				theParams.myStringParams.add((ResourceIndexedSearchParamString) next);
			}
		}

		// Specials
		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> specials = extractSearchParamSpecial(theResource);
		for (BaseResourceIndexedSearchParam next : specials) {
			if (next instanceof ResourceIndexedSearchParamCoords) {
				theParams.myCoordsParams.add((ResourceIndexedSearchParamCoords) next);
			}
		}

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

	private void extractResourceLinks(RequestPartitionId theRequestPartitionId, ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, TransactionDetails theTransactionDetails, boolean theFailOnInvalidReference, RequestDetails theRequest) {
		String resourceName = myContext.getResourceType(theResource);

		ISearchParamExtractor.SearchParamSet<PathAndRef> refs = mySearchParamExtractor.extractResourceLinks(theResource, false);
		SearchParamExtractorService.handleWarnings(theRequest, myInterceptorBroadcaster, refs);

		for (PathAndRef nextPathAndRef : refs) {
			RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(resourceName, nextPathAndRef.getSearchParamName());
			extractResourceLinks(theRequestPartitionId, theParams, theEntity, theTransactionDetails, searchParam, nextPathAndRef, theFailOnInvalidReference, theRequest);
		}

		theEntity.setHasLinks(theParams.myLinks.size() > 0);
	}

	private void extractResourceLinks(@Nonnull RequestPartitionId theRequestPartitionId, ResourceIndexedSearchParams theParams, ResourceTable theEntity, TransactionDetails theTransactionDetails, RuntimeSearchParam theRuntimeSearchParam, PathAndRef thePathAndRef, boolean theFailOnInvalidReference, RequestDetails theRequest) {
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

		if (myContext.getParserOptions().isStripVersionsFromReferences() && !myContext.getParserOptions().getDontStripVersionsFromReferencesAtPaths().contains(thePathAndRef.getPath()) && nextId.hasVersionIdPart()) {
			nextId = nextId.toVersionless();
		}

		theParams.myPopulatedResourceLinkParameters.add(thePathAndRef.getSearchParamName());

		boolean canonical = thePathAndRef.isCanonical();
		if (LogicalReferenceHelper.isLogicalReference(myModelConfig, nextId) || canonical) {
			String value = nextId.getValue();
			ResourceLink resourceLink = ResourceLink.forLogicalReference(thePathAndRef.getPath(), theEntity, value, transactionDate);
			if (theParams.myLinks.add(resourceLink)) {
				ourLog.debug("Indexing remote resource reference URL: {}", nextId);
			}
			return;
		}

		String baseUrl = nextId.getBaseUrl();
		String typeString = nextId.getResourceType();
		if (isBlank(typeString)) {
			String msg = "Invalid resource reference found at path[" + path + "] - Does not contain resource type - " + nextId.getValue();
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
			String msg = "Invalid resource reference found at path[" + path + "] - Resource type is unknown or not supported on this server - " + nextId.getValue();
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
			if (!myModelConfig.getTreatBaseUrlsAsLocal().contains(baseUrl) && !myModelConfig.isAllowExternalReferences()) {
				String msg = myContext.getLocalizer().getMessage(BaseSearchParamExtractor.class, "externalReferenceNotAllowed", nextId.getValue());
				throw new InvalidRequestException(Msg.code(507) + msg);
			} else {
				ResourceLink resourceLink = ResourceLink.forAbsoluteReference(thePathAndRef.getPath(), theEntity, nextId, transactionDate);
				if (theParams.myLinks.add(resourceLink)) {
					ourLog.debug("Indexing remote resource reference URL: {}", nextId);
				}
				return;
			}
		}

		Class<? extends IBaseResource> type = resourceDefinition.getImplementingClass();
		String targetId = nextId.getIdPart();
		if (StringUtils.isBlank(targetId)) {
			String msg = "Invalid resource reference found at path[" + path + "] - Does not contain resource ID - " + nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(Msg.code(508) + msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}

		IIdType referenceElement = thePathAndRef.getRef().getReferenceElement();
		ResourcePersistentId resolvedTargetId = theTransactionDetails.getResolvedResourceId(referenceElement);
		ResourceLink resourceLink;

		Long targetVersionId = nextId.getVersionIdPartAsLong();
		if (resolvedTargetId != null) {

			/*
			 * If we have already resolved the given reference within this transaction, we don't
			 * need to resolve it again
			 */
			myResourceLinkResolver.validateTypeOrThrowException(type);
			resourceLink = ResourceLink.forLocalReference(thePathAndRef.getPath(), theEntity, typeString, resolvedTargetId.getIdAsLong(), targetId, transactionDate, targetVersionId);

		} else if (theFailOnInvalidReference) {

			/*
			 * The reference points to another resource, so let's look it up. We need to do this
			 * since the target may be a forced ID, but also so that we can throw an exception
			 * if the reference is invalid
			 */
			myResourceLinkResolver.validateTypeOrThrowException(type);
			resourceLink = resolveTargetAndCreateResourceLinkOrReturnNull(theRequestPartitionId, theEntity, transactionDate, theRuntimeSearchParam, path, thePathAndRef, nextId, typeString, type, nextReference, theRequest, theTransactionDetails);
			if (resourceLink == null) {
				return;
			} else {
				// Cache the outcome in the current transaction in case there are more references
				ResourcePersistentId persistentId = new ResourcePersistentId(resourceLink.getTargetResourcePid());
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
			resourceLink = ResourceLink.forLocalReference(thePathAndRef.getPath(), theEntity, typeString, null, targetId, transactionDate, targetVersionId);

		}

		theParams.myLinks.add(resourceLink);
	}

	private void extractResourceLinksForContainedResources(RequestPartitionId theRequestPartitionId, ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, TransactionDetails theTransactionDetails, boolean theFailOnInvalidReference, RequestDetails theRequest) {

		FhirTerser terser = myContext.newTerser();

		// 1. get all contained resources
		Collection<IBaseResource> containedResources = terser.getAllEmbeddedResources(theResource, false);

		extractResourceLinksForContainedResources(theRequestPartitionId, theParams, theEntity, theResource, theTransactionDetails, theFailOnInvalidReference, theRequest, containedResources, new HashSet<>());
	}

	private void extractResourceLinksForContainedResources(RequestPartitionId theRequestPartitionId, ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, TransactionDetails theTransactionDetails, boolean theFailOnInvalidReference, RequestDetails theRequest, Collection<IBaseResource> theContainedResources, Collection<IBaseResource> theAlreadySeenResources) {

		// 2. Find referenced search parameters
		ISearchParamExtractor.SearchParamSet<PathAndRef> referencedSearchParamSet = mySearchParamExtractor.extractResourceLinks(theResource, true);

		String spNamePrefix;
		ResourceIndexedSearchParams currParams;
		// 3. for each referenced search parameter, create an index
		for (PathAndRef nextPathAndRef : referencedSearchParamSet) {

			// 3.1 get the search parameter name as spname prefix
			spNamePrefix = nextPathAndRef.getSearchParamName();

			if (spNamePrefix == null || nextPathAndRef.getRef() == null)
				continue;

			// 3.2 find the contained resource
			IBaseResource containedResource = findContainedResource(theContainedResources, nextPathAndRef.getRef());
			if (containedResource == null)
				continue;

			// 3.2.1 if we've already processed this resource upstream, do not process it again, to prevent infinite loops
			if (theAlreadySeenResources.contains(containedResource)) {
				continue;
			}

			currParams = new ResourceIndexedSearchParams();

			// 3.3 create indexes for the current contained resource
			extractResourceLinks(theRequestPartitionId, currParams, theEntity, containedResource, theTransactionDetails, theFailOnInvalidReference, theRequest);

			// 3.4 recurse to process any other contained resources referenced by this one
			if (myModelConfig.isIndexOnContainedResourcesRecursively()) {
				HashSet<IBaseResource> nextAlreadySeenResources = new HashSet<>(theAlreadySeenResources);
				nextAlreadySeenResources.add(containedResource);
				extractResourceLinksForContainedResources(theRequestPartitionId, currParams, theEntity, containedResource, theTransactionDetails, theFailOnInvalidReference, theRequest, theContainedResources, nextAlreadySeenResources);
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

	private ResourceLink resolveTargetAndCreateResourceLinkOrReturnNull(@Nonnull RequestPartitionId theRequestPartitionId, ResourceTable theEntity, Date theUpdateTime, RuntimeSearchParam nextSpDef, String theNextPathsUnsplit, PathAndRef nextPathAndRef, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, IBaseReference theReference, RequestDetails theRequest, TransactionDetails theTransactionDetails) {
		assert theRequestPartitionId != null;

		ResourcePersistentId resolvedResourceId = theTransactionDetails.getResolvedResourceId(theNextId);
		if (resolvedResourceId != null) {
			String targetResourceType = theNextId.getResourceType();
			Long targetResourcePid = resolvedResourceId.getIdAsLong();
			String targetResourceIdPart = theNextId.getIdPart();
			Long targetVersion = theNextId.getVersionIdPartAsLong();
			return ResourceLink.forLocalReference(nextPathAndRef.getPath(), theEntity, targetResourceType, targetResourcePid, targetResourceIdPart, theUpdateTime, targetVersion);
		}

		/*
		 * We keep a cache of resolved target resources. This is good since for some resource types, there
		 * are multiple search parameters that map to the same element path within a resource (e.g.
		 * Observation:patient and Observation.subject and we don't want to force a resolution of the
		 * target any more times than we have to.
		 */

		RequestPartitionId targetRequestPartitionId = theRequestPartitionId;
		if (myPartitionSettings.isPartitioningEnabled() && myPartitionSettings.getAllowReferencesAcrossPartitions() == PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED) {
			targetRequestPartitionId = RequestPartitionId.allPartitions();
		}

		IResourceLookup targetResource = myResourceLinkResolver.findTargetResource(targetRequestPartitionId, nextSpDef, theNextPathsUnsplit, theNextId, theTypeString, theType, theReference, theRequest, theTransactionDetails);

		if (targetResource == null) {
			return null;
		}

		String targetResourceType = targetResource.getResourceType();
		Long targetResourcePid = targetResource.getResourceId();
		String targetResourceIdPart = theNextId.getIdPart();
		Long targetVersion = theNextId.getVersionIdPartAsLong();
		return ResourceLink.forLocalReference(nextPathAndRef.getPath(), theEntity, targetResourceType, targetResourcePid, targetResourceIdPart, theUpdateTime, targetVersion);
	}

	private void populateResourceTable(Collection<? extends BaseResourceIndexedSearchParam> theParams, ResourceTable theResourceTable) {
		for (BaseResourceIndexedSearchParam next : theParams) {
			if (next.getResourcePid() == null) {
				next.setResource(theResourceTable);
			}
		}
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamDates(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamNumber(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamQuantity(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> extractSearchParamQuantityNormalized(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamQuantityNormalized(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamStrings(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamTokens(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamSpecial(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamSpecial(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamUri(theResource);
	}

	@VisibleForTesting
	void setInterceptorBroadcasterForUnitTest(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@Nonnull
	public List<String> extractParamValuesAsStrings(RuntimeSearchParam theActiveSearchParam, IBaseResource theResource) {
		return mySearchParamExtractor.extractParamValuesAsStrings(theActiveSearchParam, theResource);
	}

	static void handleWarnings(RequestDetails theRequestDetails, IInterceptorBroadcaster theInterceptorBroadcaster, ISearchParamExtractor.SearchParamSet<?> theSearchParamSet) {
		if (theSearchParamSet.getWarnings().isEmpty()) {
			return;
		}

		// If extraction generated any warnings, broadcast an error
		for (String next : theSearchParamSet.getWarnings()) {
			StorageProcessingMessage messageHolder = new StorageProcessingMessage();
			messageHolder.setMessage(next);
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(StorageProcessingMessage.class, messageHolder);
			CompositeInterceptorBroadcaster.doCallHooks(theInterceptorBroadcaster, theRequestDetails, Pointcut.JPA_PERFTRACE_WARNING, params);
		}
	}
}

