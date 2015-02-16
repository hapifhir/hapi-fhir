/*
Copyright (c) 2011+, HL7, Inc
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.
 * Neither the name of HL7 nor the names of its contributors may be used to 
   endorse or promote products derived from this software without specific 
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.

*/
package org.hl7.fhir.instance.model;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;

/**
 * Primitive type "uri" in FHIR: any valid URI. Sometimes constrained to be only an absolute URI, and sometimes constrained to be a literal reference
 */
@DatatypeDef(name = "uri")
public class UriType extends PrimitiveType<String> {

	private static final long serialVersionUID = 3L;

	/**
	 * Constructor
	 */
	public UriType() {
		// nothing
	}

	/**
	 * Constructor
	 */
	public UriType(String theValue) {
		setValueAsString(theValue);
	}

	/**
	 * Constructor
	 */
	public UriType(URI theValue) {
		setValue(theValue.toString());
	}

	@Override
	public UriType copy() {
		return new UriType(getValue());
	}

	@Override
	protected String encode(String theValue) {
		return theValue;
	}

	/**
	 * Compares the given string to the string representation of this URI. In many cases it is preferable to use this
	 * instead of the standard {@link #equals(Object)} method, since that method returns <code>false</code> unless it is
	 * passed an instance of {@link UriType}
	 */
	public boolean equals(String theString) {
		return StringUtils.equals(getValueAsString(), theString);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		String normalize = normalize(getValue());
		result = prime * result + ((normalize == null) ? 0 : normalize.hashCode());

		return result;
	}

	private String normalize(String theValue) {
		if (theValue == null) {
			return null;
		}
		try {
			URI retVal = new URI(getValue()).normalize();
			String urlString = retVal.toString();
			if (urlString.endsWith("/") && urlString.length() > 1) {
				retVal = new URI(urlString.substring(0, urlString.length() - 1));
			}
			return retVal.toASCIIString();
		} catch (URISyntaxException e) {
			// ourLog.debug("Failed to normalize URL '{}', message was: {}", urlString, e.toString());
			return theValue;
		}
	}

	@Override
	protected String parse(String theValue) {
		return theValue;
	}

	/**
	 * Creates a new OidType instance which uses the given OID as the content (and prepends "urn:oid:" to the OID string
	 * in the value of the newly created OidType, per the FHIR specification).
	 * 
	 * @param theOid
	 *            The OID to use (<code>null</code> is acceptable and will result in a UriDt instance with a
	 *            <code>null</code> value)
	 * @return A new UriDt instance
	 */
	public static OidType fromOid(String theOid) {
		if (theOid == null) {
			return new OidType();
		}
		return new OidType("urn:oid:" + theOid);
	}

	 @Override
   public boolean equalsDeep(Base obj) {
     if (!super.equalsDeep(obj))
       return false;
 		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		UriType other = (UriType) obj;
		if (getValue() == null && other.getValue() == null) {
			return true;
		}
		if (getValue() == null || other.getValue() == null) {
			return false;
		}

		String normalize = normalize(getValue());
		String normalize2 = normalize(other.getValue());
		return normalize.equals(normalize2);
   }

}
