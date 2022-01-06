package ca.uhn.fhir.rest.gclient;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

	private class Criterion implements IDateCriterion, ICriterionInternal {

		private String myValue;
		private ParamPrefixEnum myPrefix;
		private Criterion orCriterion;

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
			if (orCriterion != null) {
				String orValue = orCriterion.getParameterValue(theContext);
				if (isNotBlank(orValue)) {
					b.append(orValue);
				}
			}
			if (isNotBlank(myValue)) {
				if (b.length() > 0) {
					b.append(',');
				}
				if (myPrefix != null && myPrefix != ParamPrefixEnum.EQUAL) {
					b.append(myPrefix.getValue());
				}
				b.append(myValue);
			}
			return b.toString();
		}

		@Override
		public IDateSpecifier orAfter() {
			return new DateWithPrefix(ParamPrefixEnum.GREATERTHAN, this);
		}

		@Override
		public IDateSpecifier orAfterOrEquals() {
			return new DateWithPrefix(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, this);
		}

		@Override
		public IDateSpecifier orBefore() {
			return new DateWithPrefix(ParamPrefixEnum.LESSTHAN, this);
		}

		@Override
		public IDateSpecifier orBeforeOrEquals() {
			return new DateWithPrefix(ParamPrefixEnum.LESSTHAN_OR_EQUALS, this);
		}

		@Override
		public IDateSpecifier orExactly() {
			return new DateWithPrefix(ParamPrefixEnum.EQUAL, this);
		}

	}

	private class DateWithPrefix implements IDateSpecifier {
		private ParamPrefixEnum myPrefix;
		private Criterion previous = null;

		public DateWithPrefix(ParamPrefixEnum thePrefix, Criterion previous) {
			myPrefix = thePrefix;
			this.previous = previous;
		}

		public DateWithPrefix(ParamPrefixEnum thePrefix) {
			myPrefix = thePrefix;
		}

		@Override
		public IDateCriterion day(Date theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.DAY);
			return constructCriterion(dt);
		}

		@Override
		public IDateCriterion day(String theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.DAY);
			return constructCriterion(dt);
		}

		@Override
		public IDateCriterion now() {
			DateTimeDt dt = DateTimeDt.withCurrentTime();
			dt.setPrecision(TemporalPrecisionEnum.SECOND);
			return constructCriterion(dt);
		}

		@Override
		public IDateCriterion second(Date theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.SECOND);
			return constructCriterion(dt);
		}

		@Override
		public IDateCriterion second(String theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.SECOND);
			return constructCriterion(dt);
		}

		@Override
		public IDateCriterion millis(Date theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.MILLI);
			return constructCriterion(dt);
		}

		@Override
		public IDateCriterion millis(String theValue) {
			DateTimeDt dt = new DateTimeDt(theValue);
			dt.setPrecision(TemporalPrecisionEnum.MILLI);
			return constructCriterion(dt);
		}

		private IDateCriterion constructCriterion(DateTimeDt dt) {
			String valueAsString = dt.getValueAsString();
			Criterion criterion = new Criterion(myPrefix, valueAsString);
			if (previous != null) {
				criterion.orCriterion = previous;
			}
			return criterion;
		}
	}

	public interface IDateSpecifier {

		IDateCriterion day(Date theValue);

		IDateCriterion day(String theValue);

		IDateCriterion now();

		IDateCriterion second(Date theValue);

		IDateCriterion second(String theValue);

		IDateCriterion millis(Date theValue);

		IDateCriterion millis(String theValue);

	}

	public interface IDateCriterion extends ICriterion<DateClientParam> {
		IDateSpecifier orAfter();

		IDateSpecifier orAfterOrEquals();

		IDateSpecifier orBefore();

		IDateSpecifier orBeforeOrEquals();

		IDateSpecifier orExactly();
	}

}
