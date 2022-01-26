package ca.uhn.fhir.model.api;

import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

/**
 * Represents a FHIR resource path specification, e.g. <code>Patient:name</code>
 * <p>
 * Note on equality: This class uses {@link #getValue() value} and the {@link #isRecurse() recurse} properties to test
 * equality. Prior to HAPI 1.2 (and FHIR DSTU2) the recurse property did not exist, so this may merit consideration when
 * upgrading servers.
 * </p>
 * <p>
 * Note on thrwead safety: This class is not thread safe.
 * </p>
 */
public class Include implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final boolean myImmutable;
	private boolean myIterate;
	private String myValue;
	private String myParamType;
	private String myParamName;
	private String myParamTargetType;

	/**
	 * Constructor for <b>non-recursive</b> include
	 * 
	 * @param theValue
	 *           The <code>_include</code> value, e.g. "Patient:name"
	 */
	public Include(String theValue) {
		this(theValue, false);
	}

	/**
	 * Constructor for an include
	 * 
	 * @param theValue
	 *           The <code>_include</code> value, e.g. "Patient:name"
	 * @param theIterate
	 *           Should the include recurse
	 */
	public Include(String theValue, boolean theIterate) {
		this(theValue, theIterate, false);
	}

	/**
	 * Constructor for an include
	 * 
	 * @param theValue
	 *           The <code>_include</code> value, e.g. "Patient:name"
	 * @param theIterate
	 *           Should the include recurse
	 */
	public Include(String theValue, boolean theIterate, boolean theImmutable) {
		setValue(theValue);
		myIterate = theIterate;
		myImmutable = theImmutable;
	}

	/**
	 * Creates a copy of this include with non-recurse behaviour
	 */
	public Include asNonRecursive() {
		return new Include(myValue, false);
	}

	/**
	 * Creates a copy of this include with recurse behaviour
	 */
	public Include asRecursive() {
		return new Include(myValue, true);
	}

	/**
	 * See the note on equality on the {@link Include class documentation}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Include other = (Include) obj;
		if (myIterate != other.myIterate) {
			return false;
		}
		if (myValue == null) {
			if (other.myValue != null) {
				return false;
			}
		} else if (!myValue.equals(other.myValue)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the portion of the value before the first colon
	 */
	public String getParamType() {
		return myParamType;
	}

	/**
	 * Returns the portion of the value after the first colon but before the second colon
	 */
	public String getParamName() {
		return myParamName;
	}

	/**
	 * Returns the portion of the string after the second colon, or null if there are not two colons in the value.
	 */
	public String getParamTargetType() {
		return myParamTargetType;

	}

	public String getValue() {
		return myValue;
	}

	/**
	 * See the note on equality on the {@link Include class documentation}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (myIterate ? 1231 : 1237);
		result = prime * result + ((myValue == null) ? 0 : myValue.hashCode());
		return result;
	}

	/**
	 * Is this object {@link #toLocked() locked}?
	 */
	public boolean isLocked() {
		return myImmutable;
	}

	public boolean isRecurse() {
		return myIterate;
	}

	/**
	 * Should this include recurse
	 *
	 * @return  Returns a reference to <code>this</code> for easy method chaining
	 */
	public Include setRecurse(boolean theRecurse) {
		myIterate = theRecurse;
		return this;
	}

	public void setValue(String theValue) {
		if (myImmutable) {
			throw new IllegalStateException(Msg.code(1888) + "Can not change the value of this include");
		}

		String value = defaultString(theValue);

		int firstColon = value.indexOf(':');
		String paramType;
		String paramName;
		String paramTargetType;
		if (firstColon == -1 || firstColon == value.length() - 1) {
			paramType = null;
			paramName = null;
			paramTargetType = null;
		} else {
			paramType = value.substring(0, firstColon);
			int secondColon = value.indexOf(':', firstColon + 1);
			if (secondColon == -1) {
				paramName = value.substring(firstColon + 1);
				paramTargetType = null;
			} else {
				paramName =  value.substring(firstColon + 1, secondColon);
				paramTargetType = value.substring(secondColon + 1);
			}
		}

		myParamType = paramType;
		myParamName = paramName;
		myParamTargetType = paramTargetType;
		myValue = theValue;

	}

	/**
	 * Return a new
	 */
	public Include toLocked() {
		Include retVal = new Include(myValue, myIterate, true);
		return retVal;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("value", myValue);
		builder.append("iterate", myIterate);
		return builder.toString();
	}

	/**
	 * Creates and returns a new copy of this Include with the given type. The following table shows what will be
	 * returned:
	 * <table>
	 * <tr>
	 * <th>Initial Contents</th>
	 * <th>theResourceType</th>
	 * <th>Output</th>
	 * </tr>
	 * <tr>
	 * <td>Patient:careProvider</th>
	 * <th>Organization</th>
	 * <th>Patient:careProvider:Organization</th>
	 * </tr>
	 * <tr>
	 * <td>Patient:careProvider:Practitioner</th>
	 * <th>Organization</th>
	 * <th>Patient:careProvider:Organization</th>
	 * </tr>
	 * <tr>
	 * <td>Patient</th>
	 * <th>(any)</th>
	 * <th>{@link IllegalStateException}</th>
	 * </tr>
	 * </table>
	 * 
	 * @param theResourceType
	 *           The resource type (e.g. "Organization")
	 * @return A new copy of the include. Note that if this include is {@link #toLocked() locked}, the returned include
	 *         will be too
	 */
	public Include withType(String theResourceType) {
		StringBuilder b = new StringBuilder();
		
		String paramType = getParamType();
		String paramName = getParamName();
		if (isBlank(paramType) || isBlank(paramName)) {
			throw new IllegalStateException(Msg.code(1889) + "This include does not contain a value in the format [ResourceType]:[paramName]");
		}
		b.append(paramType);
		b.append(":");
		b.append(paramName);
		
		if (isNotBlank(theResourceType)) {
			b.append(':');
			b.append(theResourceType);
		}
		Include retVal = new Include(b.toString(), myIterate, myImmutable);
		return retVal;
	}

}
