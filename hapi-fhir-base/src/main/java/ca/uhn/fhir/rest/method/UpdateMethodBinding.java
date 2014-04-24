package ca.uhn.fhir.rest.method;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.client.PutClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

class UpdateMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	private Integer myIdParameterIndex;

	private Integer myVersionIdParameterIndex;

	public UpdateMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Update.class, theProvider);

		myIdParameterIndex = Util.findIdParameterIndex(theMethod);
		if (myIdParameterIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName() + "' has no parameter annotated with the @" + IdParam.class.getSimpleName() + " annotation");
		}
		myVersionIdParameterIndex = Util.findVersionIdParameterIndex(theMethod);
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return RestfulOperationTypeEnum.UPDATE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	protected void addParametersForServerRequest(Request theRequest, Object[] theParams) {
		/*
		 * We are being a bit lenient here, since technically the client is
		 * supposed to include the version in the Content-Location header, but
		 * we allow it in the PUT URL as well..
		 */
		String locationHeader = theRequest.getServletRequest().getHeader(Constants.HEADER_CONTENT_LOCATION);
		if (isNotBlank(locationHeader)) {
			MethodOutcome mo = new MethodOutcome();
			parseContentLocation(mo, locationHeader);
			if (mo.getId() == null || mo.getId().isEmpty()) {
				throw new InvalidRequestException("Invalid Content-Location header for resource " + getResourceName()+ ": " + locationHeader);
			}
			if (mo.getVersionId() != null && mo.getVersionId().isEmpty() == false) {
				theRequest.setVersion(mo.getVersionId());
			}
		}

		// TODO: we should send an HTTP 412 automatically if the server
		// only has a method which requires a version ID, and no
		// Content-Location header is present

		theParams[myIdParameterIndex] = theRequest.getId();
		if (myVersionIdParameterIndex != null) {
			theParams[myVersionIdParameterIndex] = theRequest.getVersion();
		}
	}

	@Override
	protected BaseClientInvocation createClientInvocation(Object[] theArgs, IResource resource, String resourceName) {
		IdDt idDt = (IdDt) theArgs[myIdParameterIndex];
		if (idDt == null) {
			throw new NullPointerException("ID can not be null");
		}
		String id = idDt.getValue();

		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(resourceName);
		urlExtension.append('/');
		urlExtension.append(id);
		PutClientInvocation retVal = new PutClientInvocation(getContext(), resource, urlExtension.toString());

		if (myVersionIdParameterIndex != null) {
			IdDt versionIdDt = (IdDt) theArgs[myVersionIdParameterIndex];
			if (versionIdDt != null) {
				String versionId = versionIdDt.getValue();
				if (StringUtils.isNotBlank(versionId)) {
					StringBuilder b = new StringBuilder();
					b.append('/');
					b.append(urlExtension);
					b.append("/_history/");
					b.append(versionId);
					retVal.addHeader(Constants.HEADER_CONTENT_LOCATION, b.toString());
				}
			}
		}

		return retVal;
	}

	@Override
	protected Set<RequestType> provideAllowableRequestTypes() {
		return Collections.singleton(RequestType.PUT);
	}

}
