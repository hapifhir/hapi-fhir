package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.context.FhirVersionEnum;
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
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
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
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.Set;

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
	@Autowired
	private IRequestPartitionHelperSvc myPartitionHelperSvc;

	@VisibleForTesting
	public void setSearchParamExtractor(ISearchParamExtractor theSearchParamExtractor) {
		mySearchParamExtractor = theSearchParamExtractor;
	}


	public void extractFromResource(RequestPartitionId theRequestPartitionId, RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, TransactionDetails theTransactionDetails, boolean theFailOnInvalidReference) {
		extractFromResource(theRequestPartitionId, theRequestDetails, theParams, new ResourceIndexedSearchParams(), theEntity, theResource, theTransactionDetails, theFailOnInvalidReference);
	}

	/**
	 * This method is responsible for scanning a resource for all of the search parameter instances.
	 * I.e. for all search parameters defined for
	 * a given resource type, it extracts the associated indexes and populates
	 * {@literal theParams}.
	 */
	public void extractFromResource(RequestPartitionId theRequestPartitionId, RequestDetails theRequestDetails, ResourceIndexedSearchParams theNewParams, ResourceIndexedSearchParams theExistingParams, ResourceTable theEntity, IBaseResource theResource, TransactionDetails theTransactionDetails, boolean theFailOnInvalidReference) {

		// All search parameter types except Reference
		ResourceIndexedSearchParams normalParams = new ResourceIndexedSearchParams();
		extractSearchIndexParameters(theRequestDetails, normalParams, theResource, ISearchParamExtractor.ALWAYS_TRUE);
		mergeParams(normalParams, theNewParams);

		boolean indexOnContainedResources = myStorageSettings.isIndexOnContainedResources();
		ISearchParamExtractor.SearchParamSet<PathAndRef> indexedReferences = mySearchParamExtractor.extractResourceLinks(theResource, indexOnContainedResources);
		SearchParamExtractorService.handleWarnings(theRequestDetails, myInterceptorBroadcaster, indexedReferences);

		if (indexOnContainedResources) {
			ResourceIndexedSearchParams containedParams = new ResourceIndexedSearchParams();
			extractSearchIndexParametersForContainedResources(theRequestDetails, containedParams, theResource, theEntity, indexedReferences);
			mergeParams(containedParams, theNewParams);
		}

		if (myStorageSettings.isIndexOnUpliftedRefchains()) {
			ResourceIndexedSearchParams containedParams = new ResourceIndexedSearchParams();
			extractSearchIndexParametersForUpliftedRefchains(theRequestDetails, containedParams, theResource, theEntity, theRequestPartitionId, theTransactionDetails, indexedReferences);
			mergeParams(containedParams, theNewParams);
		}

		// Do this after, because we add to strings during both string and token processing, and contained resource if any
		populateResourceTables(theNewParams, theEntity);

		// Reference search parameters
		extractResourceLinks(theRequestPartitionId, theExistingParams, theNewParams, theEntity, theResource, theTransactionDetails, theFailOnInvalidReference, theRequestDetails, indexedReferences);

		if (indexOnContainedResources) {
			extractResourceLinksForContainedResources(theRequestPartitionId, theNewParams, theEntity, theResource, theTransactionDetails, theFailOnInvalidReference, theRequestDetails);
		}

		theNewParams.setUpdatedTime(theTransactionDetails.getTransactionDate());
	}

	@VisibleForTesting
	public void setStorageSettings(StorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	private void extractSearchIndexParametersForContainedResources(RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, IBaseResource theResource, ResourceTable theEntity, ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences) {

		FhirTerser terser = myContext.newTerser();

		// 1. get all contained resources
		Collection<IBaseResource> containedResources = terser.getAllEmbeddedResources(theResource, false);

		// Extract search parameters
		Function<PathAndRef, ISearchParamExtractor.ISearchParamInclusionChecker> appliesChecker = t -> ISearchParamExtractor.ALWAYS_TRUE;
		Function<PathAndRef, IBaseResource> targetFetcher = u -> findContainedResource(containedResources, u.getRef());
		boolean recurse = myStorageSettings.isIndexOnContainedResourcesRecursively();
		extractSearchIndexParametersForTargetResources(theRequestDetails, theParams, theResource, theEntity, new HashSet<>(), appliesChecker, targetFetcher, theIndexedReferences, recurse);
	}

	private void extractSearchIndexParametersForUpliftedRefchains(RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, IBaseResource theResource, ResourceTable theEntity, RequestPartitionId theRequestPartitionId, TransactionDetails theTransactionDetails, ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences) {
		Function<PathAndRef, ISearchParamExtractor.ISearchParamInclusionChecker> searchParamAppliesChecker = pathAndRef -> {
			String searchParamName = pathAndRef.getSearchParamName();
			RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(theEntity.getResourceType(), searchParamName);
			if (searchParam.hasUpliftRefchains()) {
				return searchParam::hasUpliftRefchain;
			}
			return null;
		};
		Function<PathAndRef, IBaseResource> targetFetcher = pathAndRef -> {
			if (myResourceLinkResolver != null) {
				RequestPartitionId targetRequestPartitionId = determineResolverPartitionId(theRequestPartitionId);
				IBaseResource resource = myResourceLinkResolver.loadTargetResource(targetRequestPartitionId, theEntity.getResourceType(), pathAndRef, theRequestDetails, theTransactionDetails);
				if (resource != null) {
					ourLog.trace("Found target: {}", resource.getIdElement());
					return resource;
				}
			}
			return null;
		};
		extractSearchIndexParametersForTargetResources(theRequestDetails, theParams, theResource, theEntity, new HashSet<>(), searchParamAppliesChecker, targetFetcher, theIndexedReferences, false);
	}

	private void extractSearchIndexParametersForTargetResources(RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, IBaseResource theResource, ResourceTable theEntity, Collection<IBaseResource> theAlreadySeenResources, Function<PathAndRef, ISearchParamExtractor.ISearchParamInclusionChecker> theSearchParamAppliesChecker, Function<PathAndRef, IBaseResource> theTargetFetcher, ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences, boolean theRecurse) {
		// 2. Find referenced search parameters

		String spnamePrefix;
		ResourceIndexedSearchParams currParams;
		// 3. for each referenced search parameter, create an index
		for (PathAndRef nextPathAndRef : theIndexedReferences) {

			// 3.1 get the search parameter name as spname prefix
			spnamePrefix = nextPathAndRef.getSearchParamName();

			if (spnamePrefix == null || nextPathAndRef.getRef() == null)
				continue;

			// 3.1.2 check if this ref actually applies here
			ISearchParamExtractor.ISearchParamInclusionChecker searchParamInclusionChecker = theSearchParamAppliesChecker.apply(nextPathAndRef);
			if (searchParamInclusionChecker == null) {
				continue;
			}

			// 3.2 find the target resource
			IBaseResource targetResource = theTargetFetcher.apply(nextPathAndRef);
			if (targetResource == null)
				continue;

			// 3.2.1 if we've already processed this resource upstream, do not process it again, to prevent infinite loops
			if (theAlreadySeenResources.contains(targetResource)) {
				continue;
			}

			currParams = new ResourceIndexedSearchParams();

			// 3.3 create indexes for the current contained resource
			extractSearchIndexParameters(theRequestDetails, currParams, targetResource, searchParamInclusionChecker);

			// 3.4 recurse to process any other contained resources referenced by this one
			if (theRecurse) {
				HashSet<IBaseResource> nextAlreadySeenResources = new HashSet<>(theAlreadySeenResources);
				nextAlreadySeenResources.add(targetResource);
				extractSearchIndexParametersForTargetResources(theRequestDetails, currParams, targetResource, theEntity, nextAlreadySeenResources, theSearchParamAppliesChecker, theTargetFetcher, theIndexedReferences, theRecurse);
			}

			// 3.5 added reference name as a prefix for the contained resource if any
			// e.g. for Observation.subject contained reference
			// the SP_NAME = subject.family
			currParams.updateSpnamePrefixForIndexedOnChainedResource(theEntity.getResourceType(), spnamePrefix);

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
		theTargetParams.myCompositeParams.addAll(theSrcParams.myCompositeParams);
	}

	void extractSearchIndexParameters(RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {

		// Strings
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> strings = extractSearchParamStrings(theResource, theInclusionChecker);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, strings);
		theParams.myStringParams.addAll(strings);

		// Numbers
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamNumber> numbers = extractSearchParamNumber(theResource, theInclusionChecker);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, numbers);
		theParams.myNumberParams.addAll(numbers);

		// Quantities
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantity> quantities = extractSearchParamQuantity(theResource, theInclusionChecker);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, quantities);
		theParams.myQuantityParams.addAll(quantities);

		if (myStorageSettings.getNormalizedQuantitySearchLevel().equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED) || myStorageSettings.getNormalizedQuantitySearchLevel().equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED)) {
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> quantitiesNormalized = extractSearchParamQuantityNormalized(theResource, theInclusionChecker);
			handleWarnings(theRequestDetails, myInterceptorBroadcaster, quantitiesNormalized);
			theParams.myQuantityNormalizedParams.addAll(quantitiesNormalized);
		}

		// Dates
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> dates = extractSearchParamDates(theResource, theInclusionChecker);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, dates);
		theParams.myDateParams.addAll(dates);

		// URIs
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamUri> uris = extractSearchParamUri(theResource, theInclusionChecker);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, uris);
		theParams.myUriParams.addAll(uris);

		// Tokens (can result in both Token and String, as we index the display name for
		// the types: Coding, CodeableConcept)
		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> tokens = extractSearchParamTokens(theResource, theInclusionChecker);
		for (BaseResourceIndexedSearchParam next : tokens) {
			if (next instanceof ResourceIndexedSearchParamToken) {
				theParams.myTokenParams.add((ResourceIndexedSearchParamToken) next);
			} else if (next instanceof ResourceIndexedSearchParamCoords) {
				theParams.myCoordsParams.add((ResourceIndexedSearchParamCoords) next);
			} else {
				theParams.myStringParams.add((ResourceIndexedSearchParamString) next);
			}
		}

		// Composites
		// dst2 composites use stuff like value[x] , and we don't support them.
		if (myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamComposite> composites = extractSearchParamComposites(theResource, theInclusionChecker);
			handleWarnings(theRequestDetails, myInterceptorBroadcaster, composites);
			theParams.myCompositeParams.addAll(composites);
		}

		// Specials
		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> specials = extractSearchParamSpecial(theResource, theInclusionChecker);
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

	private void extractResourceLinks(RequestPartitionId theRequestPartitionId, ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, TransactionDetails theTransactionDetails, boolean theFailOnInvalidReference, RequestDetails theRequest, ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences) {
		extractResourceLinks(theRequestPartitionId, new ResourceIndexedSearchParams(), theParams, theEntity, theResource, theTransactionDetails, theFailOnInvalidReference, theRequest, theIndexedReferences);
	}

	private void extractResourceLinks(RequestPartitionId theRequestPartitionId, ResourceIndexedSearchParams theExistingParams, ResourceIndexedSearchParams theNewParams, ResourceTable theEntity, IBaseResource theResource, TransactionDetails theTransactionDetails, boolean theFailOnInvalidReference, RequestDetails theRequest, ISearchParamExtractor.SearchParamSet<PathAndRef> theIndexedReferences) {
		String sourceResourceName = myContext.getResourceType(theResource);

		for (PathAndRef nextPathAndRef : theIndexedReferences) {
			RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(sourceResourceName, nextPathAndRef.getSearchParamName());
			extractResourceLinks(theRequestPartitionId, theExistingParams, theNewParams, theEntity, theTransactionDetails, sourceResourceName, searchParam, nextPathAndRef, theFailOnInvalidReference, theRequest);
		}

		theEntity.setHasLinks(theNewParams.myLinks.size() > 0);
	}

	private void extractResourceLinks(@Nonnull RequestPartitionId theRequestPartitionId, ResourceIndexedSearchParams theExistingParams, ResourceIndexedSearchParams theNewParams, ResourceTable theEntity, TransactionDetails theTransactionDetails, String theSourceResourceName, RuntimeSearchParam theRuntimeSearchParam, PathAndRef thePathAndRef, boolean theFailOnInvalidReference, RequestDetails theRequest) {
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

		theNewParams.myPopulatedResourceLinkParameters.add(thePathAndRef.getSearchParamName());

		boolean canonical = thePathAndRef.isCanonical();
		if (LogicalReferenceHelper.isLogicalReference(myStorageSettings, nextId) || canonical) {
			String value = nextId.getValue();
			ResourceLink resourceLink = ResourceLink.forLogicalReference(thePathAndRef.getPath(), theEntity, value, transactionDate);
			if (theNewParams.myLinks.add(resourceLink)) {
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
			if (!myStorageSettings.getTreatBaseUrlsAsLocal().contains(baseUrl) && !myStorageSettings.isAllowExternalReferences()) {
				String msg = myContext.getLocalizer().getMessage(BaseSearchParamExtractor.class, "externalReferenceNotAllowed", nextId.getValue());
				throw new InvalidRequestException(Msg.code(507) + msg);
			} else {
				ResourceLink resourceLink = ResourceLink.forAbsoluteReference(thePathAndRef.getPath(), theEntity, nextId, transactionDate);
				if (theNewParams.myLinks.add(resourceLink)) {
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
		JpaPid resolvedTargetId = (JpaPid) theTransactionDetails.getResolvedResourceId(referenceElement);
		ResourceLink resourceLink;

		Long targetVersionId = nextId.getVersionIdPartAsLong();
		if (resolvedTargetId != null) {

			/*
			 * If we have already resolved the given reference within this transaction, we don't
			 * need to resolve it again
			 */
			myResourceLinkResolver.validateTypeOrThrowException(type);
			resourceLink = ResourceLink.forLocalReference(thePathAndRef.getPath(), theEntity, typeString, resolvedTargetId.getId(), targetId, transactionDate, targetVersionId);

		} else if (theFailOnInvalidReference) {

			/*
			 * The reference points to another resource, so let's look it up. We need to do this
			 * since the target may be a forced ID, but also so that we can throw an exception
			 * if the reference is invalid
			 */
			myResourceLinkResolver.validateTypeOrThrowException(type);

			/**
			 * We need to obtain a resourceLink out of the provided {@literal thePathAndRef}.  In the case
			 * where we are updating a resource that already has resourceLinks (stored in {@literal theExistingParams.getResourceLinks()}),
			 * let's try to match thePathAndRef to an already existing resourceLink to avoid the
			 * very expensive operation of creating a resourceLink that would end up being exactly the same
			 * one we already have.
			 */
			Optional<ResourceLink> optionalResourceLink = findMatchingResourceLink(thePathAndRef, theExistingParams.getResourceLinks());
			if (optionalResourceLink.isPresent()) {
				resourceLink = optionalResourceLink.get();
			} else {
				resourceLink = resolveTargetAndCreateResourceLinkOrReturnNull(theRequestPartitionId, theSourceResourceName, thePathAndRef, theEntity, transactionDate, nextId, theRequest, theTransactionDetails);
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
			resourceLink = ResourceLink.forLocalReference(thePathAndRef.getPath(), theEntity, typeString, null, targetId, transactionDate, targetVersionId);

		}

		theNewParams.myLinks.add(resourceLink);
	}

	private Optional<ResourceLink> findMatchingResourceLink(PathAndRef thePathAndRef, Collection<ResourceLink> theResourceLinks) {
		IIdType referenceElement = thePathAndRef.getRef().getReferenceElement();
		List<ResourceLink> resourceLinks = new ArrayList<>(theResourceLinks);
		for (ResourceLink resourceLink : resourceLinks) {

			// comparing the searchParam path ex: Group.member.entity
			boolean hasMatchingSearchParamPath = StringUtils.equals(resourceLink.getSourcePath(), thePathAndRef.getPath());

			boolean hasMatchingResourceType = StringUtils.equals(resourceLink.getTargetResourceType(), referenceElement.getResourceType());

			boolean hasMatchingResourceId = StringUtils.equals(resourceLink.getTargetResourceId(), referenceElement.getIdPart());

			boolean hasMatchingResourceVersion = myContext.getParserOptions().isStripVersionsFromReferences() || referenceElement.getVersionIdPartAsLong() == null || referenceElement.getVersionIdPartAsLong().equals(resourceLink.getTargetResourceVersion());

			if (hasMatchingSearchParamPath && hasMatchingResourceType && hasMatchingResourceId && hasMatchingResourceVersion) {
				return Optional.of(resourceLink);
			}
		}

		return Optional.empty();

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
			ISearchParamExtractor.SearchParamSet<PathAndRef> indexedReferences = mySearchParamExtractor.extractResourceLinks(containedResource, true);
			extractResourceLinks(theRequestPartitionId, currParams, theEntity, containedResource, theTransactionDetails, theFailOnInvalidReference, theRequest, indexedReferences);

			// 3.4 recurse to process any other contained resources referenced by this one
			if (myStorageSettings.isIndexOnContainedResourcesRecursively()) {
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

	private ResourceLink resolveTargetAndCreateResourceLinkOrReturnNull(@Nonnull RequestPartitionId theRequestPartitionId, String theSourceResourceName, PathAndRef thePathAndRef, ResourceTable theEntity, Date theUpdateTime, IIdType theNextId, RequestDetails theRequest, TransactionDetails theTransactionDetails) {
		assert theRequestPartitionId != null;

		JpaPid resolvedResourceId = (JpaPid) theTransactionDetails.getResolvedResourceId(theNextId);
		if (resolvedResourceId != null) {
			String targetResourceType = theNextId.getResourceType();
			Long targetResourcePid = resolvedResourceId.getId();
			String targetResourceIdPart = theNextId.getIdPart();
			Long targetVersion = theNextId.getVersionIdPartAsLong();
			return ResourceLink.forLocalReference(thePathAndRef.getPath(), theEntity, targetResourceType, targetResourcePid, targetResourceIdPart, theUpdateTime, targetVersion);
		}

		/*
		 * We keep a cache of resolved target resources. This is good since for some resource types, there
		 * are multiple search parameters that map to the same element path within a resource (e.g.
		 * Observation:patient and Observation.subject and we don't want to force a resolution of the
		 * target any more times than we have to.
		 */

		IResourceLookup<JpaPid> targetResource;
		if (myPartitionSettings.isPartitioningEnabled()) {
			if (myPartitionSettings.getAllowReferencesAcrossPartitions() == PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED) {

				// Interceptor: Pointcut.JPA_CROSS_PARTITION_REFERENCE_DETECTED
				if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.JPA_RESOLVE_CROSS_PARTITION_REFERENCE, myInterceptorBroadcaster, theRequest)) {
					CrossPartitionReferenceDetails referenceDetails = new CrossPartitionReferenceDetails(theRequestPartitionId, theSourceResourceName, thePathAndRef, theRequest, theTransactionDetails);
					HookParams params = new HookParams(referenceDetails);
					targetResource = (IResourceLookup<JpaPid>) CompositeInterceptorBroadcaster.doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequest, Pointcut.JPA_RESOLVE_CROSS_PARTITION_REFERENCE, params);
				} else {
					targetResource = myResourceLinkResolver.findTargetResource(RequestPartitionId.allPartitions(), theSourceResourceName, thePathAndRef, theRequest, theTransactionDetails);
				}

			} else {
				targetResource = myResourceLinkResolver.findTargetResource(theRequestPartitionId, theSourceResourceName, thePathAndRef, theRequest, theTransactionDetails);
			}
		} else {
			targetResource = myResourceLinkResolver.findTargetResource(theRequestPartitionId, theSourceResourceName, thePathAndRef, theRequest, theTransactionDetails);
		}


		if (targetResource == null) {
			return null;
		}

		String targetResourceType = targetResource.getResourceType();
		Long targetResourcePid = targetResource.getPersistentId().getId();
		String targetResourceIdPart = theNextId.getIdPart();
		Long targetVersion = theNextId.getVersionIdPartAsLong();
		return ResourceLink.forLocalReference(thePathAndRef.getPath(), theEntity, targetResourceType, targetResourcePid, targetResourceIdPart, theUpdateTime, targetVersion);
	}

	private RequestPartitionId determineResolverPartitionId(@Nonnull RequestPartitionId theRequestPartitionId) {
		RequestPartitionId targetRequestPartitionId = theRequestPartitionId;
		if (myPartitionSettings.isPartitioningEnabled() && myPartitionSettings.getAllowReferencesAcrossPartitions() == PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED) {
			targetRequestPartitionId = RequestPartitionId.allPartitions();
		}
		return targetRequestPartitionId;
	}

	private void populateResourceTable(Collection<? extends BaseResourceIndexedSearchParam> theParams, ResourceTable theResourceTable) {
		for (BaseResourceIndexedSearchParam next : theParams) {
			if (next.getResourcePid() == null) {
				next.setResource(theResourceTable);
			}
		}
	}

	private void populateResourceTableForComboParams(Collection<? extends IResourceIndexComboSearchParameter> theParams, ResourceTable theResourceTable) {
		for (IResourceIndexComboSearchParameter next : theParams) {
			if (next.getResource() == null) {
				next.setResource(theResourceTable);
				if (next instanceof BasePartitionable) {
					((BasePartitionable) next).setPartitionId(theResourceTable.getPartitionId());
				}
			}
		}
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {
		return mySearchParamExtractor.extractSearchParamDates(theResource, theInclusionChecker);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {
		return mySearchParamExtractor.extractSearchParamNumber(theResource, theInclusionChecker);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {
		return mySearchParamExtractor.extractSearchParamQuantity(theResource, theInclusionChecker);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> extractSearchParamQuantityNormalized(IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {
		return mySearchParamExtractor.extractSearchParamQuantityNormalized(theResource, theInclusionChecker);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {
		return mySearchParamExtractor.extractSearchParamStrings(theResource, theInclusionChecker);
	}

	private ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {
		return mySearchParamExtractor.extractSearchParamTokens(theResource, theInclusionChecker);
	}

	private ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamSpecial(IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {
		return mySearchParamExtractor.extractSearchParamSpecial(theResource, theInclusionChecker);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {
		return mySearchParamExtractor.extractSearchParamUri(theResource, theInclusionChecker);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamComposite> extractSearchParamComposites(IBaseResource theResource, ISearchParamExtractor.ISearchParamInclusionChecker theInclusionChecker) {
		return mySearchParamExtractor.extractSearchParamComposites(theResource, theInclusionChecker);
	}


	@VisibleForTesting
	void setInterceptorBroadcasterForUnitTest(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@Nonnull
	public List<String> extractParamValuesAsStrings(RuntimeSearchParam theActiveSearchParam, IBaseResource theResource) {
		return mySearchParamExtractor.extractParamValuesAsStrings(theActiveSearchParam, theResource);
	}

	public void extractSearchParamComboUnique(ResourceTable theEntity, ResourceIndexedSearchParams theParams) {
		String resourceType = theEntity.getResourceType();
		Set<ResourceIndexedComboStringUnique> comboUniques = mySearchParamExtractor.extractSearchParamComboUnique(resourceType, theParams);
		theParams.myComboStringUniques.addAll(comboUniques);
		populateResourceTableForComboParams(theParams.myComboStringUniques, theEntity);
	}

	public void extractSearchParamComboNonUnique(ResourceTable theEntity, ResourceIndexedSearchParams theParams) {
		String resourceType = theEntity.getResourceType();
		Set<ResourceIndexedComboTokenNonUnique> comboNonUniques = mySearchParamExtractor.extractSearchParamComboNonUnique(resourceType, theParams);
		theParams.myComboTokenNonUnique.addAll(comboNonUniques);
		populateResourceTableForComboParams(theParams.myComboTokenNonUnique, theEntity);
	}

	static void handleWarnings(RequestDetails theRequestDetails, IInterceptorBroadcaster theInterceptorBroadcaster, ISearchParamExtractor.SearchParamSet<?> theSearchParamSet) {
		if (theSearchParamSet.getWarnings().isEmpty()) {
			return;
		}

		// If extraction generated any warnings, broadcast an error
		if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_WARNING, theInterceptorBroadcaster, theRequestDetails)) {
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
}

