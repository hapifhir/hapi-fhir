package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.apache.jena.reasoner.ValidityReport;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @since 8.6.0
 */
public class BulkPatchProvider {

	@Autowired
	private FhirContext myContext;
	@Autowired
	private IJobCoordinator myJobCoordinator;

	@Operation(
		name=JpaConstants.OPERATION_BULK_PATCH,
		manualResponse = true)
	public void bulkPatch(
		ServletRequestDetails theRequestDetails,
		@Description("The FHIRPatch document to apply to resources. Must be a Parameters resource.")
		@OperationParam(name = JpaConstants.OPERATION_BULK_PATCH_PARAM_PATCH, typeName = "Parameters", min = 1, max = 1)
		IBaseResource thePatch,
			@Description(
				"One ore more relative search parameter URLs (e.g. \"Patient?active=true\" or \"Observation?\") that will be reindexed.")
			@OperationParam(
				name = JpaConstants.OPERATION_BULK_PATCH_PARAM_URL,
				typeName = "string",
				min = 1,
				max = OperationParam.MAX_UNLIMITED)
		List<IPrimitiveType<String>> theUrlsToReindex
	) {
		ServletRequestUtil.validatePreferAsyncHeader(theRequestDetails, JpaConstants.OPERATION_BULK_PATCH);

		BulkPatchJobParameters jobParameters = new BulkPatchJobParameters();
		jobParameters.setFhirPatch(myContext, thePatch);

		if (theUrlsToReindex != null) {
			for (IPrimitiveType<String> url : theUrlsToReindex) {
				if (isNotBlank(url.getValueAsString())) {
					jobParameters.addUrl(url.getValueAsString());
				}
			}
		}

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(BulkPatchJobAppCtx.JOB_ID);
		startRequest.setParameters(jobParameters);

		Batch2JobStartResponse outcome = myJobCoordinator.startInstance(theRequestDetails, startRequest);

		String jobInstanceId = outcome.getInstanceId();

		ServletContext servletContext = (ServletContext) theRequestDetails.getAttribute(RestfulServer.SERVLET_CONTEXT_ATTRIBUTE);
		HttpServletRequest servletRequest = theRequestDetails.getServletRequest();
		String baseUrl = theRequestDetails.getServer().getServerAddressStrategy().determineServerBase(servletContext, servletRequest);

		StringBuilder pollUrl = new StringBuilder(baseUrl);
		if (!baseUrl.endsWith("/")) {
			pollUrl.append("/");
		}
		pollUrl.append(JpaConstants.OPERATION_BULK_PATCH_STATUS);
		pollUrl.append('?');
		pollUrl.append(JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID);
		pollUrl.append('=');
		pollUrl.append(jobInstanceId);

		// Provide a response
		HttpServletResponse servletResponse = theRequestDetails.getServletResponse();
		servletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
		servletResponse.addHeader(Constants.HEADER_CONTENT_LOCATION, pollUrl.toString());
	}


	@Operation(
		name=JpaConstants.OPERATION_BULK_PATCH_STATUS,
		idempotent=true)
	public IBaseResource bulkPatchStatus(
		@Description("Query the server for the status of a bulk patch operation")
		@OperationParam(name = JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID, typeName = "string", min = 1, max = 1)
		IPrimitiveType<String> theJobId) {

		ValidateUtil.isTrueOrThrowInvalidRequest(theJobId != null && theJobId.hasValue(), "Missing job id");
		JobInstance instance = myJobCoordinator.getInstance(theJobId.getValue());
		ValidateUtil.isTrueOrThrowInvalidRequest(instance.getJobDefinitionId().equals(BulkPatchJobAppCtx.JOB_ID), "Job ID does not correspond to a Bulk Patch job");

		int status;
		String message = "";
		String severity = "";
		String code = "";
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
				BulkModifyResourcesResultsJson results = JsonUtil.deserialize(reportText, BulkModifyResourcesResultsJson.class);
				message = results.getReport();
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
		return oo;
	}


	@VisibleForTesting
	void setContextForUnitTest(FhirContext theContext) {
		myContext = theContext;
	}

	@VisibleForTesting
	void setJobCoordinatorForUnitTest(IJobCoordinator theJobCoordinator) {
		myJobCoordinator = theJobCoordinator;
	}
}
