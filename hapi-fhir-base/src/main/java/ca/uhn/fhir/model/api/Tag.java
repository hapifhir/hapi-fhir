package ca.uhn.fhir.model.api;

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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IBaseCoding;

/**
 * A single tag
 * <p>
 * Note on equality- When computing hashCode or equals values for this class, only the 
 * {@link #getScheme() scheme} and 
 * </p>
 */
public class Tag extends BaseElement implements IElement, IBaseCoding {
	
	private static final long serialVersionUID = 1L;
	
	public static final String ATTR_LABEL = "label";
	public static final String ATTR_SCHEME = "scheme";
	public static final String ATTR_TERM = "term";

	/**
	 * Convenience constant containing the "http://hl7.org/fhir/tag" scheme value
	 */
	public static final String HL7_ORG_FHIR_TAG = "http://hl7.org/fhir/tag";
	/**
	 * Convenience constant containing the "http://hl7.org/fhir/tag/profile" scheme value
	 */
	public static final String HL7_ORG_PROFILE_TAG = "http://hl7.org/fhir/tag/profile";
	/**
	 * Convenience constant containing the "http://hl7.org/fhir/tag/security" scheme value
	 */
	public static final String HL7_ORG_SECURITY_TAG = "http://hl7.org/fhir/tag/security";

	private String myLabel;
	private String myScheme;
	private String myTerm;

	public Tag() {
	}

	/**
	 * @deprecated There is no reason to create a tag with a term and not a scheme, so this constructor will be removed
	 */
	@Deprecated
	public Tag(String theTerm) {
		this((String) null, theTerm, null);
	}

	public Tag(String theScheme, String theTerm) {
		myScheme = theScheme;
		myTerm = theTerm;
	}

	public Tag(String theScheme, String theTerm, String theLabel) {
		myTerm = theTerm;
		myLabel = theLabel;
		myScheme = theScheme;
	}

	public Tag(URI theScheme, URI theTerm, String theLabel) {
		if (theScheme != null) {
			myScheme = theScheme.toASCIIString();
		}
		if (theTerm != null) {
			myTerm = theTerm.toASCIIString();
		}
		myLabel = theLabel;
	}


	public String getLabel() {
		return myLabel;
	}

	public String getScheme() {
		return myScheme;
	}

	public String getTerm() {
		return myTerm;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (myScheme == null) {
			if (other.myScheme != null)
				return false;
		} else if (!myScheme.equals(other.myScheme))
			return false;
		if (myTerm == null) {
			if (other.myTerm != null)
				return false;
		} else if (!myTerm.equals(other.myTerm))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myScheme == null) ? 0 : myScheme.hashCode());
		result = prime * result + ((myTerm == null) ? 0 : myTerm.hashCode());
		return result;
	}

	/**
	 * Returns <code>true</code> if either scheme or term is populated.
	 */
	@Override
	public boolean isEmpty() {
		return StringUtils.isBlank(myScheme) && StringUtils.isBlank(myTerm);
	}

	/**
	 * Sets the label and returns a reference to this tag
	 */
	public Tag setLabel(String theLabel) {
		myLabel = theLabel;
		return this;
	}

	/**
	 * Sets the scheme and returns a reference to this tag
	 */
	public Tag setScheme(String theScheme) {
		myScheme = theScheme;
		return this;
	}

	/**
	 * Sets the term and returns a reference to this tag
	 */
	public Tag setTerm(String theTerm) {
		myTerm = theTerm;
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("Scheme", myScheme);
		b.append("Term", myTerm);
		b.append("Label", myLabel);
		return b.toString();
	}

	@Override
	public String getCode() {
		return getTerm();
	}

	@Override
	public String getDisplay() {
		return getLabel();
	}

	@Override
	public String getSystem() {
		return getScheme();
	}

	@Override
	public IBaseCoding setCode(String theTerm) {
		setTerm(theTerm);
		return this;
	}

	@Override
	public IBaseCoding setDisplay(String theLabel) {
		setLabel(theLabel);
		return this;
	}

	@Override
	public IBaseCoding setSystem(String theScheme) {
		setScheme(theScheme);
		return this;
	}

}
