package ca.uhn.fhir.rest.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.method.BaseMethodBinding;

public class RestulfulServerConfiguration {
    
    private List<ResourceBinding> resourceBindings;
    private List<BaseMethodBinding<?>> serverBindings;
    private String implementationDescription;
    private String serverVersion;
    private String serverName;
    private FhirContext fhirContext;
    private ServletContext servletContext;
    private IServerAddressStrategy serverAddressStrategy;
    private String conformanceDate;
    
    public RestulfulServerConfiguration() {
    }
    
    public RestulfulServerConfiguration(RestfulServer theRestfulServer) {
        this.resourceBindings = new LinkedList<ResourceBinding>(theRestfulServer.getResourceBindings());
        this.serverBindings = theRestfulServer.getServerBindings();
        this.implementationDescription = theRestfulServer.getImplementationDescription();
        this.serverVersion = theRestfulServer.getServerVersion();
        this.serverName = theRestfulServer.getServerName();
        this.fhirContext = theRestfulServer.getFhirContext();
        this.serverAddressStrategy= theRestfulServer.getServerAddressStrategy();
        this.servletContext = theRestfulServer.getServletContext();
        if (servletContext != null) {
            InputStream inputStream = null;
            try {
                inputStream = getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF");
                if (inputStream != null) {
                    Manifest manifest = new Manifest(inputStream);
                    this.conformanceDate = manifest.getMainAttributes().getValue("Build-Time");
                }
            } catch (IOException e) {
                // fall through
            }
            finally {
                if (inputStream != null) {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        }
    }

    /**
     * Get the resourceBindings
     * @return the resourceBindings
     */
    public List<ResourceBinding> getResourceBindings() {
        return resourceBindings;
    }

    /**
     * Set the resourceBindings
     * @param resourceBindings the resourceBindings to set
     */
    public RestulfulServerConfiguration setResourceBindings(List<ResourceBinding> resourceBindings) {
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
     * Get the servletContext
     * @return the servletContext
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Set the servletContext
     * @param servletContext the servletContext to set
     */
    public RestulfulServerConfiguration setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
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
    
    public String getServerBaseForRequest(HttpServletRequest theRequest) {
        return getServerAddressStrategy().determineServerBase(getServletContext(), theRequest);        
    }
}
