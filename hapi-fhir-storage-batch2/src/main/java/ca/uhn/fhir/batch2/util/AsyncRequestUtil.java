package ca.uhn.fhir.batch2.util;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
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
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import ca.uhn.fhir.batch2.model.JobInstance;

import java.io.IOException;
import java.util.Set;
import java.util.function.Consumer;

/**
 * This class is currently considered an internal HAPI FHIR API and is subject to change. Use with caution!
 */
public class AsyncRequestUtil {

	private AsyncRequestUtil() {
		// non-instantiable
	}


	/**
	 * This method is currently considered an internal HAPI FHIR API and is subject to change. Use with caution!
	 */
	@Beta
	public static void handleAsynchronousOperationStartRequest(ServletRequestDetails theRequestDetails, String theRelativeUrl, String theOperationName, @Nullable Consumer<IBaseOperationOutcome> theOperationOutcomePostProcessor) throws IOException {
		String pollUrl = RestfulServerUtils.createFullyQualifiedUrlFromRelativeUrl(theRequestDetails, theRelativeUrl);
		FhirContext fhirContext = theRequestDetails.getServer().getFhirContext();

		// Create an OperationOutcome to return
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(fhirContext);
		String message =
				theOperationName + " job has been accepted. Poll for status at the following URL: " + pollUrl;
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
	public static JobInstance getJobInstance(IPrimitiveType<String> theJobInstanceId, IJobCoordinator jobCoordinator, String jobId, String operationName) {
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
}
