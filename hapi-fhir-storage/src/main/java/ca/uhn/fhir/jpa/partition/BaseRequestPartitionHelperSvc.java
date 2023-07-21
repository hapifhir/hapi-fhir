/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster.doCallHooks;
import static ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster.doCallHooksAndReturnObject;
import static ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster.hasHooks;

public abstract class BaseRequestPartitionHelperSvc implements IRequestPartitionHelperSvc {

	private final HashSet<Object> myNonPartitionableResourceNames;

	@Autowired
	protected FhirContext myFhirContext;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private PartitionSettings myPartitionSettings;

	public BaseRequestPartitionHelperSvc() {
		myNonPartitionableResourceNames = new HashSet<>();

		// Infrastructure
		myNonPartitionableResourceNames.add("SearchParameter");

		// Validation and Conformance
		myNonPartitionableResourceNames.add("StructureDefinition");
		myNonPartitionableResourceNames.add("Questionnaire");
		myNonPartitionableResourceNames.add("CapabilityStatement");
		myNonPartitionableResourceNames.add("CompartmentDefinition");
		myNonPartitionableResourceNames.add("OperationDefinition");

		myNonPartitionableResourceNames.add("Library");

		// Terminology
		myNonPartitionableResourceNames.add("ConceptMap");
		myNonPartitionableResourceNames.add("CodeSystem");
		myNonPartitionableResourceNames.add("ValueSet");
		myNonPartitionableResourceNames.add("NamingSystem");
		myNonPartitionableResourceNames.add("StructureMap");
	}

	/**
	 * Invoke the {@link Pointcut#STORAGE_PARTITION_IDENTIFY_READ} interceptor pointcut to determine the tenant for a read request.
	 * <p>
	 * If no interceptors are registered with a hook for {@link Pointcut#STORAGE_PARTITION_IDENTIFY_READ}, return
	 * {@link RequestPartitionId#allPartitions()} instead.
	 */
	@Nonnull
	@Override
	public RequestPartitionId determineReadPartitionForRequest(
			@Nullable RequestDetails theRequest, ReadPartitionIdRequestDetails theDetails) {
		RequestPartitionId requestPartitionId;

		String resourceType = theDetails != null ? theDetails.getResourceType() : null;
		boolean nonPartitionableResource = !isResourcePartitionable(resourceType);
		if (myPartitionSettings.isPartitioningEnabled()) {

			RequestDetails requestDetails = theRequest;
			// TODO GGG eventually, theRequest will not be allowed to be null here, and we will pass through
			// SystemRequestDetails instead.
			if (requestDetails == null) {
				requestDetails = new SystemRequestDetails();
			}

			// Handle system requests
			if (requestDetails instanceof SystemRequestDetails
					&& systemRequestHasExplicitPartition((SystemRequestDetails) requestDetails)
					&& !nonPartitionableResource) {
				requestPartitionId =
						getSystemRequestPartitionId((SystemRequestDetails) requestDetails, nonPartitionableResource);
			} else if ((requestDetails instanceof SystemRequestDetails) && nonPartitionableResource) {
				return RequestPartitionId.fromPartitionId(myPartitionSettings.getDefaultPartitionId());
			} else if (hasHooks(Pointcut.STORAGE_PARTITION_IDENTIFY_ANY, myInterceptorBroadcaster, requestDetails)) {
				// Interceptor call: STORAGE_PARTITION_IDENTIFY_ANY
				HookParams params = new HookParams()
						.add(RequestDetails.class, requestDetails)
						.addIfMatchesType(ServletRequestDetails.class, requestDetails);
				requestPartitionId = (RequestPartitionId) doCallHooksAndReturnObject(
						myInterceptorBroadcaster, requestDetails, Pointcut.STORAGE_PARTITION_IDENTIFY_ANY, params);
			} else if (hasHooks(Pointcut.STORAGE_PARTITION_IDENTIFY_READ, myInterceptorBroadcaster, requestDetails)) {
				// Interceptor call: STORAGE_PARTITION_IDENTIFY_READ
				HookParams params = new HookParams()
						.add(RequestDetails.class, requestDetails)
						.addIfMatchesType(ServletRequestDetails.class, requestDetails)
						.add(ReadPartitionIdRequestDetails.class, theDetails);
				requestPartitionId = (RequestPartitionId) doCallHooksAndReturnObject(
						myInterceptorBroadcaster, requestDetails, Pointcut.STORAGE_PARTITION_IDENTIFY_READ, params);
			} else {
				requestPartitionId = null;
			}

			validateRequestPartitionNotNull(requestPartitionId, Pointcut.STORAGE_PARTITION_IDENTIFY_READ);

			return validateNormalizeAndNotifyHooksForRead(requestPartitionId, requestDetails, resourceType);
		}

		return RequestPartitionId.allPartitions();
	}

	@Override
	public RequestPartitionId determineGenericPartitionForRequest(RequestDetails theRequestDetails) {
		RequestPartitionId retVal = null;

		if (myPartitionSettings.isPartitioningEnabled()) {
			if (theRequestDetails instanceof SystemRequestDetails) {
				SystemRequestDetails systemRequestDetails = (SystemRequestDetails) theRequestDetails;
				retVal = systemRequestDetails.getRequestPartitionId();
			}
		}

		if (retVal == null) {
			if (hasHooks(Pointcut.STORAGE_PARTITION_IDENTIFY_ANY, myInterceptorBroadcaster, theRequestDetails)) {
				// Interceptor call: STORAGE_PARTITION_IDENTIFY_ANY
				HookParams params = new HookParams()
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				retVal = (RequestPartitionId) doCallHooksAndReturnObject(
						myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PARTITION_IDENTIFY_ANY, params);

				if (retVal != null) {
					retVal = validateNormalizeAndNotifyHooksForRead(retVal, theRequestDetails, null);
				}
			}
		}

		return retVal;
	}

	/**
	 * For system requests, read partition from tenant ID if present, otherwise set to DEFAULT. If the resource they are attempting to partition
	 * is non-partitionable scream in the logs and set the partition to DEFAULT.
	 */
	private RequestPartitionId getSystemRequestPartitionId(
			SystemRequestDetails theRequest, boolean theNonPartitionableResource) {
		RequestPartitionId requestPartitionId;
		requestPartitionId = getSystemRequestPartitionId(theRequest);
		if (theNonPartitionableResource && !requestPartitionId.isDefaultPartition()) {
			throw new InternalErrorException(Msg.code(1315)
					+ "System call is attempting to write a non-partitionable resource to a partition! This is a bug!");
		}
		return requestPartitionId;
	}

	/**
	 * Determine the partition for a System Call (defined by the fact that the request is of type SystemRequestDetails)
	 * <p>
	 * 1. If the tenant ID is set to the constant for all partitions, return all partitions
	 * 2. If there is a tenant ID set in the request, use it.
	 * 3. Otherwise, return the Default Partition.
	 *
	 * @param theRequest The {@link SystemRequestDetails}
	 * @return the {@link RequestPartitionId} to be used for this request.
	 */
	@Nonnull
	private RequestPartitionId getSystemRequestPartitionId(@Nonnull SystemRequestDetails theRequest) {
		if (theRequest.getRequestPartitionId() != null) {
			return theRequest.getRequestPartitionId();
		}
		if (theRequest.getTenantId() != null) {
			// TODO: JA2 we should not be inferring the partition name from the tenant name
			return RequestPartitionId.fromPartitionName(theRequest.getTenantId());
		} else {
			return RequestPartitionId.defaultPartition();
		}
	}

	/**
	 * Invoke the {@link Pointcut#STORAGE_PARTITION_IDENTIFY_CREATE} interceptor pointcut to determine the tenant for a create request.
	 */
	@Nonnull
	@Override
	public RequestPartitionId determineCreatePartitionForRequest(
			@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource, @Nonnull String theResourceType) {
		RequestPartitionId requestPartitionId;

		if (myPartitionSettings.isPartitioningEnabled()) {
			boolean nonPartitionableResource = myNonPartitionableResourceNames.contains(theResourceType);

			// TODO GGG eventually, theRequest will not be allowed to be null here, and we will pass through
			// SystemRequestDetails instead.
			if ((theRequest == null || theRequest instanceof SystemRequestDetails) && nonPartitionableResource) {
				return RequestPartitionId.defaultPartition();
			}

			if (theRequest instanceof SystemRequestDetails
					&& systemRequestHasExplicitPartition((SystemRequestDetails) theRequest)) {
				requestPartitionId =
						getSystemRequestPartitionId((SystemRequestDetails) theRequest, nonPartitionableResource);
			} else {
				if (hasHooks(Pointcut.STORAGE_PARTITION_IDENTIFY_ANY, myInterceptorBroadcaster, theRequest)) {
					// Interceptor call: STORAGE_PARTITION_IDENTIFY_ANY
					HookParams params = new HookParams()
							.add(RequestDetails.class, theRequest)
							.addIfMatchesType(ServletRequestDetails.class, theRequest);
					requestPartitionId = (RequestPartitionId) doCallHooksAndReturnObject(
							myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_IDENTIFY_ANY, params);
				} else {
					// This is an external Request (e.g. ServletRequestDetails) so we want to figure out the partition
					// via interceptor.
					// Interceptor call: STORAGE_PARTITION_IDENTIFY_CREATE
					HookParams params = new HookParams()
							.add(IBaseResource.class, theResource)
							.add(RequestDetails.class, theRequest)
							.addIfMatchesType(ServletRequestDetails.class, theRequest);
					requestPartitionId = (RequestPartitionId) doCallHooksAndReturnObject(
							myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE, params);
				}

				// If the interceptors haven't selected a partition, and its a non-partitionable resource anyhow, send
				// to DEFAULT
				if (nonPartitionableResource && requestPartitionId == null) {
					requestPartitionId = RequestPartitionId.defaultPartition();
				}
			}

			String resourceName = myFhirContext.getResourceType(theResource);
			validateSinglePartitionForCreate(
					requestPartitionId, resourceName, Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE);

			return validateNormalizeAndNotifyHooksForRead(requestPartitionId, theRequest, theResourceType);
		}

		return RequestPartitionId.allPartitions();
	}

	private boolean systemRequestHasExplicitPartition(@Nonnull SystemRequestDetails theRequest) {
		return theRequest.getRequestPartitionId() != null || theRequest.getTenantId() != null;
	}

	@Nonnull
	@Override
	public Set<Integer> toReadPartitions(@Nonnull RequestPartitionId theRequestPartitionId) {
		return theRequestPartitionId.getPartitionIds().stream()
				.map(t -> t == null ? myPartitionSettings.getDefaultPartitionId() : t)
				.collect(Collectors.toSet());
	}

	/**
	 * If the partition only has a name but not an ID, this method resolves the ID.
	 * <p>
	 * If the partition has an ID but not a name, the name is resolved.
	 * <p>
	 * If the partition has both, they are validated to ensure that they correspond.
	 */
	@Nonnull
	private RequestPartitionId validateNormalizeAndNotifyHooksForRead(
			@Nonnull RequestPartitionId theRequestPartitionId,
			RequestDetails theRequest,
			@Nullable String theResourceType) {
		RequestPartitionId retVal = theRequestPartitionId;

		if (!myPartitionSettings.isUnnamedPartitionMode()) {
			if (retVal.getPartitionNames() != null) {
				retVal = validateAndNormalizePartitionNames(retVal);
			} else if (retVal.hasPartitionIds()) {
				retVal = validateAndNormalizePartitionIds(retVal);
			}
		}

		// Note: It's still possible that the partition only has a date but no name/id

		if (StringUtils.isNotBlank(theResourceType)) {
			validateHasPartitionPermissions(theRequest, theResourceType, retVal);
		}

		return retVal;
	}

	@Override
	public void validateHasPartitionPermissions(
			RequestDetails theRequest, String theResourceType, RequestPartitionId theRequestPartitionId) {
		if (myInterceptorBroadcaster.hasHooks(Pointcut.STORAGE_PARTITION_SELECTED)) {
			RuntimeResourceDefinition runtimeResourceDefinition = null;
			if (theResourceType != null) {
				runtimeResourceDefinition = myFhirContext.getResourceDefinition(theResourceType);
			}
			HookParams params = new HookParams()
					.add(RequestPartitionId.class, theRequestPartitionId)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(RuntimeResourceDefinition.class, runtimeResourceDefinition);
			doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_SELECTED, params);
		}
	}

	@Override
	public boolean isResourcePartitionable(String theResourceType) {
		return !myNonPartitionableResourceNames.contains(theResourceType);
	}

	protected abstract RequestPartitionId validateAndNormalizePartitionIds(RequestPartitionId theRequestPartitionId);

	protected abstract RequestPartitionId validateAndNormalizePartitionNames(RequestPartitionId theRequestPartitionId);

	private void validateSinglePartitionForCreate(
			RequestPartitionId theRequestPartitionId, @Nonnull String theResourceName, Pointcut thePointcut) {
		validateRequestPartitionNotNull(theRequestPartitionId, thePointcut);

		if (theRequestPartitionId.hasPartitionIds()) {
			validateSinglePartitionIdOrNameForCreate(theRequestPartitionId.getPartitionIds());
		}
		validateSinglePartitionIdOrNameForCreate(theRequestPartitionId.getPartitionNames());

		// Make sure we're not using one of the conformance resources in a non-default partition
		if ((theRequestPartitionId.hasPartitionIds()
						&& !theRequestPartitionId.getPartitionIds().contains(null))
				|| (theRequestPartitionId.hasPartitionNames()
						&& !theRequestPartitionId.getPartitionNames().contains(JpaConstants.DEFAULT_PARTITION_NAME))) {

			if (!isResourcePartitionable(theResourceName)) {
				String msg = myFhirContext
						.getLocalizer()
						.getMessageSanitized(
								BaseRequestPartitionHelperSvc.class,
								"nonDefaultPartitionSelectedForNonPartitionable",
								theResourceName);
				throw new UnprocessableEntityException(Msg.code(1318) + msg);
			}
		}
	}

	private void validateRequestPartitionNotNull(RequestPartitionId theRequestPartitionId, Pointcut theThePointcut) {
		if (theRequestPartitionId == null) {
			throw new InternalErrorException(
					Msg.code(1319) + "No interceptor provided a value for pointcut: " + theThePointcut);
		}
	}

	private void validateSinglePartitionIdOrNameForCreate(@Nullable List<?> thePartitionIds) {
		if (thePartitionIds != null && thePartitionIds.size() != 1) {
			throw new InternalErrorException(
					Msg.code(1320) + "RequestPartitionId must contain a single partition for create operations, found: "
							+ thePartitionIds);
		}
	}
}
