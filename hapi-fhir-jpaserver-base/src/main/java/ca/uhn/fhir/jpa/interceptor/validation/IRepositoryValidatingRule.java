package ca.uhn.fhir.jpa.interceptor.validation;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This is an internal API for HAPI FHIR. It is subject to change without warning.
 */
public interface IRepositoryValidatingRule {

	String getResourceType();

	RuleEvaluation evaluate(IBaseResource theResource);

	class RuleEvaluation {

		private boolean myPasses;
		private String myFailureDescription;

		private RuleEvaluation(boolean thePasses, String theFailureDescription) {
			myPasses = thePasses;
			myFailureDescription = theFailureDescription;
		}

		public boolean isPasses() {
			return myPasses;
		}

		public String getFailureDescription() {
			return myFailureDescription;
		}

		static RuleEvaluation forSuccess() {
			return new RuleEvaluation(true, null);
		}

		static RuleEvaluation forFailure(String theFailureDescription) {
			return new RuleEvaluation(false, theFailureDescription);
		}

	}
}
