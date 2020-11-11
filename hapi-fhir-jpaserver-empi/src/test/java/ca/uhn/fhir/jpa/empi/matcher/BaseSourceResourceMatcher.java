package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.util.EmpiUtil;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseSourceResourceMatcher extends TypeSafeMatcher<IAnyResource> {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSourceResourceMatcher.class);

	protected IdHelperService myIdHelperService;
	protected EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	protected Collection<IAnyResource> myBaseResources;
	protected String myTargetType;

	protected BaseSourceResourceMatcher(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IAnyResource... theBaseResource) {
		myIdHelperService = theIdHelperService;
		myEmpiLinkDaoSvc = theEmpiLinkDaoSvc;
		myBaseResources = Arrays.stream(theBaseResource).collect(Collectors.toList());
	}

	@Nullable
	protected Long getMatchedResourcePidFromResource(IAnyResource theResource) {
		Long retval;

		boolean isGoldenRecord = EmpiUtil.isEmpiManaged(theResource);
		if (isGoldenRecord) {
			return myIdHelperService.getPidOrNull(theResource);
		}
		EmpiLink matchLink = getMatchedEmpiLink(theResource);

		if (matchLink == null) {
			return null;
		} else {
			retval = matchLink.getSourceResourcePid();
			myTargetType = matchLink.getEmpiTargetType();
		}
		return retval;
	}

	protected List<Long> getPossibleMatchedSourceResourcePidsFromTarget(IAnyResource theBaseResource) {
		return getEmpiLinksForTarget(theBaseResource, EmpiMatchResultEnum.POSSIBLE_MATCH).stream().map(EmpiLink::getSourceResourcePid).collect(Collectors.toList());
	}

	protected EmpiLink getMatchedEmpiLink(IAnyResource thePatientOrPractitionerResource) {
		List<EmpiLink> empiLinks = getEmpiLinksForTarget(thePatientOrPractitionerResource, EmpiMatchResultEnum.MATCH);
		if (empiLinks.size() == 0) {
			return null;
		} else if (empiLinks.size() == 1) {
			return empiLinks.get(0);
		} else {
			throw new IllegalStateException("Its illegal to have more than 1 match for a given target! we found " + empiLinks.size() + " for resource with id: " + thePatientOrPractitionerResource.getIdElement().toUnqualifiedVersionless());
		}
	}

	protected List<EmpiLink> getEmpiLinksForTarget(IAnyResource theTargetResource, EmpiMatchResultEnum theMatchResult) {
		Long pidOrNull = myIdHelperService.getPidOrNull(theTargetResource);
		List<EmpiLink> matchLinkForTarget = myEmpiLinkDaoSvc.getEmpiLinksByTargetPidAndMatchResult(pidOrNull, theMatchResult);
		if (!matchLinkForTarget.isEmpty()) {
			return matchLinkForTarget;
		} else {
			return new ArrayList<>();
		}
	}
}
