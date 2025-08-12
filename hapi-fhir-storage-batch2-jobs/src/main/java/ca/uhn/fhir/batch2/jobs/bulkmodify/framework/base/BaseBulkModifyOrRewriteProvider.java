package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobAppCtx;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
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
			BaseBulkModifyJobParameters theJobParameters) {
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

		// Provide a response
		HttpServletResponse servletResponse = theRequestDetails.getServletResponse();
		servletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
		servletResponse.addHeader(Constants.HEADER_CONTENT_LOCATION, pollUrl.toString());
	}

	/**
	 * Subclasses should call this method to poll for job status
	 */
	protected void pollForJobStatus(ServletRequestDetails theRequestDetails, IPrimitiveType<String> theJobId)
			throws IOException {
		ValidateUtil.isTrueOrThrowInvalidRequest(theJobId != null && theJobId.hasValue(), "Missing job id");
		JobInstance instance = myJobCoordinator.getInstance(theJobId.getValue());
		ValidateUtil.isTrueOrThrowInvalidRequest(
				instance.getJobDefinitionId().equals(BulkPatchJobAppCtx.JOB_ID),
				"Job ID does not correspond to a Bulk Patch job");

		int status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		String message = "";
		String severity = "";
		String code = "";
		String progressMessage = null;
		switch (instance.getStatus()) {
			case QUEUED -> {
				status = HttpStatus.SC_ACCEPTED;
				message = "Job has not yet started";
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case IN_PROGRESS -> {
				status = HttpStatus.SC_ACCEPTED;
				message = "Job has started and is in progress";
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case FINALIZE -> {
				status = HttpStatus.SC_ACCEPTED;
				message = "Job has started and is being finalized";
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case COMPLETED -> {
				status = HttpStatus.SC_OK;
				String reportText = instance.getReport();
				progressMessage = "Job has completed successfully";
				if (isBlank(reportText)) {
					message = progressMessage;
				} else {
					BulkModifyResourcesResultsJson results =
							JsonUtil.deserialize(reportText, BulkModifyResourcesResultsJson.class);
					message = results.getReport();
				}
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_SUCCESS;
			}
			case ERRORED, FAILED -> {
				status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
				message = instance.getErrorMessage();
				severity = OperationOutcomeUtil.OO_SEVERITY_ERROR;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_PROCESSING;
			}
			case CANCELLED -> {
				status = HttpStatus.SC_OK;
				message = "Job has been cancelled";
				severity = OperationOutcomeUtil.OO_SEVERITY_WARN;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
		}

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myContext);
		OperationOutcomeUtil.addIssue(myContext, oo, severity, message, null, code);

		RestfulServerUtils.ResponseEncoding encoding =
				RestfulServerUtils.determineResponseEncodingWithDefault(theRequestDetails);

		HttpServletResponse servletResponse = theRequestDetails.getServletResponse();
		servletResponse.setStatus(status);
		servletResponse.setContentType(encoding.getContentType());
		servletResponse.setCharacterEncoding(Constants.CHARSET_NAME_UTF8);
		if (progressMessage != null) {
			servletResponse.addHeader(Constants.HEADER_X_PROGRESS, progressMessage);
		} else {
			servletResponse.addHeader(Constants.HEADER_X_PROGRESS, message);
		}

		try (PrintWriter writer = servletResponse.getWriter()) {
			myContext.newJsonParser().encodeResourceToWriter(oo, writer);
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
