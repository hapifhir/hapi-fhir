package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.rules.svc.EmpiRulesSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpiCandidateSearchSvc {
	@Autowired
	EmpiRulesSvc myEmpiRulesSvc;

	public List<IBaseResource> findCandidates(IBaseResource theResource) {
		// FIXME EMPI implement
		return null;
	}
}
