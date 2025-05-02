/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.provider;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;

public class PatientMatchProvider {
	private final MdmControllerHelper myMdmControllerHelper;

	public PatientMatchProvider(MdmControllerHelper theMdmControllerHelper) {
		myMdmControllerHelper = theMdmControllerHelper;
	}

	/**
	 * Searches for matches for the provided patient resource
	 * @param thePatient - the patient resource
	 * @param theRequestDetails - the request details
	 * @return - any matches to the provided patient resource
	 */
	@Operation(name = ProviderConstants.PATIENT_MATCH, typeName = "Patient")
	public IBaseBundle match(
			@OperationParam(name = ProviderConstants.MDM_MATCH_RESOURCE, min = 1, max = 1, typeName = "Patient")
					IAnyResource thePatient,
			RequestDetails theRequestDetails) {
		if (thePatient == null) {
			throw new InvalidRequestException(Msg.code(1498) + "resource may not be null");
		}
		return myMdmControllerHelper.getMatchesAndPossibleMatchesForResource(thePatient, "Patient", theRequestDetails);
	}
}
