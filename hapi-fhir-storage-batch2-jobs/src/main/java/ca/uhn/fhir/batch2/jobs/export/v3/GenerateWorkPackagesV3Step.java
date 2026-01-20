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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.bulk.export.svc.BulkExportHelperService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.SearchConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.IResourceSupportedSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.TaskChunker;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
	private IHapiTransactionService myTransactionService;

	@Autowired
	private BulkExportHelperService myBulkExportHelperService;

	@Autowired
	private DaoRegistry myDaoRegistry;

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

		switch (params.getExportStyle()) {
			case GROUP -> handleGroupLevelExport(theStepExecutionDetails, theDataSink, params);
			case PATIENT -> handlePatientLevelExport(theStepExecutionDetails, theDataSink, params);
			case SYSTEM -> handleSystemLevelExport(theStepExecutionDetails, theDataSink, params);
		}

		return RunOutcome.SUCCESS;
	}

	/**
	 * Generate work packages for GROUP type export
	 */
	private void handleGroupLevelExport(
			StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			IJobDataSink<BulkExportWorkPackageJson> theDataSink,
			BulkExportJobParameters jobParams) {
		Validate.notNull(jobParams.getGroupId(), "groupId must be set for export style GROUP");
		Validate.isTrue(
				jobParams.getGroupId().contains("/"),
				"groupId must be a typed FHIR ID (Group/XYZ) for export style GROUP");

		IIdType groupId = myFhirContext.getVersion().newIdType(jobParams.getGroupId());
		RequestPartitionId groupReadPartition = myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(
				theStepExecutionDetails.newSystemRequestDetails(), groupId);

		ExportPIDIteratorParameters patientListParams = new ExportPIDIteratorParameters();
		patientListParams.setGroupId(jobParams.getGroupId());
		patientListParams.setPatientIds(jobParams.getPatientIds());
		patientListParams.setPartitionId(groupReadPartition);
		patientListParams.setStartDate(jobParams.getSince());
		patientListParams.setEndDate(jobParams.getUntil());
		patientListParams.setExpandMdm(jobParams.isExpandMdm());
		// Filters are applied later on
		patientListParams.setFilters(List.of());

		Set<String> patientResourceIds = myTransactionService
				.withSystemRequest()
				.withRequestPartitionId(groupReadPartition)
				.readOnly()
				.execute(() -> myBulkExportProcessor.getPatientSetForGroupExport(patientListParams));

		/*
		 * Patient ID parameters are validated by BulkExportJobParametersValidator to ensure
		 * that they are always in the format "Patient/[id]"
		 */
		List<String> requestedPatientIds =
				theStepExecutionDetails.getParameters().getPatientIds();
		if (requestedPatientIds != null && !requestedPatientIds.isEmpty()) {
			patientResourceIds.retainAll(requestedPatientIds);
		}

		ListMultimap<RequestPartitionId, String> patientResourceIdsByPartition =
				indexPatientIdsByPartition(theStepExecutionDetails, patientResourceIds);
		List<String> resourceTypes = getResourceTypes(theStepExecutionDetails, jobParams);

		boolean includesGroup = resourceTypes.remove("Group");
		for (RequestPartitionId nextPartitionChunk : patientResourceIdsByPartition.keySet()) {

			List<String> patientIds = patientResourceIdsByPartition.get(nextPartitionChunk);
			patientIds = applyTypeFiltersToPatientIdList(jobParams, nextPartitionChunk, patientIds);

			if (!patientIds.isEmpty()) {
				BulkExportWorkPackageJson workPackage = new BulkExportWorkPackageJson();
				workPackage.setPatientIds(patientIds);
				workPackage.setResourceTypes(resourceTypes);
				workPackage.setPartitionId(nextPartitionChunk);
				theDataSink.accept(workPackage);
			}
		}

		if (includesGroup) {
			BulkExportWorkPackageJson workPackage = new BulkExportWorkPackageJson();
			workPackage.setResourceTypes(List.of("Group"));
			workPackage.setPartitionId(groupReadPartition);
			workPackage.setGroupId(jobParams.getGroupId());
			theDataSink.accept(workPackage);
		}
	}

	/**
	 * Generate work packages for PATIENT type export
	 */
	private void handlePatientLevelExport(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkExportWorkPackageJson> theDataSink,
			BulkExportJobParameters theParams) {

		List<String> requestedPatientIds = theParams.getPatientIds();
		if (requestedPatientIds == null || requestedPatientIds.isEmpty()) {
			handleSystemOrPatientTypeLevelExport(theStepExecutionDetails, theDataSink, theParams);
			return;
		}

		handlePatientInstanceLevelExport(theStepExecutionDetails, theDataSink, theParams, requestedPatientIds);
	}

	/**
	 * Generate work packages for SYSTEM type export
	 */
	private void handleSystemLevelExport(
			StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			IJobDataSink<BulkExportWorkPackageJson> theDataSink,
			BulkExportJobParameters theParams) {
		handleSystemOrPatientTypeLevelExport(theStepExecutionDetails, theDataSink, theParams);
	}

	/**
	 * Generate work packages for PATIENT type export with patient ID parameters specified
	 */
	private void handlePatientInstanceLevelExport(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkExportWorkPackageJson> theDataSink,
			BulkExportJobParameters theParams,
			List<String> requestedPatientIds) {
		List<String> resourceTypes = getResourceTypes(theStepExecutionDetails, theParams);

		ListMultimap<RequestPartitionId, String> partitions =
				indexPatientIdsByPartition(theStepExecutionDetails, requestedPatientIds);
		for (RequestPartitionId partition : partitions.keySet()) {
			List<String> patientIds = partitions.get(partition);

			ExportPIDIteratorParameters parameters = new ExportPIDIteratorParameters();
			parameters.setPatientIds(patientIds);
			parameters.setPartitionId(partition);
			parameters.setExpandMdm(theParams.isExpandMdm());
			Set<String> expandedPatientIds = myBulkExportProcessor.getPatientSetForPatientExport(parameters);
			if (!expandedPatientIds.isEmpty()) {

				BulkExportWorkPackageJson workPackage = new BulkExportWorkPackageJson();
				workPackage.setPatientIds(List.copyOf(expandedPatientIds));
				workPackage.setPartitionId(partition);
				workPackage.setResourceTypes(resourceTypes);
				theDataSink.accept(workPackage);
			}
		}
	}

	/**
	 * Generate work packages for SYSTEM type export or for PATIENT type export without
	 * any patient ID parameters specified
	 */
	private void handleSystemOrPatientTypeLevelExport(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkExportWorkPackageJson> theDataSink,
			BulkExportJobParameters theParams) {

		RequestDetails srd = theStepExecutionDetails.newSystemRequestDetails();
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
						srd, ProviderConstants.OPERATION_EXPORT);

		List<RequestPartitionId> requestPartitions =
				myJobPartitionProvider.splitPartitionsForJobExecution(requestPartitionId);

		List<String> resourceTypes = getResourceTypes(theStepExecutionDetails, theParams);

		for (RequestPartitionId requestPartition : requestPartitions) {
			BulkExportWorkPackageJson workPackage = new BulkExportWorkPackageJson();
			workPackage.setResourceTypes(resourceTypes);
			workPackage.setPartitionId(requestPartition);
			theDataSink.accept(workPackage);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Nonnull
	private List<String> applyTypeFiltersToPatientIdList(
			BulkExportJobParameters theJobParams, RequestPartitionId thePartitionId, Collection<String> thePatientIds) {
		List<String> newPatientIdList = new ArrayList<>(thePatientIds.size());
		TaskChunker.chunk(thePatientIds, SearchConstants.MAX_PAGE_SIZE, idChunk -> {
			RuntimeResourceDefinition patientDef = myFhirContext.getResourceDefinition("Patient");
			ExportPIDIteratorParameters pidParams = new ExportPIDIteratorParameters();
			pidParams.setPatientIds(idChunk);
			pidParams.setFilters(theJobParams.getFilters());
			List<SearchParameterMap> maps =
					myBulkExportHelperService.createSearchParameterMapsForResourceType(patientDef, pidParams, false);
			for (SearchParameterMap map : maps) {
				if (maps.isEmpty()) {
					newPatientIdList.addAll(idChunk);
				} else {
					TokenOrListParam idParamOrList = new TokenOrListParam();
					idChunk.forEach(id -> idParamOrList.add(new TokenParam(null, id)));
					map.add(IAnyResource.SP_RES_ID, idParamOrList);

					List<IIdType> filteredIds = myTransactionService
							.withSystemRequest()
							.withRequestPartitionId(thePartitionId)
							.execute(() -> {
								IFhirResourceDao patientDao = myDaoRegistry.getResourceDao("Patient");
								SystemRequestDetails requestDetails =
										new SystemRequestDetails().setRequestPartitionId(thePartitionId);
								return patientDao.searchForResourceIds(map, requestDetails);
							});
					for (IIdType filteredId : filteredIds) {
						newPatientIdList.add(
								filteredId.toUnqualifiedVersionless().getValue());
					}
				}
			}
		});
		return newPatientIdList;
	}

	@Nonnull
	private ListMultimap<RequestPartitionId, String> indexPatientIdsByPartition(
			StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			Collection<String> patientResourceIds) {
		return Multimaps.index(patientResourceIds, t -> determinePatientPartition(theStepExecutionDetails, t));
	}

	private RequestPartitionId determinePatientPartition(
			StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails, String thePatientId) {
		return myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(
				theStepExecutionDetails.newSystemRequestDetails(),
				"Patient",
				myFhirContext.getVersion().newIdType(thePatientId));
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

		return params.getResourceTypes().stream()
				.filter(resourceType -> myResourceSupportedSvc.isSupported(resourceType))
				.filter(resourceType -> !resourceTypesToOmit.contains(resourceType))
				.collect(Collectors.toList());
	}
}
