package ca.uhn.fhir.rest.param;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

@SuppressWarnings("deprecation")
public class DateParam extends BaseParamWithPrefix<DateParam> implements IQueryParameterType , IQueryParameterOr<DateParam> {

	private final DateTimeDt myValue = new DateTimeDt();

	/**
	 * Constructor
	 */
	public DateParam() {
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
		Validate.inclusiveBetween(1, Long.MAX_VALUE, theDate, "theDate must not be 0 or negative");
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
	 * Constructor
	 * 
	 * @deprecated Use constructors with {@link ParamPrefixEnum} parameter instead, as {@link QuantityCompararatorEnum}
	 *             is deprecated
	 */
	@Deprecated
	public DateParam(QuantityCompararatorEnum theComparator, Date theDate) {
		setPrefix(toPrefix(theComparator));
		setValue(theDate);
	}

	/**
	 * Constructor
	 * 
	 * @deprecated Use constructors with {@link ParamPrefixEnum} parameter instead, as {@link QuantityCompararatorEnum}
	 *             is deprecated
	 */
	@Deprecated
	public DateParam(QuantityCompararatorEnum theComparator, DateTimeDt theDate) {
		setPrefix(toPrefix(theComparator));
		setValue(theDate);
	}

	/**
	 * Constructor
	 * 
	 * @deprecated Use constructors with {@link ParamPrefixEnum} parameter instead, as {@link QuantityCompararatorEnum}
	 *             is deprecated
	 */
	@Deprecated
	public DateParam(QuantityCompararatorEnum theComparator, IPrimitiveType<Date> theDate) {
		setPrefix(toPrefix(theComparator));
		setValue(theDate);
	}

	/**
	 * Constructor
	 * 
	 * @deprecated Use constructors with {@link ParamPrefixEnum} parameter instead, as {@link QuantityCompararatorEnum}
	 *             is deprecated
	 */
	@Deprecated
	public DateParam(QuantityCompararatorEnum theComparator, long theDate) {
		Validate.inclusiveBetween(1, Long.MAX_VALUE, theDate, "theDate must not be 0 or negative");
		setPrefix(toPrefix(theComparator));
		setValue(new Date(theDate));
	}

	/**
	 * Constructor
	 * 
	 * @deprecated Use constructors with {@link ParamPrefixEnum} parameter instead, as {@link QuantityCompararatorEnum}
	 *             is deprecated
	 */
	@Deprecated
	public DateParam(QuantityCompararatorEnum theComparator, String theDate) {
		setPrefix(toPrefix(theComparator));
		setValueAsString(theDate);
	}

	/**
	 * Constructor which takes a complete [qualifier]{date} string.
	 * 
	 * @param theString
	 *           The string
	 */
	public DateParam(String theString) {
		setValueAsQueryToken(null, theString);
	}

	@Override
	String doGetQueryParameterQualifier() {
		return null;
	}

	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		StringBuilder b = new StringBuilder();
		if (getPrefix() != null) {
			b.append(ParameterUtil.escapeWithDefault(getPrefix().getValueForContext(theContext)));
		}
		
		if (myValue != null) {
			b.append(ParameterUtil.escapeWithDefault(myValue.getValueAsString()));
		}

		return b.toString();
	}

	@Override
	void doSetValueAsQueryToken(String theQualifier, String theValue) {
		setValueAsString(theValue);
	}

	public TemporalPrecisionEnum getPrecision() {
		if (myValue != null) {
			return myValue.getPrecision();
		}
		return null;
	}

	public Date getValue() {
		if (myValue != null) {
			return myValue.getValue();
		}
		return null;
	}

	public DateTimeDt getValueAsDateTimeDt() {
		if (myValue == null) {
			return null;
		}
		return new DateTimeDt(myValue.getValue());
	}

	public InstantDt getValueAsInstantDt() {
		if (myValue == null) {
			return null;
		}
		return new InstantDt(myValue.getValue());
	}

	public String getValueAsString() {
		if (myValue != null) {
			return myValue.getValueAsString();
		} else {
			return null;
		}
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

	private ParamPrefixEnum toPrefix(QuantityCompararatorEnum theComparator) {
		if (theComparator != null) {
			return ParamPrefixEnum.forDstu1Value(theComparator.getCode());
		}
		return null;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("prefix", getPrefix());
		b.append("value", getValueAsString());
		return b.build();
	}

	@Override
	public void  setValuesAsQueryTokens(QualifiedParamList theParameters) {
		setMissing(null);
		setPrefix(null);
		setValueAsString(null);
		
		if (theParameters.size() == 1) {
			setValueAsString(theParameters.get(0));
		} else if (theParameters.size() > 1) {
			throw new InvalidRequestException("This server does not support multi-valued dates for this paramater: " + theParameters);
		}
		
	}

	@Override
	public List<DateParam> getValuesAsQueryTokens() {
		return Collections.singletonList(this);
	}


}
