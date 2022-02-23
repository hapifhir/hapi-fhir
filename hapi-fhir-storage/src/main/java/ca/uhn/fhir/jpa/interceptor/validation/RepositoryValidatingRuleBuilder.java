package ca.uhn.fhir.jpa.interceptor.validation;

/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.validation.ValidatorPolicyAdvisor;
import ca.uhn.fhir.jpa.validation.ValidatorResourceFetcher;
import ca.uhn.fhir.rest.server.interceptor.ValidationResultEnrichingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.WordUtils;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Ascii.toLowerCase;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is used to construct rules to populate the {@link RepositoryValidatingInterceptor}.
 * See <a href="https://hapifhir.io/hapi-fhir/docs/validation/repository_validating_interceptor.html">Repository Validating Interceptor</a>
 * in the HAPI FHIR documentation for more information on how to use this.
 */
public final class RepositoryValidatingRuleBuilder implements IRuleRoot {

	public static final String REPOSITORY_VALIDATING_RULE_BUILDER = "repositoryValidatingRuleBuilder";
	private final List<IRepositoryValidatingRule> myRules = new ArrayList<>();

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IValidationSupport myValidationSupport;
	@Autowired
	private ValidatorResourceFetcher myValidatorResourceFetcher;
	@Autowired
	private ValidatorPolicyAdvisor myValidationPolicyAdvisor;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	/**
	 * Begin a new rule for a specific resource type.
	 *
	 * @param theType The resource type e.g. "Patient" (must not be null)
	 */
	@Override
	public RepositoryValidatingRuleBuilderTyped forResourcesOfType(String theType) {
		return new RepositoryValidatingRuleBuilderTyped(theType);
	}

	/**
	 * Create the repository validation rules
	 */
	@Override
	public List<IRepositoryValidatingRule> build() {
		return myRules;
	}

	public class FinalizedTypedRule implements IRuleRoot {

		private final String myType;

		FinalizedTypedRule(String theType) {
			myType = theType;
		}

		@Override
		public RepositoryValidatingRuleBuilderTyped forResourcesOfType(String theType) {
			return RepositoryValidatingRuleBuilder.this.forResourcesOfType(theType);
		}

		@Override
		public List<IRepositoryValidatingRule> build() {
			return RepositoryValidatingRuleBuilder.this.build();
		}

		public RepositoryValidatingRuleBuilderTyped and() {
			return new RepositoryValidatingRuleBuilderTyped(myType);
		}
	}

	public final class RepositoryValidatingRuleBuilderTyped {

		private final String myType;

		RepositoryValidatingRuleBuilderTyped(String theType) {
			myType = myFhirContext.getResourceType(theType);
		}

		/**
		 * Require any resource being persisted to declare conformance to the given profile, meaning that the specified
		 * profile URL must be found within the resource in <code>Resource.meta.profile</code>.
		 * <p>
		 * This rule is non-exclusive, meaning that a resource will pass as long as one of its profile declarations
		 * in <code>Resource.meta.profile</code> matches. If the resource declares conformance to multiple profiles, any
		 * other profile declarations found in that field will be ignored.
		 * </p>
		 */
		public FinalizedTypedRule requireAtLeastProfile(String theProfileUrl) {
			return requireAtLeastOneProfileOf(theProfileUrl);
		}

		/**
		 * Require any resource being persisted to declare conformance to at least one of the given profiles, meaning that the specified
		 * profile URL must be found within the resource in <code>Resource.meta.profile</code>.
		 * <p>
		 * This rule is non-exclusive, meaning that a resource will pass as long as one of its profile declarations
		 * in <code>Resource.meta.profile</code> matches. If the resource declares conformance to multiple profiles, any
		 * other profile declarations found in that field will be ignored.
		 * </p>
		 */
		public FinalizedTypedRule requireAtLeastOneProfileOf(String... theProfileUrls) {
			Validate.notNull(theProfileUrls, "theProfileUrls must not be null");
			requireAtLeastOneProfileOf(Arrays.asList(theProfileUrls));
			return new FinalizedTypedRule(myType);
		}

		/**
		 * Require any resource being persisted to declare conformance to at least one of the given profiles, meaning that the specified
		 * profile URL must be found within the resource in <code>Resource.meta.profile</code>.
		 * <p>
		 * This rule is non-exclusive, meaning that a resource will pass as long as one of its profile declarations
		 * in <code>Resource.meta.profile</code> matches. If the resource declares conformance to multiple profiles, any
		 * other profile declarations found in that field will be ignored.
		 * </p>
		 */
		private FinalizedTypedRule requireAtLeastOneProfileOf(Collection<String> theProfileUrls) {
			Validate.notNull(theProfileUrls, "theProfileUrls must not be null");
			Validate.notEmpty(theProfileUrls, "theProfileUrls must not be null or empty");
			myRules.add(new RuleRequireProfileDeclaration(myFhirContext, myType, theProfileUrls));
			return new FinalizedTypedRule(myType);
		}

		/**
		 * If set, any resources that contain a profile declaration in <code>Resource.meta.profile</code>
		 * matching {@literal theProfileUrl} will be rejected.
		 *
		 * @param theProfileUrl The profile canonical URL
		 */
		public FinalizedTypedRule disallowProfile(String theProfileUrl) {
			return disallowProfiles(theProfileUrl);
		}

		/**
		 * Perform a resource validation step using the FHIR Instance Validator and reject the
		 * storage if the validation fails.
		 *
		 * <p>
		 * If the {@link ValidationResultEnrichingInterceptor} is registered against the
		 * {@link ca.uhn.fhir.rest.server.RestfulServer} interceptor registry, the validation results
		 * will be appended to any <code>OperationOutcome</code> resource returned by the server.
		 * </p>
		 *
		 * @see ValidationResultEnrichingInterceptor
		 */
		public FinalizedRequireValidationRule requireValidationToDeclaredProfiles() {
			RequireValidationRule rule = new RequireValidationRule(myFhirContext, myType, myValidationSupport,
				myValidatorResourceFetcher, myValidationPolicyAdvisor, myInterceptorBroadcaster);
			myRules.add(rule);
			return new FinalizedRequireValidationRule(rule);
		}

		public FinalizedTypedRule disallowProfiles(String... theProfileUrls) {
			Validate.notNull(theProfileUrls, "theProfileUrl must not be null or empty");
			Validate.notEmpty(theProfileUrls, "theProfileUrl must not be null or empty");
			myRules.add(new RuleDisallowProfile(myFhirContext, myType, theProfileUrls));
			return new FinalizedTypedRule(myType);
		}


		public class FinalizedRequireValidationRule extends FinalizedTypedRule {

			private final RequireValidationRule myRule;

			public FinalizedRequireValidationRule(RequireValidationRule theRule) {
				super(myType);
				myRule = theRule;
			}

			/**
			 * Sets the "Best Practice Warning Level", which is the severity at which any "best practices" that
			 * are specified in the FHIR specification will be added to the validation outcome. Set to
			 * <code>ERROR</code> to cause any best practice notices to result in a validation failure.
			 * Set to <code>IGNORE</code> to not include any best practice notifications.
			 */
			@Nonnull
			public FinalizedRequireValidationRule withBestPracticeWarningLevel(String theBestPracticeWarningLevel) {
				BestPracticeWarningLevel level = null;
				if (isNotBlank(theBestPracticeWarningLevel)) {
					level = BestPracticeWarningLevel.valueOf(WordUtils.capitalize(theBestPracticeWarningLevel.toLowerCase()));
				}
				return withBestPracticeWarningLevel(level);
			}

			/**
			 * Sets the "Best Practice Warning Level", which is the severity at which any "best practices" that
			 * are specified in the FHIR specification will be added to the validation outcome. Set to
			 * {@link org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel#Error} to
			 * cause any best practice notices to result in a validation failure.
			 * Set to {@link org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel#Ignore}
			 * to not include any best practice notifications.
			 */
			@Nonnull
			public FinalizedRequireValidationRule withBestPracticeWarningLevel(BestPracticeWarningLevel bestPracticeWarningLevel) {
				myRule.setBestPracticeWarningLevel(bestPracticeWarningLevel);
				return this;
			}

			/**
			 * Specifies that the resource should not be rejected from storage even if it does not pass validation.
			 */
			@Nonnull
			public FinalizedRequireValidationRule neverReject() {
				myRule.dontReject();
				return this;
			}

			/**
			 * Specifies the minimum validation result severity that should cause a rejection. For example, if
			 * this is set to <code>ERROR</code> (which is the default), any validation results with a severity
			 * of <code>ERROR</code> or <code>FATAL</code> will cause the create/update operation to be rejected and
			 * rolled back, and no data will be saved.
			 * <p>
			 * Valid values must be drawn from {@link ResultSeverityEnum}
			 * </p>
			 */
			@Nonnull
			public FinalizedRequireValidationRule rejectOnSeverity(@Nonnull String theSeverity) {
				ResultSeverityEnum severity = ResultSeverityEnum.fromCode(toLowerCase(theSeverity));
				Validate.notNull(severity, "Invalid severity code: %s", theSeverity);
				return rejectOnSeverity(severity);
			}

			/**
			 * Specifies the minimum validation result severity that should cause a rejection. For example, if
			 * Specifies the minimum validation result severity that should cause a rejection. For example, if
			 * this is set to <code>ERROR</code> (which is the default), any validation results with a severity
			 * of <code>ERROR</code> or <code>FATAL</code> will cause the create/update operation to be rejected and
			 * rolled back, and no data will be saved.
			 * <p>
			 * Valid values must be drawn from {@link ResultSeverityEnum}
			 * </p>
			 */
			@Nonnull
			public FinalizedRequireValidationRule rejectOnSeverity(@Nonnull ResultSeverityEnum theSeverity) {
				myRule.rejectOnSeverity(theSeverity);
				return this;
			}

			/**
			 * Specifies that if the validation results in any results with a severity of <code>theSeverity</code> or
			 * greater, the resource will be tagged with the given tag when it is saved.
			 *
			 * @param theSeverity  The minimum severity. Must be drawn from values in {@link ResultSeverityEnum} and must not be <code>null</code>
			 * @param theTagSystem The system for the tag to add. Must not be <code>null</code>
			 * @param theTagCode   The code for the tag to add. Must not be <code>null</code>
			 * @return
			 */
			@Nonnull
			public FinalizedRequireValidationRule tagOnSeverity(@Nonnull String theSeverity, @Nonnull String theTagSystem, @Nonnull String theTagCode) {
				ResultSeverityEnum severity = ResultSeverityEnum.fromCode(toLowerCase(theSeverity));
				return tagOnSeverity(severity, theTagSystem, theTagCode);
			}

			/**
			 * Specifies that if the validation results in any results with a severity of <code>theSeverity</code> or
			 * greater, the resource will be tagged with the given tag when it is saved.
			 *
			 * @param theSeverity  The minimum severity. Must be drawn from values in {@link ResultSeverityEnum} and must not be <code>null</code>
			 * @param theTagSystem The system for the tag to add. Must not be <code>null</code>
			 * @param theTagCode   The code for the tag to add. Must not be <code>null</code>
			 * @return
			 */
			@Nonnull
			public FinalizedRequireValidationRule tagOnSeverity(@Nonnull ResultSeverityEnum theSeverity, @Nonnull String theTagSystem, @Nonnull String theTagCode) {
				myRule.tagOnSeverity(theSeverity, theTagSystem, theTagCode);
				return this;
			}

			/**
			 * Configure the validator to never reject extensions
			 */
			@Nonnull
			public FinalizedRequireValidationRule allowAnyExtensions() {
				myRule.getValidator().setAnyExtensionsAllowed(true);
				return this;
			}

			/**
			 * Configure the validator to reject unknown extensions
			 */
			@Nonnull
			public FinalizedRequireValidationRule rejectUnknownExtensions() {
				myRule.getValidator().setAnyExtensionsAllowed(false);
				return this;
			}

			/**
			 * Configure the validator to not perform terminology validation
			 */
			@Nonnull
			public FinalizedRequireValidationRule disableTerminologyChecks() {
				myRule.getValidator().setNoTerminologyChecks(true);
				return this;
			}

			/**
			 * Configure the validator to raise an error if a resource being validated
			 * declares a profile, and the StructureDefinition for this profile
			 * can not be found.
			 */
			@Nonnull
			public FinalizedRequireValidationRule errorOnUnknownProfiles() {
				myRule.getValidator().setErrorForUnknownProfiles(true);
				return this;
			}

			/**
			 * Configure the validator to suppress the information-level message that
			 * is added to the validation result if a profile StructureDefinition does
			 * not declare a binding for a coded field.
			 */
			@Nonnull
			public FinalizedRequireValidationRule suppressNoBindingMessage() {
				myRule.getValidator().setNoBindingMsgSuppressed(true);
				return this;
			}

			/**
			 * Configure the validator to suppress the warning-level message that
			 * is added when validating a code that can't be found in an ValueSet that
			 * has an extensible binding.
			 */
			@Nonnull
			public FinalizedRequireValidationRule suppressWarningForExtensibleValueSetValidation() {
				myRule.getValidator().setNoExtensibleWarnings(true);
				return this;
			}

		}

	}

}
