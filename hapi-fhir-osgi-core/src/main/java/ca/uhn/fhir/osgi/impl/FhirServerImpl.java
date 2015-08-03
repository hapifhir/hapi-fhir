/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * SOA Software grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */

package ca.uhn.fhir.osgi.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.osgi.FhirConfigurationException;
import ca.uhn.fhir.osgi.FhirServer;
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
