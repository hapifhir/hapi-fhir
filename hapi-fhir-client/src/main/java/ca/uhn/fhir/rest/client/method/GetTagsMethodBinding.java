package ca.uhn.fhir.rest.client.method;

import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class GetTagsMethodBinding extends BaseMethodBinding<TagList> {

	private Integer myIdParamIndex;
	private String myResourceName;
	private Class<? extends IBaseResource> myType;
	private Integer myVersionIdParamIndex;

	public GetTagsMethodBinding(Method theMethod, FhirContext theContext, Object theProvider, GetTags theAnnotation) {
		super(theMethod, theContext, theProvider);

		myType = theAnnotation.type();

		if (!Modifier.isInterface(myType.getModifiers())) {
			myResourceName = theContext.getResourceDefinition(myType).getName();
		}

		myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod, getContext());
		myVersionIdParamIndex = MethodUtil.findVersionIdParameterIndex(theMethod);

		if (myIdParamIndex != null && myType.equals(IResource.class)) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' does not specify a resource type, but has an @" + IdParam.class.getSimpleName()
					+ " parameter. Please specity a resource type in the @" + GetTags.class.getSimpleName() + " annotation");
		}
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.GET_TAGS;
	}


	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		HttpGetClientInvocation retVal;

		IdDt id = null;
		IdDt versionId = null;
		if (myIdParamIndex != null) {
			id = (IdDt) theArgs[myIdParamIndex];
			if (myVersionIdParamIndex != null) {
				versionId = (IdDt) theArgs[myVersionIdParamIndex];
			}
		}

		if (myType != IResource.class) {
			if (id != null) {
				if (versionId != null) {
					retVal = new HttpGetClientInvocation(getContext(), getResourceName(), id.getIdPart(), Constants.PARAM_HISTORY, versionId.getValue(), Constants.PARAM_TAGS);
				} else if (id.hasVersionIdPart()) {
					retVal = new HttpGetClientInvocation(getContext(), getResourceName(), id.getIdPart(), Constants.PARAM_HISTORY, id.getVersionIdPart(), Constants.PARAM_TAGS);
				} else {
					retVal = new HttpGetClientInvocation(getContext(), getResourceName(), id.getIdPart(), Constants.PARAM_TAGS);
				}
			} else {
				retVal = new HttpGetClientInvocation(getContext(), getResourceName(), Constants.PARAM_TAGS);
			}
		} else {
			retVal = new HttpGetClientInvocation(getContext(), Constants.PARAM_TAGS);
		}

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
			}
		}

		return retVal;
	}

	@Override
	public TagList invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
		if (theResponseStatusCode == Constants.STATUS_HTTP_200_OK) {
			IParser parser = createAppropriateParserForParsingResponse(theResponseMimeType, theResponseReader, theResponseStatusCode, null);
			TagList retVal = parser.parseTagList(theResponseReader);
			return retVal;
		}
		throw processNon2xxResponseAndReturnExceptionToThrow(theResponseStatusCode, theResponseMimeType, theResponseReader);
	}

}
