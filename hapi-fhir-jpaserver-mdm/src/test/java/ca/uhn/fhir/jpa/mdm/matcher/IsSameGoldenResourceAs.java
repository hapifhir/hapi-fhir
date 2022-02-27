package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.List;
import java.util.stream.Collectors;

public class IsSameGoldenResourceAs extends BaseGoldenResourceMatcher {

	private List<Long> goldenResourcePidsToMatch;
	private Long incomingGoldenResourcePid;

	public IsSameGoldenResourceAs(IJpaIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		super(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		incomingGoldenResourcePid = getMatchedResourcePidFromResource(theIncomingResource);
		goldenResourcePidsToMatch = myBaseResources.stream().map(this::getMatchedResourcePidFromResource).collect(Collectors.toList());
		boolean allToCheckAreSame = goldenResourcePidsToMatch.stream().allMatch(pid -> pid.equals(goldenResourcePidsToMatch.get(0)));
		if (!allToCheckAreSame) {
			throw new IllegalStateException("You wanted to do a source resource comparison, but the pool of source resources you submitted for checking don't match! We won't even check the incoming source resource against them.");
		}
		return goldenResourcePidsToMatch.contains(incomingGoldenResourcePid);
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText(String.format(" %s linked to source resource %s/%s", myTargetType, myTargetType, goldenResourcePidsToMatch));
	}

	@Override
	protected void describeMismatchSafely(IAnyResource item, Description mismatchDescription) {
		super.describeMismatchSafely(item, mismatchDescription);
		mismatchDescription.appendText(String.format(" was actually linked to %s/%s", myTargetType, incomingGoldenResourcePid));
	}

	public static Matcher<IAnyResource> sameGoldenResourceAs(IJpaIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsSameGoldenResourceAs(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}
}
