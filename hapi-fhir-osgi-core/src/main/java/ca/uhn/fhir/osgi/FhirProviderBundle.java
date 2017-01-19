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
