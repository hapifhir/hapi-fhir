package ca.uhn.fhir.rest.param;

import org.apache.commons.lang3.Validate;

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


public class TokenAndListParam extends BaseAndListParam<TokenOrListParam> {

	@Override
	TokenOrListParam newInstance() {
		return new TokenOrListParam();
	}
	
	@Override
	public TokenAndListParam addAnd(TokenOrListParam theValue) {
		addValue(theValue);
		return this;
	}

	/**
	 * @param theValue The OR values
	 * @return Returns a reference to this for convenient chaining
	 */
	public TokenAndListParam addAnd(TokenParam... theValue) {
		Validate.notNull(theValue, "theValue must not be null");
		TokenOrListParam orListParam = new TokenOrListParam();
		for (TokenParam next : theValue) {
			orListParam.add(next);
		}
		addValue(orListParam);
		return this;
	}
}
