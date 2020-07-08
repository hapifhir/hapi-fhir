package ca.uhn.fhir.jpa.partition;

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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.util.HashSet;

import static ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster.doCallHooks;
import static ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster.doCallHooksAndReturnObject;
import static ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster.hasHooks;

public class RequestPartitionHelperSvc implements IRequestPartitionHelperSvc {

	private final HashSet<Object> myPartitioningBlacklist;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private IPartitionLookupSvc myPartitionConfigSvc;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private PartitionSettings myPartitionSettings;

	public RequestPartitionHelperSvc() {
		myPartitioningBlacklist = new HashSet<>();

		// Infrastructure
		myPartitioningBlacklist.add("Subscription");
		myPartitioningBlacklist.add("SearchParameter");

		// Validation
		myPartitioningBlacklist.add("StructureDefinition");
		myPartitioningBlacklist.add("Questionnaire");

		// Terminology
		myPartitioningBlacklist.add("ConceptMap");
		myPartitioningBlacklist.add("CodeSystem");
		myPartitioningBlacklist.add("ValueSet");
	}

	/**
	 * Invoke the {@link Pointcut#STORAGE_PARTITION_IDENTIFY_READ} interceptor pointcut to determine the tenant for a read request.
	 *
	 * If no interceptors are registered with a hook for {@link Pointcut#STORAGE_PARTITION_IDENTIFY_READ}, return
	 * {@link RequestPartitionId#allPartitions()} instead.
	 */
	@Nonnull
	@Override
	public RequestPartitionId determineReadPartitionForRequest(@Nullable RequestDetails theRequest, String theResourceType) {
		RequestPartitionId requestPartitionId;

		if (myPartitionSettings.isPartitioningEnabled()) {
			// Handle system requests
			if (theRequest == null && myPartitioningBlacklist.contains(theResourceType)) {
				return RequestPartitionId.defaultPartition();
			}

			// Interceptor call: STORAGE_PARTITION_IDENTIFY_READ
			if (hasHooks(Pointcut.STORAGE_PARTITION_IDENTIFY_READ, myInterceptorBroadcaster, theRequest)) {
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
				requestPartitionId = (RequestPartitionId) doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_IDENTIFY_READ, params);
			} else {
				requestPartitionId = RequestPartitionId.allPartitions();
			}

			validatePartition(requestPartitionId, theResourceType, Pointcut.STORAGE_PARTITION_IDENTIFY_READ);

			return normalizeAndNotifyHooks(requestPartitionId, theRequest);
		}

		return RequestPartitionId.allPartitions();
	}

	/**
	 * Invoke the {@link Pointcut#STORAGE_PARTITION_IDENTIFY_CREATE} interceptor pointcut to determine the tenant for a create request.
	 */
	@Nonnull
	@Override
	public RequestPartitionId determineCreatePartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource, @Nonnull String theResourceType) {
		RequestPartitionId requestPartitionId;

		if (myPartitionSettings.isPartitioningEnabled()) {
			// Handle system requests
			if (theRequest == null && myPartitioningBlacklist.contains(theResourceType)) {
				return RequestPartitionId.defaultPartition();
			}

			// Interceptor call: STORAGE_PARTITION_IDENTIFY_CREATE
			HookParams params = new HookParams()
				.add(IBaseResource.class, theResource)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			requestPartitionId = (RequestPartitionId) doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE, params);

			String resourceName = myFhirContext.getResourceType(theResource);
			validatePartition(requestPartitionId, resourceName, Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE);

			return normalizeAndNotifyHooks(requestPartitionId, theRequest);
		}

		return RequestPartitionId.allPartitions();
	}

	/**
	 * If the partition only has a name but not an ID, this method resolves the ID
	 */
	@Nonnull
	private RequestPartitionId normalizeAndNotifyHooks(@Nonnull RequestPartitionId theRequestPartitionId, RequestDetails theRequest) {
		RequestPartitionId retVal = theRequestPartitionId;

		if (retVal.getPartitionName() != null) {

			PartitionEntity partition;
			try {
				partition = myPartitionConfigSvc.getPartitionByName(retVal.getPartitionName());
			} catch (IllegalArgumentException e) {
				String msg = myFhirContext.getLocalizer().getMessage(RequestPartitionHelperSvc.class, "unknownPartitionName", retVal.getPartitionName());
				throw new ResourceNotFoundException(msg);
			}

			if (retVal.getPartitionId() != null) {
				Validate.isTrue(retVal.getPartitionId().equals(partition.getId()), "Partition name %s does not match ID %n", retVal.getPartitionName(), retVal.getPartitionId());
			} else {
				retVal = RequestPartitionId.forPartitionIdAndName(partition.getId(), retVal.getPartitionName(), retVal.getPartitionDate());
			}

		} else if (retVal.getPartitionId() != null) {

			PartitionEntity partition;
			try {
				partition = myPartitionConfigSvc.getPartitionById(retVal.getPartitionId());
			} catch (IllegalArgumentException e) {
				String msg = myFhirContext.getLocalizer().getMessage(RequestPartitionHelperSvc.class, "unknownPartitionId", retVal.getPartitionId());
				throw new ResourceNotFoundException(msg);
			}
			retVal = RequestPartitionId.forPartitionIdAndName(partition.getId(), partition.getName(), retVal.getPartitionDate());

		}

		// Note: It's still possible that the partition only has a date but no name/id

		HookParams params = new HookParams()
			.add(RequestPartitionId.class, retVal)
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest);
		doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_SELECTED, params);

		return retVal;

	}

	private void validatePartition(@Nonnull RequestPartitionId theRequestPartitionId, @Nonnull String theResourceName, Pointcut thePointcut) {
		Validate.notNull(theRequestPartitionId, "Interceptor did not provide a value for pointcut: %s", thePointcut);

		if (theRequestPartitionId.getPartitionId() != null) {

			// Make sure we're not using one of the conformance resources in a non-default partition
			if (myPartitioningBlacklist.contains(theResourceName)) {
				String msg = myFhirContext.getLocalizer().getMessageSanitized(RequestPartitionHelperSvc.class, "blacklistedResourceTypeForPartitioning", theResourceName);
				throw new UnprocessableEntityException(msg);
			}

			// Make sure the partition exists
			try {
				myPartitionConfigSvc.getPartitionById(theRequestPartitionId.getPartitionId());
			} catch (IllegalArgumentException e) {
				String msg = myFhirContext.getLocalizer().getMessageSanitized(RequestPartitionHelperSvc.class, "unknownPartitionId", theRequestPartitionId.getPartitionId());
				throw new InvalidRequestException(msg);
			}

		}
	}
}
