/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.util.ResourceCompartmentUtil;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * This interceptor can be used to block resource updates which would make resource patient compartment change.
 * <p/>
 * This could be used when the JPA server has partitioning enabled, and Tenant Identification Strategy is PATIENT_ID.
 */
@Interceptor
public class PatientCompartmentEnforcingInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(PatientCompartmentEnforcingInterceptor.class);

	private final FhirContext myFhirContext;
	private final ISearchParamExtractor mySearchParamExtractor;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	/**
	 * Constructor
	 */
	public PatientCompartmentEnforcingInterceptor(FhirContext theFhirContext, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myFhirContext = theFhirContext;
		mySearchParamExtractor = null;
	}

	/**
	 * @deprecated Use {@link #PatientCompartmentEnforcingInterceptor(FhirContext, IRequestPartitionHelperSvc)} instead
	 */
	@Deprecated(since = "8.8.0", forRemoval = true)
	public PatientCompartmentEnforcingInterceptor(
			FhirContext theFhirContext, ISearchParamExtractor theSearchParamExtractor) {
		myFhirContext = theFhirContext;
		mySearchParamExtractor = theSearchParamExtractor;
		myRequestPartitionHelperSvc = null;
	}

	/**
	 * Blocks resource updates which would make the resource change Patient Compartment.
	 * @param theOldResource the original resource state
	 * @param theResource the updated resource state
	 */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void storagePreStorageResourceUpdated(RequestDetails theRequestDetails, IBaseResource theOldResource, IBaseResource theResource) {

		ourLog.debug("Interceptor STORAGE_PRESTORAGE_RESOURCE_UPDATED - started");
		StopWatch stopWatch = new StopWatch();
		try {
			String patientCompartmentOld;
			String patientCompartmentCurrent;
			if (myRequestPartitionHelperSvc != null) {
				String resourceType = myFhirContext.getResourceType(theOldResource);
				patientCompartmentOld = determinePartition(theRequestDetails, theOldResource, resourceType);
				patientCompartmentCurrent = determinePartition(theRequestDetails, theResource, resourceType);
			} else {
				patientCompartmentOld = ResourceCompartmentUtil.getPatientCompartmentIdentity(
						theOldResource, myFhirContext, mySearchParamExtractor)
					.orElse(EMPTY);
				patientCompartmentCurrent = ResourceCompartmentUtil.getPatientCompartmentIdentity(
						theResource, myFhirContext, mySearchParamExtractor)
					.orElse(EMPTY);
			}

			if (!Objects.equals(patientCompartmentOld, patientCompartmentCurrent)) {
				// Avoid disclosing compartments in message, which could have security implications
				throw new PreconditionFailedException(
						Msg.code(2476) + "Resource compartment changed. Was a referenced Patient changed?");
			}

		} finally {
			ourLog.debug("Interceptor STORAGE_PRESTORAGE_RESOURCE_UPDATED - ended, execution took {}", stopWatch);
		}
	}

	private String determinePartition(RequestDetails theRequestDetails, IBaseResource theResource, String resourceType) {
		Object stashedPartition = theResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (stashedPartition != null) {
			theResource.setUserData(Constants.RESOURCE_PARTITION_ID, null);
		}
		try {
			RequestPartitionId requestPartition = myRequestPartitionHelperSvc.determineCreatePartitionForRequest(theRequestDetails, theResource, resourceType);
			return requestPartition.toJson();
		} finally {
			if (stashedPartition != null) {
				theResource.setUserData(Constants.RESOURCE_PARTITION_ID, stashedPartition);
			}
		}
	}
}
