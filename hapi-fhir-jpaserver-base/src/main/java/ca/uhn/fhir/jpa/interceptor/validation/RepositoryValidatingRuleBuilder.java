package ca.uhn.fhir.jpa.interceptor.validation;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.WordUtils;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is used to construct rules to populate the {@link RepositoryValidatingInterceptor}.
 * See <a href="https://hapifhir.io/hapi-fhir/docs/validation/repository_validating_interceptor.html">Repository Validating Interceptor</a>
 * in the HAPI FHIR documentation for more information on how to use this.
 */
public final class RepositoryValidatingRuleBuilder implements IRuleRoot {

	private final List<IRepositoryValidatingRule> myRules = new ArrayList<>();

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IValidationSupport myValidationSupport;
	@Autowired
	private ValidatorResourceFetcher myValidatorResourceFetcher;

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
		 * @param theProfileUrl
		 * @return
		 */
		public FinalizedTypedRule disallowProfile(String theProfileUrl) {
			return disallowProfiles(theProfileUrl);
		}

		public FinalizedRequireValidationRule requireValidationToDeclaredProfiles() {
			RequireValidationRule rule = new RequireValidationRule(myFhirContext, myType, myValidationSupport, myValidatorResourceFetcher);
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
				IResourceValidator.BestPracticeWarningLevel level = null;
				if (isNotBlank(theBestPracticeWarningLevel)) {
					level = IResourceValidator.BestPracticeWarningLevel.valueOf(WordUtils.capitalize(theBestPracticeWarningLevel.toLowerCase()));
				}
				return withBestPracticeWarningLevel(level);
			}

			/**
			 * Sets the "Best Practice Warning Level", which is the severity at which any "best practices" that
			 * are specified in the FHIR specification will be added to the validation outcome. Set to
			 * {@link org.hl7.fhir.r5.utils.IResourceValidator.BestPracticeWarningLevel#Error} to
			 * cause any best practice notices to result in a validation failure.
			 * Set to {@link org.hl7.fhir.r5.utils.IResourceValidator.BestPracticeWarningLevel#Ignore}
			 * to not include any best practice notifications.
			 */
			@Nonnull
			public FinalizedRequireValidationRule withBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel bestPracticeWarningLevel) {
				myRule.setBestPracticeWarningLevel(bestPracticeWarningLevel);
				return this;
			}
		}

	}

}
