/**
 * This software has been produced by Akana, Inc. under a professional services
 * agreement with our customer. This work may contain material that is confidential 
 * and proprietary information of Akana, Inc. and is subject to copyright 
 * protection under laws of the United States of America and other countries. 
 * Akana, Inc. grants the customer non-exclusive rights to this material without
 * any warranty expressed or implied. 
 */

package ca.uhn.fhir.osgi;

import java.util.Collection;


/**
 * Instances of the FHIR Server must implement this interface
 * in order to be registered as OSGi services capable of dynamic
 * provider registration. It expected that implementations of this
 * interface will also extend RestfulService.
 *
 * The OSGi service definition for instances of the FHIR SERver
 * should have the following <service-property> entry:
 * 
 * <entry key="fhir.server.name" value="a-name"/>
 * 
 * where the value matches the same <service-property> specified
 * on the published "provider" OSGi services that are to be
 * dynamically registered in the FHIR Server instance.
 *
 * @author Akana, Inc. Professional Services
 *
 */
public interface FhirServer {
	public static final String SVCPROP_SERVICE_NAME = "fhir.server.name";

	/**
	 * Dynamically registers a single provider with the RestfulServer
	 * 
	 * @param provider the provider to be registered
	 * @throws FhirConfigurationException
	 */
	public void registerOsgiProvider(Object provider) throws FhirConfigurationException;

	/**
	 * Dynamically unregisters a single provider with the RestfulServer
	 * 
	 * @param provider the provider to be unregistered
	 * @throws FhirConfigurationException
	 */
	public void unregisterOsgiProvider(Object provider) throws FhirConfigurationException;

	/**
	 * Dynamically registers a list of providers with the RestfulServer
	 * 
	 * @param provider the providers to be registered
	 * @throws FhirConfigurationException
	 */
	public void registerOsgiProviders(Collection<Object> provider) throws FhirConfigurationException;

	/**
	 * Dynamically unregisters a list of providers with the RestfulServer
	 * 
	 * @param provider the providers to be unregistered
	 * @throws FhirConfigurationException
	 */
	public void unregisterOsgiProviders(Collection<Object> provider) throws FhirConfigurationException;

	/**
	 * Dynamically unregisters all of providers currently registered
	 * 
	 * @throws FhirConfigurationException
	 */
	public void unregisterOsgiProviders() throws FhirConfigurationException;

}
