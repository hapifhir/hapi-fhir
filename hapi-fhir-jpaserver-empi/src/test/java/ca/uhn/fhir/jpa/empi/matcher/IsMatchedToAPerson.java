package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.Optional;

public class IsMatchedToAPerson extends TypeSafeMatcher<IAnyResource> {

	private final IdHelperService myIdHelperService;
	private final EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	public IsMatchedToAPerson(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc) {
		myIdHelperService = theIdHelperService;
		myEmpiLinkDaoSvc = theEmpiLinkDaoSvc;
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		Optional<EmpiLink> matchedLinkForTargetPid = myEmpiLinkDaoSvc.getMatchedLinkForTargetPid(myIdHelperService.getPidOrNull(theIncomingResource));
		return matchedLinkForTargetPid.isPresent();
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText("patient/practitioner was not linked to a Person.");
	}

	public static Matcher<IAnyResource> matchedToAPerson(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc) {
		return new IsMatchedToAPerson(theIdHelperService, theEmpiLinkDaoSvc);
	}
}
