/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.pid.ListWrappingPidStream;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Date;

public interface IBatch2DaoSvc {

	/**
	 * Indicates whether reindexing all resource types is supported. Implementations are expected to provide a static response (either they support this or they don't).
	 */
	boolean isAllResourceTypeSupported();

	/**
	 * Fetches a page of resource IDs for all resource types. The page size is up to the discretion of the implementation.
	 *
	 * @param theStart The start of the date range, must be inclusive.
	 * @param theEnd   The end of the date range, should be exclusive.
	 * @param theRequestPartitionId The request partition ID (may be <code>null</code> on non-partitioned systems)
	 * @param theUrl   The search URL, or <code>null</code> to return IDs for all resources across all resource types. Null will only be supplied if {@link #isAllResourceTypeSupported()} returns <code>true</code>.
	 */
	default IResourcePidList fetchResourceIdsPage(
			Date theStart, Date theEnd, @Nullable RequestPartitionId theRequestPartitionId, @Nullable String theUrl) {
		throw new UnsupportedOperationException(Msg.code(2425) + "Not implemented unless explicitly overridden");
	}

	// TODO: LD:  eliminate this call in all other implementors
	/**
	 * @deprecated Please call (@link {@link #fetchResourceIdsPage(Date, Date, RequestPartitionId, String)} instead.
	 * <p/>
	 * Fetches a page of resource IDs for all resource types. The page size is up to the discretion of the implementation.
	 *
	 * @param theStart The start of the date range, must be inclusive.
	 * @param theEnd   The end of the date range, should be exclusive.
	 * @param thePageSize  The number of records to query in each pass.
	 * @param theRequestPartitionId The request partition ID (may be <code>null</code> on non-partitioned systems)
	 * @param theUrl   The search URL, or <code>null</code> to return IDs for all resources across all resource types. Null will only be supplied if {@link #isAllResourceTypeSupported()} returns <code>true</code>.
	 */
	@Deprecated
	default IResourcePidList fetchResourceIdsPage(
			Date theStart,
			Date theEnd,
			@Nonnull Integer thePageSize,
			@Nullable RequestPartitionId theRequestPartitionId,
			@Nullable String theUrl) {
		return fetchResourceIdsPage(theStart, theEnd, theRequestPartitionId, theUrl);
	}

	default IResourcePidStream fetchResourceIdStream(
			Date theStart, Date theEnd, RequestPartitionId theTargetPartitionId, String theUrl) {
		return new ListWrappingPidStream(fetchResourceIdsPage(
				theStart, theEnd, 20000 /* ResourceIdListStep.DEFAULT_PAGE_SIZE */, theTargetPartitionId, theUrl));
	}
}
