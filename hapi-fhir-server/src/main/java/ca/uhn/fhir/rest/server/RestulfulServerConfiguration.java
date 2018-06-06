package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import java.util.Date;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.util.VersionUtil;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class RestulfulServerConfiguration {
    
    private Collection<ResourceBinding> resourceBindings;
    private List<BaseMethodBinding<?>> serverBindings;
    private String implementationDescription;
    private String serverVersion = VersionUtil.getVersion();
    private String serverName = "HAPI FHIR";
    private FhirContext fhirContext;
    private IServerAddressStrategy serverAddressStrategy;
    private IPrimitiveType<Date> myConformanceDate;
    
    /**
     * Constructor
     */
    public RestulfulServerConfiguration() {
       super();
    }
    
    /**
     * Get the resourceBindings
     * @return the resourceBindings
     */
    public Collection<ResourceBinding> getResourceBindings() {
        return resourceBindings;
    }

    /**
     * Set the resourceBindings
     * @param resourceBindings the resourceBindings to set
     */
    public RestulfulServerConfiguration setResourceBindings(Collection<ResourceBinding> resourceBindings) {
        this.resourceBindings = resourceBindings;
        return this;
    }

    /**
     * Get the serverBindings
     * @return the serverBindings
     */
    public List<BaseMethodBinding<?>> getServerBindings() {
        return serverBindings;
    }

    /**
     * Set the theServerBindings
     */
    public RestulfulServerConfiguration setServerBindings(List<BaseMethodBinding<?>> theServerBindings) {
        this.serverBindings = theServerBindings;
        return this;
    }

    /**
     * Get the implementationDescription
     * @return the implementationDescription
     */
    public String getImplementationDescription() {
    	if (isBlank(implementationDescription)) {
			return "HAPI FHIR";
		}
        return implementationDescription;
    }

    /**
     * Set the implementationDescription
     * @param implementationDescription the implementationDescription to set
     */
    public RestulfulServerConfiguration setImplementationDescription(String implementationDescription) {
        this.implementationDescription = implementationDescription;
        return this;
    }

    /**
     * Get the serverVersion
     * @return the serverVersion
     */
    public String getServerVersion() {
        return serverVersion;
    }

    /**
     * Set the serverVersion
     * @param serverVersion the serverVersion to set
     */
    public RestulfulServerConfiguration setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
        return this;
    }

    /**
     * Get the serverName
     * @return the serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * Set the serverName
     * @param serverName the serverName to set
     */
    public RestulfulServerConfiguration setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    /**
     * Gets the {@link FhirContext} associated with this server. For efficient processing, resource providers and plain providers should generally use this context if one is needed, as opposed to
     * creating their own.
     */
    public FhirContext getFhirContext() {
        return this.fhirContext;
    }
    
    /**
     * Set the fhirContext
     * @param fhirContext the fhirContext to set
     */
    public RestulfulServerConfiguration setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
        return this;
    }
    
    /**
     * Get the serverAddressStrategy
     * @return the serverAddressStrategy
     */
    public IServerAddressStrategy getServerAddressStrategy() {
        return serverAddressStrategy;
    }

    /**
     * Set the serverAddressStrategy
     * @param serverAddressStrategy the serverAddressStrategy to set
     */
    public void setServerAddressStrategy(IServerAddressStrategy serverAddressStrategy) {
        this.serverAddressStrategy = serverAddressStrategy;
    }


	/**
	 * Get the date that will be specified in the conformance profile
	 * exported by this server. Typically this would be populated with
	 * an InstanceType.
	 */
    public IPrimitiveType<Date> getConformanceDate() {
        return myConformanceDate;
    }

    /**
     * Set the date that will be specified in the conformance profile
	  * exported by this server. Typically this would be populated with
	  * an InstanceType.
     */
    public void setConformanceDate(IPrimitiveType<Date> theConformanceDate) {
        myConformanceDate = theConformanceDate;
    }
    
}
