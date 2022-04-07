package ca.uhn.fhir.rest.gclient;

import static org.apache.commons.lang3.StringUtils.defaultString;

import java.util.*;

import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBaseCoding;

import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;

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
 * http://www.apache.org/licenses/LICENSE-2.0
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
public class TokenClientParam extends BaseClientParam implements IParam {

	private static final String[] EMPTY_STRING_LIST = new String[0];

	private String myParamName;

	public TokenClientParam(String theParamName) {
		myParamName = theParamName;
	}

	public IMatches exactly() {
		return new IMatches() {
			@Override
			public ICriterion<TokenClientParam> code(String theCode) {
				return new TokenCriterion(getParamName(), null, theCode);
			}

			@Override
			public ICriterion<?> codes(Collection<String> theCodes) {
				return new TokenCriterion(getParamName(), theCodes);
			}

			@Override
			public ICriterion<?> codes(String... theCodes) {
				return new TokenCriterion(getParamName(), convertToList(theCodes));
			}

			private List<String> convertToList(String[] theValues) {
				String[] values = ObjectUtils.defaultIfNull(theValues, EMPTY_STRING_LIST);
				return Arrays.asList(values);
			}

			@Override
			public ICriterion<TokenClientParam> identifier(BaseIdentifierDt theIdentifier) {
				return new TokenCriterion(getParamName(), theIdentifier.getSystemElement().getValueAsString(), theIdentifier.getValueElement().getValue());
			}

			@Override
			public ICriterion<TokenClientParam> identifier(String theIdentifier) {
				return new TokenCriterion(getParamName(), null, theIdentifier);
			}

			@Override
			public ICriterion<TokenClientParam> identifiers(BaseIdentifierDt... theIdentifiers) {
				return new TokenCriterion(getParamName(), Arrays.asList(theIdentifiers));
			}

			@Override
			public ICriterion<TokenClientParam> identifiers(List<BaseIdentifierDt> theIdentifiers) {
				return new TokenCriterion(getParamName(), theIdentifiers);
			}

			@Override
			public ICriterion<TokenClientParam> systemAndCode(String theSystem, String theCode) {
				return new TokenCriterion(getParamName(), defaultString(theSystem), theCode);
			}

			@Override
			public ICriterion<TokenClientParam> systemAndIdentifier(String theSystem, String theCode) {
				return new TokenCriterion(getParamName(), defaultString(theSystem), theCode);
			}

			@Override
			public ICriterion<?> systemAndValues(String theSystem, Collection<String> theValues) {
				return new TokenCriterion(getParamName(), defaultString(theSystem), theValues);
			}

			@Override
			public ICriterion<?> systemAndValues(String theSystem, String... theValues) {
				return new TokenCriterion(getParamName(), defaultString(theSystem), convertToList(theValues));
			}

			@Override
			public ICriterion<?> codings(IBaseCoding... theCodings) {
				return new TokenCriterion(getParamName(), theCodings);
			}
		};
	}

	@Override
	public String getParamName() {
		return myParamName;
	}

	/**
	 * Create a search criterion that matches against the given system
	 * value but does not specify a code. This means that any code/identifier with
	 * the given system should match.
	 * <p>
	 * Use {@link #exactly()} if you want to specify a code.
	 * </p>
	 */
	public ICriterion<TokenClientParam> hasSystemWithAnyCode(String theSystem) {
		return new TokenCriterion(getParamName(), theSystem, (String) null);
	}

	public interface IMatches {
		/**
		 * Creates a search criterion that matches against the given code, with no code system specified
		 * 
		 * @param theIdentifier
		 *           The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> code(String theIdentifier);

		/**
		 * Creates a search criterion that matches a given system with a collection of possible
		 * codes (this will be used to form a comma-separated OR query) with any system value.
		 * The URL form of this method will create a parameter like
		 * <code>parameter=code1,code2</code>
		 * 
		 * @param theCodes
		 *           The codes
		 */
		ICriterion<?> codes(Collection<String> theCodes);

		/**
		 * Creates a search criterion that matches a given system with a collection of possible
		 * codes (this will be used to form a comma-separated OR query) with any system value.
		 * The URL form of this method will create a parameter like
		 * <code>parameter=code1,code2</code>
		 * 
		 * @param theCodes
		 *           The codes
		 */
		ICriterion<?> codes(String... theCodes);

		/**
		 * Creates a search criterion that matches a given system with a collection of possible
		 * codes (this will be used to form a comma-separated OR query) with the given
		 * <code>Coding.system</code> and <code>Coding.value</code> values.
		 * <p>
		 * The URL form of this method will create a parameter like
		 * <code>parameter=system1|code1,system2|code2</code>
		 * </p>
		 * 
		 * @param theCodings
		 *           The codings
		 */
		ICriterion<?> codings(IBaseCoding... theCodings);

		/**
		 * Creates a search criterion that matches against the given identifier (system and code if both are present, or whatever is present)
		 * 
		 * @param theIdentifier
		 *           The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> identifier(BaseIdentifierDt theIdentifier);

		/**
		 * Creates a search criterion that matches against the given identifier, with no system specified
		 * 
		 * @param theIdentifier
		 *           The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> identifier(String theIdentifier);

		/**
		 * Creates a search criterion that matches against the given collection of identifiers (system and code if both are present, or whatever is present).
		 * In the query URL that is generated, identifiers will be joined with a ',' to create an OR query.
		 * 
		 * @param theIdentifiers
		 *           The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> identifiers(BaseIdentifierDt... theIdentifiers);

		/**
		 * Creates a search criterion that matches against the given collection of identifiers (system and code if both are present, or whatever is present).
		 * In the query URL that is generated, identifiers will be joined with a ',' to create an OR query.
		 * 
		 * @param theIdentifiers
		 *           The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> identifiers(List<BaseIdentifierDt> theIdentifiers);

		/**
		 * Creates a search criterion that matches against the given code system and code
		 * 
		 * @param theSystem
		 *           The code system (should be a URI)
		 * @param theCode
		 *           The code
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> systemAndCode(String theSystem, String theCode);

		/**
		 * Creates a search criterion that matches against the given system and identifier
		 * 
		 * @param theSystem
		 *           The code system (should be a URI)
		 * @param theIdentifier
		 *           The identifier
		 * @return A criterion
		 */
		ICriterion<TokenClientParam> systemAndIdentifier(String theSystem, String theIdentifier);

		/**
		 * Creates a search criterion that matches a given system with a collection of possible
		 * values (this will be used to form a comma-separated OR query)
		 * 
		 * @param theSystem
		 *           The system, which will be used with each value
		 * @param theValues
		 *           The values
		 */
		public ICriterion<?> systemAndValues(String theSystem, Collection<String> theValues);

		/**
		 * Creates a search criterion that matches a given system with a collection of possible
		 * values (this will be used to form a comma-separated OR query)
		 * 
		 * @param theSystem
		 *           The system, which will be used with each value
		 * @param theValues
		 *           The values
		 */
		ICriterion<?> systemAndValues(String theSystem, String... theValues);

	}

}
