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

import static org.apache.commons.lang3.StringUtils.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.server.Constants;

public class TokenParam extends BaseParam implements IQueryParameterType {

	private String mySystem;
	private boolean myText;
	private String myValue;

	public TokenParam() {
	}

	/**
	 * Constructor which copies the {@link InternalCodingDt#getSystemElement() system} and {@link InternalCodingDt#getCodeElement() code} from a {@link InternalCodingDt} instance and adds it as a parameter
	 * 
	 * @param theCodingDt
	 *            The coding
	 */
	public TokenParam(BaseCodingDt theCodingDt) {
		this(toSystemValue(theCodingDt.getSystemElement()), theCodingDt.getCodeElement().getValue());
	}

	/**
	 * Constructor which copies the {@link BaseIdentifierDt#getSystemElement() system} and {@link BaseIdentifierDt#getValueElement() value} from a {@link BaseIdentifierDt} instance and adds it as a parameter
	 * 
	 * @param theIdentifierDt
	 *            The identifier
	 */
	public TokenParam(BaseIdentifierDt theIdentifierDt) {
		this(toSystemValue(theIdentifierDt.getSystemElement()), theIdentifierDt.getValueElement().getValue());
	}

	public TokenParam(String theSystem, String theValue) {
		setSystem(theSystem);
		setValue(theValue);
	}

	public TokenParam(String theSystem, String theValue, boolean theText) {
		if (theText && isNotBlank(theSystem)) {
			throw new IllegalArgumentException(
					"theSystem can not be non-blank if theText is true (:text searches do not include a system). In other words, set the first parameter to null for a text search");
		}
		setSystem(theSystem);
		setValue(theValue);
		setText(theText);
	}

	@Override
	String doGetQueryParameterQualifier() {
		if (isText()) {
			return Constants.PARAMQUALIFIER_TOKEN_TEXT;
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	String doGetValueAsQueryToken() {
		if (getSystem() != null) {
			return ParameterUtil.escape(StringUtils.defaultString(getSystem())) + '|' + ParameterUtil.escape(getValue());
		} else {
			return ParameterUtil.escape(getValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void doSetValueAsQueryToken(String theQualifier, String theParameter) {
		int barIndex = ParameterUtil.nonEscapedIndexOf(theParameter, '|');
		if (barIndex != -1) {
			setSystem(theParameter.substring(0, barIndex));
			setValue(ParameterUtil.unescape(theParameter.substring(barIndex + 1)));
		} else {
			setValue(ParameterUtil.unescape(theParameter));
		}
	}

	public String getSystem() {
		return mySystem;
	}

	public String getValue() {
		return myValue;
	}

	public InternalCodingDt getValueAsCoding() {
		return new InternalCodingDt(mySystem, myValue);
	}

	public String getValueNotNull() {
		return defaultString(myValue);
	}

	public boolean isEmpty() {
		return StringUtils.isEmpty(myValue);
	}

	public boolean isText() {
		return myText;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}

	public void setText(boolean theText) {
		myText = theText;
	}

	public void setValue(String theValue) {
		myValue = theValue;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("system", defaultString(getSystem()));
		builder.append("value", getValue());
		if (myText) {
			builder.append(":text", myText);
		}
		if (getMissing() != null) {
			builder.append(":missing", getMissing());
		}
		return builder.toString();
	}

	private static String toSystemValue(UriDt theSystem) {
		return theSystem.getValueAsString();
	}

}
