package ca.uhn.fhir.rest.server.method;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.parser.json.JsonLikeObject;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeValue;
import ca.uhn.fhir.parser.json.jackson.JacksonStructure;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Collection;

import static ca.uhn.fhir.rest.api.Constants.CT_GRAPHQL;
import static ca.uhn.fhir.rest.api.Constants.CT_JSON;
import static ca.uhn.fhir.rest.server.method.ResourceParameter.createRequestReader;

public class GraphQLQueryBodyParameter implements IParameter {

	private Class<?> myType;

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		String ctValue = theRequest.getHeader(Constants.HEADER_CONTENT_TYPE);
		Reader requestReader = createRequestReader(theRequest);

		if (CT_JSON.equals(ctValue)) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(requestReader);
				if (jsonNode != null && jsonNode.get("query") != null) {
					return jsonNode.get("query").asText();
				}
			} catch (IOException e) {
				throw new InternalErrorException(e);
			}
		}

		if (CT_GRAPHQL.equals(ctValue)) {
			try {
				return IOUtils.toString(requestReader);
			} catch (IOException e) {
				throw new InternalErrorException(e);
			}
		}

		return null;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Count.class.getName() + " but can not be of collection type");
		}
		if (!String.class.equals(theParameterType)) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' in type '" +theMethod.getDeclaringClass().getCanonicalName()+ "' is annotated with @" + Count.class.getName() + " but type '" + theParameterType + "' is an invalid type, must be one of Integer or IntegerType");
		}
		myType = theParameterType;
	}

}
