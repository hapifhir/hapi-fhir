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

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class ClientInvocationHandler extends BaseClient implements InvocationHandler {

	private final Map<Method, BaseMethodBinding<?>> myBindings = new HashMap<Method, BaseMethodBinding<?>>();

	private final Map<Method, ILambda> myMethodToLambda = new HashMap<Method, ILambda>();

	private final Map<Method, Object> myMethodToReturnValue = new HashMap<Method, Object>();

	public ClientInvocationHandler(HttpClient theClient, FhirContext theContext, String theUrlBase, Class<? extends IRestfulClient> theClientType) {
		super(theClient, theUrlBase);

		try {
			myMethodToReturnValue.put(theClientType.getMethod("getFhirContext"), theContext);
			myMethodToReturnValue.put(theClientType.getMethod("getHttpClient"), theClient);
			myMethodToReturnValue.put(theClientType.getMethod("getServerBase"), theUrlBase);

			myMethodToLambda.put(theClientType.getMethod("setEncoding", EncodingEnum.class), new SetEncodingLambda());
			myMethodToLambda.put(theClientType.getMethod("setPrettyPrint", boolean.class), new SetPrettyPrintLambda());
			myMethodToLambda.put(theClientType.getMethod("registerInterceptor", IClientInterceptor.class), new RegisterInterceptorLambda());
			myMethodToLambda.put(theClientType.getMethod("unregisterInterceptor", IClientInterceptor.class), new UnregisterInterceptorLambda());

		} catch (NoSuchMethodException e) {
			throw new ConfigurationException("Failed to find methods on client. This is a HAPI bug!", e);
		} catch (SecurityException e) {
			throw new ConfigurationException("Failed to find methods on client. This is a HAPI bug!", e);
		}
	}

	public void addBinding(Method theMethod, BaseMethodBinding<?> theBinding) {
		myBindings.put(theMethod, theBinding);
	}

	@Override
	public Object invoke(Object theProxy, Method theMethod, Object[] theArgs) throws Throwable {
		Object directRetVal = myMethodToReturnValue.get(theMethod);
		if (directRetVal != null) {
			return directRetVal;
		}

		BaseMethodBinding<?> binding = myBindings.get(theMethod);
		if (binding != null) {
			BaseHttpClientInvocation clientInvocation = binding.invokeClient(theArgs);
			return invokeClient(binding, clientInvocation);
		}

		ILambda lambda = myMethodToLambda.get(theMethod);
		if (lambda != null) {
			return lambda.handle(theArgs);
		}

		throw new UnsupportedOperationException("The method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getSimpleName() + " has no handler. Did you forget to annotate it with a RESTful method annotation?");
	}

	private interface ILambda {
		Object handle(Object[] theArgs);
	}

	private class SetEncodingLambda implements ILambda {
		@Override
		public Object handle(Object[] theArgs) {
			EncodingEnum encoding = (EncodingEnum) theArgs[0];
			setEncoding(encoding);
			return null;
		}
	}

	private class SetPrettyPrintLambda implements ILambda {
		@Override
		public Object handle(Object[] theArgs) {
			Boolean prettyPrint = (Boolean) theArgs[0];
			setPrettyPrint(prettyPrint);
			return null;
		}
	}
	private class UnregisterInterceptorLambda implements ILambda {
		@Override
		public Object handle(Object[] theArgs) {
			IClientInterceptor interceptor = (IClientInterceptor) theArgs[0];
			unregisterInterceptor(interceptor);
			return null;
		}
	}

	private class RegisterInterceptorLambda implements ILambda {
		@Override
		public Object handle(Object[] theArgs) {
			IClientInterceptor interceptor = (IClientInterceptor) theArgs[0];
			registerInterceptor(interceptor);
			return null;
		}
	}

}
