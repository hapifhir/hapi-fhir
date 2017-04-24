package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.reflect.Method;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
//TODO Use of a deprecated method should be resolved
import ca.uhn.fhir.rest.annotation.VersionIdParam;
import ca.uhn.fhir.rest.server.IResourceProvider;

public abstract class BaseOutcomeReturningMethodBindingWithResourceIdButNoResourceBody extends BaseOutcomeReturningMethodBinding {

	private String myResourceName;
	private Integer myIdParameterIndex;

	public BaseOutcomeReturningMethodBindingWithResourceIdButNoResourceBody(Method theMethod, FhirContext theContext, Object theProvider, Class<?> theMethodAnnotationType, Class<? extends IBaseResource> theResourceTypeFromAnnotation) {
		super(theMethod, theContext, theMethodAnnotationType, theProvider);

		Class<? extends IBaseResource> resourceType = theResourceTypeFromAnnotation;
		if (resourceType != IBaseResource.class) {
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
			//TODO Use of a deprecated method should be resolved
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
