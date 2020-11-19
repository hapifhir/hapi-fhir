package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.MdmLink;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.Optional;

public class IsMatchedToAPerson extends TypeSafeMatcher<IAnyResource> {

	private final IdHelperService myIdHelperService;
	private final MdmLinkDaoSvc myMdmLinkDaoSvc;

	public IsMatchedToAPerson(IdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		myIdHelperService = theIdHelperService;
		myMdmLinkDaoSvc = theMdmLinkDaoSvc;
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		Optional<MdmLink> matchedLinkForTargetPid = myMdmLinkDaoSvc.getMatchedLinkForTargetPid(myIdHelperService.getPidOrNull(theIncomingResource));
		return matchedLinkForTargetPid.isPresent();
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText("patient/practitioner was not linked to a Person.");
	}

	public static Matcher<IAnyResource> matchedToAGoldenResource(IdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		return new IsMatchedToAPerson(theIdHelperService, theMdmLinkDaoSvc);
	}
}
