package ca.uhn.fhir.rest.gclient;

import java.util.Date;

import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;

public class DateParam implements IParam {

	private String myParamName;

	@Override
	public String getParamName() {
		return myParamName;
	}

	public DateParam(String theParamName) {
		myParamName = theParamName;
	}

	public IDateSpecifier after() {
		return new DateWithPrefix(">");
	}

	public IDateSpecifier afterOrEquals() {
		return new DateWithPrefix(">=");
	}

	public IDateSpecifier before() {
		return new DateWithPrefix("<=");
	}

	public IDateSpecifier beforeOrEquals() {
		return new DateWithPrefix("<=");
	}

	public IDateSpecifier exactly() {
		return new DateWithPrefix("");
	}

	private class Criterion implements ICriterion, ICriterionInternal {

		private String myValue;

		public Criterion(String theValue) {
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

	private class DateWithPrefix implements IDateSpecifier {
		private String myPrefix;

		public DateWithPrefix(String thePrefix) {
			myPrefix = thePrefix;
		}

		@Override
		public ICriterion day(Date theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.DAY);
			return new Criterion(myPrefix + dt.getValueAsString());
		}

		@Override
		public ICriterion day(String theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.DAY);
			return new Criterion(myPrefix + dt.getValueAsString());
		}

		@Override
		public ICriterion now() {
			DateTimeDt dt = new DateTimeDt();
			dt.setPrecision(TemporalPrecisionEnum.DAY);
			return new Criterion(myPrefix + dt.getValueAsString());
		}

		@Override
		public ICriterion second(Date theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.DAY);
			return new Criterion(myPrefix + dt.getValueAsString());
		}

		@Override
		public ICriterion second(String theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.DAY);
			return new Criterion(myPrefix + dt.getValueAsString());
		}

	}

	public interface IDateSpecifier {

		ICriterion day(Date theValue);

		ICriterion day(String theValue);

		ICriterion now();

		ICriterion second(Date theValue);

		ICriterion second(String theValue);

	}

}
