package ca.uhn.fhir.rest.method;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ConformanceMethodBinding extends BaseResourceReturningMethodBinding {

	public ConformanceMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(Conformance.class, theMethod, theContext, theProvider);

		if (getMethodReturnType() != MethodReturnTypeEnum.RESOURCE || theMethod.getReturnType() != Conformance.class) {
			throw new ConfigurationException("Conformance resource provider method '" + theMethod.getName() + "' should return type " + Conformance.class);
		}

	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public GetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		return new GetClientInvocation("metadata");
	}

	@Override
	public List<IResource> invokeServer(Object theResourceProvider, Request theRequest) throws InvalidRequestException,
			InternalErrorException {
		IResource conf;
		try {
			conf = (Conformance) getMethod().invoke(theResourceProvider);
		} catch (Exception e) {
			throw new InternalErrorException("Failed to call access method", e);
		}

		return Collections.singletonList(conf);
	}

	@Override
	public boolean matches(Request theRequest) {
		if (theRequest.getRequestType() == RequestType.OPTIONS) {
			return true;
		}

		if (theRequest.getRequestType() == RequestType.GET && "metadata".equals(theRequest.getOperation())) {
			return true;
		}

		return false;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return null;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

}
