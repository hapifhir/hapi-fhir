package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IResourceLinkResolver {

	/**
	 * This method resolves the target of a reference found within a resource that is being created/updated. We do this
	 * so that we can create indexed links between resources, and so that we can validate that the target actually
	 * exists in cases where we need to check that.
	 * <p>
	 * This method returns an {@link IResourceLookup} so as to avoid needing to resolve the entire resource.
	 *
	 * @param theRequestPartitionId The partition ID of the target resource
	 * @param theSearchParam        The param that is being indexed
	 * @param theSourcePath         The path within the resource where this reference was found
	 * @param theSourceResourceId   The ID of the resource containing the reference to the target being resolved
	 * @param theTypeString         The type of the resource being resolved
	 * @param theType               The resource type of the target
	 * @param theReference          The reference being resolved
	 * @param theRequest            The incoming request, if any
	 * @param theTransactionDetails
	 */
	IResourceLookup findTargetResource(RequestPartitionId theRequestPartitionId, RuntimeSearchParam theSearchParam, String theSourcePath, IIdType theSourceResourceId, String theTypeString, Class<? extends IBaseResource> theType, IBaseReference theReference, RequestDetails theRequest, TransactionDetails theTransactionDetails);

	void validateTypeOrThrowException(Class<? extends IBaseResource> theType);

}
