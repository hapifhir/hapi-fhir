package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
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
import java.util.List;

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.rest.method.QualifiedParamList;

public class TokenOrListParam implements IQueryParameterOr<TokenParam> {

	private List<TokenParam> myList = new ArrayList<TokenParam>();

	public void addToken(TokenParam theParam) {
		Validate.notNull(theParam, "Param can not be null");
		myList.add(theParam);
	}

	/**
	 * Returns a copy of all tokens in this list as {@link CodingDt} instances
	 */
	public List<CodingDt> toCodings() {
		ArrayList<CodingDt> retVal = new ArrayList<CodingDt>();
		for (TokenParam next : myList) {
			retVal.add(new CodingDt(next.getSystem(), next.getValue()));
		}
		return retVal;
	}

	@Override
	public void setValuesAsQueryTokens(QualifiedParamList theParameters) {
		myList.clear();
		for (String next : theParameters) {
			TokenParam nextParam = new TokenParam();
			nextParam.setValueAsQueryToken(theParameters.getQualifier(), next);
			myList.add(nextParam);
		}
	}

	@Override
	public List<TokenParam> getValuesAsQueryTokens() {
		return myList;
	}

}
