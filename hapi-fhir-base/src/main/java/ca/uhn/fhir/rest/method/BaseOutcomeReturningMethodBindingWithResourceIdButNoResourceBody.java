package ca.uhn.fhir.rest.method;

import java.lang.reflect.Method;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.server.IResourceProvider;

public abstract class BaseOutcomeReturningMethodBindingWithResourceIdButNoResourceBody extends BaseOutcomeReturningMethodBinding {

	private String myResourceName;
	private Integer myIdParameterIndex;

	public BaseOutcomeReturningMethodBindingWithResourceIdButNoResourceBody(Method theMethod, FhirContext theContext, Object theProvider, Class<?> theMethodAnnotationType, Class<? extends IBaseResource> theResourceTypeFromAnnotation) {
		super(theMethod, theContext, theMethodAnnotationType, theProvider);

		Class<? extends IBaseResource> resourceType = theResourceTypeFromAnnotation;
		if (resourceType != IResource.class) {
			RuntimeResourceDefinition def = theContext.getResourceDefinition(resourceType);
			myResourceName = def.getName();
		} else {
			if (theProvider != null && theProvider instanceof IResourceProvider) {
				RuntimeResourceDefinition def = theContext.getResourceDefinition(((IResourceProvider) theProvider).getResourceType());
				myResourceName = def.getName();
			} else {
				throw new ConfigurationException(
						"Can not determine resource type for method '" + theMethod.getName() + "' on type " + theMethod.getDeclaringClass().getCanonicalName() + " - Did you forget to include the resourceType() value on the @" + Delete.class.getSimpleName() + " method annotation?");
			}
		}

		myIdParameterIndex = MethodUtil.findIdParameterIndex(theMethod, getContext());
		if (myIdParameterIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName() + "' has no parameter annotated with the @" + IdParam.class.getSimpleName() + " annotation");
		}

		Integer versionIdParameterIndex = MethodUtil.findVersionIdParameterIndex(theMethod);
		if (versionIdParameterIndex != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' on type '" + theMethod.getDeclaringClass().getCanonicalName() + "' has a parameter annotated with the @" + VersionIdParam.class.getSimpleName() + " annotation but delete methods may not have this annotation");
		}

	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	protected Integer getIdParameterIndex() {
		return myIdParameterIndex;
	}


}
