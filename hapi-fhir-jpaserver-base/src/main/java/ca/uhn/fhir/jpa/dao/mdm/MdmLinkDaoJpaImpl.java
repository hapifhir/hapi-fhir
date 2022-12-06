package ca.uhn.fhir.jpa.dao.mdm;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaRepository;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MdmLinkDaoJpaImpl implements IMdmLinkDao<JpaPid, MdmLink> {
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
		return MdmPidTuple.fromGoldenAndSource(new JpaPid(theMdmPidTuple.getSourcePid()), new JpaPid(theMdmPidTuple.getGoldenPid()));
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
			.map( theResourcePids -> new JpaPid(theResourcePids))
			.collect(Collectors.toList());
	}

	@Override
	public List<JpaPid> findPidByResourceNameAndThresholdAndPartitionId(String theResourceName, Date theHighThreshold, List<Integer> thePartitionIds, Pageable thePageable) {
		return myMdmLinkDao.findPidByResourceNameAndThresholdAndPartitionId(theResourceName,theHighThreshold, thePartitionIds, thePageable)
			.stream()
			.map( theResourcePids -> new JpaPid(theResourcePids))
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
	public Page<MdmLink> search(IIdType theGoldenResourceId, IIdType theSourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmPageRequest thePageRequest, List<Integer> thePartitionId) {
		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<MdmLink> criteriaQuery = criteriaBuilder.createQuery(MdmLink.class);
		Root<MdmLink> from = criteriaQuery.from(MdmLink.class);

		List<Predicate> andPredicates = new ArrayList<>();

		if (theGoldenResourceId != null) {
			Predicate goldenResourcePredicate = criteriaBuilder.equal(from.get("myGoldenResourcePid").as(Long.class), (myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theGoldenResourceId)).getId());
			andPredicates.add(goldenResourcePredicate);
		}
		if (theSourceId != null) {
			Predicate sourceIdPredicate = criteriaBuilder.equal(from.get("mySourcePid").as(Long.class), (myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theSourceId)).getId());
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
}
