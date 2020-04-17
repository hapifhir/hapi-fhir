package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.jpa.empi.svc.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.empi.svc.ResourceTableHelper;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A Matcher which allows us to check that a target patient/practitioner at a given link level.
 * is linked to a set of patients/practitioners via a person.
 *
 * FIXME EMPI GGG modify the functionality to actually check links on a person.
 *
 */
public class IsLinkedTo extends BasePersonMatcher {

	private List<Long> baseResourcePersonPids;
	private Long incomingResourcePersonPid;

	protected IsLinkedTo(ResourceTableHelper theResourceTableHelper, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IBaseResource... theBaseResource) {
		super(theResourceTableHelper, theEmpiLinkDaoSvc, theBaseResource);
	}


	@Override
	protected boolean matchesSafely(IBaseResource theIncomingResource) {
		incomingResourcePersonPid =  getMatchedPersonPidFromResource(theIncomingResource);

		//OK, lets grab all the person pids of the resources passed in via the constructor.
		baseResourcePersonPids = myBaseResources.stream()
			.map(this::getMatchedPersonPidFromResource)
			.collect(Collectors.toList());

		//The resources are linked if all person pids match the incoming person pid.
		return baseResourcePersonPids.stream()
			.allMatch(pid -> pid.equals(incomingResourcePersonPid));
	}

	@Override
	public void describeTo(Description theDescription) {
	}

	public static Matcher<IBaseResource> linkedTo(ResourceTableHelper theResourceTableHelper, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IBaseResource... theBaseResource) {
		return new IsLinkedTo(theResourceTableHelper, theEmpiLinkDaoSvc, theBaseResource);
	}

}
