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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Assertion class for asserting matching of golden resources.
 */
public  class GoldenResourceMatchingAssert extends AbstractAssert<GoldenResourceMatchingAssert,  IAnyResource> {

	private IResourcePersistentId actualGoldenResourcePid;
	private IResourcePersistentId actualSourceResourcePid;
	private IIdHelperService myIdHelperService;
	private MdmLinkDaoSvc myMdmLinkDaoSvc;

	protected GoldenResourceMatchingAssert(IAnyResource actual, IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		super(actual, GoldenResourceMatchingAssert.class);
		myIdHelperService = theIdHelperService;
		myMdmLinkDaoSvc = theMdmLinkDaoSvc;
		actualGoldenResourcePid = getGoldenResourcePid(actual);
		actualSourceResourcePid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), actual);
	}

	public static GoldenResourceMatchingAssert assertThat(IAnyResource actual, IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		return new GoldenResourceMatchingAssert(actual, theIdHelperService, theMdmLinkDaoSvc);
	}

	// Method to compare with another resource
	public GoldenResourceMatchingAssert is_MATCH_to(IAnyResource other) {
		IResourcePersistentId otherGoldenPid = getGoldenResourcePid(other);
		if (!actualGoldenResourcePid.equals(otherGoldenPid)) {
			failWithActualExpectedAndMessage(actualGoldenResourcePid, otherGoldenPid, "Did not match golden resource pids!");
		}
		return this;
	}

	public GoldenResourceMatchingAssert is_not_MATCH_to(IAnyResource other) {
		IResourcePersistentId otherGoldenPid = getGoldenResourcePid(other);
		if (actualGoldenResourcePid != null &&  actualGoldenResourcePid.equals(otherGoldenPid)) {
			failWithActualExpectedAndMessage(actualGoldenResourcePid, otherGoldenPid, "Matched when it should not have!");
		}
		return this;
	}



	public GoldenResourceMatchingAssert is_NO_MATCH_to(IAnyResource other) {
		IResourcePersistentId otherGoldenPid = getGoldenResourcePid(other);
		if (actualGoldenResourcePid != null && actualGoldenResourcePid.equals(otherGoldenPid)) {
			failWithActualExpectedAndMessage(actualGoldenResourcePid, otherGoldenPid, "Both resources are linked to the same Golden pid!");
		}
		return this;
	}

	public GoldenResourceMatchingAssert is_POSSIBLE_MATCH_to(IAnyResource other) {
		boolean possibleMatch = hasPossibleMatchWith(other);

		if (!possibleMatch) {
			failWithMessage("No POSSIBLE_MATCH between these two resources.");
		}

		return this;
	}

	private boolean hasPossibleMatchWith(IAnyResource other) {
		IResourcePersistentId otherSourcePid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), other);
		IResourcePersistentId otherGoldenPid = getGoldenResourcePid(other);

		//Check for direct matches in either direction.
		// A POSSIBLE_MATCH -> B
		if (actualGoldenResourcePid != null) {
			Optional directForwardLink = myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(actualGoldenResourcePid, otherSourcePid, MdmMatchResultEnum.POSSIBLE_MATCH);
			if (directForwardLink.isPresent()) {
				return true;
			}
		}
		// B -> POSSIBLE_MATCH -> A
		if (otherGoldenPid != null) {
			Optional directBackwardLink = myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(otherGoldenPid, actualSourceResourcePid, MdmMatchResultEnum.POSSIBLE_MATCH);
			if (directBackwardLink.isPresent()) {
				return true;
			}
		}

		// Check for indirect possible matches, e.g.
		// A -> POSSIBLE_MATCH -> B
		// C -> POSSIBLE_MATCH -> B
		// this implies
		// A -> POSSIBLE_MATCH ->C

		boolean possibleMatch = false;
		Set<IResourcePersistentId> goldenPids = new HashSet<>();
		List<? extends IMdmLink> possibleLinksForOther = myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(otherSourcePid, MdmMatchResultEnum.POSSIBLE_MATCH);
		Set<IResourcePersistentId> otherPossibles = possibleLinksForOther.stream().map(IMdmLink::getGoldenResourcePersistenceId).collect(Collectors.toSet());
		goldenPids.addAll(otherPossibles);


//		Compare and inflate with all possible matches from the actual. If we hit a collision, we know that the implies POSSIBLE_MATCH exists.
		List<? extends IMdmLink> possibleLinksForActual = myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(actualSourceResourcePid, MdmMatchResultEnum.POSSIBLE_MATCH);
		Set<IResourcePersistentId> actualPossiblePids = possibleLinksForActual.stream().map(IMdmLink::getGoldenResourcePersistenceId).collect(Collectors.toSet());
		possibleMatch = isPossibleMatch(actualPossiblePids, goldenPids, possibleMatch);
		return possibleMatch;
	}

	private static boolean isPossibleMatch(Set<IResourcePersistentId> matchedPids, Set<IResourcePersistentId> goldenPids, boolean possibleMatch) {
		for (IResourcePersistentId pid : matchedPids) {
			if (goldenPids.contains(pid)) {
				return true;
			} else {
				goldenPids.add(pid);
			}
		}
		return false;
	}

	public boolean possibleDuplicateLinkExistsBetween(IResourcePersistentId goldenPid1, IResourcePersistentId goldenPid2) {
		Optional possibleForwardsLink = myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(goldenPid1, goldenPid2, MdmMatchResultEnum.POSSIBLE_DUPLICATE);
		Optional possibleBackwardsLink = myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(goldenPid2, goldenPid1, MdmMatchResultEnum.POSSIBLE_DUPLICATE);
		return possibleBackwardsLink.isPresent() || possibleForwardsLink.isPresent();
	}

	public GoldenResourceMatchingAssert is_POSSIBLE_DUPLICATE_to(IAnyResource other) {
		IResourcePersistentId otherGoldenPid = getGoldenResourcePid(other);
		if (actualGoldenResourcePid == null || otherGoldenPid == null) {
			failWithMessage("For a POSSIBLE_DUPLICATE, both resources must have a MATCH. This is not the case for these resources.");
		}
		boolean possibleDuplicateExists = possibleDuplicateLinkExistsBetween(actualGoldenResourcePid, otherGoldenPid);
		if (!possibleDuplicateExists) {
			failWithActualExpectedAndMessage("No POSSIBLE_DUPLICATE found between " + actualGoldenResourcePid + " and " + otherGoldenPid,
				"POSSIBLE_DUPLICATE found between " + actualGoldenResourcePid + " and " + otherGoldenPid,
				"No POSSIBLE_DUPLICATE links were found between golden resources");
		}
		return this;
	}


	@Nullable
	protected IResourcePersistentId getGoldenResourcePid(IAnyResource theResource) {
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

	public GoldenResourceMatchingAssert hasGoldenResourceMatch() {
		IResourcePersistentId otherGoldenPid = getGoldenResourcePid(actual);
		if (otherGoldenPid == null) {
			failWithMessage("Expected resource to be matched to a golden resource. Found no such matches. ");
		}
		return this;
	}

	public GoldenResourceMatchingAssert doesNotHaveGoldenResourceMatch() {
		IResourcePersistentId otherGoldenPid = getGoldenResourcePid(actual);
		if (otherGoldenPid != null) {
			failWithMessage("Expected resource to have no golden resource match, but it did.");
		}
		return this;
	}

	public GoldenResourceMatchingAssert is_not_POSSIBLE_DUPLICATE_to(IAnyResource other) {
		IResourcePersistentId otherGoldenResourcePid = getGoldenResourcePid(other);
		boolean possibleDuplicateExists = possibleDuplicateLinkExistsBetween(actualGoldenResourcePid, otherGoldenResourcePid);
		if (possibleDuplicateExists) {
			failWithMessage("Possible duplicate exists between both resources!");
		}
		return this;
	}
}
