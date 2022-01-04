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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Collection;

import static ca.uhn.fhir.rest.api.Constants.CT_GRAPHQL;
import static ca.uhn.fhir.rest.api.Constants.CT_JSON;
import static ca.uhn.fhir.rest.server.method.ResourceParameter.createRequestReader;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.trim;

public class GraphQLQueryBodyParameter implements IParameter {

	private Class<?> myType;

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		String ctValue = defaultString(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE));
		Reader requestReader = createRequestReader(theRequest);

		// Trim off "; charset=FOO" from the content-type header
		int semicolonIdx = ctValue.indexOf(';');
		if (semicolonIdx != -1) {
			ctValue = ctValue.substring(0, semicolonIdx);
		}
		ctValue = trim(ctValue);

		if (CT_JSON.equals(ctValue)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(requestReader);
				if (jsonNode != null && jsonNode.get("query") != null) {
					return jsonNode.get("query").asText();
				}
			} catch (IOException e) {
				throw new InternalErrorException(Msg.code(356) + e);
			}
		}

		if (CT_GRAPHQL.equals(ctValue)) {
			try {
				return IOUtils.toString(requestReader);
			} catch (IOException e) {
				throw new InternalErrorException(Msg.code(357) + e);
			}
		}

		return null;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException(Msg.code(358) + "Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Count.class.getName() + " but can not be of collection type");
		}
		if (!String.class.equals(theParameterType)) {
			throw new ConfigurationException(Msg.code(359) + "Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Count.class.getName() + " but type '" + theParameterType + "' is an invalid type, must be one of Integer or IntegerType");
		}
		myType = theParameterType;
	}

}
