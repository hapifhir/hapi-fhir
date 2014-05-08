package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.rest.server.Constants;

public class StringParam implements IParam {

	private String myParamName;

	public StringParam(String theParamName) {
		myParamName = theParamName;
	}

	/**
	 * The string matches exactly the given value
	 */
	public IStringMatch matchesExactly() {
		return new StringExactly();
	}

	/**
	 * The string matches the given value (servers will often, but are not required to) implement this 
	 * as a left match, meaning that a value of "smi" would match "smi" and "smith".
	 */
	public IStringMatch matches() {
		return new StringMatches();
	}

	@Override
	public String getParamName() {
		return myParamName;
	}

	public interface IStringMatch {

		ICriterion value(String theValue);

	}

	private class StringExactly implements IStringMatch {
		@Override
		public ICriterion value(String theValue) {
			return new StringCriterion(getParamName() + Constants.PARAMNAME_SUFFIX_EXACT, theValue);
		}
	}

	private class StringMatches implements IStringMatch {
		@Override
		public ICriterion value(String theValue) {
			return new StringCriterion(getParamName(), theValue);
		}
	}

}
