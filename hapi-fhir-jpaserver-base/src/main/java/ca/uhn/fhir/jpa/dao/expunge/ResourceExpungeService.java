package ca.uhn.fhir.jpa.dao.expunge;

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

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
class ResourceExpungeService implements IResourceExpungeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceExpungeService.class);

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
	private IResourceIndexedSearchParamCoordsDao myResourceIndexedSearchParamCoordsDao;
	@Autowired
	private IResourceIndexedSearchParamNumberDao myResourceIndexedSearchParamNumberDao;
	@Autowired
	private IResourceLinkDao myResourceLinkDao;
	@Autowired
	private IResourceTagDao myResourceTagDao;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private IResourceHistoryTagDao myResourceHistoryTagDao;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IResourceProvenanceDao myResourceHistoryProvenanceTableDao;

	@Override
	@Transactional
	public Slice<Long> findHistoricalVersionsOfNonDeletedResources(String theResourceName, Long theResourceId, Long theVersion, int theRemainingCount) {
		Pageable page = PageRequest.of(0, theRemainingCount);
		if (theResourceId != null) {
			if (theVersion != null) {
				return toSlice(myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(theResourceId, theVersion));
			} else {
				return myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResourceId(page, theResourceId);
			}
		} else {
			if (theResourceName != null) {
				return myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResources(page, theResourceName);
			} else {
				return myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResources(page);
			}
		}
	}

	@Override
	@Transactional
	public Slice<Long> findHistoricalVersionsOfDeletedResources(String theResourceName, Long theResourceId, int theRemainingCount) {
		Pageable page = PageRequest.of(0, theRemainingCount);
		if (theResourceId != null) {
			Slice<Long> ids = myResourceTableDao.findIdsOfDeletedResourcesOfType(page, theResourceId, theResourceName);
			ourLog.info("Expunging {} deleted resources of type[{}] and ID[{}]", ids.getNumberOfElements(), theResourceName, theResourceId);
			return ids;
		} else {
			if (theResourceName != null) {
				Slice<Long> ids = myResourceTableDao.findIdsOfDeletedResourcesOfType(page, theResourceName);
				ourLog.info("Expunging {} deleted resources of type[{}]", ids.getNumberOfElements(), theResourceName);
				return ids;
			} else {
				Slice<Long> ids = myResourceTableDao.findIdsOfDeletedResources(page);
				ourLog.info("Expunging {} deleted resources (all types)", ids.getNumberOfElements());
				return ids;
			}
		}
	}

	@Override
	@Transactional
	public void expungeCurrentVersionOfResources(RequestDetails theRequestDetails, List<Long> theResourceIds, AtomicInteger theRemainingCount) {
		for (Long next : theResourceIds) {
			expungeCurrentVersionOfResource(theRequestDetails, next, theRemainingCount);
			if (theRemainingCount.get() <= 0) {
				return;
			}
		}
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
		if (JpaInterceptorBroadcaster.hasHooks(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE, myInterceptorBroadcaster, theRequestDetails)) {
			IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(theId.getResourceType());
			IBaseResource resource = resourceDao.toResource(theVersion, false);
			HookParams params = new HookParams()
				.add(AtomicInteger.class, counter)
				.add(IIdType.class, theId)
				.add(IBaseResource.class, resource)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PRESTORAGE_EXPUNGE_RESOURCE, params);
		}
		theRemainingCount.addAndGet(-1 * counter.get());
	}


	@Override
	@Transactional
	public void expungeHistoricalVersionsOfIds(RequestDetails theRequestDetails, List<Long> theResourceIds, AtomicInteger theRemainingCount) {
		for (Long next : theResourceIds) {
			expungeHistoricalVersionsOfId(theRequestDetails, next, theRemainingCount);
			if (theRemainingCount.get() <= 0) {
				return;
			}
		}
	}

	@Override
	@Transactional
	public void expungeHistoricalVersions(RequestDetails theRequestDetails, List<Long> theHistoricalIds, AtomicInteger theRemainingCount) {
		for (Long next : theHistoricalIds) {
			expungeHistoricalVersion(theRequestDetails, next, theRemainingCount);
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

		deleteAllSearchParams(resource.getResourceId());
		resource.getTags().clear();

		if (resource.getForcedId() != null) {
			ForcedId forcedId = resource.getForcedId();
			resource.setForcedId(null);
			myResourceTableDao.saveAndFlush(resource);
			myIdHelperService.delete(forcedId);
		}

		myResourceTableDao.deleteByPid(resource.getId());
	}


	@Autowired
	private ISearchParamPresentDao mySearchParamPresentDao;

	@Override
	@Transactional
	public void deleteAllSearchParams(Long theResourceId) {
		myResourceIndexedSearchParamUriDao.deleteByResourceId(theResourceId);
		myResourceIndexedSearchParamCoordsDao.deleteByResourceId(theResourceId);
		myResourceIndexedSearchParamDateDao.deleteByResourceId(theResourceId);
		myResourceIndexedSearchParamNumberDao.deleteByResourceId(theResourceId);
		myResourceIndexedSearchParamQuantityDao.deleteByResourceId(theResourceId);
		myResourceIndexedSearchParamStringDao.deleteByResourceId(theResourceId);
		myResourceIndexedSearchParamTokenDao.deleteByResourceId(theResourceId);
		mySearchParamPresentDao.deleteByResourceId(theResourceId);
		myResourceLinkDao.deleteByResourceId(theResourceId);

		myResourceTagDao.deleteByResourceId(theResourceId);
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
