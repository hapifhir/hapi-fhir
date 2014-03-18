package ca.uhn.fhir.rest.method;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.SearchParameter;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.Util;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.QueryUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SearchMethodBinding extends BaseMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchMethodBinding.class);

	private Method method;

	private Class<?> myDeclaredResourceType;
	private List<IParameter> myParameters;

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

	public List<IParameter> getParameters() {
		return myParameters;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public GetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		assert theArgs.length == myParameters.size() : "Wrong number of arguments: " + theArgs.length;

		Map<String, List<String>> args = new LinkedHashMap<String, List<String>>();

		for (int idx = 0; idx < theArgs.length; idx++) {
			Object object = theArgs[idx];
			IParameter nextParam = myParameters.get(idx);

			if (object == null) {
				if (nextParam.isRequired()) {
					throw new NullPointerException("SearchParameter '" + nextParam.getName() + "' is required and may not be null");
				}
			} else {
				List<List<String>> value = nextParam.encode(object);
				ArrayList<String> paramValues = new ArrayList<String>(value.size());
				args.put(nextParam.getName(), paramValues);

				for (List<String> nextParamEntry : value) {
					StringBuilder b = new StringBuilder();
					for (String str : nextParamEntry) {
						if (b.length() > 0) {
							b.append(",");
						}
						b.append(str.replace(",", "\\,"));
					}
					paramValues.add(b.toString());
				}
				
			}

		}

		return new GetClientInvocation(args, getResourceName());
	}

	@Override
	public List<IResource> invokeServer(IResourceProvider theResourceProvider, IdDt theId, IdDt theVersionId, Map<String, String[]> parameterValues) throws InvalidRequestException, InternalErrorException {
		assert theId == null;
		assert theVersionId == null;

		Object[] params = new Object[myParameters.size()];
		for (int i = 0; i < myParameters.size(); i++) {
			IParameter param = myParameters.get(i);
			String[] value = parameterValues.get(param.getName());
			if (value == null || value.length == 0) {
				continue;
			}
			
			List<List<String>> paramList=new ArrayList<List<String>>(value.length);
			for (String nextParam : value) {
				if (nextParam.contains(",")==false) {
					paramList.add(Collections.singletonList(nextParam));
				}else {
					paramList.add(QueryUtil.splitQueryStringByCommasIgnoreEscape(nextParam));
				}
			}
			
			params[i] = param.parse(paramList);
			
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
			IParameter temp = this.myParameters.get(i);
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

	public void setParameters(List<IParameter> parameters) {
		this.myParameters = parameters;
	}

	public void setResourceType(Class<?> resourceType) {
		this.myDeclaredResourceType = resourceType;
	}

	public static enum RequestType {
		DELETE, GET, POST, PUT, OPTIONS
	}

}
