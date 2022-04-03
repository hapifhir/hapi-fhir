package ca.uhn.fhir.rest.gclient;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;

abstract class BaseClientParam implements IParam {

	@Override
	public ICriterion<?> isMissing(boolean theMissing) {
		return new MissingCriterion(theMissing ? Constants.PARAMQUALIFIER_MISSING_TRUE : Constants.PARAMQUALIFIER_MISSING_FALSE);
	}

	private class MissingCriterion implements ICriterion<IParam>, ICriterionInternal
	{
		private String myParameterValue;


		public MissingCriterion(String theParameterValue) {
			myParameterValue = theParameterValue;
		}

		@Override
		public String getParameterValue(FhirContext theContext) {
			return myParameterValue;
		}

		@Override
		public String getParameterName() {
			return BaseClientParam.this.getParamName() + Constants.PARAMQUALIFIER_MISSING;
		}
		
	}
	
}
