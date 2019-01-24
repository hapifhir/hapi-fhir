package ca.uhn.fhir.jpa.model.interceptor.api;

/*-
 * #%L
 * HAPI FHIR Model
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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HookParams {

	private ListMultimap<Class<?>, Object> myParams = ArrayListMultimap.create();

	/**
	 * Constructor
	 */
	public HookParams() {
	}

	/**
	 * Constructor
	 */
	public HookParams(Object... theParams) {
		for (Object next : theParams) {
			add(next);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void add(T theNext) {
		Class<T> nextClass = (Class<T>) theNext.getClass();
		add(nextClass, theNext);
	}

	public <T> HookParams add(Class<T> theType, T theParam) {
		myParams.put(theType, theParam);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> theParamType, int theIndex) {
		List<T> objects = (List<T>) myParams.get(theParamType);
		T retVal = null;
		if (objects.size() > theIndex) {
			retVal = objects.get(theIndex);
		}
		return retVal;
	}

	/**
	 * Multivalued parameters will be returned twice in this list
	 */
	public List<String> getTypesAsSimpleName() {
		return myParams.values().stream().map(t -> t.getClass().getSimpleName()).collect(Collectors.toList());
	}

	public Collection<Object> values() {
		return Collections.unmodifiableCollection(myParams.values());
	}
}
