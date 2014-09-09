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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.server.Constants;

public class TokenParam extends BaseParam implements IQueryParameterType {

	private String mySystem;
	private boolean myText;
	private String myValue;

	public TokenParam() {
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

	/**
	 * Constructor which copies the {@link CodingDt#getSystem() system} and {@link CodingDt#getCode() code} from a {@link CodingDt} instance and adds it as a parameter
	 * 
	 * @param theCodingDt
	 *            The coding
	 */
	public TokenParam(CodingDt theCodingDt) {
		this(toSystemValue(theCodingDt.getSystem()), theCodingDt.getCode().getValue());
	}

	/**
	 * Constructor which copies the {@link IdentifierDt#getSystem() system} and {@link IdentifierDt#getValue() value} from a {@link IdentifierDt} instance and adds it as a parameter
	 * 
	 * @param theCodingDt
	 *            The coding
	 */
	public TokenParam(IdentifierDt theIdentifierDt) {
		this(toSystemValue(theIdentifierDt.getSystem()), theIdentifierDt.getValue().getValue());
	}

	private static String toSystemValue(UriDt theSystem) {
		return theSystem.getValueAsString();
	}

	@Override
	public String getQueryParameterQualifier() {
		if (getMissing() != null) {
			return super.getQueryParameterQualifier();
		} else if (isText()) {
			return Constants.PARAMQUALIFIER_TOKEN_TEXT;
		} else {
			return null;
		}
	}

	public String getSystem() {
		return mySystem;
	}

	public String getValue() {
		return myValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken() {
		if (getMissing() != null) {
			return super.getValueAsQueryToken();
		} else if (getSystem() != null) {
			return ParameterUtil.escape(StringUtils.defaultString(getSystem())) + '|' + ParameterUtil.escape(getValue());
		} else {
			return ParameterUtil.escape(getValue());
		}
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(String theQualifier, String theParameter) {
		super.setValueAsQueryToken(theQualifier, theParameter);
		if (getMissing() != null) {
			return;
		}
		
		int barIndex = ParameterUtil.nonEscapedIndexOf(theParameter, '|');
		if (barIndex != -1) {
			setSystem(theParameter.substring(0, barIndex));
			setValue(ParameterUtil.unescape(theParameter.substring(barIndex + 1)));
		} else {
			setValue(ParameterUtil.unescape(theParameter));
		}
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("system", defaultString(getValue()));
		builder.append("value", getValue());
		if (myText) {
			builder.append("text", myText);
		}
		if (getMissing() != null) {
			builder.append("missing", getMissing());
		}
		return builder.toString();
	}

	public CodingDt getValueAsCoding() {
		return new CodingDt(mySystem, myValue);
	}

}
