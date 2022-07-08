package ca.uhn.fhir.jpa.partition;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.ALL_PARTITIONS_NAME;
import static ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster.doCallHooks;
import static ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster.doCallHooksAndReturnObject;
import static ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster.hasHooks;
import static org.slf4j.LoggerFactory.getLogger;

public class RequestPartitionHelperSvc implements IRequestPartitionHelperSvc {
	private static final Logger ourLog = getLogger(RequestPartitionHelperSvc.class);

	private final HashSet<Object> myNonPartitionableResourceNames;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private IPartitionLookupSvc myPartitionConfigSvc;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private PartitionSettings myPartitionSettings;

	public RequestPartitionHelperSvc() {
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
	public RequestPartitionId determineReadPartitionForRequest(@Nullable RequestDetails theRequest, String theResourceType, ReadPartitionIdRequestDetails theDetails) {
		RequestPartitionId requestPartitionId;

		boolean nonPartitionableResource = myNonPartitionableResourceNames.contains(theResourceType);
		if (myPartitionSettings.isPartitioningEnabled()) {
			// Handle system requests
			//TODO GGG eventually, theRequest will not be allowed to be null here, and we will pass through SystemRequestDetails instead.
			if ((theRequest == null || theRequest instanceof SystemRequestDetails) && nonPartitionableResource) {
				return RequestPartitionId.defaultPartition();
			}

			if (theRequest instanceof SystemRequestDetails && systemRequestHasExplicitPartition((SystemRequestDetails) theRequest)) {
				requestPartitionId = getSystemRequestPartitionId((SystemRequestDetails) theRequest, nonPartitionableResource);
			} else if (hasHooks(Pointcut.STORAGE_PARTITION_IDENTIFY_READ, myInterceptorBroadcaster, theRequest)) {
				// Interceptor call: STORAGE_PARTITION_IDENTIFY_READ
				HookParams params = new HookParams()
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest)
					.add(ReadPartitionIdRequestDetails.class, theDetails);
				requestPartitionId = (RequestPartitionId) doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_IDENTIFY_READ, params);
			} else {
				requestPartitionId = null;
			}

			validateRequestPartitionNotNull(requestPartitionId, Pointcut.STORAGE_PARTITION_IDENTIFY_READ);

			return validateNormalizeAndNotifyHooksForRead(requestPartitionId, theRequest, theResourceType);
		}

		return RequestPartitionId.allPartitions();
	}

	/**
	 * For system requests, read partition from tenant ID if present, otherwise set to DEFAULT. If the resource they are attempting to partition
	 * is non-partitionable scream in the logs and set the partition to DEFAULT.
	 *
	 */
	private RequestPartitionId getSystemRequestPartitionId(SystemRequestDetails theRequest, boolean theNonPartitionableResource) {
		RequestPartitionId requestPartitionId;
		requestPartitionId = getSystemRequestPartitionId(theRequest);
		if (theNonPartitionableResource && !requestPartitionId.isDefaultPartition()) {
			throw new InternalErrorException(Msg.code(1315) + "System call is attempting to write a non-partitionable resource to a partition! This is a bug!");
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
			if (theRequest.getTenantId().equals(ALL_PARTITIONS_NAME)) {
				return RequestPartitionId.allPartitions();
			} else {
				return RequestPartitionId.fromPartitionName(theRequest.getTenantId());
			}
		} else {
			return RequestPartitionId.defaultPartition();
		}
	}

	/**
	 * Invoke the {@link Pointcut#STORAGE_PARTITION_IDENTIFY_CREATE} interceptor pointcut to determine the tenant for a create request.
	 */
	@Nonnull
	@Override
	public RequestPartitionId determineCreatePartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource, @Nonnull String theResourceType) {
		RequestPartitionId requestPartitionId;

		if (myPartitionSettings.isPartitioningEnabled()) {
			boolean nonPartitionableResource = myNonPartitionableResourceNames.contains(theResourceType);

			//TODO GGG eventually, theRequest will not be allowed to be null here, and we will pass through SystemRequestDetails instead.
			if ((theRequest == null || theRequest instanceof SystemRequestDetails) && nonPartitionableResource) {
				return RequestPartitionId.defaultPartition();
			}

			if (theRequest instanceof SystemRequestDetails && systemRequestHasExplicitPartition((SystemRequestDetails) theRequest)) {
				requestPartitionId = getSystemRequestPartitionId((SystemRequestDetails) theRequest, nonPartitionableResource);
			} else {
				//This is an external Request (e.g. ServletRequestDetails) so we want to figure out the partition via interceptor.
				// Interceptor call: STORAGE_PARTITION_IDENTIFY_CREATE
				HookParams params = new HookParams()
					.add(IBaseResource.class, theResource)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
				requestPartitionId = (RequestPartitionId) doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE, params);

				//If the interceptors haven't selected a partition, and its a non-partitionable resource anyhow, send to DEFAULT
				if (nonPartitionableResource && requestPartitionId == null) {
					requestPartitionId = RequestPartitionId.defaultPartition();
				}
			}

			String resourceName = myFhirContext.getResourceType(theResource);
			validateSinglePartitionForCreate(requestPartitionId, resourceName, Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE);

			return validateNormalizeAndNotifyHooksForRead(requestPartitionId, theRequest, theResourceType);
		}

		return RequestPartitionId.allPartitions();
	}

	private boolean systemRequestHasExplicitPartition(@Nonnull SystemRequestDetails theRequest) {
		return theRequest.getRequestPartitionId() != null || theRequest.getTenantId() != null;
	}

	@Nonnull
	@Override
	public PartitionablePartitionId toStoragePartition(@Nonnull RequestPartitionId theRequestPartitionId) {
		Integer partitionId = theRequestPartitionId.getFirstPartitionIdOrNull();
		if (partitionId == null) {
			partitionId = myPartitionSettings.getDefaultPartitionId();
		}
		return new PartitionablePartitionId(partitionId, theRequestPartitionId.getPartitionDate());
	}

	@Nonnull
	@Override
	public Set<Integer> toReadPartitions(@Nonnull RequestPartitionId theRequestPartitionId) {
		return theRequestPartitionId
			.getPartitionIds()
			.stream()
			.map(t->t == null ? myPartitionSettings.getDefaultPartitionId() : t)
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
	private RequestPartitionId validateNormalizeAndNotifyHooksForRead(@Nonnull RequestPartitionId theRequestPartitionId, RequestDetails theRequest, @Nonnull String theResourceType) {
		RequestPartitionId retVal = theRequestPartitionId;

		if (retVal.getPartitionNames() != null) {
			retVal = validateAndNormalizePartitionNames(retVal);
		} else if (retVal.hasPartitionIds()) {
			retVal = validateAndNormalizePartitionIds(retVal);
		}

		// Note: It's still possible that the partition only has a date but no name/id

		if (StringUtils.isNotBlank(theResourceType)) {
			validateHasPartitionPermissions(theRequest, theResourceType, retVal);
		}

		return retVal;

	}

	public void validateHasPartitionPermissions(RequestDetails theRequest, String theResourceType, RequestPartitionId theRequestPartitionId) {
		if (myInterceptorBroadcaster.hasHooks(Pointcut.STORAGE_PARTITION_SELECTED)) {
			RuntimeResourceDefinition runtimeResourceDefinition;
			runtimeResourceDefinition = myFhirContext.getResourceDefinition(theResourceType);
			HookParams params = new HookParams()
				.add(RequestPartitionId.class, theRequestPartitionId)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(RuntimeResourceDefinition.class, runtimeResourceDefinition);
			doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PARTITION_SELECTED, params);
		}
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
					throw new ResourceNotFoundException(Msg.code(1316) + msg);
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
				throw new ResourceNotFoundException(Msg.code(1317) + msg);
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

			if (myNonPartitionableResourceNames.contains(theResourceName)) {
				String msg = myFhirContext.getLocalizer().getMessageSanitized(RequestPartitionHelperSvc.class, "nonDefaultPartitionSelectedForNonPartitionable", theResourceName);
				throw new UnprocessableEntityException(Msg.code(1318) + msg);
			}

		}

	}

	private void validateRequestPartitionNotNull(RequestPartitionId theRequestPartitionId, Pointcut theThePointcut) {
		if (theRequestPartitionId == null) {
			throw new InternalErrorException(Msg.code(1319) + "No interceptor provided a value for pointcut: " + theThePointcut);
		}
	}

	private void validateSinglePartitionIdOrNameForCreate(@Nullable List<?> thePartitionIds) {
		if (thePartitionIds != null && thePartitionIds.size() != 1) {
			throw new InternalErrorException(Msg.code(1320) + "RequestPartitionId must contain a single partition for create operations, found: " + thePartitionIds);
		}
	}
}
