/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;

class TenantCheckingTester implements IAuthRuleTester {

	private final Collection<String> myTenantIds;
	private final boolean myOutcome;

	public TenantCheckingTester(Collection<String> theTenantIds, boolean theOutcome) {
		myTenantIds = theTenantIds;
		myOutcome = theOutcome;
	}

	@Override
	public boolean matches(
			RestOperationTypeEnum theOperation,
			RequestDetails theRequestDetails,
			IIdType theInputResourceId,
			IBaseResource theInputResource) {
		if (!myTenantIds.contains(theRequestDetails.getTenantId())) {
			return !myOutcome;
		}

		return matchesResource(theInputResource);
	}

	@Override
	public boolean matchesOutput(
			RestOperationTypeEnum theOperation, RequestDetails theRequestDetails, IBaseResource theOutputResource) {
		if (!myTenantIds.contains(theRequestDetails.getTenantId())) {
			return !myOutcome;
		}

		return matchesResource(theOutputResource);
	}

	/**
	 * Returns <code>true</code> if the rule carrying this tester applies to requests for the given tenant.
	 */
	boolean appliesToTenant(String theTenantId) {
		return myTenantIds.contains(theTenantId) == myOutcome;
	}

	private boolean matchesResource(IBaseResource theResource) {
		if (theResource != null) {
			RequestPartitionId partitionId =
					(RequestPartitionId) theResource.getUserData(Constants.RESOURCE_PARTITION_ID);
			if (partitionId != null) {
				if (partitionId.hasDefaultPartitionId()
						&& myTenantIds.contains(ProviderConstants.DEFAULT_PARTITION_NAME)) {
					return myOutcome;
				}

				String partitionNameOrNull = partitionId.getFirstPartitionNameOrNull();
				if (partitionNameOrNull == null || !myTenantIds.contains(partitionNameOrNull)) {
					return !myOutcome;
				}
			}
		}

		return myOutcome;
	}
}
