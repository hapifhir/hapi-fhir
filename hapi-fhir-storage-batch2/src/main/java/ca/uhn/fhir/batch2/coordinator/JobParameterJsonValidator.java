/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

class JobParameterJsonValidator {
	private final ValidatorFactory myValidatorFactory = Validation.buildDefaultValidatorFactory();

	<PT extends IModelJson> void validateJobParameters(
			RequestDetails theRequestDetails,
			@Nonnull JobInstanceStartRequest theStartRequest,
			@Nonnull JobDefinition<PT> theJobDefinition) {

		// JSR 380
		Validator validator = myValidatorFactory.getValidator();
		PT parameters = theStartRequest.getParameters(theJobDefinition.getParametersType());
		Set<ConstraintViolation<IModelJson>> constraintErrors = validator.validate(parameters);
		List<String> errorStrings = constraintErrors.stream()
				.map(t -> t.getPropertyPath() + " - " + t.getMessage())
				.sorted()
				.collect(Collectors.toList());

		// Programmatic Validator
		IJobParametersValidator<PT> parametersValidator = theJobDefinition.getParametersValidator();
		if (parametersValidator != null) {
			List<String> outcome = parametersValidator.validate(theRequestDetails, parameters);
			outcome = defaultIfNull(outcome, Collections.emptyList());
			errorStrings.addAll(outcome);
		}

		if (!errorStrings.isEmpty()) {
			String message = "Failed to validate parameters for job of type " + theJobDefinition.getJobDefinitionId()
					+ ": " + errorStrings.stream().map(t -> "\n * " + t).collect(Collectors.joining());

			throw new InvalidRequestException(Msg.code(2039) + message);
		}
	}
}
