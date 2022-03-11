package ca.uhn.fhir.rest.param.binder;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.util.Collections;
import java.util.List;

public final class QueryParameterOrBinder extends BaseBinder<IQueryParameterOr<?>> implements IParamBinder<IQueryParameterOr<?>> {

	public QueryParameterOrBinder(Class<? extends IQueryParameterOr<?>> theType, List<Class<? extends IQueryParameterType>> theCompositeTypes) {
		super(theType, theCompositeTypes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IQueryParameterOr<?>> encode(FhirContext theContext, IQueryParameterOr<?> theValue) throws InternalErrorException {
		IQueryParameterOr<?> retVal = (theValue);
		List<?> retVal2 = Collections.singletonList((IQueryParameterOr<?>)retVal);
		return (List<IQueryParameterOr<?>>) retVal2;
	}

	@Override
	public IQueryParameterOr<?> parse(FhirContext theContext, String theParamName, List<QualifiedParamList> theString) throws InternalErrorException, InvalidRequestException {
		IQueryParameterOr<?> dt;
		try {
			dt = newInstance();
			if (theString.size() == 0 || theString.get(0).size() == 0) {
				return dt;
			}
			if (theString.size() > 1) {
				throw new InvalidRequestException(Msg.code(1953) + "Multiple values detected for non-repeatable parameter '" + theParamName + "'. This server is not configured to allow multiple (AND/OR) values for this param.");
			}
			
			dt.setValuesAsQueryTokens(theContext, theParamName, theString.get(0));
		} catch (SecurityException e) {
			throw new InternalErrorException(Msg.code(1954) + e);
		}
		return dt;
	}
}
