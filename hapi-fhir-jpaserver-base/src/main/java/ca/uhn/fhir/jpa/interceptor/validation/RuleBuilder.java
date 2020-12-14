package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RuleBuilder {

	private final FhirContext myFhirContext;
	private final List<IRule> myRules = new ArrayList<>();

	public RuleBuilder(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public RuleBuilderTyped forResourcesOfType(String theType) {
		return new RuleBuilderTyped(theType);
	}


	public class RuleBuilderTyped {

		private final String myType;

		public RuleBuilderTyped(String theType) {
			myType = myFhirContext.getResourceType(theType);
		}

		/**
		 * Require any resource being persisted to declare conformance to at least one of the given profiles
		 * in <code>Resource.meta.profile</code>
		 */
		public RuleBuilderTyped requireAtLeastOneProfileOf(String... theProfileOptions) {
			Validate.notNull(theProfileOptions, "theProfileOptions must not be null");
			return requireAtLeastOneProfileOf(Arrays.asList(theProfileOptions));
		}

		/**
		 * Require any resource being persisted to declare conformance to at least one of the given profiles
		 * in <code>Resource.meta.profile</code>
		 */
		private RuleBuilderTyped requireAtLeastOneProfileOf(Collection<String> theProfileOptions) {
			Validate.notNull(theProfileOptions, "theProfileOptions must not be null");
			Validate.notEmpty(theProfileOptions, "theProfileOptions must not be null or empty");
			myRules.add(new RequireProfileTypedRule(myFhirContext, myType, theProfileOptions));
			return this;
		}


	}

}
