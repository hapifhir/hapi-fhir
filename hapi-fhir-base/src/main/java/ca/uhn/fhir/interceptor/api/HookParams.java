/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.interceptor.api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HookParams {

	private ListMultimap<Class<?>, Object> myParams = ArrayListMultimap.create();

	/**
	 * Constructor
	 */
	public HookParams() {}

	/**
	 * Constructor
	 */
	public HookParams(Object... theParams) {
		for (Object next : theParams) {
			add(next);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> HookParams add(@Nonnull T theNext) {
		Class<T> nextClass = (Class<T>) theNext.getClass();
		add(nextClass, theNext);
		return this;
	}

	public <T> HookParams add(Class<T> theType, T theParam) {
		return doAdd(theType, theParam);
	}

	//	/**
	//	 * This is useful for providing a lazy-loaded (generally expensive to create)
	//	 * parameters
	//	 */
	//	public <T> HookParams addSupplier(Class<T> theType, Supplier<T> theParam) {
	//		return doAdd(theType, theParam);
	//	}

	private <T> HookParams doAdd(Class<T> theType, Object theParam) {
		Validate.isTrue(theType.equals(Supplier.class) == false, "Can not add parameters of type Supplier");
		myParams.put(theType, theParam);
		return this;
	}

	public <T> T get(Class<T> theParamType) {
		return get(theParamType, 0);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> theParamType, int theIndex) {
		List<Object> objects = myParams.get(theParamType);
		Object retVal = null;
		if (objects.size() > theIndex) {
			retVal = objects.get(theIndex);
		}

		retVal = unwrapValue(retVal);

		return (T) retVal;
	}

	private Object unwrapValue(Object theValue) {
		if (theValue instanceof Supplier) {
			theValue = ((Supplier) theValue).get();
		}
		return theValue;
	}

	/**
	 * Returns an unmodifiable multimap of the params, where the
	 * key is the param type and the value is the actual instance
	 */
	public ListMultimap<Class<?>, Object> getParamsForType() {
		ArrayListMultimap<Class<?>, Object> retVal = ArrayListMultimap.create();
		myParams.entries().forEach(entry -> retVal.put(entry.getKey(), unwrapValue(entry.getValue())));
		return Multimaps.unmodifiableListMultimap(retVal);
	}

	public Collection<Object> values() {
		return Collections.unmodifiableCollection(myParams.values()).stream()
				.map(t -> unwrapValue(t))
				.collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	public <T> HookParams addIfMatchesType(Class<T> theType, Object theParam) {
		if (theParam == null) {
			add(theType, null);
		} else {
			if (theType.isAssignableFrom(theParam.getClass())) {
				T param = (T) theParam;
				add(theType, param);
			} else {
				add(theType, null);
			}
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		myParams.forEach((key, value) -> {
			b.append("  ")
					.append(key.getSimpleName())
					.append(": ")
					.append(valueAsStringForLog(value))
					.append("\n");
		});
		return b.toString();
	}

	private static String valueAsStringForLog(Object theValue) {
		if (theValue instanceof IBaseResource) {
			return ((IBaseResource) theValue).getIdElement().toString();
		}
		return theValue.toString();
	}
}
