package ca.uhn.fhir.rest.server.interceptor;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Interceptor
public class ValidationResultEnrichingInterceptor {

	/**
	 * A {@link RequestDetails#getUserData() user data} entry will be created with this
	 * key which contains the {@link ValidationResult} from validating the request.
	 */
	public static final String REQUEST_VALIDATION_RESULT = ValidationResultEnrichingInterceptor.class.getName() + "_REQUEST_VALIDATION_RESULT";

	@SuppressWarnings("unchecked")
	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public boolean addValidationResultsToOperationOutcome(RequestDetails theRequestDetails, IBaseResource theResponseObject) {
		if (theResponseObject instanceof IBaseOperationOutcome) {
			IBaseOperationOutcome oo = (IBaseOperationOutcome) theResponseObject;

			if (theRequestDetails != null) {
				List<ValidationResult> validationResult = (List<ValidationResult>) theRequestDetails.getUserData().remove(REQUEST_VALIDATION_RESULT);
				if (validationResult != null) {
					for (ValidationResult next : validationResult) {
						next.populateOperationOutcome(oo);
					}
				}
			}

		}

		return true;
	}


	@SuppressWarnings("unchecked")
	public static void addValidationResultToRequestDetails(@Nullable RequestDetails theRequestDetails, @Nonnull ValidationResult theValidationResult) {
		if (theRequestDetails != null) {
			List<ValidationResult> results = (List<ValidationResult>) theRequestDetails.getUserData().computeIfAbsent(REQUEST_VALIDATION_RESULT, t -> new ArrayList<>(2));
			results.add(theValidationResult);
		}
	}
}
