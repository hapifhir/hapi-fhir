/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Parameters;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters.ExportStyle;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public class BulkDataExportProvider {

	private static final Logger ourLog = getLogger(BulkDataExportProvider.class);

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperService;

	/**
	 * Constructor
	 */
	public BulkDataExportProvider() {
		super();
	}

	/**
	 * $export
	 */
	@Operation(
			name = ProviderConstants.OPERATION_EXPORT,
			global = false /* set to true once we can handle this */,
			manualResponse = true,
			idempotent = true,
			canonicalUrl = "http://hl7.org/fhir/uv/bulkdata/OperationDefinition/export")
	public void export(
			@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theType,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant")
					IPrimitiveType<Date> theSince,
			@OperationParam(
							name = JpaConstants.PARAM_EXPORT_TYPE_FILTER,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypeFilter,
			@OperationParam(
							name = JpaConstants.PARAM_EXPORT_TYPE_POST_FETCH_FILTER_URL,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypePostFetchFilterUrl,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_IDENTIFIER, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theExportId,
			ServletRequestDetails theRequestDetails) {
		// JPA export provider
		BulkDataExportUtil.validatePreferAsyncHeader(theRequestDetails, ProviderConstants.OPERATION_EXPORT);

		BulkExportJobParameters bulkExportJobParameters = new BulkExportJobParametersBuilder()
				.outputFormat(theOutputFormat)
				.resourceTypes(theType)
				.since(theSince)
				.filters(theTypeFilter)
				.exportIdentifier(theExportId)
				.exportStyle(ExportStyle.SYSTEM)
				.postFetchFilterUrl(theTypePostFetchFilterUrl)
				.build();

		getBulkDataExportJobService().startJob(theRequestDetails, bulkExportJobParameters);
	}

	/**
	 * Group/[id]/$export
	 */
	@Operation(
			name = ProviderConstants.OPERATION_EXPORT,
			manualResponse = true,
			idempotent = true,
			typeName = "Group",
			canonicalUrl = "http://hl7.org/fhir/uv/bulkdata/OperationDefinition/group-export")
	public void groupExport(
			@IdParam IIdType theIdParam,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theType,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant")
					IPrimitiveType<Date> theSince,
			@OperationParam(
							name = JpaConstants.PARAM_EXPORT_TYPE_FILTER,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypeFilter,
			@OperationParam(
							name = JpaConstants.PARAM_EXPORT_TYPE_POST_FETCH_FILTER_URL,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypePostFetchFilterUrl,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_MDM, min = 0, max = 1, typeName = "boolean")
					IPrimitiveType<Boolean> theMdm,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_IDENTIFIER, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theExportIdentifier,
			ServletRequestDetails theRequestDetails) {
		ourLog.debug("Received Group Bulk Export Request for Group {}", theIdParam);
		ourLog.debug("_type={}", theType);
		ourLog.debug("_since={}", theSince);
		ourLog.debug("_typeFilter={}", theTypeFilter);
		ourLog.debug("_mdm={}", theMdm);

		BulkDataExportUtil.validatePreferAsyncHeader(theRequestDetails, ProviderConstants.OPERATION_EXPORT);

		// verify the Group exists before starting the job
		getBulkDataExportSupport().validateTargetsExists(theRequestDetails, "Group", List.of(theIdParam));

		final BulkExportJobParameters bulkExportJobParameters = new BulkExportJobParametersBuilder()
				.outputFormat(theOutputFormat)
				.resourceTypes(theType)
				.since(theSince)
				.filters(theTypeFilter)
				.exportIdentifier(theExportIdentifier)
				.exportStyle(ExportStyle.GROUP)
				.postFetchFilterUrl(theTypePostFetchFilterUrl)
				.groupId(theIdParam)
				.expandMdm(theMdm)
				.build();

		getBulkDataExportSupport().validateOrDefaultResourceTypesForGroupBulkExport(bulkExportJobParameters);
		getBulkDataExportJobService().startJob(theRequestDetails, bulkExportJobParameters);
	}

	@VisibleForTesting
	Set<String> getPatientCompartmentResources(FhirContext theFhirContext) {
		return getBulkDataExportSupport().getPatientCompartmentResources(theFhirContext);
	}

	/**
	 * Patient/$export
	 */
	@Operation(
			name = ProviderConstants.OPERATION_EXPORT,
			manualResponse = true,
			idempotent = true,
			typeName = "Patient",
			canonicalUrl = "http://hl7.org/fhir/uv/bulkdata/OperationDefinition/patient-export")
	public void patientExport(
			@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theType,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant")
					IPrimitiveType<Date> theSince,
			@OperationParam(
							name = JpaConstants.PARAM_EXPORT_TYPE_FILTER,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypeFilter,
			@OperationParam(
							name = JpaConstants.PARAM_EXPORT_TYPE_POST_FETCH_FILTER_URL,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypePostFetchFilterUrl,
			@OperationParam(
							name = JpaConstants.PARAM_EXPORT_PATIENT,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> thePatient,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_IDENTIFIER, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theExportIdentifier,
			ServletRequestDetails theRequestDetails) {

		List<IPrimitiveType<String>> patientIds = thePatient != null ? thePatient : new ArrayList<>();

		doPatientExport(
				theRequestDetails,
				theOutputFormat,
				theType,
				theSince,
				theExportIdentifier,
				theTypeFilter,
				theTypePostFetchFilterUrl,
				patientIds);
	}

	/**
	 * Patient/[id]/$export
	 */
	@Operation(
			name = ProviderConstants.OPERATION_EXPORT,
			manualResponse = true,
			idempotent = true,
			typeName = "Patient")
	public void patientInstanceExport(
			@IdParam IIdType theIdParam,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theOutputFormat,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theType,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant")
					IPrimitiveType<Date> theSince,
			@OperationParam(
							name = JpaConstants.PARAM_EXPORT_TYPE_FILTER,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypeFilter,
			@OperationParam(
							name = JpaConstants.PARAM_EXPORT_TYPE_POST_FETCH_FILTER_URL,
							min = 0,
							max = OperationParam.MAX_UNLIMITED,
							typeName = "string")
					List<IPrimitiveType<String>> theTypePostFetchFilterUrl,
			@OperationParam(name = JpaConstants.PARAM_EXPORT_IDENTIFIER, min = 0, max = 1, typeName = "string")
					IPrimitiveType<String> theExportIdentifier,
			ServletRequestDetails theRequestDetails) {

		// call the type-level export to ensure spec compliance
		patientExport(
				theOutputFormat,
				theType,
				theSince,
				theTypeFilter,
				theTypePostFetchFilterUrl,
				List.of(theIdParam),
				theExportIdentifier,
				theRequestDetails);
	}

	private void doPatientExport(
			ServletRequestDetails theRequestDetails,
			IPrimitiveType<String> theOutputFormat,
			IPrimitiveType<String> theType,
			IPrimitiveType<Date> theSince,
			IPrimitiveType<String> theExportIdentifier,
			List<IPrimitiveType<String>> theTypeFilter,
			List<IPrimitiveType<String>> theTypePostFetchFilterUrl,
			List<IPrimitiveType<String>> thePatientIds) {
		BulkDataExportUtil.validatePreferAsyncHeader(theRequestDetails, ProviderConstants.OPERATION_EXPORT);

		getBulkDataExportSupport()
				.validateTargetsExists(
						theRequestDetails,
						"Patient",
						thePatientIds.stream()
								.map(c -> new IdType(c.getValue()))
								.collect(Collectors.toList()));

		// set resourceTypes to all patient compartment resources if it is null
		IPrimitiveType<String> resourceTypes = theType == null
				? new StringDt(String.join(",", getBulkDataExportSupport().getPatientCompartmentResources()))
				: theType;

		BulkExportJobParameters bulkExportJobParameters = new BulkExportJobParametersBuilder()
				.outputFormat(theOutputFormat)
				.resourceTypes(resourceTypes)
				.since(theSince)
				.filters(theTypeFilter)
				.exportIdentifier(theExportIdentifier)
				.exportStyle(ExportStyle.PATIENT)
				.postFetchFilterUrl(theTypePostFetchFilterUrl)
				.patientIds(thePatientIds)
				.build();

		getBulkDataExportSupport()
				.validateResourceTypesAllContainPatientSearchParams(bulkExportJobParameters.getResourceTypes());

		getBulkDataExportJobService().startJob(theRequestDetails, bulkExportJobParameters);
	}

	/**
	 * $export-poll-status
	 */
	@SuppressWarnings("unchecked")
	@Operation(
			name = ProviderConstants.OPERATION_EXPORT_POLL_STATUS,
			manualResponse = true,
			idempotent = true,
			deleteEnabled = true)
	public void exportPollStatus(
			@OperationParam(name = JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID, typeName = "string", min = 0, max = 1)
					IPrimitiveType<String> theJobId,
			ServletRequestDetails theRequestDetails)
			throws IOException {
		HttpServletResponse response = theRequestDetails.getServletResponse();
		theRequestDetails.getServer().addHeadersToResponse(response);

		// When export-poll-status through POST
		// Get theJobId from the request details
		if (theJobId == null) {
			Parameters parameters = (Parameters) theRequestDetails.getResource();
			Parameters.ParametersParameterComponent parameter = parameters.getParameter().stream()
					.filter(param -> param.getName().equals(JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID))
					.findFirst()
					.orElseThrow(() -> new InvalidRequestException(Msg.code(2227)
							+ "$export-poll-status requires a job ID, please provide the value of target jobId."));
			theJobId = (IPrimitiveType<String>) parameter.getValue();
		}

		JobInstance info = myJobCoordinator.getInstance(theJobId.getValueAsString());

		BulkExportJobParameters parameters = info.getParameters(BulkExportJobParameters.class);
		if (parameters.getPartitionId() != null) {
			// Determine and validate permissions for partition (if needed)
			RequestPartitionId partitionId =
					myRequestPartitionHelperService.determineReadPartitionForRequestForServerOperation(
							theRequestDetails, ProviderConstants.OPERATION_EXPORT_POLL_STATUS);
			myRequestPartitionHelperService.validateHasPartitionPermissions(theRequestDetails, "Binary", partitionId);
			if (!parameters.getPartitionId().equals(partitionId)) {
				throw new InvalidRequestException(
						Msg.code(2304) + "Invalid partition in request for Job ID " + theJobId);
			}
		}

		switch (info.getStatus()) {
			case COMPLETED:
				if (theRequestDetails.getRequestType() == RequestTypeEnum.DELETE) {
					handleDeleteRequest(theJobId, response, info.getStatus());
				} else {
					response.setStatus(Constants.STATUS_HTTP_200_OK);
					response.setContentType(Constants.CT_JSON);

					// Create a JSON response
					BulkExportResponseJson bulkResponseDocument = new BulkExportResponseJson();
					bulkResponseDocument.setTransactionTime(info.getEndTime()); // completed

					bulkResponseDocument.setRequiresAccessToken(true);

					String report = info.getReport();
					if (isEmpty(report)) {
						// this should never happen, but just in case...
						ourLog.error("No report for completed bulk export job.");
						response.getWriter().close();
					} else {
						BulkExportJobResults results = JsonUtil.deserialize(report, BulkExportJobResults.class);
						bulkResponseDocument.setMsg(results.getReportMsg());
						bulkResponseDocument.setRequest(results.getOriginalRequestUrl());

						String serverBase = BulkDataExportUtil.getServerBase(theRequestDetails);

						// an output is required, even if empty, according to HL7 FHIR IG
						bulkResponseDocument.getOutput();

						for (Map.Entry<String, List<String>> entrySet :
								results.getResourceTypeToBinaryIds().entrySet()) {
							String resourceType = entrySet.getKey();
							List<String> binaryIds = entrySet.getValue();
							for (String binaryId : binaryIds) {
								IIdType iId = new IdType(binaryId);
								String nextUrl = serverBase + "/"
										+ iId.toUnqualifiedVersionless().getValue();
								bulkResponseDocument
										.addOutput()
										.setType(resourceType)
										.setUrl(nextUrl);
							}
						}
						JsonUtil.serialize(bulkResponseDocument, response.getWriter());
						response.getWriter().close();
					}
				}
				break;
			case FAILED:
				response.setStatus(Constants.STATUS_HTTP_500_INTERNAL_ERROR);
				response.setContentType(Constants.CT_FHIR_JSON);

				// Create an OperationOutcome response
				IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myFhirContext);

				OperationOutcomeUtil.addIssue(myFhirContext, oo, "error", info.getErrorMessage(), null, null);
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(oo, response.getWriter());
				response.getWriter().close();
				break;
			default:
				// Deliberate fall through
				ourLog.warn(
						"Unrecognized status encountered: {}. Treating as BUILDING/SUBMITTED",
						info.getStatus().name());
				//noinspection fallthrough
			case FINALIZE:
			case QUEUED:
			case IN_PROGRESS:
			case CANCELLED:
			case ERRORED:
				if (theRequestDetails.getRequestType() == RequestTypeEnum.DELETE) {
					handleDeleteRequest(theJobId, response, info.getStatus());
				} else {
					response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
					String dateString = getTransitionTimeOfJobInfo(info);
					response.addHeader(
							Constants.HEADER_X_PROGRESS,
							"Build in progress - Status set to " + info.getStatus() + " at " + dateString);
					response.addHeader(Constants.HEADER_RETRY_AFTER, "120");
				}
				break;
		}
	}

	private void handleDeleteRequest(
			IPrimitiveType<String> theJobId, HttpServletResponse response, StatusEnum theOrigStatus)
			throws IOException {
		IBaseOperationOutcome outcome = OperationOutcomeUtil.newInstance(myFhirContext);
		JobOperationResultJson resultMessage = myJobCoordinator.cancelInstance(theJobId.getValueAsString());
		if (theOrigStatus.equals(StatusEnum.COMPLETED)) {
			response.setStatus(Constants.STATUS_HTTP_404_NOT_FOUND);
			OperationOutcomeUtil.addIssue(
					myFhirContext,
					outcome,
					"error",
					"Job instance <" + theJobId.getValueAsString()
							+ "> was already cancelled or has completed.  Nothing to do.",
					null,
					null);
		} else {
			response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
			OperationOutcomeUtil.addIssue(
					myFhirContext, outcome, "information", resultMessage.getMessage(), null, "informational");
		}
		myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(outcome, response.getWriter());
		response.getWriter().close();
	}

	private String getTransitionTimeOfJobInfo(JobInstance theInfo) {
		if (theInfo.getEndTime() != null) {
			return new InstantType(theInfo.getEndTime()).getValueAsString();
		} else if (theInfo.getStartTime() != null) {
			return new InstantType(theInfo.getStartTime()).getValueAsString();
		} else {
			// safety check
			return "";
		}
	}

	@VisibleForTesting
	public void setStorageSettings(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	@VisibleForTesting
	public void setDaoRegistry(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	// Do not use this variable directly, use getBulkDataExportJobService() instead
	private BulkExportJobService myBulkExportJobService;

	private BulkExportJobService getBulkDataExportJobService() {
		if (myBulkExportJobService == null) {
			myBulkExportJobService = new BulkExportJobService(
					myInterceptorBroadcaster,
					myJobCoordinator,
					myDaoRegistry,
					myRequestPartitionHelperService,
					myStorageSettings);
		}
		return myBulkExportJobService;
	}

	// Do not use this variable directly, use getBulkDataExportSupport() instead
	private BulkDataExportSupport myBulkDataExportSupport;

	private BulkDataExportSupport getBulkDataExportSupport() {
		if (myBulkDataExportSupport == null) {
			myBulkDataExportSupport =
					new BulkDataExportSupport(myFhirContext, myDaoRegistry, myRequestPartitionHelperService);
		}
		return myBulkDataExportSupport;
	}
}
