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
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.TokenParam;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseCodingDt extends BaseIdentifiableElement implements ICompositeDatatype, IQueryParameterType {

	private static final long serialVersionUID = 4425182816398730643L;

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
	 * Gets the value(s) for <b>display</b> (Representation defined by the system).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A representation of the meaning of the code in the system, following the rules of the system.
     * </p> 
	 */
	public abstract StringDt getDisplayElement();

	public abstract BaseCodingDt setDisplay( String theString);

	/*
	todo: handle version
	public abstract StringDt getVersion();

	public abstract BaseCodingDt setVersion ( String theString);
	*/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken(FhirContext theContext) {
		if (getSystemElement().getValueAsString() != null) {
			return ParameterUtil.escape(StringUtils.defaultString(getSystemElement().getValueAsString())) + '|' + ParameterUtil.escape(getCodeElement().getValueAsString());
		} 
		return ParameterUtil.escape(getCodeElement().getValueAsString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theParameter) {
		int barIndex = ParameterUtil.nonEscapedIndexOf(theParameter, '|');
		if (barIndex != -1) {
			setSystem(theParameter.substring(0, barIndex));
			setCode(ParameterUtil.unescape(theParameter.substring(barIndex + 1)));
		} else {
			setCode(ParameterUtil.unescape(theParameter));
		}
	}

	/**
	 * Returns true if <code>this</code> Coding has the same {@link #getCodeElement() Code} and {@link #getSystemElement() system} (as compared by simple equals comparison). Does not compare other
	 * Codes (e.g. getUseElement()) or any extensions.
	 */
	public boolean matchesSystemAndCode(BaseCodingDt theCoding) {
		if (theCoding == null) {
			return false;
		}
		return getCodeElement().equals(theCoding.getCodeElement()) && getSystemElement().equals(theCoding.getSystemElement());
	}

	/**
	 * returns true if <code>this</code> Coding matches a search for the coding specified by <code>theSearchParam</code>, according
	 * to the following:
	 * <ul>
	 *		<li>[parameter]=[namespace]|[code] matches a code/value in the given system namespace</li>
	 *		<li>[parameter]=[code] matches a code/value irrespective of it's system namespace</li>
	 *		<li>[parameter]=|[code] matches a code/value that has no system namespace</li>
	 * </ul>
	 * @param theSearchParam - coding to test <code>this</code> against
	 * @return true if the coding matches, false otherwise
	 */
	public boolean matchesToken(BaseCodingDt theSearchParam) {
		if (theSearchParam.isSystemPresent()) {
			if (theSearchParam.isSystemBlank()) {
				//  [parameter]=|[code] matches a code/value that has no system namespace
				if (isSystemPresent() && !isSystemBlank())
					return false;
			} else {
				//  [parameter]=[namespace]|[code] matches a code/value in the given system namespace
				if (!isSystemPresent())
					return false;
				if (!getSystemElement().equals(theSearchParam.getSystemElement()))
					return false;
			}
		} else {
			//  [parameter]=[code] matches a code/value irrespective of it's system namespace
			// (nothing to do for system for this case)
		}

		return getCodeElement().equals(theSearchParam.getCodeElement());
	}

	private boolean isSystemPresent() {
		return !getSystemElement().isEmpty();
	}

	private boolean isSystemBlank() {
		return isSystemPresent() && getSystemElement().getValueAsString().equals("");
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


	/**
	 * <b>Not supported!</b>
	 * 
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link TokenParam} instead if you
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
	 * @deprecated get/setMissing is not supported in StringDt. Use {@link TokenParam} instead if you
	 * need this functionality
	 */
	@Deprecated
	@Override
	public IQueryParameterType setMissing(Boolean theMissing) {
		throw new UnsupportedOperationException(Msg.code(1903) + "get/setMissing is not supported in StringDt. Use {@link StringParam} instead if you need this functionality");
	}

}
