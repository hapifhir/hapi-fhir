package ca.uhn.fhir.jpa.bulk.imprt.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderValueParser;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import static org.slf4j.LoggerFactory.getLogger;


public class BulkDataImportProvider {
	private static final Logger ourLog = getLogger(BulkDataImportProvider.class);

	@Autowired
	private IBulkDataImportSvc myBulkDataImportSvc;

	@Autowired
	private FhirContext myFhirContext;

	@VisibleForTesting
	public void setFhirContextForUnitTest(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@VisibleForTesting
	public void setBulkDataImportSvcForUnitTests(IBulkDataImportSvc theBulkDataImportSvc) {
		myBulkDataImportSvc = theBulkDataImportSvc;
	}

	/**
	 * $import
	 */
	@Operation(name = JpaConstants.OPERATION_IMPORT, global = false /* set to true once we can handle this */, manualResponse = true, idempotent = true, manualRequest = true)
	public void imprt(
		@OperationParam(name = JpaConstants.PARAM_IMPORT_JOB_DESCRIPTION, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theJobDescription,
		@OperationParam(name = JpaConstants.PARAM_IMPORT_PROCESSING_MODE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theProcessingMode,
		@OperationParam(name = JpaConstants.PARAM_IMPORT_FILE_COUNT, min = 0, max = 1, typeName = "integer") IPrimitiveType<Integer> theFileCount,
		@OperationParam(name = JpaConstants.PARAM_IMPORT_BATCH_SIZE, min = 0, max = 1, typeName = "integer") IPrimitiveType<Integer> theBatchSize,
		ServletRequestDetails theRequestDetails
	) throws IOException {
                validatePreferAsyncHeader(theRequestDetails);

                // Import requests are expected to be in NDJson format.
                if (RestfulServerUtils.determineRequestEncodingNoDefault(theRequestDetails) != EncodingEnum.NDJSON) {
                        throw new InvalidRequestException("An NDJson content type, like " + Constants.CT_FHIR_NDJSON.toString() + " must be provided for $import.");
                }
  
                BulkImportJobJson theImportJobJson = new BulkImportJobJson();
                theImportJobJson.setJobDescription(theJobDescription == null ? null : theJobDescription.getValueAsString());

                theImportJobJson.setProcessingMode(theProcessingMode == null ? JobFileRowProcessingModeEnum.FHIR_TRANSACTION : JobFileRowProcessingModeEnum.valueOf(theProcessingMode.getValueAsString()));
                theImportJobJson.setBatchSize(theBatchSize == null ? 1 : theBatchSize.getValue());
                theImportJobJson.setFileCount(theFileCount == null ? 1 : theFileCount.getValue());

                // For now, we expect theImportJobJson.getFileCount() to be 1.
                // In the future, the arguments to $import can be changed to allow additional files to be attached to an existing, known job.
                // Then, when the correct number of files have been attached, the job would be started automatically.
                if (theImportJobJson.getFileCount() != 1) {
                        throw new InvalidRequestException("$import requires " + JpaConstants.PARAM_IMPORT_FILE_COUNT.toString() + " to be exactly 1.");
                }

                List<BulkImportJobFileJson> theInitialFiles = new ArrayList<BulkImportJobFileJson>();

                BulkImportJobFileJson theJobFile = new BulkImportJobFileJson();
                theJobFile.setTenantName(theRequestDetails.getTenantId());
                if (theJobDescription != null) {
                        theJobFile.setDescription(theJobDescription.getValueAsString());
                }

                IParser myParser = myFhirContext.newNDJsonParser();

                // We validate the NDJson by parsing it and then re-writing it.
                // In the future, we could add a parameter to skip validation if desired.
                theJobFile.setContents(myParser.encodeResourceToString(myParser.parseResource(theRequestDetails.getInputStream())));

                theInitialFiles.add(theJobFile);

                // Start the job.
                // In a future change, we could add an additional parameter to add files to an existing job.
                // In that world, we would only create a new job if we weren't provided an existing job ID that is to
                // be augmented.
                String theJob = myBulkDataImportSvc.createNewJob(theImportJobJson, theInitialFiles);
                myBulkDataImportSvc.markJobAsReadyForActivation(theJob);
		writePollingLocationToResponseHeaders(theRequestDetails, theJob);
	}

        /**
         * $import-poll-status
         */
        @Operation(name = JpaConstants.OPERATION_IMPORT_POLL_STATUS, manualResponse = true, idempotent = true)
        public void importPollStatus(
                @OperationParam(name = JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID, typeName = "string", min = 0, max = 1) IPrimitiveType<String> theJobId,
                ServletRequestDetails theRequestDetails
        ) throws IOException {
                HttpServletResponse response = theRequestDetails.getServletResponse();
                theRequestDetails.getServer().addHeadersToResponse(response);
                IBulkDataImportSvc.JobInfo status = myBulkDataImportSvc.getJobStatus(theJobId.getValueAsString());
                IBaseOperationOutcome oo;
                switch (status.getStatus()) {
                        case STAGING:
                        case READY:
                        case RUNNING:
                                response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
                                response.addHeader(Constants.HEADER_X_PROGRESS, "Status set to " + status.getStatus() + " at " + new InstantType(status.getStatusTime()).getValueAsString());
                                response.addHeader(Constants.HEADER_RETRY_AFTER, "120");
                                break;
                        case COMPLETE:
                                response.setStatus(Constants.STATUS_HTTP_200_OK);
                                response.setContentType(Constants.CT_FHIR_JSON);
                                // Create an OperationOutcome response
                                oo = OperationOutcomeUtil.newInstance(myFhirContext);
                                myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(oo, response.getWriter());
                                response.getWriter().close();
                                break;
                        case ERROR:
                                response.setStatus(Constants.STATUS_HTTP_500_INTERNAL_ERROR);
                                response.setContentType(Constants.CT_FHIR_JSON);
                                // Create an OperationOutcome response
                                oo = OperationOutcomeUtil.newInstance(myFhirContext);
                                OperationOutcomeUtil.addIssue(myFhirContext, oo, "error", status.getStatusMessage(), null, null);
                                myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(oo, response.getWriter());
                                response.getWriter().close();
                }
        }

	public void writePollingLocationToResponseHeaders(ServletRequestDetails theRequestDetails, String theJob) {
		String serverBase = getServerBase(theRequestDetails);
		String pollLocation = serverBase + "/" + JpaConstants.OPERATION_IMPORT_POLL_STATUS + "?" + JpaConstants.PARAM_IMPORT_POLL_STATUS_JOB_ID + "=" + theJob;
		HttpServletResponse response = theRequestDetails.getServletResponse();
		// Add standard headers
		theRequestDetails.getServer().addHeadersToResponse(response);
		// Successful 202 Accepted
		response.addHeader(Constants.HEADER_CONTENT_LOCATION, pollLocation);
		response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
	}

        private String getServerBase(ServletRequestDetails theRequestDetails) {
                return StringUtils.removeEnd(theRequestDetails.getServerBaseForRequest(), "/");
        }

	private void validatePreferAsyncHeader(ServletRequestDetails theRequestDetails) {
		String preferHeader = theRequestDetails.getHeader(Constants.HEADER_PREFER);
		PreferHeader prefer = RestfulServerUtils.parsePreferHeader(null, preferHeader);
		if (prefer.getRespondAsync() == false) {
			throw new InvalidRequestException("Must request async processing for $import");
		}
	}
}
