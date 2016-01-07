package ca.uhn.fhir.osgi.impl;

/*
 * #%L
 * HAPI FHIR - OSGi Bundle
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


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.osgi.FhirConfigurationException;
import ca.uhn.fhir.osgi.FhirProviderBundle;
import ca.uhn.fhir.osgi.FhirServer;

/**
 * Manage the dynamic registration of FHIR Servers and FHIR Providers.
 * Methods on this Spring Bean will be invoked from OSGi Reference
 * Listeners when OSGi services are published for these interfaces.
 *
 * @author Akana, Inc. Professional Services
 *
 */
public class FhirServerManager {

	private static Logger log = LoggerFactory.getLogger(FhirServerManager.class);
	private static final String FIRST_SERVER = "#first";
	
	private Map<String,FhirServer> registeredServers = new ConcurrentHashMap<String,FhirServer>();
	private Map<String,Collection<Collection<Object>>> serverProviders = new ConcurrentHashMap<String,Collection<Collection<Object>>>();
	private Collection<Collection<Object>> registeredProviders = Collections.synchronizedList(new ArrayList<Collection<Object>>());
	private Map<String,Collection<Collection<Object>>> pendingProviders = new ConcurrentHashMap<String,Collection<Collection<Object>>>();
	private boolean haveDefaultProviders = false;
	
	/**
	 * Register a new FHIR Server OSGi service.
	 * We need to track these services so we can find the correct 
	 * server to use when registering/unregistering providers.
	 * <p>
	 * The OSGi service definition of a FHIR Server should look like:
	 * <code><pre>
	 * &lt;osgi:service ref="<b><i>some.bean</i></b>" interface="ca.uhn.fhir.osgi.FhirServer">
	 *     &lt;osgi:service-properties>
	 *         &lt;entry key="name" value="<b><i>osgi-service-name</i></b>"/>
	 *         &lt;entry key="fhir.server.name" value="<b><i>fhir-server-name</i></b>"/>
	 *     &lt;/osgi:service-properties>	
	 * &lt;/osgi:service>
	 * </pre></code>
	 * The <b><i>fhir-server-name</i></b> parameter is also specified for all
	 * of the FHIR Providers that are to be dynamically registered with the
	 * named FHIR Server.
	 * 
	 * @param server OSGi service implementing the FhirService interface
	 * @param props the <service-properties> for that service
	 * 
	 * @throws FhirConfigurationException
	 */
	public void registerFhirServer (FhirServer server, Map<String,Object> props) throws FhirConfigurationException {
		if (server != null) {
			String serviceName = (String)props.get("name");
			if (null == serviceName) {
				serviceName = "<default>";
			}
			String serverName = (String)props.get(FhirServer.SVCPROP_SERVICE_NAME);
			if (serverName != null) {
				if (registeredServers.containsKey(serverName)) {
					throw new FhirConfigurationException("FHIR Server named ["+serverName+"] is already registered. These names must be unique.");
				}
				log.trace("Registering FHIR Server ["+serverName+"]. (OSGi service named ["+serviceName+"])");
				registeredServers.put(serverName, server);
				if (haveDefaultProviders && registeredServers.size() > 1) {
					throw new FhirConfigurationException("FHIR Providers are registered without a server name. Only one FHIR Server is allowed.");
				}
				Collection<Collection<Object>> providers = pendingProviders.get(serverName);
				if (providers != null) {
					log.trace("Registering FHIR providers waiting for this server to be registered.");
					pendingProviders.remove(serverName);
					for (Collection<Object> list : providers) {
						this.registerProviders(list, server, serverName);
					}
				}
				if (registeredServers.size() == 1) {
					providers = pendingProviders.get(FIRST_SERVER);
					if (providers != null) {
						log.trace("Registering FHIR providers waiting for the first/only server to be registered.");
						pendingProviders.remove(FIRST_SERVER);
						for (Collection<Object> list : providers) {
							this.registerProviders(list, server, serverName);
						}
					}
				}
			} else {
				throw new FhirConfigurationException("FHIR Server registered in OSGi is missing the required ["+FhirServer.SVCPROP_SERVICE_NAME+"] service-property");
			}
		}
	}
	
	/**
	 * This method will be called when a FHIR Server OSGi service
	 * is being removed from the container. This normally will only
	 * occur when its bundle is stopped because it is being removed
	 * or updated. 
	 * 
	 * @param server OSGi service implementing the FhirService interface
	 * @param props the <service-properties> for that service
	 * 
	 * @throws FhirConfigurationException
	 */
	public void unregisterFhirServer (FhirServer server, Map<String,Object> props) throws FhirConfigurationException {
		if (server != null) {
			String serverName = (String)props.get(FhirServer.SVCPROP_SERVICE_NAME);
			if (serverName != null) {
				FhirServer service = registeredServers.get(serverName);
				if (service != null) {
					log.trace("Unregistering FHIR Server ["+serverName+"]");
					service.unregisterOsgiProviders();
					registeredServers.remove(serverName);
					log.trace("Dequeue any FHIR providers waiting for this server");
					pendingProviders.remove(serverName);
					if (registeredServers.size() == 0) {
						log.trace("Dequeue any FHIR providers waiting for the first/only server");
						pendingProviders.remove(FIRST_SERVER);
					}
					Collection<Collection<Object>> providers = serverProviders.get(serverName);
					if (providers != null) {
						serverProviders.remove(serverName);
						registeredProviders.removeAll(providers);
					}
				}
			} else {
				throw new FhirConfigurationException("FHIR Server registered in OSGi is missing the required ["+FhirServer.SVCPROP_SERVICE_NAME+"] service-property");
			}
		}
	}
	
	/**
	 * Register a new FHIR Provider-Bundle OSGi service.
	 * 
	 * This could be a "plain" provider that is published with the 
	 * FhirProvider interface or it could be a resource provider that
	 * is published with either that same interface or the IResourceProvider
	 * interface.
	 * 
	 * (That check is not made here but is included as usage documentation)
	 *  
	 * <p>
	 * The OSGi service definition of a FHIR Provider would look like:
	 * <code><pre>
	 * &lt;osgi:service ref="<b><i>some.bean</i></b>" interface="ca.uhn.fhir.osgi.IResourceProvider">
	 *     &lt;osgi:service-properties>
	 *         &lt;entry key="name" value="<b><i>osgi-service-name</i></b>"/>
	 *         &lt;entry key="fhir.server.name" value="<b><i>fhir-server-name</i></b>"/>
	 *     &lt;/osgi:service-properties>	
	 * &lt;/osgi:service>
	 * </pre></code>
	 * The <b><i>fhir-server-name</i></b> parameter is the value assigned to the
	 * <code>fhir.server.name</code> service-property of one of the OSGi-published
	 * FHIR Servers.
	 * 
	 * @param server OSGi service implementing a FHIR provider interface
	 * @param props the <service-properties> for that service
	 * 
	 * @throws FhirConfigurationException
	 */
	public void registerFhirProviders (FhirProviderBundle bundle, Map<String,Object> props) throws FhirConfigurationException {
		if (bundle != null) {
			Collection<Object> providers = bundle.getProviders();
			if (providers != null && !providers.isEmpty()) {
				try {
					String serverName = (String)props.get(FhirServer.SVCPROP_SERVICE_NAME);
					String ourServerName = getServerName(serverName);
					String bundleName = (String)props.get("name");
					if (null == bundleName) {
						bundleName = "<default>";
					}
					log.trace("Register FHIR Provider Bundle ["+bundleName+"] on FHIR Server ["+ourServerName+"]");
					FhirServer server = registeredServers.get(ourServerName);
					if (server != null) {
						registerProviders(providers, server, serverName);
					} else {
						log.trace("Queue the Provider Bundle waiting for FHIR Server to be registered");
						Collection<Collection<Object>> pending;
						synchronized(pendingProviders) {
							pending = pendingProviders.get(serverName);
							if (null == pending) {
								pending = Collections.synchronizedCollection(new ArrayList<Collection<Object>>());
								pendingProviders.put(serverName, pending);
							}
						}
						pending.add(providers);
					}
				
				} catch (BadServerException e) {
					throw new FhirConfigurationException("Unable to register the OSGi FHIR Provider. Multiple Restful Servers exist. Specify the ["+FhirServer.SVCPROP_SERVICE_NAME+"] service-property");
				}
			}
		}
	}
	
	protected void registerProviders (Collection<Object> providers, FhirServer server, String serverName) throws FhirConfigurationException {
		server.registerOsgiProviders(providers);

		Collection<Collection<Object>> active;
		synchronized(serverProviders) {
			active = serverProviders.get(serverName);
			if (null == active) {
				active = Collections.synchronizedCollection(new ArrayList<Collection<Object>>());
				serverProviders.put(serverName, active);
			}
		}
		active.add(providers);
		registeredProviders.add(providers);
	}
	
	/**
	 * This method will be called when a FHIR Provider OSGi service
	 * is being removed from the container. This normally will only
	 * occur when its bundle is stopped because it is being removed
	 * or updated. 
	 * 
	 * @param server OSGi service implementing one of the provider 
	 * interfaces
	 * @param props the <service-properties> for that service
	 * 
	 * @throws FhirConfigurationException
	 */
	public void unregisterFhirProviders (FhirProviderBundle bundle, Map<String,Object> props) throws FhirConfigurationException {
		if (bundle != null) {
			Collection<Object> providers = bundle.getProviders();
			if (providers != null && !providers.isEmpty()) {
				try {
					registeredProviders.remove(providers);
					String serverName = (String)props.get(FhirServer.SVCPROP_SERVICE_NAME);
					String ourServerName = getServerName(serverName);
					FhirServer server = registeredServers.get(ourServerName);
					if (server != null) {
						
						server.unregisterOsgiProviders(providers);
						
						Collection<Collection<Object>> active = serverProviders.get(serverName);
						if (active != null) {
							active.remove(providers);
						}
					}
				} catch (BadServerException e) {
					throw new FhirConfigurationException("Unable to register the OSGi FHIR Provider. Multiple Restful Servers exist. Specify the ["+FhirServer.SVCPROP_SERVICE_NAME+"] service-property");
				}
			}
		}
	}

	/*
	 * Adjust the FHIR Server name allowing for null which would
	 * indicate that the Provider should be registered with the
	 * only FHIR Server defined.
	 */
	private String getServerName (String osgiName) throws BadServerException {
		String result = osgiName;
		if (null == result) {
			if (registeredServers.isEmpty()) { // wait for the first one
				haveDefaultProviders = true; // only allow one server
				result = FIRST_SERVER;
			} else
			if (registeredServers.size() == 1) { // use the only one
				haveDefaultProviders = true; // only allow one server
				result = registeredServers.keySet().iterator().next();
			} else {
				throw new BadServerException();
			}
		}
		return result;
	}
	
	class BadServerException extends Exception {
		BadServerException() {
			super();
		}
	}

}
