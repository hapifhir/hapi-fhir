package ca.uhn.fhir.rest.param;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DateParam extends BaseParamWithPrefix<DateParam> implements /*IQueryParameterType , */IQueryParameterOr<DateParam> {

	private static final long serialVersionUID = 1L;

	private final DateParamDateTimeHolder myValue = new DateParamDateTimeHolder();

	/**
	 * Constructor
	 */
	public DateParam() {
		super();
	}

	/**
	 * Constructor
	 */
	public DateParam(ParamPrefixEnum thePrefix, Date theDate) {
		setPrefix(thePrefix);
		setValue(theDate);
	}

	/**
	 * Constructor
	 */
	public DateParam(ParamPrefixEnum thePrefix, DateTimeDt theDate) {
		setPrefix(thePrefix);
		myValue.setValueAsString(theDate != null ? theDate.getValueAsString() : null);
	}

	/**
	 * Constructor
	 */
	public DateParam(ParamPrefixEnum thePrefix, IPrimitiveType<Date> theDate) {
		setPrefix(thePrefix);
		myValue.setValueAsString(theDate != null ? theDate.getValueAsString() : null);
	}

	/**
	 * Constructor
	 */
	public DateParam(ParamPrefixEnum thePrefix, long theDate) {
		ValidateUtil.isGreaterThan(theDate, 0, "theDate must not be 0 or negative");
		setPrefix(thePrefix);
		setValue(new Date(theDate));
	}

	/**
	 * Constructor
	 */
	public DateParam(ParamPrefixEnum thePrefix, String theDate) {
		setPrefix(thePrefix);
		setValueAsString(theDate);
	}


	/**
	 * Constructor which takes a complete [qualifier]{date} string.
	 * 
	 * @param theString
	 *           The string
	 */
	public DateParam(String theString) {
		setValueAsQueryToken(null, null, null, theString);
	}

	@Override
	String doGetQueryParameterQualifier() {
		return null;
	}

	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		StringBuilder b = new StringBuilder();
		if (getPrefix() != null) {
			b.append(ParameterUtil.escapeWithDefault(getPrefix().getValue()));
		}
		
		b.append(ParameterUtil.escapeWithDefault(myValue.getValueAsString()));

		return b.toString();
	}

	@Override
	void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		setValueAsString(theValue);
	}

	public TemporalPrecisionEnum getPrecision() {
			return myValue.getPrecision();
	}

	public Date getValue() {
			return myValue.getValue();
	}

	public String getValueAsString() {
			return myValue.getValueAsString();
	}

	@Override
	public List<DateParam> getValuesAsQueryTokens() {
		return Collections.singletonList(this);
	}

	/**
	 * Returns <code>true</code> if no date/time is specified. Note that this method does not check the comparator, so a
	 * QualifiedDateParam with only a comparator and no date/time is considered empty.
	 */
	public boolean isEmpty() {
		return myValue.isEmpty();
	}

	/**
	 * Sets the value of the param to the given date (sets to the {@link TemporalPrecisionEnum#MILLI millisecond}
	 * precision, and will be encoded using the system local time zone).
	 */
	public DateParam setValue(Date theValue) {
		myValue.setValue(theValue, TemporalPrecisionEnum.MILLI);
		return this;
	}

	/**
	 * Sets the value using a FHIR Date type, such as a {@link DateDt}, or a DateTimeType.
	 */
	public void setValue(IPrimitiveType<Date> theValue) {
		if (theValue != null) {
			myValue.setValueAsString(theValue.getValueAsString());
		} else {
			myValue.setValue(null);
		}
	}

	/**
	 * Accepts values with or without a prefix (e.g. <code>gt2011-01-01</code> and <code>2011-01-01</code>).
	 * If no prefix is provided in the given value, the {@link #getPrefix() existing prefix} is preserved
	 */
	public void setValueAsString(String theDate) {
		if (isNotBlank(theDate)) {
			ParamPrefixEnum existingPrefix = getPrefix();
			myValue.setValueAsString(super.extractPrefixAndReturnRest(theDate));
			if (getPrefix() == null) {
				setPrefix(existingPrefix);
			}
		} else {
			myValue.setValue(null);
		}
	}

	@Override
	public void  setValuesAsQueryTokens(FhirContext theContext, String theParamName, QualifiedParamList theParameters) {
		setMissing(null);
		setPrefix(null);
		setValueAsString(null);
		
		if (theParameters.size() == 1) {
			setValueAsString(theParameters.get(0));
		} else if (theParameters.size() > 1) {
			throw new InvalidRequestException(Msg.code(1939) + "This server does not support multi-valued dates for this parameter: " + theParameters);
		}
		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof DateParam)) {
			return false;
		}
		DateParam other = (DateParam) obj;
		return	Objects.equals(getValue(), other.getValue()) &&
					Objects.equals(getPrefix(), other.getPrefix());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getValue(), getPrefix());
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("prefix", getPrefix());
		b.append("value", getValueAsString());
		return b.build();
	}

	public static class DateParamDateTimeHolder extends BaseDateTimeDt {

		/**
		 * Constructor
		 */
		// LEAVE THIS AS PUBLIC!!
		@SuppressWarnings("WeakerAccess")
		public DateParamDateTimeHolder() {
			super();
		}

		@Override
		protected TemporalPrecisionEnum getDefaultPrecisionForDatatype() {
			return TemporalPrecisionEnum.SECOND;
		}

		@Override
		protected boolean isPrecisionAllowed(TemporalPrecisionEnum thePrecision) {
			return true;
		}
	}
}
