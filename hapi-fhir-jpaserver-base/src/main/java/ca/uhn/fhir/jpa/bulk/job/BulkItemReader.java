package ca.uhn.fhir.jpa.bulk.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.UrlUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Basic Bulk Export implementation which simply reads all type filters and applies them, along with the _since param
 * on a given resource type.
 */
public class BulkItemReader extends BaseBulkItemReader {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();


	@Override
	Iterator<ResourcePersistentId> getResourcePidIterator() {
		ourLog.info("Bulk export assembling export of type {} for job {}", myResourceType, myJobUUID);


		SearchParameterMap map = createSearchParameterMapForJob();
		ISearchBuilder sb = getSearchBuilderForLocalResourceType();
		IResultIterator myResultIterator = sb.createQuery(map, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());

		List<ResourcePersistentId> myReadPids = new ArrayList<>();
		while (myResultIterator.hasNext()) {
			myReadPids.add(myResultIterator.next());
		}
		return myReadPids.iterator();
	}

}
