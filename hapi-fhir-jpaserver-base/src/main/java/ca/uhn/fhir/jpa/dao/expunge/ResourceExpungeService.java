package ca.uhn.fhir.jpa.dao.expunge;

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

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTagDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboStringUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedComboTokensNonUniqueDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamCoordsDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamDateDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamNumberDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamQuantityDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamQuantityNormalizedDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamStringDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceProvenanceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.data.ISearchParamPresentDao;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ResourceExpungeService implements IResourceExpungeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceExpungeService.class);

	@Autowired
	private IForcedIdDao myForcedIdDao;
	@Autowired
	private IResourceTableDao myResourceTableDao;
	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	private IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;
	@Autowired
	private IResourceIndexedSearchParamStringDao myResourceIndexedSearchParamStringDao;
	@Autowired
	private IResourceIndexedSearchParamTokenDao myResourceIndexedSearchParamTokenDao;
	@Autowired
	private IResourceIndexedSearchParamDateDao myResourceIndexedSearchParamDateDao;
	@Autowired
	private IResourceIndexedSearchParamQuantityDao myResourceIndexedSearchParamQuantityDao;
	@Autowired
	private IResourceIndexedSearchParamQuantityNormalizedDao myResourceIndexedSearchParamQuantityNormalizedDao;
	@Autowired
	private IResourceIndexedSearchParamCoordsDao myResourceIndexedSearchParamCoordsDao;
	@Autowired
	private IResourceIndexedSearchParamNumberDao myResourceIndexedSearchParamNumberDao;
	@Autowired
	private IResourceIndexedComboStringUniqueDao myResourceIndexedCompositeStringUniqueDao;
	@Autowired
	private IResourceIndexedComboTokensNonUniqueDao myResourceIndexedComboTokensNonUniqueDao;
	@Autowired
	private IResourceLinkDao myResourceLinkDao;
	@Autowired
	private IResourceTagDao myResourceTagDao;
	@Autowired
	private IIdHelperService myIdHelperService;
	@Autowired
	private IResourceHistoryTagDao myResourceHistoryTagDao;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IResourceProvenanceDao myResourceHistoryProvenanceTableDao;
	@Autowired
	private ISearchParamPresentDao mySearchParamPresentDao;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@Override
	@Transactional
	public List<ResourcePersistentId> findHistoricalVersionsOfNonDeletedResources(String theResourceName, ResourcePersistentId theResourceId, int theRemainingCount) {
		Pageable page = PageRequest.of(0, theRemainingCount);

		Slice<Long> ids;
		if (theResourceId != null && theResourceId.getId() != null) {
			if (theResourceId.getVersion() != null) {
				ids = toSlice(myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theResourceId.getIdAsLong(), theResourceId.getVersion()));
			} else {
				ids = myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResourceId(page, theResourceId.getIdAsLong());
			}
		} else {
			if (theResourceName != null) {
				ids = myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResources(page, theResourceName);
			} else {
				ids = myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResources(page);
			}
		}

		return ResourcePersistentId.fromLongList(ids.getContent());
	}

	@Override
	@Transactional
	public List<ResourcePersistentId> findHistoricalVersionsOfDeletedResources(String theResourceName, ResourcePersistentId theResourceId, int theRemainingCount) {
		Pageable page = PageRequest.of(0, theRemainingCount);
		Slice<Long> ids;
		if (theResourceId != null) {
			ids = myResourceTableDao.findIdsOfDeletedResourcesOfType(page, theResourceId.getIdAsLong(), theResourceName);
			ourLog.info("Expunging {} deleted resources of type[{}] and ID[{}]", ids.getNumberOfElements(), theResourceName, theResourceId);
		} else {
			if (theResourceName != null) {
				ids = myResourceTableDao.findIdsOfDeletedResourcesOfType(page, theResourceName);
				ourLog.info("Expunging {} deleted resources of type[{}]", ids.getNumberOfElements(), theResourceName);
			} else {
				ids = myResourceTableDao.findIdsOfDeletedResources(page);
				ourLog.info("Expunging {} deleted resources (all types)", ids.getNumberOfElements());
			}
		}
		return ResourcePersistentId.fromLongList(ids.getContent());
	}

	@Override
	@Transactional
	public void expungeCurrentVersionOfResources(RequestDetails theRequestDetails, List<ResourcePersistentId> theResourceIds, AtomicInteger theRemainingCount) {
		for (ResourcePersistentId next : theResourceIds) {
			expungeCurrentVersionOfResource(theRequestDetails, next.getIdAsLong(), theRemainingCount);
			if (theRemainingCount.get() <= 0) {
				return;
			}
		}

		/*
		 * Once this transaction is committed, we will invalidate all memory caches
		 * in order to avoid any caches having references to things that no longer
		 * exist. This is a pretty brute-force way of addressing this, and could probably
		 * be optimized, but expunge is hopefully not frequently called on busy servers
		 * so it shouldn't be too big a deal.
		 */
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization(){
			@Override
			public void afterCommit() {
				myMemoryCacheService.invalidateAllCaches();
			}
		});
	}

	private void expungeHistoricalVersion(RequestDetails theRequestDetails, Long theNextVersionId, AtomicInteger theRemainingCount) {
		ResourceHistoryTable version = myResourceHistoryTableDao.findById(theNextVersionId).orElseThrow(IllegalArgumentException::new);
		IdDt id = version.getIdDt();
		ourLog.info("Deleting resource version {}", id.getValue());

		callHooks(theRequestDetails, theRemainingCount, version, id);

		if (version.getProvenance() != null) {
			myResourceHistoryProvenanceTableDao.deleteByPid(version.getProvenance().getId());
		}

		myResourceHistoryTagDao.deleteByPid(version.getId());
		myResourceHistoryTableDao.deleteByPid(version.getId());

		theRemainingCount.decrementAndGet();
	}

	private void callHooks(RequestDetails theRequestDetails, AtomicInteger theRemainingCount, ResourceHistoryTable theVersion, IdDt theId) {
		final AtomicInteger counter = new AtomicInteger();
		if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE, myInterceptorBroadcaster, theRequestDetails)) {
			IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(theId.getResourceType());
			IBaseResource resource = resourceDao.toResource(theVersion, false);
			HookParams params = new HookParams()
				.add(AtomicInteger.class, counter)
				.add(IIdType.class, theId)
				.add(IBaseResource.class, resource)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE, params);
		}
		theRemainingCount.addAndGet(-1 * counter.get());
	}

	@Override
	@Transactional
	public void expungeHistoricalVersionsOfIds(RequestDetails theRequestDetails, List<ResourcePersistentId> theResourceIds, AtomicInteger theRemainingCount) {
		for (ResourcePersistentId next : theResourceIds) {
			expungeHistoricalVersionsOfId(theRequestDetails, next.getIdAsLong(), theRemainingCount);
			if (theRemainingCount.get() <= 0) {
				return;
			}
		}
	}

	@Override
	@Transactional
	public void expungeHistoricalVersions(RequestDetails theRequestDetails, List<ResourcePersistentId> theHistoricalIds, AtomicInteger theRemainingCount) {
		for (ResourcePersistentId next : theHistoricalIds) {
			expungeHistoricalVersion(theRequestDetails, next.getIdAsLong(), theRemainingCount);
			if (theRemainingCount.get() <= 0) {
				return;
			}
		}
	}

	private void expungeCurrentVersionOfResource(RequestDetails theRequestDetails, Long theResourceId, AtomicInteger theRemainingCount) {
		ResourceTable resource = myResourceTableDao.findById(theResourceId).orElseThrow(IllegalStateException::new);

		ResourceHistoryTable currentVersion = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(resource.getId(), resource.getVersion());
		if (currentVersion != null) {
			expungeHistoricalVersion(theRequestDetails, currentVersion.getId(), theRemainingCount);
		}

		ourLog.info("Expunging current version of resource {}", resource.getIdDt().getValue());

		deleteAllSearchParams(new ResourcePersistentId(resource.getResourceId()));
		resource.getTags().clear();

		if (resource.getForcedId() != null) {
			ForcedId forcedId = resource.getForcedId();
			resource.setForcedId(null);
			myResourceTableDao.saveAndFlush(resource);
			myForcedIdDao.deleteByPid(forcedId.getId());
		}

		myResourceTableDao.deleteByPid(resource.getId());
	}

	@Override
	@Transactional
	public void deleteAllSearchParams(ResourcePersistentId theResourceId) {
		ResourceTable resource = myResourceTableDao.findById(theResourceId.getIdAsLong()).orElse(null);

		if (resource == null || resource.isParamsUriPopulated()) {
			myResourceIndexedSearchParamUriDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isParamsCoordsPopulated()) {
			myResourceIndexedSearchParamCoordsDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isParamsDatePopulated()) {
			myResourceIndexedSearchParamDateDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isParamsNumberPopulated()) {
			myResourceIndexedSearchParamNumberDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isParamsQuantityPopulated()) {
			myResourceIndexedSearchParamQuantityDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isParamsQuantityNormalizedPopulated()) {
			myResourceIndexedSearchParamQuantityNormalizedDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isParamsStringPopulated()) {
			myResourceIndexedSearchParamStringDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isParamsTokenPopulated()) {
			myResourceIndexedSearchParamTokenDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isParamsComboStringUniquePresent()) {
			myResourceIndexedCompositeStringUniqueDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isParamsComboTokensNonUniquePresent()) {
			myResourceIndexedComboTokensNonUniqueDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.ENABLED) {
			mySearchParamPresentDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
		if (resource == null || resource.isHasLinks()) {
			myResourceLinkDao.deleteByResourceId(theResourceId.getIdAsLong());
		}

		if (resource == null || resource.isHasTags()) {
			myResourceTagDao.deleteByResourceId(theResourceId.getIdAsLong());
		}
	}

	private void expungeHistoricalVersionsOfId(RequestDetails theRequestDetails, Long myResourceId, AtomicInteger theRemainingCount) {
		ResourceTable resource = myResourceTableDao.findById(myResourceId).orElseThrow(IllegalArgumentException::new);

		Pageable page = PageRequest.of(0, theRemainingCount.get());

		Slice<Long> versionIds = myResourceHistoryTableDao.findForResourceId(page, resource.getId(), resource.getVersion());
		ourLog.debug("Found {} versions of resource {} to expunge", versionIds.getNumberOfElements(), resource.getIdDt().getValue());
		for (Long nextVersionId : versionIds) {
			expungeHistoricalVersion(theRequestDetails, nextVersionId, theRemainingCount);
			if (theRemainingCount.get() <= 0) {
				return;
			}
		}
	}

	private Slice<Long> toSlice(ResourceHistoryTable myVersion) {
		Validate.notNull(myVersion);
		return new SliceImpl<>(Collections.singletonList(myVersion.getId()));
	}
}
