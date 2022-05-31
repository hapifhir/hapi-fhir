package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.MdmLink;
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

public abstract class BaseGoldenResourceMatcher extends TypeSafeMatcher<IAnyResource> {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseGoldenResourceMatcher.class);

	protected IJpaIdHelperService myIdHelperService;
	protected MdmLinkDaoSvc myMdmLinkDaoSvc;
	protected Collection<IAnyResource> myBaseResources;
	protected String myTargetType;

	protected BaseGoldenResourceMatcher(IJpaIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		myIdHelperService = theIdHelperService;
		myMdmLinkDaoSvc = theMdmLinkDaoSvc;
		myBaseResources = Arrays.stream(theBaseResource).collect(Collectors.toList());
	}

	@Nullable
	protected Long getMatchedResourcePidFromResource(IAnyResource theResource) {
		Long retval;

		boolean isGoldenRecord = MdmResourceUtil.isMdmManaged(theResource);
		if (isGoldenRecord) {
			return myIdHelperService.getPidOrNull(theResource);
		}
		MdmLink matchLink = getMatchedMdmLink(theResource);

		if (matchLink == null) {
			return null;
		} else {
			retval = matchLink.getGoldenResourcePid();
			myTargetType = matchLink.getMdmSourceType();
		}
		return retval;
	}

	protected List<Long> getPossibleMatchedGoldenResourcePidsFromTarget(IAnyResource theBaseResource) {
		return getMdmLinksForTarget(theBaseResource, MdmMatchResultEnum.POSSIBLE_MATCH).stream().map(MdmLink::getGoldenResourcePid).collect(Collectors.toList());
	}

	protected MdmLink getMatchedMdmLink(IAnyResource thePatientOrPractitionerResource) {
		List<MdmLink> mdmLinks = getMdmLinksForTarget(thePatientOrPractitionerResource, MdmMatchResultEnum.MATCH);
		if (mdmLinks.size() == 0) {
			return null;
		} else if (mdmLinks.size() == 1) {
			return mdmLinks.get(0);
		} else {
			throw new IllegalStateException("Its illegal to have more than 1 match for a given target! we found " + mdmLinks.size() + " for resource with id: " + thePatientOrPractitionerResource.getIdElement().toUnqualifiedVersionless());
		}
	}

	protected List<MdmLink> getMdmLinksForTarget(IAnyResource theTargetResource, MdmMatchResultEnum theMatchResult) {
		Long pidOrNull = myIdHelperService.getPidOrNull(theTargetResource);
		List<MdmLink> matchLinkForTarget = myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(pidOrNull, theMatchResult);
		if (!matchLinkForTarget.isEmpty()) {
			return matchLinkForTarget;
		} else {
			return new ArrayList<>();
		}
	}
}
