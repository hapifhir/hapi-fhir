package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.jpa.dao.data.IEmpiLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmpiLinkDaoSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiLinkDaoSvc.class);

	@Autowired
	private IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	private IdHelperService myIdHelperService;

	public EmpiLink createOrUpdateLinkEntity(IBaseResource thePerson, IBaseResource theResource, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource, @Nullable EmpiTransactionContext theEmpiTransactionContext) {
		Long personPid = myIdHelperService.getPidOrNull(thePerson);
		Long resourcePid = myIdHelperService.getPidOrNull(theResource);

		EmpiLink empiLink = getOrCreateEmpiLinkByPersonPidAndTargetPid(personPid, resourcePid);
		empiLink.setLinkSource(theLinkSource);
		empiLink.setMatchResult(theMatchResult);

		String message = String.format("Creating EmpiLink from %s to %s -> %s", thePerson.getIdElement().toUnqualifiedVersionless(), theResource.getIdElement().toUnqualifiedVersionless(), theMatchResult);
		theEmpiTransactionContext.addTransactionLogMessage(message);
		ourLog.debug(message);
		myEmpiLinkDao.save(empiLink);
		return empiLink;
	}


	@Nonnull
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

	public Optional<EmpiLink> getMatchedLinkForTarget(IBaseResource theTarget) {
		Long pid = myIdHelperService.getPidOrNull(theTarget);
		if (pid == null) {
			return Optional.empty();
		}

		EmpiLink exampleLink = new EmpiLink();
		exampleLink.setTargetPid(pid);
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

	/**
	 * Get all {@link EmpiLink} which have {@link EmpiMatchResultEnum#POSSIBLE_DUPLICATE} as their match result.
	 *
	 * @return A list of EmpiLinks that hold potential duplicate persons.
	 */
	public List<EmpiLink> getPossibleDuplicates() {
		EmpiLink exampleLink = new EmpiLink();
		exampleLink.setMatchResult(EmpiMatchResultEnum.POSSIBLE_DUPLICATE);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findAll(example);
	}

	public Optional<EmpiLink> findEmpiLinkByTargetId(IBaseResource theTargetResource) {
		@Nullable Long pid = myIdHelperService.getPidOrNull(theTargetResource);
		if (pid == null) {
			return Optional.empty();
		}
		EmpiLink empiLink = new EmpiLink().setTargetPid(pid);
		Example<EmpiLink> example = Example.of(empiLink);
		return myEmpiLinkDao.findOne(example);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteLink(EmpiLink theEmpiLink) {
		myEmpiLinkDao.delete(theEmpiLink);
	}

	/**
	 * Delete all EmpiLink records with any reference to this resource.  (Used by Expunge.)
	 * @param theResource
	 * @return the number of records deleted
	 */
	public int deleteWithAnyReferenceTo(IBaseResource theResource) {
		Long pid = myIdHelperService.getPidOrThrowException(theResource.getIdElement(), null);
		int removed =  myEmpiLinkDao.deleteWithAnyReferenceToPid(pid);
		if (removed > 0) {
			ourLog.info("Removed {} references to {}", removed, theResource.getIdElement());
		}
		return removed;
	}

	public List<EmpiLink> findEmpiLinksByPersonId(IBaseResource thePersonResource) {
		@Nullable Long pid = myIdHelperService.getPidOrNull(thePersonResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		EmpiLink empiLink = new EmpiLink().setPersonPid(pid);
		Example<EmpiLink> example = Example.of(empiLink);
		return myEmpiLinkDao.findAll(example);
	}

	public void update(EmpiLink theEmpiLink) {
		myEmpiLinkDao.save(theEmpiLink);
	}
}
