/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract class BaseCdsMethod implements ICdsMethod {
	private final Object myServiceBean;
	private final Method myMethod;

	BaseCdsMethod(Object theServiceBean, Method theMethod) {
		myServiceBean = theServiceBean;
		myMethod = theMethod;
	}

	public Object invoke(ObjectMapper theObjectMapper, IModelJson theJson, String theServiceId) {
		try {
			// If the method takes a String parameter, first serialize the json request before calling the method
			if (parameterIsString()) {
				String json = encodeRequest(theObjectMapper, theJson, theServiceId);

				return myMethod.invoke(myServiceBean, json);
			} else {
				return myMethod.invoke(myServiceBean, theJson);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			if (e.getCause() != null && e.getCause() instanceof BaseServerResponseException) {
				throw (BaseServerResponseException) e.getCause();
			}
			throw new ConfigurationException(
					Msg.code(2376) + "Failed to invoke "
							+ myMethod.getName() + " method on "
							+ myServiceBean.getClass().getName(),
					e);
		}
	}

	private boolean parameterIsString() {
		return String.class.isAssignableFrom(myMethod.getParameterTypes()[0]);
	}

	private String encodeRequest(
			ObjectMapper theObjectMapper, IModelJson theCdsServiceRequestJson, String theServiceId) {
		try {
			return theObjectMapper.writeValueAsString(theCdsServiceRequestJson);
		} catch (JsonProcessingException e) {
			throw new InvalidRequestException(
					Msg.code(2377)
							+ "Failed to deserialize CDS Hooks service request json instance when calling CDS Hooks Service "
							+ theServiceId,
					e);
		}
	}
}
