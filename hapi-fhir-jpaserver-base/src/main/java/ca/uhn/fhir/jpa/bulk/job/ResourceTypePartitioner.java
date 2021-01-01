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

import ca.uhn.fhir.jpa.bulk.svc.BulkExportDaoSvc;
import org.slf4j.Logger;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class ResourceTypePartitioner implements Partitioner {
	private static final Logger ourLog = getLogger(ResourceTypePartitioner.class);


	@Value("#{jobExecutionContext['jobUUID']}")
	private String myJobUUID;

	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> partitionContextMap = new HashMap<>();

		Map<Long, String> idToResourceType = myBulkExportDaoSvc.getBulkJobCollectionIdToResourceTypeMap(myJobUUID);

		idToResourceType.entrySet().stream()
			.forEach(entry -> {
				String resourceType = entry.getValue();
				Long collectionEntityId = entry.getKey();
				ourLog.debug("Creating a partition step for CollectionEntity: [{}] processing resource type [{}]", collectionEntityId, resourceType);

				ExecutionContext context = new ExecutionContext();
				//The worker step needs to know what resource type it is looking for.
				context.putString("resourceType", resourceType);

				// The worker step needs to know which parent job it is processing for, and which collection entity it will be
				// attaching its results to.
				context.putString("jobUUID", myJobUUID);
				context.putLong("bulkExportCollectionEntityId", collectionEntityId);

				// Name the partition based on the resource type
				partitionContextMap.put(resourceType, context);
				});


		return partitionContextMap;
	}


}
