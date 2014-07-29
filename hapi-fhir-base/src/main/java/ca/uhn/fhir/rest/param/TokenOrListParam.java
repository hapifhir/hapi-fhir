package ca.uhn.fhir.rest.param;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;

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


public class TokenOrListParam  extends BaseOrListParam<TokenParam> {

	@Override
	TokenParam newInstance() {
		return new TokenParam();
	}
	
	public List<CodingDt> getListAsCodings() {
		ArrayList<CodingDt> retVal = new ArrayList<CodingDt>();
		for (TokenParam next : getValuesAsQueryTokens()) {
			CodingDt nextCoding = next.getValueAsCoding();
			if (!nextCoding.isEmpty()) {
				retVal.add(nextCoding);
			}
		}
		return retVal;
	}

	/**
	 * Convenience method which adds a token to this OR list
	 * using the system and code from a coding
	 */
	public void add(CodingDt theCodingDt) {
		add(new TokenParam(theCodingDt));
	}

	/**
	 * Convenience method which adds a token to this OR list
	 * using the system and value from an identifier
	 */
	public void add(IdentifierDt theIdentifierDt) {
		add(new TokenParam(theIdentifierDt));
	}

}
