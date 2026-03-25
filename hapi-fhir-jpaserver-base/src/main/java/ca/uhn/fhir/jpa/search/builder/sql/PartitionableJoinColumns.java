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
package ca.uhn.fhir.jpa.search.builder.sql;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;

/**
 * A typed wrapper around the join columns used in SQL search predicates.
 * <p>
 * In database partition mode, join predicates require both a PARTITION_ID and a RES_ID column.
 * In non-partition mode, only the RES_ID column is used. This class makes that distinction
 * explicit, replacing raw {@code DbColumn[]} arrays where the column identity was encoded
 * by array index.
 * TODO - replace all related DbColumn[] references with this class.
 *   This was not done in this PR because it would require many signature changes
 *   and this PR needs to be backported.
 */
public class PartitionableJoinColumns {

	@Nullable
	private final DbColumn myPartitionIdColumn;

	private final DbColumn myResourceIdColumn;

	private PartitionableJoinColumns(@Nullable DbColumn thePartitionIdColumn, DbColumn theResourceIdColumn) {
		myPartitionIdColumn = thePartitionIdColumn;
		myResourceIdColumn = theResourceIdColumn;
	}

	/**
	 * Creates join columns for database partition mode, including both PARTITION_ID and RES_ID.
	 */
	public static PartitionableJoinColumns newPartitioned(DbColumn thePartitionIdColumn, DbColumn theResourceIdColumn) {
		return new PartitionableJoinColumns(thePartitionIdColumn, theResourceIdColumn);
	}

	/**
	 * Creates join columns for non-partition mode, with only a RES_ID column.
	 */
	public static PartitionableJoinColumns newNonPartitioned(DbColumn theResourceIdColumn) {
		return new PartitionableJoinColumns(null, theResourceIdColumn);
	}

	/**
	 * Converts a raw {@code DbColumn[]} array into typed {@code PartitionableJoinColumns}.
	 * <p>
	 * By convention, a two-element array is {@code [PARTITION_ID, RES_ID]} and
	 * a single-element array is {@code [RES_ID]}.
	 */
	public static PartitionableJoinColumns from(DbColumn[] theColumns) {
		Validate.isTrue(
				theColumns.length == 1 || theColumns.length == 2,
				"Expected 1 or 2 join columns, got %d",
				theColumns.length);
		if (theColumns.length == 2) {
			return newPartitioned(theColumns[0], theColumns[1]);
		}
		return newNonPartitioned(theColumns[0]);
	}

	public boolean isPartitioned() {
		return myPartitionIdColumn != null;
	}

	@Nullable
	public DbColumn getPartitionIdColumn() {
		return myPartitionIdColumn;
	}

	public DbColumn getResourceIdColumn() {
		return myResourceIdColumn;
	}
}
