/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IRequestPartitionHelperSvc {

	@Nonnull
	RequestPartitionId determineReadPartitionForRequest(
			@Nullable RequestDetails theRequest, ReadPartitionIdRequestDetails theDetails);

	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForRead(
			RequestDetails theRequest, String theResourceType, @Nonnull IIdType theId) {
		ReadPartitionIdRequestDetails details =
				ReadPartitionIdRequestDetails.forRead(theResourceType, theId, theId.hasVersionIdPart());
		return determineReadPartitionForRequest(theRequest, details);
	}

	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForSearchType(
			RequestDetails theRequest,
			String theResourceType,
			SearchParameterMap theParams,
			IBaseResource theConditionalOperationTargetOrNull) {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forSearchType(
				theResourceType, theParams, theConditionalOperationTargetOrNull);
		return determineReadPartitionForRequest(theRequest, details);
	}

	RequestPartitionId determineGenericPartitionForRequest(RequestDetails theRequestDetails);

	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForHistory(
			RequestDetails theRequest, String theResourceType, IIdType theIdType) {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forHistory(theResourceType, theIdType);
		return determineReadPartitionForRequest(theRequest, details);
	}

	@Nonnull
	default void validateHasPartitionPermissions(
			RequestDetails theRequest, String theResourceType, RequestPartitionId theRequestPartitionId) {}

	@Nonnull
	RequestPartitionId determineCreatePartitionForRequest(
			@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource, @Nonnull String theResourceType);

	@Nonnull
	Set<Integer> toReadPartitions(@Nonnull RequestPartitionId theRequestPartitionId);

	boolean isResourcePartitionable(String theResourceType);
}
