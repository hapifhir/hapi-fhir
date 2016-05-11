package ca.uhn.fhir.util;

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

import java.lang.reflect.Method;

import ca.uhn.fhir.util.reflection.IBeanUtils;

public class BeanUtils {

	private static IBeanUtils beanUtils;

	private static IBeanUtils getBeanUtils() {
		if (beanUtils == null) {
			try {
				beanUtils = (IBeanUtils) Class.forName("ca.uhn.fhir.util.reflection.JavaBeansBeanUtil").newInstance();
			} catch (Exception e) {
				try {
					beanUtils = (IBeanUtils) Class.forName("ca.uhn.fhir.util.reflection.JavaReflectBeanUtil")
							.newInstance();
				} catch (Exception e1) {
					throw new RuntimeException("Could not resolve BeanUtil implementation");
				}
			}
		}
		return beanUtils;
	}

	public static Method findAccessor(Class<?> theClassToIntrospect, Class<?> theTargetReturnType, String thePropertyName)
			throws NoSuchFieldException {
		return getBeanUtils().findAccessor(theClassToIntrospect, theTargetReturnType, thePropertyName);
	}

	public static Method findMutator(Class<?> theClassToIntrospect, Class<?> theTargetReturnType, String thePropertyName)
			throws NoSuchFieldException {
		return getBeanUtils().findMutator(theClassToIntrospect, theTargetReturnType, thePropertyName);
	}	
}
