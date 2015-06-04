package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.RestfulServer.NarrativeModeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

class NarrativeModeParameter implements IParameter {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServletResponseParameter.class);

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource)
			throws InternalErrorException {
		if (theSourceClientArgument != null) {
			NarrativeModeEnum n = (NarrativeModeEnum) theSourceClientArgument;
			theTargetQueryArguments.put(Constants.PARAM_NARRATIVE, Collections.singletonList(n.name().toLowerCase()));
		}
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(RequestDetails theRequest, byte[] theRequestContents, BaseMethodBinding<?> theMethodBinding) throws InternalErrorException, InvalidRequestException {
		String val = theRequest.getServletRequest().getParameter(Constants.PARAM_NARRATIVE);
		if (val != null) {
			try {
				return NarrativeModeEnum.valueOfCaseInsensitive(val);
			} catch (IllegalArgumentException e) {
				ourLog.debug("Invalid {} parameger: {}", Constants.PARAM_NARRATIVE, val);
				return null;
			}
		}
		return null;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		// ignore
	}

}
