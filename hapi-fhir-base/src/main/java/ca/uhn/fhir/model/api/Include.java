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
 * Represents a FHIR resource path specification, e.g.
 * <code>Patient.gender.coding</code>
 * <p>
 * Note on equality: This class uses the {@link Include#setValue(String) value} 
 * as the single item used to provide {@link #hashCode()} and {@link #equals(Object)}.
 * </p>
 */
public class Include {

	private String myValue;

	public Include(String theValue) {
		myValue = theValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if ((obj instanceof Include)==false)
			return false;
		Include other = (Include) obj;
		if (myValue == null) {
			if (other.myValue != null)
				return false;
		} else if (!myValue.equals(other.myValue))
			return false;
		return true;
	}

	public String getValue() {
		return myValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myValue == null) ? 0 : myValue.hashCode());
		return result;
	}

	public void setValue(String theValue) {
		myValue = theValue;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("myValue", myValue);
		return builder.toString();
	}
	
}
