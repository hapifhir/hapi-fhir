package ca.uhn.fhir.jpa.bulk.svc;

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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.bulk.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.model.BulkJobStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.UrlUtil.escapeUrlParam;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BulkExportDaoSvc {
	private static final Logger ourLog = getLogger(BulkExportDaoSvc.class);

	private int myRetentionPeriod = (int) (2 * DateUtils.MILLIS_PER_HOUR);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	IBulkExportJobDao myBulkExportJobDao;

	@Autowired
	private DaoRegistry myDaoRegistry;

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
	public void setJobToStatus(String theJobUUID, BulkJobStatusEnum theStatus) {
		Optional<BulkExportJobEntity> oJob = myBulkExportJobDao.findByJobId(theJobUUID);
		if (!oJob.isPresent()) {
			ourLog.error("Job with UUID {} doesn't exist!", theJobUUID);
			return;
		}

		ourLog.info("Setting job with UUID {} to {}", theJobUUID, theStatus);
		BulkExportJobEntity bulkExportJobEntity = oJob.get();
		bulkExportJobEntity.setStatus(theStatus);
		myBulkExportJobDao.save(bulkExportJobEntity);

	}

	public IBulkDataExportSvc.JobInfo submitJob(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters, int theReuseMillis) {
		String outputFormat = Constants.CT_FHIR_NDJSON;
		if (isNotBlank(theOutputFormat)) {
			outputFormat = theOutputFormat;
		}
		if (!Constants.CTS_NDJSON.contains(outputFormat)) {
			throw new InvalidRequestException("Invalid output format: " + theOutputFormat);
		}

		StringBuilder requestBuilder = new StringBuilder();
		requestBuilder.append("/").append(JpaConstants.OPERATION_EXPORT);
		requestBuilder.append("?").append(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT).append("=").append(escapeUrlParam(outputFormat));
		Set<String> resourceTypes = theResourceTypes;
		if (resourceTypes != null) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE).append("=").append(String.join(",", resourceTypes));
		}
		Date since = theSince;
		if (since != null) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_SINCE).append("=").append(new InstantType(since).setTimeZoneZulu(true).getValueAsString());
		}
		if (theFilters != null && theFilters.size() > 0) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE_FILTER).append("=").append(String.join(",", theFilters));
		}
		String request = requestBuilder.toString();

		Date cutoff = DateUtils.addMilliseconds(new Date(), -theReuseMillis);
		Pageable page = PageRequest.of(0, 10);
		Slice<BulkExportJobEntity> existing = myBulkExportJobDao.findExistingJob(page, request, cutoff, BulkJobStatusEnum.ERROR);
		if (!existing.isEmpty()) {
			return toSubmittedJobInfo(existing.iterator().next());
		}

		if (resourceTypes != null && resourceTypes.contains("Binary")) {
			String msg = myFhirContext.getLocalizer().getMessage(BulkDataExportSvcImpl.class, "onlyBinarySelected");
			throw new InvalidRequestException(msg);
		}

		if (resourceTypes == null || resourceTypes.isEmpty()) {
			// This is probably not a useful default, but having the default be "download the whole
			// server" seems like a risky default too. We'll deal with that by having the default involve
			// only returning a small time span
			resourceTypes = myFhirContext.getResourceTypes();
			if (since == null) {
				since = DateUtils.addDays(new Date(), -1);
			}
		}

		resourceTypes =
			resourceTypes
				.stream()
				.filter(t -> !"Binary".equals(t))
				.collect(Collectors.toSet());

		BulkExportJobEntity job = new BulkExportJobEntity();
		job.setJobId(UUID.randomUUID().toString());
		job.setStatus(BulkJobStatusEnum.SUBMITTED);
		job.setSince(since);
		job.setCreated(new Date());
		job.setRequest(request);

		updateExpiry(job);
		myBulkExportJobDao.save(job);

		for (String nextType : resourceTypes) {
			if (!myDaoRegistry.isResourceTypeSupported(nextType)) {
				String msg = myFhirContext.getLocalizer().getMessage(BulkDataExportSvcImpl.class, "unknownResourceType", nextType);
				throw new InvalidRequestException(msg);
			}

			BulkExportCollectionEntity collection = new BulkExportCollectionEntity();
			collection.setJob(job);
			collection.setResourceType(nextType);
			job.getCollections().add(collection);
			myBulkExportCollectionDao.save(collection);
		}

		ourLog.info("Bulk export job submitted: {}", job.toString());

		return toSubmittedJobInfo(job);
	}

	private void updateExpiry(BulkExportJobEntity theJob) {
		theJob.setExpiry(DateUtils.addMilliseconds(new Date(), myRetentionPeriod));
	}
	private IBulkDataExportSvc.JobInfo toSubmittedJobInfo(BulkExportJobEntity theJob) {
		return new IBulkDataExportSvc.JobInfo().setJobId(theJob.getJobId());
	}

}
