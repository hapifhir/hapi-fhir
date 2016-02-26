package ca.uhn.fhir.model.primitive;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.UrlUtil;

/**
 * Represents the FHIR ID type. This is the actual resource ID, meaning the ID that will be used in RESTful URLs, Resource References, etc. to represent a specific instance of a resource.
 * 
 * <p>
 * <b>Description</b>: A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length
 * limit of 36 characters.
 * </p>
 * <p>
 * regex: [a-z-Z0-9\-\.]{1,36}
 * </p>
 */
@DatatypeDef(name = "id", profileOf=StringDt.class)
public class IdDt extends UriDt implements IPrimitiveDatatype<String>, IIdType {

	private String myBaseUrl;
	private boolean myHaveComponentParts;
	private String myResourceType;
	private String myUnqualifiedId;
	private String myUnqualifiedVersionId;

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
	 * @param theIdPart
	 *            The ID (e.g. "123")
	 */
	public IdDt(String theResourceType, BigDecimal theIdPart) {
		this(theResourceType, toPlainStringWithNpeThrowIfNeeded(theIdPart));
	}

	/**
	 * Constructor
	 * 
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theIdPart
	 *            The ID (e.g. "123")
	 */
	public IdDt(String theResourceType, Long theIdPart) {
		this(theResourceType, toPlainStringWithNpeThrowIfNeeded(theIdPart));
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
		this(null, theResourceType, theId, theVersionId);
	}

	/**
	 * Constructor
	 * 
	 * @param theBaseUrl
	 *            The server base URL (e.g. "http://example.com/fhir")
	 * @param theResourceType
	 *            The resource type (e.g. "Patient")
	 * @param theId
	 *            The ID (e.g. "123")
	 * @param theVersionId
	 *            The version ID ("e.g. "456")
	 */
	public IdDt(String theBaseUrl, String theResourceType, String theId, String theVersionId) {
		myBaseUrl = theBaseUrl;
		myResourceType = theResourceType;
		myUnqualifiedId = theId;
		myUnqualifiedVersionId = StringUtils.defaultIfBlank(theVersionId, null);
		myHaveComponentParts = true;
	}

	/**
	 * Creates an ID based on a given URL
	 */
	public IdDt(UriDt theUrl) {
		setValue(theUrl.getValueAsString());
	}

	public void applyTo(IBaseResource theResouce) {
		if (theResouce == null) {
			throw new NullPointerException("theResource can not be null");
		} else if (theResouce instanceof IResource) {
			((IResource) theResouce).setId(new IdDt(getValue()));
		} else if (theResouce instanceof IAnyResource) {
			((IAnyResource) theResouce).setId(getValue());
		} else {
			throw new IllegalArgumentException("Unknown resource class type, does not implement IResource or extend Resource");
		}
	}

	/**
	 * @deprecated Use {@link #getIdPartAsBigDecimal()} instead (this method was deprocated because its name is ambiguous)
	 */
	@Deprecated
	public BigDecimal asBigDecimal() {
		return getIdPartAsBigDecimal();
	}

	private String determineLocalPrefix(String theValue) {
		if (theValue == null || theValue.isEmpty()) {
			return null;
		}
		if (theValue.startsWith("#")) {
			return "#";
		}
		int lastPrefix = -1;
		for (int i = 0; i < theValue.length(); i++) {
			char nextChar = theValue.charAt(i);
			if (nextChar == ':') {
				lastPrefix = i;
			} else if (!Character.isLetter(nextChar) || !Character.isLowerCase(nextChar)) {
				break;
			}
		}
		if (lastPrefix != -1) {
			String candidate = theValue.substring(0, lastPrefix + 1);
			if (candidate.startsWith("cid:") || candidate.startsWith("urn:")) {
				return candidate;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean equals(Object theArg0) {
		if (!(theArg0 instanceof IdDt)) {
			return false;
		}
		IdDt id = (IdDt) theArg0;
		return StringUtils.equals(getValueAsString(), id.getValueAsString());
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
	 * Returns the portion of this resource ID which corresponds to the server base URL. For example given the resource ID <code>http://example.com/fhir/Patient/123</code> the base URL would be
	 * <code>http://example.com/fhir</code>.
	 * <p>
	 * This method may return null if the ID contains no base (e.g. "Patient/123")
	 * </p>
	 */
	@Override
	public String getBaseUrl() {
		return myBaseUrl;
	}

	/**
	 * Returns only the logical ID part of this ID. For example, given the ID "http://example,.com/fhir/Patient/123/_history/456", this method would return "123".
	 */
	@Override
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
	@Override
	public Long getIdPartAsLong() {
		String val = getIdPart();
		if (isBlank(val)) {
			return null;
		}
		return Long.parseLong(val);
	}

	@Override
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
		if (super.getValue() == null && myHaveComponentParts) {
			
			if (determineLocalPrefix(myBaseUrl) != null && myResourceType == null && myUnqualifiedVersionId == null) {
				return myBaseUrl + myUnqualifiedId;
			}
			
			StringBuilder b = new StringBuilder();
			if (isNotBlank(myBaseUrl)) {
				b.append(myBaseUrl);
				if (myBaseUrl.charAt(myBaseUrl.length() - 1) != '/') {
					b.append('/');
				}
			}

			if (isNotBlank(myResourceType)) {
				b.append(myResourceType);
			}

			if (b.length() > 0) {
				b.append('/');
			}

			b.append(myUnqualifiedId);
			if (isNotBlank(myUnqualifiedVersionId)) {
				b.append('/');
				b.append(Constants.PARAM_HISTORY);
				b.append('/');
				b.append(myUnqualifiedVersionId);
			}
			String value = b.toString();
			super.setValue(value);
		}
		return super.getValue();
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

	@Override
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

	/**
	 * Returns true if this ID has a base url
	 * 
	 * @see #getBaseUrl()
	 */
	public boolean hasBaseUrl() {
		return isNotBlank(myBaseUrl);
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getValueAsString());
		return b.toHashCode();
	}

	@Override
	public boolean hasIdPart() {
		return isNotBlank(getIdPart());
	}

	@Override
	public boolean hasResourceType() {
		return isNotBlank(myResourceType);
	}

	@Override
	public boolean hasVersionIdPart() {
		return isNotBlank(getVersionIdPart());
	}

	/**
	 * Returns <code>true</code> if this ID contains an absolute URL (in other words, a URL starting with "http://" or "https://"
	 */
	@Override
	public boolean isAbsolute() {
		if (StringUtils.isBlank(getValue())) {
			return false;
		}
		return UrlUtil.isAbsolute(getValue());
	}

	@Override
	public boolean isEmpty() {
		return isBlank(getValue());
	}

	@Override
	public boolean isIdPartValid() {
		String id = getIdPart();
		if (StringUtils.isBlank(id)) {
			return false;
		}
		if (id.length() > 64) {
			return false;
		}
		for (int i = 0; i < id.length(); i++) {
			char nextChar = id.charAt(i);
			if (nextChar >= 'a' && nextChar <= 'z') {
				continue;
			}
			if (nextChar >= 'A' && nextChar <= 'Z') {
				continue;
			}
			if (nextChar >= '0' && nextChar <= '9') {
				continue;
			}
			if (nextChar == '-' || nextChar == '.') {
				continue;
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean isIdPartValidLong() {
		return isValidLong(getIdPart());
	}

	/**
	 * Returns <code>true</code> if the ID is a local reference (in other words, it begins with the '#' character)
	 */
	@Override
	public boolean isLocal() {
		return "#".equals(myBaseUrl);
	}

	@Override
	public boolean isVersionIdPartValidLong() {
		return isValidLong(getVersionIdPart());
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
	public IdDt setValue(String theValue) throws DataFormatException {
		// TODO: add validation
		super.setValue(theValue);
		myHaveComponentParts = false;
		
		String localPrefix = determineLocalPrefix(theValue);
		
		if (StringUtils.isBlank(theValue)) {
			myBaseUrl = null;
			super.setValue(null);
			myUnqualifiedId = null;
			myUnqualifiedVersionId = null;
			myResourceType = null;
		} else if (theValue.charAt(0) == '#' && theValue.length() > 1) {
			super.setValue(theValue);
			myBaseUrl = "#";
			myUnqualifiedId = theValue.substring(1);
			myUnqualifiedVersionId = null;
			myResourceType = null;
			myHaveComponentParts = true;
		} else if (localPrefix != null) {
			myBaseUrl = localPrefix;
			myUnqualifiedId = theValue.substring(localPrefix.length());
			myUnqualifiedVersionId = null;
			myResourceType = null;
			myHaveComponentParts = true;
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

			myBaseUrl = null;
			if (idIndex <= 0) {
				myResourceType = null;
			} else {
				int typeIndex = theValue.lastIndexOf('/', idIndex - 1);
				if (typeIndex == -1) {
					myResourceType = theValue.substring(0, idIndex);
				} else {
					myResourceType = theValue.substring(typeIndex + 1, idIndex);

					if (typeIndex > 4) {
						myBaseUrl = theValue.substring(0, typeIndex);
					}

				}
			}

		}
		return this;
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

	/**
	 * Returns a new IdDt containing this IdDt's values but with no server base URL if one is present in this IdDt. For example, if this IdDt contains the ID "http://foo/Patient/1", this method will
	 * return a new IdDt containing ID "Patient/1".
	 */
	@Override
	public IdDt toUnqualified() {
		return new IdDt(getResourceType(), getIdPart(), getVersionIdPart());
	}

	@Override
	public IdDt toUnqualifiedVersionless() {
		if (isLocal()) {
			return toVersionless();
		}
		return new IdDt(getResourceType(), getIdPart());
	}

	@Override
	public IdDt toVersionless() {
		return new IdDt(getBaseUrl(), getResourceType(), getIdPart(), null);
	}

	@Override
	public IdDt withResourceType(String theResourceName) {
		return new IdDt(theResourceName, getIdPart(), getVersionIdPart());
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
	@Override
	public IdDt withServerBase(String theServerBase, String theResourceType) {
		return new IdDt(theServerBase, theResourceType, getIdPart(), getVersionIdPart());
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

	private static boolean isValidLong(String id) {
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
	 * Construct a new ID with with form "urn:uuid:[UUID]" where [UUID] is a new, randomly
	 * created UUID generated by {@link UUID#randomUUID()}
	 */
	public static IdDt newRandomUuid() {
		return new IdDt("urn:uuid:" + UUID.randomUUID().toString());
	}

	/**
	 * Retrieves the ID from the given resource instance
	 */
	public static IdDt of(IBaseResource theResouce) {
		if (theResouce == null) {
			throw new NullPointerException("theResource can not be null");
		} else {
			IIdType retVal = theResouce.getIdElement();
			if (retVal == null) {
				return null;
			} else if (retVal instanceof IdDt) {
				return (IdDt) retVal;
			} else {
				return new IdDt(retVal.getValue());
			}
		}
	}

	private static String toPlainStringWithNpeThrowIfNeeded(BigDecimal theIdPart) {
		if (theIdPart == null) {
			throw new NullPointerException("BigDecimal ID can not be null");
		}
		return theIdPart.toPlainString();
	}

	private static String toPlainStringWithNpeThrowIfNeeded(Long theIdPart) {
		if (theIdPart == null) {
			throw new NullPointerException("Long ID can not be null");
		}
		return theIdPart.toString();
	}

	@Override
	public IIdType setParts(String theBaseUrl, String theResourceType, String theIdPart, String theVersionIdPart) {
		if (isNotBlank(theVersionIdPart)) {
			Validate.notBlank(theResourceType, "If theVersionIdPart is populated, theResourceType and theIdPart must be populated");
			Validate.notBlank(theIdPart, "If theVersionIdPart is populated, theResourceType and theIdPart must be populated");
		}
		if (isNotBlank(theBaseUrl) && isNotBlank(theIdPart)) {
			Validate.notBlank(theResourceType, "If theBaseUrl is populated and theIdPart is populated, theResourceType must be populated");
		}
		
		setValue(null);
		
		myBaseUrl = theBaseUrl;
		myResourceType = theResourceType;
		myUnqualifiedId = theIdPart;
		myUnqualifiedVersionId = StringUtils.defaultIfBlank(theVersionIdPart, null);
		myHaveComponentParts = true;
		
		return this;
	}

}
