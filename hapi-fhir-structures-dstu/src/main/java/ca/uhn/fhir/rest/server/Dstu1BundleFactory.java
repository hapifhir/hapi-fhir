package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR Structures - DSTU1 (FHIR v0.80)
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ResourceReferenceInfo;

public class Dstu1BundleFactory implements IVersionSpecificBundleFactory {

	private Bundle myBundle;
	private FhirContext myContext;

	public Dstu1BundleFactory(FhirContext theContext) {
		myContext = theContext;
	}

	private void addProfileIfNeeded(IRestfulServer<?> theServer, String theServerBase, IBaseResource nextRes) {
		RuntimeResourceDefinition def = theServer.getFhirContext().getResourceDefinition(nextRes);
		if (theServer.getAddProfileTag() == AddProfileTagEnum.ALWAYS || !def.isStandardType()) {
			TagList tl = ResourceMetadataKeyEnum.TAG_LIST.get((IResource) nextRes);
			if (tl == null) {
				tl = new TagList();
				ResourceMetadataKeyEnum.TAG_LIST.put((IResource) nextRes, tl);
			}

			RuntimeResourceDefinition nextDef = myContext.getResourceDefinition(nextRes);
			String profile = nextDef.getResourceProfile(theServerBase);
			if (isNotBlank(profile)) {
				tl.add(new Tag(Tag.HL7_ORG_PROFILE_TAG, profile, null));
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

			myBundle.addResource(next, myContext, theServerBase);
		}

		/*
		 * Actually add the resources to the bundle
		 */
		for (IResource next : includedResources) {
			BundleEntry entry = myBundle.addResource(next, myContext, theServerBase);
			if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
				if (entry.getSearchMode().isEmpty()) {
					entry.getSearchMode().setValueAsEnum(BundleEntrySearchModeEnum.INCLUDE);
				}
			}
		}

	}

	@Override
	public void addRootPropertiesToBundle(String theAuthor, String theServerBase, String theCompleteUrl, Integer theTotalResults, BundleTypeEnum theBundleType, IPrimitiveType<Date> theLastUpdated) {
		if (myBundle.getAuthorName().isEmpty()) {
			myBundle.getAuthorName().setValue(theAuthor);
		}

		if (theLastUpdated != null && myBundle.getUpdated().isEmpty() && isNotBlank(theLastUpdated.getValueAsString())) {
			myBundle.getUpdated().setValueAsString(theLastUpdated.getValueAsString());
		}

		if (myBundle.getBundleId().isEmpty()) {
			myBundle.getBundleId().setValue(UUID.randomUUID().toString());
		}

		if (myBundle.getLinkBase().isEmpty()) {
			myBundle.getLinkBase().setValue(theServerBase);
		}

		if (myBundle.getLinkSelf().isEmpty()) {
			myBundle.getLinkSelf().setValue(theCompleteUrl);
		}

		if (theBundleType != null && myBundle.getType().isEmpty()) {
			myBundle.getType().setValueAsString(theBundleType.getCode());
		}

		if (myBundle.getTotalResults().isEmpty() && theTotalResults != null) {
			myBundle.getTotalResults().setValue(theTotalResults);
		}
	}

	@Override
	public Bundle getDstu1Bundle() {
		return myBundle;
	}

	@Override
	public IResource getResourceBundle() {
		return null;
	}

	@Override
	public void initializeBundleFromBundleProvider(IRestfulServer<?> theServer, IBundleProvider theResult, EncodingEnum theResponseEncoding, String theServerBase, String theCompleteUrl,
			boolean thePrettyPrint, int theOffset, Integer theLimit, String theSearchId, BundleTypeEnum theBundleType, Set<Include> theIncludes) {
		int numToReturn;
		String searchId = null;
		List<IBaseResource> resourceList;
		Integer numTotalResults = theResult.size();
		if (theServer.getPagingProvider() == null) {
			numToReturn = numTotalResults;
			resourceList = theResult.getResources(0, numToReturn);
			RestfulServerUtils.validateResourceListNotNull(resourceList);

		} else {
			IPagingProvider pagingProvider = theServer.getPagingProvider();
			if (theLimit == null || theLimit.equals(Integer.valueOf(0))) {
				numToReturn = pagingProvider.getDefaultPageSize();
			} else {
				numToReturn = Math.min(pagingProvider.getMaximumPageSize(), theLimit);
			}

			if (numTotalResults != null) {
				numToReturn = Math.min(numToReturn, numTotalResults - theOffset);
			}
			
			resourceList = theResult.getResources(theOffset, numToReturn + theOffset);
			RestfulServerUtils.validateResourceListNotNull(resourceList);

			if (theSearchId != null) {
				searchId = theSearchId;
			} else {
				if (numTotalResults == null || numTotalResults > numToReturn) {
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
			if (theServer.getAddProfileTag() != AddProfileTagEnum.NEVER) {
				addProfileIfNeeded(theServer, theServerBase, next);
			}
		}


		addResourcesToBundle(new ArrayList<IBaseResource>(resourceList), theBundleType, theServerBase, theServer.getBundleInclusionRule(), theIncludes);
		addRootPropertiesToBundle(null, theServerBase, theCompleteUrl, numTotalResults, theBundleType, theResult.getPublished());

		if (theServer.getPagingProvider() != null) {
			int limit;
			limit = theLimit != null ? theLimit : theServer.getPagingProvider().getDefaultPageSize();
			limit = Math.min(limit, theServer.getPagingProvider().getMaximumPageSize());

			if (searchId != null) {
				if (numTotalResults == null || theOffset + numToReturn < numTotalResults) {
					myBundle.getLinkNext()
							.setValue(RestfulServerUtils.createPagingLink(theIncludes, theServerBase, searchId, theOffset + numToReturn, numToReturn, theResponseEncoding, thePrettyPrint, theBundleType));
				}
				if (theOffset > 0) {
					int start = Math.max(0, theOffset - limit);
					myBundle.getLinkPrevious().setValue(RestfulServerUtils.createPagingLink(theIncludes, theServerBase, searchId, start, limit, theResponseEncoding, thePrettyPrint, theBundleType));
				}
			}
		}
	}

	@Override
	public void initializeBundleFromResourceList(String theAuthor, List<? extends IBaseResource> theResult, String theServerBase, String theCompleteUrl, int theTotalResults,
			BundleTypeEnum theBundleType) {
		myBundle = new Bundle();

		myBundle.getAuthorName().setValue(theAuthor);
		myBundle.getBundleId().setValue(UUID.randomUUID().toString());
		myBundle.getLinkBase().setValue(theServerBase);
		myBundle.getLinkSelf().setValue(theCompleteUrl);
		myBundle.getType().setValueAsEnum(theBundleType);

		List<IBaseResource> includedResources = new ArrayList<IBaseResource>();
		Set<IIdType> addedResourceIds = new HashSet<IIdType>();

		for (IBaseResource next : theResult) {
			if (next.getIdElement().isEmpty() == false) {
				addedResourceIds.add(next.getIdElement());
			}
		}

		for (IBaseResource nextRes : theResult) {
			IResource next = (IResource) nextRes;

			Set<String> containedIds = new HashSet<String>();
			for (IResource nextContained : next.getContained().getContainedResources()) {
				if (nextContained.getId().isEmpty() == false) {
					containedIds.add(nextContained.getId().getValue());
				}
			}

			List<BaseResourceReferenceDt> references = myContext.newTerser().getAllPopulatedChildElementsOfType(next, BaseResourceReferenceDt.class);
			do {
				List<IBaseResource> addedResourcesThisPass = new ArrayList<IBaseResource>();

				for (BaseResourceReferenceDt nextRef : references) {
					IBaseResource nextRefRes = nextRef.getResource();
					if (nextRefRes != null) {
						if (nextRefRes.getIdElement().hasIdPart()) {
							if (containedIds.contains(nextRefRes.getIdElement().getValue())) {
								// Don't add contained IDs as top level resources
								continue;
							}

							IIdType id = nextRefRes.getIdElement();
							if (id.hasResourceType() == false) {
								String resName = myContext.getResourceDefinition(nextRefRes).getName();
								id = id.withResourceType(resName);
							}

							if (!addedResourceIds.contains(id)) {
								addedResourceIds.add(id);
								addedResourcesThisPass.add(nextRefRes);
							}

						}
					}
				}

				// Linked resources may themselves have linked resources
				references = new ArrayList<BaseResourceReferenceDt>();
				for (IBaseResource iResource : addedResourcesThisPass) {
					List<BaseResourceReferenceDt> newReferences = myContext.newTerser().getAllPopulatedChildElementsOfType(iResource, BaseResourceReferenceDt.class);
					references.addAll(newReferences);
				}

				includedResources.addAll(addedResourcesThisPass);

			} while (references.isEmpty() == false);

			myBundle.addResource(next, myContext, theServerBase);

		}

		/*
		 * Actually add the resources to the bundle
		 */
		for (IBaseResource next : includedResources) {
			BundleEntry entry = myBundle.addResource((IResource) next, myContext, theServerBase);
			if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
				if (entry.getSearchMode().isEmpty()) {
					entry.getSearchMode().setValueAsEnum(BundleEntrySearchModeEnum.INCLUDE);
				}
			}
		}

		myBundle.getTotalResults().setValue(theTotalResults);
	}

	@Override
	public void initializeWithBundleResource(IBaseResource theResource) {
		throw new UnsupportedOperationException("DSTU1 server doesn't support resource style bundles");
	}

	@Override
	public List<IBaseResource> toListOfResources() {
		return new ArrayList<IBaseResource>(myBundle.toListOfResources());
	}

}
