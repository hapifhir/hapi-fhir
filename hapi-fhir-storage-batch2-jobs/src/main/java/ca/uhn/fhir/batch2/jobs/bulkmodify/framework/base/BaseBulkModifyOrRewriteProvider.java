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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.dao.BaseTransactionProcessor;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.CanonicalBundleEntry;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Base class for a plain provider which can initiate a Bulk Modify or Bulk Rewrite job.
 */
public abstract class BaseBulkModifyOrRewriteProvider {

	@Autowired
	protected FhirContext myContext;

	@Autowired
	protected IJobCoordinator myJobCoordinator;

	/**
	 * Subclasses should call this method to initiate a new job
	 */
	protected void startJobAndReturnResponse(
			ServletRequestDetails theRequestDetails,
			List<IPrimitiveType<String>> theUrlsToReindex,
			BaseBulkModifyJobParameters theJobParameters)
			throws IOException {
		ServletRequestUtil.validatePreferAsyncHeader(theRequestDetails, getOperationName());
		if (theUrlsToReindex != null) {
			for (IPrimitiveType<String> url : theUrlsToReindex) {
				if (isNotBlank(url.getValueAsString())) {
					theJobParameters.addUrl(url.getValueAsString());
				}
			}
		}

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(getJobId());
		startRequest.setParameters(theJobParameters);

		Batch2JobStartResponse outcome = myJobCoordinator.startInstance(theRequestDetails, startRequest);

		String jobInstanceId = outcome.getInstanceId();

		ServletContext servletContext =
				(ServletContext) theRequestDetails.getAttribute(RestfulServer.SERVLET_CONTEXT_ATTRIBUTE);
		HttpServletRequest servletRequest = theRequestDetails.getServletRequest();
		String baseUrl = theRequestDetails
				.getServer()
				.getServerAddressStrategy()
				.determineServerBase(servletContext, servletRequest);

		StringBuilder pollUrl = new StringBuilder(baseUrl);
		if (!baseUrl.endsWith("/")) {
			pollUrl.append("/");
		}
		pollUrl.append(getOperationPollForStatusStatus());
		pollUrl.append('?');
		pollUrl.append(JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID);
		pollUrl.append('=');
		pollUrl.append(jobInstanceId);

		// Create an OperationOutcome to return
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myContext);
		String message = getOperationName() + " job has been accepted";
		String severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
		String code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
		OperationOutcomeUtil.addIssue(myContext, oo, severity, message, null, code);
		RestfulServerUtils.ResponseEncoding encoding =
				RestfulServerUtils.determineResponseEncodingWithDefault(theRequestDetails);

		// Provide a response
		HttpServletResponse servletResponse = theRequestDetails.getServletResponse();
		servletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
		servletResponse.addHeader(Constants.HEADER_CONTENT_LOCATION, pollUrl.toString());

		servletResponse.setContentType(encoding.getContentType());
		servletResponse.setCharacterEncoding(Constants.CHARSET_NAME_UTF8);
		writeResponse(servletResponse, encoding, oo);
	}

	/**
	 * Subclasses should call this method to poll for job status
	 */
	protected void pollForJobStatus(ServletRequestDetails theRequestDetails, IPrimitiveType<String> theJobId)
			throws IOException {
		ValidateUtil.isTrueOrThrowInvalidRequest(theJobId != null && theJobId.hasValue(), "Missing job id");

		JobInstance instance;
		try {
			instance = myJobCoordinator.getInstance(theJobId.getValue());
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(
					Msg.code(2787) + "Invalid/unknown job ID: " + UrlUtil.sanitizeUrlPart(theJobId.getValue()));
		}

		ValidateUtil.isTrueOrThrowInvalidRequest(
				instance.getJobDefinitionId().equals(getJobId()),
				"Job ID does not correspond to a " + getOperationName() + " job");

		int status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		String message = "";
		String severity = "";
		String code = "";
		String progressMessage = null;
		boolean respondUsingBundle = false;
		switch (instance.getStatus()) {
			case QUEUED -> {
				status = HttpStatus.SC_ACCEPTED;
				message = getOperationName() + " job has not yet started";
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case IN_PROGRESS -> {
				status = HttpStatus.SC_ACCEPTED;
				message = getOperationName() + " job has started and is in progress";
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case FINALIZE -> {
				status = HttpStatus.SC_ACCEPTED;
				message = getOperationName() + " job has started and is being finalized";
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case COMPLETED -> {
				status = HttpStatus.SC_OK;
				String reportText = instance.getReport();
				progressMessage = getOperationName() + " job has completed successfully";
				if (isBlank(reportText)) {
					message = progressMessage;
				} else {
					BulkModifyResourcesResultsJson results =
							JsonUtil.deserialize(reportText, BulkModifyResourcesResultsJson.class);
					message = results.getReport();
				}
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_SUCCESS;
				respondUsingBundle = true;
			}
			case ERRORED, FAILED -> {
				status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
				message = getOperationName() + " job has failed with error: " + instance.getErrorMessage();
				severity = OperationOutcomeUtil.OO_SEVERITY_ERROR;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_PROCESSING;
				respondUsingBundle = true;
			}
			case CANCELLED -> {
				status = HttpStatus.SC_OK;
				message = getOperationName() + " job has been cancelled";
				severity = OperationOutcomeUtil.OO_SEVERITY_WARN;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
				respondUsingBundle = true;
			}
		}

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myContext);
		OperationOutcomeUtil.addIssue(myContext, oo, severity, message, null, code);

		RestfulServerUtils.ResponseEncoding encoding =
				RestfulServerUtils.determineResponseEncodingWithDefault(theRequestDetails);

		HttpServletResponse servletResponse = theRequestDetails.getServletResponse();

		/*
		 * According to the Asynchronous Interaction Request Pattern at
		 * https://hl7.org/fhir/async-bundle.html,
		 * if the job has completed (either successfully or unsuccessfully/prematurely), the response
		 * should use an HTTP 200 status and should indicate the actual status in a Bundle
		 * resource.
		 */
		if (respondUsingBundle) {
			servletResponse.setStatus(HttpStatus.SC_OK);
		} else {
			servletResponse.setStatus(status);
		}

		servletResponse.setContentType(encoding.getContentType());
		servletResponse.setCharacterEncoding(Constants.CHARSET_NAME_UTF8);
		if (progressMessage != null) {
			servletResponse.addHeader(Constants.HEADER_X_PROGRESS, progressMessage);
		} else {
			servletResponse.addHeader(Constants.HEADER_X_PROGRESS, message);
		}

		IBaseResource responseResource = oo;
		if (respondUsingBundle) {
			BundleBuilder bundleBuilder = new BundleBuilder(myContext);
			bundleBuilder.setType(BundleTypeEnum.BATCH_RESPONSE.getCode());

			CanonicalBundleEntry entry = new CanonicalBundleEntry();
			entry.setResponseStatus(BaseTransactionProcessor.toStatusString(status));
			entry.setResponseOutcome(oo);
			bundleBuilder.addEntry(entry);

			responseResource = bundleBuilder.getBundle();
		}

		writeResponse(servletResponse, encoding, responseResource);
	}

	private void writeResponse(
			HttpServletResponse servletResponse,
			RestfulServerUtils.ResponseEncoding encoding,
			IBaseResource responseResource)
			throws IOException {
		try (PrintWriter writer = servletResponse.getWriter()) {
			IParser parser = encoding.getEncoding().newParser(myContext);
			parser.encodeResourceToWriter(responseResource, writer);
		}
	}

	@Nonnull
	protected abstract String getOperationPollForStatusStatus();

	@Nonnull
	protected abstract String getJobId();

	@Nonnull
	protected abstract String getOperationName();

	@VisibleForTesting
	public void setContextForUnitTest(FhirContext theContext) {
		myContext = theContext;
	}

	@VisibleForTesting
	public void setJobCoordinatorForUnitTest(IJobCoordinator theJobCoordinator) {
		myJobCoordinator = theJobCoordinator;
	}
}
