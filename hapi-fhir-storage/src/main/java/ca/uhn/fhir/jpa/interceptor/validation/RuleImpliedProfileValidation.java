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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;
import java.util.stream.Collectors;

class RuleImpliedProfileValidation extends BaseTypedRule {

	public enum ImpliedProfileMode {
		IF_NOT_EXPLICIT,
		ALWAYS
	}

	private final String myImpliedProfileUrl;
	private final ImpliedProfileMode myMode;
	private final FhirInstanceValidator myValidator;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private ResultSeverityEnum myRejectOnSeverity = ResultSeverityEnum.ERROR;

	RuleImpliedProfileValidation(
			FhirContext theFhirContext,
			String theType,
			String theImpliedProfileUrl,
			ImpliedProfileMode theMode,
			IValidationSupport theValidationSupport,
			ValidatorResourceFetcher theValidatorResourceFetcher,
			ValidatorPolicyAdvisor theValidationPolicyAdvisor,
			IInterceptorBroadcaster theInterceptorBroadcaster) {
		super(theFhirContext, theType);
		myImpliedProfileUrl = theImpliedProfileUrl;
		myMode = theMode;
		myInterceptorBroadcaster = theInterceptorBroadcaster;

		myValidator = new FhirInstanceValidator(theValidationSupport);
		myValidator.setValidatorResourceFetcher(theValidatorResourceFetcher);
		myValidator.setValidatorPolicyAdvisor(theValidationPolicyAdvisor);
	}

	@Nonnull
	@Override
	public RuleEvaluation evaluate(RequestDetails theRequestDetails, @Nonnull IBaseResource theResource) {
		List<String> declaredProfiles = theResource.getMeta().getProfile().stream()
				.map(IPrimitiveType::getValueAsString)
				.collect(Collectors.toList());

		boolean shouldApplyImpliedProfile = false;

		IBaseResource resourceCopy = getFhirContext()
				.newJsonParser()
				.parseResource(getFhirContext().newJsonParser().encodeResourceToString(theResource));

		if (myMode == ImpliedProfileMode.ALWAYS) {
			shouldApplyImpliedProfile = true;
			resourceCopy.getMeta().getProfile().removeIf(p -> !p.getValueAsString()
					.equals(myImpliedProfileUrl));
		} else if (myMode == ImpliedProfileMode.IF_NOT_EXPLICIT) {
			shouldApplyImpliedProfile = declaredProfiles.isEmpty();
		}

		if (!shouldApplyImpliedProfile) {
			return RuleEvaluation.forSuccess(this);
		} else if (!declaredProfiles.contains(myImpliedProfileUrl)) {
			resourceCopy.getMeta().addProfile(myImpliedProfileUrl);
		}

		FhirValidator validator = getFhirContext().newValidator();
		validator.setInterceptorBroadcaster(
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequestDetails));
		validator.registerValidatorModule(myValidator);
		ValidationResult outcome = validator.validateWithResult(resourceCopy);

		for (SingleValidationMessage next : outcome.getMessages()) {
			if (next.getSeverity().ordinal() >= ResultSeverityEnum.ERROR.ordinal()) {
				if (myRejectOnSeverity != null
						&& myRejectOnSeverity.ordinal() <= next.getSeverity().ordinal()) {
					return RuleEvaluation.forFailure(this, outcome.toOperationOutcome());
				}
			}
		}

		ValidationResultEnrichingInterceptor.addValidationResultToRequestDetails(theRequestDetails, outcome);

		return RuleEvaluation.forSuccess(this);
	}

	public void rejectOnSeverity(ResultSeverityEnum theSeverity) {
		myRejectOnSeverity = theSeverity;
	}

	public void dontReject() {
		myRejectOnSeverity = null;
	}

	public FhirInstanceValidator getValidator() {
		return myValidator;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("resourceType", getResourceType())
				.append("impliedProfileUrl", myImpliedProfileUrl)
				.append("mode", myMode)
				.append("rejectOnSeverity", myRejectOnSeverity)
				.toString();
	}
}
