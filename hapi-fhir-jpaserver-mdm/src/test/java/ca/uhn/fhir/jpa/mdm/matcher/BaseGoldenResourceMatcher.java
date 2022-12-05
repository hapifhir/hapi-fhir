package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
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

	protected IIdHelperService myIdHelperService;
	protected MdmLinkDaoSvc myMdmLinkDaoSvc;
	protected Collection<IAnyResource> myBaseResources;
	protected String myTargetType;

	protected BaseGoldenResourceMatcher(IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		myIdHelperService = theIdHelperService;
		myMdmLinkDaoSvc = theMdmLinkDaoSvc;
		myBaseResources = Arrays.stream(theBaseResource).collect(Collectors.toList());
	}

	@Nullable
	protected IResourcePersistentId getMatchedResourcePidFromResource(IAnyResource theResource) {
		IResourcePersistentId retval;

		boolean isGoldenRecord = MdmResourceUtil.isMdmManaged(theResource);
		if (isGoldenRecord) {
			return myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theResource);
		}
		IMdmLink matchLink = getMatchedMdmLink(theResource);

		if (matchLink == null) {
			return null;
		} else {
			retval = matchLink.getGoldenResourcePersistenceId();
			myTargetType = matchLink.getMdmSourceType();
		}
		return retval;
	}

	protected List<IResourcePersistentId> getPossibleMatchedGoldenResourcePidsFromTarget(IAnyResource theBaseResource) {
		return getMdmLinksForTarget(theBaseResource, MdmMatchResultEnum.POSSIBLE_MATCH)
			.stream()
			.map(IMdmLink::getGoldenResourcePersistenceId).collect(Collectors.toList());
	}

	protected IMdmLink getMatchedMdmLink(IAnyResource thePatientOrPractitionerResource) {
		List<? extends IMdmLink> mdmLinks = getMdmLinksForTarget(thePatientOrPractitionerResource, MdmMatchResultEnum.MATCH);
		if (mdmLinks.size() == 0) {
			return null;
		} else if (mdmLinks.size() == 1) {
			return mdmLinks.get(0);
		} else {
			throw new IllegalStateException("Its illegal to have more than 1 match for a given target! we found " + mdmLinks.size() + " for resource with id: " + thePatientOrPractitionerResource.getIdElement().toUnqualifiedVersionless());
		}
	}

	protected List<? extends IMdmLink> getMdmLinksForTarget(IAnyResource theTargetResource, MdmMatchResultEnum theMatchResult) {
		IResourcePersistentId pidOrNull = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theTargetResource);
		List<? extends IMdmLink> matchLinkForTarget = myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(pidOrNull, theMatchResult);
		if (!matchLinkForTarget.isEmpty()) {
			return matchLinkForTarget;
		} else {
			return new ArrayList<>();
		}
	}
}
