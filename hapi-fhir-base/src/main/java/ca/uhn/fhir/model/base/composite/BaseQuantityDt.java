package ca.uhn.fhir.model.base.composite;

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
import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public abstract class BaseQuantityDt extends BaseIdentifiableElement implements ICompositeDatatype, IQueryParameterType {

	private static final long serialVersionUID = 1L;

	/**
	 * Sets the value(s) for <b>value</b> (Numerical value (with implicit precision))
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public abstract BaseQuantityDt setValue(BigDecimal theValue);

	
	@Override
	public void setValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		getComparatorElement().setValue(null);
		setCode( null);
		setSystem(null);
		setUnits( null);
		setValue( null);

		if (theValue == null) {
			return;
		}
		String[] parts = theValue.split("\\|");
		if (parts.length > 0 && StringUtils.isNotBlank(parts[0])) {
			if (parts[0].startsWith("le")) {
				//TODO: Use of a deprecated method should be resolved.
				getComparatorElement().setValue(ParamPrefixEnum.LESSTHAN_OR_EQUALS.getValue());
				setValue(new BigDecimal(parts[0].substring(2)));
			} else if (parts[0].startsWith("lt")) {
				//TODO: Use of a deprecated method should be resolved.
				getComparatorElement().setValue(ParamPrefixEnum.LESSTHAN.getValue());
				setValue(new BigDecimal(parts[0].substring(1)));
			} else if (parts[0].startsWith("ge")) {
				//TODO: Use of a deprecated method should be resolved.
				getComparatorElement().setValue(ParamPrefixEnum.GREATERTHAN_OR_EQUALS.getValue());
				setValue(new BigDecimal(parts[0].substring(2)));
			} else if (parts[0].startsWith("gt")) {
				//TODO: Use of a deprecated method should be resolved.
				getComparatorElement().setValue(ParamPrefixEnum.GREATERTHAN.getValue());
				setValue(new BigDecimal(parts[0].substring(1)));
			} else {
				setValue(new BigDecimal(parts[0]));
			}
		}
		if (parts.length > 1 && StringUtils.isNotBlank(parts[1])) {
			setSystem(parts[1]);
		}
		if (parts.length > 2 && StringUtils.isNotBlank(parts[2])) {
			setUnits(parts[2]);
		}

	}
	
	/**
	 * Gets the value(s) for <b>comparator</b> (&lt; | &lt;= | &gt;= | &gt; - how to understand the value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than 
     * the stated value due to measurement issues. E.g. if the comparator is \"&lt;\" , then the real value is &lt; stated value
     * </p> 
	 */
	public abstract BoundCodeDt<?> getComparatorElement();

	@Override
	public String getValueAsQueryToken(FhirContext theContext) {
		StringBuilder b= new StringBuilder();
		if (getComparatorElement() != null) {
			b.append(getComparatorElement().getValue());
		}
		if (!getValueElement().isEmpty()) {
			b.append(getValueElement().getValueAsString());
		}
		b.append('|');
		if (!getSystemElement().isEmpty()) {
		b.append(getSystemElement().getValueAsString());
		}
		b.append('|');
		if (!getUnitsElement().isEmpty()) {
		b.append(getUnitsElement().getValueAsString());
		}
		
		return b.toString();
	}
	

	@Override
	public String getQueryParameterQualifier() {
		return null;
	}	
	
	
	
	

 	/**
	 * Sets the value for <b>units</b> (Unit representation)
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public abstract BaseQuantityDt setUnits( String theString);

 
	/**
	 * Gets the value(s) for <b>system</b> (System that defines coded unit form).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public abstract UriDt getSystemElement();

	

 	/**
	 * Sets the value for <b>system</b> (System that defines coded unit form)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public abstract BaseQuantityDt setSystem( String theUri);
 
	/**
	 * Gets the value(s) for <b>code</b> (Coded form of the unit).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public abstract CodeDt getCodeElement();

 	/**
	 * Sets the value for <b>code</b> (Coded form of the unit)
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public abstract BaseQuantityDt setCode( String theCode);
	/**
	 * Gets the value(s) for <b>units</b> (Unit representation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public abstract StringDt getUnitsElement() ;
	/**
	 * Gets the value(s) for <b>value</b> (Numerical value (with implicit precision)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public abstract DecimalDt getValueElement();
	
	/**
	 * <b>Not supported!</b>
	 * 
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link QuantityParam} instead if you
	 * need this functionality
	 */
	@Deprecated
	@Override
	public Boolean getMissing() {
		return null;
	}

	/**
	 * <b>Not supported!</b>
	 * 
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link QuantityParam} instead if you
	 * need this functionality
	 */
	@Deprecated
	@Override
	public IQueryParameterType setMissing(Boolean theMissing) {
		throw new UnsupportedOperationException(Msg.code(1904) + "get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you need this functionality");
	}

	
}
