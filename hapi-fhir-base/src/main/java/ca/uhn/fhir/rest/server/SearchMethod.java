package ca.uhn.fhir.rest.server;

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
import org.junit.internal.MethodSorter;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SearchMethod extends BaseMethod {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchMethod.class);

	private Method method;

	private List<Parameter> parameters;
	private RequestType requestType;
	private Class<?> resourceType;

	public SearchMethod() {
	}

	public SearchMethod(Method method, List<Parameter> parameters) {
		this.method = method;
		this.parameters = parameters;
	}

	public Method getMethod() {
		return method;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public Class getResourceType() {
		return resourceType.getClass();
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public List<IResource> invoke(IResourceProvider theResourceProvider, IdDt theId, IdDt theVersionId, Map<String, String[]> parameterValues) throws InvalidRequestException, InternalErrorException {
		assert theId == null;
		assert theVersionId == null;

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
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(e);
		} catch (IllegalArgumentException e) {
			throw new InternalErrorException(e);
		} catch (InvocationTargetException e) {
			throw new InternalErrorException(e);
		}

		return toResourceList(response);

	}

	@Override
	public boolean matches(String theResourceName, IdDt theId, IdDt theVersion, Set<String> theParameterNames) {
		if (!theResourceName.equals(getResource().getResourceName())) {
			ourLog.info("Method {} doesn't match because resource name {} != {}", method.getName(), theResourceName, getResource().getResourceName());
			return false;
		}
		if (theId != null || theVersion != null) {
			ourLog.info("Method {} doesn't match because ID or Version are not null: {} - {}", theId, theVersion);
			return false;
		}

		Set<String> methodParamsTemp = new HashSet<String>();
		for (int i = 0; i < this.parameters.size(); i++) {
			Parameter temp = this.parameters.get(i);
			methodParamsTemp.add(temp.getName());
			if (temp.isRequired() && !theParameterNames.contains(temp.getName())) {
				ourLog.info("Method {} doesn't match param '{}' is not present", temp.getName());
				return false;
			}
		}
		boolean retVal = methodParamsTemp.containsAll(theParameterNames);

		ourLog.info("Method {} matches: {}", method.getName(), retVal);

		return retVal;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public void setResourceType(Class<?> resourceType) {
		this.resourceType = resourceType;
	}

	public static enum RequestType {
		DELETE, GET, POST, PUT
	}

}
