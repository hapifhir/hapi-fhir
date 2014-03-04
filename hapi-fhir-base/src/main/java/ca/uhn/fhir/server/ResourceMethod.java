package ca.uhn.fhir.server;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.server.exceptions.InternalErrorException;
import ca.uhn.fhir.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.server.exceptions.MethodNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ResourceMethod {
    private Class<?> resourceType;

    public static enum RequestType {
        GET,
        POST,
        PUT,
        DELETE
    }

    private RequestType requestType;
    private List<Parameter> parameters;
    private Method method;
	private Resource resource;

    public ResourceMethod() {}

    public ResourceMethod(Method method, List<Parameter> parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    public void setResourceType(Class<?> resourceType) {
        this.resourceType = resourceType;
    }

    public Class getResourceType() {
        return resourceType.getClass();
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean matches(Set<String> parameterNames) {
        Set<String> methodParamsTemp = new HashSet<String>();
        for (int i = 0; i < this.parameters.size(); i++){
            Parameter temp = this.parameters.get(i);
            methodParamsTemp.add(temp.getName());
            if (temp.isRequired() && !parameterNames.contains(temp.getName())){
                return false;
            }
        }
        return methodParamsTemp.containsAll(parameterNames);
    }

    public List<IResource> invoke(IResourceProvider theResourceProvider, Map<String,String[]> parameterValues) throws InvalidRequestException, InternalErrorException {
        Object[] params = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            Parameter param = parameters.get(i);
            String[] value = parameterValues.get(param.getName());
            if (value == null || value.length == 0 || StringUtils.isBlank(value[0])) {
            	continue;
            }
            if (value.length > 1) {
            	throw new InvalidRequestException("Multiple values specified for parameter: " + param.getName());
            }
            params[i] = param.parse(value[0]);
        }
        
		Object response;
        try {
			response = this.method.invoke(theResourceProvider, params);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new InternalErrorException(e);
		}
        
        if (response == null) {
        	return Collections.emptyList();
        }else if (response instanceof IResource) {
        	return Collections.singletonList((IResource)response);
        } else if (response instanceof Collection) {
        	List<IResource> retVal = new ArrayList<>();
        	for (Object next : ((Collection<?>)response)) {
				retVal.add((IResource) next);
			}
        	return retVal;
        } else {
        	throw new InternalErrorException("Unexpected return type: " + response.getClass().getCanonicalName());
        }
        
    }

	public void setResource(Resource theResource) {
		this.resource = theResource;
	}
}
