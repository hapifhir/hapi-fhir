/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import org.apache.commons.lang3.Validate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyUtil {
	private ProxyUtil() {}

	/**
	 * Wrap theInstance in a Proxy that synchronizes every method.
	 *
	 * @param theClass the target interface
	 * @param theInstance the instance to wrap
	 * @return a Proxy implementing theClass interface that syncronizes every call on theInstance
	 * @param <T> the interface type
	 */
	public static <T> T synchronizedProxy(Class<T> theClass, T theInstance) {
		Validate.isTrue(theClass.isInterface(), "%s is not an interface", theClass);
		InvocationHandler handler = new SynchronizedHandler(theInstance);
		Object object = Proxy.newProxyInstance(theClass.getClassLoader(), new Class<?>[] {theClass}, handler);
		return theClass.cast(object);
	}

	/**
	 * Simple handler that first synchronizes on the delegate
	 */
	static class SynchronizedHandler implements InvocationHandler {
		private final Object theDelegate;

		SynchronizedHandler(Object theDelegate) {
			this.theDelegate = theDelegate;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			synchronized (theDelegate) {
				return method.invoke(theDelegate, args);
			}
		}
	}
}
