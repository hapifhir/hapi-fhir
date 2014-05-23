package ca.uhn.fhir.model.primitive;

/*
 * #%L
 * HAPI FHIR Library
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

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

/**
 * Represents the FHIR ID type. This is the actual resource ID, meaning the ID that will be used in RESTful URLs,
 * Resource References, etc. to represent a specific instance of a resource.
 * 
 * <p>
 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any
 * other combination of lowercase letters, numerals, "-" and ".", with a length limit of 36 characters.
 * </p>
 * <p>
 * regex: [a-z0-9\-\.]{1,36}
 * </p>
 */
@DatatypeDef(name = "id")
public class IdDt extends BasePrimitive<String> {

	private String myUnqualifiedId;
	private String myValue;
	private String myUnqualifiedVersionId;
	private String myResourceType;

	/**
	 * Create a new empty ID
	 */
	public IdDt() {
		super();
	}

	/**
	 * Create a new ID, using a BigDecimal input. Uses {@link BigDecimal#toPlainString()} to generate the string
	 * representation.
	 */
	public IdDt(BigDecimal thePid) {
		if (thePid != null) {
			setValue(thePid.toPlainString());
		} else {
			setValue(null);
		}
	}

	/**
	 * Create a new ID using a long
	 */
	public IdDt(long theId) {
		setValue(Long.toString(theId));
	}

	/**
	 * Create a new ID using a string. This String may contain a simple ID (e.g. "1234") or it may contain a complete
	 * URL (http://example.com/fhir/Patient/1234).
	 * 
	 * <p>
	 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or
	 * any other combination of lowercase letters, numerals, "-" and ".", with a length limit of 36 characters.
	 * </p>
	 * <p>
	 * regex: [a-z0-9\-\.]{1,36}
	 * </p>
	 */
	@SimpleSetter
	public IdDt(@SimpleSetter.Parameter(name = "theId") String theValue) {
		setValue(theValue);
	}

	/**
	 * Returns the unqualified portion of this ID as a big decimal, or <code>null</code> if the value is null
	 * 
	 * @throws NumberFormatException
	 *             If the value is not a valid BigDecimal
	 */
	public BigDecimal asBigDecimal() {
		String val = getUnqualifiedId();
		if (isBlank(val)) {
			return null;
		}
		return new BigDecimal(val);
	}

	/**
	 * Returns the unqualified portion of this ID as a {@link Long}, or <code>null</code> if the value is null
	 * 
	 * @throws NumberFormatException
	 *             If the value is not a valid Long
	 */
	public Long asLong() {
		String val = getUnqualifiedId();
		if (isBlank(val)) {
			return null;
		}
		return Long.parseLong(val);
	}

	/**
	 * Returns a reference to <code>this</code> IdDt. It is generally not neccesary to use this method but it is
	 * provided for consistency with the rest of the API.
	 */
	@Override
	public IdDt getId() {
		return this;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public String getUnqualifiedId() {
		return myUnqualifiedId;
	}

	public String getUnqualifiedVersionId() {
		return myUnqualifiedVersionId;
	}

	/**
	 * Returns the value of this ID. Note that this value may be a fully qualified URL, a relative/partial URL, or
	 * a simple ID. Use {@link #getUnqualifiedId()} to get just the ID portion.
	 * 
	 * @see #getUnqualifiedId()
	 */
	@Override
	public String getValue() {
		return myValue;
	}

	@Override
	public String getValueAsString() {
		return myValue;
	}

	/**
	 * Copies the value from the given IdDt to <code>this</code> IdDt. It is generally not neccesary to use this method
	 * but it is provided for consistency with the rest of the API.
	 */
	@Override
	public void setId(IdDt theId) {
		setValue(theId.getValue());
	}

	/**
	 * Set the value
	 * 
	 * <p>
	 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or
	 * any other combination of lowercase letters, numerals, "-" and ".", with a length limit of 36 characters.
	 * </p>
	 * <p>
	 * regex: [a-z0-9\-\.]{1,36}
	 * </p>
	 */
	@Override
	public void setValue(String theValue) throws DataFormatException {
		// TODO: add validation
		myValue = theValue;
		if (StringUtils.isBlank(theValue)) {
			myValue = null;
			myUnqualifiedId = null;
			myUnqualifiedVersionId = null;
			myResourceType = null;
		} else {
			int vidIndex = theValue.indexOf("/_history/");
			int idIndex;
			if (vidIndex != -1) {
				myUnqualifiedVersionId = theValue.substring(vidIndex + "/_history/".length());
				idIndex = theValue.lastIndexOf('/', vidIndex - 1);
				myUnqualifiedId = theValue.substring(idIndex + 1, vidIndex);
			} else {
				idIndex = theValue.lastIndexOf('/');
				myUnqualifiedId = theValue.substring(idIndex + 1);
				myUnqualifiedVersionId = null;
			}
			
			if (idIndex <= 0) {
				myResourceType = null;
			}else {
				int typeIndex = theValue.lastIndexOf('/', idIndex - 1);
				if (typeIndex == -1) {
					myResourceType = theValue.substring(0,idIndex);
				} else {
					myResourceType = theValue.substring(typeIndex+1, idIndex);
				}
			}

		}
	}

	/**
	 * Set the value
	 * 
	 * <p>
	 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or
	 * any other combination of lowercase letters, numerals, "-" and ".", with a length limit of 36 characters.
	 * </p>
	 * <p>
	 * regex: [a-z0-9\-\.]{1,36}
	 * </p>
	 */
	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		setValue(theValue);
	}

	@Override
	public String toString() {
		return myValue;
	}

	/**
	 * Returns <code>true</code> if the unqualified ID is a valid {@link Long} value (in other
	 * words, it consists only of digits)
	 */
	public boolean isValidLong() {
		String id = getUnqualifiedId();
		if (StringUtils.isBlank(id)) {
			return false;
		}
		for (int i = 0; i < id.length(); i++) {
			if (Character.isDigit(id.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

}
