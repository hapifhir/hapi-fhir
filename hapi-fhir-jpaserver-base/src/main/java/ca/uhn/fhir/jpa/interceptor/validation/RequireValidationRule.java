package ca.uhn.fhir.jpa.interceptor.validation;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.validation.ValidatorResourceFetcher;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.utils.IResourceValidator;

import javax.annotation.Nonnull;

class RequireValidationRule extends BaseTypedRule {
	private final IValidationSupport myValidationSupport;
	private final ValidatorResourceFetcher myValidatorResourceFetcher;
	private final FhirInstanceValidator myValidator;

	RequireValidationRule(FhirContext theFhirContext, String theType, IValidationSupport theValidationSupport, ValidatorResourceFetcher theValidatorResourceFetcher) {
		super(theFhirContext, theType);
		myValidationSupport = theValidationSupport;
		myValidatorResourceFetcher = theValidatorResourceFetcher;

		myValidator = new FhirInstanceValidator(theValidationSupport);
		myValidator.setValidatorResourceFetcher(theValidatorResourceFetcher);
		myValidator.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Warning);
	}

	void setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel theBestPracticeWarningLevel) {
		myValidator.setBestPracticeWarningLevel(theBestPracticeWarningLevel);
	}

	@Nonnull
	@Override
	public RuleEvaluation evaluate(@Nonnull IBaseResource theResource) {

		FhirValidator validator = getFhirContext().newValidator();
		validator.registerValidatorModule(myValidator);
		ValidationResult outcome = validator.validateWithResult(theResource);

		for (SingleValidationMessage next : outcome.getMessages()) {
			if (next.getSeverity().ordinal() >= ResultSeverityEnum.ERROR.ordinal()) {
				return RuleEvaluation.forFailure(this, outcome.toOperationOutcome());
			}
		}

		return RuleEvaluation.forSuccess(this);
	}
}
