package ca.uhn.fhir.rest.method;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.client.DeleteClientInvocation;
import ca.uhn.fhir.rest.client.PostClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class DeleteMethodBinding extends BaseOutcomeReturningMethodBinding {

	private String myResourceName;
	private Integer myIdParameterIndex;

	public DeleteMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Delete.class, 
				theProvider);

		Delete deleteAnnotation = theMethod.getAnnotation(Delete.class);
		Class<? extends IResource> resourceType = deleteAnnotation.resourceType();
		if (resourceType != Delete.NotSpecified.class) {
			RuntimeResourceDefinition def = theContext.getResourceDefinition(resourceType);
			myResourceName = def.getName();
		} else {
			if (theProvider != null && theProvider instanceof IResourceProvider) {
				RuntimeResourceDefinition def = theContext.getResourceDefinition(((IResourceProvider) theProvider).getResourceType());
				myResourceName = def.getName();
			} else {
				throw new ConfigurationException("Can not determine resource type for method '" + theMethod.getName() + "' on type " + theMethod.getDeclaringClass().getCanonicalName() + " - Did you forget to include the resourceType() value on the @"
						+ Delete.class.getSimpleName() + " method annotation?");
			}
		}

		myIdParameterIndex = Util.findIdParameterIndex(theMethod);
		if (myIdParameterIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName() + "' has no parameter annotated with the @" + IdParam.class.getSimpleName() + " annotation");
		}

		Integer versionIdParameterIndex = Util.findVersionIdParameterIndex(theMethod);
		if (versionIdParameterIndex != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName() + "' has a parameter annotated with the @" + VersionIdParam.class.getSimpleName()
					+ " annotation but delete methods may not have this annotation");
		}

	}

	@Override
	protected boolean allowVoidReturnType() {
		return true;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return RestfulOperationTypeEnum.DELETE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	protected Set<RequestType> provideAllowableRequestTypes() {
		return Collections.singleton(RequestType.DELETE);
	}

	@Override
	protected BaseClientInvocation createClientInvocation(Object[] theArgs, IResource resource, String resourceName) {
		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(resourceName);

		return new PostClientInvocation(getContext(), resource, urlExtension.toString());
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	// @Override
	// public boolean matches(Request theRequest) {
	// // TODO Auto-generated method stub
	// return super.matches(theRequest);
	// }

	@Override
	public BaseClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		IdDt idDt = (IdDt) theArgs[myIdParameterIndex];
		if (idDt == null) {
			throw new NullPointerException("ID can not be null");
		}
		String id = idDt.getValue();

		DeleteClientInvocation retVal = new DeleteClientInvocation(getResourceName(), id);

		return retVal;
	}

	@Override
	protected void addParametersForServerRequest(Request theRequest, Object[] theParams) {
		String url = theRequest.getCompleteUrl();
		int resNameIdx = url.indexOf(getResourceName());
		String id = url.substring(resNameIdx+getResourceName().length() + 1);
		if (id.contains("/")) {
			throw new InvalidRequestException("Invalid request path for a DELETE operation: "+theRequest.getCompleteUrl());
		}
		
		theParams[myIdParameterIndex] = new IdDt(id);
	}

}
