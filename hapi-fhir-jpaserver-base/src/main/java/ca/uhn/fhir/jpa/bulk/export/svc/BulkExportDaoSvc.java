package ca.uhn.fhir.jpa.bulk.export.svc;

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

import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
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
			exportCollectionEntity.getFiles().add(theFile);
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
	public void setJobToStatus(String theJobUUID, BulkExportJobStatusEnum theStatus) {
		setJobToStatus(theJobUUID, theStatus, null);
	}

	@Transactional
	public void setJobToStatus(String theJobUUID, BulkExportJobStatusEnum theStatus, String theStatusMessage) {
		Optional<BulkExportJobEntity> oJob = myBulkExportJobDao.findByJobId(theJobUUID);
		if (!oJob.isPresent()) {
			ourLog.error("Job with UUID {} doesn't exist!", theJobUUID);
			return;
		}

		ourLog.info("Setting job with UUID {} to {}", theJobUUID, theStatus);
		BulkExportJobEntity bulkExportJobEntity = oJob.get();
		bulkExportJobEntity.setStatus(theStatus);
		bulkExportJobEntity.setStatusMessage(theStatusMessage);
		myBulkExportJobDao.save(bulkExportJobEntity);

	}

}
