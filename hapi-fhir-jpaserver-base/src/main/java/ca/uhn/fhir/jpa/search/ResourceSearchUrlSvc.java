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
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
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
	 * Perform removal of entries older than {@code theCutoffDate} since the create operations are done.
	 *
	 * <p>Pages the deletion in batches of {@link #DELETE_PAGE_SIZE} so no individual
	 * statement scans the whole table. A single unbounded DELETE on millions of rows trips MSSQL's
	 * per-statement timeout on MegaScale deployments (SMILE-12080); each paged DELETE is bounded.
	 *
	 * <p>Runs outside any caller transaction ({@link Propagation#NOT_SUPPORTED}) so each page commits
	 * independently — otherwise the whole sweep would still be one giant transaction.
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void deleteEntriesOlderThan(Date theCutoffDate) {
		ourLog.debug("About to delete SearchUrl which are older than {}", theCutoffDate);
		PageRequest page = PageRequest.of(0, DELETE_PAGE_SIZE);
		AtomicLong totalDeleted = new AtomicLong();
		while (true) {
			// Each page in its own transaction — the repository is @Transactional(MANDATORY), so it
			// needs an enclosing tx, and we want that tx to commit between pages, not span the sweep.
			Boolean shouldContinue = myPageTxTemplate.execute(status -> {
				Slice<Long> stale = myResourceSearchUrlDao.findStaleIds(theCutoffDate, page);
				if (stale.isEmpty()) {
					return false;
				}
				List<Long> ids = stale.getContent();
				totalDeleted.addAndGet(myResourceSearchUrlDao.deleteByResIds(ids));
				return ids.size() >= DELETE_PAGE_SIZE;
			});
			if (shouldContinue == null || !shouldContinue) {
				break;
			}
		}
		ourLog.info("Deleted {} SearchUrls", totalDeleted.get());
	}

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
