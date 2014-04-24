package ca.uhn.fhir.model.primitive;

/*
 * #%L
 * HAPI FHIR Library
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

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.dstu.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu.composite.CodingDt;

@DatatypeDef(name = "CodeableConcept")
public class BoundCodeableConceptDt<T extends Enum<?>> extends CodeableConceptDt {

	private IValueSetEnumBinder<T> myBinder;

	public BoundCodeableConceptDt(IValueSetEnumBinder<T> theBinder) {
		myBinder = theBinder;
	}

	public BoundCodeableConceptDt(IValueSetEnumBinder<T> theBinder, T theValue) {
		myBinder = theBinder;
		setValueAsEnum(theValue);
	}

	public BoundCodeableConceptDt(IValueSetEnumBinder<T> theBinder, Collection<T> theValues) {
		myBinder = theBinder;
		setValueAsEnum(theValues);
	}

	public void setValueAsEnum(Collection<T> theValues) {
		getCoding().clear();
		if (theValues == null) {
			return;
		}
		for (T next : theValues) {
			getCoding().add(new CodingDt(myBinder.toSystemString(next), myBinder.toCodeString(next)));
		}
	}

	public void setValueAsEnum(T theValue) {
		getCoding().clear();
		if (theValue == null) {
			return;
		}
		getCoding().add(new CodingDt(myBinder.toSystemString(theValue), myBinder.toCodeString(theValue)));
	}

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
