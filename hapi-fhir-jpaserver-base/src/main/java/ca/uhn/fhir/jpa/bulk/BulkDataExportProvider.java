package ca.uhn.fhir.jpa.bulk;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.util.JsonUtil;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ArrayUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.InstantType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

public class BulkDataExportProvider {

	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	private FhirContext myFhirContext;

	@VisibleForTesting
	public void setFhirContextForUnitTest(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@VisibleForTesting
	public void setBulkDataExportSvcForUnitTests(IBulkDataExportSvc theBulkDataExportSvc) {
		myBulkDataExportSvc = theBulkDataExportSvc;
	}

	/**
	 * $export
	 */
	@Operation(name = JpaConstants.OPERATION_EXPORT, global = false /* set to true once we can handle this */, manualResponse = true, idempotent = true)
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

		String serverBase = getServerBase(theRequestDetails);
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
		@OperationParam(name = JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID, typeName = "string", min = 0, max = 1) IPrimitiveType<String> theJobId,
		ServletRequestDetails theRequestDetails
	) throws IOException {

		HttpServletResponse response = theRequestDetails.getServletResponse();
		theRequestDetails.getServer().addHeadersToResponse(response);

		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobStatusOrThrowResourceNotFound(theJobId.getValueAsString());

		switch (status.getStatus()) {
			case SUBMITTED:
			case BUILDING:

				response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
				response.addHeader(Constants.HEADER_X_PROGRESS, "Build in progress - Status set to " + status.getStatus() + " at " + new InstantType(status.getStatusTime()).getValueAsString());
				response.addHeader(Constants.HEADER_RETRY_AFTER, "120");
				break;

			case COMPLETE:

				response.setStatus(Constants.STATUS_HTTP_200_OK);
				response.setContentType(Constants.CT_JSON);

				// Create a JSON response
				BulkExportResponseJson bulkResponseDocument = new BulkExportResponseJson();
				bulkResponseDocument.setTransactionTime(status.getStatusTime());
				bulkResponseDocument.setRequest(status.getRequest());
				for (IBulkDataExportSvc.FileEntry nextFile : status.getFiles()) {
					String serverBase = getServerBase(theRequestDetails);
					String nextUrl = serverBase + "/" + nextFile.getResourceId().toUnqualifiedVersionless().getValue();
					bulkResponseDocument
						.addOutput()
						.setType(nextFile.getResourceType())
						.setUrl(nextUrl);
				}
				JsonUtil.serialize(bulkResponseDocument, response.getWriter());
				response.getWriter().close();
				break;

			case ERROR:

				response.setStatus(Constants.STATUS_HTTP_500_INTERNAL_ERROR);
				response.setContentType(Constants.CT_FHIR_JSON);

				// Create an OperationOutcome response
				IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myFhirContext);
				OperationOutcomeUtil.addIssue(myFhirContext, oo, "error", status.getStatusMessage(), null, null);
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(oo, response.getWriter());
				response.getWriter().close();

		}


	}

	private String getServerBase(ServletRequestDetails theRequestDetails) {
		return StringUtils.removeEnd(theRequestDetails.getServerBaseForRequest(), "/");
	}

}
