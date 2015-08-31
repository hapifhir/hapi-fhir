package ca.uhn.fhir.model.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
public class Include {

	private boolean myRecurse;
	private String myValue;
	private boolean myImmutable;

	/**
	 * Constructor for <b>non-recursive</b> include
	 * 
	 * @param theValue
	 *           The <code>_include</code> value, e.g. "Patient:name"
	 */
	public Include(String theValue) {
		myValue = theValue;
	}

	/**
	 * Constructor for <b>non-recursive</b> include
	 * 
	 * @param theValue
	 *           The <code>_include</code> value, e.g. "Patient:name"
	 * @param theRecurse
	 *           Should the include recurse
	 */
	public Include(String theValue, boolean theRecurse) {
		myValue = theValue;
		myRecurse = theRecurse;
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

	public boolean isRecurse() {
		return myRecurse;
	}

	public void setRecurse(boolean theRecurse) {
		myRecurse = theRecurse;
	}

	public void setValue(String theValue) {
		if (myImmutable) {
			throw new IllegalStateException("Can not change the value of this include");
		}
		myValue = theValue;
	}

	public Include toLocked() {
		Include retVal = new Include(myValue, myRecurse);
		retVal.myImmutable = true;
		return retVal;
	}
	
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("value", myValue);
		builder.append("recurse", myRecurse);
		return builder.toString();
	}
}
