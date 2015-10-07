package ca.uhn.fhir.jaxrs.server.util;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractResourceRestServer;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.OperationMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.util.ReflectionUtil;

public class MethodBindings {
    
    /** ALL METHOD BINDINGS */
    ConcurrentHashMap<RestOperationTypeEnum, ConcurrentHashMap<String, BaseMethodBinding<?>>> allBindings = new ConcurrentHashMap<RestOperationTypeEnum, ConcurrentHashMap<String,BaseMethodBinding<?>>>();    
    
    public <T extends AbstractResourceRestServer<?>> void findMethods(T theProvider, Class<?> subclass, FhirContext fhirContext) {
        for (final Method m : ReflectionUtil.getDeclaredMethods(subclass)) {
            final BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, fhirContext, theProvider);
            if (foundMethodBinding == null) {
                continue;
            }
            ConcurrentHashMap<String, BaseMethodBinding<?>> map = getAllBindingsMap(foundMethodBinding.getRestOperationType());
            if (foundMethodBinding instanceof OperationMethodBinding) {
                OperationMethodBinding binding = (OperationMethodBinding) foundMethodBinding;
                putIfAbsent(map, binding.getName(), binding);
            } else if (foundMethodBinding instanceof SearchMethodBinding) {
                Search search = m.getAnnotation(Search.class);
                String compartmentName = StringUtils.defaultIfBlank(search.compartmentName(), "");
                putIfAbsent(map, compartmentName, foundMethodBinding);
            } else {
                putIfAbsent(map, "", foundMethodBinding);
            }
        }
    }

    private void putIfAbsent(ConcurrentHashMap<String, BaseMethodBinding<?>> map, String key, BaseMethodBinding binding) {
        if (map.containsKey(key)) {
            throw new IllegalArgumentException("Multiple Search Method Bindings Found : " + map.get(key) + " -- " + binding.getMethod());
        }        
        map.put(key, binding);
    }

    private ConcurrentHashMap<String,BaseMethodBinding<?>> getAllBindingsMap(final RestOperationTypeEnum restOperationTypeEnum) {
        allBindings.putIfAbsent(restOperationTypeEnum, new ConcurrentHashMap<String, BaseMethodBinding<?>>());
        return allBindings.get(restOperationTypeEnum);
    }

    public BaseMethodBinding<?> getBinding(RestOperationTypeEnum operationType, String qualifier) {
        String nonEmptyQualifier = StringUtils.defaultIfBlank(qualifier, "");        
        ConcurrentHashMap<String, BaseMethodBinding<?>> map = getAllBindingsMap(operationType);
        if(!map.containsKey(nonEmptyQualifier)) {
            throw new UnsupportedOperationException();
        }  else {
            return map.get(nonEmptyQualifier);
        }
    }
    
    public BaseMethodBinding<?> getBinding(RestOperationTypeEnum operationType) {
        return getBinding(operationType, "");
    }

}
