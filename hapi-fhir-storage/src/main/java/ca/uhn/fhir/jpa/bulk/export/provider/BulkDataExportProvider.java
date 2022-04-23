package ca.uhn.fhir.jpa.bulk.export.provider;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportResponseJson;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ArrayUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES;
import static org.slf4j.LoggerFactory.getLogger;


public class BulkDataExportProvider {
	public static final String FARM_TO_TABLE_TYPE_FILTER_REGEX = "(?:,)(?=[A-Z][a-z]+\\?)";
	private static final Logger ourLog = getLogger(BulkDataExportProvider.class);

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
		@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE_FILTER, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theTypeFilter,
		ServletRequestDetails theRequestDetails
	) {
		validatePreferAsyncHeader(theRequestDetails, JpaConstants.OPERATION_EXPORT);
		BulkDataExportOptions bulkDataExportOptions = buildSystemBulkExportOptions(theOutputFormat, theType, theSince, theTypeFilter);
		Boolean useCache = shouldUseCache(theRequestDetails);
		IBulkDataExportSvc.JobInfo outcome = myBulkDataExportSvc.submitJob(bulkDataExportOptions, useCache, theRequestDetails);
		writePollingLocationToResponseHeaders(theRequestDetails, outcome);
	}

	private boolean shouldUseCache(ServletRequestDetails theRequestDetails) {
		CacheControlDirective cacheControlDirective = new CacheControlDirective().parse(theRequestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL));
		return !cacheControlDirective.isNoCache();
	}

	private String getServerBase(ServletRequestDetails theRequestDetails) {
		return StringUtils.removeEnd(theRequestDetails.getServerBaseForRequest(), "/");
	}

	private String getDefaultPartitionServerBase(ServletRequestDetails theRequestDetails) {
		if (theRequestDetails.getTenantId() == null || theRequestDetails.getTenantId().equals(JpaConstants.DEFAULT_PARTITION_NAME)) {
			return getServerBase(theRequestDetails);
		}
		else {
			return StringUtils.removeEnd(theRequestDetails.getServerBaseForRequest().replace(theRequestDetails.getTenantId(), JpaConstants.DEFAULT_PARTITION_NAME), "/");
		}
	}

	/**
	 * Group/Id/$export
	 */
	@Operation(name = JpaConstants.OPERATION_EXPORT, manualResponse = true, idempotent = true, typeName = "Group")
	public void groupExport(
		@IdParam IIdType theIdParam,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theOutputFormat,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theType,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant") IPrimitiveType<Date> theSince,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE_FILTER, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theTypeFilter,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_MDM, min = 0, max = 1, typeName = "boolean") IPrimitiveType<Boolean> theMdm,
		ServletRequestDetails theRequestDetails
	) {
		ourLog.debug("Received Group Bulk Export Request for Group {}", theIdParam);
		ourLog.debug("_type={}", theIdParam);
		ourLog.debug("_since={}", theSince);
		ourLog.debug("_typeFilter={}", theTypeFilter);
		ourLog.debug("_mdm=", theMdm);


		validatePreferAsyncHeader(theRequestDetails, JpaConstants.OPERATION_EXPORT);
		BulkDataExportOptions bulkDataExportOptions = buildGroupBulkExportOptions(theOutputFormat, theType, theSince, theTypeFilter, theIdParam, theMdm);
		validateResourceTypesAllContainPatientSearchParams(bulkDataExportOptions.getResourceTypes());
		IBulkDataExportSvc.JobInfo outcome = myBulkDataExportSvc.submitJob(bulkDataExportOptions, shouldUseCache(theRequestDetails), theRequestDetails);
		writePollingLocationToResponseHeaders(theRequestDetails, outcome);
	}

	private void validateResourceTypesAllContainPatientSearchParams(Set<String> theResourceTypes) {
		if (theResourceTypes != null) {
			List<String> badResourceTypes = theResourceTypes.stream()
				.filter(resourceType -> !PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(resourceType))
				.filter(resourceType -> !myBulkDataExportSvc.getPatientCompartmentResources().contains(resourceType))
				.collect(Collectors.toList());

			if (!badResourceTypes.isEmpty()) {
				throw new InvalidRequestException(Msg.code(512) + String.format("Resource types [%s] are invalid for this type of export, as they do not contain search parameters that refer to patients.", String.join(",", badResourceTypes)));
			}
		}
	}

	/**
	 * Patient/$export
	 */
	@Operation(name = JpaConstants.OPERATION_EXPORT, manualResponse = true, idempotent = true, typeName = "Patient")
	public void patientExport(
		@OperationParam(name = JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theOutputFormat,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE, min = 0, max = 1, typeName = "string") IPrimitiveType<String> theType,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_SINCE, min = 0, max = 1, typeName = "instant") IPrimitiveType<Date> theSince,
		@OperationParam(name = JpaConstants.PARAM_EXPORT_TYPE_FILTER, min = 0, max = OperationParam.MAX_UNLIMITED, typeName = "string") List<IPrimitiveType<String>> theTypeFilter,
		ServletRequestDetails theRequestDetails
	) {
		validatePreferAsyncHeader(theRequestDetails, JpaConstants.OPERATION_EXPORT);
		BulkDataExportOptions bulkDataExportOptions = buildPatientBulkExportOptions(theOutputFormat, theType, theSince, theTypeFilter);
		validateResourceTypesAllContainPatientSearchParams(bulkDataExportOptions.getResourceTypes());
		IBulkDataExportSvc.JobInfo outcome = myBulkDataExportSvc.submitJob(bulkDataExportOptions, shouldUseCache(theRequestDetails), theRequestDetails);
		writePollingLocationToResponseHeaders(theRequestDetails, outcome);
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

		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(theJobId.getValueAsString());

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
					String serverBase = getDefaultPartitionServerBase(theRequestDetails);
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

	private BulkDataExportOptions buildSystemBulkExportOptions(IPrimitiveType<String> theOutputFormat, IPrimitiveType<String> theType, IPrimitiveType<Date> theSince, List<IPrimitiveType<String>> theTypeFilter) {
		return buildBulkDataExportOptions(theOutputFormat, theType, theSince, theTypeFilter, BulkDataExportOptions.ExportStyle.SYSTEM);
	}

	private BulkDataExportOptions buildGroupBulkExportOptions(IPrimitiveType<String> theOutputFormat, IPrimitiveType<String> theType, IPrimitiveType<Date> theSince, List<IPrimitiveType<String>> theTypeFilter, IIdType theGroupId, IPrimitiveType<Boolean> theExpandMdm) {
		BulkDataExportOptions bulkDataExportOptions = buildBulkDataExportOptions(theOutputFormat, theType, theSince, theTypeFilter, BulkDataExportOptions.ExportStyle.GROUP);
		bulkDataExportOptions.setGroupId(theGroupId);

		boolean mdm = false;
		if (theExpandMdm != null) {
			mdm = theExpandMdm.getValue();
		}
		bulkDataExportOptions.setExpandMdm(mdm);

		return bulkDataExportOptions;
	}

	private BulkDataExportOptions buildPatientBulkExportOptions(IPrimitiveType<String> theOutputFormat, IPrimitiveType<String> theType, IPrimitiveType<Date> theSince, List<IPrimitiveType<String>> theTypeFilter) {
		return buildBulkDataExportOptions(theOutputFormat, theType, theSince, theTypeFilter, BulkDataExportOptions.ExportStyle.PATIENT);
	}

	private BulkDataExportOptions buildBulkDataExportOptions(IPrimitiveType<String> theOutputFormat, IPrimitiveType<String> theType, IPrimitiveType<Date> theSince, List<IPrimitiveType<String>> theTypeFilter, BulkDataExportOptions.ExportStyle theExportStyle) {
		String outputFormat = theOutputFormat != null ? theOutputFormat.getValueAsString() : null;

		Set<String> resourceTypes = null;
		if (theType != null) {
			resourceTypes = ArrayUtil.commaSeparatedListToCleanSet(theType.getValueAsString());
		}

		Date since = null;
		if (theSince != null) {
			since = theSince.getValue();
		}

		Set<String> typeFilters = splitTypeFilters(theTypeFilter);

		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setFilters(typeFilters);
		bulkDataExportOptions.setExportStyle(theExportStyle);
		bulkDataExportOptions.setSince(since);
		bulkDataExportOptions.setResourceTypes(resourceTypes);
		bulkDataExportOptions.setOutputFormat(outputFormat);
		return bulkDataExportOptions;
	}

	public void writePollingLocationToResponseHeaders(ServletRequestDetails theRequestDetails, IBulkDataExportSvc.JobInfo theOutcome) {
		String serverBase = getServerBase(theRequestDetails);
		String pollLocation = serverBase + "/" + JpaConstants.OPERATION_EXPORT_POLL_STATUS + "?" + JpaConstants.PARAM_EXPORT_POLL_STATUS_JOB_ID + "=" + theOutcome.getJobId();

		HttpServletResponse response = theRequestDetails.getServletResponse();

		// Add standard headers
		theRequestDetails.getServer().addHeadersToResponse(response);

		// Successful 202 Accepted
		response.addHeader(Constants.HEADER_CONTENT_LOCATION, pollLocation);
		response.setStatus(Constants.STATUS_HTTP_202_ACCEPTED);
	}

	public static void validatePreferAsyncHeader(ServletRequestDetails theRequestDetails, String theOperationName) {
		String preferHeader = theRequestDetails.getHeader(Constants.HEADER_PREFER);
		PreferHeader prefer = RestfulServerUtils.parsePreferHeader(null, preferHeader);
		if (prefer.getRespondAsync() == false) {
			throw new InvalidRequestException(Msg.code(513) + "Must request async processing for " + theOperationName);
		}
	}

	private Set<String> splitTypeFilters(List<IPrimitiveType<String>> theTypeFilter) {
		if (theTypeFilter== null) {
			return null;
		}

		Set<String> retVal = new HashSet<>();

		for (IPrimitiveType<String> next : theTypeFilter) {
			String typeFilterString = next.getValueAsString();
			Arrays
				.stream(typeFilterString.split(FARM_TO_TABLE_TYPE_FILTER_REGEX))
				.filter(StringUtils::isNotBlank)
				.forEach(t->retVal.add(t));
		}

		return retVal;
	}

}
