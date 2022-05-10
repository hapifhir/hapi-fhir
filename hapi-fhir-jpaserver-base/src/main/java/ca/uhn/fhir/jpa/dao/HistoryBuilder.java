package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ca.uhn.fhir.jpa.dao.LegacySearchBuilder.toPredicateArray;

/**
 * The HistoryBuilder is responsible for building history queries
 */
public class HistoryBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(HistoryBuilder.class);
	private final String myResourceType;
	private final Long myResourceId;
	private final Date myRangeStartInclusive;
	private final Date myRangeEndInclusive;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private PartitionSettings myPartitionSettings;
	@Autowired
	private FhirContext myCtx;
	@Autowired
	private IIdHelperService myIdHelperService;

	/**
	 * Constructor
	 */
	public HistoryBuilder(@Nullable String theResourceType, @Nullable Long theResourceId, @Nullable Date theRangeStartInclusive, @Nullable Date theRangeEndInclusive) {
		myResourceType = theResourceType;
		myResourceId = theResourceId;
		myRangeStartInclusive = theRangeStartInclusive;
		myRangeEndInclusive = theRangeEndInclusive;
	}

	public Long fetchCount(RequestPartitionId thePartitionId) {
		CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
		Root<ResourceHistoryTable> from = criteriaQuery.from(ResourceHistoryTable.class);
		criteriaQuery.select(cb.count(from));

		addPredicatesToQuery(cb, thePartitionId, criteriaQuery, from);

		TypedQuery<Long> query = myEntityManager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	@SuppressWarnings("OptionalIsPresent")
	public List<ResourceHistoryTable> fetchEntities(RequestPartitionId thePartitionId, Integer theOffset, int theFromIndex, int theToIndex) {
		CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceHistoryTable> criteriaQuery = cb.createQuery(ResourceHistoryTable.class);
		Root<ResourceHistoryTable> from = criteriaQuery.from(ResourceHistoryTable.class);

		addPredicatesToQuery(cb, thePartitionId, criteriaQuery, from);

		from.fetch("myProvenance", JoinType.LEFT);

		criteriaQuery.orderBy(cb.desc(from.get("myUpdated")));

		TypedQuery<ResourceHistoryTable> query = myEntityManager.createQuery(criteriaQuery);

		int startIndex = theFromIndex;
		if (theOffset != null) {
			startIndex += theOffset;
		}
		query.setFirstResult(startIndex);

		query.setMaxResults(theToIndex - theFromIndex);

		List<ResourceHistoryTable> tables = query.getResultList();
		if (tables.size() > 0) {
			ImmutableListMultimap<Long, ResourceHistoryTable> resourceIdToHistoryEntries = Multimaps.index(tables, ResourceHistoryTable::getResourceId);

			Map<Long, Optional<String>> pidToForcedId = myIdHelperService.translatePidsToForcedIds(resourceIdToHistoryEntries.keySet());
			ourLog.trace("Translated IDs: {}", pidToForcedId);

			for (Long nextResourceId : resourceIdToHistoryEntries.keySet()) {
				List<ResourceHistoryTable> historyTables = resourceIdToHistoryEntries.get(nextResourceId);

				String resourceId;
				Optional<String> forcedId = pidToForcedId.get(nextResourceId);
				if (forcedId.isPresent()) {
					resourceId = forcedId.get();
				} else {
					resourceId = nextResourceId.toString();
				}

				for (ResourceHistoryTable nextHistoryTable : historyTables) {
					nextHistoryTable.setTransientForcedId(resourceId);
				}
			}
		}

		return tables;
	}

	private void addPredicatesToQuery(CriteriaBuilder theCriteriaBuilder, RequestPartitionId thePartitionId, CriteriaQuery<?> theQuery, Root<ResourceHistoryTable> theFrom) {
		List<Predicate> predicates = new ArrayList<>();

		if (!thePartitionId.isAllPartitions()) {
			if (thePartitionId.isDefaultPartition()) {
				predicates.add(theCriteriaBuilder.isNull(theFrom.get("myPartitionIdValue").as(Integer.class)));
			} else if (thePartitionId.hasDefaultPartitionId()) {
				predicates.add(theCriteriaBuilder.or(
					theCriteriaBuilder.isNull(theFrom.get("myPartitionIdValue").as(Integer.class)),
					theFrom.get("myPartitionIdValue").as(Integer.class).in(thePartitionId.getPartitionIdsWithoutDefault())
				));
			} else {
				predicates.add(theFrom.get("myPartitionIdValue").as(Integer.class).in(thePartitionId.getPartitionIds()));
			}
		}

		if (myResourceId != null) {
			predicates.add(theCriteriaBuilder.equal(theFrom.get("myResourceId"), myResourceId));
		} else if (myResourceType != null) {
			validateNotSearchingAllPartitions(thePartitionId);
			predicates.add(theCriteriaBuilder.equal(theFrom.get("myResourceType"), myResourceType));
		} else {
			validateNotSearchingAllPartitions(thePartitionId);
		}

		if (myRangeStartInclusive != null) {
			predicates.add(theCriteriaBuilder.greaterThanOrEqualTo(theFrom.get("myUpdated").as(Date.class), myRangeStartInclusive));
		}
		if (myRangeEndInclusive != null) {
			predicates.add(theCriteriaBuilder.lessThanOrEqualTo(theFrom.get("myUpdated").as(Date.class), myRangeEndInclusive));
		}

		if (predicates.size() > 0) {
			theQuery.where(toPredicateArray(predicates));
		}
	}

	private void validateNotSearchingAllPartitions(RequestPartitionId thePartitionId) {
		if (myPartitionSettings.isPartitioningEnabled()) {
			if (thePartitionId.isAllPartitions()) {
				String msg = myCtx.getLocalizer().getMessage(HistoryBuilder.class, "noSystemOrTypeHistoryForPartitionAwareServer");
				throw new InvalidRequestException(Msg.code(953) + msg);
			}
		}
	}


}
