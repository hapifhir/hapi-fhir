package ca.uhn.fhir.rest.server.method;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.i18n.Msg;
import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Offset;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class OffsetParameter implements IParameter {

	private Class<?> myType;

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		String[] sinceParams = theRequest.getParameters().get(Constants.PARAM_OFFSET);
		if (sinceParams != null) {
			if (sinceParams.length > 0) {
				if (StringUtils.isNotBlank(sinceParams[0])) {
					try {
						IntegerDt since = new IntegerDt(sinceParams[0]);
						return ParameterUtil.fromInteger(myType, since);
					} catch (DataFormatException e) {
						throw new InvalidRequestException(Msg.code(461) + "Invalid " + Constants.PARAM_OFFSET + " value: " + sinceParams[0]);
					}
				}
			}
		}
		return ParameterUtil.fromInteger(myType, null);
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException(Msg.code(462) + "Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Offset.class.getName() + " but can not be of collection type");
		}
		if (!ParameterUtil.isBindableIntegerType(theParameterType)) {
			throw new ConfigurationException(Msg.code(463) + "Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Offset.class.getName() + " but type '" + theParameterType + "' is an invalid type, must be one of Integer or IntegerType");
		}
		myType = theParameterType;
	}

}
