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
import ca.uhn.fhir.jpa.dao.DaoConfig;
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
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
		myPersistentIdCache = Caffeine
			.newBuilder()
			.maximumSize(10000)
			.build();
		myResourceLookupCache = Caffeine
			.newBuilder()
			.maximumSize(10000)
			.build();
	}

	public void delete(ForcedId forcedId) {
		myForcedIdDao.deleteByPid(forcedId.getId());
	}

	/**
	 * @throws ResourceNotFoundException If the ID can not be found
	 */
	@Nonnull
	public IResourceLookup translateForcedIdToPid(String theResourceName, String theResourceId, RequestDetails theRequestDetails) throws ResourceNotFoundException {
		// We only pass 1 input in so only 0..1 will come back
		IdDt id = new IdDt(theResourceName, theResourceId);
		Collection<IResourceLookup> matches = translateForcedIdToPids(theRequestDetails, Collections.singletonList(id));
		assert matches.size() <= 1;
		if (matches.isEmpty()) {
			throw new ResourceNotFoundException(id);
		}
		return matches.iterator().next();
	}

	public IIdType translatePidIdToForcedId(FhirContext theCtx, String theResourceType, ResourcePersistentId theId) {
		IIdType retVal = theCtx.getVersion().newIdType();
		retVal.setValue(translatePidIdToForcedId(theResourceType, theId));
		return retVal;
	}

	public String translatePidIdToForcedId(String theResourceType, ResourcePersistentId theId) {
		ForcedId forcedId = myForcedIdDao.findByResourcePid(theId.getIdAsLong());
		if (forcedId != null) {
			return forcedId.getResourceType() + '/' + forcedId.getForcedId();
		} else {
			return theResourceType + '/' + theId.toString();
		}
	}

	// FIXME: rename methods in this
	@Nonnull
	public ResourcePersistentId translateForcedIdToPid_(String theResourceType, String theId, RequestDetails theRequest) {
		Long retVal;
		if (myDaoConfig.getResourceClientIdStrategy() == DaoConfig.ClientIdStrategyEnum.ANY || !isValidPid(theId)) {
			if (myDaoConfig.isDeleteEnabled()) {
				retVal = translateForcedIdToPid(theResourceType, theId);
			} else {
				String key = theResourceType + "/" + theId;
				retVal = myPersistentIdCache.get(key, t -> translateForcedIdToPid(theResourceType, theId));
			}

		} else {
			retVal = Long.parseLong(theId);
		}

		return new ResourcePersistentId(retVal);
	}

	public Long translateForcedIdToPid(String theResourceType, String theId) {
		Long retVal;
		retVal = myForcedIdDao
			.findByTypeAndForcedId(theResourceType, theId)
			.orElseThrow(() -> new ResourceNotFoundException(new IdDt(theResourceType, theId)));
		return retVal;
	}

	public List<ResourcePersistentId> translateForcedIdToPids_(List<IIdType> theIds, RequestDetails theRequest) {
		theIds.forEach(id -> Validate.isTrue(id.hasIdPart()));

		if (theIds.isEmpty()) {
			return Collections.emptyList();
		}

		List<ResourcePersistentId> retVal = new ArrayList<>();

		if (myDaoConfig.getResourceClientIdStrategy() != DaoConfig.ClientIdStrategyEnum.ANY) {
			theIds
				.stream()
				.filter(t -> isValidPid(t))
				.map(t -> t.getIdPartAsLong())
				.map(t -> new ResourcePersistentId(t))
				.forEach(t -> retVal.add(t));
		}

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

		for (Map.Entry<String, Collection<String>> nextEntry : typeToIds.asMap().entrySet()) {
			String nextResourceType = nextEntry.getKey();
			Collection<String> nextIds = nextEntry.getValue();
			if (isBlank(nextResourceType)) {

				StorageProcessingMessage msg = new StorageProcessingMessage()
					.setMessage("This search uses unqualified resource IDs (an ID without a resource type). This is less efficient than using a qualified type.");
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(StorageProcessingMessage.class, msg);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_WARNING, params);

				List<Long> views = myForcedIdDao.findByForcedId(nextIds);
				views.forEach(t -> retVal.add(new ResourcePersistentId(t)));

			} else {

				for (Itera)

				Collection<Long> views = myForcedIdDao.findByTypeAndForcedId(nextResourceType, nextIds);
				views.forEach(t -> retVal.add(new ResourcePersistentId(t)));

			}
		}

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

				Collection<Object[]> lookups = myResourceTableDao.findLookupFieldsByResourcePid(pids);
				for (Object[] next : lookups) {
					String resourceType = (String) next[0];
					Long resourcePid = (Long) next[1];
					Date deletedAt = (Date) next[2];
					retVal.add(new ResourceLookup(resourceType, resourcePid, deletedAt));
				}

			}
		}

		ListMultimap<String, String> typeToIds = MultimapBuilder.hashKeys().arrayListValues().build();
		for (IIdType nextId : theId) {
			if (myDaoConfig.getResourceClientIdStrategy() == DaoConfig.ClientIdStrategyEnum.ANY || !isValidPid(nextId)) {
				if (nextId.hasResourceType()) {
					typeToIds.put(nextId.getResourceType(), nextId.getIdPart());
				} else {
					typeToIds.put("", nextId.getIdPart());
				}
			}
		}

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

					StorageProcessingMessage msg = new StorageProcessingMessage()
						.setMessage("This search uses unqualified resource IDs (an ID without a resource type). This is less efficient than using a qualified type.");
					HookParams params = new HookParams()
						.add(RequestDetails.class, theRequest)
						.addIfMatchesType(ServletRequestDetails.class, theRequest)
						.add(StorageProcessingMessage.class, msg);
					JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_WARNING, params);

					views = myForcedIdDao.findAndResolveByTypeAndForcedId(nextIds);

				} else {

					views = myForcedIdDao.findAndResolveByTypeAndForcedId(nextResourceType, nextIds);

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
