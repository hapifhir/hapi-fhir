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
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.primitive.UriDt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseCoding;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TokenParam extends BaseParam /*implements IQueryParameterType*/ {

	private TokenParamModifier myModifier;
	private String mySystem;
	private String myValue;

	private Boolean myMdmExpand;

	/**
	 * Constructor
	 */
	public TokenParam() {
		super();
	}

	/**
	 * Constructor which copies the {@link InternalCodingDt#getSystemElement() system} and
	 * {@link InternalCodingDt#getCodeElement() code} from a {@link InternalCodingDt} instance and adds it as a parameter
	 *
	 * @param theCodingDt The coding
	 */
	public TokenParam(BaseCodingDt theCodingDt) {
		this(toSystemValue(theCodingDt.getSystemElement()), theCodingDt.getCodeElement().getValue());
	}

	/**
	 * Constructor which copies the {@link BaseIdentifierDt#getSystemElement() system} and
	 * {@link BaseIdentifierDt#getValueElement() value} from a {@link BaseIdentifierDt} instance and adds it as a
	 * parameter
	 *
	 * @param theIdentifierDt The identifier
	 */
	public TokenParam(BaseIdentifierDt theIdentifierDt) {
		this(toSystemValue(theIdentifierDt.getSystemElement()), theIdentifierDt.getValueElement().getValue());
	}

	/**
	 * Construct a {@link TokenParam} from the {@link IBaseCoding#getSystem()} () system} and
	 * {@link IBaseCoding#getCode()} () code} of a {@link IBaseCoding} instance.
	 *
	 * @param theCoding The coding
	 */
	public TokenParam(IBaseCoding theCoding) {
		this(theCoding.getSystem(), theCoding.getCode());
	}

	public TokenParam(String theSystem, String theValue) {
		setSystem(theSystem);
		setValue(theValue);
	}

	public TokenParam(String theSystem, String theValue, boolean theText) {
		if (theText && isNotBlank(theSystem)) {
			throw new IllegalArgumentException(Msg.code(1938) + "theSystem can not be non-blank if theText is true (:text searches do not include a system). In other words, set the first parameter to null for a text search");
		}
		setSystem(theSystem);
		setValue(theValue);
		setText(theText);
	}

	/**
	 * Constructor that takes a code but no system
	 */
	public TokenParam(String theCode) {
		this(null, theCode);
	}

	public boolean isMdmExpand() {
		return myMdmExpand != null && myMdmExpand;
	}

	public TokenParam setMdmExpand(boolean theMdmExpand) {
		myMdmExpand = theMdmExpand;
		return this;
	}

	@Override
	String doGetQueryParameterQualifier() {
		if (getModifier() != null) {
			return getModifier().getValue();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		if (getSystem() != null) {
			if (getValue() != null) {
				return ParameterUtil.escape(StringUtils.defaultString(getSystem())) + '|' + ParameterUtil.escape(getValue());
			} else {
				return ParameterUtil.escape(StringUtils.defaultString(getSystem())) + '|';
			}
		}
		return ParameterUtil.escape(getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theParameter) {
		setModifier(null);
		if (theQualifier != null) {
			TokenParamModifier modifier = TokenParamModifier.forValue(theQualifier);
			setModifier(modifier);

			if (modifier == TokenParamModifier.TEXT) {
				setSystem(null);
				setValue(ParameterUtil.unescape(theParameter));
				return;
			}
		}

		setSystem(null);
		if (theParameter == null) {
			setValue(null);
		} else {
			int barIndex = ParameterUtil.nonEscapedIndexOf(theParameter, '|');
			if (barIndex != -1) {
				setSystem(theParameter.substring(0, barIndex));
				setValue(ParameterUtil.unescape(theParameter.substring(barIndex + 1)));
			} else {
				setValue(ParameterUtil.unescape(theParameter));
			}
		}
	}

	/**
	 * Returns the modifier for this token
	 */
	public TokenParamModifier getModifier() {
		return myModifier;
	}

	public TokenParam setModifier(TokenParamModifier theModifier) {
		myModifier = theModifier;
		return this;
	}

	/**
	 * Returns the system for this token. Note that if a {@link #getModifier()} is being used, the entire value of the
	 * parameter will be placed in {@link #getValue() value} and this method will return <code>null</code>.
	 * <p
	 * Also note that this value may be <code>null</code> or <code>""</code> (empty string) and that
	 * each of these have a different meaning. When a token is passed on a URL and it has no
	 * vertical bar (often meaning "return values that match the given code in any codesystem")
	 * this method will return <code>null</code>. When a token is passed on a URL and it has
	 * a vetical bar but nothing before the bar (often meaning "return values that match the
	 * given code but that have no codesystem) this method will return <code>""</code>
	 * </p>
	 */
	public String getSystem() {
		return mySystem;
	}

	public TokenParam setSystem(String theSystem) {
		mySystem = theSystem;
		return this;
	}

	/**
	 * Returns the value for the token (generally the value to the right of the
	 * vertical bar on the URL)
	 */
	public String getValue() {
		return myValue;
	}

	public TokenParam setValue(String theValue) {
		myValue = theValue;
		return this;
	}

	public InternalCodingDt getValueAsCoding() {
		return new InternalCodingDt(mySystem, myValue);
	}

	public String getValueNotNull() {
		return defaultString(myValue);
	}

	public boolean isEmpty() {
		return StringUtils.isBlank(mySystem) && StringUtils.isBlank(myValue) && getMissing() == null;
	}

	/**
	 * Returns true if {@link #getModifier()} returns {@link TokenParamModifier#TEXT}
	 */
	public boolean isText() {
		return myModifier == TokenParamModifier.TEXT;
	}

	/**
	 * @deprecated Use {@link #setModifier(TokenParamModifier)} instead
	 */
	@Deprecated
	public TokenParam setText(boolean theText) {
		if (theText) {
			myModifier = TokenParamModifier.TEXT;
		} else {
			myModifier = null;
		}
		return this;
	}


	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("system", defaultString(getSystem()));
		if (myModifier != null) {
			builder.append(":" + myModifier.getValue());
		}
		builder.append("value", getValue());
		if (getMissing() != null) {
			builder.append(":missing", getMissing());
		}
		return builder.toString();
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		TokenParam that = (TokenParam) theO;

		EqualsBuilder b = new EqualsBuilder();
		b.append(myModifier, that.myModifier);
		b.append(mySystem, that.mySystem);
		b.append(myValue, that.myValue);
		return b.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder(17, 37);
		b.append(myModifier);
		b.append(mySystem);
		b.append(myValue);
		return b.toHashCode();
	}

	private static String toSystemValue(UriDt theSystem) {
		return theSystem.getValueAsString();
	}

}
