package ca.uhn.fhir.jpa.interceptor.validation;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;

/**
 * This is an internal API for HAPI FHIR. It is subject to change without warning.
 */
public interface IRepositoryValidatingRule {

	@Nonnull
	String getResourceType();

	@Nonnull
	RuleEvaluation evaluate(@Nonnull IBaseResource theResource);

	class RuleEvaluation {

		private final IBaseOperationOutcome myOperationOutcome;
		private IRepositoryValidatingRule myRule;
		private boolean myPasses;
		private String myFailureDescription;

		private RuleEvaluation(IRepositoryValidatingRule theRule, boolean thePasses, String theFailureDescription, IBaseOperationOutcome theOperationOutcome) {
			myRule = theRule;
			myPasses = thePasses;
			myFailureDescription = theFailureDescription;
			myOperationOutcome = theOperationOutcome;
		}

		public IBaseOperationOutcome getOperationOutcome() {
			return myOperationOutcome;
		}

		public IRepositoryValidatingRule getRule() {
			return myRule;
		}

		public boolean isPasses() {
			return myPasses;
		}

		public String getFailureDescription() {
			return myFailureDescription;
		}

		static RuleEvaluation forSuccess(IRepositoryValidatingRule theRule) {
			Validate.notNull(theRule);
			return new RuleEvaluation(theRule, true, null, null);
		}

		static RuleEvaluation forFailure(IRepositoryValidatingRule theRule, String theFailureDescription) {
			Validate.notNull(theRule);
			Validate.notNull(theFailureDescription);
			return new RuleEvaluation(theRule, false, theFailureDescription, null);
		}

		static RuleEvaluation forFailure(IRepositoryValidatingRule theRule, IBaseOperationOutcome theOperationOutcome) {
			Validate.notNull(theRule);
			Validate.notNull(theOperationOutcome);
			return new RuleEvaluation(theRule, false, null, theOperationOutcome);
		}

	}
}
