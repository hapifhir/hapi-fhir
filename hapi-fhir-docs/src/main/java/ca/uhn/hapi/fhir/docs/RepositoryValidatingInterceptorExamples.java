package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.interceptor.validation.IRepositoryValidatingRule;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingInterceptor;
import ca.uhn.fhir.jpa.interceptor.validation.RepositoryValidatingRuleBuilder;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SuppressWarnings("unused")
public class RepositoryValidatingInterceptorExamples {

	private ApplicationContext myAppCtx;
	private FhirContext myFhirCtx;
	private InterceptorService myInterceptorService;

	public void createSimpleRule() {
		//START SNIPPET: createSimpleRule
		// First you must ask the Spring Application Context for a rule builder
		RepositoryValidatingRuleBuilder ruleBuilder = myAppCtx.getBean(RepositoryValidatingRuleBuilder.class);

		// Add a simple rule requiring all Patient resources to declare conformance to the US Core
		// Patient Profile, and to validate successfully.
		ruleBuilder
			.forResourcesOfType("Patient")
				.requireAtLeastProfile("http://hl7.org/fhir/us/core/StructureDefinition/us-core-patient")
				.and()
				.requireValidationToDeclaredProfiles();

		// Build the rule list
		List<IRepositoryValidatingRule> rules = ruleBuilder.build();

		// Create and register the interceptor
		RepositoryValidatingInterceptor interceptor = new RepositoryValidatingInterceptor(myFhirCtx, rules);
		myInterceptorService.registerInterceptor(interceptor);
		//END SNIPPET: createSimpleRule
	}

	public void requireProfileDeclarations() {
		RepositoryValidatingRuleBuilder ruleBuilder = myAppCtx.getBean(RepositoryValidatingRuleBuilder.class);

		//START SNIPPET: requireProfileDeclarations
		// Require Patient resources to declare conformance to US Core patient profile
		ruleBuilder
			.forResourcesOfType("Patient")
			.requireAtLeastProfile("http://www.hl7.org/fhir/us/core/StructureDefinition-us-core-patient.html");

		// Require Patient resources to declare conformance to either the US Core patient profile
		// or the UK Core patient profile
		ruleBuilder
			.forResourcesOfType("Patient")
			.requireAtLeastOneProfileOf(
				"http://www.hl7.org/fhir/us/core/StructureDefinition-us-core-patient.html",
				"https://fhir.nhs.uk/R4/StructureDefinition/UKCore-Patient");
		//END SNIPPET: requireProfileDeclarations
	}

	public void requireValidationToDeclaredProfiles() {
		RepositoryValidatingRuleBuilder ruleBuilder = myAppCtx.getBean(RepositoryValidatingRuleBuilder.class);

		//START SNIPPET: requireValidationToDeclaredProfiles
		// Require Patient resources to validate to any declared profiles
		ruleBuilder
			.forResourcesOfType("Patient")
			.requireValidationToDeclaredProfiles();
		//END SNIPPET: requireValidationToDeclaredProfiles
	}

	public void requireValidationToDeclaredProfilesAdjustThreshold() {
		RepositoryValidatingRuleBuilder ruleBuilder = myAppCtx.getBean(RepositoryValidatingRuleBuilder.class);

		//START SNIPPET: requireValidationToDeclaredProfilesAdjustThreshold
		ruleBuilder
			.forResourcesOfType("Patient")
			.requireValidationToDeclaredProfiles()
			.rejectOnSeverity(ResultSeverityEnum.WARNING);
		//END SNIPPET: requireValidationToDeclaredProfilesAdjustThreshold
	}

	public void requireValidationToDeclaredProfilesTagOnFailure() {
		RepositoryValidatingRuleBuilder ruleBuilder = myAppCtx.getBean(RepositoryValidatingRuleBuilder.class);

		//START SNIPPET: requireValidationToDeclaredProfilesTagOnFailure
		ruleBuilder
			.forResourcesOfType("Patient")
			.requireValidationToDeclaredProfiles()
			.neverReject()
			.tagOnSeverity(ResultSeverityEnum.ERROR, "http://example.com", "validation-failure");
		//END SNIPPET: requireValidationToDeclaredProfilesTagOnFailure
	}

	public void requireValidationToDeclaredProfilesAdditionalOptions() {
		RepositoryValidatingRuleBuilder ruleBuilder = myAppCtx.getBean(RepositoryValidatingRuleBuilder.class);

		//START SNIPPET: requireValidationToDeclaredProfilesAdditionalOptions
		ruleBuilder
			.forResourcesOfType("Patient")
			.requireValidationToDeclaredProfiles()

			// Configure the validator to reject unknown extensions
			// by default, all extensions are accepted and to undo this rejection
			// call allowAnyExtensions()
			.rejectUnknownExtensions()

			// Configure the validator to not perform terminology validation
			.disableTerminologyChecks()

			// Configure the validator to raise an error if a resource being
			// validated declares a profile, and the StructureDefinition for
			// this profile can not be found.
			.errorOnUnknownProfiles()

			// Configure the validator to suppress the information-level
			// message that is added to the validation result if a profile
			// StructureDefinition does not declare a binding for a coded
			// field.
			.suppressNoBindingMessage()

			// Configure the validator to suppress the warning-level message
			// that is added when validating a code that can't be found in a
			// ValueSet that has an extensible binding.
			.suppressWarningForExtensibleValueSetValidation();
		//END SNIPPET: requireValidationToDeclaredProfilesAdditionalOptions
	}


	public void disallowProfiles() {
		RepositoryValidatingRuleBuilder ruleBuilder = myAppCtx.getBean(RepositoryValidatingRuleBuilder.class);

		//START SNIPPET: disallowProfiles
		// No UK Core patients allowed!
		ruleBuilder
			.forResourcesOfType("Patient")
			.disallowProfile("https://fhir.nhs.uk/R4/StructureDefinition/UKCore-Patient");
		//END SNIPPET: disallowProfiles
	}
}

