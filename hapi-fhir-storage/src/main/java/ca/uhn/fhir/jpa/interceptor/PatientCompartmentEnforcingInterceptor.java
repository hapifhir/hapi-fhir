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
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.util.ResourceCompartmentUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public PatientCompartmentEnforcingInterceptor(
			FhirContext theFhirContext, ISearchParamExtractor theSearchParamExtractor) {
		myFhirContext = theFhirContext;
		mySearchParamExtractor = theSearchParamExtractor;
	}

	/**
	 * Blocks resource updates which would make the resource change Patient Compartment.
	 * @param theOldResource the original resource state
	 * @param theResource the updated resource state
	 */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void storagePreStorageResourceUpdated(IBaseResource theOldResource, IBaseResource theResource) {

		ourLog.info("Interceptor STORAGE_PRESTORAGE_RESOURCE_UPDATED - started");
		StopWatch stopWatch = new StopWatch();
		try {
			String patientCompartmentOld = ResourceCompartmentUtil.getPatientCompartmentIdentity(
							theOldResource, myFhirContext, mySearchParamExtractor)
					.orElse(EMPTY);
			String patientCompartmentCurrent = ResourceCompartmentUtil.getPatientCompartmentIdentity(
							theResource, myFhirContext, mySearchParamExtractor)
					.orElse(EMPTY);

			if (!StringUtils.equals(patientCompartmentOld, patientCompartmentCurrent)) {
				// Avoid disclosing compartments in message, which could have security implications
				throw new InternalErrorException(
						Msg.code(2476) + "Resource compartment changed. Was a referenced Patient changed?");
			}

		} finally {
			ourLog.info("Interceptor STORAGE_PRESTORAGE_RESOURCE_UPDATED - ended, execution took {}", stopWatch);
		}
	}
}
