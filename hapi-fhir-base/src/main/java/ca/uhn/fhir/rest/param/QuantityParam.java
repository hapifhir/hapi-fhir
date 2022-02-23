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
 * http://www.apache.org/licenses/LICENSE-2.0
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
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.util.CoverageIgnore;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.math.BigDecimal;
import java.util.List;

public class QuantityParam extends BaseParamWithPrefix<QuantityParam> implements IQueryParameterType {

	private static final long serialVersionUID = 1L;
	private BigDecimal myValue;
	private String mySystem;
	private String myUnits;

	/**
	 * Constructor
	 */
	public QuantityParam() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param thePrefix
	 *           The comparator, or <code>null</code> for an equals comparator
	 * @param theValue
	 *           A quantity value
	 * @param theSystem
	 *           The unit system
	 * @param theUnits
	 *           The unit code
	 */
	public QuantityParam(ParamPrefixEnum thePrefix, BigDecimal theValue, String theSystem, String theUnits) {
		setPrefix(thePrefix);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param thePrefix
	 *           The comparator, or <code>null</code> for an equals comparator
	 * @param theValue
	 *           A quantity value
	 * @param theSystem
	 *           The unit system
	 * @param theUnits
	 *           The unit code
	 */
	public QuantityParam(ParamPrefixEnum thePrefix, double theValue, String theSystem, String theUnits) {
		setPrefix(thePrefix);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param thePrefix
	 *           The comparator, or <code>null</code> for an equals comparator
	 * @param theValue
	 *           A quantity value
	 * @param theSystem
	 *           The unit system
	 * @param theUnits
	 *           The unit code
	 */
	public QuantityParam(ParamPrefixEnum thePrefix, long theValue, String theSystem, String theUnits) {
		setPrefix(thePrefix);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param theQuantity
	 *           A quantity value (with no system or units), such as "100.0" or "gt4"
	 */
	public QuantityParam(String theQuantity) {
		setValueAsQueryToken(null, null, null, theQuantity);
	}

	/**
	 * Constructor
	 * 
	 * @param theQuantity
	 *           A quantity value (with no system or units), such as <code>100</code>
	 */
	public QuantityParam(long theQuantity) {
		setValueAsQueryToken(null, null, null, Long.toString(theQuantity));
	}

	/**
	 * Constructor
	 * 
	 * @param theQuantity
	 *           A quantity value (with no system or units), such as "100.0" or "&lt;=4"
	 * @param theSystem
	 *           The unit system
	 * @param theUnits
	 *           The unit code
	 */
	public QuantityParam(String theQuantity, String theSystem, String theUnits) {
		setValueAsQueryToken(null, null, null, theQuantity);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	private void clear() {
		setPrefix(null);
		setSystem((String) null);
		setUnits(null);
		setValue((BigDecimal) null);
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

		b.append(ParameterUtil.escapeWithDefault(getValueAsString()));
		b.append('|');
		b.append(ParameterUtil.escapeWithDefault(mySystem));
		b.append('|');
		b.append(ParameterUtil.escapeWithDefault(myUnits));

		return b.toString();
	}

	public String getValueAsString() {
		if (myValue != null) {
			return myValue.toPlainString();
		}
		return null;
	}

	@Override
	void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		clear();

		if (theValue == null) {
			return;
		}
		List<String> parts = ParameterUtil.splitParameterString(theValue, '|', true);

		if (parts.size() > 0 && StringUtils.isNotBlank(parts.get(0))) {
			String value = super.extractPrefixAndReturnRest(parts.get(0));
			setValue(value);
		}
		if (parts.size() > 1 && StringUtils.isNotBlank(parts.get(1))) {
			setSystem(parts.get(1));
		}
		if (parts.size() > 2 && StringUtils.isNotBlank(parts.get(2))) {
			setUnits(parts.get(2));
		}

	}

	/**
	 * Returns the system, or null if none was provided
	 * <p>
	 * Note that prior to HAPI FHIR 1.5, this method returned a {@link UriDt}
	 * </p>
	 * 
	 * @since 1.5
	 */
	public String getSystem() {
		return mySystem;
	}

	/**
	 * @deprecated Use {{@link #getSystem()}} instead
	 */
	@Deprecated
	@CoverageIgnore
	public UriDt getSystemAsUriDt() {
		return new UriDt(mySystem);
	}

	public String getUnits() {
		return myUnits;
	}

	/**
	 * Returns the quantity/value, or null if none was provided
	 * <p>
	 * Note that prior to HAPI FHIR 1.5, this method returned a {@link DecimalDt}
	 * </p>
	 * 
	 * @since 1.5
	 */
	public BigDecimal getValue() {
		return myValue;
	}

	public QuantityParam setSystem(String theSystem) {
		mySystem = theSystem;
		return this;
	}

	public QuantityParam setSystem(IPrimitiveType<String> theSystem) {
		mySystem = null;
		if (theSystem != null) {
			mySystem = theSystem.getValue();
		}
		return this;
	}

	public QuantityParam setUnits(String theUnits) {
		myUnits = theUnits;
		return this;
	}

	public QuantityParam setValue(BigDecimal theValue) {
		myValue = theValue;
		return this;
	}

	public QuantityParam setValue(IPrimitiveType<BigDecimal> theValue) {
		myValue = null;
		if (theValue != null) {
			myValue = theValue.getValue();
		}
		return this;
	}

	public QuantityParam setValue(String theValue) {
		myValue = null;
		if (theValue != null) {
			myValue = new BigDecimal(theValue);
		}
		return this;
	}

	public QuantityParam setValue(double theValue) {
		// Use the valueOf here because the constructor gives crazy precision
		// changes due to the floating point conversion
		myValue = BigDecimal.valueOf(theValue);
		return this;
	}

	public QuantityParam setValue(long theValue) {
		// Use the valueOf here because the constructor gives crazy precision
		// changes due to the floating point conversion
		myValue = BigDecimal.valueOf(theValue);
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("prefix", getPrefix());
		b.append("value", myValue);
		b.append("system", mySystem);
		b.append("units", myUnits);
		if (getMissing() != null) {
			b.append("missing", getMissing());
		}
		return b.toString();
	}

    public static QuantityParam toQuantityParam(IQueryParameterType theParam) {
        if (theParam instanceof BaseQuantityDt) {
            BaseQuantityDt param = (BaseQuantityDt) theParam;
            String systemValue = param.getSystemElement().getValueAsString();
            String unitsValue = param.getUnitsElement().getValueAsString();
            ParamPrefixEnum cmpValue = ParamPrefixEnum.forValue(param.getComparatorElement().getValueAsString());
            BigDecimal valueValue = param.getValueElement().getValue();
            return new QuantityParam()
                .setSystem(systemValue)
                .setUnits(unitsValue)
                .setPrefix(cmpValue)
                .setValue(valueValue);
        } else if (theParam instanceof QuantityParam) {
            return (QuantityParam) theParam;
        } else {
            throw new IllegalArgumentException(Msg.code(1948) + "Invalid quantity type: " + theParam.getClass());
        }
    }
}
