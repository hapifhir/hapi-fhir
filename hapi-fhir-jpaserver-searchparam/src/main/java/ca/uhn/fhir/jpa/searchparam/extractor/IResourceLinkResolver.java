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
package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IResourceLinkResolver {

	/**
	 * This method resolves the target of a reference found within a resource that is being created/updated. We do this
	 * so that we can create indexed links between resources, and so that we can validate that the target actually
	 * exists in cases where we need to check that.
	 * <p>
	 * This method returns an {@link IResourceLookup} to avoid needing to resolve the entire resource.
	 *
	 * @param theRequestPartitionId The partition ID of the target resource
	 * @param theSourceResourceName The resource type for the resource containing the reference
	 * @param thePathAndRef         The path and reference
	 * @param theRequest            The incoming request, if any
	 * @param theTransactionDetails The current TransactionDetails object
	 */
	IResourceLookup findTargetResource(
			@Nonnull RequestPartitionId theRequestPartitionId,
			String theSourceResourceName,
			PathAndRef thePathAndRef,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails);

	/**
	 * This method resolves the target of a reference found within a resource that is being created/updated. We do this
	 * so that we can create indexed links between resources, and so that we can validate that the target actually
	 * exists in cases where we need to check that.
	 * <p>
	 * This method returns an {@link IResourceLookup} to avoid needing to resolve the entire resource.
	 *
	 * @param theRequestPartitionId The partition ID of the target resource
	 * @param theSourceResourceName The resource type for the resource containing the reference
	 * @param thePathAndRef         The path and reference
	 * @param theRequest            The incoming request, if any
	 * @param theTransactionDetails The current TransactionDetails object
	 */
	@Nullable
	IBaseResource loadTargetResource(
			@Nonnull RequestPartitionId theRequestPartitionId,
			String theSourceResourceName,
			PathAndRef thePathAndRef,
			RequestDetails theRequest,
			TransactionDetails theTransactionDetails);

	void validateTypeOrThrowException(Class<? extends IBaseResource> theType);
}
