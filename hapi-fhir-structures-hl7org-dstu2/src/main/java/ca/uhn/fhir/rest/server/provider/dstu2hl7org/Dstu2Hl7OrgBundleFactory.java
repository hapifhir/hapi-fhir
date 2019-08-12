package ca.uhn.fhir.rest.server.provider.dstu2hl7org;

/*
 * #%L
 * HAPI FHIR Structures - HL7.org DSTU2
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
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import org.hl7.fhir.dstu2.model.Bundle;
import org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.dstu2.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu2.model.Bundle.SearchEntryMode;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.InstantType;
import org.hl7.fhir.dstu2.model.Resource;
import org.hl7.fhir.instance.model.api.*;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class Dstu2Hl7OrgBundleFactory implements IVersionSpecificBundleFactory {

  private String myBase;
  private Bundle myBundle;
  private FhirContext myContext;

  public Dstu2Hl7OrgBundleFactory(FhirContext theContext) {
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
      IDomainResource next = (IDomainResource) nextBaseRes;
      Set<String> containedIds = new HashSet<String>();
      for (IBaseResource nextContained : next.getContained()) {
        if (nextContained.getIdElement().isEmpty() == false) {
          containedIds.add(nextContained.getIdElement().getValue());
        }
      }

      List<IBaseReference> references = myContext.newTerser().getAllPopulatedChildElementsOfType(next,
        IBaseReference.class);
      do {
        List<IBaseResource> addedResourcesThisPass = new ArrayList<IBaseResource>();

        for (IBaseReference nextRef : references) {
          IBaseResource nextRes = (IBaseResource) nextRef.getResource();
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
        for (IBaseResource iResource : addedResourcesThisPass) {
          List<IBaseReference> newReferences = myContext.newTerser().getAllPopulatedChildElementsOfType(iResource,
            IBaseReference.class);
          references.addAll(newReferences);
        }

        includedResources.addAll(addedResourcesThisPass);

      } while (references.isEmpty() == false);

      BundleEntryComponent entry = myBundle.addEntry().setResource((Resource) next);
      populateBundleEntryFullUrl(next, entry);
    }

		/*
       * Actually add the resources to the bundle
		 */
    for (IBaseResource next : includedResources) {
      BundleEntryComponent entry = myBundle.addEntry();
      entry.setResource((Resource) next).getSearch().setMode(SearchEntryMode.INCLUDE);
      populateBundleEntryFullUrl(next, entry);
    }
  }

  @Override
  public void addResourcesToBundle(List<IBaseResource> theResult, BundleTypeEnum theBundleType, String theServerBase,
                                   BundleInclusionRule theBundleInclusionRule, Set<Include> theIncludes) {
    ensureBundle();

    List<IBaseResource> includedResources = new ArrayList<IBaseResource>();
    Set<IIdType> addedResourceIds = new HashSet<IIdType>();

    for (IBaseResource next : theResult) {
      if (next.getIdElement().isEmpty() == false) {
        addedResourceIds.add(next.getIdElement());
      }
    }

    for (IBaseResource next : theResult) {

      List<? extends IAnyResource> contained;
      if (next instanceof IDomainResource) {
        IDomainResource nextDomain = (IDomainResource) next;
        contained = nextDomain.getContained();
      } else {
        contained = Collections.emptyList();
      }

      Set<String> containedIds = new HashSet<String>();
      for (IAnyResource nextContained : contained) {
        if (nextContained.getId().isEmpty() == false) {
          containedIds.add(nextContained.getIdElement().getValue());
        }
      }

      List<ResourceReferenceInfo> references = myContext.newTerser().getAllResourceReferences(next);
      do {
        List<IBaseResource> addedResourcesThisPass = new ArrayList<IBaseResource>();

        for (ResourceReferenceInfo nextRefInfo : references) {
          if (!theBundleInclusionRule.shouldIncludeReferencedResource(nextRefInfo, theIncludes))
            continue;

          IBaseResource nextRes = (IBaseResource) nextRefInfo.getResourceReference().getResource();
          if (nextRes != null) {
            if (nextRes.getIdElement().hasIdPart()) {
              if (containedIds.contains(nextRes.getIdElement().getValue())) {
                // Don't add contained IDs as top level resources
                continue;
              }

              IdType id = (IdType) nextRes.getIdElement();
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
        for (IBaseResource iResource : addedResourcesThisPass) {
          List<ResourceReferenceInfo> newReferences = myContext.newTerser().getAllResourceReferences(iResource);
          references.addAll(newReferences);
        }
      } while (references.isEmpty() == false);

      BundleEntryComponent entry = myBundle.addEntry().setResource((Resource) next);
      populateBundleEntryFullUrl(next, entry);

      // BundleEntrySearchModeEnum searchMode =
      // ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(next);
      // if (searchMode != null) {
      // entry.getSearch().getModeElement().setValue(searchMode.getCode());
      // }
    }

		/*
		 * Actually add the resources to the bundle
		 */
    for (IBaseResource next : includedResources) {
      myBundle.addEntry().setResource((Resource) next).getSearch().setMode(SearchEntryMode.INCLUDE);
    }

  }

  @Override
  public void addRootPropertiesToBundle(String theId, String theServerBase, String theLinkSelf, String theLinkPrev, String theLinkNext,
                                        Integer theTotalResults, BundleTypeEnum theBundleType, IPrimitiveType<Date> theLastUpdated) {
    ensureBundle();

    if (myBundle.getIdElement().isEmpty()) {
      myBundle.setId(theId);
    }
    if (isBlank(myBundle.getId())) {
      myBundle.setId(UUID.randomUUID().toString());
    }

    if (myBundle.getMeta().getLastUpdated() == null) {
      InstantType instantType = new InstantType();
      instantType.setValueAsString(theLastUpdated.getValueAsString());
      myBundle.getMeta().setLastUpdatedElement(instantType);
    }

    if (!hasLink(Constants.LINK_SELF, myBundle) && isNotBlank(theLinkSelf)) {
      myBundle.addLink().setRelation(Constants.LINK_SELF).setUrl(theLinkSelf);
    }
    if (!hasLink(Constants.LINK_NEXT, myBundle) && isNotBlank(theLinkNext)) {
      myBundle.addLink().setRelation(Constants.LINK_NEXT).setUrl(theLinkNext);
    }
    if (!hasLink(Constants.LINK_SELF, myBundle) && isNotBlank(theLinkPrev)) {
      myBundle.addLink().setRelation(Constants.LINK_PREVIOUS).setUrl(theLinkPrev);
    }

    myBase = theServerBase;

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
  public void initializeBundleFromResourceList(String theAuthor, List<? extends IBaseResource> theResources,
                                               String theServerBase, String theCompleteUrl, int theTotalResults, BundleTypeEnum theBundleType) {
    ensureBundle();

    myBundle.setId(UUID.randomUUID().toString());

    myBundle.getMeta().setLastUpdatedElement(InstantType.withCurrentTime());

    myBundle.addLink().setRelation(Constants.LINK_FHIR_BASE).setUrl(theServerBase);
    myBundle.addLink().setRelation(Constants.LINK_SELF).setUrl(theCompleteUrl);
    myBundle.getTypeElement().setValueAsString(theBundleType.getCode());

    if (theBundleType.equals(BundleTypeEnum.TRANSACTION)) {
      for (IBaseResource nextBaseRes : theResources) {
        IBaseResource next = (IBaseResource) nextBaseRes;
        BundleEntryComponent nextEntry = myBundle.addEntry();

        nextEntry.setResource((Resource) next);
        if (next.getIdElement().isEmpty()) {
          nextEntry.getRequest().setMethod(HTTPVerb.POST);
        } else {
          nextEntry.getRequest().setMethod(HTTPVerb.PUT);
          if (next.getIdElement().isAbsolute()) {
            nextEntry.getRequest().setUrl(next.getIdElement().getValue());
          } else {
            String resourceType = myContext.getResourceDefinition(next).getName();
            nextEntry.getRequest().setUrl(new IdType(theServerBase, resourceType, next.getIdElement().getIdPart(),
              next.getIdElement().getVersionIdPart()).getValue());
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

  private void populateBundleEntryFullUrl(IBaseResource next, BundleEntryComponent entry) {
    if (next.getIdElement().hasBaseUrl()) {
      entry.setFullUrl(next.getIdElement().toVersionless().getValue());
    } else {
      if (isNotBlank(myBase) && next.getIdElement().hasIdPart()) {
        IIdType id = next.getIdElement().toVersionless();
        id = id.withServerBase(myBase, myContext.getResourceDefinition(next).getName());
        entry.setFullUrl(id.getValue());
      }
    }
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
          IBaseResource res = (IBaseResource) myContext.getResourceDefinition(resourceType).newInstance();
          res.setId(id);
          retVal.add(res);
        }
      }
    }
    return retVal;
  }

}
