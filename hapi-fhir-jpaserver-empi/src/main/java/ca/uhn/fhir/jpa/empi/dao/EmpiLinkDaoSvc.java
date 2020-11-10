package ca.uhn.fhir.jpa.empi.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.jpa.dao.data.IEmpiLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EmpiLinkDaoSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private IEmpiLinkDao myEmpiLinkDao;
	@Autowired
	private EmpiLinkFactory myEmpiLinkFactory;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private FhirContext myFhirContext;

	@Transactional
	public EmpiLink createOrUpdateLinkEntity(IBaseResource theSourceResource, IBaseResource theTargetResource, EmpiMatchOutcome theMatchOutcome, EmpiLinkSourceEnum theLinkSource, @Nullable EmpiTransactionContext theEmpiTransactionContext) {
		Long sourceResourcePid = myIdHelperService.getPidOrNull(theSourceResource);
		Long targetResourcePid = myIdHelperService.getPidOrNull(theTargetResource);

		EmpiLink empiLink = getOrCreateEmpiLinkBySourceResourcePidAndTargetResourcePid(sourceResourcePid, targetResourcePid);
		empiLink.setLinkSource(theLinkSource);
		empiLink.setMatchResult(theMatchOutcome.getMatchResultEnum());
		// Preserve these flags for link updates
		empiLink.setEidMatch(theMatchOutcome.isEidMatch() | empiLink.isEidMatch());
		empiLink.setHadToCreateNewResource(theMatchOutcome.isNewPerson() | empiLink.getHadToCreateNewResource());
		empiLink.setEmpiTargetType(myFhirContext.getResourceType(theTargetResource));
		if (empiLink.getScore() != null) {
			empiLink.setScore(Math.max(theMatchOutcome.score, empiLink.getScore()));
		} else {
			empiLink.setScore(theMatchOutcome.score);
		}

		String message = String.format("Creating EmpiLink from %s to %s -> %s", theSourceResource.getIdElement().toUnqualifiedVersionless(), theTargetResource.getIdElement().toUnqualifiedVersionless(), theMatchOutcome);
		theEmpiTransactionContext.addTransactionLogMessage(message);
		ourLog.debug(message);
		save(empiLink);
		return empiLink;
	}

	@Nonnull
	public EmpiLink getOrCreateEmpiLinkBySourceResourcePidAndTargetResourcePid(Long theSourceResourcePid, Long theTargetResourcePid) {
		Optional<EmpiLink> oExisting = getLinkBySourceResourcePidAndTargetResourcePid(theSourceResourcePid, theTargetResourcePid);
		if (oExisting.isPresent()) {
			return oExisting.get();
		} else {
			EmpiLink newLink = myEmpiLinkFactory.newEmpiLink();
			newLink.setSourceResourcePid(theSourceResourcePid);
			newLink.setPersonPid(theSourceResourcePid);
			newLink.setTargetPid(theTargetResourcePid);
			return newLink;
		}
	}

	public Optional<EmpiLink> getLinkBySourceResourcePidAndTargetResourcePid(Long theSourceResourcePid, Long theTargetResourcePid) {
		if (theTargetResourcePid == null || theSourceResourcePid == null) {
			return Optional.empty();
		}
		EmpiLink link = myEmpiLinkFactory.newEmpiLink();
		link.setTargetPid(theTargetResourcePid);
		link.setSourceResourcePid(theSourceResourcePid);
		Example<EmpiLink> example = Example.of(link);
		return myEmpiLinkDao.findOne(example);
	}

	/**
	 * Given a Target Pid, and a match result, return all links that match these criteria.
	 *
	 * @param theTargetPid   the target of the relationship.
	 * @param theMatchResult the Match Result of the relationship
	 * @return a list of {@link EmpiLink} entities matching these criteria.
	 */
	public List<EmpiLink> getEmpiLinksByTargetPidAndMatchResult(Long theTargetPid, EmpiMatchResultEnum theMatchResult) {
		EmpiLink exampleLink = myEmpiLinkFactory.newEmpiLink();
		exampleLink.setTargetPid(theTargetPid);
		exampleLink.setMatchResult(theMatchResult);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findAll(example);
	}

	/**
	 * Given a target Pid, return its Matched EmpiLink. There can only ever be at most one of these, but its possible
	 * the target has no matches, and may return an empty optional.
	 *
	 * @param theTargetPid The Pid of the target you wish to find the matching link for.
	 * @return the {@link EmpiLink} that contains the Match information for the target.
	 */
	public Optional<EmpiLink> getMatchedLinkForTargetPid(Long theTargetPid) {
		EmpiLink exampleLink = myEmpiLinkFactory.newEmpiLink();
		exampleLink.setTargetPid(theTargetPid);
		exampleLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findOne(example);
	}

	/**
	 * Given an IBaseResource, return its Matched EmpiLink. There can only ever be at most one of these, but its possible
	 * the target has no matches, and may return an empty optional.
	 *
	 * @param theTarget The IBaseResource representing the target you wish to find the matching link for.
	 * @return the {@link EmpiLink} that contains the Match information for the target.
	 */
	public Optional<EmpiLink> getMatchedLinkForTarget(IBaseResource theTarget) {
		Long pid = myIdHelperService.getPidOrNull(theTarget);
		if (pid == null) {
			return Optional.empty();
		}

		EmpiLink exampleLink = myEmpiLinkFactory.newEmpiLink();
		exampleLink.setTargetPid(pid);
		exampleLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findOne(example);
	}

	/**
	 * Given a person a target and a match result, return the matching EmpiLink, if it exists.
	 *
	 * @param thePersonPid   The Pid of the Person in the relationship
	 * @param theTargetPid   The Pid of the target in the relationship
	 * @param theMatchResult The MatchResult you are looking for.
	 * @return an Optional {@link EmpiLink} containing the matched link if it exists.
	 */
	public Optional<EmpiLink> getEmpiLinksByPersonPidTargetPidAndMatchResult(Long thePersonPid, Long theTargetPid, EmpiMatchResultEnum theMatchResult) {
		EmpiLink exampleLink = myEmpiLinkFactory.newEmpiLink();
		exampleLink.setSourceResourcePid(thePersonPid);
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
		EmpiLink exampleLink = myEmpiLinkFactory.newEmpiLink();
		exampleLink.setMatchResult(EmpiMatchResultEnum.POSSIBLE_DUPLICATE);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findAll(example);
	}

	public Optional<EmpiLink> findEmpiLinkByTarget(IBaseResource theTargetResource) {
		@Nullable Long pid = myIdHelperService.getPidOrNull(theTargetResource);
		if (pid == null) {
			return Optional.empty();
		}
		EmpiLink exampleLink = myEmpiLinkFactory.newEmpiLink().setTargetPid(pid);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findOne(example);
	}

	/**
	 * Delete a given EmpiLink. Note that this does not clear out the Person, or the Person's related links.
	 * It is a simple entity delete.
	 *
	 * @param theEmpiLink the EmpiLink to delete.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteLink(EmpiLink theEmpiLink) {
		myEmpiLinkDao.delete(theEmpiLink);
	}

	/**
	 * Given a Person, return all links in which they are the source Person of the {@link EmpiLink}
	 *
	 * @param thePersonResource The {@link IBaseResource} Person who's links you would like to retrieve.
	 * @return A list of all {@link EmpiLink} entities in which thePersonResource is the source Person.
	 */
	public List<EmpiLink> findEmpiLinksBySourceResource(IBaseResource thePersonResource) {
		Long pid = myIdHelperService.getPidOrNull(thePersonResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		EmpiLink exampleLink = myEmpiLinkFactory.newEmpiLink().setSourceResourcePid(pid);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findAll(example);
	}

	/**
	 * Delete all {@link EmpiLink} entities, and return all resource PIDs from the source of the relationship.
	 *
	 * @return A list of Long representing the related Person Pids.
	 */
	@Transactional
	public List<Long> deleteAllEmpiLinksAndReturnPersonPids() {
		List<EmpiLink> all = myEmpiLinkDao.findAll();
		return deleteEmpiLinksAndReturnPersonPids(all);
	}

	private List<Long> deleteEmpiLinksAndReturnPersonPids(List<EmpiLink> theLinks) {
		Set<Long> persons = theLinks.stream().map(EmpiLink::getSourceResourcePid).collect(Collectors.toSet());
		persons.addAll(theLinks.stream().filter(link -> "Person".equals(link.getEmpiTargetType())).map(EmpiLink::getTargetPid).collect(Collectors.toSet()));
		ourLog.info("Deleting {} EMPI link records...", theLinks.size());
		myEmpiLinkDao.deleteAll(theLinks);
		ourLog.info("{} EMPI link records deleted", theLinks.size());
		return new ArrayList<>(persons);
	}

	/**
	 * Given a valid {@link String}, delete all {@link EmpiLink} entities for that type, and get the Pids
	 * for the Person resources which were the sources of the links.
	 *
	 * @param theTargetType the type of relationship you would like to delete.
	 * @return A list of longs representing the Pids of the Person resources used as the sources of the relationships that were deleted.
	 */
	public List<Long> deleteAllEmpiLinksOfTypeAndReturnPersonPids(String theTargetType) {
		EmpiLink link = new EmpiLink();
		link.setEmpiTargetType(theTargetType);
		Example<EmpiLink> exampleLink = Example.of(link);
		List<EmpiLink> allOfType = myEmpiLinkDao.findAll(exampleLink);
		return deleteEmpiLinksAndReturnPersonPids(allOfType);
	}

	/**
	 * Persist an EmpiLink to the database.
	 *
	 * @param theEmpiLink the link to save.
	 * @return the persisted {@link EmpiLink} entity.
	 */
	public EmpiLink save(EmpiLink theEmpiLink) {
		if (theEmpiLink.getCreated() == null) {
			theEmpiLink.setCreated(new Date());
		}
		theEmpiLink.setUpdated(new Date());
		return myEmpiLinkDao.save(theEmpiLink);
	}


	/**
	 * Given an example {@link EmpiLink}, return all links from the database which match the example.
	 *
	 * @param theExampleLink The EmpiLink containing the data we would like to search for.
	 * @return a list of {@link EmpiLink} entities which match the example.
	 */
	public List<EmpiLink> findEmpiLinkByExample(Example<EmpiLink> theExampleLink) {
		return myEmpiLinkDao.findAll(theExampleLink);
	}

	/**
	 * Given a target {@link IBaseResource}, return all {@link EmpiLink} entities in which this target is the target
	 * of the relationship. This will show you all links for a given Patient/Practitioner.
	 *
	 * @param theTargetResource the target resource to find links for.
	 * @return all links for the target.
	 */
	public List<EmpiLink> findEmpiLinksByTarget(IBaseResource theTargetResource) {
		Long pid = myIdHelperService.getPidOrNull(theTargetResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		EmpiLink exampleLink = myEmpiLinkFactory.newEmpiLink().setTargetPid(pid);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findAll(example);
	}


	/**
	 * Finds all {@link EmpiLink} entities in which theSourceResource's PID is the source
	 * of the relationship.
	 *
	 * @param theSourceResource the source resource to find links for.
	 * @return all links for the source.
	 */
	public List<EmpiLink> findEmpiMatchLinksBySource(IBaseResource theSourceResource) {
		Long pid = myIdHelperService.getPidOrNull(theSourceResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		EmpiLink exampleLink = myEmpiLinkFactory.newEmpiLink().setSourceResourcePid(pid);
		exampleLink.setMatchResult(EmpiMatchResultEnum.MATCH);
		Example<EmpiLink> example = Example.of(exampleLink);
		return myEmpiLinkDao.findAll(example);
	}

	/**
	 * Factory delegation method, whenever you need a new EmpiLink, use this factory method.
	 * //TODO Should we make the constructor private for EmpiLink? or work out some way to ensure they can only be instantiated via factory.
	 *
	 * @return A new {@link EmpiLink}.
	 */
	public EmpiLink newEmpiLink() {
		return myEmpiLinkFactory.newEmpiLink();
	}

//	@Transactional
//	public List<EmpiLink> deleteEmpiLinks(IAnyResource theSourceResource, IAnyResource theTargetResource) {
//		Long sourceResourcePid = myIdHelperService.getPidOrNull(theSourceResource);
//		if (sourceResourcePid == null) {
//			if (ourLog.isDebugEnabled()) {
//				ourLog.debug(String.format("Unable to find source resource for ID %s, no links deleted", sourceResourcePid));
//			}
//			return new ArrayList<>();
//		}
//
//		Long targetResourcePid = myIdHelperService.getPidOrNull(theSourceResource);
//		if (targetResourcePid == null) {
//			if (ourLog.isDebugEnabled()) {
//				ourLog.debug(String.format("Unable to find target resource for ID %s, no links deleted", targetResourcePid));
//			}
//
//			return new ArrayList<>();
//		}
//
//		EmpiLink exampleLink = myEmpiLinkFactory
//			.newEmpiLink()
//			.setSourceResourcePid(sourceResourcePid)
//			.setTargetPid(targetResourcePid);
//
//		Example<EmpiLink> example = Example.of(exampleLink);
//		List<EmpiLink> linksToDelete = myEmpiLinkDao.findAll(example);
//		myEmpiLinkDao.deleteAll(linksToDelete);
//		return linksToDelete;
//	}
}
