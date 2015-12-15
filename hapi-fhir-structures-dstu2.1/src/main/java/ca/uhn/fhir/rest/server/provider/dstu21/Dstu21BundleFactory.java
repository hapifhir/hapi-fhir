package ca.uhn.fhir.rest.server.provider.dstu21;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2.1 (FHIR v1.1.x)
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.dstu21.resource.Bundle;
import ca.uhn.fhir.model.dstu21.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu21.resource.Bundle.Link;
import ca.uhn.fhir.model.dstu21.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu21.valueset.SearchEntryModeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.BundleInclusionRule;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServer;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ResourceReferenceInfo;

public class Dstu21BundleFactory implements IVersionSpecificBundleFactory {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Dstu21BundleFactory.class);
	private Bundle myBundle;
	private FhirContext myContext;
	private String myBase;

	public Dstu21BundleFactory(FhirContext theContext) {
		myContext = theContext;
	}

	private void addResourcesForSearch(List<? extends IBaseResource> theResult) {
		List<IBaseResource> includedResources = new ArrayList<IBaseResource>();
		Set<IIdType> addedResourceIds = new HashSet<IIdType>();

		for (IBaseResource next : theResult) {
			if (next.getIdElement().isEmpty() == false) {
				addedResourceIds.add(next.getIdElement());
			}
		}

		for (IBaseResource nextBaseRes : theResult) {
			IResource next = (IResource) nextBaseRes;
			Set<String> containedIds = new HashSet<String>();
			for (IResource nextContained : next.getContained().getContainedResources()) {
				if (nextContained.getId().isEmpty() == false) {
					containedIds.add(nextContained.getId().getValue());
				}
			}

			if (myContext.getNarrativeGenerator() != null) {
				String title = myContext.getNarrativeGenerator().generateTitle(next);
				ourLog.trace("Narrative generator created title: {}", title);
				if (StringUtils.isNotBlank(title)) {
					ResourceMetadataKeyEnum.TITLE.put(next, title);
				}
			} else {
				ourLog.trace("No narrative generator specified");
			}

			List<BaseResourceReferenceDt> references = myContext.newTerser().getAllPopulatedChildElementsOfType(next, BaseResourceReferenceDt.class);
			do {
				List<IResource> addedResourcesThisPass = new ArrayList<IResource>();

				for (BaseResourceReferenceDt nextRef : references) {
					IResource nextRes = (IResource) nextRef.getResource();
					if (nextRes != null) {
						if (nextRes.getId().hasIdPart()) {
							if (containedIds.contains(nextRes.getId().getValue())) {
								// Don't add contained IDs as top level resources
								continue;
							}

							IdDt id = nextRes.getId();
							if (id.hasResourceType() == false) {
								String resName = myContext.getResourceDefinition(nextRes).getName();
								id = id.withResourceType(resName);
							}

							if (!addedResourceIds.contains(id)) {
								addedResourceIds.add(id);
								addedResourcesThisPass.add(nextRes);
							}

						}
					}
				}

				// Linked resources may themselves have linked resources
				references = new ArrayList<BaseResourceReferenceDt>();
				for (IResource iResource : addedResourcesThisPass) {
					List<BaseResourceReferenceDt> newReferences = myContext.newTerser().getAllPopulatedChildElementsOfType(iResource, BaseResourceReferenceDt.class);
					references.addAll(newReferences);
				}

				includedResources.addAll(addedResourcesThisPass);

			} while (references.isEmpty() == false);

			Entry entry = myBundle.addEntry().setResource(next);
			if (next.getId().hasBaseUrl()) {
				entry.setFullUrl(next.getId().getValue());
			}
			BundleEntryTransactionMethodEnum httpVerb = ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(next);
			if (httpVerb != null) {
				entry.getRequest().getMethodElement().setValueAsString(httpVerb.getCode());
			}
		}

		/*
		 * Actually add the resources to the bundle
		 */
		for (IBaseResource next : includedResources) {
			Entry entry = myBundle.addEntry();
			entry.setResource((IResource) next).getSearch().setMode(SearchEntryModeEnum.INCLUDE);
			if (next.getIdElement().hasBaseUrl()) {
				entry.setFullUrl(next.getIdElement().getValue());
			}
		}
	}

	@Override
	public void addResourcesToBundle(List<IBaseResource> theResult, BundleTypeEnum theBundleType, String theServerBase, BundleInclusionRule theBundleInclusionRule, Set<Include> theIncludes) {
		if (myBundle == null) {
			myBundle = new Bundle();
		}

		List<IResource> includedResources = new ArrayList<IResource>();
		Set<IdDt> addedResourceIds = new HashSet<IdDt>();

		for (IBaseResource next : theResult) {
			if (next.getIdElement().isEmpty() == false) {
				addedResourceIds.add((IdDt) next.getIdElement());
			}
		}

		for (IBaseResource nextBaseRes : theResult) {
			IResource next = (IResource) nextBaseRes;

			Set<String> containedIds = new HashSet<String>();
			for (IResource nextContained : next.getContained().getContainedResources()) {
				if (nextContained.getId().isEmpty() == false) {
					containedIds.add(nextContained.getId().getValue());
				}
			}

			if (myContext.getNarrativeGenerator() != null) {
				String title = myContext.getNarrativeGenerator().generateTitle(next);
				ourLog.trace("Narrative generator created title: {}", title);
				if (StringUtils.isNotBlank(title)) {
					ResourceMetadataKeyEnum.TITLE.put(next, title);
				}
			} else {
				ourLog.trace("No narrative generator specified");
			}

			List<ResourceReferenceInfo> references = myContext.newTerser().getAllResourceReferences(next);
			do {
				List<IResource> addedResourcesThisPass = new ArrayList<IResource>();

				for (ResourceReferenceInfo nextRefInfo : references) {
					if (!theBundleInclusionRule.shouldIncludeReferencedResource(nextRefInfo, theIncludes))
						continue;

					IResource nextRes = (IResource) nextRefInfo.getResourceReference().getResource();
					if (nextRes != null) {
						if (nextRes.getId().hasIdPart()) {
							if (containedIds.contains(nextRes.getId().getValue())) {
								// Don't add contained IDs as top level resources
								continue;
							}

							IdDt id = nextRes.getId();
							if (id.hasResourceType() == false) {
								String resName = myContext.getResourceDefinition(nextRes).getName();
								id = id.withResourceType(resName);
							}

							if (!addedResourceIds.contains(id)) {
								addedResourceIds.add(id);
								addedResourcesThisPass.add(nextRes);
							}

						}
					}
				}

				includedResources.addAll(addedResourcesThisPass);

				// Linked resources may themselves have linked resources
				references = new ArrayList<ResourceReferenceInfo>();
				for (IResource iResource : addedResourcesThisPass) {
					List<ResourceReferenceInfo> newReferences = myContext.newTerser().getAllResourceReferences(iResource);
					references.addAll(newReferences);
				}
			} while (references.isEmpty() == false);

			Entry entry = myBundle.addEntry().setResource(next);
			BundleEntryTransactionMethodEnum httpVerb = ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(next);
			if (httpVerb != null) {
				entry.getRequest().getMethodElement().setValueAsString(httpVerb.getCode());
			}
			populateBundleEntryFullUrl(next, entry);
			
			BundleEntrySearchModeEnum searchMode = ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(next);
			if (searchMode != null) {
				entry.getSearch().getModeElement().setValue(searchMode.getCode());
			}
		}

		/*
		 * Actually add the resources to the bundle
		 */
		for (IResource next : includedResources) {
			Entry entry = myBundle.addEntry();
			entry.setResource(next).getSearch().setMode(SearchEntryModeEnum.INCLUDE);
			populateBundleEntryFullUrl(next, entry);
		}

	}

	private void populateBundleEntryFullUrl(IResource next, Entry entry) {
		if (next.getId().hasBaseUrl()) {
			entry.setFullUrl(next.getId().toVersionless().getValue());
		} else {
			if (isNotBlank(myBase) && next.getId().hasIdPart()) {
				IdDt id = next.getId().toVersionless();
				id = id.withServerBase(myBase, myContext.getResourceDefinition(next).getName());
				entry.setFullUrl(id.getValue());
			}
		}
	}

	@Override
	public void addRootPropertiesToBundle(String theAuthor, String theServerBase, String theCompleteUrl, Integer theTotalResults, BundleTypeEnum theBundleType, IPrimitiveType<Date> theLastUpdated) {

		myBase = theServerBase;
		
		if (myBundle.getId().isEmpty()) {
			myBundle.setId(UUID.randomUUID().toString());
		}

		if (ResourceMetadataKeyEnum.UPDATED.get(myBundle) == null) {
			ResourceMetadataKeyEnum.UPDATED.put(myBundle, (InstantDt) theLastUpdated);
		}

		if (!hasLink(Constants.LINK_SELF, myBundle) && isNotBlank(theCompleteUrl)) {
			myBundle.addLink().setRelation("self").setUrl(theCompleteUrl);
		}

		if (myBundle.getTypeElement().isEmpty() && theBundleType != null) {
			myBundle.getTypeElement().setValueAsString(theBundleType.getCode());
		}

		if (myBundle.getTotalElement().isEmpty() && theTotalResults != null) {
			myBundle.getTotalElement().setValue(theTotalResults);
		}
	}

	@Override
	public ca.uhn.fhir.model.api.Bundle getDstu1Bundle() {
		return null;
	}

	@Override
	public IResource getResourceBundle() {
		return myBundle;
	}

	private boolean hasLink(String theLinkType, Bundle theBundle) {
		for (Link next : theBundle.getLink()) {
			if (theLinkType.equals(next.getRelation())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void initializeBundleFromBundleProvider(IRestfulServer theServer, IBundleProvider theResult, EncodingEnum theResponseEncoding, String theServerBase, String theCompleteUrl,
			boolean thePrettyPrint, int theOffset, Integer theLimit, String theSearchId, BundleTypeEnum theBundleType, Set<Include> theIncludes) {
		myBase = theServerBase;
		
		int numToReturn;
		String searchId = null;
		List<IBaseResource> resourceList;
		if (theServer.getPagingProvider() == null) {
			numToReturn = theResult.size();
			if (numToReturn > 0) {
				resourceList = theResult.getResources(0, numToReturn);
			} else {
				resourceList = Collections.emptyList();
			}
			RestfulServerUtils.validateResourceListNotNull(resourceList);

		} else {
			IPagingProvider pagingProvider = theServer.getPagingProvider();
			if (theLimit == null) {
				numToReturn = pagingProvider.getDefaultPageSize();
			} else {
				numToReturn = Math.min(pagingProvider.getMaximumPageSize(), theLimit);
			}

			numToReturn = Math.min(numToReturn, theResult.size() - theOffset);
			if (numToReturn > 0) {
				resourceList = theResult.getResources(theOffset, numToReturn + theOffset);
			} else {
				resourceList = Collections.emptyList();
			}
			RestfulServerUtils.validateResourceListNotNull(resourceList);

			if (theSearchId != null) {
				searchId = theSearchId;
			} else {
				if (theResult.size() > numToReturn) {
					searchId = pagingProvider.storeResultList(theResult);
					Validate.notNull(searchId, "Paging provider returned null searchId");
				}
			}
		}

		for (IBaseResource next : resourceList) {
			if (next.getIdElement() == null || next.getIdElement().isEmpty()) {
				if (!(next instanceof BaseOperationOutcome)) {
					throw new InternalErrorException("Server method returned resource of type[" + next.getClass().getSimpleName() + "] with no ID specified (IResource#setId(IdDt) must be called)");
				}
			}
		}

		if (theServer.getAddProfileTag() != AddProfileTagEnum.NEVER) {
			for (IBaseResource nextRes : resourceList) {
				RuntimeResourceDefinition def = theServer.getFhirContext().getResourceDefinition(nextRes);
				if (theServer.getAddProfileTag() == AddProfileTagEnum.ALWAYS || !def.isStandardProfile()) {
					RestfulServerUtils.addProfileToBundleEntry(theServer.getFhirContext(), nextRes, theServerBase);
				}
			}
		}

		addResourcesToBundle(new ArrayList<IBaseResource>(resourceList), theBundleType, theServerBase, theServer.getBundleInclusionRule(), theIncludes);
		addRootPropertiesToBundle(null, theServerBase, theCompleteUrl, theResult.size(), theBundleType, theResult.getPublished());

		if (theServer.getPagingProvider() != null) {
			int limit;
			limit = theLimit != null ? theLimit : theServer.getPagingProvider().getDefaultPageSize();
			limit = Math.min(limit, theServer.getPagingProvider().getMaximumPageSize());

			if (searchId != null) {
				if (theOffset + numToReturn < theResult.size()) {
					myBundle.addLink().setRelation(Constants.LINK_NEXT)
							.setUrl(RestfulServerUtils.createPagingLink(theIncludes, theServerBase, searchId, theOffset + numToReturn, numToReturn, theResponseEncoding, thePrettyPrint, theBundleType));
				}
				if (theOffset > 0) {
					int start = Math.max(0, theOffset - limit);
					myBundle.addLink().setRelation(Constants.LINK_PREVIOUS)
							.setUrl(RestfulServerUtils.createPagingLink(theIncludes, theServerBase, searchId, start, limit, theResponseEncoding, thePrettyPrint, theBundleType));
				}
			}
		}
	}

	@Override
	public void initializeBundleFromResourceList(String theAuthor, List<? extends IBaseResource> theResources, String theServerBase, String theCompleteUrl, int theTotalResults,
			BundleTypeEnum theBundleType) {
		myBundle = new Bundle();

		myBundle.setId(UUID.randomUUID().toString());

		ResourceMetadataKeyEnum.PUBLISHED.put(myBundle, InstantDt.withCurrentTime());

		myBundle.addLink().setRelation(Constants.LINK_FHIR_BASE).setUrl(theServerBase);
		myBundle.addLink().setRelation(Constants.LINK_SELF).setUrl(theCompleteUrl);
		myBundle.getTypeElement().setValueAsString(theBundleType.getCode());

		if (theBundleType.equals(BundleTypeEnum.TRANSACTION)) {
			for (IBaseResource nextBaseRes : theResources) {
				IResource next = (IResource) nextBaseRes;
				Entry nextEntry = myBundle.addEntry();

				nextEntry.setResource(next);
				if (next.getId().isEmpty()) {
					nextEntry.getRequest().setMethod(HTTPVerbEnum.POST);
				} else {
					nextEntry.getRequest().setMethod(HTTPVerbEnum.PUT);
					if (next.getId().isAbsolute()) {
						nextEntry.getRequest().setUrl(next.getId());
					} else {
						String resourceType = myContext.getResourceDefinition(next).getName();
						nextEntry.getRequest().setUrl(new IdDt(theServerBase, resourceType, next.getId().getIdPart(), next.getId().getVersionIdPart()).getValue());
					}
				}
			}
		} else {
			addResourcesForSearch(theResources);
		}

		myBundle.getTotalElement().setValue(theTotalResults);
	}

	@Override
	public void initializeWithBundleResource(IBaseResource theBundle) {
		myBundle = (Bundle) theBundle;
	}

	@Override
	public List<IBaseResource> toListOfResources() {
		ArrayList<IBaseResource> retVal = new ArrayList<IBaseResource>();
		for (Entry next : myBundle.getEntry()) {
			if (next.getResource() != null) {
				retVal.add(next.getResource());
			} else if (next.getResponse().getLocationElement().isEmpty() == false) {
				IdDt id = new IdDt(next.getResponse().getLocation());
				String resourceType = id.getResourceType();
				if (isNotBlank(resourceType)) {
					IResource res = (IResource) myContext.getResourceDefinition(resourceType).newInstance();
					res.setId(id);
					retVal.add(res);
				}
			}
		}
		return retVal;
	}

}
