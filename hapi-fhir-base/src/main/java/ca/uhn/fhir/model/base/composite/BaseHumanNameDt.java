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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.DatatypeUtil;

public abstract class BaseHumanNameDt extends BaseIdentifiableElement {

	private static final long serialVersionUID = 2765500013165698259L;

	/**
	 * Gets the value(s) for <b>family</b> (Family name (often called 'Surname')). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
	 * </p>
	 */
	public abstract java.util.List<StringDt> getFamily();

	/**
	 * Returns all repetitions of {@link #getFamily() family name} as a space separated string
	 * 
	 * @see DatatypeUtil#joinStringsSpaceSeparated(List)
	 */
	public String getFamilyAsSingleString() {
		return ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated(getFamily());
	}

	/**
	 * Gets the value(s) for <b>given</b> (Given names (not always 'first'). Includes middle names). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> Given name
	 * </p>
	 */
	public abstract java.util.List<StringDt> getGiven();

	/**
	 * Returns all repetitions of {@link #getGiven() given name} as a space separated string
	 * 
	 * @see DatatypeUtil#joinStringsSpaceSeparated(List)
	 */
	public String getGivenAsSingleString() {
		return ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated(getGiven());
	}

	/**
	 * Gets the value(s) for <b>prefix</b> (Parts that come before the name). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
	 * </p>
	 */
	public abstract java.util.List<StringDt> getPrefix();

	/**
	 * Returns all repetitions of {@link #getPrefix() prefix name} as a space separated string
	 * 
	 * @see DatatypeUtil#joinStringsSpaceSeparated(List)
	 */
	public String getPrefixAsSingleString() {
		return ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated(getPrefix());
	}

	/**
	 * Gets the value(s) for <b>suffix</b> (Parts that come after the name). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
	 * </p>
	 */
	public abstract java.util.List<StringDt> getSuffix();

	/**
	 * Returns all repetitions of {@link #getSuffix() suffix} as a space separated string
	 * 
	 * @see DatatypeUtil#joinStringsSpaceSeparated(List)
	 */
	public String getSuffixAsSingleString() {
		return ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated(getSuffix());
	}

	/**
	 * Gets the value(s) for <b>text</b> (Text representation of the full name). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> A full text representation of the name
	 * </p>
	 */
	public abstract StringDt getTextElement();

	/**
	 * Sets the value(s) for <b>text</b> (Text representation of the full name)
	 *
	 * <p>
	 * <b>Definition:</b> A full text representation of the name
	 * </p>
	 */
	public abstract BaseHumanNameDt setText(StringDt theValue);

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("family", getFamilyAsSingleString());
		b.append("given", getGivenAsSingleString());
		return b.toString();
	}

	/**
	 * Returns all of the components of the name (prefix, given, family, suffix) as a 
	 * single string with a single spaced string separating each part. 
	 * <p>
	 * If none of the parts are populated, returns the {@link #getTextElement() text}
	 * element value instead.
	 * </p>
	 */
	public String getNameAsSingleString() {
		List<StringDt> nameParts = new ArrayList<StringDt>();
		nameParts.addAll(getPrefix());
		nameParts.addAll(getGiven());
		nameParts.addAll(getFamily());
		nameParts.addAll(getSuffix());
		if (nameParts.size() > 0) {
			return ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated(nameParts);
		}
		return getTextElement().getValue();
	}

}
