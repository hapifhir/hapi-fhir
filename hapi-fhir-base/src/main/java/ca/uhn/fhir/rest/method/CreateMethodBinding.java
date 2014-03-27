package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class CreateMethodBinding extends BaseMethodBinding {

	public CreateMethodBinding(Method theMethod, FhirContext theContext) {
		super(theMethod, theContext);
	}

	@Override
	public GetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return RestfulOperationTypeEnum.CREATE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	public boolean matches(Request theRequest) {
		if (theRequest.getRequestType()!= RequestType.POST) {
			return false;
		}
		if (StringUtils.isBlank(theRequest.getResourceName())) {
			return false;
		}
		if (StringUtils.isNotBlank(theRequest.getOperation())) {
			return false;
		}
		return true;
	}

	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
