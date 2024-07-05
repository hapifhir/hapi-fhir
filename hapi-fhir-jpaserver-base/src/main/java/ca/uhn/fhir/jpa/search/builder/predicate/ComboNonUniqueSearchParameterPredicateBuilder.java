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
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.List;
import java.util.stream.Collectors;

public class ComboNonUniqueSearchParameterPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private final DbColumn myColumnHashComplete;

	/**
	 * Constructor
	 */
	public ComboNonUniqueSearchParameterPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_IDX_CMB_TOK_NU"));

		myColumnHashComplete = getTable().addColumn("HASH_COMPLETE");
	}

	public Condition createPredicateHashComplete(
			RequestPartitionId theRequestPartitionId, List<String> theIndexStrings) {
		PartitionablePartitionId partitionId =
				PartitionablePartitionId.toStoragePartition(theRequestPartitionId, getPartitionSettings());
		List<Long> hashes = theIndexStrings.stream()
				.map(t -> ResourceIndexedComboTokenNonUnique.calculateHashComplete(
						getPartitionSettings(), partitionId, t))
				.collect(Collectors.toList());
		Condition predicate =
				QueryParameterUtils.toEqualToOrInPredicate(myColumnHashComplete, generatePlaceholders(hashes));
		return combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}
}
