package ca.uhn.fhir.rest.server.method;

import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.TagListParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.client.method.RequestDetails;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

abstract class BaseAddOrDeleteTagsMethodBinding extends BaseMethodBinding<Void> {

	private Class<? extends IBaseResource> myType;
	private Integer myIdParamIndex;
	private Integer myVersionIdParamIndex;
	private String myResourceName;
	private Integer myTagListParamIndex;

	public BaseAddOrDeleteTagsMethodBinding(Method theMethod, FhirContext theContext, Object theProvider, Class<? extends IBaseResource> theTypeFromMethodAnnotation) {
		super(theMethod, theContext, theProvider);

		if (theProvider instanceof IResourceProvider) {
			myType = ((IResourceProvider) theProvider).getResourceType();
		} else {
			myType = theTypeFromMethodAnnotation;
		}

		if (Modifier.isInterface(myType.getModifiers())) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' does not specify a resource type, but has an @" + IdParam.class.getSimpleName() + " parameter. Please specity a resource type in the method annotation on this method");
		}
		
		myResourceName = theContext.getResourceDefinition(myType).getName();

		myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod, getContext());
		myVersionIdParamIndex = MethodUtil.findVersionIdParameterIndex(theMethod);
		myTagListParamIndex = MethodUtil.findTagListParameterIndex(theMethod);

		if (myIdParamIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' does not have an @" + IdParam.class.getSimpleName() + " parameter.");
		}

		if (myTagListParamIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' does not have a parameter of type " + TagList.class.getSimpleName() + ", or paramater is not annotated with the @" + TagListParam.class.getSimpleName() + " annotation");
		}

	}

	@Override
	public Void invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
		switch (theResponseStatusCode) {
		case Constants.STATUS_HTTP_200_OK:
		case Constants.STATUS_HTTP_201_CREATED:
		case Constants.STATUS_HTTP_204_NO_CONTENT:
			return null;
		default:
			throw processNon2xxResponseAndReturnExceptionToThrow(theResponseStatusCode, theResponseMimeType, theResponseReader);
		}

	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return null;
	}

	protected abstract boolean isDelete();

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		HttpPostClientInvocation retVal;

		IdDt id = (IdDt) theArgs[myIdParamIndex];
		if (id == null || id.isEmpty()) {
			throw new InvalidRequestException("ID must not be null or empty for this operation");
		}

		IdDt versionId = null;
		if (myVersionIdParamIndex != null) {
			versionId = (IdDt) theArgs[myVersionIdParamIndex];
		}

		TagList tagList = (TagList) theArgs[myTagListParamIndex];

		Class<? extends IBaseResource> type = myType;
		assert type != null;

		if (isDelete()) {
			if (versionId != null) {
				retVal = new HttpPostClientInvocation(getContext(), tagList, getResourceName(), id.getValue(), Constants.PARAM_HISTORY, versionId.getValue(), Constants.PARAM_TAGS, Constants.PARAM_DELETE);
			} else {
				retVal = new HttpPostClientInvocation(getContext(), tagList, getResourceName(), id.getValue(), Constants.PARAM_TAGS, Constants.PARAM_DELETE);
			}
		} else {
			if (versionId != null) {
				retVal = new HttpPostClientInvocation(getContext(), tagList, getResourceName(), id.getValue(), Constants.PARAM_HISTORY, versionId.getValue(), Constants.PARAM_TAGS);
			} else {
				retVal = new HttpPostClientInvocation(getContext(), tagList, getResourceName(), id.getValue(), Constants.PARAM_TAGS);
			}
		}
		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
		}

		return retVal;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (theRequest.getRequestType() != RequestTypeEnum.POST) {
			return false;
		}
		if (!Constants.PARAM_TAGS.equals(theRequest.getOperation())) {
			return false;
		}

		if (!myResourceName.equals(theRequest.getResourceName())) {
			return false;
		}

		if (theRequest.getId() == null) {
			return false;
		}

		if (isDelete()) {
			if (Constants.PARAM_DELETE.equals(theRequest.getSecondaryOperation()) == false) {
				return false;
			}
		} else {
			if (theRequest.getSecondaryOperation() != null) {
				return false;
			}
		}

		return true;
	}

}
