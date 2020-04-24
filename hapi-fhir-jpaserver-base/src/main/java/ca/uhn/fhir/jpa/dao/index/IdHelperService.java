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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.cross.ResourceLookup;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
	private static final Logger ourLog = LoggerFactory.getLogger(IdHelperService.class);

	@Autowired
	protected IForcedIdDao myForcedIdDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired(required = true)
	private DaoConfig myDaoConfig;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	private Cache<String, Long> myPersistentIdCache;
	private Cache<String, IResourceLookup> myResourceLookupCache;

	@PostConstruct
	public void start() {
		myPersistentIdCache = newCache();
		myResourceLookupCache = newCache();
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
	public IResourceLookup resolveResourceIdentity(String theResourceName, String theResourceId, RequestDetails theRequestDetails) throws ResourceNotFoundException {
		// We only pass 1 input in so only 0..1 will come back
		IdDt id = new IdDt(theResourceName, theResourceId);
		Collection<IResourceLookup> matches = translateForcedIdToPids(theRequestDetails, Collections.singletonList(id));
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
	public ResourcePersistentId resolveResourcePersistentIds(String theResourceType, String theId) {
		Long retVal;
		if (myDaoConfig.getResourceClientIdStrategy() == DaoConfig.ClientIdStrategyEnum.ANY || !isValidPid(theId)) {
			if (myDaoConfig.isDeleteEnabled()) {
				retVal = resolveResourceIdentity(theResourceType, theId);
			} else {
				String key = theResourceType + "/" + theId;
				retVal = myPersistentIdCache.get(key, t -> resolveResourceIdentity(theResourceType, theId));
			}

		} else {
			retVal = Long.parseLong(theId);
		}

		return new ResourcePersistentId(retVal);
	}

	/**
	 * Given a collection of resource IDs (resource type + id), resolves the internal persistent IDs
	 */
	@Nonnull
	public List<ResourcePersistentId> resolveResourcePersistentIds(List<IIdType> theIds, RequestDetails theRequest) {
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

				if (!myDaoConfig.isDeleteEnabled()) {
					for (Iterator<String> idIterator = nextIds.iterator(); idIterator.hasNext(); ) {
						String nextId = idIterator.next();
						String key = nextResourceType + "/" + nextId;
						Long nextCachedPid = myPersistentIdCache.getIfPresent(key);
						if (nextCachedPid != null) {
							idIterator.remove();
							retVal.add(new ResourcePersistentId(nextCachedPid));
						}
					}
				}

				if (nextIds.size() > 0) {
					Collection<Object[]> views = myForcedIdDao.findByTypeAndForcedId(nextResourceType, nextIds);
					for (Object[] nextView : views) {
						String forcedId = (String) nextView[0];
						Long pid = (Long) nextView[1];
						retVal.add(new ResourcePersistentId(pid));

						if (!myDaoConfig.isDeleteEnabled()) {
							String key = nextResourceType + "/" + forcedId;
							myPersistentIdCache.put(key, pid);
						}
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
		retVal.setValue(translatePidIdToForcedId(theResourceType, theId));
		return retVal;
	}

	private String translatePidIdToForcedId(String theResourceType, ResourcePersistentId theId) {
		ForcedId forcedId = myForcedIdDao.findByResourcePid(theId.getIdAsLong());
		if (forcedId != null) {
			return forcedId.getResourceType() + '/' + forcedId.getForcedId();
		} else {
			return theResourceType + '/' + theId.toString();
		}
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

	private Long resolveResourceIdentity(String theResourceType, String theId) {
		Long retVal;
		retVal = myForcedIdDao
			.findByTypeAndForcedId(theResourceType, theId)
			.orElseThrow(() -> new ResourceNotFoundException(new IdDt(theResourceType, theId)));
		return retVal;
	}

	private Collection<IResourceLookup> translateForcedIdToPids(RequestDetails theRequest, Collection<IIdType> theId) {
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
				myResourceTableDao.findLookupFieldsByResourcePid(pids)
					.stream()
					.map(lookup -> new ResourceLookup((String)lookup[0], (Long)lookup[1], (Date)lookup[2]))
					.forEach(retVal::add);
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
				if (isBlank(nextResourceType)) {
					warnAboutUnqualifiedForcedIdResolution(theRequest);
					views = myForcedIdDao.findAndResolveByForcedIdWithNoType(nextIds);

				} else {

					views = myForcedIdDao.findAndResolveByForcedIdWithNoType(nextResourceType, nextIds);

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

	private void warnAboutUnqualifiedForcedIdResolution(RequestDetails theRequest) {
		StorageProcessingMessage msg = new StorageProcessingMessage()
			.setMessage("This search uses unqualified resource IDs (an ID without a resource type). This is less efficient than using a qualified type.");
		ourLog.debug(msg.getMessage());
		HookParams params = new HookParams()
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(StorageProcessingMessage.class, msg);
		JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_WARNING, params);
	}

	public void clearCache() {
		myPersistentIdCache.invalidateAll();
		myResourceLookupCache.invalidateAll();
	}

	private <T, V> @NonNull Cache<T, V> newCache() {
		return Caffeine
			.newBuilder()
			.maximumSize(10000)
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build();
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
