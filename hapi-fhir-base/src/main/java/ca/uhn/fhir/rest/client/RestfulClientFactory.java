package ca.uhn.fhir.rest.client;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.FhirClientInappropriateForServerException;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.FhirTerser;

/**
 * Base class for a REST client factory implementation
 */
public abstract class RestfulClientFactory implements IRestfulClientFactory {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulClientFactory.class);
	private int myConnectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
	private int myConnectTimeout = DEFAULT_CONNECT_TIMEOUT;
	private FhirContext myContext;
	private Map<Class<? extends IRestfulClient>, ClientInvocationHandlerFactory> myInvocationHandlers = new HashMap<Class<? extends IRestfulClient>, ClientInvocationHandlerFactory>();
	private ServerValidationModeEnum myServerValidationMode = DEFAULT_SERVER_VALIDATION_MODE;
	private int mySocketTimeout = DEFAULT_SOCKET_TIMEOUT;
	private Set<String> myValidatedServerBaseUrls = Collections.synchronizedSet(new HashSet<String>());
	private String myProxyUsername;
	private String myProxyPassword;	
	private int myPoolMaxTotal = DEFAULT_POOL_MAX;
	private int myPoolMaxPerRoute = DEFAULT_POOL_MAX_PER_ROUTE;
	
	/**
	 * Constructor
	 */
	public RestfulClientFactory() {
	}

	/**
	 * Constructor
	 * 
	 * @param theFhirContext
	 *            The context
	 */
	public RestfulClientFactory(FhirContext theFhirContext) {
		myContext = theFhirContext;
	}

	@Override
	public int getConnectionRequestTimeout() {
		return myConnectionRequestTimeout;
	}

	@Override
	public int getConnectTimeout() {
		return myConnectTimeout;
	}

	/**
	 * Return the proxy username to authenticate with the HTTP proxy
	 * @param The proxy username
	 */	
	protected String getProxyUsername() {
		return myProxyUsername;
	}
	
	/**
	 * Return the proxy password to authenticate with the HTTP proxy
	 * @param The proxy password
	 */	
	protected String getProxyPassword() {
		return myProxyPassword;
	}

	@Override
	public void setProxyCredentials(String theUsername, String thePassword) {
		myProxyUsername=theUsername;
		myProxyPassword=thePassword;
	}
	
	@Override
	public ServerValidationModeEnum getServerValidationMode() {
		return myServerValidationMode;
	}

	@Override
	public int getSocketTimeout() {
		return mySocketTimeout;
	}

	@Override
	public int getPoolMaxTotal() {
		return myPoolMaxTotal;
	}

	@Override
	public int getPoolMaxPerRoute() {
		return myPoolMaxPerRoute;
	}

	@SuppressWarnings("unchecked")
	private <T extends IRestfulClient> T instantiateProxy(Class<T> theClientType, InvocationHandler theInvocationHandler) {
		T proxy = (T) Proxy.newProxyInstance(theClientType.getClassLoader(), new Class[] { theClientType }, theInvocationHandler);
		return proxy;
	}

	/**
	 * Instantiates a new client instance
	 * 
	 * @param theClientType
	 *            The client type, which is an interface type to be instantiated
	 * @param theServerBase
	 *            The URL of the base for the restful FHIR server to connect to
	 * @return A newly created client
	 * @throws ConfigurationException
	 *             If the interface type is not an interface
	 */
	@Override
	public synchronized <T extends IRestfulClient> T newClient(Class<T> theClientType, String theServerBase) {
		validateConfigured();
		
		if (!theClientType.isInterface()) {
			throw new ConfigurationException(theClientType.getCanonicalName() + " is not an interface");
		}
		
		ClientInvocationHandlerFactory invocationHandler = myInvocationHandlers.get(theClientType);
		if (invocationHandler == null) {
			IHttpClient httpClient = getHttpClient(theServerBase);
			invocationHandler = new ClientInvocationHandlerFactory(httpClient, myContext, theServerBase, theClientType);
			for (Method nextMethod : theClientType.getMethods()) {
				BaseMethodBinding<?> binding = BaseMethodBinding.bindMethod(nextMethod, myContext, null);
				invocationHandler.addBinding(nextMethod, binding);
			}
			myInvocationHandlers.put(theClientType, invocationHandler);
		}

		T proxy = instantiateProxy(theClientType, invocationHandler.newInvocationHandler(this));

		return proxy;
	}

	/**
	 * Called automatically before the first use of this factory to ensure that
	 * the configuration is sane. Subclasses may override, but should also call 
	 * <code>super.validateConfigured()</code>
	 */
	protected void validateConfigured() {
		if (getFhirContext() == null) {
			throw new IllegalStateException(getClass().getSimpleName() + " does not have FhirContext defined. This must be set via " + getClass().getSimpleName() + "#setFhirContext(FhirContext)");
		}
	}

	@Override
	public synchronized IGenericClient newGenericClient(String theServerBase) {
		validateConfigured();
		IHttpClient httpClient = getHttpClient(theServerBase);
		
		return new GenericClient(myContext, httpClient, theServerBase, this);
	}

	@Override
	public void validateServerBaseIfConfiguredToDoSo(String theServerBase, IHttpClient theHttpClient, BaseClient theClient) {
		String serverBase = normalizeBaseUrlForMap(theServerBase);

		switch (getServerValidationMode()) {
		case NEVER:
			break;
		case ONCE:
			if (!myValidatedServerBaseUrls.contains(serverBase)) {
				validateServerBase(serverBase, theHttpClient, theClient);
			}
			break;
		}

	}

	private String normalizeBaseUrlForMap(String theServerBase) {
		String serverBase = theServerBase;
		if (!serverBase.endsWith("/")) {
			serverBase = serverBase + "/";
		}
		return serverBase;
	}

	@Override
	public synchronized void setConnectionRequestTimeout(int theConnectionRequestTimeout) {
		myConnectionRequestTimeout = theConnectionRequestTimeout;
		resetHttpClient();
	}

	@Override
	public synchronized void setConnectTimeout(int theConnectTimeout) {
		myConnectTimeout = theConnectTimeout;
		resetHttpClient();
	}

	/**
	 * Sets the context associated with this client factory. Must not be called more than once.
	 */
	public void setFhirContext(FhirContext theContext) {
		if (myContext != null && myContext != theContext) {
			throw new IllegalStateException("RestfulClientFactory instance is already associated with one FhirContext. RestfulClientFactory instances can not be shared.");
		}
		myContext = theContext;
	}
	
	/**
	 * Return the fhir context
	 * @return the fhir context 
	 */
	public FhirContext getFhirContext() {
		return myContext;
	}

	@Override
	public void setServerValidationMode(ServerValidationModeEnum theServerValidationMode) {
		Validate.notNull(theServerValidationMode, "theServerValidationMode may not be null");
		myServerValidationMode = theServerValidationMode;
	}

	@Override
	public synchronized void setSocketTimeout(int theSocketTimeout) {
		mySocketTimeout = theSocketTimeout;
		resetHttpClient();
	}

	@Override
	public synchronized void setPoolMaxTotal(int thePoolMaxTotal) {
		myPoolMaxTotal = thePoolMaxTotal;
		resetHttpClient();
	}

	@Override
	public synchronized void setPoolMaxPerRoute(int thePoolMaxPerRoute) {
		myPoolMaxPerRoute = thePoolMaxPerRoute;
		resetHttpClient();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validateServerBase(String theServerBase, IHttpClient theHttpClient, BaseClient theClient) {
		GenericClient client = new GenericClient(myContext, theHttpClient, theServerBase, this);
		client.setEncoding(theClient.getEncoding());
		for (IClientInterceptor interceptor : theClient.getInterceptors()) {
			client.registerInterceptor(interceptor);
		}
		client.setDontValidateConformance(true);
		
		IBaseResource conformance;
		try {
			String capabilityStatementResourceName = "CapabilityStatement";
			if (myContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
				capabilityStatementResourceName = "Conformance";
			}
			
			@SuppressWarnings("rawtypes")
			Class implementingClass;
			try {
				implementingClass = myContext.getResourceDefinition(capabilityStatementResourceName).getImplementingClass();
			} catch (DataFormatException e) {
				if (!myContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
					capabilityStatementResourceName = "Conformance";
					implementingClass = myContext.getResourceDefinition(capabilityStatementResourceName).getImplementingClass();
				} else {
					throw e;
				}
			}
			try {
				conformance = (IBaseResource) client.fetchConformance().ofType(implementingClass).execute();
			} catch (FhirClientConnectionException e) {
				if (!myContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3) && e.getCause() instanceof DataFormatException) {
					capabilityStatementResourceName = "Conformance";
					implementingClass = myContext.getResourceDefinition(capabilityStatementResourceName).getImplementingClass();
					conformance = (IBaseResource) client.fetchConformance().ofType(implementingClass).execute();
				} else {
					throw e;
				}
			}
		} catch (FhirClientConnectionException e) {
			String msg = myContext.getLocalizer().getMessage(RestfulClientFactory.class, "failedToRetrieveConformance", theServerBase + Constants.URL_TOKEN_METADATA);
			throw new FhirClientConnectionException(msg, e);
		}

		FhirTerser t = myContext.newTerser();
		String serverFhirVersionString = null;
		Object value = t.getSingleValueOrNull(conformance, "fhirVersion");
		if (value instanceof IPrimitiveType) {
			serverFhirVersionString = IPrimitiveType.class.cast(value).getValueAsString();
		}
		FhirVersionEnum serverFhirVersionEnum = null;
		if (StringUtils.isBlank(serverFhirVersionString)) {
			// we'll be lenient and accept this
		} else {
			//FIXME null access on serverFhirVersionString
			if (serverFhirVersionString.startsWith("0.80") || serverFhirVersionString.startsWith("0.0.8")) {
				serverFhirVersionEnum = FhirVersionEnum.DSTU1;
			} else if (serverFhirVersionString.startsWith("0.4")) {
				serverFhirVersionEnum = FhirVersionEnum.DSTU2;
			} else if (serverFhirVersionString.startsWith("0.5")) {
				serverFhirVersionEnum = FhirVersionEnum.DSTU2;
			} else {
				// we'll be lenient and accept this
				ourLog.debug("Server conformance statement indicates unknown FHIR version: {}", serverFhirVersionString);
			}
		}

		if (serverFhirVersionEnum != null) {
			FhirVersionEnum contextFhirVersion = myContext.getVersion().getVersion();
			if (!contextFhirVersion.isEquivalentTo(serverFhirVersionEnum)) {
				throw new FhirClientInappropriateForServerException(myContext.getLocalizer().getMessage(RestfulClientFactory.class, "wrongVersionInConformance", theServerBase + Constants.URL_TOKEN_METADATA, serverFhirVersionString, serverFhirVersionEnum, contextFhirVersion));
			}
		}
		
		myValidatedServerBaseUrls.add(normalizeBaseUrlForMap(theServerBase));

	}

	@Deprecated //override deprecated method
	@Override
	public ServerValidationModeEnum getServerValidationModeEnum() {
		return getServerValidationMode();
	}

	@Deprecated //override deprecated method
	@Override
	public void setServerValidationModeEnum(ServerValidationModeEnum theServerValidationMode) {
		setServerValidationMode(theServerValidationMode);
	}
	
	/**
	 * Get the http client for the given server base
	 * @param theServerBase the server base
	 * @return the http client
	 */
	protected abstract IHttpClient getHttpClient(String theServerBase);
	
	/**
	 * Reset the http client. This method is used when parameters have been set and a
	 * new http client needs to be created
	 */
	protected abstract void resetHttpClient();

}
