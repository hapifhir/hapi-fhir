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
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;

public class GraphQLQueryUrlParameter implements IParameter {

	private Class<?> myType;

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		String[] queryParams = theRequest.getParameters().get(Constants.PARAM_GRAPHQL_QUERY);
		String retVal = null;
		if (queryParams != null) {
			if (queryParams.length > 0) {
				retVal = queryParams[0];
			}
		}
		return retVal;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException(Msg.code(459) + "Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Count.class.getName() + " but can not be of collection type");
		}
		if (!String.class.equals(theParameterType)) {
			throw new ConfigurationException(Msg.code(460) + "Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Count.class.getName() + " but type '" + theParameterType + "' is an invalid type, must be one of Integer or IntegerType");
		}
		myType = theParameterType;
	}

}
