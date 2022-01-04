package ca.uhn.fhir.rest.param.binder;

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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

import java.lang.reflect.Constructor;
import java.util.List;

class BaseBinder<T> {
	private List<Class<? extends IQueryParameterType>> myCompositeTypes;
	private Constructor<? extends T> myConstructor;
	private final Class<? extends T> myType;

	public BaseBinder(Class<? extends T> theType, List<Class<? extends IQueryParameterType>> theCompositeTypes) {
		myType = theType;
		myCompositeTypes = theCompositeTypes;
		
		
		if (myType.equals(CompositeParam.class)) {
			if (myCompositeTypes.size() != 2) {
				throw new ConfigurationException(Msg.code(1959) + "Search parameter of type " + myType.getName() + " must have 2 composite types declared in parameter annotation, found " + theCompositeTypes.size());
			}
		}
		
		try {
			Class<?>[] types = new Class<?>[myCompositeTypes.size()];
			for (int i = 0; i < myCompositeTypes.size(); i++) {
				types[i] = Class.class;
			}
			myConstructor = myType.getConstructor(types);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException(Msg.code(1960) + "Query parameter type " + theType.getName() + " has no constructor with types " + theCompositeTypes);
		}
	}

	public T newInstance() {
		try {
			final Object[] args = new Object[myCompositeTypes.size()];
			for (int i = 0; i < myCompositeTypes.size();i++) {
				args[i] = myCompositeTypes.get(i);//.newInstance();
			}
			
			T dt = myConstructor.newInstance(args);
			return dt;
		} catch (final Exception e) {
			throw new InternalErrorException(Msg.code(1961) + e);
		}
	}

}
