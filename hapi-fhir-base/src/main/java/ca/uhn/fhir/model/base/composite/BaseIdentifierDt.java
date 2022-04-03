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
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.StringParam;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseIdentifierDt extends BaseIdentifiableElement implements ICompositeDatatype, IQueryParameterType {

	private static final long serialVersionUID = 4400972469749953077L;

	@Override
	public String getQueryParameterQualifier() {
		return null;
	}

	/**
	 * Gets the value(s) for <b>system</b> (The namespace for the identifier). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> Establishes the namespace in which set of possible id values is unique.
	 * </p>
	 */
	public abstract UriDt getSystemElement();

	/**
	 * Gets the value(s) for <b>value</b> (The value that is unique). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> The portion of the identifier typically displayed to the user and which is unique within the context of the system.
	 * </p>
	 */
	public abstract StringDt getValueElement();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken(FhirContext theContext) {
		UriDt system = getSystemElement();
		StringDt value = getValueElement();
		if (system.getValueAsString() != null) {
			return ParameterUtil.escape(StringUtils.defaultString(system.getValueAsString())) + '|' + ParameterUtil.escape(value.getValueAsString());
		}
		return ParameterUtil.escape(value.getValueAsString());
	}

	/**
	 * Returns true if <code>this</code> identifier has the same {@link #getValueElement() value} and
	 * {@link #getSystemElement() system} (as compared by simple equals comparison). Does not compare other values (e.g.
	 * getUse()) or any extensions.
	 */
	public boolean matchesSystemAndValue(BaseIdentifierDt theIdentifier) {
		if (theIdentifier == null) {
			return false;
		}
		return getValueElement().equals(theIdentifier.getValueElement()) && getSystemElement().equals(theIdentifier.getSystemElement());
	}

	/**
	 * Sets the value for <b>system</b> (The namespace for the identifier)
	 *
	 * <p>
	 * <b>Definition:</b> Establishes the namespace in which set of possible id values is unique.
	 * </p>
	 */
	public abstract BaseIdentifierDt setSystem(String theUri);

	/**
	 * Sets the value for <b>value</b> (The value that is unique)
	 *
	 * <p>
	 * <b>Definition:</b> The portion of the identifier typically displayed to the user and which is unique within the context of the system.
	 * </p>
	 */
	public abstract BaseIdentifierDt setValue(String theString);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theParameter) {
		int barIndex = ParameterUtil.nonEscapedIndexOf(theParameter, '|');
		if (barIndex != -1) {
			setSystem(theParameter.substring(0, barIndex));
			setValue(ParameterUtil.unescape(theParameter.substring(barIndex + 1)));
		} else {
			setValue(ParameterUtil.unescape(theParameter));
		}
	}

	
	/**
	 * <b>Not supported!</b>
	 * 
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you
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
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you
	 * need this functionality
	 */
	@Deprecated
	@Override
	public IQueryParameterType setMissing(Boolean theMissing) {
		throw new UnsupportedOperationException(Msg.code(1907) + "get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you need this functionality");
	}

}
