package ca.uhn.fhir.jpa.bulk.batch;

import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BulkExportDaoSvc {
	private static final Logger ourLog = getLogger(BulkExportDaoSvc.class);

	@Autowired
	IBulkExportJobDao myBulkExportJobDao;

	@Transactional
	public List<String> getBulkJobResourceTypes(String theJobUUID) {
		BulkExportJobEntity bulkExportJobEntity = loadJob(theJobUUID);
		return bulkExportJobEntity.getCollections()
			.stream()
			.map(BulkExportCollectionEntity::getResourceType)
			.collect(Collectors.toList());
	}

	private BulkExportJobEntity loadJob(String theJobUUID) {
		Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(theJobUUID);
		if (!jobOpt.isPresent()) {
			ourLog.info("Job appears to be deleted");
			return null;
		}
		return jobOpt.get();
	}

}
