/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.search.builder.sql;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredicateBuilderFactory {

	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderFactory.class);

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
				// we don't expect to see this
				ourLog.error("Invalid param type " + theParamType.name());
				return null;
		}
	}

	private static StringPredicateBuilder createStringPredicateBuilder(SearchQueryBuilder theBuilder) {
		StringPredicateBuilder sp = theBuilder.getSqlBuilderFactory().stringIndexTable(theBuilder);
		return sp;
	}

	private static NumberPredicateBuilder createNumberPredicateBuilder(SearchQueryBuilder theBuilder) {
		NumberPredicateBuilder np = theBuilder.getSqlBuilderFactory().numberIndexTable(theBuilder);
		return np;
	}

	private static QuantityPredicateBuilder createQuantityPredicateBuilder(SearchQueryBuilder theBuilder) {
		QuantityPredicateBuilder qp = theBuilder.getSqlBuilderFactory().quantityIndexTable(theBuilder);
		return qp;
	}

	private static CoordsPredicateBuilder createCoordsPredicateBuilder(SearchQueryBuilder theBuilder) {
		CoordsPredicateBuilder cp = theBuilder.getSqlBuilderFactory().coordsPredicateBuilder(theBuilder);
		return cp;
	}

	private static TokenPredicateBuilder createTokenPredicateBuilder(SearchQueryBuilder theBuilder) {
		TokenPredicateBuilder tp = theBuilder.getSqlBuilderFactory().tokenIndexTable(theBuilder);
		return tp;
	}

	private static DatePredicateBuilder createDatePredicateBuilder(SearchQueryBuilder theBuilder) {
		DatePredicateBuilder dp = theBuilder.getSqlBuilderFactory().dateIndexTable(theBuilder);
		return dp;
	}

	private static UriPredicateBuilder createUriPredicateBuilder(SearchQueryBuilder theBuilder) {
		UriPredicateBuilder up = theBuilder.getSqlBuilderFactory().uriIndexTable(theBuilder);
		return up;
	}

	private static ResourceLinkPredicateBuilder createReferencePredicateBuilder(
			QueryStack theQueryStack, SearchQueryBuilder theBuilder) {
		ResourceLinkPredicateBuilder retVal =
				theBuilder.getSqlBuilderFactory().referenceIndexTable(theQueryStack, theBuilder, false);
		return retVal;
	}
}
