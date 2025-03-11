/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.reindex.svcs;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ReindexJobStatus;

import java.util.Map;

public class ReindexJobService {

	private final DaoRegistry myDaoRegistry;

	public ReindexJobService(DaoRegistry theRegistry) {
		myDaoRegistry = theRegistry;
	}

	/**
	 * Checks if any of the resource types in the map have any pending reindex work waiting.
	 * This will return true after the first such encounter, and only return false if no
	 * reindex work is required for any resource.
	 * @param theResourceTypesToCheckFlag map of resourceType:whether or not to check
	 * @return true if there's reindex work pending, false otherwise
	 */
	public boolean anyResourceHasPendingReindexWork(Map<String, Boolean> theResourceTypesToCheckFlag) {
		for (String resourceType : theResourceTypesToCheckFlag.keySet()) {
			boolean toCheck = theResourceTypesToCheckFlag.get(resourceType);
			if (toCheck) {
				IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);

				ReindexJobStatus status = dao.getReindexJobStatus();
				if (status.isHasReindexWorkPending()) {
					return true;
				}
			}
		}
		return false;
	}
}
