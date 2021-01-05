package ca.uhn.fhir.jpa.interceptor.validation;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
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

/**
 * This interceptor enforces validation rules on any data saved in a HAPI FHIR JPA repository.
 * See <a href="https://hapifhir.io/hapi-fhir/docs/validation/repository_validating_interceptor.html">Repository Validating Interceptor</a>
 * in the HAPI FHIR documentation for more information on how to use this.
 */
@Interceptor
public class RepositoryValidatingInterceptor {

	private Multimap<String, IRepositoryValidatingRule> myRules = ArrayListMultimap.create();
	private FhirContext myFhirContext;

	/**
	 * Constructor
	 *
	 * If this constructor is used, {@link #setFhirContext(FhirContext)} and {@link #setRules(List)} must be called
	 * manually before the interceptor is used.
	 */
	public RepositoryValidatingInterceptor() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param theFhirContext The FHIR Context (must not be <code>null</code>)
	 * @param theRules The rule list (must not be <code>null</code>)
	 */
	public RepositoryValidatingInterceptor(FhirContext theFhirContext, List<IRepositoryValidatingRule> theRules) {
		setFhirContext(theFhirContext);
		setRules(theRules);
	}

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

	/**
	 * Interceptor hook method. This method should not be called directly.
	 */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	void create(IBaseResource theResource) {
		handle(theResource);
	}

	/**
	 * Interceptor hook method. This method should not be called directly.
	 */
	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	void update(IBaseResource theOldResource, IBaseResource theNewResource) {
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
			String firstIssue = OperationOutcomeUtil.getFirstIssueDetails(myFhirContext, theOutcome.getOperationOutcome());
			throw new PreconditionFailedException(firstIssue, theOutcome.getOperationOutcome());
		}
		throw new PreconditionFailedException(theOutcome.getFailureDescription());
	}

}
