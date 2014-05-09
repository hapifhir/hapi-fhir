package ca.uhn.fhir.rest.gclient;

import static org.apache.commons.lang3.StringUtils.*;

import org.apache.commons.lang.StringUtils;

import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.rest.gclient.NumberParam.IMatches;

/**
 * Token parameter type for use in fluent client interfaces
 */
public class QuantityParam implements IParam {

	private String myParamName;

	@Override
	public String getParamName() {
		return myParamName;
	}

	public QuantityParam(String theParamName) {
		myParamName = theParamName;
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

	private class AndUnits implements IAndUnits {

		private String myToken1;

		public AndUnits(String theComparator, String theNumber) {
			myToken1 = StringUtils.defaultString(theComparator) + StringUtils.defaultString(theNumber);
		}

		@Override
		public ICriterion andUnits(String theUnits) {
			return andUnits(theUnits, null);
		}

		@Override
		public ICriterion andUnits(String theSystem, String theUnits) {
			return new StringCriterion(getParamName(), myToken1 + "|" + defaultString(theSystem) + "|" + defaultString(theUnits));
		}

		@Override
		public ICriterion andNoUnits() {
			return andUnits(null, null);
		}

	}

	public interface IAndUnits {

		ICriterion andNoUnits();

		ICriterion andUnits(String theUnits);

		ICriterion andUnits(String theSystem, String theUnits);
	}

}
