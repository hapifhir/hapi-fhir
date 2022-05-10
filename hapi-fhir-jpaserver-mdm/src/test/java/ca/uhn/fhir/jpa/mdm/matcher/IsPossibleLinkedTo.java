package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A Matcher which allows us to check that a target resource at a given link level
 * is linked to a set of target resources via a golden resource.
 */
public class IsPossibleLinkedTo extends BaseGoldenResourceMatcher {

	private List<Long> baseResourceGoldenResourcePids;
	private Long incomingResourceGoldenResourcePid;

	protected IsPossibleLinkedTo(IJpaIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theTargetResources) {
		super(theIdHelperService, theMdmLinkDaoSvc, theTargetResources);
	}

	@Override
	protected boolean matchesSafely(IAnyResource theGoldenResource) {
		incomingResourceGoldenResourcePid = myIdHelperService.getPidOrNull(theGoldenResource);

		//OK, lets grab all the golden resource pids of the resources passed in via the constructor.
		baseResourceGoldenResourcePids = myBaseResources.stream()
			.flatMap(iBaseResource -> getPossibleMatchedGoldenResourcePidsFromTarget(iBaseResource).stream())
			.collect(Collectors.toList());

		//The resources are linked if all golden resource pids match the incoming golden resource pid.
		return baseResourceGoldenResourcePids.stream()
			.allMatch(pid -> pid.equals(incomingResourceGoldenResourcePid));
	}

	@Override
	public void describeTo(Description theDescription) {
	}

	public static Matcher<IAnyResource> possibleLinkedTo(IJpaIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsPossibleLinkedTo(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}
}
