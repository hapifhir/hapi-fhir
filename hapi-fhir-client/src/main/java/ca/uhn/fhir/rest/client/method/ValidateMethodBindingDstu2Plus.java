package ca.uhn.fhir.rest.client.method;

/*
 * #%L
 * HAPI FHIR - Client Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.util.ParametersUtil;

public class ValidateMethodBindingDstu2Plus extends OperationMethodBinding {

	public ValidateMethodBindingDstu2Plus(Class<?> theReturnResourceType, Class<? extends IBaseResource> theReturnTypeFromRp, Method theMethod, FhirContext theContext, Object theProvider,
			Validate theAnnotation) {
		super(null, theReturnTypeFromRp, theMethod, theContext, theProvider, true, Constants.EXTOP_VALIDATE, theAnnotation.type(), BundleTypeEnum.COLLECTION);

		List<IParameter> newParams = new ArrayList<IParameter>();
		int idx = 0;
		for (IParameter next : getParameters()) {
			if (next instanceof ResourceParameter) {
				if (IBaseResource.class.isAssignableFrom(((ResourceParameter) next).getResourceType())) {
					Class<?> parameterType = theMethod.getParameterTypes()[idx];
					if (String.class.equals(parameterType) || EncodingEnum.class.equals(parameterType)) {
						newParams.add(next);
					} else {
						OperationParameter parameter = new OperationParameter(theContext, Constants.EXTOP_VALIDATE, Constants.EXTOP_VALIDATE_RESOURCE, 0, 1);
						parameter.initializeTypes(theMethod, null, null, parameterType);
						newParams.add(parameter);
					}
				} else {
					newParams.add(next);
				}
			} else {
				newParams.add(next);
			}
			idx++;
		}
		setParameters(newParams);

	}
	
	
	public static BaseHttpClientInvocation createValidateInvocation(FhirContext theContext, IBaseResource theResource) {
		IBaseParameters parameters = (IBaseParameters) theContext.getResourceDefinition("Parameters").newInstance();
		ParametersUtil.addParameterToParameters(theContext, parameters, "resource", theResource);
		
		String resourceName = theContext.getResourceType(theResource);
		String resourceId = theResource.getIdElement().getIdPart();
		
		BaseHttpClientInvocation retVal = createOperationInvocation(theContext, resourceName, resourceId, null,Constants.EXTOP_VALIDATE, parameters, false);
		return retVal;
	}


}
