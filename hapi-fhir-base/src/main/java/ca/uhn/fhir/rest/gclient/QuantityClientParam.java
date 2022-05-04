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
import static org.apache.commons.lang3.StringUtils.defaultString;

import ca.uhn.fhir.rest.gclient.NumberClientParam.IMatches;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;

/**
 * Quantity parameter type for use in fluent client interfaces
 */
@SuppressWarnings("deprecation")
public class QuantityClientParam extends BaseClientParam  implements IParam {

	private String myParamName;

	public QuantityClientParam(String theParamName) {
		myParamName = theParamName;
	}

	public IMatches<IAndUnits> approximately() {
		return new NumberClientParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(ParamPrefixEnum.APPROXIMATE, Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(ParamPrefixEnum.APPROXIMATE, theNumber);
			}
		};
	}

	public IMatches<IAndUnits> exactly() {
		return new NumberClientParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(null, Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(null, theNumber);
			}
		};
	}

	@Override
	public String getParamName() {
		return myParamName;
	}

	public IMatches<IAndUnits> greaterThan() {
		return new NumberClientParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(ParamPrefixEnum.GREATERTHAN, Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(ParamPrefixEnum.GREATERTHAN, theNumber);
			}
		};
	}

	public IMatches<IAndUnits> greaterThanOrEquals() {
		return new NumberClientParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, theNumber);
			}
		};
	}

	public IMatches<IAndUnits> lessThan() {
		return new NumberClientParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(ParamPrefixEnum.LESSTHAN, Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(ParamPrefixEnum.LESSTHAN, theNumber);
			}
		};
	}

	public IMatches<IAndUnits> lessThanOrEquals() {
		return new NumberClientParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(ParamPrefixEnum.LESSTHAN_OR_EQUALS, Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(ParamPrefixEnum.LESSTHAN_OR_EQUALS, theNumber);
			}
		};
	}

	/**
	 * Use the given quantity prefix
	 * 
	 * @param thePrefix The prefix, or <code>null</code> for no prefix
	 */
	public IMatches<IAndUnits> withPrefix(final ParamPrefixEnum thePrefix) {
		return new NumberClientParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(thePrefix, Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(thePrefix, theNumber);
			}
		};
	}

	public interface IAndUnits {

		ICriterion<QuantityClientParam> andNoUnits();

		ICriterion<QuantityClientParam> andUnits(String theUnits);

		ICriterion<QuantityClientParam> andUnits(String theSystem, String theUnits);
	}

	private class AndUnits implements IAndUnits {

		private ParamPrefixEnum myPrefix;
		private String myValue;

		public AndUnits(ParamPrefixEnum thePrefix, String theNumber) {
			myPrefix = thePrefix;
			myValue = theNumber;
		}

		@Override
		public ICriterion<QuantityClientParam> andNoUnits() {
			return andUnits(null, null);
		}

		@Override
		public ICriterion<QuantityClientParam> andUnits(String theUnits) {
			return andUnits(null, theUnits);
		}

		@Override
		public ICriterion<QuantityClientParam> andUnits(String theSystem, String theUnits) {
			return new QuantityCriterion(getParamName(), myPrefix, myValue , defaultString(theSystem) , defaultString(theUnits));
		}

	}

}
