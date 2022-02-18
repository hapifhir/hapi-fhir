package ca.uhn.fhir.model.api;

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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uhn.fhir.parser.DataFormatException;

public abstract class BasePrimitive<T> extends BaseIdentifiableElement implements IPrimitiveDatatype<T>, Externalizable {

	private T myCoercedValue;
	private String myStringValue;

	/**
	 * Subclasses must override to convert a "coerced" value into an encoded one.
	 * 
	 * @param theValue
	 *           Will not be null
	 * @return May return null if the value does not correspond to anything
	 */
	protected abstract String encode(T theValue);

	@Override
	public boolean equals(Object theObj) {
		if (theObj == null) {
			return false;
		}
		if (!(theObj.getClass() == getClass())) {
			return false;
		}

		BasePrimitive<?> o = (BasePrimitive<?>) theObj;

		EqualsBuilder b = new EqualsBuilder();
		b.append(getValue(), o.getValue());
		return b.isEquals();
	}

	@Override
	public T getValue() {
		return myCoercedValue;
	}

	@Override
	public String getValueAsString() throws DataFormatException {
		return myStringValue;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getValue()).toHashCode();
	}

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && getValue() == null;
	}

	/**
	 * Subclasses must override to convert an encoded representation of this datatype into a "coerced" one
	 * 
	 * @param theValue
	 *           Will not be null
	 * @return May return null if the value does not correspond to anything
	 */
	protected abstract T parse(String theValue);

	@Override
	public void readExternal(ObjectInput theIn) throws IOException, ClassNotFoundException {
		String object = (String) theIn.readObject();
		setValueAsString(object);
	}

	@Override
	public BasePrimitive<T> setValue(T theValue) throws DataFormatException {
		myCoercedValue = theValue;
		updateStringValue();
		return this;
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		if (theValue == null) {
			myCoercedValue = null;
		} else {
			// NB this might be null
			myCoercedValue = parse(theValue);
		}
		myStringValue = theValue;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getValueAsString() + "]";
	}

	protected void updateStringValue() {
		if (myCoercedValue == null) {
			myStringValue = null;
		} else {
			// NB this might be null
			myStringValue = encode(myCoercedValue);
		}
	}

	@Override
	public void writeExternal(ObjectOutput theOut) throws IOException {
		theOut.writeObject(getValueAsString());
	}

	@Override
	public boolean hasValue() {
		return !StringUtils.isBlank(getValueAsString());
	}

}
