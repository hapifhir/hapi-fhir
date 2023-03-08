package ca.uhn.fhir.jpa.dao.mdm;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaRepository;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revisions;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.mdm.api.MdmQuerySearchParameters.GOLDEN_RESOURCE_NAME;
import static ca.uhn.fhir.mdm.api.MdmQuerySearchParameters.GOLDEN_RESOURCE_PID_NAME;
import static ca.uhn.fhir.mdm.api.MdmQuerySearchParameters.LINK_SOURCE_NAME;
import static ca.uhn.fhir.mdm.api.MdmQuerySearchParameters.MATCH_RESULT_NAME;
import static ca.uhn.fhir.mdm.api.MdmQuerySearchParameters.PARTITION_ID_NAME;
import static ca.uhn.fhir.mdm.api.MdmQuerySearchParameters.RESOURCE_TYPE_NAME;
import static ca.uhn.fhir.mdm.api.MdmQuerySearchParameters.SOURCE_PID_NAME;

public class MdmLinkDaoJpaImpl implements IMdmLinkDao<JpaPid, MdmLink> {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkDaoJpaImpl.class);

	@Autowired
	IMdmLinkJpaRepository myMdmLinkDao;
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;

	@Override
	public int deleteWithAnyReferenceToPid(JpaPid thePid) {
		return myMdmLinkDao.deleteWithAnyReferenceToPid(thePid.getId());
	}

	@Override
	public int deleteWithAnyReferenceToPidAndMatchResultNot(JpaPid thePid, MdmMatchResultEnum theMatchResult) {
		return myMdmLinkDao.deleteWithAnyReferenceToPidAndMatchResultNot(thePid.getId(), theMatchResult);
	}

	@Override
	public List<MdmPidTuple<JpaPid>> expandPidsFromGroupPidGivenMatchResult(JpaPid theGroupPid, MdmMatchResultEnum theMdmMatchResultEnum) {
		return myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult((theGroupPid).getId(), theMdmMatchResultEnum)
			.stream()
			.map(this::daoTupleToMdmTuple)
			.collect(Collectors.toList());
	}

	private MdmPidTuple<JpaPid> daoTupleToMdmTuple(IMdmLinkJpaRepository.MdmPidTuple theMdmPidTuple) {
		return MdmPidTuple.fromGoldenAndSource(JpaPid.fromId(theMdmPidTuple.getGoldenPid()), JpaPid.fromId(theMdmPidTuple.getSourcePid()));
	}

	@Override
	public List<MdmPidTuple<JpaPid>> expandPidsBySourcePidAndMatchResult(JpaPid theSourcePid, MdmMatchResultEnum theMdmMatchResultEnum) {
		return myMdmLinkDao.expandPidsBySourcePidAndMatchResult((theSourcePid).getId(), theMdmMatchResultEnum)
			.stream()
			.map(this::daoTupleToMdmTuple)
			.collect(Collectors.toList());
	}

	@Override
	public List<MdmPidTuple<JpaPid>> expandPidsByGoldenResourcePidAndMatchResult(JpaPid theSourcePid, MdmMatchResultEnum theMdmMatchResultEnum) {
		return myMdmLinkDao.expandPidsByGoldenResourcePidAndMatchResult((theSourcePid).getId(), theMdmMatchResultEnum)
			.stream()
			.map(this::daoTupleToMdmTuple)
			.collect(Collectors.toList());
	}

	@Override
	public List<JpaPid> findPidByResourceNameAndThreshold(String theResourceName, Date theHighThreshold, Pageable thePageable) {
		return myMdmLinkDao.findPidByResourceNameAndThreshold(theResourceName,theHighThreshold, thePageable)
			.stream()
			.map(JpaPid::fromId)
			.collect(Collectors.toList());
	}

	@Override
	public List<JpaPid> findPidByResourceNameAndThresholdAndPartitionId(String theResourceName, Date theHighThreshold, List<Integer> thePartitionIds, Pageable thePageable) {
		return myMdmLinkDao.findPidByResourceNameAndThresholdAndPartitionId(theResourceName,theHighThreshold, thePartitionIds, thePageable)
			.stream()
			.map(JpaPid::fromId)
			.collect(Collectors.toList());
	}

	@Override
	public List<MdmLink> findAllById(List<JpaPid> thePids) {
		List<Long> theLongPids = thePids.stream().map(JpaPid::getId).collect(Collectors.toList());
		return myMdmLinkDao.findAllById(theLongPids);
	}

	@Override
	public Optional<MdmLink> findById(JpaPid thePid) {
		return myMdmLinkDao.findById(thePid.getId());
	}

	@Override
	public void deleteAll(List<MdmLink> theLinks) {
		myMdmLinkDao.deleteAll(theLinks);
	}


	@Override
	public List<MdmLink> findAll(Example<MdmLink> theExample) {
		return myMdmLinkDao.findAll(theExample);
	}

	@Override
	public List<MdmLink> findAll() {
		return myMdmLinkDao.findAll();
	}

	@Override
	public Long count() {
		return myMdmLinkDao.count();
	}

	@Override
	public void deleteAll() {
		myMdmLinkDao.deleteAll();
	}

	@Override
	public MdmLink save(MdmLink theMdmLink) {
		return myMdmLinkDao.save(theMdmLink);
	}

	@Override
	public Optional<MdmLink> findOne(Example<MdmLink> theExample) {
		return myMdmLinkDao.findOne(theExample);
	}

	@Override
	public void delete(MdmLink theMdmLink) {
		myMdmLinkDao.delete(theMdmLink);
	}

	@Override
	public MdmLink validateMdmLink(IMdmLink theMdmLink) throws UnprocessableEntityException {
		if (theMdmLink instanceof MdmLink){
			return  (MdmLink) theMdmLink;
		}
		else {
			throw new UnprocessableEntityException(Msg.code(2109) + "Unprocessable MdmLink implementation");
		}
	}

	@Override
	@Deprecated
	public Page<MdmLink> search(IIdType theGoldenResourceId, IIdType theSourceId, MdmMatchResultEnum theMatchResult,
										 MdmLinkSourceEnum theLinkSource, MdmPageRequest thePageRequest, List<Integer> thePartitionIds) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
			.setGoldenResourceId(theGoldenResourceId)
			.setSourceId(theSourceId)
			.setMatchResult(theMatchResult)
			.setLinkSource(theLinkSource)
			.setPartitionIds(thePartitionIds);
		return search(mdmQuerySearchParameters);
	}

	@Override
	public Page<MdmLink> search(MdmQuerySearchParameters theParams) {
		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<MdmLink> criteriaQuery = criteriaBuilder.createQuery(MdmLink.class);
		Root<MdmLink> from = criteriaQuery.from(MdmLink.class);
		List<Order> orderList = getOrderList(theParams, criteriaBuilder, from);

		List<Predicate> andPredicates = buildPredicates(theParams, criteriaBuilder, from);

		Predicate finalQuery = criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
		if ( ! orderList.isEmpty()) {
			criteriaQuery.orderBy(orderList);
		}
		TypedQuery<MdmLink> typedQuery = myEntityManager.createQuery(criteriaQuery.where(finalQuery));

		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(MdmLink.class)))
			.where(finalQuery);

		Long totalResults = myEntityManager.createQuery(countQuery).getSingleResult();
		MdmPageRequest pageRequest = theParams.getPageRequest();

		List<MdmLink> result = typedQuery
			.setFirstResult(pageRequest.getOffset())
			.setMaxResults(pageRequest.getCount())
			.getResultList();

		return new PageImpl<>(result,
			PageRequest.of(pageRequest.getPage(), pageRequest.getCount()),
			totalResults);
	}

	@NotNull
	private List<Predicate> buildPredicates(MdmQuerySearchParameters theParams, CriteriaBuilder criteriaBuilder, Root<MdmLink> from) {
		List<Predicate> andPredicates = new ArrayList<>();
		if (theParams.getGoldenResourceId() != null) {
			Predicate goldenResourcePredicate = criteriaBuilder.equal(from.get(GOLDEN_RESOURCE_PID_NAME).as(Long.class), (myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theParams.getGoldenResourceId())).getId());
			andPredicates.add(goldenResourcePredicate);
		}
		if (theParams.getSourceId() != null) {
			Predicate sourceIdPredicate = criteriaBuilder.equal(from.get(SOURCE_PID_NAME).as(Long.class), (myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theParams.getSourceId())).getId());
			andPredicates.add(sourceIdPredicate);
		}
		if (theParams.getMatchResult() != null) {
			Predicate matchResultPredicate = criteriaBuilder.equal(from.get(MATCH_RESULT_NAME).as(MdmMatchResultEnum.class), theParams.getMatchResult());
			andPredicates.add(matchResultPredicate);
		}
		if (theParams.getLinkSource() != null) {
			Predicate linkSourcePredicate = criteriaBuilder.equal(from.get(LINK_SOURCE_NAME).as(MdmLinkSourceEnum.class), theParams.getLinkSource());
			andPredicates.add(linkSourcePredicate);
		}
		if (!CollectionUtils.isEmpty(theParams.getPartitionIds())) {
			Expression<Integer> exp = from.get(PARTITION_ID_NAME).get(PARTITION_ID_NAME).as(Integer.class);
			Predicate linkSourcePredicate = exp.in(theParams.getPartitionIds());
			andPredicates.add(linkSourcePredicate);
		}

		if (theParams.getResourceType() != null) {
			Predicate resourceTypePredicate = criteriaBuilder.equal(from.get(GOLDEN_RESOURCE_NAME).get(RESOURCE_TYPE_NAME).as(String.class), theParams.getResourceType());
			andPredicates.add(resourceTypePredicate);
		}

		return andPredicates;
	}


	private List<Order> getOrderList(MdmQuerySearchParameters theParams, CriteriaBuilder criteriaBuilder, Root<MdmLink> from) {
		if (CollectionUtils.isEmpty(theParams.getSort())) {
			return Collections.emptyList();
		}

		return theParams.getSort().stream().map(sortSpec -> {
				Path<Object> path = from.get(sortSpec.getParamName());
				return sortSpec.getOrder() == SortOrderEnum.DESC ? criteriaBuilder.desc(path) : criteriaBuilder.asc(path);
			})
			.collect(Collectors.toList());
	}

	@Override
	public Optional<MdmLink> findBySourcePidAndMatchResult(JpaPid theSourcePid, MdmMatchResultEnum theMatch) {
		return myMdmLinkDao.findBySourcePidAndMatchResult((theSourcePid).getId(), theMatch);
	}

	@Override
	public void deleteLinksWithAnyReferenceToPids(List<JpaPid> theResourcePersistentIds) {
		List<Long> goldenResourcePids = theResourcePersistentIds.stream().map(JpaPid::getId).collect(Collectors.toList());
		// Split into chunks of 500 so older versions of Oracle don't run into issues (500 = 1000 / 2 since the dao
		// method uses the list twice in the sql predicate)
		List<List<Long>> chunks = ListUtils.partition(goldenResourcePids, 500);
		for (List<Long> chunk : chunks) {
			myMdmLinkDao.deleteLinksWithAnyReferenceToPids(chunk);
		}
	}

	@Override
	public Revisions<Integer, MdmLink> findHistory(JpaPid theMdmLinkPid) {
		// TODO:  LD:  future MR for MdmdLink History return some other object than Revisions, like a Map of List, Pageable, etc?
		final Revisions<Integer, MdmLink> revisions = myMdmLinkDao.findRevisions(theMdmLinkPid.getId());

		revisions.forEach(revision -> ourLog.info("MdmLink revision: {}", revision));

		return revisions;
	}
}
