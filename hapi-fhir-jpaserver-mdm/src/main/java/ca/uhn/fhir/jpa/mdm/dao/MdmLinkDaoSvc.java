package ca.uhn.fhir.jpa.mdm.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.dao.MdmLinkFactory;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MdmLinkDaoSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private IMdmLinkDao myMdmLinkDao;
	@Autowired
	private MdmLinkFactory myMdmLinkFactory;
	@Autowired
	private IIdHelperService myIdHelperService;
	@Autowired
	private FhirContext myFhirContext;

	@Transactional
	public IMdmLink createOrUpdateLinkEntity(IAnyResource theGoldenResource, IAnyResource theSourceResource, MdmMatchOutcome theMatchOutcome, MdmLinkSourceEnum theLinkSource, @Nullable MdmTransactionContext theMdmTransactionContext) {
		IMdmLink mdmLink = getOrCreateMdmLinkByGoldenResourceAndSourceResource(theGoldenResource, theSourceResource);
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
		// Add partition for the mdm link if it's available in the source resource
		RequestPartitionId partitionId = (RequestPartitionId) theSourceResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (partitionId != null && partitionId.getFirstPartitionIdOrNull() != null) {
			mdmLink.setPartitionId(new PartitionablePartitionId(partitionId.getFirstPartitionIdOrNull(), partitionId.getPartitionDate()));
		}

		String message = String.format("Creating %s link from %s to Golden Resource %s.", mdmLink.getMatchResult(), theSourceResource.getIdElement().toUnqualifiedVersionless(), theGoldenResource.getIdElement().toUnqualifiedVersionless());
		theMdmTransactionContext.addTransactionLogMessage(message);
		ourLog.debug(message);
		save(mdmLink);
		return mdmLink;
	}

	@Nonnull
	public IMdmLink getOrCreateMdmLinkByGoldenResourceAndSourceResource(
		IAnyResource theGoldenResource, IAnyResource theSourceResource
	) {
		ResourcePersistentId goldenResourcePid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource);
		ResourcePersistentId sourceResourcePid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		Optional<? extends IMdmLink> oExisting = getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourcePid, sourceResourcePid);
		if (oExisting.isPresent()) {
			return oExisting.get();
		} else {
			IMdmLink newLink = myMdmLinkFactory.newMdmLink();
			newLink.setGoldenResourcePersistenceId(goldenResourcePid);
			newLink.setSourcePersistenceId(sourceResourcePid);
			return newLink;
		}
	}

	/**
	 * Given a golden resource Pid and source Pid, return the mdm link that matches these criterias if exists
	 * @deprecated This was deprecated in favour of using ResourcePersistenceId rather than longs
	 * @param theGoldenResourcePid
	 * @param theSourceResourcePid
	 * @return
	 */
	@Deprecated
	public Optional<? extends IMdmLink> getLinkByGoldenResourcePidAndSourceResourcePid(Long theGoldenResourcePid, Long theSourceResourcePid) {
		return getLinkByGoldenResourcePidAndSourceResourcePid(new ResourcePersistentId(theGoldenResourcePid), new ResourcePersistentId(theSourceResourcePid));
	}

	/**
	 * Given a golden resource Pid and source Pid, return the mdm link that matches these criterias if exists
	 * @param theGoldenResourcePid The ResourcePersistenceId of the golden resource
	 * @param theSourceResourcePid The ResourcepersistenceId of the Source resource
	 * @return The {@link IMdmLink} entity that matches these criteria if exists
	 */
	@SuppressWarnings("unchecked")
	public Optional<? extends IMdmLink> getLinkByGoldenResourcePidAndSourceResourcePid(ResourcePersistentId theGoldenResourcePid, ResourcePersistentId theSourceResourcePid) {
		if (theSourceResourcePid == null || theGoldenResourcePid == null) {
			return Optional.empty();
		}
		IMdmLink link = myMdmLinkFactory.newMdmLink();
		link.setSourcePersistenceId(theSourceResourcePid);
		link.setGoldenResourcePersistenceId(theGoldenResourcePid);

		//TODO - replace the use of example search
		Example<? extends IMdmLink> example = Example.of(link);

		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Given a source resource Pid, and a match result, return all links that match these criteria.
	 *
	 * @param theSourcePid   the source of the relationship.
	 * @param theMatchResult the Match Result of the relationship
	 * @return a list of {@link IMdmLink} entities matching these criteria.
	 */
	public List<? extends IMdmLink> getMdmLinksBySourcePidAndMatchResult(ResourcePersistentId theSourcePid, MdmMatchResultEnum theMatchResult) {
		IMdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePersistenceId(theSourcePid);
		exampleLink.setMatchResult(theMatchResult);
		Example<? extends IMdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Given a source Pid, return its Matched {@link IMdmLink}. There can only ever be at most one of these, but its possible
	 * the source has no matches, and may return an empty optional.
	 *
	 * @param theSourcePid The Pid of the source you wish to find the matching link for.
	 * @return the {@link IMdmLink} that contains the Match information for the source.
	 */
	@Deprecated
	@Transactional
	public Optional<? extends IMdmLink> getMatchedLinkForSourcePid(ResourcePersistentId theSourcePid) {
		return myMdmLinkDao.findBySourcePidAndMatchResult(theSourcePid, MdmMatchResultEnum.MATCH);
	}

	/**
	 * Given an IBaseResource, return its Matched {@link IMdmLink}. There can only ever be at most one of these, but its possible
	 * the source has no matches, and may return an empty optional.
	 *
	 * @param theSourceResource The IBaseResource representing the source you wish to find the matching link for.
	 * @return the {@link IMdmLink} that contains the Match information for the source.
	 */
	public Optional<? extends IMdmLink> getMatchedLinkForSource(IBaseResource theSourceResource) {
		return getMdmLinkWithMatchResult(theSourceResource, MdmMatchResultEnum.MATCH);
	}

	public Optional<? extends IMdmLink> getPossibleMatchedLinkForSource(IBaseResource theSourceResource) {
		return getMdmLinkWithMatchResult(theSourceResource, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Nonnull
	private Optional<? extends IMdmLink> getMdmLinkWithMatchResult(IBaseResource theSourceResource, MdmMatchResultEnum theMatchResult) {
		ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		if (pid == null) {
			return Optional.empty();
		}

		IMdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePersistenceId(pid);
		exampleLink.setMatchResult(theMatchResult);
		Example<? extends IMdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Given a golden resource a source and a match result, return the matching {@link IMdmLink}, if it exists.
	 *
	 * @param theGoldenResourcePid The Pid of the Golden Resource in the relationship
	 * @param theSourcePid         The Pid of the source in the relationship
	 * @param theMatchResult       The MatchResult you are looking for.
	 * @return an Optional {@link IMdmLink} containing the matched link if it exists.
	 */
	public Optional<? extends IMdmLink> getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(Long theGoldenResourcePid,
																															Long theSourcePid, MdmMatchResultEnum theMatchResult) {
		return getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(new ResourcePersistentId(theGoldenResourcePid), new ResourcePersistentId(theSourcePid), theMatchResult);
	}

	public Optional<? extends IMdmLink> getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(ResourcePersistentId theGoldenResourcePid,
																											 ResourcePersistentId theSourcePid, MdmMatchResultEnum theMatchResult) {
		IMdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setGoldenResourcePersistenceId(theGoldenResourcePid);
		exampleLink.setSourcePersistenceId(theSourcePid);
		exampleLink.setMatchResult(theMatchResult);
		Example<? extends IMdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Get all {@link IMdmLink} which have {@link MdmMatchResultEnum#POSSIBLE_DUPLICATE} as their match result.
	 *
	 * @return A list of {@link IMdmLink} that hold potential duplicate golden resources.
	 */
	public List<? extends IMdmLink> getPossibleDuplicates() {
		IMdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE);
		Example<? extends IMdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	@Transactional
	public Optional<? extends IMdmLink> findMdmLinkBySource(IBaseResource theSourceResource) {
		@Nullable ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		if (pid == null) {
			return Optional.empty();
		}
		IMdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePersistenceId(pid);
		Example<? extends IMdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findOne(example);

	}
	/**
	 * Delete a given {@link IMdmLink}. Note that this does not clear out the Golden resource.
	 * It is a simple entity delete.
	 *
	 * @param theMdmLink the {@link IMdmLink} to delete.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteLink(IMdmLink theMdmLink) {
		myMdmLinkDao.validateMdmLink(theMdmLink);
		myMdmLinkDao.delete(theMdmLink);
	}

	/**
	 * Given a Golden Resource, return all links in which they are the source Golden Resource of the {@link IMdmLink}
	 *
	 * @param theGoldenResource The {@link IBaseResource} Golden Resource who's links you would like to retrieve.
	 * @return A list of all {@link IMdmLink} entities in which theGoldenResource is the source Golden Resource
	 */
	@Transactional
	public List<? extends IMdmLink> findMdmLinksByGoldenResource(IBaseResource theGoldenResource) {
		ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		IMdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setGoldenResourcePersistenceId(pid);
		Example<? extends IMdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Persist an MDM link to the database.
	 *
	 * @param theMdmLink the link to save.
	 * @return the persisted {@link IMdmLink} entity.
	 */
	public IMdmLink save(IMdmLink theMdmLink) {
		IMdmLink mdmLink = myMdmLinkDao.validateMdmLink(theMdmLink);
		if (mdmLink.getCreated() == null) {
			mdmLink.setCreated(new Date());
		}
		mdmLink.setUpdated(new Date());
		return myMdmLinkDao.save(mdmLink);
	}


	/**
	 * Given a list of criteria, return all links from the database which fits the criteria provided
	 *
	 * @param theGoldenResourceId The resource ID of the golden resource being searched.
	 * @param theSourceId         The resource ID of the source resource being searched.
	 * @param theMatchResult      the {@link MdmMatchResultEnum} being searched.
	 * @param theLinkSource       the {@link MdmLinkSourceEnum} being searched.
	 * @param thePageRequest      the {@link MdmPageRequest} paging information
	 * @param thePartitionId      List of partitions ID being searched, where the link's partition must be in the list.
	 * @return a list of {@link IMdmLink} entities which match the example.
	 */
	public Page<? extends IMdmLink> executeTypedQuery(IIdType theGoldenResourceId, IIdType theSourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmPageRequest thePageRequest, List<Integer> thePartitionId) {
		return myMdmLinkDao.search(theGoldenResourceId, theSourceId, theMatchResult, theLinkSource, thePageRequest, thePartitionId);
	}

	/**
	 * Given a source {@link IBaseResource}, return all {@link IMdmLink} entities in which this source is the source
	 * of the relationship. This will show you all links for a given Patient/Practitioner.
	 *
	 * @param theSourceResource the source resource to find links for.
	 * @return all links for the source.
	 */
	@Transactional
	public List<? extends IMdmLink> findMdmLinksBySourceResource(IBaseResource theSourceResource) {
		ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		IMdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePersistenceId(pid);
		Example<? extends IMdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Finds all {@link IMdmLink} entities in which theGoldenResource's PID is the source
	 * of the relationship.
	 *
	 * @param theGoldenResource the source resource to find links for.
	 * @return all links for the source.
	 */
	public List<? extends IMdmLink> findMdmMatchLinksByGoldenResource(IBaseResource theGoldenResource) {
		ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		IMdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setGoldenResourcePersistenceId(pid);
		exampleLink.setMatchResult(MdmMatchResultEnum.MATCH);
		Example<? extends IMdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Factory delegation method, whenever you need a new MdmLink, use this factory method.
	 * //TODO Should we make the constructor private for MdmLink? or work out some way to ensure they can only be instantiated via factory.
	 *
	 * @return A new {@link IMdmLink}.
	 */
	public IMdmLink newMdmLink() {
		return myMdmLinkFactory.newMdmLink();
	}

	public Optional<? extends IMdmLink> getMatchedOrPossibleMatchedLinkForSource(IAnyResource theResource) {
		// TODO KHS instead of two queries, just do one query with an OR
		Optional<? extends IMdmLink> retval = getMatchedLinkForSource(theResource);
		if (!retval.isPresent()) {
			retval = getPossibleMatchedLinkForSource(theResource);
		}
		return retval;
	}

	public Optional<? extends IMdmLink> getLinkByGoldenResourceAndSourceResource(@Nullable IAnyResource theGoldenResource, @Nullable IAnyResource theSourceResource) {
		if (theGoldenResource == null || theSourceResource == null) {
			return Optional.empty();
		}
		return getLinkByGoldenResourcePidAndSourceResourcePid(
			myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource),
			myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource));
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void deleteLinksWithAnyReferenceToPids(List<ResourcePersistentId> theGoldenResourcePids) {
		myMdmLinkDao.deleteLinksWithAnyReferenceToPids(theGoldenResourcePids);
	}
}
