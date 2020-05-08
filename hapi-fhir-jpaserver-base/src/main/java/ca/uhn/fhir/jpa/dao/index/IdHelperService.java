package ca.uhn.fhir.jpa.dao.index;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.cross.ResourceLookup;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
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

	@Autowired
	protected IForcedIdDao myForcedIdDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired(required = true)
	private DaoConfig myDaoConfig;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private FhirContext myFhirCtx;

	private Cache<String, Long> myPersistentIdCache;
	private Cache<String, IResourceLookup> myResourceLookupCache;
	private Cache<Long, Optional<String>> myForcedIdCache;

	@PostConstruct
	public void start() {
		myPersistentIdCache = newCache();
		myResourceLookupCache = newCache();
		myForcedIdCache = newCache();
	}


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
	public IResourceLookup resolveResourceIdentity(@Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, String theResourceId, RequestDetails theRequestDetails) throws ResourceNotFoundException {
		// We only pass 1 input in so only 0..1 will come back
		IdDt id = new IdDt(theResourceType, theResourceId);
		Collection<IResourceLookup> matches = translateForcedIdToPids(theRequestPartitionId, theRequestDetails, Collections.singletonList(id));
		assert matches.size() <= 1;
		if (matches.isEmpty()) {
			throw new ResourceNotFoundException(id);
		}
		return matches.iterator().next();
	}

	/**
	 * Given a resource type and ID, determines the internal persistent ID for the resource.
	 *
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	public ResourcePersistentId resolveResourcePersistentIds(@Nonnull RequestPartitionId theRequestPartitionId, String theResourceType, String theId) {
		Long retVal;
		if (myDaoConfig.getResourceClientIdStrategy() == DaoConfig.ClientIdStrategyEnum.ANY || !isValidPid(theId)) {
			if (myDaoConfig.isDeleteEnabled()) {
				retVal = resolveResourceIdentity(theRequestPartitionId, theResourceType, theId);
			} else {
				String key = RequestPartitionId.stringifyForKey(theRequestPartitionId) + "/" + theResourceType + "/" + theId;
				retVal = myPersistentIdCache.get(key, t -> resolveResourceIdentity(theRequestPartitionId, theResourceType, theId));
			}

		} else {
			retVal = Long.parseLong(theId);
		}

		return new ResourcePersistentId(retVal);
	}

	/**
	 * Given a collection of resource IDs (resource type + id), resolves the internal persistent IDs.
	 * <p>
	 * This implementation will always try to use a cache for performance, meaning that it can resolve resources that
	 * are deleted (but note that forced IDs can't change, so the cache can't return incorrect results)
	 */
	@Nonnull
	public List<ResourcePersistentId> resolveResourcePersistentIdsWithCache(RequestPartitionId theRequestPartitionId, List<IIdType> theIds) {
		theIds.forEach(id -> Validate.isTrue(id.hasIdPart()));

		if (theIds.isEmpty()) {
			return Collections.emptyList();
		}

		List<ResourcePersistentId> retVal = new ArrayList<>();

		if (myDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY) {
			theIds
				.stream()
				.filter(IdHelperService::isValidPid)
				.map(IIdType::getIdPartAsLong)
				.map(ResourcePersistentId::new)
				.forEach(retVal::add);
		}

		ListMultimap<String, String> typeToIds = organizeIdsByResourceType(theIds);

		for (Map.Entry<String, Collection<String>> nextEntry : typeToIds.asMap().entrySet()) {
			String nextResourceType = nextEntry.getKey();
			Collection<String> nextIds = nextEntry.getValue();
			if (isBlank(nextResourceType)) {

				List<Long> views = myForcedIdDao.findByForcedId(nextIds);
				views.forEach(t -> retVal.add(new ResourcePersistentId(t)));

			} else {

				for (Iterator<String> idIterator = nextIds.iterator(); idIterator.hasNext(); ) {
					String nextId = idIterator.next();
					String key = RequestPartitionId.stringifyForKey(theRequestPartitionId) + "/" + nextResourceType + "/" + nextId;
					Long nextCachedPid = myPersistentIdCache.getIfPresent(key);
					if (nextCachedPid != null) {
						idIterator.remove();
						retVal.add(new ResourcePersistentId(nextCachedPid));
					}
				}

				if (nextIds.size() > 0) {

					Collection<Object[]> views;
					if (theRequestPartitionId.isAllPartitions()) {
						views = myForcedIdDao.findByTypeAndForcedId(nextResourceType, nextIds);
					} else {
						if (theRequestPartitionId.getPartitionId() != null) {
							views = myForcedIdDao.findByTypeAndForcedIdInPartition(nextResourceType, nextIds, theRequestPartitionId.getPartitionId());
						} else {
							views = myForcedIdDao.findByTypeAndForcedIdInPartitionNull(nextResourceType, nextIds);
						}
					}
					for (Object[] nextView : views) {
						String forcedId = (String) nextView[0];
						Long pid = (Long) nextView[1];
						retVal.add(new ResourcePersistentId(pid));

						String key = RequestPartitionId.stringifyForKey(theRequestPartitionId) + "/" + nextResourceType + "/" + forcedId;
						myPersistentIdCache.put(key, pid);
					}
				}

			}
		}

		return retVal;
	}

	/**
	 * Given a persistent ID, returns the associated resource ID
	 */
	@Nonnull
	public IIdType translatePidIdToForcedId(FhirContext theCtx, String theResourceType, ResourcePersistentId theId) {
		IIdType retVal = theCtx.getVersion().newIdType();

		Optional<String> forcedId = translatePidIdToForcedId(theId);
		if (forcedId.isPresent()) {
			retVal.setValue(theResourceType + '/' + forcedId.get());
		} else {
			retVal.setValue(theResourceType + '/' + theId.toString());
		}

		return retVal;
	}


	public Optional<String> translatePidIdToForcedId(ResourcePersistentId theId) {
		return myForcedIdCache.get(theId.getIdAsLong(), pid -> myForcedIdDao.findByResourcePid(pid).map(t -> t.getForcedId()));
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

	private Long resolveResourceIdentity(@Nonnull RequestPartitionId theRequestPartitionId, @Nonnull String theResourceType, @Nonnull String theId) {
		Optional<Long> pid;
		if (theRequestPartitionId.isAllPartitions()) {
			try {
				pid = myForcedIdDao.findByTypeAndForcedId(theResourceType, theId);
			} catch (IncorrectResultSizeDataAccessException e) {
				/*
				 *  This means that:
				 *  1. There are two resources with the exact same resource type and forced id
				 *  2. The unique constraint on this column-pair has been dropped
				 */
				String msg = myFhirCtx.getLocalizer().getMessage(IdHelperService.class, "nonUniqueForcedId");
				throw new PreconditionFailedException(msg);
			}
		} else {
			if (theRequestPartitionId.getPartitionId() == null) {
				pid = myForcedIdDao.findByPartitionIdNullAndTypeAndForcedId(theResourceType, theId);
			} else {
				pid = myForcedIdDao.findByPartitionIdAndTypeAndForcedId(theRequestPartitionId.getPartitionId(), theResourceType, theId);
			}
		}

		if (!pid.isPresent()) {
			throw new ResourceNotFoundException(new IdDt(theResourceType, theId));
		}
		return pid.get();
	}

	private Collection<IResourceLookup> translateForcedIdToPids(@Nonnull RequestPartitionId theRequestPartitionId, RequestDetails theRequest, Collection<IIdType> theId) {
		theId.forEach(id -> Validate.isTrue(id.hasIdPart()));

		if (theId.isEmpty()) {
			return Collections.emptyList();
		}

		List<IResourceLookup> retVal = new ArrayList<>();

		if (myDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY) {
			List<Long> pids = theId
				.stream()
				.filter(t -> isValidPid(t))
				.map(t -> t.getIdPartAsLong())
				.collect(Collectors.toList());
			if (!pids.isEmpty()) {
				resolvePids(theRequestPartitionId, pids, retVal);
			}
		}

		ListMultimap<String, String> typeToIds = organizeIdsByResourceType(theId);
		for (Map.Entry<String, Collection<String>> nextEntry : typeToIds.asMap().entrySet()) {
			String nextResourceType = nextEntry.getKey();
			Collection<String> nextIds = nextEntry.getValue();

			if (!myDaoConfig.isDeleteEnabled()) {
				for (Iterator<String> forcedIdIterator = nextIds.iterator(); forcedIdIterator.hasNext(); ) {
					String nextForcedId = forcedIdIterator.next();
					String nextKey = nextResourceType + "/" + nextForcedId;
					IResourceLookup cachedLookup = myResourceLookupCache.getIfPresent(nextKey);
					if (cachedLookup != null) {
						forcedIdIterator.remove();
						retVal.add(cachedLookup);
					}
				}
			}

			if (nextIds.size() > 0) {
				Collection<Object[]> views;
				assert isNotBlank(nextResourceType);

				if (theRequestPartitionId.isAllPartitions()) {
					views = myForcedIdDao.findAndResolveByForcedIdWithNoType(nextResourceType, nextIds);
				} else {
					if (theRequestPartitionId.getPartitionId() != null) {
						views = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartition(nextResourceType, nextIds, theRequestPartitionId.getPartitionId());
					} else {
						views = myForcedIdDao.findAndResolveByForcedIdWithNoTypeInPartitionNull(nextResourceType, nextIds);
					}
				}

				for (Object[] next : views) {
					String resourceType = (String) next[0];
					Long resourcePid = (Long) next[1];
					String forcedId = (String) next[2];
					Date deletedAt = (Date) next[3];
					ResourceLookup lookup = new ResourceLookup(resourceType, resourcePid, deletedAt);
					retVal.add(lookup);

					if (!myDaoConfig.isDeleteEnabled()) {
						String key = resourceType + "/" + forcedId;
						myResourceLookupCache.put(key, lookup);
					}
				}
			}

		}

		return retVal;
	}

	private void resolvePids(@Nonnull RequestPartitionId theRequestPartitionId, List<Long> thePidsToResolve, List<IResourceLookup> theTarget) {

		if (!myDaoConfig.isDeleteEnabled()) {
			for (Iterator<Long> forcedIdIterator = thePidsToResolve.iterator(); forcedIdIterator.hasNext(); ) {
				Long nextPid = forcedIdIterator.next();
				String nextKey = Long.toString(nextPid);
				IResourceLookup cachedLookup = myResourceLookupCache.getIfPresent(nextKey);
				if (cachedLookup != null) {
					forcedIdIterator.remove();
					theTarget.add(cachedLookup);
				}
			}
		}

		if (thePidsToResolve.size() > 0) {
			Collection<Object[]> lookup;
			if (theRequestPartitionId.isAllPartitions()) {
				lookup = myResourceTableDao.findLookupFieldsByResourcePid(thePidsToResolve);
			} else {
				if (theRequestPartitionId.getPartitionId() != null) {
					lookup = myResourceTableDao.findLookupFieldsByResourcePidInPartition(thePidsToResolve, theRequestPartitionId.getPartitionId());
				} else {
					lookup = myResourceTableDao.findLookupFieldsByResourcePidInPartitionNull(thePidsToResolve);
				}
			}
			lookup
				.stream()
				.map(t -> new ResourceLookup((String) t[0], (Long) t[1], (Date) t[2]))
				.forEach(t->{
					theTarget.add(t);
					if (!myDaoConfig.isDeleteEnabled()) {
						String nextKey = Long.toString(t.getResourceId());
						myResourceLookupCache.put(nextKey, t);
					}
				});

		}
	}

	public void clearCache() {
		myPersistentIdCache.invalidateAll();
		myResourceLookupCache.invalidateAll();
		myForcedIdCache.invalidateAll();
	}

	private <T, V> @NonNull Cache<T, V> newCache() {
		return Caffeine
			.newBuilder()
			.maximumSize(10000)
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build();
	}

	public Map<Long, Optional<String>> translatePidsToForcedIds(Set<Long> thePids) {

		Map<Long, Optional<String>> retVal = new HashMap<>(myForcedIdCache.getAllPresent(thePids));

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
				myForcedIdCache.put(nextResourcePid, nextForcedId);
			}
		});

		remainingPids = thePids
			.stream()
			.filter(t -> !retVal.containsKey(t))
			.collect(Collectors.toList());
		for (Long nextResourcePid : remainingPids) {
			retVal.put(nextResourcePid, Optional.empty());
			myForcedIdCache.put(nextResourcePid, Optional.empty());
		}

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
