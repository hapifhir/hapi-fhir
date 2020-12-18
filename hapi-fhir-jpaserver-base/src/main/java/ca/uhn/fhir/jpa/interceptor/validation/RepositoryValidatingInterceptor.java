package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.List;

@Interceptor
public class RepositoryValidatingInterceptor {

	private List<IRepositoryValidatingRule> myRules = Collections.emptyList();

	/**
	 * Provide the rules to use for validation.
	 */
	public void setRules(List<IRepositoryValidatingRule> theRules) {
		Validate.notNull(theRules, "theRules must not be null");
		myRules = theRules;
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void create(IBaseResource theResource) {
		handle(theResource);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void create(IBaseResource theOldResource, IBaseResource theNewResource) {
		handle(theNewResource);
	}

	private void handle(IBaseResource theNewResource) {
		for (IRepositoryValidatingRule nextRule : myRules) {
			IRepositoryValidatingRule.RuleEvaluation outcome = nextRule.evaluate(theNewResource);
			if (!outcome.isPasses()) {
				handleFailure(outcome);
			}
		}
	}

	protected void handleFailure(IRepositoryValidatingRule.RuleEvaluation theOutcome) {
		throw new PreconditionFailedException(theOutcome.getFailureDescription());
	}

}
