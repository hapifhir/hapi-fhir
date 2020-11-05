package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.List;
import java.util.stream.Collectors;

public class IsSameSourceResourceAs extends BaseSourceResourceMatcher {

	private List<Long> sourceResourcePidsToMatch;
	private Long incomingSourceResourcePid;

	public IsSameSourceResourceAs(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IAnyResource... theBaseResource) {
		super(theIdHelperService, theEmpiLinkDaoSvc, theBaseResource);
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		incomingSourceResourcePid = getMatchedResourcePidFromResource(theIncomingResource);
		sourceResourcePidsToMatch = myBaseResources.stream().map(this::getMatchedResourcePidFromResource).collect(Collectors.toList());
		boolean allToCheckAreSame = sourceResourcePidsToMatch.stream().allMatch(pid -> pid.equals(sourceResourcePidsToMatch.get(0)));
		if (!allToCheckAreSame) {
			throw new IllegalStateException("You wanted to do a source resource comparison, but the pool of persons you submitted for checking don't match! We won't even check the incoming person against them.");
		}
		return sourceResourcePidsToMatch.contains(incomingSourceResourcePid);
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText(String.format(" %s linked to source resource %s/%s", myTargetType, myTargetType, sourceResourcePidsToMatch));
	}

	@Override
	protected void describeMismatchSafely(IAnyResource item, Description mismatchDescription) {
		super.describeMismatchSafely(item, mismatchDescription);
		mismatchDescription.appendText(String.format(" was actually linked to %s/%s", myTargetType, incomingSourceResourcePid));
	}

	public static Matcher<IAnyResource> sameSourceResourceAs(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsSameSourceResourceAs(theIdHelperService, theEmpiLinkDaoSvc, theBaseResource);
	}
}
