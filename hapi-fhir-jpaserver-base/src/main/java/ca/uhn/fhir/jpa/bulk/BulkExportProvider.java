package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ArrayUtil;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Set;

public class BulkExportProvider {

	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;

	@VisibleForTesting
	public void setBulkDataExportSvcForUnitTests(IBulkDataExportSvc theBulkDataExportSvc) {
		myBulkDataExportSvc = theBulkDataExportSvc;
	}

	/**
	 * $export
	 */
	@Operation(name = JpaConstants.OPERATION_EXPORT, global = false /* set to true once we can handle this */, manualResponse = true, idempotent = false)
	public void export(
		@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theOutputFormat,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theType,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant") IPrimitiveType<Date> theSince,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE_FILTER, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theTypeFilter,
		ServletRequestDetails theRequestDetails
	) {

		String preferHeader = theRequestDetails.getHeader(Constants.HEADER_PREFER);
		PreferHeader prefer = RestfulServerUtils.parsePreferHeader(null, preferHeader);
		if (prefer.getRespondAsync() == false) {
			throw new InvalidRequestException("Must request async processing for $export");
		}

		String outputFormat = theOutputFormat != null ? theOutputFormat.getValueAsString() : null;

		Set<String> resourceTypes = null;
		if (theType != null) {
			resourceTypes = ArrayUtil.commaSeparatedListToCleanSet(theType.getValueAsString());
		}

		Date since = null;
		if (theSince != null) {
			since = theSince.getValue();
		}

		Set<String> filters = null;
		if (theTypeFilter != null) {
			filters = ArrayUtil.commaSeparatedListToCleanSet(theTypeFilter.getValueAsString());
		}

		IBulkDataExportSvc.JobInfo outcome = myBulkDataExportSvc.submitJob(outputFormat, resourceTypes, since, filters);

		String serverBase = theRequestDetails.getServerBaseForRequest();
		String pollLocation = serverBase + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" + JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + outcome.getJobId();

		HttpServletResponse response = theRequestDetails.getServletResponse();

		// Add standard headers
		theRequestDetails.getServer().addHeadersToResponse(response);

		// Successful 202 Accepted
		response.addHeader(Constants.HEADER_CONTENT_LOCATION, pollLocation);
		response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
	}

	/**
	 * $export-poll-status
	 */
	@Operation(name = JpaConstants.OPERATION_EXPORT_POLL_STATUS, manualResponse = true, idempotent = true)
	public void exportPollStatus(
		@OperationParam(name=JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID, typeName = "string", min = 0, max = 1) IPrimitiveType<String> theJobId,
		ServletRequestDetails theRequestDetails
	) {



		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobStatus(theJobId.getValueAsString());
		switch (status.getStatus()) {
			case BUILDING:


		}

		
	}

}
