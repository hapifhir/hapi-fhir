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
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.util.List;

public final class QueryParameterAndBinder extends BaseBinder<IQueryParameterAnd<?>> implements IParamBinder<IQueryParameterAnd<?>> {

	public QueryParameterAndBinder(Class<? extends IQueryParameterAnd<?>> theType, List<Class<? extends IQueryParameterType>> theCompositeTypes) {
		super(theType, theCompositeTypes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IQueryParameterOr<?>> encode(FhirContext theContext, IQueryParameterAnd<?> theString) throws InternalErrorException {
		List<IQueryParameterOr<?>> retVal = (List<IQueryParameterOr<?>>) ((IQueryParameterAnd<?>) theString).getValuesAsQueryTokens();
		return retVal;
	}

	@Override
	public IQueryParameterAnd<?> parse(FhirContext theContext, String theParamName, List<QualifiedParamList> theString) throws InternalErrorException, InvalidRequestException {
		IQueryParameterAnd<?> dt;
		try {
			dt = newInstance();
			dt.setValuesAsQueryTokens(theContext, theParamName, theString);
		} catch (SecurityException e) {
			throw new InternalErrorException(Msg.code(1952) + e);
		}
		return dt;
	}
}
