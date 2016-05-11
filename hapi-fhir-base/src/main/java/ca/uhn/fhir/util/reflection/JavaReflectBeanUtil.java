package ca.uhn.fhir.util.reflection;

import java.lang.reflect.Method;

import org.apache.commons.lang3.text.WordUtils;

import ca.uhn.fhir.context.ConfigurationException;

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

public class JavaReflectBeanUtil implements IBeanUtils {

	@Override
	public Method findAccessor(Class<?> theClassToIntrospect, Class<?> theTargetReturnType, String thePropertyName)
			throws NoSuchFieldException {
		String methodName = "get" + WordUtils.capitalize(thePropertyName);
		try {
			Method method = theClassToIntrospect.getMethod(methodName);
			if (theTargetReturnType.isAssignableFrom(method.getReturnType())) {
				return method;
			}
		} catch (NoSuchMethodException e) {
			// fall through
		} catch (SecurityException e) {
			throw new ConfigurationException("Failed to scan class '" + theClassToIntrospect + "' because of a security exception", e);
		}
		throw new NoSuchFieldException(theClassToIntrospect + " has no accessor for field " + thePropertyName);
	}

	@Override
	public Method findMutator(Class<?> theClassToIntrospect, Class<?> theTargetArgumentType, String thePropertyName)
			throws NoSuchFieldException {
		String methodName = "set" + WordUtils.capitalize(thePropertyName);
		try {
			return theClassToIntrospect.getMethod(methodName, theTargetArgumentType);
		} catch (NoSuchMethodException e) {
			//fall through
		} catch (SecurityException e) {
			throw new ConfigurationException("Failed to scan class '" + theClassToIntrospect + "' because of a security exception", e);
		}
		throw new NoSuchFieldException(theClassToIntrospect + " has an mutator for field " + thePropertyName + " but it does not return type " + theTargetArgumentType);
		
	}

}
