package ca.uhn.fhir.rest.gclient;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Arrays;
import java.util.List;

import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
 * Token parameter type for use in fluent client interfaces
 */
public class TokenClientParam extends BaseClientParam  implements IParam {

	private String myParamName;

	@Override
	public String getParamName() {
		return myParamName;
	}

	public TokenClientParam(String theParamName) {
		myParamName = theParamName;
	}

	public IMatches exactly() {
		return new IMatches() {
			@Override
			public ICriterion<TokenClientParam> systemAndCode(String theSystem, String theCode) {
				return new TokenCriterion(getParamName(), defaultString(theSystem), theCode);
			}

			@Override
			public ICriterion<TokenClientParam> systemAndIdentifier(String theSystem, String theCode) {
				return new TokenCriterion(getParamName(), defaultString(theSystem), theCode);
			}

			@Override
			public ICriterion<TokenClientParam> code(String theCode) {
				return new TokenCriterion(getParamName(), null, theCode);
			}

			@Override
			public ICriterion<TokenClientParam> identifier(String theIdentifier) {
				return new TokenCriterion(getParamName(), null, theIdentifier);
			}

			@Override
			public ICriterion<TokenClientParam> identifier(BaseIdentifierDt theIdentifier) {
				return new TokenCriterion(getParamName(), theIdentifier.getSystemElement().getValueAsString(), theIdentifier.getValueElement().getValue());
			}

			@Override
			public ICriterion<TokenClientParam> identifiers(List<BaseIdentifierDt> theIdentifiers) {
				return new TokenCriterion(getParamName(), theIdentifiers);
			}

			@Override
			public ICriterion<TokenClientParam> identifiers(BaseIdentifierDt... theIdentifiers) {
				return new TokenCriterion(getParamName(), Arrays.asList(theIdentifiers));
			}
};
	}

	public interface IMatches {
		/**
		 * Creates a search criterion that matches against the given code system and code
		 * 
		 * @param theSystem
		 *            The code system (should be a URI)
		 * @param theCode
		 *            The code
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> systemAndCode(String theSystem, String theCode);

		/**
		 * Creates a search criterion that matches against the given system and identifier
		 * 
		 * @param theSystem
		 *            The code system (should be a URI)
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> systemAndIdentifier(String theSystem, String theIdentifier);

		/**
		 * Creates a search criterion that matches against the given identifier, with no system specified
		 * 
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> identifier(String theIdentifier);

		/**
		 * Creates a search criterion that matches against the given code, with no code system specified
		 * 
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> code(String theIdentifier);

		/**
		 * Creates a search criterion that matches against the given identifier (system and code if both are present, or whatever is present)
		 * 
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> identifier(BaseIdentifierDt theIdentifier);
		
		/**
		 * Creates a search criterion that matches against the given collection of identifiers (system and code if both are present, or whatever is present).
		 * In the query URL that is generated, identifiers will be joined with a ',' to create an OR query.
		 * 
		 * @param theIdentifiers
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> identifiers(List<BaseIdentifierDt> theIdentifiers);

		/**
		 * Creates a search criterion that matches against the given collection of identifiers (system and code if both are present, or whatever is present).
		 * In the query URL that is generated, identifiers will be joined with a ',' to create an OR query.
		 * 
		 * @param theIdentifiers
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> identifiers(BaseIdentifierDt... theIdentifiers);

	}

}
