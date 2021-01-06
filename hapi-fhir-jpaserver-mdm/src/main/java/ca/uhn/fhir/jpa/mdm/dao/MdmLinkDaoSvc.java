package ca.uhn.fhir.jpa.mdm.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
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

public class MdmLinkDaoSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private IMdmLinkDao myMdmLinkDao;
	@Autowired
	private MdmLinkFactory myMdmLinkFactory;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private FhirContext myFhirContext;

	@Transactional
	public MdmLink createOrUpdateLinkEntity(IBaseResource theGoldenResource, IBaseResource theSourceResource, MdmMatchOutcome theMatchOutcome, MdmLinkSourceEnum theLinkSource, @Nullable MdmTransactionContext theMdmTransactionContext) {
		Long goldenResourcePid = myIdHelperService.getPidOrNull(theGoldenResource);
		Long sourceResourcePid = myIdHelperService.getPidOrNull(theSourceResource);

		MdmLink mdmLink = getOrCreateMdmLinkByGoldenResourcePidAndSourceResourcePid(goldenResourcePid, sourceResourcePid);
		mdmLink.setLinkSource(theLinkSource);
		mdmLink.setMatchResult(theMatchOutcome.getMatchResultEnum());
		// Preserve these flags for link updates
		mdmLink.setEidMatch(theMatchOutcome.isEidMatch() | mdmLink.isEidMatchPresent());
		mdmLink.setHadToCreateNewGoldenResource(theMatchOutcome.isCreatedNewResource() | mdmLink.getHadToCreateNewGoldenResource());
		mdmLink.setMdmSourceType(myFhirContext.getResourceType(theSourceResource));
		if (mdmLink.getScore() != null) {
			mdmLink.setScore(Math.max(theMatchOutcome.score, mdmLink.getScore()));
		} else {
			mdmLink.setScore(theMatchOutcome.score);
		}

		String message = String.format("Creating MdmLink from %s to %s -> %s", theGoldenResource.getIdElement().toUnqualifiedVersionless(), theSourceResource.getIdElement().toUnqualifiedVersionless(), theMatchOutcome);
		theMdmTransactionContext.addTransactionLogMessage(message);
		ourLog.debug(message);
		save(mdmLink);
		return mdmLink;
	}

	@Nonnull
	public MdmLink getOrCreateMdmLinkByGoldenResourcePidAndSourceResourcePid(Long theGoldenResourcePid, Long theSourceResourcePid) {
		Optional<MdmLink> oExisting = getLinkByGoldenResourcePidAndSourceResourcePid(theGoldenResourcePid, theSourceResourcePid);
		if (oExisting.isPresent()) {
			return oExisting.get();
		} else {
			MdmLink newLink = myMdmLinkFactory.newMdmLink();
			newLink.setGoldenResourcePid(theGoldenResourcePid);
			newLink.setSourcePid(theSourceResourcePid);
			return newLink;
		}
	}

	public Optional<MdmLink> getLinkByGoldenResourcePidAndSourceResourcePid(Long theGoldenResourcePid, Long theSourceResourcePid) {
		if (theSourceResourcePid == null || theGoldenResourcePid == null) {
			return Optional.empty();
		}
		MdmLink link = myMdmLinkFactory.newMdmLink();
		link.setSourcePid(theSourceResourcePid);
		link.setGoldenResourcePid(theGoldenResourcePid);
		Example<MdmLink> example = Example.of(link);
		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Given a source resource Pid, and a match result, return all links that match these criteria.
	 *
	 * @param theSourcePid   the source of the relationship.
	 * @param theMatchResult the Match Result of the relationship
	 * @return a list of {@link MdmLink} entities matching these criteria.
	 */
	public List<MdmLink> getMdmLinksBySourcePidAndMatchResult(Long theSourcePid, MdmMatchResultEnum theMatchResult) {
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePid(theSourcePid);
		exampleLink.setMatchResult(theMatchResult);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Given a source Pid, return its Matched {@link MdmLink}. There can only ever be at most one of these, but its possible
	 * the source has no matches, and may return an empty optional.
	 *
	 * @param theSourcePid The Pid of the source you wish to find the matching link for.
	 * @return the {@link MdmLink} that contains the Match information for the source.
	 */
	public Optional<MdmLink> getMatchedLinkForSourcePid(Long theSourcePid) {
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePid(theSourcePid);
		exampleLink.setMatchResult(MdmMatchResultEnum.MATCH);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Given an IBaseResource, return its Matched {@link MdmLink}. There can only ever be at most one of these, but its possible
	 * the source has no matches, and may return an empty optional.
	 *
	 * @param theSourceResource The IBaseResource representing the source you wish to find the matching link for.
	 * @return the {@link MdmLink} that contains the Match information for the source.
	 */
	public Optional<MdmLink> getMatchedLinkForSource(IBaseResource theSourceResource) {
		Long pid = myIdHelperService.getPidOrNull(theSourceResource);
		if (pid == null) {
			return Optional.empty();
		}

		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePid(pid);
		exampleLink.setMatchResult(MdmMatchResultEnum.MATCH);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Given a golden resource a source and a match result, return the matching {@link MdmLink}, if it exists.
	 *
	 * @param theGoldenResourcePid The Pid of the Golden Resource in the relationship
	 * @param theSourcePid         The Pid of the source in the relationship
	 * @param theMatchResult       The MatchResult you are looking for.
	 * @return an Optional {@link MdmLink} containing the matched link if it exists.
	 */
	public Optional<MdmLink> getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(Long theGoldenResourcePid,
																											 Long theSourcePid, MdmMatchResultEnum theMatchResult) {
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setGoldenResourcePid(theGoldenResourcePid);
		exampleLink.setSourcePid(theSourcePid);
		exampleLink.setMatchResult(theMatchResult);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Get all {@link MdmLink} which have {@link MdmMatchResultEnum#POSSIBLE_DUPLICATE} as their match result.
	 *
	 * @return A list of {@link MdmLink} that hold potential duplicate golden resources.
	 */
	public List<MdmLink> getPossibleDuplicates() {
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	public Optional<MdmLink> findMdmLinkBySource(IBaseResource theSourceResource) {
		@Nullable Long pid = myIdHelperService.getPidOrNull(theSourceResource);
		if (pid == null) {
			return Optional.empty();
		}
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink().setSourcePid(pid);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Delete a given {@link MdmLink}. Note that this does not clear out the Golden resource.
	 * It is a simple entity delete.
	 *
	 * @param theMdmLink the {@link MdmLink} to delete.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteLink(MdmLink theMdmLink) {
		myMdmLinkDao.delete(theMdmLink);
	}

	/**
	 * Given a Golden Resource, return all links in which they are the source Golden Resource of the {@link MdmLink}
	 *
	 * @param theGoldenResource The {@link IBaseResource} Golden Resource who's links you would like to retrieve.
	 * @return A list of all {@link MdmLink} entities in which theGoldenResource is the source Golden Resource
	 */
	public List<MdmLink> findMdmLinksByGoldenResource(IBaseResource theGoldenResource) {
		Long pid = myIdHelperService.getPidOrNull(theGoldenResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink().setGoldenResourcePid(pid);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Delete all {@link MdmLink} entities, and return all resource PIDs from the source of the relationship.
	 *
	 * @return A list of Long representing the related Golden Resource Pids.
	 */
	@Transactional
	public List<Long> deleteAllMdmLinksAndReturnGoldenResourcePids() {
		List<MdmLink> all = myMdmLinkDao.findAll();
		return deleteMdmLinksAndReturnGoldenResourcePids(all);
	}

	private List<Long> deleteMdmLinksAndReturnGoldenResourcePids(List<MdmLink> theLinks) {
		Set<Long> goldenResources = theLinks.stream().map(MdmLink::getGoldenResourcePid).collect(Collectors.toSet());
		//TODO GGG this is probably invalid... we are essentially looking for GOLDEN -> GOLDEN links, which are either POSSIBLE_DUPLICATE
		//and REDIRECT
		goldenResources.addAll(theLinks.stream()
			.filter(link -> link.getMatchResult().equals(MdmMatchResultEnum.REDIRECT)
				|| link.getMatchResult().equals(MdmMatchResultEnum.POSSIBLE_DUPLICATE))
			.map(MdmLink::getSourcePid).collect(Collectors.toSet()));
		ourLog.info("Deleting {} MDM link records...", theLinks.size());
		myMdmLinkDao.deleteAll(theLinks);
		ourLog.info("{} MDM link records deleted", theLinks.size());
		return new ArrayList<>(goldenResources);
	}

	/**
	 * Given a valid {@link String}, delete all {@link MdmLink} entities for that type, and get the Pids
	 * for the Golden Resources which were the sources of the links.
	 *
	 * @param theSourceType the type of relationship you would like to delete.
	 * @return A list of longs representing the Pids of the Golden Resources resources used as the sources of the relationships that were deleted.
	 */
	public List<Long> deleteAllMdmLinksOfTypeAndReturnGoldenResourcePids(String theSourceType) {
		MdmLink link = new MdmLink();
		link.setMdmSourceType(theSourceType);
		Example<MdmLink> exampleLink = Example.of(link);
		List<MdmLink> allOfType = myMdmLinkDao.findAll(exampleLink);
		return deleteMdmLinksAndReturnGoldenResourcePids(allOfType);
	}

	/**
	 * Persist an MDM link to the database.
	 *
	 * @param theMdmLink the link to save.
	 * @return the persisted {@link MdmLink} entity.
	 */
	public MdmLink save(MdmLink theMdmLink) {
		if (theMdmLink.getCreated() == null) {
			theMdmLink.setCreated(new Date());
		}
		theMdmLink.setUpdated(new Date());
		return myMdmLinkDao.save(theMdmLink);
	}


	/**
	 * Given an example {@link MdmLink}, return all links from the database which match the example.
	 *
	 * @param theExampleLink The MDM link containing the data we would like to search for.
	 * @return a list of {@link MdmLink} entities which match the example.
	 */
	public List<MdmLink> findMdmLinkByExample(Example<MdmLink> theExampleLink) {
		return myMdmLinkDao.findAll(theExampleLink);
	}

	/**
	 * Given a source {@link IBaseResource}, return all {@link MdmLink} entities in which this source is the source
	 * of the relationship. This will show you all links for a given Patient/Practitioner.
	 *
	 * @param theSourceResource the source resource to find links for.
	 * @return all links for the source.
	 */
	public List<MdmLink> findMdmLinksBySourceResource(IBaseResource theSourceResource) {
		Long pid = myIdHelperService.getPidOrNull(theSourceResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink().setSourcePid(pid);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Finds all {@link MdmLink} entities in which theGoldenResource's PID is the source
	 * of the relationship.
	 *
	 * @param theGoldenResource the source resource to find links for.
	 * @return all links for the source.
	 */
	public List<MdmLink> findMdmMatchLinksByGoldenResource(IBaseResource theGoldenResource) {
		Long pid = myIdHelperService.getPidOrNull(theGoldenResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink().setGoldenResourcePid(pid);
		exampleLink.setMatchResult(MdmMatchResultEnum.MATCH);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Factory delegation method, whenever you need a new MdmLink, use this factory method.
	 * //TODO Should we make the constructor private for MdmLink? or work out some way to ensure they can only be instantiated via factory.
	 *
	 * @return A new {@link MdmLink}.
	 */
	public MdmLink newMdmLink() {
		return myMdmLinkFactory.newMdmLink();
	}

}
