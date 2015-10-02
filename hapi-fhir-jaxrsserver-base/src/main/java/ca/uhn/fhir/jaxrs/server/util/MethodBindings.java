package ca.uhn.fhir.jaxrs.server.util;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractResourceRestServer;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.CreateMethodBinding;
import ca.uhn.fhir.rest.method.DeleteMethodBinding;
import ca.uhn.fhir.rest.method.IParameter;
import ca.uhn.fhir.rest.method.OperationMethodBinding;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.method.UpdateMethodBinding;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.util.ReflectionUtil;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MethodBindings {

    /** BaseOutcomeReturningMethodBinding */
    private ConcurrentHashMap<String, CreateMethodBinding> createMethods = new ConcurrentHashMap<String, CreateMethodBinding>();
    private ConcurrentHashMap<String, UpdateMethodBinding> updateMethods = new ConcurrentHashMap<String, UpdateMethodBinding>();
    private ConcurrentHashMap<String, DeleteMethodBinding> delete = new ConcurrentHashMap<String, DeleteMethodBinding>();
    
    /** BaseResourceReturingMethodBinding */
    private ConcurrentHashMap<String, SearchMethodBinding>    searchMethods = new ConcurrentHashMap<String, SearchMethodBinding>();
    private ConcurrentHashMap<String, OperationMethodBinding> operationMethods = new ConcurrentHashMap<String, OperationMethodBinding>();
    
    public <T extends AbstractResourceRestServer<?>> void findMethods(T theProvider, Class<?> subclass, FhirContext fhirContext) {
        for (final Method m : ReflectionUtil.getDeclaredMethods(subclass)) {
            final BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, fhirContext, theProvider);
            if(foundMethodBinding == null) { 
                continue;
            }
            ConcurrentHashMap map = getMap(foundMethodBinding.getClass());
            if (map.contains(theProvider.getResourceType().getName())) {
                throw new IllegalArgumentException("Multiple Search Method Bindings Found : " + foundMethodBinding.getMethod() + " -- "
                        + foundMethodBinding.getMethod());
            } else {
                map.put(theProvider.getResourceType().getName(), foundMethodBinding);
            }
        }
    }
    
    private <T> ConcurrentHashMap<String, T> getMap(Class<T> class1) {
        if(class1.isAssignableFrom(CreateMethodBinding.class))    return (ConcurrentHashMap<String, T>) createMethods;
        if(class1.isAssignableFrom(UpdateMethodBinding.class))    return (ConcurrentHashMap<String, T>) updateMethods;
        if(class1.isAssignableFrom(DeleteMethodBinding.class))    return (ConcurrentHashMap<String, T>) delete;
        if(class1.isAssignableFrom(SearchMethodBinding.class))    return (ConcurrentHashMap<String, T>) searchMethods;
        if(class1.isAssignableFrom(OperationMethodBinding.class)) return (ConcurrentHashMap<String, T>) operationMethods;
        return new ConcurrentHashMap();
    }

    public Object[] createParams(IBaseResource resource, final BaseMethodBinding<?> method, final RequestDetails theRequest) {
        final Object[] paramsServer = new Object[method.getParameters().size()];
        for (int i = 0; i < method.getParameters().size(); i++) {
            final IParameter param = method.getParameters().get(i);
            if(param instanceof ResourceParameter) {
                paramsServer[i] = resource;
            } else {
                paramsServer[i] = param.translateQueryParametersIntoServerArgument(theRequest, null, method);
            }
        }
        return paramsServer;
    }

    public <T> T getBinding(Class<T> clazz) {
        ConcurrentHashMap map = getMap((Class<? extends BaseMethodBinding>) clazz);
        if(map.values().size() == 0) {
            throw new UnsupportedOperationException();
        } 
        return (T) map.values().iterator().next();
    }

}
