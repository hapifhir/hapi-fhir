package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
class ExpungeDaoService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeDaoService.class);

	@Autowired
	private IResourceTableDao myResourceTableDao;
	@Autowired
	private ISearchResultDao mySearchResultDao;
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

	Slice<Long> findHistoricalVersionsOfNonDeletedResources(String theResourceName, Long theResourceId, Long theVersion, int theRemainingCount) {
		Pageable page = PageRequest.of(0, theRemainingCount);
		if (theResourceId != null) {
			if (theVersion != null) {
				return toSlice(myResourceHistoryTableDao.findForIdAndVersion(theResourceId, theVersion));
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

	Slice<Long> findHistoricalVersionsOfDeletedResources(String theResourceName, Long theResourceId, int theRemainingCount) {
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

	void expungeCurrentVersionOfResources(List<Long> theResourceIds, AtomicInteger theRemainingCount) {
		for (Long next : theResourceIds) {
			expungeCurrentVersionOfResource(next, theRemainingCount);
			if (theRemainingCount.get() <= 0) {
				return;
			}
		}
	}

	private void expungeHistoricalVersion(Long theNextVersionId) {
		ResourceHistoryTable version = myResourceHistoryTableDao.findById(theNextVersionId).orElseThrow(IllegalArgumentException::new);
		ourLog.info("Deleting resource version {}", version.getIdDt().getValue());

		myResourceHistoryTagDao.deleteAll(version.getTags());
		myResourceHistoryTableDao.delete(version);
	}

	void expungeHistoricalVersionsOfIds(List<Long> theResourceIds, AtomicInteger theRemainingCount) {
		for (Long next : theResourceIds) {
			expungeHistoricalVersionsOfId(next, theRemainingCount);
			if (theRemainingCount.get() <= 0) {
				return;
			}
		}
	}

	void expungeHistoricalVersions(List<Long> theHistoricalIds, AtomicInteger theRemainingCount) {
		for (Long next : theHistoricalIds) {
			expungeHistoricalVersion(next);
			if (theRemainingCount.decrementAndGet() <= 0) {
				return;
			}
		}
	}

	private void expungeCurrentVersionOfResource(Long myResourceId, AtomicInteger theRemainingCount) {
		ResourceTable resource = myResourceTableDao.findById(myResourceId).orElseThrow(IllegalStateException::new);

		ResourceHistoryTable currentVersion = myResourceHistoryTableDao.findForIdAndVersion(resource.getId(), resource.getVersion());
		if (currentVersion != null) {
			expungeHistoricalVersion(currentVersion.getId());
		}

		ourLog.info("Expunging current version of resource {}", resource.getIdDt().getValue());

		myResourceIndexedSearchParamUriDao.deleteAll(resource.getParamsUri());
		myResourceIndexedSearchParamCoordsDao.deleteAll(resource.getParamsCoords());
		myResourceIndexedSearchParamDateDao.deleteAll(resource.getParamsDate());
		myResourceIndexedSearchParamNumberDao.deleteAll(resource.getParamsNumber());
		myResourceIndexedSearchParamQuantityDao.deleteAll(resource.getParamsQuantity());
		myResourceIndexedSearchParamStringDao.deleteAll(resource.getParamsString());
		myResourceIndexedSearchParamTokenDao.deleteAll(resource.getParamsToken());
		myResourceLinkDao.deleteAll(resource.getResourceLinks());
		myResourceLinkDao.deleteAll(resource.getResourceLinksAsTarget());

		myResourceTagDao.deleteAll(resource.getTags());
		resource.getTags().clear();

		if (resource.getForcedId() != null) {
			ForcedId forcedId = resource.getForcedId();
			resource.setForcedId(null);
			myResourceTableDao.saveAndFlush(resource);
			myIdHelperService.delete(forcedId);
		}

		myResourceTableDao.delete(resource);

		theRemainingCount.decrementAndGet();
	}


	private void expungeHistoricalVersionsOfId(Long myResourceId, AtomicInteger theRemainingCount) {
		ResourceTable resource = myResourceTableDao.findById(myResourceId).orElseThrow(IllegalArgumentException::new);

		Pageable page = PageRequest.of(0, theRemainingCount.get());

		Slice<Long> versionIds = myResourceHistoryTableDao.findForResourceId(page, resource.getId(), resource.getVersion());
		ourLog.debug("Found {} versions of resource {} to expunge", versionIds.getNumberOfElements(), resource.getIdDt().getValue());
		for (Long nextVersionId : versionIds) {
			expungeHistoricalVersion(nextVersionId);
			if (theRemainingCount.decrementAndGet() <= 0) {
				return;
			}
		}
	}

	void deleteByResourceIdPartitions(List<Long> theResourceIds) {
		mySearchResultDao.deleteByResourceIds(theResourceIds);
	}

	private Slice<Long> toSlice(ResourceHistoryTable myVersion) {
		Validate.notNull(myVersion);
		return new SliceImpl<>(Collections.singletonList(myVersion.getId()));
	}
}
