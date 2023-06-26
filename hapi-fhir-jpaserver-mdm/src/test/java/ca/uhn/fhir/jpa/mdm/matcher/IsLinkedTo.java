package ca.uhn.fhir.jpa.mdm.matcher;

import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IAnyResource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A Matcher which allows us to check that a target patient/practitioner at a given link level.
 * is linked to a set of patients/practitioners via a golden resource.
 */
public class IsLinkedTo extends BaseGoldenResourceMatcher {

	private List<IResourcePersistentId> baseResourceGoldenResourcePids;
	private IResourcePersistentId incomingResourceGoldenResourcePid;

	protected IsLinkedTo(IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		super(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}


	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
		incomingResourceGoldenResourcePid = getMatchedResourcePidFromResource(theIncomingResource);

		//OK, lets grab all the golden resource pids of the resources passed in via the constructor.
		baseResourceGoldenResourcePids = myBaseResources.stream()
			.map(this::getMatchedResourcePidFromResource)
			.collect(Collectors.toList());

		//The resources are linked if all golden resource pids match the incoming golden resource pid.
		return baseResourceGoldenResourcePids.stream()
			.allMatch(pid -> pid.equals(incomingResourceGoldenResourcePid));
	}

	@Override
	public void describeTo(Description theDescription) {
	}

	public static Matcher<IAnyResource> linkedTo(IIdHelperService theIdHelperService, MdmLinkDaoSvc theMdmLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsLinkedTo(theIdHelperService, theMdmLinkDaoSvc, theBaseResource);
	}
}
