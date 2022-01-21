package ca.uhn.fhir.rest.param;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.util.CoverageIgnore;

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

/**
 * This class represents a restful search operation parameter for an "OR list" of tokens (in other words, a 
 * list which can contain one-or-more tokens, where the server should return results matching any of the tokens)
 */
public class TokenOrListParam extends BaseOrListParam<TokenOrListParam, TokenParam> {

	/**
	 * Create a new empty token "OR list"
	 */
	public TokenOrListParam() {
	}

	/**
	 * Create a new token "OR list" with a single token, or multiple tokens which have the same system value
	 * 
	 * @param theSystem
	 *            The system to use for the one token to pre-populate in this list
	 * @param theValues
	 *            The values to use for the one token to pre-populate in this list
	 */
	public TokenOrListParam(String theSystem, String... theValues) {
		for (String next : theValues) {
			add(theSystem, next);
		}
	}

	/**
	 * Convenience method which adds a token to this OR list using the system and code from a coding
	 */
	public void add(BaseCodingDt theCodingDt) {
		add(new TokenParam(theCodingDt));
	}

	/**
	 * Convenience method which adds a token to this OR list using the system and value from an identifier
	 */
	public void add(BaseIdentifierDt theIdentifierDt) {
		add(new TokenParam(theIdentifierDt));
	}

	/**
	 * Add a new token to this list
	 *  @param theSystem
	 *            The system to use for the one token to pre-populate in this list
	 */
	public TokenOrListParam add(String theSystem, String theValue) {
		add(new TokenParam(theSystem, theValue));
		return this;
	}

	/**
	 * Add a new token to this list
	 */
	public TokenOrListParam add(String theValue) {
		add(new TokenParam(null, theValue));
		return this;
	}

	public List<BaseCodingDt> getListAsCodings() {
		ArrayList<BaseCodingDt> retVal = new ArrayList<BaseCodingDt>();
		for (TokenParam next : getValuesAsQueryTokens()) {
			InternalCodingDt nextCoding = next.getValueAsCoding();
			if (!nextCoding.isEmpty()) {
				retVal.add(nextCoding);
			}
		}
		return retVal;
	}

	@CoverageIgnore
	@Override
	TokenParam newInstance() {
		return new TokenParam();
	}

	public boolean doesCodingListMatch(List<? extends BaseCodingDt> theCodings) {
		List<BaseCodingDt> paramCodings = getListAsCodings();
		for (BaseCodingDt coding : theCodings) {
			for (BaseCodingDt paramCoding : paramCodings) {
				if (coding.matchesToken(paramCoding)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@CoverageIgnore
	@Override
	public TokenOrListParam addOr(TokenParam theParameter) {
		add(theParameter);
		return this;
	}

}
