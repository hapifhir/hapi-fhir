/*-
 * #%L
 * HAPI FHIR JPA Server - Firely Query Language
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.jpa.fql.executor.FqlDataTypeEnum;
import org.apache.commons.text.WordUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FqlFhirPathParser {

	private static final Map<String, FqlDataTypeEnum> FHIR_DATATYPE_TO_FQL_DATATYPE;
	private final FhirContext myFhirContext;

	static {
		Map<String, FqlDataTypeEnum> fhirDatatypeToFqlDatatype = new HashMap<>();
		fhirDatatypeToFqlDatatype.put("base64Binary", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("boolean", FqlDataTypeEnum.BOOLEAN);
		fhirDatatypeToFqlDatatype.put("canonical", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("code", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("date", FqlDataTypeEnum.DATE);
		fhirDatatypeToFqlDatatype.put("dateTime", FqlDataTypeEnum.TIMESTAMP);
		fhirDatatypeToFqlDatatype.put("decimal", FqlDataTypeEnum.DECIMAL);
		fhirDatatypeToFqlDatatype.put("id", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("instant", FqlDataTypeEnum.TIMESTAMP);
		fhirDatatypeToFqlDatatype.put("integer", FqlDataTypeEnum.INTEGER);
		fhirDatatypeToFqlDatatype.put("integer64", FqlDataTypeEnum.LONGINT);
		fhirDatatypeToFqlDatatype.put("markdown", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("oid", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("positiveInt", FqlDataTypeEnum.INTEGER);
		fhirDatatypeToFqlDatatype.put("string", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("time", FqlDataTypeEnum.TIME);
		fhirDatatypeToFqlDatatype.put("unsignedInt", FqlDataTypeEnum.INTEGER);
		fhirDatatypeToFqlDatatype.put("uri", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("url", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("uuid", FqlDataTypeEnum.STRING);
		fhirDatatypeToFqlDatatype.put("xhtml", FqlDataTypeEnum.STRING);
		FHIR_DATATYPE_TO_FQL_DATATYPE = Collections.unmodifiableMap(fhirDatatypeToFqlDatatype);
	}

	/**
	 * Non instantiable
	 */
	public FqlFhirPathParser(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}


	public FqlDataTypeEnum determineDatatypeForPath(String theResourceType, String theFhirPath) {

		BaseRuntimeElementCompositeDefinition<?> currentElementDefinition = myFhirContext.getResourceDefinition(theResourceType);
		RuntimePrimitiveDatatypeDefinition leafDefinition = null;

		FqlLexer lexer = new FqlLexer(theFhirPath);
		boolean firstToken = true;
		while (lexer.hasNextToken(FqlLexerOptions.FHIRPATH_EXPRESSION_PART)) {
			String nextToken = lexer.getNextToken(FqlLexerOptions.FHIRPATH_EXPRESSION_PART).getToken();

			// If the first token is the resource type, we can ignore that
			if (firstToken) {
				firstToken = false;
				if (nextToken.equals(theResourceType)) {
					continue;
				}
			}

			if (".".equals(nextToken)) {
				continue;
			}

			if (nextToken.contains("(")) {
				continue;
			}

			BaseRuntimeChildDefinition childDefForNode = currentElementDefinition.getChildByName(nextToken);
			if (childDefForNode == null) {
				childDefForNode = currentElementDefinition.getChildByName(nextToken + "[x]");
				if (childDefForNode != null) {
					if (lexer.peekNextToken(FqlLexerOptions.FHIRPATH_EXPRESSION_PART).getToken().equals(".")) {
						lexer.consumeNextToken();
					}
					if (lexer.hasNextToken(FqlLexerOptions.FHIRPATH_EXPRESSION_PART)) {
						String token = lexer.getNextToken(FqlLexerOptions.FHIRPATH_EXPRESSION_PART).getToken();
						if (token.startsWith("ofType(") && token.endsWith(")")) {
							String type = token.substring(7, token.length() - 1);
							nextToken = nextToken + WordUtils.capitalize(type);
						}
					}
				}
			}

			if (childDefForNode != null) {
				if (childDefForNode.getValidChildNames().contains(nextToken)) {
					BaseRuntimeElementDefinition<?> elementDefForNode = childDefForNode.getChildByName(nextToken);
					if (elementDefForNode != null) {
						if (elementDefForNode instanceof BaseRuntimeElementCompositeDefinition) {
							currentElementDefinition = (BaseRuntimeElementCompositeDefinition<?>) elementDefForNode;
							continue;
						} else if (elementDefForNode instanceof RuntimePrimitiveDatatypeDefinition) {
							leafDefinition = (RuntimePrimitiveDatatypeDefinition) elementDefForNode;
							break;
						}
					}
				}
			}

			break;
		}

		if (leafDefinition != null) {
			return FHIR_DATATYPE_TO_FQL_DATATYPE.get(leafDefinition.getName());
		}

		return null;
	}

}
