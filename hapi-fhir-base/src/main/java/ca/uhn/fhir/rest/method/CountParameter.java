package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class CountParameter implements IParameter {

	private Class<?> myType;

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		if (theSourceClientArgument != null) {
			IntegerDt since = ParameterUtil.toInteger(theSourceClientArgument);
			if (since.isEmpty() == false) {
				theTargetQueryArguments.put(Constants.PARAM_COUNT, Collections.singletonList(since.getValueAsString()));
			}
		}
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		String[] sinceParams = theRequest.getParameters().remove(Constants.PARAM_COUNT);
		if (sinceParams != null) {
			if (sinceParams.length > 0) {
				if (StringUtils.isNotBlank(sinceParams[0])) {
					try {
						IntegerDt since = new IntegerDt(sinceParams[0]);
						return ParameterUtil.fromInteger(myType, since);
					} catch (DataFormatException e) {
						throw new InvalidRequestException("Invalid " + Constants.PARAM_COUNT + " value: " + sinceParams[0]);
					}
				}
			}
		}
		return ParameterUtil.fromInteger(myType, null);
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Since.class.getName() + " but can not be of collection type");
		}
		if (!ParameterUtil.getBindableIntegerTypes().contains(theParameterType)) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Since.class.getName() + " but type '" + theParameterType + "' is an invalid type, must be one of: " + ParameterUtil.getBindableIntegerTypes());
		}
		myType = theParameterType;
	}

}
