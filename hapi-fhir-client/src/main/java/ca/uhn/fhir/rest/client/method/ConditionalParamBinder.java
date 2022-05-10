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

import ca.uhn.fhir.i18n.Msg;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

class ConditionalParamBinder implements IParameter {

	private boolean mySupportsMultiple;

	ConditionalParamBinder(RestOperationTypeEnum theOperationType, boolean theSupportsMultiple) {
		Validate.notNull(theOperationType, "theOperationType can not be null");
		mySupportsMultiple = theSupportsMultiple;
	}

	@Override
	public void initializeTypes(Method theMethod, Class<? extends Collection<?>> theOuterCollectionType, Class<? extends Collection<?>> theInnerCollectionType, Class<?> theParameterType) {
		if (theOuterCollectionType != null || theInnerCollectionType != null || theParameterType.equals(String.class) == false) {
			throw new ConfigurationException(Msg.code(1439) + "Parameters annotated with @" + ConditionalUrlParam.class.getSimpleName()  + " must be of type String, found incorrect parameter in method \"" + theMethod + "\"");
		}
	}

	public boolean isSupportsMultiple() {
		return mySupportsMultiple;
	}

	@Override
	public void translateClientArgumentIntoQueryArgument(FhirContext theContext, Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments, IBaseResource theTargetResource) throws InternalErrorException {
		throw new UnsupportedOperationException(Msg.code(1440) + "Can not use @" + getClass().getName() + " annotated parameters in client");
	}

}
