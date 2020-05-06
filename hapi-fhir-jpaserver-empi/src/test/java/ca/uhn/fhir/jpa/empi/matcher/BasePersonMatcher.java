package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasePersonMatcher extends TypeSafeMatcher<IBaseResource> {
	private static final Logger ourLog = LoggerFactory.getLogger(BasePersonMatcher.class);

	protected IdHelperService myIdHelperService;
	protected EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	protected Collection<IBaseResource> myBaseResources;

	protected BasePersonMatcher(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IBaseResource... theBaseResource) {
		myIdHelperService = theIdHelperService;
		myEmpiLinkDaoSvc = theEmpiLinkDaoSvc;
		myBaseResources = Arrays.stream(theBaseResource).collect(Collectors.toList());
	}

	@Nullable
	protected Long getMatchedPersonPidFromResource(IBaseResource theResource) {
		Long retval;
		if (isPatientOrPractitioner(theResource)) {
			EmpiLink matchLink = getMatchedEmpiLink(theResource);
			retval = matchLink == null ? null : matchLink.getPersonPid();
		} else if (isPerson(theResource)) {
			retval = myIdHelperService.getPidOrNull(theResource);
		} else {
			throw new IllegalArgumentException("Resources of type " + theResource.getIdElement().getResourceType() + " cannot be persons!");
		}
		return retval;
	}

	protected List<Long> getPossibleMatchedPersonPidsFromTarget(IBaseResource theBaseResource) {
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
			throw new IllegalStateException("Its illegal to have more than 1 match for a given target! we found " + empiLinks.size() + " for resource with id: " + thePatientOrPractitionerResource.getIdElement().toUnqualifiedVersionless());
		}
	}

	protected boolean isPerson(IBaseResource theIncomingResource) {
		return (theIncomingResource.getIdElement().getResourceType().equalsIgnoreCase("Person"));
	}

	protected List<EmpiLink> getEmpiLinksForTarget(IBaseResource thePatientOrPractitionerResource, EmpiMatchResultEnum theMatchResult) {
		Long pidOrNull = myIdHelperService.getPidOrNull(thePatientOrPractitionerResource);
		List<EmpiLink> matchLinkForTarget = myEmpiLinkDaoSvc.getEmpiLinksByTargetPidAndMatchResult(pidOrNull, theMatchResult);
		if (!matchLinkForTarget.isEmpty()) {
			return matchLinkForTarget;
		} else {
			return new ArrayList<>();
		}
	}
}
