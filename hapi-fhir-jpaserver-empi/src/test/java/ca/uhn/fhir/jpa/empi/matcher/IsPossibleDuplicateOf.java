package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IsPossibleDuplicateOf extends BaseSourceResourceMatcher {
	/**
	 * Matcher with tells us if there is an EmpiLink with between these two resources that are considered POSSIBLE DUPLICATE.
	 * For use only on persons.
	 */
	private Long incomingPersonPid;

	protected IsPossibleDuplicateOf(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IAnyResource... theBaseResource) {
		super(theIdHelperService, theEmpiLinkDaoSvc, theBaseResource);
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {

		incomingPersonPid = getMatchedResourcePidFromResource(theIncomingResource);

		List<Long> personPidsToMatch = myBaseResources.stream()
			.map(this::getMatchedResourcePidFromResource)
			.collect(Collectors.toList());


		//Returns true if there is a POSSIBLE_DUPLICATE between the incoming resource, and all of the resources passed in via the constructor.
		return personPidsToMatch.stream()
			.map(baseResourcePid -> {
				Optional<EmpiLink> duplicateLink = myEmpiLinkDaoSvc.getEmpiLinksByPersonPidTargetPidAndMatchResult(baseResourcePid, incomingPersonPid, EmpiMatchResultEnum.POSSIBLE_DUPLICATE);
				if (!duplicateLink.isPresent()) {
					duplicateLink = myEmpiLinkDaoSvc.getEmpiLinksByPersonPidTargetPidAndMatchResult(incomingPersonPid, baseResourcePid, EmpiMatchResultEnum.POSSIBLE_DUPLICATE);
				}
				return duplicateLink;
			}).allMatch(Optional::isPresent);
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText("Person was not duplicate of Person/" + incomingPersonPid);
	}

	@Override
	protected void describeMismatchSafely(IAnyResource item, Description mismatchDescription) {
		super.describeMismatchSafely(item, mismatchDescription);
		mismatchDescription.appendText("No Empi Link With POSSIBLE_DUPLICATE was found");
	}

	public static Matcher<IAnyResource> possibleDuplicateOf(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsPossibleDuplicateOf(theIdHelperService, theEmpiLinkDaoSvc, theBaseResource);
	}
}
