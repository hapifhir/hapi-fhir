package ca.uhn.fhir.ws;

import ca.uhn.fhir.model.api.IResource;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    RequestType requestType;
    List<Parameter> parameters;
    Method method;

    public ResourceMethod() {}

    public ResourceMethod(Method method, List<Parameter> parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    public void setResourceType(Class<?> resourceType) {
        this.resourceType = resourceType;
    }

    public IResource getResourceType() throws IllegalAccessException, InstantiationException {
        return (IResource)resourceType.newInstance();
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

    public IResource invoke(Map<String,String> parameterValues) {
        Object[] params = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            Parameter param = parameters.get(i);
            String value = parameterValues.get(param.getName());
            if (null != value) {
                //TODO
                //param.getType().newInstance().getClass();
            }
            else {
                params[i] = null;
            }
        }
        return null;
    }
}
