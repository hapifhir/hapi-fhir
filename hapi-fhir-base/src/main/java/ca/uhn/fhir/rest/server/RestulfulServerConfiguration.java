package ca.uhn.fhir.rest.server;

import java.util.Collection;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.method.BaseMethodBinding;

public class RestulfulServerConfiguration {
    
    private Collection<ResourceBinding> resourceBindings;
    private List<BaseMethodBinding<?>> serverBindings;
    private String implementationDescription;
    private String serverVersion;
    private String serverName;
    private FhirContext fhirContext;
    private IServerAddressStrategy serverAddressStrategy;
    private String conformanceDate;
    
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
     * Set the serverBindings
     * @param serverBindings the serverBindings to set
     */
    public RestulfulServerConfiguration setServerBindings(List<BaseMethodBinding<?>> serverBindings) {
        this.serverBindings = serverBindings;
        return this;
    }

    /**
     * Get the implementationDescription
     * @return the implementationDescription
     */
    public String getImplementationDescription() {
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
     * Get the conformanceDate
     * @return the conformanceDate
     */
    public String getConformanceDate() {
        return conformanceDate;
    }

    /**
     * Set the conformanceDate
     * @param conformanceDate the conformanceDate to set
     */
    public void setConformanceDate(String conformanceDate) {
        this.conformanceDate = conformanceDate;
    }
    
}
