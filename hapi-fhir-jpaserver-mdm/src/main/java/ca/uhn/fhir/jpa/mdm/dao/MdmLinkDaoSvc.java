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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
	@Autowired
	protected EntityManager myEntityManager;

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

		String message = String.format("Creating MdmLink from %s to %s.", theGoldenResource.getIdElement().toUnqualifiedVersionless(), theSourceResource.getIdElement().toUnqualifiedVersionless());
		theMdmTransactionContext.addTransactionLogMessage(message);
		ourLog.debug(message);
		save(mdmLink);
		return mdmLink;
	}

	@Nonnull
	public IMdmLink getOrCreateMdmLinkByGoldenResourceAndSourceResource(IAnyResource theGoldenResource, IAnyResource theSourceResource) {
		ResourcePersistentId goldenResourcePid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource);
		ResourcePersistentId sourceResourcePid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		Optional<? extends IMdmLink> oExisting = getLinkByGoldenResourcePidAndSourceResourcePid(goldenResourcePid.getIdAsLong(), sourceResourcePid.getIdAsLong());
		if (oExisting.isPresent()) {
			return oExisting.get();
		} else {
			MdmLink newLink = myMdmLinkFactory.newMdmLink();
			newLink.setGoldenResourcePersistenceId(goldenResourcePid);
			newLink.setSourcePersistenceId(sourceResourcePid);
			return newLink;
		}
	}


	public Optional<? extends IMdmLink> getLinkByGoldenResourcePidAndSourceResourcePid(Long theGoldenResourcePid, Long theSourceResourcePid) {
		return getLinkByGoldenResourcePidAndSourceResourcePid(new ResourcePersistentId(theGoldenResourcePid), new ResourcePersistentId(theSourceResourcePid));
	}

	public Optional<? extends IMdmLink> getLinkByGoldenResourcePidAndSourceResourcePid(ResourcePersistentId theGoldenResourcePid, ResourcePersistentId theSourceResourcePid) {
		if (theSourceResourcePid == null || theGoldenResourcePid == null) {
			return Optional.empty();
		}
		MdmLink link = myMdmLinkFactory.newMdmLink();
		link.setSourcePid(theSourceResourcePid.getIdAsLong());
		link.setGoldenResourcePid(theGoldenResourcePid.getIdAsLong());
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
	public List<? extends IMdmLink> getMdmLinksBySourcePidAndMatchResult(ResourcePersistentId theSourcePid, MdmMatchResultEnum theMatchResult) {
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePid(theSourcePid.getIdAsLong());
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
	@Transactional
	public Optional<? extends IMdmLink> getMatchedLinkForSourcePid(Long theSourcePid) {
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
		return getMdmLinkWithMatchResult(theSourceResource, MdmMatchResultEnum.MATCH);
	}

	public Optional<MdmLink> getPossibleMatchedLinkForSource(IBaseResource theSourceResource) {
		return getMdmLinkWithMatchResult(theSourceResource, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Nonnull
	private Optional<MdmLink> getMdmLinkWithMatchResult(IBaseResource theSourceResource, MdmMatchResultEnum theMatchResult) {
		ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		if (pid == null) {
			return Optional.empty();
		}

		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePersistenceId(pid);
		exampleLink.setMatchResult(theMatchResult);
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

	public Optional<MdmLink> getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(ResourcePersistentId theGoldenResourcePid,
																											 ResourcePersistentId theSourcePid, MdmMatchResultEnum theMatchResult) {
		return getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(theGoldenResourcePid.getIdAsLong(), theSourcePid.getIdAsLong(), theMatchResult);
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

	@Transactional
	public Optional<MdmLink> findMdmLinkBySource(IBaseResource theSourceResource) {
		@Nullable ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		if (pid == null) {
			return Optional.empty();
		}
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePersistenceId(pid);
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
	public void deleteLink(IMdmLink theMdmLink) {
		validateMdmLink(theMdmLink);
		myMdmLinkDao.delete((MdmLink) theMdmLink);
	}

	/**
	 * Given a Golden Resource, return all links in which they are the source Golden Resource of the {@link MdmLink}
	 *
	 * @param theGoldenResource The {@link IBaseResource} Golden Resource who's links you would like to retrieve.
	 * @return A list of all {@link MdmLink} entities in which theGoldenResource is the source Golden Resource
	 */
	@Transactional
	public List<? extends IMdmLink> findMdmLinksByGoldenResource(IBaseResource theGoldenResource) {
		ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setGoldenResourcePersistenceId(pid);
		Example<MdmLink> example = Example.of(exampleLink);
		return myMdmLinkDao.findAll(example);
	}

	/**
	 * Persist an MDM link to the database.
	 *
	 * @param theMdmLink the link to save.
	 * @return the persisted {@link MdmLink} entity.
	 */
	public IMdmLink save(IMdmLink theMdmLink) {
		MdmLink mdmLink = validateMdmLink(theMdmLink);
		if (mdmLink.getCreated() == null) {
			mdmLink.setCreated(new Date());
		}
		mdmLink.setUpdated(new Date());
		return myMdmLinkDao.save(mdmLink);
	}

	private MdmLink validateMdmLink(IMdmLink theMdmLink) {
		if (theMdmLink instanceof MdmLink){
			return  (MdmLink) theMdmLink;
		}
		else {
			throw new UnprocessableEntityException(Msg.code(2082) + "Unprocessable MdmLink implementation");
		}
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
	 * @return a list of {@link MdmLink} entities which match the example.
	 */
	public PageImpl<MdmLink> executeTypedQuery(IIdType theGoldenResourceId, IIdType theSourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmPageRequest thePageRequest, List<Integer> thePartitionId) {
		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<MdmLink> criteriaQuery = criteriaBuilder.createQuery(MdmLink.class);
		Root<MdmLink> from = criteriaQuery.from(MdmLink.class);

		List<Predicate> andPredicates = new ArrayList<>();

		if (theGoldenResourceId != null) {
			Predicate goldenResourcePredicate = criteriaBuilder.equal(from.get("myGoldenResourcePid").as(Long.class), myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theGoldenResourceId).getIdAsLong());
			andPredicates.add(goldenResourcePredicate);
		}
		if (theSourceId != null) {
			Predicate sourceIdPredicate = criteriaBuilder.equal(from.get("mySourcePid").as(Long.class), myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theSourceId).getIdAsLong());
			andPredicates.add(sourceIdPredicate);
		}
		if (theMatchResult != null) {
			Predicate matchResultPredicate = criteriaBuilder.equal(from.get("myMatchResult").as(MdmMatchResultEnum.class), theMatchResult);
			andPredicates.add(matchResultPredicate);
		}
		if (theLinkSource != null) {
			Predicate linkSourcePredicate = criteriaBuilder.equal(from.get("myLinkSource").as(MdmLinkSourceEnum.class), theLinkSource);
			andPredicates.add(linkSourcePredicate);
		}
		if (!CollectionUtils.isEmpty(thePartitionId)) {
			Expression<Integer> exp = from.get("myPartitionId").get("myPartitionId").as(Integer.class);
			Predicate linkSourcePredicate = exp.in(thePartitionId);
			andPredicates.add(linkSourcePredicate);
		}

		Predicate finalQuery = criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
		TypedQuery<MdmLink> typedQuery = myEntityManager.createQuery(criteriaQuery.where(finalQuery));

		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(MdmLink.class)))
			.where(finalQuery);

		Long totalResults = myEntityManager.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(typedQuery.setFirstResult(thePageRequest.getOffset()).setMaxResults(thePageRequest.getCount()).getResultList(),
			PageRequest.of(thePageRequest.getPage(), thePageRequest.getCount()),
			totalResults);
	}

	/**
	 * Given a source {@link IBaseResource}, return all {@link MdmLink} entities in which this source is the source
	 * of the relationship. This will show you all links for a given Patient/Practitioner.
	 *
	 * @param theSourceResource the source resource to find links for.
	 * @return all links for the source.
	 */
	@Transactional
	public List<MdmLink> findMdmLinksBySourceResource(IBaseResource theSourceResource) {
		ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setSourcePersistenceId(pid);
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
		ResourcePersistentId pid = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource);
		if (pid == null) {
			return Collections.emptyList();
		}
		MdmLink exampleLink = myMdmLinkFactory.newMdmLink();
		exampleLink.setGoldenResourcePersistenceId(pid);
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

	public Optional<MdmLink> getMatchedOrPossibleMatchedLinkForSource(IAnyResource theResource) {
		// TODO KHS instead of two queries, just do one query with an OR
		Optional<MdmLink> retval = getMatchedLinkForSource(theResource);
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
			myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource).getIdAsLong(),
			myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theSourceResource).getIdAsLong());
	}
}
