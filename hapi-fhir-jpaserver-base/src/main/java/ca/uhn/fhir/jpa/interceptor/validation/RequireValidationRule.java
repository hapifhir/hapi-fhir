package ca.uhn.fhir.jpa.interceptor.validation;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.utils.IResourceValidator;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RequireValidationRule extends BaseTypedRule {
	private final IValidationSupport myValidationSupport;
	private final ValidatorResourceFetcher myValidatorResourceFetcher;
	private final FhirInstanceValidator myValidator;
	private ResultSeverityEnum myRejectOnSeverity = ResultSeverityEnum.ERROR;
	private List<TagOnSeverity> myTagOnSeverity = Collections.emptyList();

	public RequireValidationRule(FhirContext theFhirContext, String theType, IValidationSupport theValidationSupport, ValidatorResourceFetcher theValidatorResourceFetcher) {
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
				if (myRejectOnSeverity != null && myRejectOnSeverity.ordinal() <= next.getSeverity().ordinal()) {
					return RuleEvaluation.forFailure(this, outcome.toOperationOutcome());
				}
			}

			for (TagOnSeverity nextTagOnSeverity : myTagOnSeverity) {
				if (next.getSeverity().ordinal() >= nextTagOnSeverity.getSeverity()) {
					theResource
						.getMeta()
						.addTag()
						.setSystem(nextTagOnSeverity.getTagSystem())
						.setCode(nextTagOnSeverity.getTagCode());
				}
			}

		}

		return RuleEvaluation.forSuccess(this);
	}

	public void rejectOnSeverity(ResultSeverityEnum theSeverity) {
		myRejectOnSeverity = theSeverity;
	}

	public void tagOnSeverity(ResultSeverityEnum theSeverity, String theTagSystem, String theTagCode) {
		Validate.notNull(theSeverity, "theSeverity must not be null");
		Validate.notEmpty(theTagSystem, "theTagSystem must not be null or empty");
		Validate.notEmpty(theTagCode, "theTagCode must not be null or empty");
		if (myTagOnSeverity.isEmpty()) {
			myTagOnSeverity = new ArrayList<>();
		}
		myTagOnSeverity.add(new TagOnSeverity(theSeverity.ordinal(), theTagSystem, theTagCode));
	}

	public void dontReject() {
		myRejectOnSeverity = null;
	}


	private static class TagOnSeverity {
		private final int mySeverity;
		private final String myTagSystem;
		private final String myTagCode;

		private TagOnSeverity(int theSeverity, String theTagSystem, String theTagCode) {
			mySeverity = theSeverity;
			myTagSystem = theTagSystem;
			myTagCode = theTagCode;
		}

		public int getSeverity() {
			return mySeverity;
		}

		public String getTagSystem() {
			return myTagSystem;
		}

		public String getTagCode() {
			return myTagCode;
		}
	}

}
