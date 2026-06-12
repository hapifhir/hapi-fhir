/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * This service ensures uniqueness of resources during create or create-on-update
 * by storing the resource searchUrl.
 *
 * @see SearchUrlJobMaintenanceSvcImpl which deletes stale entities
 */
@Service
public class ResourceSearchUrlSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceSearchUrlSvc.class);

	/**
	 * Stale-entry deletion is paged so no single statement scans the whole table — that's what
	 * trips MSSQL's per-statement timeout on MegaScale (SMILE-12080). Capped under MSSQL's
	 * 2,100-parameter-per-statement limit so the {@code DELETE … WHERE RES_ID IN (?, …)} doesn't overflow.
	 */
	static final int DELETE_PAGE_SIZE = 1_800;

	/**
	 * Safety cap on page iterations per sweep (10,000 pages of 1,800 rows = 18M rows).
	 * ({@link SearchUrlJobMaintenanceSvcImpl}).
	 */
	static final int MAX_DELETE_PAGES = 10_000;

	private final EntityManager myEntityManager;

	private final IResourceSearchUrlDao myResourceSearchUrlDao;

	private final MatchUrlService myMatchUrlService;

	private final FhirContext myFhirContext;
	private final PartitionSettings myPartitionSettings;
	private final TransactionTemplate myPageTxTemplate;

	public ResourceSearchUrlSvc(
			EntityManager theEntityManager,
			IResourceSearchUrlDao theResourceSearchUrlDao,
			MatchUrlService theMatchUrlService,
			FhirContext theFhirContext,
			PartitionSettings thePartitionSettings,
			PlatformTransactionManager theTxManager) {
		myEntityManager = theEntityManager;
		myResourceSearchUrlDao = theResourceSearchUrlDao;
		myMatchUrlService = theMatchUrlService;
		myFhirContext = theFhirContext;
		myPartitionSettings = thePartitionSettings;
		myPageTxTemplate = new TransactionTemplate(theTxManager);
	}

	/**
	 * Deletes search URL entries created before {@code theCutoffDate}, in fixed-size pages that each
	 * commit in their own transaction. A single sweep is capped at {@link #MAX_DELETE_PAGES} pages;
	 * any remaining stale entries are removed by the next scheduled run
	 * ({@link SearchUrlJobMaintenanceSvcImpl}).
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void deleteEntriesOlderThan(Date theCutoffDate) {
		ourLog.debug("Deleting SearchUrls older than {}", theCutoffDate);

		long totalDeleted = 0;
		boolean moreStaleRemain = true;
		for (int pageIndex = 0; moreStaleRemain && pageIndex < MAX_DELETE_PAGES; pageIndex++) {
			StalePageOutcome outcome = deletePageOfStaleEntries(theCutoffDate);
			totalDeleted += outcome.deletedCount();
			moreStaleRemain = outcome.moreStaleRemain();
		}

		if (moreStaleRemain) {
			ourLog.warn(
					"Reached the maximum of {} delete pages after removing {} SearchUrls; remaining stale entries"
							+ " will be removed by the next scheduled run",
					MAX_DELETE_PAGES,
					totalDeleted);
		}
		if (totalDeleted > 0) {
			ourLog.info("Deleted {} SearchUrls", totalDeleted);
		} else {
			ourLog.debug("Deleted {} SearchUrls", totalDeleted);
		}
	}

	/**
	 * Deletes one page of stale entries in its own transaction — the repository is
	 * {@code @Transactional(MANDATORY)}, so it needs an enclosing tx, and we want that tx to commit
	 * between pages, not span the sweep.
	 */
	private StalePageOutcome deletePageOfStaleEntries(Date theCutoffDate) {
		PageRequest page = PageRequest.of(0, DELETE_PAGE_SIZE);
		return myPageTxTemplate.execute(theStatus -> {
			Slice<Long> stale = myResourceSearchUrlDao.findStaleIds(theCutoffDate, page);
			if (stale.isEmpty()) {
				return new StalePageOutcome(0, false);
			}
			int deleted = myResourceSearchUrlDao.deleteByResIds(stale.getContent());
			ourLog.debug("Deleted page of {} stale SearchUrls", deleted);
			return new StalePageOutcome(deleted, stale.hasNext());
		});
	}

	private record StalePageOutcome(int deletedCount, boolean moreStaleRemain) {}

	/**
	 * Once a resource is updated or deleted, we can trust that future match checks will find the committed resource in the db.
	 * The use of the constraint table is done, and we can delete it to keep the table small.
	 */
	@Transactional
	public void deleteByResId(JpaPid theResId) {
		myResourceSearchUrlDao.deleteByResId(theResId.getId());
	}

	/**
	 * Once a resource is updated or deleted, we can trust that future match checks will find the committed resource in the db.
	 * The use of the constraint table is done, and we can delete it to keep the table small.
	 */
	@Transactional
	public void deleteByResIds(Collection<JpaPid> theResId) {
		myResourceSearchUrlDao.deleteByResIds(
				theResId.stream().map(JpaPid::getId).collect(Collectors.toList()));
	}

	/**
	 * @param theResourceName The resource name associated with the conditional URL
	 * @param theMatchUrl The URL parameters portion of the match URL. Should not include a leading {@literal ?}, but can include {@literal &} separators.
	 *
	 *  We store a record of match urls with res_id so a db constraint can catch simultaneous creates that slip through.
	 */
	@Transactional
	public void enforceMatchUrlResourceUniqueness(
			String theResourceName, String theMatchUrl, ResourceTable theResourceTable) {
		Validate.notBlank(theResourceName, "theResourceName must not be blank");
		Validate.notBlank(theMatchUrl, "theMatchUrl must not be blank");

		String canonicalizedUrlForStorage = createCanonicalizedUrlForStorage(theResourceName, theMatchUrl);

		ResourceSearchUrlEntity searchUrlEntity = ResourceSearchUrlEntity.from(
				canonicalizedUrlForStorage,
				theResourceTable,
				myPartitionSettings.isConditionalCreateDuplicateIdentifiersEnabled());
		// calling dao.save performs a merge operation which implies a trip to
		// the database to see if the resource exists.  Since we don't need the check, we avoid the trip by calling
		// em.persist.
		myEntityManager.persist(searchUrlEntity);
	}

	/**
	 * Provides a sanitized matchUrl to circumvent ordering matters.
	 */
	private String createCanonicalizedUrlForStorage(String theResourceName, String theMatchUrl) {

		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceName);
		SearchParameterMap matchUrlSearchParameterMap = myMatchUrlService.translateMatchUrl(theMatchUrl, resourceDef);

		String canonicalizedMatchUrl = matchUrlSearchParameterMap.toNormalizedQueryString(myFhirContext);

		return theResourceName + canonicalizedMatchUrl;
	}
}
