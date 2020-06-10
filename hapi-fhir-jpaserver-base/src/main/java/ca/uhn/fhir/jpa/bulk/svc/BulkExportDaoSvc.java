package ca.uhn.fhir.jpa.bulk.svc;

import ca.uhn.fhir.jpa.bulk.model.BulkJobStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BulkExportDaoSvc {
	private static final Logger ourLog = getLogger(BulkExportDaoSvc.class);

	@Autowired
	IBulkExportJobDao myBulkExportJobDao;

	@Autowired
	IBulkExportCollectionDao myBulkExportCollectionDao;

	@Autowired
	IBulkExportCollectionFileDao myBulkExportCollectionFileDao;

	@Transactional
	public void addFileToCollectionWithId(Long theCollectionEntityId, BulkExportCollectionFileEntity theFile) {
		Optional<BulkExportCollectionEntity> byId = myBulkExportCollectionDao.findById(theCollectionEntityId);
		if (byId.isPresent()) {
			BulkExportCollectionEntity exportCollectionEntity = byId.get();
			theFile.setCollection(exportCollectionEntity);
			myBulkExportCollectionFileDao.saveAndFlush(theFile);
			myBulkExportCollectionDao.saveAndFlush(exportCollectionEntity);
		}

	}

	@Transactional
	public Map<Long, String> getBulkJobCollectionIdToResourceTypeMap(String theJobUUID) {
		BulkExportJobEntity bulkExportJobEntity = loadJob(theJobUUID);
		Collection<BulkExportCollectionEntity> collections = bulkExportJobEntity.getCollections();
		return collections.stream()
			.collect(Collectors.toMap(
				BulkExportCollectionEntity::getId,
				BulkExportCollectionEntity::getResourceType
			));
	}

	private BulkExportJobEntity loadJob(String theJobUUID) {
		Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(theJobUUID);
		if (!jobOpt.isPresent()) {
			ourLog.warn("Job with UUID {} appears to be deleted", theJobUUID);
			return null;
		}
		return jobOpt.get();
	}

	@Transactional
	public void setJobToStatus(String theJobUUID, BulkJobStatusEnum theStatus) {
		Optional<BulkExportJobEntity> oJob = myBulkExportJobDao.findByJobId(theJobUUID);
		if (!oJob.isPresent()) {
			ourLog.error("Job doesn't exist!");
		} else {
			ourLog.info("Setting job with UUID {} to {}", theJobUUID, theStatus);
			BulkExportJobEntity bulkExportJobEntity = oJob.get();
			bulkExportJobEntity.setStatus(theStatus);
			myBulkExportJobDao.save(bulkExportJobEntity);
		}

	}

}
