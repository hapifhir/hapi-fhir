/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.cross.JpaResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TaskChunker;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder.replaceDefaultPartitionIdIfNonNull;
import static ca.uhn.fhir.model.primitive.IdDt.isValidLong;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is used to convert between PIDs (the internal primary key for a particular resource as
 * stored in the {@link ca.uhn.fhir.jpa.model.entity.ResourceTable HFJ_RESOURCE} table), and the
 * public ID that a resource has.
 * <p>
 * These IDs are sometimes one and the same (by default, a resource that the server assigns the ID of
 * <code>Patient/1</code> will simply use a PID of 1 and and ID of 1. However, they may also be different
 * in cases where a forced ID is used (an arbitrary client-assigned ID).
 * </p>
 * <p>
 * This service is highly optimized in order to minimize the number of DB calls as much as possible,
 * since ID resolution is fundamental to many basic operations. This service returns either
 * {@link IResourceLookup} or {@link BaseResourcePersistentId} depending on the method being called.
 * The former involves an extra database join that the latter does not require, so selecting the
 * right method here is important.
 * </p>
 */
@Service
public class IdHelperService implements IIdHelperService<JpaPid> {
	public static final Predicate[] EMPTY_PREDICATE_ARRAY = new Predicate[0];
	public static final String RESOURCE_PID = "RESOURCE_PID";
	private static final Logger ourLog = LoggerFactory.getLogger(IdHelperService.class);

	@Autowired
	protected IResourceTableDao myResourceTableDao;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private FhirContext myFhirCtx;

	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Autowired
	private PartitionSettings myPartitionSettings;

	private boolean myDontCheckActiveTransactionForUnitTest;

	@VisibleForTesting
	protected void setDontCheckActiveTransactionForUnitTest(boolean theDontCheckActiveTransactionForUnitTest) {
		myDontCheckActiveTransactionForUnitTest = theDontCheckActiveTransactionForUnitTest;
	}

	/**
	 * Given a forced ID, convert it to its Long value. Since you are allowed to use string IDs for resources, we need to
	 * convert those to the underlying Long values that are stored, for lookup and comparison purposes.
	 * Optionally filters out deleted resources.
	 *
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Override
	@Nonnull
	public IResourceLookup<JpaPid> resolveResourceIdentity(
			@Nonnull RequestPartitionId theRequestPartitionId,
			@Nullable String theResourceType,
			@Nonnull final String theResourceId,
			@Nonnull ResolveIdentityMode theMode)
			throws ResourceNotFoundException {

		IIdType id;
		if (theResourceType != null) {
			id = newIdType(theResourceType + "/" + theResourceId);
		} else {
			id = newIdType(theResourceId);
		}
		List<IIdType> ids = List.of(id);
		Map<IIdType, IResourceLookup<JpaPid>> outcome = resolveResourceIdentities(theRequestPartitionId, ids, theMode);

		// We only pass 1 input in so only 0..1 will come back
		if (!outcome.containsKey(id)) {
			throw new ResourceNotFoundException(Msg.code(2001) + "Resource " + id + " is not known");
		}

		return outcome.get(id);
	}

	@Nonnull
	@Override
	public Map<IIdType, IResourceLookup<JpaPid>> resolveResourceIdentities(
			@Nonnull RequestPartitionId theRequestPartitionId,
			Collection<IIdType> theIds,
			ResolveIdentityMode theMode) {
		assert myDontCheckActiveTransactionForUnitTest || TransactionSynchronizationManager.isSynchronizationActive()
				: "no transaction active";

		if (theIds.isEmpty()) {
			return new HashMap<>();
		}

		Collection<IIdType> ids = new ArrayList<>(theIds);
		ids.forEach(id -> Validate.isTrue(id.hasIdPart()));

		RequestPartitionId requestPartitionId = replaceDefault(theRequestPartitionId);
		ListMultimap<IIdType, IResourceLookup<JpaPid>> idToLookup =
				MultimapBuilder.hashKeys(theIds.size()).arrayListValues(1).build();

		// Do we have any FHIR ID lookups cached for any of the IDs
		if (theMode.isUseCache(myStorageSettings.isDeleteEnabled()) && !ids.isEmpty()) {
			resolveResourceIdentitiesForFhirIdsUsingCache(requestPartitionId, theMode, ids, idToLookup);
		}

		// We still haven't found IDs, let's look them up in the DB
		if (!ids.isEmpty()) {
			resolveResourceIdentitiesForFhirIdsUsingDatabase(requestPartitionId, ids, idToLookup);
		}

		// Convert the multimap into a simple map
		Map<IIdType, IResourceLookup<JpaPid>> retVal = new HashMap<>();
		for (Map.Entry<IIdType, IResourceLookup<JpaPid>> next : idToLookup.entries()) {
			if (next.getValue().getDeleted() != null) {
				if (theMode.isFailOnDeleted()) {
					String msg = myFhirCtx
							.getLocalizer()
							.getMessageSanitized(
									IdHelperService.class,
									"deletedId",
									next.getKey().getValue());
					throw new ResourceGoneException(Msg.code(2572) + msg);
				}
				if (!theMode.isIncludeDeleted()) {
					continue;
				}
			}

			IResourceLookup previousValue = retVal.put(next.getKey(), next.getValue());
			if (previousValue != null) {
				/*
				 *  This means that either:
				 *  1. There are two resources with the exact same resource type and forced
				 *     id. The most likely reason for that is that someone is performing a
				 *     multi-partition search and there are resources on each partition
				 *     with the same ID.
				 *  2. The unique constraint on the FHIR_ID column has been dropped
				 */
				ourLog.warn(
						"Resource ID[{}] corresponds to lookups: {} and {}",
						next.getKey(),
						previousValue,
						next.getValue());
				String msg = myFhirCtx.getLocalizer().getMessage(IdHelperService.class, "nonUniqueForcedId");
				throw new PreconditionFailedException(Msg.code(1099) + msg);
			}
		}

		return retVal;
	}

	/**
	 * Fetch the resource identity ({@link IResourceLookup}) for a collection of
	 * resource IDs from the internal memory cache if possible. Note that we only
	 * use cached results if deletes are disabled on the server (since it is
	 * therefore not possible that we have an entry in the cache that has since
	 * been deleted but the cache doesn't know about the deletion), or if we
	 * aren't excluding deleted results anyhow.
	 *
	 * @param theRequestPartitionId The partition(s) to search
	 * @param theIdsToResolve       The IDs we should look up. Any IDs that are resolved
	 *                              will be removed from this list. Any IDs remaining in
	 *                              the list after calling this method still haven't
	 *                              been attempted to be resolved.
	 * @param theMapToPopulate      The results will be populated into this map
	 */
	private void resolveResourceIdentitiesForFhirIdsUsingCache(
			@Nonnull RequestPartitionId theRequestPartitionId,
			ResolveIdentityMode theMode,
			Collection<IIdType> theIdsToResolve,
			ListMultimap<IIdType, IResourceLookup<JpaPid>> theMapToPopulate) {
		for (Iterator<IIdType> idIterator = theIdsToResolve.iterator(); idIterator.hasNext(); ) {
			IIdType nextForcedId = idIterator.next();
			MemoryCacheService.ForcedIdCacheKey nextKey = new MemoryCacheService.ForcedIdCacheKey(
					nextForcedId.getResourceType(), nextForcedId.getIdPart(), theRequestPartitionId);
			if (theMode.isUseCache(myStorageSettings.isDeleteEnabled())) {
				List<IResourceLookup<JpaPid>> cachedLookups = myMemoryCacheService.getIfPresent(
						MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID, nextKey);
				if (cachedLookups != null && !cachedLookups.isEmpty()) {
					idIterator.remove();
					for (IResourceLookup<JpaPid> cachedLookup : cachedLookups) {
						if (theMode.isIncludeDeleted() || cachedLookup.getDeleted() == null) {
							theMapToPopulate.put(nextKey.toIdType(myFhirCtx), cachedLookup);
						}
					}
				}
			}
		}
	}

	/**
	 * Fetch the resource identity ({@link IResourceLookup}) for a collection of
	 * resource IDs from the database
	 *
	 * @param theRequestPartitionId The partition(s) to search
	 * @param theIdsToResolve       The IDs we should look up
	 * @param theMapToPopulate      The results will be populated into this map
	 */
	private void resolveResourceIdentitiesForFhirIdsUsingDatabase(
			RequestPartitionId theRequestPartitionId,
			Collection<IIdType> theIdsToResolve,
			ListMultimap<IIdType, IResourceLookup<JpaPid>> theMapToPopulate) {

		/*
		 * If we have more than a threshold of IDs, we need to chunk the execution to
		 * avoid having too many parameters in one SQL statement
		 */
		int maxPageSize = (SearchBuilder.getMaximumPageSize() / 2) - 10;
		if (theIdsToResolve.size() > maxPageSize) {
			TaskChunker.chunk(
					theIdsToResolve,
					maxPageSize,
					chunk -> resolveResourceIdentitiesForFhirIdsUsingDatabase(
							theRequestPartitionId, chunk, theMapToPopulate));
			return;
		}

		CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
		Root<ResourceTable> from = criteriaQuery.from(ResourceTable.class);
		criteriaQuery.multiselect(
				from.get("myId"),
				from.get("myResourceType"),
				from.get("myFhirId"),
				from.get("myDeleted"),
				from.get("myPartitionIdValue"));

		List<Predicate> outerAndPredicates = new ArrayList<>(2);
		if (!theRequestPartitionId.isAllPartitions()) {
			getOptionalPartitionPredicate(theRequestPartitionId, cb, from).ifPresent(outerAndPredicates::add);
		}

		// one create one clause per id.
		List<Predicate> innerIdPredicates = new ArrayList<>(theIdsToResolve.size());
		boolean haveUntypedIds = false;
		for (IIdType next : theIdsToResolve) {
			if (!next.hasResourceType()) {
				haveUntypedIds = true;
			}

			List<Predicate> idPredicates = new ArrayList<>(2);

			if (myStorageSettings.getResourceClientIdStrategy() == JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC
					&& next.isIdPartValidLong()) {
				Predicate typeCriteria = cb.equal(from.get("myId"), next.getIdPartAsLong());
				idPredicates.add(typeCriteria);
			} else {
				if (isNotBlank(next.getResourceType())) {
					Predicate typeCriteria = cb.equal(from.get("myResourceType"), next.getResourceType());
					idPredicates.add(typeCriteria);
				}
				Predicate idCriteria = cb.equal(from.get("myFhirId"), next.getIdPart());
				idPredicates.add(idCriteria);
			}

			innerIdPredicates.add(cb.and(idPredicates.toArray(EMPTY_PREDICATE_ARRAY)));
		}
		outerAndPredicates.add(cb.or(innerIdPredicates.toArray(EMPTY_PREDICATE_ARRAY)));

		criteriaQuery.where(cb.and(outerAndPredicates.toArray(EMPTY_PREDICATE_ARRAY)));
		TypedQuery<Tuple> query = myEntityManager.createQuery(criteriaQuery);
		List<Tuple> results = query.getResultList();
		for (Tuple nextId : results) {
			// Check if the nextId has a resource ID. It may have a null resource ID if a commit is still pending.
			Long resourcePid = nextId.get(0, Long.class);
			String resourceType = nextId.get(1, String.class);
			String fhirId = nextId.get(2, String.class);
			Date deletedAd = nextId.get(3, Date.class);
			Integer partitionId = nextId.get(4, Integer.class);
			if (resourcePid != null) {
				JpaResourceLookup lookup = new JpaResourceLookup(
						resourceType, resourcePid, deletedAd, PartitionablePartitionId.with(partitionId, null));

				MemoryCacheService.ForcedIdCacheKey nextKey =
						new MemoryCacheService.ForcedIdCacheKey(resourceType, fhirId, theRequestPartitionId);
				IIdType id = nextKey.toIdType(myFhirCtx);
				theMapToPopulate.put(id, lookup);

				if (haveUntypedIds) {
					id = nextKey.toIdTypeWithoutResourceType(myFhirCtx);
					theMapToPopulate.put(id, lookup);
				}

				List<IResourceLookup<JpaPid>> valueToCache = theMapToPopulate.get(id);
				myMemoryCacheService.putAfterCommit(
						MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID, nextKey, valueToCache);
			}
		}
	}

	/**
	 * Returns a mapping of Id -> IResourcePersistentId.
	 * If any resource is not found, it will throw ResourceNotFound exception (and no map will be returned)
	 * Optionally filters out deleted resources.
	 */
	@Override
	@Nonnull
	public Map<String, JpaPid> resolveResourcePersistentIds(
			@Nonnull RequestPartitionId theRequestPartitionId,
			String theResourceType,
			List<String> theIds,
			ResolveIdentityMode theMode) {
		assert myDontCheckActiveTransactionForUnitTest || TransactionSynchronizationManager.isSynchronizationActive();
		Validate.notNull(theIds, "theIds cannot be null");
		Validate.isTrue(!theIds.isEmpty(), "theIds must not be empty");

		Map<String, JpaPid> retVals = new HashMap<>();
		for (String id : theIds) {
			JpaPid retVal;
			if (!idRequiresForcedId(id)) {
				// is already a PID
				retVal = JpaPid.fromId(Long.parseLong(id));
				retVals.put(id, retVal);
			} else {
				// is a forced id
				// we must resolve!
				if (myStorageSettings.isDeleteEnabled()) {
					retVal = resolveResourceIdentity(theRequestPartitionId, theResourceType, id, theMode)
							.getPersistentId();
					retVals.put(id, retVal);
				} else {
					// fetch from cache... adding to cache if not available
					String key = toForcedIdToPidKey(theRequestPartitionId, theResourceType, id);
					retVal = myMemoryCacheService.getThenPutAfterCommit(
							MemoryCacheService.CacheEnum.FORCED_ID_TO_PID, key, t -> {
								List<IIdType> ids = Collections.singletonList(new IdType(theResourceType, id));
								// fetches from cache using a function that checks cache first...
								List<JpaPid> resolvedIds =
										resolveResourcePersistentIdsWithCache(theRequestPartitionId, ids);
								if (resolvedIds.isEmpty()) {
									throw new ResourceNotFoundException(Msg.code(1100) + ids.get(0));
								}
								return resolvedIds.get(0);
							});
					retVals.put(id, retVal);
				}
			}
		}

		return retVals;
	}

	/**
	 * Given a resource type and ID, determines the internal persistent ID for the resource.
	 * Optionally filters out deleted resources.
	 *
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	@Override
	public JpaPid resolveResourcePersistentIds(
			@Nonnull RequestPartitionId theRequestPartitionId,
			String theResourceType,
			String theId,
			ResolveIdentityMode theMode) {
		Validate.notNull(theId, "theId must not be null");

		Map<String, JpaPid> retVal = resolveResourcePersistentIds(
				theRequestPartitionId, theResourceType, Collections.singletonList(theId), theMode);
		return retVal.get(theId); // should be only one
	}

	/**
	 * Returns true if the given resource ID should be stored in a forced ID. Under default config
	 * (meaning client ID strategy is {@link JpaStorageSettings.ClientIdStrategyEnum#ALPHANUMERIC})
	 * this will return true if the ID has any non-digit characters.
	 * <p>
	 * In {@link JpaStorageSettings.ClientIdStrategyEnum#ANY} mode it will always return true.
	 */
	@Override
	public boolean idRequiresForcedId(String theId) {
		return myStorageSettings.getResourceClientIdStrategy() == JpaStorageSettings.ClientIdStrategyEnum.ANY
				|| !isValidPid(theId);
	}

	@Nonnull
	private String toForcedIdToPidKey(
			@Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, String theId) {
		return RequestPartitionId.stringifyForKey(theRequestPartitionId) + "/" + theResourceType + "/" + theId;
	}

	/**
	 * Given a collection of resource IDs (resource type + id), resolves the internal persistent IDs.
	 * <p>
	 * This implementation will always try to use a cache for performance, meaning that it can resolve resources that
	 * are deleted (but note that forced IDs can't change, so the cache can't return incorrect results)
	 */
	@Override
	@Nonnull
	public List<JpaPid> resolveResourcePersistentIdsWithCache(
			RequestPartitionId theRequestPartitionId, List<IIdType> theIds) {
		boolean onlyForcedIds = false;
		return resolveResourcePersistentIdsWithCache(theRequestPartitionId, theIds, onlyForcedIds);
	}

	/**
	 * Given a collection of resource IDs (resource type + id), resolves the internal persistent IDs.
	 * <p>
	 * This implementation will always try to use a cache for performance, meaning that it can resolve resources that
	 * are deleted (but note that forced IDs can't change, so the cache can't return incorrect results)
	 *
	 * @param theOnlyForcedIds If <code>true</code>, resources which are not existing forced IDs will not be resolved
	 */
	@Override
	@Nonnull
	public List<JpaPid> resolveResourcePersistentIdsWithCache(
			@Nonnull RequestPartitionId theRequestPartitionId, List<IIdType> theIds, boolean theOnlyForcedIds) {
		assert myDontCheckActiveTransactionForUnitTest || TransactionSynchronizationManager.isSynchronizationActive();

		List<JpaPid> retVal = new ArrayList<>(theIds.size());

		for (IIdType id : theIds) {
			if (!id.hasIdPart()) {
				throw new InvalidRequestException(Msg.code(1101) + "Parameter value missing in request");
			}
		}

		if (!theIds.isEmpty()) {
			Set<IIdType> idsToCheck = new HashSet<>(theIds.size());
			for (IIdType nextId : theIds) {
				if (myStorageSettings.getResourceClientIdStrategy() != JpaStorageSettings.ClientIdStrategyEnum.ANY) {
					if (nextId.isIdPartValidLong()) {
						if (!theOnlyForcedIds) {
							JpaPid jpaPid = JpaPid.fromId(nextId.getIdPartAsLong());
							jpaPid.setAssociatedResourceId(nextId);
							retVal.add(jpaPid);
						}
						continue;
					}
				}

				String key = toForcedIdToPidKey(theRequestPartitionId, nextId.getResourceType(), nextId.getIdPart());
				JpaPid cachedId = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID, key);
				if (cachedId != null) {
					retVal.add(cachedId);
					continue;
				}

				idsToCheck.add(nextId);
			}
			new QueryChunker<IIdType>();
			TaskChunker.chunk(
					idsToCheck,
					SearchBuilder.getMaximumPageSize() / 2,
					ids -> doResolvePersistentIds(theRequestPartitionId, ids, retVal));
		}

		return retVal;
	}

	private void doResolvePersistentIds(
			RequestPartitionId theRequestPartitionId, List<IIdType> theIds, List<JpaPid> theOutputListToPopulate) {
		CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = cb.createTupleQuery();
		Root<ResourceTable> from = criteriaQuery.from(ResourceTable.class);

		/*
		 * IDX_RES_FHIR_ID covers these columns, but RES_ID is only INCLUDEd.
		 * Only PG, and MSSql support INCLUDE COLUMNS.
		 * @see AddIndexTask.generateSql
		 */
		criteriaQuery.multiselect(from.get("myId"), from.get("myResourceType"), from.get("myFhirId"));

		// one create one clause per id.
		List<Predicate> predicates = new ArrayList<>(theIds.size());
		for (IIdType next : theIds) {

			List<Predicate> andPredicates = new ArrayList<>(3);

			if (isNotBlank(next.getResourceType())) {
				Predicate typeCriteria = cb.equal(from.get("myResourceType"), next.getResourceType());
				andPredicates.add(typeCriteria);
			}

			Predicate idCriteria = cb.equal(from.get("myFhirId"), next.getIdPart());
			andPredicates.add(idCriteria);
			getOptionalPartitionPredicate(theRequestPartitionId, cb, from).ifPresent(andPredicates::add);
			predicates.add(cb.and(andPredicates.toArray(EMPTY_PREDICATE_ARRAY)));
		}

		// join all the clauses as OR
		criteriaQuery.where(cb.or(predicates.toArray(EMPTY_PREDICATE_ARRAY)));

		TypedQuery<Tuple> query = myEntityManager.createQuery(criteriaQuery);
		List<Tuple> results = query.getResultList();
		for (Tuple nextId : results) {
			// Check if the nextId has a resource ID. It may have a null resource ID if a commit is still pending.
			Long resourceId = nextId.get(0, Long.class);
			String resourceType = nextId.get(1, String.class);
			String forcedId = nextId.get(2, String.class);
			if (resourceId != null) {
				JpaPid jpaPid = JpaPid.fromId(resourceId);
				populateAssociatedResourceId(resourceType, forcedId, jpaPid);
				theOutputListToPopulate.add(jpaPid);

				String key = toForcedIdToPidKey(theRequestPartitionId, resourceType, forcedId);
				myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID, key, jpaPid);
			}
		}
	}

	/**
	 * Return optional predicate for searching on forcedId
	 * 1. If the partition mode is ALLOWED_UNQUALIFIED, the return optional predicate will be empty, so search is across all partitions.
	 * 2. If it is default partition and default partition id is null, then return predicate for null partition.
	 * 3. If the requested partition search is not all partition, return the request partition as predicate.
	 */
	private Optional<Predicate> getOptionalPartitionPredicate(
			RequestPartitionId theRequestPartitionId, CriteriaBuilder cb, Root<ResourceTable> from) {
		if (myPartitionSettings.isAllowUnqualifiedCrossPartitionReference()) {
			return Optional.empty();
		} else if (theRequestPartitionId.isAllPartitions()) {
			return Optional.empty();
		} else {
			List<Integer> partitionIds = theRequestPartitionId.getPartitionIds();
			partitionIds = replaceDefaultPartitionIdIfNonNull(myPartitionSettings, partitionIds);
			if (partitionIds.contains(null)) {
				Predicate partitionIdNullCriteria =
						from.get("myPartitionIdValue").isNull();
				if (partitionIds.size() == 1) {
					return Optional.of(partitionIdNullCriteria);
				} else {
					Predicate partitionIdCriteria = from.get("myPartitionIdValue")
							.in(partitionIds.stream().filter(t -> t != null).collect(Collectors.toList()));
					return Optional.of(cb.or(partitionIdCriteria, partitionIdNullCriteria));
				}
			} else {
				if (partitionIds.size() > 1) {
					Predicate partitionIdCriteria =
							from.get("myPartitionIdValue").in(partitionIds);
					return Optional.of(partitionIdCriteria);
				} else if (partitionIds.size() == 1) {
					Predicate partitionIdCriteria = cb.equal(from.get("myPartitionIdValue"), partitionIds.get(0));
					return Optional.of(partitionIdCriteria);
				}
			}
		}
		return Optional.empty();
	}

	private void populateAssociatedResourceId(String nextResourceType, String forcedId, JpaPid jpaPid) {
		IIdType resourceId = myFhirCtx.getVersion().newIdType();
		resourceId.setValue(nextResourceType + "/" + forcedId);
		jpaPid.setAssociatedResourceId(resourceId);
	}

	/**
	 * Given a persistent ID, returns the associated resource ID
	 */
	@Nonnull
	@Override
	public IIdType translatePidIdToForcedId(FhirContext theCtx, String theResourceType, JpaPid theId) {
		if (theId.getAssociatedResourceId() != null) {
			return theId.getAssociatedResourceId();
		}

		IIdType retVal = theCtx.getVersion().newIdType();

		Optional<String> forcedId = translatePidIdToForcedIdWithCache(theId);
		if (forcedId.isPresent()) {
			retVal.setValue(forcedId.get());
		} else {
			retVal.setValue(theResourceType + '/' + theId);
		}

		return retVal;
	}

	@SuppressWarnings("OptionalAssignedToNull")
	@Override
	public Optional<String> translatePidIdToForcedIdWithCache(JpaPid theId) {
		// do getIfPresent and then put to avoid doing I/O inside the cache.
		Optional<String> forcedId =
				myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, theId.getId());

		if (forcedId == null) {
			// This is only called when we know the resource exists.
			// So this optional is only empty when there is no hfj_forced_id table
			// note: this is obsolete with the new fhir_id column, and will go away.
			forcedId = myResourceTableDao.findById(theId.getId()).map(ResourceTable::asTypedFhirResourceId);
			myMemoryCacheService.put(MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, theId.getId(), forcedId);
		}

		return forcedId;
	}

	public RequestPartitionId replaceDefault(RequestPartitionId theRequestPartitionId) {
		if (myPartitionSettings.getDefaultPartitionId() != null) {
			if (!theRequestPartitionId.isAllPartitions() && theRequestPartitionId.hasDefaultPartitionId()) {
				List<Integer> partitionIds = theRequestPartitionId.getPartitionIds().stream()
						.map(t -> t == null ? myPartitionSettings.getDefaultPartitionId() : t)
						.collect(Collectors.toList());
				return RequestPartitionId.fromPartitionIds(partitionIds);
			}
		}
		return theRequestPartitionId;
	}

	@Override
	public PersistentIdToForcedIdMap<JpaPid> translatePidsToForcedIds(Set<JpaPid> theResourceIds) {
		assert myDontCheckActiveTransactionForUnitTest || TransactionSynchronizationManager.isSynchronizationActive();
		Set<Long> thePids = theResourceIds.stream().map(JpaPid::getId).collect(Collectors.toSet());
		Map<Long, Optional<String>> retVal = new HashMap<>(
				myMemoryCacheService.getAllPresent(MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, thePids));

		List<Long> remainingPids =
				thePids.stream().filter(t -> !retVal.containsKey(t)).collect(Collectors.toList());

		QueryChunker.chunk(remainingPids, t -> {
			List<ResourceTable> resourceEntities = myResourceTableDao.findAllById(t);

			for (ResourceTable nextResourceEntity : resourceEntities) {
				Long nextResourcePid = nextResourceEntity.getId();
				Optional<String> nextForcedId = Optional.of(nextResourceEntity.asTypedFhirResourceId());
				retVal.put(nextResourcePid, nextForcedId);
				myMemoryCacheService.putAfterCommit(
						MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, nextResourcePid, nextForcedId);
			}
		});

		remainingPids = thePids.stream().filter(t -> !retVal.containsKey(t)).collect(Collectors.toList());
		for (Long nextResourcePid : remainingPids) {
			retVal.put(nextResourcePid, Optional.empty());
			myMemoryCacheService.putAfterCommit(
					MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, nextResourcePid, Optional.empty());
		}
		Map<JpaPid, Optional<String>> convertRetVal = new HashMap<>();
		retVal.forEach((k, v) -> convertRetVal.put(JpaPid.fromId(k), v));

		return new PersistentIdToForcedIdMap<>(convertRetVal);
	}

	/**
	 * Pre-cache a PID-to-Resource-ID mapping for later retrieval by {@link #translatePidsToForcedIds(Set)} and related methods
	 */
	@Override
	public void addResolvedPidToFhirId(
			@Nonnull JpaPid theJpaPid,
			@Nonnull RequestPartitionId theRequestPartitionId,
			@Nonnull String theResourceType,
			@Nonnull String theFhirId,
			@Nullable Date theDeletedAt) {
		if (theJpaPid.getAssociatedResourceId() == null) {
			populateAssociatedResourceId(theResourceType, theFhirId, theJpaPid);
		}

		myMemoryCacheService.putAfterCommit(
				MemoryCacheService.CacheEnum.PID_TO_FORCED_ID,
				theJpaPid.getId(),
				Optional.of(theResourceType + "/" + theFhirId));
		String key = toForcedIdToPidKey(theRequestPartitionId, theResourceType, theFhirId);
		myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID, key, theJpaPid);

		JpaResourceLookup lookup = new JpaResourceLookup(
				theResourceType, theJpaPid.getId(), theDeletedAt, theJpaPid.getPartitionablePartitionId());

		MemoryCacheService.ForcedIdCacheKey fhirIdKey =
				new MemoryCacheService.ForcedIdCacheKey(theResourceType, theFhirId, theRequestPartitionId);
		myMemoryCacheService.putAfterCommit(
				MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID, fhirIdKey, List.of(lookup));

		// If it's a pure-numeric ID, store it in the cache without a type as well
		// so that we can resolve it this way when loading entities for update
		if (myStorageSettings.getResourceClientIdStrategy() == JpaStorageSettings.ClientIdStrategyEnum.ALPHANUMERIC
				&& isValidLong(theFhirId)) {
			MemoryCacheService.ForcedIdCacheKey fhirIdKeyWithoutType =
					new MemoryCacheService.ForcedIdCacheKey(null, theFhirId, theRequestPartitionId);
			myMemoryCacheService.putAfterCommit(
					MemoryCacheService.CacheEnum.RESOURCE_LOOKUP_BY_FORCED_ID, fhirIdKeyWithoutType, List.of(lookup));
		}
	}

	@VisibleForTesting
	public void setPartitionSettingsForUnitTest(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
	}

	@Override
	@Nonnull
	public List<JpaPid> getPidsOrThrowException(
			@Nonnull RequestPartitionId theRequestPartitionId, List<IIdType> theIds) {
		return resolveResourcePersistentIdsWithCache(theRequestPartitionId, theIds);
	}

	@Override
	@Nullable
	public JpaPid getPidOrNull(@Nonnull RequestPartitionId theRequestPartitionId, IBaseResource theResource) {
		Object resourceId = theResource.getUserData(RESOURCE_PID);
		JpaPid retVal;
		if (resourceId == null) {
			IIdType id = theResource.getIdElement();
			try {
				retVal = resolveResourceIdentityPid(
						theRequestPartitionId,
						id.getResourceType(),
						id.getIdPart(),
						ResolveIdentityMode.includeDeleted().cacheOk());
			} catch (ResourceNotFoundException e) {
				retVal = null;
			}
		} else {
			retVal = JpaPid.fromId(Long.parseLong(resourceId.toString()));
		}
		return retVal;
	}

	@Override
	@Nonnull
	public JpaPid getPidOrThrowException(@Nonnull RequestPartitionId theRequestPartitionId, IIdType theId) {
		List<IIdType> ids = Collections.singletonList(theId);
		List<JpaPid> resourcePersistentIds = resolveResourcePersistentIdsWithCache(theRequestPartitionId, ids);
		if (resourcePersistentIds.isEmpty()) {
			throw new InvalidRequestException(Msg.code(2295) + "Invalid ID was provided: [" + theId.getIdPart() + "]");
		}
		return resourcePersistentIds.get(0);
	}

	@Override
	@Nonnull
	public JpaPid getPidOrThrowException(@Nonnull IAnyResource theResource) {
		Long theResourcePID = (Long) theResource.getUserData(RESOURCE_PID);
		if (theResourcePID == null) {
			throw new IllegalStateException(Msg.code(2108)
					+ String.format(
							"Unable to find %s in the user data for %s with ID %s",
							RESOURCE_PID, theResource, theResource.getId()));
		}
		return JpaPid.fromId(theResourcePID);
	}

	@Override
	public IIdType resourceIdFromPidOrThrowException(JpaPid thePid, String theResourceType) {
		Optional<ResourceTable> optionalResource = myResourceTableDao.findById(thePid.getId());
		if (optionalResource.isEmpty()) {
			throw new ResourceNotFoundException(Msg.code(2124) + "Requested resource not found");
		}
		return optionalResource.get().getIdDt().toVersionless();
	}

	/**
	 * Given a set of PIDs, return a set of public FHIR Resource IDs.
	 * This function will resolve a forced ID if it resolves, and if it fails to resolve to a forced it, will just return the pid
	 * Example:
	 * Let's say we have Patient/1(pid == 1), Patient/pat1 (pid == 2), Patient/3 (pid == 3), their pids would resolve as follows:
	 * <p>
	 * [1,2,3] -> ["1","pat1","3"]
	 *
	 * @param thePids The Set of pids you would like to resolve to external FHIR Resource IDs.
	 * @return A Set of strings representing the FHIR IDs of the pids.
	 */
	@Override
	public Set<String> translatePidsToFhirResourceIds(Set<JpaPid> thePids) {
		assert TransactionSynchronizationManager.isSynchronizationActive();

		PersistentIdToForcedIdMap<JpaPid> pidToForcedIdMap = translatePidsToForcedIds(thePids);

		return pidToForcedIdMap.getResolvedResourceIds();
	}

	@Override
	public JpaPid newPid(Object thePid) {
		return JpaPid.fromId((Long) thePid);
	}

	@Override
	public JpaPid newPidFromStringIdAndResourceName(String thePid, String theResourceName) {
		return JpaPid.fromIdAndResourceType(Long.parseLong(thePid), theResourceName);
	}

	private IIdType newIdType(String theValue) {
		IIdType retVal = myFhirCtx.getVersion().newIdType();
		retVal.setValue(theValue);
		return retVal;
	}

	public static boolean isValidPid(IIdType theId) {
		if (theId == null) {
			return false;
		}

		String idPart = theId.getIdPart();
		return isValidPid(idPart);
	}

	public static boolean isValidPid(String theIdPart) {
		return StringUtils.isNumeric(theIdPart);
	}
}
