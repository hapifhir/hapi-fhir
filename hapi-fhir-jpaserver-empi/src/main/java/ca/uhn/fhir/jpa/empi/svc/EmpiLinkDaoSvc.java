package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.empi.dao.IEmpiLinkDao;
import ca.uhn.fhir.jpa.empi.entity.EmpiLink;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
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

		EmpiLink empiLink = getOrCreateEmpiLinkByPersonPidAndTargetPid(personPid, resourcePid);

		empiLink.setLinkSource(theLinkSource);
		empiLink.setMatchResult(theMatchResult);
		ourLog.debug("Creating EmpiLink from {} to {} -> {}", thePerson.getIdElement(), theResource.getIdElement(), theMatchResult);
		myEmpiLinkDao.save(empiLink);
	}



	@NotNull
	public EmpiLink getOrCreateEmpiLinkByPersonPidAndTargetPid(Long thePersonPid, Long theResourcePid) {
		EmpiLink existing = getLinkByPersonPidAndTargetPid(thePersonPid, theResourcePid);
		if (existing != null) {
			return existing;
		} else {
			EmpiLink empiLink = new EmpiLink();
			empiLink.setPersonPid(thePersonPid);
			empiLink.setTargetPid(theResourcePid);
			return empiLink;
		}
	}

	public EmpiLink getLinkByPersonPidAndTargetPid(Long thePersonPid, Long theTargetPid) {

		if (theTargetPid == null || thePersonPid == null) {
			return null;
		}
		EmpiLink link = new EmpiLink();
		link.setTargetPid(theTargetPid);
		link.setPersonPid(thePersonPid);
		Example<EmpiLink> example = Example.of(link);
		return myEmpiLinkDao.findOne(example).orElse(null);
	}

	public List<EmpiLink> getEmpiLinksByTargetPidAndMatchResult(Long theTargetPid, EmpiMatchResultEnum theMatchResult) {
		EmpiLink exampleLink = new EmpiLink();
		exampleLink.setTargetPid(theTargetPid);
		exampleLink.setMatchResult(theMatchResult);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findAll(example);
	}

	public Optional<EmpiLink> getMatchedLinkForTargetPid(Long theTargetPid) {
		EmpiLink exampleLink = new EmpiLink();
		exampleLink.setTargetPid(theTargetPid);
		exampleLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findOne(example);

	}

	public Optional<EmpiLink> getEmpiLinksByPersonPidTargetPidAndMatchResult(Long thePersonPid, Long theTargetPid, EmpiMatchResultEnum theMatchResult) {
		EmpiLink exampleLink = new EmpiLink();
		exampleLink.setPersonPid(thePersonPid);
		exampleLink.setTargetPid(theTargetPid);
		exampleLink.setMatchResult(theMatchResult);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findOne(example);
	}
}
