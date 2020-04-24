package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	@Autowired(required = false)
	private IResourceLinkResolver myResourceLinkResolver;

	/**
	 * This method is responsible for scanning a resource for all of the search parameter instances. I.e. for all search parameters defined for
	 * a given resource type, it extracts the associated indexes and populates {@literal theParams}.
	 */
	public void extractFromResource(RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, Date theUpdateTime, boolean theFailOnInvalidReference) {
		IBaseResource resource = normalizeResource(theResource);

		// All search parameter types except Reference
		extractSearchIndexParameters(theRequestDetails, theParams, resource, theEntity);

		// Reference search parameters
		extractResourceLinks(theParams, theEntity, resource, theUpdateTime, theFailOnInvalidReference, theRequestDetails);

		theParams.setUpdatedTime(theUpdateTime);
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
		for (BaseResourceIndexedSearchParam next : extractSearchParamTokens(theResource)) {
			if (next instanceof ResourceIndexedSearchParamToken) {
				theParams.myTokenParams.add((ResourceIndexedSearchParamToken) next);
			} else if (next instanceof ResourceIndexedSearchParamCoords) {
				theParams.myCoordsParams.add((ResourceIndexedSearchParamCoords) next);
			} else {
				theParams.myStringParams.add((ResourceIndexedSearchParamString) next);
			}
		}

		// Specials
		for (BaseResourceIndexedSearchParam next : extractSearchParamSpecial(theResource)) {
			if (next instanceof ResourceIndexedSearchParamCoords) {
				theParams.myCoordsParams.add((ResourceIndexedSearchParamCoords) next);
			}
		}

		// Do this after, because we add to strings during both string and token processing
		populateResourceTable(theParams.myNumberParams, theEntity);
		populateResourceTable(theParams.myQuantityParams, theEntity);
		populateResourceTable(theParams.myDateParams, theEntity);
		populateResourceTable(theParams.myUriParams, theEntity);
		populateResourceTable(theParams.myTokenParams, theEntity);
		populateResourceTable(theParams.myStringParams, theEntity);
		populateResourceTable(theParams.myCoordsParams, theEntity);

	}

	/**
	 * This is a bit hacky, but if someone has manually populated a resource (ie. my working directly with the model
	 * as opposed to by parsing a serialized instance) it's possible that they have put in contained resources
	 * using {@link IBaseReference#setResource(IBaseResource)}, and those contained resources have not yet
	 * ended up in the Resource.contained array, meaning that FHIRPath expressions won't be able to find them.
	 *
	 * As a result, we to a serialize-and-parse to normalize the object. This really only affects people who
	 * are calling the JPA DAOs directly, but there are a few of those...
	 */
	private IBaseResource normalizeResource(IBaseResource theResource) {
		IParser parser = myContext.newJsonParser().setPrettyPrint(false);
		theResource = parser.parseResource(parser.encodeResourceToString(theResource));
		return theResource;
	}

	private void extractResourceLinks(ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, Date theUpdateTime, boolean theFailOnInvalidReference, RequestDetails theRequest) {
		String resourceName = myContext.getResourceDefinition(theResource).getName();

		ISearchParamExtractor.SearchParamSet<PathAndRef> refs = mySearchParamExtractor.extractResourceLinks(theResource);
		SearchParamExtractorService.handleWarnings(theRequest, myInterceptorBroadcaster, refs);

		Map<String, IResourceLookup> resourceIdToResolvedTarget = new HashMap<>();
		for (PathAndRef nextPathAndRef : refs) {
			RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(resourceName, nextPathAndRef.getSearchParamName());
			extractResourceLinks(theParams, theEntity, theUpdateTime, searchParam, nextPathAndRef, theFailOnInvalidReference, theRequest, resourceIdToResolvedTarget);
		}

		theEntity.setHasLinks(theParams.myLinks.size() > 0);
	}

	private void extractResourceLinks(ResourceIndexedSearchParams theParams, ResourceTable theEntity, Date theUpdateTime, RuntimeSearchParam theRuntimeSearchParam, PathAndRef thePathAndRef, boolean theFailOnInvalidReference, RequestDetails theRequest, Map<String, IResourceLookup> theResourceIdToResolvedTarget) {
		IBaseReference nextReference = thePathAndRef.getRef();
		IIdType nextId = nextReference.getReferenceElement();
		String path = thePathAndRef.getPath();

		/*
		 * This can only really happen if the DAO is being called
		 * programmatically with a Bundle (not through the FHIR REST API)
		 * but Smile does this
		 */
		if (nextId.isEmpty() && nextReference.getResource() != null) {
			nextId = nextReference.getResource().getIdElement();
		}

		theParams.myPopulatedResourceLinkParameters.add(thePathAndRef.getSearchParamName());

		boolean canonical = thePathAndRef.isCanonical();
		if (LogicalReferenceHelper.isLogicalReference(myModelConfig, nextId) || canonical) {
			String value = nextId.getValue();
			ResourceLink resourceLink = ResourceLink.forLogicalReference(thePathAndRef.getPath(), theEntity, value, theUpdateTime);
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
				throw new InvalidRequestException(msg);
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
				throw new InvalidRequestException(msg);
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
				throw new InvalidRequestException(msg);
			} else {
				ResourceLink resourceLink = ResourceLink.forAbsoluteReference(thePathAndRef.getPath(), theEntity, nextId, theUpdateTime);
				if (theParams.myLinks.add(resourceLink)) {
					ourLog.debug("Indexing remote resource reference URL: {}", nextId);
				}
				return;
			}
		}

		Class<? extends IBaseResource> type = resourceDefinition.getImplementingClass();
		String id = nextId.getIdPart();
		if (StringUtils.isBlank(id)) {
			String msg = "Invalid resource reference found at path[" + path + "] - Does not contain resource ID - " + nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}

		ResourceLink resourceLink;
		if (theFailOnInvalidReference) {

			myResourceLinkResolver.validateTypeOrThrowException(type);
			resourceLink = resolveTargetAndCreateResourceLinkOrReturnNull(theEntity, theUpdateTime, theRuntimeSearchParam, path, thePathAndRef, nextId, typeString, type, nextReference, theRequest, theResourceIdToResolvedTarget);
			if (resourceLink == null) {
				return;
			}

		} else {

			ResourceTable target;
			target = new ResourceTable();
			target.setResourceType(typeString);
			resourceLink = ResourceLink.forLocalReference(thePathAndRef.getPath(), theEntity, typeString, null, nextId.getIdPart(), theUpdateTime);

		}

		theParams.myLinks.add(resourceLink);
	}

	private ResourceLink resolveTargetAndCreateResourceLinkOrReturnNull(ResourceTable theEntity, Date theUpdateTime, RuntimeSearchParam nextSpDef, String theNextPathsUnsplit, PathAndRef nextPathAndRef, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, IBaseReference theReference, RequestDetails theRequest, Map<String, IResourceLookup> theResourceIdToResolvedTarget) {
		/*
		 * We keep a cache of resolved target resources. This is good since for some resource types, there
		 * are multiple search parameters that map to the same element path within a resource (e.g.
		 * Observation:patient and Observation.subject and we don't want to force a resolution of the
		 * target any more times than we have to.
		 */

		IResourceLookup targetResource = theResourceIdToResolvedTarget.get(theNextId.getValue());
		if (targetResource == null) {
			targetResource = myResourceLinkResolver.findTargetResource(nextSpDef, theNextPathsUnsplit, theNextId, theTypeString, theType, theReference, theRequest);
		}

		if (targetResource == null) {
			return null;
		}

		theResourceIdToResolvedTarget.put(theNextId.getValue(), targetResource);

		String targetResourceType = targetResource.getResourceType();
		Long targetResourcePid = targetResource.getResourceId();
		String targetResourceIdPart = theNextId.getIdPart();
		return ResourceLink.forLocalReference(nextPathAndRef.getPath(), theEntity, targetResourceType, targetResourcePid, targetResourceIdPart, theUpdateTime);
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
			JpaInterceptorBroadcaster.doCallHooks(theInterceptorBroadcaster, theRequestDetails, Pointcut.JPA_PERFTRACE_WARNING, params);
		}
	}

	private void populateResourceTable(Collection<? extends BaseResourceIndexedSearchParam> theParams, ResourceTable theResourceTable) {
		for (BaseResourceIndexedSearchParam next : theParams) {
			next.setResource(theResourceTable);
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
	void setInterceptorBroadcasterForUnitTest(IInterceptorBroadcaster theJpaInterceptorBroadcaster) {
		myInterceptorBroadcaster = theJpaInterceptorBroadcaster;
	}
}

