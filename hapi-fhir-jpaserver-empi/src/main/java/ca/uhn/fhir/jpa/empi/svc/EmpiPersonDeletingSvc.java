package ca.uhn.fhir.jpa.empi.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
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

import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpiPersonDeletingSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	/**
	 * This is here for the case of possible infinite loops. Technically batch conflict deletion should handle this, but this is an escape hatch.
	 */
	private static final int MAXIMUM_DELETE_ATTEMPTS = 100000;

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private ExpungeService myExpungeService;

	/**
	 * Function which will delete all resources by their PIDs, and also delete any resources that were undeletable due to
	 * VersionConflictException
	 *
	 * @param theResourcePids
	 */
	@Transactional
	public void deletePersonResourcesAndHandleConflicts(List<Long> theResourcePids) {
		List<ResourcePersistentId> resourceIds = ResourcePersistentId.fromLongList(theResourcePids);
		ourLog.info("Deleting {} Person resources...", resourceIds.size());
		DeleteConflictList
			deleteConflictList = new DeleteConflictList();

		IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao("Person");
		resourceDao.deletePidList(ProviderConstants.EMPI_CLEAR, resourceIds, deleteConflictList, null);

		IFhirResourceDao personDao = myDaoRegistry.getResourceDao("Person");
		int batchCount = 0;
		while (!deleteConflictList.isEmpty()) {
			deleteConflictBatch(deleteConflictList, personDao);
			batchCount += 1;
			if (batchCount > MAXIMUM_DELETE_ATTEMPTS) {
				throw new IllegalStateException("Person deletion seems to have entered an infinite loop. Aborting");
			}
		}
		ourLog.info("Deleted {} Person resources in {} batches", resourceIds.size(), batchCount);
	}

	/**
	 * Use the expunge service to expunge all historical and current versions of the resources associated to the PIDs.
	 */
	public void expungeHistoricalAndCurrentVersionsOfIds(List<Long> theLongs) {
		ourLog.info("Expunging historical versions of {} Person resources...", theLongs.size());
		ExpungeOptions options = new ExpungeOptions();
		options.setExpungeDeletedResources(true);
		options.setExpungeOldVersions(true);
		theLongs
			.forEach(personId -> myExpungeService.expunge("Person", personId, null, options, null));
		ourLog.info("Expunged historical versions of {} Person resources", theLongs.size());
	}

	private void deleteConflictBatch(DeleteConflictList theDcl, IFhirResourceDao<IBaseResource> theDao) {
		DeleteConflictList newBatch = new DeleteConflictList();
		TransactionDetails transactionDetails = new TransactionDetails();
		for (DeleteConflict next : theDcl) {
			IdDt nextSource = next.getSourceId();
			ourLog.info("Have delete conflict {} - Cascading delete", nextSource);
			theDao.delete(nextSource.toVersionless(), newBatch, null, transactionDetails);
		}
		theDcl.removeAll();
		theDcl.addAll(newBatch);
	}
}
