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
 * This is an abstraction for adding one or more Providers
 * ("plain" providers as well as Resource Providers)
 * to the configuration of a Fhir Server. This approach
 * is needed versus direct publication of providers as
 * OSGi services because references to OSGi services are
 * really proxies that only implement the methods of the
 * service's interfaces. This means that the introspection
 * and annotation processing needed for HAPI FHIR provider
 * processing is not possible on those proxy references..
 * 
 * To get around this restriction, instances of this interface
 * will be published as OSGi services and the real providers
 * will typically be Spring wired into the underlying bean.
 * 
 * Beans that are decorated with this interface can be
 * published as OSGi services and will be registered in
 * the specified FHIR Server. The OSGi service definition
 * should have the following <service-property> entry:
 * 
 * <entry key="fhir.server.name" value="a-name"/>
 * 
 * where the value matches the same <service-property>
 * assigned to a FhirServer OSGi service.
*
 * @author Akana, Inc. Professional Services
 *
 */
public interface FhirProviderBundle {
	public Collection<Object> getProviders();
}
