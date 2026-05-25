package ca.uhn.fhir.batch2.util;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.BaseTransactionProcessor;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.CanonicalBundleEntry;
import ca.uhn.fhir.util.DatatypeUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class is currently considered an internal HAPI FHIR API and is subject to change. Use with caution!
 */
public class AsyncRequestUtil {

	private AsyncRequestUtil() {
		// non-instantiable
	}

	/**
	 * <p>
	 * This method is currently considered an internal HAPI FHIR API and is subject to change. Use with caution!
	 * </p>
	 */
	@Beta
	public static void handleAsynchronousOperationStartRequest(
			ServletRequestDetails theRequestDetails,
			String theRelativeUrl,
			String theOperationName,
			@Nullable Consumer<IBaseOperationOutcome> theOperationOutcomePostProcessor)
			throws IOException {
		String pollUrl = RestfulServerUtils.createFullyQualifiedUrlFromRelativeUrl(theRequestDetails, theRelativeUrl);
		FhirContext fhirContext = theRequestDetails.getServer().getFhirContext();

		// Create an OperationOutcome to return
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(fhirContext);
		String message = theOperationName + " job has been accepted. Poll for status at the following URL: " + pollUrl;
		String severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
		String code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
		OperationOutcomeUtil.addIssue(fhirContext, oo, severity, message, null, code);

		if (theOperationOutcomePostProcessor != null) {
			theOperationOutcomePostProcessor.accept(oo);
		}

		// Provide a response
		Multimap<String, String> additionalHeaders = ImmutableMultimap.<String, String>builder()
				.put(Constants.HEADER_CONTENT_LOCATION, pollUrl)
				.build();

		RestfulServerUtils.streamResponseAsResource(
				theRequestDetails.getServer(),
				oo,
				Set.of(),
				HttpServletResponse.SC_ACCEPTED,
				additionalHeaders,
				false,
				false,
				theRequestDetails,
				null,
				null);
	}

	@Nonnull
	public static JobInstance getJobInstance(
			IPrimitiveType<String> theJobInstanceId,
			IJobCoordinator jobCoordinator,
			String jobId,
			String operationName) {
		JobInstance instance;
		try {
			instance = jobCoordinator.getInstance(DatatypeUtil.toStringValue(theJobInstanceId));
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(
					Msg.code(2787) + "Invalid/unknown job ID: " + UrlUtil.sanitizeUrlPart(theJobInstanceId.getValue()));
		}

		ValidateUtil.isTrueOrThrowInvalidRequest(
				instance.getJobDefinitionId().equals(jobId),
				"Job ID does not correspond to a " + operationName + " job");
		return instance;
	}

	public static void writeResponseWithStringBody(
			HttpServletResponse theServletResponse,
			ImmutableMultimap.Builder<String, String> theAdditionalHeaders,
			String theResponseString)
			throws IOException {
		theServletResponse.setStatus(HttpStatus.SC_OK);
		theServletResponse.setContentType(Constants.CT_TEXT);
		theServletResponse.setCharacterEncoding(Constants.CHARSET_NAME_UTF8);

		// FIXME: is this used?
		if (theAdditionalHeaders != null) {
			for (Map.Entry<String, String> next : theAdditionalHeaders.build().entries()) {
				theServletResponse.addHeader(next.getKey(), next.getValue());
			}
		}

		try (PrintWriter writer = theServletResponse.getWriter()) {
			writer.write(theResponseString);
		}
	}

	/**
	 * Creates the HTTP response for an asynchronous job poll request.
	 * <p>
	 * This method is currently considered an internal HAPI FHIR API and is subject to change. Use with caution!
	 * </p>
	 *
	 * @param theRequestDetails               The request details associated with the poll request
	 * @param theJobInstance                  The Batch2 Job Instance being polled
	 * @param theOperationName                A descriptive name for the operation being polled. This isn't used for anything programmatic or machine processed, it is just used in messages returned to the client. The operation name of the operation which kicked off the job is generally appropriate.
	 * @param theCompletedJobResponseProvider A function that provides details for the completed job. This function will be called
	 *                                        when a client polls for the status of a job and the job is completed. It can supply
	 *                                        additional messages that will be returned with the response, or even supply an entire
	 *                                        report to return instead of the standard OperationOutcome.
	 * @see CompletedJobPollResponse
	 */
	@Beta
	public static void handleAsyncJobPollForStatusResponse(
			ServletRequestDetails theRequestDetails,
			JobInstance theJobInstance,
			String theOperationName,
			Function<JobInstance, CompletedJobPollResponse> theCompletedJobResponseProvider)
			throws IOException {
		int status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		List<String> messages = new ArrayList<>();
		String severity = "";
		String code = "";
		String progressMessage = null;
		boolean respondUsingBundle = false;
		switch (theJobInstance.getStatus()) {
			case QUEUED -> {
				status = HttpStatus.SC_ACCEPTED;
				messages.add(theOperationName + " job has not yet started");
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case IN_PROGRESS -> {
				status = HttpStatus.SC_ACCEPTED;
				String message = theOperationName + " job has started and is in progress.";
				if (theJobInstance.getCurrentGatedStepId() != null) {
					message += " Current step: " + theJobInstance.getCurrentGatedStepId() + ".";
				}
				messages.add(message);
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case FINALIZE -> {
				status = HttpStatus.SC_ACCEPTED;
				messages.add(theOperationName + " job has started and is being finalized");
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
				//noinspection deprecation
			case ERRORED, FAILED, COMPLETED -> {
				if (theJobInstance.getStatus() == StatusEnum.COMPLETED) {
					status = HttpStatus.SC_OK;
					progressMessage = theOperationName + " job has completed successfully";
					severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
					code = OperationOutcomeUtil.OO_ISSUE_CODE_SUCCESS;
				} else {
					progressMessage =
							theOperationName + " job has failed with error: " + theJobInstance.getErrorMessage();
					severity = OperationOutcomeUtil.OO_SEVERITY_ERROR;
					code = OperationOutcomeUtil.OO_ISSUE_CODE_PROCESSING;
				}

				String reportText = theJobInstance.getReport();
				if (isBlank(reportText)) {
					messages.add(progressMessage);
				} else {
					CompletedJobPollResponse completedJobPollResponse =
							theCompletedJobResponseProvider.apply(theJobInstance);
					if (completedJobPollResponse.rawBody() != null) {
						writeResponseWithStringBody(
								theRequestDetails.getServletResponse(), null, completedJobPollResponse.rawBody());
						return;
					}
					if (completedJobPollResponse.messages() != null) {
						messages.addAll(completedJobPollResponse.messages());
					}
				}
				respondUsingBundle = true;
			}
			case CANCELLED -> {
				status = HttpStatus.SC_OK;
				messages.add(theOperationName + " job has been cancelled");
				severity = OperationOutcomeUtil.OO_SEVERITY_WARN;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
				respondUsingBundle = true;
			}
		}

		ImmutableMultimap.Builder<String, String> additionalHeaders = ImmutableMultimap.builder();
		if (progressMessage != null) {
			additionalHeaders.put(Constants.HEADER_X_PROGRESS, progressMessage);
		} else if (!messages.isEmpty()) {
			additionalHeaders.put(Constants.HEADER_X_PROGRESS, messages.get(0));
		}

		FhirContext fhirContext = theRequestDetails.getServer().getFhirContext();
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(fhirContext);
		for (String message : messages) {
			OperationOutcomeUtil.addIssue(fhirContext, oo, severity, message, null, code);
		}

		IBaseResource responseResource;
		if (respondUsingBundle) {
			BundleBuilder bundleBuilder = new BundleBuilder(fhirContext);
			bundleBuilder.setType(BundleTypeEnum.BATCH_RESPONSE.getCode());

			CanonicalBundleEntry entry = new CanonicalBundleEntry();
			entry.setResponseStatus(BaseTransactionProcessor.toStatusString(status));
			entry.setResponseOutcome(oo);
			bundleBuilder.addEntry(entry);

			responseResource = bundleBuilder.getBundle();
		} else {
			responseResource = oo;
		}

		/*
		 * According to the Asynchronous Interaction Request Pattern at
		 * https://hl7.org/fhir/async-bundle.html,
		 * if the job has completed (either successfully or unsuccessfully/prematurely), the response
		 * should use an HTTP 200 status and should indicate the actual status in a Bundle
		 * resource.
		 */
		if (respondUsingBundle) {
			status = HttpStatus.SC_OK;
		}

		Multimap<String, String> additionalHeaders1 = additionalHeaders.build();

		RestfulServerUtils.streamResponseAsResource(
				theRequestDetails.getServer(),
				responseResource,
				Set.of(),
				status,
				additionalHeaders1,
				false,
				false,
				theRequestDetails,
				null,
				null);
	}

	/**
	 * This object is created and returned by a lambda passed to {@link #HandleAsyncJobPollForStatusResponse}
	 * @param rawBody
	 * @param messages
	 * @see #handleAsyncJobPollForStatusResponse(ServletRequestDetails, JobInstance, String, Function)
	 */
	public record CompletedJobPollResponse(@Nullable String rawBody, List<String> messages) {}
}
