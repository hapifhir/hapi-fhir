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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class DateParam extends DateTimeDt implements IQueryParameterType, IQueryParameterOr<DateParam> {

	private QuantityCompararatorEnum myComparator;
	private BaseParam myBase=new BaseParam.ComposableBaseParam();

	/**
	 * Constructor
	 */
	public DateParam() {
	}

	/**
	 * Constructor
	 */
	public DateParam(QuantityCompararatorEnum theComparator, Date theDate) {
		myComparator = theComparator;
		setValue(theDate);
	}

	/**
	 * Constructor
	 */
	public DateParam(QuantityCompararatorEnum theComparator, String theDate) {
		myComparator = theComparator;
		setValueAsString(theDate);
	}

	/**
	 * Constructor
	 */
	public DateParam(QuantityCompararatorEnum theComparator, DateTimeDt theDate) {
		myComparator = theComparator;
		setValueAsString(theDate != null ? theDate.getValueAsString() : null);
	}

	/**
	 * Constructor which takes a complete [qualifier]{date} string.
	 * 
	 * @param theString
	 *            The string
	 */
	public DateParam(String theString) {
		setValueAsQueryToken(null, theString);
	}

	/**
	 * Returns the comparator, or <code>null</code> if none has been set
	 */
	public QuantityCompararatorEnum getComparator() {
		return myComparator;
	}

	@Override
	public String getQueryParameterQualifier() {
		if (myBase.getMissing()!=null) {
			return myBase.getQueryParameterQualifier();
		}
		return null;
	}

	@Override
	public String getValueAsQueryToken() {
		if (myBase.getMissing()!=null) {
			return myBase.getValueAsQueryToken();
		}
		if (myComparator != null && getValue() != null) {
			return myComparator.getCode() + getValueAsString();
		} else if (myComparator == null && getValue() != null) {
			return getValueAsString();
		}
		return "";
	}

	@Override
	public List<DateParam> getValuesAsQueryTokens() {
		return Collections.singletonList(this);
	}

	/**
	 * Returns <code>true</code> if no date/time is specified. Note that this method does not check the comparator, so a
	 * QualifiedDateParam with only a comparator and no date/time is considered empty.
	 */
	@Override
	public boolean isEmpty() {
		// Just here to provide a javadoc
		return super.isEmpty();
	}

	public void setComparator(QuantityCompararatorEnum theComparator) {
		myComparator = theComparator;
	}

	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		myBase.setValueAsQueryToken(theQualifier, theValue);
		if (myBase.getMissing()!=null) {
			setValue(null);
			myComparator=null;
			return;
		}

		if (theValue.length() < 2) {
			throw new DataFormatException("Invalid qualified date parameter: " + theValue);
		}

		char char0 = theValue.charAt(0);
		char char1 = theValue.charAt(1);
		if (Character.isDigit(char0)) {
			setValueAsString(theValue);
		} else {
			int dateStart = 2;
			if (Character.isDigit(char1)) {
				dateStart = 1;
			}

			String comparatorString = theValue.substring(0, dateStart);
			QuantityCompararatorEnum comparator = QuantityCompararatorEnum.VALUESET_BINDER.fromCodeString(comparatorString);
			if (comparator == null) {
				throw new DataFormatException("Invalid date qualifier: " + comparatorString);
			}

			String dateString = theValue.substring(dateStart);
			setValueAsString(dateString);
			setComparator(comparator);
		}

	}

	@Override
	public void  setValuesAsQueryTokens(QualifiedParamList theParameters) {
		myBase.setMissing(null);
		myComparator = null;
		setValueAsString(null);
		
		if (theParameters.size() == 1) {
			setValueAsString(theParameters.get(0));
		} else if (theParameters.size() > 1) {
			throw new InvalidRequestException("This server does not support multi-valued dates for this paramater: " + theParameters);
		}
		
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getClass().getSimpleName());
		b.append("[");
		if (myComparator!=null) {
			b.append(myComparator.getCode());
		}
		b.append(getValueAsString());
		if (myBase.getMissing()!=null) {
			b.append(" missing=").append(myBase.getMissing());
		}
		b.append("]");
		return b.toString();
	}

	public InstantDt getValueAsInstantDt() {
		return new InstantDt(getValue());
	}

	public DateTimeDt getValueAsDateTimeDt() {
		return new DateTimeDt(getValueAsString());
	}

	@Override
	public Boolean getMissing() {
		return myBase.getMissing();
	}

	@Override
	public void setMissing(Boolean theMissing) {
		myBase.setMissing(theMissing);
	}

}
