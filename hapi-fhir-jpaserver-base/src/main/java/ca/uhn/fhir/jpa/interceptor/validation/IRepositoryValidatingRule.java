package ca.uhn.fhir.jpa.interceptor.validation;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This is an internal API for HAPI FHIR. It is subject to change without warning.
 */
public interface IRepositoryValidatingRule {

	String getResourceType();

	RuleEvaluation evaluate(IBaseResource theResource);

	class RuleEvaluation {

		private IRepositoryValidatingRule myRule;
		private boolean myPasses;
		private String myFailureDescription;

		private RuleEvaluation(IRepositoryValidatingRule theRule, boolean thePasses, String theFailureDescription) {
			myRule = theRule;
			myPasses = thePasses;
			myFailureDescription = theFailureDescription;
		}

		public boolean isPasses() {
			return myPasses;
		}

		public String getFailureDescription() {
			return myFailureDescription;
		}

		static RuleEvaluation forSuccess(IRepositoryValidatingRule theRule) {
			return new RuleEvaluation(theRule, true, null);
		}

		static RuleEvaluation forFailure(IRepositoryValidatingRule theRule, String theFailureDescription) {
			return new RuleEvaluation(theRule, false, theFailureDescription);
		}

	}
}
