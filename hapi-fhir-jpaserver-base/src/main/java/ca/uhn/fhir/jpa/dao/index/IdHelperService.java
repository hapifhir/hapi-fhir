package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.cross.ResourceLookup;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
 * {@link IResourceLookup} or {@link ResourcePersistentId} depending on the method being called.
 * The former involves an extra database join that the latter does not require, so selecting the
 * right method here is important.
 * </p>
 */
@Service
public class IdHelperService {
	private static final String RESOURCE_PID = "RESOURCE_PID";
	private static final Logger ourLog = LoggerFactory.getLogger(IdHelperService.class);
	public static final Predicate[] EMPTY_PREDICATE_ARRAY = new Predicate[0];
	@Autowired
	protected IForcedIdDao myForcedIdDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	private MemoryCacheService myMemoryCacheService;

	public void delete(ForcedId forcedId) {
		myForcedIdDao.deleteByPid(forcedId.getId());
	}

	/**
	 * Given a forced ID, convert it to it's Long value. Since you are allowed to use string IDs for resources, we need to
	 * convert those to the underlying Long values that are stored, for lookup and comparison purposes.
	 *
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	public IResourceLookup resolveResourceIdentity(@Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, String theResourceId) throws ResourceNotFoundException {
		IdDt id = new IdDt(theResourceType, theResourceId);
		Map<String, IResourceLookup> matches = translateForcedIdToPids(theRequestPartitionId,
			Collections.singletonList(id));

		// We only pass 1 input in so only 0..1 will come back
		if (matches.isEmpty()) {
			throw new ResourceNotFoundException(id);
		}

		if (matches.size() > 1) {
			/*
			 *  This means that:
			 *  1. There are two resources with the exact same resource type and forced id
			 *  2. The unique constraint on this column-pair has been dropped
			 */
			String msg = myFhirCtx.getLocalizer().getMessage(IdHelperService.class, "nonUniqueForcedId");
			throw new PreconditionFailedException(msg);
		}

		return matches.get(theResourceId);
	}

	/**
	 * Returns a mapping of Id -> ResourcePersistentId.
	 * If any resource is not found, it will throw ResourceNotFound exception
	 * (and no map will be returned)
	 *
	 * @param theRequestPartitionId
	 * @param theResourceType
	 * @param theIds
	 * @return
	 */
	@Nonnull
	public Map<String, ResourcePersistentId> resolveResourcePersistentIds(@Nonnull RequestPartitionId theRequestPartitionId,
																						String theResourceType,
																						List<String> theIds) {
		Validate.notNull(theIds, "theIds cannot be null");
		Validate.isTrue(!theIds.isEmpty(), "theIds must not be empty");

		Map<String, ResourcePersistentId> retVals = new HashMap<>();

		for (String id : theIds) {
			ResourcePersistentId retVal;
			if (!idRequiresForcedId(id)) {
				// is already a PID
				retVal = new ResourcePersistentId(Long.parseLong(id));
				retVals.put(id, retVal);
			}
			else {
				// is a forced id
				// we must resolve!
				if (myDaoConfig.isDeleteEnabled()) {
					retVal = new ResourcePersistentId(resolveResourceIdentity(theRequestPartitionId, theResourceType, id).getResourceId());
					retVals.put(id, retVal);
				}
				else {
					// fetch from cache... adding to cache if not available
					String key = toForcedIdToPidKey(theRequestPartitionId, theResourceType, id);
					retVal = myMemoryCacheService.getThenPutAfterCommit(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID, key, t -> {
						List<IIdType> ids = Collections.singletonList(new IdType(theResourceType, id));
						// fetches from cache using a function that checks cache first...
						List<ResourcePersistentId> resolvedIds = resolveResourcePersistentIdsWithCache(theRequestPartitionId, ids);
						if (resolvedIds.isEmpty()) {
							throw new ResourceNotFoundException(ids.get(0));
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
	 *
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	public ResourcePersistentId resolveResourcePersistentIds(@Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, String theId) {
		Validate.notNull(theId, "theId must not be null");

		Map<String, ResourcePersistentId> retVal = resolveResourcePersistentIds(theRequestPartitionId,
			theResourceType,
			Collections.singletonList(theId));
		return retVal.get(theId); // should be only one
	}

	/**
	 * Returns true if the given resource ID should be stored in a forced ID. Under default config
	 * (meaning client ID strategy is {@link ca.uhn.fhir.jpa.api.config.DaoConfig.ClientIdStrategyEnum#ALPHANUMERIC})
	 * this will return true if the ID has any non-digit characters.
	 *
	 * In {@link ca.uhn.fhir.jpa.api.config.DaoConfig.ClientIdStrategyEnum#ANY} mode it will always return true.
	 */
	public boolean idRequiresForcedId(String theId) {
		return myDaoConfig.getResourceClientIdStrategy() == DaoConfig.ClientIdStrategyEnum.ANY || !isValidPid(theId);
	}

	@Nonnull
	private String toForcedIdToPidKey(@Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, String theId) {
		return RequestPartitionId.stringifyForKey(theRequestPartitionId) + "/" + theResourceType + "/" + theId;
	}

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Autowired
	private PartitionSettings myPartitionSettings;

	/**
	 * Given a collection of resource IDs (resource type + id), resolves the internal persistent IDs.
	 * <p>
	 * This implementation will always try to use a cache for performance, meaning that it can resolve resources that
	 * are deleted (but note that forced IDs can't change, so the cache can't return incorrect results)
	 */
	@Nonnull
	public List<ResourcePersistentId> resolveResourcePersistentIdsWithCache(RequestPartitionId theRequestPartitionId, List<IIdType> theIds) {
		for (IIdType id : theIds) {
			if (!id.hasIdPart()) {
				throw new InvalidRequestException("Parameter value missing in request");
			}
		}

		if (theIds.isEmpty()) {
			return Collections.emptyList();
		}

		List<ResourcePersistentId> retVal = new ArrayList<>(theIds.size());

		Set<IIdType> idsToCheck = new HashSet<>(theIds.size());
		for (IIdType nextId : theIds) {
			if (myDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY) {
				if (nextId.isIdPartValidLong()) {
					retVal.add(new ResourcePersistentId(nextId.getIdPartAsLong()).setAssociatedResourceId(nextId));
					continue;
				}
			}

			String key = toForcedIdToPidKey(theRequestPartitionId, nextId.getResourceType(), nextId.getIdPart());
			ResourcePersistentId cachedId = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID, key);
			if (cachedId != null) {
				retVal.add(cachedId);
				continue;
			}

			idsToCheck.add(nextId);
		}

		if (idsToCheck.size() > 0) {
			CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
			CriteriaQuery<ForcedId> criteriaQuery = cb.createQuery(ForcedId.class);
			Root<ForcedId> from = criteriaQuery.from(ForcedId.class);

			List<Predicate> predicates = new ArrayList<>(idsToCheck.size());
			for (IIdType next : idsToCheck) {

				List<Predicate> andPredicates = new ArrayList<>(3);

				if (isNotBlank(next.getResourceType())) {
					Predicate typeCriteria = cb.equal(from.get("myResourceType").as(String.class), next.getResourceType());
					andPredicates.add(typeCriteria);
				}

				Predicate idCriteria = cb.equal(from.get("myForcedId").as(String.class), next.getIdPart());
				andPredicates.add(idCriteria);

				if (theRequestPartitionId.isDefaultPartition() && myPartitionSettings.getDefaultPartitionId() == null) {
					Predicate partitionIdCriteria = cb.isNull(from.get("myPartitionIdValue").as(Integer.class));
					andPredicates.add(partitionIdCriteria);
				} else if (!theRequestPartitionId.isAllPartitions()) {
					List<Integer> partitionIds = theRequestPartitionId.getPartitionIds();
					partitionIds = replaceDefaultPartitionIdIfNonNull(myPartitionSettings, partitionIds);

					if (partitionIds.size() > 1) {
						Predicate partitionIdCriteria = from.get("myPartitionIdValue").as(Integer.class).in(partitionIds);
						andPredicates.add(partitionIdCriteria);
					} else {
						Predicate partitionIdCriteria = cb.equal(from.get("myPartitionIdValue").as(Integer.class), partitionIds.get(0));
						andPredicates.add(partitionIdCriteria);
					}
				}

				predicates.add(cb.and(andPredicates.toArray(EMPTY_PREDICATE_ARRAY)));
			}

			criteriaQuery.where(cb.or(predicates.toArray(EMPTY_PREDICATE_ARRAY)));

			TypedQuery<ForcedId> query = myEntityManager.createQuery(criteriaQuery);
			List<ForcedId> results = query.getResultList();
			for (ForcedId nextId : results) {
				ResourcePersistentId persistentId = new ResourcePersistentId(nextId.getResourceId());
				populateAssociatedResourceId(nextId.getResourceType(), nextId.getForcedId(), persistentId);
				retVal.add(persistentId);

				String key = toForcedIdToPidKey(theRequestPartitionId, nextId.getResourceType(), nextId.getForcedId());
				myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID, key, persistentId);
			}

		}

		return retVal;
	}

	private void populateAssociatedResourceId(String nextResourceType, String forcedId, ResourcePersistentId persistentId) {
		IIdType resourceId = myFhirCtx.getVersion().newIdType();
		resourceId.setValue(nextResourceType + "/" + forcedId);
		persistentId.setAssociatedResourceId(resourceId);
	}

	/**
	 * Given a persistent ID, returns the associated resource ID
	 */
	@Nonnull
	public IIdType translatePidIdToForcedId(FhirContext theCtx, String theResourceType, ResourcePersistentId theId) {
		IIdType retVal = theCtx.getVersion().newIdType();

		Optional<String> forcedId = translatePidIdToForcedIdWithCache(theId);
		if (forcedId.isPresent()) {
			retVal.setValue(theResourceType + '/' + forcedId.get());
		} else {
			retVal.setValue(theResourceType + '/' + theId);
		}

		return retVal;
	}

	public Optional<String> translatePidIdToForcedIdWithCache(ResourcePersistentId theId) {
		return myMemoryCacheService.get(MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, theId.getIdAsLong(), pid -> myForcedIdDao.findByResourcePid(pid).map(t -> t.getForcedId()));
	}

	private ListMultimap<String, String> organizeIdsByResourceType(Collection<IIdType> theIds) {
		ListMultimap<String, String> typeToIds = MultimapBuilder.hashKeys().arrayListValues().build();
		for (IIdType nextId : theIds) {
			if (myDaoConfig.getResourceClientIdStrategy() == DaoConfig.ClientIdStrategyEnum.ANY || !isValidPid(nextId)) {
				if (nextId.hasResourceType()) {
					typeToIds.put(nextId.getResourceType(), nextId.getIdPart());
				} else {
					typeToIds.put("", nextId.getIdPart());
				}
			}
		}
		return typeToIds;
	}

	private Map<String, IResourceLookup> translateForcedIdToPids(@Nonnull RequestPartitionId theRequestPartitionId, Collection<IIdType> theId) {
		theId.forEach(id -> Validate.isTrue(id.hasIdPart()));

		if (theId.isEmpty()) {
			return new HashMap<>();
		}

		Map<String, IResourceLookup> retVal = new HashMap<>();
		RequestPartitionId requestPartitionId = replaceDefault(theRequestPartitionId);

		if (myDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY) {
			List<Long> pids = theId
				.stream()
				.filter(t -> isValidPid(t))
				.map(t -> t.getIdPartAsLong())
				.collect(Collectors.toList());
			if (!pids.isEmpty()) {
				resolvePids(requestPartitionId, pids, retVal);
			}
		}

		// returns a map of resourcetype->id
		ListMultimap<String, String> typeToIds = organizeIdsByResourceType(theId);
		for (Map.Entry<String, Collection<String>> nextEntry : typeToIds.asMap().entrySet()) {
			String nextResourceType = nextEntry.getKey();
			Collection<String> nextIds = nextEntry.getValue();

			if (!myDaoConfig.isDeleteEnabled()) {
				for (Iterator<String> forcedIdIterator = nextIds.iterator(); forcedIdIterator.hasNext(); ) {
					String nextForcedId = forcedIdIterator.next();
					String nextKey = nextResourceType + "/" + nextForcedId;
					IResourceLookup cachedLookup = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP, nextKey);
					if (cachedLookup != null) {
						forcedIdIterator.remove();
						retVal.put(nextForcedId, cachedLookup);
					}
				}
			}

			if (nextIds.size() > 0) {
				Collection<Object[]> views;
				assert isNotBlank(nextResourceType);

				if (requestPartitionId.isAllPartitions()) {
					views = myForcedIdDao.findAndResolveByForcedIdWithNoType(nextResourceType, nextIds);
				} else {
					if (requestPartitionId.isDefaultPartition()) {
						views = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(nextResourceType, nextIds);
					} else if (requestPartitionId.hasDefaultPartitionId()) {
						views = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionIdOrNullPartitionId(nextResourceType, nextIds, requestPartitionId.getPartitionIdsWithoutDefault());
					} else {
						views = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartition(nextResourceType, nextIds, requestPartitionId.getPartitionIds());
					}
				}

				for (Object[] next : views) {
					String resourceType = (String) next[0];
					Long resourcePid = (Long) next[1];
					String forcedId = (String) next[2];
					Date deletedAt = (Date) next[3];
					ResourceLookup lookup = new ResourceLookup(resourceType, resourcePid, deletedAt);
					retVal.put(forcedId, lookup);

					if (!myDaoConfig.isDeleteEnabled()) {
						String key = resourceType + "/" + forcedId;
						myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP, key, lookup);
					}
				}
			}

		}

		return retVal;
	}

	RequestPartitionId replaceDefault(RequestPartitionId theRequestPartitionId) {
		if (myPartitionSettings.getDefaultPartitionId() != null) {
			if (!theRequestPartitionId.isAllPartitions() && theRequestPartitionId.hasDefaultPartitionId()) {
				List<Integer> partitionIds = theRequestPartitionId
					.getPartitionIds()
					.stream()
					.map(t -> t == null ? myPartitionSettings.getDefaultPartitionId() : t)
					.collect(Collectors.toList());
				return RequestPartitionId.fromPartitionIds(partitionIds);
			}
		}
		return theRequestPartitionId;
	}

	private void resolvePids(@Nonnull RequestPartitionId theRequestPartitionId, List<Long> thePidsToResolve, Map<String, IResourceLookup> theTargets) {
		if (!myDaoConfig.isDeleteEnabled()) {
			for (Iterator<Long> forcedIdIterator = thePidsToResolve.iterator(); forcedIdIterator.hasNext(); ) {
				Long nextPid = forcedIdIterator.next();
				String nextKey = Long.toString(nextPid);
				IResourceLookup cachedLookup = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP, nextKey);
				if (cachedLookup != null) {
					forcedIdIterator.remove();
					theTargets.put(nextKey, cachedLookup);
				}
			}
		}

		if (thePidsToResolve.size() > 0) {
			Collection<Object[]> lookup;
			if (theRequestPartitionId.isAllPartitions()) {
				lookup = myResourceTableDao.findLookupFieldsByResourcePid(thePidsToResolve);
			} else {
				if (theRequestPartitionId.isDefaultPartition()) {
					lookup = myResourceTableDao.findLookupFieldsByResourcePidInPartitionNull(thePidsToResolve);
				} else if (theRequestPartitionId.hasDefaultPartitionId()) {
					lookup = myResourceTableDao.findLookupFieldsByResourcePidInPartitionIdsOrNullPartition(thePidsToResolve, theRequestPartitionId.getPartitionIdsWithoutDefault());
				} else {
					lookup = myResourceTableDao.findLookupFieldsByResourcePidInPartitionIds(thePidsToResolve, theRequestPartitionId.getPartitionIds());
				}
			}
			lookup
				.stream()
				.map(t -> new ResourceLookup((String) t[0], (Long) t[1], (Date) t[2]))
				.forEach(t -> {
					theTargets.put(t.getResourceId().toString(), t);
					if (!myDaoConfig.isDeleteEnabled()) {
						String nextKey = Long.toString(t.getResourceId());
						myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.RESOURCE_LOOKUP, nextKey, t);
					}
				});

		}
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
	public Set<String> translatePidsToFhirResourceIds(Set<Long> thePids) {
		Map<Long, Optional<String>> pidToForcedIdMap = translatePidsToForcedIds(thePids);

		//If the result of the translation is an empty optional, it means there is no forced id, and we can use the PID as the resource ID.
		Set<String> resolvedResourceIds = pidToForcedIdMap.entrySet().stream()
			.map(entry -> entry.getValue().isPresent() ? entry.getValue().get() : entry.getKey().toString())
			.collect(Collectors.toSet());

		return resolvedResourceIds;

	}

	public Map<Long, Optional<String>> translatePidsToForcedIds(Set<Long> thePids) {
		Map<Long, Optional<String>> retVal = new HashMap<>(myMemoryCacheService.getAllPresent(MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, thePids));

		List<Long> remainingPids = thePids
			.stream()
			.filter(t -> !retVal.containsKey(t))
			.collect(Collectors.toList());

		new QueryChunker<Long>().chunk(remainingPids, t -> {
			List<ForcedId> forcedIds = myForcedIdDao.findAllByResourcePid(t);

			for (ForcedId forcedId : forcedIds) {
				Long nextResourcePid = forcedId.getResourceId();
				Optional<String> nextForcedId = Optional.of(forcedId.getForcedId());
				retVal.put(nextResourcePid, nextForcedId);
				myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, nextResourcePid, nextForcedId);
			}
		});

		remainingPids = thePids
			.stream()
			.filter(t -> !retVal.containsKey(t))
			.collect(Collectors.toList());
		for (Long nextResourcePid : remainingPids) {
			retVal.put(nextResourcePid, Optional.empty());
			myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, nextResourcePid, Optional.empty());
		}

		return retVal;
	}

	/**
	 * @deprecated This method doesn't take a partition ID as input, so it is unsafe. It
	 * should be reworked to include the partition ID before any new use is incorporated
	 */
	@Deprecated
	@Nullable
	public Long getPidOrNull(IBaseResource theResource) {
		IAnyResource anyResource = (IAnyResource) theResource;
		Long retVal = (Long) anyResource.getUserData(RESOURCE_PID);
		if (retVal == null) {
			IIdType id = theResource.getIdElement();
			try {
				retVal = this.resolveResourcePersistentIds(RequestPartitionId.allPartitions(), id.getResourceType(), id.getIdPart()).getIdAsLong();
			} catch (ResourceNotFoundException e) {
				return null;
			}
		}
		return retVal;
	}

	/**
	 * @deprecated This method doesn't take a partition ID as input, so it is unsafe. It
	 * should be reworked to include the partition ID before any new use is incorporated
	 */
	@Deprecated
	@Nonnull
	public Long getPidOrThrowException(IIdType theId) {
		List<IIdType> ids = Collections.singletonList(theId);
		List<ResourcePersistentId> resourcePersistentIds = this.resolveResourcePersistentIdsWithCache(RequestPartitionId.allPartitions(), ids);
		return resourcePersistentIds.get(0).getIdAsLong();
	}

	/**
	 * @deprecated This method doesn't take a partition ID as input, so it is unsafe. It
	 * should be reworked to include the partition ID before any new use is incorporated
	 */
	@Deprecated
	@Nonnull
	public List<Long> getPidsOrThrowException(List<IIdType> theIds) {
		List<ResourcePersistentId> resourcePersistentIds = this.resolveResourcePersistentIdsWithCache(RequestPartitionId.allPartitions(), theIds);
		return resourcePersistentIds.stream().map(ResourcePersistentId::getIdAsLong).collect(Collectors.toList());
	}

	@Nonnull
	public Long getPidOrThrowException(IAnyResource theResource) {
		Long retVal = (Long) theResource.getUserData(RESOURCE_PID);
		if (retVal == null) {
			throw new IllegalStateException(
				String.format("Unable to find %s in the user data for %s with ID %s", RESOURCE_PID, theResource, theResource.getId())
			);
		}
		return retVal;
	}

	public IIdType resourceIdFromPidOrThrowException(Long thePid) {
		Optional<ResourceTable> optionalResource = myResourceTableDao.findById(thePid);
		if (!optionalResource.isPresent()) {
			throw new ResourceNotFoundException("Requested resource not found");
		}
		return optionalResource.get().getIdDt().toVersionless();
	}

	/**
	 * Pre-cache a PID-to-Resource-ID mapping for later retrieval by {@link #translatePidsToForcedIds(Set)} and related methods
	 */
	public void addResolvedPidToForcedId(ResourcePersistentId theResourcePersistentId, @Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, @Nullable String theForcedId) {
		if (theForcedId != null) {
			if (theResourcePersistentId.getAssociatedResourceId() == null) {
				populateAssociatedResourceId(theResourceType, theForcedId, theResourcePersistentId);
			}

			myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, theResourcePersistentId.getIdAsLong(), Optional.of(theForcedId));
			String key = toForcedIdToPidKey(theRequestPartitionId, theResourceType, theForcedId);
			myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.FORCED_ID_TO_PID, key, theResourcePersistentId);
		} else {
			myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.PID_TO_FORCED_ID, theResourcePersistentId.getIdAsLong(), Optional.empty());
		}
	}

	@VisibleForTesting
	void setPartitionSettingsForUnitTest(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
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
