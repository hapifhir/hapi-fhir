/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ICanMakeMissingParamPredicate;
import ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.UriPredicateBuilder;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;

public class PredicateBuilderFactory {

	@Nonnull
	public static ICanMakeMissingParamPredicate createPredicateBuilderForParamType(
			RestSearchParameterTypeEnum theParamType, SearchQueryBuilder theBuilder, QueryStack theQueryStack) {
		switch (theParamType) {
			case NUMBER:
				return createNumberPredicateBuilder(theBuilder);
			case DATE:
				return createDatePredicateBuilder(theBuilder);
			case STRING:
				return createStringPredicateBuilder(theBuilder);
			case TOKEN:
				return createTokenPredicateBuilder(theBuilder);
			case QUANTITY:
				return createQuantityPredicateBuilder(theBuilder);
			case URI:
				return createUriPredicateBuilder(theBuilder);
			case REFERENCE:
				return createReferencePredicateBuilder(theQueryStack, theBuilder);
			case HAS:
			case SPECIAL:
				return createCoordsPredicateBuilder(theBuilder);
			case COMPOSITE:
			default:
				throw new InternalErrorException(Msg.code(2593) + "Invalid param type " + theParamType.name());
		}
	}

	private static StringPredicateBuilder createStringPredicateBuilder(SearchQueryBuilder theBuilder) {
		return theBuilder.getSqlBuilderFactory().stringIndexTable(theBuilder);
	}

	private static NumberPredicateBuilder createNumberPredicateBuilder(SearchQueryBuilder theBuilder) {
		return theBuilder.getSqlBuilderFactory().numberIndexTable(theBuilder);
	}

	private static QuantityPredicateBuilder createQuantityPredicateBuilder(SearchQueryBuilder theBuilder) {
		return theBuilder.getSqlBuilderFactory().quantityIndexTable(theBuilder);
	}

	private static CoordsPredicateBuilder createCoordsPredicateBuilder(SearchQueryBuilder theBuilder) {
		return theBuilder.getSqlBuilderFactory().coordsPredicateBuilder(theBuilder);
	}

	private static TokenPredicateBuilder createTokenPredicateBuilder(SearchQueryBuilder theBuilder) {
		return theBuilder.getSqlBuilderFactory().tokenIndexTable(theBuilder);
	}

	private static DatePredicateBuilder createDatePredicateBuilder(SearchQueryBuilder theBuilder) {
		return theBuilder.getSqlBuilderFactory().dateIndexTable(theBuilder);
	}

	private static UriPredicateBuilder createUriPredicateBuilder(SearchQueryBuilder theBuilder) {
		return theBuilder.getSqlBuilderFactory().uriIndexTable(theBuilder);
	}

	private static ResourceLinkPredicateBuilder createReferencePredicateBuilder(
			QueryStack theQueryStack, SearchQueryBuilder theBuilder) {
		return theBuilder.getSqlBuilderFactory().referenceIndexTable(theQueryStack, theBuilder, false);
	}
}
