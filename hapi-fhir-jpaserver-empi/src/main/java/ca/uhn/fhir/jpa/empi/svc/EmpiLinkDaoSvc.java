package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.jpa.helper.ResourceTableHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpiLinkDaoSvc {
	@Autowired
	IEmpiLinkDao myEmpiLinkDao;

	public void createOrUpdateLinkEntity(IBaseResource thePerson, IBaseResource theResource, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource) {
		Long personPid = ResourceTableHelper.getPidOrNull(thePerson);
		Long resourcePid = ResourceTableHelper.getPidOrNull(theResource);

		EmpiLink empiLink = new EmpiLink();
		empiLink.setPersonPid(personPid);
		empiLink.setTargetPid(resourcePid);
		Example<EmpiLink> example = Example.of(empiLink);
		Optional<EmpiLink> found = myEmpiLinkDao.findOne(example);
		if (found.isPresent()) {
			empiLink = found.get();
		}
		empiLink.setLinkSource(theLinkSource);
		empiLink.setMatchResult(theMatchResult);
		myEmpiLinkDao.save(empiLink);
	}
}
