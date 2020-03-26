package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.helper.ResourceTableHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmpiPersonFindingSvc {

	@Autowired
	IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	ResourceTableHelper myResourceTableHelper;

	public List<MatchedPersonCandidate> findPersonCandidates(IBaseResource theBaseResource) {
	 // 1. First, check link table for any entries where this baseresource is the target of a person. If found, return.
		EmpiLink empiLink = new EmpiLink().setTargetPid(myResourceTableHelper.getPidOrNull(theBaseResource));

		Example<EmpiLink> example = Example.of(empiLink);
		Optional<EmpiLink> oLink = myEmpiLinkDao.findOne(example);
		if (oLink.isPresent()) {
			return Collections.singletonList(new MatchedPersonCandidate(oLink.get().getPerson(), oLink.get()));
		}
		return Collections.emptyList();
		// 2. Next, find Person resources that link to this resource.
		// 3. Next, find Person resource candidates and perform matching to narrow them down.
		// 4. NExt, find Patient/Pract resource candidates, then perform matching to narrow them down. Then see if any of those
		// candidates point to any persons via either link table or person records.
		// 5. If there are still no candidates, create a Person and populate it with theBaseResource information.
	}

}
