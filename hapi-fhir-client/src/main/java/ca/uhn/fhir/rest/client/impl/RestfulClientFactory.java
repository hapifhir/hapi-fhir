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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import ca.uhn.fhir.i18n.Msg;
import java.lang.reflect.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.*;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.FhirClientInappropriateForServerException;
import ca.uhn.fhir.rest.client.method.BaseMethodBinding;
import ca.uhn.fhir.util.FhirTerser;

import javax.annotation.concurrent.GuardedBy;

/**
 * Base class for a REST client factory implementation
 */
public abstract class RestfulClientFactory implements IRestfulClientFactory {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestfulClientFactory.class);

	private final Set<String> myValidatedServerBaseUrls = Collections.synchronizedSet(new HashSet<>());
	private int myConnectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
	private int myConnectTimeout = DEFAULT_CONNECT_TIMEOUT;
	private FhirContext myContext;
	private final Map<Class<? extends IRestfulClient>, ClientInvocationHandlerFactory> myInvocationHandlers = new HashMap<>();
	private ServerValidationModeEnum myServerValidationMode = DEFAULT_SERVER_VALIDATION_MODE;
	private int mySocketTimeout = DEFAULT_SOCKET_TIMEOUT;
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
	 *           The context
	 */
	public RestfulClientFactory(FhirContext theFhirContext) {
		myContext = theFhirContext;
	}

	@Override
	public synchronized int getConnectionRequestTimeout() {
		return myConnectionRequestTimeout;
	}

	@Override
	public synchronized int getConnectTimeout() {
		return myConnectTimeout;
	}

	/**
	 * Return the proxy username to authenticate with the HTTP proxy
	 */
	protected synchronized String getProxyUsername() {
		return myProxyUsername;
	}

	/**
	 * Return the proxy password to authenticate with the HTTP proxy
	 */
	protected synchronized String getProxyPassword() {
		return myProxyPassword;
	}

	@Override
	public synchronized void setProxyCredentials(String theUsername, String thePassword) {
		myProxyUsername = theUsername;
		myProxyPassword = thePassword;
	}

	@Override
	public synchronized ServerValidationModeEnum getServerValidationMode() {
		return myServerValidationMode;
	}

	@Override
	public synchronized int getSocketTimeout() {
		return mySocketTimeout;
	}

	@Override
	public synchronized int getPoolMaxTotal() {
		return myPoolMaxTotal;
	}

	@Override
	public synchronized int getPoolMaxPerRoute() {
		return myPoolMaxPerRoute;
	}

	@SuppressWarnings("unchecked")
	private <T extends IRestfulClient> T instantiateProxy(Class<T> theClientType, InvocationHandler theInvocationHandler) {
		return (T) Proxy.newProxyInstance(theClientType.getClassLoader(), new Class[] { theClientType }, theInvocationHandler);
	}

	/**
	 * Instantiates a new client instance
	 * 
	 * @param theClientType
	 *           The client type, which is an interface type to be instantiated
	 * @param theServerBase
	 *           The URL of the base for the restful FHIR server to connect to
	 * @return A newly created client
	 * @throws ConfigurationException
	 *            If the interface type is not an interface
	 */
	@Override
	public synchronized <T extends IRestfulClient> T newClient(Class<T> theClientType, String theServerBase) {
		validateConfigured();

		if (!theClientType.isInterface()) {
			throw new ConfigurationException(Msg.code(1354) + theClientType.getCanonicalName() + " is not an interface");
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

		return instantiateProxy(theClientType, invocationHandler.newInvocationHandler(this));
	}

	/**
	 * Called automatically before the first use of this factory to ensure that
	 * the configuration is sane. Subclasses may override, but should also call
	 * <code>super.validateConfigured()</code>
	 */
	protected void validateConfigured() {
		if (getFhirContext() == null) {
			throw new IllegalStateException(Msg.code(1355) + getClass().getSimpleName() + " does not have FhirContext defined. This must be set via " + getClass().getSimpleName() + "#setFhirContext(FhirContext)");
		}
	}

	@Override
	public synchronized IGenericClient newGenericClient(String theServerBase) {
		validateConfigured();
		IHttpClient httpClient = getHttpClient(theServerBase);

		return new GenericClient(myContext, httpClient, theServerBase, this);
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
			throw new IllegalStateException(Msg.code(1356) + "RestfulClientFactory instance is already associated with one FhirContext. RestfulClientFactory instances can not be shared.");
		}
		myContext = theContext;
	}

	/**
	 * Return the fhir context
	 * 
	 * @return the fhir context
	 */
	public FhirContext getFhirContext() {
		return myContext;
	}

	@Override
	public synchronized void setServerValidationMode(ServerValidationModeEnum theServerValidationMode) {
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

	@Deprecated // override deprecated method
	@Override
	public synchronized ServerValidationModeEnum getServerValidationModeEnum() {
		return getServerValidationMode();
	}

	@Deprecated // override deprecated method
	@Override
	public synchronized void setServerValidationModeEnum(ServerValidationModeEnum theServerValidationMode) {
		setServerValidationMode(theServerValidationMode);
	}

	@Override
	public void validateServerBaseIfConfiguredToDoSo(String theServerBase, IHttpClient theHttpClient, IRestfulClient theClient) {
		String serverBase = normalizeBaseUrlForMap(theServerBase);

		switch (getServerValidationMode()) {
			case NEVER:
				break;

			case ONCE:
				if (myValidatedServerBaseUrls.contains(serverBase)) {
					break;
				}

				synchronized (myValidatedServerBaseUrls) {
					if (!myValidatedServerBaseUrls.contains(serverBase)) {
						myValidatedServerBaseUrls.add(serverBase);
						validateServerBase(serverBase, theHttpClient, theClient);
					}
				}
				break;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void validateServerBase(String theServerBase, IHttpClient theHttpClient, IRestfulClient theClient) {
		GenericClient client = new GenericClient(myContext, theHttpClient, theServerBase, this);

		client.setInterceptorService(theClient.getInterceptorService());
		client.setEncoding(theClient.getEncoding());
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
					capabilityStatementResourceName = "CapabilityStatement";
					implementingClass = myContext.getResourceDefinition(capabilityStatementResourceName).getImplementingClass();
					conformance = (IBaseResource) client.fetchConformance().ofType(implementingClass).execute();
				} else {
					throw e;
				}
			}
		} catch (FhirClientConnectionException e) {
			String msg = myContext.getLocalizer().getMessage(RestfulClientFactory.class, "failedToRetrieveConformance", theServerBase + Constants.URL_TOKEN_METADATA);
			throw new FhirClientConnectionException(Msg.code(1357) + msg, e);
		}

		FhirTerser t = myContext.newTerser();
		String serverFhirVersionString = null;
		Object value = t.getSingleValueOrNull(conformance, "fhirVersion");
		if (value instanceof IPrimitiveType) {
			serverFhirVersionString = ((IPrimitiveType<?>) value).getValueAsString();
		}
		FhirVersionEnum serverFhirVersionEnum = null;
		if (StringUtils.isBlank(serverFhirVersionString)) {
			// we'll be lenient and accept this
			ourLog.debug("Server conformance statement does not indicate the FHIR version");
		} else {
			if (serverFhirVersionString.equals(FhirVersionEnum.DSTU2.getFhirVersionString())) {
				serverFhirVersionEnum = FhirVersionEnum.DSTU2;
			} else if (serverFhirVersionString.equals(FhirVersionEnum.DSTU2_1.getFhirVersionString())) {
				serverFhirVersionEnum = FhirVersionEnum.DSTU2_1;
			} else if (serverFhirVersionString.equals(FhirVersionEnum.DSTU3.getFhirVersionString())) {
				serverFhirVersionEnum = FhirVersionEnum.DSTU3;
			} else if (serverFhirVersionString.equals(FhirVersionEnum.R4.getFhirVersionString())) {
				serverFhirVersionEnum = FhirVersionEnum.R4;
			} else {
				// we'll be lenient and accept this
				ourLog.debug("Server conformance statement indicates unknown FHIR version: {}", serverFhirVersionString);
			}
		}

		if (serverFhirVersionEnum != null) {
			FhirVersionEnum contextFhirVersion = myContext.getVersion().getVersion();
			if (!contextFhirVersion.isEquivalentTo(serverFhirVersionEnum)) {
				throw new FhirClientInappropriateForServerException(Msg.code(1358) + myContext.getLocalizer().getMessage(RestfulClientFactory.class, "wrongVersionInConformance",
						theServerBase + Constants.URL_TOKEN_METADATA, serverFhirVersionString, serverFhirVersionEnum, contextFhirVersion));
			}
		}

		String serverBase = normalizeBaseUrlForMap(theServerBase);
		if (myValidatedServerBaseUrls.contains(serverBase)) {
			return;
		}

		synchronized (myValidatedServerBaseUrls) {
			myValidatedServerBaseUrls.add(serverBase);
		}
	}


	/**
	 * Get the http client for the given server base
	 * 
	 * @param theServerBase
	 *           the server base
	 * @return the http client
	 */
	protected abstract IHttpClient getHttpClient(String theServerBase);

	/**
	 * Reset the http client. This method is used when parameters have been set and a
	 * new http client needs to be created
	 */
	protected abstract void resetHttpClient();

}
