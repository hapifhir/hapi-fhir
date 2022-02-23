package ca.uhn.fhir.rest.client.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.api.RequestTypeEnum;

public interface IRestfulClientFactory {

	/**
	 * Default value for {@link #getConnectTimeout()}
	 */
	public static final int DEFAULT_CONNECT_TIMEOUT = 10000;

	/**
	 * Default value for {@link #getConnectionRequestTimeout()}
	 */
	public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 10000;

	/**
	 * Default value for {@link #getServerValidationModeEnum()}
	 */
	public static final ServerValidationModeEnum DEFAULT_SERVER_VALIDATION_MODE = ServerValidationModeEnum.ONCE;

	/**
	 * Default value for {@link #getSocketTimeout()}
	 */
	public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
	
	/**
	 * Default value for {@link #getPoolMaxTotal() ()}
	 */
	public static final int DEFAULT_POOL_MAX = 20;
	
	/**
	 * Default value for {@link #getPoolMaxPerRoute() }
	 */
	public static final int DEFAULT_POOL_MAX_PER_ROUTE = DEFAULT_POOL_MAX;
	
	/**
	 * Gets the connection request timeout, in milliseconds. This is the amount of time that the HTTPClient connection
	 * pool may wait for an available connection before failing. This setting typically does not need to be adjusted.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_CONNECTION_REQUEST_TIMEOUT}
	 * </p>
	 */
	int getConnectionRequestTimeout();
	
	/**
	 * Gets the connect timeout, in milliseconds. This is the amount of time that the initial connection attempt network
	 * operation may block without failing.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_CONNECT_TIMEOUT}
	 * </p>
	 */
	int getConnectTimeout();

	/**
	 * Returns the HTTP client instance. This method will not return null.
	 * @param theUrl
	 *            The complete FHIR url to which the http request will be sent
	 * @param theIfNoneExistParams
	 *            The params for header "If-None-Exist" as a hashmap
	 * @param theIfNoneExistString
	 *            The param for header "If-None-Exist" as a string
	 * @param theRequestType
	 *            the type of HTTP request (GET, DELETE, ..) 
	 * @param theHeaders
	 *            the headers to be sent together with the http request
	 * @return the HTTP client instance
	 */
	IHttpClient getHttpClient(StringBuilder theUrl, Map<String, List<String>> theIfNoneExistParams, String theIfNoneExistString, RequestTypeEnum theRequestType, List<Header> theHeaders);

	/**
	 * @deprecated Use {@link #getServerValidationMode()} instead (this method is a synonym for that method, but this method is poorly named and will be removed at some point)
	 */
	@Deprecated
	ServerValidationModeEnum getServerValidationModeEnum();

	/**
	 * Gets the server validation mode for any clients created from this factory. Server 
	 * validation involves the client requesting the server's conformance statement
	 * to determine whether the server is appropriate for the given client. 
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_SERVER_VALIDATION_MODE}
	 * </p>
	 * 
	 * @since 1.0
	 */
	ServerValidationModeEnum getServerValidationMode();

	/**
	 * Gets the socket timeout, in milliseconds. This is the SO_TIMEOUT time, which is the amount of time that a
	 * read/write network operation may block without failing.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_SOCKET_TIMEOUT}
	 * </p>
	 */
	int getSocketTimeout();

	/**
	 * Gets the maximum number of connections allowed in the pool.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_POOL_MAX}
	 * </p>
	 */
	int getPoolMaxTotal();

	/**
	 * Gets the maximum number of connections per route allowed in the pool.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_POOL_MAX_PER_ROUTE}
	 * </p>
	 */
	int getPoolMaxPerRoute();
	
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
	<T extends IRestfulClient> T newClient(Class<T> theClientType, String theServerBase);

	/**
	 * Instantiates a new generic client instance
	 * 
	 * @param theServerBase
	 *            The URL of the base for the restful FHIR server to connect to
	 * @return A newly created client
	 */
	IGenericClient newGenericClient(String theServerBase);

	/**
	 * Sets the connection request timeout, in milliseconds. This is the amount of time that the HTTPClient connection
	 * pool may wait for an available connection before failing. This setting typically does not need to be adjusted.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_CONNECTION_REQUEST_TIMEOUT}
	 * </p>
	 */
	void setConnectionRequestTimeout(int theConnectionRequestTimeout);

	/**
	 * Sets the connect timeout, in milliseconds. This is the amount of time that the initial connection attempt network
	 * operation may block without failing.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_CONNECT_TIMEOUT}
	 * </p>
	 */
	void setConnectTimeout(int theConnectTimeout);

	/**
	 * Sets the Apache HTTP client instance to be used by any new restful clients created by this factory. If set to
	 * <code>null</code>, a new HTTP client with default settings will be created.
	 * 
	 * @param theHttpClient
	 *            An HTTP client instance to use, or <code>null</code>
	 */
	<T> void setHttpClient(T theHttpClient);

	/**
	 * Sets the HTTP proxy to use for outgoing connections
	 * 
	 * @param theHost
	 *            The host (or null to disable proxying, as is the default)
	 * @param thePort
	 *            The port (or null to disable proxying, as is the default)
	 */
	void setProxy(String theHost, Integer thePort);

	/**
	 * Sets the credentials to use to authenticate with the HTTP proxy,
	 * if one is defined. Set to null to use no authentication with the proxy.
	 * @param theUsername The username
	 * @param thePassword The password
	 */
	void setProxyCredentials(String theUsername, String thePassword);

	/**
	 * @deprecated Use {@link #setServerValidationMode(ServerValidationModeEnum)} instead. This method was incorrectly named.
	 */
	@Deprecated
	void setServerValidationModeEnum(ServerValidationModeEnum theServerValidationMode);

	/**
	 * Sets the server validation mode for any clients created from this factory. Server 
	 * validation involves the client requesting the server's conformance statement
	 * to determine whether the server is appropriate for the given client. 
	 * <p>
	 * This check is primarily to validate that the server supports an appropriate
	 * version of FHIR
	 * </p> 
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_SERVER_VALIDATION_MODE}
	 * </p>
	 * 
	 * @since 1.0
	 */
	void setServerValidationMode(ServerValidationModeEnum theServerValidationMode);

	/**
	 * Sets the socket timeout, in milliseconds. This is the SO_TIMEOUT time, which is the amount of time that a
	 * read/write network operation may block without failing.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_SOCKET_TIMEOUT}
	 * </p>
	 */
	void setSocketTimeout(int theSocketTimeout);

	/**
	 * Sets the maximum number of connections allowed in the pool.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_POOL_MAX}
	 * </p>
	 */
	void setPoolMaxTotal(int thePoolMaxTotal);

	/**
	 * Sets the maximum number of connections per route allowed in the pool.
	 * <p>
	 * The default value for this setting is defined by {@link #DEFAULT_POOL_MAX_PER_ROUTE}
	 * </p>
	 */
	void setPoolMaxPerRoute(int thePoolMaxPerRoute);
	
	void validateServerBase(String theServerBase, IHttpClient theHttpClient, IRestfulClient theClient);

	/**
	 * This method is internal to HAPI - It may change in future versions, use with caution.
	 */	
	void validateServerBaseIfConfiguredToDoSo(String theServerBase, IHttpClient theHttpClient, IRestfulClient theClient);

}
