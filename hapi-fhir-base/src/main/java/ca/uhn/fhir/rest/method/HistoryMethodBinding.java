package ca.uhn.fhir.rest.method;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class HistoryMethodBinding extends BaseMethodBinding {

	private final Integer myIdParamIndex;
	private final RestfulOperationTypeEnum myResourceOperationType;
	private final Class<? extends IResource> myType;
	private final RestfulOperationSystemEnum mySystemOperationType;
	private String myResourceName;
	private Integer mySinceParamIndex;
	private Integer myCountParamIndex;

	public HistoryMethodBinding(Method theMethod, FhirContext theConetxt, IResourceProvider theProvider) {
		super(theMethod, theConetxt);

		myIdParamIndex = Util.findIdParameterIndex(theMethod);
		mySinceParamIndex = Util.findSinceParameterIndex(theMethod);
		myCountParamIndex = Util.findCountParameterIndex(theMethod);

		History historyAnnotation = theMethod.getAnnotation(History.class);
		Class<? extends IResource> type = historyAnnotation.resourceType();
		if (type == History.AllResources.class) {
			if (theProvider != null) {
				type = theProvider.getResourceType();
				if (myIdParamIndex != null) {
					myResourceOperationType = RestfulOperationTypeEnum.HISTORY_INSTANCE;
				} else {
					myResourceOperationType = RestfulOperationTypeEnum.HISTORY_TYPE;
				}
				mySystemOperationType = null;
			} else {
				myResourceOperationType = null;
				mySystemOperationType = RestfulOperationSystemEnum.HISTORY_SYSTEM;
			}
		} else {
			if (myIdParamIndex != null) {
				myResourceOperationType = RestfulOperationTypeEnum.HISTORY_INSTANCE;
			} else {
				myResourceOperationType = RestfulOperationTypeEnum.HISTORY_TYPE;
			}
			mySystemOperationType = null;
		}

		if (type != History.AllResources.class) {
			myResourceName = theConetxt.getResourceDefinition(type).getName();
			myType = type;
		} else {
			myResourceName = null;
			myType = null;
		}

	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return myResourceOperationType;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return mySystemOperationType;
	}

	@Override
	public BaseClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException {
		Object[] args = new Object[getMethod().getParameterTypes().length];
		if (myCountParamIndex != null) {
			String[] countValues = theRequest.getParameters().remove(Constants.PARAM_COUNT);
			if (countValues.length > 0 && StringUtils.isNotBlank(countValues[0])) {
				try {
					args[myCountParamIndex] = new IntegerDt(countValues[0]);
				} catch (DataFormatException e) {
					throw new InvalidRequestException("Invalid _count parameter value: " + countValues[0]);
				}
			}
		}
		if (mySinceParamIndex != null) {
			String[] sinceValues = theRequest.getParameters().remove(Constants.PARAM_SINCE);
			if (sinceValues.length > 0 && StringUtils.isNotBlank(sinceValues[0])) {
				try {
					args[mySinceParamIndex] = new InstantDt(sinceValues[0]);
				} catch (DataFormatException e) {
					throw new InvalidRequestException("Invalid _since parameter value: " + sinceValues[0]);
				}
			}
		}
	}

	@Override
	public boolean matches(Request theRequest) {
		if (!theRequest.getOperation().equals(Constants.PARAM_HISTORY)) {
			return false;
		}
		if (theRequest.getResourceName() == null) {
			return mySystemOperationType == RestfulOperationSystemEnum.HISTORY_SYSTEM;
		}
		if (!theRequest.getResourceName().equals(myResourceName)) {
			return false;
		}

		return false;
	}

}
