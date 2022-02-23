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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.method.BaseMethodBinding;

public class ClientInvocationHandlerFactory {

	private final Map<Method, BaseMethodBinding<?>> myBindings = new HashMap<Method, BaseMethodBinding<?>>();
	private final IHttpClient myClient;
	private final FhirContext myContext;
	private final Map<Method, ILambda> myMethodToLambda = new HashMap<Method, ILambda>();
	private final Map<Method, Object> myMethodToReturnValue = new HashMap<Method, Object>();
	private final String myUrlBase;

	public ClientInvocationHandlerFactory(IHttpClient theClient, FhirContext theContext, String theUrlBase, Class<? extends IRestfulClient> theClientType) {
		myClient = theClient;
		myUrlBase = theUrlBase;
		myContext = theContext;

		try {
			myMethodToReturnValue.put(theClientType.getMethod("getFhirContext"), theContext);
			myMethodToReturnValue.put(theClientType.getMethod("getHttpClient"), theClient);
			myMethodToReturnValue.put(theClientType.getMethod("getServerBase"), theUrlBase);

			myMethodToLambda.put(theClientType.getMethod("setEncoding", EncodingEnum.class), new SetEncodingLambda());
			myMethodToLambda.put(theClientType.getMethod("setPrettyPrint", Boolean.class), new SetPrettyPrintLambda());
			myMethodToLambda.put(theClientType.getMethod("registerInterceptor", Object.class), new RegisterInterceptorLambda());
			myMethodToLambda.put(theClientType.getMethod("unregisterInterceptor", Object.class), new UnregisterInterceptorLambda());
			myMethodToLambda.put(theClientType.getMethod("setSummary", SummaryEnum.class), new SetSummaryLambda());
			myMethodToLambda.put(theClientType.getMethod("fetchResourceFromUrl", Class.class, String.class), new FetchResourceFromUrlLambda());

		} catch (NoSuchMethodException e) {
			throw new ConfigurationException(Msg.code(1352) + "Failed to find methods on client. This is a HAPI bug!", e);
		} catch (SecurityException e) {
			throw new ConfigurationException(Msg.code(1353) + "Failed to find methods on client. This is a HAPI bug!", e);
		}
	}

	public void addBinding(Method theMethod, BaseMethodBinding<?> theBinding) {
		myBindings.put(theMethod, theBinding);
	}

	ClientInvocationHandler newInvocationHandler(RestfulClientFactory theRestfulClientFactory) {
		return new ClientInvocationHandler(myClient, myContext, myUrlBase, myMethodToReturnValue, myBindings, myMethodToLambda, theRestfulClientFactory);
	}

	public interface ILambda {
		Object handle(ClientInvocationHandler theTarget, Object[] theArgs);
	}

	class RegisterInterceptorLambda implements ILambda {
		@Override
		public Object handle(ClientInvocationHandler theTarget, Object[] theArgs) {
			Object interceptor = theArgs[0];
			theTarget.registerInterceptor(interceptor);
			return null;
		}
	}
	
	class FetchResourceFromUrlLambda implements ILambda {
		@Override
		public Object handle(ClientInvocationHandler theTarget, Object[] theArgs) {
			@SuppressWarnings("unchecked")
			Class<? extends IBaseResource> type = (Class<? extends IBaseResource>) theArgs[0];
			String url = (String) theArgs[1];
			
			return theTarget.fetchResourceFromUrl(type, url);
		}
	}
	
	class SetEncodingLambda implements ILambda {
		@Override
		public Object handle(ClientInvocationHandler theTarget, Object[] theArgs) {
			EncodingEnum encoding = (EncodingEnum) theArgs[0];
			theTarget.setEncoding(encoding);
			return null;
		}
	}

	class SetPrettyPrintLambda implements ILambda {
		@Override
		public Object handle(ClientInvocationHandler theTarget, Object[] theArgs) {
			Boolean prettyPrint = (Boolean) theArgs[0];
			theTarget.setPrettyPrint(prettyPrint);
			return null;
		}
	}

	class SetSummaryLambda implements ILambda {
		@Override
		public Object handle(ClientInvocationHandler theTarget, Object[] theArgs) {
			SummaryEnum encoding = (SummaryEnum) theArgs[0];
			theTarget.setSummary(encoding);
			return null;
		}
	}

	class UnregisterInterceptorLambda implements ILambda {
		@Override
		public Object handle(ClientInvocationHandler theTarget, Object[] theArgs) {
			Object interceptor = theArgs[0];
			theTarget.unregisterInterceptor(interceptor);
			return null;
		}
	}

}
