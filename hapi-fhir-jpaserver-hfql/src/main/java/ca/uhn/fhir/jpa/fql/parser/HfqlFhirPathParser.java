/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.fql.executor.HfqlDataTypeEnum;
import jakarta.annotation.Nullable;
import org.apache.commons.text.WordUtils;

import java.util.Map;

import static java.util.Map.entry;

public class HfqlFhirPathParser {

	private static final Map<String, HfqlDataTypeEnum> FHIR_DATATYPE_TO_FQL_DATATYPE;

	static {
		FHIR_DATATYPE_TO_FQL_DATATYPE = Map.ofEntries(
				entry("base64Binary", HfqlDataTypeEnum.STRING),
				entry("boolean", HfqlDataTypeEnum.BOOLEAN),
				entry("canonical", HfqlDataTypeEnum.STRING),
				entry("code", HfqlDataTypeEnum.STRING),
				entry("date", HfqlDataTypeEnum.DATE),
				entry("dateTime", HfqlDataTypeEnum.TIMESTAMP),
				entry("decimal", HfqlDataTypeEnum.DECIMAL),
				entry("id", HfqlDataTypeEnum.STRING),
				entry("instant", HfqlDataTypeEnum.TIMESTAMP),
				entry("integer", HfqlDataTypeEnum.INTEGER),
				entry("integer64", HfqlDataTypeEnum.LONGINT),
				entry("markdown", HfqlDataTypeEnum.STRING),
				entry("oid", HfqlDataTypeEnum.STRING),
				entry("positiveInt", HfqlDataTypeEnum.INTEGER),
				entry("string", HfqlDataTypeEnum.STRING),
				entry("time", HfqlDataTypeEnum.TIME),
				entry("unsignedInt", HfqlDataTypeEnum.INTEGER),
				entry("uri", HfqlDataTypeEnum.STRING),
				entry("url", HfqlDataTypeEnum.STRING),
				entry("uuid", HfqlDataTypeEnum.STRING),
				entry("xhtml", HfqlDataTypeEnum.STRING));
	}

	private final FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public HfqlFhirPathParser(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	/**
	 * Given a FHIRPath expression (and a resource type that it applies to), this
	 * class tries to determine the {@link HfqlDataTypeEnum HFQL Data Type} that the
	 * values will be when the expression is resolved. This is not nearly foolproof,
	 * so it is a best effort determination. If the type is ambiguous or can't be determined,
	 * this method will return {@link HfqlDataTypeEnum#STRING}.
	 */
	public HfqlDataTypeEnum determineDatatypeForPath(String theResourceType, String theFhirPath) {

		BaseRuntimeElementCompositeDefinition<?> currentElementDefinition =
				myFhirContext.getResourceDefinition(theResourceType);
		RuntimePrimitiveDatatypeDefinition leafDefinition = null;

		HfqlLexer lexer = new HfqlLexer(theFhirPath);
		boolean firstToken = true;
		boolean potentiallyRepeatableAtCurrentPath = false;
		while (lexer.hasNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART)) {
			HfqlLexerToken nextToken = lexer.getNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART);
			String nextTokenString = nextToken.getToken();

			// If the first token is the resource type, we can ignore that
			if (firstToken) {
				firstToken = false;
				if (nextTokenString.equals(theResourceType)) {
					continue;
				}
			}

			if (".".equals(nextTokenString)) {
				continue;
			}

			/*
			 * If there's a round bracket than this is a function name and not an
			 * element name. In this case we'll just move on to the next element.
			 * We're making the naive assumption here that the function is a filtering
			 * function such as in "Patient.identifier.where(system='http://foo').value"
			 * so that we can just skip the filter function and continue to navigate
			 * the element names as though the filter wasn't there. This is probably
			 * not going to hold true always, but it should be good enough for our
			 * basic type guessing.
			 *
			 * One specific case though that we deal with is the functions that take
			 * a collection and reduce it to a single element. In that case we assume
			 * we can't have a collection.
			 */
			if (nextTokenString.contains("(")) {
				String keyword = nextToken.asKeyword();
				switch (keyword) {
					case "FIRST()":
					case "LAST()":
						potentiallyRepeatableAtCurrentPath = false;
						break;
					case "TOINTEGER()":
						if (!lexer.hasNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART)) {
							return HfqlDataTypeEnum.INTEGER;
						}
						break;
				}
				continue;
			}

			/*
			 * If the element has an offset operator (e.g. "name[3]") then
			 * ignore it since we only care about the elemt name part.
			 */
			boolean hasArrayIndex = false;
			int leftSquareBracketIndex = nextTokenString.indexOf('[');
			if (leftSquareBracketIndex != -1 && nextTokenString.endsWith("]")) {
				nextTokenString = nextTokenString.substring(0, leftSquareBracketIndex);
				hasArrayIndex = true;
			}

			BaseRuntimeChildDefinition childDefForNode = currentElementDefinition.getChildByName(nextTokenString);
			if (childDefForNode == null) {
				childDefForNode = currentElementDefinition.getChildByName(nextTokenString + "[x]");
				if (childDefForNode != null) {
					if (lexer.peekNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART)
							.getToken()
							.equals(".")) {
						lexer.consumeNextToken();
					}
					if (lexer.hasNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART)) {
						String token = lexer.getNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART)
								.getToken();
						if (token.startsWith("ofType(") && token.endsWith(")")) {
							String type = token.substring(7, token.length() - 1);
							nextTokenString = nextTokenString + WordUtils.capitalize(type);
						}
					}
				}
			}

			if (childDefForNode != null) {

				if (childDefForNode.getMax() != 1 && !hasArrayIndex) {
					potentiallyRepeatableAtCurrentPath = true;
				}

				if (childDefForNode.getValidChildNames().contains(nextTokenString)) {
					BaseRuntimeElementDefinition<?> elementDefForNode = childDefForNode.getChildByName(nextTokenString);
					if (elementDefForNode != null) {
						if (elementDefForNode instanceof BaseRuntimeElementCompositeDefinition) {
							currentElementDefinition = (BaseRuntimeElementCompositeDefinition<?>) elementDefForNode;
							continue;
						} else if (elementDefForNode instanceof RuntimePrimitiveDatatypeDefinition) {
							leafDefinition = (RuntimePrimitiveDatatypeDefinition) elementDefForNode;
							continue;
						}
					}
				}
			}

			break;
		}

		if (potentiallyRepeatableAtCurrentPath) {
			return HfqlDataTypeEnum.JSON;
		}

		if (leafDefinition != null) {
			String typeName = leafDefinition.getName();
			return getHfqlDataTypeForFhirType(typeName);
		}

		return null;
	}

	static HfqlDataTypeEnum getHfqlDataTypeForFhirType(String theTypeName) {
		return FHIR_DATATYPE_TO_FQL_DATATYPE.get(theTypeName);
	}

	@Nullable
	private static String getNextFhirPathPartTokenOrNull(HfqlLexer lexer) {
		String finalToken = null;
		if (lexer.hasNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART)) {
			finalToken = lexer.getNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART)
					.getToken();
		}

		if (".".equals(finalToken)) {
			if (lexer.hasNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART)) {
				finalToken = lexer.getNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION_PART)
						.getToken();
			}
		}

		return finalToken;
	}
}
