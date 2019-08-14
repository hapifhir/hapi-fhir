package ca.uhn.fhir.model.api;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
 */
public class Include implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final boolean myImmutable;
	private boolean myRecurse;
	private String myValue;

	/**
	 * Constructor for <b>non-recursive</b> include
	 * 
	 * @param theValue
	 *           The <code>_include</code> value, e.g. "Patient:name"
	 */
	public Include(String theValue) {
		myValue = theValue;
		myImmutable = false;
	}

	/**
	 * Constructor for an include
	 * 
	 * @param theValue
	 *           The <code>_include</code> value, e.g. "Patient:name"
	 * @param theRecurse
	 *           Should the include recurse
	 */
	public Include(String theValue, boolean theRecurse) {
		myValue = theValue;
		myRecurse = theRecurse;
		myImmutable = false;
	}

	/**
	 * Constructor for an include
	 * 
	 * @param theValue
	 *           The <code>_include</code> value, e.g. "Patient:name"
	 * @param theRecurse
	 *           Should the include recurse
	 */
	public Include(String theValue, boolean theRecurse, boolean theImmutable) {
		myValue = theValue;
		myRecurse = theRecurse;
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
		if (myRecurse != other.myRecurse) {
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
		int firstColon = myValue.indexOf(':');
		if (firstColon == -1 || firstColon == myValue.length() - 1) {
			return null;
		}
		return myValue.substring(0, firstColon);
	}

	/**
	 * Returns the portion of the value after the first colon but before the second colon
	 */
	public String getParamName() {
		int firstColon = myValue.indexOf(':');
		if (firstColon == -1 || firstColon == myValue.length() - 1) {
			return null;
		}
		int secondColon = myValue.indexOf(':', firstColon + 1);
		if (secondColon != -1) {
			return myValue.substring(firstColon + 1, secondColon);
		}
		return myValue.substring(firstColon + 1);
	}

	/**
	 * Returns the portion of the string after the second colon, or null if there are not two colons in the value.
	 */
	public String getParamTargetType() {
		int firstColon = myValue.indexOf(':');
		if (firstColon == -1 || firstColon == myValue.length() - 1) {
			return null;
		}
		int secondColon = myValue.indexOf(':', firstColon + 1);
		if (secondColon != -1) {
			return myValue.substring(secondColon + 1);
		}
		return null;

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
		result = prime * result + (myRecurse ? 1231 : 1237);
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
		return myRecurse;
	}

	/**
	 * Should this include recurse
	 *
	 * @return  Returns a reference to <code>this</code> for easy method chaining
	 */
	public Include setRecurse(boolean theRecurse) {
		myRecurse = theRecurse;
		return this;
	}

	public void setValue(String theValue) {
		if (myImmutable) {
			throw new IllegalStateException("Can not change the value of this include");
		}
		myValue = theValue;
	}

	/**
	 * Return a new
	 */
	public Include toLocked() {
		Include retVal = new Include(myValue, myRecurse, true);
		return retVal;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("value", myValue);
		builder.append("recurse", myRecurse);
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
			throw new IllegalStateException("This include does not contain a value in the format [ResourceType]:[paramName]");
		}
		b.append(paramType);
		b.append(":");
		b.append(paramName);
		
		if (isNotBlank(theResourceType)) {
			b.append(':');
			b.append(theResourceType);
		}
		Include retVal = new Include(b.toString(), myRecurse, myImmutable);
		return retVal;
	}

}
