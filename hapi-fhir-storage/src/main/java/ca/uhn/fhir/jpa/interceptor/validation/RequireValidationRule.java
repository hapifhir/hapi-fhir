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
package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.validation.ValidatorPolicyAdvisor;
import ca.uhn.fhir.jpa.validation.ValidatorResourceFetcher;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ValidationResultEnrichingInterceptor;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RequireValidationRule extends BaseTypedRule {
	private final FhirInstanceValidator myValidator;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final String myImpliedProfileUrl;
	private final ImpliedProfileMode myImpliedProfileMode;
	private ResultSeverityEnum myRejectOnSeverity = ResultSeverityEnum.ERROR;
	private List<TagOnSeverity> myTagOnSeverity = Collections.emptyList();

	/**
	 * Controls when validation against an implied profile is applied to a resource,
	 * relative to the profiles already declared in {@code Resource.meta.profile}.
	 */
	public enum ImpliedProfileMode {
		/**
		 * Apply the implied profile validation only when the resource does not already
		 * declare any profile in {@code Resource.meta.profile}. If the resource
		 * explicitly declares one or more profiles, the implied profile is ignored.
		 */
		IF_NOT_EXPLICIT,

		/**
		 * Always apply the implied profile validation, regardless of any profiles
		 * already declared in {@code Resource.meta.profile}. This effectively mandates
		 * compliance with the implied profile across all resources of the configured type.
		 */
		ALWAYS
	}

	public RequireValidationRule(
			FhirContext theFhirContext,
			String theType,
			IValidationSupport theValidationSupport,
			ValidatorResourceFetcher theValidatorResourceFetcher,
			ValidatorPolicyAdvisor theValidationPolicyAdvisor,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			String theImpliedProfileUrl,
			ImpliedProfileMode theImpliedProfileMode) {
		super(theFhirContext, theType);

		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myImpliedProfileUrl = theImpliedProfileUrl;
		myImpliedProfileMode = theImpliedProfileMode;

		myValidator = new FhirInstanceValidator(theValidationSupport);
		myValidator.setValidatorResourceFetcher(theValidatorResourceFetcher);
		myValidator.setValidatorPolicyAdvisor(theValidationPolicyAdvisor);
		myValidator.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);
	}

	public RequireValidationRule(
			FhirContext theFhirContext,
			String theType,
			IValidationSupport theValidationSupport,
			ValidatorResourceFetcher theValidatorResourceFetcher,
			ValidatorPolicyAdvisor theValidationPolicyAdvisor,
			IInterceptorBroadcaster theInterceptorBroadcaster) {
		this(
				theFhirContext,
				theType,
				theValidationSupport,
				theValidatorResourceFetcher,
				theValidationPolicyAdvisor,
				theInterceptorBroadcaster,
				null,
				null);
	}

	void setBestPracticeWarningLevel(BestPracticeWarningLevel theBestPracticeWarningLevel) {
		myValidator.setBestPracticeWarningLevel(theBestPracticeWarningLevel);
	}

	@Nonnull
	@Override
	public RuleEvaluation evaluate(RequestDetails theRequestDetails, @Nonnull IBaseResource theResource) {

		IBaseResource resourceToValidate = theResource;

		// Handle implied profile validation if configured
		if (myImpliedProfileUrl != null && myImpliedProfileMode != null) {
			resourceToValidate = handleImpliedProfileValidation(theResource);
		}

		FhirValidator validator = getFhirContext().newValidator();
		validator.setInterceptorBroadcaster(
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequestDetails));
		validator.registerValidatorModule(myValidator);
		ValidationResult outcome = validator.validateWithResult(resourceToValidate);

		for (SingleValidationMessage next : outcome.getMessages()) {
			if (next.getSeverity().ordinal() >= ResultSeverityEnum.ERROR.ordinal()) {
				if (myRejectOnSeverity != null
						&& myRejectOnSeverity.ordinal() <= next.getSeverity().ordinal()) {
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

		ValidationResultEnrichingInterceptor.addValidationResultToRequestDetails(theRequestDetails, outcome);

		return RuleEvaluation.forSuccess(this);
	}

	// Clones the resource before adding the implied profile to it for validation purposes; the implied profile is never
	// added to the persisted resource.
	private IBaseResource handleImpliedProfileValidation(IBaseResource theResource) {
		List<String> declaredProfiles = theResource.getMeta().getProfile().stream()
				.map(IPrimitiveType::getValueAsString)
				.toList();

		boolean shouldApplyImpliedProfile = false;

		// Create a copy of the resource for validation
		IBaseResource resourceCopy = getFhirContext()
				.newJsonParser()
				.parseResource(getFhirContext().newJsonParser().encodeResourceToString(theResource));

		if (myImpliedProfileMode == ImpliedProfileMode.ALWAYS) {
			shouldApplyImpliedProfile = true;
		} else if (myImpliedProfileMode == ImpliedProfileMode.IF_NOT_EXPLICIT) {
			shouldApplyImpliedProfile = declaredProfiles.isEmpty();
		}

		if (!shouldApplyImpliedProfile) {
			return theResource;
		} else if (!declaredProfiles.contains(myImpliedProfileUrl)) {
			resourceCopy.getMeta().addProfile(myImpliedProfileUrl);
		}

		return resourceCopy;
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

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("resourceType", getResourceType())
				.append("rejectOnSeverity", myRejectOnSeverity)
				.append("tagOnSeverity", myTagOnSeverity);

		if (myImpliedProfileUrl != null) {
			builder.append("impliedProfileUrl", myImpliedProfileUrl).append("impliedProfileMode", myImpliedProfileMode);
		}

		return builder.toString();
	}

	public FhirInstanceValidator getValidator() {
		return myValidator;
	}

	public void setAllowAnyExtensions() {
		myValidator.setAnyExtensionsAllowed(true);
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

		@Override
		public String toString() {
			return ResultSeverityEnum.values()[mySeverity].name() + "/" + myTagSystem + "/" + myTagCode;
		}
	}
}
