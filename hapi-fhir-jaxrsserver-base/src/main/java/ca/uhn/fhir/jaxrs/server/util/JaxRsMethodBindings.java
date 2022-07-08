package ca.uhn.fhir.jaxrs.server.util;

/*
 * #%L
 * HAPI FHIR JAX-RS Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsProvider;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.OperationMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;
import ca.uhn.fhir.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that contains the method bindings defined by a ResourceProvider
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
public class JaxRsMethodBindings {
    
	/** DEFAULT_METHOD_KEY="" */
	public static final String DEFAULT_METHOD_KEY = "";
	/** Static collection of bindings mapped to a class*/
	private static final ConcurrentHashMap<Class<?>, JaxRsMethodBindings> classBindings = new ConcurrentHashMap<Class<?>, JaxRsMethodBindings>();
	/** Static collection of operationBindings mapped to a class */
    private ConcurrentHashMap<RestOperationTypeEnum, ConcurrentHashMap<String, BaseMethodBinding<?>>> operationBindings = new ConcurrentHashMap<RestOperationTypeEnum, ConcurrentHashMap<String,BaseMethodBinding<?>>>();
    
    /**
     * The constructor
     * @param theProvider the provider which is an implementation of the theProviderClass
     * @param theProviderClass the class definition contaning the operations 
     */
    public  JaxRsMethodBindings(AbstractJaxRsProvider theProvider, Class<? extends AbstractJaxRsProvider> theProviderClass) {
	    List<Method> declaredMethodsForCurrentProvider = ReflectionUtil.getDeclaredMethods(theProviderClass);
	    declaredMethodsForCurrentProvider.addAll(ReflectionUtil.getDeclaredMethods(theProviderClass.getSuperclass()));
	    for (final Method m : declaredMethodsForCurrentProvider) {
            final BaseMethodBinding<?> foundMethodBinding = BaseMethodBinding.bindMethod(m, theProvider.getFhirContext(), theProvider);
            if (foundMethodBinding == null) {
                continue;
            }
            String bindingKey = getBindingKey(foundMethodBinding);
            addMethodBinding(bindingKey, foundMethodBinding);
        }
    }

	/**
	 * Get the key for the baseMethodBinding. This is:
	 * <ul>
	 * 	<li>the compartName for SearchMethodBindings
	 * 	<li>the methodName for OperationMethodBindings
	 * 	<li> {@link #DEFAULT_METHOD_KEY} for all other MethodBindings 
	 * </ul>
	 * @param theBinding the methodbinding
	 * @return the key for the methodbinding.
	 */
	private String getBindingKey(final BaseMethodBinding<?> theBinding) {
		if (theBinding instanceof OperationMethodBinding) {
		    return ((OperationMethodBinding) theBinding).getName();
		} else if (theBinding instanceof SearchMethodBinding) {
		    Search search = theBinding.getMethod().getAnnotation(Search.class);
		    return search.compartmentName();
		} else {
			return DEFAULT_METHOD_KEY;
		}
	}

    private void addMethodBinding(String key, BaseMethodBinding<?> binding) {
    	ConcurrentHashMap<String, BaseMethodBinding<?>> mapByOperation = getMapForOperation(binding.getRestOperationType());
        if (mapByOperation.containsKey(key)) {
            throw new IllegalArgumentException(Msg.code(597) + "Multiple Search Method Bindings Found : " + mapByOperation.get(key) + " -- " + binding.getMethod());
        }
        mapByOperation.put(key, binding);
    }

	/**
	 * Get the map for the given operation type. If no map exists for this operation type, create a new hashmap for this
	 * operation type and add it to the operation bindings.
	 * 
	 * @param operationType the operation type.
	 * @return the map defined in the operation bindings
	 */
	private ConcurrentHashMap<String, BaseMethodBinding<?>> getMapForOperation(RestOperationTypeEnum operationType) {
		ConcurrentHashMap<String, BaseMethodBinding<?>> result = operationBindings.get(operationType);
		if(result == null) {
			operationBindings.putIfAbsent(operationType, new ConcurrentHashMap<String, BaseMethodBinding<?>>());
			return getMapForOperation(operationType);
		} else {
			return result;
		}
	}

    /**
     * Get the binding 
     * 
     * @param operationType the type of operation
     * @param theBindingKey the binding key
     * @return the binding defined
     * @throws NotImplementedOperationException cannot be found
     */
    public BaseMethodBinding<?> getBinding(RestOperationTypeEnum operationType, String theBindingKey) {
        String bindingKey = StringUtils.defaultIfBlank(theBindingKey, DEFAULT_METHOD_KEY);
		ConcurrentHashMap<String, BaseMethodBinding<?>> map = getMapForOperation(operationType);
        if(map == null || !map.containsKey(bindingKey)) {
            throw new NotImplementedOperationException(Msg.code(598) + "Operation not implemented");
        }  else {
            return map.get(bindingKey);
        }
    }
    
	/**
	 * Get the method bindings for the given class. If this class is not yet contained in the classBindings, they will be added for this class
	 * 
	 * @param theProvider the implementation class
	 * @param theProviderClass the provider class
	 * @return the methodBindings for this class
	 */
	public static JaxRsMethodBindings getMethodBindings(AbstractJaxRsProvider theProvider, Class<? extends AbstractJaxRsProvider> theProviderClass) {
		if(!getClassBindings().containsKey(theProviderClass)) {
			JaxRsMethodBindings foundBindings = new JaxRsMethodBindings(theProvider, theProviderClass);
			getClassBindings().putIfAbsent(theProviderClass, foundBindings);
		}
		return getClassBindings().get(theProviderClass);
	}

	/**
	 * @return the classBindings
	 */
	static ConcurrentHashMap<Class<?>, JaxRsMethodBindings> getClassBindings() {
		return classBindings;
	}
		
    
}
