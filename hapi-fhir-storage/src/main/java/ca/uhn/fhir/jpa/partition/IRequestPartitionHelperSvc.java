package ca.uhn.fhir.jpa.partition;

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

import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public interface IRequestPartitionHelperSvc {

	@Nonnull
	RequestPartitionId determineReadPartitionForRequest(@Nullable RequestDetails theRequest, String theResourceType, ReadPartitionIdRequestDetails theDetails);

	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForRead(RequestDetails theRequest, String theResourceType, IIdType theId) {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forRead(theResourceType, theId, theId.hasVersionIdPart());
		return determineReadPartitionForRequest(theRequest, theResourceType, details);
	}

	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForSearchType(RequestDetails theRequest, String theResourceType, SearchParameterMap theParams, IBaseResource theConditionalOperationTargetOrNull) {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forSearchType(theResourceType, theParams, theConditionalOperationTargetOrNull);
		return determineReadPartitionForRequest(theRequest, theResourceType, details);
	}

	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForHistory(RequestDetails theRequest, String theResourceType, IIdType theIdType) {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forHistory(theResourceType, theIdType);
		return determineReadPartitionForRequest(theRequest, theResourceType, details);
	}

	@Nonnull
	default void validateHasPartitionPermissions(RequestDetails theRequest, String theResourceType, RequestPartitionId theRequestPartitionId){}

	@Nonnull
	RequestPartitionId determineCreatePartitionForRequest(@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource, @Nonnull String theResourceType);

	@Nonnull
	PartitionablePartitionId toStoragePartition(@Nonnull RequestPartitionId theRequestPartitionId);

	@Nonnull
	Set<Integer> toReadPartitions(@Nonnull RequestPartitionId theRequestPartitionId);
}
