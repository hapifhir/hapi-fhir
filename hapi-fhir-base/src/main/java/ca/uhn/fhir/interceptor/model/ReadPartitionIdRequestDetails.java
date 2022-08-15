package ca.uhn.fhir.interceptor.model;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nullable;

public class ReadPartitionIdRequestDetails {

	private final String myResourceType;
	private final RestOperationTypeEnum myRestOperationType;
	private final IIdType myReadResourceId;
	private final Object mySearchParams;
	private final IBaseResource myConditionalTargetOrNull;

	public ReadPartitionIdRequestDetails(String theResourceType, RestOperationTypeEnum theRestOperationType, IIdType theReadResourceId, Object theSearchParams, @Nullable IBaseResource theConditionalTargetOrNull) {
		myResourceType = theResourceType;
		myRestOperationType = theRestOperationType;
		myReadResourceId = theReadResourceId;
		mySearchParams = theSearchParams;
		myConditionalTargetOrNull = theConditionalTargetOrNull;
	}

	public static ReadPartitionIdRequestDetails forRead(String theResourceType, IIdType theId, boolean theIsVread) {
		RestOperationTypeEnum op = theIsVread ? RestOperationTypeEnum.VREAD : RestOperationTypeEnum.READ;
		return new ReadPartitionIdRequestDetails(theResourceType, op, theId.withResourceType(theResourceType), null, null);
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

	public Object getSearchParams() {
		return mySearchParams;
	}

	public IBaseResource getConditionalTargetOrNull() {
		return myConditionalTargetOrNull;
	}

	public static ReadPartitionIdRequestDetails forSearchType(String theResourceType, Object theParams, IBaseResource theConditionalOperationTargetOrNull) {
		return new ReadPartitionIdRequestDetails(theResourceType, RestOperationTypeEnum.SEARCH_TYPE, null, theParams, theConditionalOperationTargetOrNull);
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
		return new ReadPartitionIdRequestDetails(theResourceType, restOperationTypeEnum, theIdType, null, null);
	}
}
