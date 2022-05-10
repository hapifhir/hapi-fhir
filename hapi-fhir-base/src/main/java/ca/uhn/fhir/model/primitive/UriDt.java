package ca.uhn.fhir.model.primitive;

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

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;

@DatatypeDef(name = "uri")
public class UriDt extends BasePrimitive<String> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UriDt.class);

	/**
	 * Create a new String
	 */
	public UriDt() {
		// nothing
	}

	/**
	 * Create a new String
	 */
	@SimpleSetter
	public UriDt(@SimpleSetter.Parameter(name = "theUri") String theValue) {
		setValueAsString(theValue);
	}

	@Override
	protected String encode(String theValue) {
		return theValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		UriDt other = (UriDt) obj;
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

	/**
	 * Compares the given string to the string representation of this URI. In many cases it is preferable to use this
	 * instead of the standard {@link #equals(Object)} method, since that method returns <code>false</code> unless it is
	 * passed an instance of {@link UriDt}
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
		URI retVal;
		try {
			retVal = new URI(theValue).normalize();
			String urlString = retVal.toString();
			if (urlString.endsWith("/") && urlString.length() > 1) {
				retVal = new URI(urlString.substring(0, urlString.length() - 1));
			}
		} catch (URISyntaxException e) {
			ourLog.debug("Failed to normalize URL '{}', message was: {}", theValue, e.toString());
			return theValue;
		}

		return retVal.toASCIIString();
	}

	@Override
	protected String parse(String theValue) {
		return theValue;
	}

	/**
	 * Creates a new UriDt instance which uses the given OID as the content (and prepends "urn:oid:" to the OID string
	 * in the value of the newly created UriDt, per the FHIR specification).
	 * 
	 * @param theOid
	 *           The OID to use (<code>null</code> is acceptable and will result in a UriDt instance with a
	 *           <code>null</code> value)
	 * @return A new UriDt instance
	 */
	public static UriDt fromOid(String theOid) {
		if (theOid == null) {
			return new UriDt();
		}
		return new UriDt("urn:oid:" + theOid);
	}

}
