package org.hl7.fhir.dstu2016may.hapi.rest.server;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v1.0.0)
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

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.dstu2016may.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu2016may.model.Bundle.SearchEntryMode;
import org.hl7.fhir.dstu2016may.model.DomainResource;
import org.hl7.fhir.dstu2016may.model.IdType;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
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

public class Dstu2_1BundleFactory implements IVersionSpecificBundleFactory {

	private Bundle myBundle;
	private FhirContext myContext;
	private String myBase;

	public Dstu2_1BundleFactory(FhirContext theContext) {
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
			Resource next = (Resource) nextBaseRes;
			Set<String> containedIds = new HashSet<String>();
			if (next instanceof DomainResource) {
				for (Resource nextContained : ((DomainResource)next).getContained()) {
					if (nextContained.getIdElement().isEmpty() == false) {
						containedIds.add(nextContained.getIdElement().getValue());
					}
				}
			}
			
			List<IBaseReference> references = myContext.newTerser().getAllPopulatedChildElementsOfType(next, IBaseReference.class);
			do {
				List<IAnyResource> addedResourcesThisPass = new ArrayList<IAnyResource>();

				for (IBaseReference nextRef : references) {
					IAnyResource nextRes = (IAnyResource) nextRef.getResource();
					if (nextRes != null) {
						if (nextRes.getIdElement().hasIdPart()) {
							if (containedIds.contains(nextRes.getIdElement().getValue())) {
								// Don't add contained IDs as top level resources
								continue;
							}

							IIdType id = nextRes.getIdElement();
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
				references = new ArrayList<IBaseReference>();
				for (IAnyResource iResource : addedResourcesThisPass) {
					List<IBaseReference> newReferences = myContext.newTerser().getAllPopulatedChildElementsOfType(iResource, IBaseReference.class);
					references.addAll(newReferences);
				}

				includedResources.addAll(addedResourcesThisPass);

			} while (references.isEmpty() == false);

			BundleEntryComponent entry = myBundle.addEntry().setResource(next);
			if (next.getIdElement().hasBaseUrl()) {
				entry.setFullUrl(next.getId());
			}

			String httpVerb = ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(next);
			if (httpVerb != null) {
				entry.getRequest().getMethodElement().setValueAsString(httpVerb);
				entry.getRequest().getUrlElement().setValue(next.getId());
			}
		}

		/*
		 * Actually add the resources to the bundle
		 */
		for (IBaseResource next : includedResources) {
			BundleEntryComponent entry = myBundle.addEntry();
			entry.setResource((Resource) next).getSearch().setMode(SearchEntryMode.INCLUDE);
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

		List<IAnyResource> includedResources = new ArrayList<IAnyResource>();
		Set<IIdType> addedResourceIds = new HashSet<IIdType>();

		for (IBaseResource next : theResult) {
			if (next.getIdElement().isEmpty() == false) {
				addedResourceIds.add(next.getIdElement());
			}
		}

		for (IBaseResource next : theResult) {

			Set<String> containedIds = new HashSet<String>();
			
			if (next instanceof DomainResource) {
				for (Resource nextContained : ((DomainResource)next).getContained()) {
					if (isNotBlank(nextContained.getId())) {
						containedIds.add(nextContained.getId());
					}
				}
			}

			List<ResourceReferenceInfo> references = myContext.newTerser().getAllResourceReferences(next);
			do {
				List<IAnyResource> addedResourcesThisPass = new ArrayList<IAnyResource>();

				for (ResourceReferenceInfo nextRefInfo : references) {
					if (!theBundleInclusionRule.shouldIncludeReferencedResource(nextRefInfo, theIncludes)) {
						continue; 
					}

					IAnyResource nextRes = (IAnyResource) nextRefInfo.getResourceReference().getResource();
					if (nextRes != null) {
						if (nextRes.getIdElement().hasIdPart()) {
							if (containedIds.contains(nextRes.getIdElement().getValue())) {
								// Don't add contained IDs as top level resources
								continue;
							}

							IIdType id = nextRes.getIdElement();
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
				for (IAnyResource iResource : addedResourcesThisPass) {
					List<ResourceReferenceInfo> newReferences = myContext.newTerser().getAllResourceReferences(iResource);
					references.addAll(newReferences);
				}
			} while (references.isEmpty() == false);

			BundleEntryComponent entry = myBundle.addEntry().setResource((Resource) next);
			Resource nextAsResource = (Resource)next;
			IIdType id = populateBundleEntryFullUrl(next, entry);
			String httpVerb = ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(nextAsResource);
			if (httpVerb != null) {
				entry.getRequest().getMethodElement().setValueAsString(httpVerb);
				if (id != null) {
					entry.getRequest().setUrl(id.getValue());
				}
			}
			
			String searchMode = ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(nextAsResource);
			if (searchMode != null) {
				entry.getSearch().getModeElement().setValueAsString(searchMode);
			}
		}

		/*
		 * Actually add the resources to the bundle
		 */
		for (IAnyResource next : includedResources) {
			BundleEntryComponent entry = myBundle.addEntry();
			entry.setResource((Resource) next).getSearch().setMode(SearchEntryMode.INCLUDE);
			populateBundleEntryFullUrl(next, entry);
		}

	}

	private IIdType populateBundleEntryFullUrl(IBaseResource next, BundleEntryComponent entry) {
		IIdType idElement = null;
		if (next.getIdElement().hasBaseUrl()) {
			idElement = next.getIdElement();
			entry.setFullUrl(idElement.toVersionless().getValue());
		} else {
			if (isNotBlank(myBase) && next.getIdElement().hasIdPart()) {
				idElement = next.getIdElement();
				idElement = idElement.withServerBase(myBase, myContext.getResourceDefinition(next).getName());
				entry.setFullUrl(idElement.toVersionless().getValue());
			}
		}
		return idElement;
	}

	@Override
	public void addRootPropertiesToBundle(String theAuthor, String theServerBase, String theCompleteUrl, Integer theTotalResults, BundleTypeEnum theBundleType, IPrimitiveType<Date> theLastUpdated) {

		myBase = theServerBase;
		
		if (myBundle.getIdElement().isEmpty()) {
			myBundle.setId(UUID.randomUUID().toString());
		}

		if (myBundle.getMeta().getLastUpdated() == null && theLastUpdated != null) {
			myBundle.getMeta().getLastUpdatedElement().setValueAsString(theLastUpdated.getValueAsString());
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
	public IBaseResource getResourceBundle() {
		return myBundle;
	}

	private boolean hasLink(String theLinkType, Bundle theBundle) {
		for (BundleLinkComponent next : theBundle.getLink()) {
			if (theLinkType.equals(next.getRelation())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void initializeBundleFromBundleProvider(IRestfulServer<?> theServer, IBundleProvider theResult, EncodingEnum theResponseEncoding, String theServerBase, String theCompleteUrl,
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

		myBundle.getMeta().setLastUpdated(new Date());

		myBundle.addLink().setRelation(Constants.LINK_FHIR_BASE).setUrl(theServerBase);
		myBundle.addLink().setRelation(Constants.LINK_SELF).setUrl(theCompleteUrl);
		myBundle.getTypeElement().setValueAsString(theBundleType.getCode());

		if (theBundleType.equals(BundleTypeEnum.TRANSACTION)) {
			for (IBaseResource nextBaseRes : theResources) {
				Resource next = (Resource) nextBaseRes;
				BundleEntryComponent nextEntry = myBundle.addEntry();

				nextEntry.setResource(next);
				if (next.getIdElement().isEmpty()) {
					nextEntry.getRequest().setMethod(HTTPVerb.POST);
				} else {
					nextEntry.getRequest().setMethod(HTTPVerb.PUT);
					if (next.getIdElement().isAbsolute()) {
						nextEntry.getRequest().setUrl(next.getId());
					} else {
						String resourceType = myContext.getResourceDefinition(next).getName();
						nextEntry.getRequest().setUrl(new IdType(theServerBase, resourceType, next.getIdElement().getIdPart(), next.getIdElement().getVersionIdPart()).getValue());
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
		for (BundleEntryComponent next : myBundle.getEntry()) {
			if (next.getResource() != null) {
				retVal.add(next.getResource());
			} else if (next.getResponse().getLocationElement().isEmpty() == false) {
				IdType id = new IdType(next.getResponse().getLocation());
				String resourceType = id.getResourceType();
				if (isNotBlank(resourceType)) {
					IAnyResource res = (IAnyResource) myContext.getResourceDefinition(resourceType).newInstance();
					res.setId(id);
					retVal.add(res);
				}
			}
		}
		return retVal;
	}

}
