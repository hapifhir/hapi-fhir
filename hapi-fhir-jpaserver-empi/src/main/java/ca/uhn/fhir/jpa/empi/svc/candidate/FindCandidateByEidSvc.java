package ca.uhn.fhir.jpa.empi.svc.candidate;

import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.CanonicalEID;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.jpa.empi.svc.EmpiResourceDaoSvc;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FindCandidateByEidSvc extends BaseCandidateFinder {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private EIDHelper myEIDHelper;
	@Autowired
	private EmpiResourceDaoSvc myEmpiResourceDaoSvc;

	protected List<MatchedPersonCandidate> findMatchPersonCandidates(IAnyResource theBaseResource) {
		List<MatchedPersonCandidate> retval = new ArrayList<>();

		List<CanonicalEID> eidFromResource = myEIDHelper.getExternalEid(theBaseResource);
		if (!eidFromResource.isEmpty()) {
			for (CanonicalEID eid : eidFromResource) {
				Optional<IAnyResource> oFoundPerson = myEmpiResourceDaoSvc.searchPersonByEid(eid.getValue());
				if (oFoundPerson.isPresent()) {
					IAnyResource foundPerson = oFoundPerson.get();
					Long pidOrNull = myIdHelperService.getPidOrNull(foundPerson);
					MatchedPersonCandidate mpc = new MatchedPersonCandidate(new ResourcePersistentId(pidOrNull), EmpiMatchOutcome.EID_MATCH);
					ourLog.debug("Matched {} by EID {}", foundPerson.getIdElement(), eid);
					retval.add(mpc);
				}
			}
		}
		return retval;
	}

	@Override
	protected CandidateStrategyEnum getStrategy() {
		return CandidateStrategyEnum.EID;
	}
}
