package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.jpa.empi.svc.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.empi.svc.ResourceTableHelper;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Optional;

public class IsMatchedToAPerson extends TypeSafeMatcher<IBaseResource> {

	private final ResourceTableHelper myResourceTableHelper;
	private final EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	public IsMatchedToAPerson(ResourceTableHelper theResourceTableHelper, EmpiLinkDaoSvc theEmpiLinkDaoSvc) {
		myResourceTableHelper = theResourceTableHelper;
		myEmpiLinkDaoSvc = theEmpiLinkDaoSvc;
	}

	@Override
	protected boolean matchesSafely(IBaseResource theIncomingResource) {
		Optional<EmpiLink> matchedLinkForTargetPid = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myResourceTableHelper.getPidOrNull(theIncomingResource));
		return matchedLinkForTargetPid.isPresent();
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText("patient/practitioner was not linked to a Person.");
	}

	public static Matcher<IBaseResource> matchedToAPerson(ResourceTableHelper theResourceTableHelper, EmpiLinkDaoSvc theEmpiLinkDaoSvc) {
		return new IsMatchedToAPerson(theResourceTableHelper, theEmpiLinkDaoSvc);
	}
}
