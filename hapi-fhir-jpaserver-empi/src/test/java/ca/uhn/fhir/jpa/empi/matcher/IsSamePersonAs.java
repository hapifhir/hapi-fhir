package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.dao.index.ResourceTablePidHelper;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.stream.Collectors;

public class IsSamePersonAs extends BasePersonMatcher {

	private List<Long> personPidsToMatch;
	private Long incomingPersonPid;

	public IsSamePersonAs(ResourceTablePidHelper theResourceTablePidHelper, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IBaseResource... theBaseResource) {
		super(theResourceTablePidHelper, theEmpiLinkDaoSvc, theBaseResource);
	}

	@Override
	protected boolean matchesSafely(IBaseResource theIncomingResource) {
		incomingPersonPid = getMatchedPersonPidFromResource(theIncomingResource);
		personPidsToMatch = myBaseResources.stream().map(br -> getMatchedPersonPidFromResource(br)).collect(Collectors.toList());
		boolean allToCheckAreSame = personPidsToMatch.stream().allMatch(pid -> pid.equals(personPidsToMatch.get(0)));
		if (!allToCheckAreSame) {
			throw new IllegalStateException("You wanted to do a person comparison, but the pool of persons you submitted for checking don't match! We won't even check the incoming person against them.");
		}
		return personPidsToMatch.contains(incomingPersonPid);
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText("patient/practitioner linked to Person/" + personPidsToMatch);
	}

	@Override
	protected void describeMismatchSafely(IBaseResource item, Description mismatchDescription) {
		super.describeMismatchSafely(item, mismatchDescription);
		mismatchDescription.appendText(" was actually linked to Person/" + incomingPersonPid);
	}

	public static Matcher<IBaseResource> samePersonAs(ResourceTablePidHelper theResourceTablePidHelper, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IBaseResource... theBaseResource) {
		return new IsSamePersonAs(theResourceTablePidHelper, theEmpiLinkDaoSvc, theBaseResource);
	}
}
