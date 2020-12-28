package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Interceptor
public class RepositoryValidatingInterceptor {

	private Multimap<String, IRepositoryValidatingRule> myRules = ArrayListMultimap.create();
	private FhirContext myFhirContext;

	/**
	 * Provide the FHIR Context (mandatory)
	 */
	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	/**
	 * Provide the rules to use for validation (mandatory)
	 */
	public void setRules(List<IRepositoryValidatingRule> theRules) {
		Validate.notNull(theRules, "theRules must not be null");
		myRules.clear();
		for (IRepositoryValidatingRule next : theRules) {
			myRules.put(next.getResourceType(), next);
		}
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
		Validate.notNull(myFhirContext, "No FhirContext has been set for this interceptor of type: %s", getClass());

		String resourceType = myFhirContext.getResourceType(theNewResource);
		Collection<IRepositoryValidatingRule> rules = myRules.get(resourceType);
		for (IRepositoryValidatingRule nextRule : rules) {
			IRepositoryValidatingRule.RuleEvaluation outcome = nextRule.evaluate(theNewResource);
			if (!outcome.isPasses()) {
				handleFailure(outcome);
			}
		}
	}

	protected void handleFailure(IRepositoryValidatingRule.RuleEvaluation theOutcome) {
		if (theOutcome.getOperationOutcome() != null) {
			throw new PreconditionFailedException(null, theOutcome.getOperationOutcome());
		}
		throw new PreconditionFailedException(theOutcome.getFailureDescription());
	}

}
