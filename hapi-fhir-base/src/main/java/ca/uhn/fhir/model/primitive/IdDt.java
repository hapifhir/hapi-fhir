package ca.uhn.fhir.model.primitive;

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

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.Constants;

/**
 * Represents the FHIR ID type. This is the actual resource ID, meaning the ID that will be used in RESTful URLs, Resource References, etc. to represent a specific instance of a resource.
 * 
 * <p>
 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length
 * limit of 36 characters.
 * </p>
 * <p>
 * regex: [a-z0-9\-\.]{1,36}
 * </p>
 */
@DatatypeDef(name = "id")
public class IdDt extends BasePrimitive<String> {

	private boolean myHaveComponentParts;
	private String myResourceType;
	private String myUnqualifiedId;
	private String myUnqualifiedVersionId;
	private String myValue;

	/**
	 * Create a new empty ID
	 */
	public IdDt() {
		super();
	}

	/**
	 * Create a new ID, using a BigDecimal input. Uses {@link BigDecimal#toPlainString()} to generate the string representation.
	 */
	public IdDt(BigDecimal thePid) {
		if (thePid != null) {
			setValue(toPlainStringWithNpeThrowIfNeeded(thePid));
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
	 * Create a new ID using a string. This String may contain a simple ID (e.g. "1234") or it may contain a complete URL (http://example.com/fhir/Patient/1234).
	 * 
	 * <p>
	 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length
	 * limit of 36 characters.
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
	 * Constructor
	 * 
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theId
	 *            The ID (e.g. "123")
	 */
	public IdDt(String theResourceType, BigDecimal theIdPart) {
		this(theResourceType, toPlainStringWithNpeThrowIfNeeded(theIdPart));
	}

	private static String toPlainStringWithNpeThrowIfNeeded(BigDecimal theIdPart) {
		if (theIdPart==null) {
			throw new NullPointerException("BigDecimal ID can not be null");
		}
		return theIdPart.toPlainString();
	}

	/**
	 * Constructor
	 * 
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theId
	 *            The ID (e.g. "123")
	 */
	public IdDt(String theResourceType, String theId) {
		this(theResourceType, theId, null);
	}

	/**
	 * Constructor
	 * 
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theId
	 *            The ID (e.g. "123")
	 * @param theVersionId
	 *            The version ID ("e.g. "456")
	 */
	public IdDt(String theResourceType, String theId, String theVersionId) {
		myResourceType = theResourceType;
		myUnqualifiedId = theId;
		myUnqualifiedVersionId = StringUtils.defaultIfBlank(theVersionId, null);
		myHaveComponentParts = true;
	}

	/**
	 * @deprecated Use {@link #getIdPartAsBigDecimal()} instead (this method was deprocated because its name is ambiguous)
	 */
	public BigDecimal asBigDecimal() {
		return getIdPartAsBigDecimal();
	}

	/**
	 * Returns true if this IdDt matches the given IdDt in terms of resource type and ID, but ignores the URL base
	 */
	@SuppressWarnings("deprecation")
	public boolean equalsIgnoreBase(IdDt theId) {
		if (theId == null) {
			return false;
		}
		if (theId.isEmpty()) {
			return isEmpty();
		}
		return ObjectUtils.equals(getResourceType(), theId.getResourceType()) && ObjectUtils.equals(getIdPart(), theId.getIdPart()) && ObjectUtils.equals(getVersionIdPart(), theId.getVersionIdPart());
	}

	/**
	 * Returns a reference to <code>this</code> IdDt. It is generally not neccesary to use this method but it is provided for consistency with the rest of the API.
	 */
	@Override
	public IdDt getId() {
		return this;
	}

	public String getIdPart() {
		return myUnqualifiedId;
	}

	/**
	 * Returns the unqualified portion of this ID as a big decimal, or <code>null</code> if the value is null
	 * 
	 * @throws NumberFormatException
	 *             If the value is not a valid BigDecimal
	 */
	public BigDecimal getIdPartAsBigDecimal() {
		String val = getIdPart();
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
	public Long getIdPartAsLong() {
		String val = getIdPart();
		if (isBlank(val)) {
			return null;
		}
		return Long.parseLong(val);
	}

	public String getResourceType() {
		return myResourceType;
	}

	/**
	 * Returns the value of this ID. Note that this value may be a fully qualified URL, a relative/partial URL, or a simple ID. Use {@link #getIdPart()} to get just the ID portion.
	 * 
	 * @see #getIdPart()
	 */
	@Override
	public String getValue() {
		if (myValue == null && myHaveComponentParts) {
			if (myUnqualifiedVersionId != null) {
				myValue = myResourceType + '/' + myUnqualifiedId + '/' + Constants.PARAM_HISTORY + '/' + myUnqualifiedVersionId;
			} else {
				myValue = myResourceType + '/' + myUnqualifiedId;
			}
		}
		return myValue;
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

	public String getVersionIdPart() {
		return myUnqualifiedVersionId;
	}

	public Long getVersionIdPartAsLong() {
		if (!hasVersionIdPart()) {
			return null;
		} else {
			return Long.parseLong(getVersionIdPart());
		}
	}

	public boolean hasIdPart() {
		return isNotBlank(getIdPart());
	}

	public boolean hasResourceType() {
		return isNotBlank(myResourceType);
	}

	public boolean hasVersionIdPart() {
		return isNotBlank(getVersionIdPart());
	}

	/**
	 * Returns <code>true</code> if the unqualified ID is a valid {@link Long} value (in other words, it consists only of digits)
	 */
	public boolean isIdPartValidLong() {
		String id = getIdPart();
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

	/**
	 * Returns <code>true</code> if the ID is a local reference (in other words, it begins with the '#' character)
	 */
	public boolean isLocal() {
		return myUnqualifiedId != null && myUnqualifiedId.isEmpty() == false && myUnqualifiedId.charAt(0) == '#';
	}

	/**
	 * Copies the value from the given IdDt to <code>this</code> IdDt. It is generally not neccesary to use this method but it is provided for consistency with the rest of the API.
	 */
	@Override
	public void setId(IdDt theId) {
		setValue(theId.getValue());
	}

	/**
	 * Set the value
	 * 
	 * <p>
	 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length
	 * limit of 36 characters.
	 * </p>
	 * <p>
	 * regex: [a-z0-9\-\.]{1,36}
	 * </p>
	 */
	@Override
	public void setValue(String theValue) throws DataFormatException {
		// TODO: add validation
		myValue = theValue;
		myHaveComponentParts=false;
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
			} else {
				int typeIndex = theValue.lastIndexOf('/', idIndex - 1);
				if (typeIndex == -1) {
					myResourceType = theValue.substring(0, idIndex);
				} else {
					myResourceType = theValue.substring(typeIndex + 1, idIndex);
				}
			}

		}
	}

	/**
	 * Set the value
	 * 
	 * <p>
	 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length
	 * limit of 36 characters.
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
		return getValue();
	}

	public IdDt toUnqualified() {
		return new IdDt(getResourceType(), getIdPart(), getVersionIdPart());
	}

	public IdDt toUnqualifiedVersionless() {
		return new IdDt(getResourceType(), getIdPart());
	}

	public IdDt toVersionless() {
		String value = getValue();
		int i = value.indexOf(Constants.PARAM_HISTORY);
		if (i > 1) {
			return new IdDt(value.substring(0, i - 1));
		} else {
			return this;
		}
	}

	/**
	 * Returns a view of this ID as a fully qualified URL, given a server base and resource name (which will only be used if the ID does not already contain those respective parts). Essentially,
	 * because IdDt can contain either a complete URL or a partial one (or even jut a simple ID), this method may be used to translate into a complete URL.
	 * 
	 * @param theServerBase
	 *            The server base (e.g. "http://example.com/fhir")
	 * @param theResourceType
	 *            The resource name (e.g. "Patient")
	 * @return A fully qualified URL for this ID (e.g. "http://example.com/fhir/Patient/1")
	 */
	public String withServerBase(String theServerBase, String theResourceType) {
		if (getValue().startsWith("http")) {
			return getValue();
		}
		StringBuilder retVal = new StringBuilder();
		retVal.append(theServerBase);
		if (retVal.charAt(retVal.length() - 1) != '/') {
			retVal.append('/');
		}
		if (isNotBlank(getResourceType())) {
			retVal.append(getResourceType());
		} else {
			retVal.append(theResourceType);
		}
		retVal.append('/');
		retVal.append(getIdPart());
		return retVal.toString();
	}

	/**
	 * Creates a new instance of this ID which is identical, but refers to the specific version of this resource ID noted by theVersion.
	 * 
	 * @param theVersion
	 *            The actual version string, e.g. "1"
	 * @return A new instance of IdDt which is identical, but refers to the specific version of this resource ID noted by theVersion.
	 */
	public IdDt withVersion(String theVersion) {
		Validate.notBlank(theVersion, "Version may not be null or empty");

		String existingValue = getValue();
		
		int i = existingValue.indexOf(Constants.PARAM_HISTORY);
		String value;
		if (i > 1) {
			value = existingValue.substring(0, i - 1);
		} else {
			value = existingValue;
		}

		return new IdDt(value + '/' + Constants.PARAM_HISTORY + '/' + theVersion);
	}

}
