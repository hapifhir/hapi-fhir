package ca.uhn.fhir.rest.server.provider.dstu2;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Link;
import ca.uhn.fhir.model.dstu2.valueset.SearchEntryModeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.server.AddProfileTagEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class Dstu2BundleFactory implements IVersionSpecificBundleFactory {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Dstu2BundleFactory.class);
	private Bundle myBundle;

	@Override
	public 	void addResourcesToBundle(FhirContext theContext, List<IResource> theResult, BundleTypeEnum theBundleType, String theServerBase) {
		if (myBundle == null) {
			myBundle = new Bundle();
		}
		
		List<IResource> includedResources = new ArrayList<IResource>();
		Set<IdDt> addedResourceIds = new HashSet<IdDt>();

		for (IResource next : theResult) {
			if (next.getId().isEmpty() == false) {
				addedResourceIds.add(next.getId());
			}
		}

		for (IResource next : theResult) {

			Set<String> containedIds = new HashSet<String>();
			for (IResource nextContained : next.getContained().getContainedResources()) {
				if (nextContained.getId().isEmpty() == false) {
					containedIds.add(nextContained.getId().getValue());
				}
			}

			if (theContext.getNarrativeGenerator() != null) {
				String title = theContext.getNarrativeGenerator().generateTitle(next);
				ourLog.trace("Narrative generator created title: {}", title);
				if (StringUtils.isNotBlank(title)) {
					ResourceMetadataKeyEnum.TITLE.put(next, title);
				}
			} else {
				ourLog.trace("No narrative generator specified");
			}

			List<BaseResourceReferenceDt> references = theContext.newTerser().getAllPopulatedChildElementsOfType(next, BaseResourceReferenceDt.class);
			do {
				List<IResource> addedResourcesThisPass = new ArrayList<IResource>();

				for (BaseResourceReferenceDt nextRef : references) {
					IResource nextRes = nextRef.getResource();
					if (nextRes != null) {
						if (nextRes.getId().hasIdPart()) {
							if (containedIds.contains(nextRes.getId().getValue())) {
								// Don't add contained IDs as top level resources
								continue;
							}

							IdDt id = nextRes.getId();
							if (id.hasResourceType() == false) {
								String resName = theContext.getResourceDefinition(nextRes).getName();
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
					List<BaseResourceReferenceDt> newReferences = theContext.newTerser().getAllPopulatedChildElementsOfType(iResource, BaseResourceReferenceDt.class);
					references.addAll(newReferences);
				}

				includedResources.addAll(addedResourcesThisPass);

			} while (references.isEmpty() == false);

			Entry entry = myBundle.addEntry().setResource(next);
			
			BundleEntrySearchModeEnum searchMode = ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(next);
			if (searchMode != null) {
				entry.getSearch().getModeElement().setValue(searchMode.getCode());
			}
		}

		/*
		 * Actually add the resources to the bundle
		 */
		for (IResource next : includedResources) {
			myBundle.addEntry().setResource(next).getSearch().setMode(SearchEntryModeEnum.INCLUDE);
		}

	}

	@Override
	public void addRootPropertiesToBundle(String theAuthor, String theServerBase, String theCompleteUrl, int theTotalResults, BundleTypeEnum theBundleType) {

		if (myBundle.getId().isEmpty()) {
			myBundle.setId(UUID.randomUUID().toString());
		}

		if (ResourceMetadataKeyEnum.PUBLISHED.get(myBundle) == null) {
			InstantDt published = new InstantDt();
			published.setToCurrentTimeInLocalTimeZone();
			ResourceMetadataKeyEnum.PUBLISHED.put(myBundle, published);
		}

		if (!hasLink(Constants.LINK_SELF, myBundle) && isNotBlank(theCompleteUrl)) {
			myBundle.addLink().setRelation("self").setUrl(theCompleteUrl);
		}

		if (!hasLink(Constants.LINK_FHIR_BASE, myBundle) && isNotBlank(theServerBase)) {
			myBundle.addLink().setRelation(Constants.LINK_FHIR_BASE).setUrl(theServerBase);
		}

		if (myBundle.getTypeElement().isEmpty() && theBundleType != null) {
			myBundle.getTypeElement().setValueAsString(theBundleType.getCode());
		}

		if (myBundle.getTotalElement().isEmpty() && theTotalResults > 0) {
			myBundle.getTotalElement().setValue(theTotalResults);
		}
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
	public void initializeBundleFromBundleProvider(RestfulServer theServer, IBundleProvider theResult, EncodingEnum theResponseEncoding, String theServerBase, String theCompleteUrl, boolean thePrettyPrint, int theOffset, Integer theLimit, String theSearchId, BundleTypeEnum theBundleType) {
		int numToReturn;
		String searchId = null;
		List<IResource> resourceList;
		if (theServer.getPagingProvider() == null) {
			numToReturn = theResult.size();
			resourceList = theResult.getResources(0, numToReturn);
			RestfulServerUtils.validateResourceListNotNull(resourceList);
	
		} else {
			IPagingProvider pagingProvider = theServer.getPagingProvider();
			if (theLimit == null) {
				numToReturn = pagingProvider.getDefaultPageSize();
			} else {
				numToReturn = Math.min(pagingProvider.getMaximumPageSize(), theLimit);
			}

			numToReturn = Math.min(numToReturn, theResult.size() - theOffset);
			resourceList = theResult.getResources(theOffset, numToReturn + theOffset);
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
	
		for (IResource next : resourceList) {
			if (next.getId() == null || next.getId().isEmpty()) {
				if (!(next instanceof BaseOperationOutcome)) {
					throw new InternalErrorException("Server method returned resource of type[" + next.getClass().getSimpleName() + "] with no ID specified (IResource#setId(IdDt) must be called)");
				}
			}
		}
	
		if (theServer.getAddProfileTag() != AddProfileTagEnum.NEVER) {
			for (IResource nextRes : resourceList) {
				RuntimeResourceDefinition def = theServer.getFhirContext().getResourceDefinition(nextRes);
				if (theServer.getAddProfileTag() == AddProfileTagEnum.ALWAYS || !def.isStandardProfile()) {
					RestfulServerUtils.addProfileToBundleEntry(theServer.getFhirContext(), nextRes, theServerBase);
				}
			}
		}
	
		addResourcesToBundle(theServer.getFhirContext(), resourceList, theBundleType, theServerBase);
		addRootPropertiesToBundle(null, theServerBase, theCompleteUrl, theResult.size(), theBundleType);
	
		if (theServer.getPagingProvider() != null) {
			int limit;
			limit = theLimit != null ? theLimit : theServer.getPagingProvider().getDefaultPageSize();
			limit = Math.min(limit, theServer.getPagingProvider().getMaximumPageSize());
	
			if (searchId != null) {
				if (theOffset + numToReturn < theResult.size()) {
					myBundle.addLink().setRelation(Constants.LINK_NEXT).setUrl(RestfulServerUtils.createPagingLink(theServerBase, searchId, theOffset + numToReturn, numToReturn, theResponseEncoding, thePrettyPrint));
				}
				if (theOffset > 0) {
					int start = Math.max(0, theOffset - limit);
					myBundle.addLink().setRelation(Constants.LINK_PREVIOUS).setUrl(RestfulServerUtils.createPagingLink(theServerBase, searchId, start, limit, theResponseEncoding, thePrettyPrint));
				}
			}
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

	
	@Override
	public void initializeBundleFromResourceList(FhirContext theContext, String theAuthor, List<IResource> theResult, String theServerBase, String theCompleteUrl, int theTotalResults, BundleTypeEnum theBundleType) {
		myBundle = new Bundle();
		
		myBundle.setId(UUID.randomUUID().toString());
		
		ResourceMetadataKeyEnum.PUBLISHED.put(myBundle, InstantDt.withCurrentTime());

		myBundle.addLink().setRelation(Constants.LINK_FHIR_BASE).setUrl(theServerBase);
		myBundle.addLink().setRelation(Constants.LINK_SELF).setUrl(theCompleteUrl);
		myBundle.getTypeElement().setValueAsString(theBundleType.getCode());
	
		List<IResource> includedResources = new ArrayList<IResource>();
		Set<IdDt> addedResourceIds = new HashSet<IdDt>();
	
		for (IResource next : theResult) {
			if (next.getId().isEmpty() == false) {
				addedResourceIds.add(next.getId());
			}
		}
	
		for (IResource next : theResult) {
	
			Set<String> containedIds = new HashSet<String>();
			for (IResource nextContained : next.getContained().getContainedResources()) {
				if (nextContained.getId().isEmpty() == false) {
					containedIds.add(nextContained.getId().getValue());
				}
			}
	
			if (theContext.getNarrativeGenerator() != null) {
				String title = theContext.getNarrativeGenerator().generateTitle(next);
				ourLog.trace("Narrative generator created title: {}", title);
				if (StringUtils.isNotBlank(title)) {
					ResourceMetadataKeyEnum.TITLE.put(next, title);
				}
			} else {
				ourLog.trace("No narrative generator specified");
			}
	
			List<BaseResourceReferenceDt> references = theContext.newTerser().getAllPopulatedChildElementsOfType(next, BaseResourceReferenceDt.class);
			do {
				List<IResource> addedResourcesThisPass = new ArrayList<IResource>();
	
				for (BaseResourceReferenceDt nextRef : references) {
					IResource nextRes = nextRef.getResource();
					if (nextRes != null) {
						if (nextRes.getId().hasIdPart()) {
							if (containedIds.contains(nextRes.getId().getValue())) {
								// Don't add contained IDs as top level resources
								continue;
							}
	
							IdDt id = nextRes.getId();
							if (id.hasResourceType() == false) {
								String resName = theContext.getResourceDefinition(nextRes).getName();
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
					List<BaseResourceReferenceDt> newReferences = theContext.newTerser().getAllPopulatedChildElementsOfType(iResource, BaseResourceReferenceDt.class);
					references.addAll(newReferences);
				}
	
				includedResources.addAll(addedResourcesThisPass);
	
			} while (references.isEmpty() == false);
	
			myBundle.addEntry().setResource(next);
	
		}
	
		/*
		 * Actually add the resources to the bundle
		 */
		for (IResource next : includedResources) {
			myBundle.addEntry().setResource(next).getSearch().setMode(SearchEntryModeEnum.INCLUDE);
		}
	
		myBundle.getTotalElement().setValue(theTotalResults);
	}

	
	
}
