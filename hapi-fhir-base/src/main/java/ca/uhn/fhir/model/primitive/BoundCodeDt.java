package ca.uhn.fhir.model.primitive;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

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

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name = "code", isSpecialization = true)
public class BoundCodeDt<T extends Enum<?>> extends CodeDt {

	private IValueSetEnumBinder<T> myBinder;

	/**
	 * @deprecated This constructor is provided only for serialization support. Do not call it directly!
	 */
	@Deprecated
	public BoundCodeDt() {
		// nothing
	}

	public BoundCodeDt(IValueSetEnumBinder<T> theBinder) {
		Validate.notNull(theBinder, "theBinder must not be null");
		myBinder = theBinder;
	}

	public BoundCodeDt(IValueSetEnumBinder<T> theBinder, T theValue) {
		Validate.notNull(theBinder, "theBinder must not be null");
		myBinder = theBinder;
		setValueAsEnum(theValue);
	}

	public IValueSetEnumBinder<T> getBinder() {
		return myBinder;
	}
	
	public T getValueAsEnum() {
		Validate.notNull(myBinder, "This object does not have a binder. Constructor BoundCodeDt() should not be called!");
		T retVal = myBinder.fromCodeString(getValue());
		if (retVal == null) {
			// TODO: throw special exception type?
		}
		return retVal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput theIn) throws IOException, ClassNotFoundException {
		super.readExternal(theIn);
		myBinder = (IValueSetEnumBinder<T>) theIn.readObject();
	}

	public void setValueAsEnum(T theValue) {
		Validate.notNull(myBinder, "This object does not have a binder. Constructor BoundCodeDt() should not be called!");
		if (theValue==null) {
			setValue(null);
		} else {
			setValue(myBinder.toCodeString(theValue));
		}
	}

	@Override
	public void writeExternal(ObjectOutput theOut) throws IOException {
		super.writeExternal(theOut);
		theOut.writeObject(myBinder);
	}
}
