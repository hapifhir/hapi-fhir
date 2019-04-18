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
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
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

	private final String myResourceName;
	private final Long myResourceId;
	private final Long myVersion;
	private final ExpungeOptions myExpungeOptions;

	public ExpungeRun(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions) {
		myResourceName = theResourceName;
		myResourceId = theResourceId;
		myVersion = theVersion;
		myExpungeOptions = theExpungeOptions;
	}

	@Override
	public ExpungeOutcome call() {

		TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		ourLog.info("Expunge: ResourceName[{}] Id[{}] Version[{}] Options[{}]", myResourceName, myResourceId, myVersion, myExpungeOptions);

		if (!myConfig.isExpungeEnabled()) {
			throw new MethodNotAllowedException("$expunge is not enabled on this server");
		}

		if (myExpungeOptions.getLimit() < 1) {
			throw new InvalidRequestException("Expunge limit may not be less than 1.  Received expunge limit "+myExpungeOptions.getLimit() + ".");
		}

		AtomicInteger remainingCount = new AtomicInteger(myExpungeOptions.getLimit());

		if (myResourceName == null && myResourceId == null && myVersion == null) {
			if (myExpungeOptions.isExpungeEverything()) {
				doExpungeEverything();
			}
		}

		if (myExpungeOptions.isExpungeDeletedResources() && myVersion == null) {

			/*
			 * Delete historical versions of deleted resources
			 */
			Pageable page = PageRequest.of(0, remainingCount.get());
			Slice<Long> resourceIds = txTemplate.execute(t -> {
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
						ourLog.info("Expunging {} deleted resources (all types)", ids.getNumberOfElements(), myResourceName);
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
					return toExpungeOutcome(myExpungeOptions, remainingCount);
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
					return toExpungeOutcome(myExpungeOptions, remainingCount);
				}
			}

		}

		if (myExpungeOptions.isExpungeOldVersions()) {

			/*
			 * Delete historical versions of non-deleted resources
			 */
			Pageable page = PageRequest.of(0, remainingCount.get());
			Slice<Long> historicalIds = txTemplate.execute(t -> {
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

			for (Long next : historicalIds) {
				txTemplate.execute(t -> {
					expungeHistoricalVersion(next);
					if (remainingCount.decrementAndGet() <= 0) {
						return toExpungeOutcome(myExpungeOptions, remainingCount);
					}
					return null;
				});
			}

		}
		return toExpungeOutcome(myExpungeOptions, remainingCount);
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

	protected void expungeHistoricalVersion(Long theNextVersionId) {
		ResourceHistoryTable version = myResourceHistoryTableDao.findById(theNextVersionId).orElseThrow(IllegalArgumentException::new);
		ourLog.info("Deleting resource version {}", version.getIdDt().getValue());

		myResourceHistoryTagDao.deleteAll(version.getTags());
		myResourceHistoryTableDao.delete(version);
	}

	protected void expungeHistoricalVersionsOfId(Long myResourceId, AtomicInteger theRemainingCount) {
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

	private ExpungeOutcome toExpungeOutcome(ExpungeOptions myExpungeOptions, AtomicInteger theRemainingCount) {
		return new ExpungeOutcome()
			.setDeletedCount(myExpungeOptions.getLimit() - theRemainingCount.get());
	}

	private Slice<Long> toSlice(ResourceHistoryTable myVersion) {
		Validate.notNull(myVersion);
		return new SliceImpl<>(Collections.singletonList(myVersion.getId()));
	}
}
