package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.DateUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.EQUAL;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN_OR_EQUALS;
import static java.lang.String.format;
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

@SuppressWarnings("UnusedReturnValue")
public class DateRangeParam implements IQueryParameterAnd<DateParam> {

	private static final long serialVersionUID = 1L;

	private DateParam myLowerBound;
	private DateParam myUpperBound;

	/**
	 * Basic constructor. Values must be supplied by calling {@link #setLowerBound(DateParam)} and
	 * {@link #setUpperBound(DateParam)}
	 */
	public DateRangeParam() {
		super();
	}

	/**
	 * Copy constructor.
	 */
	@SuppressWarnings("CopyConstructorMissesField")
	public DateRangeParam(DateRangeParam theDateRangeParam) {
		super();
		Validate.notNull(theDateRangeParam);
		setLowerBound(theDateRangeParam.getLowerBound());
		setUpperBound(theDateRangeParam.getUpperBound());
	}

	/**
	 * Constructor which takes two Dates representing the lower and upper bounds of the range (inclusive on both ends)
	 *
	 * @param theLowerBound A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public DateRangeParam(Date theLowerBound, Date theUpperBound) {
		this();
		setRangeFromDatesInclusive(theLowerBound, theUpperBound);
	}

	/**
	 * Sets the range from a single date param. If theDateParam has no qualifier, treats it as the lower and upper bound
	 * (e.g. 2011-01-02 would match any time on that day). If theDateParam has a qualifier, treats it as either the lower
	 * or upper bound, with no opposite bound.
	 */
	public DateRangeParam(DateParam theDateParam) {
		this();
		if (theDateParam == null) {
			throw new NullPointerException(Msg.code(1919) + "theDateParam can not be null");
		}
		if (theDateParam.isEmpty()) {
			throw new IllegalArgumentException(Msg.code(1920) + "theDateParam can not be empty");
		}
		if (theDateParam.getPrefix() == null) {
			setRangeFromDatesInclusive(theDateParam.getValueAsString(), theDateParam.getValueAsString());
		} else {
			switch (theDateParam.getPrefix()) {
				case NOT_EQUAL:
				case EQUAL:
					setRangeFromDatesInclusive(theDateParam.getValueAsString(), theDateParam.getValueAsString());
					break;
				case STARTS_AFTER:
				case GREATERTHAN:
				case GREATERTHAN_OR_EQUALS:
					if (theDateParam.getPrecision().ordinal() <= TemporalPrecisionEnum.MONTH.ordinal()) {
						theDateParam.setValueAsString(DateUtils.getCompletedDate(theDateParam.getValueAsString()).getRight());
					}
					validateAndSet(theDateParam, null);
					break;
				case ENDS_BEFORE:
				case LESSTHAN:
				case LESSTHAN_OR_EQUALS:
					if (theDateParam.getPrecision().ordinal() <= TemporalPrecisionEnum.MONTH.ordinal()) {
						theDateParam.setValueAsString(DateUtils.getCompletedDate(theDateParam.getValueAsString()).getLeft());
					}
					validateAndSet(null, theDateParam);
					break;
				default:
					// Should not happen
					throw new InvalidRequestException(Msg.code(1921) + "Invalid comparator for date range parameter:" + theDateParam.getPrefix() + ". This is a bug.");
			}
		}
	}

	/**
	 * Constructor which takes two Dates representing the lower and upper bounds of the range (inclusive on both ends)
	 *
	 * @param theLowerBound A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public DateRangeParam(DateParam theLowerBound, DateParam theUpperBound) {
		this();
		setRangeFromDatesInclusive(theLowerBound, theUpperBound);
	}

	/**
	 * Constructor which takes two Dates representing the lower and upper bounds of the range (inclusive on both ends)
	 *
	 * @param theLowerBound A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public DateRangeParam(IPrimitiveType<Date> theLowerBound, IPrimitiveType<Date> theUpperBound) {
		this();
		setRangeFromDatesInclusive(theLowerBound, theUpperBound);
	}

	/**
	 * Constructor which takes two strings representing the lower and upper bounds of the range (inclusive on both ends)
	 *
	 * @param theLowerBound An unqualified date param representing the lower date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Either theLowerBound or theUpperBound may both be populated, or
	 *                      one may be null, but it is not valid for both to be null.
	 * @param theUpperBound An unqualified date param representing the upper date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Either theLowerBound or theUpperBound may both be populated, or
	 *                      one may be null, but it is not valid for both to be null.
	 */
	public DateRangeParam(String theLowerBound, String theUpperBound) {
		this();
		setRangeFromDatesInclusive(theLowerBound, theUpperBound);
	}

	private void addParam(DateParam theParsed) throws InvalidRequestException {
		if (theParsed.getPrefix() == null){
			theParsed.setPrefix(EQUAL);
		}

		switch (theParsed.getPrefix()) {
			case NOT_EQUAL:
			case EQUAL:
				if (myLowerBound != null || myUpperBound != null) {
					throw new InvalidRequestException(Msg.code(1922) + "Can not have multiple date range parameters for the same param without a qualifier");
				}
				if (theParsed.getMissing() != null) {
					myLowerBound = theParsed;
					myUpperBound = theParsed;
				} else {
					myLowerBound = new DateParam(theParsed.getPrefix(), theParsed.getValueAsString());
					myUpperBound = new DateParam(theParsed.getPrefix(), theParsed.getValueAsString());
				}
				break;
			case GREATERTHAN:
			case GREATERTHAN_OR_EQUALS:
				if (myLowerBound != null) {
					throw new InvalidRequestException(Msg.code(1923) + "Can not have multiple date range parameters for the same param that specify a lower bound");
				}
				myLowerBound = theParsed;
				break;
			case LESSTHAN:
			case LESSTHAN_OR_EQUALS:
				if (myUpperBound != null) {
					throw new InvalidRequestException(Msg.code(1924) + "Can not have multiple date range parameters for the same param that specify an upper bound");
				}
				myUpperBound = theParsed;
				break;
			default:
				throw new InvalidRequestException(Msg.code(1925) + "Unknown comparator: " + theParsed.getPrefix());
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof DateRangeParam)) {
			return false;
		}
		DateRangeParam other = (DateRangeParam) obj;
		return Objects.equals(myLowerBound, other.myLowerBound) &&
			Objects.equals(myUpperBound, other.myUpperBound);
	}

	public DateParam getLowerBound() {
		return myLowerBound;
	}

	public DateRangeParam setLowerBound(DateParam theLowerBound) {
		validateAndSet(theLowerBound, myUpperBound);
		return this;
	}

	/**
	 * Sets the lower bound using a string that is compliant with
	 * FHIR dateTime format (ISO-8601).
	 * <p>
	 * This lower bound is assumed to have a <code>ge</code>
	 * (greater than or equals) modifier.
	 * </p>
	 * <p>
	 * Note: An operation can take a DateRangeParam. If only a single date is provided,
	 * it will still result in a DateRangeParam where the lower and upper bounds
	 * are the same value. As such, even though the prefixes for the lower and
	 * upper bounds default to <code>ge</code> and <code>le</code> respectively,
	 * the resulting prefix is effectively <code>eq</code> where only a single
	 * date is provided - as required by the FHIR specification (i.e. "If no
	 * prefix is present, the prefix <code>eq</code> is assumed").
	 * </p>
	 */
	public DateRangeParam setLowerBound(String theLowerBound) {
		setLowerBound(new DateParam(GREATERTHAN_OR_EQUALS, theLowerBound));
		return this;
	}

	/**
	 * Sets the lower bound to be greaterthan or equal to the given date
	 */
	public DateRangeParam setLowerBoundInclusive(Date theLowerBound) {
		validateAndSet(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, theLowerBound), myUpperBound);
		return this;
	}

	/**
	 * Sets the upper bound to be greaterthan or equal to the given date
	 */
	public DateRangeParam setUpperBoundInclusive(Date theUpperBound) {
		validateAndSet(myLowerBound, new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, theUpperBound));
		return this;
	}


	/**
	 * Sets the lower bound to be greaterthan to the given date
	 */
	public DateRangeParam setLowerBoundExclusive(Date theLowerBound) {
		validateAndSet(new DateParam(ParamPrefixEnum.GREATERTHAN, theLowerBound), myUpperBound);
		return this;
	}

	/**
	 * Sets the upper bound to be greaterthan to the given date
	 */
	public DateRangeParam setUpperBoundExclusive(Date theUpperBound) {
		validateAndSet(myLowerBound, new DateParam(ParamPrefixEnum.LESSTHAN, theUpperBound));
		return this;
	}

	/**
	 * Return the current lower bound as an integer representative of the date.
	 *
	 * e.g. 2019-02-22T04:22:00-0500 -> 20120922
	 */
	public Integer getLowerBoundAsDateInteger() {
		if (myLowerBound == null || myLowerBound.getValue() == null) {
			return null;
		}
		int retVal = DateUtils.convertDateToDayInteger(myLowerBound.getValue());

		if (myLowerBound.getPrefix() != null) {
			switch (myLowerBound.getPrefix()) {
				case GREATERTHAN:
				case STARTS_AFTER:
					retVal += 1;
					break;
				case EQUAL:
				case GREATERTHAN_OR_EQUALS:
				case NOT_EQUAL:
					break;
				case LESSTHAN:
				case APPROXIMATE:
				case LESSTHAN_OR_EQUALS:
				case ENDS_BEFORE:
					throw new IllegalStateException(Msg.code(1926) + "Invalid lower bound comparator: " + myLowerBound.getPrefix());
			}
		}
		return retVal;
	}

	/**
	 * Return the current upper bound as an integer representative of the date
	 *
	 * e.g. 2019-02-22T04:22:00-0500 -> 2019122
	 */
	public Integer getUpperBoundAsDateInteger() {
		if (myUpperBound == null || myUpperBound.getValue() == null) {
			return null;
		}
		int retVal = DateUtils.convertDateToDayInteger(myUpperBound.getValue());
		if (myUpperBound.getPrefix() != null) {
			switch (myUpperBound.getPrefix()) {
				case LESSTHAN:
				case ENDS_BEFORE:
					retVal -= 1;
					break;
				case EQUAL:
				case LESSTHAN_OR_EQUALS:
				case NOT_EQUAL:
					break;
				case GREATERTHAN_OR_EQUALS:
				case GREATERTHAN:
				case APPROXIMATE:
				case STARTS_AFTER:
					throw new IllegalStateException(Msg.code(1927) + "Invalid upper bound comparator: " + myUpperBound.getPrefix());
			}
		}
		return retVal;
	}

	public Date getLowerBoundAsInstant() {
		if (myLowerBound == null || myLowerBound.getValue() == null) {
			return null;
		}
		Date retVal = myLowerBound.getValue();

		if (myLowerBound.getPrecision().ordinal() <= TemporalPrecisionEnum.DAY.ordinal()) {
			retVal = DateUtils.getLowestInstantFromDate(retVal);
		}

		if (myLowerBound.getPrefix() != null) {
			switch (myLowerBound.getPrefix()) {
				case GREATERTHAN:
				case STARTS_AFTER:
					retVal = myLowerBound.getPrecision().add(retVal, 1);
					break;
				case EQUAL:
				case NOT_EQUAL:
				case GREATERTHAN_OR_EQUALS:
					break;
				case LESSTHAN:
				case APPROXIMATE:
				case LESSTHAN_OR_EQUALS:
				case ENDS_BEFORE:
					throw new IllegalStateException(Msg.code(1928) + "Invalid lower bound comparator: " + myLowerBound.getPrefix());
			}
		}
		return retVal;
	}

	public DateParam getUpperBound() {
		return myUpperBound;
	}

	/**
	 * Sets the upper bound using a string that is compliant with
	 * FHIR dateTime format (ISO-8601).
	 * <p>
	 * This upper bound is assumed to have a <code>le</code>
	 * (less than or equals) modifier.
	 * </p>
	 * <p>
	 * Note: An operation can take a DateRangeParam. If only a single date is provided,
	 * it will still result in a DateRangeParam where the lower and upper bounds
	 * are the same value. As such, even though the prefixes for the lower and
	 * upper bounds default to <code>ge</code> and <code>le</code> respectively,
	 * the resulting prefix is effectively <code>eq</code> where only a single
	 * date is provided - as required by the FHIR specificiation (i.e. "If no
	 * prefix is present, the prefix <code>eq</code> is assumed").
	 * </p>
	 */
	public DateRangeParam setUpperBound(String theUpperBound) {
		setUpperBound(new DateParam(LESSTHAN_OR_EQUALS, theUpperBound));
		return this;
	}

	public DateRangeParam setUpperBound(DateParam theUpperBound) {
		validateAndSet(myLowerBound, theUpperBound);
		return this;
	}

	public Date getUpperBoundAsInstant() {
		if (myUpperBound == null || myUpperBound.getValue() == null) {
			return null;
		}

		Date retVal = myUpperBound.getValue();

		if (myUpperBound.getPrecision().ordinal() <= TemporalPrecisionEnum.DAY.ordinal()) {
			retVal = DateUtils.getHighestInstantFromDate(retVal);
		}

		if (myUpperBound.getPrefix() != null) {
			switch (myUpperBound.getPrefix()) {
				case LESSTHAN:
				case ENDS_BEFORE:
					retVal = new Date(retVal.getTime() - 1L);
					break;
				case EQUAL:
				case NOT_EQUAL:
				case LESSTHAN_OR_EQUALS:
					retVal = myUpperBound.getPrecision().add(retVal, 1);
					retVal = new Date(retVal.getTime() - 1L);
					break;
				case GREATERTHAN_OR_EQUALS:
				case GREATERTHAN:
				case APPROXIMATE:
				case STARTS_AFTER:
					throw new IllegalStateException(Msg.code(1929) + "Invalid upper bound comparator: " + myUpperBound.getPrefix());
			}
		}
		return retVal;
	}

	@Override
	public List<DateParam> getValuesAsQueryTokens() {
		ArrayList<DateParam> retVal = new ArrayList<>();
		if (myLowerBound != null && myLowerBound.getMissing() != null) {
			retVal.add((myLowerBound));
		} else {
			if (myLowerBound != null && !myLowerBound.isEmpty()) {
				retVal.add((myLowerBound));
			}
			if (myUpperBound != null && !myUpperBound.isEmpty()) {
				retVal.add((myUpperBound));
			}
		}
		return retVal;
	}

	private boolean hasBound(DateParam bound) {
		return bound != null && !bound.isEmpty();
	}

	@Override
	public int hashCode() {
		return Objects.hash(myLowerBound, myUpperBound);
	}

	public boolean isEmpty() {
		return (getLowerBoundAsInstant() == null) && (getUpperBoundAsInstant() == null);
	}

	/**
	 * Sets the range from a pair of dates, inclusive on both ends
	 *
	 * @param theLowerBound A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public void setRangeFromDatesInclusive(Date theLowerBound, Date theUpperBound) {
		DateParam lowerBound = theLowerBound != null
			? new DateParam(GREATERTHAN_OR_EQUALS, theLowerBound) : null;
		DateParam upperBound = theUpperBound != null
			? new DateParam(LESSTHAN_OR_EQUALS, theUpperBound) : null;
		validateAndSet(lowerBound, upperBound);
	}

	/**
	 * Sets the range from a pair of dates, inclusive on both ends
	 *
	 * @param theLowerBound A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public void setRangeFromDatesInclusive(DateParam theLowerBound, DateParam theUpperBound) {
		validateAndSet(theLowerBound, theUpperBound);
	}

	/**
	 * Sets the range from a pair of dates, inclusive on both ends. Note that if
	 * theLowerBound is after theUpperBound, thie method will automatically reverse
	 * the order of the arguments in order to create an inclusive range.
	 *
	 * @param theLowerBound A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public void setRangeFromDatesInclusive(IPrimitiveType<Date> theLowerBound, IPrimitiveType<Date> theUpperBound) {
		IPrimitiveType<Date> lowerBound = theLowerBound;
		IPrimitiveType<Date> upperBound = theUpperBound;
		if (lowerBound != null && lowerBound.getValue() != null && upperBound != null && upperBound.getValue() != null) {
			if (lowerBound.getValue().after(upperBound.getValue())) {
				IPrimitiveType<Date> temp = lowerBound;
				lowerBound = upperBound;
				upperBound = temp;
			}
		}
		validateAndSet(
			lowerBound != null ? new DateParam(GREATERTHAN_OR_EQUALS, lowerBound) : null,
			upperBound != null ? new DateParam(LESSTHAN_OR_EQUALS, upperBound) : null);
	}

	/**
	 * Sets the range from a pair of dates, inclusive on both ends
	 *
	 * @param theLowerBound A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *                      "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or
	 *                      theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public void setRangeFromDatesInclusive(String theLowerBound, String theUpperBound) {
		DateParam lowerBound = theLowerBound != null
			? new DateParam(GREATERTHAN_OR_EQUALS, theLowerBound)
			: null;
		DateParam upperBound = theUpperBound != null
			? new DateParam(LESSTHAN_OR_EQUALS, theUpperBound)
			: null;
		if (isNotBlank(theLowerBound) && isNotBlank(theUpperBound) && theLowerBound.equals(theUpperBound)) {
			lowerBound.setPrefix(EQUAL);
			upperBound.setPrefix(EQUAL);
		}
		validateAndSet(lowerBound, upperBound);
	}

	@Override
	public void setValuesAsQueryTokens(FhirContext theContext, String theParamName, List<QualifiedParamList> theParameters)
		throws InvalidRequestException {

		boolean haveHadUnqualifiedParameter = false;
		for (QualifiedParamList paramList : theParameters) {
			if (paramList.size() == 0) {
				continue;
			}
			if (paramList.size() > 1) {
				throw new InvalidRequestException(Msg.code(1930) + "DateRange parameter does not support OR queries");
			}
			String param = paramList.get(0);

			/*
			 * Since ' ' is escaped as '+' we'll be nice to anyone might have accidentally not
			 * escaped theirs
			 */
			param = param.replace(' ', '+');

			DateParam parsed = new DateParam();
			parsed.setValueAsQueryToken(theContext, theParamName, paramList.getQualifier(), param);
			addParam(parsed);

			if (parsed.getPrefix() == null) {
				if (haveHadUnqualifiedParameter) {
					throw new InvalidRequestException(Msg.code(1931) + "Multiple date parameters with the same name and no qualifier (>, <, etc.) is not supported");
				}
				haveHadUnqualifiedParameter = true;
			}

		}

	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getClass().getSimpleName());
		b.append("[");
		if (hasBound(myLowerBound)) {
			if (myLowerBound.getPrefix() != null) {
				b.append(myLowerBound.getPrefix().getValue());
			}
			b.append(myLowerBound.getValueAsString());
		}
		if (hasBound(myUpperBound)) {
			if (hasBound(myLowerBound)) {
				b.append(" ");
			}
			if (myUpperBound.getPrefix() != null) {
				b.append(myUpperBound.getPrefix().getValue());
			}
			b.append(myUpperBound.getValueAsString());
		} else {
			if (!hasBound(myLowerBound)) {
				b.append("empty");
			}
		}
		b.append("]");
		return b.toString();
	}

	/**
	 * Note: An operation can take a DateRangeParam. If only a single date is provided,
	 * it will still result in a DateRangeParam where the lower and upper bounds
	 * are the same value. As such, even though the prefixes for the lower and
	 * upper bounds default to <code>ge</code> and <code>le</code> respectively,
	 * the resulting prefix is effectively <code>eq</code> where only a single
	 * date is provided - as required by the FHIR specificiation (i.e. "If no
	 * prefix is present, the prefix <code>eq</code> is assumed").
	 */
	private void validateAndSet(DateParam lowerBound, DateParam upperBound) {
		if (hasBound(lowerBound) && hasBound(upperBound)) {
			if (lowerBound.getValue().getTime() > upperBound.getValue().getTime()) {
				throw new DataFormatException(Msg.code(1932) + format(
					"Lower bound of %s is after upper bound of %s",
					lowerBound.getValueAsString(), upperBound.getValueAsString()));
			}
		}

		if (hasBound(lowerBound)) {
			if (lowerBound.getPrefix() == null) {
				lowerBound.setPrefix(GREATERTHAN_OR_EQUALS);
			}
			switch (lowerBound.getPrefix()) {
				case GREATERTHAN:
				case GREATERTHAN_OR_EQUALS:
				default:
					break;
				case LESSTHAN:
				case LESSTHAN_OR_EQUALS:
					throw new DataFormatException(Msg.code(1933) + "Lower bound comparator must be > or >=, can not be " + lowerBound.getPrefix().getValue());
			}
		}

		if (hasBound(upperBound)) {
			if (upperBound.getPrefix() == null) {
				upperBound.setPrefix(LESSTHAN_OR_EQUALS);
			}
			switch (upperBound.getPrefix()) {
				case LESSTHAN:
				case LESSTHAN_OR_EQUALS:
				default:
					break;
				case GREATERTHAN:
				case GREATERTHAN_OR_EQUALS:
					throw new DataFormatException(Msg.code(1934) + "Upper bound comparator must be < or <=, can not be " + upperBound.getPrefix().getValue());
			}
		}

		myLowerBound = lowerBound;
		myUpperBound = upperBound;
	}

}
