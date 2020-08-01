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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EmpiPersonDeletingSvc {
	private static final Logger ourLog = getLogger(EmpiPersonDeletingSvc.class);
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
	 * @param theLongs
	 */
	@Transactional
	public void deleteResourcesAndHandleConflicts(List<Long> theLongs) {
		DeleteConflictList
			deleteConflictList = new DeleteConflictList();
		theLongs.stream().forEach(pid -> deleteCascade(pid, deleteConflictList));

		IFhirResourceDao personDao = myDaoRegistry.getResourceDao("Person");
		int batchCount = 0;
		while (!deleteConflictList.isEmpty()) {
			deleteConflictBatch(deleteConflictList, personDao);
			batchCount += 1;
			if (batchCount > MAXIMUM_DELETE_ATTEMPTS) {
				throw new IllegalStateException("Person deletion seems to have entered an infinite loop. Aborting");
			}
		}
	}

	/**
	 * Use the expunge service to expunge all historical and current versions of the resources associated to the PIDs.
	 */
	public void expungeHistoricalAndCurrentVersionsOfIds(List<Long> theLongs) {
		ExpungeOptions options = new ExpungeOptions();
		options.setExpungeDeletedResources(true);
		options.setExpungeOldVersions(true);
		theLongs
			.forEach(personId -> myExpungeService.expunge("Person", personId, null, options, null));
	}

	private void deleteCascade(Long pid, DeleteConflictList theDeleteConflictList) {
		ourLog.debug("About to cascade delete: {}", pid);
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao("Person");
		resourceDao.delete(new IdType("Person/" + pid), theDeleteConflictList, null, null);
	}

	private void deleteConflictBatch(DeleteConflictList theDcl, IFhirResourceDao<IBaseResource> theDao) {
		DeleteConflictList newBatch = new DeleteConflictList();
		for (DeleteConflict next : theDcl) {
			IdDt nextSource = next.getSourceId();
			ourLog.info("Have delete conflict {} - Cascading delete", next);
			theDao.delete(nextSource.toVersionless(), newBatch, null, null);
		}
		theDcl.removeIf(x -> true);
		theDcl.addAll(newBatch);
	}
}
