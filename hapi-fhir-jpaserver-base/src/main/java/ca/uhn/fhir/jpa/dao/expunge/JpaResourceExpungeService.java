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
package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryProvenanceDao;
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
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.data.ISearchParamPresentDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class JpaResourceExpungeService implements IResourceExpungeService<JpaPid> {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaResourceExpungeService.class);

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
	private IResourceHistoryProvenanceDao myResourceHistoryProvenanceTableDao;

	@Autowired
	private ISearchParamPresentDao mySearchParamPresentDao;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@Autowired
	private IJpaStorageResourceParser myJpaStorageResourceParser;

	@Override
	@Transactional
	public List<JpaPid> findHistoricalVersionsOfNonDeletedResources(
			String theResourceName, JpaPid theJpaPid, int theRemainingCount) {
		if (isEmptyQuery(theRemainingCount)) {
			return Collections.EMPTY_LIST;
		}

		Pageable page = PageRequest.of(0, theRemainingCount);

		Slice<Long> ids;
		if (theJpaPid != null && theJpaPid.getId() != null) {
			if (theJpaPid.getVersion() != null) {
				ids = toSlice(myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(
						theJpaPid.getId(), theJpaPid.getVersion()));
			} else {
				ids = myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResourceId(page, theJpaPid.getId());
			}
		} else {
			if (theResourceName != null) {
				ids = myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResources(page, theResourceName);
			} else {
				ids = myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResources(page);
			}
		}

		return JpaPid.fromLongList(ids.getContent());
	}

	@Override
	@Transactional
	public List<JpaPid> findHistoricalVersionsOfDeletedResources(
			String theResourceName, JpaPid theResourceId, int theRemainingCount) {
		if (isEmptyQuery(theRemainingCount)) {
			return Collections.EMPTY_LIST;
		}

		Pageable page = PageRequest.of(0, theRemainingCount);
		Slice<Long> ids;
		if (theResourceId != null) {
			ids = myResourceTableDao.findIdsOfDeletedResourcesOfType(page, theResourceId.getId(), theResourceName);
			ourLog.info(
					"Expunging {} deleted resources of type[{}] and ID[{}]",
					ids.getNumberOfElements(),
					theResourceName,
					theResourceId);
		} else {
			if (theResourceName != null) {
				ids = myResourceTableDao.findIdsOfDeletedResourcesOfType(page, theResourceName);
				ourLog.info("Expunging {} deleted resources of type[{}]", ids.getNumberOfElements(), theResourceName);
			} else {
				ids = myResourceTableDao.findIdsOfDeletedResources(page);
				ourLog.info("Expunging {} deleted resources (all types)", ids.getNumberOfElements());
			}
		}
		return JpaPid.fromLongList(ids.getContent());
	}

	@Override
	@Transactional
	public void expungeCurrentVersionOfResources(
			RequestDetails theRequestDetails, List<JpaPid> theResourceIds, AtomicInteger theRemainingCount) {
		for (JpaPid next : theResourceIds) {
			expungeCurrentVersionOfResource(theRequestDetails, (next).getId(), theRemainingCount);
			if (expungeLimitReached(theRemainingCount)) {
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
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				myMemoryCacheService.invalidateAllCaches();
			}
		});
	}

	private void expungeHistoricalVersion(
			RequestDetails theRequestDetails, Long theNextVersionId, AtomicInteger theRemainingCount) {
		ResourceHistoryTable version =
				myResourceHistoryTableDao.findById(theNextVersionId).orElseThrow(IllegalArgumentException::new);
		IdDt id = version.getIdDt();
		ourLog.info("Deleting resource version {}", id.getValue());

		callHooks(theRequestDetails, theRemainingCount, version, id);

		if (version.getProvenance() != null) {
			myResourceHistoryProvenanceTableDao.deleteByPid(
					version.getProvenance().getId());
		}

		myResourceHistoryTagDao.deleteByPid(version.getId());
		myResourceHistoryTableDao.deleteByPid(version.getId());

		theRemainingCount.decrementAndGet();
	}

	private void callHooks(
			RequestDetails theRequestDetails,
			AtomicInteger theRemainingCount,
			ResourceHistoryTable theVersion,
			IdDt theId) {
		final AtomicInteger counter = new AtomicInteger();
		if (CompositeInterceptorBroadcaster.hasHooks(
				Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE, myInterceptorBroadcaster, theRequestDetails)) {
			IBaseResource resource = myJpaStorageResourceParser.toResource(theVersion, false);
			HookParams params = new HookParams()
					.add(AtomicInteger.class, counter)
					.add(IIdType.class, theId)
					.add(IBaseResource.class, resource)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			CompositeInterceptorBroadcaster.doCallHooks(
					myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE, params);
		}
		theRemainingCount.addAndGet(-1 * counter.get());
	}

	@Override
	@Transactional
	public void expungeHistoricalVersionsOfIds(
			RequestDetails theRequestDetails, List<JpaPid> theResourceIds, AtomicInteger theRemainingCount) {
		List<Long> pids = JpaPid.toLongList(theResourceIds);

		List<ResourceTable> resourcesToDelete = myResourceTableDao.findAllByIdAndLoadForcedIds(pids);
		for (ResourceTable next : resourcesToDelete) {
			expungeHistoricalVersionsOfId(theRequestDetails, next, theRemainingCount);
			if (expungeLimitReached(theRemainingCount)) {
				return;
			}
		}
	}

	@Override
	@Transactional
	public void expungeHistoricalVersions(
			RequestDetails theRequestDetails, List<JpaPid> theHistoricalIds, AtomicInteger theRemainingCount) {
		for (JpaPid next : theHistoricalIds) {
			expungeHistoricalVersion(theRequestDetails, (next).getId(), theRemainingCount);
			if (expungeLimitReached(theRemainingCount)) {
				return;
			}
		}
	}

	protected void expungeCurrentVersionOfResource(
			RequestDetails theRequestDetails, Long theResourceId, AtomicInteger theRemainingCount) {
		ResourceTable resource = myResourceTableDao.findById(theResourceId).orElseThrow(IllegalStateException::new);

		ResourceHistoryTable currentVersion = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(
				resource.getId(), resource.getVersion());
		if (currentVersion != null) {
			expungeHistoricalVersion(theRequestDetails, currentVersion.getId(), theRemainingCount);
		}

		ourLog.info(
				"Expunging current version of resource {}", resource.getIdDt().getValue());

		try {
			if (resource.isHasTags()) {
				myResourceTagDao.deleteByResourceId(resource.getId());
			}

			myResourceTableDao.deleteByPid(resource.getId());
		} catch (DataIntegrityViolationException e) {
			throw new PreconditionFailedException(Msg.code(2415)
					+ "The resource could not be expunged. It is likely due to unfinished asynchronous deletions, please try again later: "
					+ e);
		}
	}

	@Override
	@Transactional
	public void deleteAllSearchParams(JpaPid theResourceId) {
		Long theResourceLongId = theResourceId.getId();
		ResourceTable resource = myResourceTableDao.findById(theResourceLongId).orElse(null);

		if (resource == null || resource.isParamsUriPopulated()) {
			myResourceIndexedSearchParamUriDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isParamsCoordsPopulated()) {
			myResourceIndexedSearchParamCoordsDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isParamsDatePopulated()) {
			myResourceIndexedSearchParamDateDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isParamsNumberPopulated()) {
			myResourceIndexedSearchParamNumberDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isParamsQuantityPopulated()) {
			myResourceIndexedSearchParamQuantityDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isParamsQuantityNormalizedPopulated()) {
			myResourceIndexedSearchParamQuantityNormalizedDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isParamsStringPopulated()) {
			myResourceIndexedSearchParamStringDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isParamsTokenPopulated()) {
			myResourceIndexedSearchParamTokenDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isParamsComboStringUniquePresent()) {
			myResourceIndexedCompositeStringUniqueDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isParamsComboTokensNonUniquePresent()) {
			myResourceIndexedComboTokensNonUniqueDao.deleteByResourceId(theResourceLongId);
		}
		if (myStorageSettings.getIndexMissingFields() == JpaStorageSettings.IndexEnabledEnum.ENABLED) {
			mySearchParamPresentDao.deleteByResourceId(theResourceLongId);
		}
		if (resource == null || resource.isHasLinks()) {
			myResourceLinkDao.deleteByResourceId(theResourceLongId);
		}
	}

	private void expungeHistoricalVersionsOfId(
			RequestDetails theRequestDetails, ResourceTable theResource, AtomicInteger theRemainingCount) {
		Pageable page;
		synchronized (theRemainingCount) {
			if (expungeLimitReached(theRemainingCount)) {
				return;
			}
			page = PageRequest.of(0, theRemainingCount.get());
		}

		Slice<Long> versionIds =
				myResourceHistoryTableDao.findForResourceId(page, theResource.getId(), theResource.getVersion());
		ourLog.debug(
				"Found {} versions of resource {} to expunge",
				versionIds.getNumberOfElements(),
				theResource.getIdDt().getValue());
		for (Long nextVersionId : versionIds) {
			expungeHistoricalVersion(theRequestDetails, nextVersionId, theRemainingCount);
			if (expungeLimitReached(theRemainingCount)) {
				return;
			}
		}
	}

	private Slice<Long> toSlice(ResourceHistoryTable myVersion) {
		Validate.notNull(myVersion);
		return new SliceImpl<>(Collections.singletonList(myVersion.getId()));
	}

	private boolean isEmptyQuery(int theCount) {
		return theCount <= 0;
	}

	private boolean expungeLimitReached(AtomicInteger theRemainingCount) {
		return theRemainingCount.get() <= 0;
	}
}
