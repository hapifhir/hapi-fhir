package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.taskdef.containertests.BaseMigrationTaskTestSuite;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for AddIndexTask.
 */
public interface AddIndexTaskITTestSuite extends BaseMigrationTaskTestSuite {

	@Test
	default void testAddIndexOnline_createsIndex() throws SQLException {
		// given
		Builder builder = getSupport().getBuilder();
		String tableName = "TABLE_ADD" + System.currentTimeMillis();
		Builder.BuilderAddTableByColumns tableBuilder = builder.addTableByColumns("1", tableName, "id");
		tableBuilder.addColumn("id").nonNullable().type(ColumnTypeEnum.LONG);
		tableBuilder.addColumn("col1").nullable().type(ColumnTypeEnum.STRING, 100);
		getSupport().executeAndClearPendingTasks();

		// when
		builder.onTable(tableName)
			.addIndex("2", "FOO")
			.unique(false)
			.online(true)
			.withColumns("col1");
		getSupport().executeAndClearPendingTasks();

		// then

		// we wait since the ONLINE path is async.
		Awaitility.await("index FOO exists").atMost(10, TimeUnit.SECONDS).untilAsserted(
			() -> assertThat(JdbcUtils.getIndexNames(getSupport().getConnectionProperties(), tableName)).contains("FOO"));
	}


	@Test
	default void testDropAndAddPrimaryKeyTest() throws SQLException {
		// Setup
		Builder builder = getSupport().getBuilder();
		String tableName = "table_drop_add_pk_" + System.currentTimeMillis();
		Builder.BuilderAddTableByColumns tableBuilder = builder.addTableByColumns("1", tableName, "col_a", "col_b");
		tableBuilder.addColumn("col_a").nonNullable().type(ColumnTypeEnum.INT);
		tableBuilder.addColumn("col_b").nonNullable().type(ColumnTypeEnum.INT);
		tableBuilder.addColumn("col_c").nonNullable().type(ColumnTypeEnum.INT);
		getSupport().executeAndClearPendingTasks();
		assertThat(toUpper(JdbcUtils.getPrimaryKeyColumns(getSupport().getConnectionProperties(), tableName)))
			.contains("COL_A", "COL_B");

		// Test - Drop PK
		builder.onTable(tableName)
			.dropPrimaryKey("2");
		getSupport().executeAndClearPendingTasks();

		// Verify - PK should reflect the new one
		assertThat(toUpper(JdbcUtils.getPrimaryKeyColumns(getSupport().getConnectionProperties(), tableName)))
			.isEmpty();

		// Test - Add New PK
		builder.onTable(tableName)
			.addPrimaryKey("3", "col_b", "col_c");
		getSupport().executeAndClearPendingTasks();

		// Verify - PK should reflect the new one
		assertThat(toUpper(JdbcUtils.getPrimaryKeyColumns(getSupport().getConnectionProperties(), tableName)))
			.contains("COL_B", "COL_C");
	}

	default Set<String> toUpper(Set<String> theInput) {
		return theInput
			.stream()
			.map(t->t.toUpperCase(Locale.ROOT))
			.collect(Collectors.toSet());
	}


}
