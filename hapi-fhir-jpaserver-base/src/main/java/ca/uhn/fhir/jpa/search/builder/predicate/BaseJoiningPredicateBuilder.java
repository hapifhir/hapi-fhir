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
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseJoiningPredicateBuilder extends BasePredicateBuilder {

	private final DbTable myTable;
	private final DbColumn myColumnPartitionId;

	BaseJoiningPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder, DbTable theTable) {
		super(theSearchSqlBuilder);
		myTable = theTable;
		myColumnPartitionId = theTable.addColumn("PARTITION_ID");
	}

	public DbTable getTable() {
		return myTable;
	}

	public abstract DbColumn getResourceIdColumn();

	DbColumn getPartitionIdColumn() {
		return myColumnPartitionId;
	}

	public Condition combineWithRequestPartitionIdPredicate(
			RequestPartitionId theRequestPartitionId, Condition theCondition) {
		Condition partitionIdPredicate = createPartitionIdPredicate(theRequestPartitionId);
		if (partitionIdPredicate == null) {
			return theCondition;
		}
		return QueryParameterUtils.toAndPredicate(partitionIdPredicate, theCondition);
	}

	@Nullable
	public Condition createPartitionIdPredicate(RequestPartitionId theRequestPartitionId) {

		if (theRequestPartitionId != null && !theRequestPartitionId.isAllPartitions()) {
			Condition condition;

			boolean defaultPartitionIsNull = getPartitionSettings().getDefaultPartitionId() == null;
			if (theRequestPartitionId.isDefaultPartition() && defaultPartitionIsNull) {
				condition = UnaryCondition.isNull(getPartitionIdColumn());
			} else if (theRequestPartitionId.hasDefaultPartitionId() && defaultPartitionIsNull) {
				List<String> placeholders = generatePlaceholders(theRequestPartitionId.getPartitionIdsWithoutDefault());
				UnaryCondition partitionNullPredicate = UnaryCondition.isNull(getPartitionIdColumn());
				Condition partitionIdsPredicate =
						QueryParameterUtils.toEqualToOrInPredicate(getPartitionIdColumn(), placeholders);
				condition = QueryParameterUtils.toOrPredicate(partitionNullPredicate, partitionIdsPredicate);
			} else {
				List<Integer> partitionIds = theRequestPartitionId.getPartitionIds();
				partitionIds = replaceDefaultPartitionIdIfNonNull(getPartitionSettings(), partitionIds);

				List<String> placeholders = generatePlaceholders(partitionIds);
				condition = QueryParameterUtils.toEqualToOrInPredicate(getPartitionIdColumn(), placeholders);
			}
			return condition;
		} else {
			return null;
		}
	}

	public Condition createPredicateResourceIds(boolean theInverse, List<Long> theResourceIds) {
		Validate.notNull(theResourceIds, "theResourceIds must not be null");

		// Handle the _id parameter by adding it to the tail
		Condition inResourceIds =
				QueryParameterUtils.toEqualToOrInPredicate(getResourceIdColumn(), generatePlaceholders(theResourceIds));
		if (theInverse) {
			inResourceIds = new NotCondition(inResourceIds);
		}
		return inResourceIds;
	}

	public static List<Integer> replaceDefaultPartitionIdIfNonNull(
			PartitionSettings thePartitionSettings, List<Integer> thePartitionIds) {
		List<Integer> partitionIds = thePartitionIds;
		if (thePartitionSettings.getDefaultPartitionId() != null) {
			partitionIds = partitionIds.stream()
					.map(t -> t == null ? thePartitionSettings.getDefaultPartitionId() : t)
					.collect(Collectors.toList());
		}
		return partitionIds;
	}
}
