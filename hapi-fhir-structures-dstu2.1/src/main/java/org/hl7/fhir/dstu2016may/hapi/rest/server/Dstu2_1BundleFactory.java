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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.dstu2016may.model.Bundle.SearchEntryMode;
import org.hl7.fhir.dstu2016may.model.DomainResource;
import org.hl7.fhir.dstu2016may.model.IdType;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Dstu2_1BundleFactory implements IVersionSpecificBundleFactory {

	private String myBase;
	private Bundle myBundle;
	private FhirContext myContext;

	public Dstu2_1BundleFactory(FhirContext theContext) {
		myContext = theContext;
	}

	@Override
	public void addResourcesToBundle(List<IBaseResource> theResult, BundleTypeEnum theBundleType, String theServerBase, BundleInclusionRule theBundleInclusionRule, Set<Include> theIncludes) {
		ensureBundle();

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
				for (Resource nextContained : ((DomainResource) next).getContained()) {
					if (isNotBlank(nextContained.getId())) {
						containedIds.add(nextContained.getId());
					}
				}
			}

			List<ResourceReferenceInfo> references = myContext.newTerser().getAllResourceReferences(next);
			do {
				List<IAnyResource> addedResourcesThisPass = new ArrayList<IAnyResource>();

				for (ResourceReferenceInfo nextRefInfo : references) {
					if (theBundleInclusionRule != null && !theBundleInclusionRule.shouldIncludeReferencedResource(nextRefInfo, theIncludes)) {
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
								String resName = myContext.getResourceType(nextRes);
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
			Resource nextAsResource = (Resource) next;
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

	@Override
	public void addRootPropertiesToBundle(String theId, @Nonnull BundleLinks theBundleLinks, Integer theTotalResults,
													  IPrimitiveType<Date> theLastUpdated) {
		ensureBundle();

		myBase = theBundleLinks.serverBase;

		if (myBundle.getIdElement().isEmpty()) {
			myBundle.setId(theId);
		}

		if (myBundle.getMeta().getLastUpdated() == null && theLastUpdated != null) {
			myBundle.getMeta().getLastUpdatedElement().setValueAsString(theLastUpdated.getValueAsString());
		}

		if (!hasLink(Constants.LINK_SELF, myBundle) && isNotBlank(theBundleLinks.getSelf())) {
			myBundle.addLink().setRelation(Constants.LINK_SELF).setUrl(theBundleLinks.getSelf());
		}
		if (!hasLink(Constants.LINK_NEXT, myBundle) && isNotBlank(theBundleLinks.getNext())) {
			myBundle.addLink().setRelation(Constants.LINK_NEXT).setUrl(theBundleLinks.getNext());
		}
		if (!hasLink(Constants.LINK_PREVIOUS, myBundle) && isNotBlank(theBundleLinks.getPrev())) {
			myBundle.addLink().setRelation(Constants.LINK_PREVIOUS).setUrl(theBundleLinks.getPrev());
		}

		addTotalResultsToBundle(theTotalResults, theBundleLinks.bundleType);
	}

	@Override
	public void addTotalResultsToBundle(Integer theTotalResults, BundleTypeEnum theBundleType) {
		ensureBundle();

		if (myBundle.getIdElement().isEmpty()) {
			myBundle.setId(UUID.randomUUID().toString());
		}

		if (myBundle.getTypeElement().isEmpty() && theBundleType != null) {
			myBundle.getTypeElement().setValueAsString(theBundleType.getCode());
		}

		if (myBundle.getTotalElement().isEmpty() && theTotalResults != null) {
			myBundle.getTotalElement().setValue(theTotalResults);
		}
	}

	private void ensureBundle() {
		if (myBundle == null) {
			myBundle = new Bundle();
		}
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
	public void initializeWithBundleResource(IBaseResource theBundle) {
		myBundle = (Bundle) theBundle;
	}

	private IIdType populateBundleEntryFullUrl(IBaseResource next, BundleEntryComponent entry) {
		IIdType idElement = null;
		if (next.getIdElement().hasBaseUrl()) {
			idElement = next.getIdElement();
			entry.setFullUrl(idElement.toVersionless().getValue());
		} else {
			if (isNotBlank(myBase) && next.getIdElement().hasIdPart()) {
				idElement = next.getIdElement();
				idElement = idElement.withServerBase(myBase, myContext.getResourceType(next));
				entry.setFullUrl(idElement.toVersionless().getValue());
			}
		}
		return idElement;
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
