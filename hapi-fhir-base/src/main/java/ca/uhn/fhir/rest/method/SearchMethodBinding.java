package ca.uhn.fhir.rest.method;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.param.IQueryParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SearchMethodBinding extends BaseResourceReturningMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchMethodBinding.class);

	private Class<?> myDeclaredResourceType;
	private List<IParameter> myParameters;
	private String myQueryName;

	public SearchMethodBinding(Class<? extends IResource> theReturnResourceType, Method theMethod, String theQueryName, FhirContext theContext) {
		super(theReturnResourceType, theMethod, theContext);
		this.myParameters = Util.getResourceParameters(theMethod);
		this.myQueryName = StringUtils.defaultIfBlank(theQueryName, null);
		this.myDeclaredResourceType = theMethod.getReturnType();
	}

	public Class<?> getDeclaredResourceType() {
		return myDeclaredResourceType.getClass();
	}

	public List<IParameter> getParameters() {
		return myParameters;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return RestfulOperationTypeEnum.SEARCH_TYPE;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	public GetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		 assert (myQueryName == null || ((theArgs != null ? theArgs.length : 0) == myParameters.size())) : "Wrong number of arguments: " + (theArgs!=null?theArgs.length:"null");

		Map<String, List<String>> queryStringArgs = new LinkedHashMap<String, List<String>>();

		if (myQueryName != null) {
			queryStringArgs.put(Constants.PARAM_QUERY, Collections.singletonList(myQueryName));
		}

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = myParameters.get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(theArgs[idx], queryStringArgs);
			}
		}

		return new GetClientInvocation(queryStringArgs, getResourceName());
	}

	@Override
	public List<IResource> invokeServer(Object theResourceProvider, IdDt theId, IdDt theVersionId, Map<String, String[]> parameterValues) throws InvalidRequestException,
			InternalErrorException {
		assert theId == null;
		assert theVersionId == null;

		Object[] params = new Object[myParameters.size()];
		for (int i = 0; i < myParameters.size(); i++) {
			IParameter param = myParameters.get(i);
			params[i] = param.translateQueryParametersIntoServerArgument(parameterValues, null);
		}

		Object response;
		try {
			response = this.getMethod().invoke(theResourceProvider, params);
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
			ourLog.trace("Method {} doesn't match because resource name {} != {}", getMethod().getName(), theRequest.getResourceName(), getResourceName());
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
			if (!(myParameters.get(i) instanceof IQueryParameter)) {
				continue;
			}
			IQueryParameter temp = (IQueryParameter) myParameters.get(i);
			methodParamsTemp.add(temp.getName());
			if (temp.isRequired() && !theRequest.getParameters().containsKey(temp.getName())) {
				ourLog.trace("Method {} doesn't match param '{}' is not present", getMethod().getName(), temp.getName());
				return false;
			}
		}
		if (myQueryName != null) {
			String[] queryNameValues = theRequest.getParameters().get(Constants.PARAM_QUERY);
			if (queryNameValues != null && StringUtils.isNotBlank(queryNameValues[0])) {
				String queryName = queryNameValues[0];
				if (!myQueryName.equals(queryName)) {
					ourLog.trace("Query name does not match {}", myQueryName);
					return false;
				} else {
					methodParamsTemp.add(Constants.PARAM_QUERY);
				}
			} else {
				ourLog.trace("Query name does not match {}", myQueryName);
				return false;
			}
		}
		for (String next : theRequest.getParameters().keySet()) {
			if (ALLOWED_PARAMS.contains(next)) {
				methodParamsTemp.add(next);
			}
		}
		boolean retVal = methodParamsTemp.containsAll(theRequest.getParameters().keySet());

		ourLog.trace("Method {} matches: {}", getMethod().getName(), retVal);

		return retVal;
	}

	public void setParameters(List<IParameter> parameters) {
		this.myParameters = parameters;
	}

	public void setResourceType(Class<?> resourceType) {
		this.myDeclaredResourceType = resourceType;
	}

	public static enum RequestType {
		DELETE, GET, OPTIONS, POST, PUT
	}

}
