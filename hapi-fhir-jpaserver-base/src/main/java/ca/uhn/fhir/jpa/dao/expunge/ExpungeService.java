package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExpungeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeService.class);
	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	private DaoConfig myConfig;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected ISearchResultDao mySearchResultDao;
	@Autowired
	protected IResourceHistoryTableDao myResourceHistoryTableDao;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	protected IResourceIndexedSearchParamUriDao myResourceIndexedSearchParamUriDao;
	@Autowired
	protected IResourceIndexedSearchParamStringDao myResourceIndexedSearchParamStringDao;
	@Autowired
	protected IResourceIndexedSearchParamTokenDao myResourceIndexedSearchParamTokenDao;
	@Autowired
	protected IResourceIndexedSearchParamDateDao myResourceIndexedSearchParamDateDao;
	@Autowired
	protected IResourceIndexedSearchParamQuantityDao myResourceIndexedSearchParamQuantityDao;
	@Autowired
	protected IResourceIndexedSearchParamCoordsDao myResourceIndexedSearchParamCoordsDao;
	@Autowired
	protected IResourceIndexedSearchParamNumberDao myResourceIndexedSearchParamNumberDao;
	@Autowired
	protected IResourceLinkDao myResourceLinkDao;
	@Autowired
	protected IResourceTagDao myResourceTagDao;
	@Autowired
	protected IdHelperService myIdHelperService;
	@Autowired
	protected IResourceHistoryTagDao myResourceHistoryTagDao;

	public ExpungeOutcome expunge(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions) {

		TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		ourLog.info("Expunge: ResourceName[{}] Id[{}] Version[{}] Options[{}]", theResourceName, theResourceId, theVersion, theExpungeOptions);

		if (!myConfig.isExpungeEnabled()) {
			throw new MethodNotAllowedException("$expunge is not enabled on this server");
		}

		if (theExpungeOptions.getLimit() < 1) {
			throw new InvalidRequestException("Expunge limit may not be less than 1.  Received expunge limit "+theExpungeOptions.getLimit() + ".");
		}

		AtomicInteger remainingCount = new AtomicInteger(theExpungeOptions.getLimit());

		if (theResourceName == null && theResourceId == null && theVersion == null) {
			if (theExpungeOptions.isExpungeEverything()) {
				doExpungeEverything();
			}
		}

		if (theExpungeOptions.isExpungeDeletedResources() && theVersion == null) {

			/*
			 * Delete historical versions of deleted resources
			 */
			Pageable page = PageRequest.of(0, remainingCount.get());
			Slice<Long> resourceIds = txTemplate.execute(t -> {
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
						ourLog.info("Expunging {} deleted resources (all types)", ids.getNumberOfElements(), theResourceName);
						return ids;
					}
				}
			});

			/*
			 * Delete any search result cache entries pointing to the given resource. We do
			 * this in batches to avoid sending giant batches of parameters to the DB
			 */
			List<List<Long>> partitions = Lists.partition(resourceIds.getContent(), 800);
			for (List<Long> nextPartition : partitions) {
				ourLog.info("Expunging any search results pointing to {} resources", nextPartition.size());
				txTemplate.execute(t -> {
					mySearchResultDao.deleteByResourceIds(nextPartition);
					return null;
				});
			}

			/*
			 * Delete historical versions
			 */
			for (Long next : resourceIds) {
				txTemplate.execute(t -> {
					expungeHistoricalVersionsOfId(next, remainingCount);
					return null;
				});
				if (remainingCount.get() <= 0) {
					ourLog.debug("Expunge limit has been hit - Stopping operation");
					return toExpungeOutcome(theExpungeOptions, remainingCount);
				}
			}

			/*
			 * Delete current versions of deleted resources
			 */
			for (Long next : resourceIds) {
				txTemplate.execute(t -> {
					expungeCurrentVersionOfResource(next, remainingCount);
					return null;
				});
				if (remainingCount.get() <= 0) {
					ourLog.debug("Expunge limit has been hit - Stopping operation");
					return toExpungeOutcome(theExpungeOptions, remainingCount);
				}
			}

		}

		if (theExpungeOptions.isExpungeOldVersions()) {

			/*
			 * Delete historical versions of non-deleted resources
			 */
			Pageable page = PageRequest.of(0, remainingCount.get());
			Slice<Long> historicalIds = txTemplate.execute(t -> {
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
			});

			for (Long next : historicalIds) {
				txTemplate.execute(t -> {
					expungeHistoricalVersion(next);
					if (remainingCount.decrementAndGet() <= 0) {
						return toExpungeOutcome(theExpungeOptions, remainingCount);
					}
					return null;
				});
			}

		}
		return toExpungeOutcome(theExpungeOptions, remainingCount);
	}

	private void doExpungeEverything() {

		final AtomicInteger counter = new AtomicInteger();

		ourLog.info("BEGINNING GLOBAL $expunge");
		TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("UPDATE " + ResourceHistoryTable.class.getSimpleName() + " d SET d.myForcedId = null"));
			counter.addAndGet(doExpungeEverythingQuery("UPDATE " + ResourceTable.class.getSimpleName() + " d SET d.myForcedId = null"));
			counter.addAndGet(doExpungeEverythingQuery("UPDATE " + TermCodeSystem.class.getSimpleName() + " d SET d.myCurrentVersion = null"));
			return null;
		});
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + SearchParamPresent.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ForcedId.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamDate.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamNumber.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamQuantity.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamString.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamToken.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamUri.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamCoords.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedCompositeStringUnique.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceLink.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + SearchResult.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + SearchInclude.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptParentChildLink.class.getSimpleName() + " d"));
			return null;
		});
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptMapGroupElementTarget.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptMapGroupElement.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptMapGroup.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptMap.class.getSimpleName() + " d"));
			return null;
		});
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptProperty.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptDesignation.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConcept.class.getSimpleName() + " d"));
			for (TermCodeSystem next : myEntityManager.createQuery("SELECT c FROM " + TermCodeSystem.class.getName() + " c", TermCodeSystem.class).getResultList()) {
				next.setCurrentVersion(null);
				myEntityManager.merge(next);
			}
			return null;
		});
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermCodeSystemVersion.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermCodeSystem.class.getSimpleName() + " d"));
			return null;
		});
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + SubscriptionTable.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceHistoryTag.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceTag.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TagDefinition.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceHistoryTable.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceTable.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + org.hibernate.search.jpa.Search.class.getSimpleName() + " d"));
			return null;
		});

		ourLog.info("COMPLETED GLOBAL $expunge - Deleted {} rows", counter.get());
	}

	private int doExpungeEverythingQuery(String theQuery) {
		StopWatch sw = new StopWatch();
		int outcome = myEntityManager.createQuery(theQuery).executeUpdate();
		ourLog.debug("Query affected {} rows in {}: {}", outcome, sw.toString(), theQuery);
		return outcome;
	}

	private void expungeCurrentVersionOfResource(Long theResourceId, AtomicInteger theRemainingCount) {
		ResourceTable resource = myResourceTableDao.findById(theResourceId).orElseThrow(IllegalStateException::new);

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

	protected void expungeHistoricalVersion(Long theNextVersionId) {
		ResourceHistoryTable version = myResourceHistoryTableDao.findById(theNextVersionId).orElseThrow(IllegalArgumentException::new);
		ourLog.info("Deleting resource version {}", version.getIdDt().getValue());

		myResourceHistoryTagDao.deleteAll(version.getTags());
		myResourceHistoryTableDao.delete(version);
	}

	protected void expungeHistoricalVersionsOfId(Long theResourceId, AtomicInteger theRemainingCount) {
		ResourceTable resource = myResourceTableDao.findById(theResourceId).orElseThrow(IllegalArgumentException::new);

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

	private ExpungeOutcome toExpungeOutcome(ExpungeOptions theExpungeOptions, AtomicInteger theRemainingCount) {
		return new ExpungeOutcome()
			.setDeletedCount(theExpungeOptions.getLimit() - theRemainingCount.get());
	}

	private Slice<Long> toSlice(ResourceHistoryTable theVersion) {
		Validate.notNull(theVersion);
		return new SliceImpl<>(Collections.singletonList(theVersion.getId()));
	}
}
