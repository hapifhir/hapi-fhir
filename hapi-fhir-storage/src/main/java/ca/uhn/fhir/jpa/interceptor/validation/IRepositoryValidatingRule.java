package ca.uhn.fhir.jpa.interceptor.validation;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
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
	RuleEvaluation evaluate(RequestDetails theRequestDetails, @Nonnull IBaseResource theResource);

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

	}
}
