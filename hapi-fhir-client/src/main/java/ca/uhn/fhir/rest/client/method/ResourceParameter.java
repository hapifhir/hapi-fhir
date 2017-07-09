package ca.uhn.fhir.rest.client.method;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class ResourceParameter implements IParameter {

	private Class<? extends IBaseResource> myResourceType;

	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public ResourceParameter(Class<?> theParameterType) {
		if (IBaseResource.class.isAssignableFrom(theParameterType)) {
			myResourceType = (Class<? extends IBaseResource>) theParameterType;
		}
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// ignore for now
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource)
			throws InternalErrorException {
		// ignore, as this is handles as a special case
	}

	public enum Mode {
		BODY,
		BODY_BYTE_ARRAY,
		ENCODING,
		RESOURCE
	}

	public Class<? extends IBaseResource> getResourceType() {
		return myResourceType;
	}

}
