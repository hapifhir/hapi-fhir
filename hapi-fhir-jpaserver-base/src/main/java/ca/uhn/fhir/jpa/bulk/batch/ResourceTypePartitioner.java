package ca.uhn.fhir.jpa.bulk.batch;

import org.slf4j.Logger;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class ResourceTypePartitioner implements Partitioner {
	private static final Logger ourLog = getLogger(ResourceTypePartitioner.class);

	private String myJobUUID;

	@Autowired
	private BulkExportDaoSvc myBulkExportDaoSvc;

	public ResourceTypePartitioner(String theJobUUID) {
		myJobUUID = theJobUUID;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		Map<String, ExecutionContext> partitionContextMap = new HashMap<>();

		Map<Long, String> idToResourceType = myBulkExportDaoSvc.getBulkJobCollectionIdToResourceTypeMap(	myJobUUID);
		//observation -> obs1.json, obs2.json, obs3.json BulkJobCollectionEntity
		//bulk Collection Entity ID -> patient

		// 123123-> Patient
		// 91876389126-> Observation
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
