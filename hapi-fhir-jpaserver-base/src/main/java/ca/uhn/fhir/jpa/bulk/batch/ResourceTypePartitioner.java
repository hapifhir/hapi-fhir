package ca.uhn.fhir.jpa.bulk.batch;

import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import org.slf4j.Logger;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.transaction.TransactionSynchronizationRegistry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

		List<String> resourceTypes = myBulkExportDaoSvc.getBulkJobResourceTypes(myJobUUID);

		resourceTypes.stream()
			.forEach(resourceType -> {
				ExecutionContext context = new ExecutionContext();
				context.putString("resourceType", resourceType);
				context.putString("jobUUID", myJobUUID);
				partitionContextMap.put(resourceType, context);
				});

		return partitionContextMap;
	}


}
