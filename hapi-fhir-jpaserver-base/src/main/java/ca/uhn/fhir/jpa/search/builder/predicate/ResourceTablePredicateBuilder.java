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

import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.Set;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.toAndPredicate;
import static ca.uhn.fhir.jpa.search.builder.QueryStack.toEqualToOrInPredicate;

public class ResourceTablePredicateBuilder extends BaseJoiningPredicateBuilder {
	private final DbColumn myColumnResId;
	private final DbColumn myColumnResDeletedAt;
	private final DbColumn myColumnResType;
	private final DbColumn myColumnLastUpdated;
	private final DbColumn myColumnLanguage;

	/**
	 * Constructor
	 */
	public ResourceTablePredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RESOURCE"));
		myColumnResId = getTable().addColumn("RES_ID");
		myColumnResType = getTable().addColumn("RES_TYPE");
		myColumnResDeletedAt = getTable().addColumn("RES_DELETED_AT");
		myColumnLastUpdated = getTable().addColumn("RES_UPDATED");
		myColumnLanguage = getTable().addColumn("RES_LANGUAGE");
	}


	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public Condition createResourceTypeAndNonDeletedPredicates() {
		BinaryCondition typePredicate = null;
		if (getResourceType() != null) {
			typePredicate = BinaryCondition.equalTo(myColumnResType, generatePlaceholder(getResourceType()));
		}
		return toAndPredicate(
			typePredicate,
			UnaryCondition.isNull(myColumnResDeletedAt)
		);
	}

	public DbColumn getLastUpdatedColumn() {
		return myColumnLastUpdated;
	}

	public Condition createLanguagePredicate(Set<String> theValues, boolean theNegated) {
		Condition condition = toEqualToOrInPredicate(myColumnLanguage, generatePlaceholders(theValues));
		if (theNegated) {
			condition = new NotCondition(condition);
		}
		return condition;
	}

	public DbColumn getColumnLastUpdated() {
		return myColumnLastUpdated;
	}
}
