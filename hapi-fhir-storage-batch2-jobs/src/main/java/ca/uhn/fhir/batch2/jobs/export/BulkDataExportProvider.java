/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.ArrayUtil;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters.ExportStyle;
import static ca.uhn.fhir.util.DatatypeUtil.toStringValue;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public class BulkDataExportProvider {
	public static final String FARM_TO_TABLE_TYPE_FILTER_REGEX = "(?:,)(?=[A-Z][a-z]+\\?)";
	public static final List<String> PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES =
			List.of("Practitioner", "Organization");
	/**
	 * Bulk data $export does not include the Binary type
	 */
	public static final String UNSUPPORTED_BINARY_TYPE = "Binary";

	private static final Logger ourLog = getLogger(BulkDataExportProvider.class);
	private static final Set<FhirVersionEnum> PATIENT_COMPARTMENT_FHIR_VERSIONS_SUPPORT_DEVICE = Set.of(
			FhirVersionEnum.DSTU2,
			FhirVersionEnum.DSTU2_1,
			FhirVersionEnum.DSTU2_HL7ORG,
			FhirVersionEnum.DSTU3,
			FhirVersionEnum.R4,
			FhirVersionEnum.R4B);

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	private Set<String> myCompartmentResources;

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
		validatePreferAsyncHeader(theRequestDetails, ProviderConstants.OPERATION_EXPORT);

		BulkExportJobParameters BulkExportJobParameters = buildSystemBulkExportOptions(
				theOutputFormat, theType, theSince, theTypeFilter, theExportId, theTypePostFetchFilterUrl);

		startJob(theRequestDetails, BulkExportJobParameters);
	}

	private void startJob(ServletRequestDetails theRequestDetails, BulkExportJobParameters theOptions) {
		// parameter massaging
		expandParameters(theRequestDetails, theOptions);

		// permission check
		HookParams initiateBulkExportHookParams = (new HookParams())
				.add(BulkExportJobParameters.class, theOptions)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		CompositeInterceptorBroadcaster.doCallHooks(
				this.myInterceptorBroadcaster,
				theRequestDetails,
				Pointcut.STORAGE_INITIATE_BULK_EXPORT,
				initiateBulkExportHookParams);

		// get cache boolean
		boolean useCache = shouldUseCache(theRequestDetails);

		// start job
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setParameters(theOptions);
		startRequest.setUseCache(useCache);
		startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		Batch2JobStartResponse response = myJobCoordinator.startInstance(theRequestDetails, startRequest);

		writePollingLocationToResponseHeaders(theRequestDetails, response.getInstanceId());
	}

	/**
	 * This method changes any parameters (limiting the _type parameter, for instance)
	 * so that later steps in the export do not have to handle them.
	 */
	private void expandParameters(ServletRequestDetails theRequestDetails, BulkExportJobParameters theOptions) {
		// Set the original request URL as part of the job information, as this is used in the poll-status-endpoint, and
		// is needed for the report.
		theOptions.setOriginalRequestUrl(theRequestDetails.getCompleteUrl());

		// If no _type parameter is provided, default to all resource types except Binary
		if (theOptions.getResourceTypes().isEmpty()) {
			List<String> resourceTypes = new ArrayList<>(myDaoRegistry.getRegisteredDaoTypes());
			resourceTypes.remove(UNSUPPORTED_BINARY_TYPE);
			theOptions.setResourceTypes(resourceTypes);
		}

		// Determine and validate partition permissions (if needed).
		RequestPartitionId partitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForServerOperation(
						theRequestDetails, ProviderConstants.OPERATION_EXPORT);
		myRequestPartitionHelperService.validateHasPartitionPermissions(theRequestDetails, "Binary", partitionId);
		theOptions.setPartitionId(partitionId);

		// call hook so any other parameter manipulation can be done
		HookParams preInitiateBulkExportHookParams = new HookParams();
		preInitiateBulkExportHookParams.add(BulkExportJobParameters.class, theOptions);
		preInitiateBulkExportHookParams.add(RequestDetails.class, theRequestDetails);
		preInitiateBulkExportHookParams.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		CompositeInterceptorBroadcaster.doCallHooks(
				myInterceptorBroadcaster,
				theRequestDetails,
				Pointcut.STORAGE_PRE_INITIATE_BULK_EXPORT,
				preInitiateBulkExportHookParams);
	}

	private boolean shouldUseCache(ServletRequestDetails theRequestDetails) {
		CacheControlDirective cacheControlDirective =
				new CacheControlDirective().parse(theRequestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL));
		return myStorageSettings.getEnableBulkExportJobReuse() && !cacheControlDirective.isNoCache();
	}

	private String getServerBase(ServletRequestDetails theRequestDetails) {
		return StringUtils.removeEnd(theRequestDetails.getServerBaseForRequest(), "/");
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

		validatePreferAsyncHeader(theRequestDetails, ProviderConstants.OPERATION_EXPORT);

		// verify the Group exists before starting the job
		validateTargetsExists(theRequestDetails, "Group", List.of(theIdParam));

		BulkExportJobParameters BulkExportJobParameters = buildGroupBulkExportOptions(
				theOutputFormat,
				theType,
				theSince,
				theTypeFilter,
				theIdParam,
				theMdm,
				theExportIdentifier,
				theTypePostFetchFilterUrl);

		if (isNotEmpty(BulkExportJobParameters.getResourceTypes())) {
			validateResourceTypesAllContainPatientSearchParams(BulkExportJobParameters.getResourceTypes());
		} else {
			// all patient resource types
			Set<String> groupTypes = new HashSet<>(getPatientCompartmentResources());

			// Add the forward reference resource types from the patients, e.g. Practitioner, Organization
			groupTypes.addAll(PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES);

			groupTypes.removeIf(t -> !myDaoRegistry.isResourceTypeSupported(t));
			BulkExportJobParameters.setResourceTypes(groupTypes);
		}

		startJob(theRequestDetails, BulkExportJobParameters);
	}

	/**
	 * Throw ResourceNotFound if the target resources don't exist.
	 * Otherwise, we start a bulk-export job which then fails, reporting a 500.
	 *
	 * @param theRequestDetails     the caller details
	 * @param theTargetResourceName the type of the target
	 * @param theIdParams           the id(s) to verify exist
	 */
	private void validateTargetsExists(
			RequestDetails theRequestDetails, String theTargetResourceName, Iterable<IIdType> theIdParams) {
		if (theIdParams.iterator().hasNext()) {
			RequestPartitionId partitionId = myRequestPartitionHelperService.determineReadPartitionForRequestForRead(
					theRequestDetails,
					theTargetResourceName,
					theIdParams.iterator().next());
			SystemRequestDetails requestDetails = new SystemRequestDetails().setRequestPartitionId(partitionId);
			for (IIdType nextId : theIdParams) {
				myDaoRegistry.getResourceDao(theTargetResourceName).read(nextId, requestDetails);
			}
		}
	}

	private void validateResourceTypesAllContainPatientSearchParams(Collection<String> theResourceTypes) {
		if (theResourceTypes != null) {
			List<String> badResourceTypes = theResourceTypes.stream()
					.filter(resourceType ->
							!PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(resourceType))
					.filter(resourceType -> !getPatientCompartmentResources().contains(resourceType))
					.collect(Collectors.toList());

			if (!badResourceTypes.isEmpty()) {
				throw new InvalidRequestException(Msg.code(512)
						+ String.format(
								"Resource types [%s] are invalid for this type of export, as they do not contain search parameters that refer to patients.",
								String.join(",", badResourceTypes)));
			}
		}
	}

	private Set<String> getPatientCompartmentResources() {
		return getPatientCompartmentResources(myFhirContext);
	}

	@VisibleForTesting
	Set<String> getPatientCompartmentResources(FhirContext theFhirContext) {
		if (myCompartmentResources == null) {
			myCompartmentResources =
					new HashSet<>(SearchParameterUtil.getAllResourceTypesThatAreInPatientCompartment(theFhirContext));
			if (isDeviceResourceSupportedForPatientCompartmentForFhirVersion(
					theFhirContext.getVersion().getVersion())) {
				myCompartmentResources.add("Device");
			}
		}
		return myCompartmentResources;
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
		validatePreferAsyncHeader(theRequestDetails, ProviderConstants.OPERATION_EXPORT);

		validateTargetsExists(
				theRequestDetails,
				"Patient",
				thePatientIds.stream().map(c -> new IdType(c.getValue())).collect(Collectors.toList()));

		BulkExportJobParameters BulkExportJobParameters = buildPatientBulkExportOptions(
				theOutputFormat,
				theType,
				theSince,
				theTypeFilter,
				theExportIdentifier,
				thePatientIds,
				theTypePostFetchFilterUrl);
		validateResourceTypesAllContainPatientSearchParams(BulkExportJobParameters.getResourceTypes());

		startJob(theRequestDetails, BulkExportJobParameters);
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

						String serverBase = getServerBase(theRequestDetails);

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

	private BulkExportJobParameters buildSystemBulkExportOptions(
			IPrimitiveType<String> theOutputFormat,
			IPrimitiveType<String> theType,
			IPrimitiveType<Date> theSince,
			List<IPrimitiveType<String>> theTypeFilter,
			IPrimitiveType<String> theExportId,
			List<IPrimitiveType<String>> theTypePostFetchFilterUrl) {
		return buildBulkExportJobParameters(
				theOutputFormat,
				theType,
				theSince,
				theTypeFilter,
				theExportId,
				BulkExportJobParameters.ExportStyle.SYSTEM,
				theTypePostFetchFilterUrl);
	}

	private BulkExportJobParameters buildGroupBulkExportOptions(
			IPrimitiveType<String> theOutputFormat,
			IPrimitiveType<String> theType,
			IPrimitiveType<Date> theSince,
			List<IPrimitiveType<String>> theTypeFilter,
			IIdType theGroupId,
			IPrimitiveType<Boolean> theExpandMdm,
			IPrimitiveType<String> theExportId,
			List<IPrimitiveType<String>> theTypePostFetchFilterUrl) {
		BulkExportJobParameters BulkExportJobParameters = buildBulkExportJobParameters(
				theOutputFormat,
				theType,
				theSince,
				theTypeFilter,
				theExportId,
				ExportStyle.GROUP,
				theTypePostFetchFilterUrl);
		BulkExportJobParameters.setGroupId(toStringValue(theGroupId));

		boolean mdm = false;
		if (theExpandMdm != null) {
			mdm = theExpandMdm.getValue();
		}
		BulkExportJobParameters.setExpandMdm(mdm);

		return BulkExportJobParameters;
	}

	private BulkExportJobParameters buildPatientBulkExportOptions(
			IPrimitiveType<String> theOutputFormat,
			IPrimitiveType<String> theType,
			IPrimitiveType<Date> theSince,
			List<IPrimitiveType<String>> theTypeFilter,
			IPrimitiveType<String> theExportIdentifier,
			List<IPrimitiveType<String>> thePatientIds,
			List<IPrimitiveType<String>> theTypePostFetchFilterUrl) {
		IPrimitiveType<String> type = theType;
		if (type == null) {
			// set type to all patient compartment resources if it is null
			type = new StringDt(String.join(",", getPatientCompartmentResources()));
		}
		BulkExportJobParameters BulkExportJobParameters = buildBulkExportJobParameters(
				theOutputFormat,
				type,
				theSince,
				theTypeFilter,
				theExportIdentifier,
				ExportStyle.PATIENT,
				theTypePostFetchFilterUrl);
		if (thePatientIds != null) {
			BulkExportJobParameters.setPatientIds(
					thePatientIds.stream().map(IPrimitiveType::getValueAsString).collect(Collectors.toSet()));
		}
		return BulkExportJobParameters;
	}

	private BulkExportJobParameters buildBulkExportJobParameters(
			IPrimitiveType<String> theOutputFormat,
			IPrimitiveType<String> theType,
			IPrimitiveType<Date> theSince,
			List<IPrimitiveType<String>> theTypeFilter,
			IPrimitiveType<String> theExportIdentifier,
			BulkExportJobParameters.ExportStyle theExportStyle,
			List<IPrimitiveType<String>> theTypePostFetchFilterUrl) {
		String outputFormat = theOutputFormat != null ? theOutputFormat.getValueAsString() : Constants.CT_FHIR_NDJSON;

		Set<String> resourceTypes = null;
		if (theType != null) {
			resourceTypes = ArrayUtil.commaSeparatedListToCleanSet(theType.getValueAsString());
		}

		Date since = null;
		if (theSince != null) {
			since = theSince.getValue();
		}
		String exportIdentifier = null;
		if (theExportIdentifier != null) {
			exportIdentifier = theExportIdentifier.getValueAsString();
		}

		Set<String> typeFilters = splitTypeFilters(theTypeFilter);
		Set<String> typePostFetchFilterUrls = splitTypeFilters(theTypePostFetchFilterUrl);

		BulkExportJobParameters BulkExportJobParameters = new BulkExportJobParameters();
		BulkExportJobParameters.setFilters(typeFilters);
		BulkExportJobParameters.setPostFetchFilterUrls(typePostFetchFilterUrls);
		BulkExportJobParameters.setExportStyle(theExportStyle);
		BulkExportJobParameters.setExportIdentifier(exportIdentifier);
		BulkExportJobParameters.setSince(since);
		BulkExportJobParameters.setResourceTypes(resourceTypes);
		BulkExportJobParameters.setOutputFormat(outputFormat);
		return BulkExportJobParameters;
	}

	public void writePollingLocationToResponseHeaders(ServletRequestDetails theRequestDetails, String theInstanceId) {
		String serverBase = getServerBase(theRequestDetails);
		if (serverBase == null) {
			throw new InternalErrorException(Msg.code(2136) + "Unable to get the server base.");
		}
		String pollLocation = serverBase + "/" + ProviderConstants.OPERATION_EXPORT_POLL_STATUS + "?"
				+ JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + theInstanceId;
		pollLocation = UrlUtil.sanitizeHeaderValue(pollLocation);

		HttpServletResponse response = theRequestDetails.getServletResponse();

		// Add standard headers
		theRequestDetails.getServer().addHeadersToResponse(response);

		// Successful 202 Accepted
		response.addHeader(Constants.HEADER_CONTENT_LOCATION, pollLocation);
		response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
	}

	private Set<String> splitTypeFilters(List<IPrimitiveType<String>> theTypeFilter) {
		if (theTypeFilter == null) {
			return null;
		}

		Set<String> retVal = new HashSet<>();

		for (IPrimitiveType<String> next : theTypeFilter) {
			String typeFilterString = next.getValueAsString();
			Arrays.stream(typeFilterString.split(FARM_TO_TABLE_TYPE_FILTER_REGEX))
					.filter(StringUtils::isNotBlank)
					.forEach(retVal::add);
		}

		return retVal;
	}

	@VisibleForTesting
	public void setStorageSettings(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	@VisibleForTesting
	public void setDaoRegistry(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	public static void validatePreferAsyncHeader(ServletRequestDetails theRequestDetails, String theOperationName) {
		String preferHeader = theRequestDetails.getHeader(Constants.HEADER_PREFER);
		PreferHeader prefer = RestfulServerUtils.parsePreferHeader(null, preferHeader);
		if (!prefer.getRespondAsync()) {
			throw new InvalidRequestException(Msg.code(513) + "Must request async processing for " + theOperationName);
		}
	}

	private static boolean isDeviceResourceSupportedForPatientCompartmentForFhirVersion(
			FhirVersionEnum theFhirVersionEnum) {
		return PATIENT_COMPARTMENT_FHIR_VERSIONS_SUPPORT_DEVICE.contains(theFhirVersionEnum);
	}
}
