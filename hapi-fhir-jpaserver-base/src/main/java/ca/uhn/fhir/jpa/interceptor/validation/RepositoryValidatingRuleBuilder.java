package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class RepositoryValidatingRuleBuilder {

	private final FhirContext myFhirContext;
	private final List<IRepositoryValidatingRule> myRules = new ArrayList<>();

	private RepositoryValidatingRuleBuilder(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public RepositoryValidatingRuleBuilderTyped forResourcesOfType(String theType) {
		return new RepositoryValidatingRuleBuilderTyped(theType);
	}


	public final class RepositoryValidatingRuleBuilderTyped {

		private final String myType;

		public RepositoryValidatingRuleBuilderTyped(String theType) {
			myType = myFhirContext.getResourceType(theType);
		}

		/**
		 * Require any resource being persisted to declare conformance to at least one of the given profiles
		 * in <code>Resource.meta.profile</code>
		 */
		public RepositoryValidatingRuleBuilderTyped requireAtLeastOneProfileOf(String... theProfileOptions) {
			Validate.notNull(theProfileOptions, "theProfileOptions must not be null");
			return requireAtLeastOneProfileOf(Arrays.asList(theProfileOptions));
		}

		/**
		 * Require any resource being persisted to declare conformance to at least one of the given profiles
		 * in <code>Resource.meta.profile</code>
		 */
		private RepositoryValidatingRuleBuilderTyped requireAtLeastOneProfileOf(Collection<String> theProfileOptions) {
			Validate.notNull(theProfileOptions, "theProfileOptions must not be null");
			Validate.notEmpty(theProfileOptions, "theProfileOptions must not be null or empty");
			myRules.add(new RuleRequireProfileDeclaration(myFhirContext, myType, theProfileOptions));
			return this;
		}

		/**
		 * Create the repository validation rules
		 */
		public List<IRepositoryValidatingRule> build() {
			return myRules;
		}
	}

	public static RepositoryValidatingRuleBuilder newInstance(FhirContext theFhirCtx) {
		return new RepositoryValidatingRuleBuilder(theFhirCtx);
	}
}
