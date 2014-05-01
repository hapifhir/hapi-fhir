package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class DateRangeParam implements IQueryParameterAnd {

	private QualifiedDateParam myLowerBound;
	private QualifiedDateParam myUpperBound;

	/**
	 * Basic constructor. Values must be supplied by calling {@link #setLowerBound(QualifiedDateParam)} and {@link #setUpperBound(QualifiedDateParam)}
	 */
	public DateRangeParam() {
		// nothing
	}

	/**
	 * Constructor which takes two strings representing the lower and upper bounds of the range
	 * 
	 * @param theLowerBound
	 *            A qualified date param representing the lower date bound (optionally may include time), e.g. "2011-02-22" or "2011-02-22T13:12:00"
	 * @param theLowerBound
	 *            A qualified date param representing the upper date bound (optionally may include time), e.g. "2011-02-22" or "2011-02-22T13:12:00"
	 */
	public DateRangeParam(String theLowerBound, String theUpperBound) {
		myLowerBound = new QualifiedDateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, theLowerBound);
		myUpperBound = new QualifiedDateParam(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS, theUpperBound);
		validateAndThrowDataFormatExceptionIfInvalid();
	}

	/**
	 * Constructor which takes two Dates representing the lower and upper bounds of the range
	 * 
	 * @param theLowerBound
	 *            A qualified date param representing the lower date bound (optionally may include time), e.g. "2011-02-22" or "2011-02-22T13:12:00"
	 * @param theLowerBound
	 *            A qualified date param representing the upper date bound (optionally may include time), e.g. "2011-02-22" or "2011-02-22T13:12:00"
	 */
	public DateRangeParam(Date theLowerBound, Date theUpperBound) {
		myLowerBound = new QualifiedDateParam(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS, theLowerBound);
		myUpperBound = new QualifiedDateParam(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS, theUpperBound);
		validateAndThrowDataFormatExceptionIfInvalid();
	}
	
	
	private void validateAndThrowDataFormatExceptionIfInvalid() {
		boolean haveLowerBound = myLowerBound != null && myLowerBound.isEmpty() == false;
		boolean haveUpperBound = myUpperBound != null && myUpperBound.isEmpty() == false;
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

	private void addParam(QualifiedDateParam theParsed) throws InvalidRequestException {
		if (theParsed.getComparator() == null) {
			if (myLowerBound != null || myUpperBound != null) {
				throw new InvalidRequestException("Can not have multiple date range parameters for the same param without a qualifier");
			}

			myLowerBound = theParsed;
			myUpperBound = theParsed;
			// TODO: in this case, should set lower and upper to exact moments using specified precision
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

	public QualifiedDateParam getLowerBound() {
		return myLowerBound;
	}

	public QualifiedDateParam getUpperBound() {
		return myUpperBound;
	}

	@Override
	public List<List<String>> getValuesAsQueryTokens() {
		ArrayList<List<String>> retVal = new ArrayList<List<String>>();
		if (myLowerBound != null) {
			retVal.add(Collections.singletonList(myLowerBound.getValueAsQueryToken()));
		}
		if (myUpperBound != null) {
			retVal.add(Collections.singletonList(myUpperBound.getValueAsQueryToken()));
		}
		return retVal;
	}

	public void setLowerBound(QualifiedDateParam theLowerBound) {
		myLowerBound = theLowerBound;
		validateAndThrowDataFormatExceptionIfInvalid();
	}

	public void setUpperBound(QualifiedDateParam theUpperBound) {
		myUpperBound = theUpperBound;
		validateAndThrowDataFormatExceptionIfInvalid();
	}

	@Override
	public void setValuesAsQueryTokens(List<List<String>> theParameters) throws InvalidRequestException {
		for (List<String> paramList : theParameters) {
			if (paramList.size() == 0) {
				continue;
			}
			if (paramList.size() > 1) {
				throw new InvalidRequestException("DateRange parameter does not suppport OR queries");
			}
			String param = paramList.get(0);
			QualifiedDateParam parsed = new QualifiedDateParam();
			parsed.setValueAsQueryToken(param);
			addParam(parsed);
		}
	}

	public Date getLowerBoundAsInstant() {
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

	public Date getUpperBoundAsInstant() {
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

}
