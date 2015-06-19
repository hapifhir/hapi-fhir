package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class DateRangeParam implements IQueryParameterAnd<DateParam> {

	private DateParam myLowerBound;
	private DateParam myUpperBound;

	/**
	 * Basic constructor. Values must be supplied by calling {@link #setLowerBound(DateParam)} and
	 * {@link #setUpperBound(DateParam)}
	 */
	public DateRangeParam() {
		// nothing
	}

	/**
	 * Constructor which takes two Dates representing the lower and upper bounds of the range (inclusive on both ends)
	 * 
	 * @param theLowerBound
	 *            A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound
	 *            A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public DateRangeParam(Date theLowerBound, Date theUpperBound) {
		setRangeFromDatesInclusive(theLowerBound, theUpperBound);
	}

	/**
	 * Constructor which takes two Dates representing the lower and upper bounds of the range (inclusive on both ends)
	 * 
	 * @param theLowerBound
	 *            A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound
	 *            A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public DateRangeParam(DateTimeDt theLowerBound, DateTimeDt theUpperBound) {
		setRangeFromDatesInclusive(theLowerBound, theUpperBound);
	}

	/**
	 * Sets the range from a single date param. If theDateParam has no qualifier, treats it as the lower and upper bound
	 * (e.g. 2011-01-02 would match any time on that day). If theDateParam has a qualifier, treats it as either the
	 * lower or upper bound, with no opposite bound.
	 */
	public DateRangeParam(DateParam theDateParam) {
		if (theDateParam == null) {
			throw new NullPointerException("theDateParam can not be null");
		}
		if (theDateParam.isEmpty()) {
			throw new IllegalArgumentException("theDateParam can not be empty");
		}
		if (theDateParam.getComparator() == null) {
			setRangeFromDatesInclusive(theDateParam.getValueAsString(), theDateParam.getValueAsString());
		} else {
			switch (theDateParam.getComparator()) {
			case GREATERTHAN:
			case GREATERTHAN_OR_EQUALS:
				myLowerBound = theDateParam;
				myUpperBound = null;
				break;
			case LESSTHAN:
			case LESSTHAN_OR_EQUALS:
				myLowerBound = null;
				myUpperBound = theDateParam;
				break;
			default:
				// Should not happen
				throw new IllegalStateException("Unknown comparator:" + theDateParam.getComparator() + ". This is a bug.");
			}
		}
		validateAndThrowDataFormatExceptionIfInvalid();
	}

	/**
	 * Constructor which takes two strings representing the lower and upper bounds of the range (inclusive on both ends)
	 * 
	 * @param theLowerBound
	 *            An unqualified date param representing the lower date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound
	 *            An unqualified date param representing the upper date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public DateRangeParam(String theLowerBound, String theUpperBound) {
		setRangeFromDatesInclusive(theLowerBound, theUpperBound);
	}

	public DateParam getLowerBound() {
		return myLowerBound;
	}

	public Date getLowerBoundAsInstant() {
		if (myLowerBound == null) {
			return null;
		}
		Date retVal = myLowerBound.getValue();
		if (myLowerBound.getComparator() != null) {
			switch (myLowerBound.getComparator()) {
			case GREATERTHAN:
				retVal = myLowerBound.getPrecision().add(retVal, 1);
				break;
			case GREATERTHAN_OR_EQUALS:
				break;
			case LESSTHAN:
			case LESSTHAN_OR_EQUALS:
				throw new IllegalStateException("Unvalid lower bound comparator: " + myLowerBound.getComparator());
			}
		}
		return retVal;
	}

	public DateParam getUpperBound() {
		return myUpperBound;
	}

	public Date getUpperBoundAsInstant() {
		if (myUpperBound == null) {
			return null;
		}
		Date retVal = myUpperBound.getValue();
		if (myUpperBound.getComparator() != null) {
			switch (myUpperBound.getComparator()) {
			case LESSTHAN:
				retVal = new Date(retVal.getTime() - 1L);
				break;
			case LESSTHAN_OR_EQUALS:
				retVal = myUpperBound.getPrecision().add(retVal, 1);
				retVal = new Date(retVal.getTime() - 1L);
				break;
			case GREATERTHAN_OR_EQUALS:
			case GREATERTHAN:
				throw new IllegalStateException("Unvalid upper bound comparator: " + myUpperBound.getComparator());
			}
		}
		return retVal;
	}

	@Override
	public List<DateParam> getValuesAsQueryTokens() {
		ArrayList<DateParam> retVal = new ArrayList<DateParam>();
		if (myLowerBound != null && !myLowerBound.isEmpty()) {
			retVal.add((myLowerBound));
		}
		if (myUpperBound != null && !myUpperBound.isEmpty()) {
			retVal.add((myUpperBound));
		}
		return retVal;
	}

	public void setLowerBound(DateParam theLowerBound) {
		myLowerBound = theLowerBound;
		validateAndThrowDataFormatExceptionIfInvalid();
	}

	/**
	 * Sets the range from a pair of dates, inclusive on both ends
	 * 
	 * @param theLowerBound
	 *            A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound
	 *            A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public void setRangeFromDatesInclusive(Date theLowerBound, Date theUpperBound) {
		myLowerBound = theLowerBound != null ? new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, theLowerBound) : null;
		myUpperBound = theUpperBound != null ? new DateParam(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS, theUpperBound) : null;
		validateAndThrowDataFormatExceptionIfInvalid();
	}

	/**
	 * Sets the range from a pair of dates, inclusive on both ends
	 * 
	 * @param theLowerBound
	 *            A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound
	 *            A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public void setRangeFromDatesInclusive(DateTimeDt theLowerBound, DateTimeDt theUpperBound) {
		myLowerBound = theLowerBound != null ? new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, theLowerBound) : null;
		myUpperBound = theUpperBound != null ? new DateParam(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS, theUpperBound) : null;
		validateAndThrowDataFormatExceptionIfInvalid();
	}

	/**
	 * Sets the range from a pair of dates, inclusive on both ends
	 * 
	 * @param theLowerBound
	 *            A qualified date param representing the lower date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 * @param theUpperBound
	 *            A qualified date param representing the upper date bound (optionally may include time), e.g.
	 *            "2011-02-22" or "2011-02-22T13:12:00Z". Will be treated inclusively. Either theLowerBound or theUpperBound may both be populated, or one may be null, but it is not valid for both to be null.
	 */
	public void setRangeFromDatesInclusive(String theLowerBound, String theUpperBound) {
		myLowerBound = theLowerBound != null ? new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, theLowerBound) : null;
		myUpperBound = theUpperBound != null ? new DateParam(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS, theUpperBound) : null;
		validateAndThrowDataFormatExceptionIfInvalid();
	}

	public void setUpperBound(DateParam theUpperBound) {
		myUpperBound = theUpperBound;
		validateAndThrowDataFormatExceptionIfInvalid();
	}

	@Override
	public void setValuesAsQueryTokens(List<QualifiedParamList> theParameters) throws InvalidRequestException {
		
		boolean haveHadUnqualifiedParameter = false;
		for (QualifiedParamList paramList : theParameters) {
			if (paramList.size() == 0) {
				continue;
			}
			if (paramList.size() > 1) {
				throw new InvalidRequestException("DateRange parameter does not suppport OR queries");
			}
			String param = paramList.get(0);
			DateParam parsed = new DateParam();
			parsed.setValueAsQueryToken(paramList.getQualifier(), param);
			addParam(parsed);
			
			if (parsed.getComparator() == null) {
				if (haveHadUnqualifiedParameter) {
					throw new InvalidRequestException("Multiple date parameters with the same name and no qualifier (>, <, etc.) is not supported");					
				}
				haveHadUnqualifiedParameter=true;
			}
			
		}
		
	}

	private void addParam(DateParam theParsed) throws InvalidRequestException {
		if (theParsed.getComparator() == null) {
			if (myLowerBound != null || myUpperBound != null) {
				throw new InvalidRequestException("Can not have multiple date range parameters for the same param without a qualifier");
			}

			myLowerBound = new DateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, theParsed.getValueAsString());
			myUpperBound = new DateParam(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS, theParsed.getValueAsString());
			
		} else {

			switch (theParsed.getComparator()) {
			case GREATERTHAN:
			case GREATERTHAN_OR_EQUALS:
				if (myLowerBound != null) {
					throw new InvalidRequestException("Can not have multiple date range parameters for the same param that specify a lower bound");
				}
				myLowerBound = theParsed;
				break;
			case LESSTHAN:
			case LESSTHAN_OR_EQUALS:
				if (myUpperBound != null) {
					throw new InvalidRequestException("Can not have multiple date range parameters for the same param that specify an upper bound");
				}
				myUpperBound = theParsed;
				break;
			default:
				throw new InvalidRequestException("Unknown comparator: " + theParsed.getComparator());
			}

		}
	}

	private void validateAndThrowDataFormatExceptionIfInvalid() {
		boolean haveLowerBound = haveLowerBound();
		boolean haveUpperBound = haveUpperBound();
		if (haveLowerBound && haveUpperBound) {
			if (myLowerBound.getValue().after(myUpperBound.getValue())) {
				throw new DataFormatException("Lower bound of " + myLowerBound.getValueAsString() + " is after upper bound of " + myUpperBound.getValueAsString());
			}
		}

		if (haveLowerBound) {
			if (myLowerBound.getComparator() == null) {
				myLowerBound.setComparator(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS);
			}
			switch (myLowerBound.getComparator()) {
			case GREATERTHAN:
			case GREATERTHAN_OR_EQUALS:
			default:
				break;
			case LESSTHAN:
			case LESSTHAN_OR_EQUALS:
				throw new DataFormatException("Lower bound comparator must be > or >=, can not be " + myLowerBound.getComparator().getCode());
			}
		}

		if (haveUpperBound) {
			if (myUpperBound.getComparator() == null) {
				myUpperBound.setComparator(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS);
			}
			switch (myUpperBound.getComparator()) {
			case LESSTHAN:
			case LESSTHAN_OR_EQUALS:
			default:
				break;
			case GREATERTHAN:
			case GREATERTHAN_OR_EQUALS:
				throw new DataFormatException("Upper bound comparator must be < or <=, can not be " + myUpperBound.getComparator().getCode());
			}
		}

	}

	private boolean haveUpperBound() {
		return myUpperBound != null && myUpperBound.isEmpty() == false;
	}

	private boolean haveLowerBound() {
		return myLowerBound != null && myLowerBound.isEmpty() == false;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getClass().getSimpleName());
		b.append("[");
		if (haveLowerBound()) {
			if (myLowerBound.getComparator() != null) {
				b.append(myLowerBound.getComparator().getCode());
			}
			b.append(myLowerBound.getValueAsString());
		}
		if (haveUpperBound()) {
			if(haveLowerBound()) {
				b.append(" ");
			}
			if (myUpperBound.getComparator() != null) {
				b.append(myUpperBound.getComparator().getCode());
			}
			b.append(myUpperBound.getValueAsString());
		} else {
			if (!haveLowerBound()) {
				b.append("empty");
			}
		}
		b.append("]");
		return b.toString();
	}

}
