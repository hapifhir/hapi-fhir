package ca.uhn.fhir.rest.server.provider.dstu2;

/*
 * #%L
 * HAPI FHIR Structures - DSTU2 (FHIR v1.0.0)
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.*;

import org.hl7.fhir.instance.model.api.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Link;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.dstu2.valueset.SearchEntryModeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.*;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.util.ResourceReferenceInfo;

public class Dstu2BundleFactory implements IVersionSpecificBundleFactory {
	private Bundle myBundle;
	private FhirContext myContext;
	private String myBase;

	public Dstu2BundleFactory(FhirContext theContext) {
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
	public void addRootPropertiesToBundle(String theId, String theServerBase, String theLinkSelf, String theLinkPrev, String theLinkNext, Integer theTotalResults, BundleTypeEnum theBundleType,
													  IPrimitiveType<Date> theLastUpdated) {

		myBase = theServerBase;

		if (myBundle.getIdElement().isEmpty()) {
			myBundle.setId(theId);
		}
		if (myBundle.getId().isEmpty()) {
			myBundle.setId(UUID.randomUUID().toString());
		}

		if (ResourceMetadataKeyEnum.UPDATED.get(myBundle) == null) {
			ResourceMetadataKeyEnum.UPDATED.put(myBundle, (InstantDt) theLastUpdated);
		}

		if (!hasLink(Constants.LINK_SELF, myBundle) && isNotBlank(theLinkSelf)) {
			myBundle.addLink().setRelation(Constants.LINK_SELF).setUrl(theLinkSelf);
		}
		if (!hasLink(Constants.LINK_NEXT, myBundle) && isNotBlank(theLinkNext)) {
			myBundle.addLink().setRelation(Constants.LINK_NEXT).setUrl(theLinkNext);
		}
		if (!hasLink(Constants.LINK_PREVIOUS, myBundle) && isNotBlank(theLinkPrev)) {
			myBundle.addLink().setRelation(Constants.LINK_PREVIOUS).setUrl(theLinkPrev);
		}

		if (myBundle.getTypeElement().isEmpty() && theBundleType != null) {
			myBundle.getTypeElement().setValueAsString(theBundleType.getCode());
		}

		if (myBundle.getTotalElement().isEmpty() && theTotalResults != null) {
			myBundle.getTotalElement().setValue(theTotalResults);
		}
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
