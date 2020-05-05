package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpiPersonMergerSvcImpl implements IEmpiPersonMergerSvc {
	@Autowired
	PersonHelper myPersonHelper;
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	EmpiResourceDaoSvc myEmpiResourceDaoSvc;

	@Override
	@Transactional
	public IAnyResource mergePersons(IAnyResource thePersonToDelete, IAnyResource thePersonToKeep) {
		myPersonHelper.mergePersonFields(thePersonToDelete, thePersonToKeep);
		mergeLinks(thePersonToDelete, thePersonToKeep);
		myEmpiResourceDaoSvc.updatePerson(thePersonToKeep);
		return thePersonToKeep;
	}

	private void mergeLinks(IAnyResource thePersonToDelete, IAnyResource thePersonToKeep) {
		long personToKeepPid = myIdHelperService.getPidOrThrowException(thePersonToKeep);
		List<EmpiLink> newLinks = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(thePersonToDelete);
		List<EmpiLink> oldLinks = myEmpiLinkDaoSvc.findEmpiLinksByPersonId(thePersonToKeep);
		newLinks.removeIf(newLink -> oldLinks.stream().anyMatch(oldLink -> newLink.getTargetPid().equals(oldLink.getTargetPid())));
		for (EmpiLink newLink : newLinks) {
			newLink.setPersonPid(personToKeepPid);
			myEmpiLinkDaoSvc.update(newLink);
		}
	}
}
