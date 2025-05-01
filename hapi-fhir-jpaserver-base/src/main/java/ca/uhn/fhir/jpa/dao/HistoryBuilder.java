/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.rest.param.HistorySearchStyleEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ca.uhn.fhir.jpa.util.QueryParameterUtils.toPredicateArray;

/**
 * The HistoryBuilder is responsible for building history queries
 */
public class HistoryBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(HistoryBuilder.class);
	private final String myResourceType;
	private final JpaPid myResourceId;
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
	private IIdHelperService<JpaPid> myIdHelperService;

	/**
	 * Constructor
	 */
	public HistoryBuilder(
			@Nullable String theResourceType,
			@Nullable JpaPid theResourceId,
			@Nullable Date theRangeStartInclusive,
			@Nullable Date theRangeEndInclusive) {
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

		addPredicatesToQuery(cb, thePartitionId, criteriaQuery, from, null);

		TypedQuery<Long> query = myEntityManager.createQuery(criteriaQuery);
		return query.getSingleResult();
	}

	public List<ResourceHistoryTable> fetchEntities(
			RequestPartitionId thePartitionId,
			Integer theOffset,
			int theFromIndex,
			int theToIndex,
			HistorySearchStyleEnum theHistorySearchStyle) {
		CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceHistoryTable> criteriaQuery = cb.createQuery(ResourceHistoryTable.class);
		Root<ResourceHistoryTable> from = criteriaQuery.from(ResourceHistoryTable.class);

		addPredicatesToQuery(cb, thePartitionId, criteriaQuery, from, theHistorySearchStyle);

		/*
		 * The sort on myUpdated is the important one for _history operations, but there are
		 * cases where multiple pages of results all have the exact same myUpdated value (e.g.
		 * if they were all ingested in a single FHIR transaction). So we put a secondary sort
		 * on the resource PID just to ensure that the sort is stable across queries.
		 *
		 * There are indexes supporting the myUpdated sort at each level (system/type/instance)
		 * but those indexes don't include myResourceId. I don't think that should be an issue
		 * since myUpdated should generally be unique anyhow. If this ever becomes an issue,
		 * we might consider adding the resource PID to indexes IDX_RESVER_DATE and
		 * IDX_RESVER_TYPE_DATE in the future.
		 * -JA 2024-04-21
		 */
		criteriaQuery.orderBy(cb.desc(from.get("myUpdated")), cb.desc(from.get("myResourceId")));

		TypedQuery<ResourceHistoryTable> query = myEntityManager.createQuery(criteriaQuery);

		int startIndex = theFromIndex;
		if (theOffset != null) {
			startIndex += theOffset;
		}
		query.setFirstResult(startIndex);

		query.setMaxResults(theToIndex - theFromIndex);

		List<ResourceHistoryTable> tables = query.getResultList();
		if (!tables.isEmpty()) {
			ImmutableListMultimap<JpaPid, ResourceHistoryTable> resourceIdToHistoryEntries =
					Multimaps.index(tables, ResourceHistoryTable::getResourceId);
			Set<JpaPid> pids = resourceIdToHistoryEntries.keySet();
			PersistentIdToForcedIdMap<JpaPid> pidToForcedId = myIdHelperService.translatePidsToForcedIds(pids);
			ourLog.trace("Translated IDs: {}", pidToForcedId.getResourcePersistentIdOptionalMap());

			for (JpaPid nextResourceId : resourceIdToHistoryEntries.keySet()) {
				List<ResourceHistoryTable> historyTables = resourceIdToHistoryEntries.get(nextResourceId);

				String resourceId;

				Optional<String> forcedId = pidToForcedId.get(nextResourceId);
				if (forcedId.isPresent()) {
					resourceId = forcedId.get();
					// IdHelperService returns a forcedId with the '<resourceType>/' prefix
					// but the transientForcedId is expected to be just the idPart (without the <resourceType>/ prefix).
					// For that reason, strip the prefix before setting the transientForcedId below.
					// If not stripped this messes up the id of the resource as the resourceType would be repeated
					// twice like Patient/Patient/1234 in the resource constructed
					int slashIdx = resourceId.indexOf('/');
					if (slashIdx != -1) {
						resourceId = resourceId.substring(slashIdx + 1);
					}
				} else {
					resourceId = Long.toString(nextResourceId.getId());
				}

				for (ResourceHistoryTable nextHistoryTable : historyTables) {
					nextHistoryTable.setTransientForcedId(resourceId);
				}
			}
		}

		return tables;
	}

	private void addPredicatesToQuery(
			CriteriaBuilder theCriteriaBuilder,
			RequestPartitionId thePartitionId,
			CriteriaQuery<?> theQuery,
			Root<ResourceHistoryTable> theFrom,
			HistorySearchStyleEnum theHistorySearchStyle) {
		List<Predicate> predicates = new ArrayList<>();

		if (myResourceId != null) {

			predicates.add(theCriteriaBuilder.equal(theFrom.get("myResourceId"), myResourceId.getId()));
			if (myPartitionSettings.isPartitioningEnabled()) {
				if (myResourceId.getPartitionId() != null) {
					predicates.add(
							theCriteriaBuilder.equal(theFrom.get("myPartitionIdValue"), myResourceId.getPartitionId()));
				} else {
					predicates.add(theCriteriaBuilder.isNull(theFrom.get("myPartitionIdValue")));
				}
			}

		} else {

			if (!thePartitionId.isAllPartitions()) {
				if (thePartitionId.isDefaultPartition()) {
					predicates.add(theCriteriaBuilder.isNull(theFrom.get("myPartitionIdValue")));
				} else if (thePartitionId.hasDefaultPartitionId()) {
					predicates.add(theCriteriaBuilder.or(
							theCriteriaBuilder.isNull(theFrom.get("myPartitionIdValue")),
							theFrom.get("myPartitionIdValue").in(thePartitionId.getPartitionIdsWithoutDefault())));
				} else {
					predicates.add(theFrom.get("myPartitionIdValue").in(thePartitionId.getPartitionIds()));
				}
			}

			if (myResourceType != null) {
				validateNotSearchingAllPartitions(thePartitionId);
				predicates.add(theCriteriaBuilder.equal(theFrom.get("myResourceType"), myResourceType));
			} else {
				validateNotSearchingAllPartitions(thePartitionId);
			}
		}

		if (myRangeStartInclusive != null) {
			if (HistorySearchStyleEnum.AT == theHistorySearchStyle && myResourceId != null) {
				addPredicateForAtQueryParameter(theCriteriaBuilder, theQuery, theFrom, predicates);
			} else {
				predicates.add(
						theCriteriaBuilder.greaterThanOrEqualTo(theFrom.get("myUpdated"), myRangeStartInclusive));
			}
		}
		if (myRangeEndInclusive != null) {
			predicates.add(theCriteriaBuilder.lessThanOrEqualTo(theFrom.get("myUpdated"), myRangeEndInclusive));
		}

		if (predicates.size() > 0) {
			theQuery.where(toPredicateArray(predicates));
		}
	}

	private void addPredicateForAtQueryParameter(
			CriteriaBuilder theCriteriaBuilder,
			CriteriaQuery<?> theQuery,
			Root<ResourceHistoryTable> theFrom,
			List<Predicate> thePredicates) {
		Subquery<Date> pastDateSubQuery = theQuery.subquery(Date.class);
		Root<ResourceHistoryTable> subQueryResourceHistory = pastDateSubQuery.from(ResourceHistoryTable.class);
		Expression myUpdatedMostRecent = theCriteriaBuilder.max(subQueryResourceHistory.get("myUpdated"));

		/*
		 * This conversion from the Date in myRangeEndInclusive into a ZonedDateTime is an experiment -
		 * There is an intermittent test failure in testSearchHistoryWithAtAndGtParameters() that I can't
		 * figure out. But I've added a ton of logging to the error it fails with and I noticed that
		 * we emit SQL along the lines of
		 *   select coalesce(max(rht2_0.RES_UPDATED), timestamp with time zone '2024-10-05 18:24:48.172000000Z')
		 * for this date, and all other dates are in GMT so this is an experiment. If nothing changes,
		 * we can roll this back to
		 *   theCriteriaBuilder.literal(myRangeStartInclusive)
		 * JA 20241005
		 */
		ZonedDateTime rangeStart =
				ZonedDateTime.ofInstant(Instant.ofEpochMilli(myRangeStartInclusive.getTime()), ZoneId.of("GMT"));

		Expression myUpdatedMostRecentOrDefault =
				theCriteriaBuilder.coalesce(myUpdatedMostRecent, theCriteriaBuilder.literal(rangeStart));

		pastDateSubQuery
				.select(myUpdatedMostRecentOrDefault)
				.where(
						theCriteriaBuilder.lessThanOrEqualTo(
								subQueryResourceHistory.get("myUpdated"), myRangeStartInclusive),
						theCriteriaBuilder.equal(subQueryResourceHistory.get("myResourcePid"), myResourceId.toFk()));

		Predicate updatedDatePredicate =
				theCriteriaBuilder.greaterThanOrEqualTo(theFrom.get("myUpdated"), pastDateSubQuery);
		thePredicates.add(updatedDatePredicate);
	}

	private void validateNotSearchingAllPartitions(RequestPartitionId thePartitionId) {
		if (myPartitionSettings.isPartitioningEnabled()) {
			if (thePartitionId.isAllPartitions()) {
				String msg = myCtx.getLocalizer()
						.getMessage(HistoryBuilder.class, "noSystemOrTypeHistoryForPartitionAwareServer");
				throw new InvalidRequestException(Msg.code(953) + msg);
			}
		}
	}
}
