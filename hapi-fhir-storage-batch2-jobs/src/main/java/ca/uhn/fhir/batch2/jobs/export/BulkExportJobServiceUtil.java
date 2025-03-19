package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;

public class BulkExportJobServiceUtil {

	/**
	 * This method generates response for the bulk export request
	 * which contains the polling location
	 */
	public static void writePollingLocationsToResponseHeaders(
		@Nonnull ServletRequestDetails theRequestDetails, @Nonnull String theInstanceId) {
		String serverBase = BulkDataExportUtil.getServerBase(theRequestDetails);
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

}
