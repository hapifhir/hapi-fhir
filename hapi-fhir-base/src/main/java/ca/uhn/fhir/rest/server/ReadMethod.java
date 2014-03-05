package ca.uhn.fhir.rest.server;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

class ReadMethod extends BaseMethod {

	private Method myMethod;
	private Integer myIdIndex;
	private Integer myVersionIdIndex;
	private int myParameterCount;

	ReadMethod(Method theMethod, Integer theIdIndex, Integer theVersionIdIndex) {
		Validate.notNull(theMethod, "Method must not be null");
		Validate.notNull(theIdIndex, "ID Index must not be null");
		
		myMethod = theMethod;
		myIdIndex = theIdIndex;
		myVersionIdIndex = theVersionIdIndex;
		myParameterCount = myMethod.getParameterTypes().length;
		
		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		if (!IdDt.class.equals(parameterTypes[myIdIndex])) {
			throw new ConfigurationException("ID parameter must be of type: " + IdDt.class.getCanonicalName() + " - Found: "+parameterTypes[myIdIndex]);
		}
		if (myVersionIdIndex != null && !IdDt.class.equals(parameterTypes[myVersionIdIndex])) {
			throw new ConfigurationException("Version ID parameter must be of type: " + IdDt.class.getCanonicalName()+ " - Found: "+parameterTypes[myVersionIdIndex]);
		}

	}

	@Override
	public boolean matches(String theResourceName, IdDt theId, IdDt theVersion, Set<String> theParameterNames) {
		if (!theResourceName.equals(getResource().getResourceName())) {
			return false;
		}
		if (theParameterNames.isEmpty() == false) {
			return false;
		}
		if ((theVersion == null) != (myVersionIdIndex == null)) {
			return false;
		}
		if (theId == null) {
			return false;
		}
		return true;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public List<IResource> invoke(IResourceProvider theResourceProvider, IdDt theId, IdDt theVersionId, Map<String, String[]> theParameterValues) throws InvalidRequestException,
			InternalErrorException {
		Object[] params = new Object[myParameterCount];
		params[myIdIndex] = theId;
		if (myVersionIdIndex != null) {
			params[myVersionIdIndex] = theVersionId;
		}
		
		Object response;
		try {
			response = myMethod.invoke(theResourceProvider, params);
		} catch (Exception e) {
			throw new InternalErrorException("Failed to call access method",e);
		}
		
		return toResourceList(response);
	}

}
