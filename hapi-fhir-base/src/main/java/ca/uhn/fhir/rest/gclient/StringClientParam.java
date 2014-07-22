package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.server.Constants;

/**
 * 
 * @author james
 *
 */
public class StringClientParam implements IParam {

	private final String myParamName;

	public StringClientParam(String theParamName) {
		myParamName = theParamName;
	}

	@Override
	public String getParamName() {
		return myParamName;
	}


	/**
	 * The string matches the given value (servers will often, but are not required to) implement this as a left match,
	 * meaning that a value of "smi" would match "smi" and "smith".
	 */
	public IStringMatch matches() {
		return new StringMatches();
	}

	/**
	 * The string matches exactly the given value
	 */
	public IStringMatch matchesExactly() {
		return new StringExactly();
	}

	public interface IStringMatch {

		ICriterion value(String theValue);

		ICriterion value(StringDt theValue);

	}

	private class StringExactly implements IStringMatch {
		@Override
		public ICriterion value(String theValue) {
			return new StringCriterion(getParamName() + Constants.PARAMQUALIFIER_STRING_EXACT, theValue);
		}

		@Override
		public ICriterion value(StringDt theValue) {
			return new StringCriterion(getParamName() + Constants.PARAMQUALIFIER_STRING_EXACT, theValue.getValue());
		}
	}

	private class StringMatches implements IStringMatch {
		@Override
		public ICriterion value(String theValue) {
			return new StringCriterion(getParamName(), theValue);
		}

		@Override
		public ICriterion value(StringDt theValue) {
			return new StringCriterion(getParamName(), theValue.getValue());
		}
	}

}
