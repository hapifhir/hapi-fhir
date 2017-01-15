package ca.uhn.fhir.osgi;

/*
 * #%L
 * HAPI FHIR - OSGi Bundle
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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.RestfulServer;

/**
 *
 * @author Akana, Inc. Professional Services
 *
 */
public class FhirServerImpl extends RestfulServer implements FhirServer {
	private static Logger log = LoggerFactory.getLogger(FhirServerImpl.class);
	
	private Collection<Object> serverProviders = Collections.synchronizedCollection(new ArrayList<Object>());

	public FhirServerImpl() {
		super();
	}

	public FhirServerImpl(FhirContext theCtx) {
		super(theCtx);
	}

	/**
	 * Dynamically registers a single provider with the RestfulServer
	 * 
	 * @param provider the provider to be registered
	 * @throws FhirConfigurationException
	 */
	@Override
	public void registerOsgiProvider (Object provider) throws FhirConfigurationException {
		if (null == provider) {
			throw new NullPointerException("FHIR Provider cannot be null");
		}
		try {
			super.registerProvider(provider);
			log.trace("registered provider. class ["+provider.getClass().getName()+"]");
			this.serverProviders.add(provider);
		} catch (Exception e) {
			log.error("Error registering FHIR Provider", e);
			throw new FhirConfigurationException("Error registering FHIR Provider", e);
		}
	}

	/**
	 * Dynamically unregisters a single provider with the RestfulServer
	 * 
	 * @param provider the provider to be unregistered
	 * @throws FhirConfigurationException
	 */
	@Override
	public void unregisterOsgiProvider (Object provider) throws FhirConfigurationException {
		if (null == provider) {
			throw new NullPointerException("FHIR Provider cannot be null");
		}
		try {
			this.serverProviders.remove(provider);
			log.trace("unregistered provider. class ["+provider.getClass().getName()+"]");
			super.unregisterProvider(provider);
		} catch (Exception e) {
			log.error("Error unregistering FHIR Provider", e);
			throw new FhirConfigurationException("Error unregistering FHIR Provider", e);
		}
	}

	/**
	 * Dynamically registers a list of providers with the RestfulServer
	 * 
	 * @param provider the providers to be registered
	 * @throws FhirConfigurationException
	 */
	@Override
	public void registerOsgiProviders (Collection<Object> providers) throws FhirConfigurationException {
		if (null == providers) {
			throw new NullPointerException("FHIR Provider list cannot be null");
		}
		try {
			super.registerProviders(providers);
			for (Object provider : providers) {
				log.trace("registered provider. class ["+provider.getClass().getName()+"]");
				this.serverProviders.add(provider);
			}
		} catch (Exception e) {
			log.error("Error registering FHIR Providers", e);
			throw new FhirConfigurationException("Error registering FHIR Providers", e);
		}
	}

	/**
	 * Dynamically unregisters a list of providers with the RestfulServer
	 * 
	 * @param provider the providers to be unregistered
	 * @throws FhirConfigurationException
	 */
	@Override
	public void unregisterOsgiProviders (Collection<Object> providers) throws FhirConfigurationException {
		if (null == providers) {
			throw new NullPointerException("FHIR Provider list cannot be null");
		}
		try {
			for (Object provider : providers) {
				log.trace("unregistered provider. class ["+provider.getClass().getName()+"]");
				this.serverProviders.remove(provider);
			}
			super.unregisterProvider(providers);
		} catch (Exception e) {
			log.error("Error unregistering FHIR Providers", e);
			throw new FhirConfigurationException("Error unregistering FHIR Providers", e);
		}
	}

	/**
	 * Dynamically unregisters all of providers currently registered
	 * 
	 * @throws FhirConfigurationException
	 */
	@Override
	public void unregisterOsgiProviders () throws FhirConfigurationException {
		// need to make a copy to be able to remove items
		Collection<Object> providers = new ArrayList<Object>();
		providers.addAll(this.serverProviders);
		this.unregisterOsgiProviders(providers);
	}

}
