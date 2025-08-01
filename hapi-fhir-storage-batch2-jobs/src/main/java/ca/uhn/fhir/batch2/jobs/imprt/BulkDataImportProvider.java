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
package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.export.BulkDataExportUtil;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ParametersUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BulkDataImportProvider {
	public static final String PARAM_INPUT_FORMAT = "inputFormat";
	public static final String PARAM_INPUT_SOURCE = "inputSource";
	public static final String PARAM_STORAGE_DETAIL = "storageDetail";
	public static final String PARAM_STORAGE_DETAIL_TYPE = "type";
	public static final String PARAM_STORAGE_DETAIL_TYPE_VAL_HTTPS = "https";
	public static final String PARAM_INPUT = "input";
	public static final String PARAM_INPUT_URL = "url";
	public static final String PARAM_STORAGE_DETAIL_CREDENTIAL_HTTP_BASIC = "credentialHttpBasic";
	public static final String PARAM_STORAGE_DETAIL_MAX_BATCH_RESOURCE_COUNT = "maxBatchResourceCount";
	public static final String PARAM_STORAGE_DETAIL_CHUNK_BY_COMPARTMENT_NAME = "chunkByCompartmentName";

	public static final String PARAM_INPUT_TYPE = "type";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataImportProvider.class);

	private IJobCoordinator myJobCoordinator;

	private FhirContext myFhirCtx;

	private IRequestPartitionHelperSvc myRequestPartitionHelperService;

	private volatile List<String> myResourceTypeOrder;

	/**
	 * Constructor
	 */
	public BulkDataImportProvider() {
		super();
	}

	@Autowired
	public void setJobCoordinator(IJobCoordinator theJobCoordinator) {
		myJobCoordinator = theJobCoordinator;
	}

	@Autowired
	public void setFhirContext(FhirContext theCtx) {
		myFhirCtx = theCtx;
	}

	@Autowired
	public void setRequestPartitionHelperService(IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myRequestPartitionHelperService = theRequestPartitionHelperSvc;
	}

	/**
	 * $import operation (Import by Manifest)
	 * <p>
	 * Note that this is a weird operation, defined here: https://github.com/smart-on-fhir/bulk-import/blob/master/import-manifest.md
	 * Per the definition in that spec, this operation isn't actually a FHIR operation using a Parameters resource, but instead uses an
	 * arbitrary JSON document as its payload.
	 * </p>
	 * <p>
	 * We are using a Parameters resource here, so we're not actually technically
	 * compliant with the spec as it is written (2022-02-15). However the text
	 * indicates that there has been a suggestion to use Parameters so this
	 * feels like the right direction.
	 * </p>
	 */
	@Operation(name = JpaConstants.OPERATION_IMPORT, idempotent = false, manualResponse = true)
	public void importByManifest(
			ServletRequestDetails theRequestDetails,
			@ResourceParam IBaseParameters theRequest,
			HttpServletResponse theResponse)
			throws IOException {

		BulkDataExportUtil.validatePreferAsyncHeader(theRequestDetails, JpaConstants.OPERATION_IMPORT);

		BulkImportJobParameters jobParameters = new BulkImportJobParameters();

		String inputFormat = ParametersUtil.getNamedParameterValueAsString(myFhirCtx, theRequest, PARAM_INPUT_FORMAT)
				.orElse("");
		if (!Constants.CT_FHIR_NDJSON.equals(inputFormat)) {
			throw new InvalidRequestException(
					Msg.code(2048) + "Input format must be \"" + Constants.CT_FHIR_NDJSON + "\"");
		}

		Optional<IBase> storageDetailOpt =
				ParametersUtil.getNamedParameter(myFhirCtx, theRequest, PARAM_STORAGE_DETAIL);
		if (storageDetailOpt.isPresent()) {
			IBase storageDetail = storageDetailOpt.get();

			String httpBasicCredential = ParametersUtil.getParameterPartValueAsString(
					myFhirCtx, storageDetail, PARAM_STORAGE_DETAIL_CREDENTIAL_HTTP_BASIC);
			if (isNotBlank(httpBasicCredential)) {
				jobParameters.setHttpBasicCredentials(httpBasicCredential);
			}

			String maximumBatchResourceCount = ParametersUtil.getParameterPartValueAsString(
					myFhirCtx, storageDetail, PARAM_STORAGE_DETAIL_MAX_BATCH_RESOURCE_COUNT);
			if (isNotBlank(maximumBatchResourceCount)) {
				jobParameters.setMaxBatchResourceCount(Integer.parseInt(maximumBatchResourceCount));
			}

			String groupByCompartmentName = ParametersUtil.getParameterPartValueAsString(
					myFhirCtx, storageDetail, PARAM_STORAGE_DETAIL_CHUNK_BY_COMPARTMENT_NAME);
			if (isNotBlank(groupByCompartmentName)) {
				jobParameters.setChunkByCompartmentName(groupByCompartmentName);
			}
		}

		RequestPartitionId partitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForServerOperation(
						theRequestDetails, JpaConstants.OPERATION_IMPORT);
		myRequestPartitionHelperService.validateHasPartitionPermissions(theRequestDetails, "Binary", partitionId);
		jobParameters.setPartitionId(partitionId);

		// Extract all the URLs and order them in the order that is least
		// likely to result in conflict (e.g. Patients before Observations
		// since Observations can reference Patients but not vice versa)
		List<Pair<String, String>> typeAndUrls = new ArrayList<>();
		for (IBase input :
				ParametersUtil.getNamedParameters(myFhirCtx, theRequest, BulkDataImportProvider.PARAM_INPUT)) {
			String type = ParametersUtil.getParameterPartValueAsString(
					myFhirCtx, input, BulkDataImportProvider.PARAM_INPUT_TYPE);
			String url = ParametersUtil.getParameterPartValueAsString(
					myFhirCtx, input, BulkDataImportProvider.PARAM_INPUT_URL);
			ValidateUtil.isNotBlankOrThrowInvalidRequest(type, "Missing type for input");
			ValidateUtil.isNotBlankOrThrowInvalidRequest(url, "Missing url for input");
			Pair<String, String> typeAndUrl = Pair.of(type, url);
			typeAndUrls.add(typeAndUrl);
		}
		ValidateUtil.isTrueOrThrowInvalidRequest(!typeAndUrls.isEmpty(), "No URLs specified");
		List<String> resourceTypeOrder = getResourceTypeOrder();
		typeAndUrls.sort(Comparator.comparing(t -> resourceTypeOrder.indexOf(t.getKey())));

		for (Pair<String, String> next : typeAndUrls) {
			jobParameters.addNdJsonUrl(next.getValue());
		}

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BulkImportAppCtx.JOB_BULK_IMPORT_PULL);
		request.setParameters(jobParameters);

		ourLog.info("Requesting Bulk Import Job ($import by Manifest) with {} urls", typeAndUrls.size());

		Batch2JobStartResponse jobStartResponse = myJobCoordinator.startInstance(theRequestDetails, request);
		String jobId = jobStartResponse.getInstanceId();

		IBaseOperationOutcome response = OperationOutcomeUtil.newInstance(myFhirCtx);
		OperationOutcomeUtil.addIssue(
				myFhirCtx,
				response,
				"information",
				"Bulk import job has been submitted with ID: " + jobId,
				null,
				"informational");
		OperationOutcomeUtil.addIssue(
				myFhirCtx,
				response,
				"information",
				"Use the following URL to poll for job status: " + createPollLocationLink(theRequestDetails, jobId),
				null,
				"informational");

		theResponse.setStatus(202);
		theResponse.setContentType(Constants.CT_FHIR_JSON + Constants.CHARSET_UTF8_CTSUFFIX);
		writePollingLocationToResponseHeaders(theRequestDetails, jobId);
		myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(response, theResponse.getWriter());
		theResponse.getWriter().close();
	}

	/**
	 * $import-poll-status
	 */
	@Operation(name = JpaConstants.OPERATION_IMPORT_POLL_STATUS, manualResponse = true, idempotent = true)
	public void importPollStatus(
			@OperationParam(name = JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID, typeName = "string", min = 0, max = 1)
					IPrimitiveType<String> theJobId,
			ServletRequestDetails theRequestDetails)
			throws IOException {
		HttpServletResponse response = theRequestDetails.getServletResponse();
		theRequestDetails.getServer().addHeadersToResponse(response);
		JobInstance instance = myJobCoordinator.getInstance(theJobId.getValueAsString());
		BulkImportJobParameters parameters = instance.getParameters(BulkImportJobParameters.class);
		if (parameters != null && parameters.getPartitionId() != null) {
			// Determine and validate permissions for partition (if needed)
			RequestPartitionId partitionId =
					myRequestPartitionHelperService.determineReadPartitionForRequestForServerOperation(
							theRequestDetails, JpaConstants.OPERATION_IMPORT);
			myRequestPartitionHelperService.validateHasPartitionPermissions(theRequestDetails, "Binary", partitionId);
			if (!partitionId.equals(parameters.getPartitionId())) {
				throw new InvalidRequestException(
						Msg.code(2310) + "Invalid partition in request for Job ID " + theJobId);
			}
		}

		ourLog.debug("Client is polling for $import status: {}", instance.getStatus());
		switch (instance.getStatus()) {
			case QUEUED: {
				response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
				String msg = "Job was created at " + renderTime(instance.getCreateTime()) + " and is in "
						+ instance.getStatus() + " state.";
				response.addHeader(Constants.HEADER_X_PROGRESS, msg);
				response.addHeader(Constants.HEADER_RETRY_AFTER, "120");
				streamOperationOutcomeResponse(response, "information", msg);
				break;
			}
			case FINALIZE:
			case ERRORED:
			case IN_PROGRESS: {
				response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
				String msg = "Job was created at " + renderTime(instance.getCreateTime()) + ", started at "
						+ renderTime(instance.getStartTime()) + " and is in "
						+ instance.getStatus() + " state. Current completion: "
						+ new DecimalFormat("0.0").format(100.0 * instance.getProgress())
						+ "% and ETA is "
						+ instance.getEstimatedTimeRemaining();
				response.addHeader(Constants.HEADER_X_PROGRESS, msg);
				response.addHeader(Constants.HEADER_RETRY_AFTER, "120");
				streamOperationOutcomeResponse(response, "information", msg);
				break;
			}
			case COMPLETED: {
				response.setStatus(Constants.STATUS_HTTP_200_OK);
				String msg = instance.getReport();
				streamOperationOutcomeResponse(response, "information", msg);
				break;
			}
			case FAILED: {
				response.setStatus(Constants.STATUS_HTTP_500_INTERNAL_ERROR);
				String msg = "Job is in " + instance.getStatus() + " state with " + instance.getErrorCount()
						+ " error count. Last error: " + instance.getErrorMessage();
				String report = instance.getReport();
				streamOperationOutcomeResponse(response, "error", msg, report);
				break;
			}
			case CANCELLED: {
				response.setStatus(Constants.STATUS_HTTP_404_NOT_FOUND);
				String msg = "Job was cancelled.";
				streamOperationOutcomeResponse(response, "information", msg);
				break;
			}
			default: {
				ourLog.warn("Don't know how to handle status {}", instance.getStatus());
			}
		}
	}

	private void streamOperationOutcomeResponse(HttpServletResponse response, String theSeverity, String... theMessages)
			throws IOException {
		response.setContentType(Constants.CT_FHIR_JSON);
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myFhirCtx);
		for (String message : theMessages) {
			OperationOutcomeUtil.addIssue(myFhirCtx, oo, theSeverity, message, null, null);
		}
		myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(oo, response.getWriter());
		response.getWriter().close();
	}

	public void writePollingLocationToResponseHeaders(ServletRequestDetails theRequestDetails, String theJobId) {
		String pollLocation = createPollLocationLink(theRequestDetails, theJobId);
		pollLocation = UrlUtil.sanitizeHeaderValue(pollLocation);

		HttpServletResponse response = theRequestDetails.getServletResponse();
		// Add standard headers
		theRequestDetails.getServer().addHeadersToResponse(response);
		// Successful 202 Accepted
		response.addHeader(Constants.HEADER_CONTENT_LOCATION, pollLocation);
		response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
	}

	@Nonnull
	private String createPollLocationLink(ServletRequestDetails theRequestDetails, String theJobId) {
		String serverBase = StringUtils.removeEnd(theRequestDetails.getServerBaseForRequest(), "/");
		return serverBase + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?"
				+ JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + theJobId;
	}

	private synchronized List<String> getResourceTypeOrder() {
		List<String> retVal = myResourceTypeOrder;
		if (retVal == null) {
			retVal = ResourceOrderUtil.getResourceOrder(myFhirCtx);
			myResourceTypeOrder = retVal;
		}
		return retVal;
	}

	private static String renderTime(Date theTime) {
		if (theTime == null) {
			return "(null)";
		}
		return new InstantType(theTime).getValueAsString();
	}
}
