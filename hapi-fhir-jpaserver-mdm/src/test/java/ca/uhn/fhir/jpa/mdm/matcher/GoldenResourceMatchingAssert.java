package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nullable;
import org.assertj.core.api.AbstractAssert;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public  class GoldenResourceMatchingAssert<T extends IAnyResource > extends AbstractAssert<GoldenResourceMatchingAssert<T>,  T> {
	private static final Logger ourLog = LoggerFactory.getLogger(GoldenResourceMatchingAssert.class);

	private IResourcePersistentId actualGoldenResourcePid;
	private IIdHelperService myIdHelperService;
	private MdmLinkDaoSvc myMdmLinkDaoSvc;

	protected GoldenResourceMatchingAssert(T actual, IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		super(actual, GoldenResourceMatchingAssert.class);
		myIdHelperService = theIdHelperService;
		myMdmLinkDaoSvc = theMdmLinkDaoSvc;
		actualGoldenResourcePid = getMatchedResourcePidFromResource(actual);
	}

	public static <T extends IAnyResource> GoldenResourceMatchingAssert<T> assertThat(T actual, IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		return new GoldenResourceMatchingAssert<>(actual, theIdHelperService, theMdmLinkDaoSvc);
	}

	// Method to compare with another resource
	public GoldenResourceMatchingAssert<T> isMatchedTo(T other) {
		IResourcePersistentId otherGoldenPid = getMatchedResourcePidFromResource(other);
		if (actualGoldenResourcePid != otherGoldenPid) {
			failWithActualExpectedAndMessage(actualGoldenResourcePid, otherGoldenPid, "Did not match golden resource pids!");
		}
		return this;
	}

	@Nullable
	protected IResourcePersistentId getMatchedResourcePidFromResource( T theResource) {
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
		}
		return retval;
	}

	protected IMdmLink getMatchedMdmLink(T thePatientOrPractitionerResource) {
		List<? extends IMdmLink> mdmLinks = getMdmLinksForTarget(thePatientOrPractitionerResource, MdmMatchResultEnum.MATCH);
		if (mdmLinks.size() == 0) {
			return null;
		} else if (mdmLinks.size() == 1) {
			return mdmLinks.get(0);
		} else {
			throw new IllegalStateException("Its illegal to have more than 1 match for a given target! we found " + mdmLinks.size() + " for resource with id: " + thePatientOrPractitionerResource.getIdElement().toUnqualifiedVersionless());
		}
	}

	protected List<? extends IMdmLink> getMdmLinksForTarget(T theTargetResource, MdmMatchResultEnum theMatchResult) {
		IResourcePersistentId pidOrNull = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theTargetResource);
		List<? extends IMdmLink> matchLinkForTarget = myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(pidOrNull, theMatchResult);
		if (!matchLinkForTarget.isEmpty()) {
			return matchLinkForTarget;
		} else {
			return new ArrayList<>();
		}
	}
}
