package ca.uhn.fhir.rest.gclient;

/*
 * #%L
 * HAPI FHIR Library
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

import static org.apache.commons.lang3.StringUtils.defaultString;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.rest.gclient.NumberParam.IMatches;

/**
 * Token parameter type for use in fluent client interfaces
 */
public class QuantityParam implements IParam {

	private String myParamName;

	public QuantityParam(String theParamName) {
		myParamName = theParamName;
	}

	public IMatches<IAndUnits> approximately() {
		return new NumberParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits("~", Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits("~", theNumber);
			}
		};
	}

	public IMatches<IAndUnits> exactly() {
		return new NumberParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits("", Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits("", theNumber);
			}
		};
	}

	@Override
	public String getParamName() {
		return myParamName;
	}

	public IMatches<IAndUnits> greaterThan() {
		return new NumberParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(">", Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(">", theNumber);
			}
		};
	}

	public IMatches<IAndUnits> greaterThanOrEquals() {
		return new NumberParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(">=", Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(">=", theNumber);
			}
		};
	}

	public IMatches<IAndUnits> lessThan() {
		return new NumberParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits("<", Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits("<", theNumber);
			}
		};
	}

	public IMatches<IAndUnits> lessThanOrEquals() {
		return new NumberParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits("<=", Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits("<=", theNumber);
			}
		};
	}

	public IMatches<IAndUnits> withComparator(QuantityCompararatorEnum theComparator) {
		final String cmp = theComparator != null ? theComparator.getCode() : "";
		return new NumberParam.IMatches<IAndUnits>() {
			@Override
			public IAndUnits number(long theNumber) {
				return new AndUnits(cmp, Long.toString(theNumber));
			}

			@Override
			public IAndUnits number(String theNumber) {
				return new AndUnits(cmp, theNumber);
			}
		};
	}

	public interface IAndUnits {

		ICriterion andNoUnits();

		ICriterion andUnits(String theUnits);

		ICriterion andUnits(String theSystem, String theUnits);
	}

	private class AndUnits implements IAndUnits {

		private String myToken1;

		public AndUnits(String theComparator, String theNumber) {
			myToken1 = defaultString(theComparator) + defaultString(theNumber);
		}

		@Override
		public ICriterion andNoUnits() {
			return andUnits(null, null);
		}

		@Override
		public ICriterion andUnits(String theUnits) {
			return andUnits(theUnits, null);
		}

		@Override
		public ICriterion andUnits(String theSystem, String theUnits) {
			return new StringCriterion(getParamName(), myToken1 + "|" + defaultString(theSystem) + "|" + defaultString(theUnits));
		}

	}

}
