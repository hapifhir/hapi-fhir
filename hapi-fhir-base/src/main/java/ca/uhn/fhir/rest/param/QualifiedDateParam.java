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

import java.util.Date;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.parser.DataFormatException;

public class QualifiedDateParam extends DateTimeDt implements IQueryParameterType {

	private QuantityCompararatorEnum myComparator;

	/**
	 * Constructor
	 */
	public QualifiedDateParam() {
	}
	
	/**
	 * Constructor
	 */
	public QualifiedDateParam(QuantityCompararatorEnum theComparator, Date theDate) {
		myComparator = theComparator;
		setValue(theDate);
	}

	/**
	 * Constructor
	 */
	public QualifiedDateParam(QuantityCompararatorEnum theComparator, String theDate) {
		myComparator = theComparator;
		setValueAsString(theDate);
	}

	/**
	 * Constructor which takes a complete [qualifier]{date} string.
	 * 
	 * @param theString The string
	 */
	public QualifiedDateParam(String theString) {
		setValueAsQueryToken(null, theString);
	}

	/**
	 * Returns the comparator, or <code>null</code> if none has been set
	 */
	public QuantityCompararatorEnum getComparator() {
		return myComparator;
	}

	@Override
	public String getValueAsQueryToken() {
		if (myComparator != null && getValue() != null) {
			return myComparator.getCode() + getValueAsString();
		} else if (myComparator == null && getValue() != null) {
			return getValueAsString();
		}
		return "";
	}

	/**
	 * Returns <code>true</code> if no date/time is specified. Note that this method
	 * does not check the comparator, so a QualifiedDateParam with only a comparator
	 * and no date/time is considered empty.
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
		if (theValue.length() < 2) {
			throw new DataFormatException("Invalid qualified date parameter: "+theValue);
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
			if (comparator==null) {
				throw new DataFormatException("Invalid date qualifier: "+comparatorString);
			}
			
			String dateString = theValue.substring(dateStart);
			setValueAsString(dateString);
			setComparator(comparator);
		}

	}

	

	@Override
	public String getQueryParameterQualifier(FhirContext theContext) {
		return null;
	}	

}
