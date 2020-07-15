package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Matcher with tells us if there is an EmpiLink with between these two resources that are considered POSSIBLE_MATCH
 */
public class IsPossibleMatchWith extends BasePersonMatcher {

	protected IsPossibleMatchWith(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IAnyResource... theBaseResource) {
		super(theIdHelperService, theEmpiLinkDaoSvc, theBaseResource);
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		List<EmpiLink> empiLinks = getEmpiLinksForTarget(theIncomingResource, EmpiMatchResultEnum.POSSIBLE_MATCH);

		List<Long> personPidsToMatch = myBaseResources.stream()
			.map(this::getMatchedPersonPidFromResource)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		if (personPidsToMatch.isEmpty()) {
			personPidsToMatch = myBaseResources.stream()
				.flatMap(iBaseResource -> getPossibleMatchedPersonPidsFromTarget(iBaseResource).stream())
				.collect(Collectors.toList());
		}

		List<Long> empiLinkSourcePersonPids = empiLinks.stream().map(EmpiLink::getPersonPid).collect(Collectors.toList());

		return empiLinkSourcePersonPids.containsAll(personPidsToMatch);
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText(" no link found with POSSIBLE_MATCH to the requested PIDS");
	}

	@Override
	protected void describeMismatchSafely(IAnyResource item, Description mismatchDescription) {
		super.describeMismatchSafely(item, mismatchDescription);
		mismatchDescription.appendText("No Empi Link With POSSIBLE_MATCH was found");
	}

	public static Matcher<IAnyResource> possibleMatchWith(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsPossibleMatchWith(theIdHelperService, theEmpiLinkDaoSvc, theBaseResource);
	}
}
