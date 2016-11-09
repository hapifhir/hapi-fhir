package ca.uhn.fhir.rest.gclient;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.Date;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;

/**
 * Date parameter type for use in fluent client interfaces
 */
public class DateClientParam  extends BaseClientParam implements IParam {

	private String myParamName;

	@Override
	public String getParamName() {
		return myParamName;
	}

	public DateClientParam(String theParamName) {
		myParamName = theParamName;
	}

	public IDateSpecifier after() {
		return new DateWithPrefix(ParamPrefixEnum.GREATERTHAN);
	}

	public IDateSpecifier afterOrEquals() {
		return new DateWithPrefix(ParamPrefixEnum.GREATERTHAN_OR_EQUALS);
	}

	public IDateSpecifier before() {
		return new DateWithPrefix(ParamPrefixEnum.LESSTHAN);
	}

	public IDateSpecifier beforeOrEquals() {
		return new DateWithPrefix(ParamPrefixEnum.LESSTHAN_OR_EQUALS);
	}

	public IDateSpecifier exactly() {
		return new DateWithPrefix(ParamPrefixEnum.EQUAL);
	}

	private class Criterion implements ICriterion<DateClientParam>, ICriterionInternal {

		private String myValue;
		private ParamPrefixEnum myPrefix;

		public Criterion(ParamPrefixEnum thePrefix, String theValue) {
			myPrefix = thePrefix;
			myValue = theValue;
		}

		@Override
		public String getParameterName() {
			return myParamName;
		}

		@Override
		public String getParameterValue(FhirContext theContext) {
			StringBuilder b = new StringBuilder();
			if (isNotBlank(myValue)) {
				if (myPrefix != null && myPrefix != ParamPrefixEnum.EQUAL) {
					b.append(myPrefix.getValueForContext(theContext));
				}
				b.append(myValue);
			}
			return b.toString();
		}

	}

	private class DateWithPrefix implements IDateSpecifier {
		private ParamPrefixEnum myPrefix;

		public DateWithPrefix(ParamPrefixEnum thePrefix) {
			myPrefix = thePrefix;
		}

		@Override
		public ICriterion<DateClientParam> day(Date theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.DAY);
			String valueAsString = dt.getValueAsString();
			return new Criterion(myPrefix, valueAsString);
		}

		@Override
		public ICriterion<DateClientParam> day(String theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.DAY);
			String valueAsString = dt.getValueAsString();
			return new Criterion(myPrefix , valueAsString);
		}

		@Override
		public ICriterion<DateClientParam> now() {
			DateTimeDt dt = DateTimeDt.withCurrentTime();
			dt.setPrecision(TemporalPrecisionEnum.SECOND);
			String valueAsString = dt.getValueAsString();
			return new Criterion(myPrefix , valueAsString);
		}

		@Override
		public ICriterion<DateClientParam> second(Date theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.SECOND);
			String valueAsString = dt.getValueAsString();
			return new Criterion(myPrefix , valueAsString);
		}

		@Override
		public ICriterion<DateClientParam> second(String theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.SECOND);
			String valueAsString = dt.getValueAsString();
			return new Criterion(myPrefix , valueAsString);
		}

	}

	public interface IDateSpecifier {

		ICriterion<DateClientParam> day(Date theValue);

		ICriterion<DateClientParam> day(String theValue);

		ICriterion<DateClientParam> now();

		ICriterion<DateClientParam> second(Date theValue);

		ICriterion<DateClientParam> second(String theValue);

	}

}
