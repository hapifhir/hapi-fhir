package org.hl7.fhir.r4.model;

/*-
 * #%L
 * org.hl7.fhir.r4
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.model.api.IElement;

public abstract class PrimitiveType<T> extends Type implements IPrimitiveType<T>, IBaseHasExtensions, IElement, Externalizable {

	private static final long serialVersionUID = 3L;

	private T myCoercedValue;
	private String myStringValue;

	public String asStringValue() {
		return myStringValue;
	}

	public abstract Type copy();

	/**
	 * Subclasses must override to convert a "coerced" value into an encoded one.
	 * 
	 * @param theValue
	 *            Will not be null
	 * @return May return null if the value does not correspond to anything
	 */
	protected abstract String encode(T theValue);

	@Override
	public boolean equalsDeep(Base obj) {
		if (!super.equalsDeep(obj))
			return false;
		if (obj == null) {
			return false;
		}
		if (!(obj.getClass() == getClass())) {
			return false;
		}

		PrimitiveType<?> o = (PrimitiveType<?>) obj;

		EqualsBuilder b = new EqualsBuilder();
		b.append(getValue(), o.getValue());
		return b.isEquals();
	}

	@Override
	public boolean equalsShallow(Base obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj.getClass() == getClass())) {
			return false;
		}

		PrimitiveType<?> o = (PrimitiveType<?>) obj;

		EqualsBuilder b = new EqualsBuilder();
		b.append(getValue(), o.getValue());
		return b.isEquals();
	}

	public void fromStringValue(String theValue) {
		myStringValue = theValue;
		if (theValue == null) {
			myCoercedValue = null;
		} else {
			// NB this might be null
			myCoercedValue = parse(theValue);
		}
	}

	public T getValue() {
		return myCoercedValue;
	}

	public String getValueAsString() {
		return asStringValue();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getValue()).toHashCode();
	}

	public boolean hasValue() {
  	  return !StringUtils.isBlank(getValueAsString());
	}
	
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && StringUtils.isBlank(getValueAsString());
	}

	public boolean isPrimitive() {
		return true;
	}

	/**
	 * Subclasses must override to convert an encoded representation of this datatype into a "coerced" one
	 * 
	 * @param theValue
	 *            Will not be null
	 * @return May return null if the value does not correspond to anything
	 */
	protected abstract T parse(String theValue);

	public String primitiveValue() {
		return asStringValue();
	}

	@Override
	public void readExternal(ObjectInput theIn) throws IOException, ClassNotFoundException {
		String object = (String) theIn.readObject();
		setValueAsString(object);
	}

	public PrimitiveType<T> setValue(T theValue) {
		myCoercedValue = theValue;
		updateStringValue();
		return this;
	}

	public void setValueAsString(String theValue) {
		fromStringValue(theValue);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + asStringValue() + "]";
	}

	protected Type typedCopy() {
		return copy();
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
  public Base setProperty(int hash, String name, Base value) throws FHIRException {
    switch (hash) {
    case 111972721: // value
      setValueAsString(value.toString()); 
      return value;
    default: return super.setProperty(hash, name, value);
    }
  }

  @Override
  public Base setProperty(String name, Base value) throws FHIRException {
    if (name.equals("value"))
      setValueAsString(value.toString()); 
    else
      return super.setProperty(name, value);
    return value;
  }

  @Override
  public Base makeProperty(int hash, String name) throws FHIRException {
    if (hash == 111972721) {
      return this; 
    } else
      return super.makeProperty(hash, name);

  }


  @Override
  public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
    if (hash == 111972721) {
      Base[] b = new Base[1];
      b[0] = new StringType(getValueAsString());
      return b;
    } else
      return super.getProperty(hash, name, checkValid);
  }

  public String[] getTypesForProperty(int hash, String name) throws FHIRException {
    if (name.equals("value"))
      return new String[] {fhirType(), "string"}; 
    else
      return super.getTypesForProperty(hash, name);

  }

  /*
   * this is a work around for representation issues with Bigdecimal. So comments in DecimaType. 
   * Yes, you can cut yourself with this method... 
   */
  protected void forceStringValue(String value) {
    myStringValue = value;
  }
}
