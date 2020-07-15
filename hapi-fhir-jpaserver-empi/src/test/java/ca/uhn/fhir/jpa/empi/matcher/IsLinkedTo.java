package ca.uhn.fhir.jpa.empi.matcher;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
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
public class IsLinkedTo extends BasePersonMatcher {

	private List<Long> baseResourcePersonPids;
	private Long incomingResourcePersonPid;

	protected IsLinkedTo(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IAnyResource... theBaseResource) {
		super(theIdHelperService, theEmpiLinkDaoSvc, theBaseResource);
	}


	@Override
	protected boolean matchesSafely(IAnyResource theIncomingResource) {
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

	public static Matcher<IAnyResource> linkedTo(IdHelperService theIdHelperService, EmpiLinkDaoSvc theEmpiLinkDaoSvc, IAnyResource... theBaseResource) {
		return new IsLinkedTo(theIdHelperService, theEmpiLinkDaoSvc, theBaseResource);
	}
}
