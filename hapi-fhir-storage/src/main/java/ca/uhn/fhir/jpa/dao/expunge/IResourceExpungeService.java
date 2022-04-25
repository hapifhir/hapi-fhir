package ca.uhn.fhir.jpa.dao.expunge;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface IResourceExpungeService {
	List<ResourcePersistentId> findHistoricalVersionsOfDeletedResources(String theResourceName, ResourcePersistentId theResourceId, int theI);

	List<ResourcePersistentId> findHistoricalVersionsOfNonDeletedResources(String theResourceName, ResourcePersistentId theResourceId, int theI);

	void expungeHistoricalVersions(RequestDetails theRequestDetails, List<ResourcePersistentId> thePartition, AtomicInteger theRemainingCount);

	void expungeCurrentVersionOfResources(RequestDetails theRequestDetails, List<ResourcePersistentId> theResourceIds, AtomicInteger theRemainingCount);

	void expungeHistoricalVersionsOfIds(RequestDetails theRequestDetails, List<ResourcePersistentId> theResourceIds, AtomicInteger theRemainingCount);

	void deleteAllSearchParams(ResourcePersistentId theResourceId);
}
