package ca.uhn.fhir.jpa.bulk.export.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Basic Bulk Export implementation which simply reads all type filters and applies them, along with the _since param
 * on a given resource type.
 */
public class BulkItemReader extends BaseJpaBulkItemReader {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Override
	protected Iterator<ResourcePersistentId> getResourcePidIterator() {
		ourLog.info("Bulk export assembling export of type {} for job {}", myResourceType, myJobUUID);
		Set<ResourcePersistentId> myReadPids = new HashSet<>();

		List<SearchParameterMap> map = createSearchParameterMapsForResourceType();
		ISearchBuilder sb = getSearchBuilderForLocalResourceType();

		for (SearchParameterMap spMap: map) {
			ourLog.debug("About to evaluate query {}", spMap.toNormalizedQueryString(myContext));
			IResultIterator myResultIterator = sb.createQuery(spMap, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());
			while (myResultIterator.hasNext()) {
				myReadPids.add(myResultIterator.next());
			}
		}
		return myReadPids.iterator();
	}

}
