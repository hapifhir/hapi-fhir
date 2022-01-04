package ca.uhn.fhir.jpa.search.builder.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class BasePredicateBuilder {

	private final SearchQueryBuilder mySearchSqlBuilder;

	public BasePredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		mySearchSqlBuilder = theSearchSqlBuilder;
	}


	PartitionSettings getPartitionSettings() {
		return mySearchSqlBuilder.getPartitionSettings();
	}

	RequestPartitionId getRequestPartitionId() {
		return mySearchSqlBuilder.getRequestPartitionId();
	}

	String getResourceType() {
		return mySearchSqlBuilder.getResourceType();
	}

	ModelConfig getModelConfig() {
		return mySearchSqlBuilder.getModelConfig();
	}

	@Nonnull
	String generatePlaceholder(Object theInput) {
		return mySearchSqlBuilder.generatePlaceholder(theInput);
	}

	@Nonnull
	List<String> generatePlaceholders(Collection<?> theValues) {
		return mySearchSqlBuilder.generatePlaceholders(theValues);
	}

	protected FhirContext getFhirContext() {
		return mySearchSqlBuilder.getFhirContext();
	}

	protected void setMatchNothing() {
		mySearchSqlBuilder.setMatchNothing();
	}


	protected BinaryCondition createConditionForValueWithComparator(ParamPrefixEnum theComparator, DbColumn theColumn, Object theValue) {
		return mySearchSqlBuilder.createConditionForValueWithComparator(theComparator, theColumn, theValue);
	}

	protected BaseJoiningPredicateBuilder getOrCreateQueryRootTable(boolean theIncludeResourceTypeAndNonDeletedFlag) {
		return mySearchSqlBuilder.getOrCreateFirstPredicateBuilder(theIncludeResourceTypeAndNonDeletedFlag);
	}

	public void addJoin(DbTable theFromTable, DbTable theToTable, DbColumn theFromColumn, DbColumn theToColumn) {
		mySearchSqlBuilder.addJoin(theFromTable, theToTable, theFromColumn, theToColumn);
	}

}
