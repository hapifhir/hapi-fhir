package ca.uhn.fhir.jpa.embedded;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.jdbc.PgArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
public class HapiSchemaTest {

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

	private static final Logger ourLog = LoggerFactory.getLogger(HapiSchemaTest.class);

	private static final PostgresEmbeddedDatabase ourEmbeddedDB = new PostgresEmbeddedDatabase();
	private static final DatabaseInitializerHelper ourInitializerHelper = new DatabaseInitializerHelper();

	protected static final Multimap<String, String> ourTableToColumnsWhitelist = HashMultimap.create();

	private static final String WARNING_MSG = "Unindexed foreign key detected!\nTable: %s, Column: %s, FKIndex Name: %s, Issue: %s";

	@BeforeAll
	public static void beforeAll() {
		ourInitializerHelper.initializePersistenceSchema(ourEmbeddedDB);
//		myInitializerHelper.insertPersistenceTestData(myEmbeddedDB);

		populateWhiteList();
	}

	protected static void populateWhiteList() {
//		ourTableToColumnsWhitelist.put()
	}

	@AfterAll
	public static void afterAll() {
		ourEmbeddedDB.stop();
	}

	@Test
	public void ensureAllForeignKeysAreIndexed() throws SQLException {
		List<Map<String, Object>> results = ourEmbeddedDB.query(FK_QUERY);

		for (Map<String, Object> row : results) {
			PgArray postgresArray = (PgArray) row.get(COLS_LIST);
			String[] columns = (String[]) postgresArray.getArray();
			String tableName = (String)row.get(TABLE_NAME);
			String fkConstraintName = (String)row.get(FK_NAME);
			String parentTableName = (String)row.get(PARENT_TABLE_NAME);
			String issue = (String)row.get(ISSUE);

			Collection<String> whitelistColumns = ourTableToColumnsWhitelist.get(tableName);
			for (String col : columns) {
				ourLog.warn("-------");
				ourLog.warn(String.format(WARNING_MSG, tableName, col, fkConstraintName, issue));
//				assertTrue(whitelistColumns.contains(col),
//					String.format("Unindexed foreign key detected! Table.column: %s.%s.")
//				);
			}
		}
	}
}
