package ca.uhn.fhir.rest.param;

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ResourceParameter implements IParameter {

	private Class<? extends IResource> myResourceName ;

	public ResourceParameter(Class<? extends IResource> theParameterType) {
		myResourceName =theParameterType;
	}
	
	@Override
	public void translateClientArgumentIntoQueryArgument(Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments) throws InternalErrorException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(Map<String, String[]> theQueryParameters, Object theRequestContents) throws InternalErrorException, InvalidRequestException {
		IResource resource = (IResource) theRequestContents;
		return resource;
	}

	public Class<? extends IResource> getResourceType() {
		return myResourceName;
	}

//	public IResource
	
}
