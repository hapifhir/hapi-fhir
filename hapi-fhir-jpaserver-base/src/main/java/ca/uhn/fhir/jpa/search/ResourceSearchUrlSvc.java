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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.data.IResourceSearchUrlDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * This service ensures uniqueness of resources during create or create-on-update
 * by storing the resource searchUrl.
 *
 * @see SearchUrlJobMaintenanceSvcImpl which deletes stale entities
 */
@Transactional
@Service
public class ResourceSearchUrlSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceSearchUrlSvc.class);
	private final EntityManager myEntityManager;

	private final IResourceSearchUrlDao myResourceSearchUrlDao;

	private final MatchUrlService myMatchUrlService;

	private final FhirContext myFhirContext;
	private final PartitionSettings myPartitionSettings;

	public ResourceSearchUrlSvc(
			EntityManager theEntityManager,
			IResourceSearchUrlDao theResourceSearchUrlDao,
			MatchUrlService theMatchUrlService,
			FhirContext theFhirContext,
			PartitionSettings thePartitionSettings) {
		myEntityManager = theEntityManager;
		myResourceSearchUrlDao = theResourceSearchUrlDao;
		myMatchUrlService = theMatchUrlService;
		myFhirContext = theFhirContext;
		myPartitionSettings = thePartitionSettings;
	}

	/**
	 * Perform removal of entries older than {@code theCutoffDate} since the create operations are done.
	 */
	public void deleteEntriesOlderThan(Date theCutoffDate) {
		ourLog.debug("About to delete SearchUrl which are older than {}", theCutoffDate);
		int deletedCount = myResourceSearchUrlDao.deleteAllWhereCreatedBefore(theCutoffDate);
		ourLog.debug("Deleted {} SearchUrls", deletedCount);
	}

	/**
	 * Once a resource is updated or deleted, we can trust that future match checks will find the committed resource in the db.
	 * The use of the constraint table is done, and we can delete it to keep the table small.
	 */
	public void deleteByResId(long theResId) {
		myResourceSearchUrlDao.deleteByResId(theResId);
	}

	/**
	 *  We store a record of match urls with res_id so a db constraint can catch simultaneous creates that slip through.
	 */
	public void enforceMatchUrlResourceUniqueness(
			String theResourceName, String theMatchUrl, ResourceTable theResourceTable) {
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
