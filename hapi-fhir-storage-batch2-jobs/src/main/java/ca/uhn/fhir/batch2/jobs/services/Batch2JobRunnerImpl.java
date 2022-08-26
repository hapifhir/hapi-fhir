package ca.uhn.fhir.batch2.jobs.services;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.Batch2JobInfo;
import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.jpa.api.svc.IBatch2JobRunner;
import ca.uhn.fhir.jpa.batch.models.Batch2BaseJobParameters;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

import static org.slf4j.LoggerFactory.getLogger;

public class Batch2JobRunnerImpl implements IBatch2JobRunner {
	private static final Logger ourLog = getLogger(IBatch2JobRunner.class);

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Override
	public Batch2JobStartResponse startNewJob(Batch2BaseJobParameters theParameters) {
		switch (theParameters.getJobDefinitionId()) {
			case Batch2JobDefinitionConstants.BULK_EXPORT:
				if (theParameters instanceof BulkExportParameters) {
					return startBatch2BulkExportJob((BulkExportParameters) theParameters);
				}
				else {
					ourLog.error("Invalid parameters for " + Batch2JobDefinitionConstants.BULK_EXPORT);
				}
				break;
			default:
				// Dear future devs - add your case above
				ourLog.error("Invalid JobDefinitionId " + theParameters.getJobDefinitionId());
				break;
		}
		return null;
	}

	@Override
	public Batch2JobInfo getJobInfo(String theJobId) {
		JobInstance instance = myJobCoordinator.getInstance(theJobId);
		if (instance == null) {
			throw new ResourceNotFoundException(Msg.code(2102) + " : " + theJobId);
		}
		return fromJobInstanceToBatch2JobInfo(instance);
	}

	private Batch2JobInfo fromJobInstanceToBatch2JobInfo(@Nonnull JobInstance theInstance) {
		Batch2JobInfo info = new Batch2JobInfo();
		info.setJobId(theInstance.getInstanceId());
		info.setStatus(fromBatchStatus(theInstance.getStatus()));
		info.setCancelled(theInstance.isCancelled());
		info.setStartTime(theInstance.getStartTime());
		info.setEndTime(theInstance.getEndTime());
		info.setReport(theInstance.getReport());
		info.setErrorMsg(theInstance.getErrorMessage());
		return info;
	}

	public static BulkExportJobStatusEnum fromBatchStatus(StatusEnum status) {
		switch (status) {
			case QUEUED:
				return BulkExportJobStatusEnum.SUBMITTED;
			case COMPLETED :
				return BulkExportJobStatusEnum.COMPLETE;
			case IN_PROGRESS:
				return BulkExportJobStatusEnum.BUILDING;
			case FAILED:
			case CANCELLED:
			case ERRORED:
			default:
				return BulkExportJobStatusEnum.ERROR;
		}
	}

	private Batch2JobStartResponse startBatch2BulkExportJob(BulkExportParameters theParameters) {
		JobInstanceStartRequest request = createStartRequest(theParameters);
		request.setParameters(BulkExportJobParameters.createFromExportJobParameters(theParameters));

		return myJobCoordinator.startInstance(request);
	}

	private JobInstanceStartRequest createStartRequest(Batch2BaseJobParameters theParameters) {
		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(theParameters.getJobDefinitionId());
		request.setUseCache(theParameters.isUseExistingJobsFirst());
		return request;
	}
}
