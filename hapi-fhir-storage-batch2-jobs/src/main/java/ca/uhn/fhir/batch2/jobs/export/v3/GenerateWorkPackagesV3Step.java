/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.IResourceSupportedSvc;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.util.SearchParameterUtil;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenerateWorkPackagesV3Step
		implements IFirstJobStepWorker<BulkExportJobParameters, BulkExportWorkPackageJson> {

	private static final Logger ourLog = LoggerFactory.getLogger(GenerateWorkPackagesV3Step.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IResourceSupportedSvc myResourceSupportedSvc;

	@Autowired
	private IJobPartitionProvider myJobPartitionProvider;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private IBulkExportProcessor<?> myBulkExportProcessor;

	@Autowired
	private IIdHelperService<?> myIdHelperService;

	@Autowired
	private IHapiTransactionService myTransactionService;

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

		if (params.getExportStyle() == BulkExportJobParameters.ExportStyle.GROUP) {
			handleGroupLevelExport(theStepExecutionDetails, theDataSink, params);
		} else {
			handleSystemOrPatientLevelExport(theStepExecutionDetails, theDataSink, params);
		}

		return RunOutcome.SUCCESS;
	}

	private void handleGroupLevelExport(
			StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			IJobDataSink<BulkExportWorkPackageJson> theDataSink,
			BulkExportJobParameters params) {
		Validate.notNull(params.getGroupId(), "groupId must be set for export style GROUP");
		Validate.isTrue(
				params.getGroupId().contains("/"),
				"groupId must be a typed FHIR ID (Group/XYZ) for export style GROUP");

		IIdType groupId = myFhirContext.getVersion().newIdType(params.getGroupId());
		RequestPartitionId groupReadPartition = myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(
				theStepExecutionDetails.newSystemRequestDetails(), groupId);

		// FIXME: should consider date range?
		ExportPIDIteratorParameters patientListParams = new ExportPIDIteratorParameters();
		patientListParams.setGroupId(params.getGroupId());
		patientListParams.setPartitionId(groupReadPartition);
		patientListParams.setStartDate(params.getSince());
		patientListParams.setEndDate(params.getUntil());
		patientListParams.setFilters(List.of());

		Set<String> patientResourceIds = myTransactionService
				.withSystemRequest()
				.withRequestPartitionId(groupReadPartition)
				.readOnly()
				.execute(() -> {
					Set groupMemberPids = myBulkExportProcessor.getExpandedPatientList(patientListParams, false);
					return myIdHelperService.translatePidsToFhirResourceIds(groupMemberPids);
				});

		List<String> requestedPatientIds =
				theStepExecutionDetails.getParameters().getPatientIds();
		if (requestedPatientIds != null && !requestedPatientIds.isEmpty()) {
			// FIXME: validate format of parameter patientIds (need to be Patient/XXX)
			patientResourceIds.retainAll(requestedPatientIds);
		}

		ListMultimap<RequestPartitionId, String> patientResourceIdsByPartition =
				Multimaps.index(patientResourceIds, t -> determinePatientPartition(theStepExecutionDetails, t));
		List<String> resourceTypes = getResourceTypes(theStepExecutionDetails, params);
		for (RequestPartitionId nextPartitionChunk : patientResourceIdsByPartition.keySet()) {

			List<String> chunkPatientIds = patientResourceIdsByPartition.get(nextPartitionChunk);
			for (String resourceType : resourceTypes) {
				if ("Group".equals(resourceType)) {
					continue;
				}

				BulkExportWorkPackageJson workPackage = new BulkExportWorkPackageJson();
				workPackage.setPatientIds(chunkPatientIds);
				workPackage.setResourceType(resourceType);
				workPackage.setPartitionId(nextPartitionChunk);
				theDataSink.accept(workPackage);
			}
		}

		if (resourceTypes.contains("Group")) {
			BulkExportWorkPackageJson workPackage = new BulkExportWorkPackageJson();
			workPackage.setResourceType("Group");
			workPackage.setPartitionId(groupReadPartition);
			workPackage.setGroupId(params.getGroupId());
			theDataSink.accept(workPackage);
		}
	}

	private RequestPartitionId determinePatientPartition(
			StepExecutionDetails theStepExecutionDetails, String thePatientId) {
		return myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(
				theStepExecutionDetails.newSystemRequestDetails(),
				"Patient",
				myFhirContext.getVersion().newIdType(thePatientId));
	}

	private void handleSystemOrPatientLevelExport(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkExportWorkPackageJson> theDataSink,
			BulkExportJobParameters params) {
		RequestPartitionId requestPartitionId = params.getPartitionId();
		List<RequestPartitionId> requestPartitions =
				myJobPartitionProvider.splitPartitionsForJobExecution(requestPartitionId);

		List<String> resourceTypes = getResourceTypes(theStepExecutionDetails, params);
		for (String resourceType : resourceTypes) {

			for (RequestPartitionId requestPartition : requestPartitions) {
				BulkExportWorkPackageJson workPackage = new BulkExportWorkPackageJson();
				workPackage.setResourceType(resourceType);
				workPackage.setPartitionId(requestPartition);
				theDataSink.accept(workPackage);
			}
		}
	}

	@Nonnull
	private List<String> getResourceTypes(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			BulkExportJobParameters params) {
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

		List<String> resourceTypes = params.getResourceTypes().stream()
				.filter(resourceType -> myResourceSupportedSvc.isSupported(resourceType))
				.filter(resourceType -> !resourceTypesToOmit.contains(resourceType))
				.toList();
		return resourceTypes;
	}
}
