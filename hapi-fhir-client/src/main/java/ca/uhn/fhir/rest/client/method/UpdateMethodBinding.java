package ca.uhn.fhir.rest.client.method;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class UpdateMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	private Integer myIdParameterIndex;

	public UpdateMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Update.class, theProvider);

		myIdParameterIndex = ParameterUtil.findIdParameterIndex(theMethod, getContext());
	}


	@Override
	protected BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IResource theResource) {
		IdDt idDt = (IdDt) theArgs[myIdParameterIndex];
		if (idDt == null) {
			throw new NullPointerException("ID can not be null");
		}

		FhirContext context = getContext();

		HttpPutClientInvocation retVal = MethodUtil.createUpdateInvocation(theResource, null, idDt, context);

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
		}

		return retVal;
	}

	@Override
	protected String getMatchingOperation() {
		return null;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.UPDATE;
	}

	/*
	 * @Override public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) { if
	 * (super.incomingServerRequestMatchesMethod(theRequest)) { if (myVersionIdParameterIndex != null) { if
	 * (theRequest.getVersionId() == null) { return false; } } else { if (theRequest.getVersionId() != null) { return
	 * false; } } return true; } else { return false; } }
	 */

	@Override
	protected Set<RequestTypeEnum> provideAllowableRequestTypes() {
		return Collections.singleton(RequestTypeEnum.PUT);
	}

	@Override
	protected void validateResourceIdAndUrlIdForNonConditionalOperation(IBaseResource theResource, String theResourceId, String theUrlId, String theMatchUrl) {
		if (isBlank(theMatchUrl)) {
			if (isBlank(theUrlId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "noIdInUrlForUpdate");
				throw new InvalidRequestException(msg);
			}
			if (isBlank(theResourceId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "noIdInBodyForUpdate");
				throw new InvalidRequestException(msg);
			}
			if (!theResourceId.equals(theUrlId)) {
				String msg = getContext().getLocalizer().getMessage(BaseOutcomeReturningMethodBindingWithResourceParam.class, "incorrectIdForUpdate", theResourceId, theUrlId);
				throw new InvalidRequestException(msg);
			}
		} else {
			theResource.setId((IIdType)null);
		}
		
	}
}
