package ca.uhn.fhir.rest.client;

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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.method.BaseMethodBinding;

public class ClientInvocationHandler extends BaseClient implements InvocationHandler {

	private final Map<Method, BaseMethodBinding> myBindings = new HashMap<Method, BaseMethodBinding>();
	private final Map<Method, Object> myMethodToReturnValue = new HashMap<Method, Object>();

	public ClientInvocationHandler(HttpClient theClient, FhirContext theContext, String theUrlBase, Class<? extends IRestfulClient> theClientType) {
		super(theClient, theUrlBase);
		
		try {
			myMethodToReturnValue.put(theClientType.getMethod("getFhirContext"), theContext);
			myMethodToReturnValue.put(theClientType.getMethod("getHttpClient"), theClient);
			myMethodToReturnValue.put(theClientType.getMethod("getServerBase"), theUrlBase);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("Failed to find methods on client. This is a HAPI bug!", e);
		} catch (SecurityException e) {
			throw new ConfigurationException("Failed to find methods on client. This is a HAPI bug!", e);
		}
	}

	public void addBinding(Method theMethod, BaseMethodBinding theBinding) {
		myBindings.put(theMethod, theBinding);
	}

	@Override
	public Object invoke(Object theProxy, Method theMethod, Object[] theArgs) throws Throwable {
		Object directRetVal = myMethodToReturnValue.get(theMethod);
		if (directRetVal != null) {
			return directRetVal;
		}

		BaseMethodBinding binding = myBindings.get(theMethod);
		BaseClientInvocation clientInvocation = binding.invokeClient(theArgs);
		
		return invokeClient(binding, clientInvocation);
	}



}
