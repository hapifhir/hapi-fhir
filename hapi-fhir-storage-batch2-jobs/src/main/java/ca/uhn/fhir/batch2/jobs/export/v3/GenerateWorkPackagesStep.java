/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.export.v3;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportWorkPackageJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.IResourceSupportedSvc;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.util.SearchParameterUtil;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenerateWorkPackagesStep
		implements IFirstJobStepWorker<BulkExportJobParameters, BulkExportWorkPackageJson> {

	private static final Logger ourLog = LoggerFactory.getLogger(GenerateWorkPackagesStep.class);

	@Autowired
	private IResourceSupportedSvc myResourceSupportedSvc;

	@Autowired
	private IJobPartitionProvider myJobPartitionProvider;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkExportWorkPackageJson> theDataSink)
			throws JobExecutionFailedException {
		BulkExportJobParameters params = theStepExecutionDetails.getParameters();
		ourLog.info(
				"Generating work packages for bulk export job instance[{}]",
				theStepExecutionDetails.getInstance().getInstanceId());

		RequestPartitionId requestPartitionId = params.getPartitionId();
		List<RequestPartitionId> requestPartitions =
				myJobPartitionProvider.splitPartitionsForJobExecution(requestPartitionId);

		/*
		 * NB: patient-compartment limitation
		 * We know that Group and List are part of patient compartment.
		 * But allowing export of them seems like a security flaw.
		 * So we'll exclude them.
		 */
		Set<String> resourceTypesToOmit =
				theStepExecutionDetails.getParameters().getExportStyle() == BulkExportJobParameters.ExportStyle.PATIENT
						? new HashSet<>(
								SearchParameterUtil.RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT.keySet())
						: Set.of();

		for (String resourceType : params.getResourceTypes()) {
			if (resourceTypesToOmit.contains(resourceType) || !myResourceSupportedSvc.isSupported(resourceType)) {
				continue;
			}

			for (RequestPartitionId requestPartition : requestPartitions) {
				BulkExportWorkPackageJson workPackage = new BulkExportWorkPackageJson();
				workPackage.setResourceType(resourceType);
				workPackage.setPartitionId(requestPartition);
				theDataSink.accept(workPackage);
			}
		}

		return RunOutcome.SUCCESS;
	}
}
