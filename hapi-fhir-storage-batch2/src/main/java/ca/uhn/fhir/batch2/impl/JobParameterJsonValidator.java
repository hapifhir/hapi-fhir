package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import javax.annotation.Nonnull;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

class JobParameterJsonValidator {
	private final ValidatorFactory myValidatorFactory = Validation.buildDefaultValidatorFactory();

	<PT extends IModelJson> void validateJobParameters(@Nonnull JobInstanceStartRequest theStartRequest, @Nonnull JobDefinition<PT> theJobDefinition) {

		// JSR 380
		Validator validator = myValidatorFactory.getValidator();
		PT parameters = theStartRequest.getParameters(theJobDefinition.getParametersType());
		Set<ConstraintViolation<IModelJson>> constraintErrors = validator.validate(parameters);
		List<String> errorStrings = constraintErrors.stream().map(t -> t.getPropertyPath() + " - " + t.getMessage()).sorted().collect(Collectors.toList());

		// Programmatic Validator
		IJobParametersValidator<PT> parametersValidator = theJobDefinition.getParametersValidator();
		if (parametersValidator != null) {
			List<String> outcome = parametersValidator.validate(parameters);
			outcome = defaultIfNull(outcome, Collections.emptyList());
			errorStrings.addAll(outcome);
		}

		if (!errorStrings.isEmpty()) {
			String message = "Failed to validate parameters for job of type " + theJobDefinition.getJobDefinitionId() + ": " + errorStrings.stream().map(t -> "\n * " + t).collect(Collectors.joining());

			throw new InvalidRequestException(Msg.code(2039) + message);
		}
	}
}
