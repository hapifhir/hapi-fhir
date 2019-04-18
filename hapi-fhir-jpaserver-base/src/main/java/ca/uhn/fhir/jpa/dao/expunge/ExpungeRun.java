package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
public class ExpungeRun implements Callable<ExpungeOutcome> {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeService.class);

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

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


	private final String myResourceName;
	private final Long myResourceId;
	private final Long myVersion;
	private final ExpungeOptions myExpungeOptions;
	private final AtomicInteger myRemainingCount;
	private TransactionTemplate myTxTemplate;

	public ExpungeRun(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions) {
		myResourceName = theResourceName;
		myResourceId = theResourceId;
		myVersion = theVersion;
		myExpungeOptions = theExpungeOptions;
		myRemainingCount = new AtomicInteger(myExpungeOptions.getLimit());
	}

	@PostConstruct
	private void setTxTemplate() {
		myTxTemplate = new TransactionTemplate(myPlatformTransactionManager);
		myTxTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
	}

	@Override
	public ExpungeOutcome call() {

		if (myExpungeOptions.isExpungeDeletedResources() && myVersion == null) {

			expungeDeletedResources();
			if (expungeLimitReached()) {
				return expungeOutcome();
			}
		}

		if (myExpungeOptions.isExpungeOldVersions()) {

			expungeOldVersions();
			if (expungeLimitReached()) {
				return expungeOutcome();
			}
		}

		return expungeOutcome();
	}

	private void expungeDeletedResources() {
		Slice<Long> resourceIds = deleteHistoricalVersionsOfDeletedResources();

		deleteSearchResultCacheEntries(resourceIds);

		deleteHistoricalVersions(resourceIds);
		if (expungeLimitReached()) {
			return;
		}

		deleteCurrentVersionsOfDeletedResources(resourceIds);
	}

	private boolean expungeLimitReached() {
		boolean expungeLimitReached = myRemainingCount.get() <= 0;
		if (expungeLimitReached) {
			ourLog.debug("Expunge limit has been hit - Stopping operation");
		}
		return expungeLimitReached;
	}

	private void expungeOldVersions() {
		Slice<Long> historicalIds = deleteHistoricalVersionsOfNonDeletedResources();

		myTxTemplate.execute(t -> {
			expungeHistoricalVersions(historicalIds);
			return null;
		});
	}

	private void expungeHistoricalVersions(Slice<Long> theHistoricalIds) {
		for (Long next : theHistoricalIds) {
			expungeHistoricalVersion(next);
			if (myRemainingCount.decrementAndGet() <= 0) {
				return;
			}
		}
	}

	private Slice<Long> deleteHistoricalVersionsOfNonDeletedResources() {
		Pageable page = PageRequest.of(0, myRemainingCount.get());
		return myTxTemplate.execute(t -> {
			if (myResourceId != null) {
				if (myVersion != null) {
					return toSlice(myResourceHistoryTableDao.findForIdAndVersion(myResourceId, myVersion));
				} else {
					return myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResourceId(page, myResourceId);
				}
			} else {
				if (myResourceName != null) {
					return myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResources(page, myResourceName);
				} else {
					return myResourceHistoryTableDao.findIdsOfPreviousVersionsOfResources(page);
				}
			}
		});
	}

	private void deleteCurrentVersionsOfDeletedResources(Slice<Long> theResourceIds) {
		myTxTemplate.execute(t -> {
			expungeCurrentVersionOfResources(theResourceIds);
			return null;
		});
	}

	private void expungeCurrentVersionOfResources(Slice<Long> theResourceIds) {
		for (Long next : theResourceIds) {
			expungeCurrentVersionOfResource(next);
			if (myRemainingCount.get() <= 0) {
				return;
			}
		}
	}

	private void deleteHistoricalVersions(Slice<Long> theResourceIds) {
		myTxTemplate.execute(t -> {
			expungeHistoricalVersionsOfIds(theResourceIds);
			return null;
		});
	}

	private void expungeHistoricalVersionsOfIds(Slice<Long> theResourceIds) {
		for (Long next : theResourceIds) {
			expungeHistoricalVersionsOfId(next);
			if (myRemainingCount.get() <= 0) {
				return;
			}
		}
	}

	/*
	 * Delete any search result cache entries pointing to the given resource. We do
	 * this in batches to avoid sending giant batches of parameters to the DB
	 */
	private void deleteSearchResultCacheEntries(Slice<Long> theResourceIds) {
		List<List<Long>> partitions = Lists.partition(theResourceIds.getContent(), 800);
		myTxTemplate.execute(t -> {
			deleteByResourceIdPartitions(partitions);
			return null;
		});
	}

	private void deleteByResourceIdPartitions(List<List<Long>> thePartitions) {
		for (List<Long> nextPartition : thePartitions) {
			ourLog.info("Expunging any search results pointing to {} resources", nextPartition.size());
			mySearchResultDao.deleteByResourceIds(nextPartition);
		}
	}

	private Slice<Long> deleteHistoricalVersionsOfDeletedResources() {
		Pageable page = PageRequest.of(0, myRemainingCount.get());
		return myTxTemplate.execute(t -> {
			if (myResourceId != null) {
				Slice<Long> ids = myResourceTableDao.findIdsOfDeletedResourcesOfType(page, myResourceId, myResourceName);
				ourLog.info("Expunging {} deleted resources of type[{}] and ID[{}]", ids.getNumberOfElements(), myResourceName, myResourceId);
				return ids;
			} else {
				if (myResourceName != null) {
					Slice<Long> ids = myResourceTableDao.findIdsOfDeletedResourcesOfType(page, myResourceName);
					ourLog.info("Expunging {} deleted resources of type[{}]", ids.getNumberOfElements(), myResourceName);
					return ids;
				} else {
					Slice<Long> ids = myResourceTableDao.findIdsOfDeletedResources(page);
					ourLog.info("Expunging {} deleted resources (all types)", ids.getNumberOfElements());
					return ids;
				}
			}
		});
	}




	private void expungeCurrentVersionOfResource(Long myResourceId) {
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

		myRemainingCount.decrementAndGet();
	}

	private void expungeHistoricalVersion(Long theNextVersionId) {
		ResourceHistoryTable version = myResourceHistoryTableDao.findById(theNextVersionId).orElseThrow(IllegalArgumentException::new);
		ourLog.info("Deleting resource version {}", version.getIdDt().getValue());

		myResourceHistoryTagDao.deleteAll(version.getTags());
		myResourceHistoryTableDao.delete(version);
	}

	private void expungeHistoricalVersionsOfId(Long myResourceId) {
		ResourceTable resource = myResourceTableDao.findById(myResourceId).orElseThrow(IllegalArgumentException::new);

		Pageable page = PageRequest.of(0, myRemainingCount.get());

		Slice<Long> versionIds = myResourceHistoryTableDao.findForResourceId(page, resource.getId(), resource.getVersion());
		ourLog.debug("Found {} versions of resource {} to expunge", versionIds.getNumberOfElements(), resource.getIdDt().getValue());
		for (Long nextVersionId : versionIds) {
			expungeHistoricalVersion(nextVersionId);
			if (myRemainingCount.decrementAndGet() <= 0) {
				return;
			}
		}
	}

	private ExpungeOutcome expungeOutcome() {
		return new ExpungeOutcome()
			.setDeletedCount(myExpungeOptions.getLimit() - myRemainingCount.get());
	}

	private Slice<Long> toSlice(ResourceHistoryTable myVersion) {
		Validate.notNull(myVersion);
		return new SliceImpl<>(Collections.singletonList(myVersion.getId()));
	}
}
