package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A Matcher which allows us to check that a target patient/practitioner at a given link level.
 * is linked to a set of patients/practitioners via a person.
 *
 */
public class IsPossibleLinkedTo extends BaseSourceResourceMatcher {

	private List<Long> baseResourcePersonPids;
	private Long incomingResourcePersonPid;

	protected IsPossibleLinkedTo(IdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theTargetResources) {
		super(theIdHelperService, theMdmLinkDaoSvc, theTargetResources);
	}

	@Override
	protected boolean matchesSafely(IAnyResource theSourceResource) {
		incomingResourcePersonPid = myIdHelperService.getPidOrNull(theSourceResource);

		//OK, lets grab all the person pids of the resources passed in via the constructor.
		baseResourcePersonPids = myBaseResources.stream()
			.flatMap(iBaseResource -> getPossibleMatchedSourceResourcePidsFromTarget(iBaseResource).stream())
			.collect(Collectors.toList());

		//The resources are linked if all person pids match the incoming person pid.
		return baseResourcePersonPids.stream()
			.allMatch(pid -> pid.equals(incomingResourcePersonPid));
	}

	@Override
	public void describeTo(Description theDescription) {
	}

	public static Matcher<IAnyResource> possibleLinkedTo(IdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsPossibleLinkedTo(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}
}
