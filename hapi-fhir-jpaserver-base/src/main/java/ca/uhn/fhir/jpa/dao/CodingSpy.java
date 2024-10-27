/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.primitive.BooleanDt;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;
import org.hl7.fhir.instance.model.api.IBaseCoding;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * We are trying to preserve null behaviour despite IBaseCoding using primitive boolean for userSelected.
 */
public class CodingSpy {
	final Map<Class, Field> mySpies = new ConcurrentHashMap<>();

	/**
	 * Reach into the Coding and pull out the Boolean instead of the boolean.
	 * @param theValue the Coding, or CodingDt
	 * @return the Boolean
	 */
	public Boolean getBooleanObject(IBaseCoding theValue) {
		Field spy = getSpy(theValue);
		try {
			Object o = spy.get(theValue);
			if (o == null) {
				return null;
			}
			if (o instanceof BooleanDt) {
				BooleanDt booleanDt = (BooleanDt) o;
				return booleanDt.getValue();
			}
			if (o instanceof IBaseBooleanDatatype) {
				IBaseBooleanDatatype booleanValue = (IBaseBooleanDatatype) o;
				return booleanValue.getValue();
			}
			if (o instanceof Boolean) {
				return (Boolean) o;
			}
			throw new RuntimeException(
					Msg.code(2342) + "unsupported type :" + theValue.getClass().getName());
		} catch (IllegalAccessException theException) {
			// should never happen - all Coding models have this field.
			throw new RuntimeException(Msg.code(2343) + "illegal access during reflection", theException);
		}
	}

	private Field getSpy(IBaseCoding theValue) {
		return mySpies.computeIfAbsent(theValue.getClass(), k -> getFieldHandle(k));
	}

	private static Field getFieldHandle(Class k) {
		Field result = FieldUtils.getField(k, "userSelected", true);
		if (result == null) {
			result = FieldUtils.getField(k, "myUserSelected", true);
		}
		return result;
	}
}
