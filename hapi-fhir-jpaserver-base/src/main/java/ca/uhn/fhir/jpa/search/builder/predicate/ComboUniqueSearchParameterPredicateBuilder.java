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
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.List;

public class ComboUniqueSearchParameterPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private final DbColumn myColumnString;

	/**
	 * Constructor
	 */
	public ComboUniqueSearchParameterPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_IDX_CMP_STRING_UNIQ"));

		myColumnString = getTable().addColumn("IDX_STRING");
	}

	public Condition createPredicateIndexString(
			RequestPartitionId theRequestPartitionId, List<String> theIndexStrings) {
		Condition predicate =
				QueryParameterUtils.toEqualToOrInPredicate(myColumnString, generatePlaceholders(theIndexStrings));
		return combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}
}
