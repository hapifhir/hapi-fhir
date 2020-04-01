package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Lazy
@Service
public class EmpiLinkDaoSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiLinkDaoSvc.class);

	@Autowired
	private IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	private ResourceTableHelper myResourceTableHelper;

	public void createOrUpdateLinkEntity(IBaseResource thePerson, IBaseResource theResource, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource) {
		Long personPid = myResourceTableHelper.getPidOrNull(thePerson);
		Long resourcePid = myResourceTableHelper.getPidOrNull(theResource);

		EmpiLink empiLink = getEmpiLinkByTargetPid(personPid, resourcePid);

		empiLink.setLinkSource(theLinkSource);
		empiLink.setMatchResult(theMatchResult);
		ourLog.debug("Creating EmpiLink from {} to {} -> {}", thePerson.getIdElement(), theResource.getIdElement(), theMatchResult);
		myEmpiLinkDao.save(empiLink);
	}



	@NotNull
	public EmpiLink getEmpiLinkByTargetPid(Long thePersonPid, Long theResourcePid) {
		EmpiLink empiLink = new EmpiLink();
		empiLink.setPersonPid(thePersonPid);
		empiLink.setTargetPid(theResourcePid);
		Example<EmpiLink> example = Example.of(empiLink);
		Optional<EmpiLink> found = myEmpiLinkDao.findOne(example);
		if (found.isPresent()) {
			empiLink = found.get();
		}
		return empiLink;
	}

	public EmpiLink getLinkByTargetResourceId(Long theResourcePid) {
		if (theResourcePid == null) {
			return null;
		}
		EmpiLink link = new EmpiLink();
		link.setTargetPid(theResourcePid);
		Example<EmpiLink> example = Example.of(link);
		return myEmpiLinkDao.findOne(example).orElse(null);
	}
}
