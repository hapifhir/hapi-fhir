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
package org.hl7.fhir.r4.hapi.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleEntryTransactionMethodEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.r4.model.Bundle.SearchEntryMode;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("Duplicates")
public class R4BundleFactory implements IVersionSpecificBundleFactory {
	private String myBase;
	private Bundle myBundle;
	private final FhirContext myContext;

	public R4BundleFactory(FhirContext theContext) {
		myContext = theContext;
	}

	@Override
	public void addResourcesToBundle(
			List<IBaseResource> theResult,
			BundleTypeEnum theBundleType,
			String theServerBase,
			BundleInclusionRule theBundleInclusionRule,
			Set<Include> theIncludes) {
		ensureBundle();

		List<IAnyResource> includedResources = new ArrayList<>();
		Set<IIdType> addedResourceIds = new HashSet<>();

		for (IBaseResource next : theResult) {
			if (!next.getIdElement().isEmpty()) {
				addedResourceIds.add(next.getIdElement());
			}
		}

		for (IBaseResource next : theResult) {

			Set<String> containedIds = new HashSet<>();

			if (next instanceof DomainResource) {
				for (Resource nextContained : ((DomainResource) next).getContained()) {
					if (isNotBlank(nextContained.getId())) {
						containedIds.add(nextContained.getId());
					}
				}
			}

			List<ResourceReferenceInfo> references = myContext.newTerser().getAllResourceReferences(next);
			do {
				List<IAnyResource> addedResourcesThisPass = new ArrayList<>();

				for (ResourceReferenceInfo nextRefInfo : references) {
					if (theBundleInclusionRule != null
							&& !theBundleInclusionRule.shouldIncludeReferencedResource(nextRefInfo, theIncludes)) {
						continue;
					}

					IAnyResource nextRes =
							(IAnyResource) nextRefInfo.getResourceReference().getResource();
					if (nextRes != null) {
						if (nextRes.getIdElement().hasIdPart()) {
							if (containedIds.contains(nextRes.getIdElement().getValue())) {
								// Don't add contained IDs as top level resources
								continue;
							}

							IIdType id = nextRes.getIdElement();
							if (!id.hasResourceType()) {
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
				references = new ArrayList<>();
				for (IAnyResource iResource : addedResourcesThisPass) {
					List<ResourceReferenceInfo> newReferences =
							myContext.newTerser().getAllResourceReferences(iResource);
					references.addAll(newReferences);
				}
			} while (!references.isEmpty());

			BundleEntryComponent entry = myBundle.addEntry().setResource((Resource) next);
			Resource nextAsResource = (Resource) next;
			IIdType id = populateBundleEntryFullUrl(next, entry);

			// Populate Request
			BundleEntryTransactionMethodEnum httpVerb =
					ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(nextAsResource);
			if (httpVerb != null) {
				entry.getRequest().getMethodElement().setValueAsString(httpVerb.name());
				if (id != null) {
					entry.getRequest().setUrl(id.toUnqualified().getValue());
				}
			}
			if (BundleEntryTransactionMethodEnum.DELETE.equals(httpVerb)) {
				entry.setResource(null);
			}

			// Populate Bundle.entry.response
			if (theBundleType != null) {
				switch (theBundleType) {
					case BATCH_RESPONSE:
					case TRANSACTION_RESPONSE:
					case HISTORY:
						if (id != null) {
							String version = id.getVersionIdPart();
							if ("1".equals(version)) {
								entry.getResponse().setStatus("201 Created");
							} else if (isNotBlank(version)) {
								entry.getResponse().setStatus("200 OK");
							}
							if (isNotBlank(version)) {
								entry.getResponse().setEtag(RestfulServerUtils.createEtag(version));
							}
						}
						break;
				}
			}

			// Populate Bundle.entry.search
			BundleEntrySearchModeEnum searchMode = ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(nextAsResource);
			if (searchMode != null) {
				entry.getSearch().getModeElement().setValueAsString(searchMode.getCode());
			}
			BigDecimal searchScore = ResourceMetadataKeyEnum.ENTRY_SEARCH_SCORE.get(nextAsResource);
			if (searchScore != null) {
				entry.getSearch().getScoreElement().setValue(searchScore);
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
	public void addRootPropertiesToBundle(
			String theId,
			@Nonnull BundleLinks theBundleLinks,
			Integer theTotalResults,
			IPrimitiveType<Date> theLastUpdated) {
		ensureBundle();

		myBase = theBundleLinks.serverBase;

		if (myBundle.getIdElement().isEmpty()) {
			myBundle.setId(theId);
		}

		if (myBundle.getMeta().getLastUpdated() == null && theLastUpdated != null) {
			myBundle.getMeta().getLastUpdatedElement().setValueAsString(theLastUpdated.getValueAsString());
		}

		if (hasNoLinkOfType(Constants.LINK_SELF, myBundle) && isNotBlank(theBundleLinks.getSelf())) {
			myBundle.addLink().setRelation(Constants.LINK_SELF).setUrl(theBundleLinks.getSelf());
		}
		if (hasNoLinkOfType(Constants.LINK_NEXT, myBundle) && isNotBlank(theBundleLinks.getNext())) {
			myBundle.addLink().setRelation(Constants.LINK_NEXT).setUrl(theBundleLinks.getNext());
		}
		if (hasNoLinkOfType(Constants.LINK_PREVIOUS, myBundle) && isNotBlank(theBundleLinks.getPrev())) {
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

	private boolean hasNoLinkOfType(String theLinkType, Bundle theBundle) {
		for (BundleLinkComponent next : theBundle.getLink()) {
			if (theLinkType.equals(next.getRelation())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void initializeWithBundleResource(IBaseResource theBundle) {
		myBundle = (Bundle) theBundle;
	}

	@Nullable
	private IIdType populateBundleEntryFullUrl(IBaseResource theResource, BundleEntryComponent theEntry) {
		final IIdType idElement;
		if (theResource.getIdElement().hasBaseUrl()) {
			idElement = theResource.getIdElement();
			theEntry.setFullUrl(idElement.toVersionless().getValue());
		} else {
			if (isNotBlank(myBase) && theResource.getIdElement().hasIdPart()) {
				idElement = theResource.getIdElement().withServerBase(myBase, myContext.getResourceType(theResource));
				theEntry.setFullUrl(idElement.toVersionless().getValue());
			} else {
				idElement = null;
			}
		}
		return idElement;
	}

	@Override
	public List<IBaseResource> toListOfResources() {
		ArrayList<IBaseResource> retVal = new ArrayList<>();
		for (BundleEntryComponent next : myBundle.getEntry()) {
			if (next.getResource() != null) {
				retVal.add(next.getResource());
			} else if (!next.getResponse().getLocationElement().isEmpty()) {
				IdType id = new IdType(next.getResponse().getLocation());
				String resourceType = id.getResourceType();
				if (isNotBlank(resourceType)) {
					IAnyResource res = (IAnyResource)
							myContext.getResourceDefinition(resourceType).newInstance();
					res.setId(id);
					retVal.add(res);
				}
			}
		}
		return retVal;
	}
}
