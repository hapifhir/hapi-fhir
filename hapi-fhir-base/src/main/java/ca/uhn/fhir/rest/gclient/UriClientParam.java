package ca.uhn.fhir.rest.gclient;

/*
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

import java.util.Arrays;
import java.util.List;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 *
 */
public class UriClientParam extends BaseClientParam  implements IParam {

	//TODO: handle :above and :below
	
	private final String myParamName;

	public UriClientParam(String theParamName) {
		myParamName = theParamName;
	}

	@Override
	public String getParamName() {
		return myParamName;
	}

	/**
	 * The string matches the given value (servers will often, but are not required to) implement this as a left match, meaning that a value of "smi" would match "smi" and "smith".
	 * @param theValue THIS PARAMETER DOES NOT DO ANYTHING - This method was added by accident
	 * 
	 * @deprecated theValue does not do anything, use {@link #matches()} instead
	 */
	@CoverageIgnore
	@Deprecated
	public IUriMatch matches(String theValue) {
		return new UriMatches();
	}

	/**
	 * The string matches the given value (servers will often, but are not required to) implement this as a left match, meaning that a value of "smi" would match "smi" and "smith".
	 */
	public IUriMatch matches() {
		return new UriMatches();
	}

	public interface IUriMatch {

		/**
		 * Requests that resources be returned which match the given value
		 */
		ICriterion<UriClientParam> value(String theValue);

		/**
		 * Requests that resources be returned which match ANY of the given values (this is an OR search). Note that to specify an AND search, simply add a subsequent {@link IQuery#where(ICriterion)
		 * where} criteria with the same parameter.
		 */
		ICriterion<UriClientParam> values(List<String> theValues);

		/**
		 * Requests that resources be returned which match the given value
		 */
		ICriterion<UriClientParam> value(StringDt theValue);

		/**
		 * Requests that resources be returned which match ANY of the given values (this is an OR search). Note that to specify an AND search, simply add a subsequent {@link IQuery#where(ICriterion)
		 * where} criteria with the same parameter.
		 */
		ICriterion<?> values(String... theValues);

	}

	private class UriMatches implements IUriMatch {
		@Override
		public ICriterion<UriClientParam> value(String theValue) {
			return new StringCriterion<UriClientParam>(getParamName(), theValue);
		}

		@Override
		public ICriterion<UriClientParam> value(StringDt theValue) {
			return new StringCriterion<UriClientParam>(getParamName(), theValue.getValue());
		}

		@Override
		public ICriterion<UriClientParam> values(List<String> theValue) {
			return new StringCriterion<UriClientParam>(getParamName(), theValue);
		}

		@Override
		public ICriterion<?> values(String... theValues) {
			return new StringCriterion<UriClientParam>(getParamName(), Arrays.asList(theValues));
		}

	}

}
