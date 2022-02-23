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
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public final class QueryParameterTypeBinder extends BaseBinder<IQueryParameterType> implements IParamBinder<IQueryParameterType> {

	public QueryParameterTypeBinder(Class<? extends IQueryParameterType> theType, List<Class<? extends IQueryParameterType>> theCompositeTypes) {
		super(theType, theCompositeTypes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IQueryParameterOr<?>> encode(FhirContext theContext, IQueryParameterType theValue) throws InternalErrorException {
		IQueryParameterType param = theValue;
		List<?> retVal = Collections.singletonList(ParameterUtil.singleton(param, null));
		return (List<IQueryParameterOr<?>>) retVal;
	}

	@Override
	public IQueryParameterType parse(FhirContext theContext, String theParamName, List<QualifiedParamList> theParams) throws InternalErrorException, InvalidRequestException {
		String value = theParams.get(0).get(0);
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		IQueryParameterType dt = super.newInstance();

		if (theParams.size() == 0 || theParams.get(0).size() == 0) {
			return dt;
		}
		if (theParams.size() > 1 || theParams.get(0).size() > 1) {
			throw new InvalidRequestException(Msg.code(1962) + "Multiple values detected for non-repeatable parameter '" + theParamName + "'. This server is not configured to allow multiple (AND/OR) values for this param.");
		}

		dt.setValueAsQueryToken(theContext, theParamName, theParams.get(0).getQualifier(), value);
		return dt;
	}

}
