package ca.uhn.fhir.jpa.interceptor.validation;

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


public final class RepositoryValidatingRuleBuilder implements IRuleRoot {


	private final List<IRepositoryValidatingRule> myRules = new ArrayList<>();
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IValidationSupport myValidationSupport;
	@Autowired
	private ValidatorResourceFetcher myValidatorResourceFetcher;

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

	public class FinalizedRule implements IRuleRoot {


		@Override
		public RepositoryValidatingRuleBuilderTyped forResourcesOfType(String theType) {
			return RepositoryValidatingRuleBuilder.this.forResourcesOfType(theType);
		}

		@Override
		public List<IRepositoryValidatingRule> build() {
			return RepositoryValidatingRuleBuilder.this.build();
		}
	}

	public final class RepositoryValidatingRuleBuilderTyped {

		private final String myType;

		RepositoryValidatingRuleBuilderTyped(String theType) {
			myType = myFhirContext.getResourceType(theType);
		}

		/**
		 * Require any resource being persisted to declare conformance to at least one of the given profiles
		 * in <code>Resource.meta.profile</code>
		 */
		public FinalizedRule requireAtLeastOneProfileOf(String... theProfileOptions) {
			Validate.notNull(theProfileOptions, "theProfileOptions must not be null");
			requireAtLeastOneProfileOf(Arrays.asList(theProfileOptions));
			return new FinalizedRule();
		}

		/**
		 * Require any resource being persisted to declare conformance to at least one of the given profiles
		 * in <code>Resource.meta.profile</code>
		 */
		private FinalizedRule requireAtLeastOneProfileOf(Collection<String> theProfileOptions) {
			Validate.notNull(theProfileOptions, "theProfileOptions must not be null");
			Validate.notEmpty(theProfileOptions, "theProfileOptions must not be null or empty");
			myRules.add(new RuleRequireProfileDeclaration(myFhirContext, myType, theProfileOptions));
			return new FinalizedRule();
		}


		public FinalizedRule disallowProfile(String theProfileUrl) {
			Validate.notBlank(theProfileUrl, "theProfileUrl must not be null or empty");
			myRules.add(new RuleDisallowProfile(myFhirContext, myType, theProfileUrl));
			return new FinalizedRule();
		}

		public FinalizedRequireValidationRule requireValidation() {
			RequireValidationRule rule = new RequireValidationRule(myFhirContext, myType, myValidationSupport, myValidatorResourceFetcher);
			myRules.add(rule);
			return new FinalizedRequireValidationRule(rule);
		}


		public class FinalizedRequireValidationRule extends FinalizedRule {

			private final RequireValidationRule myRule;

			public FinalizedRequireValidationRule(RequireValidationRule theRule) {
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
			private FinalizedRequireValidationRule withBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel bestPracticeWarningLevel) {
				myRule.setBestPracticeWarningLevel(bestPracticeWarningLevel);
				return this;
			}
		}

	}

}
