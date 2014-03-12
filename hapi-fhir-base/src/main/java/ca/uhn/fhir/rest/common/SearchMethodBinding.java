package ca.uhn.fhir.rest.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.Parameter;
import ca.uhn.fhir.rest.server.Util;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SearchMethodBinding extends BaseMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchMethodBinding.class);

	private Method method;

	private Class<?> myDeclaredResourceType;
	private List<Parameter> myParameters;

	public SearchMethodBinding(MethodReturnTypeEnum theMethodReturnTypeEnum, Class<? extends IResource> theReturnResourceType, Method theMethod) {
		super(theMethodReturnTypeEnum, theReturnResourceType);
		this.method = theMethod;
		this.myParameters = Util.getResourceParameters(theMethod);
		
		this.myDeclaredResourceType = theMethod.getReturnType();
	}

	public Class<?> getDeclaredResourceType() {
		return myDeclaredResourceType.getClass();
	}

	public Method getMethod() {
		return method;
	}

	public List<Parameter> getParameters() {
		return myParameters;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public GetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		assert theArgs.length == myParameters.size() : "Wrong number of arguments: " + theArgs.length;
		
		Map<String, String> args = new LinkedHashMap<String, String>();
		
		for (int idx = 0; idx < theArgs.length; idx++) {
			Object object = theArgs[idx];
			Parameter nextParam = myParameters.get(idx);
			String value;

			if (object == null) {
				if (nextParam.isRequired()) {
					throw new NullPointerException("Parameter '" + nextParam.getName() + "' is required and may not be null");
				}else {
					value=null;
				}
			}else {
				value = nextParam.encode(object);
			}
			
			args.put(nextParam.getName(), value);
		}
		
		return new GetClientInvocation(args, getResourceName());
	}

	@Override
	public List<IResource> invokeServer(IResourceProvider theResourceProvider, IdDt theId, IdDt theVersionId, Map<String, String[]> parameterValues) throws InvalidRequestException, InternalErrorException {
		assert theId == null;
		assert theVersionId == null;

		Object[] params = new Object[myParameters.size()];
		for (int i = 0; i < myParameters.size(); i++) {
			Parameter param = myParameters.get(i);
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
	public boolean matches(Request theRequest) {
		if (!theRequest.getResourceName().equals(getResourceName())) {
			ourLog.trace("Method {} doesn't match because resource name {} != {}", method.getName(), theRequest.getResourceName(), getResourceName());
			return false;
		}
		if (theRequest.getId() != null || theRequest.getVersion() != null) {
			ourLog.trace("Method {} doesn't match because ID or Version are not null: {} - {}", theRequest.getId(), theRequest.getVersion());
			return false;
		}
		if (theRequest.getRequestType() == RequestType.GET && theRequest.getOperation() != null) {
			ourLog.trace("Method {} doesn't match because request type is GET but operation is not null: {}", theRequest.getId(), theRequest.getOperation());
			return false;
		}
		if (theRequest.getRequestType() == RequestType.POST && !"_search".equals(theRequest.getOperation())) {
			ourLog.trace("Method {} doesn't match because request type is POST but operation is not _search: {}", theRequest.getId(), theRequest.getOperation());
			return false;
		}
		
		Set<String> methodParamsTemp = new HashSet<String>();
		for (int i = 0; i < this.myParameters.size(); i++) {
			Parameter temp = this.myParameters.get(i);
			methodParamsTemp.add(temp.getName());
			if (temp.isRequired() && !theRequest.getParameterNames().contains(temp.getName())) {
				ourLog.trace("Method {} doesn't match param '{}' is not present", method.getName(), temp.getName());
				return false;
			}
		}
		boolean retVal = methodParamsTemp.containsAll(theRequest.getParameterNames());

		ourLog.trace("Method {} matches: {}", method.getName(), retVal);

		return retVal;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public void setParameters(List<Parameter> parameters) {
		this.myParameters = parameters;
	}

	public void setResourceType(Class<?> resourceType) {
		this.myDeclaredResourceType = resourceType;
	}

	public static enum RequestType {
		DELETE, GET, POST, PUT, OPTIONS
	}

}
