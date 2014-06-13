package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.dstu.composite.IdentifierDt;

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


/**
 * Token parameter type for use in fluent client interfaces
 */
public class TokenParam implements IParam {

	private String myParamName;

	@Override
	public String getParamName() {
		return myParamName;
	}

	public TokenParam(String theParamName) {
		myParamName = theParamName;
	}

	public IMatches exactly() {
		return new IMatches() {
			@Override
			public ICriterion systemAndCode(String theSystem, String theCode) {
				return new TokenCriterion(getParamName(), theSystem, theCode);
			}

			@Override
			public ICriterion systemAndIdentifier(String theSystem, String theCode) {
				return new TokenCriterion(getParamName(), theSystem, theCode);
			}

			@Override
			public ICriterion code(String theCode) {
				return new TokenCriterion(getParamName(), null, theCode);
			}

			@Override
			public ICriterion identifier(String theIdentifier) {
				return new TokenCriterion(getParamName(), null, theIdentifier);
			}

			@Override
			public ICriterion identifier(IdentifierDt theIdentifier) {
				return new TokenCriterion(getParamName(), theIdentifier.getSystem().getValueAsString(), theIdentifier.getValue().getValue());
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
		ICriterion systemAndCode(String theSystem, String theCode);

		/**
		 * Creates a search criterion that matches against the given system and identifier
		 * 
		 * @param theSystem
		 *            The code system (should be a URI)
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion systemAndIdentifier(String theSystem, String theIdentifier);

		/**
		 * Creates a search criterion that matches against the given identifier, with no system specified
		 * 
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion identifier(String theIdentifier);

		/**
		 * Creates a search criterion that matches against the given code, with no code system specified
		 * 
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion code(String theIdentifier);

		/**
		 * Creates a search criterion that matches against the given identifier (system and code if both are present, or whatever is present)
		 * 
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion identifier(IdentifierDt theIdentifier);
	}

}
