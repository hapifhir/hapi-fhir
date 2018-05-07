package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import java.util.Arrays;
import java.util.List;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.Constants;

/**
 * 
 * @author james
 *
 */
public class StringClientParam extends BaseClientParam implements IParam {

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

		/**
		 * Requests that resources be returned which match the given value
		 */
		ICriterion<StringClientParam> value(String theValue);

		/**
		 * Requests that resources be returned which match ANY of the given values (this is an OR search, not an AND search). Note that to
		 * specify an AND search, simply add a subsequent {@link IQuery#where(ICriterion) where} criteria with the same
		 * parameter.
		 */
		ICriterion<StringClientParam> values(List<String> theValues);

		/**
		 * Requests that resources be returned which match the given value
		 */
		ICriterion<StringClientParam> value(StringDt theValue);

		/**
		 * Requests that resources be returned which match ANY of the given values (this is an OR search, not an AND search). Note that to
		 * specify an AND search, simply add a subsequent {@link IQuery#where(ICriterion) where} criteria with the same
		 * parameter.
		 */
		ICriterion<?> values(String... theValues);

	}

	private class StringExactly implements IStringMatch {
		@Override
		public ICriterion<StringClientParam> value(String theValue) {
			return new StringCriterion<StringClientParam>(getParamName() + Constants.PARAMQUALIFIER_STRING_EXACT, theValue);
		}

		@Override
		public ICriterion<StringClientParam> value(StringDt theValue) {
			return new StringCriterion<StringClientParam>(getParamName() + Constants.PARAMQUALIFIER_STRING_EXACT, theValue.getValue());
		}

		@Override
		public ICriterion<StringClientParam> values(List<String> theValue) {
			return new StringCriterion<StringClientParam>(getParamName() + Constants.PARAMQUALIFIER_STRING_EXACT, theValue);
		}

		@Override
		public ICriterion<?> values(String... theValues) {
			return new StringCriterion<StringClientParam>(getParamName() + Constants.PARAMQUALIFIER_STRING_EXACT, Arrays.asList(theValues));
		}
	}

	private class StringMatches implements IStringMatch {
		@Override
		public ICriterion<StringClientParam> value(String theValue) {
			return new StringCriterion<StringClientParam>(getParamName(), theValue);
		}

		@Override
		public ICriterion<StringClientParam> value(StringDt theValue) {
			return new StringCriterion<StringClientParam>(getParamName(), theValue.getValue());
		}

		@Override
		public ICriterion<StringClientParam> values(List<String> theValue) {
			return new StringCriterion<StringClientParam>(getParamName(), theValue);
		}

		@Override
		public ICriterion<?> values(String... theValues) {
			return new StringCriterion<StringClientParam>(getParamName(), Arrays.asList(theValues));
		}

	}

}
