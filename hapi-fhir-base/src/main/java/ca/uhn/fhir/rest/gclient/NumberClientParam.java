package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.rest.param.ParamPrefixEnum;

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

/**
 * Token parameter type for use in fluent client interfaces
 */
public class NumberClientParam extends BaseClientParam  implements IParam {

	private final String myParamName;

	public NumberClientParam(String theParamName) {
		myParamName = theParamName;
	}

	public IMatches<ICriterion<NumberClientParam>> exactly() {
		return new IMatches<ICriterion<NumberClientParam>>() {
			@Override
			public ICriterion<NumberClientParam> number(long theNumber) {
				return new StringCriterion<>(getParamName(), Long.toString(theNumber));
			}

			@Override
			public ICriterion<NumberClientParam> number(String theNumber) {
				return new StringCriterion<>(getParamName(), (theNumber));
			}
		};
	}

	@Override
	public String getParamName() {
		return myParamName;
	}

	public IMatches<ICriterion<NumberClientParam>> greaterThan() {
		return new IMatches<ICriterion<NumberClientParam>>() {
			@Override
			public ICriterion<NumberClientParam> number(long theNumber) {
				return new StringCriterion<>(getParamName(), ParamPrefixEnum.GREATERTHAN, Long.toString(theNumber));
			}

			@Override
			public ICriterion<NumberClientParam> number(String theNumber) {
				return new StringCriterion<>(getParamName(), ParamPrefixEnum.GREATERTHAN, (theNumber));
			}
		};
	}

	public IMatches<ICriterion<NumberClientParam>> greaterThanOrEqual() {
		return new IMatches<ICriterion<NumberClientParam>>() {
			@Override
			public ICriterion<NumberClientParam> number(long theNumber) {
				return new StringCriterion<>(getParamName(), ParamPrefixEnum.GREATERTHAN_OR_EQUALS, Long.toString(theNumber));
			}

			@Override
			public ICriterion<NumberClientParam> number(String theNumber) {
				return new StringCriterion<>(getParamName(), ParamPrefixEnum.GREATERTHAN_OR_EQUALS, (theNumber));
			}
		};
	}

	public IMatches<ICriterion<NumberClientParam>> lessThan() {
		return new IMatches<ICriterion<NumberClientParam>>() {
			@Override
			public ICriterion<NumberClientParam> number(long theNumber) {
				return new StringCriterion<>(getParamName(), ParamPrefixEnum.LESSTHAN, Long.toString(theNumber));
			}

			@Override
			public ICriterion<NumberClientParam> number(String theNumber) {
				return new StringCriterion<>(getParamName(), ParamPrefixEnum.LESSTHAN, (theNumber));
			}
		};
	}

	public IMatches<ICriterion<NumberClientParam>> lessThanOrEqual() {
		return new IMatches<ICriterion<NumberClientParam>>() {
			@Override
			public ICriterion<NumberClientParam> number(long theNumber) {
				return new StringCriterion<>(getParamName(), ParamPrefixEnum.LESSTHAN_OR_EQUALS, Long.toString(theNumber));
			}

			@Override
			public ICriterion<NumberClientParam> number(String theNumber) {
				return new StringCriterion<>(getParamName(), ParamPrefixEnum.LESSTHAN_OR_EQUALS, (theNumber));
			}
		};
	}

	public IMatches<ICriterion<NumberClientParam>> withPrefix(final ParamPrefixEnum thePrefix) {
		return new IMatches<ICriterion<NumberClientParam>>() {
			@Override
			public ICriterion<NumberClientParam> number(long theNumber) {
				return new StringCriterion<>(getParamName(), thePrefix, Long.toString(theNumber));
			}

			@Override
			public ICriterion<NumberClientParam> number(String theNumber) {
				return new StringCriterion<>(getParamName(), thePrefix, (theNumber));
			}
		};
	}

	public interface IMatches<T> {
		/**
		 * Creates a search criterion that matches against the given number
		 * 
		 * @param theNumber
		 *            The number
		 * @return A criterion
		 */
		T number(long theNumber);

		/**
		 * Creates a search criterion that matches against the given number
		 * 
		 * @param theNumber
		 *            The number
		 * @return A criterion
		 */
		T number(String theNumber);
	}

}
