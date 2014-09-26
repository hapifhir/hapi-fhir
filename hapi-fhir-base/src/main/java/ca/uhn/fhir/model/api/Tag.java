package ca.uhn.fhir.model.api;

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

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Tag extends BaseElement implements IElement {
	
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

	private transient Integer myHashCode;
	private String myLabel;
	private String myScheme;
	private String myTerm;

	public Tag() {
	}

	public Tag(String theTerm) {
		this((String) null, theTerm, null);
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (myLabel == null) {
			if (other.myLabel != null)
				return false;
		} else if (!myLabel.equals(other.myLabel))
			return false;
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
	public int hashCode() {
		if (myHashCode != null) {
			System.out.println("Tag alread has hashcode " + myHashCode + " - " + myScheme + " - " + myTerm + " - " + myLabel);
			return myHashCode;
		}
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myLabel == null) ? 0 : myLabel.hashCode());
		result = prime * result + ((myScheme == null) ? 0 : myScheme.hashCode());
		result = prime * result + ((myTerm == null) ? 0 : myTerm.hashCode());
		myHashCode = result;
		System.out.println("Tag has hashcode " + myHashCode + " - " + myScheme + " - " + myTerm + " - " + myLabel);
		return result;
	}

	@Override
	public boolean isEmpty() {
		return StringUtils.isBlank(myLabel) && StringUtils.isBlank(myScheme) && StringUtils.isBlank(myTerm);
	}

	public Tag setLabel(String theLabel) {
		myLabel = theLabel;
		myHashCode = null;
		return this;
	}

	public Tag setScheme(String theScheme) {
		myScheme = theScheme;
		myHashCode = null;
		return this;
	}

	public Tag setTerm(String theTerm) {
		myTerm = theTerm;
		myHashCode = null;
		return this;
	}

	public String toHeaderValue() {
		StringBuilder b = new StringBuilder();
		b.append(this.getTerm());
		if (isNotBlank(this.getLabel())) {
			b.append("; label=\"").append(this.getLabel()).append('"');
		}
		if (isNotBlank(this.getScheme())) {
			b.append("; scheme=\"").append(this.getScheme()).append('"');
		}
		return b.toString();
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("Scheme", myScheme);
		b.append("Term", myTerm);
		b.append("Label", myLabel);
		return b.toString();
	}

}
