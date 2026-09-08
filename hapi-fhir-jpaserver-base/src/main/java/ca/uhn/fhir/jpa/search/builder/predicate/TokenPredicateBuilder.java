/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import com.google.common.annotations.VisibleForTesting;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TokenPredicateBuilder extends BaseTokenPredicateBuilder {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TokenPredicateBuilder.class);

	private final DbColumn myColumnResId;
	private final DbColumn myColumnHashSystemAndValue;
	private final DbColumn myColumnHashSystem;
	private final DbColumn myColumnHashValue;
	private final DbColumn myColumnSystem;
	private final DbColumn myColumnValue;
	private final DbColumn myColumnHashIdentity;

	/**
	 * Constructor
	 */
	public TokenPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_TOKEN"));
		myColumnResId = getTable().addColumn("RES_ID");
		myColumnHashIdentity = getTable().addColumn("HASH_IDENTITY");
		myColumnHashSystem = getTable().addColumn("HASH_SYS");
		myColumnHashSystemAndValue = getTable().addColumn("HASH_SYS_AND_VALUE");
		myColumnHashValue = getTable().addColumn("HASH_VALUE");
		myColumnSystem = getTable().addColumn("SP_SYSTEM");
		myColumnValue = getTable().addColumn("SP_VALUE");
	}

	@Override
	public DbColumn getColumnHashIdentity() {
		return myColumnHashIdentity;
	}

	@VisibleForTesting
	public void setStorageSettingsForUnitTest(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public DbColumn getColumnSystem() {
		return myColumnSystem;
	}

	public DbColumn getColumnValue() {
		return myColumnValue;
	}

	@Override
	public void addSort(
			DbColumn[] theSourceJoinColumns,
			String theResourceName,
			String theParamName,
			boolean theAscending,
			boolean theUseAggregate) {
		SearchQueryBuilder sqlBuilder = getSearchQueryBuilder();

		ComboCondition onCondition = sqlBuilder.createOnCondition(theSourceJoinColumns, getJoinColumns());
		onCondition.addCondition(createHashIdentityPredicate(theResourceName, theParamName));
		sqlBuilder.addCustomJoin(
				SelectQuery.JoinType.LEFT_OUTER, theSourceJoinColumns[0].getTable(), getTable(), onCondition);

		sqlBuilder.addSortString(getColumnSystem(), theAscending, theUseAggregate);
		sqlBuilder.addSortString(getColumnValue(), theAscending, theUseAggregate);
	}

	@Override
	protected Condition buildOptionalHashIdentityForEquals(
			RequestPartitionId theRequestPartitionId, List<String> theResourceNames, String theParamName) {
		if (myStorageSettings.isIncludeHashIdentityForTokenSearches()) {
			return createHashIdentityPredicate(theRequestPartitionId, theResourceNames, theParamName);
		}
		return null;
	}

	@Override
	protected Condition createPredicateOrList(
			List<String> theResourceTypes,
			String theSearchParamName,
			List<FhirVersionIndependentConcept> theCodes,
			boolean theWantEquals) {

		// Group hashes by column so each column gets a single IN (...) clause.
		Map<DbColumn, Collection<Long>> hashesByColumn = new LinkedHashMap<>();
		for (String nextResourceType : theResourceTypes) {
			for (FhirVersionIndependentConcept nextToken : theCodes) {
				long hash;
				DbColumn column;
				if (nextToken.getSystem() == null) {
					hash = ResourceIndexedSearchParamToken.calculateHashValue(
							getPartitionSettings(),
							getRequestPartitionId(),
							nextResourceType,
							theSearchParamName,
							nextToken.getCode());
					column = myColumnHashValue;
				} else if (isBlank(nextToken.getCode())) {
					hash = ResourceIndexedSearchParamToken.calculateHashSystem(
							getPartitionSettings(),
							getRequestPartitionId(),
							nextResourceType,
							theSearchParamName,
							nextToken.getSystem());
					column = myColumnHashSystem;
				} else {
					hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(
							getPartitionSettings(),
							getRequestPartitionId(),
							nextResourceType,
							theSearchParamName,
							nextToken.getSystem(),
							nextToken.getCode());
					column = myColumnHashSystemAndValue;
				}
				hashesByColumn
						.computeIfAbsent(column, t -> new LinkedHashSet<>())
						.add(hash);
			}
		}

		List<Condition> conditions = new ArrayList<>(hashesByColumn.size());
		for (Map.Entry<DbColumn, Collection<Long>> nextEntry : hashesByColumn.entrySet()) {
			conditions.add(QueryParameterUtils.toEqualToOrInPredicate(
					nextEntry.getKey(), generatePlaceholders(nextEntry.getValue()), !theWantEquals));
		}

		if (conditions.size() == 1) {
			return conditions.get(0);
		}
		if (theWantEquals) {
			return QueryParameterUtils.toOrPredicate(conditions);
		}
		return QueryParameterUtils.toAndPredicate(conditions);
	}
}
