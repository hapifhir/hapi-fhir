package ca.uhn.fhir.batch2.jobs.export;

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

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.models.Id;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FetchResourceIdsStep implements IFirstJobStepWorker<BulkExportJobParameters, ResourceIdList> {
	private static final Logger ourLog = LoggerFactory.getLogger(FetchResourceIdsStep.class);

	public static final int MAX_IDS_TO_BATCH = 900;

	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Autowired
	private DaoConfig myDaoConfig;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
								 @Nonnull IJobDataSink<ResourceIdList> theDataSink) throws JobExecutionFailedException {
		BulkExportJobParameters params = theStepExecutionDetails.getParameters();
		ourLog.info("Starting BatchExport job");

		ExportPIDIteratorParameters providerParams = new ExportPIDIteratorParameters();
		providerParams.setFilters(params.getFilters());
		providerParams.setStartDate(params.getStartDate());
		providerParams.setExportStyle(params.getExportStyle());
		providerParams.setGroupId(params.getGroupId());
		providerParams.setPatientIds(params.getPatientIds());
		providerParams.setExpandMdm(params.isExpandMdm());

		int submissionCount = 0;
		try {
			Set<Id> submittedIds = new HashSet<>();

			for (String resourceType : params.getResourceTypes()) {
				providerParams.setResourceType(resourceType);

				// filters are the filters for searching
				ourLog.info("Running FetchResourceIdsStep for resource type: {} with params: {}", resourceType, providerParams);
				Iterator<BaseResourcePersistentId<?>> pidIterator = myBulkExportProcessor.getResourcePidIterator(providerParams);
				List<Id> idsToSubmit = new ArrayList<>();

				if (!pidIterator.hasNext()) {
					ourLog.debug("Bulk Export generated an iterator with no results!");
				}
				while (pidIterator.hasNext()) {
					BaseResourcePersistentId<?> pid = pidIterator.next();

					Id id;
					if (pid.getResourceType() != null) {
						id = Id.getIdFromPID(pid, pid.getResourceType());
					} else {
						id = Id.getIdFromPID(pid, resourceType);
					}

					if (!submittedIds.add(id)) {
						continue;
					}

					idsToSubmit.add(id);

					// Make sure resources stored in each batch does not go over the max capacity
					if (idsToSubmit.size() >= myDaoConfig.getBulkExportFileMaximumCapacity()) {
						submitWorkChunk(idsToSubmit, resourceType, params, theDataSink);
						submissionCount++;
						idsToSubmit = new ArrayList<>();
					}
				}

				// if we have any other Ids left, submit them now
				if (!idsToSubmit.isEmpty()) {
					submitWorkChunk(idsToSubmit, resourceType, params, theDataSink);
					submissionCount++;
				}
			}
		} catch (Exception ex) {
			ourLog.error(ex.getMessage(), ex);

			theDataSink.recoveredError(ex.getMessage());

			throw new JobExecutionFailedException(Msg.code(2104) + " : " + ex.getMessage());
		}

		ourLog.info("Submitted {} groups of ids for processing", submissionCount);
		return RunOutcome.SUCCESS;
	}

	private void submitWorkChunk(List<Id> theIds,
										  String theResourceType,
										  BulkExportJobParameters theParams,
										  IJobDataSink<ResourceIdList> theDataSink) {
		ResourceIdList idList = new ResourceIdList();

		idList.setIds(theIds);

		idList.setResourceType(theResourceType);

		theDataSink.accept(idList);
	}
}
