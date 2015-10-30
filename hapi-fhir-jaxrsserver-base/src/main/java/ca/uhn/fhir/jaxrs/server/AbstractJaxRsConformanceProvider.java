package ca.uhn.fhir.jaxrs.server;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jaxrs.server.util.JaxRsRequest;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.ParseAction;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IRestfulResponse;
import ca.uhn.fhir.rest.server.ResourceBinding;
import ca.uhn.fhir.rest.server.RestulfulServerConfiguration;
import ca.uhn.fhir.rest.server.provider.dstu2.ServerConformanceProvider;
import ca.uhn.fhir.util.ReflectionUtil;

/**
 * Conformance Rest Service
 * @author Peter Van Houte
 */
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public abstract class AbstractJaxRsConformanceProvider extends AbstractJaxRsProvider {

	public static  final String PATH = "/";
    private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(AbstractJaxRsConformanceProvider.class);
    
    private ResourceBinding myServerBinding = new ResourceBinding();
    private ConcurrentHashMap<String, ResourceBinding> myResourceNameToBinding = new ConcurrentHashMap<String, ResourceBinding>();
    private RestulfulServerConfiguration serverConfiguration = new RestulfulServerConfiguration();

    private Conformance myConformance;
    
    public AbstractJaxRsConformanceProvider(String implementationDescription, String serverName, String serverVersion) {
        serverConfiguration.setFhirContext(getFhirContext());
        serverConfiguration.setImplementationDescription(implementationDescription);
        serverConfiguration.setServerName(serverName);
        serverConfiguration.setServerVersion(serverVersion);
    }

    @PostConstruct
    protected void setUpPostConstruct() {
    	for (Entry<Class<? extends IResourceProvider>, IResourceProvider> provider : getProviders().entrySet()) {
			addProvider(provider.getValue(), provider.getKey());
		}
        List<BaseMethodBinding<?>> serverBindings = new ArrayList<BaseMethodBinding<?>>();
        for (ResourceBinding baseMethodBinding : myResourceNameToBinding.values()) {
            serverBindings.addAll(baseMethodBinding.getMethodBindings());
        }
        serverConfiguration.setServerBindings(serverBindings);
        serverConfiguration.setResourceBindings(new LinkedList<ResourceBinding>(myResourceNameToBinding.values()));
        HardcodedServerAddressStrategy hardcodedServerAddressStrategy = new HardcodedServerAddressStrategy();
        hardcodedServerAddressStrategy.setValue(getBaseForServer());
        serverConfiguration.setServerAddressStrategy(hardcodedServerAddressStrategy);
        ServerConformanceProvider serverConformanceProvider = new ServerConformanceProvider(serverConfiguration);
        serverConformanceProvider.initializeOperations();
        myConformance = serverConformanceProvider.getServerConformance(null);
    }
    
    protected abstract ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders();
    
    @OPTIONS
    @Path("/metadata")
    public Response conformanceUsingOptions() throws IOException {
    	return conformance();
    }

	@GET
    @Path("/metadata")
    public Response conformance() throws IOException {
        JaxRsRequest request = createRequestDetails(null, RequestTypeEnum.OPTIONS, RestOperationTypeEnum.METADATA);
        IRestfulResponse response = request.getResponse();
		response.addHeader(Constants.HEADER_CORS_ALLOW_ORIGIN, "*");
		return (Response) response.returnResponse(ParseAction.create(myConformance), Constants.STATUS_HTTP_200_OK, true, null, getResourceType().getSimpleName());
    }

    public int addProvider(Object theProvider, Class<?> theProviderInterface) throws ConfigurationException {
        int count = 0;

        for (Method m : ReflectionUtil.getDeclaredMethods(theProviderInterface)) {
            BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, getFhirContext(), theProvider);
            if (foundMethodBinding == null) {
                continue;
            }

            count++;

//            if (foundMethodBinding instanceof ConformanceMethodBinding) {
//                myServerConformanceMethod = foundMethodBinding;
//                continue;
//            }

            if (!Modifier.isPublic(m.getModifiers())) {
                throw new ConfigurationException("Method '" + m.getName() + "' is not public, FHIR RESTful methods must be public");
            } else {
                if (Modifier.isStatic(m.getModifiers())) {
                    throw new ConfigurationException("Method '" + m.getName() + "' is static, FHIR RESTful methods must not be static");
                } else {
                    ourLog.debug("Scanning public method: {}#{}", theProvider.getClass(), m.getName());

                    String resourceName = foundMethodBinding.getResourceName();
                    ResourceBinding resourceBinding;
                    if (resourceName == null) {
                        resourceBinding = myServerBinding;
                    } else {
                        RuntimeResourceDefinition definition = getFhirContext().getResourceDefinition(resourceName);
                        if (myResourceNameToBinding.containsKey(definition.getName())) {
                            resourceBinding = myResourceNameToBinding.get(definition.getName());
                        } else {
                            resourceBinding = new ResourceBinding();
                            resourceBinding.setResourceName(resourceName);
                            myResourceNameToBinding.put(resourceName, resourceBinding);
                        }
                    }

                    List<Class<?>> allowableParams = foundMethodBinding.getAllowableParamAnnotations();
                    if (allowableParams != null) {
                        for (Annotation[] nextParamAnnotations : m.getParameterAnnotations()) {
                            for (Annotation annotation : nextParamAnnotations) {
                                Package pack = annotation.annotationType().getPackage();
                                if (pack.equals(IdParam.class.getPackage())) {
                                    if (!allowableParams.contains(annotation.annotationType())) {
                                        throw new ConfigurationException("Method[" + m.toString() + "] is not allowed to have a parameter annotated with " + annotation);
                                    }
                                }
                            }
                        }
                    }

                    resourceBinding.addMethod(foundMethodBinding);
                    ourLog.debug(" * Method: {}#{} is a handler", theProvider.getClass(), m.getName());
                }
            }
        }

        return count;
    }

    @Override
    public Class<Conformance> getResourceType() {
        return Conformance.class;
    }
    
}
