package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.empi.svc.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.empi.svc.ResourceTableHelper;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasePersonMatcher extends TypeSafeMatcher<IBaseResource> {
    protected ResourceTableHelper myResourceTableHelper;
    protected EmpiLinkDaoSvc myEmpiLinkDaoSvc;
    protected Collection<IBaseResource> myBaseResources;

    protected BasePersonMatcher(ResourceTableHelper theResourceTableHelper, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IBaseResource... theBaseResource) {
        myResourceTableHelper = theResourceTableHelper;
        myEmpiLinkDaoSvc = theEmpiLinkDaoSvc;
        myBaseResources = Arrays.stream(theBaseResource).collect(Collectors.toList());
    }

    protected Long getMatchedPersonPidFromResource(IBaseResource theResource) {
        if (isPatientOrPractitioner(theResource)) {
			  EmpiLink matchLink = getMatchedEmpiLink(theResource);
			  return matchLink == null ? null : matchLink.getPersonPid();
        } else if (isPerson(theResource)) {
            return myResourceTableHelper.getPidOrNull(theResource);
        } else {
            throw new IllegalArgumentException("Resources of type " + theResource.getIdElement().getResourceType() + " cannot be persons!");
        }
    }
    protected List<Long> getPossibleMatchedPersonPidsFromResource(IBaseResource theBaseResource) {
    	return getEmpiLinksForTarget(theBaseResource, EmpiMatchResultEnum.POSSIBLE_MATCH).stream().map(EmpiLink::getPersonPid).collect(Collectors.toList());
	 }

    protected boolean isPatientOrPractitioner(IBaseResource theResource) {
        String resourceType = theResource.getIdElement().getResourceType();
        return (resourceType.equalsIgnoreCase("Patient") || resourceType.equalsIgnoreCase("Practitioner"));
    }

    protected EmpiLink getMatchedEmpiLink(IBaseResource thePatientOrPractitionerResource) {
        List<EmpiLink> empiLinks = getEmpiLinksForTarget(thePatientOrPractitionerResource, EmpiMatchResultEnum.MATCH);
        if (empiLinks.size() == 0) {
        	return null;
        } else if (empiLinks.size() == 1) {
            return empiLinks.get(0);
        } else {
            throw new IllegalStateException("Its illegal to have more than 1 match! we found " + empiLinks.size() + " for resource with pid: " + thePatientOrPractitionerResource.getIdElement());
        }
    }

    protected boolean isPerson(IBaseResource theIncomingResource) {
        return (theIncomingResource.getIdElement().getResourceType().equalsIgnoreCase("Person"));
    }

    protected List<EmpiLink> getEmpiLinksForTarget(IBaseResource thePatientOrPractitionerResource, EmpiMatchResultEnum theMatchResult) {
        Long pidOrNull = myResourceTableHelper.getPidOrNull(thePatientOrPractitionerResource);
        List<EmpiLink> matchLinkForTarget = myEmpiLinkDaoSvc.getEmpiLinksByTargetPidAndMatchResult(pidOrNull, theMatchResult);
        if (!matchLinkForTarget.isEmpty()) {
            return matchLinkForTarget;
        } else {
        	return new ArrayList<>();
		  }
    }
}
