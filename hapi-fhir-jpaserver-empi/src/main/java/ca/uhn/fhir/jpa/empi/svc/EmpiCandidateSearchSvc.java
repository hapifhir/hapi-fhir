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

	/**
	 * Given a target resource, search for all resources that are considered an EMPI match based on locally defined
	 * EMPI rules.
	 *
	 * @param theResource the target resource we are attempting to match.
	 * @return the list of candidate resources which could be matches to theResource
	 */
	public List<IBaseResource> findCandidates(IBaseResource theResource) {
		// FIXME EMPI implement
		return null;
	}
}
