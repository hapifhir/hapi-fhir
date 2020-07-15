package ca.uhn.fhir.jpa.empi.svc.candidate;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseCandidateFinder {
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	CandidateList findCandidates(IAnyResource theTarget) {
		CandidateList candidateList = new CandidateList(getStrategy());
		candidateList.addAll(findMatchPersonCandidates(theTarget));
		return candidateList;
	}

	protected abstract List<MatchedPersonCandidate> findMatchPersonCandidates(IAnyResource theTarget);

	protected abstract CandidateStrategyEnum getStrategy();
}
