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

import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboNonUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityNormalizedPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceHistoryPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceHistoryProvenancePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SearchParamPresentPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TagPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.UriPredicateBuilder;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

public class SqlObjectFactory {

	@Autowired
	private ApplicationContext myApplicationContext;

	public ComboUniqueSearchParameterPredicateBuilder newComboUniqueSearchParameterPredicateBuilder(
			SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ComboUniqueSearchParameterPredicateBuilder.class, theSearchSqlBuilder);
	}

	public ComboNonUniqueSearchParameterPredicateBuilder newComboNonUniqueSearchParameterPredicateBuilder(
			SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ComboNonUniqueSearchParameterPredicateBuilder.class, theSearchSqlBuilder);
	}

	public CoordsPredicateBuilder coordsPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(CoordsPredicateBuilder.class, theSearchSqlBuilder);
	}

	public DatePredicateBuilder dateIndexTable(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(DatePredicateBuilder.class, theSearchSqlBuilder);
	}

	public NumberPredicateBuilder numberIndexTable(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(NumberPredicateBuilder.class, theSearchSqlBuilder);
	}

	public QuantityPredicateBuilder quantityIndexTable(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(QuantityPredicateBuilder.class, theSearchSqlBuilder);
	}

	public QuantityNormalizedPredicateBuilder quantityNormalizedIndexTable(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(QuantityNormalizedPredicateBuilder.class, theSearchSqlBuilder);
	}

	public ResourceLinkPredicateBuilder referenceIndexTable(
			QueryStack theQueryStack, SearchQueryBuilder theSearchSqlBuilder, boolean theReversed) {
		return myApplicationContext.getBean(
				ResourceLinkPredicateBuilder.class, theQueryStack, theSearchSqlBuilder, theReversed);
	}

	public ResourceTablePredicateBuilder resourceTable(
			SearchQueryBuilder theSearchSqlBuilder, SearchIncludeDeletedEnum theSearchIncludeDeleted) {
		// the default is searching non-deleted resources
		return myApplicationContext.getBean(
				ResourceTablePredicateBuilder.class,
				theSearchSqlBuilder,
				Objects.requireNonNullElse(theSearchIncludeDeleted, SearchIncludeDeletedEnum.NEVER));
	}

	public ResourceIdPredicateBuilder resourceId(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ResourceIdPredicateBuilder.class, theSearchSqlBuilder);
	}

	public SearchParamPresentPredicateBuilder searchParamPresentPredicateBuilder(
			SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(SearchParamPresentPredicateBuilder.class, theSearchSqlBuilder);
	}

	public StringPredicateBuilder stringIndexTable(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(StringPredicateBuilder.class, theSearchSqlBuilder);
	}

	public TokenPredicateBuilder tokenIndexTable(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(TokenPredicateBuilder.class, theSearchSqlBuilder);
	}

	public UriPredicateBuilder uriIndexTable(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(UriPredicateBuilder.class, theSearchSqlBuilder);
	}

	public TagPredicateBuilder newTagPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(TagPredicateBuilder.class, theSearchSqlBuilder);
	}

	public ResourceHistoryPredicateBuilder newResourceHistoryPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ResourceHistoryPredicateBuilder.class, theSearchSqlBuilder);
	}

	public ResourceHistoryProvenancePredicateBuilder newResourceHistoryProvenancePredicateBuilder(
			SearchQueryBuilder theSearchSqlBuilder) {
		return myApplicationContext.getBean(ResourceHistoryProvenancePredicateBuilder.class, theSearchSqlBuilder);
	}

	public SearchQueryExecutor newSearchQueryExecutor(GeneratedSql theGeneratedSql, Integer theMaxResultsToFetch) {
		return myApplicationContext.getBean(SearchQueryExecutor.class, theGeneratedSql, theMaxResultsToFetch);
	}
}
