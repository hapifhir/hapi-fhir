/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.interceptor.model;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This is a model class used as a parameter for the interceptor pointcut
 * {@link ca.uhn.fhir.interceptor.api.Pointcut#STORAGE_PARTITION_IDENTIFY_READ}.
 */
public class ReadPartitionIdRequestDetails extends PartitionIdRequestDetails {

	private final String myResourceType;
	private final RestOperationTypeEnum myRestOperationType;
	private final IIdType myReadResourceId;

	@Nullable
	private final SearchParameterMap mySearchParams;

	@Nullable
	private final IBaseResource myConditionalTargetOrNull;

	@Nullable
	private final String mySearchUuid;

	@Nullable
	private final String myExtendedOperationName;

	private ReadPartitionIdRequestDetails(
			String theResourceType,
			RestOperationTypeEnum theRestOperationType,
			IIdType theReadResourceId,
			@Nullable SearchParameterMap theSearchParams,
			@Nullable IBaseResource theConditionalTargetOrNull,
			@Nullable String theSearchUuid,
			String theExtendedOperationName) {
		myResourceType = theResourceType;
		myRestOperationType = theRestOperationType;
		myReadResourceId = theReadResourceId;
		mySearchParams = theSearchParams;
		myConditionalTargetOrNull = theConditionalTargetOrNull;
		mySearchUuid = theSearchUuid;
		myExtendedOperationName = theExtendedOperationName;
	}

	@Nullable
	public String getExtendedOperationName() {
		return myExtendedOperationName;
	}

	@Nullable
	public String getSearchUuid() {
		return mySearchUuid;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public RestOperationTypeEnum getRestOperationType() {
		return myRestOperationType;
	}

	public IIdType getReadResourceId() {
		return myReadResourceId;
	}

	@Nullable
	public SearchParameterMap getSearchParams() {
		return mySearchParams;
	}

	@Nullable
	public IBaseResource getConditionalTargetOrNull() {
		return myConditionalTargetOrNull;
	}

	/**
	 * @param theId The resource ID (must include a resource type and ID)
	 */
	public static ReadPartitionIdRequestDetails forRead(IIdType theId) {
		assert isNotBlank(theId.getResourceType());
		assert isNotBlank(theId.getIdPart());
		return forRead(theId.getResourceType(), theId, false);
	}

	public static ReadPartitionIdRequestDetails forServerOperation(@Nonnull String theOperationName) {
		return new ReadPartitionIdRequestDetails(
				null, RestOperationTypeEnum.EXTENDED_OPERATION_SERVER, null, null, null, null, theOperationName);
	}

	public static ReadPartitionIdRequestDetails forRead(
			String theResourceType, @Nonnull IIdType theId, boolean theIsVread) {
		RestOperationTypeEnum op = theIsVread ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
		return new ReadPartitionIdRequestDetails(
				theResourceType, op, theId.withResourceType(theResourceType), null, null, null, null);
	}

	public static ReadPartitionIdRequestDetails forSearchType(
			String theResourceType, SearchParameterMap theParams, IBaseResource theConditionalOperationTargetOrNull) {
		return new ReadPartitionIdRequestDetails(
				theResourceType,
				RestOperationTypeEnum.SEARCH_TYPE,
				null,
				theParams != null ? theParams : SearchParameterMap.newSynchronous(),
				theConditionalOperationTargetOrNull,
				null,
				null);
	}

	public static ReadPartitionIdRequestDetails forHistory(String theResourceType, IIdType theIdType) {
		RestOperationTypeEnum restOperationTypeEnum;
		if (theIdType != null) {
			restOperationTypeEnum = RestOperationTypeEnum.HISTORY_INSTANCE;
		} else if (theResourceType != null) {
			restOperationTypeEnum = RestOperationTypeEnum.HISTORY_TYPE;
		} else {
			restOperationTypeEnum = RestOperationTypeEnum.HISTORY_SYSTEM;
		}
		return new ReadPartitionIdRequestDetails(
				theResourceType, restOperationTypeEnum, theIdType, null, null, null, null);
	}

	public static ReadPartitionIdRequestDetails forSearchUuid(String theUuid) {
		return new ReadPartitionIdRequestDetails(null, RestOperationTypeEnum.GET_PAGE, null, null, null, theUuid, null);
	}
}
