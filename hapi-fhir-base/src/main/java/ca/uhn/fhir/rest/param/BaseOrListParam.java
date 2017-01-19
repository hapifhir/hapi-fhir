package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.method.QualifiedParamList;

abstract class BaseOrListParam<MT extends BaseOrListParam<?, ?>, PT extends IQueryParameterType> implements IQueryParameterOr<PT> {

	private List<PT> myList=new ArrayList<PT>();

//	public void addToken(T theParam) {
//		Validate.notNull(theParam,"Param can not be null");
//		myList.add(theParam);
//	}
	
	@Override
	public void setValuesAsQueryTokens(FhirContext theContext, String theParamName, QualifiedParamList theParameters) {
		myList.clear();
		for (String next : theParameters) {
			PT nextParam = newInstance();
			nextParam.setValueAsQueryToken(theContext, theParamName, theParameters.getQualifier(), next);
			myList.add(nextParam);
		}
	}

	abstract PT newInstance();

	public abstract MT addOr(PT theParameter);
	
	@SuppressWarnings("unchecked")
	public MT add(PT theParameter) {
		if (theParameter != null) {
			myList.add(theParameter);
		}
		return (MT) this;
	}
	
	@Override
	public List<PT> getValuesAsQueryTokens() {
		return myList;
	}

}
