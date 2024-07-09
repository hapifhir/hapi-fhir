/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.params.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.dao.MdmLinkFactory;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.history.Revisions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MdmLinkDaoSvc<P extends IResourcePersistentId, M extends IMdmLink<P>> {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private IMdmLinkDao<P, M> myMdmLinkDao;

	@Autowired
	private MdmLinkFactory<M> myMdmLinkFactory;

	@Autowired
	private IIdHelperService<P> myIdHelperService;

	@Autowired
	private FhirContext myFhirContext;

	@Transactional
	public M createOrUpdateLinkEntity(
			IAnyResource theGoldenResource,
			IAnyResource theSourceResource,
			MdmMatchOutcome theMatchOutcome,
			MdmLinkSourceEnum theLinkSource,
			@Nullable MdmTransactionContext theMdmTransactionContext) {
		M mdmLink = getOrCreateMdmLinkByGoldenResourceAndSourceResource(theGoldenResource, theSourceResource);
		mdmLink.setLinkSource(theLinkSource);
		mdmLink.setMatchResult(theMatchOutcome.getMatchResultEnum());
		// Preserve these flags for link updates
		mdmLink.setEidMatch(theMatchOutcome.isEidMatch() | mdmLink.isEidMatchPresent());
		mdmLink.setHadToCreateNewGoldenResource(
				theMatchOutcome.isCreatedNewResource() | mdmLink.getHadToCreateNewGoldenResource());
		mdmLink.setMdmSourceType(myFhirContext.getResourceType(theSourceResource));

		setScoreProperties(theMatchOutcome, mdmLink);

		// Add partition for the mdm link if it's available in the source resource
		RequestPartitionId partitionId =
				(RequestPartitionId) theSourceResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (partitionId != null && partitionId.getFirstPartitionIdOrNull() != null) {
			mdmLink.setPartitionId(new PartitionablePartitionId(
					partitionId.getFirstPartitionIdOrNull(), partitionId.getPartitionDate()));
		}

		String message = String.format(
				"Creating %s link from %s to Golden Resource %s.",
				mdmLink.getMatchResult(),
				theSourceResource.getIdElement().toUnqualifiedVersionless(),
				theGoldenResource.getIdElement().toUnqualifiedVersionless());
		theMdmTransactionContext.addTransactionLogMessage(message);
		ourLog.debug(message);
		save(mdmLink);
		return mdmLink;
	}

	private void setScoreProperties(MdmMatchOutcome theMatchOutcome, M mdmLink) {
		if (theMatchOutcome.getScore() != null) {
			mdmLink.setScore(
					mdmLink.getScore() != null
							? Math.max(theMatchOutcome.getNormalizedScore(), mdmLink.getScore())
							: theMatchOutcome.getNormalizedScore());
		}

		if (theMatchOutcome.getVector() != null) {
			mdmLink.setVector(
					mdmLink.getVector() != null
							? Math.max(theMatchOutcome.getVector(), mdmLink.getVector())
							: theMatchOutcome.getVector());
		}

		mdmLink.setRuleCount(
				mdmLink.getRuleCount() != null
						? Math.max(theMatchOutcome.getMdmRuleCount(), mdmLink.getRuleCount())
						: theMatchOutcome.getMdmRuleCount());
	}

	@Nonnull
	public M getOrCreateMdmLinkByGoldenResourceAndSourceResource(
			IAnyResource theGoldenResource, IAnyResource theSourceResource) {
		P goldenResourcePid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource);
		P sourceResourcePid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		Optional<M> oExisting = getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourcePid, sourceResourcePid);
		if (oExisting.isPresent()) {
			return oExisting.get();
		} else {
			M newLink = myMdmLinkFactory.newMdmLink();
			newLink.setGoldenResourcePersistenceId(goldenResourcePid);
			newLink.setSourcePersistenceId(sourceResourcePid);
			return newLink;
		}
	}

	/**
	 * Given a golden resource Pid and source Pid, return the mdm link that matches these criterias if exists
	 *
	 * @param theGoldenResourcePid
	 * @param theSourceResourcePid
	 * @return
	 * @deprecated This was deprecated in favour of using ResourcePersistenceId rather than longs
	 */
	@Deprecated
	public Optional<M> getLinkByGoldenResourcePidAndSourceResourcePid(
			Long theGoldenResourcePid, Long theSourceResourcePid) {
		return getLinkByGoldenResourcePidAndSourceResourcePid(
				myIdHelperService.newPid(theGoldenResourcePid), myIdHelperService.newPid(theSourceResourcePid));
	}

	/**
	 * Given a golden resource Pid and source Pid, return the mdm link that matches these criterias if exists
	 * @param theGoldenResourcePid The ResourcePersistenceId of the golden resource
	 * @param theSourceResourcePid The ResourcepersistenceId of the Source resource
	 * @return The {@link IMdmLink} entity that matches these criteria if exists
	 */
	public Optional<M> getLinkByGoldenResourcePidAndSourceResourcePid(P theGoldenResourcePid, P theSourceResourcePid) {
		if (theSourceResourcePid == null || theGoldenResourcePid == null) {
			return Optional.empty();
		}
		M link = myMdmLinkFactory.newMdmLinkVersionless();
		link.setSourcePersistenceId(theSourceResourcePid);
		link.setGoldenResourcePersistenceId(theGoldenResourcePid);

		// TODO - replace the use of example search
		Example<M> example = Example.of(link);

		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Given a source resource Pid, and a match result, return all links that match these criteria.
	 *
	 * @param theSourcePid   the source of the relationship.
	 * @param theMatchResult the Match Result of the relationship
	 * @return a list of {@link IMdmLink} entities matching these criteria.
	 */
	public List<M> getMdmLinksBySourcePidAndMatchResult(P theSourcePid, MdmMatchResultEnum theMatchResult) {
		M exampleLink = myMdmLinkFactory.newMdmLinkVersionless();
		exampleLink.setSourcePersistenceId(theSourcePid);
		exampleLink.setMatchResult(theMatchResult);
		Example<M> example = Example.of(exampleLink);
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
	public Optional<M> getMatchedLinkForSourcePid(P theSourcePid) {
		return myMdmLinkDao.findBySourcePidAndMatchResult(theSourcePid, MdmMatchResultEnum.MATCH);
	}

	/**
	 * Given an IBaseResource, return its Matched {@link IMdmLink}. There can only ever be at most one of these, but its possible
	 * the source has no matches, and may return an empty optional.
	 *
	 * @param theSourceResource The IBaseResource representing the source you wish to find the matching link for.
	 * @return the {@link IMdmLink} that contains the Match information for the source.
	 */
	public Optional<M> getMatchedLinkForSource(IBaseResource theSourceResource) {
		return getMdmLinkWithMatchResult(theSourceResource, MdmMatchResultEnum.MATCH);
	}

	public Optional<M> getPossibleMatchedLinkForSource(IBaseResource theSourceResource) {
		return getMdmLinkWithMatchResult(theSourceResource, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Nonnull
	private Optional<M> getMdmLinkWithMatchResult(IBaseResource theSourceResource, MdmMatchResultEnum theMatchResult) {
		P pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		if (pid == null) {
			return Optional.empty();
		}

		M exampleLink = myMdmLinkFactory.newMdmLinkVersionless();
		exampleLink.setSourcePersistenceId(pid);
		exampleLink.setMatchResult(theMatchResult);
		Example<M> example = Example.of(exampleLink);
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
	public Optional<M> getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(
			Long theGoldenResourcePid, Long theSourcePid, MdmMatchResultEnum theMatchResult) {
		return getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(
				myIdHelperService.newPid(theGoldenResourcePid), myIdHelperService.newPid(theSourcePid), theMatchResult);
	}

	public Optional<M> getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(
			P theGoldenResourcePid, P theSourcePid, MdmMatchResultEnum theMatchResult) {
		M exampleLink = myMdmLinkFactory.newMdmLinkVersionless();
		exampleLink.setGoldenResourcePersistenceId(theGoldenResourcePid);
		exampleLink.setSourcePersistenceId(theSourcePid);
		exampleLink.setMatchResult(theMatchResult);
		Example<M> example = Example.of(exampleLink);
		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Get all {@link IMdmLink} which have {@link MdmMatchResultEnum#POSSIBLE_DUPLICATE} as their match result.
	 *
	 * @return A list of {@link IMdmLink} that hold potential duplicate golden resources.
	 */
	public List<M> getPossibleDuplicates() {
		M exampleLink = myMdmLinkFactory.newMdmLinkVersionless();
		exampleLink.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE);
		Example<M> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	@Transactional
	public Optional<M> findMdmLinkBySource(IBaseResource theSourceResource) {
		@Nullable P pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		if (pid == null) {
			return Optional.empty();
		}
		M exampleLink = myMdmLinkFactory.newMdmLinkVersionless();
		exampleLink.setSourcePersistenceId(pid);
		Example<M> example = Example.of(exampleLink);
		return myMdmLinkDao.findOne(example);
	}

	/**
	 * Delete a given {@link IMdmLink}. Note that this does not clear out the Golden resource.
	 * It is a simple entity delete.
	 *
	 * @param theMdmLink the {@link IMdmLink} to delete.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteLink(M theMdmLink) {
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
	public List<M> findMdmLinksByGoldenResource(IBaseResource theGoldenResource) {
		P pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		M exampleLink = myMdmLinkFactory.newMdmLinkVersionless();
		exampleLink.setGoldenResourcePersistenceId(pid);
		Example<M> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Persist an MDM link to the database.
	 *
	 * @param theMdmLink the link to save.
	 * @return the persisted {@link IMdmLink} entity.
	 */
	public M save(M theMdmLink) {
		M mdmLink = myMdmLinkDao.validateMdmLink(theMdmLink);
		if (mdmLink.getCreated() == null) {
			mdmLink.setCreated(new Date());
		}
		mdmLink.setUpdated(new Date());
		return myMdmLinkDao.save(mdmLink);
	}

	/**
	 * Given a list of criteria, return all links from the database which fits the criteria provided
	 *
	 * @param theMdmQuerySearchParameters The {@link MdmQuerySearchParameters} being searched.
	 * @return a list of {@link IMdmLink} entities which match the example.
	 */
	public Page<M> executeTypedQuery(MdmQuerySearchParameters theMdmQuerySearchParameters) {
		return myMdmLinkDao.search(theMdmQuerySearchParameters);
	}

	/**
	 * Given a source {@link IBaseResource}, return all {@link IMdmLink} entities in which this source is the source
	 * of the relationship. This will show you all links for a given Patient/Practitioner.
	 *
	 * @param theSourceResource the source resource to find links for.
	 * @return all links for the source.
	 */
	@Transactional
	public List<M> findMdmLinksBySourceResource(IBaseResource theSourceResource) {
		P pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		M exampleLink = myMdmLinkFactory.newMdmLinkVersionless();
		exampleLink.setSourcePersistenceId(pid);
		Example<M> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Finds all {@link IMdmLink} entities in which theGoldenResource's PID is the source
	 * of the relationship.
	 *
	 * @param theGoldenResource the source resource to find links for.
	 * @return all links for the source.
	 */
	public List<M> findMdmMatchLinksByGoldenResource(IBaseResource theGoldenResource) {
		P pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		M exampleLink = myMdmLinkFactory.newMdmLinkVersionless();
		exampleLink.setGoldenResourcePersistenceId(pid);
		exampleLink.setMatchResult(MdmMatchResultEnum.MATCH);
		Example<M> example = Example.of(exampleLink);
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

	public Optional<M> getMatchedOrPossibleMatchedLinkForSource(IAnyResource theResource) {
		// TODO KHS instead of two queries, just do one query with an OR
		Optional<M> retval = getMatchedLinkForSource(theResource);
		if (!retval.isPresent()) {
			retval = getPossibleMatchedLinkForSource(theResource);
		}
		return retval;
	}

	public Optional<M> getLinkByGoldenResourceAndSourceResource(
			@Nullable IAnyResource theGoldenResource, @Nullable IAnyResource theSourceResource) {
		if (theGoldenResource == null || theSourceResource == null) {
			return Optional.empty();
		}
		return getLinkByGoldenResourcePidAndSourceResourcePid(
				myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource),
				myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource));
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void deleteLinksWithAnyReferenceToPids(List<P> theGoldenResourcePids) {
		myMdmLinkDao.deleteLinksWithAnyReferenceToPids(theGoldenResourcePids);
	}

	// TODO: LD:  delete for good on the next bump
	@Deprecated(since = "6.5.7", forRemoval = true)
	public Revisions<Long, M> findMdmLinkHistory(M mdmLink) {
		return myMdmLinkDao.findHistory(mdmLink.getId());
	}

	@Transactional
	public List<MdmLinkWithRevision<M>> findMdmLinkHistory(MdmHistorySearchParameters theMdmHistorySearchParameters) {
		return myMdmLinkDao.getHistoryForIds(theMdmHistorySearchParameters);
	}
}
