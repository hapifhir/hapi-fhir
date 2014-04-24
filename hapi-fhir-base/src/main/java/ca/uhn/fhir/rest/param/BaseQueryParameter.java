package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.dstu.valueset.SearchParamTypeEnum;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.QueryUtil;

public abstract class BaseQueryParameter implements IParameter {

	public abstract List<List<String>> encode(Object theObject) throws InternalErrorException;

	public abstract String getName();

	public abstract Object parse(List<List<String>> theString) throws InternalErrorException, InvalidRequestException;

	public abstract boolean isRequired();
	
	/**
	 * Parameter should return true if {@link #parse(List)} should be called even
	 * if the query string contained no values for the given parameter 
	 */
	public abstract boolean handlesMissing();

	public abstract SearchParamTypeEnum getParamType();

	@Override
	public void translateClientArgumentIntoQueryArgument(Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments) throws InternalErrorException {
		if (theSourceClientArgument == null) {
			if (isRequired()) {
				throw new NullPointerException("SearchParameter '" + getName() + "' is required and may not be null");
			}
		} else {
			List<List<String>> value = encode(theSourceClientArgument);
			ArrayList<String> paramValues = new ArrayList<String>(value.size());
			theTargetQueryArguments.put(getName(), paramValues);

			for (List<String> nextParamEntry : value) {
				StringBuilder b = new StringBuilder();
				for (String str : nextParamEntry) {
					if (b.length() > 0) {
						b.append(",");
					}
					b.append(str.replace(",", "\\,"));
				}
				paramValues.add(b.toString());
			}

		}
	}
	
	@Override
	public Object translateQueryParametersIntoServerArgument(Request theRequest, Object theRequestContents) throws InternalErrorException, InvalidRequestException {
		String[] value = theRequest.getParameters().get(getName());
		if (value == null || value.length == 0) {
			if (handlesMissing()) {
				return parse(new ArrayList<List<String>>(0));
			}else {
				return null;
			}
		}

		List<List<String>> paramList = new ArrayList<List<String>>(value.length);
		for (String nextParam : value) {
			if (nextParam.contains(",") == false) {
				paramList.add(Collections.singletonList(nextParam));
			} else {
				paramList.add(QueryUtil.splitQueryStringByCommasIgnoreEscape(nextParam));
			}
		}

		return parse(paramList);
		
	}
	

}
