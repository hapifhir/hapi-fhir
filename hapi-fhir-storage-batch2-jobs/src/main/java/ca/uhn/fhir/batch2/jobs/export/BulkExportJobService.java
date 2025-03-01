/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for initiating a bulk export job
 * with appropriate _type parameter & partitionId as well as
 * generating response for request which includes the polling location.
 * It also calls hooks which can update BulkExportJobParameters and the incoming requests.
 */
public class BulkExportJobService {
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final IJobCoordinator myJobCoordinator;
	private final DaoRegistry myDaoRegistry;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperService;
	private final JpaStorageSettings myStorageSettings;

	public BulkExportJobService(
			@Nonnull IInterceptorBroadcaster theInterceptorBroadcaster,
			@Nonnull IJobCoordinator theJobCoordinator,
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull IRequestPartitionHelperSvc theRequestPartitionHelperService,
			@Nonnull JpaStorageSettings theStorageSettings) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myJobCoordinator = theJobCoordinator;
		myDaoRegistry = theDaoRegistry;
		myRequestPartitionHelperService = theRequestPartitionHelperService;
		myStorageSettings = theStorageSettings;
	}

	/**
	 * Start BulkExport job with appropriate parameters
	 */
	public void startJob(
			@Nonnull ServletRequestDetails theRequestDetails,
			@Nonnull BulkExportJobParameters theBulkExportJobParameters) {
		// parameter massaging
		expandParameters(theRequestDetails, theBulkExportJobParameters);
		callBulkExportHooks(theRequestDetails, theBulkExportJobParameters);

		// get cache boolean
		boolean useCache = shouldUseCache(theRequestDetails);

		// start job
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setParameters(theBulkExportJobParameters);
		startRequest.setUseCache(useCache);
		startRequest.setJobDefinitionId(Batch2JobDefinitionConstants.BULK_EXPORT);
		Batch2JobStartResponse response = myJobCoordinator.startInstance(theRequestDetails, startRequest);

		writePollingLocationToResponseHeaders(theRequestDetails, response.getInstanceId());
	}

	/**
	 * This method changes any parameters (limiting the _type parameter, for instance)
	 * so that later steps in the export do not have to handle them.
	 */
	private void expandParameters(
			@Nonnull ServletRequestDetails theRequestDetails,
			@Nonnull BulkExportJobParameters theBulkExportJobParameters) {
		// Set the original request URL as part of the job information, as this is used in the poll-status-endpoint, and
		// is needed for the report.
		theBulkExportJobParameters.setOriginalRequestUrl(theRequestDetails.getCompleteUrl());

		// If no _type parameter is provided, default to all resource types except Binary
		if (theBulkExportJobParameters.getResourceTypes().isEmpty()) {
			List<String> resourceTypes = new ArrayList<>(myDaoRegistry.getRegisteredDaoTypes());
			resourceTypes.remove(BulkDataExportUtil.UNSUPPORTED_BINARY_TYPE);
			theBulkExportJobParameters.setResourceTypes(resourceTypes);
		}

		// Determine and validate partition permissions (if needed).
		RequestPartitionId partitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForServerOperation(
						theRequestDetails, ProviderConstants.OPERATION_EXPORT);
		myRequestPartitionHelperService.validateHasPartitionPermissions(theRequestDetails, "Binary", partitionId);
		theBulkExportJobParameters.setPartitionId(partitionId);
	}

	/**
	 * This method calls STORAGE_PRE_INITIATE_BULK_EXPORT & STORAGE_INITIATE_BULK_EXPORT,
	 * if present, which allows modification to the request and the bulk export job parameters
	 */
	private void callBulkExportHooks(
			@Nonnull ServletRequestDetails theRequestDetails,
			@Nonnull BulkExportJobParameters theBulkExportJobParameters) {
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequestDetails);
		if (compositeBroadcaster.hasHooks(Pointcut.STORAGE_PRE_INITIATE_BULK_EXPORT)) {
			HookParams preInitiateBulkExportHookParams = new HookParams()
					.add(BulkExportJobParameters.class, theBulkExportJobParameters)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			compositeBroadcaster.callHooks(Pointcut.STORAGE_PRE_INITIATE_BULK_EXPORT, preInitiateBulkExportHookParams);
		}

		if (compositeBroadcaster.hasHooks(Pointcut.STORAGE_INITIATE_BULK_EXPORT)) {
			HookParams initiateBulkExportHookParams = (new HookParams())
					.add(BulkExportJobParameters.class, theBulkExportJobParameters)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			compositeBroadcaster.callHooks(Pointcut.STORAGE_INITIATE_BULK_EXPORT, initiateBulkExportHookParams);
		}
	}

	/**
	 * This method checks if the request has the cache-control header
	 * set to no-cache
	 */
	private boolean shouldUseCache(@Nonnull ServletRequestDetails theRequestDetails) {
		CacheControlDirective cacheControlDirective =
				new CacheControlDirective().parse(theRequestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL));
		return myStorageSettings.getEnableBulkExportJobReuse() && !cacheControlDirective.isNoCache();
	}

	/**
	 * This method generates response for the bulk export request
	 * which contains the polling location
	 */
	private void writePollingLocationToResponseHeaders(
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
