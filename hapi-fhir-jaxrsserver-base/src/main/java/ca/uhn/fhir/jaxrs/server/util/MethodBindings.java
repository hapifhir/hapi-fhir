package ca.uhn.fhir.jaxrs.server.util;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.rest.method.OperationMethodBinding;
import ca.uhn.fhir.rest.method.SearchMethodBinding;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.util.ReflectionUtil;

/**
 * @author Peter Van Houte
 * Class that contains the method bindings defined by a ResourceProvider
 */
public class MethodBindings {
    
	/** Static collection of bindings mapped to a class*/
	private static final ConcurrentHashMap<Class<?>, MethodBindings> classBindings = new ConcurrentHashMap<Class<?>, MethodBindings>();
	/** Static collection of operationBindings mapped to a class */
    private ConcurrentHashMap<RestOperationTypeEnum, ConcurrentHashMap<String, BaseMethodBinding<?>>> operationBindings = new ConcurrentHashMap<RestOperationTypeEnum, ConcurrentHashMap<String,BaseMethodBinding<?>>>();
    
    public <T extends AbstractJaxRsResourceProvider<?>> MethodBindings(T theProvider, Class<?> clazz) {
        for (final Method m : ReflectionUtil.getDeclaredMethods(clazz)) {
            final BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, theProvider.getFhirContext(), theProvider);
            if (foundMethodBinding == null) {
                continue;
            }
            String bindingKey = getBindingKey(foundMethodBinding);
            putIfAbsent(bindingKey, foundMethodBinding);
        }
    }

	private String getBindingKey(final BaseMethodBinding<?> foundMethodBinding) {
		if (foundMethodBinding instanceof OperationMethodBinding) {
		    return ((OperationMethodBinding) foundMethodBinding).getName();
		} else if (foundMethodBinding instanceof SearchMethodBinding) {
		    Search search = foundMethodBinding.getMethod().getAnnotation(Search.class);
		    return search.compartmentName();
		} else {
			return "";
		}
	}

    private void putIfAbsent(String key, BaseMethodBinding<?> binding) {
    	operationBindings.putIfAbsent(binding.getRestOperationType(), new ConcurrentHashMap<String, BaseMethodBinding<?>>());
    	ConcurrentHashMap<String, BaseMethodBinding<?>> map = operationBindings.get(binding.getRestOperationType());
        if (map.containsKey(key)) {
            throw new IllegalArgumentException("Multiple Search Method Bindings Found : " + map.get(key) + " -- " + binding.getMethod());
        }
        map.put(key, binding);
    }

    public BaseMethodBinding<?> getBinding(RestOperationTypeEnum operationType) {
        return getBinding(operationType, "");
    }    

    public BaseMethodBinding<?> getBinding(RestOperationTypeEnum operationType, String qualifier) {
        String nonEmptyQualifier = StringUtils.defaultIfBlank(qualifier, "");
		ConcurrentHashMap<String, BaseMethodBinding<?>> map = operationBindings.get(operationType);
        if(map == null || !map.containsKey(nonEmptyQualifier)) {
            throw new NotImplementedOperationException("Operation not implemented");
        }  else {
            return map.get(nonEmptyQualifier);
        }
    }
    
	public static <T extends AbstractJaxRsResourceProvider<?>> MethodBindings getMethodBindings(T theProvider, Class<?> clazz) {
		if(!getClassBindings().containsKey(clazz)) {
			MethodBindings foundBindings = new MethodBindings(theProvider, clazz);
			getClassBindings().putIfAbsent(clazz, foundBindings);
		}
		return getClassBindings().get(clazz);
	}

	public static ConcurrentHashMap<Class<?>, MethodBindings> getClassBindings() {
		return classBindings;
	}
		
    
}
