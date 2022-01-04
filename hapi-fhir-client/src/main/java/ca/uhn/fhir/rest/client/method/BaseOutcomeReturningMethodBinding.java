package ca.uhn.fhir.rest.client.method;

/*-
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
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.impl.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

abstract class BaseOutcomeReturningMethodBinding extends BaseMethodBinding<MethodOutcome> {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseOutcomeReturningMethodBinding.class);

	private boolean myReturnVoid;

	public BaseOutcomeReturningMethodBinding(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theProvider);

		if (!theMethod.getReturnType().equals(MethodOutcome.class)) {
			if (!allowVoidReturnType()) {
				throw new ConfigurationException(Msg.code(1413) + "Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " is a @" + theMethodAnnotation.getSimpleName() + " method but it does not return " + MethodOutcome.class);
			} else if (theMethod.getReturnType() == void.class) {
				myReturnVoid = true;
			}
		}
	}

	/**
	 * Subclasses may override to allow a void method return type, which is allowable for some methods (e.g. delete)
	 */
	protected boolean allowVoidReturnType() {
		return false;
	}

	protected abstract BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IBaseResource resource);

	/**
	 * For servers, this method will match only incoming requests that match the given operation, or which have no
	 * operation in the URL if this method returns null.
	 */
	protected abstract String getMatchingOperation();

	@Override
	public MethodOutcome invokeClient(String theResponseMimeType, InputStream theResponseInputStream, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
		if (theResponseStatusCode >= 200 && theResponseStatusCode < 300) {
			if (myReturnVoid) {
				return null;
			}
			MethodOutcome retVal = MethodUtil.process2xxResponse(getContext(), theResponseStatusCode, theResponseMimeType, theResponseInputStream, theHeaders);
			return retVal;
		}
		throw processNon2xxResponseAndReturnExceptionToThrow(theResponseStatusCode, theResponseMimeType, theResponseInputStream);
	}

	public boolean isReturnVoid() {
		return myReturnVoid;
	}

	protected abstract Set<RequestTypeEnum> provideAllowableRequestTypes();



	protected static void parseContentLocation(FhirContext theContext, MethodOutcome theOutcomeToPopulate, String theLocationHeader) {
		if (StringUtils.isBlank(theLocationHeader)) {
			return;
		}

		IIdType id = theContext.getVersion().newIdType();
		id.setValue(theLocationHeader);
		theOutcomeToPopulate.setId(id);
	}

}
