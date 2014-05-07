package ca.uhn.fhir.rest.gclient;

public class StringParam {

	private String myParamName;

	public StringParam(String theParamName) {
		myParamName = theParamName;
	}

	public IStringExactly exactly() {
		return new StringExactly();
	}

	private class EqualsExactlyCriterion implements ICriterion, ICriterionInternal {

		private String myValue;

		public EqualsExactlyCriterion(String theValue) {
			myValue = theValue;
		}

		@Override
		public String getParameterName() {
			return myParamName;
		}

		@Override
		public String getParameterValue() {
			return myValue;
		}

	}

	public interface IStringExactly {

		ICriterion value(String theValue);

	}

	public class StringExactly implements IStringExactly {
		@Override
		public ICriterion value(String theValue) {
			return new EqualsExactlyCriterion(theValue);
		}
	}

}
