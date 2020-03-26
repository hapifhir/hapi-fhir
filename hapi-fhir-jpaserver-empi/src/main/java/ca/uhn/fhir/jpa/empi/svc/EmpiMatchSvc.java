package ca.uhn.fhir.jpa.empi.svc;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EmpiMatchSvc {
	@Autowired
	EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;

	public void updatePatientLinks(IBaseResource theResource) {
		Collection<IBaseResource> candidates = myEmpiCandidateSearchSvc.findCandidates("Patient", theResource);
	}
}
