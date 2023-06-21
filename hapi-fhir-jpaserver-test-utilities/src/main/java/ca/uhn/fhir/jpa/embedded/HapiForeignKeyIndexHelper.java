/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.embedded;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.intellij.lang.annotations.Language;
import org.postgresql.jdbc.PgArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This test class is used to verify all foreign key constraints
 * are indexed as well.
 * --
 * We use postgres to do this, because H2 automatically adds
 * indexes to fk contraints. But Oracle, Postgres, etc do not.
 * And Oracle will have deadlocks if there are unindexed FKs
 * --
 * It should be noted that because of this restriction,
 * Indexes on columns that are foreign keys must have the
 * exact same name as their FK constraint name.
 * ---
 * If while running this test, you get the error
 * "Could not find a valid docker environment"
 * and you have docker running, the issue
 * is permissions.
 * See <a href="https://stackoverflow.com/questions/61108655/test-container-test-cases-are-failing-due-to-could-not-find-a-valid-docker-envi">...</a>
 */
public class HapiForeignKeyIndexHelper {

	@Language("SQL")
	private static final String FK_QUERY = """
		   WITH fk_actions ( code, action ) AS (
		       VALUES ( 'a', 'error' ),
		           ( 'r', 'restrict' ),
		           ( 'c', 'cascade' ),
		           ( 'n', 'set null' ),
		           ( 'd', 'set default' )
		   ),
		   fk_list AS (
		       SELECT pg_constraint.oid as fkoid, conrelid, confrelid as parentid,
		           conname, relname, nspname,
		           fk_actions_update.action as update_action,
		           fk_actions_delete.action as delete_action,
		           conkey as key_cols
		       FROM pg_constraint
		           JOIN pg_class ON conrelid = pg_class.oid
		           JOIN pg_namespace ON pg_class.relnamespace = pg_namespace.oid
		           JOIN fk_actions AS fk_actions_update ON confupdtype = fk_actions_update.code
		           JOIN fk_actions AS fk_actions_delete ON confdeltype = fk_actions_delete.code
		       WHERE contype = 'f'
		       -- unique keys are indexed by default; so exclude any UNIQUE column constraints
		       AND not (conkey = any(select conkey from pg_constraint where contype = 'u'))
		   ),
		   fk_attributes AS (
		       SELECT fkoid, conrelid, attname, attnum
		       FROM fk_list
		           JOIN pg_attribute
		               ON conrelid = attrelid
		               AND attnum = ANY( key_cols )
		       ORDER BY fkoid, attnum
		   ),
		   fk_cols_list AS (
		       SELECT fkoid, array_agg(attname) as cols_list
		       FROM fk_attributes
		       GROUP BY fkoid
		   ),
		   index_list AS (
		       SELECT indexrelid as indexid,
		           pg_class.relname as indexname,
		           indrelid,
		           indkey,
		           indpred is not null as has_predicate,
		           pg_get_indexdef(indexrelid) as indexdef
		       FROM pg_index
		           JOIN pg_class ON indexrelid = pg_class.oid
		       WHERE indisvalid
		   ),
		   fk_index_match AS (
		       SELECT fk_list.*,
		           indexid,
		           indexname,
		           indkey::int[] as indexatts,
		           has_predicate,
		           indexdef,
		           array_length(key_cols, 1) as fk_colcount,
		           array_length(indkey,1) as index_colcount,
		           round(pg_relation_size(conrelid)/(1024^2)::numeric) as table_mb,
		           cols_list
		       FROM fk_list
		           JOIN fk_cols_list USING (fkoid)
		           LEFT OUTER JOIN index_list
		               ON conrelid = indrelid
		               AND (indkey::int2[])[0:(array_length(key_cols,1) -1)] @> key_cols
		   
		   ),
		   fk_perfect_match AS (
		       SELECT fkoid
		       FROM fk_index_match
		       WHERE (index_colcount - 1) <= fk_colcount
		           AND NOT has_predicate
		           AND indexdef LIKE '%USING btree%'
		   ),
		   fk_index_check AS (
		       SELECT 'no index' as issue, *, 1 as issue_sort
		       FROM fk_index_match
		       WHERE indexid IS NULL
		       UNION ALL
		       SELECT 'questionable index' as issue, *, 2
		       FROM fk_index_match
		       WHERE indexid IS NOT NULL
		           AND fkoid NOT IN (
		               SELECT fkoid
		               FROM fk_perfect_match)
		   ),
		   parent_table_stats AS (
		       SELECT fkoid, tabstats.relname as parent_name,
		           (n_tup_ins + n_tup_upd + n_tup_del + n_tup_hot_upd) as parent_writes,
		           round(pg_relation_size(parentid)/(1024^2)::numeric) as parent_mb
		       FROM pg_stat_user_tables AS tabstats
		           JOIN fk_list
		               ON relid = parentid
		   ),
		   fk_table_stats AS (
		       SELECT fkoid,
		           (n_tup_ins + n_tup_upd + n_tup_del + n_tup_hot_upd) as writes,
		           seq_scan as table_scans
		       FROM pg_stat_user_tables AS tabstats
		           JOIN fk_list
		               ON relid = conrelid
		   )
		   SELECT nspname as schema_name,
		       relname as table_name,
		       conname as fk_name,
		       issue,
		       table_mb,
		       writes,
		       table_scans,
		       parent_name,
		       parent_mb,
		       parent_writes,
		       cols_list,
		       indexdef
		   FROM fk_index_check
		       JOIN parent_table_stats USING (fkoid)
		       JOIN fk_table_stats USING (fkoid)
		   WHERE issue = 'no index'
		   ORDER BY issue_sort, table_mb DESC, table_name, fk_name;
		""";

	// columns
	private static final String TABLE_NAME = "table_name";
	private static final String FK_NAME = "fk_name";
	private static final String PARENT_TABLE_NAME = "parent_name";
	private static final String COLS_LIST = "cols_list";
	private static final String ISSUE = "issue";

	private static final Logger ourLog = LoggerFactory.getLogger(HapiForeignKeyIndexHelper.class);

	protected static final Multimap<String, String> ourTableToColumnsWhitelist = HashMultimap.create();

	private static final String MESSAGE = "\nUnindexed foreign key detected!\nTable: %s, Column: %s, FKIndex Name: %s, Parent Table: %s, Issue: %s";

	public HapiForeignKeyIndexHelper() {
		populateWhiteList();
	}

	/**
	 * This method populates a whitelist of table name -> column name
	 * for foreign key constraints that do not have proper indexes.
	 * --
	 * Any whitelisted table:column pairing should be documented why it
	 * doesn't require indexing, or be provided an explicit index.
	 */
	protected void populateWhiteList() {
		// HFJ_BLK_EXPORT_COLFILE - deprecated table
		ourTableToColumnsWhitelist.put("HFJ_BLK_EXPORT_COLFILE", "COLLECTION_PID");

		// HFJ_BLK_EXPORT_COLLECTION - deprecated table
		ourTableToColumnsWhitelist.put("HFJ_BLK_EXPORT_COLLECTION", "JOB_PID");

		// TODO - LS - entries below here require further investigation
		// MPI_LINK_AID - autogenerated table
		ourTableToColumnsWhitelist.put("MPI_LINK_AUD", "REV");
	}

	public void ensureAllForeignKeysAreIndexed(DataSource theDataSource) throws SQLException {
		try (Connection connection = theDataSource.getConnection()) {
			try (Statement statement = connection.createStatement()) {
				ResultSet results = statement.executeQuery(FK_QUERY);

				while (results.next()) {
					PgArray postgresArray = (PgArray) results.getArray(COLS_LIST);
					String[] columns = (String[]) postgresArray.getArray();
					String tableName = results.getString(TABLE_NAME);
					String fkConstraintName = results.getString(FK_NAME);
					String parentTableName = results.getString(PARENT_TABLE_NAME);
					String issue = results.getString(ISSUE);

					Collection<String> whitelistColumns = ourTableToColumnsWhitelist.get(tableName.toUpperCase());
					for (String col : columns) {
						boolean isWhitelisted = whitelistColumns.contains(col.toUpperCase());
						if (!isWhitelisted) {
							ourLog.error(String.format(MESSAGE,
								tableName,
								col,
								fkConstraintName,
								parentTableName,
								issue));
						}
						assertTrue(isWhitelisted,
							String.format("Unindexed foreign key detected! Table.column: %s.%s.", tableName, col)
						);
					}
				}
			}
		}
	}

}
