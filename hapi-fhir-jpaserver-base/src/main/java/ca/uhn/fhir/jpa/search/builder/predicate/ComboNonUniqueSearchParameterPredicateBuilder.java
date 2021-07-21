package ca.uhn.fhir.jpa.search.builder.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

public class ComboNonUniqueSearchParameterPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private final DbColumn myColumnHashComplete;

	/**
	 * Constructor
	 */
	public ComboNonUniqueSearchParameterPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_IDX_CMB_TOK_NU"));

		myColumnHashComplete = getTable().addColumn("HASH_COMPLETE");
	}


	public Condition createPredicateHashComplete(RequestPartitionId theRequestPartitionId, String theParamName, String theIndexString) {
		long hash = ResourceIndexedComboTokenNonUnique.calculateHashComplete(getPartitionSettings(), getRequestPartitionId(), theIndexString);

		BinaryCondition predicate = BinaryCondition.equalTo(myColumnHashComplete, generatePlaceholder(hash));
		return combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}
}
