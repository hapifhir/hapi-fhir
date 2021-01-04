package ca.uhn.fhir.jpa.partition;

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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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
	 * <p>
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
				requestPartitionId = null;
			}

			validateRequestPartitionNotNull(requestPartitionId, Pointcut.STORAGE_PARTITION_IDENTIFY_READ);

			return validateNormalizeAndNotifyHooksForRead(requestPartitionId, theRequest);
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
			validateSinglePartitionForCreate(requestPartitionId, resourceName, Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE);

			return validateNormalizeAndNotifyHooksForRead(requestPartitionId, theRequest);
		}

		return RequestPartitionId.allPartitions();
	}

	/**
	 * If the partition only has a name but not an ID, this method resolves the ID.
	 * <p>
	 * If the partition has an ID but not a name, the name is resolved.
	 * <p>
	 * If the partition has both, they are validated to ensure that they correspond.
	 */
	@Nonnull
	private RequestPartitionId validateNormalizeAndNotifyHooksForRead(@Nonnull RequestPartitionId theRequestPartitionId, RequestDetails theRequest) {
		RequestPartitionId retVal = theRequestPartitionId;

		if (retVal.getPartitionNames() != null) {
			retVal = validateAndNormalizePartitionNames(retVal);
		} else if (retVal.hasPartitionIds()) {
			retVal = validateAndNormalizePartitionIds(retVal);
		}

		// Note: It's still possible that the partition only has a date but no name/id

		HookParams params = new HookParams()
			.add(RequestPartitionId.class, retVal)
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest);
		doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_SELECTED, params);

		return retVal;

	}

	private RequestPartitionId validateAndNormalizePartitionIds(RequestPartitionId theRequestPartitionId) {
		List<String> names = null;
		for (int i = 0; i < theRequestPartitionId.getPartitionIds().size(); i++) {

			PartitionEntity partition;
			Integer id = theRequestPartitionId.getPartitionIds().get(i);
			if (id == null) {
				partition = null;
			} else {
				try {
					partition = myPartitionConfigSvc.getPartitionById(id);
				} catch (IllegalArgumentException e) {
					String msg = myFhirContext.getLocalizer().getMessage(RequestPartitionHelperSvc.class, "unknownPartitionId", theRequestPartitionId.getPartitionIds().get(i));
					throw new ResourceNotFoundException(msg);
				}
			}

			if (theRequestPartitionId.getPartitionNames() != null) {
				if (partition == null) {
					Validate.isTrue(theRequestPartitionId.getPartitionIds().get(i) == null, "Partition %s must not have an ID", JpaConstants.DEFAULT_PARTITION_NAME);
				} else {
					Validate.isTrue(Objects.equals(theRequestPartitionId.getPartitionIds().get(i), partition.getId()), "Partition name %s does not match ID %n", theRequestPartitionId.getPartitionNames().get(i), theRequestPartitionId.getPartitionIds().get(i));
				}
			} else {
				if (names == null) {
					names = new ArrayList<>();
				}
				if (partition != null) {
					names.add(partition.getName());
				} else {
					names.add(null);
				}
			}

		}

		if (names != null) {
			return RequestPartitionId.forPartitionIdsAndNames(names, theRequestPartitionId.getPartitionIds(), theRequestPartitionId.getPartitionDate());
		}

		return theRequestPartitionId;
	}

	private RequestPartitionId validateAndNormalizePartitionNames(RequestPartitionId theRequestPartitionId) {
		List<Integer> ids = null;
		for (int i = 0; i < theRequestPartitionId.getPartitionNames().size(); i++) {

			PartitionEntity partition;
			try {
				partition = myPartitionConfigSvc.getPartitionByName(theRequestPartitionId.getPartitionNames().get(i));
			} catch (IllegalArgumentException e) {
				String msg = myFhirContext.getLocalizer().getMessage(RequestPartitionHelperSvc.class, "unknownPartitionName", theRequestPartitionId.getPartitionNames().get(i));
				throw new ResourceNotFoundException(msg);
			}

			if (theRequestPartitionId.hasPartitionIds()) {
				if (partition == null) {
					Validate.isTrue(theRequestPartitionId.getPartitionIds().get(i) == null, "Partition %s must not have an ID", JpaConstants.DEFAULT_PARTITION_NAME);
				} else {
					Validate.isTrue(Objects.equals(theRequestPartitionId.getPartitionIds().get(i), partition.getId()), "Partition name %s does not match ID %n", theRequestPartitionId.getPartitionNames().get(i), theRequestPartitionId.getPartitionIds().get(i));
				}
			} else {
				if (ids == null) {
					ids = new ArrayList<>();
				}
				if (partition != null) {
					ids.add(partition.getId());
				} else {
					ids.add(null);
				}
			}

		}

		if (ids != null) {
			return RequestPartitionId.forPartitionIdsAndNames(theRequestPartitionId.getPartitionNames(), ids, theRequestPartitionId.getPartitionDate());
		}

		return theRequestPartitionId;
	}

	private void validateSinglePartitionForCreate(RequestPartitionId theRequestPartitionId, @Nonnull String theResourceName, Pointcut thePointcut) {
		validateRequestPartitionNotNull(theRequestPartitionId, thePointcut);

		if (theRequestPartitionId.hasPartitionIds()) {
			validateSinglePartitionIdOrNameForCreate(theRequestPartitionId.getPartitionIds());
		}
		validateSinglePartitionIdOrNameForCreate(theRequestPartitionId.getPartitionNames());

		// Make sure we're not using one of the conformance resources in a non-default partition
		if ((theRequestPartitionId.hasPartitionIds() && !theRequestPartitionId.getPartitionIds().contains(null)) ||
			(theRequestPartitionId.hasPartitionNames() && !theRequestPartitionId.getPartitionNames().contains(JpaConstants.DEFAULT_PARTITION_NAME))) {

			if (myPartitioningBlacklist.contains(theResourceName)) {
				String msg = myFhirContext.getLocalizer().getMessageSanitized(RequestPartitionHelperSvc.class, "blacklistedResourceTypeForPartitioning", theResourceName);
				throw new UnprocessableEntityException(msg);
			}

		}

	}

	private void validateRequestPartitionNotNull(RequestPartitionId theTheRequestPartitionId, Pointcut theThePointcut) {
		if (theTheRequestPartitionId == null) {
			throw new InternalErrorException("No interceptor provided a value for pointcut: " + theThePointcut);
		}
	}

	private void validateSinglePartitionIdOrNameForCreate(@Nullable List<?> thePartitionIds) {
		if (thePartitionIds != null && thePartitionIds.size() != 1) {
			throw new InternalErrorException("RequestPartitionId must contain a single partition for create operations, found: " + thePartitionIds);
		}
	}
}
