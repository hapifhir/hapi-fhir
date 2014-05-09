package ca.uhn.fhir.rest.gclient;

/**
 * Token parameter type for use in fluent client interfaces
 */
public class NumberParam implements IParam {

	private String myParamName;

	public NumberParam(String theParamName) {
		myParamName = theParamName;
	}

	public IMatches<ICriterion> exactly() {
		return new IMatches<ICriterion>() {
			@Override
			public ICriterion number(long theNumber) {
				return new StringCriterion(getParamName(), Long.toString(theNumber));
			}

			@Override
			public ICriterion number(String theNumber) {
				return new StringCriterion(getParamName(), (theNumber));
			}
		};
	}

	@Override
	public String getParamName() {
		return myParamName;
	}

	public IMatches<ICriterion> greaterThan() {
		return new IMatches<ICriterion>() {
			@Override
			public ICriterion number(long theNumber) {
				return new StringCriterion(getParamName(), ">" + Long.toString(theNumber));
			}

			@Override
			public ICriterion number(String theNumber) {
				return new StringCriterion(getParamName(), ">" + (theNumber));
			}
		};
	}

	public IMatches<ICriterion> greaterThanOrEqual() {
		return new IMatches<ICriterion>() {
			@Override
			public ICriterion number(long theNumber) {
				return new StringCriterion(getParamName(), ">=" + Long.toString(theNumber));
			}

			@Override
			public ICriterion number(String theNumber) {
				return new StringCriterion(getParamName(), ">=" + (theNumber));
			}
		};
	}

	public IMatches<ICriterion> lessThan() {
		return new IMatches<ICriterion>() {
			@Override
			public ICriterion number(long theNumber) {
				return new StringCriterion(getParamName(), "<" + Long.toString(theNumber));
			}

			@Override
			public ICriterion number(String theNumber) {
				return new StringCriterion(getParamName(), "<" + (theNumber));
			}
		};
	}

	public IMatches<ICriterion> lessThanOrEqual() {
		return new IMatches<ICriterion>() {
			@Override
			public ICriterion number(long theNumber) {
				return new StringCriterion(getParamName(), "<=" + Long.toString(theNumber));
			}

			@Override
			public ICriterion number(String theNumber) {
				return new StringCriterion(getParamName(), "<=" + (theNumber));
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
