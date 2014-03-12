package ca.uhn.fhir.rest.common;

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.common.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ConformanceMethodBinding extends BaseMethodBinding {

	public ConformanceMethodBinding(MethodReturnTypeEnum theMethodReturnType) {
		super(theMethodReturnType, Conformance.class);
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
	public List<IResource> invokeServer(IResourceProvider theResourceProvider, IdDt theId, IdDt theVersionId, Map<String, String[]> theParameterValues) throws InvalidRequestException, InternalErrorException {
		return null;
	}

	@Override
	public boolean matches(Request theRequest) {
		if (theRequest.getRequestType() == RequestType.OPTIONS) {
			return true;
		}
		
		if (theRequest.getRequestType() == RequestType.GET && "metadata".equals(theRequest.getOperation())) {
			return false;
		}
		
		return false;
	}

}
