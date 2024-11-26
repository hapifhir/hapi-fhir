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
package ca.uhn.fhir.jpa.dao.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_400_BAD_REQUEST;

public class ResourceMergeService {

	IFhirResourceDaoPatient<?> myDao;
	FhirContext myFhirContext;

	public ResourceMergeService(IFhirResourceDaoPatient<?> thePatientDao) {
		myDao = thePatientDao;
		myFhirContext = myDao.getContext();
	}

	/**
	 * Implemention of the $merge operation for resources
	 * @param theMergeOperationParameters	the merge operation parameters
	 * @param theRequestDetails	the request details
	 * @return the merge outcome containing OperationOutcome and HTTP status code
	 */
	public MergeOutcome merge(MergeOperationParameters theMergeOperationParameters, RequestDetails theRequestDetails) {

		MergeOutcome mergeOutcome = new MergeOutcome();
		IBaseOperationOutcome outcome = OperationOutcomeUtil.newInstance(myFhirContext);
		mergeOutcome.setOperationOutcome(outcome);

		if (!validateMergeOperationParameters(theMergeOperationParameters, outcome)) {
			mergeOutcome.setHttpStatusCode(STATUS_HTTP_400_BAD_REQUEST);
			return mergeOutcome;
		}

		return mergeOutcome;
	}

	/**
	 * Validates the merge operation parameters and adds validation errors to the outcome
	 * @param theMergeOperationParameters the merge operation parameters
	 * @param theOutcome the outcome to add validation errors to
	 * @return true if the parameters are valid, false otherwise
	 */
	private boolean validateMergeOperationParameters(
			MergeOperationParameters theMergeOperationParameters, IBaseOperationOutcome theOutcome) {
		List<String> errorMessages = new ArrayList<>();
		if (!theMergeOperationParameters.hasAtLeastOneSourceIdentifier()
				&& theMergeOperationParameters.getSourceResource() == null) {
			errorMessages.add("There are no source resource parameters provided, include either a source-patient, "
					+ "source-patient-identifier parameter.");
		}

		// Spec has conflicting information about this case
		if (theMergeOperationParameters.hasAtLeastOneSourceIdentifier()
				&& theMergeOperationParameters.getSourceResource() != null) {
			errorMessages.add(
					"Source patient must be provided either by source-patient-identifier or by source-resource, not both.");
		}

		if (!theMergeOperationParameters.hasAtLeastOneTargetIdentifier()
				&& theMergeOperationParameters.getTargetResource() == null) {
			errorMessages.add("There are no target resource parameters provided, include either a target-patient, "
					+ "target-patient-identifier parameter.");
		}

		// Spec has conflicting information about this case
		if (theMergeOperationParameters.hasAtLeastOneTargetIdentifier()
				&& theMergeOperationParameters.getTargetResource() != null) {
			errorMessages.add("Target patient must be provided either by target-patient-identifier or by "
					+ "target-resource, not both.");
		}

		if (!errorMessages.isEmpty()) {
			for (String validationError : errorMessages) {
				OperationOutcomeUtil.addIssue(myFhirContext, theOutcome, "error", validationError, null, null);
			}
			// there are validation errors
			return false;
		}

		// no validation errors
		return true;
	}

	public static class MergeOutcome {
		private IBaseOperationOutcome myOperationOutcome;
		private int myHttpStatusCode;

		public IBaseOperationOutcome getOperationOutcome() {
			return myOperationOutcome;
		}

		public void setOperationOutcome(IBaseOperationOutcome theOperationOutcome) {
			this.myOperationOutcome = theOperationOutcome;
		}

		public int getHttpStatusCode() {
			return myHttpStatusCode;
		}

		public void setHttpStatusCode(int theHttpStatusCode) {
			this.myHttpStatusCode = theHttpStatusCode;
		}
	}
}
