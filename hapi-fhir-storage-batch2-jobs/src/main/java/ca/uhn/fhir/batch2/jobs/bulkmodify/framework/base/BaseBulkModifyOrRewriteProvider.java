/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.util.AsyncRequestUtil;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ServletRequestUtil;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.ALL_PARTITIONS_TENANT_NAME;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
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

	@Autowired
	protected IJobPartitionProvider myJobPartitionProvider;

	@Autowired
	private IDaoRegistry myDaoRegistry;

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
		if (isRequirePreferAsyncHeader(theRequestDetails)) {
			ServletRequestUtil.validatePreferAsyncHeader(theRequestDetails, getOperationName());
		}

		if (myPartitionSettings.isPartitioningEnabled() && myPartitionSettings.isUnnamedPartitionMode()) {
			theJobParameters.setRequestPartitionId(
					parsePartitionIdsParameterAndInvokeInterceptors(theRequestDetails, thePartitionIds));
		}

		List<IPrimitiveType<String>> urlsToReindex = getIfNull(theUrlsToReindex, List.of());
		List<String> urls = urlsToReindex.stream()
				.filter(Objects::nonNull)
				.map(IPrimitiveType::getValueAsString)
				.filter(StringUtils::isNotBlank)
				.toList();

		if (isAutoExpandEmptyUrlList()) {
			// if the url list is empty, use all the supported resource types to build the url list
			// we can go back to no url scenario if all resource types point to the same partition
			if (urls.isEmpty()) {
				List<String> list = new ArrayList<>();
				for (String t : myContext.getResourceTypes()) {
					if (myDaoRegistry.isResourceTypeSupported(t)) {
						list.add(t + "?");
					}
				}
				urls = list;
			}
		}

		if (!urls.isEmpty()) {
			if (theJobParameters.getRequestPartitionId() == null) {
				List<PartitionedUrl> partitionedUrls =
						myJobPartitionProvider.getPartitionedUrls(theRequestDetails, urls);
				theJobParameters.addPartitionedUrls(partitionedUrls);
			} else {
				for (String url : urls) {
					theJobParameters.addPartitionedUrl(new PartitionedUrl().setUrl(url));
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

		String relativeUrl = createPollingRelativeUrl(outcome.getInstanceId());
		String operationName = getOperationName();

		Consumer<IBaseOperationOutcome> operationOutcomePostProcessor =
				oo -> postProcessResponseOperationOutcome(oo, theRequestDetails);
		AsyncRequestUtil.handleAsynchronousOperationStartRequest(
				theRequestDetails, relativeUrl, operationName, operationOutcomePostProcessor);
	}

	/**
	 * Subclasses may implement this method to provide post-processing on the response OperationOutcome
	 */
	protected void postProcessResponseOperationOutcome(
			IBaseOperationOutcome theOo, ServletRequestDetails theRequestDetails) {
		// nothing
	}

	/**
	 * Subclasses may override. If <code>true</code> (default implementation returns <code>false</code>),
	 * if no URLs are provided, the list will be auto expanded to include all supported resource types.
	 */
	protected boolean isAutoExpandEmptyUrlList() {
		return false;
	}

	/**
	 * Subclasses may override this if they don't want to require a
	 * Prefer async header in the request. Generally we should always want this
	 * for kicking off batch jobs but we can skip it for legacy operations.
	 */
	protected boolean isRequirePreferAsyncHeader(ServletRequestDetails theRequestDetails) {
		return true;
	}

	/**
	 * Parses the {@link JpaConstants#OPERATION_BULK_PATCH_PARAM_PARTITION_ID} parameter value(s)
	 * and invokes any registered {@link Pointcut#STORAGE_PARTITION_SELECTED} interceptors (used
	 * for security)
	 */
	private RequestPartitionId parsePartitionIdsParameterAndInvokeInterceptors(
			RequestDetails theRequestDetails, List<IPrimitiveType<String>> thePartitionIds) {
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
	private String createPollingRelativeUrl(String jobInstanceId) {
		return getOperationPollForStatusStatus()
				+ '?'
				+ JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_JOB_ID
				+ '='
				+ jobInstanceId;
	}

	/**
	 * Subclasses should call this method to poll for job status
	 */
	protected void pollForJobStatus(
			ServletRequestDetails theRequestDetails,
			IPrimitiveType<String> theJobInstanceId,
			IPrimitiveType<String> theReturn)
			throws IOException {
		ValidateUtil.isTrueOrThrowInvalidRequest(
				theJobInstanceId != null && theJobInstanceId.hasValue(), "Missing job id");

		String returnValue = null;
		if (theReturn != null) {
			returnValue = theReturn.getValueAsString();
		}

		IJobCoordinator jobCoordinator = myJobCoordinator;
		String jobId = getJobId();
		String operationName = getOperationName();

		JobInstance instance = AsyncRequestUtil.getJobInstance(jobCoordinator, jobId, theJobInstanceId, operationName);

		if (instance.getStatus().isEnded()) {
			if (isNotBlank(instance.getReport())) {
				if (JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_DRYRUN_CHANGES.equals(returnValue)) {
					BaseBulkModifyJobParameters jobParameters =
							instance.getParameters(BaseBulkModifyJobParameters.DeserializingImpl.class);
					boolean isDryRunCollectChanges = jobParameters.isDryRun()
							&& jobParameters.getDryRunMode() == BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED;
					if (!isDryRunCollectChanges) {
						throw new InvalidRequestException(Msg.code(2815) + "Changes response can only be provided for "
								+ JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN + " jobs with "
								+ JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN_MODE + "="
								+ JpaConstants.OPERATION_BULK_PATCH_PARAM_DRY_RUN_MODE_COLLECT_CHANGES);
					}

					BulkModifyResourcesResultsJson results =
							JsonUtil.deserialize(instance.getReport(), BulkModifyResourcesResultsJson.class);
					IBaseBundle returnBundle = createChangesBundle(results);
					int status = instance.getStatus() == StatusEnum.COMPLETED
							? HttpStatus.SC_OK
							: HttpStatus.SC_INTERNAL_SERVER_ERROR;
					RestfulServerUtils.streamResponseAsResource(
							theRequestDetails.getServer(),
							returnBundle,
							Set.of(),
							status,
							null,
							false,
							false,
							theRequestDetails,
							null,
							null);
					return;
				}
			}
		}

		handleAsyncJobPollForStatusResponse(theRequestDetails, instance, operationName, returnValue);
	}

	private void handleAsyncJobPollForStatusResponse(
			ServletRequestDetails theRequestDetails,
			JobInstance theJobInstance,
			String theOperationName,
			String theReturnParameterValue)
			throws IOException {
		Function<JobInstance, AsyncRequestUtil.CompletedJobPollResponse> createCompletionPollResponse =
				theInstance -> createCompletedJobPollResponse(theRequestDetails, theInstance, theReturnParameterValue);
		AsyncRequestUtil.handleAsyncJobPollForStatusResponse(
				theRequestDetails, theJobInstance, theOperationName, createCompletionPollResponse);
	}

	private AsyncRequestUtil.CompletedJobPollResponse createCompletedJobPollResponse(
			ServletRequestDetails theRequestDetails, JobInstance theInstance, String theReturnParameterValue) {
		BulkModifyResourcesResultsJson results =
				JsonUtil.deserialize(theInstance.getReport(), BulkModifyResourcesResultsJson.class);
		BaseBulkModifyJobParameters jobParameters =
				theInstance.getParameters(BaseBulkModifyJobParameters.DeserializingImpl.class);
		boolean isDryRunCollectChanges = jobParameters.isDryRun()
				&& jobParameters.getDryRunMode() == BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED;

		if (JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_REPORT.equals(theReturnParameterValue)) {
			return new AsyncRequestUtil.CompletedJobPollResponse(results.getReport(), null);
		}

		List<String> messages = new ArrayList<>();
		messages.add(results.getReport());
		String relativeUrl1 = createPollingRelativeUrl(theInstance.getInstanceId());

		messages.add("Access raw text report at URL: "
				+ RestfulServerUtils.createFullyQualifiedUrlFromRelativeUrl(theRequestDetails, relativeUrl1) + "&"
				+ JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN + "="
				+ JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_REPORT);
		if (isDryRunCollectChanges) {
			String relativeUrl = createPollingRelativeUrl(theInstance.getInstanceId());

			messages.add("Access collected dry-run changes at URL: "
					+ RestfulServerUtils.createFullyQualifiedUrlFromRelativeUrl(theRequestDetails, relativeUrl) + "&"
					+ JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN + "="
					+ JpaConstants.OPERATION_BULK_PATCH_STATUS_PARAM_RETURN_VALUE_DRYRUN_CHANGES);
		}

		return new AsyncRequestUtil.CompletedJobPollResponse(null, messages);
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

	@VisibleForTesting
	public void setDaoRegistryForUnitTest(IDaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	@VisibleForTesting
	public void setJobPartitionProviderForUnitTest(IJobPartitionProvider theJobPartitionProvider) {
		myJobPartitionProvider = theJobPartitionProvider;
	}

	@VisibleForTesting
	public void setPartitionSettingsForUnitTest(PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
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
