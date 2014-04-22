package ca.uhn.fhir.rest.method;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.client.BaseClientInvocation;
import ca.uhn.fhir.rest.client.PostClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;

public class CreateMethodBinding extends BaseOutcomeReturningMethodBindingWithResourceParam {

	

	public CreateMethodBinding(Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, Create.class, theProvider);
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
	protected Set<RequestType> provideAllowableRequestTypes() {
		return Collections.singleton(RequestType.POST);
	}

	@Override
	protected BaseClientInvocation createClientInvocation(Object[] theArgs, IResource resource, String resourceName) {
		StringBuilder urlExtension = new StringBuilder();
		urlExtension.append(resourceName);

		return new PostClientInvocation(getContext(), resource, urlExtension.toString());
	}

}
