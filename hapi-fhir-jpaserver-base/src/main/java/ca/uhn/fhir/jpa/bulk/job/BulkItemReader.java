package ca.uhn.fhir.jpa.bulk.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class BulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Value("#{jobParameters['readChunkSize']}")
	private Long READ_CHUNK_SIZE;

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myContext;

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	private BulkExportJobEntity myJobEntity;

	@Value("#{jobExecutionContext['jobUUID']}")
	private String myJobUUID;

	@Value("#{stepExecutionContext['resourceType']}")
	private String myResourceType;

	Iterator<ResourcePersistentId> myPidIterator;

	private void loadResourcePids() {
		Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(myJobUUID);
		if (!jobOpt.isPresent()) {
			ourLog.warn("Job appears to be deleted");
			return;
		}
		myJobEntity = jobOpt.get();
		ourLog.info("Bulk export starting generation for batch export job: {}", myJobEntity);

		IFhirResourceDao dao = myDaoRegistry.getResourceDao(myResourceType);

		ourLog.info("Bulk export assembling export of type {} for job {}", myResourceType, myJobUUID);

		Class<? extends IBaseResource> nextTypeClass = myContext.getResourceDefinition(myResourceType).getImplementingClass();
		ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, myResourceType, nextTypeClass);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		if (myJobEntity.getSince() != null) {
			map.setLastUpdated(new DateRangeParam(myJobEntity.getSince(), null));
		}

		IResultIterator myResultIterator = sb.createQuery(map, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());
		List<ResourcePersistentId> myReadPids = new ArrayList<>();
		while (myResultIterator.hasNext()) {
			myReadPids.add(myResultIterator.next());
		}
		myPidIterator = myReadPids.iterator();
	}

	@Override
	public List<ResourcePersistentId> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (myPidIterator == null) {
			loadResourcePids();
		}
		int count = 0;
		List<ResourcePersistentId> outgoing = new ArrayList<>();
		while (myPidIterator.hasNext() && count < READ_CHUNK_SIZE) {
			outgoing.add(myPidIterator.next());
			count += 1;
		}

		return outgoing.size() == 0 ? null : outgoing;

	}
}
