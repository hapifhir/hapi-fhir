/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.HashSet;
import java.util.Set;

public class TestPartitionSelectorInterceptor {
	private RequestPartitionId myNextPartition;
	private final Set<String> myNonPartitionableResources = new HashSet<>();
	private BaseRequestPartitionHelperSvc myHelperSvc = new RequestPartitionHelperSvc();

	/**
	 * Constructor
	 */
	public TestPartitionSelectorInterceptor() {
		super();
	}

	public TestPartitionSelectorInterceptor addNonPartitionableResource(@Nonnull String theResourceName) {
		Validate.notBlank(theResourceName, "Must not be blank");
		myNonPartitionableResources.add(theResourceName);
		return this;
	}

	public void setNextPartitionId(Integer theNextPartitionId) {
		myNextPartition = RequestPartitionId.fromPartitionId(theNextPartitionId);
	}

	public void setNextPartition(RequestPartitionId theNextPartition) {
		myNextPartition = theNextPartition;
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId selectPartitionCreate(IBaseResource theResource) {
		FhirContext fhirContext = FhirContext.forCached(theResource.getStructureFhirVersionEnum());
		String resourceType = fhirContext.getResourceType(theResource);
		return selectPartition(resourceType);
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId selectPartitionRead(ReadPartitionIdRequestDetails theDetails) {
		return selectPartition(theDetails.getResourceType());
	}

	@Nonnull
	private RequestPartitionId selectPartition(String theResourceType) {
		if (theResourceType != null) {
			if (!myHelperSvc.isResourcePartitionable(theResourceType)) {
				return RequestPartitionId.defaultPartition();
			}
			if (myNonPartitionableResources.contains(theResourceType)) {
				return RequestPartitionId.defaultPartition();
			}
		}

		assert myNextPartition != null;
		return myNextPartition;
	}
}
