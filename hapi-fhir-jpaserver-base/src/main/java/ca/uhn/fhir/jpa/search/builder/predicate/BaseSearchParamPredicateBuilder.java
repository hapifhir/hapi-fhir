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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.search.builder.models.MissingQueryParameterPredicateParams;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSearchParamPredicateBuilder extends BaseJoiningPredicateBuilder
		implements ICanMakeMissingParamPredicate {

	private final DbColumn myColumnMissing;
	private final DbColumn myColumnResType;
	private final DbColumn myColumnParamName;
	private final DbColumn myColumnResId;
	private final DbColumn myColumnHashIdentity;

	public BaseSearchParamPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder, DbTable theTable) {
		super(theSearchSqlBuilder, theTable);

		myColumnResId = getTable().addColumn("RES_ID");
		myColumnMissing = theTable.addColumn("SP_MISSING");
		myColumnResType = theTable.addColumn("RES_TYPE");
		myColumnParamName = theTable.addColumn("SP_NAME");
		myColumnHashIdentity = theTable.addColumn("HASH_IDENTITY");
	}

	public DbColumn getColumnHashIdentity() {
		return myColumnHashIdentity;
	}

	public DbColumn getResourceTypeColumn() {
		return myColumnResType;
	}

	public DbColumn getColumnParamName() {
		return myColumnParamName;
	}

	public DbColumn getMissingColumn() {
		return myColumnMissing;
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public Condition combineWithHashIdentityPredicate(
			String theResourceName, String theParamName, Condition thePredicate) {
		List<Condition> andPredicates = new ArrayList<>();

		Condition hashIdentityPredicate = createHashIdentityPredicate(theResourceName, theParamName);
		andPredicates.add(hashIdentityPredicate);
		andPredicates.add(thePredicate);

		return QueryParameterUtils.toAndPredicate(andPredicates);
	}

	@Nonnull
	public Condition createHashIdentityPredicate(String theResourceType, String theParamName) {
		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
				getPartitionSettings(), getRequestPartitionId(), theResourceType, theParamName);
		String hashIdentityVal = generatePlaceholder(hashIdentity);
		return BinaryCondition.equalTo(myColumnHashIdentity, hashIdentityVal);
	}

	public Condition createPredicateParamMissingForNonReference(
			String theResourceName, String theParamName, Boolean theMissing, RequestPartitionId theRequestPartitionId) {

		List<Condition> conditions = new ArrayList<>();
		if (getStorageSettings().isIndexStorageOptimized()) {
			Long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
					getPartitionSettings(), getRequestPartitionId(), theResourceName, theParamName);
			conditions.add(BinaryCondition.equalTo(getColumnHashIdentity(), generatePlaceholder(hashIdentity)));
		} else {
			conditions.add(BinaryCondition.equalTo(getResourceTypeColumn(), generatePlaceholder(theResourceName)));
			conditions.add(BinaryCondition.equalTo(getColumnParamName(), generatePlaceholder(theParamName)));
		}
		conditions.add(BinaryCondition.equalTo(getMissingColumn(), generatePlaceholder(theMissing)));

		ComboCondition condition = ComboCondition.and(conditions.toArray());
		return combineWithRequestPartitionIdPredicate(theRequestPartitionId, condition);
	}

	@Override
	public Condition createPredicateParamMissingValue(MissingQueryParameterPredicateParams theParams) {
		SelectQuery subquery = new SelectQuery();
		subquery.addCustomColumns(1);
		subquery.addFromTable(getTable());

		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
				getPartitionSettings(),
				theParams.getRequestPartitionId(),
				theParams.getResourceTablePredicateBuilder().getResourceType(),
				theParams.getParamName());

		Condition subQueryCondition = ComboCondition.and(
				BinaryCondition.equalTo(
						getResourceIdColumn(),
						theParams.getResourceTablePredicateBuilder().getResourceIdColumn()),
				BinaryCondition.equalTo(getColumnHashIdentity(), generatePlaceholder(hashIdentity)));

		subquery.addCondition(subQueryCondition);

		Condition unaryCondition = UnaryCondition.exists(subquery);
		if (theParams.isMissing()) {
			unaryCondition = new NotCondition(unaryCondition);
		}

		return combineWithRequestPartitionIdPredicate(theParams.getRequestPartitionId(), unaryCondition);
	}
}
