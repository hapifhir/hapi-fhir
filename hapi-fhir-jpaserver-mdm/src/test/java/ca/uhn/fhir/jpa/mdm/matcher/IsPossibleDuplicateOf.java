package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IsPossibleDuplicateOf extends BaseGoldenResourceMatcher {

	/**
	 * Matcher with tells us if there is an MdmLink with between these two resources that are considered POSSIBLE DUPLICATE.
	 * For use only on GoldenResource.
	 */
	private Long incomingGoldenResourcePid;

	protected IsPossibleDuplicateOf(IJpaIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		super(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		incomingGoldenResourcePid = getMatchedResourcePidFromResource(theIncomingResource);

		List<Long> goldenResourcePidsToMatch = myBaseResources.stream()
			.map(this::getMatchedResourcePidFromResource)
			.collect(Collectors.toList());


		//Returns true if there is a POSSIBLE_DUPLICATE between the incoming resource, and all of the resources passed in via the constructor.
		return goldenResourcePidsToMatch.stream()
			.map(baseResourcePid -> {
				Optional<MdmLink> duplicateLink = myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(baseResourcePid, incomingGoldenResourcePid, MdmMatchResultEnum.POSSIBLE_DUPLICATE);
				if (!duplicateLink.isPresent()) {
					duplicateLink = myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(incomingGoldenResourcePid, baseResourcePid, MdmMatchResultEnum.POSSIBLE_DUPLICATE);
				}
				return duplicateLink;
			}).allMatch(Optional::isPresent);
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText("Resource was not duplicate of Resource/" + incomingGoldenResourcePid);
	}

	@Override
	protected void describeMismatchSafely(IAnyResource item, Description mismatchDescription) {
		super.describeMismatchSafely(item, mismatchDescription);
		mismatchDescription.appendText("No MdmLink With POSSIBLE_DUPLICATE was found");
	}

	public static Matcher<IAnyResource> possibleDuplicateOf(IJpaIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsPossibleDuplicateOf(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}
}
