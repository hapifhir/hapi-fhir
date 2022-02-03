package ca.uhn.fhir.rest.client.impl;

/*
 * #%L
 * HAPI FHIR - Client Framework
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

import ca.uhn.fhir.i18n.Msg;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.impl.ClientInvocationHandlerFactory.ILambda;
import ca.uhn.fhir.rest.client.method.BaseMethodBinding;

public class ClientInvocationHandler extends BaseClient implements InvocationHandler {

	private final Map<Method, BaseMethodBinding<?>> myBindings;
	private final Map<Method, Object> myMethodToReturnValue;
	private FhirContext myContext;
	private Map<Method, ILambda> myMethodToLambda;

	public ClientInvocationHandler(IHttpClient theClient, FhirContext theContext, String theUrlBase, Map<Method, Object> theMethodToReturnValue, Map<Method, BaseMethodBinding<?>> theBindings, Map<Method, ILambda> theMethodToLambda, RestfulClientFactory theFactory) {
		super(theClient, theUrlBase, theFactory);

		myContext = theContext;
		myMethodToReturnValue = theMethodToReturnValue;
		myBindings = theBindings;
		myMethodToLambda = theMethodToLambda;
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
			return invokeClient(myContext, binding, clientInvocation);
		}

		ILambda lambda = myMethodToLambda.get(theMethod);
		if (lambda != null) {
			return lambda.handle(this, theArgs);
		}

		throw new UnsupportedOperationException(Msg.code(1403) + "The method '" + theMethod.getName() + "' in type " + theMethod.getDeclaringClass().getSimpleName() + " has no handler. Did you forget to annotate it with a RESTful method annotation?");
	}

	@Override
	public FhirContext getFhirContext() {
		return myContext;
	}

}
