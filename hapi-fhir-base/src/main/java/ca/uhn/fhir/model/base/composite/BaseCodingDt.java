package ca.uhn.fhir.model.base.composite;

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

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.param.ParameterUtil;

public abstract class BaseCodingDt extends BaseIdentifiableElement implements ICompositeDatatype, IQueryParameterType {

	/**
	 * Gets the value(s) for <b>code</b> (Symbol in syntax defined by the system). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
	 * </p>
	 */
	public abstract CodeDt getCodeElement();

	@Override
	public String getQueryParameterQualifier() {
		return null;
	}

	/**
	 * Gets the value(s) for <b>system</b> (Identity of the terminology system). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> The identification of the code system that defines the meaning of the symbol in the code.
	 * </p>
	 */
	public abstract UriDt getSystemElement();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken() {
		if (getSystemElement().getValueAsString() != null) {
			return ParameterUtil.escape(StringUtils.defaultString(getSystemElement().getValueAsString())) + '|' + ParameterUtil.escape(getCodeElement().getValueAsString());
		} else {
			return ParameterUtil.escape(getCodeElement().getValueAsString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(String theQualifier, String theParameter) {
		int barIndex = ParameterUtil.nonEscapedIndexOf(theParameter, '|');
		if (barIndex != -1) {
			setSystem(theParameter.substring(0, barIndex));
			setCode(ParameterUtil.unescape(theParameter.substring(barIndex + 1)));
		} else {
			setCode(ParameterUtil.unescape(theParameter));
		}
	}

	/**
	 * Returns true if <code>this</code> Coding has the same {@link ca.uhn.fhir.model.dstu.composite.InternalCodingDt#getCode() Code} and {@link ca.uhn.fhir.model.dstu.composite.InternalCodingDt#getSystem() system} (as compared by simple equals comparison). Does not compare other
	 * Codes (e.g. {@link ca.uhn.fhir.model.dstu.composite.InternalCodingDt#getUse() use}) or any extensions.
	 */
	public boolean matchesSystemAndCode(BaseCodingDt theCoding) {
		if (theCoding == null) {
			return false;
		}
		return getCodeElement().equals(theCoding.getCodeElement()) && getSystemElement().equals(theCoding.getSystemElement());
	}

	/**
	 * Sets the value for <b>code</b> (Symbol in syntax defined by the system)
	 *
	 * <p>
	 * <b>Definition:</b> A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
	 * </p>
	 */
	public abstract BaseCodingDt setCode(String theCode);

	/**
	 * Sets the value for <b>system</b> (Identity of the terminology system)
	 *
	 * <p>
	 * <b>Definition:</b> The identification of the code system that defines the meaning of the symbol in the code.
	 * </p>
	 */
	public abstract BaseCodingDt setSystem(String theUri);

}
