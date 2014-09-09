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

import static ca.uhn.fhir.rest.param.ParameterUtil.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

public class QuantityParam extends BaseParam implements IQueryParameterType {

	private boolean myApproximate;
	private QuantityDt myQuantity = new QuantityDt();

	/**
	 * Constructor
	 */
	public QuantityParam() {
	}

	/**
	 * Constructor
	 * 
	 * @param theComparator
	 *            The comparator, or <code>null</code> for an equals comparator
	 * @param theQuantity
	 *            A quantity value
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 */
	public QuantityParam(QuantityCompararatorEnum theComparator, BigDecimal theValue, String theSystem, String theUnits) {
		setComparator(theComparator);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param theComparator
	 *            The comparator, or <code>null</code> for an equals comparator
	 * @param theQuantity
	 *            A quantity value
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 */
	public QuantityParam(QuantityCompararatorEnum theComparator, double theValue, String theSystem, String theUnits) {
		setComparator(theComparator);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param theComparator
	 *            The comparator, or <code>null</code> for an equals comparator
	 * @param theQuantity
	 *            A quantity value
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 */
	public QuantityParam(QuantityCompararatorEnum theComparator, long theValue, String theSystem, String theUnits) {
		setComparator(theComparator);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param theQuantity
	 *            A quantity value (with no system or units), such as "100.0" or "<=4"
	 */
	public QuantityParam(String theQuantity) {
		setValueAsQueryToken(null, theQuantity);
	}

	/**
	 * Constructor
	 * 
	 * @param theQuantity
	 *            A quantity value (with no system or units), such as "100.0" or "<=4"
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 */
	public QuantityParam(String theQuantity, String theSystem, String theUnits) {
		setValueAsQueryToken(null, theQuantity);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	private void clear() {
		setMissing(null);
		myQuantity.setComparator((BoundCodeDt<QuantityCompararatorEnum>) null);
		myQuantity.setCode((CodeDt) null);
		myQuantity.setSystem((UriDt) null);
		myQuantity.setUnits((StringDt) null);
		myQuantity.setValue((DecimalDt) null);
		myApproximate = false;
	}

	public QuantityCompararatorEnum getComparator() {
		return myQuantity.getComparator().getValueAsEnum();
	}

	@Override
	public String getQueryParameterQualifier() {
		return super.getQueryParameterQualifier();
	}

	public UriDt getSystem() {
		return myQuantity.getSystem();
	}

	public String getUnits() {
		return myQuantity.getUnits().getValue();
	}

	public DecimalDt getValue() {
		return myQuantity.getValue();
	}

	@Override
	public String getValueAsQueryToken() {
		if (super.getMissing() != null) {
			return super.getValueAsQueryToken();
		}

		StringBuilder b = new StringBuilder();
		if (myApproximate) {
			b.append('~');
		} else {
			b.append(defaultString(escape(myQuantity.getComparator().getValue())));
		}

		if (!myQuantity.getValue().isEmpty()) {
			b.append(defaultString(escape(myQuantity.getValue().getValueAsString())));
		}
		b.append('|');
		if (!myQuantity.getSystem().isEmpty()) {
			b.append(defaultString(escape(myQuantity.getSystem().getValueAsString())));
		}
		b.append('|');
		if (!myQuantity.getUnits().isEmpty()) {
			b.append(defaultString(escape(myQuantity.getUnits().getValueAsString())));
		}

		return b.toString();
	}

	public boolean isApproximate() {
		return myApproximate;
	}

	public void setApproximate(boolean theApproximate) {
		myApproximate = theApproximate;
		if (theApproximate) {
			myQuantity.setComparator((QuantityCompararatorEnum) null);
		}
	}

	public QuantityParam setComparator(QuantityCompararatorEnum theComparator) {
		myQuantity.setComparator(theComparator);
		return this;
	}

	public QuantityParam setComparator(String theComparator) {
		if ("~".equals(theComparator)) {
			myApproximate = true;
			myQuantity.setComparator(((QuantityCompararatorEnum) null));
		} else {
			myApproximate = false;
			myQuantity.setComparator(QuantityCompararatorEnum.VALUESET_BINDER.fromCodeString(theComparator));
		}
		return this;
	}

	public QuantityParam setSystem(String theSystem) {
		myQuantity.setSystem(theSystem);
		return this;
	}

	public QuantityParam setSystem(UriDt theSystem) {
		myQuantity.setSystem(theSystem);
		return this;
	}

	public QuantityParam setUnits(String theUnits) {
		myQuantity.setUnits(theUnits);
		return this;
	}

	public QuantityParam setValue(BigDecimal theValue) {
		myQuantity.setValue(theValue);
		return this;
	}

	public QuantityParam setValue(DecimalDt theValue) {
		myQuantity.setValue(theValue);
		return this;
	}

	public QuantityParam setValue(double theValue) {
		myQuantity.setValue(theValue);
		return this;
	}

	public QuantityParam setValue(long theValue) {
		myQuantity.setValue(theValue);
		return this;
	}

	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		clear();

		super.setValueAsQueryToken(theQualifier, theValue);
		if (getMissing() != null) {
			return;
		}

		if (theValue == null) {
			return;
		}
		List<String> parts = ParameterUtil.splitParameterString(theValue, '|', true);

		if (parts.size() > 0 && StringUtils.isNotBlank(parts.get(0))) {
			if (parts.get(0).startsWith("~")) {
				myQuantity.setComparator((QuantityCompararatorEnum) null);
				myApproximate = true;
				myQuantity.setValue(new BigDecimal(parts.get(0).substring(1)));
			} else if (parts.get(0).startsWith("<=")) {
				myQuantity.setComparator(QuantityCompararatorEnum.LESSTHAN_OR_EQUALS);
				myQuantity.setValue(new BigDecimal(parts.get(0).substring(2)));
			} else if (parts.get(0).startsWith("<")) {
				myQuantity.setComparator(QuantityCompararatorEnum.LESSTHAN);
				String valStr = parts.get(0).substring(1);
				myQuantity.setValue(new BigDecimal(valStr));
			} else if (parts.get(0).startsWith(">=")) {
				myQuantity.setComparator(QuantityCompararatorEnum.GREATERTHAN_OR_EQUALS);
				myQuantity.setValue(new BigDecimal(parts.get(0).substring(2)));
			} else if (parts.get(0).startsWith(">")) {
				myQuantity.setComparator(QuantityCompararatorEnum.GREATERTHAN);
				myQuantity.setValue(new BigDecimal(parts.get(0).substring(1)));
			} else {
				myQuantity.setValue(new BigDecimal(parts.get(0)));
			}
		}
		if (parts.size() > 1 && StringUtils.isNotBlank(parts.get(1))) {
			myQuantity.setSystem(parts.get(1));
		}
		if (parts.size() > 2 && StringUtils.isNotBlank(parts.get(2))) {
			myQuantity.setUnits(parts.get(2));
		}

	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("cmp", myQuantity.getComparator().getValueAsString());
		b.append("value", myQuantity.getValue().getValueAsString());
		b.append("system", myQuantity.getSystem().getValueAsString());
		b.append("units", myQuantity.getUnits().getValueAsString());
		if (getMissing() != null) {
			b.append("missing", getMissing());
		}
		return b.toString();
	}

}
