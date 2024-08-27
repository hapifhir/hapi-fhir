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
package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

public interface IRequestPartitionHelperSvc {

	@Nonnull
	RequestPartitionId determineReadPartitionForRequest(
			@Nullable RequestDetails theRequest, @Nonnull ReadPartitionIdRequestDetails theDetails);

	/**
	 * Determine partition to use when performing a server operation such as $bulk-import, $bulk-export, $reindex etc.
	 * @param theRequest the request details from the context of the call
	 * @param theOperationName the explicit name of the operation
	 * @return the partition id which should be used for the operation
	 */
	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForServerOperation(
			@Nullable RequestDetails theRequest, @Nonnull String theOperationName) {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forServerOperation(theOperationName);
		return determineReadPartitionForRequest(theRequest, details);
	}

	/**
	 * Determine partition to use when performing database reads based on a resource instance.
	 * @param theRequest the request details from the context of the call
	 * @param theId the id of the resource instance
	 * @return the partition id which should be used for the database read
	 */
	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForRead(
			@Nullable RequestDetails theRequest, @Nonnull IIdType theId) {
		ReadPartitionIdRequestDetails details =
				ReadPartitionIdRequestDetails.forRead(theId.getResourceType(), theId, theId.hasVersionIdPart());
		return determineReadPartitionForRequest(theRequest, details);
	}

	/**
	 * Determine partition to use when performing database reads against a certain resource type based on a resource instance.
	 * @param theRequest the request details from the context of the call
	 * @param theResourceType the resource type
	 * @param theId the id of the resource instance
	 * @return the partition id which should be used for the database read
	 */
	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForRead(
			@Nullable RequestDetails theRequest, @Nonnull String theResourceType, @Nonnull IIdType theId) {
		ReadPartitionIdRequestDetails details =
				ReadPartitionIdRequestDetails.forRead(theResourceType, theId, theId.hasVersionIdPart());
		return determineReadPartitionForRequest(theRequest, details);
	}

	/**
	 * Determine partition to use when performing a database search against a certain resource type.
	 * @param theRequest the request details from the context of the call
	 * @param theResourceType the resource type
	 * @return the partition id which should be used for the database search
	 */
	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForSearchType(
			@Nullable RequestDetails theRequest, @Nonnull String theResourceType) {
		ReadPartitionIdRequestDetails details =
				ReadPartitionIdRequestDetails.forSearchType(theResourceType, SearchParameterMap.newSynchronous(), null);
		return determineReadPartitionForRequest(theRequest, details);
	}

	/**
	 * Determine partition to use when performing a database search based on a resource type and other search parameters.
	 * @param theRequest the request details from the context of the call
	 * @param theResourceType the resource type
	 * @param theParams the search parameters
	 * @return the partition id which should be used for the database search
	 */
	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForSearchType(
			@Nullable RequestDetails theRequest,
			@Nonnull String theResourceType,
			@Nonnull SearchParameterMap theParams) {
		ReadPartitionIdRequestDetails details =
				ReadPartitionIdRequestDetails.forSearchType(theResourceType, theParams, null);
		return determineReadPartitionForRequest(theRequest, details);
	}

	/**
	 * Determine partition to use when performing a database search based on a resource type, search parameters and a conditional target resource (if available).
	 * @param theRequest the request details from the context of the call
	 * @param theResourceType the resource type
	 * @param theParams the search parameters
	 * @param theConditionalOperationTargetOrNull the conditional target resource
	 * @return the partition id which should be used for the database search
	 */
	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForSearchType(
			RequestDetails theRequest,
			String theResourceType,
			SearchParameterMap theParams,
			IBaseResource theConditionalOperationTargetOrNull) {
		SearchParameterMap searchParameterMap = theParams != null ? theParams : SearchParameterMap.newSynchronous();
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forSearchType(
				theResourceType, searchParameterMap, theConditionalOperationTargetOrNull);
		return determineReadPartitionForRequest(theRequest, details);
	}

	RequestPartitionId determineGenericPartitionForRequest(RequestDetails theRequestDetails);

	/**
	 * Determine partition to use when performing the history operation based on a resource type and resource instance.
	 * @param theRequest the request details from the context of the call
	 * @param theResourceType the resource type
	 * @param theIdType the id of the resource instance
	 * @return the partition id which should be used for the history operation
	 */
	@Nonnull
	default RequestPartitionId determineReadPartitionForRequestForHistory(
			@Nullable RequestDetails theRequest, String theResourceType, IIdType theIdType) {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forHistory(theResourceType, theIdType);
		return determineReadPartitionForRequest(theRequest, details);
	}

	default void validateHasPartitionPermissions(
			@Nonnull RequestDetails theRequest, String theResourceType, RequestPartitionId theRequestPartitionId) {}

	@Nonnull
	RequestPartitionId determineCreatePartitionForRequest(
			@Nullable RequestDetails theRequest, @Nonnull IBaseResource theResource, @Nonnull String theResourceType);

	@Nonnull
	Set<Integer> toReadPartitions(@Nonnull RequestPartitionId theRequestPartitionId);

	boolean isResourcePartitionable(String theResourceType);

	/**
	 * <b>No interceptors should be invoked by this method. It should ONLY be used when partition ids are
	 * known, but partition names are not.</b>
	 * <br/><br/>
	 * Ensures the list of partition ids inside the given {@link RequestPartitionId} correctly map to the
	 * list of partition names. If the list of partition names is empty, this method will map the correct
	 * partition names and return a normalized {@link RequestPartitionId}.
	 * <br/><br/>
	 * @param theRequestPartitionId - An unvalidated and unnormalized {@link RequestPartitionId}.
	 * @return - A {@link RequestPartitionId} with a normalized list of partition ids and partition names.
	 */
	RequestPartitionId validateAndNormalizePartitionIds(RequestPartitionId theRequestPartitionId);

	/**
	 * <b>No interceptors should be invoked by this method. It should ONLY be used when partition names are
	 * known, but partition ids are not.</b>
	 * <br/><br/>
	 * Ensures the list of partition names inside the given {@link RequestPartitionId} correctly map to the
	 * list of partition ids. If the list of partition ids is empty, this method will map the correct
	 * partition ids and return a normalized {@link RequestPartitionId}.
	 * <br/><br/>
	 * @param theRequestPartitionId - An unvalidated and unnormalized {@link RequestPartitionId}.
	 * @return - A {@link RequestPartitionId} with a normalized list of partition ids and partition names.
	 */
	RequestPartitionId validateAndNormalizePartitionNames(RequestPartitionId theRequestPartitionId);
}
