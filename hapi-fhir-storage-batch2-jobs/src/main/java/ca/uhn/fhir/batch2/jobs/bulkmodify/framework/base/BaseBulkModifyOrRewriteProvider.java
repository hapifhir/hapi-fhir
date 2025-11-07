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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.dao.BaseTransactionProcessor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.CanonicalBundleEntry;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.ALL_PARTITIONS_TENANT_NAME;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Base class for a plain provider which can initiate a Bulk Modify or Bulk Rewrite job.
 */
public abstract class BaseBulkModifyOrRewriteProvider {

	@Autowired
	protected FhirContext myContext;

	@Autowired
	protected IJobCoordinator myJobCoordinator;

	@Autowired
	protected PartitionSettings myPartitionSettings;

	@Autowired
	protected IInterceptorService myInterceptorService;

	/**
	 * Subclasses should call this method to initiate a new job
	 */
	protected void startJobAndReturnResponse(
			ServletRequestDetails theRequestDetails,
			List<IPrimitiveType<String>> theUrlsToReindex,
			IPrimitiveType<Boolean> theDryRun,
			IPrimitiveType<String> theDryRunMode,
			IPrimitiveType<Integer> theBatchSize,
			IPrimitiveType<Integer> theLimitResourceCount,
			IPrimitiveType<Integer> theLimitResourceVersionCount,
			List<IPrimitiveType<String>> thePartitionIds,
			BaseBulkModifyJobParameters theJobParameters)
			throws IOException {
		ServletRequestUtil.validatePreferAsyncHeader(theRequestDetails, getOperationName());

		theJobParameters.setRequestPartitionId(
				parsePartitionIdsParameterAndInvokeInterceptors(theRequestDetails, thePartitionIds));

		if (theUrlsToReindex != null) {
			for (IPrimitiveType<String> url : theUrlsToReindex) {
				if (isNotBlank(url.getValueAsString())) {
					theJobParameters.addUrl(url.getValueAsString());
				}
			}
		}

		if (theDryRun != null && theDryRun.getValue().equals(Boolean.TRUE)) {
			theJobParameters.setDryRun(true);
		}
		if (theDryRunMode != null) {
			String dryRunMode = theDryRunMode.getValue();
			if (JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN_MODE_COUNT.equalsIgnoreCase(dryRunMode)) {
				theJobParameters.setDryRunMode(BaseBulkModifyJobParameters.DryRunMode.COUNT);
			} else if (JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN_MODE_COLLECT_CHANGES.equalsIgnoreCase(
					dryRunMode)) {
				theJobParameters.setDryRunMode(BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED);
			} else {
				throw new InvalidRequestException(
						Msg.code(2814) + "Invalid dry run code: " + UrlUtil.sanitizeUrlPart(dryRunMode));
			}
		}
		if (theBatchSize != null) {
			theJobParameters.setBatchSize(theBatchSize.getValue());
		}
		if (theLimitResourceCount != null) {
			theJobParameters.setLimitResourceCount(theLimitResourceCount.getValue());
		}
		if (theLimitResourceVersionCount != null) {
			theJobParameters.setLimitResourceVersionCount(theLimitResourceVersionCount.getValue());
		}

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(getJobId());
		startRequest.setParameters(theJobParameters);

		Batch2JobStartResponse outcome = myJobCoordinator.startInstance(theRequestDetails, startRequest);

		String jobInstanceId = outcome.getInstanceId();

		String pollUrl = createPollUrl(theRequestDetails, jobInstanceId);

		// Create an OperationOutcome to return
		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myContext);
		String message =
				getOperationName() + " job has been accepted. Poll for status at the following URL: " + pollUrl;
		String severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
		String code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
		OperationOutcomeUtil.addIssue(myContext, oo, severity, message, null, code);

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

	/**
	 * Parses the {@link JpaConstants#OPERATION_BULK_PATCH_PARAM_PARTITION_ID} parameter value(s)
	 * and invokes any registered {@link Pointcut#STORAGE_PARTITION_SELECTED} interceptors (used
	 * for security)
	 */
	private RequestPartitionId parsePartitionIdsParameterAndInvokeInterceptors(
			RequestDetails theRequestDetails, List<IPrimitiveType<String>> thePartitionIds) {
		if (!myPartitionSettings.isPartitioningEnabled()) {
			return null;
		}

		RequestPartitionId partitionId = parsePartitionIdsParameter(thePartitionIds);

		// Invoke interceptor: STORAGE_PARTITION_SELECTED
		CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorService, theRequestDetails)
				.ifHasCallHooks(Pointcut.STORAGE_PARTITION_SELECTED, () -> new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
						.add(RequestPartitionId.class, partitionId)
						.add(RuntimeResourceDefinition.class, null));

		return partitionId;
	}

	@Nonnull
	private String createPollUrl(ServletRequestDetails theRequestDetails, String jobInstanceId) {
		ServletContext servletContext =
				(ServletContext) theRequestDetails.getServletAttribute(RestfulServer.SERVLET_CONTEXT_ATTRIBUTE);
		HttpServletRequest servletRequest = theRequestDetails.getServletRequest();
		String baseUrl = theRequestDetails
				.getServer()
				.getServerAddressStrategy()
				.determineServerBase(servletContext, servletRequest);

		StringBuilder pollUrlBuilder = new StringBuilder(baseUrl);
		if (!baseUrl.endsWith("/")) {
			pollUrlBuilder.append("/");
		}
		pollUrlBuilder.append(getOperationPollForStatusStatus());
		pollUrlBuilder.append('?');
		pollUrlBuilder.append(JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID);
		pollUrlBuilder.append('=');
		pollUrlBuilder.append(jobInstanceId);
		return pollUrlBuilder.toString();
	}

	/**
	 * Subclasses should call this method to poll for job status
	 */
	protected void pollForJobStatus(
			ServletRequestDetails theRequestDetails, IPrimitiveType<String> theJobId, IPrimitiveType<String> theReturn)
			throws IOException {
		ValidateUtil.isTrueOrThrowInvalidRequest(theJobId != null && theJobId.hasValue(), "Missing job id");

		String returnValue = null;
		if (theReturn != null) {
			returnValue = theReturn.getValueAsString();
		}

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
		List<String> messages = new ArrayList<>();
		String severity = "";
		String code = "";
		String progressMessage = null;
		String returnString = null;
		IBaseBundle returnBundle = null;
		boolean respondUsingBundle = false;
		switch (instance.getStatus()) {
			case QUEUED -> {
				status = HttpStatus.SC_ACCEPTED;
				messages.add(getOperationName() + " job has not yet started");
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case IN_PROGRESS -> {
				status = HttpStatus.SC_ACCEPTED;
				messages.add(getOperationName() + " job has started and is in progress");
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case FINALIZE -> {
				status = HttpStatus.SC_ACCEPTED;
				messages.add(getOperationName() + " job has started and is being finalized");
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_INFORMATIONAL;
			}
			case COMPLETED -> {
				status = HttpStatus.SC_OK;
				String reportText = instance.getReport();
				progressMessage = getOperationName() + " job has completed successfully";
				if (isBlank(reportText)) {
					messages.add(progressMessage);
				} else {
					BulkModifyResourcesResultsJson results =
							JsonUtil.deserialize(reportText, BulkModifyResourcesResultsJson.class);
					BaseBulkModifyJobParameters jobParameters =
							instance.getParameters(BaseBulkModifyJobParameters.DeserializingImpl.class);
					boolean isDryRunCollectChanges = jobParameters.isDryRun()
							&& jobParameters.getDryRunMode() == BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED;

					if (JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_REPORT.equals(returnValue)) {
						returnString = results.getReport();
					} else if (JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_DRYRUN_CHANGES.equals(
							returnValue)) {
						if (!isDryRunCollectChanges) {
							throw new InvalidRequestException(
									Msg.code(2815) + "Changes response can only be provided for "
											+ JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN + " jobs with "
											+ JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN_MODE + "="
											+ JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN_MODE_COLLECT_CHANGES);
						}

						returnBundle = createChangesBundle(results);
					}
					messages.add(results.getReport());
					messages.add("Access raw text report at URL: "
							+ createPollUrl(theRequestDetails, instance.getInstanceId()) + "&"
							+ JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN + "="
							+ JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_REPORT);
					if (isDryRunCollectChanges) {
						messages.add("Access collected dry-run changes at URL: "
								+ createPollUrl(theRequestDetails, instance.getInstanceId()) + "&"
								+ JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN + "="
								+ JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_DRYRUN_CHANGES);
					}
				}
				severity = OperationOutcomeUtil.OO_SEVERITY_INFO;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_SUCCESS;
				respondUsingBundle = true;
			}
			case ERRORED, FAILED -> {
				status = HttpStatus.SC_INTERNAL_SERVER_ERROR;
				messages.add(getOperationName() + " job has failed with error: " + instance.getErrorMessage());
				severity = OperationOutcomeUtil.OO_SEVERITY_ERROR;
				code = OperationOutcomeUtil.OO_ISSUE_CODE_PROCESSING;
				respondUsingBundle = true;
			}
			case CANCELLED -> {
				status = HttpStatus.SC_OK;
				messages.add(getOperationName() + " job has been cancelled");
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

		if (returnBundle != null) {
			RestfulServerUtils.streamResponseAsResource(
					theRequestDetails.getServer(),
					returnBundle,
					Set.of(),
					status,
					additionalHeaders.build(),
					false,
					false,
					theRequestDetails,
					null,
					null);
			return;
		}

		if (returnString != null) {
			writeResponseWithStringBody(theRequestDetails.getServletResponse(), additionalHeaders, returnString);
			return;
		}

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myContext);
		for (String message : messages) {
			OperationOutcomeUtil.addIssue(myContext, oo, severity, message, null, code);
		}

		IBaseResource responseResource;
		if (respondUsingBundle) {
			BundleBuilder bundleBuilder = new BundleBuilder(myContext);
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

	private IBaseBundle createChangesBundle(BulkModifyResourcesResultsJson theResultsJson) {
		IParser parser = myContext.newJsonParser();
		BundleBuilder bundleBuilder = new BundleBuilder(myContext);
		for (String next : theResultsJson.getResourcesChangedBodies()) {
			IBaseResource resource = parser.parseResource(next);
			bundleBuilder.addTransactionUpdateEntry(resource);
		}
		for (String next : theResultsJson.getResourcesDeletedIds()) {
			IdType id = new IdType(next);
			bundleBuilder.addTransactionDeleteEntry(id.getResourceType(), id.getIdPart());
		}
		return bundleBuilder.getBundle();
	}

	private void writeResponseWithStringBody(
			HttpServletResponse theServletResponse,
			ImmutableMultimap.Builder<String, String> theAdditionalHeaders,
			String theResponseString)
			throws IOException {
		theServletResponse.setStatus(HttpStatus.SC_OK);
		theServletResponse.setContentType(Constants.CT_TEXT);
		theServletResponse.setCharacterEncoding(Constants.CHARSET_NAME_UTF8);

		for (Map.Entry<String, String> next : theAdditionalHeaders.build().entries()) {
			theServletResponse.addHeader(next.getKey(), next.getValue());
		}

		try (PrintWriter writer = theServletResponse.getWriter()) {
			writer.write(theResponseString);
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

	public static RequestPartitionId parsePartitionIdsParameter(List<IPrimitiveType<String>> thePartitionIdsParameter) {
		List<Integer> partitionIds = new ArrayList<>();

		if (thePartitionIdsParameter != null) {
			for (IPrimitiveType<String> next : thePartitionIdsParameter) {
				String value = next.getValueAsString();
				if (isNotBlank(value)) {
					if (ALL_PARTITIONS_TENANT_NAME.equals(value)) {
						partitionIds.clear();
						break;
					}
					try {
						partitionIds.add(Integer.parseInt(trim(value)));
					} catch (NumberFormatException e) {
						throw new InvalidRequestException(
								Msg.code(2820) + "Invalid partition ID: " + UrlUtil.sanitizeUrlPart(value));
					}
				}
			}
		}

		if (partitionIds.isEmpty()) {
			return RequestPartitionId.allPartitions();
		}

		return RequestPartitionId.fromPartitionIds(partitionIds);
	}
}
