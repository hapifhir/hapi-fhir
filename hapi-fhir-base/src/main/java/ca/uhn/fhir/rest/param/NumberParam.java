package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import static org.apache.commons.lang3.StringUtils.*;

import java.math.BigDecimal;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;

public class NumberParam extends BaseParam implements IQueryParameterType {

	private QuantityDt myQuantity = new QuantityDt();

	public NumberParam() {
	}

	/**
	 * Constructor
	 * 
	 * @param theValue
	 *            A string value, e.g. "&gt;5.0"
	 */
	public NumberParam(String theValue) {
		setValueAsQueryToken(null, theValue);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getClass().getSimpleName());
		b.append("[");
		if (myQuantity.getComparator().isEmpty() == false) {
			b.append(myQuantity.getComparator().getValue());
		}
		if (myQuantity.getValue().isEmpty() == false) {
			b.append(myQuantity.getValue().toString());
		}
		b.append("]");
		return b.toString();
	}

	public QuantityCompararatorEnum getComparator() {
		return myQuantity.getComparator().getValueAsEnum();
	}
	
	public BigDecimal getValue() {
		return myQuantity.getValue().getValue();
	}
	
	
	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		super.setValueAsQueryToken(theQualifier, theValue);
		if (getMissing() != null && isBlank(theValue)) {
			return;
		}
		if (theValue.startsWith("<=")) {
			myQuantity.setComparator(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS);
			myQuantity.setValue(new BigDecimal(theValue.substring(2)));
		} else if (theValue.startsWith("<")) {
			myQuantity.setComparator(QuantityCompararatorEnum.LESSTHAN);
			myQuantity.setValue(new BigDecimal(theValue.substring(1)));
		} else if (theValue.startsWith(">=")) {
			myQuantity.setComparator(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS);
			myQuantity.setValue(new BigDecimal(theValue.substring(2)));
		} else if (theValue.startsWith(">")) {
			myQuantity.setComparator(QuantityCompararatorEnum.GREATERTHAN);
			myQuantity.setValue(new BigDecimal(theValue.substring(1)));
		} else {
			myQuantity.setComparator((QuantityCompararatorEnum) null);
			myQuantity.setValue(new BigDecimal(theValue));
		}
	}

	@Override
	public String getValueAsQueryToken() {
		if (getMissing() != null) {
			return super.getQueryParameterQualifier();
		}
		
		StringBuilder b = new StringBuilder();
		if (myQuantity.getComparator().isEmpty() == false) {
			b.append(myQuantity.getComparator().getValue());
		}
		if (myQuantity.getValue().isEmpty() == false) {
			b.append(myQuantity.getValue().toString());
		}
		return b.toString();
	}

	@Override
	public String getQueryParameterQualifier() {
		return super.getQueryParameterQualifier();
	}

}
