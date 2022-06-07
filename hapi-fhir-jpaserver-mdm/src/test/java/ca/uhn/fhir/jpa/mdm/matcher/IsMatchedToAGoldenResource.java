package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.Optional;

public class IsMatchedToAGoldenResource extends TypeSafeMatcher<IAnyResource> {

	private final IIdHelperService myIdHelperService;
	private final MdmLinkDaoSvc myMdmLinkDaoSvc;

	public IsMatchedToAGoldenResource(IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		myIdHelperService = theIdHelperService;
		myMdmLinkDaoSvc = theMdmLinkDaoSvc;
	}

	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		Optional<? extends IMdmLink> matchedLinkForTargetPid = myMdmLinkDaoSvc.getMatchedLinkForSourcePid(myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theIncomingResource));
		return matchedLinkForTargetPid.isPresent();
	}

	@Override
	public void describeTo(Description theDescription) {
		theDescription.appendText("target was not linked to a Golden Resource.");
	}

	public static Matcher<IAnyResource> matchedToAGoldenResource(IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc) {
		return new IsMatchedToAGoldenResource(theIdHelperService, theMdmLinkDaoSvc);
	}
}
