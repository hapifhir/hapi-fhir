/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.lang.reflect.Method;
import java.util.Collection;

class ConditionalParamBinder implements IParameter {

	private boolean mySupportsMultiple;

	ConditionalParamBinder(boolean theSupportsMultiple) {
		mySupportsMultiple = theSupportsMultiple;
	}

	@Override
	public void initializeTypes(
			Method theMethod,
			Class<? extends Collection<?>> theOuterCollectionType,
			Class<? extends Collection<?>> theInnerCollectionType,
			Class<?> theParameterType) {
		if (theOuterCollectionType != null
				|| theInnerCollectionType != null
				|| theParameterType.equals(String.class) == false) {
			throw new ConfigurationException(
					Msg.code(409) + "Parameters annotated with @" + ConditionalUrlParam.class.getSimpleName()
							+ " must be of type String, found incorrect parameter in method \"" + theMethod + "\"");
		}
	}

	public boolean isSupportsMultiple() {
		return mySupportsMultiple;
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(
			RequestDetails theRequest, BaseMethodBinding theMethodBinding)
			throws InternalErrorException, InvalidRequestException {
		return theRequest.getConditionalUrl(theMethodBinding.getRestOperationType());
	}
}
