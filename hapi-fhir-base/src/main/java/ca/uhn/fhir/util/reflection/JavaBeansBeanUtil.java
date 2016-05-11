package ca.uhn.fhir.util.reflection;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class JavaBeansBeanUtil implements IBeanUtils {
	
	@Override
	public Method findAccessor(Class<?> theClassToIntrospect, Class<?> theTargetReturnType, String thePropertyName) throws NoSuchFieldException {
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(theClassToIntrospect);
		} catch (IntrospectionException e) {
			throw new NoSuchFieldException(e.getMessage());
		}
		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
			if (thePropertyName.equals(pd.getName())) {
				if (theTargetReturnType.isAssignableFrom(pd.getPropertyType())) {
				return pd.getReadMethod();
				}else {
					throw new NoSuchFieldException(theClassToIntrospect + " has an accessor for field " + thePropertyName + " but it does not return type " + theTargetReturnType);
				}
			}
		}
		throw new NoSuchFieldException(theClassToIntrospect + " has no accessor for field " + thePropertyName);
	}

	@Override
	public Method findMutator(Class<?> theClassToIntrospect, Class<?> theTargetReturnType, String thePropertyName) throws NoSuchFieldException {
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(theClassToIntrospect);
		} catch (IntrospectionException e) {
			throw new NoSuchFieldException(e.getMessage());
		}
		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
			if (thePropertyName.equals(pd.getName())) {
				if (theTargetReturnType.isAssignableFrom(pd.getPropertyType())) {
					return pd.getWriteMethod();
				}else {
					throw new NoSuchFieldException(theClassToIntrospect + " has an mutator for field " + thePropertyName + " but it does not return type " + theTargetReturnType);
				}
			}
		}
		throw new NoSuchFieldException(theClassToIntrospect + " has no mutator for field " + thePropertyName);
	}
}
