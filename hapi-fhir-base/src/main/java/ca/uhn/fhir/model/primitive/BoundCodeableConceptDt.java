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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;

@DatatypeDef(name = "CodeableConcept", isSpecialization = true)
public class BoundCodeableConceptDt<T extends Enum<?>> extends CodeableConceptDt {

	private IValueSetEnumBinder<T> myBinder;

	/**
	 * Constructor
	 */
	public BoundCodeableConceptDt(IValueSetEnumBinder<T> theBinder) {
		myBinder = theBinder;
	}

	/**
	 * Constructor
	 */
	public BoundCodeableConceptDt(IValueSetEnumBinder<T> theBinder, T theValue) {
		myBinder = theBinder;
		setValueAsEnum(theValue);
	}

	/**
	 * Constructor
	 */
	public BoundCodeableConceptDt(IValueSetEnumBinder<T> theBinder, Collection<T> theValues) {
		myBinder = theBinder;
		setValueAsEnum(theValues);
	}

	/**
	 * Sets the {@link #getCoding()} to contain a coding with the code and
	 * system defined by the given enumerated types, AND clearing any existing
	 * codings first. If theValue is null, existing codings are cleared and no
	 * codings are added.
	 * 
	 * @param theValue
	 *            The value to add, or <code>null</code>
	 */
	public void setValueAsEnum(Collection<T> theValues) {
		getCoding().clear();
		if (theValues != null) {
			for (T next : theValues) {
				getCoding().add(new CodingDt(myBinder.toSystemString(next), myBinder.toCodeString(next)));
			}
		}
	}

	/**
	 * Sets the {@link #getCoding()} to contain a coding with the code and
	 * system defined by the given enumerated type, AND clearing any existing
	 * codings first. If theValue is null, existing codings are cleared and no
	 * codings are added.
	 * 
	 * @param theValue
	 *            The value to add, or <code>null</code>
	 */
	public void setValueAsEnum(T theValue) {
		getCoding().clear();
		if (theValue == null) {
			return;
		}
		getCoding().add(new CodingDt(myBinder.toSystemString(theValue), myBinder.toCodeString(theValue)));
	}

	/**
	 * Loops through the {@link #getCoding() codings} in this codeable concept
	 * and returns the first bound enumerated type that matches. <b>Use
	 * caution</b> using this method, see the return description for more
	 * information.
	 * 
	 * @return Returns the bound enumerated type, or <code>null</code> if none
	 *         are found. Note that a null return value doesn't neccesarily
	 *         imply that this Codeable Concept has no codes, only that it has
	 *         no codes that match the enum.
	 */
	public Set<T> getValueAsEnum() {
		Set<T> retVal = new HashSet<T>();
		for (CodingDt next : getCoding()) {
			if (next == null) {
				continue;
			}
			T nextT = myBinder.fromCodeString(defaultString(next.getCode().getValue()), defaultString(next.getSystem().getValueAsString()));
			if (nextT != null) {
				retVal.add(nextT);
			} else {
				// TODO: throw special exception type?
			}
		}
		return retVal;
	}

}
