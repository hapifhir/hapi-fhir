package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.MdmLink;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.Optional;

public class IsMatchedToAGoldenResource extends TypeSafeMatcher<IAnyResource> {

	private final IdHelperService myIdHelperService;
	private final MdmLinkDaoSvc myMdmLinkDaoSvc;

	public IsMatchedToAGoldenResource(IdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		myIdHelperService = theIdHelperService;
		myMdmLinkDaoSvc = theMdmLinkDaoSvc;
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		Optional<MdmLink> matchedLinkForTargetPid = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(myIdHelperService.getPidOrNull(theIncomingResource));
		return matchedLinkForTargetPid.isPresent();
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText("target was not linked to a Golden Resource.");
	}

	public static Matcher<IAnyResource> matchedToAGoldenResource(IdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		return new IsMatchedToAGoldenResource(theIdHelperService, theMdmLinkDaoSvc);
	}
}
